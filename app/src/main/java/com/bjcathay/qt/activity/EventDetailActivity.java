
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.EventModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.IsLoginUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengt on 15-8-31.
 */
public class EventDetailActivity extends Activity implements ICallback, View.OnClickListener {
    private TopView topView;
    private Long id;
    private EventModel eventModel;
    private String url;
    private WebView filmWebVIew;
    private TextView eventTime;
    private TextView eventAdd;
    private TextView eventPrice;
    private TextView eventNumber;
    private Button sinup;
    private ImageView imageView;
    private TextView flag;
    private ImageView imageadd;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
context=this;
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_competition_detail_layout);
        filmWebVIew = ViewUtil.findViewById(this, R.id.film_webview);
        eventTime = ViewUtil.findViewById(this, R.id.event_detail_time);
        eventAdd = ViewUtil.findViewById(this, R.id.event_detail_address);
        eventPrice = ViewUtil.findViewById(this, R.id.event_detail_price);
        eventNumber = ViewUtil.findViewById(this, R.id.event_detail_number);
        sinup = ViewUtil.findViewById(this, R.id.order_detail_now_pay);
        imageView = ViewUtil.findViewById(this, R.id.event_detail_img);
        flag = ViewUtil.findViewById(this, R.id.event_detail_flag);
        imageadd=ViewUtil.findViewById(this,R.id.golfcourse_map);
    }

    private void initEvent() {
        topView.setTitleText("加载中");
        topView.setTitleBackVisiable();
        imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BaiduAddressActivity.class);
                intent.putExtra("url", getString(R.string.course_address));
                intent.putExtra("location", eventModel.getLat() + "," + eventModel.getLon());
                intent.putExtra("lat", eventModel.getLat());
                intent.putExtra("lon", eventModel.getLon());
                intent.putExtra("title", eventModel.getName());
                intent.putExtra("content", eventModel.getAddress());
                intent.putExtra("src", "A|GOLF");
                ViewUtil.startActivity(context, intent);
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        url = intent.getStringExtra("url");
        if (id != 0) {
            EventModel.getEventDetail(id).done(this);

        }
    }

    @Override
    public void call(Arguments arguments) {
        eventModel = arguments.get(0);
        url = eventModel.getUrl();
        topView.setTitleText(eventModel.getName());
        eventTime.setText(DateUtil.stringEventString(eventModel.getDate()));
        eventAdd.setText(eventModel.getAddress());
        eventPrice.setText(Long.toString(eventModel.getPrice()) + "元("
                + eventModel.getPriceInclude() + ")");
        eventNumber.setText("仅限" + eventModel.getSignUpAmount() + "人");
        //todo 转成富文本
//        filmWebVIew.loadDataWithBaseURL(
//                null,
//                eventModel.getUrl()
//                        .replaceAll("font-size:.*pt;", "font-size:0pt;")
//                        .replaceAll("font-family:.*;", "font-family:;"),
//                "text/html", "UTF-8", null);
        filmWebVIew.loadUrl(url);
        ImageViewAdapter.adapt(imageView, eventModel.getImageUrl(), R.drawable.ic_default_user);

        if ("已结束".equals(eventModel.getStatusLabel())) {
            sinup.setVisibility(View.GONE);
            flag.setText("本场赛事已结束~");
        } else if ("即将开始".equals(eventModel.getStatusLabel())) {
            flag.setText("本场赛事即将开始~");
            sinup.setVisibility(View.GONE);
        } else {
            flag.setText("本场赛事正在报名~");
            sinup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.order_detail_now_pay:
                Intent intent = new Intent(this, EventCommitActivity.class);
                intent.putExtra("event", eventModel);
                IsLoginUtil.isLogin(this, intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MessageReceiver.baseActivity = this;
        MobclickAgent.onPageStart("赛事详情页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("赛事详情页面");
        MobclickAgent.onPause(this);
    }

}