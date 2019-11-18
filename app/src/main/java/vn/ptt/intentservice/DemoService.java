package vn.ptt.intentservice;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.ptt.intentservice.utils.MySchedule;

public class DemoService extends Service {
    private boolean isRunning  = false;

    @Override
    public void onCreate() {
        super.onCreate();

        isRunning = true;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MySchedule mySchedule = new MySchedule(10000, new MySchedule.ScheduleCallback() {
            @Override
            public void onSchedule() {
                isAppOnForeground(DemoService.this);
                //if (isAppOnForeground(DemoService.this))
                    //Toast.makeText(DemoService.this, "True: is running", Toast.LENGTH_SHORT).show();
                //else
                    //Toast.makeText(DemoService.this, "False: Stopped", Toast.LENGTH_SHORT).show();
            }
        });
        mySchedule.loop();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                //Your logic that service will perform will be placed here
//                //In this example we are just looping and waits for 1000 milliseconds in each loop.
//                for (int i = 0; i < 5; i++) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                    }
//
//                    if(isRunning){
//                        Toast.makeText(DemoService.this, "Hello", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                //Stop service once it finishes its task
//                stopSelf();
//            }
//        }).start();

        return Service.START_STICKY; // Never stop
        //return Service.START_NOT_STICKY; //Stop when close app, always run in background
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

    private Boolean isAppOnForeground(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (activityManager == null) return false;
//
//        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
//        ComponentName componentInfo = taskInfo.get(0).topActivity;
//
//        Toast.makeText(DemoService.this, componentInfo.getPackageName(), Toast.LENGTH_SHORT).show();
//
//        List<ActivityManager.RunningAppProcessInfo> appProcess = activityManager.getRunningAppProcesses();
//        if (appProcess == null) return false;
//
//        final String packageName = "com.google.android.youtube";
//        for (ActivityManager.RunningAppProcessInfo app : appProcess) {
//            Toast.makeText(DemoService.this, app.processName, Toast.LENGTH_SHORT).show();
//            if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && app.processName.equals(packageName))
//                return true;
//        }
//        return false;



        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String[] activePackages;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    activePackages = getActivePackages(activityManager);
                } else {
                    activePackages = getActivePackagesCompat(activityManager);
                }
                if (activePackages != null) {
                    for (String activePackage : activePackages) {
                        Toast.makeText(DemoService.this, activePackage, Toast.LENGTH_SHORT).show();
                        if (activePackage.equals("com.google.android.youtube")) {
                            //Calendar app is launched, do something
                            Toast.makeText(DemoService.this, activePackage, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        return false;

    }


    String[] getActivePackagesCompat(ActivityManager activityManager) {
        final List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        final ComponentName componentName = taskInfo.get(0).topActivity;
        final String[] activePackages = new String[1];
        activePackages[0] = componentName.getPackageName();
        return activePackages;
    }

    String[] getActivePackages(ActivityManager activityManager) {
        final Set<String> activePackages = new HashSet<String>();
        final List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                activePackages.addAll(Arrays.asList(processInfo.pkgList));
            }
        }
        return activePackages.toArray(new String[activePackages.size()]);
    }


}
