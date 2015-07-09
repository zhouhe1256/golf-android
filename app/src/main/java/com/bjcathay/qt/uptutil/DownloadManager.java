
package com.bjcathay.qt.uptutil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.qt.model.UpdateModel;

import java.text.SimpleDateFormat;

/**
 * 下载管理
 */
public class DownloadManager {

    private Context mContext;

    final static int CHECK_FAIL = 0;
    final static int CHECK_SUCCESS = 1;
    final static int CHECK_NOUPGRADE = 2;
    final static int CHECK_NETFAIL = 3;

    private ApkInfo apkinfo;
    private AlertDialog noticeDialog; // 提示弹出框
    private ProgressDialog progressDialog;
    private String verName;

    private boolean isAccord; // 是否主动检查软件升级
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DownloadManager(Context mContext, boolean isAccord) {
        this.mContext = mContext;
        this.isAccord = isAccord;
        verName = IntentUtil.getCurrentVersionName(mContext);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            switch (msg.what) {
                case CHECK_SUCCESS: {
                    showNoticeDialog();
                    break;
                }
                case CHECK_NOUPGRADE: { // 不需要更新
                    if (isAccord)
                        Toast.makeText(mContext, "当前版本是最新版。", Toast.LENGTH_SHORT).show();
                    break;
                }
                case CHECK_NETFAIL: {
                    if (isAccord)
                        Toast.makeText(mContext, "网络连接不正常。", Toast.LENGTH_SHORT).show();
                    break;
                }
                case CHECK_FAIL: {
                    if (isAccord)
                        Toast.makeText(mContext, "从服务器获取更新数据失败。", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        };
    };

    /* 检查下载更新 [apk下载入口] */
    public void checkDownload() {
        if (isAccord)
            progressDialog = ProgressDialog.show(mContext, "", "正在检查更新内容...");
        // progressDialog.setCanceledOnTouchOutside(true);
        new Thread() {
            @Override
            public void run() {
                if (!IntentUtil.isConnect(mContext)) { // 检查网络连接是否正常
                    handler.sendEmptyMessage(CHECK_NETFAIL);
                } else if (/* checkTodayUpdate() || */isAccord) {// 判断今天是否已自动检查过更新
                                                                 // ；如果手动检查更新，直接进入
                    // String result =
                    // HttpRequestUtil.getSourceResult(Const.apkCheckUpdateUrl,
                    // null, mContext);

                    try {
                        UpdateModel.sendVersion().done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                try {
                                    UpdateModel updateModel = arguments.get(0);
                                    double version = updateModel.getVersion();
                                    String downurl = updateModel.getUrl();
                                    String description = updateModel.getDescription();
                                    apkinfo = new ApkInfo(downurl, version, null, 0, null,
                                            description);
                                    if (apkinfo != null && checkApkVercode()) {// 检查版本号
                                        // alreayCheckTodayUpdate();
                                        // //设置今天已经检查过更新
                                        handler.sendEmptyMessage(CHECK_SUCCESS);
                                    } else {
                                        handler.sendEmptyMessage(CHECK_NOUPGRADE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(CHECK_FAIL);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(CHECK_FAIL);
                    }
                }
            }
        }.start();
    }

    public void checkDownload(boolean flag) {
        // if(isAccord) progressDialog = ProgressDialog.show(mContext, "",
        // "正在检查更新内容...");
        // progressDialog.setCanceledOnTouchOutside(true);
        new Thread() {
            @Override
            public void run() {
                if (!IntentUtil.isConnect(mContext)) { // 检查网络连接是否正常
                    handler.sendEmptyMessage(CHECK_NETFAIL);
                } else if (/* checkTodayUpdate() || */isAccord) {// 判断今天是否已自动检查过更新
                                                                 // ；如果手动检查更新，直接进入
                    // String result =
                    // HttpRequestUtil.getSourceResult(Const.apkCheckUpdateUrl,
                    // null, mContext);

                    try {
                        UpdateModel.sendVersion().done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                try {
                                    UpdateModel updateModel = arguments.get(0);
                                    double version = updateModel.getVersion();
                                    String downurl = updateModel.getUrl();
                                    String description = updateModel.getDescription();
                                    apkinfo = new ApkInfo(downurl, version, null, 0, null,
                                            description);
                                    if (apkinfo != null && checkApkVercode()) {// 检查版本号
                                        // alreayCheckTodayUpdate();
                                        // //设置今天已经检查过更新
                                        handler.sendEmptyMessage(CHECK_SUCCESS);
                                    } else {
                                        // handler.sendEmptyMessage(CHECK_NOUPGRADE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // handler.sendEmptyMessage(CHECK_FAIL);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        // handler.sendEmptyMessage(CHECK_FAIL);
                    }
                }
            }
        }.start();
    }

    /* 弹出软件更新提示对话框 */
    private void showNoticeDialog() {
        StringBuffer sb = new StringBuffer();
        sb/*
           * .append("版本号："+apkinfo.getApkVersion()+"\n")
           * .append("文件大小："+apkinfo.getApkSize()+"\n")
           */
                .append("更新日志：\n" + apkinfo.getApkLog());
        Builder builder = new Builder(mContext);
        builder.setTitle("版本更新").setMessage(sb.toString());
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String apkPath = Const.apkSavepath + "qtgolf.apk";
                DownloadCallback downCallback = new DownloadInstall(mContext, apkPath, apkinfo
                        .getApkVersion(), apkinfo.getApkCode());
                DownloadAsyncTask request = new DownloadAsyncTask(downCallback);
                request.execute(apkinfo.getDownloadUrl(), apkPath);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        // noticeDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        // //设置最顶层Alertdialog
        noticeDialog.show();
    }

    /**
     * 检查版本是否需要更新
     * 
     * @return
     */
    private boolean checkApkVercode() {
        /*
         * SharedPreferences sharedPreference =
         * mContext.getSharedPreferences(UpdateShared.SETTING_UPDATE_APK_INFO,
         * 0); int verCode = sharedPreference.getInt(UpdateShared.APK_VERCODE,
         * 0);
         */
        double versionname = Double.valueOf(verName);
        if (apkinfo.getApkVersion() > versionname) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * static interface UpdateShared{ String SETTING_UPDATE_APK_INFO =
     * "cbt_upgrade_setting"; String UPDATE_DATE = "updatedate"; String
     * APK_VERSION = "apkversion"; String APK_VERCODE = "apkvercode"; String
     * CHECK_DATE = "checkdate"; }
     */
}
