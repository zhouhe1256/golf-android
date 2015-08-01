
package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.ProductActivity;
import com.bjcathay.qt.activity.ProductSearchResultActivity;
import com.bjcathay.qt.db.DBManager;
import com.bjcathay.qt.model.GolfCourseModel;
import com.bjcathay.qt.util.ViewUtil;

import java.util.List;

/**
 * Created by dengt on 15-6-25.
 */
public class GolfCourseAdapter extends BaseAdapter {
    private List<GolfCourseModel> list;
    private Activity context;

    public GolfCourseAdapter(List<GolfCourseModel> list, Activity context) {
        this.list = list;
        this.context = context;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<GolfCourseModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_history_list, null);
        }
        final GolfCourseModel g = list.get(i);
        TextView textview = ViewUtil.findViewById(view, R.id.golf_search_name);
        textview.setText(g.getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo save
                DBManager.getInstance().add(g);
                Intent intent = new Intent(context,
                        ProductActivity.class);
                intent.putExtra("id", g.getId());
                intent.putExtra("name", g.getName());
                ViewUtil.startActivity(context, intent);
                context.finish();
            }
        });
        return view;
    }
}
