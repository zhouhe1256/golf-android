
package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.PlaceListActivity;
import com.bjcathay.qt.activity.ProductSearchResultActivity;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.model.StadiumListModel;
import com.bjcathay.qt.model.StadiumModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.RoundCornerImageView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dengt on 15-4-20.
 */
public class PlaceListAdapter extends BaseAdapter {
    private List<StadiumModel> items;
    private Activity context;
    private int count = 0;

    public PlaceListAdapter(List<StadiumModel> items, PlaceListActivity activity) {
        if (items == null) {
            items = new ArrayList<StadiumModel>();
        }
        this.items = items;
        this.context = activity;

    }

    public void setCount(int count) {
        this.count = count;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_place_list, parent,
                    false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        StadiumModel productModel = items.get(position);
        ImageViewAdapter.adapt(holder.imageView, productModel.getImageUrl(),
                R.drawable.exchange_default);
        holder.title.setText(productModel.getName());
        if ("REAL_TIME".equals(productModel.getType())) {
            holder.priceNote.setVisibility(View.GONE);
            holder.price.setText("实时计价");
        } else {
            holder.priceNote.setVisibility(View.VISIBLE);
            holder.price.setText(String.valueOf((int) Math.floor(productModel.getPrice())));
        }
        holder.sale.setText(productModel.getFeature());
        holder.address.setText(productModel.getAddress());
        if (productModel.getDistance() == 0) {
            holder.distance.setVisibility(View.GONE);
        } else {
            holder.distance.setVisibility(View.VISIBLE);
            if (productModel.getDistance() < 10000 && productModel.getDistance() > 1000) {
                double c = ((double) productModel.getDistance() / (double) 1000);
                BigDecimal b = new BigDecimal(c);
                double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                holder.distance.setText(Double.toString(f1) + "km");
            } else if (productModel.getDistance() < 1000) {
                holder.distance.setText("0.1km");
            } else {
                holder.distance.setText((int) productModel.getDistance() / 1000 + "km");
            }
        }
        String[] tagType = productModel.getTagsType();
        holder.hotImg.setVisibility(View.GONE);
        holder.temaiImg.setVisibility(View.GONE);
        holder.fanxian.setVisibility(View.GONE);
        holder.comboImg.setVisibility(View.GONE);
        if (tagType != null)
            for (int i = 0; i < tagType.length; i++) {
                if ("人气".equals(tagType[i])) {
                    holder.hotImg.setVisibility(View.VISIBLE);
                } else if ("特卖".equals(tagType[i])) {
                    holder.temaiImg.setVisibility(View.VISIBLE);
                } else if ("返现".equals(tagType[i])) {
                    holder.fanxian.setVisibility(View.VISIBLE);
                } else if ("套餐".equals(tagType[i])) {
                    holder.comboImg.setVisibility(View.VISIBLE);
                }
            }

        return convertView;
    }

    class Holder {
        TextView fanxian;
        ImageView hotImg;
        TextView temaiImg;
        TextView comboImg;
        RoundCornerImageView imageView;
        TextView title;
        TextView price;
        TextView sale;
        TextView address;
        TextView distance;
        TextView priceNote;

        public Holder(View view) {
            imageView = ViewUtil.findViewById(view, R.id.place_image);
            title = ViewUtil.findViewById(view, R.id.place_title);
            price = ViewUtil.findViewById(view, R.id.place_price);
            sale = ViewUtil.findViewById(view, R.id.place_sale);
            address = ViewUtil.findViewById(view, R.id.place_address);
            fanxian = ViewUtil.findViewById(view, R.id.fanxian);
            temaiImg = ViewUtil.findViewById(view, R.id.temai);
            hotImg = ViewUtil.findViewById(view, R.id.hot_flag);
            distance = ViewUtil.findViewById(view, R.id.place_distance);
            comboImg = ViewUtil.findViewById(view, R.id.combo);
            priceNote = ViewUtil.findViewById(view, R.id.price_note);
        }
    }
}
