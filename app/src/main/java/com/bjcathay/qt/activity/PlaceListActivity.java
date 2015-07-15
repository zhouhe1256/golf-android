
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
import com.bjcathay.qt.util.LocationUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.model.ProductListModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.TimeCount;
import com.bjcathay.qt.util.ViewUtil;

import com.bjcathay.qt.view.TopView;
import com.bjcathay.qt.view.AutoListView;
import com.bjcathay.qt.view.AutoListView.OnLoadListener;
import com.bjcathay.qt.view.AutoListView.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 场地页面 Created by dengt on 15-4-20.
 */
public class PlaceListActivity extends Activity implements OnRefreshListener,
        OnLoadListener, ICallback, View.OnClickListener {
    private TopView topView;
    private GApplication gApplication;
    private PlaceListAdapter placeListAdapter;
    private List<ProductModel> stadiumModelList;
    private AutoListView lstv;
    private int page = 1;
    private TimeCount timeCount;

    private Date now;
    private String latitude;
    private String longitude;
    private String cityID;

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
        topView = ViewUtil.findViewById(this, R.id.top_placelist_layout);
        topView.setHomeBackVisiable();
        topView.setSearchVisiable();
        topView.setTitleText("推荐");
        stadiumModelList = new ArrayList<ProductModel>();
        placeListAdapter = new PlaceListAdapter(stadiumModelList, this);

        lstv = (AutoListView) findViewById(R.id.place_lstv);
        lstv.setAdapter(placeListAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
    }

    private void initEvent() {

        lstv.setListViewEmptyImage(R.drawable.ic_network_error);
        lstv.setListViewEmptyMessage(getString(R.string.empty_net_text));
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= stadiumModelList.size()) {
                    // todo
                    Intent intent = new Intent(PlaceListActivity.this,
                            OrderStadiumDetailActivity.class);
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
            boolean hasNext = result.isHasNext();
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
                lstv.setResultSize(stadiumModelList.size(), hasNext);
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
                lstv.setResultSize(stadiumModelList.size(), hasNext);
                placeListAdapter.notifyDataSetChanged();
            }
        }

        ;
    };

    private void initData() {
        latitude = PreferencesUtils.getString(this, PreferencesConstant.LATITUDE);
        longitude = PreferencesUtils.getString(this, PreferencesConstant.LONGITUDE);
        cityID = PreferencesUtils.getString(this, PreferencesConstant.CITY_ID);
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

        ProductListModel.productList(page, latitude, longitude, cityID).done(this)
                .fail(new ICallback() {
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
        if (timeCount == null) {
            timeCount = new TimeCount(Long.MAX_VALUE, 60000, new TimeCount.TimeUpdate() {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (now != null) {
                        now = new Date(now.getTime() + 60000);
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
        Intent intent;
        switch (view.getId()) {
            case R.id.home_back_img:
                intent = new Intent(this, MainActivity.class);
                ViewUtil.startTopActivity(this, intent);
                finish();
                break;
            case R.id.title_search_img:
                intent = new Intent(this, SearchActivity.class);
                ViewUtil.startActivity(this, intent);
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
