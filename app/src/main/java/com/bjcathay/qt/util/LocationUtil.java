package com.bjcathay.qt.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.bjcathay.android.util.LogUtil;

import java.util.List;

/**
 * Created by bjcathay on 15-6-23.
 */
public class LocationUtil {
    public static Location getLocation(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
        List<String> lp = lm.getAllProviders();
        for (String item : lp) {
            LogUtil.i("8023", "可用位置服务：" + item);
        }
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
//设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //设置水平位置精度
        //getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String providerName = lm.getBestProvider(criteria, true);
        LogUtil.i("8023", "------位置服务：" + providerName);
        if (providerName != null) {
            Location location = lm.getLastKnownLocation(providerName);
            if (location != null) {
                PreferencesUtils.putString(context, PreferencesConstant.LATITUDE, String.valueOf(location.getLatitude()));
                PreferencesUtils.putString(context, PreferencesConstant.LONGITUDE, String.valueOf(location.getLongitude()));
            }
            LogUtil.i("8023", "-------" + location);
            return location;
        } else {
            Toast.makeText(context, "1.请检查网络连接 \n2.请打开我的位置", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
