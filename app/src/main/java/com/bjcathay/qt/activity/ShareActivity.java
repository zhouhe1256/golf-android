package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;

/**
 * Created by bjcathay on 15-5-21.
 */
public class ShareActivity extends Activity implements View.OnClickListener,View.OnTouchListener {
private ImageView imageView;

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
                    return true;
                }
                // 手势向上 up
                if ((e1.getRawY() - e2.getRawY()) > 0) {
                    finish();
                    overridePendingTransition(R.anim.activity_close_up,R.anim.activity_close_up);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        imageView= ViewUtil.findViewById(this,R.id.share_back);
        imageView.setOnTouchListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
          /*  case R.id.share_back:
                finish();
                overridePendingTransition(R.anim.activity_close,R.anim.activity_close);
                break;*/
        }
    }
    private GestureDetector mGestureDetector;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // OnGestureListener will analyzes the given motion event
        return mGestureDetector.onTouchEvent(motionEvent);
    }
}
