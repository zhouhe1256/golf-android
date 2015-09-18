
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.MoneyAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.MoneyListModel;
import com.bjcathay.qt.model.MoneyModel;
import com.bjcathay.qt.model.RechargeModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.GridViewexs;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by dengt on 15-7-23.
 */
public class MyWalletActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private TextView wallet_remain;
    private GridViewexs price_gridview;
    private List<MoneyModel> chargeList;
    private MoneyAdapter moneyAdapter;
    private MoneyModel moneyModel;
    private TextView desc_text;
    private ImageView helpImage;
    private Button chargeBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        GApplication.getInstance().setFlag(true);
        topView = ViewUtil.findViewById(this, R.id.top_my_wallet_layout);
        wallet_remain = ViewUtil.findViewById(this, R.id.wallet_remain);
        price_gridview = ViewUtil.findViewById(this, R.id.price_gridview);
        desc_text = ViewUtil.findViewById(this, R.id.desc_text);
        chargeBt=ViewUtil.findViewById(this,R.id.deposit_btn);
        topView.setTitleBackVisiable();
        topView.setTitleHelpVisiable();
        topView.setTitleText("我的钱包");
        getUserModel();
        initDate();
        initEvent();
        // wallet_remain.setText((int)
        // Math.floor(getIntent().getDoubleExtra("balance", 0l)) + "");

    }

    private  void getUserModel(){
        UserModel.get().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {

                UserModel userModel = arguments.get(0);
                GApplication.getInstance().setUser(userModel);
                PreferencesUtils.putInt(MyWalletActivity.this, PreferencesConstant.VALIDATED_USER,
                        userModel.getInviteAmount());
                PreferencesUtils.putString(MyWalletActivity.this, PreferencesConstant.INVITE_CODE,
                        userModel.getInviteCode());
                wallet_remain.setText((int) Math.floor(userModel.getBalance()) + "");
            }
        });

    }
    private void initDate() {

        MoneyListModel.getCharge().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                MoneyListModel moneyListModel = arguments.get(0);
                chargeList = moneyListModel.getChargeList();

                if(chargeList.size()!=0){
                    chargeBt.setVisibility(View.VISIBLE);
                    Log.i("111", "" + chargeList);
                    moneyModel=chargeList.get(0);
                    moneyAdapter = new MoneyAdapter(chargeList, MyWalletActivity.this);
                    price_gridview.setAdapter(moneyAdapter);
                    desc_text.setText(moneyModel.getDescription());

                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
             //   DialogUtil.showMessage("");
            }
        });
    }

    public void initEvent() {
        price_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               moneyAdapter.setSeclection(i);
               moneyModel=chargeList.get(i);
               desc_text.setText(moneyModel.getDescription());
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.title_help_img:
                Intent intentH=new Intent(MyWalletActivity.this,AboutWalletActivity.class);
                ViewUtil.startActivity(MyWalletActivity.this,intentH);
                break;
            case R.id.deposit_btn:
                Intent intent = new Intent(MyWalletActivity.this, SelectRechargeTypeActivity.class);
                intent.putExtra("money", moneyModel);
                ViewUtil.startActivity(MyWalletActivity.this, intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GApplication.getInstance().setFlag(true);
        MessageReceiver.baseActivity = this;
        MobclickAgent.onPageStart("我的钱包");
        MobclickAgent.onResume(this);
        getUserModel();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的钱包");
        MobclickAgent.onPause(this);
    }
}
