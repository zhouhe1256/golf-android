package com.bjcathay.qt.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bjcathay on 15-4-23.
 */
public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 要画图形，最起码要有三个对象：
     * 1.颜色对象 Color
     * 2.画笔对象 Paint
     * 3.画布对象 Canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub

        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        //设置字体大小
        paint.setTextSize(100);
        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        //让画出的图形是空心的
        paint.setStyle(Paint.Style.FILL);
        //设置画出的线的 粗细程度
        paint1.setStrokeWidth(5);
        paint1.setTextSize(30);
        //画出一根线
        //canvas.drawLine(0, 0, 200, 200, paint);
        //canvas.drawLine(0, 0, 200, 200, paint);

        //画矩形
       // canvas.drawRect(200, 500, 300, 300, paint);

        //画圆
        canvas.drawCircle(30, 30, 30, paint);
        //画出字符串 drawText(String text, float x, float y, Paint paint)
        // y 是 基准线 ，不是 字符串的 底部
        canvas.drawText("x", 15, 45, paint1);
       // canvas.drawLine(0, 60, 500, 60, paint);

        //绘制图片
        //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 150, 150, paint);

        super.onDraw(canvas);
    }
}
