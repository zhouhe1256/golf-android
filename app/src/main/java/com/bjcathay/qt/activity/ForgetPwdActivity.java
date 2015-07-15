
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
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.TimeCount;
import com.bjcathay.qt.util.ValidformUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.ClearEditText;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-5-15.
 */
public class ForgetPwdActivity extends Activity implements View.OnClickListener, ICallback,
        TimeCount.TimeUpdate {
    private GApplication gApplication;
    private ClearEditText userPhone;
    private ClearEditText userCode;
    private Button registerBtn;
    private TextView userCodeBtn;
    private TimeCount time;
    private ImageView topView;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
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
            userCodeBtn.setClickable(false);
            UserModel.sendCheckCode(phone, "FORGET_PWD").done(new ICallback() {
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
        if (code.length() == 0) {
            DialogUtil.showMessage("请输入验证码");
        }
        if (phone.length() > 0 && code.length() > 0)
            UserModel.verifyCheckCode(phone, code, "FORGET_PWD").done(this);
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
                // 下一步
                forget();
                break;
            case R.id.top_forget_layout:
                finish();
                overridePendingTransition(R.anim.activity_close, R.anim.activity_close);
                break;
        }
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
            Intent intent = new Intent(this, ForgetSetNewPwdActivity.class);
            intent.putExtra("phone", userPhone.getText().toString().trim());
            intent.putExtra("code", userCode.getText().toString().trim());
            ViewUtil.startActivity(this, intent);
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
        MobclickAgent.onPageStart("忘记密码页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("忘记密码页面");
        MobclickAgent.onPause(this);
    }
}
