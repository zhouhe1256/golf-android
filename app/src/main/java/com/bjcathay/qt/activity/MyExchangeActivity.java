
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.MyPropAdapter;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.PropListModel;
import com.bjcathay.qt.model.PropModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.AutoListView;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-4-29.
 */
public class MyExchangeActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback, View.OnClickListener {
    private MyPropAdapter myPropAdapter;
    private List<PropModel> eventModels;
    private TopView topView;
    private AutoListView lstv;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_exchange);
        GApplication.getInstance().setFlag(true);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_my_exchange_layout);
        topView.setTitleText("我的兑换");
        topView.setTitleBackVisiable();
        eventModels = new ArrayList<PropModel>();
        myPropAdapter = new MyPropAdapter(eventModels, this);

        lstv = (AutoListView) findViewById(R.id.my_exchange_list);
        lstv.setAdapter(myPropAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        lstv.setListViewEmptyImage(R.drawable.ic_empty_exchange);
        lstv.setListViewEmptyMessage(getString(R.string.empty_exchange_text));
    }

    private void initEvent() {

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            PropListModel result = (PropListModel) msg.obj;
            boolean hasNext = false;
            if (result != null && result.getProps() != null && !result.getProps().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        eventModels.clear();
                        eventModels.addAll(result.getProps());
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        eventModels.addAll(result.getProps());
                        break;
                }
                lstv.setResultSize(eventModels.size(), hasNext);
                myPropAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        break;
                }
                lstv.setResultSize(eventModels.size(), hasNext);
                myPropAdapter.notifyDataSetChanged();
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
        PropListModel.getMyProps().done(this).fail(new ICallback() {
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
        PropListModel propListModel = arguments.get(0);
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = AutoListView.REFRESH;
        else {
            msg.what = AutoListView.LOAD;
        }
        msg.obj = propListModel;
        handler.sendMessage(msg);
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
        GApplication.getInstance().setFlag(true);
        MessageReceiver.baseActivity=this;
        initData();
        MobclickAgent.onPageStart("我的兑换页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的兑换页面");
        MobclickAgent.onPause(this);
    }
}
