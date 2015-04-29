package com.bjcathay.golf.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bjcathay.golf.R;
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
    public static void showDialog(Activity context,int resource){
        LayoutInflater inflater = context.getLayoutInflater();
        ViewGroup rootView = (ViewGroup) inflater.inflate(resource, null);
        Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        dialog.setContentView(rootView);
        //dialog.create();
        // dialog.setContentView(rootView);
        dialog.show();
    }
}
