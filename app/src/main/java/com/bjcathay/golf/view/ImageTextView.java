package com.bjcathay.golf.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bjcathay.golf.util.BitmapUtil;

/**
 * Created by dengt on 15-4-23.
 */
public class ImageTextView extends TextView {
    private Bitmap bitmap;
    private String text;
    Drawable d;

    public ImageTextView(Context context) {
        super(context);

    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setIconText(Context context,String text){
        text = this.getText().toString().substring(0, 1);
        bitmap = BitmapUtil.getIndustry(context, text);
        d = BitmapUtil.bitmapTodrawable(bitmap);
        this.setCompoundDrawables(d, null, null, null);
    }

}
