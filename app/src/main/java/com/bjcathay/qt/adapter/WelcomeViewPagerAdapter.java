package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.MainActivity;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.JazzyViewPager;
import com.bjcathay.qt.view.OutlineContainer;

/**
 * Created by bjcathay on 15-6-9.
 */
public class WelcomeViewPagerAdapter extends PagerAdapter {

    private Activity context;
    private JazzyViewPager bannerViewPager;
    private int[] items;

    public WelcomeViewPagerAdapter(Activity context, JazzyViewPager bannerViewPager,
                                   int[] items) {
        this.context = context;
        this.bannerViewPager = bannerViewPager;
        this.items = items;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_welcome_banner, container, false);
        //R.drawable.sp_1, R.drawable.sp_2, R.drawable.sp_3, R.drawable.sp_4
        container.addView(convertView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageView = ViewUtil.findViewById(convertView, R.id.bg_welcome);
        imageView.setBackgroundResource(items[position]);
        if (position == 1) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainActivity.class);
                    ViewUtil.startActivity(context, intent);
                    context.finish();
                }
            });
        }
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView(bannerViewPager.findViewFromObject(position));
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == obj;
        } else {
            return view == obj;
        }
    }

}
