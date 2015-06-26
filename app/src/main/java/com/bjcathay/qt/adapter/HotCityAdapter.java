package com.bjcathay.qt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.model.CModel;
import com.bjcathay.qt.util.DialogUtil;

import java.util.List;

/**
 * Created by bjcathay on 15-6-25.
 */
public class HotCityAdapter extends BaseAdapter {
    private List<CModel> cities;
    private Context context;

    public HotCityAdapter(List<CModel> cities, Context context) {
        this.cities = cities;
        this.context = context;
    }
    public void updateListView(List<CModel> list) {
        this.cities = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int i) {
        return cities.get(i);
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
            convertView = inflater.inflate(
                    R.layout.item_hot_list, null);
        }
          CModel c=cities.get(i);
        ((TextView) convertView
                .findViewById(R.id.group_name)).setText(c.getName());
      /*  convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showMessage("北京");
            }
        });*/
        return convertView;
    }
}
