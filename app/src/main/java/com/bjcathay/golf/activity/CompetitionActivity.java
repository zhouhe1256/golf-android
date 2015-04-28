package com.bjcathay.golf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bjcathay.golf.R;
import com.bjcathay.golf.adapter.CompetitionAdapter;
import com.bjcathay.golf.adapter.PlaceListAdapter;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛事页面
 * Created by dengt on 15-4-20.
 */
public class CompetitionActivity extends Activity {
    private ListView listView;
    private CompetitionAdapter competitionAdapter;
    private List<PlaceModel> placeModels;
    private TopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        initView();
        initEvent();
    }

    private void initView() {
        listView = ViewUtil.findViewById(this, R.id.competition_list);
        topView = ViewUtil.findViewById(this, R.id.top_competition_layout);
        topView.setActivity(this);
        topView.setTitleText("赛事");
        placeModels = new ArrayList<PlaceModel>();
        competitionAdapter = new CompetitionAdapter(placeModels, this);

    }


    private void initEvent() {
        listView.setAdapter(competitionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(CompetitionActivity.this, CompetitionDetailActivity.class);
                ViewUtil.startActivity(CompetitionActivity.this, intent);
            }
        });
    }
}
