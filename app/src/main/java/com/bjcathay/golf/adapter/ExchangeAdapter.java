package com.bjcathay.golf.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-5-6.
 */
public class ExchangeAdapter extends BaseAdapter {
    private List<PlaceModel> items;
    private Activity context;
    private int count = 0;

    public ExchangeAdapter(List<PlaceModel> items, Activity activity) {
        if (items == null) {
            items = new ArrayList<PlaceModel>();
        }
        this.items = items;
        this.context = activity;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        // return items == null ? 0 : items.size();
        if (count == 0) {
            count = 10;
        }
        return count;
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

       /* convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.hintMessage("选档期");
            }
        });*/
        holder.toExch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = context.getLayoutInflater();
                ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.dialog_exchange_award, null);
                Dialog dialog = new Dialog(context, R.style.myDialogTheme);
                dialog.setContentView(rootView);
                //dialog.create();
                // dialog.setContentView(rootView);
                dialog.show();
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
            toExch=ViewUtil.findViewById(view,R.id.to_exchange);
        }
    }
}
