package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.model.InviteModel;
import com.bjcathay.qt.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-5-25.
 */
public class MyFriendAdapter extends BaseAdapter {
    private List<InviteModel> items;
    private Activity context;

    public MyFriendAdapter(List<InviteModel> items, Activity activity) {
        if (items == null) {
            items = new ArrayList<InviteModel>();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_friends, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        InviteModel inviteModel = items.get(position);
        holder.date.setText(inviteModel.getDate().substring(0,10));
        holder.phone.setText(inviteModel.getName());
        if(inviteModel.isValid()){

            holder.status.setText("已捧场");
            holder.status.setTextColor(context.getResources().getColor(R.color.guolinbi_color));
        }else{
            holder.status.setText("未捧场");
        holder.status.setTextColor(context.getResources().getColor(R.color.exchange_text_color));}

        return convertView;
    }

    class Holder {

        TextView date;
        TextView phone;
        TextView status;



        public Holder(View view) {
            date = ViewUtil.findViewById(view, R.id.my_friend_date);
            phone = ViewUtil.findViewById(view, R.id.my_friend_phone);
            status = ViewUtil.findViewById(view, R.id.my_friend_status);


        }
    }
}
