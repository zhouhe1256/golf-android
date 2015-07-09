package com.bjcathay.qt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.model.PModel;

import java.util.List;

/**
 * Created by dengt on 15-6-24.
 */
public class CityAdapter extends BaseExpandableListAdapter {
    private List<PModel> pModelList;
    private Context context;

    public CityAdapter(Context context, List<PModel> pModels) {
        this.pModelList = pModels;
        this.context = context;

    }
    public void updateListView(List<PModel> list) {
        this.pModelList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getGroupCount() {
        return null == pModelList ? 0 : pModelList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return null == pModelList.get(groupPosition).getCity() ? 0 : pModelList.get(groupPosition).getCity().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return pModelList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return pModelList.get(groupPosition).getCity().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.item_select_tag, null);
        }
        ((TextView) convertView
                .findViewById(R.id.group_name)).setText(pModelList.get(groupPosition).getProvince());
        ImageView iv = (ImageView) convertView.findViewById(R.id.expand_right_img);
        if (isExpanded) {
            iv.setImageResource(R.drawable.ic_city_select);
        } else {
            iv.setImageResource(R.drawable.ic_into_list);

        }
        if (mHideGroupPos == groupPosition) {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            convertView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private int mHideGroupPos = -1;

    public void hideGroup(int groupPos) {
        mHideGroupPos = groupPos;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_select_list, null);

        }
        if (!pModelList.isEmpty() && pModelList.get(groupPosition).getCity().size() > 0) {
            TextView title = (TextView) view
                    .findViewById(R.id.child_name);
            title.setText(pModelList.get(groupPosition).getCity().get(childPosition).getName());
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}
