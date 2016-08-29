package com.jtwaller.pomodorotimer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class CountdownActivity extends AppCompatActivity {

    /* CountdownActivity is only a visual component for the user's benefit.
    Actual alarm and device waking is handled by AlarmReceiver. */

    static final String TAG = "CountdownActivity";

    AlarmReceiver alarm = new AlarmReceiver();

    TextView timerTextView;
    long endTime;
    long timeRemaining = 25*60*1000; // 25 minutes in ms
    int minutes;
    int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        minutes = (int) (timeRemaining / 1000) / 60;
        seconds = (int) (timeRemaining / 1000) % 60;

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        timerTextView.setText(String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds));

        final CountdownHandler timerHandler = new CountdownHandler(timerTextView, timeRemaining);

        final Button b = (Button) findViewById(R.id.timerButton);
        b.setText(getString(R.string.button_start));
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals(getString(R.string.button_stop))) {
                    // Record time remaining and kill handler
                    timeRemaining = endTime - SystemClock.elapsedRealtime();

                    alarm.cancelAlarm(CountdownActivity.this);
                    timerHandler.stop();
                    b.setText(R.string.button_start);
                } else {
                    // Calculate end time and start the timer
                    endTime = SystemClock.elapsedRealtime() + timeRemaining;

                    alarm.setAlarm(CountdownActivity.this, timeRemaining);
                    timerHandler.start(endTime);
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
                                SQLiteHelper mDbHelper = new SQLiteHelper(getApplicationContext());
                                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                                ContentValues values = new ContentValues();
                                values.put(SQLContract.PomoTable.COLUMN_NAME_DATE_CREATED,
                                        (System.currentTimeMillis() - timeRemaining));
                                values.put(SQLContract.PomoTable.COLUMN_NAME_COMPLETED, false);

                                db.insert(SQLContract.PomoTable.TABLE_NAME, null, values);

                                timeRemaining = 25*60*1000;
                                b.setText("25:00");  // TODO: Make a reset function
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
}
