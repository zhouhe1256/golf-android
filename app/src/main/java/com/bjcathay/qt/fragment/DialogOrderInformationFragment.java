
package com.bjcathay.qt.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.OrderCommitActivity;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.ViewUtil;

/**
 * Created by dengt on 15-7-2.
 */
public class DialogOrderInformationFragment extends DialogFragment {
    private Context context;
    private ProductModel stadiumModel;
    Button sure;
    TextView name;
    TextView time;
    TextView note;
    String date;
    int number;
    private int currentPrice;

    public DialogOrderInformationFragment() {
    }

    @SuppressLint("ValidFragment")
    public DialogOrderInformationFragment(Context context, ProductModel stadiumModel,
            int currentPrice, String date, int number) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.dialog_show_order_message, container);
        name = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_name);
        time = ViewUtil.findViewById(rootView, R.id.dialog_order_sure_time);
        note=ViewUtil.findViewById(rootView,R.id.order_need_know_note1);
        name.setText(stadiumModel.getName());
        note.setText(stadiumModel.getPurchasingNotice());
        time.setText(DateUtil.stringToDateToOrderString(date));
        sure = ViewUtil.findViewById(rootView, R.id.sure_order);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderCommitActivity.class);
                intent.putExtra("product", stadiumModel);
                intent.putExtra("date", date);
                intent.putExtra("number", number);
                intent.putExtra("currentPrice", currentPrice);
                ViewUtil.startActivity(context, intent);
                dismiss();
            }
        });
        return rootView;
    }
}
