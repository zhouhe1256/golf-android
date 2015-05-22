package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by bjcathay on 15-4-29.
 */
public class OrderDetailActivity extends Activity implements ICallback, View.OnClickListener {
    private Activity context;
    private TopView topView;
    private Long id;
    private String imaUrl;
    private ImageView orderImg;
    private TextView orderName;
    private TextView orderSale;
    private TextView orderConDate;
    private TextView orderConNum;
    private TextView orderPrice;
    private TextView orderPay;
    private TextView orderPhone;
    private TextView orderNum;
    private TextView orderPayDate;
    private TextView orderStatus;
    private OrderModel orderModel;
    private TextView orderAddress;
    private Button orderToPay;
    private ImageView tuangou;
    private ImageView temai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_order_detail_layout);
        orderImg = ViewUtil.findViewById(this, R.id.order_detail_img);
        orderName = ViewUtil.findViewById(this, R.id.order_detail_name);
        orderSale = ViewUtil.findViewById(this, R.id.order_detail_sale);
        orderConDate = ViewUtil.findViewById(this, R.id.order_detail_comsumer_date);
        orderConNum = ViewUtil.findViewById(this, R.id.order_detail_comsumer_numb);
        orderPrice = ViewUtil.findViewById(this, R.id.order_detail_price);
        orderPay = ViewUtil.findViewById(this, R.id.order_detail_pay);
        orderPhone = ViewUtil.findViewById(this, R.id.order_detail_phone);
        orderNum = ViewUtil.findViewById(this, R.id.order_detail_nomber);
        orderPayDate = ViewUtil.findViewById(this, R.id.order_detail_pay_date);
        orderStatus = ViewUtil.findViewById(this, R.id.order_detail_status);
        orderAddress = ViewUtil.findViewById(this, R.id.order_detail_address);
        orderToPay = ViewUtil.findViewById(this, R.id.order_detail_now_pay);
        tuangou = ViewUtil.findViewById(this, R.id.order_detail_tuangou);
        temai = ViewUtil.findViewById(this, R.id.order_detail_temai);
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setShareVisiable();
        topView.setTitleText("订单详情");
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        // imaUrl = intent.getStringExtra("imageurl");

        OrderModel.orderDetail(id).done(this);

    }

    @Override
    public void call(Arguments arguments) {
        orderModel = arguments.get(0);
        ImageViewAdapter.adapt(orderImg, orderModel.getImageUrl(), R.drawable.ic_launcher);
        orderName.setText(orderModel.getTitle());
        orderSale.setText(orderModel.getPriceInclude());
        orderConDate.setText("预约日期:" + orderModel.getDate());
        orderConNum.setText("预约人数:" + (orderModel.getPeopleNumber() == 0 ? "4人+" : (orderModel.getPeopleNumber() + "人")));
        orderPrice.setText("￥" + Double.toString(orderModel.getTotalPrice()));
        orderPay.setText("订单金额:￥" + Double.toString(orderModel.getTotalPrice()));
        orderPhone.setText("手机号码:" + PreferencesUtils.getString(this, PreferencesConstant.USER_PHONE));
        orderNum.setText("订单号:" + orderModel.getOrderId());
        orderPayDate.setText("交易日期:" + orderModel.getCreatedAt());
        orderAddress.setText("球场地址：" + orderModel.getAddress());
        if ("PENDING".equals(orderModel.getStatus())) {
            orderToPay.setVisibility(View.GONE);
            orderStatus.setText("待确认");
        } else if ("PROCESSING".equals(orderModel.getStatus())) {
            orderToPay.setVisibility(View.GONE);
            orderStatus.setText("待确认");
        } else if ("UNPAID".equals(orderModel.getStatus())) {
            orderToPay.setVisibility(View.VISIBLE);
            orderStatus.setText("未支付");
        } else if ("PAID".equals(orderModel.getStatus())) {
            orderToPay.setVisibility(View.GONE);
            orderStatus.setText("已支付");
        } else if ("FINISH".equals(orderModel.getStatus())) {
            orderToPay.setVisibility(View.GONE);
            orderStatus.setText("已完成");
        } else if ("CANCEL".equals(orderModel.getStatus())) {
            orderToPay.setVisibility(View.GONE);
            orderStatus.setText("已取消");
        }


        if ("SPECIAL".equals(orderModel.getType())) {
            temai.setVisibility(View.VISIBLE);
            tuangou.setVisibility(View.GONE);
        } else if ("GROUP".equals(orderModel.getType())) {
            tuangou.setVisibility(View.VISIBLE);
            temai.setVisibility(View.GONE);
        } else {
            tuangou.setVisibility(View.GONE);
            temai.setVisibility(View.GONE);
        }
        orderToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectPayWayActivity.class);
                intent.putExtra("order", orderModel);
                ViewUtil.startActivity(context, intent);
            }
        });
        orderAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddressActivity.class);
                intent.putExtra("url", getString(R.string.course_address));
                intent.putExtra("location", orderModel.getLat() + "," +orderModel.getLon() );
                intent.putExtra("title", "球场地址");
                intent.putExtra("content", orderModel.getAddress());
                //intent.putExtra("address",stationAddress.getText().toString().trim());
                intent.putExtra("src", "A|GOLF");
                ViewUtil.startActivity(context, intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.title_share_img:
                ShareUtil.showShare(this);
                break;
        }
    }
}
