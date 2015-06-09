package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by bjcathay on 15-6-1.
 */
public class AttendSucActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private TextView textView;
    String tiele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_suc);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_sttend_suc_layout);
        textView = ViewUtil.findViewById(this, R.id.attend_name);
    }

    private void initData() {
        Intent intent = getIntent();
        tiele = intent.getStringExtra("title");
        textView.setText(getString(R.string.attend_success_note, tiele));
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText(tiele);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                Intent intent = new Intent(this, MainActivity.class);
                ViewUtil.startTopActivity(this, intent);
                finish();
                break;
        }
    }
}
