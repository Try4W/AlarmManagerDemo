package al.demo.alarmmanagerdemo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    private String TAG = "MainActivity";

    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 3);

                schedulePendingIntent(calendar.getTimeInMillis(), getPendingIntent());
            }
        });
    }

    private void schedulePendingIntent(long triggerTimeMillis, PendingIntent pendingIntent) {
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

    private PendingIntent getPendingIntent() {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        return PendingIntent.getBroadcast(this, REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static class NotificationPublisher extends BroadcastReceiver {

        private String TAG = "NotificationPublisher";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent contentIntent = new Intent(context, MainActivity.class);
            contentIntent.setAction(Intent.ACTION_MAIN);
            contentIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            contentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Notification notification = new NotificationCompat.Builder(context)
                    .setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, 0))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentTitle("Test title")
                    .setContentText("Test text")
                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                    .build();

            notificationManager.notify(123, notification);
        }
    }

}
