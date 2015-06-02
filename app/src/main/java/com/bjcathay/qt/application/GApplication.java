package com.bjcathay.qt.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;
import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.async.LooperCallbackExecutor;
import com.bjcathay.android.cache.ChainedCache;
import com.bjcathay.android.cache.DiskCache;
import com.bjcathay.android.cache.MemoryCache;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.HttpOption;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.igexin.sdk.PushManager;

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
    //  private static File errorLogDir;

    @Override
    public void onCreate() {
        super.onCreate();
        gApplication = this;
        // UncaughtHandler.getInstance(this).init(this, errorLogDir, "");
        SDKInitializer.initialize(this);
        initHttp(this);
    }

    public static GApplication getInstance() {
        PushManager.getInstance().initialize(gApplication.getApplicationContext());
        return gApplication;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

    }

    public synchronized void initHttp(Context context) {
        if (httpInited) {
            return;
        }
        httpInited = true;

        baseDir = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                Environment.getExternalStorageDirectory() : context.getCacheDir();
        baseDir = new File(baseDir, "golf");
        //   errorLogDir = new File(baseDir, "error_log");
        DiskCache.setBaseDir(baseDir);
        // ScreenCapturer.setBaseDir(baseDir);

        String token = getApiToken();
        // DiskCache<String, byte[]> apiCache = new DiskCache<String, byte[]>("api", new DiskCache.ByteArraySerialization());
        Http.instance().option(HttpOption.BASE_URL, ApiUrl.HOST_URL).
                option(HttpOption.MIME, "application/json").
                param("t", token).param("v", ApiUrl.VERSION).
                param("o", ApiUrl.OS).
               /* option(HttpOption.X_Token,token).
                option(HttpOption.X_Version,ApiUrl.VERSION).
                option(HttpOption.X_OS,ApiUrl.OS).*/
                       option(HttpOption.CONNECT_TIMEOUT, 10000).
                option(HttpOption.READ_TIMEOUT, 10000).
                setContentDecoder(new IContentDecoder.JSONDecoder()).
                //cache(apiCache).fallbackToCache(true).
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
                            int code = json.optInt("code");
                            if (ErrorCode.USER_FROZE.equals(code)) {
                                DialogUtil.showMessage(ErrorCode.getCodeName(code));
                            }
                        }
                    }
                }).callbackExecutor(new LooperCallbackExecutor()).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {

                /*if (!NetUtil.isConnected()) {
                    DialogUtil.hintMessage(getString(R.string.network_failed));
                }*/
            }
        });

        ChainedCache<String, byte[]> imageCache = ChainedCache.create(
                400 * 1024 * 1024, new MemoryCache.ByteArraySizer<String>(),
                "images", new DiskCache.ByteArraySerialization<String>()
        );
        Http.imageInstance().cache(imageCache).baseUrl(ApiUrl.HOST_URL).aheadReadInCache(true);
    }

    public void updateApiToken() {
        String token = PreferencesUtils.getString(gApplication, PreferencesConstant.API_TOKEN);
        Http.instance().param("t", token);
    }

    public boolean isLogin() {
        if (getApiToken() == null || getApiToken() == "")
            return false;
        else if (getApiToken().length() > 1)
            return true;
        else return false;
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
