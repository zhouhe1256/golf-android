package com.bjcathay.golf.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-4-29.
 */
public class MyCompetitionAdapter extends BaseAdapter {
    private List<PlaceModel> items;
    private Activity context;

    public MyCompetitionAdapter(List<PlaceModel> items, Activity activity) {
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
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_competition_list, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
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
        TextView total;
        TextView status;
        TextView title;
        TextView address;
        TextView time;
        TextView count;
        TextView detail;

        public Holder(View view) {
            total=ViewUtil.findViewById(view,R.id.my_competition_total);
            status = ViewUtil.findViewById(view, R.id.my_competition_status);
            title = ViewUtil.findViewById(view, R.id.my_competition_title);
            address = ViewUtil.findViewById(view, R.id.my_competition_address);
            time = ViewUtil.findViewById(view, R.id.my_competition_time);
            count = ViewUtil.findViewById(view, R.id.my_competition_count);
            detail = ViewUtil.findViewById(view, R.id.my_competition_detail);
        }
    }
}

