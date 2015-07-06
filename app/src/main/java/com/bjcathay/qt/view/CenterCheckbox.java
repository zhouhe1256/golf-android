package com.bjcathay.qt.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;

/**
 * Created by dengt on 15-7-6.
 */
public class CenterCheckbox extends CheckBox {
    public CenterCheckbox(Context context) {
        super(context);
    }

    public CenterCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterCheckbox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
   /* @Override
    protected void onDraw(Canvas canvas) {
        setButtonDrawable(parentds);      super.onDraw(canvas);
        setButtonDrawable(ds);//真正要显示的图片
        final Drawable buttonDrawable = ds;
        if (buttonDrawable != null) {
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int drawableHeight = buttonDrawable.getIntrinsicHeight()>selfHeight
                    ?selfHeight:buttonDrawable.getIntrinsicHeight();
            final int drawableWidth = buttonDrawable.getIntrinsicWidth()>selfHeight
                    ?selfHeight:buttonDrawable.getIntrinsicWidth();
            //<span style="font-family: Arial, Helvetica, sans-serif;">selfHeight为要定义的图片高度</span>
            int top = 0;
            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    top = getHeight() - drawableHeight;
                    break;
                case Gravity.CENTER_VERTICAL:
                    top = (getHeight() - drawableHeight) / 2;
                    break;
            }

            int left = 0;
            int right = left+drawableWidth;
            int bottom = top+drawableHeight;

            buttonDrawable.setBounds(left, top, right, bottom);
            buttonDrawable.draw(canvas);
        }
    }
    // drawable 转换成bitmap
    private Drawable occupyPosDrawable(int height){//
        Picture p=new Picture();
        Canvas c=p.beginRecording(height,height);
        p.endRecording();

        PictureDrawable pd=new PictureDrawable(p);

        return pd;
    }
    @Override
    public int getCompoundPaddingLeft() {
        int padding = super.getCompoundPaddingLeft();
        if(getAndroidSDKVersion()<=16){//4.1.2
            if (!isLayoutRtl()) {
                padding = selfHeight + padding;
            }
        }
        return padding;
    }*/

}
