
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.CancleInfoDialog;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.RoundCornerImageView;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-4-29.
 */
public class OrderDetailActivity extends Activity implements ICallback, View.OnClickListener,
        DeleteInfoDialog.DeleteInfoDialogResult, CancleInfoDialog.CancleInfoDialogResult {

    private Activity context;
    private TopView topView;
    private Long id = 0l;
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
    private Button orderToPay;
    private Button contactUs;
    private Button cancleOrder;
    private LinearLayout orderUndel;
    private TextView orderDel;
    private ShareModel shareModel;
    private TextView userRealName;
    private TextView personNames;
    private TextView purchasingNotice;
    private RoundCornerImageView orderImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_detail);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_order_detail_layout);
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
        orderToPay = ViewUtil.findViewById(this, R.id.order_detail_now_pay);
        orderUndel = ViewUtil.findViewById(this, R.id.order_undelete_note);
        orderDel = ViewUtil.findViewById(this, R.id.order_delete_note);
        contactUs = ViewUtil.findViewById(this, R.id.contact_us);
        cancleOrder = ViewUtil.findViewById(this, R.id.order_cancle);
        userRealName = ViewUtil.findViewById(this, R.id.userRealName);
        personNames = ViewUtil.findViewById(this, R.id.personNames);
        purchasingNotice = ViewUtil.findViewById(this, R.id.purchasingNotice);
        orderImg = ViewUtil.findViewById(this, R.id.my_order_img);
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText("订单详情");
    }

    private void initData() {
        Intent intent = getIntent();
        if ("message".equals(intent.getAction())) {
            orderModel = (OrderModel) intent.getSerializableExtra("orderModel");
            initViewData();
        } else {
            id = intent.getLongExtra("id", 0);
            OrderModel.orderDetail(id).done(this).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    orderDel.setVisibility(View.VISIBLE);
                    orderUndel.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void call(Arguments arguments) {
        orderModel = arguments.get(0);
        initViewData();
    }

    private void initViewData() {
        if (orderModel != null) {
            topView.setShareVisiable();
            orderName.setText(orderModel.getTitle());
            orderSale.setText("" + orderModel.getPriceInclude());
            ImageViewAdapter.adapt(orderImg, orderModel.getImageUrl(), R.drawable.exchange_default);

            orderConDate.setText("" + DateUtil.stringToDateToOrderString(orderModel.getDate()));
            orderConNum.setText(""
                    + (orderModel.getPeopleNumber() == 0 ? "4人+"
                            : (orderModel.getPeopleNumber() + "人")));
            if (orderModel.getPeopleNumber() == 0)
                orderPrice.setText("￥" + (int) Math.floor(orderModel.getTotalPrice()) + "+");
            else
                orderPrice.setText("￥" + (int) Math.floor(orderModel.getTotalPrice()));
            orderPay.setText("￥0");
            orderPhone.setText(orderModel.getMobileNumber());
            orderNum.setText("" + orderModel.getOrderId());
            orderPayDate.setText("" + DateUtil.shortDateString(orderModel.getCreatedAt()));
            personNames.setText(orderModel.getPersonNames());
            userRealName.setText(orderModel.getUserRealName());
            purchasingNotice.setText(orderModel.getPurchasingNotice());
            cancleOrder.setOnClickListener(this);
            if ("PENDING".equals(orderModel.getStatus())) {
                orderToPay.setVisibility(View.GONE);
                orderStatus.setText("确认中");
                cancleOrder.setBackgroundResource(R.drawable.yellow_stroke_bg);
                cancleOrder.setTextColor(getResources().getColor(R.color.order_price_color));
                cancleOrder.setVisibility(View.VISIBLE);
            } else if ("UNPAID".equals(orderModel.getStatus())) {
                orderToPay.setVisibility(View.VISIBLE);
                orderStatus.setText("待支付");
                cancleOrder.setBackgroundResource(R.drawable.yellow_stroke_bg);
                cancleOrder.setTextColor(getResources().getColor(R.color.order_price_color));
                cancleOrder.setVisibility(View.VISIBLE);
            } else if ("PAID".equals(orderModel.getStatus())) {
                orderToPay.setVisibility(View.GONE);
                orderStatus.setText("已支付");
                cancleOrder.setVisibility(View.VISIBLE);
                cancleOrder.setBackgroundResource(R.drawable.gray_stroke_bg);
                cancleOrder.setTextColor(Color.GRAY);
                cancleOrder.setOnClickListener(null);
                if (orderModel.getPeopleNumber() == 0)
                    orderPay.setText("￥" + (int) Math.floor(orderModel.getTotalPrice()) + "+");
                else
                    orderPay.setText("￥" + (int) Math.floor(orderModel.getTotalPrice()));
            } else if ("FINISH".equals(orderModel.getStatus())) {
                orderToPay.setVisibility(View.GONE);
                orderStatus.setText("已完成");
                cancleOrder.setBackgroundResource(R.drawable.gray_stroke_bg);
                cancleOrder.setTextColor(Color.GRAY);
                cancleOrder.setOnClickListener(null);
                cancleOrder.setVisibility(View.VISIBLE);
                if (orderModel.getPeopleNumber() == 0)
                    orderPay.setText("￥" + (int) Math.floor(orderModel.getTotalPrice()) + "+");
                else
                    orderPay.setText("￥" + (int) Math.floor(orderModel.getTotalPrice()));
            } else if ("CANCEL".equals(orderModel.getStatus())) {
                orderToPay.setVisibility(View.GONE);
                orderStatus.setText("已取消");
                cancleOrder.setBackgroundResource(R.drawable.gray_stroke_bg);
                cancleOrder.setTextColor(Color.GRAY);
                cancleOrder.setOnClickListener(null);
                cancleOrder.setVisibility(View.VISIBLE);
            }
            orderToPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SelectPayWayActivity.class);
                    intent.putExtra("order", orderModel);
                    ViewUtil.startActivity(context, intent);
                }
            });

            contactUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteInfoDialog infoDialog = new DeleteInfoDialog(OrderDetailActivity.this,
                            R.style.InfoDialog, getResources()
                                    .getString(R.string.service_tel_format).toString().trim(),
                            "呼叫", 0l, OrderDetailActivity.this);
                    infoDialog.show();
                }
            });
        }

    }

    private void cancleOrder() {
        CancleInfoDialog infoDialog = new CancleInfoDialog(OrderDetailActivity.this,
                R.style.InfoDialog, "确认取消订单?", 0l, OrderDetailActivity.this);
        infoDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.title_share_img:
                if (orderModel != null)
                    if (shareModel == null)
                        ShareModel.shareOrders(orderModel.getId()).done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                shareModel = arguments.get(0);
                                ShareUtil.getInstance().shareDemo(context, shareModel);
                            }
                        });
                    else
                        ShareUtil.getInstance().shareDemo(context, shareModel);
                break;
            case R.id.order_cancle:
                cancleOrder();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (id != 0)
            OrderModel.orderDetail(id).done(this).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    orderDel.setVisibility(View.VISIBLE);
                    orderUndel.setVisibility(View.GONE);

                }
            });
        MobclickAgent.onPageStart("订单详情页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("订单详情页面");
        MobclickAgent.onPause(this);
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + getResources().getString(R.string.service_tel).toString().trim()));
            this.startActivity(intent);
        }
    }

    @Override
    public void cancleResult(Long targetId, boolean isDelete) {
        if (isDelete)
            OrderModel.orderCancle(orderModel.getId()).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        DialogUtil.showMessage("订单已取消");
                        cancleOrder.setBackgroundResource(R.drawable.gray_stroke_bg);
                        cancleOrder.setTextColor(Color.GRAY);

                      //  cancleOrder.setVisibility(View.GONE);
                        cancleOrder.setOnClickListener(null);
                        orderToPay.setVisibility(View.GONE);
                        orderStatus.setText("已取消");
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
            });
    }
}
