package com.bjcathay.qt.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjcathay.qt.util.SizeUtil;

import java.util.List;

/**
 * Created by bjcathay on 15-6-1.
 */
public class WheelTextAdapter extends BaseAdapter {
    List<String> mData = null;
    int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
    int mHeight = 50;
    Context mContext = null;

    public WheelTextAdapter(Context context) {
        mContext = context;
        mHeight = (int) SizeUtil.pixelToDp(context, mHeight);
    }

    public void setData(List<String> data) {
        mData = data;
        this.notifyDataSetChanged();
    }

    public void setItemSize(int width, int height) {
        mWidth = width;
        mHeight = (int) SizeUtil.pixelToDp(mContext, height);
    }

    @Override
    public int getCount() {
        return (null != mData) ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;

        if (null == convertView) {
            convertView = new TextView(mContext);
            convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
            textView = (TextView) convertView;
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            textView.setTextColor(Color.BLACK);
        }

        if (null == textView) {
            textView = (TextView) convertView;
        }

        String info = mData.get(position);
        textView.setText(info);
        //  textView.setTextColor(info.mColor);

        return convertView;
    }
}

