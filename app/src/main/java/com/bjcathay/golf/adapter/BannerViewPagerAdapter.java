package com.bjcathay.golf.adapter;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.golf.R;
import com.bjcathay.golf.activity.ExerciseActivity;
import com.bjcathay.golf.model.BannerModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.JazzyViewPager;
import com.bjcathay.golf.view.OutlineContainer;

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

        final BannerModel carouselModel = items.get(position);

        ImageView bgView = ViewUtil.findViewById(convertView, R.id.bg);
        final TextView bannerTitleView = ViewUtil.findViewById(convertView, R.id.banner_title);

         ImageViewAdapter.adapt(bgView, carouselModel.getImageUrl(), R.drawable.ic_launcher);

        bannerTitleView.setTextColor(Color.WHITE);
        bannerTitleView.setText(carouselModel.getTitle());
        //bannerTitleView.setBackgroundResource(R.drawable.ic_launcher);

        container.addView(convertView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        bannerViewPager.setObjectForPosition(convertView, position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ExerciseActivity.class);
                intent.putExtra("url",carouselModel.getWebsiteUrl());
                ViewUtil.startActivity(context,intent);
               // DialogUtil.hintMessage(carouselModel, context);
            }
        });
        return convertView;
    }
}
