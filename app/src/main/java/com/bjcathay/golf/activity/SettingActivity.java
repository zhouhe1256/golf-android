package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bjcathay.golf.R;
import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.PreferencesConstant;
import com.bjcathay.golf.util.PreferencesUtils;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by bjcathay on 15-5-14.
 */
public class SettingActivity extends Activity implements View.OnClickListener {
    private Button logoutBtn;
    private GApplication gApplication;
    private TopView topView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        gApplication = GApplication.getInstance();
        initView();
        initDate();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_setting_layout);
        logoutBtn = ViewUtil.findViewById(this, R.id.logout_btn);
    }

    private void initEvent() {
        logoutBtn.setOnClickListener(this);
    }

    private void initDate() {
        topView.setActivity(this);
        topView.setTitleText("设置");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.logout_btn:
               /* intent = new Intent(this, LoginActivity.class);
                ViewUtil.startActivity(this, intent);*/
                PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, "");
                gApplication.updateApiToken();
                DialogUtil.showMessage("退出成功");
                intent = new Intent(this, MainActivity.class);
                ViewUtil.startTopActivity(this, intent);
                break;
            case R.id.setting_share:
                DialogUtil.showMessage("share");
                break;
            case R.id.setting_change_pwd:
                intent = new Intent(this, EditPwdActivity.class);
                ViewUtil.startTopActivity(this, intent);
              //  DialogUtil.showMessage("share");
                break;
            case R.id.setting_feedback:
                intent = new Intent(this, FeedbackActivity.class);
                ViewUtil.startTopActivity(this, intent);
               // DialogUtil.showMessage("share");
                break;
            case R.id.setting_about:
                DialogUtil.showMessage("share");
                break;
        }
    }
}
