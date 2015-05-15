package com.bjcathay.golf.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;

/**
 * Created by dengt on 15-4-27.
 */
public class TopView extends LinearLayout {
    private TextView leftbtn;
    private TextView rightbtn;
    private TextView title;
    private Activity activity;
    private ImageView titleBack;
    private ImageView homeBack;
    private ImageView share;
    private ImageView setting;

    private void initView(final Context context) {
        View.inflate(context, R.layout.activity_title, this);
        leftbtn = ViewUtil.findViewById(this, R.id.title_back);
        rightbtn = ViewUtil.findViewById(this, R.id.title_right);
        title = ViewUtil.findViewById(this, R.id.title_title);
        titleBack = ViewUtil.findViewById(this, R.id.title_back_img);
        homeBack = ViewUtil.findViewById(this, R.id.home_back_img);
        share = ViewUtil.findViewById(this, R.id.title_share_img);
        setting = ViewUtil.findViewById(this, R.id.title_setting_img);

        leftbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity != null) {
                    activity.finish();
                }
            }
        });

    }

    public void setTitleBackVisiable() {
        titleBack.setVisibility(VISIBLE);
    }

    public void setHomeBackVisiable() {
        homeBack.setVisibility(VISIBLE);
    }

    public void setShareVisiable() {
        share.setVisibility(VISIBLE);
    }

    public void setSettingVisiable() {
        setting.setVisibility(VISIBLE);
    }


    /**
     * 设置控件可见性
     */
    public void setVisiable(int leftVisiable, int titleVisiable, int rightVisiable) {
        title.setVisibility(titleVisiable);
        leftbtn.setVisibility(leftVisiable);
        rightbtn.setVisibility(rightVisiable);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setContext(Context context) {

    }

    public void setTitleText(String titleText) {
        title.setText(titleText);
    }

    public void setLeftbtnText(String leftText, int resid) {
        if (leftText != null)
            leftbtn.setText(leftText);
        leftbtn.setBackgroundResource(resid);
    }

    public void setRightbtn(String rightText, int resid) {
        rightbtn.setText(rightText);
        if (resid != 0)
            rightbtn.setBackgroundResource(resid);
    }

    public TextView getRightbtn() {
        return rightbtn;
    }

    public TextView getTitle() {
        return title;

    }

    public TopView(Context context) {
        super(context);
        initView(context);
    }

    public TopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public TopView(Context context, Activity activity) {
        super(context);
        this.activity = activity;
        initView(context);
    }
}
