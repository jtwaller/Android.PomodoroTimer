package com.jtwaller.pomodorotimer;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class BreakActivity extends AppCompatActivity {

    static final String TAG = "BreakActivity";

    AlarmReceiver alarm = new AlarmReceiver();

    TextView timerTextView;
    long endTime;
    long timeRemaining = 5 * 60 * 1000; // 5 minutes in ms
    int minutes;
    int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break);

        minutes = (int) (timeRemaining / 1000) / 60;
        seconds = (int) (timeRemaining / 1000) % 60;

        timerTextView = (TextView) findViewById(R.id.breakTimerTextView);
        timerTextView.setText(String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds));

        final CountdownHandler timerHandler = new CountdownHandler(timerTextView, timeRemaining);

        Button b = (Button) findViewById(R.id.breakTimerButton);
        b.setText(getString(R.string.button_stop));

        alarm.setAlarm(BreakActivity.this, timeRemaining);
        endTime = SystemClock.elapsedRealtime() + timeRemaining;
        timerHandler.start(endTime);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals(getString(R.string.button_stop))) {
                    // Record time remaining and kill handler
                    timeRemaining = endTime - SystemClock.elapsedRealtime();
                    //timeRemaining = 10 * 1000;

                    alarm.cancelAlarm(BreakActivity.this);
                    timerHandler.stop();
                    b.setText(R.string.button_start);
                } else {
                    // Calculate end time and start the timer
                    endTime = SystemClock.elapsedRealtime() + timeRemaining;

                    alarm.setAlarm(BreakActivity.this, timeRemaining);
                    timerHandler.start(endTime);
                    b.setText(R.string.button_stop);
                }
            }
        });
    }
}