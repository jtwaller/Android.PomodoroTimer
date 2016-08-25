package com.jtwaller.pomodorotimer;

import android.os.SystemClock;
import android.os.Handler;
import android.widget.TextView;

import java.util.Locale;


public class CountdownHandler extends Handler {

    TextView textView;
    long endTime;

    TimerRunnable r;
    int seconds;
    int minutes;

    public CountdownHandler(TextView textView, long endTime) {
        super();
        this.textView = textView;
        this.endTime = endTime;

        r = new TimerRunnable();
    }

    public void start(long endTime) {
        this.post(r);
        this.endTime = endTime;
    }

    public void stop() {
        this.removeCallbacks(r);
    }

    private class TimerRunnable implements Runnable {

        @Override
        public void run() {
            // SystemClock.elapsedRealtime is preferable over System.currentTimeMillis
            // https://developer.android.com/reference/android/os/SystemClock.html#elapsedRealtime()
            long millis = endTime - SystemClock.elapsedRealtime();
            seconds = (int) (millis / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;

            if (millis > 0) {
                textView.setText(String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds));
                CountdownHandler.this.postDelayed(this, 1000);
            } else {
                textView.setText("00:00");
                CountdownHandler.this.removeCallbacks(this);
            }
        }
    };
}
