
package com.bjcathay.qt.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.OrderSucActivity;
import com.bjcathay.qt.activity.OrderSucTEActivity;
import com.bjcathay.qt.activity.SelectPayWayActivity;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.PreferencesConstant;
import com.bjcathay.qt.util.PreferencesUtils;
import com.bjcathay.qt.util.ViewUtil;
import com.ta.utdid2.android.utils.StringUtils;
import org.json.JSONObject;

/**
 * Created by dengt on 15-4-27.
 */
public class DialogSureOrderFragment extends DialogFragment {
    private Context context;
    private ProductModel stadiumModel;
    private TextView name;
    private TextView time;
    private TextView phone;
    private TextView service;
    private TextView number_;
    private TextView price;
    private Button sure;
    private String date;
    private int number;
    private LinearLayout linearLayout;
    private ImageView minas;
    private ImageView plus;
    private TextView fourPlus;
    private ProgressDialog dialog = null;
    private int currentPrice;

    public DialogSureOrderFragment() {
    }

    @SuppressLint("ValidFragment")
    public DialogSureOrderFragment(Context context, ProductModel stadiumModel, int currentPrice,
            String date, int number) {
        // super();
        this.date = date;
        this.number = number;
        this.context = context;
        this.stadiumModel = stadiumModel;
        this.currentPrice = currentPrice;
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
        if ("LIMIT".equals(stadiumModel.getType()) || "NONE".equals(stadiumModel.getType())) {
            rootView = inflater.inflate(R.layout.dialog_order_sure, container);
        } else if ("SPECIAL".equals(stadiumModel.getType())) {
            rootView = inflater.inflate(R.layout.dialog_temai_order_sure, container);

        } else if ("GROUP".equals(stadiumModel.getType())) {
            rootView = inflater.inflate(R.layout.dialog_tuan_order_sure, container);

        }
        linearLayout = ViewUtil.findViewById(rootView, R.id.dialog_order_layout);
        minas = ViewUtil.findViewById(rootView, R.id.dialog_order_minas);
        plus = ViewUtil.findViewById(rootView, R.id.dialog_order_plus);
        fourPlus = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_number_edit);

        name = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_name);
        time = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_time);
        phone = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_phone);
        service = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_service);
        number_ = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_number);
        price = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_price);
        sure = ViewUtil.findViewById(rootView, R.id.sure_order);
        name.setText(stadiumModel.getName());

        time.setText(DateUtil.stringToDateToOrderString(date));
        // 手机号码
        phone.setText("" + PreferencesUtils.getString(context, PreferencesConstant.USER_PHONE, ""));
        service.setText("" + stadiumModel.getPriceInclude());

        if (number == 0) {
            number_.setVisibility(View.GONE);
            number_.setText("" + 4 + "人+");
            number = 5;
            price.setText("￥" + currentPrice * number);
            linearLayout.setVisibility(View.VISIBLE);
            minas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (number > 5) {
                        number--;
                        fourPlus.setText(number < 10 ? " " + number : number + "");
                        price.setText("￥" + currentPrice * number);
                    }
                }
            });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    number++;
                    fourPlus.setText(number < 10 ? " " + number : number + "");
                    price.setText("￥" + currentPrice * number);
                }
            });

        } else {
            number_.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            price.setText("￥" + currentPrice * number + "");
            number_.setText("" + number + "人");
        }
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClickUtil.isFastClick()) {
                    return;
                }
                if (dialog == null) {
                    dialog = ProgressDialog.show(context, "", "正在提交订单，请稍等...", false);
                    dialog.setCanceledOnTouchOutside(false);// 创建ProgressDialog
                }
                OrderModel.commitOrder(stadiumModel.getId(), number, date).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        if (jsonObject.optBoolean("success")) {
                            OrderModel orderModel = JSONUtil.load(OrderModel.class,
                                    jsonObject.optJSONObject("order"));
                            dismiss();
                            Intent intent = null;
                            dialog.dismiss();
                            if ("LIMIT".equals(stadiumModel.getType())
                                    || "NONE".equals(stadiumModel.getType())) {
                                intent = new Intent(context, OrderSucActivity.class);
                            } else if ("SPECIAL".equals(stadiumModel.getType())) {
                                intent = new Intent(context, OrderSucTEActivity.class);
                            } else if ("GROUP".equals(stadiumModel.getType())) {
                                intent = new Intent(context, SelectPayWayActivity.class);
                                intent.putExtra("order", orderModel);
                            }
                            intent.putExtra("id", orderModel.getId());
                            ViewUtil.startActivity(context, intent);
                        } else {
                            dismiss();
                            dialog.dismiss();
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
                                dialog.dismiss();
                                dismiss();
                                DialogUtil.showMessage("下单失败");
                            }
                        }
                        );
            }
        }
                );
        return rootView;
    }
}
