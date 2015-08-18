package com.bjcathay.android.util;

import android.util.Log;

/**
 * Created by dengt on 15-6-23.
 */
public class LogUtil {
    private static final boolean DEBUG = false;
    public static void e(String TAG, String msg, Throwable tr) {
        Log.e(TAG, msg,tr);
    }
    public static void w(String TAG, String msg, Throwable tr) {
        Log.w(TAG, msg,tr);
    }

    public static void d(String TAG, String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void v(String TAG, String msg) {
        if (DEBUG) {
            Log.v(TAG, msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String TAG, String msg) {
        if (DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String TAG, String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

}
