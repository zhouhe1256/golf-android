package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.JazzyViewPager;
import com.bjcathay.qt.view.OutlineContainer;

import java.util.List;

/**
 * Created by bjcathay on 15-5-20.
 */
public class GolfDetailViewPagerAdapter extends PagerAdapter {
    private Activity context;
    private JazzyViewPager bannerViewPager;
    private List<String> items;

    public GolfDetailViewPagerAdapter(Activity context, JazzyViewPager bannerViewPager,
                                      List<String> recommendations) {
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
        final String bannerModel = items.get(position);
        ImageView bgView = ViewUtil.findViewById(convertView, R.id.bg);
        ImageViewAdapter.adapt(bgView, bannerModel, R.drawable.exchange_default);

        container.addView(convertView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        bannerViewPager.setObjectForPosition(convertView, position);
        return convertView;
    }
}
