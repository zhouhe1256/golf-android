
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.SelectMapPopupWindow;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by dengt on 15-5-28.
 */
public class BaiduAddressActivity extends Activity implements View.OnClickListener,
        SelectMapPopupWindow.SelectMapResult {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private TopView topView;
    private String url;
    private String location;
    private String content;
    private String srcapp;
    private String title;
    private String bduri;
    private String gduri;
    private String gguri;
    private double lat;
    private double lon;
    private SelectMapPopupWindow menuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bd_address);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        location = intent.getStringExtra("location");
        lat = intent.getDoubleExtra("lat", 0);
        lon = intent.getDoubleExtra("lon", 0);
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        srcapp = intent.getStringExtra("src");
        topView.setTitleText(title);
        LatLng ll = new LatLng(lat, lon);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mMapView.showZoomControls(false);
        mBaiduMap.animateMapStatus(u);
        bduri = "intent://map/direction?origin=我的位置&destination=latlng:"
                + location
                + "|name:"
                + title
                + "&mode=driving&region=北京&src=bjcathay|qtgolf#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
        gduri = "intent://map/direction?origin=我的位置&destination=latlng:"
                + location
                + "|name:"
                + title
                + "&mode=driving&region=北京&src=bjcathay|qtgolf#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
        gguri = "intent://map/direction?origin=我的位置&destination=latlng:"
                + location
                + "|name:"
                + title
                + "&mode=driving&region=北京&src=bjcathay|qtgolf#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
    }

    private void initView() {
        mMapView = ViewUtil.findViewById(this, R.id.bmapView);
        topView = ViewUtil.findViewById(this, R.id.top_bd_address_layout);
    }

    private void initEvent() {
        topView.setDHVisiable();
        topView.setTitleBackVisiable();

        LatLng point = new LatLng(lat, lon);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        mBaiduMap.addOverlay(option);
        View text = getLayoutInflater().inflate(R.layout.address_content, null);
        FrameLayout.LayoutParams lpLl = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lpLl.gravity = Gravity.CENTER;
        text.setLayoutParams(lpLl);
        TextView t = ViewUtil.findViewById(text, R.id.address_title);
        TextView c = ViewUtil.findViewById(text, R.id.address_content);
        t.setText(title);
        c.setText(content);
        LatLng pt = new LatLng(lat, lon);
        InfoWindow mInfoWindow = new InfoWindow(text, pt, -47);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("球场地址页面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        MobclickAgent.onPageEnd("球场地址页面");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_daohang:
                if (isInstallByread("com.baidu.BaiduMap") == false
                        && isInstallByread("com.autonavi.minimap") == false
                        && isInstallByread("com.tencent.map") == false
                        && isInstallByread("com.sogou.map.android.maps") == false)
                    DialogUtil.showMessage("您尚未安装地图客户端");
                else {
                    menuWindow = new SelectMapPopupWindow(this, this);
                    menuWindow.showAtLocation(this.findViewById(R.id.bmapView), Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                }
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    static double a = 6378245.0;
    static double ee = 0.00669342162296594323;

    public static LatLng transformFromWGSToGCJ(LatLng wgLoc) {

        // 如果在国外，则默认不进行转换
        if (outOfChina(wgLoc.latitude, wgLoc.longitude)) {
            return new LatLng(wgLoc.latitude, wgLoc.longitude);
        }
        double dLat = transformLat(wgLoc.longitude - 105.0,
                wgLoc.latitude - 35.0);
        double dLon = transformLon(wgLoc.longitude - 105.0,
                wgLoc.latitude - 35.0);
        double radLat = wgLoc.latitude / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);

        return new LatLng(wgLoc.latitude + dLat, wgLoc.longitude + dLon);
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(x > 0 ? x : -x);
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x
                * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0
                * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y
                * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(x > 0 ? x : -x);
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x
                * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0
                * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x
                / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    @Override
    public void selectBDMapResult() {
        Intent intent = null;
        try {
            intent = Intent.getIntent(bduri);
            startActivity(intent); // 启动调用
            LogUtil.e("GasStation", "百度地图客户端已经安装");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void selectGDMapResult() {
        LatLng latLng = new LatLng(lat, lon);
        latLng = transformFromWGSToGCJ(latLng);
        StringBuffer sb = new StringBuffer();
        sb.append("androidamap://viewMap?");
        sb.append("sourceApplication=").append(getPackageName());
        sb.append("&poiname=").append(title);
        sb.append("&lat=").append(latLng.latitude);
        sb.append("&lon=").append(latLng.longitude);
        sb.append("&dev=").append("1");
        sb.append("&style=").append("2");
        Intent intent = null;
        try {
            intent = Intent.getIntent(sb.toString());
            startActivity(intent); // 启动调用
            LogUtil.e("GasStation", "高德地图客户端已经安装");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void selectTXMapResult() {
        // http://apis.map.qq.com/uri/v1/routeplan?type=bus&from=我的家&fromcoord=39.980683,116.302&to=中关村&tocoord=39.9836,116.3164&policy=1&referer=tengxun
        // qqmap://map/routeplan?type=drive&from=中关村&to=望京&policy=0&referer=tengxun
        // fromcoord=CurrentLocation
        LatLng latLng = new LatLng(lat, lon);
        latLng = transformFromWGSToGCJ(latLng);
        StringBuffer sb = new StringBuffer();
        sb.append("qqmap://map/routeplan?");
        sb.append("referer=").append("tengxun");
        sb.append("&to=").append(title);
        sb.append("&tocoord=").append(latLng.longitude + "," + latLng.latitude);
        sb.append("&type=").append("drive");
        sb.append("&fromcoord=").append("CurrentLocation");
        Intent intent = null;
        try {
            intent = Intent.getIntent(sb.toString());
            startActivity(intent); // 启动调用
            LogUtil.e("GasStation", "腾讯地图客户端已经安装");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectSGMapResult() {
        /*
         * LatLng latLng = new LatLng(lat, lon); latLng =
         * transformFromWGSToGCJ(latLng); Uri mUri = Uri.parse("geo:" +
         * latLng.latitude + "," + latLng.longitude + "?q=" + title); Intent
         * mIntent = new Intent(Intent.ACTION_VIEW, mUri);
         * startActivity(mIntent);
         */
    }
}
