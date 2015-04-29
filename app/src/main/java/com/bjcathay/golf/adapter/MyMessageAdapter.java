package com.bjcathay.golf.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-4-29.
 */
public class MyMessageAdapter extends BaseAdapter {
    private List<PlaceModel> items;
    private Activity context;

    public MyMessageAdapter(List<PlaceModel> items, Activity activity) {
        if (items == null) {
            items = new ArrayList<PlaceModel>();
        }
        this.items = items;
        this.context = activity;
    }

    @Override
    public int getCount() {
        // return items == null ? 0 : items.size();
        return 10;
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
        final Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_message_list, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.detail.getVisibility() == View.GONE) {
                    holder.detail.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                } else {
                    holder.detail.setVisibility(View.GONE);
                }
            }
        });
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.detail.getVisibility() == View.VISIBLE){
                    DialogUtil.hintMessage("this",context);
                }
            }
        });
        return convertView;
    }

    class Holder {
        LinearLayout detail;
        TextView toPay;
        LinearLayout total;


        public Holder(View view) {
            total = ViewUtil.findViewById(view, R.id.my_message_total);
            detail = ViewUtil.findViewById(view, R.id.my_message_detail);
            toPay = ViewUtil.findViewById(view, R.id.my_message_to_pay);

        }
    }
}
