
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.alipay.Alipay;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.model.WXPayModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.bjcathay.qt.wxpay.WXpay;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-5-14.
 */
public class SelectPayWayActivity extends Activity implements View.OnClickListener,
        Alipay.PaySucessOrNot {
    private Activity activity;
    private TopView topView;
    private OrderModel orderModel;
    private TextView orderName;
    private TextView orderSale;
    private TextView orderConDate;
    private TextView orderConNum;
    private TextView orderPay;
    private TextView orderPhone;
    private Button sysPay;
    private LinearLayout payThree;
    private boolean isUseBalance = false;
    private LinearLayout useWallet;

    private ImageView showSelect;
    private TextView myWallet;
    UserModel userModel;
    private TextView shouldPay;

    private WXPAYBroadcastReceiver wxpayBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_detail);
        activity = this;
        initView();
        initReceiver();
        initData();
        initEvent();
    }

    private void initData() {
        Intent intent = getIntent();
        orderModel = (OrderModel) intent.getSerializableExtra("order");
        orderName.setText(orderModel.getTitle());
        orderSale.setText(orderModel.getPriceInclude());
        orderConDate.setText("" + DateUtil.stringToDateToOrderString(orderModel.getDate()));
        orderConNum.setText("" + Integer.toString(orderModel.getPeopleNumber()) + "人");
        orderPay.setText("" + (int) Math.floor(orderModel.getTotalPrice()));
        orderPhone.setText("" + PreferencesUtils.getString(this, PreferencesConstant.USER_PHONE));
        shouldPay.setText((int) Math.floor(orderModel.getTotalPrice()) + "");
        userModel = GApplication.getInstance().getUser();
        myWallet.setText((int) Math.floor(userModel.getBalance()) + "");

    }

    private void initReceiver() {
        wxpayBroadcastReceiver = new WXPAYBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WXPAY");
        this.registerReceiver(wxpayBroadcastReceiver, intentFilter);
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_pay_detail_layout);
        orderName = ViewUtil.findViewById(this, R.id.dialog_order_sure_name);
        orderSale = ViewUtil.findViewById(this, R.id.dialog_order_sure_service);
        orderConDate = ViewUtil.findViewById(this, R.id.dialog_order_sure_time);
        orderConNum = ViewUtil.findViewById(this, R.id.dialog_order_sure_number);
        orderPay = ViewUtil.findViewById(this, R.id.dialog_order_sure_price);
        orderPhone = ViewUtil.findViewById(this, R.id.dialog_order_sure_phone);
        sysPay = ViewUtil.findViewById(this, R.id.pay_sure);
        useWallet = ViewUtil.findViewById(this, R.id.use_wallet);
        showSelect = ViewUtil.findViewById(this, R.id.show_selected);
        payThree = ViewUtil.findViewById(this, R.id.to_pay_layout);
        shouldPay = ViewUtil.findViewById(this, R.id.shouldpay);
        myWallet = ViewUtil.findViewById(this, R.id.my_wallet);
    }

    private void initEvent() {
        topView.setTitleText("支付");
        topView.setTitleBackVisiable();
        findViewById(R.id.pay_ipay).setOnClickListener(this);
        findViewById(R.id.pay_yinlian).setOnClickListener(this);
        findViewById(R.id.pay_wx).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.pay_wx:
                findViewById(R.id.pay_wx).setOnClickListener(null);
                WXpay wXpay = new WXpay(this, orderModel);
                wXpay.wxpay(isUseBalance);
                break;
            case R.id.pay_ipay:
                findViewById(R.id.pay_ipay).setOnClickListener(null);
                WXPayModel.paytext(orderModel.getId(), isUseBalance, "alipay").done(
                        new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                if (jsonObject.optBoolean("success")) {
                                    String pay = jsonObject.optString("prepay");
                                    if (Integer.valueOf(pay) > 0) {
                                        Alipay alipay = new Alipay(activity, orderModel,
                                                SelectPayWayActivity.this);
                                        alipay.pay(pay);
                                    } else {
                                        DialogUtil.showMessage("支付失败");
                                    }
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

                break;
            case R.id.pay_yinlian:
                findViewById(R.id.pay_yinlian).setOnClickListener(null);
                break;
            case R.id.title_back_img:
                finish();
                break;
            case R.id.use_wallet:
                if (showSelect.getVisibility() == View.VISIBLE) {
                    showSelect.setVisibility(View.GONE);
                    isUseBalance = false;
                    payThree.setVisibility(View.VISIBLE);
                    shouldPay.setText((int) Math.floor(orderModel.getTotalPrice())
                            + "");
                } else {
                    showSelect.setVisibility(View.VISIBLE);
                    isUseBalance = true;

                    if (orderModel.getTotalPrice() <= userModel.getBalance()) {
                        sysPay.setVisibility(View.VISIBLE);
                        payThree.setVisibility(View.GONE);
                    } else {
                        sysPay.setVisibility(View.GONE);
                        payThree.setVisibility(View.VISIBLE);
                        shouldPay.setText((int) Math.floor(orderModel.getTotalPrice()
                                - userModel.getBalance())
                                + "");
                    }
                }

                break;
            case R.id.pay_sure:
                // 钱包支付
                WXPayModel.paytext(orderModel.getId(), isUseBalance, "system").done(
                        new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                if (jsonObject.optBoolean("success")) {
                                     DialogUtil.showMessage("支付成功");
                                    Intent intent;
//                                    if ("GROUP".equals(orderModel.getType())) {
//                                        intent = new Intent(SelectPayWayActivity.this,
//                                                OrderSucTuanActivity.class);
//                                        intent.putExtra("id", orderModel.getId());
//                                    } else {
//                                        intent = new Intent(SelectPayWayActivity.this,
//                                                PaySuccessActivity.class);
//                                    }
//                                    intent.putExtra("order", orderModel);
//                                    ViewUtil.startActivity(SelectPayWayActivity.this, intent);
                                    finish();
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
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void payStatus(String status, boolean isPay) {
        findViewById(R.id.pay_ipay).setOnClickListener(this);
        if (isPay) {
            if ("sucess".equals(status)) {
                DialogUtil.showMessage("支付成功");
                Intent intent;
                /*
                 * OrderModel.orderVerify(orderModel.getId()).done(new
                 * ICallback() {
                 * @Override public void call(Arguments arguments) { JSONObject
                 * jsonObject = arguments.get(0); if
                 * (jsonObject.optBoolean("success")) {
                 */
                // Intent intent;
//                if ("GROUP".equals(orderModel.getType())) {
//                    intent = new Intent(SelectPayWayActivity.this, OrderSucTuanActivity.class);
//                    intent.putExtra("id", orderModel.getId());
//                } else {
//                    intent = new Intent(SelectPayWayActivity.this, PaySuccessActivity.class);
//                }
//                intent.putExtra("order", orderModel);
//                ViewUtil.startActivity(SelectPayWayActivity.this, intent);
                finish();
                /*
                 * } else { int code = jsonObject.optInt("code");
                 * DialogUtil.showMessage(ErrorCode.getCodeName(code)); }
                 */
                /*
                 * } });
                 */

                /*
                 * if ("GROUP".equals(orderModel.getType())) { intent = new
                 * Intent(context, SelectPayWayActivity.class); } else { intent
                 * = new Intent(this, PaySuccessActivity.class); }
                 * intent.putExtra("order", orderModel);
                 * ViewUtil.startActivity(this, intent); finish();
                 */
            } else if ("process".equals(status)) {
                DialogUtil.showMessage("支付结果确认中");
            } else if ("fail".equals(status)) {
                DialogUtil.showMessage("支付失败");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wxpayBroadcastReceiver != null) {
            this.unregisterReceiver(wxpayBroadcastReceiver);
        }
    }

    private class WXPAYBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("WXPAY".equals(action)) {
                activity.findViewById(R.id.pay_wx).setOnClickListener(SelectPayWayActivity.this);
                /*
                 * OrderModel.orderVerify(orderModel.getId()).done(new
                 * ICallback() {
                 * @Override public void call(Arguments arguments) { JSONObject
                 * jsonObject = arguments.get(0); if
                 * (jsonObject.optBoolean("success")) {
                 */
                // Intent intent;
                String tag = intent.getStringExtra("tag");
                if ("sucess".equals(tag)) {
                    DialogUtil.showMessage("支付成功");
//                    if ("GROUP".equals(orderModel.getType())) {
//                        intent = new Intent(SelectPayWayActivity.this, OrderSucTuanActivity.class);
//                        intent.putExtra("id", orderModel.getId());
//                    } else {
//                        intent = new Intent(SelectPayWayActivity.this, PaySuccessActivity.class);
//                    }
//                    intent.putExtra("order", orderModel);
//                    ViewUtil.startActivity(SelectPayWayActivity.this, intent);
                    finish();
                } else {

                }
                /*
                 * } else { int code = jsonObject.optInt("code");
                 * DialogUtil.showMessage(ErrorCode.getCodeName(code)); } } });
                 */

                /*
                 * Intent intent1; if ("GROUP".equals(orderModel.getType())) {
                 * intent1 = new Intent(SelectPayWayActivity.this,
                 * OrderSucTuanActivity.class); intent1.putExtra("id",
                 * orderModel.getId()); } else { intent1 = new
                 * Intent(SelectPayWayActivity.this, PaySuccessActivity.class);
                 * } intent1.putExtra("order", orderModel);
                 * ViewUtil.startActivity(SelectPayWayActivity.this, intent1);
                 * finish();
                 */
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("支付页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("支付页面");
        MobclickAgent.onPause(this);
    }
}
