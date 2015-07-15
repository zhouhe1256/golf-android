
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.MyCompetitionAdapter;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.EventListModel;
import com.bjcathay.qt.model.EventModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.AutoListView;
import com.bjcathay.qt.view.DeleteInfoDialog.DeleteInfoDialogResult;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-4-29.
 */
public class MyCompetitionActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback, View.OnClickListener {
    private Activity context;
    private MyCompetitionAdapter myCompetitionAdapter;
    private List<EventModel> eventModels;
    private TopView topView;
    private AutoListView lstv;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_competition);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_my_competition_layout);
        topView.setTitleBackVisiable();
        topView.setTitleText("我的赛事");
        eventModels = new ArrayList<EventModel>();
        myCompetitionAdapter = new MyCompetitionAdapter(eventModels, this);

        lstv = (AutoListView) findViewById(R.id.my_competition_list);
        lstv.setAdapter(myCompetitionAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        lstv.setListViewEmptyImage(R.drawable.ic_empty_comp);
        lstv.setListViewEmptyMessage(getString(R.string.empty_com_text));
    }

    private void initEvent() {

        lstv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= eventModels.size()) {
                    Intent intent = new Intent(MyCompetitionActivity.this,
                            CompetitionDetailActivity.class);
                    intent.putExtra("url", eventModels.get(i - 1).getUrl());
                    intent.putExtra("id", eventModels.get(i - 1).getId());
                    ViewUtil.startActivity(MyCompetitionActivity.this, intent);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            EventListModel result = (EventListModel) msg.obj;
            boolean hasNext = result.isHasNext();
            if (result != null && result.getEvents() != null && !result.getEvents().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        eventModels.clear();
                        eventModels.addAll(result.getEvents());
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        eventModels.addAll(result.getEvents());
                        break;
                }
                lstv.setResultSize(eventModels.size(), hasNext);
                myCompetitionAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        break;
                }
                lstv.setResultSize(eventModels.size(), hasNext);
                myCompetitionAdapter.notifyDataSetChanged();
            }
        }

        ;
    };

    private void initData() {
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onRefresh() {
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        loadData(AutoListView.LOAD);
    }

    private void loadData(final int what) {
        switch (what) {
            case AutoListView.REFRESH:
                page = 1;
                break;
            case AutoListView.LOAD:
                page++;
                break;
        }
        EventListModel.getMyEvent(page).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                if (lstv != null) {
                    lstv.onRefreshComplete();
                    lstv.setResultSize(-1, false);
                }
            }
        });
    }

    @Override
    public void call(Arguments arguments) {
        EventListModel eventListModel = arguments.get(0);
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = AutoListView.REFRESH;
        else {
            msg.what = AutoListView.LOAD;
        }
        msg.obj = eventListModel;
        handler.sendMessage(msg);
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的赛事页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的赛事页面");
        MobclickAgent.onPause(this);
    }
}
