package com.jtwaller.pomodorotimer;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    AlarmReceiver alarm = new AlarmReceiver();

    TextView timerTextView;
    long endTime;
    long timeRemaining = 1500000; // 25 minutes in ms

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            // SystemClock.elapsedRealtime is preferable over System.currentTimeMillis
            // https://developer.android.com/reference/android/os/SystemClock.html#elapsedRealtime()
            long millis = endTime - SystemClock.elapsedRealtime();
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            if(millis > 0) {
                timerTextView.setText(String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 1000);
            } else {
                timerTextView.setText("00:00");

            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = (TextView) findViewById(R.id.timerTextView);

        Button b = (Button) findViewById(R.id.timerButton);
        b.setText(getString(R.string.button_start));
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals(getString(R.string.button_stop))) {
                    timeRemaining = endTime - SystemClock.elapsedRealtime();

                    alarm.cancelAlarm();
                    timerHandler.removeCallbacks(timerRunnable); //stop timer
                    b.setText(R.string.button_start);
                } else {
                    endTime = SystemClock.elapsedRealtime() + timeRemaining;

                    alarm.setAlarm(MainActivity.this, timeRemaining);
                    timerHandler.postDelayed(timerRunnable, 0); //start timer
                    b.setText(R.string.button_stop);
                }
            }

        });

    }



}
