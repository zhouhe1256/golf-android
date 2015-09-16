package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

import com.bjcathay.qt.R;
import com.bjcathay.qt.view.SwipeBackLayout;

/**
 * Created by dengt on 15-9-16.
 */
public class SwipeBackFragmentActivity extends FragmentActivity {
    protected SwipeBackLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);
    }
}
