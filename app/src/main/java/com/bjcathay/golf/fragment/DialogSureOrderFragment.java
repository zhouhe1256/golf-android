package com.bjcathay.golf.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.golf.R;
import com.bjcathay.golf.activity.OrderSucActivity;
import com.bjcathay.golf.activity.OrderSucTEActivity;
import com.bjcathay.golf.activity.OrderSucTuanActivity;
import com.bjcathay.golf.activity.RemindInfoActivity;
import com.bjcathay.golf.model.OrderModel;
import com.bjcathay.golf.model.StadiumModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.PreferencesConstant;
import com.bjcathay.golf.util.PreferencesUtils;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

/**
 * Created by dengt on 15-4-27.
 */
public class DialogSureOrderFragment extends DialogFragment {
    private Context context;
    private StadiumModel stadiumModel;
    TopView topView1;
    TextView name;
    TextView time;
    TextView phone;
    TextView service;
    TextView number_;
    TextView price;
    Button sure;
    String date;
    int number;

    public DialogSureOrderFragment() {
    }

    @SuppressLint("ValidFragment")
    public DialogSureOrderFragment(Context context, StadiumModel stadiumModel, String date, int number) {
        // super();
        this.date = date;
        this.number = number;
        this.context = context;
        this.stadiumModel = stadiumModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.myDialogTheme);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        if ("COMMON".equals(stadiumModel.getType())) {
            rootView = inflater.inflate(R.layout.dialog_order_sure, container);
        } else if ("LIMITED".equals(stadiumModel.getType())) {
            rootView = inflater.inflate(R.layout.dialog_temai_order_sure, container);

        } else if ("SPECIAL".equals(stadiumModel.getType())) {
            rootView = inflater.inflate(R.layout.dialog_tuan_order_sure, container);

        }
        topView1 = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_title);
        topView1.setVisiable(View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
        topView1.setRightbtn("X", R.drawable.ic_close_dialog);

        name = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_name);
        time = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_time);
        phone = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_phone);
        service = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_service);
        number_ = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_number);
        price = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_price);
        sure = ViewUtil.findViewById(rootView, R.id.sure_order);
        name.setText(stadiumModel.getName());
        time.setText(date);
        //手机号码
        phone.setText("" + PreferencesUtils.getString(context, PreferencesConstant.USER_PHONE, ""));
        service.setText("" + stadiumModel.getPriceInclude());
        number_.setText("" + number + "人");

        price.setText("￥" + Double.valueOf(stadiumModel.getPrice()) * number + "");
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                OrderModel.commitOrder(stadiumModel.getId(), number, date).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        //JSONObject jsonObject = arguments.get(0);
                        OrderModel orderModel = arguments.get(0);
                        Intent intent = null;
                        if ("COMMON".equals(stadiumModel.getType())) {
                            intent = new Intent(context, OrderSucActivity.class);
                        } else if ("LIMITED".equals(stadiumModel.getType())) {
                            intent = new Intent(context, OrderSucTEActivity.class);
                        } else if ("SPECIAL".equals(stadiumModel.getType())) {
                            intent = new Intent(context, OrderSucTuanActivity.class);
                        }
                        intent.putExtra("id", orderModel.getId());
                        ViewUtil.startActivity(context, intent);
                    }
                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        DialogUtil.showMessage("下单失败");
                    }
                });


            }
        });
        topView1.getRightbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        // textView= (TextView) view.findViewById(R.id.text_1);
        return rootView;
    }
}
