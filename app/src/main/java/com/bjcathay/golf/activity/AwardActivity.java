package com.bjcathay.golf.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * 兑换页面
 * Created by dengt on 15-4-20.
 */
public class AwardActivity extends Activity implements View.OnClickListener,View.OnLongClickListener{
    private TopView topView;
    private TextView awardingFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        initView();
        initEvent();
    }
    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_award_layout);
        awardingFirst=ViewUtil.findViewById(this,R.id.exchange_first);
    }

    private void initEvent() {
        topView.setTitleText("兑换");
        topView.setActivity(this);
        awardingFirst.setOnClickListener(this);
        awardingFirst.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.exchange_first:
                LayoutInflater inflater = getLayoutInflater();
                ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.dialog_exchange_award, null);
                Dialog dialog = new Dialog(this, R.style.myDialogTheme);
                dialog.setContentView(rootView);
                //dialog.create();
                // dialog.setContentView(rootView);
                dialog.show();
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.exchange_first:
                intent = new Intent(this, SendFriendActivity.class);
                ViewUtil.startActivity(this, intent);
            return true;
        }
        return false;
    }
}
