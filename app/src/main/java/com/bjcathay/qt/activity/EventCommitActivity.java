
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.BookListModel;
import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.model.EventModel;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.model.PackagePriceModel;
import com.bjcathay.qt.model.PriceModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ValidformUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-8-31.
 */
public class EventCommitActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private Context context;
    private Button sure;
    private TextView name;
    private TextView time;
    private EventModel eventModel;
    String hourSelect;
    private int number;
    private ImageView minas;
    private ImageView plus;
    private TextView fourPlus;
    private TextView price;
    private EditText phone;
    private EditText cName;
    private TextView palyerNames;
    private String players;
    private EditText commemt;
    private BookListModel bookListModel = new BookListModel();

   // private ProgressDialog dialog = null;
    private String contactName;
    private String contactPhone;
    private LinearLayout timeLinear;
    private TextView totalprice;
    private TextView spotPrice;
    private TextView payType;
    private String playName;
    private String playNote;
    private EditText playNameipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_commit);
        context = this;
        GApplication.getInstance().setFlag(false);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_order_commit_layout);
        name = ViewUtil.findViewById(this, R.id.commit_place_name);
        time = ViewUtil.findViewById(this, R.id.commit_place_date);
        minas = ViewUtil.findViewById(this, R.id.dialog_order_minas);
        plus = ViewUtil.findViewById(this, R.id.dialog_order_plus);
        fourPlus = ViewUtil.findViewById(this, R.id.dialog_order_sure_number_edit);
        price = ViewUtil.findViewById(this, R.id.dialog_order_sure_price);
        phone = ViewUtil.findViewById(this, R.id.dialog_order_sure_phone);
        cName = ViewUtil.findViewById(this, R.id.dialog_order_sure_name);
        sure = ViewUtil.findViewById(this, R.id.sure_order);
        palyerNames = ViewUtil.findViewById(this, R.id.select_player_names);
        timeLinear = ViewUtil.findViewById(this, R.id.time);
        totalprice = ViewUtil.findViewById(this, R.id.total_price);
        spotPrice = ViewUtil.findViewById(this, R.id.spotPrice);
        payType = ViewUtil.findViewById(this, R.id.pay_type);
        commemt = ViewUtil.findViewById(this, R.id.order_commit_comment);

        playNameipt = ViewUtil.findViewById(this, R.id.order_commit_player_names);
    }

    private void commitOrder() {
        contactPhone = phone.getText().toString().trim();
        contactName = cName.getText().toString().trim();
        playName = playNameipt.getText().toString().trim();
        playNote = commemt.getText().toString().trim();
        if (StringUtils.isEmpty(contactName)) {
            DialogUtil.showMessage("尚未填写联系人姓名");
            return;
        }
        if (StringUtils.isEmpty(contactPhone)) {
            DialogUtil.showMessage("尚未填写联系人电话");
            return;
        }
        if (!ValidformUtil.isMobileNo(contactPhone)) {
            DialogUtil.showMessage("请填写正确的手机号码");
            return;
        }
//        if (dialog == null) {
//            dialog = ProgressDialog.show(context, "", "正在提交订单，请稍等...", false);
//            dialog.setCanceledOnTouchOutside(false);// 创建ProgressDialog
//        }
        if(playNote.equals("备注：")){
            playNote=null;
        }else{
            playNote.replace("备注：","");
        }
        OrderModel
                .commitEventOrder("EVENT",eventModel.getId(), number, eventModel.getDate(), contactName,
                        contactPhone,
                        playName, playNote, null).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        if (jsonObject.optBoolean("success")) {
                            OrderModel orderModel = JSONUtil.load(OrderModel.class,
                                    jsonObject.optJSONObject("order"));
                         //   dialog.dismiss();
                            DialogUtil.showMessage("下单成功");
                            Intent intent = new Intent(context, OrderDetailActivity.class);
                            intent.putExtra("imageurl", orderModel.getImageUrl());
                            intent.putExtra("id", orderModel.getId());
                            ViewUtil.startActivity(context, intent);
                        } else {
                           // dialog.dismiss();
                            String errorMessage = jsonObject.optString("message");
                            if (!StringUtils.isEmpty(errorMessage))
                                DialogUtil.showMessage(errorMessage);
                            else {
                                int code = jsonObject.optInt("code");
                                DialogUtil.showMessage(ErrorCode.getCodeName(code));
                            }
                        }
                    }
                }
                ).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                       // dialog.dismiss();
                        DialogUtil.showMessage(getString(R.string.empty_net_text));
                    }
                }
                );
    }

    private void initEvent() {
        topView.setTitleText("填写赛事信息");
        topView.setTitleBackVisiable();
        sure.setOnClickListener(this);
        commemt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (commemt.getText().length() <= 4) {
                        commemt.setText("备注： ");
                        commemt.clearFocus();
                    } else {
                        commemt.requestFocus();
                    }
                }
                return false;
            }
        });
        timeLinear.setVisibility(View.GONE);
    }

    private void initData() {
        Intent intent = getIntent();
        eventModel = (EventModel) intent.getSerializableExtra("event");
        number = intent.getIntExtra("number", 1);
        hourSelect = intent.getStringExtra("hourSelect");
        name.setText(eventModel.getName());
        time.setText(DateUtil.stringToDateToOrderString(eventModel.getDate())+"-"+DateUtil.stringToDateToEventString(eventModel.getDate()));
        UserModel userModel1 = GApplication.getInstance().getUser();
        phone.setText(userModel1.getMobileNumber());
        cName.setText(userModel1.getRealName());
        BookModel bookModel = new BookModel();
        if (userModel1.getRealName() == null) {
            bookModel.setName(userModel1.getMobileNumber() + "(本人)");
        } else {
            bookModel.setName(userModel1.getRealName() + "(本人)");
        }
        bookModel.setPhone(userModel1.getMobileNumber());
        List<BookModel> bookModels = new ArrayList<BookModel>();
        bookModels.add(bookModel);
        bookListModel.setPersons(bookModels);
        players = JSONUtil.dump(bookListModel);
        palyerNames.setText(bookModel.getName());
        minas.setVisibility(View.VISIBLE);
        plus.setVisibility(View.VISIBLE);
        getEventPrice();
    }

    private void getEventPrice() {
        minas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number > 1) {
                    number--;
                    fourPlus.setText(number < 10 ? " " + number : number + "");
                    price.setText("总金额： ￥"
                            +
                            (int) Math
                                    .floor(eventModel.getPrice() * number));
                }
            }

        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number++;
                fourPlus.setText(number < 10 ? " " + number : number + "");
                price.setText("总金额： ￥"
                        +
                        (int) Math.floor(eventModel.getPrice() * number));
            }
        });
        fourPlus.setText(number < 10 ? " " + number : number + "");
        price.setText("总金额： ￥"
                +
                (int) Math.floor(eventModel.getPrice() * number));

    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.sure_order:
                commitOrder();
                break;
            case R.id.select_player_layout:
                intent = new Intent(this, SelectPlayerActivity.class);
                intent.putExtra("select", bookListModel);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GApplication.getInstance().setFlag(false);
        MessageReceiver.baseActivity = this;
        MobclickAgent.onPageStart("完善赛事订单页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("完善赛事订单页面");
        MobclickAgent.onPause(this);
    }
}
