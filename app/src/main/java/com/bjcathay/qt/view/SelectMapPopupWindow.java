package com.bjcathay.qt.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;

/**
 * Created by bjcathay on 15-5-29.
 */
public class SelectMapPopupWindow extends PopupWindow {

    public interface SelectMapResult {
        void selectBDMapResult();

        void selectGDMapResult();
    }

    private Button btn_bd, btn_gd, btn_cancel;
    private View mMenuView;
    private Animation animShow;
    // sliding down animation
    private Animation animHide;
    LinearLayout poplayout;
    boolean bd;
    boolean gd;

    public SelectMapPopupWindow(Activity context, boolean bd, boolean gd, final SelectMapResult selectResult) {
        super(context);
        initAnim();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_map_popuo_window, null);
        poplayout = ViewUtil.findViewById(mMenuView, R.id.pop_layout);
        this.bd = bd;
        this.gd = gd;
        btn_bd = (Button) mMenuView.findViewById(R.id.btn_take_photo);
        btn_gd = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
        if (bd) {
            btn_bd.setVisibility(View.VISIBLE);
        }
        if (gd) {
            btn_gd.setVisibility(View.VISIBLE);
        }
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        //取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                //dismiss();
                poplayout.setVisibility(View.VISIBLE);
                poplayout.clearAnimation();
                poplayout.startAnimation(animHide);
            }
        });
        //设置按钮监听
        btn_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectResult.selectBDMapResult();
                dismiss();
            }
        });
        btn_gd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectResult.selectGDMapResult();
                dismiss();
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        //dismiss();
                        poplayout.setVisibility(View.VISIBLE);
                        poplayout.clearAnimation();
                        poplayout.startAnimation(animHide);
                    }
                }
                return true;
            }
        });
        poplayout.setVisibility(View.VISIBLE);
        poplayout.clearAnimation();
        poplayout.startAnimation(animShow);
    }

    private void initAnim() {
        animShow = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        animShow.setDuration(300);

        animHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1);
        animHide.setDuration(300);
        animHide.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                // poplayout.setVisibility(View.GONE);
                dismiss();
            }
        });
    }
}
