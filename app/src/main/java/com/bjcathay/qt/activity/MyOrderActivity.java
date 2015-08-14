
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
import com.bjcathay.qt.adapter.MyOrderAdapter;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.OrderListModel;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.AutoListView;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.DeleteInfoDialog.DeleteInfoDialogResult;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-4-28.
 */
public class MyOrderActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback, DeleteInfoDialogResult, View.OnClickListener {
    private Activity context;
    private MyOrderAdapter myOrderAdapter;
    private List<OrderModel> orderModels;
    private TopView topView;
    private AutoListView lstv;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {

        topView = ViewUtil.findViewById(this, R.id.top_my_order_layout);
        topView.setTitleBackVisiable();
        topView.setTitleText("我的订单");
        orderModels = new ArrayList<OrderModel>();
        myOrderAdapter = new MyOrderAdapter(orderModels, this);
        lstv = (AutoListView) findViewById(R.id.my_order_list);
        lstv.setAdapter(myOrderAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        lstv.setListViewEmptyImage(R.drawable.ic_empty_order);
        lstv.setListViewEmptyMessage(getString(R.string.empty_order_text));
    }

    private void initEvent() {
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= orderModels.size()) {
                    Intent intent = new Intent(MyOrderActivity.this, OrderDetailActivity.class);
                    intent.putExtra("imageurl", orderModels.get(i - 1).getImageUrl());
                    intent.putExtra("id", orderModels.get(i - 1).getId());
                    ViewUtil.startActivity(MyOrderActivity.this, intent);
                }
            }
        });
        lstv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(context,
                        R.style.InfoDialog, "确认删除该订单", orderModels.get(i - 1).getId(),
                        MyOrderActivity.this);
                infoDialog.show();
                return true;
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            OrderListModel result = (OrderListModel) msg.obj;
            boolean hasNext = result.isHasNext();
            if (result != null && result.getOrders() != null && !result.getOrders().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        orderModels.clear();
                        orderModels.addAll(result.getOrders());
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        orderModels.addAll(result.getOrders());
                        break;
                }
                lstv.setResultSize(orderModels.size(), hasNext);
                myOrderAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        break;
                }
                lstv.setResultSize(orderModels.size(), hasNext);
                myOrderAdapter.notifyDataSetChanged();
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
        OrderListModel.getMyOrder(page).done(this).fail(new ICallback() {
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
        OrderListModel orderListModel = arguments.get(0);
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = AutoListView.REFRESH;
        else {
            msg.what = AutoListView.LOAD;
        }
        msg.obj = orderListModel;
        handler.sendMessage(msg);
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            OrderModel.orderDelete(targetId).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        DialogUtil.showMessage("已删除订单");
                        loadData(AutoListView.REFRESH);
                        // }
                    } else {
                        String errorMessage = jsonObject.optString("message");
                        if (!StringUtils.isEmpty(errorMessage))
                            DialogUtil.showMessage(errorMessage);
                        else {
                            int code = jsonObject.optInt("code");
                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    DialogUtil.showMessage(getString(R.string.empty_net_text));
                }
            });
        }
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
        MessageReceiver.baseActivity=this;
        initData();
        MobclickAgent.onPageStart("我的订单页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的订单页面");
        MobclickAgent.onPause(this);
    }
}
