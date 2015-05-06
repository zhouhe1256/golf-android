package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by dengt on 15-4-21.
 */
public class UserCenterActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private LinearLayout myOrder;
    private LinearLayout myCompe;
    private LinearLayout myMessage;
    private LinearLayout myPerson;
    private LinearLayout myExchange;
    private Button logoutBtn;

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
        myExchange = ViewUtil.findViewById(this, R.id.my_exchange);
        logoutBtn = ViewUtil.findViewById(this, R.id.logout_btn);
    }

    private void initEvent() {
        topView.setTitleText("个人中心");
        topView.setActivity(this);
        myOrder.setOnClickListener(this);
        myCompe.setOnClickListener(this);
        myMessage.setOnClickListener(this);
        myPerson.setOnClickListener(this);
        myExchange.setOnClickListener(this);
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
                intent = new Intent(this, MyCompetitionActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.my_message:
                intent = new Intent(this, MyMessageActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.my_personal:
                intent = new Intent(this, MyInformationActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.my_exchange:
                intent = new Intent(this, MyExchangeActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.logout_btn:
                intent = new Intent(this, LoginActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
        }
    }
}
