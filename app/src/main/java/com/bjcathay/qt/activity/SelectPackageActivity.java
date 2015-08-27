
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
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.SelectPackageAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.ProductListModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.TimeCount;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.AutoListView;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dengt on 15-7-28.
 */
public class SelectPackageActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback, View.OnClickListener {
    private TopView topView;
    private GApplication gApplication;
    private SelectPackageAdapter placeListAdapter;
    private List<ProductModel> stadiumModelList;
    private AutoListView lstv;
    private int page = 1;
    private TimeCount timeCount;
    private String provinceID;
    private Date now;
    private String latitude;
    private String longitude;
    private String cityID;
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
        setContentView(R.layout.activity_package);
        gApplication = GApplication.getInstance();
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_placelist_layout);
        topView.setHomeBackVisiable();
        //topView.setSearchVisiable();
        topView.setTitleText("精选套餐");
        cityName = ViewUtil.findViewById(this, R.id.city_name);
        totalFrist = ViewUtil.findViewById(this, R.id.total_frist);
        priceFrist = ViewUtil.findViewById(this, R.id.price_frist);
        disatnceFrist = ViewUtil.findViewById(this, R.id.distance_frist);
        csl = getResources().getColorStateList(R.color.order_price_color);
        stadiumModelList = new ArrayList<ProductModel>();
        placeListAdapter = new SelectPackageAdapter(stadiumModelList, this);

        lstv = (AutoListView) findViewById(R.id.place_lstv);
        lstv.setAdapter(placeListAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
    }

    private void initEvent() {

        lstv.setListViewEmptyImage(R.drawable.yuechang);
        lstv.setListViewEmptyMessage("没有查到相关套餐～");
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= stadiumModelList.size()) {
                    // todo
                    if (ProductType.prdtType.COMBO.equals(stadiumModelList.get(i - 1).getType())) {
                        // 跳套餐
                        final int ids = i - 1;
                        ProductModel.product(stadiumModelList.get(ids).getId())
                                .done(new ICallback() {
                                    @Override
                                    public void call(Arguments arguments) {
                                        JSONObject jsonObject = arguments.get(0);
                                        if (jsonObject.optBoolean("success")) {
                                            ProductModel productModel = JSONUtil.load(ProductModel.class,
                                                    jsonObject.optJSONObject("product"));
                                      //  ProductModel productModel = arguments.get(0);
                                        Intent intent = new Intent(SelectPackageActivity.this,
                                                PackageDetailActivity.class);
                                        intent.putExtra("id", stadiumModelList.get(ids).getId());
                                        intent.putExtra("name", stadiumModelList.get(ids).getName());
                                        intent.putExtra("product", productModel);
                                        ViewUtil.startActivity(SelectPackageActivity.this, intent);
                                        } else {
                                            String errorMessage = jsonObject.optString("message");
                                            int code = jsonObject.optInt("code");
                                            if (code == 13005){
                                                Intent intent = new Intent(SelectPackageActivity.this,
                                                        ProductOfflineActivity.class);
                                                intent.putExtra("name", stadiumModelList.get(ids).getName());
                                                ViewUtil.startActivity(SelectPackageActivity.this, intent);
                                            } else {
                                            if (!StringUtils.isEmpty(errorMessage))
                                                DialogUtil.showMessage(errorMessage);
                                            else {

                                                DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                            }}
                                        }
                                    }
                                }).fail(new ICallback() {
                                    @Override
                                    public void call(Arguments arguments) {
                                        DialogUtil.showMessage(getString(R.string.empty_net_text));
                                    }
                                });

                    } else {
                    }
                }
                /*
                 * Intent intent = new Intent(SelectPackageActivity.this,
                 * PackageDetailActivity.class);
                 * ViewUtil.startActivity(SelectPackageActivity.this, intent);
                 */
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

        ProductListModel.comboList(page, latitude, longitude, null, null, order).done(this)
                .fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        if (lstv != null) {
                            lstv.onRefreshComplete();
                            lstv.setResultSize(-1, false);
                        }
                    }
                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {

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
        } else if (fristFlag == 2) {
            priceFrist.setTextColor(csl);
            totalFrist.setTextColor(Color.BLACK);
            disatnceFrist.setTextColor(Color.BLACK);
            order = "price";
        } else if (fristFlag == 3) {
            disatnceFrist.setTextColor(csl);
            priceFrist.setTextColor(Color.BLACK);
            totalFrist.setTextColor(Color.BLACK);
            order = "day";
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
                    cityID = null;
                    provinceID = String.valueOf(proId);
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
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("套餐列表页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("套餐列表页面");
        MobclickAgent.onPause(this);
    }
}
