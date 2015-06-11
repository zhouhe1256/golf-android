package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.fragment.DialogExchFragment;
import com.bjcathay.qt.model.PropModel;
import com.bjcathay.qt.model.UserListModle;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.ClearEditText;
import com.bjcathay.qt.view.TopView;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-4-29.
 */
public class SendFriendActivity extends FragmentActivity implements View.OnClickListener, DialogExchFragment.ExchangeResult {
    private FragmentActivity context;
    private TopView topView;
    private RelativeLayout sendToList;
    private RelativeLayout inputNumber;
    private Long id;
    private String proName;
    private String number;
    private DialogExchFragment dialogExchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_friend);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        proName=intent.getStringExtra("name");
       // number = inputNumber.getText().toString().trim();
        dialogExchFragment = new DialogExchFragment(this, this);
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_send_friend_layout);
        sendToList = ViewUtil.findViewById(this, R.id.send_to_mail_list);
        inputNumber = ViewUtil.findViewById(this, R.id.send_input_number);
    }

    private void initEvent() {
        topView.setTitleText("赠送好友");
        topView.setTitleBackVisiable();
        sendToList.setOnClickListener(this);
        inputNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.send_to_mail_list:
                intent = new Intent(this, ContactActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name",proName);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.send_input_number:
                intent = new Intent(this, SendToPhoneActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name",proName);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    @Override
    public void exchangeResult(UserModel userModel, boolean isExchange) {

    }
}
