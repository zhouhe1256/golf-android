
package com.bjcathay.qt.receiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.CompetitionDetailActivity;
import com.bjcathay.qt.activity.ExerciseActivity;
import com.bjcathay.qt.activity.LoginActivity;
import com.bjcathay.qt.activity.MyWalletActivity;
import com.bjcathay.qt.activity.OrderDetailActivity;
import com.bjcathay.qt.activity.OrderStadiumDetailActivity;
import com.bjcathay.qt.activity.PackageDetailActivity;
import com.bjcathay.qt.activity.ProductOfflineActivity;
import com.bjcathay.qt.activity.RealTOrderActivity;
import com.bjcathay.qt.activity.WelcomeActivity;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.model.PushModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.IsLoginUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.PushInfoDialog;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by dengt on 15-5-22.
 */
public class MessageReceiver extends BroadcastReceiver implements PushInfoDialog.PushResult {

    private static int ids = 0;
    public static Activity baseActivity;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid,
                        messageid, 90001);
                if (payload != null) {
                    String data = new String(payload);
                    LogUtil.d("GetuiSdkDemo", "Got Payload:" + data);// {"type":"ORDER","target":33}
                    // DialogUtil.showMessage(data);
                    try {
                        PushModel pushModel = JSONUtil.load(PushModel.class, data);
                        if (pushModel != null) {
                            handlePush(context, pushModel);
                        }
                    } catch (IllegalArgumentException e) {
                    }

                }
                break;
            case PushConsts.GET_CLIENTID:
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                break;
            default:
                break;
        }
    }

    private void handlePush(final Context context, final PushModel pushModel) {
        PreferencesUtils.putBoolean(context, PreferencesConstant.NEW_MESSAGE_FLAG, true);
        Intent intent;
        if (isRunning(context)) {
            if (isRunningForeground(context)) {
                if (baseActivity != null)
                    PushInfoDialog.getInstance(baseActivity,
                            R.style.InfoDialog, pushModel.getM(), "忽略", pushModel, this).show();
            } else {
                // ORDER,SYSTEM,BALANCE,PRODUCT,AD,COMPETITION,OTHER;
                switch (pushModel.getT()) {
                    case ORDER:
                        if (GApplication.getInstance().isLogin()) {
                            intent = new Intent(context, OrderDetailActivity.class);
                            intent.putExtra("id", Long.parseLong(pushModel.getId()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        }
                        sendNotice(context, intent, pushModel);
                        // IsLoginUtil.isLogin(context, intent);
                        break;
                    case MESSAGE:
                        intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setClass(context, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                                | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);// 关键的一步，设置启动模式
                        sendNotice(context, intent, pushModel);
                        break;
                    case BALANCE:
                        if (GApplication.getInstance().isLogin()) {
                            intent = new Intent(context, MyWalletActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        }
                        sendNotice(context, intent, pushModel);
                        break;
                    case COMPETITION:
                        intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setClass(context, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                                | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);// 关键的一步，设置启动模式
                        sendNotice(context, intent, pushModel);
                        break;
                    case PRODUCT:
                        ProductModel.product(Long.parseLong(pushModel.getId()))
                                .done(new ICallback() {
                                    @Override
                                    public void call(Arguments arguments) {
                                        JSONObject jsonObject = arguments.get(0);
                                        if (jsonObject.optBoolean("success")) {
                                            ProductModel productModel = JSONUtil.load(
                                                    ProductModel.class,
                                                    jsonObject.optJSONObject("product"));
                                            Intent intent = null;
                                            switch (productModel.getType()) {
                                                case COMBO:
                                                    intent = new Intent(context,
                                                            PackageDetailActivity.class);
                                                    intent.putExtra("id", productModel.getId());
                                                    intent.putExtra("name", productModel.getName());
                                                    intent.putExtra("product", productModel);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    sendNotice(context, intent, pushModel);
                                                    // ViewUtil.startActivity(context,
                                                    // intent);
                                                    break;
                                                case REAL_TIME:
                                                    intent = new Intent(context,
                                                            RealTOrderActivity.class);
                                                    intent.putExtra("id", productModel.getId());
                                                    intent.putExtra("imageurl",
                                                            productModel.getImageUrl());
                                                    intent.putExtra("name", productModel.getName());
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    sendNotice(context, intent, pushModel);
                                                    // /
                                                    // ViewUtil.startActivity(context,
                                                    // intent);
                                                    break;
                                                default:
                                                    intent = new Intent(context,
                                                            OrderStadiumDetailActivity.class);
                                                    intent.putExtra("id", productModel.getId());
                                                    intent.putExtra("imageurl",
                                                            productModel.getImageUrl());
                                                    intent.putExtra("name", productModel.getName());
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    sendNotice(context, intent, pushModel);
                                                    // ViewUtil.startActivity(context,
                                                    // intent);
                                                    break;
                                            }
                                        } else {
                                            String errorMessage = jsonObject.optString("message");
                                            int code = jsonObject.optInt("code");
                                            if (code == 13005) {
                                                Intent intent = new Intent(context,
                                                        ProductOfflineActivity.class);
                                                intent.putExtra("name", "产品已下线");
                                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                sendNotice(context, intent, pushModel);
                                                // ViewUtil.startActivity(context,
                                                // intent);
                                            } else {
                                                if (!StringUtils.isEmpty(errorMessage))
                                                    DialogUtil.showMessage(errorMessage);
                                                else {

                                                    DialogUtil.showMessage(ErrorCode
                                                            .getCodeName(code));
                                                }
                                            }
                                        }
                                    }
                                }

                                ).

                                fail(new ICallback() {
                                    @Override
                                    public void call(Arguments arguments) {
                                        DialogUtil.showMessage(context
                                                .getString(R.string.empty_net_text));
                                    }
                                }

                                );
                        break;
                    case AD:
                        intent = new Intent(context, ExerciseActivity.class);
                        intent.putExtra("url", pushModel.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        sendNotice(context, intent, pushModel);
                        // ViewUtil.startActivity(context, intent);
                        break;
                    case OTHER:
                        // intent = new Intent(context, MainActivity.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        // ViewUtil.startActivity(context, intent);
                        break;
                    default:
                        // intent = new Intent(context, MainActivity.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        // ViewUtil.startActivity(context, intent);
                        break;
                }

                // NotificationManager mNotificationManager =
                // (NotificationManager) context
                // .getSystemService(Context.NOTIFICATION_SERVICE);
                // Notification.Builder builder = new
                // Notification.Builder(context);
                // intent = new Intent(Intent.ACTION_MAIN);
                // intent.addCategory(Intent.CATEGORY_LAUNCHER);
                // intent.setClass(context, WelcomeActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                // Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                // | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);// 关键的一步，设置启动模式
                // PendingIntent contentIntent =
                // PendingIntent.getActivity(context, ids, intent,
                // PendingIntent.FLAG_UPDATE_CURRENT);
                // builder.setContentIntent(contentIntent).setTicker(pushModel.getM()).
                // setContentTitle("7铁高尔夫").
                // setContentText(pushModel.getM()).
                // setSmallIcon(R.drawable.ic_launcher).
                // setAutoCancel(true)
                // .setDefaults(Notification.DEFAULT_SOUND);
                // Notification notification;
                // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                // notification = new Notification();
                // notification.contentIntent = contentIntent;
                // notification.tickerText = pushModel.getM();
                // notification.flags = Notification.FLAG_AUTO_CANCEL;
                // notification.defaults = Notification.DEFAULT_SOUND;
                // notification.icon = R.drawable.ic_launcher;
                // } else
                // notification = builder.
                // build();
                // mNotificationManager.notify(ids++, notification);
                // if (baseActivity != null)
                // PushInfoDialog.getInstance(baseActivity,
                // R.style.InfoDialog, pushModel.getM(), "忽略", pushModel,
                // this).show();
            }
        } else {

            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("push", pushModel);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClass(context, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            PendingIntent contentIntent = PendingIntent.getActivity(context, ids, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent).setTicker(pushModel.getM()).
                    setContentTitle("7铁高尔夫").
                    setContentText(pushModel.getM()).
                    setSmallIcon(R.drawable.ic_launcher).
                    setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND);
            Notification notification;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                notification = new Notification();
                notification.contentIntent = contentIntent;
                notification.tickerText = pushModel.getM();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notification.defaults = Notification.DEFAULT_SOUND;
                notification.icon = R.drawable.ic_launcher;

            } else
                notification = builder.
                        build();
            mNotificationManager.notify(ids++, notification);
        }

    }

    public void sendNotice(Context context, Intent intent, PushModel pushModel) {
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        // intent = new Intent(Intent.ACTION_MAIN);
        // intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // intent.setClass(context, WelcomeActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
        // Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        // | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);// 关键的一步，设置启动模式
        PendingIntent contentIntent = PendingIntent.getActivity(context, ids, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent).setTicker(pushModel.getM()).
                setContentTitle("7铁高尔夫").
                setContentText(pushModel.getM()).
                setSmallIcon(R.drawable.ic_launcher).
                setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND);
        Notification notification;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification();
            notification.contentIntent = contentIntent;
            notification.tickerText = pushModel.getM();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_SOUND;
            notification.icon = R.drawable.ic_launcher;
        } else
            notification = builder.
                    build();
        mNotificationManager.notify(ids++, notification);
    }

    public boolean isRunningForeground(Context context) {
        String packageName = getPackageName(context);
        String topActivityClassName = getTopActivityName(context);
        System.out.println("packageName=" + packageName + ",topActivityClassName="
                + topActivityClassName);
        if (packageName != null && topActivityClassName != null
                && topActivityClassName.startsWith(packageName)) {
            System.out.println("---> isRunningForeGround");
            return true;
        } else {
            System.out.println("---> isRunningBackGround");
            return false;
        }
    }

    private boolean isRunning(Context context) {
        // 判断应用是否在运行
        ActivityManager am = (ActivityManager) GApplication.getInstance().getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = getPackageName(context);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
                    || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                LogUtil.i("isRunning",
                        info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="
                                + info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }

    public String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context
                        .getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    public String getPackageName(Context context) {
        String packageName = context.getPackageName();
        return packageName;
    }

    @Override
    public void pushResult(PushModel pushModel, boolean isDelete, final Context context) {
        if (isDelete) {
            Intent intent;
            // ORDER,SYSTEM,BALANCE,PRODUCT,AD,COMPETITION,OTHER;
            switch (pushModel.getT()) {
                case ORDER:
                    intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("id", Long.parseLong(pushModel.getId()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    IsLoginUtil.isLogin(context, intent);
                    break;
                case MESSAGE:
                    break;
                case BALANCE:
                    intent = new Intent(context, MyWalletActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    IsLoginUtil.isLogin(context, intent);
                    break;
                case COMPETITION:
                    // intent = new Intent(context,
                    // CompetitionDetailActivity.class);
                    // intent.putExtra("id", Long.parseLong(pushModel.getId()));
                    // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    // ViewUtil.startActivity(context, intent);
                    break;
                case PRODUCT:
                    ProductModel.product(Long.parseLong(pushModel.getId()))
                            .done(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    JSONObject jsonObject = arguments.get(0);
                                    if (jsonObject.optBoolean("success")) {
                                        ProductModel productModel = JSONUtil.load(
                                                ProductModel.class,
                                                jsonObject.optJSONObject("product"));
                                        Intent intent = null;
                                        switch (productModel.getType()) {
                                            case COMBO:
                                                intent = new Intent(context,
                                                        PackageDetailActivity.class);
                                                intent.putExtra("id", productModel.getId());
                                                intent.putExtra("name", productModel.getName());
                                                intent.putExtra("product", productModel);
                                                ViewUtil.startActivity(context, intent);
                                                break;
                                            case REAL_TIME:
                                                intent = new Intent(context,
                                                        RealTOrderActivity.class);
                                                intent.putExtra("id", productModel.getId());
                                                intent.putExtra("imageurl",
                                                        productModel.getImageUrl());
                                                intent.putExtra("name", productModel.getName());
                                                ViewUtil.startActivity(context, intent);
                                                break;
                                            default:
                                                intent = new Intent(context,
                                                        OrderStadiumDetailActivity.class);
                                                intent.putExtra("id", productModel.getId());
                                                intent.putExtra("imageurl",
                                                        productModel.getImageUrl());
                                                intent.putExtra("name", productModel.getName());
                                                ViewUtil.startActivity(context, intent);
                                                break;
                                        }
                                    } else {
                                        String errorMessage = jsonObject.optString("message");
                                        int code = jsonObject.optInt("code");
                                        if (code == 13005) {
                                            Intent intent = new Intent(context,
                                                    ProductOfflineActivity.class);
                                            intent.putExtra("name", "产品已下线");
                                            ViewUtil.startActivity(context, intent);
                                        } else {
                                            if (!StringUtils.isEmpty(errorMessage))
                                                DialogUtil.showMessage(errorMessage);
                                            else {

                                                DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                            }
                                        }
                                    }
                                }
                            }

                            ).

                            fail(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    DialogUtil.showMessage(context
                                            .getString(R.string.empty_net_text));
                                }
                            }

                            );
                    break;
                case AD:
                    intent = new Intent(context, ExerciseActivity.class);
                    intent.putExtra("url", pushModel.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ViewUtil.startActivity(context, intent);
                    break;
                case OTHER:
                    // intent = new Intent(context, MainActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    // ViewUtil.startActivity(context, intent);
                    break;
                default:
                    // intent = new Intent(context, MainActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    // ViewUtil.startActivity(context, intent);
                    break;
            }

        }
    }
}
