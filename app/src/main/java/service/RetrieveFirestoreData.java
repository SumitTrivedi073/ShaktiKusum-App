package service;


import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.window.SplashScreen;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.shaktipumplimited.shaktikusum.R;

import bean.AppConfig;
import debugapp.GlobalValue.Constant;
import receiver.FirestoreBroadcastReceiver;
import utility.CustomUtility;

public class RetrieveFirestoreData extends Service {

    public static final String NOTIFICATION_CHANNEL_ID = "1001";
    private final String TAG = "RetrieveFirestoreData";
    public static boolean isServiceRunning;
    public static DocumentReference appConfigRef;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");
        prepareForegroundNotification();
        if(CustomUtility.isInternetOn(getApplicationContext())) {
            getFirestoreDatabaseReferance();
        }
        isServiceRunning = true;
    }
    private void prepareForegroundNotification() {

        NotificationChannel serviceChannel =
                new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Location Service Channel",
                        NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);


        Notification notification =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).setContentTitle(getResources().getString(R.string.app_name)).setContentText(
                        "App Running Background").setSmallIcon(R.mipmap.ic_notification).build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(111, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        }else {
            startForeground(111, notification);

        }


    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        return START_STICKY;
    }

    private void getFirestoreDatabaseReferance() {

        appConfigRef =  FirebaseFirestore.getInstance().collection("Setting").document("AppConfig");
        appConfigRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    AppConfig appConfig = snapshot.toObject(AppConfig.class);

                    if (appConfig != null) {
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            if (pInfo != null && appConfig.getMinKusumAppVersion() != null
                                    && !appConfig.getMinKusumAppVersion().isEmpty()) {

                                if (pInfo.versionCode < Integer.parseInt(appConfig.getMinKusumAppVersion())) {
                                    CustomUtility.setSharedPreference(getApplicationContext(), Constant.APPURL, appConfig.getKusumAppUrl());
                                    Intent broadcastIntent = new Intent(RetrieveFirestoreData.this, FirestoreBroadcastReceiver.class);
                                    broadcastIntent.putExtra(Constant.SwVersionConfig, Constant.SwVersionConfig);
                                    sendBroadcast(broadcastIntent);

                                }
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });



    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy called");
        isServiceRunning = false;

    }
}
