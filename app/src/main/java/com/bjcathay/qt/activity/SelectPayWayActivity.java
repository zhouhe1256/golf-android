package com.bjcathay.qt.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog.Builder;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.alipay.Alipay;
import com.bjcathay.qt.model.ChargeModel;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.pingplusplus.android.PaymentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by bjcathay on 15-5-14.
 */
public class SelectPayWayActivity extends Activity implements View.OnClickListener, Alipay.PaySucessOrNot {
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
    private Button button;

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
        orderConDate.setText("" + DateUtil.shortDateString(orderModel.getDate()));
        orderConNum.setText("" + Integer.toString(orderModel.getPeopleNumber()) + "人");
        orderPay.setText("" + (int) Math.floor(orderModel.getTotalPrice()));
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
        button = ViewUtil.findViewById(this, R.id.zfbzfu);


    }

    private void initEvent() {
        topView.setTitleText("支付");
        topView.setTitleBackVisiable();
        findViewById(R.id.pay_ipay).setOnClickListener(this);
        findViewById(R.id.pay_yinlian).setOnClickListener(this);
        button.setOnClickListener(this);
    }

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final String CHANNEL_UPMP = "upmp";
    private static final String CHANNEL_ALIPAY = "alipay";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_ipay:
                // DialogUtil.showMessage("支付宝支付哦！！！");
                findViewById(R.id.pay_ipay).setOnClickListener(null);
                Alipay alipay = new Alipay(this, orderModel, this);
                alipay.pay();
                break;
            case R.id.pay_yinlian:
                //  DialogUtil.showMessage("银联支付哦！！！");
                findViewById(R.id.pay_yinlian).setOnClickListener(null);
                ChargeModel.OrderToPay(orderModel.getId(), CHANNEL_UPMP).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        if (jsonObject.optBoolean("success")) {
                            String charge = jsonObject.optString("charge");
                            Log.d("charge", charge);
                            Intent intent = new Intent();
                            String packageName = getPackageName();
                            ComponentName componentName = new ComponentName(packageName, "com.pingplusplus.android.PaymentActivity");
                            intent.setComponent(componentName);
                            intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
                            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                        }
                    }
                });
                break;
            case R.id.title_back_img:
                finish();
                break;
            case R.id.zfbzfu:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        findViewById(R.id.pay_ipay).setOnClickListener(this);
        findViewById(R.id.pay_yinlian).setOnClickListener(this);

        //支付页面返回处理
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                if ("success".equals(result)) {
                    Intent intent = new Intent(this, PaySuccessActivity.class);
                    intent.putExtra("order", orderModel);
                    ViewUtil.startActivity(this, intent);
                    finish();
                } else if ("cancel".equals(result)) {
                    DialogUtil.showMessage("支付取消");
                } else if ("fail".equals(result)) {
                    DialogUtil.showMessage("支付失败");
                }
                //   showMsg(result, errorMsg, extraMsg);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // showMsg("User canceled", "", "");
                DialogUtil.showMessage("支付失败");
            }
        }
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new Builder(this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    @Override
    public void payStatus(String status, boolean isPay) {
        findViewById(R.id.pay_ipay).setOnClickListener(this);
        if (isPay) {
            if ("sucess".equals(status)) {
                Intent intent;
                if ("GROUP".equals(orderModel.getType())) {
                    intent = new Intent(context, SelectPayWayActivity.class);
                } else {
                    intent = new Intent(this, PaySuccessActivity.class);
                }
                intent.putExtra("order", orderModel);
                ViewUtil.startActivity(this, intent);
                finish();
            } else if ("process".equals(status)) {
                DialogUtil.showMessage("支付结果确认中");
            } else if ("fail".equals(status)) {
                DialogUtil.showMessage("支付失败");
            }
        }
    }
}
