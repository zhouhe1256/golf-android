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
 * Created by dengt on 15-4-27.
 */
public class TopView extends LinearLayout {
    private TextView leftbtn;
    private TextView rightbtn;
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

    public TopView(Context context, Activity activity) {
        super(context);
        this.activity = activity;
        initView(context);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    public void setContext(Context context){

    }

    private void initView(final Context context) {
        View.inflate(context, R.layout.activity_title, this);
        leftbtn = ViewUtil.findViewById(this, R.id.title_back);
        rightbtn = ViewUtil.findViewById(this, R.id.title_right);
        title = ViewUtil.findViewById(this, R.id.title_title);

        leftbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity != null) {
                    activity.finish();
                }
            }
        });

    }

    /**
     * 设置控件可见性
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
        if (resid != 0)
            rightbtn.setBackgroundResource(resid);
    }
    public TextView getRightbtn(){
        return rightbtn;
    }
}
