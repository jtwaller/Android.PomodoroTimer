package com.jtwaller.pomodorotimer;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class CountdownActivity extends AppCompatActivity {

    /* CountdownActivity is only a visual component for the user's benefit.
    Actual alarm and device waking is handled by AlarmReceiver. */

    AlarmReceiver alarm = new AlarmReceiver();

    TextView timerTextView;
    long endTime;
    long timeRemaining = 2 * 1000; // 25 minutes in ms
    int minutes;
    int seconds;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            // SystemClock.elapsedRealtime is preferable over System.currentTimeMillis
            // https://developer.android.com/reference/android/os/SystemClock.html#elapsedRealtime()
            long millis = endTime - SystemClock.elapsedRealtime();
            seconds = (int) (millis / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;

            if(millis > 0) {
                timerTextView.setText(String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 1000);
            } else {
                timerTextView.setText("00:00");
                timerHandler.removeCallbacks(timerRunnable);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        minutes = (int) (timeRemaining / 1000) / 60;
        seconds = (int) (timeRemaining / 1000) % 60;

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        timerTextView.setText(String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds));

        Button b = (Button) findViewById(R.id.timerButton);
        b.setText(getString(R.string.button_start));
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals(getString(R.string.button_stop))) {
                    // Record time remaining and kill handler
                    //timeRemaining = endTime - SystemClock.elapsedRealtime();
                    timeRemaining = 5 * 1000;

                    alarm.cancelAlarm(CountdownActivity.this);
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText(R.string.button_start);
                } else {
                    // Calculate end time and start the timer
                    endTime = SystemClock.elapsedRealtime() + timeRemaining;

                    alarm.setAlarm(CountdownActivity.this, timeRemaining);
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText(R.string.button_stop);
                }
            }

        });

    }
}
