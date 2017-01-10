package al.demo.alarmmanagerdemo;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class AlarmHelper {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1234;

    private Context context;
    private AlarmManager alarmManager;

    public AlarmHelper(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void schedulePendingIntent() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 5);

        schedulePendingIntent(calendar.getTimeInMillis(), getPendingIntent());
    }

    public void unschedulePendingIntent() {
        PendingIntent pendingIntent = getPendingIntent();
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    public void schedulePendingIntent(long triggerTimeMillis, PendingIntent pendingIntent) {
        Log.d(TAG, "schedulePendingIntent: " + triggerTimeMillis + "/" + pendingIntent);
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d(TAG, "setExactAndAllowWhileIdle");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                Log.d(TAG, "setExact");
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
            } else {
                Log.d(TAG, "set");
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
            }
        }
    }

    public boolean isAlarmScheduled() {
        Intent notificationIntent = new Intent("al.demo.alarmmanagerdemo.NOTIFY_ACTION");
        return PendingIntent.getBroadcast(context, REQUEST_CODE, notificationIntent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    private PendingIntent getPendingIntent() {
        Intent notificationIntent = new Intent("al.demo.alarmmanagerdemo.NOTIFY_ACTION");
        return PendingIntent.getBroadcast(context, REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
