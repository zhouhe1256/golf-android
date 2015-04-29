package com.bjcathay.golf.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.adapter.MyCompetitionAdapter;
import com.bjcathay.golf.adapter.MyMessageAdapter;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-4-29.
 */
public class MyMessageActivity extends Activity {
    private ListView listView;
    private MyMessageAdapter myMessageAdapter;
    private List<PlaceModel> placeModels;
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        initView();
        initEvent();
    }

    private void initView() {
        listView = ViewUtil.findViewById(this, R.id.my_message_list);
        topView = ViewUtil.findViewById(this, R.id.top_my_message_layout);
        topView.setActivity(this);
        topView.setTitleText("我的消息");
        topView.setVisiable(View.VISIBLE,View.VISIBLE,View.VISIBLE);
        topView.setRightbtn("清空", 0);
        placeModels = new ArrayList<PlaceModel>();
        myMessageAdapter = new MyMessageAdapter(placeModels, this);

    }

    private void initEvent() {
        listView.setAdapter(myMessageAdapter);
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // DialogUtil.hintMessage("选档期", PlaceListActivity.this);
               *//* Intent intent = new Intent(MyCompetitionActivity.this, OrderDetailActivity.class);
                ViewUtil.startActivity(MyCompetitionActivity.this, intent);*//*
            }
        });*/
    }
}