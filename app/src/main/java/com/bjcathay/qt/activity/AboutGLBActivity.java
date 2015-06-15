package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by dengt on 15-5-25.
 */
public class AboutGLBActivity extends Activity implements View.OnClickListener {
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_guolinbi);
        topView = ViewUtil.findViewById(this, R.id.top_glb_layout);
        topView.setTitleBackVisiable();
        topView.setTitleText("关于果岭币");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
        }
    }
}
