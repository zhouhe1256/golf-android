package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.SendFriendActivity;
import com.bjcathay.qt.model.PropModel;
import com.bjcathay.qt.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-5-11.
 */
public class MyPropAdapter extends BaseAdapter {
    private List<PropModel> items;
    private Activity context;

    public MyPropAdapter(List<PropModel> items, Activity activity) {
        if (items == null) {
            items = new ArrayList<PropModel>();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_exchange_list, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final PropModel propModel = items.get(position);
        holder.title.setText(propModel.getName());
        ImageViewAdapter.adapt(holder.imageView, propModel.getImageUrl(), R.drawable.exchange_default);
        holder.sale.setText(propModel.getDescription());
        holder.number.setText(Integer.toString(propModel.getAmount()));
        //int num=Integer.valueOf(number.getText().toString().trim());
        holder.toExch.setText("赠送");
        holder.toExch.setBackgroundResource(R.drawable.ic_exchange_yellow);
        holder.toExch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SendFriendActivity.class);
                intent.putExtra("id", propModel.getId());
                intent.putExtra("name", propModel.getName());
                ViewUtil.startActivity(context, intent);
            }
        });

        return convertView;
    }

    class Holder {
        ImageView imageView;
        TextView title;
        TextView sale;
        Button toExch;
        TextView number;

        public Holder(View view) {
            imageView = ViewUtil.findViewById(view, R.id.exchange_image);
            title = ViewUtil.findViewById(view, R.id.exchange_title);
            sale = ViewUtil.findViewById(view, R.id.exchange_note);
            toExch = ViewUtil.findViewById(view, R.id.to_exchange);
            number = ViewUtil.findViewById(view, R.id.exchange_number);
        }
    }
}
