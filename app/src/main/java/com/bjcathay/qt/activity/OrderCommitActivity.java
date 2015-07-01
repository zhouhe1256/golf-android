package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;

import com.bjcathay.qt.R;
import com.bjcathay.qt.view.TopView;

/**
 * Created by dengt on 15-7-1.
 */
public class OrderCommitActivity extends Activity{
   private TopView topView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_commit);

    }
}
