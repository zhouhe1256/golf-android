package com.bjcathay.golf.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.golf.R;
import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.model.UserModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.ClearEditText;
import com.bjcathay.golf.view.TopView;

/**
 * Created by bjcathay on 15-4-23.
 */
public class RegisterActivity extends Activity implements View.OnClickListener, ICallback {
    private GApplication gApplication;
    private ClearEditText userPhone;
    private ClearEditText userPwd;
    private ClearEditText userCode;
    private ClearEditText userInvite;
    private Button registerBtn;
    private TextView userCodeBtn;

    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        gApplication = GApplication.getInstance();
        initView();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_register_layout);
        userPwd = ViewUtil.findViewById(this, R.id.register_user_pwd);
        userPhone = ViewUtil.findViewById(this, R.id.register_user_phone);
        userCode = ViewUtil.findViewById(this, R.id.register_user_code);
        userInvite = ViewUtil.findViewById(this, R.id.register_user_invite_code);
        registerBtn = ViewUtil.findViewById(this, R.id.register_btn);
        userCodeBtn = ViewUtil.findViewById(this, R.id.register_get_code_btn);


    }

    private void initEvent() {
        topView.setTitleText("返回");
        //topView.setActivity(this);
        topView.setVisiable(View.INVISIBLE,View.VISIBLE,View.INVISIBLE);
        topView.setOnClickListener(this);
        userPhone.setOnClickListener(this);
        userPwd.setOnClickListener(this);
        userCode.setOnClickListener(this);
        userInvite.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        userCodeBtn.setOnClickListener(this);


    }

    private void sendCheckCode() {
        String phone = userPhone.getText().toString().trim();
        if (phone.length() > 0)
            UserModel.sendCheckCode(phone, "REGISTER").done(new ICallback() {
                @Override
                public void call(Arguments arguments) {

                }
            });
    }

    private void register() {
        String phone = userPhone.getText().toString().trim();
        String password = userPhone.getText().toString().trim();
        String code = userPhone.getText().toString().trim();
        String inviteCode = userPhone.getText().toString().trim();
        if (phone.length() > 0 && password.length() > 0 && code.length() > 0)
            UserModel.register(phone, password, code, inviteCode).done(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_get_code_btn:
                //获取验证码
                sendCheckCode();
                break;
            case R.id.register_btn:
                //注册
                register();
                break;
            case R.id.top_register_layout:
                finish();
                break;
        }
    }

    @Override
    public void call(Arguments arguments) {
        UserModel userModel = arguments.get(0);
        if (userModel.getMobileNumber() != null) {
            DialogUtil.showMessage("注册成功");
        }
    }
}
