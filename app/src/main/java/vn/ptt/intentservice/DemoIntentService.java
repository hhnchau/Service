package vn.ptt.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

public class DemoIntentService extends IntentService {

    private ResultReceiver resultReceiver;

    public DemoIntentService() {
        super("DemoIntentService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            String food = intent.getStringExtra("food");
            resultReceiver = intent.getParcelableExtra("receiver");

            try {
                Thread.sleep(3000);
                Bundle bundle = new Bundle();
                bundle.putString("food1", "FOOD1");
                resultReceiver.send(100, bundle);
            } catch (Exception e) {
                e.getLocalizedMessage();
            }




        }
    }
}
