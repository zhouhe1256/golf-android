package com.bjcathay.qt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.bjcathay.qt.R;

/**
 * Created by dengt on 15-9-7.
 */
public class NetAccessView extends LinearLayout {
    public NetAccessView(Context context) {
        super(context);
        initView(context);
    }

    public NetAccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NetAccessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        View.inflate(context, R.layout.layout_my_dialog, this);
    }
}
