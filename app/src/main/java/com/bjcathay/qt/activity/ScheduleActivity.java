package com.bjcathay.qt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.fragment.DialogSureOrderFragment;
import com.bjcathay.qt.model.PriceModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.model.StadiumModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.PickerView;
import com.bjcathay.qt.view.TopView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dengt on 15-4-21.
 */
public class ScheduleActivity extends FragmentActivity implements ICallback {
    private GApplication gApplication;
    private PickerView hourView;
    private PickerView wheelView1;
    private PickerView dayView;
    private ImageView imageView;
    private TextView stadiumContents;
    private TextView stadiumAddress;
    private TextView stadiumPrice;
    private RadioGroup radioGroup;
    private ProductModel stadiumModel;
    private Button okbtn;
    private List<String> days;
    private List<String> hours;
    private TopView topView;
    private Long id;
    private String imaUrl;
    private String daySelect;
    private int beforSelect = 0;//0上午　１下午
    private String hourSelect = "07:00";
    private List<String> minits = new ArrayList<String>();
    private int attendNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        gApplication = GApplication.getInstance();
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
        stadiumAddress = ViewUtil.findViewById(this, R.id.sch_address);
        stadiumContents = ViewUtil.findViewById(this, R.id.sch_content);
        stadiumPrice = ViewUtil.findViewById(this, R.id.sch_price);
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
                // ((TextView) findViewById(radioButtonId)).setTextColor(Color.WHITE);

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
                getDayPrice(stadiumModel.getPrices());
            }
        });


    }

    private void initEvent() {
        topView.setTitleText("加载中");
        topView.setActivity(this);
        dayView.setData(days);
        dayView.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text,int i) {
                daySelect = text;
                getDate();
                getDayPrice(stadiumModel.getPrices());
            }
        });
        dayView.setSelected(0);
        wheelView1.setData(minits);
        wheelView1.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text,int i) {
                if ("上午".equals(text)) {
 //                   hourView.setData(DateUtil.getAM(stadiumModel.getStartAt()));
                    beforSelect = 0;
                    hourSelect = "07:00";
                } else {
  //                  hourView.setData(DateUtil.getPM(stadiumModel.getEndAt()));
                    beforSelect = 1;
                    hourSelect = "12:00";
                }
                hourView.setSelected(0);

                getDate();
                //second_pv.setData(secondss);
            }
        });
        hourView.setData(hours);
        hourView.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text,int i) {
                hourSelect = text;
                getDate();
            }
        });
        hourView.setSelected(0);
        wheelView1.setSelected(0);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent=new Intent(ScheduleActivity.this,OrderSureActivity.class);
                //ViewUtil.startActivity(ScheduleActivity.this,intent);
                if (gApplication.isLogin() == true) {
                    showDialog();
                } else {
                    Intent intent = new Intent(ScheduleActivity.this, LoginActivity.class);
                    ViewUtil.startActivity(ScheduleActivity.this, intent);
                }
            }
        });
    }

    DialogSureOrderFragment dialogSureOrderFragment;

    private void showDialog() {
      /*  if (stadiumModel != null) {
            dialogSureOrderFragment = new DialogSureOrderFragment(this, stadiumModel, getDate(), attendNumber);
            dialogSureOrderFragment.show(getSupportFragmentManager(), "sure");
        }*/
    }

    private void initData() {
        minits.add("上午");
        minits.add("下午");
        days = DateUtil.getDate(this);
        hours = DateUtil.getAM("07:00");
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        imaUrl = intent.getStringExtra("imageurl");
        ImageViewAdapter.adapt(imageView, imaUrl, R.drawable.exchange_default);
        ProductModel.product(id).done(this);
    }


    @Override
    public void call(Arguments arguments) {
        stadiumModel = arguments.get(0);
        hours = DateUtil.getAM(stadiumModel.getPrices().get(0).getStartAt());
        topView.setTitleText(stadiumModel.getName());
       // String endAt = stadiumModel.getEndAt();
        stadiumContents.setText(stadiumModel.getPriceInclude());
        // stadiumPrice.setText(stadiumModel.getPrice());
        if ("SPECIAL".equals(stadiumModel.getType())) {
            days.clear();
        //    days.add(stadiumModel.getDate().substring(0, 10));
            minits.clear();
            minits.add("上午");
            hours.clear();
     //       hours.add(stadiumModel.getDate().substring(11, 15));
        } else if ("LIMITED".equals(stadiumModel.getType())) {
            days.clear();
    //        days.add(stadiumModel.getDate().substring(0, 10));
        }
        getDayPrice(stadiumModel.getPrices());
        stadiumAddress.setText(stadiumModel.getAddress());
    }

    private String select;

    private String getDate() {
        if (beforSelect == 1) {
            hourSelect = DateUtil.To24(hourSelect);
        }
        Calendar c = Calendar.getInstance();
        //取得系统日期:
        int year = c.get(Calendar.YEAR);
        if (daySelect == null) {
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            daySelect = ((month + 1) > 10 ? (month + 1) : "0" + (month + 1)) + "月" + (day > 10 ? day : "0" + day) + "日";
        }
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(daySelect);
        String day = m.replaceAll("").trim();
        String daysub = day.substring(2);
        select = year + "-" + day.substring(0, 2) + "-"
                + (daysub.length() == 1 ? "0" + daysub : daysub)
                + " " + (hourSelect.length() == 1 ? "0" + hourSelect : hourSelect) + ":00";
        Log.e("选择的日期是", select);
        // getDayPrice(stadiumModel.getPrices());
        return select;
    }

    private void getDayPrice(List<PriceModel> priceModels) {
        if (select == null)
            getDate();
        for (PriceModel priceModel : priceModels) {
            if (DateUtil.CompareTime(select, priceModel.getStartAt(), priceModel.getEndAt()) == true) {
                stadiumPrice.setText("￥" + Double.toString(priceModel.getPrice() * attendNumber));
                return;
            }
        }
        stadiumPrice.setText("￥--");
    }
}
