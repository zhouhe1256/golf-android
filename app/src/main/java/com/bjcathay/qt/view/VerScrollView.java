package com.bjcathay.qt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.widget.WheelView;

/**
 * Created by bjcathay on 15-6-3.
 */
public class VerScrollView extends ScrollView {
    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;

    public VerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        mGestureListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               /* if(view.getId()== R.id.weelviewlinear){
                    requestDisallowInterceptTouchEvent(false);
                }else{
                    requestDisallowInterceptTouchEvent(true);
                }*/
                if (view instanceof WheelView) {
                    requestDisallowInterceptTouchEvent(false);
                } else {
                    //requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        };
        setOnTouchListener(mGestureListener);
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);
    }

    // Return false if we're scrolling in the x direction
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }
}
