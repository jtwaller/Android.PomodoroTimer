package com.jtwaller.pomodorotimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Received!", Toast.LENGTH_SHORT).show();

        Intent service = new Intent(context, AlarmService.class);
        startWakefulService(context, service);

        //Toast.makeText(context, "You are here.", Toast.LENGTH_SHORT).show();
    }

    public void setAlarm(Context context, long timeRemaining) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // TODO: 8/15/16 Update for Marshmallow Doze / Idle modes (setExactAndAllowWhileIdle)
        alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + timeRemaining, alarmIntent);

        Toast.makeText(context, "Alarm set!", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context){
        if(alarmMgr != null) {
            Toast.makeText(context, "Alarm disabled", Toast.LENGTH_SHORT).show();
            alarmMgr.cancel(alarmIntent);
        }
    }
}
