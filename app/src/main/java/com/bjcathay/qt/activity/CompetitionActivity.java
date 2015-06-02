package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.CompetitionAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.EventListModel;
import com.bjcathay.qt.model.EventModel;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.AutoListView;
import com.bjcathay.qt.view.TopView;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛事页面
 * Created by dengt on 15-4-20.
 */
public class CompetitionActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback, View.OnClickListener {
    private GApplication gApplication;
    private AutoListView listView;
    private CompetitionAdapter competitionAdapter;
    private List<EventModel> eventModels;
    private TopView topView;
    private int page = 1;
    private View empty;
    private ImageView emptyImg;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        gApplication = GApplication.getInstance();
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        listView = ViewUtil.findViewById(this, R.id.competition_list);
        topView = ViewUtil.findViewById(this, R.id.top_competition_layout);
        topView.setTitleText("赛事");
        topView.setHomeBackVisiable();

    }


    private void initEvent() {
        listView.setAdapter(competitionAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        LayoutInflater inflater = getLayoutInflater();
        empty = inflater.inflate(R.layout.listview_empty, null);
        FrameLayout.LayoutParams lpLl = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lpLl.gravity = Gravity.CENTER;
        empty.setLayoutParams(lpLl);
        emptyImg = (ImageView) empty.findViewById(R.id.list_image_empty);
        emptyText = (TextView) empty.findViewById(R.id.list_view_empty);
        emptyImg.setImageResource(R.drawable.ic_empty_comp);
        emptyText.setText(getString(R.string.empty_free_compet_text));
        ((ViewGroup)listView.getParent()).addView(empty);
        listView.setEmptyView(empty);
        eventModels = new ArrayList<EventModel>();
        competitionAdapter = new CompetitionAdapter(eventModels, this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= eventModels.size()) {
                    Intent intent = new Intent(CompetitionActivity.this, CompetitionDetailActivity.class);
                    intent.putExtra("url", eventModels.get(i - 1).getUrl());
                    intent.putExtra("id", eventModels.get(i - 1).getId());
                    ViewUtil.startActivity(CompetitionActivity.this, intent);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            EventListModel result = (EventListModel) msg.obj;
            boolean hasNext=result.isHasNext();
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
                listView.setResultSize(eventModels.size(),hasNext);
                competitionAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        listView.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        listView.onLoadComplete();
                        break;
                }
                listView.setResultSize(eventModels.size(),hasNext);
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
        switch (what) {
            case AutoListView.REFRESH:
                page = 1;
                break;
            case AutoListView.LOAD:
                page++;
                break;
        }
        EventListModel.get(page).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                if (listView != null)
                    listView.onRefreshComplete();
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
            case R.id.home_back_img:
                finish();
                break;
        }
    }
}
