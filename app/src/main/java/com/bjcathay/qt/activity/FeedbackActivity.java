package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-5-14.
 */
public class FeedbackActivity extends Activity implements View.OnClickListener, ICallback {
    private EditText editText;
    private EditText editphoneText;

    private GApplication gApplication;
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        gApplication = GApplication.getInstance();
        initView();
        initDate();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_feedback_layout);
        editText = ViewUtil.findViewById(this, R.id.feedback_content);
        editphoneText = ViewUtil.findViewById(this, R.id.feedback_phone);
    }

    private void initEvent() {
        //logoutBtn.setOnClickListener(this);
    }

    private void initDate() {
        topView.setTitleBackVisiable();
        topView.setTitleText("意见反馈");
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.feedback_btn:
                commit();
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    private void commit() {
       /* String content=editText.getText().toString().trim();
        if(content.length()==0){
            DialogUtil.showMessage("不能为空哦");
            return;
        }*/
        UserModel.feedBack(editphoneText.getText().toString().trim(), editText.getText().toString().trim()).done(this);
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
            DialogUtil.showMessage("感谢参与！");
            finish();
        } else {
            int code = jsonObject.optInt("code");
            DialogUtil.showMessage(ErrorCode.getCodeName(code));
        }
    }
}
