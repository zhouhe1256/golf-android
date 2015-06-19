package com.bjcathay.qt.activity;

import android.app.Activity;
import android.hardware.GeomagneticField;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-5-21.
 */
public class ShareActivity extends Activity implements View.OnClickListener/*, View.OnTouchListener*/ {
    private ImageView imageView;
    private LinearLayout linearLayout;
    private TextView inviteCode;
    private TextView share;
    private ShareModel shareModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // if (Math.abs(e1.getRawX() - e2.getRawX()) > 250) {
                // // System.out.println("水平方向移动距离过大");
                // return true;
                // }
                if (Math.abs(velocityY) < 100) {
                    // System.out.println("手指移动的太慢了");
                    return true;
                }

                // 手势向下 down
                if ((e2.getRawY() - e1.getRawY()) > 200) {
                    //finish();//在此处控制关闭
                   // return true;
                }
                // 手势向上 up
                if ((e1.getRawY() - e2.getRawY()) < 0) {
                    finish();
                    overridePendingTransition(R.anim.activity_close, R.anim.activity_close);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        imageView = ViewUtil.findViewById(this, R.id.share_back);
        linearLayout = ViewUtil.findViewById(this, R.id.share_code_linear);
        inviteCode = ViewUtil.findViewById(this, R.id.share_you_invite_code);
        share = ViewUtil.findViewById(this, R.id.share_to_click);
    }

    private void initData() {
        if (GApplication.getInstance().isLogin()) {
            linearLayout.setVisibility(View.VISIBLE);
            inviteCode.setText(PreferencesUtils.getString(this, PreferencesConstant.INVITE_CODE));
        } else {
            linearLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void initEvent() {
      //  imageView.setOnTouchListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.share_to_click:
                if (shareModel == null)
                    ShareModel.share().done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            shareModel = arguments.get(0);
                            ShareUtil.getInstance().shareDemo(ShareActivity.this, shareModel);
                        }
                    });
                else ShareUtil.getInstance().shareDemo(ShareActivity.this, shareModel);
                break;
        }
    }

    private GestureDetector mGestureDetector;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
   /* @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // OnGestureListener will analyzes the given motion event
        return mGestureDetector.onTouchEvent(motionEvent);
    }*/
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
