package com.bjcathay.qt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.qt.activity.CompetitionDetailActivity;
import com.bjcathay.qt.activity.ExerciseActivity;
import com.bjcathay.qt.activity.OrderDetailActivity;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.PushModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-5-22.
 */
public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {

            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    Log.d("GetuiSdkDemo", "Got Payload:" + data);//{"type":"ORDER","target":33}
                    PushModel pushModel = JSONUtil.load(PushModel.class, data);
                    if (pushModel != null) {
                        handlePush(context, pushModel);
                    }
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                break;
            default:
                break;
        }
    }

    private void handlePush(Context context, PushModel pushModel) {
        PreferencesUtils.putBoolean(context, PreferencesConstant.NEW_MESSAGE_FLAG, true);
        //ORDER|COMPETITION,
        String type = pushModel.getType();
        String target = pushModel.getTarget();
        // Long columnId = pushProduct.getColumnId();
        if ("ORDER".equals(type)) {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("id", Long.valueOf(target));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ViewUtil.startActivity(context, intent);
        } else if ("COMPETITION".equals(type)) {
            Intent intent = new Intent(context, CompetitionDetailActivity.class);
            intent.putExtra("url", target);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ViewUtil.startActivity(context, intent);
        }

    }
}
