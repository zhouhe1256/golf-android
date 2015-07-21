
package com.bjcathay.qt.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.OrderCommitActivity;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.ViewUtil;

/**
 * Created by dengt on 15-7-2.
 */
public class DialogOrderInformationFragment extends PopupWindow {
    private Context context;
    private ProductModel stadiumModel;
    Button sure;
    TextView name;
    TextView time;
    TextView note;
    String date;
    int number;
    private int currentPrice;
    private Animation animShow;
    private Animation animHide;
    private View rootView;
    LinearLayout poplayout;
    boolean hasMeasured = false;
    int height;

    public DialogOrderInformationFragment() {
    }

    @SuppressLint("ValidFragment")
    public DialogOrderInformationFragment(Context context_, ProductModel stadiumModel_,
            int currentPrice_, String date_, int number_) {
        this.date = date_;
        this.number = number_;
        this.context = context_;
        this.stadiumModel = stadiumModel_;
        this.currentPrice = currentPrice_;
        this.height = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        initAnim();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.dialog_show_order_message, null);
        poplayout = ViewUtil.findViewById(rootView, R.id.pop_layout);
        name = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_name);
        time = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_time);
        note = ViewUtil.findViewById(rootView, R.id.order_need_know_note1);
        note.setMovementMethod(ScrollingMovementMethod.getInstance());
        name.setText(stadiumModel.getName());
        note.setText(stadiumModel.getPurchasingNotice());
        time.setText(DateUtil.stringToDateToOrderString(date));
        sure = ViewUtil.findViewById(rootView, R.id.sure_order);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderCommitActivity.class);
                intent.putExtra("product", stadiumModel);
                intent.putExtra("date", date);
                intent.putExtra("number", number);
                intent.putExtra("currentPrice", currentPrice);
                ViewUtil.startActivity(context, intent);
                // dismiss();
                poplayout.setVisibility(View.VISIBLE);
                poplayout.clearAnimation();
                poplayout.startAnimation(animHide);
            }
        });

        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        ViewTreeObserver vto = note.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (hasMeasured == false) {
                    int h = note.getMeasuredHeight();
                    if (h * 3 > height) {
                        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) note
                                .getLayoutParams();
                        linearParams.height = height / 3;
                        note.setLayoutParams(linearParams);
                    }
                    hasMeasured = true;

                }
                return true;
            }
        });

        rootView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = rootView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
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

    /*
     * @Override public void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState);
     * setStyle(DialogFragment.STYLE_NO_TITLE, R.style.OrderDialogTheme); final
     * DisplayMetrics dm = new DisplayMetrics();
     * getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
     * final WindowManager.LayoutParams layoutParams =
     * getDialog().getWindow().getAttributes(); layoutParams.width =
     * dm.widthPixels; //layoutParams.height =
     * getResources().getDimensionPixelOffset(R.dimen.dialog_four_items);
     * layoutParams.gravity = Gravity.BOTTOM;
     * getDialog().getWindow().setAttributes(layoutParams); }
     */
    /*
     * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
     * container, Bundle savedInstanceState) { final View rootView =
     * inflater.inflate(R.layout.dialog_show_order_message, container); name =
     * ViewUtil.findViewById(rootView, R.id.dialog_order_sure_name); time =
     * ViewUtil.findViewById(rootView, R.id.dialog_order_sure_time); note =
     * ViewUtil.findViewById(rootView, R.id.order_need_know_note1);
     * note.setMovementMethod(ScrollingMovementMethod.getInstance());
     * name.setText(stadiumModel.getName());
     * note.setText(stadiumModel.getPurchasingNotice());
     * time.setText(DateUtil.stringToDateToOrderString(date)); sure =
     * ViewUtil.findViewById(rootView, R.id.sure_order);
     * sure.setOnClickListener(new View.OnClickListener() {
     * @Override public void onClick(View view) { Intent intent = new
     * Intent(context, OrderCommitActivity.class); intent.putExtra("product",
     * stadiumModel); intent.putExtra("date", date); intent.putExtra("number",
     * number); intent.putExtra("currentPrice", currentPrice);
     * ViewUtil.startActivity(context, intent); // dismiss();
     * rootView.setVisibility(View.VISIBLE); rootView.clearAnimation();
     * rootView.startAnimation(animHide); } });
     * rootView.setVisibility(View.VISIBLE); rootView.clearAnimation();
     * rootView.startAnimation(animShow); return rootView; }
     */
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
