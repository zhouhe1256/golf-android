package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.TimeCount;
import com.bjcathay.qt.util.ValidformUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.ClearEditText;
import com.bjcathay.qt.view.TopView;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-5-15.
 */
public class ForgetPwdActivity extends Activity implements View.OnClickListener, ICallback, TimeCount.TimeUpdate {
    private GApplication gApplication;
    private ClearEditText userPhone;
    private ClearEditText userCode;
    private Button registerBtn;
    private TextView userCodeBtn;
    private TimeCount time;
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        gApplication = GApplication.getInstance();
        initView();
        initDate();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_forget_layout);
        userPhone = ViewUtil.findViewById(this, R.id.register_user_phone);
        userCode = ViewUtil.findViewById(this, R.id.register_user_code);
        registerBtn = ViewUtil.findViewById(this, R.id.register_btn);
        userCodeBtn = ViewUtil.findViewById(this, R.id.register_get_code_btn);


    }

    private void initDate() {
        time = new TimeCount(60000, 1000, this);
    }

    private void initEvent() {
        //topView.setActivity(this);
        topView.setVisiable(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
        topView.setOnClickListener(this);
        userPhone.setOnClickListener(this);
        userCode.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        userCodeBtn.setOnClickListener(this);


    }

    private void sendCheckCode() {
        String phone = userPhone.getText().toString().trim();
        if (phone.length() == 0) {
            DialogUtil.showMessage("请输入手机号码");
            return;
        }
        if (!ValidformUtil.isMobileNo(phone)) {
            DialogUtil.showMessage("请填写正确的手机号码");
            return;
        }
        if (phone.length() > 0) {
            time.start();
            UserModel.sendCheckCode(phone, "FORGET_PWD").done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                    } else {
                        DialogUtil.showMessage("验证码发送失败");
                        time.onFinish();
                    }
                }
            });
        }
    }

    private void forget() {
        String phone = userPhone.getText().toString().trim();

        String code = userCode.getText().toString().trim();
        if (phone.length() == 0) {
            DialogUtil.showMessage("请输入手机号码");
            return;
        }
        if (!ValidformUtil.isMobileNo(phone)) {
            DialogUtil.showMessage("请填写正确的手机号码");
            return;
        }
        if (phone.length() > 0 && code.length() > 0)
            UserModel.verifyCheckCode(phone, code, "FORGET_PWD").done(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_get_code_btn:
                //获取验证码
                sendCheckCode();
                break;
            case R.id.register_btn:
                //下一步
                forget();
                break;
            case R.id.top_forget_layout:
                finish();
                break;
        }
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
          /*  UserModel userModel = arguments.get(0);
            if (userModel.getMobileNumber() != null) {*/
            Intent intent = new Intent(this, ForgetSetNewPwdActivity.class);
            intent.putExtra("phone", userPhone.getText().toString().trim());
            intent.putExtra("code", userCode.getText().toString().trim());
            ViewUtil.startActivity(this, intent);
            //  DialogUtil.showMessage("验证成功");
            // }
        } else {
            int code = jsonObject.optInt("code");
            DialogUtil.showMessage(ErrorCode.getCodeName(code));
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        userCodeBtn.setText("重新获取还需" + (millisUntilFinished / 1000) + "秒");
        userCodeBtn.setClickable(false);
    }

    @Override
    public void onFinish() {
        userCodeBtn.setText("获取验证码");
        userCodeBtn.setClickable(true);
    }
}