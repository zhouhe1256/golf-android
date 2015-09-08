
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.Enumeration.MessageType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.MessageListModel;
import com.bjcathay.qt.model.MessageModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-7-23.
 */
public class MyMessageActivity extends Activity implements View.OnClickListener, ICallback,
        DeleteInfoDialog.DeleteInfoDialogResult, View.OnLongClickListener {
    private TopView topView;
    private LinearLayout myOrder;
    private LinearLayout myActivity;
    private LinearLayout myNotify;
    private LinearLayout myWallet;
    private LinearLayout empty;
    private ImageView orderStatus;
    private ImageView activityStatus;
    private ImageView notifyStatus;
    private ImageView walletStatus;

    private TextView orderTitle;
    private TextView activityTitle;
    private TextView notifyTitle;
    private TextView walletTitle;

    private TextView orderTime;
    private TextView activityTime;
    private TextView notifyTime;
    private TextView walletTime;

    private TextView orderContent;
    private TextView activityContent;
    private TextView notifyContent;
    private TextView walletContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        GApplication.getInstance().setFlag(false);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_my_message_layout);
        topView.setTitleBackVisiable();
        topView.setTitleText("我的消息");
        empty = ViewUtil.findViewById(this, R.id.empty_lin);
        myOrder = ViewUtil.findViewById(this, R.id.my_order_message);
        myActivity = ViewUtil.findViewById(this, R.id.my_activity_message);
        myNotify = ViewUtil.findViewById(this, R.id.my_notification_message);
        myWallet = ViewUtil.findViewById(this, R.id.my_wallet_message);

        orderStatus = ViewUtil.findViewById(this, R.id.my_order_status);
        activityStatus = ViewUtil.findViewById(this, R.id.my_activity_status);
        notifyStatus = ViewUtil.findViewById(this, R.id.my_notify_status);
        walletStatus = ViewUtil.findViewById(this, R.id.my_wallet_status);

        orderTime = ViewUtil.findViewById(this, R.id.order_message_time);
        activityTime = ViewUtil.findViewById(this, R.id.order_activity_time);
        notifyTime = ViewUtil.findViewById(this, R.id.order_notify_time);
        walletTime = ViewUtil.findViewById(this, R.id.order_wallet_time);

        orderTitle = ViewUtil.findViewById(this, R.id.order_message_title);
        activityTitle = ViewUtil.findViewById(this, R.id.order_activity_title);
        notifyTitle = ViewUtil.findViewById(this, R.id.order_notify_title);
        walletTitle = ViewUtil.findViewById(this, R.id.order_wallet_title);

        orderContent = ViewUtil.findViewById(this, R.id.order_message_content);
        activityContent = ViewUtil.findViewById(this, R.id.order_activity_content);
        notifyContent = ViewUtil.findViewById(this, R.id.order_notify_content);
        walletContent = ViewUtil.findViewById(this, R.id.order_wallet_content);
    }

    private void initEvent() {
        myOrder.setOnLongClickListener(this);
      //  myActivity.setOnLongClickListener(this);
        myNotify.setOnLongClickListener(this);
        myWallet.setOnLongClickListener(this);
    }

    private void initData() {
        MessageListModel.getMyMessage().done(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.my_order_message:
                intent = new Intent(this, MyOrderMessageActivity.class);
                ViewUtil.startActivity(this, intent);
                if(orderStatus.getVisibility()==View.VISIBLE){
                    MessageListModel.msgReadByType(MessageType.msgType.ORDER.name()).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            JSONObject jsonObject = arguments.get(0);
                            if (jsonObject.optBoolean("success")) {
                                orderStatus.setVisibility(View.GONE);
                            }
                        }
                    });
                }
              //  PreferencesUtils.putBoolean(this, PreferencesConstant.NEW_MESSAGE_FLAG, false);
                break;
            case R.id.my_activity_message:
                intent = new Intent(this, MyEventMessageActivity.class);
                ViewUtil.startActivity(this, intent);
                if(activityStatus.getVisibility()==View.VISIBLE){
                    MessageListModel.msgReadByType(MessageType.msgType.SYSTEM.name()).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            JSONObject jsonObject = arguments.get(0);
                            if (jsonObject.optBoolean("success")) {
                                activityStatus.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                // DialogUtil.showMessage("活动");
                break;
            case R.id.my_notification_message:
                intent = new Intent(this, MyNtfMessageActivity.class);
                ViewUtil.startActivity(this, intent);
                if(notifyStatus.getVisibility()==View.VISIBLE){
                    MessageListModel.msgReadByType(MessageType.msgType.NOTIFY.name()).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            JSONObject jsonObject = arguments.get(0);
                            if (jsonObject.optBoolean("success")) {
                                notifyStatus.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                // DialogUtil.showMessage("通知");
                break;
            case R.id.my_wallet_message:
                intent = new Intent(this, MyWalletMessageActivity.class);
                ViewUtil.startActivity(this, intent);
                if(walletStatus.getVisibility()==View.VISIBLE){
                MessageListModel.msgReadByType(MessageType.msgType.PROPERTY.name()).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        if (jsonObject.optBoolean("success")) {
                            walletStatus.setVisibility(View.GONE);
                        }
                    }
                });
            }
                // DialogUtil.showMessage("资产");
                break;
        }
    }

    MessageListModel messageListModel;

    @Override
    public void call(Arguments arguments) {
        messageListModel = arguments.get(0);
        if (messageListModel == null || messageListModel.getMessages().isEmpty())
            empty.setVisibility(View.VISIBLE);
        else {
            empty.setVisibility(View.GONE);
            for (MessageModel messageModel : messageListModel.getMessages()) {
                if (MessageType.msgType.ORDER.equals(messageModel.getType())) {
                    myOrder.setVisibility(View.VISIBLE);
                    orderContent.setText(messageModel.getName());
                    orderTime.setText(messageModel.getRelativeDate());
                    if (MessageType.msgReadType.UNREAD.equals(messageModel.getStatus())) {
                        orderStatus.setVisibility(View.VISIBLE);
                    } else
                        orderStatus.setVisibility(View.GONE);}
//                } else if (MessageType.msgType.COMPETITION.equals(messageModel.getType())) {
//                    myActivity.setVisibility(View.VISIBLE);
//                    activityContent.setText(messageModel.getName());
//                    activityTime.setText(messageModel.getRelativeDate());
//                    if (MessageType.msgReadType.UNREAD.equals(messageModel.getStatus())) {
//                        activityStatus.setVisibility(View.VISIBLE);
//                    } else
//                        activityStatus.setVisibility(View.GONE);
//                }
                else if (MessageType.msgType.NOTIFY.equals(messageModel.getType())) {
                    myNotify.setVisibility(View.VISIBLE);
                    notifyContent.setText(messageModel.getName());
                    notifyTime.setText(messageModel.getRelativeDate());
                    if (MessageType.msgReadType.UNREAD.equals(messageModel.getStatus())) {
                        notifyStatus.setVisibility(View.VISIBLE);
                    } else
                        notifyStatus.setVisibility(View.GONE);
                }
                else if (MessageType.msgType.PROPERTY.equals(messageModel.getType())) {
                    myWallet.setVisibility(View.VISIBLE);
                    walletContent.setText(messageModel.getName());
                    walletTime.setText(messageModel.getRelativeDate());
                    if (MessageType.msgReadType.UNREAD.equals(messageModel.getStatus())) {
                        walletStatus.setVisibility(View.VISIBLE);
                    } else
                        walletStatus.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            String type = null;
            if (targetId == 1l) {
                // 清空消息
                type = MessageType.msgType.ORDER.name();
            } else if (targetId == 2l) {
             //   type = MessageType.msgType.COMPETITION.name();
            } else if (targetId == 3l) {
                type = MessageType.msgType.NOTIFY.name();
            }
            else if (targetId == 4l) {
                type = MessageType.msgType.PROPERTY.name();
            }
            if (messageListModel != null && messageListModel.getMessages().size() > 0)
                MessageListModel.msgClearByType(type).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        if (jsonObject.optBoolean("success")) {
                            DialogUtil.showMessage("已清空消息");
                            reflesh();
                            // messageModels.clear();
                            // loadData(AutoListView.REFRESH);
                            // }
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
                        DialogUtil.showMessage(getString(R.string.empty_net_text));
                    }
                });
        }
    }

    @Override
    public boolean onLongClick(View view) {
        String info = null;
        long tag = 0l;
        switch (view.getId()) {

            case R.id.my_order_message:
                info = "确认清空订单消息";
                tag = 1l;
                break;
//            case R.id.my_activity_message:
//                info = "确认清空活动消息";
//                tag = 2l;
//                break;
            case R.id.my_notification_message:
                info = "确认清空通知消息";
                tag = 3l;
                break;
            case R.id.my_wallet_message:
                info = "确认清空资产消息";
                tag = 4l;
                break;
        }
        DeleteInfoDialog infoDialog = new DeleteInfoDialog(this,
                R.style.InfoDialog, info, tag,
                this);
        infoDialog.show();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        GApplication.getInstance().setFlag(false);
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("消息页面");
        MobclickAgent.onResume(this);
    }

    private void reflesh() {
        myOrder.setVisibility(View.GONE);
        myActivity.setVisibility(View.GONE);
        myNotify.setVisibility(View.GONE);
        myWallet.setVisibility(View.GONE);
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("消息页面");
        MobclickAgent.onPause(this);
    }
}
