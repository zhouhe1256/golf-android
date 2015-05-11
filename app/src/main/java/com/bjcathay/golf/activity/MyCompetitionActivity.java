package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.golf.R;
import com.bjcathay.golf.adapter.MyCompetitionAdapter;
import com.bjcathay.golf.adapter.MyOrderAdapter;
import com.bjcathay.golf.model.EventListModel;
import com.bjcathay.golf.model.EventModel;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.AutoListView;
import com.bjcathay.golf.view.TopView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-4-29.
 */
public class MyCompetitionActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback {

    private MyCompetitionAdapter myCompetitionAdapter;
    private List<EventModel> eventModels;
    private TopView topView;
    private AutoListView lstv;
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_competition);
        initView();
        initData();
        initEvent();
    }

    private void initView() {

        topView = ViewUtil.findViewById(this, R.id.top_my_competition_layout);
        topView.setActivity(this);
        topView.setTitleText("我的赛事");
        eventModels = new ArrayList<EventModel>();
        myCompetitionAdapter = new MyCompetitionAdapter(eventModels, this);

        lstv = (AutoListView) findViewById(R.id.my_competition_list);
        lstv.setAdapter(myCompetitionAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);

    }

    private void initEvent() {

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // DialogUtil.hintMessage("选档期", PlaceListActivity.this);
               /* Intent intent = new Intent(MyCompetitionActivity.this, OrderDetailActivity.class);
                ViewUtil.startActivity(MyCompetitionActivity.this, intent);*/
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            EventListModel result = (EventListModel) msg.obj;
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
                lstv.setResultSize(result.getEvents().size());
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
                lstv.setResultSize(0);
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
        EventListModel.getMyEvent(page).done(this);
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
}