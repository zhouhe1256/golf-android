
package com.bjcathay.qt.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.fragment.DialogExchFragment;
import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.model.SortModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dengt on 15-7-3.
 */
public class SelectContactAdapter extends BaseAdapter implements SectionIndexer {
    private List<BookModel> list = null;
    private Context mContext;
    private Long id;
    private String name;
    private ShareModel shareModel;
    /**
     * CheckBox 是否选择的存储集合,key 是 position , value 是该position是否选中
     */
    private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
    private List<BookModel> check = new ArrayList<BookModel>();

    public SelectContactAdapter(Context mContext, List<BookModel> list, Long id, String name) {
        this.mContext = mContext;
        this.list = list;
        this.id = id;
        this.name = name;
        // 初始化,默认都没有选中
        configCheckMap(false);
    }

    /**
     * 首先,默认情况下,所有项目都是没有选中的.这里进行初始化
     */
    public void configCheckMap(boolean bool) {

        for (int i = 0; i < list.size(); i++) {
            isCheckMap.put(i, bool);
        }

    }

    public List<BookModel> getCheckedItems() {
        return check;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<BookModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final BookModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_select_contact_list, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.statusTrue = (CheckBox) view.findViewById(R.id.contact_status_true);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(this.list.get(position).getName());
        viewHolder.statusTrue
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        /*
                         * 将选择项加载到map里面寄存
                         */
                        isCheckMap.put(position, isChecked);
                        if (isChecked) {
                            check.add(list.get(position));
                            compoundButton.setChecked(true);
                        } else {
                            check.remove(list.get(position));
                            compoundButton.setChecked(false);
                        }
                    }
                });
        /*
         * view.setOnClickListener(new View.OnClickListener() {
         * @Override public void onClick(View view) { if
         * (isCheckMap.containsKey(position)) { if (isCheckMap.get(position)) {
         * check.add(list.get(position)); } else {
         * check.remove(list.get(position)); } } else { isCheckMap.put(position,
         * true); check.add(list.get(position)); } ((CheckBox)
         * view.findViewById(
         * R.id.contact_status_true)).setChecked(isCheckMap.get(position)); //
         * setitemChecked(position); } });
         */
        /*
         * AdapterView.OnItemClickListener itemListener= new
         * AdapterView.OnItemClickListener() {
         * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
         * arg2, long arg3) { LinearLayout view1 = (LinearLayout) arg1;
         * RelativeLayout view = (RelativeLayout) view1.getChildAt(0); CheckBox
         * box = (CheckBox) view.getChildAt(1);
         * box.setChecked(!box.isChecked()); } };
         */

        if (isCheckMap.get(position) == null) {
            isCheckMap.put(position, false);
        }
        viewHolder.statusTrue.setChecked(isCheckMap.get(position));
        return view;

    }

    /*
     * CheckBox.OnCheckedChangeListener checkListener = new
     * CheckBox.OnCheckedChangeListener(){
     * @Override public void onCheckedChanged(CompoundButton buttonView, boolean
     * isChecked) { // TODO Auto-generated method stub
     *//*
        * 将选择项加载到map里面寄存
        *//*
           * isCheckMap.put(buttonView.getId(), isChecked); if (isChecked) {
           * check.add(list.get(buttonView.getId())); } else {
           * check.remove(list.get(buttonView.getId())); } } };
           */
    private void setitemChecked(int position) {
        // viewHolder.statusTrue.setChecked(isCheckMap.get(position));
    }

    private void sendMessage(String phone, String message) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.putExtra("sms_body", message);
        ViewUtil.startActivity(mContext, sendIntent);
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        ImageView icon;
        CheckBox statusTrue;

    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            if (list != null) {
                String sortStr = list.get(i).getSortLetters();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
