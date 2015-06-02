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

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.CompetitionDetailActivity;
import com.bjcathay.qt.activity.ExerciseActivity;
import com.bjcathay.qt.activity.MyMessageActivity;
import com.bjcathay.qt.activity.OrderDetailActivity;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.MessageListModel;
import com.bjcathay.qt.model.MessageModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-4-29.
 */
public class MyMessageAdapter extends BaseAdapter {
    private List<MessageModel> items;
    private Activity context;
    private int nowPosition;

    public MyMessageAdapter(List<MessageModel> items, Activity activity) {
        if (items == null) {
            items = new ArrayList<MessageModel>();
        }
        this.items = items;
        this.context = activity;
        nowPosition = 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_message_list, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final MessageModel messageModel = items.get(position);
        holder.name.setText(messageModel.getName());
        holder.day.setText(messageModel.getRelativeDate());
        holder.content.setText(messageModel.getContent());
        if ("UNREAD".equals(messageModel.getStatus()))
            holder.imageView.setVisibility(View.VISIBLE);
        else
            holder.imageView.setVisibility(View.INVISIBLE);
        if (position == 0) {
            holder.detail.setVisibility(View.VISIBLE);
        }
        if (nowPosition == position) {
            holder.detail.setVisibility(View.VISIBLE);
        } else {
            holder.detail.setVisibility(View.GONE);
        }
        holder.total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.detail.getVisibility() == View.GONE) {
                    nowPosition = position;
                    holder.detail.setVisibility(View.VISIBLE);
                    if ("UNREAD".equals(messageModel.getStatus())) {
                        MessageListModel.changeAlreadyRead(messageModel.getId()).done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                if (jsonObject.optBoolean("success")) {
                                    ((MyMessageActivity) (context)).onRefresh();
                                    notifyDataSetChanged();
                                } else {
                                    int code = jsonObject.optInt("code");
                                    DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                }
                            }
                        });
                    }
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

                    if ("ORDER".equals(messageModel.getType())) {
                        Intent intent = new Intent(context, OrderDetailActivity.class);
                        intent.putExtra("id", Long.valueOf(messageModel.getTarget()));
                        ViewUtil.startActivity(context, intent);
                    } else if ("COMPETITION".equals(messageModel.getType())) {
                        Intent intent = new Intent(context, CompetitionDetailActivity.class);
                        intent.putExtra("id", Long.valueOf(messageModel.getTarget()));
                        ViewUtil.startActivity(context, intent);
                    }
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
