package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.golf.R;
import com.bjcathay.golf.constant.ErrorCode;
import com.bjcathay.golf.model.UserModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.PreferencesConstant;
import com.bjcathay.golf.util.PreferencesUtils;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.ClearEditText;
import com.bjcathay.golf.view.TopView;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-5-15.
 */
public class ForgetSetNewPwdActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private ClearEditText newPwd;
    private ClearEditText surePwd;
    private String phone;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_new_pwd);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_edit_pwd_layout);
        newPwd = ViewUtil.findViewById(this, R.id.edit_new_pwd);
        surePwd = ViewUtil.findViewById(this, R.id.edit_sure_new_pwd);
    }

    private void initData() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        code = intent.getStringExtra("code");
    }

    private void initEvent() {
        topView.setActivity(this);
        topView.setTitleText("设置新密码");

    }

    private void edit() {
        String pwd1 = newPwd.getText().toString();
        String pwd2 = surePwd.getText().toString();
        if (pwd1.isEmpty() || pwd2.isEmpty()) {
            DialogUtil.showMessage("密码不能为空");
            return;
        }

        if (!pwd1.equals(pwd2)) {
            DialogUtil.showMessage("两次输入的密码不一样");
            return;
        }

        UserModel.resetPassword(phone, newPwd.getText().toString().trim(), code).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                if (jsonObject.optBoolean("success")) {
                    DialogUtil.showMessage("密码设置成功");
                    PreferencesUtils.putString(ForgetSetNewPwdActivity.this, PreferencesConstant.USER_PASSWORD, newPwd.getText().toString().trim());

                    finish();
                } else {
                    int code = jsonObject.optInt("code");
                    DialogUtil.showMessage(ErrorCode.getCodeName(code));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_sure_btn:
                edit();
                break;

        }
    }
}