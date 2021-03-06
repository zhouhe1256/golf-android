
package com.bjcathay.qt.util;

import android.content.Context;

import com.bjcathay.qt.model.PriceModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.model.TextInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dengt on 15-4-24.
 */
public class DateUtil {
    // 根据起始时间决定日期
    public static List<String> getLimitDate(List<PriceModel> priceModels) {
        if (priceModels != null) {
            List<String> dayList = new ArrayList<String>();
            String start = priceModels.get(0).getStartAt();
            String end = priceModels.get(priceModels.size() - 1).getStartAt();
            Date s = stringToDate(start);
            Date e = stringToDate(end);
            Calendar rights = Calendar.getInstance();
            rights.setTime(s);
            int month = rights.get(Calendar.MONTH);
            int day = rights.get(Calendar.DAY_OF_MONTH);
            int day1 = rights.get(Calendar.DAY_OF_WEEK);
            Calendar righte = Calendar.getInstance();
            righte.setTime(e);
            int daye = righte.get(Calendar.DAY_OF_MONTH);
            int monthe = righte.get(Calendar.MONTH);
            Calendar now = Calendar.getInstance();
            int now_daye = now.get(Calendar.DAY_OF_MONTH);
            String today = "今天";
            for (int i = day; month < monthe || (month == monthe && day < daye); i++) {
                month = rights.get(Calendar.MONTH);
                day = rights.get(Calendar.DAY_OF_MONTH);
                day1 = rights.get(Calendar.DAY_OF_WEEK);
                if (day == now_daye) {
                    today = "今天";
                }/*
                  * else if (day == now_daye + 1) { today = "明天"; }
                  *//*
                     * else if (day == now_daye + 2) { today = "后天"; }
                     */else {
                    switch (day1) {
                        case 2:
                            today = "星期一";
                            break;
                        case 3:
                            today = "星期二";
                            break;
                        case 4:
                            today = "星期三";
                            break;
                        case 5:
                            today = "星期四";
                            break;
                        case 6:
                            today = "星期五";
                            break;
                        case 7:
                            today = "星期六";
                            break;
                        case 1:
                            today = "星期日";
                            break;
                    }
                }
                if (month < 9) {
                    dayList.add("0" + (month + 1) + "月" + day + "日" + "(" + today + ")");
                } else
                    dayList.add(month + 1 + "月" + day + "日" + "(" + today + ")");
                rights.add(Calendar.DATE, 1);
            }
            return dayList;
        } else
            return null;
    }

    public static List<String> getLimitDates(List<PriceModel> priceModels) {
        if (priceModels != null) {
            List<String> dayList = new ArrayList<String>();
            // String start = priceModels.get(0).getStartAt();
            // String end = priceModels.get(priceModels.size() -
            // 1).getStartAt();
            Calendar now = Calendar.getInstance();
            int now_daye = now.get(Calendar.DAY_OF_MONTH);
            String today = "今天";
            for (int i = 0; i < priceModels.size(); i++) {
                String start = priceModels.get(i).getStartAt();
                Date s = stringToDate(start);
                Calendar rights = Calendar.getInstance();
                rights.setTime(s);
                int month = rights.get(Calendar.MONTH);
                int day = rights.get(Calendar.DAY_OF_MONTH);
                int day1 = rights.get(Calendar.DAY_OF_WEEK);
                if (day == now_daye) {
                    today = "今天";
                } else {
                    switch (day1) {
                        case 2:
                            today = "星期一";
                            break;
                        case 3:
                            today = "星期二";
                            break;
                        case 4:
                            today = "星期三";
                            break;
                        case 5:
                            today = "星期四";
                            break;
                        case 6:
                            today = "星期五";
                            break;
                        case 7:
                            today = "星期六";
                            break;
                        case 1:
                            today = "星期日";
                            break;
                    }
                }
                if (month < 9) {
                    dayList.add("0" + (month + 1) + "月" + day + "日" + "(" + today + ")");
                } else
                    dayList.add(month + 1 + "月" + day + "日" + "(" + today + ")");
            }
            return dayList;
        }
        return null;
    }

