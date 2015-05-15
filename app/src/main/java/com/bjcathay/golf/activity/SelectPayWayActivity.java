package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.model.OrderModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.PreferencesConstant;
import com.bjcathay.golf.util.PreferencesUtils;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

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
        orderSale.setText(orderModel.getPackageContent());
        orderConDate.setText("" + orderModel.getStartAt());
        orderConNum.setText("" + Integer.toString(orderModel.getPeopleNumber()));
        orderPay.setText("￥" + Double.toString(orderModel.getTotalPrice()));
        orderPhone.setText("" + PreferencesUtils.getString(this, PreferencesConstant.USER_PHONE));

    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_pay_detail_layout);
        orderName = ViewUtil.findViewById(this, R.id.order_detail_name);
        orderSale = ViewUtil.findViewById(this, R.id.order_detail_sale);
        orderConDate = ViewUtil.findViewById(this, R.id.order_detail_comsumer_date);
        orderConNum = ViewUtil.findViewById(this, R.id.order_detail_comsumer_numb);

        orderPay = ViewUtil.findViewById(this, R.id.order_detail_pay);
        orderPhone = ViewUtil.findViewById(this, R.id.order_detail_phone);

    }
    private void initEvent() {
        topView.setActivity(this);
        topView.setTitleText("支付");
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
        }
    }
}
