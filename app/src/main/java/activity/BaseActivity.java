package activity;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import service.RetrieveFirestoreData;
import utility.CustomUtility;

public abstract class  BaseActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(SwVersionConfigBroadcastReceiver, new IntentFilter(Constant.SwVersionConfig), Context.RECEIVER_NOT_EXPORTED);
        }else {
            registerReceiver(SwVersionConfigBroadcastReceiver, new IntentFilter(Constant.SwVersionConfig), Context.RECEIVER_EXPORTED);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CustomUtility.isInternetOn(BaseActivity.this)) {
            getFirestoreData();
        }
    }

    private void getFirestoreData() {
        databaseHelper = new DatabaseHelper(this);
        if (databaseHelper.getLogin() &&  CustomUtility.getSharedPreferences(getApplicationContext(), "CHECK_OTP_VERIFED").equals("Y"))  {

            if (!RetrieveFirestoreData.isServiceRunning) {
                Intent intent = new Intent(BaseActivity.this, RetrieveFirestoreData.class);

                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
                      startForegroundService(intent);
                }else {
                    startService(intent);
                }
            }
        }
    }

    BroadcastReceiver SwVersionConfigBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

           Intent intent1 = new Intent(getApplicationContext(),SwVersionCheckActivity.class);
            startActivity(intent1);
            finish();

        }
    };
}
