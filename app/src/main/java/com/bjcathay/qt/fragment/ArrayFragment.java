
package com.bjcathay.qt.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.PackagePriceModel;
import com.bjcathay.qt.model.PriceModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.SizeUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.RichTextView;
import com.bjcathay.qt.widget.TosGallery;
import com.bjcathay.qt.widget.WheelView;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dengt on 15-7-28.
 */
public class ArrayFragment extends Fragment {
    int mNum;
    private Context context;
    private static ProductModel productModel;
    private ChangePrice changePrice;
    private List<PriceModel> priceModels;
    private int years;
    private int months;
    private int days;
    int[] priceStr;
    private boolean judge;
   // PackagePriceModel packagePriceModel;
    public interface ChangePrice {
        void priceChanged(int price, PriceModel currentPrice, int number, String date);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.changePrice = (ChangePrice) activity;
    }

    public static ArrayFragment newInstance(int num, ProductModel productModel) {
        ArrayFragment array = new ArrayFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putSerializable("product", productModel);
        array.setArguments(args);
        return array;
    }

    public void setContext(Context context, ProductModel productModel) {
        this.context = context;
        this.productModel = productModel;
        this.priceModels = productModel.getPrices();
    }

    private TextView time;
    private String date;
    String hourSelect;
    private int number;
    private PriceModel currentPrice;
    private ImageView minas;
    private ImageView plus;
    private TextView fourPlus;
    private int amount = -1;
    private int amountmax = -1;
    private String daySelect;
    private int p = 0;
   /* private void getDate() {
        // mCurYear, mCurMonth + 1, mCurDate
        String mounth = mCurMonth + "";
        String day = mCurDate + "";
        daySelect = mCurYear + "-" + (mounth.length() == 1 ? "0" + mounth : mounth) + "-"
                + (day.length() == 1 ? "0" + day : day) + " 00:00:00";
        int[] priceStr = packagePriceModel.getFianlPrice(productModel.getType(),
                number, hourSelect);
        changePrice.priceChanged(priceStr[0], currentPrice, number, daySelect);
    }*/
    //获取当天的价格
    private void getDayPrice() {
            if (number < amount) {
                number = amount;
            }
           // fourPlus.setText(number < 10 ? " " + number : number + "");
            daySelect = years + "-" + months + "-" + days + " 00:00:00";
            changePrice.priceChanged(priceStr[0], currentPrice, number, daySelect);
        return;
    }

