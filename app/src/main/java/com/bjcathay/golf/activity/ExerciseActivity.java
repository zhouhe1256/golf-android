package com.bjcathay.golf.activity;

import android.app.Activity;
import android.os.Bundle;

import com.bjcathay.golf.R;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by bjcathay on 15-4-27.
 */
public class ExerciseActivity extends Activity {
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        initView();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_exercise_layout);
    }

    private void initEvent() {
        topView.setTitleText("活动");
        topView.setActivity(this);
    }
}
