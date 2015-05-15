package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.golf.R;
import com.bjcathay.golf.constant.ErrorCode;
import com.bjcathay.golf.model.PropModel;
import com.bjcathay.golf.model.UserListModle;
import com.bjcathay.golf.model.UserModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.ClearEditText;
import com.bjcathay.golf.view.TopView;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by bjcathay on 15-4-29.
 */
public class SendFriendActivity extends Activity implements View.OnClickListener {
    private Activity context;
    private TopView topView;
    private LinearLayout sendToList;
    private ClearEditText inputNumber;
    private Long id;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_friend);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        number = inputNumber.getText().toString().trim();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_send_friend_layout);
        sendToList = ViewUtil.findViewById(this, R.id.send_to_mail_list);
        inputNumber = ViewUtil.findViewById(this, R.id.send_input_number);
    }

    private void initEvent() {
        topView.setTitleText("赠送好友");
        topView.setActivity(this);
        sendToList.setOnClickListener(this);
        inputNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    context
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS
                            );
                    UserListModle.searchUser(inputNumber.getText().toString().trim()).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                                UserListModle userModel = arguments.get(0);
                                if(userModel.getUsers()==null||userModel.getUsers().size()<1){
                                    DialogUtil.showMessage("不是我们的用户");
                                }else
                                PropModel.sendProp(id, inputNumber.getText().toString().trim())
                                        .done(new ICallback() {
                                                  @Override
                                                  public void call(Arguments arguments) {
                                                      JSONObject jsonObject = arguments.get(0);
                                                      if (jsonObject.optBoolean("success")) {
                                                          DialogUtil.showMessage("赠送成功");
                                                      } else {
                                                          int code = jsonObject.optInt("code");
                                                          DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                                      }
                                                  }
                                              }
                                        );

                        }
                    });

                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.send_to_mail_list:
                intent = new Intent(this, ContactActivity.class);
                intent.putExtra("id", id);
                ViewUtil.startActivity(this, intent);
                break;
        }
    }
}
