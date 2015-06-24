package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by bjcathay on 15-6-23.
 */
public class SearchActivity extends Activity implements View.OnClickListener {
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_search_layout);
        topView.setTitleText("搜索球场");
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
                //  intent = new Intent(this, SendToPhoneActivity.class);
                //  ViewUtil.startActivity(this, intent);
                break;
            case R.id.select_sure:
                break;
        }
    }
}
