package com.bjcathay.qt.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bjcathay.android.remote.Http;
import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.wxpay.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.acvitity_pay_success);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            LogUtil.d(TAG, "onPayFinish, errCode = " + resp.errCode);

            String msg = "";

            if (resp.errCode == 0) {
                msg = "支付成功";
                Intent intent = new Intent();
                intent.setAction("WXPAY");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                finish();
            } else if (resp.errCode == -1) {
                msg = "支付失败";
                finish();
            } else if (resp.errCode == -2) {
                msg = "用户已取消";
                finish();
            }
            DialogUtil.showMessage(msg);

          /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("微信支付结果" + resp.errStr + ";code=" + String.valueOf(resp.errCode));
            builder.show();*/
        }
    }
}