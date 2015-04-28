package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by dengt on 15-4-21.
 */
public class UserCenterActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private TextView myOrder;
    private TextView myCompe;
    private TextView myMessage;
    private TextView myPerson;
    private TextView myInvite;
    private TextView logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
        initView();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_usercenter_layout);
        myOrder = ViewUtil.findViewById(this, R.id.my_order);
        myCompe = ViewUtil.findViewById(this, R.id.my_competition);
        myMessage = ViewUtil.findViewById(this, R.id.my_message);
        myPerson = ViewUtil.findViewById(this, R.id.my_personal);
        myInvite = ViewUtil.findViewById(this, R.id.my_invitation);
        logoutBtn = ViewUtil.findViewById(this, R.id.logout_btn);
    }

    private void initEvent() {
        topView.setTitleText("个人中心");
        topView.setActivity(this);
        myOrder.setOnClickListener(this);
        myCompe.setOnClickListener(this);
        myMessage.setOnClickListener(this);
        myPerson.setOnClickListener(this);
        myInvite.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.my_order:
                intent = new Intent(this, MyOrderActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.my_competition:
                break;
            case R.id.my_message:
                break;
            case R.id.my_personal:
                break;
            case R.id.my_invitation:
                break;
            case R.id.logout_btn:
                intent = new Intent(this, LoginActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
        }
    }
}
