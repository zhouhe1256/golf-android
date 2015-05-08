package com.bjcathay.golf.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.golf.R;
import com.bjcathay.golf.model.StadiumModel;
import com.bjcathay.golf.util.DateUtil;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.PickerView;
import com.bjcathay.golf.view.TopView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dengt on 15-4-21.
 */
public class ScheduleActivity extends FragmentActivity implements ICallback {
    private PickerView hourView;
    private PickerView wheelView1;
    private PickerView dayView;
    private ImageView imageView;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private StadiumModel stadiumModel;
    private Button okbtn;
    private List<String> days;
    private List<String> hours;
    private TopView topView;
    private Long id;
    private String imaUrl;
    private String daySelect;
    private int beforSelect = 0;//0上午　１下午
    private String hourSelect = "7:00";
    private List<String> minits = new ArrayList<String>();
    private int attendNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        hourView = ViewUtil.findViewById(this, R.id.wheelView);
        wheelView1 = ViewUtil.findViewById(this, R.id.wheelView1);
        dayView = ViewUtil.findViewById(this, R.id.wheelView0);
        topView = ViewUtil.findViewById(this, R.id.top_schedule_layout);
        imageView = ViewUtil.findViewById(this, R.id.place_img);
        okbtn = ViewUtil.findViewById(this, R.id.ok);
        //根据ID找到RadioGroup实例
        radioGroup = (RadioGroup) this.findViewById(R.id.radio_group);
        //绑定一个匿名监听器
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                              /* RadioButton rb = (RadioButton)ScheduleActivity.this.findViewById(radioButtonId);
                               //更新文本内容，以符合选中项
                            tv.setText("您的性别是：" + rb.getText());*/
                switch (radioButtonId) {
                    case R.id.btn_0:
                        attendNumber = 1;
                        break;
                    case R.id.btn_1:
                        attendNumber = 2;
                        break;
                    case R.id.btn_2:
                        attendNumber = 3;
                        break;
                    case R.id.btn_3:
                        attendNumber = 4;
                        break;
                    case R.id.btn_4:
                        //转人工
                        //showSureDialog();
                        break;
                }
            }
        });


    }

    private void initEvent() {
        topView.setTitleText("加载中");
        topView.setActivity(this);
        dayView.setData(days);
        dayView.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                daySelect = text;
                getPrice();
            }
        });
        dayView.setSelected(0);
        wheelView1.setData(minits);
        wheelView1.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                if ("上午".equals(text)) {
                    hourView.setData(DateUtil.getAM(stadiumModel.getStartAt()));
                    beforSelect = 0;
                    hourSelect = "7:00";
                } else {
                    hourView.setData(DateUtil.getPM(stadiumModel.getEndAt()));
                    beforSelect = 1;
                    hourSelect = "12:00";
                }
                hourView.setSelected(0);

                getPrice();
                //second_pv.setData(secondss);
            }
        });
        hourView.setData(hours);
        hourView.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                hourSelect = text;
                getPrice();
            }
        });
        hourView.setSelected(0);
        wheelView1.setSelected(0);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent=new Intent(ScheduleActivity.this,OrderSureActivity.class);
                //ViewUtil.startActivity(ScheduleActivity.this,intent);
                showSureDialog();
            }
        });
    }

    private void initData() {
        minits.add("上午");
        minits.add("下午");
        days = DateUtil.getDate(this);
        hours = DateUtil.getAM("7:00");
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        imaUrl = intent.getStringExtra("imageurl");
        ImageViewAdapter.adapt(imageView, imaUrl, R.drawable.ic_launcher);

        StadiumModel.stadiumDetail(id).done(this);
    }

    private void showSureDialog() {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.dialog_order_sure, null);
        TopView topView1 = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_title);
        topView1.setVisiable(View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
        topView1.setRightbtn("X", 0);

        TextView name = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_name);
        TextView time = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_time);
        TextView phone = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_phone);
        TextView service = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_service);
        TextView number_ = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_number);
        TextView price = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_price);
        Button sure = ViewUtil.findViewById(rootView, R.id.sure_order);

        name.setText(stadiumModel.getName());
        time.setText(getPrice());
        //手机号码
        service.setText("提供服务:" + stadiumModel.getComboContent());
        number_.setText("消费人数：" + attendNumber + "人");
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        dialog.setContentView(rootView);
        price.setText("价格:￥" + Double.valueOf(stadiumModel.getPrice()) * attendNumber + "");
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
               Intent intent = new Intent(ScheduleActivity.this, RemindInfoActivity.class);
                ViewUtil.startActivity(ScheduleActivity.this, intent);
            }
        });
        topView1.getRightbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        //dialog.create();
        // dialog.setContentView(rootView);
        dialog.show();

    }


    @Override
    public void call(Arguments arguments) {
        stadiumModel = arguments.get(0);
        hours = DateUtil.getAM(stadiumModel.getStartAt());
        topView.setTitleText(stadiumModel.getName());
        String endAt = stadiumModel.getEndAt();
    }

    private String getPrice() {
        if (beforSelect == 1) {
            hourSelect = DateUtil.To24(hourSelect);
        }
        Calendar c = Calendar.getInstance();
        //取得系统日期:
        int year = c.get(Calendar.YEAR);
        if (daySelect == null) {
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            daySelect = ((month + 1) > 10 ? (month + 1) : "0" + (month + 1)) + "月" + (day > 10 ? 10 : "0" + day) + "日";
        }
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(daySelect);
        String day = m.replaceAll("").trim();
        String daysub = day.substring(2);
        String select = year + "-" + day.substring(0, 2) + "-"
                + (daysub.length() == 1 ? "0" + daysub : daysub)
                + " " + (hourSelect.length() == 1 ? "0" + hourSelect : hourSelect) + ":00";
        Log.e("选择的日期是", select);
        return select;
    }
}
