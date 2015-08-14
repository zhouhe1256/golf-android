
package com.bjcathay.qt.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.LogUtil;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.fragment.DialogOrderInformationFragment;
import com.bjcathay.qt.model.PriceModel;
import com.bjcathay.qt.model.ProductListModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.TimeView;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;
import com.bjcathay.qt.widget.TosGallery;
import com.bjcathay.qt.widget.WheelTextAdapter;
import com.bjcathay.qt.widget.WheelView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dengt on 15-6-17.
 */
public class OrderStadiumDetailActivity extends FragmentActivity implements ICallback,
        View.OnClickListener, DeleteInfoDialog.DeleteInfoDialogResult {
    private WheelView mOption1 = null;
    private WheelView mOption2 = null;
    private WheelView mOption3 = null;
    private FragmentActivity context;
    private GApplication gApplication;
    private ImageView imageView;
    private TextView stadiumContents;
    private TextView stadiumAddress;
    private TextView stadiumPrice;
    private TextView schBack;
    private RadioGroup radioGroup;
    private ProductModel stadiumModel;
    private Button okbtn;
    private List<String> days = new ArrayList<String>();
    private List<String> hoursAM = new ArrayList<String>();
    private List<String> hoursPM = new ArrayList<String>();
    private List<String> minits = new ArrayList<String>();
    private TopView topView;
    private Long id;
    private String imaUrl;
    private String daySelect;
    private String beforSelect;// 0上午　１下午
    private String hourSelect = "07:00";
    private int attendNumber = 1;

    private TextView tuanCount;
    private TextView temaiCount;
    private TextView soldOut;
    private List<PriceModel> priceModels;
    private int currentPrice;
    private ShareModel shareModel;
    private List<String> hoursAMnow = new ArrayList<String>();
    private List<String> hoursPMnow = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursedetail);
        gApplication = GApplication.getInstance();
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_schedule_layout);
        topView.setTitleBackVisiable();
        imageView = ViewUtil.findViewById(this, R.id.place_img);
        stadiumAddress = ViewUtil.findViewById(this, R.id.sch_address);
        stadiumContents = ViewUtil.findViewById(this, R.id.sch_content);
        stadiumPrice = ViewUtil.findViewById(this, R.id.sch_price);
        schBack = ViewUtil.findViewById(this, R.id.sch_back);
        okbtn = ViewUtil.findViewById(this, R.id.ok);
        // 根据ID找到RadioGroup实例
        radioGroup = (RadioGroup) this.findViewById(R.id.radio_group);
        topView.setTitleText("加载中");
        tuanCount = ViewUtil.findViewById(this, R.id.tuan_short);
        temaiCount = ViewUtil.findViewById(this, R.id.temai_short);
        soldOut = ViewUtil.findViewById(this, R.id.seld_out_short);
        payType = ViewUtil.findViewById(this, R.id.pay_type);
        mOption1 = (WheelView) findViewById(R.id.wheel_date);
        mOption2 = (WheelView) findViewById(R.id.wheel_month);
        mOption3 = (WheelView) findViewById(R.id.wheel_year);

        mOption1.setOnEndFlingListener(mListener);
        mOption2.setOnEndFlingListener(mListener);
        mOption3.setOnEndFlingListener(mListener);

        mOption1.setSoundEffectsEnabled(true);
        mOption2.setSoundEffectsEnabled(true);
        mOption3.setSoundEffectsEnabled(true);

        mOption1.setAdapter(new WheelTextAdapter(this));
        mOption2.setAdapter(new WheelTextAdapter(this));
        mOption3.setAdapter(new WheelTextAdapter(this));

    }

    private void initEvent() {
        okbtn.setOnClickListener(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GolfCourseDetailActicity.class);
                intent.putExtra("id", stadiumModel.getGolfCourseId());
                ViewUtil.startActivity(context, intent);
            }
        });
        stadiumAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stadiumModel != null) {
                    Intent intent = new Intent(context, BaiduAddressActivity.class);
                    intent.putExtra("url", getString(R.string.course_address));
                    intent.putExtra("location", stadiumModel.getLat() + "," + stadiumModel.getLon());
                    intent.putExtra("lat", stadiumModel.getLat());
                    intent.putExtra("lon", stadiumModel.getLon());
                    intent.putExtra("title", stadiumModel.getName());
                    intent.putExtra("content", stadiumModel.getAddress());
                    intent.putExtra("src", "A|GOLF");
                    ViewUtil.startActivity(context, intent);
                }
            }
        });
        // 绑定一个匿名监听器
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                // 获取变更后的选中项的ID
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
                        attendNumber = 0;
                        break;
                }
                if (stadiumModel != null)
                    getDayPrice(stadiumModel.getPrice());
            }
        });
    }

    DialogOrderInformationFragment dialogSureOrderFragment;

    private void showDialog() {
        if (stadiumModel != null) {
            String orderDate = getDate();
            if (DateUtil.CompareNowTime(orderDate)) {
                dialogSureOrderFragment = new DialogOrderInformationFragment(this, stadiumModel,
                        finalPrice, orderDate, attendNumber, hourSelect);

                dialogSureOrderFragment.showAtLocation(this.findViewById(R.id.ok), Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                // dialogSureOrderFragment.show(getSupportFragmentManager(),
                // "sure");
            } else {
                DialogUtil.showMessage("您选的时间已过呦～");
            }
        }
    }

    private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {
        @Override
        public void onEndFling(TosGallery v) {
            int pos = v.getSelectedItemPosition();
            if (days != null && !days.isEmpty()) {
                if (v == mOption1) {
                    String info = days.get(pos);
                    setDate(info, pos);
                } else if (v == mOption2) {
                    String info = minits.get(pos);
                    setMinit(info);
                } else if (v == mOption3) {
                    String info;
                    if ("上午".equals(beforSelect)) {
                        info = hoursAM.get(pos);
                        setHour(info);
                    } else {
                        info = hoursPM.get(pos);
                        setHour(info);
                    }
                }
                if (stadiumModel != null) {
                    getDate();
                    if ("LIMIT".equals(stadiumModel.getType())
                            || "NONE".equals(stadiumModel.getType()))
                        getDayPrice(0);
                    else
                        getDayPrice(stadiumModel.getPrice());
                }
            }
        }
    };

    private void setDate(String date, int pos) {
        if (!date.equals(daySelect)) {
            daySelect = date;
            // todo 设置小时数据
            // todo 1
            if (ProductType.prdtType.TIME.equals(stadiumModel.getType())) {
                // if (hoursAMnow.isEmpty())
                hoursAMnow = DateUtil.getAM("00:00");
                // if (hoursPMnow.isEmpty())
                hoursPMnow = DateUtil.getPMShort("23:30");
                // todo 2
                for (int i = hoursAMnow.size() - 1; i >= 0; i--) {
                    if (!priceModels.get(pos).getAMTime(ProductType.prdtType.TIME,
                            hoursAMnow.get(i))) {
                        hoursAMnow.remove(i);
                    } else {

                    }
                    if (!priceModels.get(pos).getAMTime(ProductType.prdtType.TIME,
                            hoursPMnow.get(i))) {
                        hoursPMnow.remove(i);
                    } else {

                    }
                }
                hoursAM = hoursAMnow;
                hoursPM = hoursPMnow;
                minits.clear();
                if (!hoursAM.isEmpty()) {
                    minits.add("上午");
                }
                if (!hoursPM.isEmpty()) {
                    minits.add("下午");
                }

                ((WheelTextAdapter) mOption2.getAdapter()).setData(minits);
                if (minits.size() == 1) {
                    if ("上午".equals(minits.get(0))) {
                        beforSelect = "上午";
                        ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursAM);
                    }
                    else {
                        beforSelect = "下午";
                        ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursPM);
                    }
                } else {
                    beforSelect = "上午";
                    ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursAM);
                }

                // if (priceModels != null && priceModels.size() == 1) {
                // mOption1.setSelection(0);
                // } else
                // mOption1.setSelection(0);
                mOption2.setSelection(0);
                mOption3.setSelection(0);
              //  getDayPrice(0);
            }
        }
    }

    private void setMinit(String minit) {
        if (!minit.equals(beforSelect)) {
            beforSelect = minit;
            prepareDayData(minit);
        }
    }

    private void setHour(String hour) {
        if (!hour.equals(hourSelect)) {
            hourSelect = hour;
        }
    }

    private void prepareData(int a) {
        ((WheelTextAdapter) mOption1.getAdapter()).setData(days);
        ((WheelTextAdapter) mOption2.getAdapter()).setData(minits);
        if (minits.size() == 1) {
            if ("上午".equals(minits.get(0)))
                ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursAM);
            else
                ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursPM);
        } else
            ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursAM);

        if (priceModels != null && priceModels.size() == 1) {
            mOption1.setSelection(0);
        } else
            mOption1.setSelection(a);
        mOption2.setSelection(0);
        mOption3.setSelection(0);
        getDayPrice(0);
    }

    private void prepareDayData(String minit) {
        if ("上午".equals(minit)) {
            ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursAM);
            hourSelect = hoursAM.get(0);
            mOption3.setSelection(0);
        } else {
            ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursPM);
            hourSelect = hoursPM.get(0);
            mOption3.setSelection(0);
        }

    }

    private void initData() {
        minits.add("上午");
        minits.add("下午");
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        imaUrl = intent.getStringExtra("imageurl");
        if (imaUrl != null)
            ImageViewAdapter.adapt(imageView, imaUrl, R.drawable.exchange_default);
        ProductModel.product(id).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
            }
        });
    }

    public void setTextDate() {
        if (imaUrl == null)
            ImageViewAdapter.adapt(imageView, stadiumModel.getImageUrl(),
                    R.drawable.exchange_default);
        topView.setTitleText(stadiumModel.getName());
        priceModels = stadiumModel.getPrices();
        days.clear();
        if (ProductType.prdtType.TIME.equals(stadiumModel.getType())) {
            // if (hoursAMnow.isEmpty())
            days = DateUtil.getLimitDates(priceModels);
            hoursAMnow = DateUtil.getAM("00:00");
            // if (hoursPMnow.isEmpty())
            hoursPMnow = DateUtil.getPMShort("23:30");
            // todo 2
            for (int i = hoursAMnow.size() - 1; i >= 0; i--) {
                if (!priceModels.get(0).getAMTime(ProductType.prdtType.TIME,
                        hoursAMnow.get(i))) {
                    hoursAMnow.remove(i);
                } else {

                }
                if (!priceModels.get(0).getAMTime(ProductType.prdtType.TIME,
                        hoursPMnow.get(i))) {
                    hoursPMnow.remove(i);
                } else {

                }
            }
            hoursAM = hoursAMnow;
            hoursPM = hoursPMnow;
            minits.clear();
            if (!hoursAM.isEmpty()) {
                minits.add("上午");
            }
            if (!hoursPM.isEmpty()) {
                minits.add("下午");
            }
            ((WheelTextAdapter) mOption1.getAdapter()).setData(days);
            ((WheelTextAdapter) mOption2.getAdapter()).setData(minits);
            if (minits.size() == 1) {
                if ("上午".equals(minits.get(0))) {
                    beforSelect = "上午";
                    ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursAM);
                    hourSelect = hoursAM.get(0);
                }
                else {
                    beforSelect = "下午";
                    ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursPM);
                    hourSelect = hoursPM.get(0);
                }
            } else {
                beforSelect = "上午";
                ((WheelTextAdapter) mOption3.getAdapter()).setData(hoursAM);
                hourSelect = hoursAM.get(0);
            }

            if (priceModels != null && priceModels.size() == 1) {
                mOption1.setSelection(0);
            } else {
                mOption1.setSelection(1);
            }
            mOption2.setSelection(0);
            mOption3.setSelection(0);
            getDayPrice(0);
        } else {
            hoursAM = DateUtil.getAM(stadiumModel.getBhStartAt().substring(0, 5));
            hoursPM = DateUtil.getPMShort(stadiumModel.getBhEndAt().substring(0, 5));
            // todo
            hourSelect = hoursAM.get(0);
            beforSelect = "上午";
            days = DateUtil.getLimitDates(priceModels);
            prepareData(1);
        }
        if ("LIMIT".equals(stadiumModel.getType())) {
            int num = stadiumModel.getAmount();
            attendNumber = num;
            for (int i = 0; i < num - 1; i++)
                radioGroup.getChildAt(i).setVisibility(View.GONE);
            radioGroup.check(radioGroup.getChildAt(num - 1).getId());
        } else
            getDayPrice(stadiumModel.getPrice());
        stadiumContents.setText(stadiumModel.getPriceInclude());
        stadiumAddress.setText(stadiumModel.getAddress());
        if (ProductType.payType.PREPAY.equals(stadiumModel.getPayType())) {
            payType.setText("(全额预付)");
        } else if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
            payType.setText("(部分预付)");
        } else if (ProductType.payType.SPOTPAY.equals(stadiumModel.getPayType())) {
            payType.setText("(全额现付)");
        }
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
            stadiumModel = JSONUtil.load(ProductModel.class, jsonObject.optJSONObject("product"));
            // stadiumModel = arguments.get(0);
            if (imaUrl == null)
                ImageViewAdapter.adapt(imageView, stadiumModel.getImageUrl(),
                        R.drawable.exchange_default);
            topView.setTitleText(stadiumModel.getName());
            setTextDate();
        } else {
            String errorMessage = jsonObject.optString("message");
            if (!StringUtils.isEmpty(errorMessage))
                DialogUtil.showMessage(errorMessage);
            else {
                int code = jsonObject.optInt("code");
                DialogUtil.showMessage(ErrorCode.getCodeName(code));
            }
        }
    }

    private String select;

    private String getDate() {
        if ("下午".equals(beforSelect)) {
            // todo 24
            // hourSelect = DateUtil.To24(hourSelect);
        }
        Calendar c = Calendar.getInstance();
        Date d = null;
        if (priceModels != null && priceModels.size() == 1) {
            d = DateUtil.stringToDate(priceModels.get(0).getStartAt());
        } else {
            if ("SPECIAL".equals(stadiumModel.getType())) {
                d = DateUtil.stringToDate(priceModels.get(1).getStartAt());
            } else if ("GROUP".equals(stadiumModel.getType())) {
                d = DateUtil.stringToDate(priceModels.get(0).getStartAt());
            } else {

                d = DateUtil.stringToDate(priceModels.get(1).getStartAt());
            }
        }
        c.setTime(d);
        // 取得系统日期:
        int year = c.get(Calendar.YEAR);
        if (daySelect == null) {
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            daySelect = ((month + 1) > 10 ? (month + 1) : "0" + (month + 1)) + "月"
                    + (day > 10 ? day : "0" + day) + "日";
        }
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(daySelect);
        String day = m.replaceAll("").trim();
        String daysub = day.substring(2);
        select = year + "-" + day.substring(0, 2) + "-"
                + (daysub.length() == 1 ? "0" + daysub : daysub)
                + " " + (hourSelect.length() == 1 ? "0" + hourSelect : hourSelect) + ":00";
        LogUtil.e("选择的日期是", select);
        return select;
    }

    ColorStateList csl;
    boolean noPrice;
    private PriceModel finalPrice;
    private TextView payType;

    private void getDayPrice(double price) {
        if (ProductType.prdtType.REAL_TIME.equals(stadiumModel.getType())) {
            // okbtn.setBackgroundResource(R.drawable.yellow_big_selector);
            okbtn.setBackgroundResource(R.color.yellow_bg_color);
            okbtn.setText("联系客服");
            stadiumPrice.setText("实时计价");
            noPrice = true;
            schBack.setVisibility(View.GONE);
            payType.setVisibility(View.GONE);
            okbtn.setOnClickListener(this);
            return;
        }
        if (select == null)
            getDate();
        if (priceModels != null && priceModels.size() > 0)
            for (PriceModel priceModel : priceModels) {
                if (DateUtil.CompareTime(select, priceModel.getStartAt(), priceModel.getEndAt()) == true) {

                    if (ProductType.priceType.INACTIVE.equals(priceModel.getStatus())) {
                        // okbtn.setBackgroundResource(R.drawable.bg_sold_out);
                        okbtn.setBackgroundResource(R.color.gray);
                        okbtn.setOnClickListener(null);
                        okbtn.setText("提交订单");
                        stadiumPrice.setText("球场休息");
                        schBack.setVisibility(View.GONE);
                        payType.setVisibility(View.GONE);
                        stadiumPrice.setTextColor(Color.GRAY);
                    } else if (ProductType.priceType.REAL_TIME.equals(priceModel.getStatus())) {
                        // okbtn.setBackgroundResource(R.drawable.yellow_big_selector);
                        okbtn.setBackgroundResource(R.color.yellow_bg_color);
                        okbtn.setText("联系客服");
                        stadiumPrice.setText("实时计价");
                        schBack.setVisibility(View.GONE);
                        payType.setVisibility(View.GONE);
                        noPrice = true;
                        okbtn.setOnClickListener(this);
                        return;
                    } else {

                        noPrice = false;
                        okbtn.setText("提交订单");
                        stadiumContents.setText(priceModel.getPriceInclude());
                        // okbtn.setBackgroundResource(R.drawable.yellow_big_selector);
                        okbtn.setBackgroundResource(R.color.yellow_bg_color);
                        okbtn.setOnClickListener(this);
                        if (csl != null) {
                            stadiumPrice.setTextColor(csl);
                        } else {
                            csl = getResources().getColorStateList(R.color.order_price_color);
                            stadiumPrice.setTextColor(csl);
                        }
                        if (attendNumber == 0) {
                            int[] priceStr = priceModel.getFianlPrice(stadiumModel.getType(),
                                    4, hourSelect);
                            stadiumPrice.setText("￥" + (int) Math.floor(priceStr[0]) * 4
                                    + "+");
                            currentPrice = (int) Math.floor(priceModel.getPrice());
                        } else {
                            priceModel.getPersonsDuring(stadiumModel.getType());
                            int min = priceModel.getMinPerson();
                            if (attendNumber < min) {
                                attendNumber = min;
                                for (int i = 0; i < min - 1; i++) {
                                    radioGroup.getChildAt(i).setVisibility(View.GONE);
                                    break;
                                }
                                radioGroup.getChildAt(attendNumber - 1).isSelected();
                            }

                            int[] priceStr = priceModel.getFianlPrice(stadiumModel.getType(),
                                    attendNumber, hourSelect);
                            stadiumPrice
                                    .setText("￥"
                                            + (int) Math
                                            .floor((priceStr[0] * attendNumber == 0 ? priceModel
                                                    .getPrice()
                                                    : priceStr[0]
                                                    * attendNumber)) + "");
                            if (priceStr[1] != 0) {
                                schBack.setText("返" + priceStr[1] * attendNumber);
                                schBack.setVisibility(View.VISIBLE);
                            }
                            else
                                schBack.setVisibility(View.GONE);
                            payType.setVisibility(View.VISIBLE);
                            finalPrice = priceModel;
                            currentPrice = (int) Math.floor(priceModel.getPrice());
                        }
                    }
                    return;
                }
            }
        return;
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.title_share_img:
                if (shareModel == null)
                    ShareModel.shareProducts(id).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            shareModel = arguments.get(0);
                            ShareUtil.getInstance().shareDemo(context, shareModel);
                        }
                    });
                else
                    ShareUtil.getInstance().shareDemo(context, shareModel);
                break;
            case R.id.ok:
                if (gApplication.isLogin() == true) {
                    if (noPrice) {
                        DeleteInfoDialog infoDialog = new DeleteInfoDialog(this,
                                R.style.InfoDialog, getResources()
                                .getString(R.string.service_tel_format)
                                .toString().trim(), "呼叫", 0l, this);
                        infoDialog.show();
                    } else
                        showDialog();
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    ViewUtil.startActivity(context, intent);

                }
                break;
        }
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + getResources().getString(R.string.service_tel).toString().trim()));
            this.startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("产品详情页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("产品详情页面");
        MobclickAgent.onPause(this);
    }
}
