package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.FileUtils;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.golf.R;
import com.bjcathay.golf.model.UserModel;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.CircleImageView;
import com.bjcathay.golf.view.SelectPicPopupWindow;
import com.bjcathay.golf.view.TopView;

import java.io.ByteArrayOutputStream;

/**
 * Created by bjcathay on 15-4-29.
 */
public class MyInformationActivity extends Activity implements View.OnClickListener, SelectPicPopupWindow.SelectResult {
    private TopView topView;
    private CircleImageView userImg;
    private TextView userName;
    private TextView userPhone;
    private TextView userInvite;
    private UserModel userModel;
    private RelativeLayout imgRelativeLayout;
    private RelativeLayout nameRelativeLayout;
    //自定义的弹出框类
    SelectPicPopupWindow menuWindow;
    private int selectCode = 1;
    private int requestCropIcon = 2;
    private int resultPictureCode = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        initView();
        initData();
        initEvent();
    }
    private void initView(){
        topView= ViewUtil.findViewById(this, R.id.top_my_information_layout);
        userImg=ViewUtil.findViewById(this,R.id.info_img);
        userName=ViewUtil.findViewById(this,R.id.info_name);
        userPhone=ViewUtil.findViewById(this,R.id.info_phone);
        userInvite=ViewUtil.findViewById(this,R.id.info_invite);
    }
    private  void initData(){
        Intent intent=getIntent();
        userModel= (UserModel) intent.getSerializableExtra("user");
        ImageViewAdapter.adapt(userImg, userModel.getImageUrl(), R.drawable.ic_launcher);
        userName.setText(userModel.getRealName());
        userPhone.setText(userModel.getMobileNumber());
        userInvite.setText(userModel.getInviteCode());

    }
    private void initEvent(){
        topView.setActivity(this);
        topView.setTitleText("个人资料");
    }
    private void initUserData(UserModel userModel, boolean isWriteSD) {
        ImageViewAdapter.adapt(userImg, userModel.getImageUrl(), R.drawable.ic_launcher);
        this.userModel = userModel;
    }
    @Override
    public void resultPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
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
    }

    @Override
    public void onClick(View view) {
              switch (view.getId()){
                  case R.id.info_img_relative:
                      menuWindow = new SelectPicPopupWindow(this, this);
                      //显示窗口
                      menuWindow.showAtLocation(this.findViewById(R.id.info_img_relative), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

                      break;
                  case R.id.info_name_relative:
                      break;
              }
    }
}
