package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;

/**
 * Created by bjcathay on 15-5-27.
 */
public class PaySuccessActivity extends Activity implements View.OnClickListener, DeleteInfoDialog.DeleteInfoDialogResult {
    private TopView topView;
    private Long id;
    private OrderModel orderModel;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acvitity_pay_success);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_order_suc_layout);
        textView = ViewUtil.findViewById(this, R.id.pay_success_place);
    }

    private void initData() {
        Intent intent = getIntent();
        orderModel = (OrderModel) intent.getSerializableExtra("order");
        id = orderModel.getId();
        String s = DateUtil.stringToDateToOrderString(orderModel.getDate());
        textView.setText(getString(R.string.pay_success_note, s, orderModel.getTitle()));
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText("支付");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.order_suc_first:
                intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra("id", id);
                ViewUtil.startActivity(this, intent);
                break;
            case R.id.order_suc_second:
                if (id != 0) {
                    ShareModel.shareOrders(id).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            ShareModel shareModel = arguments.get(0);
                            ShareUtil.getInstance().shareDemo(PaySuccessActivity.this, shareModel);
                        }
                    });
                }
                break;
            case R.id.title_back_img:
               /* intent = new Intent(this, MyOrderActivity.class);
                ViewUtil.startActivity(this, intent);*/
                finish();
                break;
            case R.id.call_phone:
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(this,
                        R.style.InfoDialog, "呼叫" + getResources().getString(R.string.service_tel).toString().trim() + "？", 0l, this);
                infoDialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(this, MyOrderActivity.class);
        ViewUtil.startActivity(this, intent);*/
        finish();
    }
    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.service_tel).toString().trim()));
            this.startActivity(intent);
        }
    }
}