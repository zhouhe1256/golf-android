package com.bjcathay.qt.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.qt.R;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * Created by dengt on 15-7-7.
 */
public class CancleInfoDialog extends Dialog {

    public interface CancleInfoDialogResult {
        void cancleResult(Long targetId, boolean isDelete);
    }

    private Long targetId;
    private String text;
    private CancleInfoDialogResult dialogResult;
    private TextView dialogCancel;
    private TextView dialogConfirm;
    private TextView dialogTitle;
    private String comfire;

    public CancleInfoDialog(Context context) {
        this(context, 0, "", null, null);
    }

    public CancleInfoDialog(Context context, int theme, String text, Long targetId,
                            CancleInfoDialogResult result) {
        super(context, theme);
        this.targetId = targetId;
        this.dialogResult = result;
        this.text = text;

    }
    public void onCancleListen(){

    }
    public CancleInfoDialog(Context context, int theme, String text, String comfire, Long targetId,
                            CancleInfoDialogResult result) {
        super(context, theme);
        this.targetId = targetId;
        this.dialogResult = result;
        this.text = text;
        this.comfire = comfire;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_info);
        dialogCancel = (TextView) findViewById(R.id.dialog_cancel);
        dialogConfirm = (TextView) findViewById(R.id.dialog_confirm);
        dialogTitle = (TextView) findViewById(R.id.dialog_content);
        dialogTitle.setText(text);
        if (!StringUtils.isEmpty(comfire)) {
            dialogConfirm.setText(comfire);
            dialogConfirm.setTextColor(Color.BLUE);
            dialogCancel.setTextColor(Color.BLUE);
        }
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResult.cancleResult(targetId, false);
                cancel();
            }
        });
        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResult.cancleResult(targetId, true);
                cancel();
            }
        });

    }
}