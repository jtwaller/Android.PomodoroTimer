package com.jtwaller.pomodorotimer;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmService extends Service {

    private static final String TAG = "AlarmService";

    AlarmReceiver mAlarmReceiver = new AlarmReceiver();
    final Thread mAlarmThread = new Thread(new AlarmRunnable(getBaseContext()));
//    AlarmRunnable mAlarmRunnable;

    Notification notification;

    long endTime;
    long timeRemaining;
    int minutes;
    int seconds;
    String intentExtra;

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.alarm_notification_icon)
                .setContentTitle("Alarm running")
                .setContentText("Keep working!");

        notification = mBuilder.build();

        // TODO -> left off here, make notification and debug service
        startForeground(1, notification);
        Log.d(TAG, "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        intentExtra = intent.getStringExtra("serviceIntent");
        Log.d(TAG, "onStartCommand: " + intent.toString() + intent.getExtras().toString());

        switch(intentExtra) {
            case "setAlarm" : // Set in CountdownActivity
                timeRemaining = intent.getLongExtra("timeRemaining", 0L);
                mAlarmReceiver.setAlarm(this, timeRemaining);
                break;
            case "cancelAlarm" :
                Log.d(TAG, "onStartCommand: cancelAlarm case.");
                mAlarmReceiver.cancelAlarm(this);
            case "soundAlarm" : // Triggered by AlarmReceiver
                Log.d(TAG, "onStartCommand: soundAlarm case.");
                mAlarmThread.start();
                break;
            case "stopAlarm" : // Stopped by BreakActivity or canceled Countdown
                mAlarmThread.interrupt();
            default :
                Log.d(TAG, "onStartCommand: invalid extra: " + intentExtra);

        }
//        Runnable r = new alarmRunnable(this);
//        alarmThread = new Thread(r);
//        alarmThread.start();
//
//        Intent gotoAlarmActivity = new Intent(this, AlarmActivity.class);
//        gotoAlarmActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        startActivity(gotoAlarmActivity);
//
//        Log.d(TAG, "onStartCommand, Tid: " + Thread.currentThread().getId());
//        Log.d(TAG, "onStartCommand, alarm Tid: " + alarmThread.getId());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(this, AlarmService.class);
        AlarmReceiver.completeWakefulIntent(intent);

        Log.d(TAG, "onDestroy, Tid: " + Thread.currentThread().getId());
        Log.d(TAG, "Alarm interrupted, Alarm Tid:" + mAlarmThread.getId());

//        mAlarmRunnable.stop();
        mAlarmThread.interrupt();
//        mAlarmThread = null;
        super.onDestroy();

        Log.d(TAG, "onDestroy end reached");
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }

    private class AlarmRunnable implements Runnable {
        final MediaPlayer mMediaPlayer = new MediaPlayer();
        Uri alarmFile;
        Context context;

        private AlarmRunnable(Context context) {
            super();
            this.context = context;

            alarmFile = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            // If no alarm ringtone has been set
            if(alarmFile == null){
                alarmFile = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }

            try {
                mMediaPlayer.setDataSource(context, alarmFile);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Log.d(TAG, "Runnable, Tid: " + Thread.currentThread().getId());
            mMediaPlayer.start();
        }

        public void stop() {
            mMediaPlayer.release();
        }
    }

    // Binder stuff below here
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        AlarmService getService() {
            return AlarmService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


}

