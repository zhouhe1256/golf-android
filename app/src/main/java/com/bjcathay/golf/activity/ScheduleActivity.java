package com.bjcathay.golf.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bjcathay.golf.R;
import com.bjcathay.golf.fragment.DialogSureOrderFragment;
import com.bjcathay.golf.util.DateUtil;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.WheelView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dengt on 15-4-21.
 */
public class ScheduleActivity extends FragmentActivity {
    private WheelView wheelView;
    private WheelView wheelView1;
    private WheelView wheelView0;
    private Button okbtn;
    private List<String> days;


    private static final String[] HOURS = new String[]{"1","1:30","2","2:30","3","4","5","6","7","8", "9", "10", "11", "12"};
    private static final String[] MINITES = new String[]{"上午", "下午"};
    private static final String[] DAYS = new String[]{"周一", "周二","周三","周四", "周五","周六","周天"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        initView();
        initEvent();
    }
    private void initView(){
        wheelView= ViewUtil.findViewById(this,R.id.wheelView);
        wheelView1= ViewUtil.findViewById(this,R.id.wheelView1);
        wheelView0= ViewUtil.findViewById(this,R.id.wheelView0);

        okbtn=ViewUtil.findViewById(this,R.id.ok);
        days= DateUtil.getDate(this);




    }
    private void initEvent(){
        wheelView0.setOffset(1);
        wheelView0.setItems(days);
        wheelView0.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                
            }
        });
        wheelView.setOffset(1);
        wheelView.setItems(Arrays.asList(HOURS));
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
            }
        });
        wheelView1.setOffset(1);
        wheelView1.setItems(Arrays.asList(MINITES));
        wheelView1.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent=new Intent(ScheduleActivity.this,OrderSureActivity.class);
                //ViewUtil.startActivity(ScheduleActivity.this,intent);
                showSureDialog();
            }
        });
    }
    private void showSureDialog(){
        LayoutInflater inflater=getLayoutInflater();
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.dialog_order_sure, null);
        Dialog dialog = new Dialog(this,R.style.myDialogTheme);
        dialog.setContentView(rootView);
        //dialog.create();
       // dialog.setContentView(rootView);
        dialog.show();
      /*  FragmentManager fm=getSupportFragmentManager();
        DialogSureOrderFragment dialogSureOrderFragment=new DialogSureOrderFragment();
        dialogSureOrderFragment.show(fm,"ll");*/
    }
}
