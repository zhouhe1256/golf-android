
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
    private List<ProductModel> items;
    private Activity context;
    private int count = 0;

    public PlaceListAdapter(List<ProductModel> items, PlaceListActivity activity) {
        if (items == null) {
            items = new ArrayList<ProductModel>();
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
        ProductModel productModel = items.get(position);
        ImageViewAdapter.adapt(holder.imageView, productModel.getImageUrl(),
                R.drawable.exchange_default);
        holder.title.setText(productModel.getName());
        holder.price.setText(String.valueOf((int) Math.floor(productModel.getPrice())));
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
        // NORMAL|REST|REAL_TIME 正常|休息|实时
        if ("NORMAL".equals(productModel.getPriceType())) {
            holder.price.setVisibility(View.VISIBLE);
            holder.priceNote.setVisibility(View.VISIBLE);
            holder.priceRest.setVisibility(View.GONE);
            holder.priceReal.setVisibility(View.GONE);
        } else if ("REST".equals(productModel.getPriceType())) {
            holder.price.setVisibility(View.GONE);
            holder.priceNote.setVisibility(View.GONE);
            holder.priceRest.setVisibility(View.VISIBLE);
            holder.priceReal.setVisibility(View.GONE);
        } else if ("REAL_TIME".equals(productModel.getPriceType())) {
            holder.price.setVisibility(View.GONE);
            holder.priceNote.setVisibility(View.GONE);
            holder.priceRest.setVisibility(View.GONE);
            holder.priceReal.setVisibility(View.VISIBLE);
        }
        // GROUP|SPECIAL|LIMIT|NONE //团购，特卖，最低起卖，无
        if ("NORMAL".equals(productModel.getLabel())) {
            holder.hotImg.setVisibility(View.GONE);
        } else if ("HOT".equals(productModel.getLabel())) {
            holder.hotImg.setVisibility(View.VISIBLE);
        } else {
            holder.hotImg.setVisibility(View.GONE);
        }
        if ("GROUP".equals(productModel.getType())) {
            holder.tuanImg.setVisibility(View.VISIBLE);
            holder.tuanCount.setVisibility(View.VISIBLE);
            holder.temaiCount.setVisibility(View.INVISIBLE);
            holder.temaiImg.setVisibility(View.GONE);
            Date start = null;
            if (context instanceof PlaceListActivity) {
                start = ((PlaceListActivity) context).getNow();
            } else if (context instanceof PlaceListActivity) {
                start = ((ProductSearchResultActivity) context).getNow();
            }
            Date end = DateUtil.stringToDate(productModel.getEnd());
            long diff = end.getTime() - start.getTime();
            if (diff < 0) {
                holder.tuanCount.setBackgroundResource(R.drawable.stroke_bg);
                holder.tuanCount.setText("已售罄");
                holder.tuanCount.setTextColor(Color.GRAY);
                holder.tuanImg.setImageResource(R.drawable.ic_tuan_finish);
            } else {
                holder.tuanCount.setText(getcountTime(diff));
                holder.tuanCount.setBackgroundResource(R.drawable.tuangou_bg);
                holder.tuanCount.setTextColor(context.getResources().getColorStateList(
                        R.color.tuangou_color));
                holder.tuanImg.setImageResource(R.drawable.ic_tuan_icon);
            }
        } else if ("SPECIAL".equals(productModel.getType())) {
            holder.temaiImg.setVisibility(View.VISIBLE);
            holder.temaiCount.setVisibility(View.VISIBLE);
            holder.tuanImg.setVisibility(View.GONE);
            holder.tuanCount.setVisibility(View.INVISIBLE);
            if (productModel.getAmount() > 0) {
                holder.temaiCount.setText("仅剩" + productModel.getAmount() + "个名额");
                holder.temaiCount.setBackgroundResource(R.drawable.temai_bg);
                holder.temaiCount.setTextColor(context.getResources().getColorStateList(
                        R.color.order_price_color));
                holder.temaiImg.setImageResource(R.drawable.ic_te_icon);
            } else {
                holder.temaiCount.setBackgroundResource(R.drawable.texiangqingjieshu_bg);
                holder.temaiCount.setText("已售罄");
                holder.temaiCount.setTextColor(Color.GRAY);
                holder.temaiImg.setImageResource(R.drawable.ic_te_finished);
            }
        } else if ("LIMIT".equals(productModel.getType())) {
            holder.tuanCount.setVisibility(View.INVISIBLE);
            holder.tuanImg.setVisibility(View.GONE);
            holder.temaiCount.setVisibility(View.INVISIBLE);
            holder.temaiImg.setVisibility(View.GONE);
        } else {
            holder.tuanImg.setVisibility(View.GONE);
            holder.tuanCount.setVisibility(View.INVISIBLE);
            holder.temaiCount.setVisibility(View.INVISIBLE);
            holder.temaiImg.setVisibility(View.GONE);
        }
        return convertView;
    }

    private String getcountTime(long diff) {
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60))
                / (1000 * 60);
        return "仅剩" + days + "天" + hours + "小时" + minutes + "分";
    }

    class Holder {
        ImageView tuanImg;
        ImageView hotImg;
        ImageView temaiImg;
        RoundCornerImageView imageView;
        TextView title;
        TextView price;
        TextView sale;
        TextView address;
        TextView tuanCount;
        TextView temaiCount;
        TextView distance;
        TextView priceNote;
        TextView priceRest;
        TextView priceReal;

        public Holder(View view) {
            imageView = ViewUtil.findViewById(view, R.id.place_image);
            title = ViewUtil.findViewById(view, R.id.place_title);
            price = ViewUtil.findViewById(view, R.id.place_price);
            sale = ViewUtil.findViewById(view, R.id.place_sale);
            address = ViewUtil.findViewById(view, R.id.place_address);
            tuanImg = ViewUtil.findViewById(view, R.id.tuan_img);
            temaiImg = ViewUtil.findViewById(view, R.id.temai_img);
            tuanCount = ViewUtil.findViewById(view, R.id.tuan_short);
            temaiCount = ViewUtil.findViewById(view, R.id.temai_short);
            hotImg = ViewUtil.findViewById(view, R.id.hot_flag);
            distance = ViewUtil.findViewById(view, R.id.place_distance);
            priceNote = ViewUtil.findViewById(view, R.id.price_note);
            priceRest = ViewUtil.findViewById(view, R.id.place_rest);
            priceReal = ViewUtil.findViewById(view, R.id.place_real);
        }
    }
}
