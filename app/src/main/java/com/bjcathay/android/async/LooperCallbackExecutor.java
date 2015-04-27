package com.bjcathay.android.async;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class LooperCallbackExecutor implements ICallbackExecutor {

    private final Handler handler;

    public LooperCallbackExecutor() {
        this(Looper.getMainLooper());
    }

    public LooperCallbackExecutor(Looper looper) {
        handler = new Handler(looper);
    }

    @Override
    public void run(final ICallback callback, final Arguments arguments) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    callback.call(arguments);
                } catch (Exception e) {
                    Log.e("async", "run callback failed", e);
                }
            }
        };
        if (Looper.myLooper() == handler.getLooper()) {
            runnable.run();
        } else {
            handler.post(runnable);
        }
    }

}
