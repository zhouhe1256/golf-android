package com.bjcathay.golf.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.golf.R;
import com.bjcathay.golf.adapter.MyCompetitionAdapter;
import com.bjcathay.golf.adapter.MyMessageAdapter;
import com.bjcathay.golf.constant.ErrorCode;
import com.bjcathay.golf.model.EventListModel;
import com.bjcathay.golf.model.MessageListModel;
import com.bjcathay.golf.model.MessageModel;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.PreferencesConstant;
import com.bjcathay.golf.util.PreferencesUtils;
import com.bjcathay.golf.util.SystemUtil;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.AutoListView;
import com.bjcathay.golf.view.DeleteInfoDialog;
import com.bjcathay.golf.view.TopView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-4-29.
 */
public class MyMessageActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback, DeleteInfoDialog.DeleteInfoDialogResult {
    private Activity context;
    private MyMessageAdapter myMessageAdapter;
    private List<MessageModel> messageModels;
    private TopView topView;
    private AutoListView lstv;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        context=this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_my_message_layout);
        topView.setActivity(this);
        topView.setTitleText("我的消息");
        topView.setVisiable(View.VISIBLE,View.VISIBLE,View.VISIBLE);
        topView.setRightbtn("清空", 0);
        messageModels = new ArrayList<MessageModel>();
        myMessageAdapter = new MyMessageAdapter(messageModels, this);
        lstv = (AutoListView) findViewById(R.id.my_message_list);
        lstv.setAdapter(myMessageAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
    }

    private void initEvent() {
        topView.getRightbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹窗设置是否取消
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(context,
                        R.style.InfoDialog, "确认清空消息？",0l,MyMessageActivity.this);
                infoDialog.show();

            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // DialogUtil.hintMessage("选档期", PlaceListActivity.this);
               *//* Intent intent = new Intent(MyCompetitionActivity.this, OrderDetailActivity.class);
                ViewUtil.startActivity(MyCompetitionActivity.this, intent);*//*
            }
        });*/

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            MessageListModel result = (MessageListModel) msg.obj;
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
                lstv.setResultSize(result.getMessages().size());
                myMessageAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        break;
                }
                lstv.setResultSize(0);
                myMessageAdapter.notifyDataSetChanged();
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
        String lastUpdate= PreferencesUtils.getString(this,PreferencesConstant.LAST_UPDATE_MESSAGE,"1970-11-11 00:00:00");
        MessageListModel.getMyMessage(page, lastUpdate).done(this);
    }

    @Override
    public void call(Arguments arguments) {
        MessageListModel messageListModel = arguments.get(0);
        PreferencesUtils.putString(this,PreferencesConstant.LAST_UPDATE_MESSAGE,SystemUtil.getCurrentTime());
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

                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < messageModels.size(); i++) {
                    if (i < messageModels.size() - 1)
                        buffer.append(messageModels.get(i).getId() + ",");
                    else
                        buffer.append(messageModels.get(i).getId());
                }

            MessageListModel.deleteMessages(buffer.toString()).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        DialogUtil.showMessage("已清空消息");
                        loadData(AutoListView.REFRESH);
                        // }
                    } else {
                        int code = jsonObject.optInt("code");
                        DialogUtil.showMessage(ErrorCode.getCodeName(code));
                    }
                }
            }).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    DialogUtil.showMessage("失败");
                }
            });
        }
    }
}