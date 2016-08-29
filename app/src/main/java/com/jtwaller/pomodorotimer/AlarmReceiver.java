package com.jtwaller.pomodorotimer;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Alarm Received!");

        Intent service = new Intent(context, AlarmService.class);
        startWakefulService(context, service);

        Log.d(TAG, "Wakeful service started.  Context: " + context + " service: " + service);

        //Toast.makeText(context, "You are here.", Toast.LENGTH_SHORT).show();
    }

    @TargetApi(19)
    public void setAlarm(Context context, long timeRemaining) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Compatibility for Marshmellow+ Doze feature
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + timeRemaining, alarmIntent);
        } else {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + timeRemaining, alarmIntent);
        }

        Log.d(TAG, "time remaining:" + timeRemaining);
        Toast.makeText(context, "Alarm set!", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context){
        if(alarmMgr != null) {
            Toast.makeText(context, "Alarm disabled", Toast.LENGTH_SHORT).show();
            alarmMgr.cancel(alarmIntent);
        }
    }
}
