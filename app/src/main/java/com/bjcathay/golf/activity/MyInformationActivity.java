package com.bjcathay.golf.activity;

import android.app.Activity;
import android.os.Bundle;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by bjcathay on 15-4-29.
 */
public class MyInformationActivity extends Activity {
    private TopView topView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        initView();
        initEvent();
    }
    private void initView(){
        topView= ViewUtil.findViewById(this, R.id.top_my_information_layout);
    }
    private void initEvent(){
        topView.setActivity(this);
        topView.setTitleText("个人资料");
    }
}
