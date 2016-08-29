package com.jtwaller.pomodorotimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class BreakActivity extends AppCompatActivity {

    static final String TAG = "BreakActivity";

    TextView timerTextView;

    int minutes;
    int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break);

        timerTextView = (TextView) findViewById(R.id.breakTimerTextView);
        final Button b = (Button) findViewById(R.id.breakTimerButton);

        // Text stuff
        final CountDownTimer breakTimer = new CountDownTimer(5*60*1000, 1000) {
            @Override
            public void onTick(long l) {
                minutes = (int) (l / 1000) / 60;
                seconds = (int) (l / 1000) % 60;

                timerTextView.setText(String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                try {
                    Uri alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alertSound);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                timerTextView.setText("00:00");
                b.setText("Get back to work!");
            }
        };
        breakTimer.start();

        b.setText("I DON'T NEED YOUR BREAKS");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakTimer.cancel();
                startActivity(new Intent(getBaseContext(), CountdownActivity.class));
            }
        });
    }
}