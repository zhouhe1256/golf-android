package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;


import com.bjcathay.qt.R;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * created by jiangm on 15-9-15
 */
public class AboutWalletActivity extends Activity implements View.OnClickListener{
private TopView topView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_wallet);
        topView = ViewUtil.findViewById(this, R.id.top_my_wallet_layout);

        topView.setTitleBackVisiable();

        topView.setTitleText("关于钱包余额");
    }

    @Override
    public void onClick(View view) {

        if (ClickUtil.isFastClick()) {
            return;
        }
        switch(view.getId()){
            case R.id.title_back_img:
                finish();
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("关于钱包余额");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关于钱包余额");
        MobclickAgent.onPause(this);
    }
}
