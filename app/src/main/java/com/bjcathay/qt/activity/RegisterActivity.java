
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.bjcathay.qt.util.TimeCount;
import com.bjcathay.qt.util.ValidformUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.ClearEditText;
import com.igexin.sdk.PushManager;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-4-23.
 */
public class RegisterActivity extends Activity implements View.OnClickListener, ICallback,
        TimeCount.TimeUpdate {
    private GApplication gApplication;
    private ClearEditText userPhone;
    private ClearEditText userPwd;
    private ClearEditText userCode;
    private ClearEditText userInvite;
    private Button registerBtn;
    private TextView userCodeBtn;
    private TimeCount time;
    private GestureDetector mGestureDetector;
    private ImageView topView;

    private TextView inviteNoteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        gApplication = GApplication.getInstance();
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if (Math.abs(velocityY) < 100) {
                    return true;
                }

                // 手势向下 down
                if ((e2.getRawY() - e1.getRawY()) > 200) {
                }
                // 手势向上 up
                if ((e1.getRawY() - e2.getRawY()) < 0) {
                    finish();
                    overridePendingTransition(R.anim.activity_close, R.anim.activity_close);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
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
        inviteNoteView = ViewUtil.findViewById(this, R.id.invite_note);
    }

    private void initEvent() {
        userPhone.setOnClickListener(this);
        userPwd.setOnClickListener(this);
        userCode.setOnClickListener(this);
        userInvite.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        userCodeBtn.setOnClickListener(this);

        userInvite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!StringUtils.isEmpty(String.valueOf(userInvite.getText()))) {
                    inviteNoteView.setVisibility(View.GONE);
                } else {
                    inviteNoteView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
            userCodeBtn.setClickable(false);
            UserModel.sendCheckCode(phone, "REGISTER").done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        time.start();
                    } else {
                        time.cancel();
                        userCodeBtn.setText("获取验证码");
                        userCodeBtn.setClickable(true);
                        String errorMessage = jsonObject.optString("message");
                        if (!StringUtils.isEmpty(errorMessage))
                            DialogUtil.showMessage(errorMessage);
                        else {
                            int code = jsonObject.optInt("code");
                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    userCodeBtn.setClickable(true);
                    DialogUtil.showMessage("网络出现故障");
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
        if (code.length() == 0) {
            DialogUtil.showMessage("请输入验证码");
        }
        if (password.length() >= 6 && password.length() <= 18) {
            if (phone.length() > 0 && password.length() > 0 && code.length() > 0)
                UserModel.register(phone, password, code, inviteCode).done(this)
                        .fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                DialogUtil.showMessage(getString(R.string.empty_net_text));
                            }
                        });
        } else {
            DialogUtil.showMessage("密码长度必须大于6位小于18位");
        }
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.register_get_code_btn:
                // 获取验证码
                sendCheckCode();
                break;
            case R.id.register_btn:
                // 注册
                register();
                break;
            case R.id.top_register_layout:
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
            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
            PreferencesUtils.putString(gApplication, PreferencesConstant.NICK_NAME,
                    userModel.getNickname());
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_PHONE,
                    userModel.getMobileNumber());
            PreferencesUtils.putString(gApplication, PreferencesConstant.INVITE_CODE,
                    userModel.getInviteCode());
            PreferencesUtils.putInt(gApplication, PreferencesConstant.VALIDATED_USER,
                    userModel.getInviteAmount());
            gApplication.setUser(userModel);
            gApplication.updateApiToken();
            DialogUtil.showMessage("注册成功");
            UserModel.updateUserInfo(null, null, PushManager.getInstance().getClientid(this), null,
                    null).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                }
            });
            Intent intent = new Intent(this, MainActivity.class);
            ViewUtil.startTopActivity(this, intent);
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
    public void onTick(long millisUntilFinished) {
        userCodeBtn.setText((millisUntilFinished / 1000) + "秒后重发");
        userCodeBtn.setClickable(false);
    }

    @Override
    public void onFinish() {
        userCodeBtn.setText("获取验证码");
        userCodeBtn.setClickable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
