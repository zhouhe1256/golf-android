
package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.EditPlayerActivity;
import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dengt on 15-7-3.
 */
public class PlayerAdapter extends BaseAdapter {
    private List<BookModel> items;
    private Activity context;
    private int reqedit = 1;
    private int respedit = 2;
    /**
     * CheckBox 是否选择的存储集合,key 是 position , value 是该position是否选中
     */
    private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
    private List<BookModel> check = new ArrayList<BookModel>();

    public PlayerAdapter(List<BookModel> bookModels, Activity context) {
        if (items == null) {
            items = new ArrayList<BookModel>();
        }
        this.context = context;
        this.items = bookModels;
        // 初始化,默认都没有选中
        configCheckMap(false);
    }

    /**
     * 首先,默认情况下,所有项目都是没有选中的.这里进行初始化
     */
    public void configCheckMap(boolean bool) {

        for (int i = 0; i < items.size(); i++) {
            isCheckMap.put(i, bool);
        }

    }

    public void uodateView(List<BookModel> bookModels) {
        this.items = bookModels;
        notifyDataSetChanged();
    }

    public List<BookModel> getCheckedItems() {
        return check;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();

    }

    @Override
    public Object getItem(int i) {
        return items == null ? null : items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_player_list,
                    viewGroup, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        BookModel bookModel = items.get(position);
        holder.name.setText(bookModel.getName());
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                /*
                 * 将选择项加载到map里面寄存
                 */
                isCheckMap.put(position, isChecked);
                if (isChecked) {
                    check.add(items.get(position));
                } else {
                    check.remove(items.get(position));
                }
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditPlayerActivity.class);
                intent.putExtra("book", items.get(position));
                // ViewUtil.startActivity(context, intent);
                context.startActivityForResult(intent, reqedit);
                // DialogUtil.showMessage("编辑");
            }
        });
        if (isCheckMap.get(position) == null) {
            isCheckMap.put(position, false);
        }
        holder.check.setChecked(isCheckMap.get(position));
        return convertView;
    }

    class Holder {
        ImageView edit;
        TextView name;
        CheckBox check;

        public Holder(View view) {
            edit = ViewUtil.findViewById(view, R.id.player_edit);
            name = ViewUtil.findViewById(view, R.id.player_name);
            check = ViewUtil.findViewById(view, R.id.checkbox);
        }
    }
}
