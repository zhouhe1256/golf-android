
package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.ExerciseActivity;
import com.bjcathay.qt.activity.MyCompetitionActivity;
import com.bjcathay.qt.activity.OrderDetailActivity;
import com.bjcathay.qt.activity.OrderStadiumDetailActivity;
import com.bjcathay.qt.activity.PackageDetailActivity;
import com.bjcathay.qt.activity.RealTOrderActivity;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.MessageModel;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-7-23.
 */
public class MyNtfMessageAdapter extends BaseAdapter {
    private List<MessageModel> items;
    private Activity context;
    private int nowPosition;
    private Dialog dialog;

    public MyNtfMessageAdapter(List<MessageModel> items, Activity activity) {
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
        holder.day.setText(messageModel.getCreated().substring(5, 16));
        holder.content.setText(messageModel.getContent());
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                // 产品
                if ("a".equals("a")) {
                    // 跳转产品详情页
                    ProductModel.product(1)
                            .done(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    JSONObject jsonObject = arguments.get(0);
                                    if (jsonObject.optBoolean("success")) {
                                        ProductModel productModel = JSONUtil.load(
                                                ProductModel.class,
                                                jsonObject.optJSONObject("product"));
                                        Intent intent = null;
                                        switch (productModel.getType()) {
                                            case COMBO:
                                                intent = new Intent(context,
                                                        PackageDetailActivity.class);
                                                intent.putExtra("id", productModel.getId());
                                                intent.putExtra("name", productModel.getName());
                                                intent.putExtra("product", productModel);
                                                ViewUtil.startActivity(context, intent);
                                                break;
                                            case REAL_TIME:
                                                intent = new Intent(context,
                                                        RealTOrderActivity.class);
                                                intent.putExtra("id", productModel.getId());
                                                intent.putExtra("imageurl",
                                                        productModel.getImageUrl());
                                                ViewUtil.startActivity(context, intent);
                                                break;
                                            default:
                                                intent = new Intent(context,
                                                        OrderStadiumDetailActivity.class);
                                                intent.putExtra("id", productModel.getId());
                                                intent.putExtra("imageurl",
                                                        productModel.getImageUrl());
                                                ViewUtil.startActivity(context, intent);
                                                break;
                                        }
                                    } else {
                                        String errorMessage = jsonObject.optString("message");
                                        if (!StringUtils.isEmpty(errorMessage))
                                            DialogUtil.showMessage(errorMessage);
                                        else {
                                            int code = jsonObject.optInt("code");
                                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                        }
                                    }
                                }
                            }

                            ).

                            fail(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    DialogUtil.showMessage(context
                                            .getString(R.string.empty_net_text));
                                }
                            }

                            );

                }
                // web
                if ("a".equals("a")) {
                    // 跳转web页
                    intent = new Intent(context, ExerciseActivity.class);
                    intent.putExtra("url", "www.baidu.com");
                    ViewUtil.startActivity(context, intent);
                }
                // 纯消息
                if ("a".equals("a")) {
                    // 无操作
                }

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
