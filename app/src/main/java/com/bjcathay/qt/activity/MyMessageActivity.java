
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by dengt on 15-7-23.
 */
public class MyMessageActivity extends Activity implements View.OnClickListener {
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_my_message_layout);
        topView.setTitleBackVisiable();
        topView.setTitleText("我的消息");
    }

    private void initEvent() {
    }

    private void initData() {
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.my_order_message:
                intent = new Intent(this, MyOrderMessageActivity.class);
                ViewUtil.startActivity(this, intent);
                PreferencesUtils.putBoolean(this, PreferencesConstant.NEW_MESSAGE_FLAG, false);
                break;
            case R.id.my_activity_message:
                intent = new Intent(this, MyEventMessageActivity.class);
                ViewUtil.startActivity(this, intent);
                // DialogUtil.showMessage("活动");
                break;
            case R.id.my_notification_message:
                intent = new Intent(this, MyNtfMessageActivity.class);
                ViewUtil.startActivity(this, intent);
                // DialogUtil.showMessage("通知");
                break;
            case R.id.my_wallet_message:
                intent = new Intent(this, MyWalletMessageActivity.class);
                ViewUtil.startActivity(this, intent);
                // DialogUtil.showMessage("资产");
                break;
        }
    }
}
