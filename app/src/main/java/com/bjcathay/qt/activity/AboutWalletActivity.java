package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

public class AboutWalletActivity extends Activity implements View.OnClickListener{
private TopView topView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_wallet);
        topView = ViewUtil.findViewById(this, R.id.top_my_wallet_layout);

        topView.setTitleBackVisiable();

        topView.setTitleText("关于钱包余额");
    }

    @Override
    public void onClick(View view) {

        if (ClickUtil.isFastClick()) {
            return;
        }
        switch(view.getId()){
            case R.id.title_back_img:
                finish();
                break;
        }
    }
}
