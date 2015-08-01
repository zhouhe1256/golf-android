
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.Enumeration.MessageType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.MyNtfMessageAdapter;
import com.bjcathay.qt.adapter.MyOrderMessageAdapter;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.MessageListModel;
import com.bjcathay.qt.model.MessageModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.SystemUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.AutoListView;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-7-23.
 */
public class MyNtfMessageActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback, DeleteInfoDialog.DeleteInfoDialogResult,
        View.OnClickListener {
    private Activity context;
    private MyNtfMessageAdapter myOrderMessageAdapter;
    private List<MessageModel> messageModels;
    private TopView topView;
    private AutoListView lstv;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification_message);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_my_ntf_layout);
        topView.setTitleBackVisiable();
        topView.setTitleText("通知消息");
        messageModels = new ArrayList<MessageModel>();
        myOrderMessageAdapter = new MyNtfMessageAdapter(messageModels, this);
        lstv = (AutoListView) findViewById(R.id.my_ntf_list);
        lstv.setAdapter(myOrderMessageAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        lstv.setListViewEmptyImage(R.drawable.ic_empty_msg);
        lstv.setListViewEmptyMessage(getString(R.string.empty_msg_text));
    }

    private void initEvent() {
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            MessageListModel result = (MessageListModel) msg.obj;
            boolean hasNext = result.isHasNext();
            if (result != null && result.getMessages() != null && !result.getMessages().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        messageModels.clear();
                        messageModels.addAll(result.getMessages());
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        messageModels.addAll(result.getMessages());
                        break;
                }
                lstv.setResultSize(messageModels.size(), hasNext);
                myOrderMessageAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        break;
                }
                lstv.setResultSize(messageModels.size(), hasNext);
                myOrderMessageAdapter.notifyDataSetChanged();
            }
        }

        ;
    };

    private void initData() {
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onRefresh() {
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        loadData(AutoListView.LOAD);
    }

    private void loadData(final int what) {
        switch (what) {
            case AutoListView.REFRESH:
                page = 1;
                break;
            case AutoListView.LOAD:
                page++;
                break;
        }
        String lastUpdate = PreferencesUtils.getString(this,
                PreferencesConstant.LAST_UPDATE_MESSAGE, "1970-11-11 00:00:00");
        MessageListModel.getMyMessage(MessageType.msgType.NOTIFY.name()).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                if (lstv != null) {
                    lstv.onRefreshComplete();
                    lstv.setResultSize(-1, false);
                }
            }
        });
    }

    @Override
    public void call(Arguments arguments) {
        MessageListModel messageListModel = arguments.get(0);
        PreferencesUtils.putString(this, PreferencesConstant.LAST_UPDATE_MESSAGE,
                SystemUtil.getCurrentTime());
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = AutoListView.REFRESH;
        else {
            msg.what = AutoListView.LOAD;
        }
        msg.obj = messageListModel;
        handler.sendMessage(msg);
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            if (messageModels != null && messageModels.size() > 0)
                MessageListModel.deleteMessages().done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        if (jsonObject.optBoolean("success")) {
                            DialogUtil.showMessage("已清空消息");
                            messageModels.clear();
                            loadData(AutoListView.REFRESH);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("通知消息页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("通知消息页面");
        MobclickAgent.onPause(this);
    }
}
