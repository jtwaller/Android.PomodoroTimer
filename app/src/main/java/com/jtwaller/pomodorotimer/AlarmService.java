package com.jtwaller.pomodorotimer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends Service {

    private static final String TAG = "AlarmService";

    Thread alarmThread;

    public AlarmService() {
        super();
        Log.d(TAG, "AlarmService constructor");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Runnable r = new alarmRunnable(this);
        alarmThread = new Thread(r);
        alarmThread.start();

        Intent gotoAlarmActivity = new Intent(this, AlarmActivity.class);
        gotoAlarmActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(gotoAlarmActivity);

        Log.d(TAG, "onStartCommand");

        /*
        while (alarmThread.isAlive()) {
            // Are continuous loops bad practice for battery/performance?
            Log.d(TAG, "AlarmThread lives, sleeping 2 seconds...");

//            try {
//                This is bad.  Will sleep UI thread, consider putting while loop into new thread?
//                Thread.sleep(2000);
//            } catch(InterruptedException e) {}
        }
        */

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        alarmThread.interrupt();
        alarmThread = null;
        Log.d(TAG, "Alarm interrupted");

        Intent intent = new Intent(this, AlarmService.class);
        AlarmReceiver.completeWakefulIntent(intent);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class alarmRunnable implements Runnable {

        Context threadContext;

        private alarmRunnable(Context context) {
            super();
            threadContext = context;
        }

        @Override
        public void run() {

            //Ringtone alarmSound;
            MediaPlayer mMediaPlayer = new MediaPlayer();

            Uri alarmFile = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            // If no alarm ringtone has been set
            if(alarmFile == null){
                alarmFile = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }

            try {
                mMediaPlayer.setDataSource(threadContext, alarmFile);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (Exception e) {}

            // TODO: When interrupting & nulling alarmThread in onStartCommand,
            // will this release the media player?

            //alarmSound = RingtoneManager.getRingtone(getApplicationContext(), alarmFile);
            //alarmSound.play();
        }
    }

}
