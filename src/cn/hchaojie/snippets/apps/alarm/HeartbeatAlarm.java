package cn.hchaojie.snippets.apps.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class HeartbeatAlarm extends BroadcastReceiver {
    private static final String TAG = "ALARM";

    @SuppressLint("Wakelock")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive -> intent: %s" + intent);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        Log.d(TAG, "WakeLock acquired");

        wl.release();
    }

    /**
     * Start the alarm
     * 
     * @param context
     */
    public void startAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, HeartbeatAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3 * 1000, pi);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, HeartbeatAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
