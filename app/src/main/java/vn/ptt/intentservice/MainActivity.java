package vn.ptt.intentservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import vn.ptt.intentservice.BootDeviceService.RunAfterBootService;

public class MainActivity extends AppCompatActivity {
    private HandleReceiver receiver;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiver = new HandleReceiver(new Handler());

    }

    public void startIntentService(View view) {
        Intent intent = new Intent(this, DemoIntentService.class);
        intent.putExtra("receiver", receiver);
        intent.putExtra("food", "FOOD");
        startService(intent);
    }

    public void startService(View view) {
        Intent intent = new Intent(this, DemoService.class);
        startService(intent);
    }

    public void startServiceByAlarm(View view) {
        startServiceByAlarm(this);
    }


    public class HandleReceiver extends ResultReceiver {
        public HandleReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 100) {
                Toast.makeText(MainActivity.this, resultData.get("food1").toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleSendBroadCast() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("ACTION3");
        broadcastIntent.putExtra("food", "FOOD");
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    public void startBroadCast(View view) {
        handleSendBroadCast();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION1");
        filter.addAction("ACTION2");
        if (broadcastReceiver == null) {
            initBroadcastReceiver();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    private void unregisterReceiver() {
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
    }

    private void initBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String INTENT_ACTION = intent.getAction();
                    if (INTENT_ACTION != null) {
                        switch (INTENT_ACTION) {
                            case "ACTION1":
                                Bundle bundle = intent.getExtras();
                                if (bundle != null) {

                                }
                                Toast.makeText(context, "ACTION1", Toast.LENGTH_SHORT).show();
                                break;
                            case "ACTION2":
                                Toast.makeText(context, "ACTION2", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }


    private void startServiceByAlarm(Context context)
    {
        // Create intent to invoke the background service.
        Intent intent = new Intent(context, RunAfterBootService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Toast.makeText(context, "Start", Toast.LENGTH_LONG).show();

        long startTime = System.currentTimeMillis();
        long intervalTime = 1*1000;
        // Get alarm manager.
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Create repeat alarm.
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);

        startService(intent);
    }

}
