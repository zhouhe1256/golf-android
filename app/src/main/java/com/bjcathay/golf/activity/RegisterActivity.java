package com.bjcathay.golf.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by bjcathay on 15-4-23.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_register_layout);

    }

    private void initEvent() {
        topView.setTitleText("注册");
        topView.setActivity(this);

    }

    @Override
    public void onClick(View view) {

    }
}
