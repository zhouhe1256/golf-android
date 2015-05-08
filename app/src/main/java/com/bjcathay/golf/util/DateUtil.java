package com.bjcathay.golf.util;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by bjcathay on 15-4-24.
 */
public class DateUtil {
    public static List<String> getDate(Context context) {
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
            if (month < 9) {
                days.add("0" + (month + 1) + "月" + day + "日" + "(" + today + ")");
            } else
                days.add(month + 1 + "月" + day + "日" + "(" + today + ")");
            c.add(Calendar.DATE, 1);
        }
        return days;
    }

    public static List<String> getAM(String am) {
        DateFormat df = new SimpleDateFormat("hh:mm");
        try {
            Date dt = df.parse(am);
            List<String> ams = new ArrayList<String>();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            while (hour < 12) {
                Date a = rightNow.getTime();
                ams.add(df.format(a));
                rightNow.add(Calendar.MINUTE, 30);
                hour = rightNow.get(Calendar.HOUR_OF_DAY);
            }
            //ams.remove(ams.size() - 1);
            return ams;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static List<String> getPM(String pm) {

        try {
            String pmStart = "12:00";
            DateFormat df = new SimpleDateFormat("hh:mm");
            Date dt = df.parse(pm);
            Date pmDate = df.parse(pmStart);
            List<String> ams = new ArrayList<String>();
            Calendar end = Calendar.getInstance();
            end.setTime(dt);
            Calendar start = Calendar.getInstance();
            start.setTime(pmDate);
            int endHour = end.get(Calendar.HOUR);
            //endHour = endHour - 12;
            int endmin = end.get(Calendar.MINUTE);
            int startHour = start.get(Calendar.HOUR);
            int startmin = start.get(Calendar.MINUTE);
            while (startHour < endHour) {

                if (startHour < endHour) {
                    Date a = start.getTime();
                    ams.add(df.format(a));
                    start.add(Calendar.MINUTE, 30);
                    startHour = start.get(Calendar.HOUR);
                }
            }
            if (startHour == endHour && startmin < endmin == true) {
                start.add(Calendar.MINUTE, 30);
                Date a = start.getTime();
                ams.add(df.format(a));
                //startHour = start.get(Calendar.HOUR_OF_DAY);
            }
            ams.add(df.format(dt));
            return ams;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String To24(String hour) {
        DateFormat df = new SimpleDateFormat("hh:mm");
        try {
            Date date = df.parse(hour);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            String hours = (c.get(Calendar.HOUR_OF_DAY) + 12) + ":" + (c.get(Calendar.MINUTE) == 0 ? "00" : "30");

            return hours;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
