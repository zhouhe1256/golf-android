package com.bjcathay.qt.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.receiver.DialogReceiver;

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
    DialogReceiver dialogReceiver;
    public NetAccessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        View.inflate(context, R.layout.layout_my_dialog, this);
//        if(dialogReceiver==null){
//            dialogReceiver=new DialogReceiver();
//        }
//        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        context.registerReceiver(dialogReceiver, homeFilter);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                GApplication.getInstance().closeDialog();
            case KeyEvent.KEYCODE_MENU:
                // 处理自己的逻辑break;
                GApplication.getInstance().closeDialog();
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }
}
