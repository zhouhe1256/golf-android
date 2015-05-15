package com.bjcathay.golf.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.golf.R;
import com.bjcathay.golf.activity.LoginActivity;
import com.bjcathay.golf.activity.RemindInfoActivity;
import com.bjcathay.golf.activity.SendFriendActivity;
import com.bjcathay.golf.constant.ErrorCode;
import com.bjcathay.golf.model.OrderModel;
import com.bjcathay.golf.model.PropModel;
import com.bjcathay.golf.model.UserModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.PreferencesConstant;
import com.bjcathay.golf.util.PreferencesUtils;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

import org.json.JSONObject;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exchange_list, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final PropModel propModel = items.get(position);
        holder.title.setText(propModel.getName());
        ImageViewAdapter.adapt(holder.imageView, propModel.getImageUrl(), R.drawable.ic_launcher);
        holder.sale.setText(propModel.getDescription());
        holder.price.setText(propModel.getInviteUserCount() + "个有效用户");
       /* convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.hintMessage("选档期");
            }
        });*/
        //int num=Integer.valueOf(number.getText().toString().trim());
        holder.toExch.setText("赠送");
        holder.toExch.setBackgroundResource(R.drawable.ic_exchange_yellow);
        holder.toExch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SendFriendActivity.class);
                intent.putExtra("id", propModel.getId());
                ViewUtil.startActivity(context, intent);
            }
        });

        return convertView;
    }

    class Holder {
        ImageView imageView;
        TextView title;
        TextView price;
        TextView sale;
        Button toExch;

        public Holder(View view) {
            imageView = ViewUtil.findViewById(view, R.id.exchange_image);
            title = ViewUtil.findViewById(view, R.id.exchange_title);
            price = ViewUtil.findViewById(view, R.id.exchange_need_number);
            sale = ViewUtil.findViewById(view, R.id.exchange_note);
            toExch = ViewUtil.findViewById(view, R.id.to_exchange);
        }
    }
}
