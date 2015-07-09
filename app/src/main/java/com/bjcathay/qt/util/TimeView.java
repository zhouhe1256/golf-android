
package com.bjcathay.qt.util;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.bjcathay.qt.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by dengt on 15-5-18.
 */
public class TimeView extends CountDownTimer {
    private TextView textView;
    private Button okbtn;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TimeView(long millisInFuture, long countDownInterval, TextView textView, Button okbtn) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.okbtn = okbtn;
    }

    @Override
    public void onTick(long diff) {
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60))
                / (1000 * 60);
        // long mm = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 *
        // 60) - minutes * (1000 * 60)) / 1000;
        textView.setText("仅剩" + days + "天" + hours + "小时" + minutes + "分");
    }

    @Override
    public void onFinish() {
        textView.setBackgroundResource(R.drawable.solid_bg);
        textView.setText("已售罄");
        // imageView.setImageResource(R.drawable.ic_tuan_disable);
        okbtn.setBackgroundResource(R.drawable.bg_sold_out);
        okbtn.setOnClickListener(null);
    }
}
