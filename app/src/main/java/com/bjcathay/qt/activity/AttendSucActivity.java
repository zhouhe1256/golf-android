
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-6-1.
 */
public class AttendSucActivity extends Activity implements View.OnClickListener,
        DeleteInfoDialog.DeleteInfoDialogResult {
    private TopView topView;
    private TextView textView;
    String tiele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_suc);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_sttend_suc_layout);
        textView = ViewUtil.findViewById(this, R.id.attend_name);
    }

    private void initData() {
        Intent intent = getIntent();
        tiele = intent.getStringExtra("title");
        textView.setText(getString(R.string.attend_success_note, tiele));
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText(tiele);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                Intent intent = new Intent(this, MainActivity.class);
                ViewUtil.startTopActivity(this, intent);
                finish();
                break;
            case R.id.call_phone:
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(this,
                        R.style.InfoDialog,
                        getResources().getString(R.string.service_tel_format).toString().trim()
                        , "呼叫", 0l, this);
                infoDialog.show();
                break;
        }
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + getResources().getString(R.string.service_tel).toString().trim()));
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

    @Override
    public void onResume() {
        super.onResume();
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("赛事参加成功页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("赛事参加成功页面");
        MobclickAgent.onPause(this);
    }
}
