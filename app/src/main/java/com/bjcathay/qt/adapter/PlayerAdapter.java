
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.EditPlayerActivity;
import com.bjcathay.qt.db.DBManager;
import com.bjcathay.qt.model.BModel;
import com.bjcathay.qt.model.BookListModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dengt on 15-7-3.
 */
public class PlayerAdapter extends BaseAdapter {
    private List<BModel> items;
    private Activity context;
    private int reqedit = 1;
    private int respedit = 2;
    /**
     * CheckBox 是否选择的存储集合,key 是 position , value 是该position是否选中
     */
    private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
    private Map<BModel, Boolean> isCheck = new HashMap<BModel, Boolean>();
    private List<BModel> check = new ArrayList<BModel>();

    public PlayerAdapter(List<BModel> bookModels, Activity context, BookListModel bookListModel) {
        if (items == null) {
            items = new ArrayList<BModel>();
        }
        this.context = context;
        this.items = bookModels;
        // 初始化,默认都没有选中
        configCheckMap(false);
        // 选中的
        configCheckMap(false, bookListModel);
    }

    /**
     * 首先,默认情况下,所有项目都是没有选中的.这里进行初始化
     */
    public void configCheckMap(boolean bool) {
        for (int i = 0; i < items.size(); i++) {
            isCheckMap.put(i, bool);
            // isCheck.put(items.get(i), bool);
        }
    }

    public void configCheckMap(boolean bool, BookListModel bookListMode) {
        if (bookListMode != null && !bookListMode.getPersons().isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                for (BModel b : bookListMode.getPersons()) {
                    if (b.getPhone().equals(items.get(i).getPhone())) {
                        isCheckMap.put(i, true);
                    }
                }
                // isCheck.put(items.get(i), bool);
            }
        }
        /*
         * if (bookListMode!=null&&!bookListMode.getPersons().isEmpty()) { for
         * (BookModel b : bookListMode.getPersons()) { isCheck.put(b, true); } }
         */

    }

    public void addItem(List<BModel> lists) {
        // todo bug去重
        // this.items.addAll(lists);
        /*
         * Set<BookModel> set = new HashSet<BookModel>(); set.addAll(items);
         * set.addAll(lists); items.clear(); items.addAll(set);
         */
        for (int i = 0; i < lists.size(); i++) {
            if (!items.contains(lists.get(i))) {
                items.add(lists.get(i));
                check.add(lists.get(i));
                DBManager.getInstance().addPlayer(lists.get(i));
            }
        }
        isCheckMap.clear();
        for (int i = 0; i < items.size(); i++) {
            isCheckMap.put(i, false);
        }
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < lists.size(); j++) {
                if (lists.get(j).getName().equals(items.get(i).getName())
                        && lists.get(j).getPhone().equals(items.get(i).getPhone())) {
                    isCheckMap.put(i, true);
                }
            }
        }
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < check.size(); j++) {
                if (check.get(j).getName().equals(items.get(i).getName())
                        && check.get(j).getPhone().equals(items.get(i).getPhone())) {
                    isCheckMap.put(i, true);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateView(List<BModel> bookModels) {
        this.items = bookModels;
        // check.clear();
        // notifyDataSetChanged();
    }

    public void repalceItem(BModel newbookModel, BModel oldbookModel) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(oldbookModel.getName())
                    && items.get(i).getPhone().equals(oldbookModel.getPhone())) {
                items.get(i).setName(newbookModel.getName());
                items.get(i).setPhone(newbookModel.getPhone());
                break;
            }
        }
        for (int i = 0; i < check.size(); i++) {
            if (check.get(i).getName().equals(oldbookModel.getName())
                    && check.get(i).getPhone().equals(oldbookModel.getPhone())) {
                check.get(i).setName(newbookModel.getName());
                check.get(i).setPhone(newbookModel.getPhone());
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void setCheckedList(List<BModel> check) {
        this.check = check;

    }

    public List<BModel> getCheckedItems() {

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
        if(position==0){
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.v.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
        }else{
            holder.linearLayout.setVisibility(View.GONE);
            holder.v.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
        }
        BModel bookModel = items.get(position);
        // holder.name.setText(bookModel.getName());
        holder.check.setText(bookModel.getName());
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                /*
                 * 将选择项加载到map里面寄存
                 */
                isCheckMap.put(position, isChecked);
                if (isChecked) {
                    if (!check.contains(items.get(position)))
                        check.add(items.get(position));
                } else {
                    check.remove(items.get(position));
                }
                /*
                 * if (isChecked) { isCheck.put(items.get(position), true); }
                 * else { isCheck.put(items.get(position), false); }
                 */
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
            isCheckMap.put(position,
                    false);
        }
        // holder.check.setChecked(isCheck.get(items.get(position)));
        holder.check.setChecked(isCheckMap.get(position));
        return convertView;
    }

    class Holder {
        ImageView edit;
        // TextView name;
        CheckBox check;
        LinearLayout linearLayout;
        View v;

        public Holder(View view) {
            edit = ViewUtil.findViewById(view, R.id.player_edit);
            // name = ViewUtil.findViewById(view, R.id.player_name);
            check = ViewUtil.findViewById(view, R.id.checkbox);
            linearLayout=ViewUtil.findViewById(view,R.id.head_name);
            v=ViewUtil.findViewById(view,R.id.line);
        }
    }
}
