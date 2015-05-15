package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by bjcathay on 15-5-14.
 */
public class OrderSucTuanActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sucess);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_order_tuan_layout);
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
    }

    private void initEvent() {
        topView.setActivity(this);
        topView.setTitleText("约场");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.order_suc_first:
                intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra("id", id);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.order_suc_second:
                /*intent = new Intent(this, PlaceListActivity.class);
                intent.putExtra("id", id);
                ViewUtil.startActivity(this, intent);*/
                DialogUtil.showMessage("分享给好友");
                break;
        }
    }
}
