package com.bjcathay.qt.view;

import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by bjcathay on 15-5-19.
 */
public class PickerViewTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private View view;
    private PickerView pv_day;
    private PickerView pv_am_pm;
    private PickerView pv_hours;


}
