
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.PlaceListAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.StadiumListModel;
import com.bjcathay.qt.model.StadiumModel;
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
    private List<StadiumModel> stadiumModelList;
    private AutoListView lstv;
    private int page = 1;
    private Date now;
    private String latitude;
    private String longitude;
    private String cityID;
    private String provinceID;
    private TextView cityName;
    private TextView totalFrist;
    private TextView priceFrist;
    private TextView disatnceFrist;
    private int cityreqCode = 1;
    private int fristFlag = 1;
    ColorStateList csl;
    private String order;

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
        cityName = ViewUtil.findViewById(this, R.id.city_name);
        totalFrist = ViewUtil.findViewById(this, R.id.total_frist);
        priceFrist = ViewUtil.findViewById(this, R.id.price_frist);
        disatnceFrist = ViewUtil.findViewById(this, R.id.distance_frist);
        csl = getResources().getColorStateList(R.color.order_price_color);
        stadiumModelList = new ArrayList<StadiumModel>();
        placeListAdapter = new PlaceListAdapter(stadiumModelList, this);

        lstv = (AutoListView) findViewById(R.id.place_lstv);
        lstv.setAdapter(placeListAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
    }

    private void initEvent() {

        lstv.setListViewEmptyImage(R.drawable.yuechang);
        lstv.setListViewEmptyMessage("没有查到相关球场～");
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= stadiumModelList.size()) {
                    Intent intent = new Intent(PlaceListActivity.this,
                            ProductActivity.class);
                    intent.putExtra("id", stadiumModelList.get(i - 1).getId());
                    intent.putExtra("name", stadiumModelList.get(i - 1).getName());
                    ViewUtil.startActivity(PlaceListActivity.this, intent);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            StadiumListModel result = (StadiumListModel) msg.obj;
            boolean hasNext = result.isHasNext();
            if (result != null && result.getGolfCourses() != null
                    && !result.getGolfCourses().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        stadiumModelList.clear();
                        stadiumModelList.addAll(result.getGolfCourses());
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        stadiumModelList.addAll(result.getGolfCourses());
                        break;
                }
                lstv.setResultSize(stadiumModelList.size(), hasNext);
                placeListAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        stadiumModelList.clear();
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

        StadiumListModel.stadiums(page, latitude, longitude, cityID, provinceID, order).done(this)
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
            case R.id.place_search_key:
                intent = new Intent(this, KeyWordSearchActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.city_search_key:
                intent = new Intent(this, CitySelectActivity.class);
                startActivityForResult(intent, cityreqCode);
                break;
            case R.id.total_frist:
                fristFlag = 1;
                changeFlag();
                break;
            case R.id.price_frist:
                fristFlag = 2;
                changeFlag();
                break;
            case R.id.distance_frist:
                fristFlag = 3;
                changeFlag();
                break;
        }
    }

    private void changeFlag() {
        if (fristFlag == 1) {
            totalFrist.setTextColor(csl);
            disatnceFrist.setTextColor(Color.BLACK);
            priceFrist.setTextColor(Color.BLACK);
            order = null;
            // price|distance
        } else if (fristFlag == 2) {
            priceFrist.setTextColor(csl);
            totalFrist.setTextColor(Color.BLACK);
            disatnceFrist.setTextColor(Color.BLACK);
            order = "price";
        } else if (fristFlag == 3) {
            disatnceFrist.setTextColor(csl);
            priceFrist.setTextColor(Color.BLACK);
            totalFrist.setTextColor(Color.BLACK);
            order = "distance";
        }
        loadData(AutoListView.REFRESH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == cityreqCode && resultCode == 1) {
            String name = data.getStringExtra("city");
            long cityId = data.getLongExtra("cityId", 0l);
            if (cityId != 0l) {
                cityID = String.valueOf(cityId);
                cityName.setText(name);
                loadData(AutoListView.REFRESH);
            } else {
                String proname = data.getStringExtra("province");
                long proId = data.getLongExtra("provinceId", 0l);
                if (proId != 0l) {
                    provinceID = String.valueOf(proId);
                    cityID = null;
                    cityName.setText(proname);
                    loadData(AutoListView.REFRESH);
                }
            }

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
        MobclickAgent.onPageStart("产品列表页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("产品列表页面");
        MobclickAgent.onPause(this);
    }
}
