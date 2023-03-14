package activity;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import database.DatabaseHelper;
import webservice.CustomHttpClient;
import webservice.WebURL;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {
    ImageView imageView;
    Context mContext;
    DatabaseHelper databaseHelper;

    private static final int REQUEST_CODE_PERMISSION = 2;


    @Override
    //** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        imageView =  findViewById(R.id.imageSplash);
        databaseHelper = new DatabaseHelper(SplashActivity.this);
        if (!checkPermission()) {

            requestPermission();

        } else {
            CheckLoginStatus();

        }

    }

    private void CheckLoginStatus() {
        new Handler().postDelayed(() -> {
            if (databaseHelper.getLogin()) {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            } else {
                new LoginSelction().execute();
                Intent i = new Intent(mContext, Login.class);
                startActivity(i);
            }
        }, 3000);
    }

    private boolean checkPermission() {
        int FineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int CoarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int Bluetooth = ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH);
        int PhoneState = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int Camera = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int WriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return FineLocation == PackageManager.PERMISSION_GRANTED && CoarseLocation == PackageManager.PERMISSION_GRANTED
                && Bluetooth == PackageManager.PERMISSION_GRANTED && PhoneState == PackageManager.PERMISSION_GRANTED
                && Camera == PackageManager.PERMISSION_GRANTED && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                && WriteExternalStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION,BLUETOOTH,READ_PHONE_STATE,
                CAMERA,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {

                boolean FineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean CoarseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean Bluetooth = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean ReadPhoneState = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean Camera = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                boolean ReadExternalStorage = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                boolean WriteExternalStorage = grantResults[6] == PackageManager.PERMISSION_GRANTED;


                if (FineLocationAccepted && CoarseLocationAccepted && Bluetooth && ReadPhoneState && Camera
                        && ReadExternalStorage && WriteExternalStorage) {
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show();
                } else {


                    showMessageOKCancel("You need to allow access to all the permissions",
                            (dialog, which) -> requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH, READ_PHONE_STATE,
                                            CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_PERMISSION));
                }
            }
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private class LoginSelction extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            String login_selec = null, project_no = null, project_nm = null, project_login_no = null, project_login_nm = null;


            try {

                login_selec = CustomHttpClient.executeHttpPost1(WebURL.LOGIN_SELEC_PAGE, param);

                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("login_type");

                Log.e("DATA","&&&&"+obj1);


                JSONArray ja = new JSONArray(obj1);


                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    project_no = jo.optString("project_no");
                    project_nm = jo.optString("project_nm");
                    project_login_no = jo.optString("project_login_no");
                    project_login_nm = jo.optString("project_login_nm");

                    databaseHelper.insertLoginSelectionData(project_no, project_nm, project_login_no, project_login_nm);

                }
                Intent i = new Intent(SplashActivity.this, Login.class);
                startActivity(i);
                SplashActivity.this.finish();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return login_selec;

        }

    }
}