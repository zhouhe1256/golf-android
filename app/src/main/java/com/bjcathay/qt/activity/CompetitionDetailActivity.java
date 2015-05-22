package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.EventModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-4-28.
 */
public class CompetitionDetailActivity extends Activity implements ICallback,View.OnClickListener{
    private TopView topView;
    EventModel eventModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_detail);
        initView();
        initData();
        initEvent();
    }
    private void initView(){
        topView= ViewUtil.findViewById(this,R.id.top_competition_detail_layout);
    }
    private void initEvent(){
        topView.setTitleBackVisiable();
        topView.setTitleText("公开赛");
    }
    private void initData(){
        Intent intent=getIntent();
        eventModel= (EventModel) intent.getSerializableExtra("event");
        EventModel.getEventDetail(eventModel.getId()).done(this);
    }

    @Override
    public void call(Arguments arguments) {
        eventModel=arguments.get(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.attend_now:
                EventModel.attendEvent(eventModel.getId()).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        if (jsonObject.optBoolean("success")) {
                            DialogUtil.showMessage("报名成功");
                        }else {
                            int code = jsonObject.optInt("code");
                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        DialogUtil.showMessage("报名失败");
                    }
                });
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }
}
