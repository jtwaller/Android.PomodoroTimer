package com.jtwaller.pomodorotimer;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class CountdownActivity extends AppCompatActivity {

    /* CountdownActivity is only a visual component for the user's benefit.
    Actual alarm and device waking is handled by AlarmReceiver. */

    static final String TAG = "CountdownActivity";

    boolean mBound = false;
    private AlarmService mAlarmService = null;

    CountdownHandler timerHandler;
    long timeRemaining;

    TextView timerTextView;
    Button timerButton;
    int minutes;
    int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        timeRemaining = 10*1000; // TODO set in preferences

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        timerHandler = new CountdownHandler(timerTextView);

        timerButton = (Button) findViewById(R.id.timerButton);
        timerButton.setText(getString(R.string.button_start));
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals(getString(R.string.button_stop))) {
                    stopCountdown();
                    b.setText(R.string.button_start);
                } else {
                    startCountdown();
                    b.setText(R.string.button_stop);
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder cancelAlert = new AlertDialog.Builder(CountdownActivity.this);
                cancelAlert.setMessage("Cancel timer and reset?");
                cancelAlert.setCancelable(true);

                cancelAlert.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface di, int id) {
                                if(timerButton.getText().equals("STOP")) { // Timer is running
                                    stopCountdown();
                                    timerButton.setText(R.string.button_start);
                                }
                                cancelPomo();
                                di.cancel();
                            }
                        });

                cancelAlert.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface di, int id) {
                                di.cancel();
                            }
                        });
                cancelAlert.create().show();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        if (mBound) { // AlarmService is active
            timeRemaining = mAlarmService.getTimeRemaining();

            minutes = (int) (timeRemaining / 1000) / 60;
            seconds = (int) (timeRemaining / 1000) % 60;
            timerTextView.setText(String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds));
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            AlarmService.LocalBinder binder = (AlarmService.LocalBinder) service;
            mAlarmService = binder.getService();
            mBound = true;
            Log.d(TAG, "onServiceConnected: " + mAlarmService.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mAlarmService = null;
            mBound = false;
        }
    };

    private void startCountdown() {
        // Calculate end time and start the timer

        // Use application context so service is tied to lifecycle
        // of the Application instead of Count
        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
        intent.putExtra("timeRemaining", timeRemaining);
        intent.putExtra("serviceIntent", "setAlarm");

        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        startService(intent);

        timerHandler.start(timeRemaining);
    }

    private void stopCountdown() {
        // Record time remaining and kill handler
        timerHandler.stop();

        timeRemaining = mAlarmService.getTimeRemaining();
        mAlarmService.stopSelf();
    }

    private void cancelPomo() {
        SQLiteHelper mDbHelper = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SQLContract.PomoTable.COLUMN_NAME_DATE_CREATED,
                (System.currentTimeMillis() - timeRemaining));
        values.put(SQLContract.PomoTable.COLUMN_NAME_COMPLETED, false);

        db.insert(SQLContract.PomoTable.TABLE_NAME, null, values);

        timeRemaining = 25*60*1000;
        timerTextView.setText("25:00");
    }
}
