
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.ProductAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.ProductListModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.TimeCount;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dengt on 15-7-27.
 */
public class ProductActivity extends Activity implements ICallback, View.OnClickListener,
        DeleteInfoDialog.DeleteInfoDialogResult {
    private TopView topView;
    private GApplication gApplication;
    private ProductAdapter placeListAdapter;
    private List<ProductModel> stadiumModelList;
    private TextView call;
    private ListView lstv;
    private int page = 1;
    private TimeCount timeCount;

    private Date now;
    private String latitude;
    private String longitude;
    private String cityID;
    private int cityreqCode = 1;
    private int fristFlag = 1;
    ColorStateList csl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        gApplication = GApplication.getInstance();
        initView();
        initEvent();
        initData();
    }

    private LayoutInflater inflater;
    private View header;
    private View footer;

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_placelist_layout);
        topView.setTitleBackVisiable();
        topView.setSearchVisiable();
        topView.setTitleText("加载中...");
        csl = getResources().getColorStateList(R.color.order_price_color);
        stadiumModelList = new ArrayList<ProductModel>();
        inflater = LayoutInflater.from(this);
        footer = inflater.inflate(R.layout.call_service_footer, null);
        call = ViewUtil.findViewById(footer, R.id.call_service);
        placeListAdapter = new ProductAdapter(stadiumModelList, this);

        lstv = (ListView) findViewById(R.id.place_lstv);
        lstv.addFooterView(footer, null, false);
        lstv.setAdapter(placeListAdapter);
        // lstv.setOnRefreshListener(this);
        // lstv.setOnLoadListener(this);
    }

    private void initEvent() {
        call.setOnClickListener(this);
        // lstv.setListViewEmptyImage(R.drawable.ic_network_error);
        // lstv.setListViewEmptyMessage(getString(R.string.empty_net_text));
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= stadiumModelList.size()) {
                    // todo
                    if (ProductType.prdtType.COMBO.equals(stadiumModelList.get(i).getType())) {
                        // 跳套餐
                        final int ids=i;
                        ProductModel.product(stadiumModelList.get(ids).getId()).done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                ProductModel productModel = arguments.get(0);
                                Intent intent = new Intent(ProductActivity.this,
                                        PackageDetailActivity.class);
                                intent.putExtra("id", stadiumModelList.get(ids).getId());
                                intent.putExtra("name", stadiumModelList.get(ids).getName());
                                intent.putExtra("product", productModel);
                                ViewUtil.startActivity(ProductActivity.this, intent);
                            }
                        }).fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                DialogUtil.showMessage("该产品已下架");
                            }
                        });

                    } else {
                        // 跳产品
                        Intent intent = new Intent(ProductActivity.this,
                                OrderStadiumDetailActivity.class);
                        intent.putExtra("imageurl", stadiumModelList.get(i).getImageUrl());
                        intent.putExtra("id", stadiumModelList.get(i).getId());
                        intent.putExtra("type", stadiumModelList.get(i).getType());
                        ViewUtil.startActivity(ProductActivity.this, intent);
                    }
                }
               /* Intent intent = new Intent(ProductActivity.this,
                        OrderStadiumDetailActivity.class);
                ViewUtil.startActivity(ProductActivity.this, intent);*/
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ProductListModel result = (ProductListModel) msg.obj;
            boolean hasNext = result.isHasNext();
            if (result != null && result.getProducts() != null && !result.getProducts().isEmpty()) {
                // switch (msg.what) {
                // case AutoListView.REFRESH:
                // // lstv.onRefreshComplete();
                // stadiumModelList.clear();
                // stadiumModelList.addAll(result.getProducts());
                // break;
                // case AutoListView.LOAD:
                // // lstv.onLoadComplete();
                // stadiumModelList.addAll(result.getProducts());
                // break;
                // }
                // lstv.setResultSize(stadiumModelList.size(), hasNext);
                stadiumModelList.addAll(result.getProducts());
                placeListAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                // case AutoListView.REFRESH:
                // // lstv.onRefreshComplete();
                // break;
                // case AutoListView.LOAD:
                // // lstv.onLoadComplete();
                // break;
                }
                // lstv.setResultSize(stadiumModelList.size(), hasNext);
                placeListAdapter.notifyDataSetChanged();
            }
        }

        ;
    };
    private Long id;
    private String name;

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        name = intent.getStringExtra("name");
        topView.setTitleText(name);
        latitude = PreferencesUtils.getString(this, PreferencesConstant.LATITUDE);
        longitude = PreferencesUtils.getString(this, PreferencesConstant.LONGITUDE);
        cityID = PreferencesUtils.getString(this, PreferencesConstant.CITY_ID);
        loadData(1);
    }

    private void loadData(final int what) {
        switch (what) {
            case 1:
                page = 1;
                break;
            case 2:
                page++;
                break;
        }
        if (id != 0l)
            ProductListModel.productList(id, page, latitude, longitude, cityID).done(this)
                    .fail(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            if (lstv != null) {
                                // lstv.onRefreshComplete();
                                // lstv.setResultSize(-1, false);
                            }
                        }
                    });
    }

    @Override
    public void call(Arguments arguments) {
        ProductListModel stadiumListModel = arguments.get(0);
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = 1;
        else {
            msg.what = 2;
        }
        msg.obj = stadiumListModel;
        handler.sendMessage(msg);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.call_service:
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(this,
                        R.style.InfoDialog, getResources().getString(R.string.service_tel_format)
                                .toString().trim(), "呼叫", 0l, this);
                infoDialog.show();
                break;
        }
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + getResources().getString(R.string.service_tel).toString().trim()));
            this.startActivity(intent);
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
