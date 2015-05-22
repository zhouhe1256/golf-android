package com.bjcathay.qt.util;

import android.os.CountDownTimer;

public class TimeCount extends CountDownTimer {

    public interface TimeUpdate {
        void onTick(long millisUntilFinished);

        void onFinish();
    }

    private TimeUpdate timeUpdate;

    public TimeCount(long millisInFuture, long countDownInterval, TimeUpdate timeUpdate) {
        super(millisInFuture, countDownInterval);
        this.timeUpdate = timeUpdate;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timeUpdate.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        timeUpdate.onFinish();
    }
}
