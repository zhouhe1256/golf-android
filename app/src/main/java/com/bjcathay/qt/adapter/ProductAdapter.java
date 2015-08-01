
package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.ProductActivity;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.RoundCornerImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-7-27.
 */
public class ProductAdapter extends BaseAdapter {
    private List<ProductModel> items;
    private Activity context;

    public ProductAdapter(List<ProductModel> items, ProductActivity activity) {
        if (items == null) {
            items = new ArrayList<ProductModel>();
        }
        this.items = items;
        this.context = activity;

    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
        // return 10;
    }

    @Override
    public Object getItem(int i) {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product_list, parent,
                    false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ProductModel productModel = items.get(position);
        ImageViewAdapter.adapt(holder.imageView, productModel.getImageUrl(),
                R.drawable.exchange_default);
        holder.title.setText(productModel.getName());
        holder.price.setText(String.valueOf((int) Math.floor(productModel.getPrice())));

        String[] tagType = productModel.getTagsType();
        holder.renqi.setVisibility(View.GONE);
        holder.temai.setVisibility(View.GONE);
        holder.fanxian.setVisibility(View.GONE);
        if(tagType!=null)
        for (int i = 0; i < tagType.length; i++) {
            if ("人气".equals(tagType[i])) {
                holder.renqi.setVisibility(View.VISIBLE);
            } else if ("特卖".equals(tagType[i])) {
                holder.temai.setVisibility(View.VISIBLE);
            } else if ("返现".equals(tagType[i])) {
                holder.fanxian.setVisibility(View.VISIBLE);
            }
        }
        holder.desp.setText(productModel.getDescription());
        return convertView;
    }

    class Holder {
        RoundCornerImageView imageView;
        TextView title;
        TextView price;
        TextView temai;
        TextView fanxian;
        TextView desp;
        ImageView renqi;

        public Holder(View view) {
            imageView = ViewUtil.findViewById(view, R.id.place_image);
            title = ViewUtil.findViewById(view, R.id.place_title);
            price = ViewUtil.findViewById(view, R.id.place_price);
            temai = ViewUtil.findViewById(view, R.id.temai);
            fanxian = ViewUtil.findViewById(view, R.id.fanxian);
            desp = ViewUtil.findViewById(view, R.id.discription);
            renqi = ViewUtil.findViewById(view, R.id.hot_flag);
        }
    }
}
