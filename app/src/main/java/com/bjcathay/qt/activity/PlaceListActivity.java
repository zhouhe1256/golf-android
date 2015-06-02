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
import com.bjcathay.qt.adapter.PlaceListAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.widget.DSActivity;
import com.bjcathay.qt.model.ProductListModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.TimeCount;
import com.bjcathay.qt.util.ViewUtil;

import com.bjcathay.qt.view.TopView;
import com.bjcathay.qt.view.AutoListView;
import com.bjcathay.qt.view.AutoListView.OnLoadListener;
import com.bjcathay.qt.view.AutoListView.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 场地页面
 * Created by dengt on 15-4-20.
 */
public class PlaceListActivity extends Activity implements OnRefreshListener,
        OnLoadListener, ICallback, View.OnClickListener {
    private TopView topView;
    // private ListView listView;
    private GApplication gApplication;
    private PlaceListAdapter placeListAdapter;
    private List<ProductModel> stadiumModelList;
    private AutoListView lstv;
    private int page = 1;
    private TimeCount timeCount;

    private Date now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placelist);
        gApplication = GApplication.getInstance();
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        // listView = ViewUtil.findViewById(this, R.id.place_list);
        topView = ViewUtil.findViewById(this, R.id.top_placelist_layout);
        topView.setHomeBackVisiable();
        topView.setTitleText("约场");
        stadiumModelList = new ArrayList<ProductModel>();
        placeListAdapter = new PlaceListAdapter(stadiumModelList, this);

        lstv = (AutoListView) findViewById(R.id.place_lstv);
        lstv.setAdapter(placeListAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
    }

    private void initEvent() {
        //   listView.setAdapter(placeListAdapter);
        lstv.setListViewEmptyImage(R.drawable.ic_network_error);
        lstv.setListViewEmptyMessage(getString(R.string.empty_net_text));
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= stadiumModelList.size()) {
                    //todo
                   // Intent intent = new Intent(PlaceListActivity.this, CourseDetailActivity.class);
                    Intent intent = new Intent(PlaceListActivity.this, DSActivity.class);
                    intent.putExtra("imageurl", stadiumModelList.get(i - 1).getImageUrl());
                    intent.putExtra("id", stadiumModelList.get(i - 1).getId());
                    intent.putExtra("type", stadiumModelList.get(i - 1).getType());
                    ViewUtil.startActivity(PlaceListActivity.this, intent);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ProductListModel result = (ProductListModel) msg.obj;
            if (result != null && result.getProducts() != null && !result.getProducts().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        stadiumModelList.clear();
                        stadiumModelList.addAll(result.getProducts());
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        stadiumModelList.addAll(result.getProducts());
                        break;
                }
                lstv.setResultSize(result.getProducts().size());
                placeListAdapter.notifyDataSetChanged();
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
                placeListAdapter.notifyDataSetChanged();
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
        ProductListModel.productList(page).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                if (lstv != null)
                    lstv.onRefreshComplete();
            }
        });
    }

    @Override
    public void call(Arguments arguments) {
        ProductListModel stadiumListModel = arguments.get(0);
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = AutoListView.REFRESH;
        else {
            msg.what = AutoListView.LOAD;
        }
        msg.obj = stadiumListModel;
        handler.sendMessage(msg);

        now = DateUtil.stringToDate(stadiumListModel.getNow());
//        now =new Date();
        if (timeCount == null) {
            timeCount = new TimeCount(Long.MAX_VALUE, 1000, new TimeCount.TimeUpdate() {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (now != null) {
                        now = new Date(now.getTime() + 1000);
                        setNow(now);
                        placeListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFinish() {

                }
            });
            timeCount.start();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_back_img:
                finish();
                break;
        }
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
