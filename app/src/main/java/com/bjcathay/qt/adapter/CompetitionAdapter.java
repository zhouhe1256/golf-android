
package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.EventModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-4-28.
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
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_competition_list,
                    parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        EventModel eventModel = items.get(position);
        ImageViewAdapter.adapt(holder.img, eventModel.getImageUrl(), R.color.event_title_img_color);

        if (eventModel.getStatus() == 4) {
            holder.status.setBackgroundResource(R.drawable.ic_attend_end_bg);
        } else
            holder.status.setBackgroundResource(R.drawable.ic_attend_bg);
        holder.title.setText(eventModel.getName());
        holder.address.setText("地址：" + eventModel.getAddress());
        holder.time.setText("时间：" + DateUtil.shortDateString(eventModel.getDate()));
        holder.count.setText("参加人数：" + eventModel.getSignUpAmount());
        if (eventModel.getSignedAmount() == 0) {
            holder.detail.setTextColor(Color.BLACK);
        } else {
            holder.detail.setTextColor(context.getResources().getColor(R.color.order_price_color));
        }
      //  if("已结束".equals(eventModel.getStatusLabel())||"即将开始".equals(eventModel.getStatusLabel())){
            holder.status.setText(eventModel.getStatusLabel());
            holder.status.setVisibility(View.VISIBLE);
            holder.already.setVisibility(View.GONE);
//        }else{
//            holder.detail.setText(eventModel.getSignedAmount() + "");
//            holder.status.setVisibility(View.GONE);
//            holder.already.setVisibility(View.VISIBLE);
//        }
       // holder.status.setText(eventModel.getStatusLabel());

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
        LinearLayout already;

        public Holder(View view) {
            status = ViewUtil.findViewById(view, R.id.competition_status);
            title = ViewUtil.findViewById(view, R.id.competition_title);
            address = ViewUtil.findViewById(view, R.id.competition_address);
            time = ViewUtil.findViewById(view, R.id.competition_time);
            count = ViewUtil.findViewById(view, R.id.competition_count);
            detail = ViewUtil.findViewById(view, R.id.competition_detail);
            img = ViewUtil.findViewById(view, R.id.compete_logo_img);
            already=ViewUtil.findViewById(view,R.id.already_persons);
        }
    }
}
