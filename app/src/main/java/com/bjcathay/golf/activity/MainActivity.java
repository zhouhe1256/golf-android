package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.golf.R;
import com.bjcathay.golf.adapter.BannerViewPagerAdapter;
import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.model.BannerListModel;
import com.bjcathay.golf.model.BannerModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.SizeUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.JazzyViewPager;
import com.bjcathay.golf.view.TopView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 * Created by dengt on 15-4-20.
 */
public class MainActivity extends Activity implements View.OnClickListener ,ICallback{
    private GApplication gApplication;
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

    private void setupBanner(final List<BannerModel> bannerModels) {
        bannerViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);
        bannerViewPager.setAdapter(new BannerViewPagerAdapter(context, bannerViewPager, bannerModels));
        bannerViewPager.setPageMargin(0);
        if (bannerModels == null || bannerModels.isEmpty()) {
            return;
        }
        int count = bannerModels.size();
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
                        if (page < bannerModels.size() - 1) {
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
        gApplication=GApplication.getInstance();
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
        BannerListModel.getHomeBanners().done(this);
    }
    @Override
    public void call(Arguments arguments) {
       /* Log.i("argument---",arguments.toString());
        JSONObject jsonObject = arguments.get(0);
        if(jsonObject.optBoolean("success")){*/
            BannerListModel bannerListModel=arguments.get(0);
            setupBanner(bannerListModel.getBanners());
       /* }else{
            DialogUtil.showMessage("暂无数据");
        }*/

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
        }
    }


}
