
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-6-24.
 */
public class StadiumSearchActivity extends Activity implements View.OnClickListener {
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        GApplication.getInstance().setFlag(false);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_search_layout);
        topView.setTitleText("输入关键字");
        topView.setTitleBackVisiable();
    }

    private void initEvent() {
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.select_city:
                intent = new Intent(this, CitySelectActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.input_place:
                intent = new Intent(this, KeyWordSearchActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.select_sure:
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        GApplication.getInstance().setFlag(false);
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("输入关键字页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("输入关键字页面");
        MobclickAgent.onPause(this);
    }
}
