
package com.bjcathay.qt.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.ExchangeSucActivity;
import com.bjcathay.qt.activity.MyExchangeActivity;
import com.bjcathay.qt.activity.SendExchangeSucActivity;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.PropModel;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONObject;

/**
 * Created by bjcathay on 15-5-25.
 */
public class DialogExchFragment extends DialogFragment {
    public interface ExchangeResult {
        void exchangeResult(UserModel userModel, boolean isExchange);
    }

    private Context context;
    private PropModel items;
    private UserModel userModel;
    TextView note;
    TextView sure;
    TextView cancle;
    private String type;
    private int num;
    private ExchangeResult exchangeResult;
    private String phone;
    private Long id;
    private String name;
    private ShareModel shareModel;

    public DialogExchFragment() {
    }

    @SuppressLint("ValidFragment")
    public DialogExchFragment(Context context, ExchangeResult exchangeResult) {
        // super();

        this.context = context;
        this.exchangeResult = exchangeResult;
    }

    public void setItems(PropModel items, int num) {
        this.items = items;
        this.num = num;
    }

    public void setItems(UserModel items, String user, String phone, Long id, String name) {
        this.userModel = items;
        this.type = user;
        this.phone = phone;
        this.id = id;
        this.name = name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.myexchangeDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.dialog_exchange_fragment, container);
        note = ViewUtil.findViewById(rootView, R.id.dialog_exchange_note);
        sure = ViewUtil.findViewById(rootView, R.id.dialog_exchange_sure);
        cancle = ViewUtil.findViewById(rootView, R.id.dialog_exchange_cancel);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if (!"user".equals(type)) {
            if (items.getNeedAmount() > num) {
                note.setText(getString(R.string.dialog_cant_to_exchange_a_card));
                sure.setText("立即邀请");
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        if (shareModel == null)
                            ShareModel.share().done(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    shareModel = arguments.get(0);
                                    ShareUtil.getInstance().shareDemo(context, shareModel);
                                }
                            });
                        else
                            ShareUtil.getInstance().shareDemo(context, shareModel);
                    }

                });
            } else {
                note.setText(getString(R.string.dialog_to_exchange_a_card, items.getName(),
                        items.getNeedAmount()));
                sure.setText("确认兑换");
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        PropModel.getProp(items.getId()).done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                if (jsonObject.optBoolean("success")) {
                                    PropModel propModel = JSONUtil.load(PropModel.class,
                                            jsonObject.optJSONObject("prop"));
                                    UserModel.get().done(new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            UserModel userModel = arguments.get(0);
                                            exchangeResult.exchangeResult(userModel, true);
                                            dismiss();
                                        }
                                    });
                                    DialogUtil.showMessage("兑换成功");
//                                    Intent intent = new Intent(context, ExchangeSucActivity.class);
//                                    intent.putExtra("prop", propModel);
//                                    intent.putExtra("title", "兑换");
//                                    ViewUtil.startActivity(context, intent);
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
                        });
                    }
                });
            }
        } else {
            if (userModel == null) {
                note.setText(getString(R.string.dialog_cant_to_send_a_card, phone));
                sure.setText("立即邀请");
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        Uri uri = Uri.parse("smsto:" + phone);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                        sendIntent.putExtra("sms_body", "");
                        startActivity(sendIntent);
                    }
                });
            } else {
                note.setText(getString(R.string.dialog_sure_to_send_a_card, name, phone));
                sure.setText("确认赠送");
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        PropModel.sendProp(id, phone)
                                .done(new ICallback() {
                                    @Override
                                    public void call(Arguments arguments) {
                                        JSONObject jsonObject = arguments.get(0);
                                        if (jsonObject.optBoolean("success")) {
                                            exchangeResult.exchangeResult(userModel, true);
                                            Intent intent = new Intent(context,
                                                    MyExchangeActivity.class);
//                                            intent.putExtra("name", name);
//                                            intent.putExtra("phone", phone);
//                                            intent.putExtra("title", "赠送");
                                            ViewUtil.startActivity(context, intent);
                                            DialogUtil.showMessage("赠送成功");
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
                                }
                                );
                    }
                });

            }
        }
        return rootView;
    }
}
