
package com.bjcathay.qt.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.AttendSucActivity;
import com.bjcathay.qt.activity.LoginActivity;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.fragment.DialogEventFragment;
import com.bjcathay.qt.model.EventModel;
import com.bjcathay.qt.model.ShareModel;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONObject;

/**
 * Created by dengt on 15-5-26.
 */
public class WebJSInterface implements /*
                                        * DeleteInfoDialog.DeleteInfoDialogResult
                                        * ,
                                        */DialogEventFragment.EventResult {
    private final int CODE = 0x717;
    private Context mContext;
    private FragmentActivity mActivity;
    private WebView mWebview;

    // 用JavaScript调用Android函数：
    // 先建立桥梁类，将要调用的Android代码写入桥梁类的public函数
    // 绑定桥梁类和WebView中运行的JavaScript代码
    // 将一个对象起一个别名传入，在JS代码中用这个别名代替这个对象
    // webview.addJavascriptInterface(new
    // WebAppInterface(getApplicationContext(), context,
    // webview,programId,annexDataModel), "mallJSInterface");

    /**
     * 初始化，用于JS调用android函数,别名　golfJSInterface
     *
     * @param a activity
     * @param c context
     * @param webview webview
     */
    public WebJSInterface(Context c, FragmentActivity a, WebView webview) {
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
        mActivity.startActivity(intent);
    }

    /**
     * js调用页面进行报名
     */
    @JavascriptInterface
    public void signup(String id, final String message) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        if (GApplication.getInstance().isLogin()) {
            if (!StringUtils.isEmpty(message)) {
                DialogEventFragment dialogEventFragment = new DialogEventFragment(mActivity,
                        Long.valueOf(id), message, WebJSInterface.this);
                dialogEventFragment.show(mActivity.getSupportFragmentManager(), "event");
            } else {
                EventModel.attendEvent(Long.valueOf(id)).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        if (jsonObject.optBoolean("success")) {
                            Intent intent = new Intent(mActivity, AttendSucActivity.class);
                            intent.putExtra("title", mWebview.getTitle());
                            ViewUtil.startActivity(mActivity, intent);
                        } else {
                            String errorMessage = jsonObject.optString("message");
                            if (!StringUtils.isEmpty(errorMessage))
                                DialogUtil.showMessage(errorMessage);
                            else {
                                int code = jsonObject.optInt("code");
                                DialogUtil.showMessage(ErrorCode.getCodeName(code));
                            }
                        }
                    }
                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        DialogUtil.showMessage("网络连接异常");
                    }
                });
            }
        } else {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            ViewUtil.startActivity(mActivity, intent);
           // mActivity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
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

    @Override
    public void exchangeResult(Long id, boolean isExchange) {
        if (isExchange)
            EventModel.attendEvent(id).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        Intent intent = new Intent(mActivity, AttendSucActivity.class);
                        intent.putExtra("title", mWebview.getTitle());
                        ViewUtil.startActivity(mActivity, intent);
                    } else {
                        String errorMessage = jsonObject.optString("message");
                        if (!StringUtils.isEmpty(errorMessage))
                            DialogUtil.showMessage(errorMessage);
                        else {
                            int code = jsonObject.optInt("code");
                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    DialogUtil.showMessage("网络连接异常");
                }
            });
    }
}
