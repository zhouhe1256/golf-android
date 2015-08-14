
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-7-23.
 */
public class MyWalletActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private TextView wallet_remain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        topView = ViewUtil.findViewById(this, R.id.top_my_wallet_layout);
        wallet_remain = ViewUtil.findViewById(this, R.id.wallet_remain);
        topView.setTitleBackVisiable();
        topView.setTitleText("我的钱包");
        initDate();

        // wallet_remain.setText((int)
        // Math.floor(getIntent().getDoubleExtra("balance", 0l)) + "");

    }

    private void initDate() {
        UserModel.get().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {

                UserModel userModel = arguments.get(0);
                GApplication.getInstance().setUser(userModel);
                PreferencesUtils.putInt(MyWalletActivity.this, PreferencesConstant.VALIDATED_USER,
                        userModel.getInviteAmount());
                PreferencesUtils.putString(MyWalletActivity.this, PreferencesConstant.INVITE_CODE,
                        userModel.getInviteCode());
                wallet_remain.setText((int) Math.floor(userModel.getBalance()) + "");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("我的钱包");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的钱包");
        MobclickAgent.onPause(this);
    }
}
