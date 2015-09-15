package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.model.MoneyModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

public class RechargeSuccessActivity extends Activity implements View.OnClickListener {
private TextView recharge_way;
    private TextView recharge_amount;
    private MoneyModel moneyModel;
    private TopView topView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_success);
        topView = ViewUtil.findViewById(this, R.id.top_pay_detail_layout);

       // topView.setTitleBackVisiable();

        topView.setTitleText("充值详情");

        initView();
        initData();

    }

    private void initView() {
        recharge_way= ViewUtil.findViewById(this,R.id.recharge_way_text);
        recharge_amount=ViewUtil.findViewById(this,R.id.recharge_amount_text);

    }
   private void  initData(){
       Intent intent=getIntent();
       String flag=intent.getStringExtra("flag");
       moneyModel=(MoneyModel)intent.getSerializableExtra("money");
       String money=String.valueOf(moneyModel.getMoney());
       if(flag.equals("wx")){
           recharge_way.setText("微信支付");
       }else{
           recharge_way.setText("支付宝支付");
       }
       recharge_amount.setText(money);
   }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }

        switch (view.getId()){
            case R.id.rec_suc_btn:

                finish();
                break;


        }

    }
}
