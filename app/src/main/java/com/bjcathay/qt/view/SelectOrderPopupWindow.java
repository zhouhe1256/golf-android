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
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;

/**
 * Created by dengt on 15-8-31.
 */
public class SelectOrderPopupWindow extends PopupWindow {
    public interface SelectOrderResult {
        void selectAllMapResult();

        void selectPlaceMapResult();

        void selectEventResult();

        void selectComboResult();

    }
    private TextView btn_bd, btn_gd, btn_tx, btn_sg;
   // private Button btn_cancel;
    private View mMenuView;
    private Animation animShow;
    private Animation animHide;
    LinearLayout poplayout;

    public SelectOrderPopupWindow(Activity context, final SelectOrderResult selectResult,int code) {
        super(context);
        initAnim();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_order_popuo_window, null);
        poplayout = ViewUtil.findViewById(mMenuView, R.id.pop_layout);
        btn_bd = ViewUtil.findViewById(mMenuView, R.id.btn_take_photo);
        btn_gd = ViewUtil.findViewById(mMenuView, R.id.btn_pick_photo);
        btn_tx = ViewUtil.findViewById(mMenuView, R.id.tx_map);
        btn_sg = ViewUtil.findViewById(mMenuView, R.id.sg_map);
        // 设置按钮监听
        switch (code){
            case 1:
                btn_bd.setTextColor(context.getResources().getColor(R.color.home_title_color));
                break;
            case 2:
                btn_gd.setTextColor(context.getResources().getColor(R.color.home_title_color));
                break;
            case 3:
                btn_tx.setTextColor(context.getResources().getColor(R.color.home_title_color));
                break;
            case 4:
                btn_sg.setTextColor(context.getResources().getColor(R.color.home_title_color));
                break;
        }
        btn_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectResult.selectAllMapResult();
                dismiss();
            }
        });
        btn_gd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectResult.selectPlaceMapResult();
                dismiss();
            }
        });
        // todo
        btn_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectResult.selectEventResult();
                dismiss();
            }
        });
        btn_sg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectResult.selectComboResult();
                dismiss();
            }
        });
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getBottom();
                int with=mMenuView.findViewById(R.id.pop_layout).getLeft();
                int y = (int) event.getY();
                int x= (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height||x<with) {
                        // dismiss();
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
                Animation.RELATIVE_TO_SELF, -2,
                Animation.RELATIVE_TO_SELF, 0);
        animShow.setDuration(300);

        animHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -2);
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
