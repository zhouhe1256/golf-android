package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-5-15.
 */
public class UserEditNicknameActivity extends Activity implements View.OnClickListener, TextWatcher {
    private Activity context;
    private EditText nicknameEdit;
    private TextView nicknameLengthText;
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_nickname);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText("修改昵称");
        nicknameEdit.addTextChangedListener(this);

    }

    private void initData() {
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");
        if (nickname == null) {
            nickname = "";
        }
        nicknameEdit.setText(nickname);
        nicknameLengthText.setText(nickname.length() + "/10");
    }

    private void initView() {
        super.setTitle("修改昵称");
        context = this;
        topView = ViewUtil.findViewById(this, R.id.top_edit_name_layout);
        nicknameEdit = ViewUtil.findViewById(context, R.id.edit_nickname_edit_view);
        nicknameLengthText = ViewUtil.findViewById(context, R.id.edit_nickname_text_view);
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.edit_sure:
                String nickname = nicknameEdit.getText().toString();
                if (nickname.length() > 10) {
                    DialogUtil.showMessage("昵称长度超出范围");
                    return;
                } else if (!nickname.isEmpty()) {
                    UserModel.updateUserInfo(nickname, null, null, null, null).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            UserModel userModel = arguments.get(0);
                            Intent intent = new Intent();
                            intent.putExtra("nickname", userModel.getNickname());
                            setResult(7, intent);
                            DialogUtil.showMessage("修改成功");
                            ViewUtil.finish(UserEditNicknameActivity.this);
                        }
                    }).fail(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            DialogUtil.showMessage(getString(R.string.empty_net_text));
                        }
                    });
                } else {
                    DialogUtil.showMessage("昵称不能为空");
                }
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String nickname = nicknameEdit.getText().toString();
        int length = nickname.length();
        nicknameLengthText.setText(length + "/10");
        if (length > 10) {
            nicknameLengthText.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

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