    private void person() {
        minas.setOnClickListener(new
                View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (amount > 0) {
                            if (number > amount) {
                                number--;
                                fourPlus.setText(number < 10 ? " " + number : number + "");
                                priceStr = productModel.getPrices().get(p).getFianlPrice(ProductType.prdtType.COMBO,number,null);
                                currentPrice = productModel.getPrices().get(p);
                                getDayPrice();
                            }
                        } else {
                            if (number > 1) {
                                number--;
                                fourPlus.setText(number < 10 ? " " + number : number + "");
                                priceStr = productModel.getPrices().get(p).getFianlPrice(ProductType.prdtType.COMBO,number,null);
                                currentPrice = productModel.getPrices().get(p);
                                getDayPrice();
                            }
                        }
                    }
                });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number < amountmax)
                    number++;
                fourPlus.setText(number < 10 ? " " + number : number + "");
                LogUtil.i("pos",p+"");
                priceStr = productModel.getPrices().get(p).getFianlPrice(ProductType.prdtType.COMBO,number,null);

                getDayPrice();
            }
        });
        fourPlus.setText(number < 10 ? " " + number : number + "");
        priceStr = productModel.getPrices().get(p).getFianlPrice(ProductType.prdtType.COMBO,number,null);
        getDayPrice();

    }

    // 日期选择
    WheelView mDateWheel = null;
    WheelView mMonthWheel = null;
    WheelView mYearWheel = null;

    int mCurDate = 0;
    int mCurMonth = 0;
    int mCurYear = 0;
    ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mDates = new ArrayList<TextInfo>();
    private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {
        @Override
        public void onEndFling(TosGallery v) {
            int pos = v.getSelectedItemPosition();

            if (v == mDateWheel) {
                p = pos;
                TextInfo info = mDates.get(pos);
                /*setDate(info.mIndex,pos);*/
                days = info.mIndex;
                priceStr = productModel.getPrices().get(pos).getFianlPrice(ProductType.prdtType.COMBO,number,null);
                currentPrice = productModel.getPrices().get(pos);
                LogUtil.i("prices",priceStr[0]+"");

            } else if (v == mMonthWheel) {
                TextInfo info = mMonths.get(pos);
                months = info.mIndex;
                setMonth(info.mIndex);
            } else if (v == mYearWheel) {
                TextInfo info = mYears.get(pos);
                years = info.mIndex;
                setYear(info.mIndex);
                setMonth(info.mIndex);
            }
               //getDate();
             getDayPrice();
        }
    };

    private String formatDate() {
        return String.format("Date: %d/%02d/%02d", mCurYear, mCurMonth + 1, mCurDate);
    }

    private void setDate(int date,int pos) {
        if (date != mCurDate) {
            mCurDate = date;

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void setYear(int year) {
        int t=0;
        try {
            mMonths.clear();
        for(int i=0;i<productModel.getPrices().size();i++){
            String time = productModel.getPrices().get(i).getStartAt();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start1 = null;
            start1 = simpleDateFormat1.parse(time);
            Calendar calendarstart1 = Calendar.getInstance();
            calendarstart1.setTime(start1);
            startMonth = calendarstart1.get(Calendar.MONTH)+1;

            if(year==calendarstart1.get(Calendar.YEAR)){
                if(t!=startMonth){

                    mMonths.add(new TextInfo(startMonth,String.valueOf(startMonth)+"月",true));
                }
                t = startMonth;
            }


        }
             months = mMonths.get(0).mIndex;
            ((WheelTextAdapter) mMonthWheel.getAdapter()).setData(mMonths);
             mMonthWheel.setSelection(0);
             mDateWheel.setSelection(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void setMonth(int month) {
        mDates.clear();
        try {
        for(int i=0;i<productModel.getPrices().size();i++){
            String time = productModel.getPrices().get(i).getStartAt();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start1 = null;
            start1 = simpleDateFormat1.parse(time);
            Calendar calendarstart1 = Calendar.getInstance();
            calendarstart1.setTime(start1);
            startday = calendarstart1.get(Calendar.DAY_OF_MONTH);
            if(years == calendarstart1.get(Calendar.YEAR)
                    &&months == (calendarstart1.get(Calendar.MONTH)+1)){
                    mDates.add(new TextInfo(startday,
                            String.valueOf(startday)+"日"+"("
                                    +isWeek(calendarstart1.get(Calendar.DAY_OF_WEEK),
                                    calendarstart1.get(Calendar.YEAR),
                                    calendarstart1.get(Calendar.MONTH),calendarstart1.get(Calendar.DATE))+")",
                            true));

            }

        }
            days = mDates.get(0).mIndex;
            ((WheelTextAdapter) mDateWheel.getAdapter()).setData(mDates);
            mDateWheel.setSelection(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initView(View v) {
        mDateWheel = (WheelView) v.findViewById(R.id.wheel_date);
        mMonthWheel = (WheelView) v.findViewById(R.id.wheel_month);
        mYearWheel = (WheelView) v.findViewById(R.id.wheel_year);

        mDateWheel.setOnEndFlingListener(mListener);
        mMonthWheel.setOnEndFlingListener(mListener);
        mYearWheel.setOnEndFlingListener(mListener);

        mDateWheel.setSoundEffectsEnabled(true);
        mMonthWheel.setSoundEffectsEnabled(true);
        mYearWheel.setSoundEffectsEnabled(true);

        mDateWheel.setAdapter(new WheelTextAdapter(context));
        mMonthWheel.setAdapter(new WheelTextAdapter(context));
        mYearWheel.setAdapter(new WheelTextAdapter(context));

        prepareData();
        person();

    }

    private static final int[] DAYS_PER_MONTH = {
            31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    private static final String[] MONTH_NAME = {
            "01月", "02月", "03月", "04月", "05月", "06月", "07月",
            "08月", "09月", "10月", "11月", "12月",
    };

    private boolean isLeapYear(int year) {
        return ((0 == year % 4) && (0 != year % 100) || (0 == year % 400));
    }

    int startYear;
    int endYear;
    int startMonth;
    int endMonth;
    int startday;
    int endday;

    private void prepareData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        // int startYear = 2015;
        // int endYear = 2016;

        // 价格

        try {

          /*  String[] period = packagePriceModel.getPeriod().split("~");
           // String[] period = "2015-12-31~2016-02-04".split("~");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date start = simpleDateFormat.parse(period[0]);
            Date end = simpleDateFormat.parse(period[1]);

            Calendar calendarstart = Calendar.getInstance();
            calendarstart.setTime(start);
            Calendar calendarend = Calendar.getInstance();
            calendarend.setTime(end);
            startYear = calendarstart.get(Calendar.YEAR);
            endYear = calendarend.get(Calendar.YEAR);
            startMonth = calendarstart.get(Calendar.MONTH) + 1;
            endMonth = calendarend.get(Calendar.MONTH) + 1;
            startday = calendarstart.get(Calendar.DATE);
            endday = calendarend.get(Calendar.DATE);
            if (startYear == endYear)
                for (int i = startMonth; i <= endMonth; ++i) {
                    mMonths.add(new TextInfo(i, String.valueOf(i) + "月", (i == month)));
                }
            else {
                for (int i = startMonth; i <= 12; ++i) {
                    mMonths.add(new TextInfo(i, String.valueOf(i) + "月", (i == month)));
                }
            }
            // for (int i = 0; i < MONTH_NAME.length; ++i) {
            // mMonths.add(new TextInfo(i, MONTH_NAME[i], (i == month)));
            // }

            for (int i = startYear; i <= endYear; ++i) {
                mYears.add(new TextInfo(i, String.valueOf(i) + "年", (i == year)));
            }*/
            int t = 0;
            for(int i=0;i<productModel.getPrices().size();i++){
                String time = productModel.getPrices().get(i).getStartAt();
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date start1 = simpleDateFormat1.parse(time);
                Calendar calendarstart1 = Calendar.getInstance();
                calendarstart1.setTime(start1);
                startYear = calendarstart1.get(Calendar.YEAR);
                if(t!=startYear){
                    mYears.add(new TextInfo(startYear,String.valueOf(startYear)+"年",true));
                }

                t = startYear;
            }
            years = mYears.get(0).mIndex;
            setYear(mYears.get(0).mIndex);
            setMonth(mMonths.get(0).mIndex);
            /*for(int i=0;i<productModel.getPrices().size();i++){
                String time = productModel.getPrices().get(i).getStartAt();
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date start1 = simpleDateFormat1.parse(time);

                Calendar calendarstart1 = Calendar.getInstance();
                calendarstart1.setTime(start1);
                startday = calendarstart1.get(Calendar.DAY_OF_MONTH);
                mDates.add(new TextInfo(startday,String.valueOf(startday)+"日",true));
            }*/
            mCurDate = startday;
            mCurMonth = startMonth;
            mCurYear = startYear;
            //getDate();
            ((WheelTextAdapter) mYearWheel.getAdapter()).setData(mYears);
           // prepareDayData(year, mCurMonth, day);
            getDayPrice();

            mYearWheel.setSelection(0);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void prepareMonth(int m) {
        mMonths.clear();
        if (startYear == mCurYear) {
            for (int i = startMonth; i <= 12; ++i) {
                mMonths.add(new TextInfo(i, String.valueOf(i) + "月", (i == m)));
            }
            mCurMonth = startMonth;
            mCurDate=startday;
        }
        else if (endYear == mCurYear) {
            for (int i = 1; i <= endMonth; ++i) {
                mMonths.add(new TextInfo(i, String.valueOf(i) + "月", (i == m)));
            }
            mCurMonth = 1;
            mCurDate=1;
        } else {
            for (int i = 1; i <= 12; ++i) {
                mMonths.add(new TextInfo(i, String.valueOf(i) + "月", (i == m)));
            }
            mCurMonth = 1;
            mCurDate=1;
        }
        ((WheelTextAdapter) mMonthWheel.getAdapter()).setData(mMonths);
    }

    /*private void prepareDayData(int year, int month, int curDate) {
        mDates.clear();
        int days = DAYS_PER_MONTH[month - 1];

        // The February.
        if (1 == month-1) {
            days = isLeapYear(year) ? 29 : 28;
        }
        if (startYear == endYear) {
            if (startMonth != endMonth) {
                if (month == startMonth) {
                    for (int i = startday; i <= days; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week
                                ,
                                (i == curDate)));
                    }
                   // mCurDate=startday;
                } else if (month == endMonth) {
                    for (int i = 1; i <= endday; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week
                                ,
                                (i == curDate)));
                    }
                   // mCurDate=1;
                } else {
                    for (int i = 1; i <= days; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week,
                                (i == curDate)));
                    }
                  //  mCurDate=1;
                }
            } else {
                for (int i = startday; i <= endday; ++i) {
                    String week = isWeek(year, month-1, i, curDate);
                    mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week,
                            (i == curDate)));
                }
            }
        } else if (startYear < endYear) {
            if (mCurYear == startYear) {
                if (month == startMonth) {
                    for (int i = startday; i <= days; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week
                                ,
                                (i == curDate)));
                    }
                   // mCurDate=startday;
                } else if (month != startMonth) {
                    for (int i = 1; i <= days; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week,
                                (i == curDate)));
                    }
                  //  mCurDate=1;
                }
            } else if (mCurYear == endYear) {
                if (month == endMonth) {
                    for (int i = 1; i <= endday; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week
                                ,
                                (i == curDate)));
                    }
                   // mCurDate=1;
                } else {
                    for (int i = 1; i <= days; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week,
                                (i == curDate)));
                    }
                  //  mCurDate=1;
                }
            }
        }

        *//*
         * for (int i = 1; i <= days; ++i) { mDates.add(new TextInfo(i,
         * String.valueOf(i) + "日" + isWeek(year, month, i, curDate), (i ==
         * curDate))); }
         *//*

        ((WheelTextAdapter) mDateWheel.getAdapter()).setData(mDates);
        mDateWheel.setSelection(0);
        //mCurDate=mDates.get(0).mIndex;
    }*/

    private String isWeek(int isweek,int y,int m,int d ) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        String s = "今天";
        if(y!=year||m!=month||d!=day){

       switch (isweek){
           case 1:
               s = "星期日";
               break;
           case 2:
               s = "星期一";
               break;
           case 3:
               s = "星期二";
               break;
           case 4:
               s = "星期三";
               break;
           case 5:
               s = "星期四";
               break;
           case 6:
               s = "星期五";
               break;
           case 7:
               s = "星期六";
               break;
       }
        }
        return s;
    }

    protected class TextInfo {
        public TextInfo(int index, String text, boolean isSelected) {
            mIndex = index;
            mText = text;
            mIsSelected = isSelected;

            if (isSelected) {
                mColor = Color.BLUE;
            }
        }

        public int mIndex;
        public String mText;
        public boolean mIsSelected = false;
        public int mColor = Color.BLACK;
    }

    protected class WheelTextAdapter extends BaseAdapter {
        ArrayList<TextInfo> mData = null;
        int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int mHeight = 50;
        Context mContext = null;

        public WheelTextAdapter(Context context) {
            mContext = context;
            mHeight = (int) SizeUtil.pixelToDp(context, mHeight);
        }

        public void setData(ArrayList<TextInfo> data) {
            mData = data;
            this.notifyDataSetChanged();
        }

        public void setItemSize(int width, int height) {
            mWidth = width;
            mHeight = (int) SizeUtil.pixelToDp(mContext, height);
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;

            if (null == convertView) {
                convertView = new TextView(mContext);
                convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
                textView = (TextView) convertView;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                textView.setTextColor(Color.BLACK);
            }

            if (null == textView) {
                textView = (TextView) convertView;
            }

            TextInfo info = mData.get(position);
            textView.setText(info.mText);
            // textView.setTextColor(info.mColor);

            return convertView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        productModel = (ProductModel) getArguments().getSerializable("product");
        this.priceModels = productModel.getPrices();
        judge = false;
       // this.packagePriceModel = productModel.getPackagePriceModel();
        if(productModel.getPrices().size()>0){
            productModel.getPrices().get(0).getPersonsDuring(ProductType.prdtType.COMBO);
            currentPrice = productModel.getPrices().get(0);
            amountmax = productModel.getPrices().get(0).getMaxPerson();
            amount = productModel.getPrices().get(0).getMinPerson();
            priceStr = productModel.getPrices().get(0).getFianlPrice(ProductType.prdtType.COMBO,1,null);
            judge = true;
        }

        LogUtil.i("amountmax",amountmax+","+amount+","+priceStr);

        System.out.println("mNum Fragment create =" + mNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        System.out.println("onCreateView = ");
        // 在这里加载每个 fragment的显示的 View
        View v = null;
        if (mNum == 0) {
            v = inflater.inflate(R.layout.fragment_package_note, container, false);
            // ((TextView) v.findViewById(R.id.textView1)).setText(mNum +
            // "= mNum");
            if (productModel != null)
                ((TextView) v.findViewById(R.id.amountNotice)).setText(productModel
                        .getAmountNotice());
            minas = ViewUtil.findViewById(v, R.id.dialog_order_minas);
            plus = ViewUtil.findViewById(v, R.id.dialog_order_plus);
            fourPlus = ViewUtil.findViewById(v, R.id.dialog_order_sure_number_edit);
            if(judge){
                initView(v);
            }else{
                fourPlus.setText("1");
                changePrice.priceChanged(0, null, 0, null);
            }

        } else if (mNum == 1) {
            v = inflater.inflate(R.layout.fragment_package_note2, container, false);
            // ((TextView) v.findViewById(R.id.textView1)).setText(mNum +
            // "= mNum");
            if (productModel != null) {

                ((TextView) v.findViewById(R.id.purchasingNotice)).setText(productModel
                        .getPurchasingNotice());
                ((TextView) v.findViewById(R.id.costInclude))
                        .setText(productModel.getCostInclude());
                ((TextView) v.findViewById(R.id.recommendReason)).setText(productModel
                        .getRecommendReason());
            }
        } else if (mNum == 2) {
            v = inflater.inflate(R.layout.fragment_package_note3, container, false);
            if (productModel != null)

                ((WebView) v.findViewById(R.id.richTextView))
                        .loadDataWithBaseURL(
                                null,
                                productModel.getScheduling()
                                        .replaceAll("font-size:.*pt;", "font-size:0pt;")
                                        .replaceAll("font-family:.*;", "font-family:;"),
                                "text/html", "UTF-8", null);

        } else {
            v = inflater.inflate(R.layout.fragment_package_note, container, false);
            // ((TextView) v.findViewById(R.id.textView1)).setText(mNum +
            // "= mNum");
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        System.out.println("onActivityCreated = ");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        System.out.println(mNum + "mNumDestory");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

   /* public void getPriceYear() {
        // period\":\"2015-08-03~2015-08-11\",\"items\":[{\"start\":\"1\",\"end\":\"5\",\"price\":500,\"fan\":20}]
       String[] period = packagePriceModel.getPeriod().split("~");
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = simpleDateFormat.parse(period[0], position);
        Date end = simpleDateFormat.parse(period[1], position);
        Calendar calendarstart = Calendar.getInstance();
        calendarstart.setTime(start);
        Calendar calendarend = Calendar.getInstance();
        calendarstart.setTime(end);
        int startYear = calendarstart.get(Calendar.YEAR);
        int endYear = calendarend.get(Calendar.YEAR);
        int startMonth = calendarstart.get(Calendar.MONTH);
        int endMonth = calendarstart.get(Calendar.MONTH);
    }*/

    public void getPriceMonth() {

    }

}
