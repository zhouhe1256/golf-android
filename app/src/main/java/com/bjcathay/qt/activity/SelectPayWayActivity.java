package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by bjcathay on 15-5-14.
 */
public class SelectPayWayActivity extends Activity implements View.OnClickListener {
    private Activity context;
    private TopView topView;
    LinearLayout ipay;
    LinearLayout yinlian;
    private OrderModel orderModel;
    private TextView orderName;
    private TextView orderSale;
    private TextView orderConDate;
    private TextView orderConNum;
    private TextView orderPay;
    private TextView orderPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_detail);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        Intent intent = getIntent();
        orderModel = (OrderModel) intent.getSerializableExtra("order");
        orderName.setText(orderModel.getTitle());
        orderSale.setText(orderModel.getPriceInclude());
        orderConDate.setText("" + orderModel.getDate());
        orderConNum.setText("" + Integer.toString(orderModel.getPeopleNumber())+"人");
        orderPay.setText("￥" + Double.toString(orderModel.getTotalPrice()));
        orderPhone.setText("" + PreferencesUtils.getString(this, PreferencesConstant.USER_PHONE));

    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_pay_detail_layout);
        orderName = ViewUtil.findViewById(this, R.id.dialog_order_sure_name);
        orderSale = ViewUtil.findViewById(this, R.id.dialog_order_sure_service);
        orderConDate = ViewUtil.findViewById(this, R.id.dialog_order_sure_time);
        orderConNum = ViewUtil.findViewById(this, R.id.dialog_order_sure_number);
        orderPay = ViewUtil.findViewById(this, R.id.dialog_order_sure_price);
        orderPhone = ViewUtil.findViewById(this, R.id.dialog_order_sure_phone);

    }
    private void initEvent() {
        topView.setTitleText("支付");
        topView.setTitleBackVisiable();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_ipay:
                DialogUtil.showMessage("支付宝支付哦！！！");
                break;
            case R.id.pay_yinlian:
                DialogUtil.showMessage("银联支付哦！！！");
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }
}
