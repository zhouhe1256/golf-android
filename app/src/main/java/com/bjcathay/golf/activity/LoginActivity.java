package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.golf.R;
import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.model.UserModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.PreferencesConstant;
import com.bjcathay.golf.util.PreferencesUtils;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.ClearEditText;
import com.bjcathay.golf.view.TopView;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-4-23.
 */
public class LoginActivity extends Activity implements View.OnClickListener,ICallback {
    private GApplication gApplication;
    private Button loginbtn;
    private TopView topView;
    private TextView newlogin;
    private TextView forgetbtn;
    private ClearEditText loginUser;
    private ClearEditText loginpwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gApplication=GApplication.getInstance();
        initView();
        initEvent();
        initData();

    }

    private void initView() {
        loginbtn = ViewUtil.findViewById(this, R.id.login_btn);
        topView = ViewUtil.findViewById(this, R.id.top_login_layout);
        newlogin = ViewUtil.findViewById(this, R.id.new_login);
        forgetbtn = ViewUtil.findViewById(this, R.id.forget_secrete);
        loginUser=ViewUtil.findViewById(this,R.id.login_user);
        loginpwd=ViewUtil.findViewById(this,R.id.login_pwd);

    }

    private void initEvent() {
        topView.setTitleText("登录");
        topView.setActivity(this);
        loginbtn.setOnClickListener(this);
        newlogin.setOnClickListener(this);
        forgetbtn.setOnClickListener(this);
    }
    private void initData(){
        String user_name = PreferencesUtils.getString(gApplication, PreferencesConstant.USER_NAME, "");
        String pass_word = PreferencesUtils.getString(gApplication, PreferencesConstant.USER_PASSWORD, "");
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
        UserModel.login(user, password).done(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.login_btn:
                intent = new Intent(this, UserCenterActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.new_login:
                intent = new Intent(this, RegisterActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.forget_secrete:
                break;
        }
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        if(jsonObject.optBoolean("success")){
            UserModel userModel = JSONUtil.load(UserModel.class, jsonObject.optJSONObject("user"));
           // userModel.setCurrentUser(userModel);
            String token = userModel.getApiToken();
            //保存用户名和密码
            PreferencesUtils.putString(gApplication, PreferencesConstant.NICK_NAME, userModel.getNickname());
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_NAME, loginUser.getText().toString().trim());
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_PASSWORD, loginpwd.getText().toString().trim());
            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
            gApplication.updateApiToken();
            DialogUtil.showMessage("登陆成功");
        }
    }
}
