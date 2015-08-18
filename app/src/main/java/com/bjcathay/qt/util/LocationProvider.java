
package com.bjcathay.qt.util;

import android.content.Context;


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
import com.bjcathay.qt.model.LocationModel;
import com.bjcathay.qt.model.LocationModel.SItude;

import java.util.List;

/**
 * Created by dengt on 15-6-23.
 */
public class LocationProvider {
    private static LocationClient mLocationClient = null;
    Context context;
    private static SItude station = new SItude();
    public LocationProvider(Context context) {
        super();
        this.context = context;
    }
    BDLocationListener listener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(final BDLocation location) {
            if (location != null) {
                PreferencesUtils.putString(context, PreferencesConstant.LATITUDE,
                        String.valueOf(location.getLatitude()));
                PreferencesUtils.putString(context, PreferencesConstant.LONGITUDE,
                        String.valueOf(location.getLongitude()));
                String name = location.getCity();
                    /*
                     * String cityName=PreferencesUtils.getString(context,
                     * PreferencesConstant.CITY_NAME," "); if
                     * (!name.equals(cityName)) {
                     */
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
                // }
            } else {
                LogUtil.d("LocSDK5", "locClient failed");
            }
        }
    };
    public void startLocation() {
        mLocationClient = new LocationClient(context);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving); // 设置网络优先
        option.setProdName("golf"); // 设置产品线名称
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(listener);
        mLocationClient.start();//将开启与获取位置分开，就可以尽量的在后面的使用中获取到位置
    }
    /**
     * 停止，减少资源消耗
     */
    public void stopListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
            mLocationClient = null;
        }
    }
    /**
     * 更新位置并保存
     */
    public void updateListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
            LogUtil.i("location","update the location");
        }
    }

    /**
     * 获取经纬度信息
     *
     * @return
     */
    public SItude getLocation() {
        return station;
    }

//    public static void getLocationBybd(final Context context) {
//
//        LocationClient mLocationClient = new LocationClient(context); // 声明LocationClient类
//        mLocationClient.registerLocationListener(my);
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
//        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
//        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
//        option.setNeedDeviceDirect(false);// 返回的定位结果包含手机机头的方向
//        mLocationClient.setLocOption(option);
//        mLocationClient.start();
//        // 注册监听函数
//        if (mLocationClient != null && mLocationClient.isStarted())
//            mLocationClient.requestLocation();
//        else
//            LogUtil.d("LocSDK5", "locClient is null or not started");
//    }
}
