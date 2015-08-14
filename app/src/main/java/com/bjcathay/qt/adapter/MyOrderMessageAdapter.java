
package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.app.Dialog;
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
import com.bjcathay.qt.activity.MyCompetitionActivity;
import com.bjcathay.qt.activity.OrderDetailActivity;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.MessageListModel;
import com.bjcathay.qt.model.MessageModel;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-4-29.
 */
public class MyOrderMessageAdapter extends BaseAdapter {
    private List<MessageModel> items;
    private Activity context;
    private int nowPosition;
    private Dialog dialog;

    public MyOrderMessageAdapter(List<MessageModel> items, Activity activity) {
        if (items == null) {
            items = new ArrayList<MessageModel>();
        }
        this.items = items;
        this.context = activity;
        nowPosition = 0;
        dialog = new Dialog(context, R.style.myMessageDialogTheme);
        dialog.setContentView(R.layout.dialog_message_delete);
        dialog.findViewById(R.id.dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_order_message_list,
                    parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final MessageModel messageModel = items.get(position);
        holder.name.setText(messageModel.getName());
        holder.day.setText(messageModel.getCreated().substring(2,16));
        holder.content.setText(messageModel.getContent());
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickUtil.isFastClick();
                OrderModel.orderDetail(Long.valueOf(messageModel.getTarget()))
                        .done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                OrderModel orderModel = arguments.get(0);
                                Intent intent = new Intent(context,
                                        OrderDetailActivity.class);
                                intent.setAction("message");
                                intent.putExtra("orderModel", orderModel);
                                ViewUtil.startActivity(context, intent);
                            }
                        }).fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                dialog.show();
                            }
                        });

            }
        });
        return convertView;
    }

    class Holder {
        LinearLayout detail;
        TextView toPay;
        TextView name;
        TextView content;
        TextView day;

        public Holder(View view) {
            detail = ViewUtil.findViewById(view, R.id.my_message_detail);
            toPay = ViewUtil.findViewById(view, R.id.my_message_to_pay);
            name = ViewUtil.findViewById(view, R.id.message_name);
            content = ViewUtil.findViewById(view, R.id.message_content);
            day = ViewUtil.findViewById(view, R.id.message_relativeDate);
        }
    }
}
