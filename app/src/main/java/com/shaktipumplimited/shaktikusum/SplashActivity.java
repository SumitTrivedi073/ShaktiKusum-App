package com.shaktipumplimited.shaktikusum;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;


import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.DatabaseHelper;
import debugapp.GlobalValue.NewSolarVFD;
import debugapp.GlobalValue.UtilMethod;

import com.shaktipumplimited.retrofit.BaseRequest;
import com.shaktipumplimited.retrofit.RequestReciever;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class SplashActivity extends Activity {


    private BaseRequest baseRequest;
    private static final int MY_REQUEST_CODE = 17300;
    // Splash screen timer
//    private static int SPLASH_TIME_OUT = 10000;
    private static int SPLASH_TIME_OUT = 5000;
    Intent i;
    ImageView imageView;
    Context context;
    Context mContext;
    String versionName = "0.0";
    String newVersion = "0.0";
    String mDebudShowStutus = "";
    AppUpdateManager appUpdateManager;
    DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;

    @Override
    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        mContext = this;
        baseRequest = new BaseRequest(this);
        imageView = (ImageView) findViewById(R.id.imageSplash);

        databaseHelper = new DatabaseHelper(SplashActivity.this);

       // versionName = BuildConfig.VERSION_NAME;
        versionName = "6.5";

        WebURL.APP_VERSION_CODE = "6.5";

        System.out.println("versionName==>>"+WebURL.APP_VERSION_CODE);

        try {

            String MOBName = UtilMethod.getDeviceName();
            System.out.println("MName==>>"+MOBName);

            int versionAPI = Build.VERSION.SDK_INT;
            String versionRelease = Build.VERSION.RELEASE;

            if(!MOBName.equalsIgnoreCase("") )
                UtilMethod.setSharedPreference(mContext,"MOBName", MOBName);


            if(!String.valueOf(versionAPI).equalsIgnoreCase("") )
                UtilMethod.setSharedPreference(mContext,"MOBversionAPI", versionAPI+"");

            if(!versionRelease.equalsIgnoreCase("") )
                UtilMethod.setSharedPreference(mContext,"MOBversionRelease", versionRelease);

            // String MNameNumber = UtilMethod.getDeviceId(mContext);
            System.out.println("MName_versionRelease==>>"+versionRelease);
            System.out.println("MName_versionAPI==>>"+versionAPI);
        } catch (Exception e) {
            e.printStackTrace();
        }

     /*   TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            @SuppressLint("MissingPermission") CellInfoGsm cellinfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
            CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
            int mSignalStr =  cellSignalStrengthGsm.getDbm();

            Toast.makeText(mContext, "signalStrengthValue==>>"+ mSignalStr, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "signalStrengthValue==>>No", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            checkUpdate(context);
        }
        else{*/
        new Worker1().execute();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Log.d("newVersion", newVersion + "--" + versionName);

                if (Float.parseFloat(newVersion) > Float.parseFloat(versionName)) {

                    SplashActivity.this.finish();
                    Intent i = new Intent(SplashActivity.this, UpdateActivity.class);
                    startActivity(i);

                } else {
                    Log.d("newVersion1", newVersion + "--" + versionName);

               /*     if (databaseHelper.getLogin()) {
                        i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        SplashActivity.this.finish();
                    } else {
                        if (CustomUtility.isInternetOn()) {
                            // Write Your Code What you want to do
                            new LoginSelction().execute();
                        } else {
                            Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                        }

                    }*/

                    if (databaseHelper.getLogin()) {

                        if (CustomUtility.isInternetOn()) {
                            // Write Your Code What you want to do
                            mDebudShowStutus = "1";
                            callAppVersionCheckAPI();
                            //  new LoginSelction().execute();


                        } else {
                            Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                            String OTP_CHECK = CustomUtility.getSharedPreferences(context, "CHECK_OTP_VARIFED");
                            if(OTP_CHECK.equalsIgnoreCase("Y"))
                            {
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(context, Login.class);
                                startActivity(intent);
                            }
                           /* i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);*/
                           finish();

                        }
                      /*  i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        SplashActivity.this.finish();*/
                    } else {
                        if (CustomUtility.isInternetOn()) {
                            // Write Your Code What you want to do
                            mDebudShowStutus = "2";
                               callAppVersionCheckAPI();

                         //   new LoginSelction().execute();


                        } else {
                            Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SplashActivity.this, Login.class);
                            startActivity(i);
                            finish();

                        }

                    }
                }

            }
        }, SPLASH_TIME_OUT);


        // }


    }

    public String getVersion() {

        String newversion = "0.0";

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        String app_version_response = null;

        try {

            app_version_response = CustomHttpClient.executeHttpPost1(WebURL.VERSION_PAGE, param);

          //  if (app_version_response != null)
            if (!app_version_response.equalsIgnoreCase(""))
            {

                JSONObject ja = new JSONObject(app_version_response);
                newversion = ja.getString("app_version");

                Log.e("newVersion1", newversion);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return newversion;
    }

    public void checkUpdate(final Context context) {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(context);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        Log.d("UPDATEINFO", "&&&&&" + appUpdateManager.getAppUpdateInfo());

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                Log.d("UPDATEINFO", "&&&&&" + appUpdateInfo.updateAvailability());
                Log.d("UPDATEINFO", "&&&&&" + UpdateAvailability.UPDATE_AVAILABLE);
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {

                    Log.d("Support in-app-update", "UPDATE_AVAILABLE");

                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        requestUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
                    } else { //AppUpdateType.FLEXIBLE
                        requestUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE);
                    }

                } else {
                    Log.d("Support in-app-update", "UPDATE_NOT_AVAILABLE");

                    if (databaseHelper.getLogin()) {

                        if (CustomUtility.isInternetOn()) {
                            // Write Your Code What you want to do

                            callAppVersionCheckAPI();
                            //  new LoginSelction().execute();


                        } else {
                            Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SplashActivity.this, Login.class);
                            startActivity(i);
                            finish();

                        }
                      /*  i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        SplashActivity.this.finish();*/
                    } else {
                        if (CustomUtility.isInternetOn()) {
                            // Write Your Code What you want to do

                           callAppVersionCheckAPI();
                          //  new LoginSelction().execute();

                        } else {
                            Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SplashActivity.this, Login.class);
                            startActivity(i);
                           finish();


                        }

                    }


                }
            }
        });
    }

    private void requestUpdate(AppUpdateInfo appUpdateInfo, int flow_type) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                    flow_type,
                    this,
                    MY_REQUEST_CODE);

        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.w("Update flow failed! ", "Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

       /* appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        new OnSuccessListener<AppUpdateInfo>() {
                            @Override
                            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                                Log.d("UPDATEINFO","&&&&&"+appUpdateInfo.updateAvailability());
                                Log.d("UPDATEINFO","&&&&&"+UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS);
                                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                    // If an in-app update is already running, resume the update.
                                    try {
                                        appUpdateManager.startUpdateFlowForResult(
                                                appUpdateInfo,
                                                AppUpdateType.IMMEDIATE,
                                                SplashActivity.this,
                                                MY_REQUEST_CODE);
                                    } catch (IntentSender.SendIntentException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });*/
    }

    private class Worker1 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {


                if (CustomUtility.isInternetOn()) {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);

                    newVersion = getVersion();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }

    private class LoginSelction extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            //progress = newworkorder ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Please Wait...");

        }

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
                progressDialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            return login_selec;

        }

        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
            progressDialog.dismiss();
        }
    }


    private void callAppVersionCheckAPI() {
        baseRequest.showLoader();
        if (UtilMethod.isOnline(mContext)) {

            baseRequest.setBaseRequestListner(new RequestReciever() {
                @Override
                public void onSuccess(int APINumber, String Json, Object obj) {
                    //  JSONArray arr = (JSONArray) obj;
                    try {
                      /*  Gson gson = new Gson();
                        //////////////add model class here
                        SettingParameterModel mSettingParameterModel = gson.fromJson(Json, SettingParameterModel.class);
                        getPlantListStatusCheck(mSettingParameterModel);*/

                        JSONObject jo = new JSONObject(Json);

                        String mStatus = jo.getString("status");
                        final String mMessage = jo.getString("message");
                        String jo11 = jo.getString("response");
                        System.out.println("jo11==>>"+jo11);
                        JSONObject join = new JSONObject(jo11);
                        if (mStatus.equalsIgnoreCase("true")) {

                          //  mDebudShowStutus = join.getString("RMS_Status");

                            WebURL.DEBUG_CHECK_IN_KUSUM = join.getString("RMS_Status");

                            System.out.println("ja==>>"+join.getString("RMS_Status"));
                           // Toast.makeText(mContext, "CheckStatus=>>"+WebURL.DEBUG_CHECK_IN_KUSUM, Toast.LENGTH_SHORT).show();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
//                                    //   addDynamicViewProNew(mSettingParameterResponse);

                                    if(mDebudShowStutus.equalsIgnoreCase("1"))
                                    {
                                       /* i = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(i);*/
                                        String OTP_CHECK = CustomUtility.getSharedPreferences(context, "CHECK_OTP_VARIFED");
                                        if(OTP_CHECK.equalsIgnoreCase("Y"))
                                        {
                                            Intent intent = new Intent(context, MainActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            Intent intent = new Intent(context, Login.class);
                                            startActivity(intent);
                                        }
                                         finish();
                                    }
                                    else
                                    {
                                        new LoginSelction().execute();
                                    }

                                    baseRequest.hideLoader();
                                }


                            });

                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
//                                    //   addDynamicViewProNew(mSettingParameterResponse);
                                    if(mDebudShowStutus.equalsIgnoreCase("1"))
                                    {
                                        /*i = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(i);*/
                                        String OTP_CHECK = CustomUtility.getSharedPreferences(context, "CHECK_OTP_VARIFED");
                                        if(OTP_CHECK.equalsIgnoreCase("Y"))
                                        {
                                            Intent intent = new Intent(context, MainActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            Intent intent = new Intent(context, Login.class);
                                            startActivity(intent);
                                        }
                                        finish();
                                    }
                                    else
                                    {
                                        new LoginSelction().execute();
                                    }
                                    baseRequest.hideLoader();
                                }


                            });
                            Toast.makeText(mContext, mMessage, Toast.LENGTH_LONG).show();
                            baseRequest.hideLoader();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int APINumber, String errorCode, String message) {
                    baseRequest.hideLoader();
                    // new HomeFragment.Worker().execute();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
//                                    //   addDynamicViewProNew(mSettingParameterResponse);
                            new LoginSelction().execute();
                            baseRequest.hideLoader();
                        }


                    });
                    //Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNetworkFailure(int APINumber, String message) {
                    baseRequest.hideLoader();
                    //Toast.makeText(mContext, "Please check internet connection!", Toast.LENGTH_LONG).show();
                }
            });

           /* JsonObject jsonObject = new JsonObject();
            try {
                ////Put input parameter here
                // jsonObject.addProperty("id", mUserID);
                //  jsonObject.addProperty("plantid", "1");
            //    jsonObject.addProperty("id", "9500000854");///Material code
                    jsonObject.addProperty("id", mMaterialCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //baseRequest.callAPIPost(1, jsonObject, Constant.GET_ALL_NOTIFICATION_LIST_API);/////
            //  baseRequest.callAPIPost(1, jsonObject, NewSolarVFD.GET_PLANT_LIST_CHECK);/////
              //baseRequest.callAPIPostIMEI(1, jsonObject, NewSolarVFD.MOTOR_PERSMETER_LIST);/////
            System.out.println("jsonObject==>>"+jsonObject);
            baseRequest.callAPIPost(1, jsonObject, WebURL.MOTOR_PERSMETER_LIST);/////*/

            Map<String, String> wordsByKey = new HashMap<>();

            wordsByKey.put("rms_obj", "KUSUM_DEBUG_BT");



            //baseRequest.callAPIPost(1, jsonObject, Constant.GET_ALL_NOTIFICATION_LIST_API);/////
            //  baseRequest.callAPIPost(1, jsonObject, NewSolarVFD.GET_PLANT_LIST_CHECK);/////
            //baseRequest.callAPIPostIMEI(1, jsonObject, NewSolarVFD.MOTOR_PERSMETER_LIST);/////
            System.out.println("jsonObject==>>"+wordsByKey);
            // baseRequest.callAPIGET(1, wordsByKey, WebURL.MOTOR_PERSMETER_LIST);/////
            baseRequest.callAPIGETIMEIOPtion(1, wordsByKey, NewSolarVFD.RMS_VALIDAION_OPTION_API);/////

        } else {
            baseRequest.hideLoader();
        }
    }
}