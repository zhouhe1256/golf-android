package com.bjcathay.qt.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.CompetitionDetailActivity;
import com.bjcathay.qt.activity.OrderDetailActivity;
import com.bjcathay.qt.model.PushModel;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

/**
 * Created by bjcathay on 15-5-22.
 */
public class MessageReceiver extends BroadcastReceiver {

    private static int ids = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                if (payload != null) {
                    String data = new String(payload);
                    LogUtil.d("GetuiSdkDemo", "Got Payload:" + data);//{"type":"ORDER","target":33}
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
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Intent intent = null;
        if ("ORDER".equals(pushModel.getT())) {
            intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("id", Long.parseLong(pushModel.getG()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if ("COMPETITION".equals(pushModel.getT())) {
            intent = new Intent(context, CompetitionDetailActivity.class);
            intent.putExtra("id",Long.parseLong(pushModel.getG()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context, ids, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.setTicker(pushModel.getM()).
                setContentTitle("7铁高尔夫").
                setContentText(pushModel.getM()).
                setSmallIcon(R.drawable.ic_launcher).
                build();

        notification.defaults = Notification.DEFAULT_SOUND;
        notification.flags = 0x00000010;
        mNotificationManager.notify(ids++, notification);
    }
}
