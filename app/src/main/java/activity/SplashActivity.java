package activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONObject;

import bean.AppConfig;
import bean.LoginSelectionModel;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.WebURL;


public class SplashActivity extends AppCompatActivity {


    private final String TAG = "RetrieveFirestoreData";
    public static DocumentReference appConfigRef;
    Context mContext;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        databaseHelper = new DatabaseHelper(SplashActivity.this);
        FirebaseApp.initializeApp(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        retriveFirestoreData();

    }

    private void retriveFirestoreData() {

        appConfigRef = FirebaseFirestore.getInstance().collection("Setting").document("AppConfig");
      appConfigRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                     AppConfig appConfig = documentSnapshot.toObject(AppConfig.class);

                    if (appConfig != null) {
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            if (pInfo != null && appConfig.getMinKusumAppVersion() != null
                                    && !appConfig.getMinKusumAppVersion().toString().isEmpty()) {

                                if (pInfo.versionCode < Integer.parseInt(appConfig.getMinKusumAppVersion())) {
                                    CustomUtility.setSharedPreference(getApplicationContext(), Constant.APPURL,appConfig.getKusumAppUrl());
                                    Intent intent = new Intent(SplashActivity.this,SwVersionCheckActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    CheckLoginStatus();
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
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


    private void CheckLoginStatus() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (databaseHelper.getLogin() && CustomUtility.getSharedPreferences(mContext, "CHECK_OTP_VERIFED").equals("Y")) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (CustomUtility.isInternetOn(getApplicationContext())) {
                        loginSelection();
                    } else {
                        Intent intent = new Intent(mContext, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }, 3000);

    }


    public void loginSelection() {
        CustomUtility.showProgressDialogue(SplashActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.LOGIN_SELEC_PAGE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(SplashActivity.this);
                if (!response.toString().isEmpty()) {
                    LoginSelectionModel loginSelectionModel = new Gson().fromJson(response.toString(), LoginSelectionModel.class);
                    if (loginSelectionModel.getLoginType().size() > 0) {

                        for (int i = 0; i < loginSelectionModel.getLoginType().size(); i++) {

                            databaseHelper.insertLoginSelectionData(loginSelectionModel.getLoginType().get(i).getProjectNo(),
                                    loginSelectionModel.getLoginType().get(i).getProjectNm(),
                                    loginSelectionModel.getLoginType().get(i).getProjectLoginNo(),
                                    loginSelectionModel.getLoginType().get(i).getProjectLoginNm());

                        }
                        Intent intent = new Intent(mContext, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SplashActivity.this, "Login types are not available", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(SplashActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
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


}