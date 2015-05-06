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
import com.bjcathay.golf.adapter.CompetitionAdapter;
import com.bjcathay.golf.adapter.PlaceListAdapter;
import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.model.EventListModel;
import com.bjcathay.golf.model.EventModel;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.AutoListView;
import com.bjcathay.golf.view.TopView;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛事页面
 * Created by dengt on 15-4-20.
 */
public class CompetitionActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback {
    private GApplication gApplication;
    private AutoListView listView;
    private CompetitionAdapter competitionAdapter;
    private List<EventModel> eventModels;
    private TopView topView;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        gApplication=GApplication.getInstance();
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        listView = ViewUtil.findViewById(this, R.id.competition_list);
        topView = ViewUtil.findViewById(this, R.id.top_competition_layout);
        topView.setActivity(this);
        topView.setTitleText("赛事");
        eventModels = new ArrayList<EventModel>();
        competitionAdapter = new CompetitionAdapter(eventModels, this);

    }


    private void initEvent() {
        listView.setAdapter(competitionAdapter);

        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(CompetitionActivity.this, CompetitionDetailActivity.class);
                ViewUtil.startActivity(CompetitionActivity.this, intent);
            }
        });
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            EventListModel result = (EventListModel) msg.obj;
            if (result != null && result.getEvents() != null && !result.getEvents().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        eventModels.clear();
                        eventModels.addAll(result.getEvents());
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        eventModels.addAll(result.getEvents());
                        break;
                }
                listView.setResultSize(result.getEvents().size());
                competitionAdapter.notifyDataSetChanged();
            }else{
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        break;
                }
                listView.setResultSize(0);
                competitionAdapter.notifyDataSetChanged();
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
        switch (what){
            case AutoListView.REFRESH:
                page=1;
                break;
            case AutoListView.LOAD:
                page++;
                break;
        }
        EventListModel.get(page).done(this);
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
