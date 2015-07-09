
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
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-6-10.
 */
public class SendExchangeSucActivity extends Activity implements View.OnClickListener,
        DeleteInfoDialog.DeleteInfoDialogResult {
    private Activity context;
    private TopView topView;
    private String name;
    private String phone;
    private TextView title;
    private TextView content;
    private TextView first;
    private TextView firstNote;
    private TextView second;
    private TextView secondNote;
    private ShareModel shareModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_suc);
        context = this;
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_remind_layout);
        title = ViewUtil.findViewById(this, R.id.remind_title);
        content = ViewUtil.findViewById(this, R.id.remind_context);
        first = ViewUtil.findViewById(this, R.id.remind_first);
        firstNote = ViewUtil.findViewById(this, R.id.remind_first_note);
        second = ViewUtil.findViewById(this, R.id.remind_second);
        secondNote = ViewUtil.findViewById(this, R.id.remind_second_note);
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        first.setOnClickListener(this);
        second.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        topView.setTitleText("赠送成功");
        title.setText("赠送成功！");
        content.setText("您已成功赠送用户" + phone + "一枚" + name);
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.remind_first:
                Intent intent = new Intent(context, MyExchangeActivity.class);
                ViewUtil.startActivity(context, intent);
                finish();
                break;
            case R.id.remind_second:
                if (shareModel == null)
                    ShareModel.share().done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            shareModel = arguments.get(0);
                            ShareUtil.getInstance().shareDemo(context, shareModel);
                        }
                    });
                else
                    ShareUtil.getInstance().shareDemo(context, shareModel);
                break;
            case R.id.title_back_img:
                intent = new Intent(this, MainActivity.class);
                ViewUtil.startTopActivity(this, intent);
                finish();
                break;
            case R.id.call_phone:
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(this,
                        R.style.InfoDialog, getResources().getString(R.string.service_tel_format)
                                .toString().trim(), "呼叫", 0l, this);
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
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
