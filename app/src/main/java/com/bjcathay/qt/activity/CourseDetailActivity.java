package com.bjcathay.qt.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.TimeView;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.PickerView;
import com.bjcathay.qt.view.TopView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bjcathay on 15-5-18.
 */
public class CourseDetailActivity extends FragmentActivity implements ICallback, View.OnClickListener {
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
    private List<String> days = new ArrayList<String>();
    private List<String> hours = new ArrayList<String>();
    private TopView topView;
    private Long id;
    private String imaUrl;
    private String daySelect;
    private int beforSelect = 0;//0上午　１下午
    private String hourSelect = "08:00";
    private List<String> minits = new ArrayList<String>();
    private int attendNumber = 1;
    private int selectDay = 0;
    //状态标识
    private ImageView tuanImg;
    private ImageView tuanDisableImg;
    private ImageView temaiImg;
    private ImageView temaiDisableImg;
    private TextView tuanCount;
    private TextView temaiCount;
    private TextView soldOut;

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
        topView.setTitleText("加载中");
        //状态标识
        tuanImg = ViewUtil.findViewById(this, R.id.tuan_img);
        tuanDisableImg = ViewUtil.findViewById(this, R.id.tuan_disable_img);
        temaiImg = ViewUtil.findViewById(this, R.id.temai_img);
        temaiDisableImg = ViewUtil.findViewById(this, R.id.temai_disable_img);
        tuanCount = ViewUtil.findViewById(this, R.id.tuan_short);
        temaiCount = ViewUtil.findViewById(this, R.id.temai_short);
        soldOut = ViewUtil.findViewById(this, R.id.seld_out_short);
    }

    private void initEvent() {
        dayView.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text, int i) {

                if (!"GROUP".equals(stadiumModel.getType())) {
                    daySelect = text;
                    getDate();
                    selectDay = i;
                    if ("SPECIAL".equals(stadiumModel.getType())) {
                        getDayPrice(stadiumModel.getPrice());
                    } else {
                        getDayPrice(priceModels.get(i).getPrice());

                    }
                } else {
                    select = stadiumModel.getDate();
                }
            }
        });

        wheelView1.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int i) {
                if (!"GROUP".equals(stadiumModel.getType())) {
                    if ("上午".equals(text)) {
                        //todo 目前写死
                        // hourView.setData(DateUtil.getAM(priceModels.get(selectDay).getStartAt()));
                        hourView.setData(DateUtil.getAM("08:00"));
                        beforSelect = 0;
                        hourSelect = "08:00";
                    } else {
                        //  hourView.setData(DateUtil.getPM(priceModels.get(selectDay).getEndAt()));
                        hourView.setData(DateUtil.getPMShort("18:00"));
                        beforSelect = 1;
                        hourSelect = "12:00";
                    }
                }
                hourView.setSelected(0);
                getDate();
            }
        });
        hourView.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text, int i) {
                if (!"GROUP".equals(stadiumModel.getType())) {
                    hourSelect = text;
                    getDate();
                }
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gApplication.isLogin() == true) {
                    showDialog();
                } else {
                    Intent intent = new Intent(CourseDetailActivity.this, LoginActivity.class);
                    ViewUtil.startActivity(CourseDetailActivity.this, intent);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailActivity.this, GolfCourseDetailActicity.class);
                intent.putExtra("id", stadiumModel.getGolfCourseId());
                ViewUtil.startActivity(CourseDetailActivity.this, intent);
            }
        });
        stadiumAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailActivity.this, AddressActivity.class);
                intent.putExtra("url", getString(R.string.course_address));
                intent.putExtra("location", stadiumModel.getLat() + "," + stadiumModel.getLon());
                intent.putExtra("title", "球场地址");
                intent.putExtra("content", stadiumModel.getAddress());
                //intent.putExtra("address",stationAddress.getText().toString().trim());
                intent.putExtra("src", "A|GOLF");
                ViewUtil.startActivity(CourseDetailActivity.this, intent);
            }
        });
        //绑定一个匿名监听器
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
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
                        attendNumber = 0;
                        break;
                }
                getDayPrice(stadiumModel.getPrice());
            }
        });
    }

    DialogSureOrderFragment dialogSureOrderFragment;

    private void showDialog() {
        if (stadiumModel != null) {
            dialogSureOrderFragment = new DialogSureOrderFragment(this, stadiumModel, getDate(), attendNumber);
            dialogSureOrderFragment.show(getSupportFragmentManager(), "sure");
        }
    }

    private void initData() {
        minits.add("上午");
        minits.add("下午");
        topView.setTitleBackVisiable();
        topView.setShareVisiable();
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        imaUrl = intent.getStringExtra("imageurl");
        ImageViewAdapter.adapt(imageView, imaUrl, R.drawable.ic_launcher);
        ProductModel.product(id).done(this);
    }

    List<PriceModel> priceModels;

    @Override
    public void call(Arguments arguments) {
        stadiumModel = arguments.get(0);
        topView.setTitleText(stadiumModel.getName());
        //控制LIMIT最低人数
        if ("LIMIT".equals(stadiumModel.getType())) {
            int num = stadiumModel.getAmount();
            attendNumber = num;
            for (int i = 0; i < num - 1; i++)
                radioGroup.getChildAt(i).setVisibility(View.GONE);
            radioGroup.getChildAt(num - 1).isSelected();
            //  radioGroup.getChildAt(stadiumModel.getAmount()).setBackgroundColor(Color.GRAY);
        }
        if ("GROUP".equals(stadiumModel.getType())) {
            days.clear();
            select = stadiumModel.getDate();
            String tuan_day = DateUtil.getTuanFinalDays(stadiumModel.getDate());
            days.add(tuan_day);
            dayView.setData(days);
            String tuan_am_pm = DateUtil.getTuanFinalAMoPM(stadiumModel.getDate());
            minits.clear();
            minits.add(tuan_am_pm);
            wheelView1.setData(minits);
            if ("下午".equals(tuan_am_pm))
                hours = DateUtil.getPM(stadiumModel.getDate());
            else
                hours = DateUtil.getAM(stadiumModel.getDate());
            hourView.setData(hours);
            getDayPrice(stadiumModel.getPrice());


            tuanImg.setVisibility(View.VISIBLE);
            tuanCount.setVisibility(View.VISIBLE);
            Date start = DateUtil.stringToDate(stadiumModel.getNow());
            Date end = DateUtil.stringToDate(stadiumModel.getEnd());
            long diff = end.getTime() - start.getTime();
            TimeView timeView = new TimeView(diff, 1000, tuanCount, tuanImg, okbtn);
            timeView.start();
            // tuanCount.setText(stadiumModel.getAmount());

        } else if ("SPECIAL".equals(stadiumModel.getType())) {
            days.clear();
            select = stadiumModel.getDate();
            String tuan_day = DateUtil.getTuanFinalDays(stadiumModel.getDate());
            days.add(tuan_day);
            dayView.setData(days);
            wheelView1.setData(minits);
            //todo 目前写死
            // hours = DateUtil.getAM(stadiumModel.getDate());
            hours = DateUtil.getAM("08:00");
            hourView.setData(hours);
            getDayPrice(stadiumModel.getPrice());
            temaiImg.setVisibility(View.VISIBLE);
            temaiCount.setVisibility(View.VISIBLE);
            if (stadiumModel.getAmount() > 0) {
            } else {
                temaiImg.setImageResource(R.drawable.ic_te_disable);
                temaiCount.setBackgroundColor(Color.GRAY);
                okbtn.setBackgroundColor(Color.GRAY);
                okbtn.setOnClickListener(null);
            }
            temaiCount.setText("仅剩" + stadiumModel.getAmount() + "个名额");
            //NONE
        } else if ("LIMIT".equals(stadiumModel.getType()) || "NONE".equals(stadiumModel.getType())) {
            priceModels = DateUtil.getCollectionsDate(stadiumModel.getPrices());
            days.clear();
            dayView.setData(DateUtil.getLimitDate(priceModels));
            wheelView1.setData(minits);
            hours = DateUtil.getAM("08:00");
            hourView.setData(hours);
            if ("LIMIT".equals(stadiumModel.getType())) {
                int num = stadiumModel.getAmount();
                attendNumber = num;
                for (int i = 0; i < num - 1; i++)
                    radioGroup.getChildAt(i).setVisibility(View.GONE);
                radioGroup.check(radioGroup.getChildAt(num - 1).getId());
                //  radioGroup.getChildAt(stadiumModel.getAmount()).setBackgroundColor(Color.GRAY);
            } else
                getDayPrice(stadiumModel.getPrice());
        } else {
            String endAt = stadiumModel.getPrices().get(0).getEndAt();
            stadiumContents.setText(stadiumModel.getPriceInclude());
        }

        //    getDayPrice(stadiumModel.getPrices().get(0).getPrice());
        stadiumAddress.setText(stadiumModel.getAddress());
        wheelView1.setSelected(0);
        dayView.setSelected(0);
        hourView.setSelected(0);
    }

    private String select;

    private String getDate() {
        if ("GROUP".equals(stadiumModel.getType()))
            return stadiumModel.getDate();
        else if (beforSelect == 1) {
            hourSelect = DateUtil.To24(hourSelect);
        }
        Calendar c = Calendar.getInstance();
        Date d = null;
        if ("SPECIAL".equals(stadiumModel.getType())) {
            d = DateUtil.stringToDate(stadiumModel.getDate());
        } else {
            d = DateUtil.stringToDate(priceModels.get(0).getStartAt());
        }
        c.setTime(d);
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

    private void getDayPrice(double price) {
        if ("LIMIT".equals(stadiumModel.getType()) || "NONE".equals(stadiumModel.getType())) {
            if (select == null)
                getDate();
            for (PriceModel priceModel : priceModels) {
                if (DateUtil.CompareTime(select, priceModel.getStartAt(), priceModel.getEndAt()) == true) {
                    if (attendNumber == 0)
                        stadiumPrice.setText("￥" + Double.toString(priceModel.getPrice() * 4) + "+");
                    else
                        stadiumPrice.setText("￥" + Double.toString(priceModel.getPrice() * attendNumber));
                    return;
                }
            }
            return;
        }
        if (attendNumber == 0)
            stadiumPrice.setText("￥" + price * 4 + "+");
        else
            stadiumPrice.setText("￥" + price * attendNumber);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.title_share_img:
                ShareUtil.showShare(this);
                break;
            case R.id.title_delete_img:
                if (dialogSureOrderFragment != null)
                    dialogSureOrderFragment.dismiss();
                break;
        }
    }
}
