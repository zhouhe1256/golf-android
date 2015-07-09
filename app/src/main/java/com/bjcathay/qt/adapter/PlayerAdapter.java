
package com.bjcathay.qt.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.EditPlayerActivity;
import com.bjcathay.qt.db.DBManager;
import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.model.BookListModel;
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
    private Map<BookModel, Boolean> isCheck = new HashMap<BookModel, Boolean>();
    private List<BookModel> check = new ArrayList<BookModel>();

    public PlayerAdapter(List<BookModel> bookModels, Activity context, BookListModel bookListModel) {
        if (items == null) {
            items = new ArrayList<BookModel>();
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
        }
    }

    public void configCheckMap(boolean bool, BookListModel bookListMode) {
        if (bookListMode != null && !bookListMode.getPersons().isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                for (BookModel b : bookListMode.getPersons()) {
                    if (b.getPhone().equals(items.get(i).getPhone())
                            && b.getName().equals(items.get(i).getName())) {
                        isCheckMap.put(i, true);
                        check.add(b);
                    }
                }
            }
        }
    }

    public void addItem(List<BookModel> lists) {
        // todo bug去重
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

    public void repalceItem(BookModel newbookModel, BookModel oldbookModel) {
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

    public void setCheckedList(List<BookModel> check) {
        this.check = check;

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
        if (position == 0) {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.v.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
        } else {
            holder.linearLayout.setVisibility(View.GONE);
            holder.v.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
        }
        BookModel bookModel = items.get(position);
        holder.check.setText(bookModel.getName());
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                /*
                 * 将选择项加载到map里面寄存
                 */
                isCheckMap.put(position, isChecked);
                if (isCheckMap.get(position)) {
                    if (!theSame(position)) {
                        check.add(items.get(position));
                    }
                } else {
                    int i = theremoveSame(position);
                    if (i != -1)
                        check.remove(i);
                }
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditPlayerActivity.class);
                intent.putExtra("book", items.get(position));
                context.startActivityForResult(intent, reqedit);
            }
        });
        if (isCheckMap.get(position) == null) {
            isCheckMap.put(position,
                    false);
        }
        holder.check.setChecked(isCheckMap.get(position));
        return convertView;
    }

    private boolean theSame(int position) {
        for (int i = 0; i < check.size(); i++) {
            if (check.get(i).getName().equals(items.get(position).getName())
                    && check.get(i).getPhone().equals(items.get(position).getPhone())) {
                return true;
            }
        }
        return false;
    }

    private int theremoveSame(int position) {
        for (int i = 0; i < check.size(); i++) {
            if (check.get(i).getName().equals(items.get(position).getName())
                    && check.get(i).getPhone().equals(items.get(position).getPhone())) {
                return i;
            }
        }
        return -1;
    }

    class Holder {
        ImageView edit;
        CheckBox check;
        LinearLayout linearLayout;
        View v;

        public Holder(View view) {
            edit = ViewUtil.findViewById(view, R.id.player_edit);
            check = ViewUtil.findViewById(view, R.id.checkbox);
            linearLayout = ViewUtil.findViewById(view, R.id.head_name);
            v = ViewUtil.findViewById(view, R.id.line);
        }
    }
}
