package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;

/**
 * Created by dengt on 14-12-29.
 */
public class AddressActivity extends Activity implements View.OnClickListener {

    private Activity context;
    private WebView webview;
    private String url;
    private String location;
    private String content;
    private String srcapp;
    private String title;
    private TopView topView;
    private WebViewClient webViewClient;
    private WebChromeClient webChromeClient;
    private boolean back;
    private RelativeLayout webRongqi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
        initData();
        initEvent();
    }

    private void initData() {

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        location = intent.getStringExtra("location");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        srcapp = intent.getStringExtra("src");
        topView.setTitleText(title);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /*if (url.startsWith("baidumap://map/direction")) {
                    url = " http://api.map.baidu.com/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving&region=西安&output=html&src=yourCompanyName|yourAppName";
                }*/
                view.loadUrl(url);
                return true;
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        if (location != null) {
            url += "&location=" + location;
        }
        if (title != null) {
            url += "&title=" + title;
        }
        if (content != null) {
            url += "&content=" + content;
        }
        if (srcapp != null) {
            url += "&src=" + srcapp;
        }
        String url_ = "http://api.map.baidu.com/marker?location=39.916979519873,116.41004950566&title=我的位置&content=百度奎科大厦&output=html";
        webview.loadUrl(url);

    }

    private void initView() {
        context = this;
        webview = ViewUtil.findViewById(this, R.id.address_webview);
        webRongqi = ViewUtil.findViewById(this, R.id.adddress_web_rongqi);
        topView = ViewUtil.findViewById(this, R.id.top_address_layout);

    }

    private void initEvent() {
        topView.setTitleBackVisiable();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                ViewUtil.finish(this);
                break;
        }

    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
