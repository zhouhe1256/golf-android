
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.TopView;
import com.bjcathay.qt.view.VerScrollView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-8-12.
 */
public class RealTOrderActivity extends Activity implements View.OnClickListener, ICallback,
        DeleteInfoDialog.DeleteInfoDialogResult {
    private TopView topView;
    private Context context;
    private GApplication gApplication;
    private ImageView imageView;
    private TextView stadiumContents;
    private TextView stadiumAddress;
    private TextView stadiumPrice;
    private Button okbtn;
    private ProductModel stadiumModel;
    private Long id;
    private String imaUrl;
    private VerScrollView verScrollView;
    private LinearLayout proOffline;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);
        gApplication = GApplication.getInstance();
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_schedule_layout);
        topView.setTitleBackVisiable();
        imageView = ViewUtil.findViewById(this, R.id.place_img);
        stadiumAddress = ViewUtil.findViewById(this, R.id.sch_address);
        stadiumContents = ViewUtil.findViewById(this, R.id.sch_content);
        stadiumPrice = ViewUtil.findViewById(this, R.id.sch_price);
        verScrollView = ViewUtil.findViewById(this, R.id.verscrollview);
        proOffline = ViewUtil.findViewById(this, R.id.pro_offline);
        okbtn = ViewUtil.findViewById(this, R.id.ok);
        topView.setTitleText("加载中");
    }

    private void initEvent() {
        okbtn.setOnClickListener(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GolfCourseDetailActicity.class);
                intent.putExtra("id", stadiumModel.getGolfCourseId());
                ViewUtil.startActivity(context, intent);
            }
        });
        stadiumAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stadiumModel != null) {
                    Intent intent = new Intent(context, BaiduAddressActivity.class);
                    intent.putExtra("url", getString(R.string.course_address));
                    intent.putExtra("location", stadiumModel.getLat() + "," + stadiumModel.getLon());
                    intent.putExtra("lat", stadiumModel.getLat());
                    intent.putExtra("lon", stadiumModel.getLon());
                    intent.putExtra("title", stadiumModel.getName());
                    intent.putExtra("content", stadiumModel.getAddress());
                    intent.putExtra("src", "A|GOLF");
                    ViewUtil.startActivity(context, intent);
                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        imaUrl = intent.getStringExtra("imageurl");
        name=intent.getStringExtra("name");
        if (imaUrl != null)
            ImageViewAdapter.adapt(imageView, imaUrl, R.drawable.exchange_default);
        ProductModel.product(id).done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(getString(R.string.empty_net_text));
            }
        });
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
            case R.id.ok:
               // if (gApplication.isLogin() == true) {
                    DeleteInfoDialog infoDialog = new DeleteInfoDialog(this,
                            R.style.InfoDialog, getResources()
                                    .getString(R.string.service_tel_format)
                                    .toString().trim(), "呼叫", 0l, this);
                    infoDialog.show();
//
//                } else {
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    ViewUtil.startActivity(context, intent);
//                }
                break;
        }
    }

    public void setTextDate() {
        if (imaUrl == null)
            ImageViewAdapter.adapt(imageView, stadiumModel.getImageUrl(),
                    R.drawable.exchange_default);
        topView.setTitleText(stadiumModel.getName());
        stadiumContents.setText(stadiumModel.getPriceInclude());
        stadiumAddress.setText(stadiumModel.getAddress());
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        if (jsonObject.optBoolean("success")) {
            stadiumModel = JSONUtil.load(ProductModel.class, jsonObject.optJSONObject("product"));
            setTextDate();
        } else {
            String errorMessage = jsonObject.optString("message");
            int code = jsonObject.optInt("code");
            if (code == 13005) {
                verScrollView.setVisibility(View.GONE);
                okbtn.setVisibility(View.GONE);
                proOffline.setVisibility(View.VISIBLE);
                topView.setTitleText(name);
            } else {
            if (!StringUtils.isEmpty(errorMessage))
                DialogUtil.showMessage(errorMessage);
            else {

                DialogUtil.showMessage(ErrorCode.getCodeName(code));
            }}
        }
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + getResources().getString(R.string.service_tel).toString().trim()));
            this.startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MessageReceiver.baseActivity=this;
        MobclickAgent.onPageStart("实时计价产品详情页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("实时计价产品详情页面");
        MobclickAgent.onPause(this);
    }
}
