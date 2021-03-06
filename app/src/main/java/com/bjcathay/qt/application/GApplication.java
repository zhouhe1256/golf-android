
package com.bjcathay.qt.application;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.async.LooperCallbackExecutor;
import com.bjcathay.android.cache.ChainedCache;
import com.bjcathay.android.cache.DiskCache;
import com.bjcathay.android.cache.MemoryCache;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.HttpOption;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.R;
import com.bjcathay.qt.constant.ApiUrl;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.receiver.DialogReceiver;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.view.NetAccessView;
import com.igexin.sdk.PushManager;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by dengt on 15-4-20.
 */
public class GApplication extends Application implements Thread.UncaughtExceptionHandler {
    private static GApplication gApplication;
    private static volatile boolean httpInited;
    private static File baseDir;
    private UserModel user;
    private Context Gcontext;
    private ProgressDialog progressDialog = null;
    private WindowManager wm;
    private WindowManager.LayoutParams para;
    private NetAccessView mView;
    private boolean flag = true;

    public UserModel getUser() {
        if (user == null) {
            String userJson = PreferencesUtils.getString(this, PreferencesConstant.USER_INFO, "");
            if (StringUtils.isEmpty(userJson)) {
                return null;
            }
            user = JSONUtil.load(UserModel.class, userJson);
        }
        return user;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setUser(UserModel user) {
        this.user = user;
        PreferencesUtils.putString(this, PreferencesConstant.USER_INFO, JSONUtil.dump(user));
    }

    public boolean isUserLogin() {
        return user != null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gApplication = this;
        SDKInitializer.initialize(this);
        wm = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        para = new WindowManager.LayoutParams();
        para.height = -1;
        para.width = -1;
        para.format = 1;
        para.x=0;
        para.y=0;

        para.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        para.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        initHttp(this);
    }

    public static GApplication getInstance() {
        PushManager.getInstance().initialize(gApplication.getApplicationContext());
        return gApplication;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

    }

    public void closeDialog() {
        if (mView.getParent() != null) {
            flag = false;
            // if (dialogReceiver != null) {
            // mView.getContext().unregisterReceiver(dialogReceiver);
            // }
            wm.removeView(mView);
        }
    }

    DialogReceiver dialogReceiver;

    public synchronized void initHttp(Context context) {
        if (httpInited) {
            return;
        }
        this.Gcontext = context;
        // mView = LayoutInflater.from(context).inflate(
        // R.layout.layout_my_dialog, null);
        mView = new NetAccessView(context);
        if (dialogReceiver == null) {
            dialogReceiver = new DialogReceiver();
        }

        httpInited = true;

        baseDir = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                Environment.getExternalStorageDirectory() : context.getCacheDir();
        baseDir = new File(baseDir, "golf");
        DiskCache.setBaseDir(baseDir);
        String token = getApiToken();
        Http.instance().option(HttpOption.BASE_URL, ApiUrl.HOST_URL).
                option(HttpOption.MIME, "application/json").
                param("t", token).param("v", ApiUrl.VERSION).
                param("os", ApiUrl.OS).
                option(HttpOption.CONNECT_TIMEOUT, 10000).
                option(HttpOption.READ_TIMEOUT, 10000).
                option(HttpOption.X_Token, token).
                option(HttpOption.X_Version, ApiUrl.VERSION).
                option(HttpOption.X_OS, ApiUrl.OS).
                setContentDecoder(new IContentDecoder.JSONDecoder()).
                always(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        Object object = arguments.get(0);

                        if (object instanceof Throwable) {
                            Throwable t = (Throwable) object;
                            StringWriter writer = new StringWriter();
                            writer.write(t.getMessage() + "\n");
                            t.printStackTrace(new PrintWriter(writer));
                            String s = writer.toString();
                            System.out.println(s);
                            return;
                        }

                        if (!(object instanceof JSONObject)) {
                            return;
                        }
                        JSONObject json = arguments.get(0);
                        if (!json.optBoolean("success")) {
                            String errorMessage = json.optString("message");
                            int code = json.optInt("code");
                            if (code == 13005) {
                            } else {
//                                if (!StringUtils.isEmpty(errorMessage))
//                                    DialogUtil.showMessage(errorMessage);
//                                else {
//                                    DialogUtil.showMessage(ErrorCode.getCodeName(code));
//                                }
                            }
                        }
                    }
                }).start(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        if (flag == false) {
                            flag = true;
                            IntentFilter homeFilter = new IntentFilter(
                                    Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                            mView.getContext().registerReceiver(dialogReceiver, homeFilter);
                            if (mView.getParent() == null)
                                wm.addView(mView, para);
                        }
                    }
                }).complete(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        if (mView.getParent() != null) {
                            flag = false;
                            if (dialogReceiver != null) {
                                mView.getContext().unregisterReceiver(dialogReceiver);
                            }
                            wm.removeView(mView);
                        }
                    }
                }).
                callbackExecutor(new LooperCallbackExecutor()).
                fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                    }
                });
        ChainedCache<String, byte[]> imageCache = ChainedCache.create(
                400 * 1024 * 1024, new MemoryCache.ByteArraySizer<String>(),
                "images", new DiskCache.ByteArraySerialization<String>()
                );
        Http.imageInstance().cache(imageCache).baseUrl(ApiUrl.HOST_URL).
                aheadReadInCache(true);
    }

    public void updateApiToken() {
        String token = PreferencesUtils.getString(gApplication, PreferencesConstant.API_TOKEN);
        Http.instance().param("t", token).option(HttpOption.X_Token, token);
    }

    public boolean isLogin() {
        if (getApiToken() == null || getApiToken() == "")
            return false;
        else if (getApiToken().length() > 1)
            return true;
        else
            return false;
    }

    private boolean isPushID;

    public void setPushID(boolean isPushID) {
        this.isPushID = isPushID;
    }

    public boolean isPushID() {
        return isPushID;
    }

    public String getApiToken() {
        return PreferencesUtils.getString(gApplication, PreferencesConstant.API_TOKEN, "");
    }
}
