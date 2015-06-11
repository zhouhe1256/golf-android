package com.bjcathay.qt.util;

/**
 * Created by bjcathay on 15-6-11.
 */
public class ClickUtil {
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
