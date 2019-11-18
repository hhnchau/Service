package vn.ptt.intentservice.utils;

import android.os.Handler;

public class MySchedule {
    private Runnable runnable;
    private Handler handler;
    private int schedule;
    private boolean loop;

    public interface ScheduleCallback {
        void onSchedule();
    }

    public MySchedule(int schedule, final ScheduleCallback callback) {
        this.schedule = schedule;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSchedule();
                    if (loop) start();
                }
            }
        };
    }

    public void start() {
        handler.postDelayed(runnable, schedule);
    }

    public void loop() {
        loop = true;
        start();
    }

    public void stop() {
        loop = false;
        handler.removeCallbacks(runnable);
    }
}