package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.EventModel;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-4-28.
 */
public class CompetitionAdapter extends BaseAdapter {
    private List<EventModel> items;
    private Activity context;

    public CompetitionAdapter(List<EventModel> items, Activity activity) {
        if (items == null) {
            items = new ArrayList<EventModel>();
        }
        this.items = items;
        this.context = activity;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
        //return 10;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_competition_list, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        EventModel eventModel = items.get(position);
        ImageViewAdapter.adapt(holder.img, eventModel.getImageUrl(), R.drawable.ic_default_user);
        if (eventModel.isAttend()) {
            holder.status.setText("已报名");
            holder.status.setBackgroundResource(R.drawable.ic_attend_bg);
        } else {
            if ("NOT_START".equals(eventModel.getStatus())) {
                holder.status.setText("未开始");
                holder.status.setBackgroundResource(R.drawable.ic_attend_bg);
            } else if ("START".equals(eventModel.getStatus())) {

                if (eventModel.isAttend()) {
                    holder.status.setText("已报名");
                    holder.status.setBackgroundResource(R.drawable.ic_attend_bg);
                } else {
                    holder.status.setText("报名中");
                    holder.status.setBackgroundResource(R.drawable.ic_attend_bg);
                }
            } else if ("END".equals(eventModel.getStatus())) {
                holder.status.setText("报名结束");
                holder.status.setBackgroundResource(R.drawable.ic_attend_end_bg);
            } else if ("DONE".equals(eventModel.getStatus())) {
                holder.status.setText("已完成");
                holder.status.setBackgroundResource(R.drawable.ic_attend_end_bg);
            }
        }

        holder.title.setText(eventModel.getName());
        holder.address.setText("地址:" + eventModel.getAddress());
        holder.time.setText("时间:" + eventModel.getStartAt().substring(0, 10) + "~" + eventModel.getEndAt().substring(0, 10));
        holder.count.setText("参加人数:" + eventModel.getSignUpAmount());
        if (eventModel.getSignedAmount() == 0)
            holder.detail.setText("正在报名");
        else
            holder.detail.setText("已有" + eventModel.getSignedAmount() + "人报名");

       /* convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.hintMessage("选档期");
            }
        });*/
        return convertView;
    }

    class Holder {
        TextView status;
        TextView title;
        TextView address;
        TextView time;
        TextView count;
        TextView detail;
        CircleImageView img;

        public Holder(View view) {
            status = ViewUtil.findViewById(view, R.id.competition_status);
            title = ViewUtil.findViewById(view, R.id.competition_title);
            address = ViewUtil.findViewById(view, R.id.competition_address);
            time = ViewUtil.findViewById(view, R.id.competition_time);
            count = ViewUtil.findViewById(view, R.id.competition_count);
            detail = ViewUtil.findViewById(view, R.id.competition_detail);
            img = ViewUtil.findViewById(view, R.id.compete_logo_img);
        }
    }
}
