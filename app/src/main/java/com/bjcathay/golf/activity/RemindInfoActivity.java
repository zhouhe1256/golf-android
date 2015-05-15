package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.model.PropModel;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by bjcathay on 15-5-8.
 */
public class RemindInfoActivity extends Activity implements View.OnClickListener{
    private  Activity context;
    private TopView topView;
    private PropModel propModel;
    private TextView title;
    private TextView content;
    private TextView first;
    private TextView firstNote;
    private TextView second;
    private TextView secondNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remindinfo);
        context=this;
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_remind_layout);
        title = ViewUtil.findViewById(this, R.id.remind_title);
        content = ViewUtil.findViewById(this, R.id.remind_context);
        first = ViewUtil.findViewById(this, R.id.remind_first);
        firstNote = ViewUtil.findViewById(this, R.id.remind_first_note);
        second = ViewUtil.findViewById(this, R.id.remind_second);
        secondNote = ViewUtil.findViewById(this, R.id.remind_second_note);
    }

    private void initEvent() {
            first.setOnClickListener(this);
        second.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        propModel = (PropModel) intent.getSerializableExtra("prop");
        String titleString = intent.getStringExtra("title");
        topView.setTitleText(titleString);
        title.setText("兑换成功！");
        content.setText("您已成功兑换一枚"+propModel.getName());
        first.setText(getString(R.string.order_sucess_to_show_order));
        firstNote.setText(getString(R.string.order_sucess_to_show_order_note));
        second.setText(getString(R.string.order_sucess_to_show_place));
        secondNote.setText(getString(R.string.order_sucess_to_show_place_note));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.remind_first:
                Intent  intent = new Intent(context, SendFriendActivity.class);
                intent.putExtra("id",propModel.getId());
                ViewUtil.startActivity(context, intent);
                break;
            case R.id.remind_second:

                break;
        }
    }
}
