
package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.RoundCornerImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-4-28.
 */
public class MyOrderAdapter extends BaseAdapter {

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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_order_list, parent,
                    false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        OrderModel orderModel = items.get(position);
        ImageViewAdapter.adapt(holder.img, orderModel.getImageUrl(), R.drawable.exchange_default);
        holder.title.setText(orderModel.getTitle());
        holder.discount.setText(orderModel.getPriceInclude());
        if (orderModel.getPeopleNumber() == 0)
            holder.price.setText("" + (int) Math.floor(orderModel.getTotalPrice()) + "+");
        else
            holder.price.setText("" + (int) Math.floor(orderModel.getTotalPrice()));

        holder.number
                .setText((orderModel.getPeopleNumber() == 0 ? "4人+" : (orderModel
                                .getPeopleNumber() + "人")));
        String currentTime = DateUtil.stringToDateToOrderString(orderModel.getDate());
        holder.time.setText(currentTime);
        if(ProductType.prdtType.EVENT.equals(orderModel.getType())){
            holder.contain.setVisibility(View.GONE);
        }else{
            holder.contain.setVisibility(View.VISIBLE);
        }
        // PENDING|PROCESSING|UNPAID|PAID|FINISH|CANCEL 待确认 确认中 待支付 已支付 已完成 已取消
        // PENDING|UNPAID|PAID|FINISH|CANCEL
        if ("PENDING".equals(orderModel.getStatus()))
            holder.status.setText("确认中");
        else if ("UNPAID".equals(orderModel.getStatus()))
            holder.status.setText("待支付");
        else if ("PAID".equals(orderModel.getStatus()))
            holder.status.setText("已支付");
        else if ("FINISH".equals(orderModel.getStatus()))
            holder.status.setText("已完成");
        else if ("CANCEL".equals(orderModel.getStatus()))
            holder.status.setText("已取消");
        else if ("CONFIRMED".equals(orderModel.getStatus())){
            holder.status.setText("已确认");
        }
      /*  if ("SPECIAL".equals(orderModel.getType())) {
            holder.temai.setVisibility(View.VISIBLE);
            holder.tuan.setVisibility(View.GONE);
        } else if ("GROUP".equals(orderModel.getType())) {
            holder.tuan.setVisibility(View.VISIBLE);
            holder.temai.setVisibility(View.GONE);
        } else {
            holder.tuan.setVisibility(View.GONE);
            holder.temai.setVisibility(View.GONE);
        }*/
        return convertView;
    }

    class Holder {
        TextView title;
        TextView discount;
        TextView price;
        TextView status;
        RoundCornerImageView img;
        TextView time;
        TextView number;
        LinearLayout contain;
        public Holder(View view) {
            img = ViewUtil.findViewById(view, R.id.my_order_img);
            title = ViewUtil.findViewById(view, R.id.my_order_title);
            price = ViewUtil.findViewById(view, R.id.my_order_price);
            discount = ViewUtil.findViewById(view, R.id.my_order_discount);
            status = ViewUtil.findViewById(view, R.id.my_order_status);
            time = ViewUtil.findViewById(view, R.id.my_order_time);
            number = ViewUtil.findViewById(view, R.id.my_order_number);
            contain = ViewUtil.findViewById(view, R.id.order_contains);
          /*  tuan = ViewUtil.findViewById(view, R.id.my_order_tuangou);
            temai = ViewUtil.findViewById(view, R.id.my_order_temai);*/
        }
    }
}
