package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.ExerciseActivity;
import com.bjcathay.qt.model.MessageModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-4-29.
 */
public class MyMessageAdapter extends BaseAdapter {
    private List<MessageModel> items;
    private Activity context;

    public MyMessageAdapter(List<MessageModel> items, Activity activity) {
        if (items == null) {
            items = new ArrayList<MessageModel>();
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
        return items.get(i);
        // return 0;
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
        MessageModel messageModel = items.get(position);
        holder.name.setText(messageModel.getName());
        holder.day.setText(messageModel.getRelativeDate());
        holder.content.setText(messageModel.getContent());
        if ("UNREAD".equals(messageModel.getStatus()))
            holder.imageView.setVisibility(View.VISIBLE);
        else
            holder.imageView.setVisibility(View.GONE);
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
                if (holder.detail.getVisibility() == View.VISIBLE) {
                    DialogUtil.hintMessage("this", context);
                   /* Intent intent = new Intent(context, ExerciseActivity.class);
                    intent.putExtra("url", messageModel.get);
                    ViewUtil.startActivity(context, intent);*/
                }
            }
        });
        return convertView;
    }

    class Holder {
        LinearLayout detail;
        TextView toPay;
        LinearLayout total;
        TextView name;
        TextView content;
        TextView day;
        ImageView imageView;


        public Holder(View view) {
            total = ViewUtil.findViewById(view, R.id.my_message_total);
            detail = ViewUtil.findViewById(view, R.id.my_message_detail);
            toPay = ViewUtil.findViewById(view, R.id.my_message_to_pay);
            name = ViewUtil.findViewById(view, R.id.message_name);
            content = ViewUtil.findViewById(view, R.id.message_content);
            day = ViewUtil.findViewById(view, R.id.message_relativeDate);
            imageView = ViewUtil.findViewById(view, R.id.message_status);

        }
    }
}
