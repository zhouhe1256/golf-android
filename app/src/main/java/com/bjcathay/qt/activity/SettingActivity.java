package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.uptutil.DownloadManager;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.IsLoginUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by bjcathay on 15-5-14.
 */
public class SettingActivity extends Activity implements View.OnClickListener {
    private Button logoutBtn;
    private GApplication gApplication;
    private TopView topView;
    private boolean shareFlag;
    private RelativeLayout shareLayout;


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
        shareLayout = ViewUtil.findViewById(this, R.id.setting_share);
        if (gApplication.isLogin()) {
            logoutBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initEvent() {
        logoutBtn.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
    }

    private void initDate() {
        topView.setTitleBackVisiable();
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
                shareLayout.setOnClickListener(null);
                ShareModel.share().done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        ShareModel shareModel = arguments.get(0);
                        ShareUtil.getInstance().shareDemo(SettingActivity.this, shareModel);
                        shareLayout.setOnClickListener(SettingActivity.this);
                    }
                });
                break;
            case R.id.setting_change_pwd:
                intent = new Intent(this, EditPwdActivity.class);
                IsLoginUtil.isLogin(this, intent);
                //  DialogUtil.showMessage("share");
                break;
            case R.id.setting_feedback:
                intent = new Intent(this, FeedbackActivity.class);
                ViewUtil.startActivity(this, intent);
                // DialogUtil.showMessage("share");
                break;
            case R.id.setting_about:
                intent = new Intent(this, AboutActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.setting_update:
                DownloadManager downManger = new DownloadManager(this, true);
                downManger.checkDownload();
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }
}