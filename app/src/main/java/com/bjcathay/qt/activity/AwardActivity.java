package com.bjcathay.qt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.ExchangeAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.fragment.DialogExchFragment;
import com.bjcathay.qt.model.PropListModel;
import com.bjcathay.qt.model.PropModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.IsLoginUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

import java.util.ArrayList;
import java.util.List;

/**
 * 兑换页面
 * Created by dengt on 15-4-20.
 */
public class AwardActivity extends FragmentActivity implements ICallback, View.OnClickListener,DialogExchFragment.ExchangeResult {
    private GApplication gApplication;
    private TopView topView;
    private ListView awardingFirst;
    private ExchangeAdapter exchangeAdapter;
    private List<PropModel> propModels;
    private TextView inviteNum;
    private LinearLayout empty;
    private DialogExchFragment dialogExchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        gApplication = GApplication.getInstance();
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_award_layout);
        awardingFirst = ViewUtil.findViewById(this, R.id.exchange_list);
        inviteNum = ViewUtil.findViewById(this, R.id.exchange_invite_numb);
        empty = ViewUtil.findViewById(this, R.id.empty_lin);
        awardingFirst.setEmptyView(empty);
        propModels = new ArrayList<PropModel>();
        dialogExchFragment = new DialogExchFragment(this,this);
        exchangeAdapter = new ExchangeAdapter(propModels, this, inviteNum,dialogExchFragment);
    }

    private void initEvent() {
        topView.setTitleText("兑换");
        topView.setHomeBackVisiable();
        topView.setExchangeVisiable();
        awardingFirst.setAdapter(exchangeAdapter);
       /* awardingFirst.setOnClickListener(this);
        awardingFirst.setOnLongClickListener(this);*/
    }

    private void initData() {
        if (gApplication.isLogin() == true)
            inviteNum.setText(PreferencesUtils.getInt(this, PreferencesConstant.VALIDATED_USER, 0) + "");
        PropListModel.get().done(this);
    }

    @Override
    public void call(Arguments arguments) {
        PropListModel propListModel = arguments.get(0);
        propModels.addAll(propListModel.getProps());
        exchangeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_exchange_img:
                intent = new Intent(AwardActivity.this, MyExchangeActivity.class);
                IsLoginUtil.isLogin(this,intent);
                break;
            case R.id.home_back_img:
                finish();
                break;
            case R.id.about_glb:
                intent = new Intent(this, AboutGLBActivity.class);
                IsLoginUtil.isLogin(this,intent);
                break;
        }
    }
    /*@Override
    public boolean onLongClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.exchange_first:
                intent = new Intent(this, SendFriendActivity.class);
                ViewUtil.startActivity(this, intent);
            return true;
        }
        return false;
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        if (gApplication.isLogin() == true)
            inviteNum.setText(PreferencesUtils.getInt(this, PreferencesConstant.VALIDATED_USER, 0) + "");
    }

    @Override
    public void exchangeResult(UserModel userModel, boolean isExchange) {
        inviteNum.setText("" + userModel.getInviteAmount());
        PreferencesUtils.putInt(gApplication, PreferencesConstant.VALIDATED_USER, userModel.getInviteAmount());

    }
}
