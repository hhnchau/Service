package vn.ptt.intentservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class DemoService extends Service {
    private boolean isRunning  = false;

    @Override
    public void onCreate() {
        super.onCreate();

        isRunning = true;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    if(isRunning){
                        Toast.makeText(DemoService.this, "Hello", Toast.LENGTH_SHORT).show();
                    }
                }

                //Stop service once it finishes its task
                stopSelf();
            }
        }).start();

        //return Service.START_STICKY;
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }
}
