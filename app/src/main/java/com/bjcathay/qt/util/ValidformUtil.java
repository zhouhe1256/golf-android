package com.bjcathay.qt.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidformUtil {


    public static boolean isMobileNo(String no) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        //1[3|4|5|7|8|][0-9]{9}
       // String s="^1(\\d{10}$)";
        String s="^1[3|4|5|7|8|][0-9]{9}$";
        Pattern ps=Pattern.compile(s);
        Matcher m = ps.matcher(no);
        return m.matches();
    }
}
