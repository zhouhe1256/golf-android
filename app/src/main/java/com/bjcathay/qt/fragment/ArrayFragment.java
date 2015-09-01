
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
    PackagePriceModel packagePriceModel;

    public interface ChangePrice {
        void priceChanged(int price, PackagePriceModel currentPrice, int number, String date);
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
    private PackagePriceModel currentPrice;
    private ImageView minas;
    private ImageView plus;
    private TextView fourPlus;
    private int amount = -1;
    private int amountmax = -1;
    private String daySelect;

    private void getDate() {
        // mCurYear, mCurMonth + 1, mCurDate
        String mounth = mCurMonth + 1 + "";
        String day = mCurDate + "";
        daySelect = mCurYear + "-" + (mounth.length() == 1 ? "0" + mounth : mounth) + "-"
                + (day.length() == 1 ? "0" + day : day) + " 00:00:00";
        int[] priceStr = packagePriceModel.getFianlPrice(productModel.getType(),
                number, hourSelect);
        changePrice.priceChanged(priceStr[0], currentPrice, number, daySelect);
    }

    private void getDayPrice() {
        if (packagePriceModel != null) {
            packagePriceModel.getPersonsDuring(ProductType.prdtType.COMBO);
            currentPrice = packagePriceModel;
            amount = packagePriceModel.getMinPerson();
            if (number < amount) {
                number = amount;
            }
            // fourPlus.setText(number < 10 ? " " + number : number + "");
            int[] priceStr = packagePriceModel.getFianlPrice(productModel.getType(),
                    number, hourSelect);
            changePrice.priceChanged(priceStr[0], currentPrice, number, daySelect);
            // return;
        }

        // if (priceModels != null && priceModels.size() > 0)
        // for (PriceModel priceModel : priceModels) {
        // if (DateUtil.CompareTime(daySelect, priceModel.getStartAt(),
        // priceModel.getEndAt()) == true) {
        // priceModel.getPersonsDuring(productModel.getType());
        // currentPrice = priceModel;
        // amount = priceModel.getMinPerson();
        // if (number < amount) {
        // number = amount;
        // }
        // fourPlus.setText(number < 10 ? " " + number : number + "");
        // int[] priceStr = priceModel.getFianlPrice(productModel.getType(),
        // number, hourSelect);
        // changePrice.priceChanged(priceStr[0], currentPrice, number,
        // daySelect);
        // return;
        // }
        // }
    }

    private void person() {
        amountmax = packagePriceModel.getMaxPerson();
        minas.setOnClickListener(new
                View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (amount > 0) {
                            if (number > amount) {
                                number--;
                                fourPlus.setText(number < 10 ? " " + number : number + "");
                                getDayPrice();
                            }
                        } else {
                            if (number > 1) {
                                number--;
                                fourPlus.setText(number < 10 ? " " + number : number + "");
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
                getDayPrice();
            }
        });
        fourPlus.setText(number < 10 ? " " + number : number + "");
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
                TextInfo info = mDates.get(pos);
                setDate(info.mIndex);
            } else if (v == mMonthWheel) {
                TextInfo info = mMonths.get(pos);
                setMonth(info.mIndex);
            } else if (v == mYearWheel) {
                TextInfo info = mYears.get(pos);
                setYear(info.mIndex);
            }
            getDate();
            // getDayPrice();
        }
    };

    private String formatDate() {
        return String.format("Date: %d/%02d/%02d", mCurYear, mCurMonth + 1, mCurDate);
    }

    private void setDate(int date) {
        if (date != mCurDate) {
            mCurDate = date;
        }
    }

    private void setYear(int year) {
        if (year != mCurYear) {
            mCurYear = year;
            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            if (startYear < endYear)
                prepareMonth(mCurMonth - 1);
            // prepareDayData(mCurYear, mCurMonth - 1, date);
            prepareDayData(mCurYear, mCurMonth, date);
        }
    }

    private void setMonth(int month) {
        if (month != mCurMonth) {
            mCurMonth = month;

            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            prepareDayData(mCurYear, month, date);
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

            String[] period = packagePriceModel.getPeriod().split("~");
           // String[] period = "2015-07-31~2015-09-04".split("~");

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
            }
            mCurDate = startday;
            mCurMonth = startMonth;
            mCurYear = startYear;
            getDate();
            ((WheelTextAdapter) mMonthWheel.getAdapter()).setData(mMonths);
            ((WheelTextAdapter) mYearWheel.getAdapter()).setData(mYears);

            prepareDayData(year, mCurMonth, day);
            getDayPrice();
            mMonthWheel.setSelection(0);
            mYearWheel.setSelection(year - startYear);
            mDateWheel.setSelection(0);
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
        }
        else if (endYear == mCurYear) {
            for (int i = 1; i <= endMonth; ++i) {
                mMonths.add(new TextInfo(i, String.valueOf(i) + "月", (i == m)));
            }
            mCurMonth = 1;
        } else {
            for (int i = 1; i <= 12; ++i) {
                mMonths.add(new TextInfo(i, String.valueOf(i) + "月", (i == m)));
            }
            mCurMonth = 1;
        }
        ((WheelTextAdapter) mMonthWheel.getAdapter()).setData(mMonths);
    }

    private void prepareDayData(int year, int month, int curDate) {
        mDates.clear();
        int days = DAYS_PER_MONTH[month - 1];

        // The February.
        if (1 == month) {
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
                } else if (month == endMonth) {
                    for (int i = 1; i <= endday; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week
                                ,
                                (i == curDate)));
                    }
                } else {
                    for (int i = 1; i <= days; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week,
                                (i == curDate)));
                    }
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
                } else if (month != startMonth) {
                    for (int i = 1; i <= days; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week,
                                (i == curDate)));
                    }
                }
            } else if (mCurYear == endYear) {
                if (month == endMonth) {
                    for (int i = 1; i <= endday; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week
                                ,
                                (i == curDate)));
                    }
                } else {
                    for (int i = 1; i <= days; ++i) {
                        String week = isWeek(year, month-1, i, curDate);
                        mDates.add(new TextInfo(i, String.valueOf(i) + "日" + week,
                                (i == curDate)));
                    }
                }
            }
        }

        /*
         * for (int i = 1; i <= days; ++i) { mDates.add(new TextInfo(i,
         * String.valueOf(i) + "日" + isWeek(year, month, i, curDate), (i ==
         * curDate))); }
         */

        ((WheelTextAdapter) mDateWheel.getAdapter()).setData(mDates);
    }

    private String isWeek(int year, int month, int day, int curDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int day1 = calendar.get(Calendar.DAY_OF_WEEK);
        int _day = calendar.get(Calendar.DAY_OF_MONTH);
        Calendar calendartoday = Calendar.getInstance();
        calendartoday.set(year, month, curDate);
        int now_daye = calendartoday.get(Calendar.DAY_OF_MONTH);
        String today = "(今天)";
        if (day == now_daye) {
            today = "(今天)";
        } else {
            switch (day1) {
                case 2:
                    today = "(星期一)";
                    break;
                case 3:
                    today = "(星期二)";
                    break;
                case 4:
                    today = "(星期三)";
                    break;
                case 5:
                    today = "(星期四)";
                    break;
                case 6:
                    today = "(星期五)";
                    break;
                case 7:
                    today = "(星期六)";
                    break;
                case 1:
                    today = "(星期日)";
                    break;
            }
        }
        return today;
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
        this.packagePriceModel = productModel.getPackagePriceModel();
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
            initView(v);
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

    public void getPriceYear() {
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
    }

    public void getPriceMonth() {

    }

}
