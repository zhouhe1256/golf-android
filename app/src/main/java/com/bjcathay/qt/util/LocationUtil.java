
package com.bjcathay.qt.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.db.DBManager;
import com.bjcathay.qt.model.CityListModel;
import com.bjcathay.qt.model.GetCitysModel;
import com.bjcathay.qt.model.ProvinceListModel;

import java.util.List;

/**
 * Created by dengt on 15-6-23.
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
        // 设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); // 设置水平位置精度
        // getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String providerName = lm.getBestProvider(criteria, true);
        LogUtil.i("8023", "------位置服务：" + providerName);
        if (providerName != null) {
            Location location = lm.getLastKnownLocation(providerName);
            if (location != null) {
                PreferencesUtils.putString(context, PreferencesConstant.LATITUDE,
                        String.valueOf(location.getLatitude()));
                PreferencesUtils.putString(context, PreferencesConstant.LONGITUDE,
                        String.valueOf(location.getLongitude()));
            }
            LogUtil.i("8023", "-------" + location);
            return location;
        } else {
            Toast.makeText(context, "1.请检查网络连接 \n2.请打开我的位置", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static void getLocationBybd(final Context context) {
        BDLocationListener my = new BDLocationListener() {
            @Override
            public void onReceiveLocation(final BDLocation location) {
                if (location != null) {
                    PreferencesUtils.putString(context, PreferencesConstant.LATITUDE,
                            String.valueOf(location.getLatitude()));
                    PreferencesUtils.putString(context, PreferencesConstant.LONGITUDE,
                            String.valueOf(location.getLongitude()));
                    String name = location.getCity();
                    if (!name.equals(PreferencesUtils.getString(context,
                            PreferencesConstant.CITY_NAME," "))) {
                        GetCitysModel city = DBManager.getInstance().getCity(
                                location.getCity());
                        if (city.getName() == null) {
                            CityListModel.getTotalCities().done(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    CityListModel cityListModel = arguments.get(0);
                                    DBManager.getInstance().addCitys(cityListModel.getCities());
                                    for (GetCitysModel getCitysModel : cityListModel.getCities()) {
                                        if (getCitysModel.getName().startsWith(location.getCity())) {
                                            PreferencesUtils.putString(context,
                                                    PreferencesConstant.CITY_ID,
                                                    String.valueOf(getCitysModel.getId()));
                                            PreferencesUtils.putString(context,
                                                    PreferencesConstant.CITY_NAME,
                                                    String.valueOf(getCitysModel.getName()));
                                            break;
                                        }
                                    }

                                }
                            });
                        } else {
                            PreferencesUtils.putString(context, PreferencesConstant.CITY_ID,
                                    String.valueOf(city.getId()));
                            PreferencesUtils.putString(context, PreferencesConstant.CITY_NAME,
                                    String.valueOf(city.getName()));
                        }
                    }
                } else {
                    LogUtil.d("LocSDK5", "locClient failed");
                }
            }
        };
        LocationClient mLocationClient = new LocationClient(context); // 声明LocationClient类
        mLocationClient.registerLocationListener(my);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(3600000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(false);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        // 注册监听函数
        if (mLocationClient != null && mLocationClient.isStarted())
            mLocationClient.requestLocation();
        else
            LogUtil.d("LocSDK5", "locClient is null or not started");
    }
}
