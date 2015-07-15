
package com.bjcathay.qt.activity;

import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-4-29.
 */
public class SendFriendActivity extends FragmentActivity implements View.OnClickListener {
    private FragmentActivity context;
    private TopView topView;
    private RelativeLayout sendToList;
    private RelativeLayout inputNumber;
    private Long id;
    private String proName;

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
        proName = intent.getStringExtra("name");

    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_send_friend_layout);
        sendToList = ViewUtil.findViewById(this, R.id.send_to_mail_list);
        inputNumber = ViewUtil.findViewById(this, R.id.send_input_number);
    }

    private void initEvent() {
        topView.setTitleText("赠送好友");
        topView.setTitleBackVisiable();
        sendToList.setOnClickListener(this);
        inputNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.send_to_mail_list:
                intent = new Intent(this, ContactActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", proName);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.send_input_number:
                intent = new Intent(this, SendToPhoneActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", proName);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("赠送好友页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("赠送好友页面");
        MobclickAgent.onPause(this);
    }
}
