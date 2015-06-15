package com.bjcathay.qt.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
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
import com.bjcathay.qt.fragment.DialogExchFragment;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.model.SortModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.ViewUtil;

import java.util.List;

/**
 * @author http://blog.csdn.net/finddreams
 * @Description:用来处理集合中数据的显示与排序
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;
    private FragmentActivity mContext;
    private Long id;
    private DialogExchFragment dialogExchFragment;
    private String name;
    private ShareModel shareModel;

    public SortAdapter(FragmentActivity mContext, List<SortModel> list, Long id, String name, DialogExchFragment dialogExchFragment) {
        this.mContext = mContext;
        this.list = list;
        this.id = id;
        this.name = name;
        this.dialogExchFragment = dialogExchFragment;
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
        ImageViewAdapter.adapt(viewHolder.icon, this.list.get(position).getImageUrl(), R.drawable.ic_default_user);
        if (this.list.get(position).isUser()) {
            viewHolder.statusTrue.setVisibility(View.VISIBLE);
            viewHolder.statusTrue.setText("赠送");
            viewHolder.statusTrue.setBackgroundResource(R.drawable.ic_exchange_yellow);
            viewHolder.statusTrue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserModel userModel = new UserModel();
                    userModel.setMobileNumber(mContent.getPhone());
                    dialogExchFragment.setItems(userModel, "user", mContent.getPhone(), id, name);

                    dialogExchFragment.show(mContext.getSupportFragmentManager(), "send");
                }
            });
        } else {
            viewHolder.statusTrue.setVisibility(View.VISIBLE);
            viewHolder.statusTrue.setText("邀请");
            viewHolder.statusTrue.setBackgroundResource(R.drawable.ic_yaoqing);
            viewHolder.statusTrue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* dialogExchFragment.setItems(null, "user", mContent.getPhone(), id);
                    dialogExchFragment.show(mContext.getSupportFragmentManager(), "send");*/
                    if (shareModel == null)
                        ShareModel.share().done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                shareModel = arguments.get(0);
                                sendMessage(mContent.getPhone(), shareModel.getSmsContent());
                            }
                        });
                    else {
                        sendMessage(mContent.getPhone(), shareModel.getSmsContent());
                    }
                }
            });
        }

        return view;

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