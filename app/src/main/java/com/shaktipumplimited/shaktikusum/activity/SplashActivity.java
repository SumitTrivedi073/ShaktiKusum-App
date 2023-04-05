package com.shaktipumplimited.shaktikusum.activity;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.shaktipumplimited.shaktikusum.bean.LoginSelectionModel;
import com.shaktipumplimited.shaktikusum.database.DatabaseHelper;
import com.shaktipumplimited.shaktikusum.utility.CustomUtility;
import com.shaktipumplimited.shaktikusum.webservice.WebURL;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONObject;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    ImageView imageView;
    Context mContext;
    DatabaseHelper databaseHelper;

    boolean FineLocationAccepted,CoarseLocationAccepted,Bluetooth,ReadPhoneState,Camera,ReadPhoneStorage,WritePhoneStorage;



    @Override
    //** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        imageView = (ImageView) findViewById(R.id.imageSplash);
        databaseHelper = new DatabaseHelper(SplashActivity.this);
        if (!checkPermission()) {

            requestPermission();

        } else {
            CheckLoginStatus();

        }

    }

    private void CheckLoginStatus() {
        if(CustomUtility.isInternetOn()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                     if (databaseHelper.getLogin() &&  CustomUtility.getSharedPreferences(mContext, "CHECK_OTP_VERIFED").equals("Y")) {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        loginSelection();

                    }
                }
            }, 3000);
        }else {
            Toast.makeText(getApplicationContext(), R.string.check_internet_connection,Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkPermission() {
        int FineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int CoarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int Bluetooth = ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH);
        int PhoneState = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int Camera = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int WriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);


        if (SDK_INT >= Build.VERSION_CODES.R) {
            return FineLocation == PackageManager.PERMISSION_GRANTED && CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Bluetooth == PackageManager.PERMISSION_GRANTED && PhoneState == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED && Environment.isExternalStorageManager();
        } else {

            return FineLocation == PackageManager.PERMISSION_GRANTED && CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Bluetooth == PackageManager.PERMISSION_GRANTED && PhoneState == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && WriteExternalStorage == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH, READ_PHONE_STATE,
                    CAMERA}, REQUEST_CODE_PERMISSION);
        }else {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH, READ_PHONE_STATE,
                    CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    CheckLoginStatus();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0) {
                     FineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                     CoarseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                     Bluetooth = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                     ReadPhoneState = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                     Camera = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    if (SDK_INT < Build.VERSION_CODES.R) {
                         ReadPhoneStorage = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                         WritePhoneStorage = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    }
                    if (SDK_INT >= Build.VERSION_CODES.R) {
                           if (FineLocationAccepted && CoarseLocationAccepted && Bluetooth && ReadPhoneState && Camera) {
                               // perform action when allow permission success
                               try {
                                   Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                   intent.addCategory("android.intent.category.DEFAULT");
                                   intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                                   startActivityForResult(intent, 2296);
                               } catch (Exception e) {
                                   Intent intent = new Intent();
                                   intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                   startActivityForResult(intent, 2296);
                               }
                           }
                    } else {
                              if( FineLocationAccepted && CoarseLocationAccepted && Bluetooth && ReadPhoneState && Camera && ReadPhoneStorage && WritePhoneStorage ){
                                  CheckLoginStatus();
                              }else {
                                  requestPermission();
                              }
                           }
                }
                break;
        }
    }

    private void checkForAlertPopup() {
        showMessageOKCancel("You need to allow access to all the permissions",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                });


    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public void loginSelection(){

        CustomUtility.showProgressDialogue(SplashActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.LOGIN_SELEC_PAGE, null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(SplashActivity.this);


                if(response.toString()!=null && !response.toString().isEmpty()) {
                    LoginSelectionModel loginSelectionModel = new Gson().fromJson(response.toString(), LoginSelectionModel.class);
                    if(loginSelectionModel.getLoginType().size()>0) {

                        for (int i = 0; i <loginSelectionModel.getLoginType().size(); i++) {

                            databaseHelper.insertLoginSelectionData(loginSelectionModel.getLoginType().get(i).getProjectNo(),
                                    loginSelectionModel.getLoginType().get(i).getProjectNm(),
                                    loginSelectionModel.getLoginType().get(i).getProjectLoginNo(),
                                    loginSelectionModel.getLoginType().get(i).getProjectLoginNm());

                        }
                        Intent intent = new Intent(mContext, Login.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"Login types are not available",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"Something Went Wrong!",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(SplashActivity.this);
                Log.e("error", String.valueOf(error));
                Toast.makeText(SplashActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}