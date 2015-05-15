package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.FileUtils;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.golf.R;
import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.model.UserModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.PreferencesConstant;
import com.bjcathay.golf.util.PreferencesUtils;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.CircleImageView;
import com.bjcathay.golf.view.SelectPicPopupWindow;
import com.bjcathay.golf.view.TopView;

import java.io.ByteArrayOutputStream;

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
    //自定义的弹出框类
    SelectPicPopupWindow menuWindow;
    private UserModel userModel;
    private int selectCode = 1;
    private int requestCropIcon = 2;
    private int resultPictureCode = 3;

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
        UserModel.get().done(this);
    }

    private void initEvent() {
        topView.setTitleText("个人中心");
        topView.setActivity(this);
        topView.setRightbtn(null,R.drawable.ic_my_setting);
        topView.setLeftbtnText(null,R.drawable.ic_home_back);
        topView.setVisiable(View.VISIBLE,View.VISIBLE,View.VISIBLE);
        topView.getRightbtn().setOnClickListener(this);
        myOrder.setOnClickListener(this);
        myCompe.setOnClickListener(this);
        myMessage.setOnClickListener(this);
        myPerson.setOnClickListener(this);
        myExchange.setOnClickListener(this);

        userImg.setOnClickListener(this);
    }

    private void initUserData(UserModel userModel, boolean isWriteSD) {
        ImageViewAdapter.adapt(userImg, userModel.getImageUrl(), R.drawable.ic_launcher);
        this.userModel = userModel;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.my_order:
                intent = new Intent(this, MyOrderActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.my_competition:
                intent = new Intent(this, MyCompetitionActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.my_message:
                intent = new Intent(this, MyMessageActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.my_personal:
                /*intent = new Intent(this, MyInformationActivity.class);
                intent.putExtra("user", userModel);
                ViewUtil.startActivity(this, intent);*/
                break;
            case R.id.my_exchange:
                intent = new Intent(this, MyExchangeActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.title_right:
                intent = new Intent(this, SettingActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.user_center_img:
                //实例化SelectPicPopupWindow
              /*  menuWindow = new SelectPicPopupWindow(UserCenterActivity.this, UserCenterActivity.this);
                //显示窗口
                menuWindow.showAtLocation(UserCenterActivity.this.findViewById(R.id.user_center_content), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                */intent = new Intent(this, MyInformationActivity.class);
                intent.putExtra("user", userModel);
                ViewUtil.startActivity(this, intent);

                break;
        }
    }



    @Override
    public void call(Arguments arguments) {
        userModel = arguments.get(0);
        PreferencesUtils.putString(gApplication, PreferencesConstant.VALIDATED_USER, userModel.getInviteAmount()+"");
        ImageViewAdapter.adapt(userImg, userModel.getImageUrl(), R.drawable.ic_launcher);
        userPhone.setText(userModel.getNickname());
    }

   /* @Override
    public void resultPicture() {
        Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, selectCode);
    }

    @Override
    public void resultCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, resultPictureCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (selectCode == requestCode) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setData(data.getData());
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 96);
            intent.putExtra("outputY", 96);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, requestCropIcon);
        } else if (requestCropIcon == requestCode) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                UserModel.setAvatar(FileUtils.Bitmap2Bytes(photo)).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        UserModel user = arguments.get(0);
                        initUserData(user, false);
                    }
                });
            }
        } else if (resultPictureCode == requestCode) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                UserModel.setAvatar(FileUtils.Bitmap2Bytes(photo)).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        UserModel user = arguments.get(0);
                        initUserData(user, false);
                    }
                });
            }
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        initDate();
    }
}
