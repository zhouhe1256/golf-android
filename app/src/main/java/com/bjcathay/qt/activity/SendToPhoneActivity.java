package com.bjcathay.qt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.fragment.DialogExchFragment;
import com.bjcathay.qt.model.UserListModle;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by bjcathay on 15-6-1.
 */
public class SendToPhoneActivity extends FragmentActivity implements View.OnClickListener, TextWatcher, DialogExchFragment.ExchangeResult {
    private FragmentActivity context;
    private EditText nicknameEdit;
    private TextView nicknameLengthText;
    private TopView topView;
    private Long id;
    private String number;
    private DialogExchFragment dialogExchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_edit_phone);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText("赠送好友");
        nicknameEdit.addTextChangedListener(this);

    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        // number = inputNumber.getText().toString().trim();
        dialogExchFragment = new DialogExchFragment(this, this);
    }

    private void initView() {
        context = this;
        topView = ViewUtil.findViewById(this, R.id.top_edit_name_layout);
        nicknameEdit = ViewUtil.findViewById(context, R.id.edit_nickname_edit_view);
        nicknameLengthText = ViewUtil.findViewById(context, R.id.edit_nickname_text_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_sure:
                String nickname = nicknameEdit.getText().toString();
                if (nickname.length() > 16) {
                    DialogUtil.showMessage("长度超出范围");
                    return;
                } else if (!nickname.isEmpty()) {
                    UserListModle.searchUser(nickname).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            UserListModle userModel = arguments.get(0);
                            if (userModel.getUsers() == null || userModel.getUsers().size() < 1) {
                                dialogExchFragment.setItems(null, "user", nicknameEdit.getText().toString().trim(), id);
                            } else {
                                dialogExchFragment.setItems(userModel.getUsers().get(0), "user", nicknameEdit.getText().toString().trim(), id);
                            }
                            dialogExchFragment.show(getSupportFragmentManager(), "send");

                        }
                    });
                } else {
                    DialogUtil.showMessage("号码不能为空");
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
        nicknameLengthText.setText(length + "/12");
        if (length > 16) {
            nicknameLengthText.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void exchangeResult(UserModel userModel, boolean isExchange) {
        if (isExchange)
            finish();
    }
}
