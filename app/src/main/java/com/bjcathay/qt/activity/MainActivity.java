
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
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.BannerViewPagerAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.BannerListModel;
import com.bjcathay.qt.model.BannerModel;
import com.bjcathay.qt.model.LocationModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.model.PushModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.uptutil.DownloadManager;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.IsLoginUtil;
import com.bjcathay.qt.util.LocationProvider;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.SizeUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.JazzyViewPager;
import com.bjcathay.qt.view.TopView;
import com.igexin.sdk.PushManager;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

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
    private LinearLayout feedbtn;
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
    private PushModel pushModel;

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
//            intent = new Intent(context, CompetitionDetailActivity.class);
//            intent.putExtra("id", webid);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            ViewUtil.startActivity(context, intent);
        }
        pushModel = (PushModel) intent.getSerializableExtra("push");
        if (pushModel != null) {
            pushNoticeIntent(pushModel);
        }
    }

    private void pushNoticeIntent(PushModel pushModel) {
        Intent intent;
        // ORDER,SYSTEM,BALANCE,PRODUCT,AD,COMPETITION,OTHER;
        switch (pushModel.getT()) {
            case ORDER:
                intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("id", Long.parseLong(pushModel.getId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                IsLoginUtil.isLogin(context, intent);
                break;
            case MESSAGE:
                break;
            case BALANCE:
                intent = new Intent(context, MyWalletActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                IsLoginUtil.isLogin(context, intent);
                break;
            case COMPETITION:
                intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra("id", Long.parseLong(pushModel.getId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ViewUtil.startActivity(context, intent);
                break;
            case PRODUCT:
                ProductModel.product(Long.parseLong(pushModel.getId()))
                        .done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                if (jsonObject.optBoolean("success")) {
                                    ProductModel productModel = JSONUtil.load(
                                            ProductModel.class,
                                            jsonObject.optJSONObject("product"));
                                    Intent intent = null;
                                    switch (productModel.getType()) {
                                        case COMBO:
                                            intent = new Intent(context,
                                                    PackageDetailActivity.class);
                                            intent.putExtra("id", productModel.getId());
                                            intent.putExtra("name", productModel.getName());
                                            intent.putExtra("product", productModel);
                                            ViewUtil.startActivity(context, intent);
                                            break;
                                        case REAL_TIME:
                                            intent = new Intent(context,
                                                    RealTOrderActivity.class);
                                            intent.putExtra("id", productModel.getId());
                                            intent.putExtra("imageurl",
                                                    productModel.getImageUrl());
                                            ViewUtil.startActivity(context, intent);
                                            break;
                                        default:
                                            intent = new Intent(context,
                                                    OrderStadiumDetailActivity.class);
                                            intent.putExtra("id", productModel.getId());
                                            intent.putExtra("imageurl",
                                                    productModel.getImageUrl());
                                            intent.putExtra("name", productModel.getName());
                                            ViewUtil.startActivity(context, intent);
                                            break;
                                    }
                                } else {
                                    String errorMessage = jsonObject.optString("message");
                                    int code = jsonObject.optInt("code");
                                    if (code == 13005) {
                                        Intent intent = new Intent(MainActivity.this,
                                                ProductOfflineActivity.class);
                                        intent.putExtra("name", "产品已下线");
                                        ViewUtil.startActivity(MainActivity.this, intent);
                                    } else {
                                        if (!StringUtils.isEmpty(errorMessage))
                                            DialogUtil.showMessage(errorMessage);
                                        else {

                                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                        }
                                    }
                                }
                            }
                        }

                        ).

                        fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                DialogUtil.showMessage(context
                                        .getString(R.string.empty_net_text));
                            }
                        }

                        );
                break;
            case AD:
                intent = new Intent(context, ExerciseActivity.class);
                intent.putExtra("url", pushModel.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ViewUtil.startActivity(context, intent);
                break;
            case OTHER:
                // intent = new Intent(context, MainActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // ViewUtil.startActivity(context, intent);
                break;
            default:
                // intent = new Intent(context, MainActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // ViewUtil.startActivity(context, intent);
                break;
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

    LocationProvider location;

    private void getLocation() {
        location = new LocationProvider(this);
        location.startLocation();
        LocationModel.SItude station = location.getLocation();
        if (station.latitude == 0.0 && station.longitude == 0.0) {
            location.updateListener();
            station = location.getLocation();
        }
        if (station.latitude == 0.0 && station.longitude == 0.0) {
            return;
        }
        location.stopListener();
    }

    private void initData() {
        PreferencesUtils.putBoolean(this, PreferencesConstant.FRIST_OPEN, false);
        getLocation();
        // LocationProvider.getLocationBybd(this);
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
        feedbtn = ViewUtil.findViewById(this, R.id.user_feedback);
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
        feedbtn.setOnClickListener(this);
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
            case R.id.selection_packages:
                intent = new Intent(this, SelectPackageActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.user_center:
                intent = new Intent(this, UserCenterActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.user_feedback:
                intent = new Intent(this, FeedbackActivity.class);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.home_share_title:
                intent = new Intent(this, ShareActivity.class);
                ViewUtil.startActivity(this, intent);
                overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                break;
            case R.id.home_call:
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
        MessageReceiver.baseActivity = this;
        MobclickAgent.onPageStart("首页");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (location != null) {
            location.stopListener();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页");
        MobclickAgent.onPause(this);
    }

}
