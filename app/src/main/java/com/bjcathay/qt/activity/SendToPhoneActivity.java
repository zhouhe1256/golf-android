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
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ValidformUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-6-1.
 */
public class SendToPhoneActivity extends FragmentActivity implements View.OnClickListener, TextWatcher, DialogExchFragment.ExchangeResult {
    private FragmentActivity context;
    private EditText phoneEdit;
    private TextView phoneLengthText;
    private TopView topView;
    private Long id;
    private String number;
    private String proName;
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
        topView.setTitleText("输入手机号码");
        phoneEdit.addTextChangedListener(this);

    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        proName=intent.getStringExtra("name");
        // number = inputNumber.getText().toString().trim();
        dialogExchFragment = new DialogExchFragment(this, this);
    }

    private void initView() {
        context = this;
        topView = ViewUtil.findViewById(this, R.id.top_edit_name_layout);
        phoneEdit = ViewUtil.findViewById(context, R.id.edit_nickname_edit_view);
        phoneLengthText = ViewUtil.findViewById(context, R.id.edit_nickname_text_view);
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.edit_sure:
                String phone = phoneEdit.getText().toString();
                if (phone.length() > 11) {
                    DialogUtil.showMessage("长度超出范围");
                    return;
                } else if (!phone.isEmpty()) {
                    if (!ValidformUtil.isMobileNo(phone)) {
                        DialogUtil.showMessage("请填写正确的手机号码");
                        return;
                    } else

                        UserListModle.searchUser(phone).done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                UserListModle userModel = arguments.get(0);
                                if (userModel.getUsers() == null || userModel.getUsers().size() < 1) {
                                    dialogExchFragment.setItems(null, "user", phoneEdit.getText().toString().trim(), id,proName);
                                } else {
                                    dialogExchFragment.setItems(userModel.getUsers().get(0), "user", phoneEdit.getText().toString().trim(), id,proName);
                                }
                                dialogExchFragment.show(getSupportFragmentManager(), "send");

                            }
                        });
                } else {
                    DialogUtil.showMessage("请输入手机号码");
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
        String nickname = phoneEdit.getText().toString();
        int length = nickname.length();
        phoneLengthText.setText(length + "/11");
        if (length > 11) {
            phoneLengthText.setTextColor(getResources().getColor(R.color.red));
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
