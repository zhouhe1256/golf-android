package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.adapter.MyOrderAdapter;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-4-28.
 */
public class MyOrderActivity extends Activity {
    private ListView listView;
    private MyOrderAdapter myOrderAdapter;
    private List<PlaceModel> placeModels;
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        initEvent();
    }

    private void initView() {
        listView = ViewUtil.findViewById(this, R.id.my_order_list);
        topView = ViewUtil.findViewById(this, R.id.top_my_order_layout);
        topView.setActivity(this);
        topView.setTitleText("我的订单");
        placeModels = new ArrayList<PlaceModel>();
        myOrderAdapter = new MyOrderAdapter(placeModels, this);

    }

    private void initEvent() {
        listView.setAdapter(myOrderAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // DialogUtil.hintMessage("选档期", PlaceListActivity.this);
               /* Intent intent = new Intent(MyOrderActivity.this, ScheduleActivity.class);
                ViewUtil.startActivity(PlaceListActivity.this, intent);*/
            }
        });
    }
}
