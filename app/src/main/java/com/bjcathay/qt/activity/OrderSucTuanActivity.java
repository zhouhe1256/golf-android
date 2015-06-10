package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;

/**
 * Created by bjcathay on 15-5-14.
 */
public class OrderSucTuanActivity extends Activity implements View.OnClickListener, DeleteInfoDialog.DeleteInfoDialogResult {
    private TopView topView;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tuan_suc);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_order_tuan_layout);
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText("约场");
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
                intent = new Intent(this, PlaceListActivity.class);
                intent.putExtra("id", id);
                ViewUtil.startActivity(this, intent);
               // DialogUtil.showMessage("分享给好友");
                break;
            case R.id.title_back_img:
                intent=new Intent(this,MainActivity.class);
                ViewUtil.startTopActivity(this,intent);
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
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.service_tel).toString().trim()));
            this.startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        ViewUtil.startTopActivity(this, intent);
        finish();
    }
}
