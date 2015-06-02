package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.MyFriendAdapter;
import com.bjcathay.qt.adapter.MyOrderAdapter;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.InviteListModel;
import com.bjcathay.qt.model.InviteModel;
import com.bjcathay.qt.model.OrderListModel;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.AutoListView;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by bjcathay on 15-5-20.
 */
public class MyFriendActivity extends Activity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, ICallback, View.OnClickListener {
    private Activity context;
    private MyFriendAdapter myFriendAdapter;
    private List<InviteModel> inviteModelList;
    private RelativeLayout myFriendTitle;
    private TopView topView;
    private AutoListView lstv;

    private int page = 1;
    private View empty;
    private ImageView emptyImg;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {

        topView = ViewUtil.findViewById(this, R.id.top_my_friend_layout);
        myFriendTitle = ViewUtil.findViewById(this, R.id.my_friend_title);
        topView.setTitleBackVisiable();
        topView.setTitleText("我的亲友团");
        inviteModelList = new ArrayList<InviteModel>();
        myFriendAdapter = new MyFriendAdapter(inviteModelList, this);
        lstv = (AutoListView) findViewById(R.id.my_friend_list);
        lstv.setAdapter(myFriendAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        LayoutInflater inflater = getLayoutInflater();
        empty = inflater.inflate(R.layout.listview_empty, null);
        FrameLayout.LayoutParams lpLl = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lpLl.gravity = Gravity.CENTER;
        empty.setLayoutParams(lpLl);
        emptyImg = (ImageView) empty.findViewById(R.id.list_image_empty);
        emptyText = (TextView) empty.findViewById(R.id.list_view_empty);
        emptyImg.setImageResource(R.drawable.ic_empty_no_firend);
        emptyText.setText(getString(R.string.empty_friend_text));
        ((ViewGroup)lstv.getParent()).addView(empty);
        lstv.setEmptyView(empty);

        //lstv.setListViewEmptyImage(R.drawable.ic_empty_no_firend);
        //lstv.setListViewEmptyMessage(getString(R.string.empty_friend_text));
    }

    private void initEvent() {
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            InviteListModel result = (InviteListModel) msg.obj;
            boolean hasNext = false;
            if (result != null && result.getUsers() != null && !result.getUsers().isEmpty()) {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        inviteModelList.clear();
                        inviteModelList.addAll(result.getUsers());
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        inviteModelList.addAll(result.getUsers());
                        break;
                }
                lstv.setResultSize(inviteModelList.size(), hasNext);
                myFriendAdapter.notifyDataSetChanged();
            } else {
                switch (msg.what) {
                    case AutoListView.REFRESH:
                        lstv.onRefreshComplete();
                        break;
                    case AutoListView.LOAD:
                        lstv.onLoadComplete();
                        break;
                }
                //  inviteModelList.clear();
                lstv.setResultSize(inviteModelList.size(), hasNext);

                myFriendTitle.setVisibility(View.GONE);
                myFriendAdapter.notifyDataSetChanged();
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
        InviteListModel.getInvite(page).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                if (lstv != null)
                    lstv.onRefreshComplete();
            }
        });
    }

    @Override
    public void call(Arguments arguments) {
        InviteListModel orderListModel = arguments.get(0);
        Message msg = handler.obtainMessage();
        if (page == 1)
            msg.what = AutoListView.REFRESH;
        else {
            msg.what = AutoListView.LOAD;
        }
        msg.obj = orderListModel;
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
}
