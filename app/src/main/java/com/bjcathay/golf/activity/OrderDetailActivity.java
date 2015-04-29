package com.bjcathay.golf.activity;

import android.app.Activity;
import android.os.Bundle;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by bjcathay on 15-4-29.
 */
public class OrderDetailActivity extends Activity {
    private TopView topView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        initEvent();
    }
    private void initView(){
        topView= ViewUtil.findViewById(this, R.id.top_order_detail_layout);
    }
    private void initEvent(){
        topView.setActivity(this);
        topView.setTitleText("订单详情");
    }
}
