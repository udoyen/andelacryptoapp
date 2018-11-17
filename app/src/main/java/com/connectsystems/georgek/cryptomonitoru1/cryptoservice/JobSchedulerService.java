package com.connectsystems.georgek.cryptomonitoru1.cryptoservice;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class JobSchedulerService extends JobService {
    private static final String MY_INTENT = "com.connectsystems.georgek.cryptomonitor.cryptservice.CUSTOM_INTENT";
    private Handler mJobHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            broadcastIntent();
            jobFinished((JobParameters) msg.obj, false);
            return true;
        }
    });


    @Override
    public boolean onStartJob(JobParameters params) {

        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(1);
        return false;
    }

    public void broadcastIntent() {
        Intent intent = new Intent(String.valueOf(this));
        intent.setAction(MY_INTENT);
        getApplicationContext().sendBroadcast(intent);
    }

}
