
package com.bjcathay.qt.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.PackageFragmentAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.fragment.ArrayFragment;
import com.bjcathay.qt.model.PackagePriceModel;
import com.bjcathay.qt.model.PriceModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.IsLoginUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopScrollView;
import com.bjcathay.qt.view.TopView;
import com.bjcathay.qt.view.WrapContentHeightViewPager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-7-28.
 */
public class PackageDetailActivity extends FragmentActivity implements
        TopScrollView.OnScrollListener, View.OnClickListener, ArrayFragment.ChangePrice {
    private TopView topView;
    // 这个是有多少个 fragment页面
    static final int NUM_ITEMS = 5;
    private PackageFragmentAdapter mAdapter;
    private WrapContentHeightViewPager mPager;
    private int nowPage;
    private ImageView cursor, cursor1;// 动画图片
    private TextView t1, t2, t3;// 页卡头标
    private TextView t11, t22, t33;// 页卡头标

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private TopScrollView scrollView;
    private ImageView imageView;
    private TextView packageTitle;
    private TextView packagePrice;
    private TextView totalPrice;
    private Button sureOrder;
    private boolean sch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);
        initView();
        InitImageView();
        InitImageViewtop();
        initEvent();
        initData();
    }

    View viewA, viewB;

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_package_detail_layout);
        imageView = ViewUtil.findViewById(this, R.id.package_detail_img);
        packageTitle = ViewUtil.findViewById(this, R.id.package_detail_name);
        packagePrice = ViewUtil.findViewById(this, R.id.package_detail_price);
        totalPrice = ViewUtil.findViewById(this, R.id.package_detail_sure_price);
        sureOrder = ViewUtil.findViewById(this, R.id.sure_order);

        viewA = findViewById(R.id.top_menu);
        viewB = findViewById(R.id.middle_menu);
        scrollView = ViewUtil.findViewById(this, R.id.hScrollView);
        t1 = (TextView) viewA.findViewById(R.id.text1);
        t2 = (TextView) viewA.findViewById(R.id.text2);
        t3 = (TextView) viewA.findViewById(R.id.text3);
        t11 = (TextView) viewB.findViewById(R.id.text1);
        t22 = (TextView) viewB.findViewById(R.id.text2);
        t33 = (TextView) viewB.findViewById(R.id.text3);
    }

    private void InitImageViewtop() {
        cursor1 = (ImageView) viewA.findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.tab_line)
                .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor1.setImageMatrix(matrix);// 设置动画初始位置
    }

    private void InitImageView() {
        cursor = (ImageView) viewB.findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.tab_line)
                .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
        t11.setOnClickListener(new MyOnClickListener(0));
        t22.setOnClickListener(new MyOnClickListener(1));
        t33.setOnClickListener(new MyOnClickListener(2));
        sureOrder.setOnClickListener(this);
        scrollView.setOnScrollListener(this);
        // 当布局的状态或者控件的可见性发生改变回调的接口
        findViewById(R.id.parent_layout).getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        // 这一步很重要，使得上面的购买布局和下面的购买布局重合
                        onScroll(scrollView.getScrollY());

                    }
                });
    }

    private Long id;
    private String name;
    PackagePriceModel packagePriceModel;

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        name = intent.getStringExtra("name");
        productModel = (ProductModel) intent.getSerializableExtra("product");
        sch = intent.getBooleanExtra("sch", false);

        packagePriceModel = productModel.getPackagePriceModel();
        topView.setTitleText(name);
        ImageViewAdapter.adapt(imageView, productModel.getImageUrl(),
                R.drawable.exchange_default);
        packageTitle.setText(productModel.getName());
        packagePrice.setText((int) Math.floor(productModel.getPrice()) + "");
        mAdapter = new PackageFragmentAdapter(this, getSupportFragmentManager(), productModel);
        mPager = (WrapContentHeightViewPager) findViewById(R.id.vPager);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        if (sch) {
            mPager.setCurrentItem(2);
        }
    }

    @Override
    public void onScroll(int scrollY) {
        int mBuyLayout2ParentTop = Math.max(scrollY, viewB.getTop());
        viewA.layout(0, mBuyLayout2ParentTop, viewA.getWidth(),
                mBuyLayout2ParentTop + viewA.getHeight());
    }

    String date;
    int number;
    private PackagePriceModel currentPrice;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.sure_order:

                Intent intent = new Intent(this, OrderCommitActivity.class);
                intent.putExtra("product", productModel);
                intent.putExtra("date", date);
                intent.putExtra("number", number);
                intent.putExtra("comboPrice", currentPrice);
                IsLoginUtil.isLogin(this, intent);
                break;
        }
    }

    ProductModel productModel;

    public ProductModel getProduct() {
        return productModel;
    }

    @Override
    public void priceChanged(int price, PackagePriceModel currentPrice, int number, String date) {
        this.currentPrice = currentPrice;
        this.number = number;
        this.date = date;
        totalPrice.setText("总金额:￥" + price * number);
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    /**
     * 2 * 页卡切换监听 3
     */
    public class MyOnPageChangeListener implements WrapContentHeightViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            t1.setTextColor(getResources().getColor(R.color.main_text_color));
            t2.setTextColor(getResources().getColor(R.color.main_text_color));
            t3.setTextColor(getResources().getColor(R.color.main_text_color));
            t11.setTextColor(getResources().getColor(R.color.main_text_color));
            t22.setTextColor(getResources().getColor(R.color.main_text_color));
            t33.setTextColor(getResources().getColor(R.color.main_text_color));
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    t1.setTextColor(getResources().getColor(R.color.order_price_color));
                    t11.setTextColor(getResources().getColor(R.color.order_price_color));
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    t2.setTextColor(getResources().getColor(R.color.order_price_color));
                    t22.setTextColor(getResources().getColor(R.color.order_price_color));
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    t3.setTextColor(getResources().getColor(R.color.order_price_color));
                    t33.setTextColor(getResources().getColor(R.color.order_price_color));
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
            cursor1.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("套餐详情页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("套餐详情页面");
        MobclickAgent.onPause(this);
    }
}
