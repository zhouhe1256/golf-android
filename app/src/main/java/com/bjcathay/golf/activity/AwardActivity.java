package com.bjcathay.golf.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.golf.R;
import com.bjcathay.golf.adapter.ExchangeAdapter;
import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.model.PropListModel;
import com.bjcathay.golf.model.PropModel;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

import java.util.ArrayList;
import java.util.List;

/**
 * 兑换页面
 * Created by dengt on 15-4-20.
 */
public class AwardActivity extends Activity implements ICallback/* implements View.OnClickListener,View.OnLongClickListener*/{
    private GApplication gApplication;
    private TopView topView;
    private ListView awardingFirst;
    private ExchangeAdapter exchangeAdapter;
    private List<PropModel> propModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        gApplication=GApplication.getInstance();
        initView();
        initEvent();
        initData();
    }
    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_award_layout);
        awardingFirst=ViewUtil.findViewById(this,R.id.exchange_list);
        propModels = new ArrayList<PropModel>();
        exchangeAdapter = new ExchangeAdapter(propModels, this);
    }

    private void initEvent() {
        topView.setTitleText("兑换");
        topView.setActivity(this);
        awardingFirst.setAdapter(exchangeAdapter);
       /* awardingFirst.setOnClickListener(this);
        awardingFirst.setOnLongClickListener(this);*/
    }
private void initData(){
    PropListModel.get().done(this);
}

    @Override
    public void call(Arguments arguments) {
        PropListModel propListModel=arguments.get(0);

        propModels.addAll(propListModel.getProps());
        exchangeAdapter.notifyDataSetChanged();
    }
   /* @Override
    public void onClick(View view) {
        switch(view.getId()){
           *//* case R.id.exchange_first:
                LayoutInflater inflater = getLayoutInflater();
                ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.dialog_exchange_award, null);
                Dialog dialog = new Dialog(this, R.style.myDialogTheme);
                dialog.setContentView(rootView);
                //dialog.create();
                // dialog.setContentView(rootView);
                dialog.show();
                break;*//*
        }
    }*/

    /*@Override
    public boolean onLongClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.exchange_first:
                intent = new Intent(this, SendFriendActivity.class);
                ViewUtil.startActivity(this, intent);
            return true;
        }
        return false;
    }*/
}
