package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by bjcathay on 15-6-8.
 */
public class ShareWithFriendsActivity extends Activity implements View.OnClickListener{
    private TopView topView;
    private TextView yourInvite;
    private Button inviteSureBtn;
    private TextView inviteNote;
    private EditText inputCode;
    private Button inputCodeBtn;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_with_friends);
        initView();
        initData();
        initEvent();
    }
    private void initView(){
        topView= ViewUtil.findViewById(this,R.id.top_share_with_friend_layout);
        inviteNote=ViewUtil.findViewById(this,R.id.invite_note);
        yourInvite=ViewUtil.findViewById(this,R.id.your_invite_code);
        inviteSureBtn=ViewUtil.findViewById(this,R.id.invite_sure);
        inputCode=ViewUtil.findViewById(this,R.id.input_your_invite_code);
        inputCodeBtn=ViewUtil.findViewById(this,R.id.input_invite_sure);

    }
    private void initData(){
        Intent intent = getIntent();
        userModel = (UserModel) intent.getSerializableExtra("user");
        yourInvite.setText(userModel.getInviteCode());
        inviteNote.setText(Html.fromHtml(getString(R.string.share_with_friend_text)));
    }
    private void initEvent(){
        topView.setTitleText("与好友分享７铁");
        topView.setTitleBackVisiable();
        topView.setInvitebtnVisiable();
        inviteSureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.title_invite:
                intent = new Intent(this, MyFriendActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.invite_sure:
                findViewById(R.id.invite_sure).setOnClickListener(null);
                ShareModel.share().done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        ShareModel shareModel = arguments.get(0);
                        ShareUtil.getInstance().shareDemo(ShareWithFriendsActivity.this, shareModel);
                        findViewById(R.id.invite_sure).setOnClickListener(ShareWithFriendsActivity.this);
                    }
                });
                break;
        }
    }
}
