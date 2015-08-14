
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
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.ClearEditText;
import com.bjcathay.qt.view.TopView;
import com.igexin.sdk.PushManager;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-5-15.
 */
public class ForgetSetNewPwdActivity extends Activity implements View.OnClickListener {
    private ClearEditText newPwd;
    private ClearEditText surePwd;
    private String phone;
    private String code;
    private TopView topView;

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
        topView.setTitleBackVisiable();
        topView.setTitleText("重置密码");
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
            UserModel.resetPassword(phone, newPwd.getText().toString().trim(), code).done(
                    new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            JSONObject jsonObject = arguments.get(0);
                            if (jsonObject.optBoolean("success")) {
                                DialogUtil.showMessage("密码设置成功");
                                PreferencesUtils.putString(ForgetSetNewPwdActivity.this,
                                        PreferencesConstant.USER_PASSWORD, newPwd.getText()
                                                .toString().trim());
                                UserModel.updateUserInfo(
                                        null,
                                        null,
                                        PushManager.getInstance().getClientid(
                                                ForgetSetNewPwdActivity.this), null, null).done(
                                        new ICallback() {
                                            @Override
                                            public void call(Arguments arguments) {
                                            }
                                        });
                                Intent intent = new Intent(ForgetSetNewPwdActivity.this,
                                        MainActivity.class);
                                ViewUtil.startTopActivity(ForgetSetNewPwdActivity.this, intent);
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
            case R.id.title_back_img:
                finish();
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("忘记密码之修改密码页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("忘记密码之修改密码页面");
        MobclickAgent.onPause(this);
    }
}
