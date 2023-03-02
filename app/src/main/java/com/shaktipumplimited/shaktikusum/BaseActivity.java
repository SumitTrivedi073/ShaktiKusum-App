package com.shaktipumplimited.shaktikusum;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;


@ReportsCrashes

        (mailTo = "vikas.gothi@shaktipumps.com;",

                customReportContent = {ReportField.APP_VERSION_CODE,
                        ReportField.APP_VERSION_NAME,
                        ReportField.ANDROID_VERSION,
                        ReportField.PHONE_MODEL,
                        ReportField.CUSTOM_DATA,
                        ReportField.STACK_TRACE,
                        ReportField.LOGCAT},
                mode = ReportingInteractionMode.TOAST,
                resToastText = R.string.crash_toast_text) //you get to define resToastText


public class BaseActivity extends Application {

    private static BaseActivity instance;

    public static BaseActivity getInstance() {
        return instance;
    }

    public static void setInstance(BaseActivity instance) {
        BaseActivity.instance = instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        setInstance(this);
        // The following line triggers the initialization of ACRA
       // ACRA.init(this);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                handleUncaughtException(thread, ex);

            }
        });
    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        String stackTrace = Log.getStackTraceString(e);
        String message = e.getMessage();

        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"vikas.gothi@shaktipumps.com"});
        intent.putExtra (Intent.EXTRA_SUBJECT, R.string.crash_toast_text);
        intent.putExtra (Intent.EXTRA_TEXT, stackTrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);
    }
}
