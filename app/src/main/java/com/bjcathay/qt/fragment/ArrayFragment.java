
package com.bjcathay.qt.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.SizeUtil;
import com.bjcathay.qt.view.RichTextView;
import com.bjcathay.qt.widget.TosGallery;
import com.bjcathay.qt.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by dengt on 15-7-28.
 */
public class ArrayFragment extends Fragment {
    int mNum;
    private Context context;

    public static ArrayFragment newInstance(int num) {
        ArrayFragment array = new ArrayFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        array.setArguments(args);
        return array;
    }

    public void setContext(Context context) {
        this.context = context;
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

            // mSelDateTxt.setText(formatDate());
            DialogUtil.showMessage(formatDate());
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

    private void prepareData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int startYear = 2015;
        int endYear = 2016;

        mCurDate = day;
        mCurMonth = month;
        mCurYear = year;

        for (int i = 0; i < MONTH_NAME.length; ++i) {
            mMonths.add(new TextInfo(i, MONTH_NAME[i], (i == month)));
        }

        for (int i = startYear; i <= endYear; ++i) {
            mYears.add(new TextInfo(i, String.valueOf(i)+"年", (i == year)));
        }

        ((WheelTextAdapter) mMonthWheel.getAdapter()).setData(mMonths);
        ((WheelTextAdapter) mYearWheel.getAdapter()).setData(mYears);

        prepareDayData(year, month, day);

        mMonthWheel.setSelection(month);
        mYearWheel.setSelection(year - startYear);
        mDateWheel.setSelection(day - 1);
    }

    private void prepareDayData(int year, int month, int curDate) {
        mDates.clear();

        int days = DAYS_PER_MONTH[month];

        // The February.
        if (1 == month) {
            days = isLeapYear(year) ? 29 : 28;
        }

        for (int i = 1; i <= days; ++i) {
            mDates.add(new TextInfo(i, String.valueOf(i)+"日"+isWeek(year,month,i,curDate), (i == curDate)));
        }

        ((WheelTextAdapter) mDateWheel.getAdapter()).setData(mDates);
    }
private String isWeek(int year,int month,int day,int curDate){
    Calendar calendar = Calendar.getInstance();
    calendar.set(year,month,day);
    Calendar calendartoday = Calendar.getInstance();
    calendartoday.set(year,month,curDate);
    int day1 = calendar.get(Calendar.DAY_OF_WEEK);
    int _day = calendar.get(Calendar.DAY_OF_MONTH);
    int now_daye = calendartoday.get(Calendar.DAY_OF_MONTH);
    String today = "(今天)";
    if (day == now_daye) {
        today = "(今天)";
    }else{
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
            initView(v);
        } else if (mNum == 1) {
            v = inflater.inflate(R.layout.fragment_package_note2, container, false);
            // ((TextView) v.findViewById(R.id.textView1)).setText(mNum +
            // "= mNum");
        } else if (mNum == 2) {
            v = inflater.inflate(R.layout.fragment_package_note3, container, false);
            ((RichTextView) v.findViewById(R.id.richTextView))
                    .setRichText("富文本<br><br>可以解析含有图片的html网页，图片既可以是本地的也可以是网络的"
                            + "<font color='red'></font><br><br><br>"
                            + "<font color='#0000ff'><big><i></i></big></font><p>"
                            + "<big><a href='http://www.baidu.com'>百度</a></big><br>");
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

}
