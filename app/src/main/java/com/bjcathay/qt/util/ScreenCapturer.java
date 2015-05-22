package com.bjcathay.qt.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.bjcathay.android.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;

public class ScreenCapturer {
    private static File baseDir;

    public static void setBaseDir(File baseDir) {
        ScreenCapturer.baseDir = baseDir;
    }

    // 获取指定Activity的截屏，保存到png文件
    public Bitmap capture(Activity activity) {
        //View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        //获取状态栏高度
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        //获取屏幕长和高
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        //适应微信
        int expectedHeight = width;
        int height = Math.min(dm.heightPixels, expectedHeight);

        //去掉标题栏
        bitmap = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height - statusBarHeight);

        //int destWidth = (int) ((480 * 1.0 / height) * width);
        //bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, 480, false);

        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public File captureToFile(Activity activity) {
        Bitmap bitmap = capture(activity);
        File dir = new File(baseDir, "screenshots");
        dir.mkdirs();
        File file = new File(dir, System.currentTimeMillis() + ".jpg");

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            return file;
        } catch (Exception e) {
            Log.w("ScreenCapturer", "save screen shot failed", e);
            return null;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

}
