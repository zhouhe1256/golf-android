package com.bjcathay.qt.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.bjcathay.qt.R;

/**
 * Created by bjcathay on 15-5-29.
 */
public class PhoneTextView extends TextView implements View.OnClickListener, DeleteInfoDialog.DeleteInfoDialogResult {
    private Context context;

    public PhoneTextView(Context context) {
        super(context);
        this.context = context;
        getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        setText(R.string.service_tel);
    }

    public PhoneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        setText(R.string.service_tel);
    }

    public PhoneTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        setText(R.string.service_tel);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call_phone:
                DeleteInfoDialog infoDialog = new DeleteInfoDialog(context,
                        R.style.InfoDialog, "呼叫" + getText().toString().trim() + "？", 0l, this);
                infoDialog.show();
                break;
        }
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getText().toString().trim()));
            context.startActivity(intent);
        }
    }
}
