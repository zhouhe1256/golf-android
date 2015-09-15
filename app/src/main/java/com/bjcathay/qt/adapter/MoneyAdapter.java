
package com.bjcathay.qt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.model.CModel;
import com.bjcathay.qt.model.MoneyModel;

import java.util.List;

/**
 * Created by jiangm on 15-9-14.
 */
public class MoneyAdapter extends BaseAdapter {
    private List<MoneyModel> moneies;
    private Context context;
    private int clickTemp = 0;

    public MoneyAdapter(List<MoneyModel> moneies, Context context) {
        this.moneies = moneies;
        this.context = context;
    }

    public void updateListView(List<MoneyModel> list) {
        this.moneies = list;
        notifyDataSetChanged();
    }

    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return moneies.size();
    }

    @Override
    public Object getItem(int i) {
        return moneies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          /*  convertView = inflater.inflate(
                    R.layout.item_hot_list, null);*/
            convertView = inflater.inflate(
                    R.layout.item_money_gridview, null);
        }
        MoneyModel m = moneies.get(i);
       /* ((TextView) convertView
                .findViewById(R.id.group_name)).setText(c.getName());*/
        TextView textview=(TextView) convertView.findViewById(R.id.my_money_note);
       textview.setText(String.valueOf(m.getMoney()));

        if (clickTemp == i) {
            textview.setBackgroundResource(R.drawable.per_wallet_selected);
            textview.setTextColor(context.getResources().getColor(R.color.money_text_color));

        } else {
            textview.setBackgroundResource(R.drawable.yc_rectangle);
            textview.setTextColor(context.getResources().getColor(R.color.main_text_color));
        }

        return convertView;
    }
}
