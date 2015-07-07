
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.BannerViewPagerAdapter;
import com.bjcathay.qt.adapter.GolfDetailViewPagerAdapter;
import com.bjcathay.qt.model.BannerModel;
import com.bjcathay.qt.model.StadiumModel;
import com.bjcathay.qt.util.SizeUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.JazzyViewPager;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by dengt on 15-5-20.
 */
public class GolfCourseDetailActicity extends Activity implements ICallback, View.OnClickListener,
        DeleteInfoDialog.DeleteInfoDialogResult {
    private TopView topView;
    private JazzyViewPager bannerViewPager;
    private Activity context;
    private Long id;
    Handler handler = new Handler();
    private int page = 1;
    private Runnable runnable;
    private ImageView toMap;
    private StadiumModel stadiumModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golfcourse_detail);
        initView();
        initData();
        initEvent();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bannerViewPager.setCurrentItem(page, true);
            }
        }, 5000);
    }

    private void initView() {
        context = this;
        topView = ViewUtil.findViewById(this, R.id.top_golf_detail_layout);
        bannerViewPager = ViewUtil.findViewById(this, R.id.jazzy_viewpager);
        dotoParendLinearLayout = ViewUtil.findViewById(context, R.id.doto_main_ly);
        toMap = ViewUtil.findViewById(context, R.id.golfcourse_map);
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText("球场详情");
        ((TextView) findViewById(R.id.golfcourse_phone))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DeleteInfoDialog infoDialog = new DeleteInfoDialog(context,
                                R.style.InfoDialog, "呼叫" + stadiumModel.getPhone() + "？", 0l,
                                GolfCourseDetailActicity.this);
                        infoDialog.show();
                    }
                });
        toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BaiduAddressActivity.class);
                intent.putExtra("url", getString(R.string.course_address));
                intent.putExtra("location", stadiumModel.getLat() + "," + stadiumModel.getLon());
                intent.putExtra("lat", stadiumModel.getLat());
                intent.putExtra("lon", stadiumModel.getLon());
                intent.putExtra("title", stadiumModel.getName());
                intent.putExtra("content", stadiumModel.getAddress());
                // intent.putExtra("address",stationAddress.getText().toString().trim());
                intent.putExtra("src", "A|GOLF");
                ViewUtil.startActivity(context, intent);
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        StadiumModel.stadiumDetail(id).done(this);

    }

    @Override
    public void call(Arguments arguments) {
        stadiumModel = arguments.get(0);
        topView.setTitleText(stadiumModel.getName());
        setupBanner(stadiumModel.getImageUrls());

        ((TextView) findViewById(R.id.golfcourse_description)).setText(stadiumModel
                .getDescription());
        ((TextView) findViewById(R.id.golfcourse_mode)).setText(stadiumModel.getMode());
        ((TextView) findViewById(R.id.golfcourse_data)).setText(stadiumModel.getData());
        ((TextView) findViewById(R.id.golfcourse_area)).setText(stadiumModel.getArea());
        ((TextView) findViewById(R.id.golfcourse_length)).setText(stadiumModel.getLength());
        ((TextView) findViewById(R.id.golfcourse_date)).setText(stadiumModel.getDate());
        ((TextView) findViewById(R.id.golfcourse_address)).setText(stadiumModel.getAddress());
        ((TextView) findViewById(R.id.golfcourse_greenGrass)).setText(stadiumModel.getGreenGrass());
        ((TextView) findViewById(R.id.golfcourse_fairwayGrass)).setText(stadiumModel
                .getFairwayGrass());
        ((TextView) findViewById(R.id.golfcourse_phone)).setText(Html.fromHtml("<u>"
                + stadiumModel.getPhone() + "</u>"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    /* 头部 start */
    private LinearLayout dotoParendLinearLayout;
    ImageView[] dots;

    private void setupBanner(final List<String> bannerModels) {
        bannerViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);
        bannerViewPager.setAdapter(new GolfDetailViewPagerAdapter(context, bannerViewPager,
                bannerModels));
        bannerViewPager.setPageMargin(0);
        if (bannerModels == null || bannerModels.isEmpty()) {
            return;
        }
        int count = bannerModels.size();
        int widthAndHeight = SizeUtil.dip2px(context, 7);
        dots = new ImageView[count];
        int margin = SizeUtil.dip2px(context, 10);
        dotoParendLinearLayout.removeAllViews();
        if (bannerModels.size() == 1)
            dotoParendLinearLayout.setVisibility(View.INVISIBLE);
        for (int i = 0; i < count; i++) {
            ImageView mImageView = new ImageView(context);
            dots[i] = mImageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(widthAndHeight, widthAndHeight));
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
            public void onPageSelected(int i) {
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
                // System.out.println("i" + i);
            }
        });
    }

    private void setImageBackground(int currentPosition) {
        /*
         * if (currentPosition == 4) {
         * dotoParendLinearLayout.setVisibility(View.INVISIBLE); } else {
         * dotoParendLinearLayout.setVisibility(View.VISIBLE); }
         */
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

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + stadiumModel.getPhone()));
            startActivity(intent);
        }
    }

    /* 头部 end */
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
