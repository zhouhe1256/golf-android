
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by dengt on 15-6-23.
 */
public class SearchActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private int placereqCode = 2;
    private int cityreqCode = 1;
    private TextView citySelect;
    private TextView placeSelect;
    private long cityId;
    private long placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_search_layout);
        citySelect = ViewUtil.findViewById(this, R.id.select_city_name);
        placeSelect = ViewUtil.findViewById(this, R.id.select_place_name);
        topView.setTitleText("搜索球场");
        topView.setTitleBackVisiable();
    }

    private void initEvent() {
    }

    private void search() {
        if (cityId == 0l && placeId == 0l) {
            DialogUtil.showMessage("请输入搜索条件");
            return;
        }
        Intent i = new Intent(this, ProductSearchResultActivity.class);
        i.putExtra("cityId", cityId);
        i.putExtra("placeId", placeId);
        ViewUtil.startActivity(this, i);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.select_city:
                intent = new Intent(this, CitySelectActivity.class);
                startActivityForResult(intent, cityreqCode);
                break;
            case R.id.input_place:
                intent = new Intent(this, KeyWordSearchActivity.class);
                startActivityForResult(intent, placereqCode);
                break;
            case R.id.select_sure:
                search();
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == cityreqCode && resultCode == 1) {
            String name = data.getStringExtra("city");
            cityId = data.getLongExtra("cityId", 0l);
            if (cityId != 0l) {
                citySelect.setTextColor(Color.BLACK);
            }
            citySelect.setText(name);
        } else if (requestCode == placereqCode && resultCode == 2) {
            String name = data.getStringExtra("place");
            placeId = data.getLongExtra("placeId", 0l);
            if (placeId != 0l) {
                placeSelect.setTextColor(Color.BLACK);
            }
            placeSelect.setText(name);
        }
    }

}
