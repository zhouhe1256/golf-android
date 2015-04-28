package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjcathay.golf.R;
import com.bjcathay.golf.adapter.BannerViewPagerAdapter;
import com.bjcathay.golf.util.SizeUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.ImageTextView;
import com.bjcathay.golf.view.JazzyViewPager;
import com.bjcathay.golf.view.MyView;
import com.bjcathay.golf.view.TopView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 * Created by dengt on 15-4-20.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private Button orderbtn;
    private Button compebtn;
    private Button exchbtn;
    private Button usercenter;
    private JazzyViewPager bannerViewPager;
    private TopView topView;
    private Activity context;
    Handler handler = new Handler();
    /*首页头部的广告轮番效果 start*/
    private LinearLayout dotoParendLinearLayout;
    ImageView[] dots;

    private int page = 1;
    private Runnable runnable;

    private void setupBanner(final List<String> recommendations) {
        bannerViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);
        bannerViewPager.setAdapter(new BannerViewPagerAdapter(context, bannerViewPager, recommendations));
        bannerViewPager.setPageMargin(0);
        if (recommendations == null || recommendations.isEmpty()) {
            return;
        }
        int count = recommendations.size();
        int widthAndHeight = SizeUtil.dip2px(context, 7);
        dots = new ImageView[count];
        int margin = SizeUtil.dip2px(context, 10);
        dotoParendLinearLayout.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView mImageView = new ImageView(context);
            dots[i] = mImageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(widthAndHeight, widthAndHeight));
            layoutParams.rightMargin = margin;

            mImageView.setBackgroundResource(R.drawable.dot_normal);
            dotoParendLinearLayout.setGravity(Gravity.RIGHT);
            dotoParendLinearLayout.addView(mImageView, layoutParams);
        }
        setImageBackground(0);

        bannerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(final int i) {
                setImageBackground(i);
                page = i;
                handler.removeCallbacks(runnable);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (page < recommendations.size() - 1) {
                            bannerViewPager.setCurrentItem(page + 1, true);
                        } else {
                            bannerViewPager.setCurrentItem(0, true);
                        }
                    }
                };
                handler.postDelayed(runnable, 5000);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //System.out.println("i" + i);
            }
        });
    }

    private void setImageBackground(int currentPosition) {
        if (dots != null) {
            for (int i = 0; i < dots.length; i++) {
                if (i == currentPosition) {
                    dots[i].setBackgroundResource(R.drawable.dot_focused);
                } else {
                    dots[i].setBackgroundResource(R.drawable.dot_normal);
                }
            }
        }
    }
    /*首页头部的广告轮番效果 end*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initData();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bannerViewPager.setCurrentItem(page, true);
            }
        }, 5000);
    }

    private void initData() {
        List<String> recommendations = new ArrayList<String>();
        recommendations.add("a");
        recommendations.add("b");
        recommendations.add("c");
        setupBanner(recommendations);
    }

    private void initView() {
        context = this;
        topView = ViewUtil.findViewById(this, R.id.top_main_layout);
        orderbtn = ViewUtil.findViewById(this, R.id.order_place);
        compebtn = ViewUtil.findViewById(this, R.id.compitetion_event);
        exchbtn = ViewUtil.findViewById(this, R.id.exchange_award);
        usercenter=ViewUtil.findViewById(this,R.id.user_center);
        bannerViewPager = ViewUtil.findViewById(this, R.id.jazzy_viewpager);
        dotoParendLinearLayout = ViewUtil.findViewById(context, R.id.doto_ly);

    }

    private void initEvent() {
        topView.setVisiable(View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
        topView.setTitleText("首頁");
        orderbtn.setOnClickListener(this);
        compebtn.setOnClickListener(this);
        exchbtn.setOnClickListener(this);
        usercenter.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.order_place:
                intent = new Intent(this, PlaceListActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.compitetion_event:
                intent = new Intent(this, CompetitionActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.exchange_award:
                intent = new Intent(this, AwardActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.user_center:
                intent = new Intent(this, LoginActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            /*case R.id.image_textview:
                Bitmap newb = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888 );
                Canvas canvasTemp = new Canvas( newb );
                canvasTemp.drawColor(Color.WHITE);
                Paint p = new Paint();
                Paint p1 = new Paint();
                String familyName = "宋体";
                Typeface font = Typeface.create(familyName, Typeface.ITALIC);
                p.setColor(Color.GRAY);
                p1.setColor(Color.WHITE);
                p.setTypeface(font);
                p.setTextSize(10);
                p.setAntiAlias(true);
                p1.setAntiAlias(true);
                p1.setTextSize(20);
               // canvasTemp.drawText("写字。。。", 30, 30, p);
                canvasTemp.drawCircle(10, 10, 10, p);
                //画出字符串 drawText(String text, float x, float y, Paint paint)
                // y 是 基准线 ，不是 字符串的 底部
                canvasTemp.drawText("x", 5, 15, p1);
                Drawable drawable = new BitmapDrawable(newb);
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackground(drawable);
                break;*/
        }
    }
}
