package com.bjcathay.golf.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.golf.R;
import com.bjcathay.golf.activity.LoginActivity;
import com.bjcathay.golf.activity.RemindInfoActivity;
import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.constant.ErrorCode;
import com.bjcathay.golf.model.PlaceModel;
import com.bjcathay.golf.model.PropModel;
import com.bjcathay.golf.model.UserModel;
import com.bjcathay.golf.util.DialogUtil;
import com.bjcathay.golf.util.PreferencesConstant;
import com.bjcathay.golf.util.PreferencesUtils;
import com.bjcathay.golf.util.ViewUtil;
import com.bjcathay.golf.view.TopView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-5-6.
 */
public class ExchangeAdapter extends BaseAdapter {
    private GApplication gApplication;
    private List<PropModel> items;
    private Activity context;
    private TextView number;
    private int count = 0;

    public ExchangeAdapter(List<PropModel> items, Activity activity, TextView number) {
        if (items == null) {
            items = new ArrayList<PropModel>();
        }
        this.items = items;
        this.context = activity;
        this.number = number;
        gApplication = GApplication.getInstance();
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
        /*if (count == 0) {
            count = 10;
        }
        return count;*/
    }

    @Override
    public Object getItem(int i) {
        // return items.get(i);
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exchange_list, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final PropModel propModel = items.get(position);
        holder.title.setText(propModel.getName());
        ImageViewAdapter.adapt(holder.imageView, propModel.getImageUrl(), R.drawable.ic_launcher);
        holder.sale.setText(propModel.getDescription());
        holder.price.setText(propModel.getInviteUserCount() + "个有效用户");
       /* convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.hintMessage("选档期");
            }
        });*/
        int num;
        if (gApplication.isLogin() == true) {

            num = Integer.valueOf(number.getText().toString().trim());
        } else {
            num = 0;
        }
        //int num=Integer.valueOf(number.getText().toString().trim());
        if (Integer.valueOf(propModel.getInviteUserCount()) > num) {
            holder.toExch.setOnClickListener(null);

            holder.toExch.setBackgroundResource(R.drawable.btn_gray_bg);
        } else {
            holder.toExch.setBackgroundResource(R.drawable.ic_exchange_yellow);

            holder.toExch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final LayoutInflater inflater = context.getLayoutInflater();
                    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.dialog_exchange_award, null);
                    final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
                    TopView topView = ViewUtil.findViewById(rootView, R.id.dialog_award_title);
                    topView.setVisiable(View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                    topView.setRightbtn(null, R.drawable.ic_close_dialog);

                    Button sure = ViewUtil.findViewById(rootView, R.id.sure_to_exchange);
                    topView.getRightbtn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setContentView(rootView);
                    sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            if (gApplication.isLogin() == true) {
                                PropModel.getProp(propModel.getId()).done(new ICallback() {
                                    @Override
                                    public void call(Arguments arguments) {
                                        JSONObject jsonObject = arguments.get(0);
                                        if (jsonObject.optBoolean("success")) {
                                            PropModel propModel = JSONUtil.load(PropModel.class, jsonObject.optJSONObject("prop"));
                                            DialogUtil.showMessage("成功兑换一枚" + propModel.getName());
                                            UserModel.get().done(new ICallback() {
                                                @Override
                                                public void call(Arguments arguments) {
                                                    UserModel userModel = arguments.get(0);
                                                    number.setText("" + userModel.getInviteAmount());
                                                    PreferencesUtils.putString(gApplication, PreferencesConstant.VALIDATED_USER, userModel.getInviteAmount() + "");


                                                }
                                            });
                                            Intent intent = new Intent(context, RemindInfoActivity.class);
                                            intent.putExtra("prop", propModel);
                                            intent.putExtra("title", "兑换");
                                            ViewUtil.startActivity(context, intent);
                                        } else {
                                            int code = jsonObject.optInt("code");
                                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                        }
                                    }
                                });
                            } else {
                                Intent intent = new Intent(context, LoginActivity.class);
                                ViewUtil.startActivity(context, intent);
                            }
                        }
                    });
                    //dialog.create();
                    // dialog.setContentView(rootView);
                    dialog.show();
                }
            });
        }
        return convertView;
    }

    class Holder {
        ImageView imageView;
        TextView title;
        TextView price;
        TextView sale;
        Button toExch;

        public Holder(View view) {
            imageView = ViewUtil.findViewById(view, R.id.exchange_image);
            title = ViewUtil.findViewById(view, R.id.exchange_title);
            price = ViewUtil.findViewById(view, R.id.exchange_need_number);
            sale = ViewUtil.findViewById(view, R.id.exchange_note);
            toExch = ViewUtil.findViewById(view, R.id.to_exchange);
        }
    }
}
