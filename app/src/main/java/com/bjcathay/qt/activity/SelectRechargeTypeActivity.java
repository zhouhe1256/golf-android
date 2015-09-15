package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.view.View;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.alipay.Alipay;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.MoneyModel;
import com.bjcathay.qt.model.RechargeModel;
import com.bjcathay.qt.model.WXPayModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.bjcathay.qt.wxpay.WXpay;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by jiangm on 15-9-14
 */
public class SelectRechargeTypeActivity extends Activity implements View.OnClickListener,
        Alipay.PaySucessOrNot  {
    private TopView topView;
   private RechargeModel rechargeModel;
    private MoneyModel moneyModel;

    private Activity activity;
    private WXRECHARGEBroadcastReceiver wxrechargeBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recharge_type);
        activity=this;
        GApplication.getInstance().setFlag(false);
        topView = ViewUtil.findViewById(this, R.id.top_pay_detail_layout);
        topView.setTitleBackVisiable();
        topView.setTitleText("选择充值类型");
        initData();
        initReceiver();
    }

    public void initData(){
        Intent intent= getIntent();
        moneyModel=(MoneyModel)intent.getSerializableExtra("money");
        rechargeModel=new RechargeModel();
        rechargeModel.setChargeId(moneyModel.getId());

    }
    private void initReceiver() {
        wxrechargeBroadcastReceiver= new WXRECHARGEBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WXPAY");
        this.registerReceiver(wxrechargeBroadcastReceiver, intentFilter);
    }
    @Override
    public void onClick(View view) {

        if (ClickUtil.isFastClick()) {
            return;
        }

        switch (view.getId()){
            case R.id.rec_wx:
               findViewById(R.id.rec_wx).setOnClickListener(null);
                WXpay wXpay = new WXpay(this, rechargeModel);
                wXpay.wxRecharge();
                break;
            case R.id.rec_ipay:
                findViewById(R.id.rec_ipay).setOnClickListener(null);
                WXPayModel.rechargetext(rechargeModel.getChargeId(), "alipay").done(
                        new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                if (jsonObject.optBoolean("success")) {
                                    String pay = jsonObject.optString("prepay");
                                    String orderId=jsonObject.optString("orderId");
                                    if (Double.valueOf(pay) > 0) {
                                        Alipay alipay = new Alipay(activity, rechargeModel,
                                                SelectRechargeTypeActivity.this);
                                        alipay.recharge(pay,orderId);
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
            case R.id.title_back_img:
                finish();
                break;
        }

    }

    @Override
    public void payStatus(String status, boolean isPay) {
        findViewById(R.id.rec_ipay).setOnClickListener(this);
        if (isPay) {
            if ("sucess".equals(status)) {
                DialogUtil.showMessage("充值成功");
                Intent intent= new Intent(activity,RechargeSuccessActivity.class);
                intent.putExtra("flag","alipay");
                intent.putExtra("money",moneyModel);
                ViewUtil.startActivity(activity,intent);
                finish();
            } else if ("process".equals(status)) {
                DialogUtil.showMessage("充值结果确认中");
                finish();
            } else if ("fail".equals(status)) {
                DialogUtil.showMessage("充值失败");

               /* Intent intentS= new Intent(activity,RechargeSuccessActivity.class);
                intentS.putExtra("flag","wx");
                intentS.putExtra("money",moneyModel);
                ViewUtil.startActivity(activity,intentS);
                finish();*/

            }
        }
    }

    private class WXRECHARGEBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            activity.findViewById(R.id.rec_wx).setOnClickListener(SelectRechargeTypeActivity.this);

            if ("WXPAY".equals(action)) {
                activity.findViewById(R.id.rec_wx).setOnClickListener(SelectRechargeTypeActivity.this);
                                String tag = intent.getStringExtra("tag");
                if ("sucess".equals(tag)) {
                    //DialogUtil.showMessage("充值成功");
                    Intent intentS= new Intent(activity,RechargeSuccessActivity.class);
                    intentS.putExtra("flag","wx");
                    intentS.putExtra("money",moneyModel);
                    ViewUtil.startActivity(activity,intentS);
                    finish();

                } else {
//                    Intent intentF= new Intent(activity,RechargeSuccessActivity.class);
//                    intentF.putExtra("flag","fail");
//                    ViewUtil.startActivity(activity,intentF);
                 /*   Intent intentS= new Intent(activity,RechargeSuccessActivity.class);
                    intentS.putExtra("flag","wx");
                    intentS.putExtra("money",moneyModel);
                    ViewUtil.startActivity(activity,intentS);*/
           //  finish();
                }

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择充值类型");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wxrechargeBroadcastReceiver != null) {
            this.unregisterReceiver(wxrechargeBroadcastReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GApplication.getInstance().setFlag(false);
        MessageReceiver.baseActivity = this;
        MobclickAgent.onPageStart("选择充值类型");
        MobclickAgent.onResume(this);
        activity.findViewById(R.id.rec_wx).setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            }
}
