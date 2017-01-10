package al.demo.alarmmanagerdemo;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AlarmHelper alarmHelper;

    private TextView alarmStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmHelper = new AlarmHelper(this);

        alarmStatusTextView = (TextView) findViewById(R.id.status_text_view);

        findViewById(R.id.update_stats_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStats();
            }
        });
        findViewById(R.id.schedule_notification_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmHelper.schedulePendingIntent();
                updateStats();
            }
        });
        findViewById(R.id.unschedule_notification_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmHelper.unschedulePendingIntent();
                updateStats();
            }
        });
        updateStats();
    }

    @SuppressLint("SetTextI18n")
    private void updateStats() {
        alarmStatusTextView.setText(
                "PendingIntent.getBroadcast(...) != null: " + alarmHelper.isAlarmScheduled()
        );
    }

}
