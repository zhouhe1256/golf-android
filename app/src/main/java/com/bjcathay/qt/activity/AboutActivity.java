package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.util.SystemUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-5-26.
 */
public class AboutActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        topView = ViewUtil.findViewById(this, R.id.top_about_layout);
        version = ViewUtil.findViewById(this, R.id.app_version);
        topView.setTitleBackVisiable();
        topView.setTitleText("关于七铁");
        version.setText(SystemUtil.getCurrentVersionName(GApplication.getInstance()));
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
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