    public static List<TextInfo> getComboDates(List<PriceModel> priceModels) {
        if (priceModels != null) {
            List<TextInfo> dayList = new ArrayList<TextInfo>();
            Calendar now = Calendar.getInstance();
            int now_daye = now.get(Calendar.DAY_OF_MONTH);
            String today = "今天";
            TextInfo yearText1 = new TextInfo();
            TextInfo yearText2 = new TextInfo();
            TextInfo monthText = new TextInfo();
            int lastYear = 0;
            int lastMonth = 0;
            int lastDay = 0;
            for (int i = 0; i < priceModels.size(); i++) {
                TextInfo dayText = new TextInfo();
                String start = priceModels.get(i).getStartAt();
                Date s = stringToDate(start);
                Calendar rights = Calendar.getInstance();
                rights.setTime(s);
                int year = rights.get(Calendar.YEAR);
                int month = rights.get(Calendar.MONTH);
                int day = rights.get(Calendar.DAY_OF_MONTH);
                int day1 = rights.get(Calendar.DAY_OF_WEEK);
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
                if (i == 0) {
                    lastYear = year;
                    lastMonth = month;
                    lastDay = day;
                }
                dayText.mIndex = day;
                dayText.mText = day + "日" + today;
                if (lastMonth == month) {
                } else {
                    lastMonth = month;
                    monthText = new TextInfo();
                }
                monthText.mIndex = month + 1;
                monthText.mText = month+1+ "月";
                monthText.addTextInfo(dayText);
                if (lastYear == year) {
                    yearText1.mIndex = year;
                    yearText1.mText = year + "年";
                    yearText1.addTextInfo(monthText);
                } else {
                    yearText2.mIndex = year;
                    yearText2.mText = year + "年";
                    yearText2.addTextInfo(monthText);
                }
            }
            dayList.add(yearText1);
            if (yearText2.mIndex != 0)
                dayList.add(yearText2);
            return dayList;
        }
        return null;
    }

    // 根据起始时间决定小时上午
    public static List<String> getTuanHourAM(ProductModel productModel) {
        List<String> dayList = new ArrayList<String>();
        String start = productModel.getStart();
        String end = productModel.getEnd();
        Date s = stringToDate(start);
        Date e = stringToDate(end);
        Calendar rights = Calendar.getInstance();
        rights.setTime(s);
        int year = rights.get(Calendar.YEAR);
        int month = rights.get(Calendar.MONTH);
        int day = rights.get(Calendar.DAY_OF_MONTH);
        int hours = rights.get(Calendar.HOUR_OF_DAY);
        int minute = rights.get(Calendar.MINUTE);
        Calendar righte = Calendar.getInstance();
        righte.setTime(e);
        int yeare = righte.get(Calendar.YEAR);
        int monthe = righte.get(Calendar.MONTH);
        int daye = righte.get(Calendar.DAY_OF_MONTH);
        int hourse = righte.get(Calendar.HOUR_OF_DAY);
        int minutee = righte.get(Calendar.MINUTE);
        Calendar now = Calendar.getInstance();
        int now_yeare = righte.get(Calendar.YEAR);
        int now_monthe = righte.get(Calendar.MONTH);
        int now_daye = righte.get(Calendar.DAY_OF_MONTH);
        int now_hourse = righte.get(Calendar.HOUR_OF_DAY);
        int now_minutee = righte.get(Calendar.MINUTE);
        String today = "今天";
        DateFormat df = new SimpleDateFormat("hh:mm");
        List<String> ams = new ArrayList<String>();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(s);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        while (hour < 12) {
            Date a = rightNow.getTime();
            ams.add(df.format(a));
            rightNow.add(Calendar.MINUTE, 30);
            hour = rightNow.get(Calendar.HOUR_OF_DAY);
        }
        // ams.remove(ams.size() - 1);
        return ams;
    }

