package com.bjcathay.qt.adapter;

import android.app.Activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.CompetitionDetailActivity;
import com.bjcathay.qt.activity.ExerciseActivity;
import com.bjcathay.qt.activity.OrderStadiumDetailActivity;
import com.bjcathay.qt.model.BannerModel;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.JazzyViewPager;
import com.bjcathay.qt.view.OutlineContainer;

import java.util.List;

/**
 * Created by bjcathay on 15-4-24.
 */
public class BannerViewPagerAdapter extends PagerAdapter {
    private Activity context;
    private JazzyViewPager bannerViewPager;
    private List<BannerModel> items;

    public BannerViewPagerAdapter(Activity context, JazzyViewPager bannerViewPager,
                                  List<BannerModel> recommendations) {
        this.context = context;
        this.bannerViewPager = bannerViewPager;
        this.items = recommendations;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView(bannerViewPager.findViewFromObject(position));
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == obj;
        } else {
            return view == obj;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_home_banner, container, false);

        final BannerModel bannerModel = items.get(position);

        ImageView bgView = ViewUtil.findViewById(convertView, R.id.bg);
        // final TextView bannerTitleView = ViewUtil.findViewById(convertView, R.id.banner_title);

        ImageViewAdapter.adapt(bgView, bannerModel.getImageUrl(), R.drawable.exchange_default);

        // bannerTitleView.setTextColor(Color.WHITE);
        // bannerTitleView.setText(carouselModel.getTitle());
        //bannerTitleView.setBackgroundResource(R.drawable.ic_launcher);

        container.addView(convertView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        bannerViewPager.setObjectForPosition(convertView, position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String target = bannerModel.getType();
                Intent intent;
                if ("SITE".equals(target)) {
                    intent = new Intent(context, ExerciseActivity.class);
                    intent.putExtra("url", bannerModel.getTarget());
                    ViewUtil.startActivity(context, intent);
                } else if ("PRODUCT".equals(target)) {
                    intent = new Intent(context, OrderStadiumDetailActivity.class);
                    intent.putExtra("imageurl", bannerModel.getImageUrl());
                    intent.putExtra("id", Long.valueOf(bannerModel.getTarget()));
                    ViewUtil.startActivity(context, intent);
                } else if ("COMPETITION".equals(target)) {
                    intent = new Intent(context, CompetitionDetailActivity.class);
                    intent.putExtra("id", Long.parseLong(bannerModel.getTarget()));
                    ViewUtil.startActivity(context, intent);
                }
                // DialogUtil.hintMessage(carouselModel, context);
            }
        });
        return convertView;
    }
}
