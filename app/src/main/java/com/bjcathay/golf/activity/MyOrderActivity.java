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
import com.bjcathay.golf.adapter.MyOrderAdapter;
import com.bjcathay.golf.constant.ErrorCode;
import com.bjcathay.golf.model.OrderListModel;
import com.bjcathay.golf.model.OrderModel;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.AutoListView;
import com.bjcathay.golf.view.DeleteInfoDialog;
import com.bjcathay.golf.view.DeleteInfoDialog.DeleteInfoDialogResult;
import com.bjcathay.golf.view.TopView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-4-28.
 */
public class MyOrderActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback, DeleteInfoDialogResult {
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
        topView.setActivity(this);
        topView.setTitleText("我的订单");
        orderModels = new ArrayList<OrderModel>();
        myOrderAdapter = new MyOrderAdapter(orderModels, this);
        lstv = (AutoListView) findViewById(R.id.my_order_list);
        lstv.setAdapter(myOrderAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
    }

    private void initEvent() {
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // DialogUtil.hintMessage("选档期", PlaceListActivity.this);
                Intent intent = new Intent(MyOrderActivity.this, OrderDetailActivity.class);
                intent.putExtra("imageurl", orderModels.get(i - 1).getImageUrl());
                intent.putExtra("id", orderModels.get(i - 1).getId());
                ViewUtil.startActivity(MyOrderActivity.this, intent);
            }
        });
        lstv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //弹窗设置是否取消
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(context,
                        R.style.InfoDialog, "确认删除该订单", orderModels.get(i - 1).getId(), MyOrderActivity.this);
                infoDialog.show();
                return false;
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            OrderListModel result = (OrderListModel) msg.obj;
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
                lstv.setResultSize(result.getOrders().size());
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
                orderModels.clear();
                lstv.setResultSize(0);
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
        OrderListModel.getMyOrder(page).done(this);
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
                        int code = jsonObject.optInt("code");
                        DialogUtil.showMessage(ErrorCode.getCodeName(code));
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    DialogUtil.showMessage("失败");
                }
            });
        }
    }
}