    public static List<String> getDate(Context context) {
        String today = "星期一";
        Calendar c = Calendar.getInstance();
        List<String> days = new ArrayList<String>();
        Calendar now = Calendar.getInstance();
        int now_daye = now.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < 7; i++) {
            // 取得系统日期:
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            int day1 = c.get(Calendar.DAY_OF_WEEK);
            if (day == now_daye) {
                today = "今天";
            } else
                switch (day1) {
                    case 2:
                        today = "星期一";
                        break;
                    case 3:
                        today = "星期二";
                        break;
                    case 4:
                        today = "星期三";
                        break;
                    case 5:
                        today = "星期四";
                        break;
                    case 6:
                        today = "星期五";
                        break;
                    case 7:
                        today = "星期六";
                        break;
                    case 1:
                        today = "星期日";
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
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dff = new SimpleDateFormat("HH:mm");
        try {
            Date dt = dff.parse(am);
            List<String> ams = new ArrayList<String>();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            while (hour < 12) {
                Date a = rightNow.getTime();
                ams.add(dff.format(a));
                rightNow.add(Calendar.MINUTE, 30);
                hour = rightNow.get(Calendar.HOUR_OF_DAY);
            }
            // ams.remove(ams.size() - 1);
            return ams;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getAM(String am, String end) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dff = new SimpleDateFormat("HH:mm");
        try {
            Date dt = dff.parse(am);
            List<String> ams = new ArrayList<String>();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            while (hour < 12) {
                Date a = rightNow.getTime();
                ams.add(dff.format(a));
                rightNow.add(Calendar.MINUTE, 30);
                hour = rightNow.get(Calendar.HOUR_OF_DAY);
            }
            // ams.remove(ams.size() - 1);
            return ams;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getPMShort(String pm, String ends) {

        try {
            String pmStart = "12:00";
            // DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat dff = new SimpleDateFormat("HH:mm");
            Date dt = dff.parse(pm);
            Date pmDate = dff.parse(pmStart);
            List<String> ams = new ArrayList<String>();
            Calendar end = Calendar.getInstance();
            end.setTime(dt);
            Calendar start = Calendar.getInstance();
            start.setTime(pmDate);
            int endHour = end.get(Calendar.HOUR);
            // endHour = endHour - 12;
            int endmin = end.get(Calendar.MINUTE);
            int startHour = start.get(Calendar.HOUR);
            int startmin = start.get(Calendar.MINUTE);
            while (startHour < endHour) {

                if (startHour < endHour) {
                    Date a = start.getTime();
                    ams.add(dff.format(a));
                    start.add(Calendar.MINUTE, 30);
                    startHour = start.get(Calendar.HOUR);
                }
            }
            if (startHour == endHour && startmin < endmin == true) {
                start.add(Calendar.MINUTE, 30);
                Date a = start.getTime();
                ams.add(dff.format(a));
                // startHour = start.get(Calendar.HOUR_OF_DAY);
            }
            ams.add(dff.format(dt));
            return ams;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getPM(String pm) {

        try {
            String pmStart = "12:00";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat dff = new SimpleDateFormat("HH:mm");
            Date dt = df.parse(pm);
            Date pmDate = dff.parse(pmStart);
            List<String> ams = new ArrayList<String>();
            Calendar end = Calendar.getInstance();
            end.setTime(dt);
            Calendar start = Calendar.getInstance();
            start.setTime(pmDate);
            int endHour = end.get(Calendar.HOUR);
            // endHour = endHour - 12;
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
                // startHour = start.get(Calendar.HOUR_OF_DAY);
            }
            ams.add(dff.format(dt));
            return ams;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> To12(String pm) {

        try {
            List<String> ams = new ArrayList<String>();
            DateFormat dff = new SimpleDateFormat("hh:mm");
            Date dt = dff.parse(pm);
            Calendar end = Calendar.getInstance();
            end.setTime(dt);
            Date a = end.getTime();
            ams.add(dff.format(a));
            return ams;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getPMShort(String pm) {
          pm="19:45";
        try {
            String pmStart = "12:00";
            // DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat dff = new SimpleDateFormat("HH:mm");
            Date dt = dff.parse(pm);
            Date pmDate = dff.parse(pmStart);
            List<String> ams = new ArrayList<String>();
            Calendar end = Calendar.getInstance();
            end.setTime(dt);
            Calendar start = Calendar.getInstance();
            start.setTime(pmDate);
            int endHour = end.get(Calendar.HOUR);
            // endHour = endHour - 12;
            int endmin = end.get(Calendar.MINUTE);
            int startHour = start.get(Calendar.HOUR);
            int startmin = start.get(Calendar.MINUTE);
            while (startHour < endHour) {

                if (startHour < endHour) {
                    Date a = start.getTime();
                    ams.add(dff.format(a));
                    start.add(Calendar.MINUTE, 30);
                    startHour = start.get(Calendar.HOUR);
                }
            }
            if (startHour == endHour && startmin < endmin == true) {
                Date s = start.getTime();
                ams.add(dff.format(s));
                start.add(Calendar.MINUTE, 30);
                if (start.get(Calendar.MINUTE) < endmin) {
                    Date a = start.getTime();
                    ams.add(dff.format(a));
                }
                // startHour = start.get(Calendar.HOUR_OF_DAY);
            }
          //  ams.add(dff.format(dt));
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
            int h = c.get(Calendar.HOUR_OF_DAY);
            String first = Integer.toString(h > 12 ? h : h + 12);
            String hours = (first.length() == 1 ? "0" + first : first) + ":"
                    + (c.get(Calendar.MINUTE) == 0 ? "00" : "30");

            return hours;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean CompareTime(String time, String start, String end) {

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sd.parse(start);
            Date date2 = sd.parse(end);
            Date date3 = sd.parse(time);
            long t = date3.getTime();// 时间的毫秒
            long s = date1.getTime();
            long e = date2.getTime();
            if (t >= s && t <= e) {
                return true;
            } else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean CompareShortTime(String time, String start, String end) {

        try {
            SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
            Date date1 = sd.parse(start);
            Date date2 = sd.parse(end);
            Date date3 = sd.parse(time);
            long t = date3.getTime();// 时间的毫秒
            long s = date1.getTime();
            long e = date2.getTime();
            if (t >= s && t <= e) {
                return true;
            } else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static LinkedHashMap<String, String> getDate(List<PriceModel> priceModels) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        for (PriceModel p : priceModels) {

        }

        return null;
    }

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    public static Date stringDateToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    // 排序
    public static List<PriceModel> getCollectionsDate(List<PriceModel> priceModels) {
        /*
         * SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
         * DateFormat df = new SimpleDateFormat("hh:mm"); List<String> days =
         * new ArrayList<String>(); List<String> hours = new
         * ArrayList<String>(); boolean amOpm; double price;
         */
        Collections.sort(priceModels, new Comparator<PriceModel>() {
            @Override
            public int compare(PriceModel lhs, PriceModel rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getStartAt());
                Date date2 = DateUtil.stringToDate(rhs.getStartAt());
                // 对日期字段进行升序，如果欲降序可采用after方法
                if (date1.after(date2)) {
                    return 1;
                }
                return -1;

            }
        });
        /*
         * for (PriceModel p : priceModels) { days.add(p.getStartAt()); }
         */
        return priceModels;
    }

    public static String stringToDateToString(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM月dd日");
        String nowDate = simpleDateFormat1.format(dateValue);
        return nowDate;
    }

    public static String stringToDateToOrderString(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
        String nowDate = simpleDateFormat1.format(dateValue);
        return nowDate;
    }

    public static String stringToDateToEventString(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        Date newD = new Date(dateValue.getTime() + 3 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        String nowDate = simpleDateFormat1.format(newD);
        return nowDate;
    }

    public static String stringEventString(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy年MM月dd日(EE)HH:mm" + "开球");
        String nowDate = simpleDateFormat1.format(dateValue);
        return nowDate;
    }

    public static String stringToDateToHourString(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        String nowDate = simpleDateFormat1.format(dateValue);
        return nowDate;
    }

    public static String shortDateString(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy年MM月dd日");
        String nowDate = simpleDateFormat1.format(dateValue);
        return nowDate;
    }

    public static String getTuanFinalDays(String date) {
        String today = "星期一";

        Date s = stringToDate(date);
        Calendar c = Calendar.getInstance();
        c.setTime(s);
        Calendar now = Calendar.getInstance();
        int now_daye = now.get(Calendar.DAY_OF_MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int day1 = c.get(Calendar.DAY_OF_WEEK);
        if (day == now_daye) {
            today = "今天";
        } else
            switch (day1) {
                case 2:
                    today = "星期一";
                    break;
                case 3:
                    today = "星期二";
                    break;
                case 4:
                    today = "星期三";
                    break;
                case 5:
                    today = "星期四";
                    break;
                case 6:
                    today = "星期五";
                    break;
                case 7:
                    today = "星期六";
                    break;
                case 1:
                    today = "星期日";
                    break;
            }
        if (month < 9) {
            return "0" + (month + 1) + "月" + day + "日" + "(" + today + ")";
        } else
            return month + 1 + "月" + day + "日" + "(" + today + ")";

    }

    public static String getTuanFinalAMoPM(String date) {
        String today = "星期一";

        Date s = stringToDate(date);
        Calendar c = Calendar.getInstance();
        c.setTime(s);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int day1 = c.get(Calendar.DAY_OF_WEEK);
        int hour = c.get(Calendar.HOUR_OF_DAY);

        if (hour >= 12) {
            return "下午";
        } else
            return "上午";

    }

    public static String getTuanAMoPM(String date) {
        String today = "星期一";

        Date s = stringDateToDate(date);
        Calendar c = Calendar.getInstance();
        c.setTime(s);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int day1 = c.get(Calendar.DAY_OF_WEEK);
        int hour = c.get(Calendar.HOUR_OF_DAY);

        if (hour >= 12) {
            return "下午";
        } else
            return "上午";

    }

    public static boolean CompareNowTime(String start) {

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sd.parse(start);
            Date date2 = new Date();
            long s = date1.getTime();
            long e = date2.getTime();
            if (s > e) {
                return true;
            } else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
