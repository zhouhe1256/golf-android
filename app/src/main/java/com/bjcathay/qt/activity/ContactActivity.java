package com.bjcathay.qt.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.SortAdapter;
import com.bjcathay.qt.fragment.DialogExchFragment;
import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.model.BooksModel;
import com.bjcathay.qt.model.SortListModel;
import com.bjcathay.qt.model.SortModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.CharacterParser;
import com.bjcathay.qt.util.ConstactUtil;
import com.bjcathay.qt.util.PinyinComparator;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.ClearEditText;
import com.bjcathay.qt.view.SideBar;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dengt on 15-4-29.
 */
public class ContactActivity extends FragmentActivity implements View.OnClickListener, DialogExchFragment.ExchangeResult {
    private DialogExchFragment dialogExchFragment;
    private View mBaseView;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    //private Map<String, String> callRecords;
    List<BookModel> callRecords;
    private CharacterParser characterParser;
    //private List<SortModel> SourceDateList;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    private TopView topView;
    private TextView netNote;
    private Long id;
    private Drawable imgLeft;
    private LinearLayout centerImg;
    private String proName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_contact_layout);
        netNote = ViewUtil.findViewById(this, R.id.network_note);
        sideBar = (SideBar) this.findViewById(R.id.sidrbar);
        dialog = (TextView) this.findViewById(R.id.dialog);
        sortListView = (ListView) this.findViewById(R.id.sortlist);
        netNote.setVisibility(View.VISIBLE);


    }

    private void initEvent() {
        //   mClearEditText.setOnFocusChangeListener(mOnFocusChangeListener);
    }


    private void initData() {
        topView.setTitleBackVisiable();
        topView.setTitleText("选择好友");
        id = getIntent().getLongExtra("id", 0);
        proName = getIntent().getStringExtra("name");
        dialogExchFragment = new DialogExchFragment(this, this);
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                if (adapter.getCount() > 0) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        sortListView.setSelection(position);
                    }
                }
            }
        });
        new ConstactAsyncTask().execute(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    @Override
    public void exchangeResult(UserModel userModel, boolean isExchange) {

    }

    private class ConstactAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... arg0) {
            int result = -1;
            callRecords = ConstactUtil.getQuickRecords(ContactActivity.this);
            result = 1;
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 1) {
                final BooksModel booksModel = new BooksModel();
                booksModel.setBooks(callRecords);
                //ViewUtil.encode(JSONUtil.dump(booksModel).getBytes())
                SortListModel.searchContactListUser(ViewUtil.encode(JSONUtil.dump(booksModel).getBytes())).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        SortListModel userListModle = arguments.get(0);
                        List<SortModel> sortModels = userListModle.getUsers();
                        SourceDateList = filledData(sortModels);
                        // 根据a-z进行排序源数据
                        Collections.sort(SourceDateList, pinyinComparator);
                        adapter = new SortAdapter(ContactActivity.this, SourceDateList, id, proName, dialogExchFragment);
                        sortListView.setAdapter(adapter);
                        sortListView.setVisibility(View.VISIBLE);
                        netNote.setVisibility(View.GONE);

                    }
                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        List<SortModel> sortModels = new ArrayList<SortModel>();
                        for (BookModel b : callRecords) {
                            SortModel sortModel = new SortModel();
                            sortModel.setPhone(b.getPhone());
                            sortModel.setName(b.getName());
                            sortModels.add(sortModel);
                        }
                        SourceDateList = filledData(sortModels);
                        // 根据a-z进行排序源数据
                        Collections.sort(SourceDateList, pinyinComparator);
                        adapter = new SortAdapter(ContactActivity.this, SourceDateList, id, proName, dialogExchFragment);
                        sortListView.setAdapter(adapter);
                        sortListView.setVisibility(View.VISIBLE);
                        netNote.setVisibility(View.GONE);
                    }
                });
                mClearEditText = (ClearEditText) ContactActivity.this
                        .findViewById(R.id.filter_edit);
                centerImg = ViewUtil.findViewById(ContactActivity.this, R.id.layout_default);
                mClearEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View arg0, boolean hasFocus) {
                        mClearEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        String hint;
                        if (!hasFocus) {
                            if (mClearEditText != null && mClearEditText.getHint() != null) {
                                hint = mClearEditText.getHint().toString();
                                mClearEditText.setTag(hint);
                                mClearEditText.setHint("");
                                centerImg.setVisibility(View.VISIBLE);
                            }
                        } else {
                            centerImg.setVisibility(View.GONE);
                            hint = mClearEditText.getTag().toString();
                            mClearEditText.setHint(hint);
                            if (imgLeft == null) {
                                imgLeft = getResources().getDrawable(R.drawable.ic_search);
                                imgLeft.setBounds(0, 0, imgLeft.getMinimumWidth(), imgLeft.getMinimumHeight());
                            }
                            mClearEditText.setCompoundDrawables(imgLeft, null, null, null);
                        }
                    }
                });
                // 根据输入框输入值的改变来过滤搜索
                mClearEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                        filterData(s.toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    /**
     * 为ListView填充数据
     *
     * @param sortModels
     * @return
     */
    private List<SortModel> filledData(List<SortModel> sortModels) {
        for (int i = 0; i < sortModels.size(); i++) {
            String pinyin = characterParser.getSelling(sortModels.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModels.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                sortModels.get(i).setSortLetters("#");
            }
        }
        return sortModels;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        if (SourceDateList != null) {
            List<SortModel> filterDateList = new ArrayList<SortModel>();
            if (filterStr.matches("[A-Z]"))
                filterStr = filterStr.toLowerCase();
            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = SourceDateList;
            } else {
                filterDateList.clear();
                for (SortModel sortModel : SourceDateList) {
                    String name = sortModel.getName();

                    if (name.indexOf(filterStr.toString()) != -1
                            || characterParser.getSelling(name).startsWith(
                            filterStr.toString())) {
                        filterDateList.add(sortModel);
                    }
                }
            }

            // 根据a-z进行排序
            Collections.sort(filterDateList, pinyinComparator);
            adapter.updateListView(filterDateList);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
