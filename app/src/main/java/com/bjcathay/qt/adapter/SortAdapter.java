package com.bjcathay.qt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.model.PropModel;
import com.bjcathay.qt.model.SortModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.view.ImageTextView;

import org.json.JSONObject;

import java.util.List;

/**
 * @author http://blog.csdn.net/finddreams
 * @Description:用来处理集合中数据的显示与排序
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;
    private Context mContext;
    private Long id;

    public SortAdapter(Context mContext, List<SortModel> list,Long id) {
        this.mContext = mContext;
        this.list = list;
        this.id=id;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SortModel> list) {
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
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_phone_constacts, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.statusTrue = (TextView) view.findViewById(R.id.contact_status_true);
            viewHolder.statusFalse = (TextView) view.findViewById(R.id.contact_status_false);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(this.list.get(position).getName());
       /* viewHolder.icon.setText(this.list.get(position).getName());
        viewHolder.icon.setIconText(mContext, this.list.get(position).getName());*/
        ImageViewAdapter.adapt(viewHolder.icon, this.list.get(position).getImageUrl(), R.drawable.ic_launcher);
        if (this.list.get(position).isUser()) {
            viewHolder.statusTrue.setVisibility(View.VISIBLE);
            viewHolder.statusTrue.setText("赠送");
            viewHolder.statusTrue.setBackgroundResource(R.drawable.ic_exchange_yellow);
            viewHolder.statusTrue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PropModel.sendProp(id, mContent.getPhone()).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            JSONObject jsonObject = arguments.get(0);
                            if (jsonObject.optBoolean("success")) {
                                DialogUtil.showMessage("赠送成功");
                            } else {
                                int code = jsonObject.optInt("code");
                                DialogUtil.showMessage(ErrorCode.getCodeName(code));
                            }
                        }
                    });
                }
            });
           // viewHolder.statusFalse.setVisibility(View.GONE);
        } else {
            viewHolder.statusTrue.setVisibility(View.VISIBLE);
            viewHolder.statusTrue.setText("邀请");
            viewHolder.statusTrue.setBackgroundResource(R.drawable.ic_yaoqing);
          // viewHolder.statusTrue.setVisibility(View.GONE);
         // viewHolder.statusFalse.setVisibility(View.VISIBLE);
        }

        return view;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        ImageView icon;
        TextView statusTrue;
        TextView statusFalse;

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
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
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