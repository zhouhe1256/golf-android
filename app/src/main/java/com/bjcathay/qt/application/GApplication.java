
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
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.HttpOption;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
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
        DiskCache.setBaseDir(baseDir);
        String token = getApiToken();
        Http.instance().option(HttpOption.BASE_URL, ApiUrl.HOST_URL).
                option(HttpOption.MIME, "application/json").
                param("t", token).param("v", ApiUrl.VERSION).
                param("os", ApiUrl.OS).
                option(HttpOption.CONNECT_TIMEOUT, 10000).
                option(HttpOption.READ_TIMEOUT, 10000).
                option(HttpOption.X_Token,token).
                option(HttpOption.X_Version, ApiUrl.VERSION).
                option(HttpOption.X_OS,ApiUrl.OS).
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
                                if (!StringUtils.isEmpty(errorMessage))
                                    DialogUtil.showMessage(errorMessage);
                                else {
                                    DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                }
                            }
                        }
                    }
                }).callbackExecutor(new LooperCallbackExecutor()).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
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
