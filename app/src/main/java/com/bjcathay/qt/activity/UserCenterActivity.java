package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.IsLoginUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.CircleImageView;
import com.bjcathay.qt.view.SelectPicPopupWindow;
import com.bjcathay.qt.view.TopView;

/**
 * Created by dengt on 15-4-21.
 */
public class UserCenterActivity extends Activity implements View.OnClickListener/*, SelectPicPopupWindow.SelectResult*/, ICallback {
    private GApplication gApplication;
    private TopView topView;
    private LinearLayout myOrder;
    private LinearLayout myCompe;
    private LinearLayout myMessage;
    private LinearLayout myPerson;
    private LinearLayout myExchange;

    private CircleImageView userImg;
    private TextView userPhone;
    private UserModel userModel;
    private boolean newMeaage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
        gApplication = GApplication.getInstance();
        initView();
        initDate();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_usercenter_layout);
        myOrder = ViewUtil.findViewById(this, R.id.my_order);
        userImg = ViewUtil.findViewById(this, R.id.user_center_img);
        userPhone = ViewUtil.findViewById(this, R.id.user_center_phone);
        myCompe = ViewUtil.findViewById(this, R.id.my_competition);
        myMessage = ViewUtil.findViewById(this, R.id.my_message);
        myPerson = ViewUtil.findViewById(this, R.id.my_personal);
        myExchange = ViewUtil.findViewById(this, R.id.my_exchange);

    }

    private void initDate() {
        if (gApplication.isLogin())
            UserModel.get().done(this);
        updateMessage();

    }

    private void initEvent() {
        topView.setTitleText("个人中心");
        topView.setSettingVisiable();
        topView.setHomeBackVisiable();
        myOrder.setOnClickListener(this);
        myCompe.setOnClickListener(this);
        myMessage.setOnClickListener(this);
        myPerson.setOnClickListener(this);
        myExchange.setOnClickListener(this);
        userImg.setOnClickListener(this);
    }

    private void initUserData(UserModel userModel, boolean isWriteSD) {
        ImageViewAdapter.adapt(userImg, userModel.getImageUrl(), R.drawable.ic_default_user);
        this.userModel = userModel;
    }

    private void updateMessage() {
        newMeaage = PreferencesUtils.getBoolean(this, PreferencesConstant.NEW_MESSAGE_FLAG, false);
        if (newMeaage)
            findViewById(R.id.new_message_flag).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.new_message_flag).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.my_order:
                intent = new Intent(this, MyOrderActivity.class);
                IsLoginUtil.isLogin(this, intent);
                break;
            case R.id.my_competition:
                intent = new Intent(this, MyCompetitionActivity.class);
                IsLoginUtil.isLogin(this, intent);
                break;
            case R.id.my_message:
                intent = new Intent(this, MyMessageActivity.class);
                IsLoginUtil.isLogin(this, intent);
                PreferencesUtils.putBoolean(this, PreferencesConstant.NEW_MESSAGE_FLAG, false);
                break;
            case R.id.my_personal:
                intent = new Intent(this, MyFriendActivity.class);
                IsLoginUtil.isLogin(this, intent);
                break;
            case R.id.my_exchange:
                intent = new Intent(this, MyExchangeActivity.class);
                IsLoginUtil.isLogin(this, intent);
                break;
            case R.id.title_setting_img:
                intent = new Intent(this, SettingActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.home_back_img:
                finish();
                break;
            case R.id.user_center_img:
                //实例化SelectPicPopupWindow
              /*  menuWindow = new SelectPicPopupWindow(UserCenterActivity.this, UserCenterActivity.this);
                //显示窗口
                menuWindow.showAtLocation(UserCenterActivity.this.findViewById(R.id.user_center_content), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                */
                intent = new Intent(this, MyInformationActivity.class);
                intent.putExtra("user", userModel);
                IsLoginUtil.isLogin(this, intent);
                break;
        }
    }


    @Override
    public void call(Arguments arguments) {
        userModel = arguments.get(0);
        PreferencesUtils.putInt(gApplication, PreferencesConstant.VALIDATED_USER, userModel.getInviteAmount());
        ImageViewAdapter.adapt(userImg, userModel.getImageUrl(), R.drawable.ic_default_user);
        if (userModel.getNickname() == null)
            userPhone.setText(userModel.getMobileNumber());
        else
            userPhone.setText(userModel.getNickname());
    }
  /*  @Override
    protected void onStart() {
        super.onStart();
        initDate();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        initDate();
    }

}