package com.jtwaller.pomodorotimer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {

    private static final String TAG = "AlarmService";

    Thread alarmThread;
    MediaPlayer mMediaPlayer;
    Uri alarmFile;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        alarmFile = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        // If no alarm ringtone has been set
        if(alarmFile == null){
            alarmFile = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

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

        Log.d(TAG, "onStartCommand, Tid: " + Thread.currentThread().getId());
        Log.d(TAG, "onStartCommand, alarm Tid: " + alarmThread.getId());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(this, AlarmService.class);
        AlarmReceiver.completeWakefulIntent(intent);

        Log.d(TAG, "onDestroy, Tid: " + Thread.currentThread().getId());
        Log.d(TAG, "Alarm interrupted, Alarm Tid:" + alarmThread.getId());

        mMediaPlayer.release();
        alarmThread.interrupt();
        alarmThread = null;
        super.onDestroy();

        Log.d(TAG, "onDestroy end reached");
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
            try {
                Log.d(TAG, "Runnable, Tid: " + Thread.currentThread().getId());
                mMediaPlayer.setDataSource(threadContext, alarmFile);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (Exception e) {}
        }
    }

}
