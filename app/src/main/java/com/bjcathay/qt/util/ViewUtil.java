
package com.bjcathay.qt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewUtil {

    private ViewUtil() {
    }

    public static void finish(Activity context) {
        context.finish();
        context.overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

    public static void startActivity(Activity context, Intent intent) {
        context.startActivity(intent);
        // context.overridePendingTransition(android.R.anim.slide_out_right,
        // android.R.anim.slide_in_left);
    }

    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
        // context.overridePendingTransition(android.R.anim.slide_out_right,
        // android.R.anim.slide_in_left);
    }

    public static void startTopActivity(Activity context, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static <T> T findViewById(Activity context, int id) {
        return (T) context.findViewById(id);
    }

    public static <T> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /*
     * public static void setListViewHeightBasedOnChildren(PullToRefreshListView
     * listView) { ListAdapter listAdapter =
     * listView.getRefreshableView().getAdapter(); if (listAdapter == null) { //
     * pre-condition return; } int totalHeight = 0; for (int i = 0; i <
     * listAdapter.getCount(); i++) { View listItem = listAdapter.getView(i,
     * null, listView); listItem.measure(0, 0); totalHeight +=
     * listItem.getMeasuredHeight(); } ViewGroup.LayoutParams params =
     * listView.getLayoutParams(); params.height = totalHeight +
     * (listView.getRefreshableView().getDividerHeight() *
     * (listAdapter.getCount() - 1)); listView.setLayoutParams(params); }
     */

    public static String encode(byte[] bstr) {

        return Base64.encodeToString(bstr, Base64.NO_WRAP);
    }

    /*
     * public static String encode(byte[] bytes){ Base }
     */
}
