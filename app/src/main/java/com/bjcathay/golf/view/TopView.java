package com.bjcathay.golf.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;

/**
 * Created by bjcathay on 15-4-27.
 */
public class TopView extends LinearLayout {
    private Button leftbtn;
    private Button rightbtn;
    private TextView title;
    private Activity activity;

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

    private void initView(final Context context) {
        View.inflate(context, R.layout.activity_title, this);
        leftbtn = ViewUtil.findViewById(this, R.id.title_back);
        rightbtn = ViewUtil.findViewById(this, R.id.title_right);
        title = ViewUtil.findViewById(this, R.id.title_title);
        
    }

    /**
     * 设置控件可见性
     *
     */
    public void setVisiable(int leftVisiable, int titleVisiable, int rightVisiable) {
        title.setVisibility(titleVisiable);
        leftbtn.setVisibility(leftVisiable);
        rightbtn.setVisibility(rightVisiable);
    }

    public void setTitleText(String titleText) {
        title.setText(titleText);
    }

    public void setLeftbtnText(String leftText, int resid) {
        leftbtn.setText(leftText);
        leftbtn.setBackgroundResource(resid);
    }

    public void setRightbtn(String rightText, int resid) {
        rightbtn.setText(rightText);
        rightbtn.setBackgroundResource(resid);
    }
}
