package com.bjcathay.golf.util;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import com.bjcathay.golf.application.GApplication;

/**
 * Created by dengt on 15-4-21.
 */
public class DialogUtil {
    public static void hintMessage(String message,Activity activity) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
