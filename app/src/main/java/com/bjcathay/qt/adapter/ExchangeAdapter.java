
package com.bjcathay.qt.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.AwardActivity;
import com.bjcathay.qt.activity.LoginActivity;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.fragment.DialogExchFragment;
import com.bjcathay.qt.model.PropModel;
import com.bjcathay.qt.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-5-6.
 */
public class ExchangeAdapter extends BaseAdapter {
    private GApplication gApplication;
    private List<PropModel> items;
    private FragmentActivity context;
    private TextView number;
    private DialogExchFragment dialogExchFragment;
    int num;

    public ExchangeAdapter(List<PropModel> items, AwardActivity activity, TextView number,
            DialogExchFragment dialogExchFragment) {
        if (items == null) {
            items = new ArrayList<PropModel>();
        }
        this.items = items;
        this.context = activity;
        this.number = number;
        gApplication = GApplication.getInstance();
        this.dialogExchFragment = dialogExchFragment;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exchange_list, parent,
                    false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final PropModel propModel = items.get(position);
        holder.title.setText(propModel.getName());
        ImageViewAdapter.adapt(holder.imageView, propModel.getImageUrl(),
                R.drawable.exchange_default);
        holder.sale.setText(propModel.getDescription());
        holder.price.setText(propModel.getValue());
        holder.toExch.setText(propModel.getNeedAmount() + "币兑换");
        if (gApplication.isLogin() == true) {

            num = Integer.valueOf(number.getText().toString().trim());
        } else {
            num = 0;
        }

        if (propModel.getNeedAmount() > num) {
            holder.toExch.setBackgroundResource(R.drawable.solid_bg);
        } else {
            holder.toExch.setBackgroundResource(R.drawable.ic_exchange_yellow);
        }
        holder.toExch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gApplication.isLogin() == true) {
                    dialogExchFragment.setItems(propModel, num);
                    dialogExchFragment.show(context.getSupportFragmentManager(), "exchange");
                }else{
                    Intent intent= new Intent(context, LoginActivity.class);
                    // IsLoginUtil.isLogin(context,intent);
                    ViewUtil.startActivity(context,intent);
                }
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
