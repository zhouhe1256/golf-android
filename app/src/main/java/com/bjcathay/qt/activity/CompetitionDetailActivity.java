package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.EventModel;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.util.WebJSInterface;
import com.bjcathay.qt.view.TopView;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-4-28.
 */
public class CompetitionDetailActivity extends Activity implements ICallback, View.OnClickListener {
    private TopView topView;
    private Long id;
    private EventModel eventModel;
    private WebView webview;
    private WebChromeClient webChromeClient;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_detail);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_competition_detail_layout);
        webview = (WebView) findViewById(R.id.webview);
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setShareVisiable();
        topView.setTitleText("公开赛");

        webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                topView.setTitleText(title);
            }

        };
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {//拨打电话
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                } else {
                    view.loadUrl(url);
                    return true;
                }
            }
        });
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setJavaScriptEnabled(true);

        // 用JavaScript调用Android函数：
        // 先建立桥梁类，将要调用的Android代码写入桥梁类的public函数
        // 绑定桥梁类和WebView中运行的JavaScript代码
        // 将一个对象起一个别名传入，在JS代码中用这个别名代替这个对象
        webview.addJavascriptInterface(new WebJSInterface(getApplicationContext(), this, webview), "golfJSInterface");
        webview.setWebChromeClient(webChromeClient);
        webview.getSettings().setJavaScriptEnabled(true);

        if (url != null) {
            if (!url.contains("?"))
                url += "?";
            webview.loadUrl(url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            if (url.equals(webview.getUrl())) {
                finish();
            } else {
                webview.goBack(); //goBack()表示返回WebView的上一页面
            }
            return true;
        } else {
            finish();
        }
        return false;
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        url = intent.getStringExtra("url");
        if (id != 0)
            EventModel.getEventDetail(id).done(this);
    }

    @Override
    public void call(Arguments arguments) {
        eventModel = arguments.get(0);
        url = eventModel.getUrl();
        if (webview != null)
            webview.loadUrl(url);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.attend_now:
                if (id != 0)
                    if (GApplication.getInstance().isLogin()) {
                        EventModel.attendEvent(id).done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                if (jsonObject.optBoolean("success")) {
                                    // DialogUtil.showMessage("报名成功");
                                    Intent intent = new Intent(CompetitionDetailActivity.this, AttendSucActivity.class);
                                    intent.putExtra("title", eventModel.getName());
                                    ViewUtil.startActivity(CompetitionDetailActivity.this, intent);
                                } else {
                                    int code = jsonObject.optInt("code");
                                    DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                }
                            }
                        }).fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                DialogUtil.showMessage("报名失败");
                            }
                        });
                    } else {
                        Intent intent = new Intent(CompetitionDetailActivity.this, LoginActivity.class);
                        ViewUtil.startActivity(CompetitionDetailActivity.this, intent);
                        CompetitionDetailActivity.this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                    }
                break;
            case R.id.title_back_img:
                finish();
                break;
            case R.id.title_share_img:
                if (id != 0)
                    ShareModel.shareCompetitions(id).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            ShareModel shareModel = arguments.get(0);
                            ShareUtil.getInstance().shareDemo(CompetitionDetailActivity.this, shareModel);
                        }
                    });

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webview != null)
            webview.loadUrl(url);
    }
}
