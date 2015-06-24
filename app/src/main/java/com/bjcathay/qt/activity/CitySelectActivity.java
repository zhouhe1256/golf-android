package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.ExpandableLayoutListView;
import com.bjcathay.qt.view.TopView;

/**
 * Created by bjcathay on 15-6-24.
 */
public class CitySelectActivity extends Activity implements View.OnClickListener {
    private final String[] array = {"Hello", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome"};

    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.view_row, R.id.header_text, array);
        final ExpandableLayoutListView expandableLayoutListView = (ExpandableLayoutListView) findViewById(R.id.listview);

        expandableLayoutListView.setAdapter(arrayAdapter);
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_city_select_layout);
        topView.setTitleText("选择城市");
        topView.setTitleBackVisiable();
    }

    private void initEvent() {
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
           /* case R.id.select_city:
                // intent = new Intent(this, ContactActivity.class);
                // ViewUtil.startActivity(this, intent);
                break;
            case R.id.input_place:
                //  intent = new Intent(this, SendToPhoneActivity.class);
                //  ViewUtil.startActivity(this, intent);
                break;
            case R.id.select_sure:
                break;*/
        }
    }
}
