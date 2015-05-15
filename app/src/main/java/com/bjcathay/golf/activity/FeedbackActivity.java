package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.golf.R;
import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.constant.ErrorCode;
import com.bjcathay.golf.model.UserModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-5-14.
 */
public class FeedbackActivity extends Activity implements View.OnClickListener,ICallback{
    private EditText editText;
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
    }

    private void initEvent() {
        //logoutBtn.setOnClickListener(this);
    }

    private void initDate() {
        topView.setActivity(this);
        topView.setTitleText("意见反馈");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.feedback_btn:
                commit();
                break;
        }
    }
    private void commit(){
        UserModel.feedBack("",editText.getText().toString().toString()).done(this);
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
            DialogUtil.showMessage("谢谢！");
            finish();
        }else {
            int code = jsonObject.optInt("code");
            DialogUtil.showMessage(ErrorCode.getCodeName(code));
        }
    }
}
