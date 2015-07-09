
package com.bjcathay.qt.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.bjcathay.qt.util.ViewUtil;

/**
 * Created by dengt on 15-6-10.
 */
public class DialogEventFragment extends DialogFragment {
    public interface EventResult {
        void exchangeResult(Long id, boolean isExchange);
    }

    private Context context;
    private EventResult exchangeResult;
    private String message;
    TextView note;
    TextView sure;
    private Long id;

    public DialogEventFragment() {
    }

    @SuppressLint("ValidFragment")
    public DialogEventFragment(Context context, Long id, String message, EventResult exchangeResult) {
        // super();
        this.id = id;
        this.message = message;
        this.context = context;
        this.exchangeResult = exchangeResult;
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
        note.setText(message);
        sure.setText("确认消耗");
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                exchangeResult.exchangeResult(id, true);
            }
        });
        return rootView;
    }
}
