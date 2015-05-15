package com.bjcathay.golf.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.golf.R;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.model.StadiumModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-4-20.
 */
public class PlaceListAdapter extends BaseAdapter {
    private List<StadiumModel> items;
    private Activity context;
    private int count = 0;

    public PlaceListAdapter(List<StadiumModel> items, Activity activity) {
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
       /* if (count == 0) {
            count = 10;
        }
        return count;*/
    }

    @Override
    public Object getItem(int i) {
        // return items.get(i);
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_place_list, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        StadiumModel stadiumModel = items.get(position);
        ImageViewAdapter.adapt(holder.imageView, stadiumModel.getImageUrl(), R.drawable.ic_launcher);
        holder.title.setText(stadiumModel.getName());
        holder.price.setText("￥" + stadiumModel.getPrice());
        holder.sale.setText(stadiumModel.getPriceInclude());
        holder.address.setText(stadiumModel.getAddress());
        // LIMITED|SPECIAL|COMMON, //限购，团购，平常
        if ("COMMON".equals(stadiumModel.getType())) {
            holder.tuanImg.setVisibility(View.GONE);
            holder.tuanCount.setVisibility(View.GONE);
            holder.temaiCount.setVisibility(View.GONE);
            holder.temaiImg.setVisibility(View.GONE);
        } else if ("LIMITED".equals(stadiumModel.getType())) {
            holder.temaiImg.setVisibility(View.VISIBLE);
            holder.temaiCount.setVisibility(View.VISIBLE);
            holder.tuanImg.setVisibility(View.GONE);
            holder.tuanCount.setVisibility(View.GONE);
            holder.temaiCount.setText("剩余" + stadiumModel.getRemainingAmount() + "人");
        } else if ("SPECIAL".equals(stadiumModel.getType())) {
            holder.tuanCount.setVisibility(View.VISIBLE);
            holder.tuanImg.setVisibility(View.VISIBLE);
            holder.temaiCount.setVisibility(View.GONE);
            holder.temaiImg.setVisibility(View.GONE);
            holder.tuanCount.setText("还差" + stadiumModel.getRemainingAmount() + "成团");
        }

       /* convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.hintMessage("选档期");
            }
        });*/
        return convertView;
    }

    class Holder {
        ImageView tuanImg;
        ImageView temaiImg;
        ImageView imageView;
        TextView title;
        TextView price;
        TextView sale;
        TextView address;
        TextView tuanCount;
        TextView temaiCount;

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
        }
    }
}
