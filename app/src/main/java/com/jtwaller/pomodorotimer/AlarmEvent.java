package com.jtwaller.pomodorotimer;

import android.app.IntentService;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmEvent extends IntentService {
    public AlarmEvent() {
        super("AlarmEvent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        // If no alarm has been set
        if(alarmSound == null){
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
        AlarmReceiver.completeWakefulIntent(intent);
    }
}
