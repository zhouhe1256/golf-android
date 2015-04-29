package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by bjcathay on 15-4-29.
 */
public class SendFriendActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private LinearLayout sendToList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_friend);
        initView();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_send_friend_layout);
        sendToList = ViewUtil.findViewById(this, R.id.send_to_mail_list);
    }

    private void initEvent() {
        topView.setTitleText("赠送好友");
        topView.setActivity(this);
        sendToList.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.send_to_mail_list:
                intent = new Intent(this, ContactActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
        }
    }
}
