package com.jtwaller.pomodorotimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.view.View;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmEvent.class);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context, long timeRemaining) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // TODO: 8/15/16 Update for Marshmallow Doze / Idle modes (setExactAndAllowWhileIdle)
        alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeRemaining, alarmIntent);
    }

    public void cancelAlarm(){
        if(alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }

    }


}
