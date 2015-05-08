package com.bjcathay.golf.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bjcathay.golf.R;

/**
 * Created by dengt on 15-4-27.
 */
public class DialogSureOrderFragment extends DialogFragment {
    Button btn;
    TextView textView;
    public DialogSureOrderFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.myDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_order_sure, container);
        btn = (Button) view.findViewById(R.id.sure_order);
       // textView= (TextView) view.findViewById(R.id.text_1);
        return view;
    }
}
