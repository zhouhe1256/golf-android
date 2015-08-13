
package com.bjcathay.qt.receiver;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.Enumeration.MessageType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.CompetitionDetailActivity;
import com.bjcathay.qt.activity.MainActivity;
import com.bjcathay.qt.activity.MyWalletActivity;
import com.bjcathay.qt.activity.OrderDetailActivity;
import com.bjcathay.qt.activity.WelcomeActivity;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.PushModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import java.util.List;

/**
 * Created by dengt on 15-5-22.
 */
public class MessageReceiver extends BroadcastReceiver {

    private static int ids = 0;

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
                    PushModel pushModel = JSONUtil.load(PushModel.class, data);
                    if (pushModel != null) {
                        handlePush(context, pushModel);
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

    private void handlePush(Context context, PushModel pushModel) {
        PreferencesUtils.putBoolean(context, PreferencesConstant.NEW_MESSAGE_FLAG, true);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Intent intent;
        if (isRunning(context)) {
            switch (pushModel.getT()) {
                case ORDER:
                    intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("id", Long.parseLong(pushModel.getG()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    break;
                case COMPETITION:
                    intent = new Intent(context, CompetitionDetailActivity.class);
                    intent.putExtra("id", Long.parseLong(pushModel.getG()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    break;
                case PROPERTY:
                    intent = new Intent(context, MyWalletActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    break;
                default:
                    intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    break;

            }
        } else {
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClass(context, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context, ids, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent).setTicker(pushModel.getM()).
                setContentTitle("7铁高尔夫").
                setContentText(pushModel.getM()).
                setSmallIcon(R.drawable.ic_launcher).
                setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND);
        Notification notification = builder.
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

}
