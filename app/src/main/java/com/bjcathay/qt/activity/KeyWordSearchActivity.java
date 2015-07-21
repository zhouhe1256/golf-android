
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.GolfCourseAdapter;
import com.bjcathay.qt.db.DBManager;
import com.bjcathay.qt.model.GolfCourseListModel;
import com.bjcathay.qt.model.GolfCourseModel;
import com.bjcathay.qt.util.CharacterParser;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-6-25.
 */
public class KeyWordSearchActivity extends Activity implements View.OnClickListener, TextWatcher,
        ICallback {
    private TopView topView;
    private EditText editText;
    private List<GolfCourseModel> list;
    private List<GolfCourseModel> historylist;
    private ListView listView;
    private Button clearBtn;
    private CharacterParser characterParser;
    private GolfCourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_keyword);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        historylist = DBManager.getInstance().queryHistory();
        if (historylist == null || historylist.isEmpty()) {
            clearBtn.setVisibility(View.GONE);
        } else {
            clearBtn.setVisibility(View.VISIBLE);
        }
        GolfCourseListModel.searchGolf().done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
            }
        });
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_search_keyword_layout);
        editText = ViewUtil.findViewById(this, R.id.search_key);
        listView = ViewUtil.findViewById(this, R.id.golflist);
        clearBtn = ViewUtil.findViewById(this, R.id.history_clear);
        topView.setTitleText("输入关键字");
        topView.setTitleBackVisiable();
    }

    private void initEvent() {
        list = new ArrayList<GolfCourseModel>();
        adapter = new GolfCourseAdapter(historylist, this);
        listView.setAdapter(adapter);

        editText.addTextChangedListener(this);
        clearBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.history_clear:
                historylist.clear();
                clearBtn.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                DBManager.getInstance().clearHistory();
                break;
            case R.id.cancle_search_key:
                finish();
                break;
        /*
         * case R.id.select_city: intent = new Intent(this,
         * CitySelectActivity.class); ViewUtil.startActivity(this, intent);
         * break; case R.id.input_place: intent = new Intent(this, SE.class);
         * ViewUtil.startActivity(this, intent); break; case R.id.select_sure:
         * break;
         */
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i2, int i3) {
        // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
        filterData(s.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        if (list != null) {
            List<GolfCourseModel> filterDateList = new ArrayList<GolfCourseModel>();
            if (filterStr.matches("[A-Z]"))
                filterStr = filterStr.toLowerCase();
            if (TextUtils.isEmpty(filterStr)) {
                // 修改成历史记录
                filterDateList = historylist;
                clearBtn.setVisibility(View.VISIBLE);
                setListViewHeight(listView);
            } else {
                clearBtn.setVisibility(View.GONE);
                filterDateList.clear();
                String filterStrword = characterParser.getSelling(filterStr);
                for (GolfCourseModel sortModel : list) {
                    String name = sortModel.getName();
                    String address = sortModel.getAddress();
                    if (name.indexOf(filterStr.toString()) != -1
                            || characterParser.getSelling(name).contains(
                                    filterStrword) || characterParser.getSelling(address).contains(
                                    filterStrword)) {
                        filterDateList.add(sortModel);
                    }
                }
            }
            adapter.updateListView(filterDateList);

        }
    }

    @Override
    public void call(Arguments arguments) {
        GolfCourseListModel golfCourseListModel = arguments.get(0);
        list.addAll(golfCourseListModel.getGolfCourses());
        adapter.notifyDataSetChanged();
    }

    public static void setListViewHeight(ListView listview) {
        int totalHeight = 0;
        ListAdapter adapter = listview.getAdapter();
        if (null != adapter) {
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listview);
                if (null != listItem) {
                    listItem.measure(0, 0);// 注意listview子项必须为LinearLayout才能调用该方法
                    totalHeight += listItem.getMeasuredHeight();
                }
            }

            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = totalHeight + (listview.getDividerHeight() * (listview.getCount() - 1));
            listview.setLayoutParams(params);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关键字搜索页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关键字搜索页面");
        MobclickAgent.onPause(this);
    }
}
