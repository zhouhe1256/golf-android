package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.BannerViewPagerAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.BannerListModel;
import com.bjcathay.qt.model.BannerModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.SizeUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.JazzyViewPager;
import com.bjcathay.qt.view.TopView;
import com.igexin.sdk.PushManager;

import java.util.List;

/**
 * 主页面
 * Created by dengt on 15-4-20.
 */
public class MainActivity extends Activity implements View.OnClickListener, ICallback {
    private GApplication gApplication;
    private LinearLayout orderbtn;
    private LinearLayout compebtn;
    private LinearLayout exchbtn;
    private LinearLayout usercenter;
    private LinearLayout shareBtn;
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

            mImageView.setBackgroundResource(R.drawable.pic_22);
            dotoParendLinearLayout.setGravity(Gravity.CENTER);
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
        if (currentPosition == 4) {
            dotoParendLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            dotoParendLinearLayout.setVisibility(View.VISIBLE);
        }
        if (dots != null) {
            for (int i = 0; i < dots.length; i++) {
                if (i == currentPosition) {
                    dots[i].setBackgroundResource(R.drawable.pic_11);
                } else {
                    dots[i].setBackgroundResource(R.drawable.pic_22);
                }
            }
        }
    }
    /*首页头部的广告轮番效果 end*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        gApplication = GApplication.getInstance();
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
        if (GApplication.getInstance().isLogin() && GApplication.getInstance().isPushID() == false)
            UserModel.updateUserInfo(null, null, PushManager.getInstance().getClientid(this), null, null).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    UserModel userModel = arguments.get(0);
                    if (userModel != null) {
                        GApplication.getInstance().setPushID(true);
                    }
                }
            });
    }

    @Override
    public void call(Arguments arguments) {
       /* Log.i("argument---",arguments.toString());
        JSONObject jsonObject = arguments.get(0);
        if(jsonObject.optBoolean("success")){*/
        BannerListModel bannerListModel = arguments.get(0);
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
        usercenter = ViewUtil.findViewById(this, R.id.user_center);
        shareBtn = ViewUtil.findViewById(this, R.id.home_share_title);
        bannerViewPager = ViewUtil.findViewById(this, R.id.jazzy_viewpager);
        dotoParendLinearLayout = ViewUtil.findViewById(context, R.id.doto_main_ly);
       /* Drawable drawable1 = getResources().getDrawable(R.drawable.home_order);
        drawable1.setBounds(0, 0, 100, 200);
        orderbtn.setCompoundDrawables(null, null, null, drawable1);*/
       /* compebtn.setCompoundDrawables(null, null, null, getResources().getDrawable(R.drawable.home_order));
        exchbtn.setCompoundDrawables(null, null, null, getResources().getDrawable(R.drawable.home_order));
        usercenter.setCompoundDrawables(null, null, null, getResources().getDrawable(R.drawable.home_order));
        shareBtn.setCompoundDrawables(null, null, null, getResources().getDrawable(R.drawable.home_order));*/


    }

    private void initEvent() {
        topView.setVisiable(View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
        topView.setTitleText("7铁");
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
                intent = new Intent(this, UserCenterActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.home_share_title:
                intent = new Intent(this, ShareActivity.class);
                ViewUtil.startActivity(this, intent);
                overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                break;
        }
    }


}
