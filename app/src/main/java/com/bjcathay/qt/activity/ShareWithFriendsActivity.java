
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-6-8.
 */
public class ShareWithFriendsActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private TextView yourInvite;
    private Button inviteSureBtn;
    private TextView inviteNote;
    private EditText inputCode;
    private Button inputCodeBtn;
    private UserModel userModel;
    private LinearLayout linearLayout;
    private ShareModel shareModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_with_friends);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_share_with_friend_layout);
        inviteNote = ViewUtil.findViewById(this, R.id.invite_note);
        yourInvite = ViewUtil.findViewById(this, R.id.your_invite_code);
        inviteSureBtn = ViewUtil.findViewById(this, R.id.invite_sure);
        inputCode = ViewUtil.findViewById(this, R.id.input_your_invite_code);
        inputCodeBtn = ViewUtil.findViewById(this, R.id.input_invite_sure);
        linearLayout = ViewUtil.findViewById(this, R.id.your_invite_code_linear);

    }

    private void initData() {
        Intent intent = getIntent();
        userModel = (UserModel) intent.getSerializableExtra("user");
        if (userModel == null) {
            userModel = GApplication.getInstance().getUser();
        }
        yourInvite.setText(userModel.getInviteCode());
        inviteNote.setText(Html.fromHtml(getString(R.string.share_with_friend_text)
                + "<font color=#FFAC41>免费</font>参加诸如<font color=#FFAC41>美国圆石滩邀请赛</font>之类的赛事。"));
        if (userModel.getInviteUserId() != null) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initEvent() {
        topView.setTitleText("与好友分享７铁");
        topView.setTitleBackVisiable();
        topView.setInvitebtnVisiable();
        inviteSureBtn.setOnClickListener(this);
    }

    private void update() {
        String inviteCode = inputCode.getText().toString().trim();
        if (inviteCode.isEmpty()) {
            DialogUtil.showMessage("邀请码不能为空");
            return;
        }
        if (inviteCode.equals(yourInvite.getText().toString().trim())) {
            DialogUtil.showMessage("不能填写自己的邀请码");
            return;
        }
        UserModel.updateUserInfo(inviteCode).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                if (jsonObject.optBoolean("success")) {
                    DialogUtil.showMessage("保存邀请码成功");
                    linearLayout.setVisibility(View.GONE);
                } else {
                    String errorMessage = jsonObject.optString("message");
                    if (!StringUtils.isEmpty(errorMessage))
                        DialogUtil.showMessage(errorMessage);
                    else {
                        int code = jsonObject.optInt("code");
                        DialogUtil.showMessage(ErrorCode.getCodeName(code));
                    }
                }

            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(ShareWithFriendsActivity.this
                        .getString(R.string.empty_net_text));
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
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
                if (shareModel == null)
                    ShareModel.share().done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            ShareModel shareModel = arguments.get(0);
                            ShareUtil.getInstance().shareDemo(ShareWithFriendsActivity.this,
                                    shareModel);
                        }
                    });
                else
                    ShareUtil.getInstance().shareDemo(ShareWithFriendsActivity.this, shareModel);
                break;
            case R.id.input_invite_sure:
                update();
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
