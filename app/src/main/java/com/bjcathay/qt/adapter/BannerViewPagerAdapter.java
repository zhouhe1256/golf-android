
package com.bjcathay.qt.adapter;

import android.app.Activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.CompetitionDetailActivity;
import com.bjcathay.qt.activity.EventDetailActivity;
import com.bjcathay.qt.activity.ExerciseActivity;
import com.bjcathay.qt.activity.OrderStadiumDetailActivity;
import com.bjcathay.qt.activity.PackageDetailActivity;
import com.bjcathay.qt.activity.ProductOfflineActivity;
import com.bjcathay.qt.activity.RealTOrderActivity;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.BannerModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.JazzyViewPager;
import com.bjcathay.qt.view.OutlineContainer;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by dengt on 15-4-24.
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
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_home_banner,
                container, false);

        final BannerModel bannerModel = items.get(position);
        ImageView bgView = ViewUtil.findViewById(convertView, R.id.bg);
        ImageViewAdapter.adapt(bgView, bannerModel.getImageUrl(), R.drawable.exchange_default);
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
                    ProductModel.product(Long.valueOf(bannerModel.getTarget()))
                            .done(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    JSONObject jsonObject = arguments.get(0);
                                    if (jsonObject.optBoolean("success")) {
                                        ProductModel productModel = JSONUtil.load(
                                                ProductModel.class,
                                                jsonObject.optJSONObject("product"));
                                        // ProductModel productModel =
                                        // arguments.get(0);
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
                                                intent.putExtra("name",productModel.getName());
                                                ViewUtil.startActivity(context, intent);
                                                break;
                                            default:
                                                intent = new Intent(context,
                                                        OrderStadiumDetailActivity.class);
                                                intent.putExtra("id", productModel.getId());
                                                intent.putExtra("imageurl",
                                                        productModel.getImageUrl());
                                                intent.putExtra("name",productModel.getName());
                                                ViewUtil.startActivity(context, intent);
                                                break;
                                        }
                                    } else {
                                        String errorMessage = jsonObject.optString("message");
                                        int code = jsonObject.optInt("code");
                                        if (code == 13005) {
                                            Intent intent = new Intent(context,
                                                    ProductOfflineActivity.class);
                                            intent.putExtra("name", bannerModel.getTitle());
                                            ViewUtil.startActivity(context, intent);
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

                    // intent = new Intent(context,
                    // OrderStadiumDetailActivity.class);
                    // intent.putExtra("imageurl", bannerModel.getImageUrl());
                    // intent.putExtra("id",
                    // Long.valueOf(bannerModel.getTarget()));
          // ViewUtil.startActivity(context, intent);
                } else if ("COMPETITION".equals(target)) {
                    intent = new Intent(context, EventDetailActivity.class);
                    intent.putExtra("id", Long.parseLong(bannerModel.getTarget()));
                    ViewUtil.startActivity(context, intent);
                }
            }
        });
        return convertView;
    }
}
