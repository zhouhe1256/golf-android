
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.BannerViewPagerAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.BannerListModel;
import com.bjcathay.qt.model.BannerModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.uptutil.DownloadManager;
import com.bjcathay.qt.util.LocationUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.SizeUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.JazzyViewPager;
import com.bjcathay.qt.view.TopView;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 主页面 Created by dengt on 15-4-20.
 */
public class MainActivity extends Activity implements View.OnClickListener, ICallback,
        DeleteInfoDialog.DeleteInfoDialogResult {
    private GApplication gApplication;
    private LinearLayout orderbtn;
    private LinearLayout compebtn;
    private LinearLayout exchbtn;
    private LinearLayout usercenter;
    private LinearLayout shareBtn;
    private static final String FILE_NAME = "qtgolf.png";
    public static String TEST_IMAGE;
    private JazzyViewPager bannerViewPager;
    private TopView topView;
    private Activity context;
    private Handler handler = new Handler();
    private LinearLayout dotoParendLinearLayout;
    private ImageView[] dots;

    private int page = 1;
    private Runnable runnable;

    private void setupBanner(final List<BannerModel> bannerModels) {
        bannerViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);
        bannerViewPager.setAdapter(new BannerViewPagerAdapter(context, bannerViewPager,
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
        else
            dotoParendLinearLayout.setVisibility(View.VISIBLE);
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
            }
        });
    }

    private void setImageBackground(int currentPosition) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        new Thread() {
            public void run() {
                initImagePath();
            }
        }.start();
        Intent intent = getIntent();
        if ("web".equals(intent.getAction())) {
            Long webid = intent.getLongExtra("id", 0l);
            intent = new Intent(context, CompetitionDetailActivity.class);
            intent.putExtra("id", webid);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ViewUtil.startActivity(context, intent);
        }
    }

    // 把图片从drawable复制到sdcard中
    private void initImagePath() {
        try {
            String cachePath = com.mob.tools.utils.R.getCachePath(this, null);
            TEST_IMAGE = cachePath + FILE_NAME;
            File file = new File(TEST_IMAGE);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(getResources(),
                        R.drawable.icon_share_logo);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }

        } catch (Throwable t) {
            t.printStackTrace();
            TEST_IMAGE = null;
        }
    }

    private void initData() {
        PreferencesUtils.putBoolean(this, PreferencesConstant.FRIST_OPEN, false);
        LocationUtil.getLocationBybd(this);
        BannerListModel.getHomeBanners().done(this);
        if (GApplication.getInstance().isLogin() && GApplication.getInstance().isPushID() == false) {
            String latitude = PreferencesUtils.getString(this, PreferencesConstant.LATITUDE);
            String longitude = PreferencesUtils.getString(this, PreferencesConstant.LONGITUDE);
            UserModel.updateUserInfo(null, null, PushManager.getInstance().getClientid(this),
                    longitude, latitude).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    UserModel userModel = arguments.get(0);
                    if (userModel != null) {
                        GApplication.getInstance().setPushID(true);
                    }
                }
            });
        }
        // 版本更新
        DownloadManager downManger = new DownloadManager(this, true);
        downManger.checkDownload(true);
    }

    @Override
    public void call(Arguments arguments) {
        BannerListModel bannerListModel = arguments.get(0);
        setupBanner(bannerListModel.getBanners());
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
    }

    private void initEvent() {
        topView.setVisiable(View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
        topView.setTitleText("7铁");
        topView.setPhonebtnVisiable();
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
            case R.id.title_phone_img:
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(this,
                        R.style.InfoDialog, getResources().getString(R.string.service_tel_format)
                                .toString().trim(), "呼叫", 0l, this);
                infoDialog.show();
                break;
        }
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + getResources().getString(R.string.service_tel).toString().trim()));
            this.startActivity(intent);
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

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
