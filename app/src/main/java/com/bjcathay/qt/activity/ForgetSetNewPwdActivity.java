package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.ClearEditText;
import com.bjcathay.qt.view.TopView;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-5-15.
 */
public class ForgetSetNewPwdActivity extends Activity implements View.OnClickListener/*,View.OnTouchListener*/ {
    private ImageView topView;
    private ClearEditText newPwd;
    private ClearEditText surePwd;
    private String phone;
    private String code;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_new_pwd);
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // if (Math.abs(e1.getRawX() - e2.getRawX()) > 250) {
                // // System.out.println("水平方向移动距离过大");
                // return true;
                // }
                if (Math.abs(velocityY) < 100) {
                    // System.out.println("手指移动的太慢了");
                    return true;
                }

                // 手势向下 down
                if ((e2.getRawY() - e1.getRawY()) > 200) {
                    //finish();//在此处控制关闭
                   // return true;
                }
                // 手势向上 up
                if ((e1.getRawY() - e2.getRawY()) < 0) {
                    finish();
                    overridePendingTransition(R.anim.activity_close,R.anim.activity_close);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
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
       // topView.setOnTouchListener(this);
        //  topView.setTitleText("设置新密码");

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
        if (pwd2.length() >= 6 && pwd2.length() <= 18) {
        UserModel.resetPassword(phone, newPwd.getText().toString().trim(), code).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                if (jsonObject.optBoolean("success")) {
                    DialogUtil.showMessage("密码设置成功");
                    PreferencesUtils.putString(ForgetSetNewPwdActivity.this, PreferencesConstant.USER_PASSWORD, newPwd.getText().toString().trim());
                    // finish();
                    UserModel.updateUserInfo(null, null, PushManager.getInstance().getClientid(ForgetSetNewPwdActivity.this), null, null).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                        }
                    });
                    Intent intent = new Intent(ForgetSetNewPwdActivity.this, MainActivity.class);
                    ViewUtil.startTopActivity(ForgetSetNewPwdActivity.this, intent);
                } else {
                    int code = jsonObject.optInt("code");
                    DialogUtil.showMessage(ErrorCode.getCodeName(code));
                }
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
            case R.id.edit_sure_btn:
                edit();
                break;
            case R.id.top_edit_pwd_layout:
                finish();
                overridePendingTransition(R.anim.activity_close,R.anim.activity_close);
                break;

        }
    }

  /*  @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }*/
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