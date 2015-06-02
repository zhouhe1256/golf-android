package com.bjcathay.qt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.LoginActivity;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.EventModel;
import com.bjcathay.qt.model.ShareModel;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-5-26.
 */
public class WebJSInterface {
    private final int CODE = 0x717;
    private Context mContext;
    private Activity mActivity;
    private WebView mWebview;
    // 用JavaScript调用Android函数：
    // 先建立桥梁类，将要调用的Android代码写入桥梁类的public函数
    // 绑定桥梁类和WebView中运行的JavaScript代码
    // 将一个对象起一个别名传入，在JS代码中用这个别名代替这个对象
    //  webview.addJavascriptInterface(new WebAppInterface(getApplicationContext(), context, webview,programId,annexDataModel), "mallJSInterface");

    /**
     * 初始化，用于JS调用android函数,别名　golfJSInterface
     *
     * @param a       activity
     * @param c       context
     * @param webview webview
     */
    public WebJSInterface(Context c, Activity a, WebView webview) {
        mContext = c;
        mActivity = a;
        mWebview = webview;

    }

    /**
     * js调用登录页面进行登录
     */
    @JavascriptInterface
    public void login() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        //intent.setAction(LoginActivity.MALL_LOGIN);
        // intent.putExtra("url", url);
        mActivity.startActivity(intent);
    }

    /**
     * js调用页面进行报名
     */
    @JavascriptInterface
    public void signup(Long id) {
        if (GApplication.getInstance().isLogin()) {
            EventModel.attendEvent(id).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        DialogUtil.showMessage("报名成功");
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
            Intent intent = new Intent(mContext, LoginActivity.class);
            ViewUtil.startActivity(mContext, intent);
            mActivity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
        }
    }

    /**
     * js调用进行分享
     */
    @JavascriptInterface
    public void golfshare(Long id, String kind) {
        final String KIND = kind;
        final Long id_ = id;
        final String type;
        if (kind.startsWith("PRODUCT")) {
            type = "产品";
        } else if (kind.startsWith("GROUP")) {
            type = "团购";
        } else if (kind.startsWith("ACT")) {
            type = "活动";
        } else if (kind.startsWith("GIFT")) {
            type = "代金券";
        } else {
            type = "产品";
        }
        Handler handler = new Handler();

        handler.post(new Runnable() {

            public void run() {
                // 更新UI界面元素代码
                ShareModel.shareProducts(id_).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        ShareModel shareModel = arguments.get(0);
                        ShareUtil.getInstance().shareDemo(mContext, shareModel);
                    }
                });
            }

        });

    }
}
