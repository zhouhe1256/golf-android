
package com.bjcathay.qt.util;

import android.app.Activity;
import android.content.Intent;

import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.LoginActivity;
import com.bjcathay.qt.application.GApplication;

/**
 * Created by dengt on 15-5-18.
 */
public class IsLoginUtil {
    public static void isLogin(Activity context, Intent intent) {
        if (GApplication.getInstance().isLogin()) {
            ViewUtil.startActivity(context, intent);

        } else {
            intent = new Intent(context, LoginActivity.class);
            ViewUtil.startActivity(context, intent);
            //context.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
        }
    }
}
