
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.CityAdapter;
import com.bjcathay.qt.adapter.HotCityAdapter;
import com.bjcathay.qt.db.DBManager;
import com.bjcathay.qt.model.CModel;
import com.bjcathay.qt.model.CityListModel;
import com.bjcathay.qt.model.GetCitysModel;
import com.bjcathay.qt.model.PModel;
import com.bjcathay.qt.model.ProvinceListModel;
import com.bjcathay.qt.model.ProvinceModel;
import com.bjcathay.qt.util.CitySelectUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.GridViewexs;
import com.bjcathay.qt.view.QExpandedListView;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-6-24.
 */
public class CitySelectActivity extends Activity implements View.OnClickListener {

    private TopView topView;
    private int expandableListSelectionType = ExpandableListView.PACKED_POSITION_TYPE_NULL;
    private QExpandedListView elv;
    private CityAdapter cityAdapter;
    private List<PModel> pModels;
    private HotCityAdapter hotCityAdapter;
    private GridViewexs listView;
    private TextView myAddress;
    private List<ProvinceModel> province;
    private List<GetCitysModel> getCity;
    private List<CModel> cModels;
    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private int expandFlag = -1;
    private Context context;
    private TextView myAddressNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void GeoCoderAddreaa() {
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                ReverseGeoCodeResult.AddressComponent addressComponent = result.getAddressDetail();
                String address = addressComponent.city;
                if (address.endsWith("市")) {
                    address = address.substring(0, address.length() - 1);
                }
                if (!getCity.isEmpty()) {
                    int j = 0;
                    for (int i = 0; i < getCity.size(); i++) {
                        if (getCity.get(i).getName().startsWith(address)) {
                            j = i;
                            break;
                        }
                    }

                    PreferencesUtils.putString(context, PreferencesConstant.CITY_ID,
                            String.valueOf(getCity.get(j).getId()));
                    PreferencesUtils.putString(context, PreferencesConstant.CITY_NAME,
                            String.valueOf(getCity.get(j).getName()));
                    myAddressNote.setText(getCity.get(j).getName());
                    /*
                     * Intent intent = new Intent(); intent.putExtra("cityId",
                     * getCity.get(j).getId()); intent.putExtra("city",
                     * getCity.get(j).getName()); setResult(1, intent);
                     * finish();
                     */
                }
            }
        });
        String latitude = PreferencesUtils.getString(this, PreferencesConstant.LATITUDE);
        String longitude = PreferencesUtils.getString(this, PreferencesConstant.LONGITUDE);
        if (latitude != null && longitude != null) {
            LatLng ptCenter = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            // 113.134437,41.014679
            // LatLng ptCenter = new LatLng(41.014679,113.134437);
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(ptCenter));
        } else {
            DialogUtil.showMessage("请打开位置定位以获取您的位置");
        }
    }

    private void initData() {
        pModels = new ArrayList<PModel>();
        cModels = new ArrayList<CModel>();
        // todo　从数据库读取省份
        province = DBManager.getInstance().queryProvinces();
        // todo 从数据库去读城市
        getCity = DBManager.getInstance().queryCitys();
        hotCityAdapter = new HotCityAdapter(cModels, this);
        listView.setAdapter(hotCityAdapter);

        cityAdapter = new CityAdapter(this, pModels);
        elv.setAdapter(cityAdapter);
        if (province==null||province.isEmpty()) {
            ProvinceListModel.getProvince().done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    ProvinceListModel provinceListModel = arguments.get(0);
                    province = provinceListModel.getProvinces();
                    DBManager.getInstance().addProvinces(province);
                    if (getCity==null||getCity.isEmpty()) {
                        CityListModel.getTotalCities().done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                CityListModel cityListModel = arguments.get(0);
                                getCity = cityListModel.getCities();
                                DBManager.getInstance().addCitys(getCity);
                                pModels = CitySelectUtil.getCities(province, getCity);
                                cModels = CitySelectUtil.getHot(getCity);
                                hotCityAdapter.updateListView(cModels);
                                cityAdapter.updateListView(pModels);
                                // setListViewHeight(listView);
                            }
                        }).fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                DialogUtil.showMessage(getString(R.string.empty_net_text));
                            }
                        });
                    } else {
                        pModels = CitySelectUtil.getCities(province, getCity);
                        cModels = CitySelectUtil.getHot(getCity);
                        hotCityAdapter.updateListView(cModels);
                        cityAdapter.updateListView(pModels);
                        // setListViewHeight(listView);
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    DialogUtil.showMessage(getString(R.string.empty_net_text));
                }
            });
        } else {
            pModels = CitySelectUtil.getCities(province, getCity);
            cModels = CitySelectUtil.getHot(getCity);
            hotCityAdapter.updateListView(cModels);
            cityAdapter.updateListView(pModels);
        }
        // setListViewHeight(listView);
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_city_select_layout);
        topView.setTitleText("选择城市");
        topView.setTitleBackVisiable();

        elv = ViewUtil.findViewById(this, R.id.qelistview);
        // listView = ViewUtil.findViewById(this, R.id.hot_city_list);
        listView = ViewUtil.findViewById(this, R.id.hot_city_gridview);
        myAddress = ViewUtil.findViewById(this, R.id.my_address);
        myAddressNote = ViewUtil.findViewById(this, R.id.my_address_note);

    }

    /**
     * 设置listview高度，注意listview子项必须为LinearLayout才能调用该方法
     *
     * @param listview listview
     */
    public static void setListViewHeight(ListView listview) {
        int totalHeight = 0;
        ListAdapter adapter = listview.getAdapter();
        if (null != adapter) {
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listview);
                if (null != listItem) {
                    listItem.measure(0, 0);// 注意listview子项必须为LinearLayout才能调用该方法
                    totalHeight += listItem.getMeasuredHeight();
                }
            }

            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = totalHeight + (listview.getDividerHeight() * (listview.getCount() - 1));
            listview.setLayoutParams(params);
        }
    }

    /*
     * public static void setListViewHeight(GridView listview) { int totalHeight
     * = 0; ListAdapter adapter = listview.getAdapter(); if (null != adapter) {
     * for (int i = 0; i < adapter.getCount(); i++) { View listItem =
     * adapter.getView(i, null, listview); if (null != listItem) {
     * listItem.measure(0, 0);// 注意listview子项必须为LinearLayout才能调用该方法 totalHeight
     * += listItem.getMeasuredHeight(); } } ViewGroup.LayoutParams params =
     * listview.getLayoutParams(); params.height = totalHeight -
     * listview.getVerticalSpacing() - 110; listview.setLayoutParams(params); }
     * }
     */

    public static void setListViewHeight(ExpandableListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        int count = listAdapter.getCount();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < cModels.size()) {
                    Intent intent = new Intent();
                    intent.putExtra("cityId", cModels.get(i).getId());
                    intent.putExtra("city", cModels.get(i).getName());
                    setResult(1, intent);
                    finish();
                }
            }
        });
        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // TODO Auto-generated method stub
                if (expandFlag == -1) {
                    elv.expandGroup(groupPosition);
                    expandFlag = groupPosition;
                } else if (expandFlag == groupPosition) {
                    elv.collapseGroup(expandFlag);
                    expandFlag = -1;
                } else {
                    elv.collapseGroup(expandFlag);
                    elv.expandGroup(groupPosition);
                    expandFlag = groupPosition;
                }
                return true;

            }

        });
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                if (expandableListSelectionType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int flatPosition = parent
                            .getFlatListPosition(ExpandableListView
                                    .getPackedPositionForChild(
                                            groupPosition, childPosition));
                    parent.setItemChecked(flatPosition,
                            !parent.isItemChecked(flatPosition));
                }
                Intent intent = new Intent();
                if (childPosition == 0) {
                    intent.putExtra("provinceId", pModels.get(groupPosition).getId());
                    intent.putExtra("province", pModels.get(groupPosition).getProvince());
                } else {
                    intent.putExtra("cityId",
                            pModels.get(groupPosition).getCity().get(childPosition)
                                    .getId());
                    intent.putExtra("city", pModels.get(groupPosition).getCity().get(childPosition)
                            .getName());
                }
                setResult(1, intent);
                finish();
                return true;
            }
        });
        GeoCoderAddreaa();
        myAddressNote.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.my_address_note:
                intent = new Intent();
                String cityID = PreferencesUtils.getString(this, PreferencesConstant.CITY_ID, "");
                String cityName =
                        PreferencesUtils.getString(this, PreferencesConstant.CITY_NAME, "");
                if (!StringUtils.isEmpty(cityID) && !StringUtils.isEmpty(cityID)) {
                    intent.putExtra("cityId",
                            Long.valueOf(cityID));
                    intent.putExtra("city", cityName);
                    setResult(1, intent);
                    finish();
                } else {
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("城市选择页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("城市选择页面");
        MobclickAgent.onPause(this);
    }
}
