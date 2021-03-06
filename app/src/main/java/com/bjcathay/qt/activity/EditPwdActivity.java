
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
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
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-5-15.
 */
public class EditPwdActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private ClearEditText oldPwd;
    private ClearEditText newPwd;
    private ClearEditText surePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd);
        GApplication.getInstance().setFlag(false);
        initView();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_edit_pwd_layout);
        oldPwd = ViewUtil.findViewById(this, R.id.edit_old_user);
        newPwd = ViewUtil.findViewById(this, R.id.edit_new_pwd);
        surePwd = ViewUtil.findViewById(this, R.id.edit_sure_new_pwd);
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText("修改密码");

    }

    private void edit() {
        String pwd1 = newPwd.getText().toString();
        String pwd2 = surePwd.getText().toString();
        if (pwd1.isEmpty() || pwd2.isEmpty()||oldPwd.getText().toString().isEmpty()) {
            DialogUtil.showMessage("密码不能为空");
            return;
        }

        if (!pwd1.equals(pwd2)) {
            DialogUtil.showMessage("两次输入的密码不一样");
            return;
        }
        if (pwd2.length() >= 6 && pwd2.length() <= 18) {
            UserModel.changePassword(oldPwd.getText().toString(),
                    newPwd.getText().toString()).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        DialogUtil.showMessage("修改成功");
                        PreferencesUtils.putString(EditPwdActivity.this,
                                PreferencesConstant.USER_PASSWORD, newPwd.getText().toString());
                        finish();
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
        GApplication.getInstance().setFlag(false);
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("修改密码页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("修改密码页面");
        MobclickAgent.onPause(this);
    }
}
