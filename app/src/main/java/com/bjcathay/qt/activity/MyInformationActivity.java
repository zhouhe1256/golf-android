
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.FileUtils;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.CircleImageView;
import com.bjcathay.qt.view.SelectPicPopupWindow;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dengt on 15-4-29.
 */
public class MyInformationActivity extends Activity implements SelectPicPopupWindow.SelectResult,
        View.OnClickListener {
    private TopView topView;
    private CircleImageView userImg;
    private TextView userName;
    private TextView userNickName;
    private TextView userPhone;
    private TextView userInvite;
    private UserModel userModel;
    // 自定义的弹出框类
    private SelectPicPopupWindow menuWindow;
    private int selectCode = 1;
    private int requestCropIcon = 2;
    private int resultPictureCode = 3;
    private int resultEditName = 4;
    private int selectEditName = 5;
    private int resultEditNickName = 6;
    private int selectEditNickName = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_my_information_layout);
        userImg = ViewUtil.findViewById(this, R.id.info_img);
        userName = ViewUtil.findViewById(this, R.id.info_name);
        userNickName = ViewUtil.findViewById(this, R.id.info_nick_name);
        userPhone = ViewUtil.findViewById(this, R.id.info_phone);
        userInvite = ViewUtil.findViewById(this, R.id.info_invite);
    }

    private void initData() {
        Intent intent = getIntent();
        userModel = (UserModel) intent.getSerializableExtra("user");
        if (userModel == null)
            userModel = GApplication.getInstance().getUser();
        if (userModel != null) {
            ImageViewAdapter.adapt(userImg, userModel.getImageUrl(), R.drawable.ic_default_user);
            userName.setText(userModel.getRealName());
            userNickName.setText(userModel.getNickname());
            userPhone.setText(userModel.getMobileNumber());
            userInvite.setText(userModel.getInviteCode());
        } else {
            userModel = GApplication.getInstance().getUser();
        }
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText("个人资料");
    }

    private void initUserData(UserModel userModel, boolean isWriteSD) {
        ImageViewAdapter.adapt(userImg, userModel.getImageUrl(), R.drawable.ic_default_user);
        this.userModel = userModel;
    }

    @Override
    public void resultPicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, selectCode);
    }

    @Override
    public void resultCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, resultPictureCode);
    }

    Uri uri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        if (selectCode == requestCode) {
            uri = data.getData();
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 60);
            intent.putExtra("outputY", 60);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("return-data", true);// 设置为不返回数据
            startActivityForResult(intent, requestCropIcon);
        } else if (requestCropIcon == requestCode) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                if (uri == null) {
                    return;
                }
                Bitmap photo = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                    UserModel.setAvatar(FileUtils.Bitmap2Bytes(photo)).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            UserModel user = arguments.get(0);
                            initUserData(user, false);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
        if (resultEditName == requestCode && selectEditName == resultCode) {
            userName.setText(data.getStringExtra("name"));
        }
        if (resultEditNickName == requestCode && selectEditNickName == resultCode) {
            userNickName.setText(data.getStringExtra("nickname"));
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.info_img_relative:
                menuWindow = new SelectPicPopupWindow(this, this);
                menuWindow.showAtLocation(this.findViewById(R.id.info_img_relative), Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.info_name_relative:
                intent = new Intent(this, UserEditNnameActivity.class);
                intent.putExtra("name", userName.getText().toString().trim());
                startActivityForResult(intent, resultEditName);
                break;
            case R.id.info_nick_name_relative:
                intent = new Intent(this, UserEditNicknameActivity.class);
                intent.putExtra("nickname", userNickName.getText().toString().trim());
                startActivityForResult(intent, resultEditNickName);
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
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
