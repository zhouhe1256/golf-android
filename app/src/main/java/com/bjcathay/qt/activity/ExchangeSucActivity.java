
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
import com.bjcathay.qt.model.PropModel;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-5-8.
 */
public class ExchangeSucActivity extends Activity implements View.OnClickListener,
        DeleteInfoDialog.DeleteInfoDialogResult {
    private Activity context;
    private TopView topView;
    private PropModel propModel;
    private TextView title;
    private TextView content;
    private TextView first;
    private TextView second;

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
        second = ViewUtil.findViewById(this, R.id.remind_second);
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        first.setOnClickListener(this);
        second.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        propModel = (PropModel) intent.getSerializableExtra("prop");
        String titleString = intent.getStringExtra("title");
        topView.setTitleText(titleString);
        title.setText("兑换成功！");
        content.setText("您已成功兑换一枚" + propModel.getName());
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.remind_first:
                Intent intent = new Intent(context, SendFriendActivity.class);
                intent.putExtra("id", propModel.getId());
                ViewUtil.startActivity(context, intent);
                finish();
                break;
            case R.id.remind_second:
                ShareModel.share().done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        ShareModel shareModel = arguments.get(0);
                        ShareUtil.getInstance().shareDemo(context, shareModel);
                    }
                });
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
        MobclickAgent.onPageStart("兑换成功页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("兑换成功页面");
        MobclickAgent.onPause(this);
    }
}
