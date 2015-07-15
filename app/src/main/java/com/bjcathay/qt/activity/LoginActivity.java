
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.ClearEditText;
import com.igexin.sdk.PushManager;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-4-23.
 */
public class LoginActivity extends Activity implements View.OnClickListener, ICallback {
    private GApplication gApplication;
    private Button loginbtn;
    private ImageView topView;
    private TextView newlogin;
    private TextView forgetbtn;
    private ClearEditText loginUser;
    private ClearEditText loginpwd;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gApplication = GApplication.getInstance();
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityY) < 100) {
                    return true;
                }

                if ((e2.getRawY() - e1.getRawY()) > 200) {
                }
                if ((e1.getRawY() - e2.getRawY()) < 0) {
                    finish();
                    overridePendingTransition(R.anim.activity_close, R.anim.activity_close);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        initView();
        initEvent();
        initData();

    }

    private void initView() {
        loginbtn = ViewUtil.findViewById(this, R.id.login_btn);
        topView = ViewUtil.findViewById(this, R.id.top_login_layout);
        newlogin = ViewUtil.findViewById(this, R.id.new_login);
        forgetbtn = ViewUtil.findViewById(this, R.id.forget_secrete);
        loginUser = ViewUtil.findViewById(this, R.id.login_user);
        loginpwd = ViewUtil.findViewById(this, R.id.login_pwd);

    }

    private void initEvent() {
        loginbtn.setOnClickListener(this);
        newlogin.setOnClickListener(this);
        forgetbtn.setOnClickListener(this);
    }

    private void initData() {
        String user_name = PreferencesUtils.getString(gApplication, PreferencesConstant.USER_NAME,
                "");
        String pass_word = PreferencesUtils.getString(gApplication,
                PreferencesConstant.USER_PASSWORD, "");
        loginUser.setText(user_name);
        loginpwd.setText(pass_word);
    }

    private void login() {
        String user = loginUser.getText().toString();
        String password = loginpwd.getText().toString();
        if (user.isEmpty() || password.isEmpty()) {
            DialogUtil.showMessage("用户名或密码不能为空");
            return;
        }
        UserModel.login(user, password).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.login_btn:
                login();
                break;
            case R.id.new_login:
                intent = new Intent(this, RegisterActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.forget_secrete:
                intent = new Intent(this, ForgetPwdActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.top_login_layout:
                finish();
                overridePendingTransition(R.anim.activity_close, R.anim.activity_close);
                break;
        }
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
            UserModel userModel = JSONUtil.load(UserModel.class, jsonObject.optJSONObject("user"));
            String token = userModel.getApiToken();
            // 保存用户名和密码
            PreferencesUtils.putString(gApplication, PreferencesConstant.NICK_NAME,
                    userModel.getNickname());
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_PHONE,
                    userModel.getMobileNumber());
            PreferencesUtils.putInt(gApplication, PreferencesConstant.VALIDATED_USER,
                    userModel.getInviteAmount());
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_NAME, loginUser
                    .getText().toString().trim());
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_PASSWORD, loginpwd
                    .getText().toString().trim());
            PreferencesUtils.putString(gApplication, PreferencesConstant.INVITE_CODE,
                    userModel.getInviteCode());

            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
            gApplication.setUser(userModel);
            gApplication.updateApiToken();
            DialogUtil.showMessage("登录成功");
            UserModel.updateUserInfo(null, null, PushManager.getInstance().getClientid(this), null,
                    null).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                }
            });
            finish();
            overridePendingTransition(R.anim.activity_close, R.anim.activity_close);
        } else {
            String errorMessage = jsonObject.optString("message");
            if (!StringUtils.isEmpty(errorMessage))
                DialogUtil.showMessage(errorMessage);
            else {
                int code = jsonObject.optInt("code");
                DialogUtil.showMessage(ErrorCode.getCodeName(code));
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("登录页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("登录页面");
        MobclickAgent.onPause(this);
    }
}
