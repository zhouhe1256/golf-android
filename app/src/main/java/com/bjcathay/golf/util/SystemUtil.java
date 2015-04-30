package com.bjcathay.golf.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by bjcathay on 15-4-30.
 */
public class SystemUtil {
    /**
     * 获取操作系统版本
     * */
    public static String getVersion() {
        String[] version = {"null", "null", "null", "null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0] = arrayOfString[2];//KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = Build.VERSION.RELEASE;// firmware version
        version[2] = Build.MODEL;//model
        version[3] = Build.DISPLAY;//system version
        return version[0]+version[1]+version[2]+version[3];
    }
    /**
     * 获取当前版本号
     * @param mContext
     * @return
     */
    public static String getCurrentVersionName(Context mContext){
        // 获取packagemanager的实例
        PackageManager packageManager =mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(),0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }
}
