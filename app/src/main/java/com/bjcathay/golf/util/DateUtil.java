package com.bjcathay.golf.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by bjcathay on 15-4-24.
 */
public class DateUtil {
    public static List<String> getDate(Context context){
        String today = "周一";
        Calendar c = Calendar.getInstance();
        List<String> days = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            //取得系统日期:
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int day1 = c.get(Calendar.DAY_OF_WEEK);
            switch (day1) {
                case 2:
                    today = "周一";
                    break;
                case 3:
                    today = "周二";
                    break;
                case 4:
                    today = "周三";
                    break;
                case 5:
                    today = "周四";
                    break;
                case 6:
                    today = "周五";
                    break;
                case 7:
                    today = "周六";
                    break;
                case 1:
                    today = "周日";
                    break;
            }

            days.add(month + 1 + "月" + day + "日" + "(" + today + ")");
            c.add(Calendar.DATE, 1);
        }
        return days;
    }
}
