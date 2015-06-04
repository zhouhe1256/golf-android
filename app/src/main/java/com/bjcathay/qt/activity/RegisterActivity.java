package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.TimeCount;
import com.bjcathay.qt.util.ValidformUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.ClearEditText;
import com.bjcathay.qt.view.TopView;
import com.igexin.sdk.PushManager;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-4-23.
 */
public class RegisterActivity extends Activity implements View.OnClickListener, ICallback, TimeCount.TimeUpdate {
    private GApplication gApplication;
    private ClearEditText userPhone;
    private ClearEditText userPwd;
    private ClearEditText userCode;
    private ClearEditText userInvite;
    private Button registerBtn;
    private TextView userCodeBtn;
    private TimeCount time;

    private ImageView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        gApplication = GApplication.getInstance();
        initView();
        initDate();
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
        //topView.setActivity(this);
        topView.setOnClickListener(this);
        userPhone.setOnClickListener(this);
        userPwd.setOnClickListener(this);
        userCode.setOnClickListener(this);
        userInvite.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        userCodeBtn.setOnClickListener(this);


    }

    private void initDate() {
        time = new TimeCount(60000, 1000, RegisterActivity.this);
    }

    private void sendCheckCode() {
        String phone = userPhone.getText().toString().trim();
        if (!ValidformUtil.isMobileNo(phone)) {
            DialogUtil.showMessage("请填写正确的手机号码");
            return;
        }
        if (phone.length() > 0) {
            time.start();
            UserModel.sendCheckCode(phone, "REGISTER").done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        DialogUtil.showMessage("验证码已发送");
                    } else {
                        time.cancel();
                        userCodeBtn.setText("获取验证码");
                        userCodeBtn.setClickable(true);
                        int code = jsonObject.optInt("code");
                        DialogUtil.showMessage(ErrorCode.getCodeName(code));
                    }


                }
            });
        }
    }

    private void register() {
        String phone = userPhone.getText().toString().trim();
        String password = userPwd.getText().toString().trim();
        String code = userCode.getText().toString().trim();
        String inviteCode = userInvite.getText().toString().trim();
        if (phone.length() == 0) {
            DialogUtil.showMessage("请输入手机号码");
            return;
        }
        if (password.length() >= 6 && password.length() <= 18) {
            if (phone.length() > 0 && password.length() > 0 && code.length() > 0)
                UserModel.register(phone, password, code, inviteCode).done(this);
        } else {
            DialogUtil.showMessage("密码长度必须大于6位小于18位");
        }
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
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
          /*  UserModel userModel = arguments.get(0);
            if (userModel.getMobileNumber() != null) {*/
            UserModel userModel = JSONUtil.load(UserModel.class, jsonObject.optJSONObject("user"));
            // userModel.setCurrentUser(userModel);
            String token = userModel.getApiToken();
            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
            gApplication.updateApiToken();
            DialogUtil.showMessage("注册成功");
            UserModel.updateUserInfo(null, null, PushManager.getInstance().getClientid(this), null, null).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                }
            });
            Intent intent = new Intent(this, MainActivity.class);
            ViewUtil.startTopActivity(this, intent);
            // }
        } else {
            int code = jsonObject.optInt("code");
            DialogUtil.showMessage(ErrorCode.getCodeName(code));
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        userCodeBtn.setText((millisUntilFinished / 1000) + "秒后重发");
        userCodeBtn.setClickable(false);
    }

    @Override
    public void onFinish() {
        userCodeBtn.setText("获取验证码");
        userCodeBtn.setClickable(true);
    }
}
