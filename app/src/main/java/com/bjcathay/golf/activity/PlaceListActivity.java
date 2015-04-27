package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.adapter.PlaceListAdapter;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 场地页面
 * Created by dengt on 15-4-20.
 */
public class PlaceListActivity extends Activity{
    private ListView listView;
    private PlaceListAdapter placeListAdapter;
    private List<PlaceModel> placeModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placelist);
         initView();
        initEvent();
    }
    private void initView(){
        listView= ViewUtil.findViewById(this,R.id.place_list);
        placeModels=new ArrayList<PlaceModel>();
        placeListAdapter=new PlaceListAdapter(placeModels,this);

    }
    private void initEvent(){
        listView.setAdapter(placeListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DialogUtil.hintMessage("选档期",PlaceListActivity.this);
                Intent intent=new Intent(PlaceListActivity.this,ScheduleActivity.class);
                ViewUtil.startActivity(PlaceListActivity.this,intent);
            }
        });
    }
}
