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
import com.bjcathay.golf.model.OrderModel;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.ViewUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bjcathay on 15-4-28.
 */
public class MyOrderAdapter extends BaseAdapter {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm", Locale.getDefault());
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<OrderModel> items;
    private Activity context;

    public MyOrderAdapter(List<OrderModel> items, Activity activity) {
        if (items == null) {
            items = new ArrayList<OrderModel>();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_order_list, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        OrderModel orderModel = items.get(position);
        holder.detail.setText("订单号: " + orderModel.getOrderId());
        ImageViewAdapter.adapt(holder.img, orderModel.getImageUrl(), R.drawable.ic_launcher);
        holder.title.setText(orderModel.getTitle());
        holder.discount.setText(orderModel.getPackageContent());
        holder.price.setText("￥:" + orderModel.getTotalPrice());

        holder.number.setText("预约人数:" + orderModel.getPeopleNumber() + "人");
        Date date = null;
        try {
            date = df.parse(orderModel.getStartAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String currentTime = sdf.format(date);
        holder.time.setText("预约时间:" + currentTime);
        //PENDING|PROCESSING|UNPAID|PAID|FINISH|CANCEL 待确认 确认中 待支付 已支付 已完成 已取消
        if ("PENDING".equals(orderModel.getStatus()))
            holder.status.setText("待确认");
        else if ("PROCESSING".equals(orderModel.getStatus()))
            holder.status.setText("待确认");
        else if ("UNPAID".equals(orderModel.getStatus()))
            holder.status.setText("未支付");
        else if ("PAID".equals(orderModel.getStatus()))
            holder.status.setText("已支付");
        else if ("FINISH".equals(orderModel.getStatus()))
            holder.status.setText("已完成");
        else if ("CANCEL".equals(orderModel.getStatus()))
            holder.status.setText("已取消");

        if ("COMMON".equals(orderModel.getType())) {
            holder.tuan.setVisibility(View.GONE);
            holder.temai.setVisibility(View.GONE);
        } else if ("LIMITED".equals(orderModel.getType())) {
            holder.temai.setVisibility(View.VISIBLE);
            holder.tuan.setVisibility(View.GONE);
        } else if ("SPECIAL".equals(orderModel.getType())) {
            holder.tuan.setVisibility(View.VISIBLE);
            holder.temai.setVisibility(View.GONE);
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
        TextView detail;
        TextView title;
        TextView discount;
        TextView price;
        TextView status;
        ImageView img;
        TextView time;
        TextView number;
        ImageView tuan;
        ImageView temai;

        public Holder(View view) {
            img = ViewUtil.findViewById(view, R.id.my_order_img);
            detail = ViewUtil.findViewById(view, R.id.my_order_detail);
            title = ViewUtil.findViewById(view, R.id.my_order_title);
            price = ViewUtil.findViewById(view, R.id.my_order_price);
            discount = ViewUtil.findViewById(view, R.id.my_order_discount);
            status = ViewUtil.findViewById(view, R.id.my_order_status);
            time = ViewUtil.findViewById(view, R.id.my_order_time);
            number = ViewUtil.findViewById(view, R.id.my_order_number);
            tuan = ViewUtil.findViewById(view, R.id.my_order_tuangou);
            temai = ViewUtil.findViewById(view, R.id.my_order_temai);
        }
    }
}
