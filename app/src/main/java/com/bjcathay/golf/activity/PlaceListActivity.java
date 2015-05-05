package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.golf.R;
import com.bjcathay.golf.adapter.PlaceListAdapter;
import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.model.StadiumListModel;
import com.bjcathay.golf.model.StadiumModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;

import com.bjcathay.golf.view.TopView;
import com.bjcathay.golf.view.AutoListView;
import com.bjcathay.golf.view.AutoListView.OnLoadListener;
import com.bjcathay.golf.view.AutoListView.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 场地页面
 * Created by dengt on 15-4-20.
 */
public class PlaceListActivity extends Activity implements OnRefreshListener,
        OnLoadListener, ICallback {
    // private ListView listView;
    private GApplication gApplication;
    private PlaceListAdapter placeListAdapter;
    private List<StadiumModel> stadiumModelList;
    private TopView topView;
    private AutoListView lstv;
    // private List<String> list = new ArrayList<String>();
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placelist);
        gApplication=GApplication.getInstance();
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        // listView = ViewUtil.findViewById(this, R.id.place_list);
        topView = ViewUtil.findViewById(this, R.id.top_placelist_layout);
        topView.setActivity(this);
        topView.setTitleText("选场地");
        stadiumModelList = new ArrayList<StadiumModel>();
        placeListAdapter = new PlaceListAdapter(stadiumModelList, this);

        lstv = (AutoListView) findViewById(R.id.place_lstv);
        lstv.setAdapter(placeListAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
    }

    private void initEvent() {
        //   listView.setAdapter(placeListAdapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DialogUtil.hintMessage("选档期", PlaceListActivity.this);
                Intent intent = new Intent(PlaceListActivity.this, ScheduleActivity.class);
                ViewUtil.startActivity(PlaceListActivity.this, intent);
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            StadiumListModel result = (StadiumListModel) msg.obj;
            if (result != null && result.getStadiums() != null && !result.getStadiums().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        stadiumModelList.clear();
                        stadiumModelList.addAll(result.getStadiums());
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        stadiumModelList.addAll(result.getStadiums());
                        break;
                }
                lstv.setResultSize(result.getStadiums().size());
                placeListAdapter.notifyDataSetChanged();
            }else{
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        break;
                }
                lstv.setResultSize(0);
                placeListAdapter.notifyDataSetChanged();
            }
        }

        ;
    };

    private void initData() {
        loadData(AutoListView.REFRESH);
        page = 1;
    }

    @Override
    public void onRefresh() {
        loadData(AutoListView.REFRESH);
        page = 1;
    }

    @Override
    public void onLoad() {
        loadData(AutoListView.LOAD);
        page++;
    }

    private void loadData(final int what) {
        StadiumListModel.stadiums(page).done(this);
       /* // 这里模拟从服务器获取数据
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                // msg.what = what;
                msg.obj = getData();
                handler.sendMessage(msg);
            }
        }).start();*/
    }

    // 测试数据
    public List<String> getData() {
        List<String> result = new ArrayList<String>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            long l = random.nextInt(10000);
            result.add("当前条目的ID：" + l);
        }
        return result;
    }

    @Override
    public void call(Arguments arguments) {
        StadiumListModel stadiumListModel = arguments.get(0);
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = AutoListView.REFRESH;
        else {
            msg.what = AutoListView.LOAD;
        }
        msg.obj = stadiumListModel;
        handler.sendMessage(msg);
    }
}
