
package com.bjcathay.qt.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.qt.Enumeration.MessageType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.PushModel;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-8-14.
 */
public class PushInfoDialog extends Dialog {
    private static List<PushInfoDialog> lastInstance = new ArrayList<PushInfoDialog>();
    private static PushInfoDialog instance = null;

    public interface PushResult {
        void pushResult(PushModel pushModel, boolean isDelete, Context context);
    }

    private PushModel pushModel;
    private String text;
    private PushResult dialogResult;
    private TextView dialogCancel;
    private TextView dialogConfirm;
    private TextView dialogTitle;
    private String comfire;
    private Context context;

    public PushInfoDialog(Context context) {
        this(context, 0, "", null, null);
    }

    public PushInfoDialog(Context context, int theme, String text, PushModel pushModel,
            PushResult result) {
        super(context, theme);
        this.pushModel = pushModel;
        this.dialogResult = result;
        this.context = context;
        this.text = text;

    }

    public static synchronized PushInfoDialog getInstance(Context context, int theme, String text,
            String comfire, PushModel pushModel,
            PushResult result) {
        if (lastInstance.size() > 0) {
            lastInstance.remove(0).dismiss();
        }
        instance = new PushInfoDialog(context, theme, text, comfire, pushModel,
                result);
        lastInstance.add(instance);
        return instance;
    }

    public void onCancleListen() {

    }

    public PushInfoDialog(Context context, int theme, String text, String comfire,
            PushModel pushModel,
            PushResult result) {
        super(context, theme);
        this.pushModel = pushModel;
        this.dialogResult = result;
        this.text = text;
        this.comfire = comfire;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_info);
        dialogCancel = (TextView) findViewById(R.id.dialog_cancel);
        dialogConfirm = (TextView) findViewById(R.id.dialog_confirm);
        dialogTitle = (TextView) findViewById(R.id.dialog_content);
       // dialogTitle.setMovementMethod(ScrollingMovementMethod.getInstance());
        dialogTitle.setText(text);
        if (pushModel != null)
            if (MessageType.pushMsgType.MESSAGE.equals(pushModel.getT())) {
                dialogCancel.setVisibility(View.GONE);
                dialogConfirm.setText("确认");
            } else {
                dialogCancel.setVisibility(View.VISIBLE);
                dialogConfirm.setText("查看");
            }
        if (!StringUtils.isEmpty(comfire)) {
            dialogCancel.setText(comfire);
            // dialogCancel.setTextColor(Color.BLUE);
            // dialogCancel.setTextColor(Color.BLUE);
        }
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResult.pushResult(pushModel, false, context);
                cancel();
            }
        });
        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResult.pushResult(pushModel, true, context);
                cancel();
            }
        });

    }
}
