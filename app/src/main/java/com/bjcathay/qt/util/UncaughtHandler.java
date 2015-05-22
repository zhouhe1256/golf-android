package com.bjcathay.qt.util;

import android.content.Context;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.util.FileUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UncaughtHandler implements Thread.UncaughtExceptionHandler {

    private static UncaughtHandler uncaughtHandler = null;
    private File logFile;
    private String uploadUrl;
    private boolean upload = false;
    private Context context;

    private FileNameRule fileNameRule = new FileNameRule() {
        @Override
        public String buildName() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault());
            return sdf.format(new Date());
        }
    };

    public interface FileNameRule {
        String buildName();
    }

    private UncaughtHandler(Context context) {
        this.context = context;
    }

    public static UncaughtHandler getInstance(Context context) {
        if (uncaughtHandler == null) {
            uncaughtHandler = new UncaughtHandler(context);
            Thread.setDefaultUncaughtExceptionHandler(uncaughtHandler);
        }
        return uncaughtHandler;
    }

    public void init(Context context, File logFile) {
        this.context = context;
        this.logFile = logFile;
    }

    public void init(Context context, File logFile, String uploadUrl) {
        this.context = context;
        this.logFile = logFile;
        this.upload = true;
        this.uploadUrl = uploadUrl;
    }


    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public void setFileNameRule(FileNameRule fileNameRule) {
        this.fileNameRule = fileNameRule;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        PrintStream printStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            printStream = new PrintStream(byteArrayOutputStream);
            ex.printStackTrace(printStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            writeErrorLog(new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printStream != null) {
                    printStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void writeErrorLog(String info) {
        File dir = logFile;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileNameRule.buildName() + ".log");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(info.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void upload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (logFile != null && logFile.isDirectory()) {
                    File[] files = logFile.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getName().endsWith(".log");
                        }
                    });

                    for (File file : files) {
                        uploadFile(file, uploadUrl);
                    }
                }
            }
        }).start();

    }

    public void uploadFile(final File file, String requestUrl) {
        String model = android.os.Build.MODEL;
        String type = "Android " + android.os.Build.VERSION.RELEASE + " SDK " + android.os.Build.VERSION.SDK_INT;

        try {
            byte[] bytes = FileUtils.readFully(file);
            Http.instance().post(requestUrl).
                    param("deviceType", type).
                    param("deviceName", model).
                    data(bytes).run().done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObj = arguments.get(0);
                    if (jsonObj.optBoolean("success")) {
                        file.delete();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
