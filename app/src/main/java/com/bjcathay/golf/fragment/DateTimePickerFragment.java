package com.bjcathay.golf.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;


import com.bjcathay.golf.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bjcathay on 15-5-7.
 */
public class DateTimePickerFragment extends DialogFragment implements View.OnClickListener {

    /**
     * 起始时间。
     * <p>
     * 数据类型： long 。
     */
    public static final String EXTRA_START_DATE_TIME = "start date time";
    /**
     * 结束时间。
     * <p>
     * 数据类型： long 。
     */
    public static final String EXTRA_END_DATE_TIME = "end date time";

    protected DatePicker startDatePicker;
    protected TimePicker startTimePicker;
    protected DatePicker endDatePicker;
    protected TimePicker endTimePicker;

    protected Calendar startCalendar;
    protected Calendar endCalendar;

    protected DateTimeListener onDateTimeChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startCalendar = Calendar.getInstance(Locale.getDefault());
        endCalendar = Calendar.getInstance(Locale.getDefault());
        // 默认“一天内”
        startCalendar.setTimeInMillis(startCalendar.getTimeInMillis() - 24 * 60 * 60 * 1000);
        correctStartDate();
        if (getArguments() != null) {
            if (getArguments().containsKey(EXTRA_START_DATE_TIME)) {
                startCalendar.setTimeInMillis(getArguments().getLong(EXTRA_START_DATE_TIME));
            }
            if (getArguments().containsKey(EXTRA_END_DATE_TIME)) {
                endCalendar.setTimeInMillis(getArguments().getLong(EXTRA_END_DATE_TIME));
            }
            correctStartDate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getDialog() != null) {
            refreshTitle();
            getDialog().setCanceledOnTouchOutside(true);
        }

        View view = inflater.inflate(R.layout.fragment_date_time_picker, container, false);
        startDatePicker = (DatePicker) view.findViewById(R.id.startDatePicker);
        startTimePicker = (TimePicker) view.findViewById(R.id.startTimePicker);
        endDatePicker = (DatePicker) view.findViewById(R.id.endDatePicker);
        endTimePicker = (TimePicker) view.findViewById(R.id.endTimePicker);

        startDatePicker.init(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                        startCalendar.set(year, monthOfYear, dayOfMonth);
                        if (year != endCalendar.get(Calendar.YEAR)
                                || monthOfYear != endCalendar.get(Calendar.MONTH)) {
                            endCalendar.set(year, monthOfYear,
                                    endCalendar.get(Calendar.DAY_OF_MONTH));
                            showTime();
                        }
                        // TODO 日检测
                        refreshTitle();
                    }
                });
        startTimePicker.setIs24HourView(true);
        startTimePicker.setCurrentHour(startCalendar.get(Calendar.HOUR_OF_DAY));
        startTimePicker.setCurrentMinute(startCalendar.get(Calendar.MINUTE));
        startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                startCalendar.set(Calendar.MINUTE, minute);
                showTime();
            }
        });
        endDatePicker.init(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                        endCalendar.set(year, monthOfYear, dayOfMonth);
                        if (year != startCalendar.get(Calendar.YEAR)
                        /* || monthOfYear != startCalendar.get(Calendar.MONTH) */) {
                            startCalendar.set(year, monthOfYear,
                                    startCalendar.get(Calendar.DAY_OF_MONTH));
                            showTime();
                        }
                        // TODO 日检测
                        refreshTitle();
                    }
                });
        endTimePicker.setIs24HourView(true);
        endTimePicker.setCurrentHour(endCalendar.get(Calendar.HOUR_OF_DAY));
        endTimePicker.setCurrentMinute(endCalendar.get(Calendar.MINUTE));
        endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                endCalendar.set(Calendar.MINUTE, minute);
                showTime();
            }
        });

        view.findViewById(R.id.shortcutOneDay).setOnClickListener(this);
        view.findViewById(R.id.shortcutThreeDays).setOnClickListener(this);
        view.findViewById(R.id.shortcutOneWeek).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        view.findViewById(R.id.ok).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shortcutOneDay:
                endCalendar.setTime(new Date());
                startCalendar.setTimeInMillis(endCalendar.getTimeInMillis() - 1 * 24 * 60 * 60
                        * 1000);
                correctStartDate();
                showTime();
                break;

            case R.id.shortcutThreeDays:
                endCalendar.setTime(new Date());
                startCalendar.setTimeInMillis(endCalendar.getTimeInMillis() - 3 * 24 * 60 * 60
                        * 1000);
                correctStartDate();
                showTime();
                break;

            case R.id.shortcutOneWeek:
                endCalendar.setTime(new Date());
                startCalendar.setTimeInMillis(endCalendar.getTimeInMillis() - 7 * 24 * 60 * 60
                        * 1000);
                correctStartDate();
                showTime();
                break;

            case R.id.cancel:
                getDialog().cancel();
                break;

            case R.id.ok:
                if (endCalendar.getTimeInMillis() > startCalendar.getTimeInMillis()) {
                    if (onDateTimeChangeListener != null) {
                        onDateTimeChangeListener.onDateTimePicked(startCalendar.getTime(),
                                endCalendar.getTime());
                    }
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), R.string.msg_end_before_start, Toast.LENGTH_LONG)
                            .show();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 设置日期时间事件监听器。
     *
     * @param listener 日期时间事件监听器
     */
    public void setDateTimeListener(DateTimeListener listener) {
        this.onDateTimeChangeListener = listener;
    }

    /**
     * 修正开始时间（不可跨月,此功能暂时作废）
     */
    private void correctStartDate() {
        // TUDO
        /*
         * if (startCalendar.get(Calendar.YEAR) !=
         * endCalendar.get(Calendar.YEAR) && startCalendar.get(Calendar.MONTH) -
         * endCalendar.get(Calendar.MONTH) > 1 &&
         * startCalendar.get(Calendar.DAY_OF_YEAR) !=
         * endCalendar.get(Calendar.DAY_OF_YEAR)) { // 跨月则月初
         * startCalendar.set(endCalendar.get(Calendar.YEAR),
         * endCalendar.get(Calendar.MONTH), 1, 0, 0); }
         */
    }

    /**
     * 显示时间
     */
    private void showTime() {
        startDatePicker.updateDate(startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
        startTimePicker.setCurrentHour(startCalendar.get(Calendar.HOUR_OF_DAY));
        startTimePicker.setCurrentMinute(startCalendar.get(Calendar.MINUTE));
        endDatePicker.updateDate(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH));
        endTimePicker.setCurrentHour(endCalendar.get(Calendar.HOUR_OF_DAY));
        endTimePicker.setCurrentMinute(endCalendar.get(Calendar.MINUTE));
    }

    /**
     * 将时间间隔转换成标题
     */
    private String toTitle() {
        long minutes = (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis())
                / (1000 * 60);
        long hours = minutes / 60;
        long days = hours / 24;
        return getResources().getString(R.string.title_pick_date_time, days, hours % 24,
                minutes % 60);
    }

    /**
     * 刷新显示时间间隔标题
     */
    private void refreshTitle() {
        if (getDialog() != null) {
            getDialog().setTitle(toTitle());
        }
    }

    /**
     * 日期时间事件监听器。
     *
     * @author rongws
     */
    public static interface DateTimeListener {
        /**
         * 事件监听：日期时间已选择。
         *
         * @param startTime 开始时间。
         * @param endTime 结束时间
         */
        void onDateTimePicked(Date startTime, Date endTime);
    }

}
