package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by bjcathay on 15-4-23.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private Button loginbtn;
    private TopView topView;
    private TextView newlogin;
    private TextView forgetbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    private void initView() {
        loginbtn = ViewUtil.findViewById(this, R.id.login_btn);
        topView = ViewUtil.findViewById(this, R.id.top_login_layout);
        newlogin = ViewUtil.findViewById(this, R.id.new_login);
        forgetbtn = ViewUtil.findViewById(this, R.id.forget_secrete);

    }

    private void initEvent() {
        topView.setTitleText("登录");
        topView.setActivity(this);
        loginbtn.setOnClickListener(this);
        newlogin.setOnClickListener(this);
        forgetbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.login_btn:
                intent = new Intent(this, UserCenterActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.new_login:
                intent = new Intent(this, RegisterActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.forget_secrete:
                break;
        }
    }
}
