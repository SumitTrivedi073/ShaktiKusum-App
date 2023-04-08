package activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONObject;

import bean.LoginSelectionModel;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.WebURL;



@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {


    ImageView imageView;
    Context mContext;
    DatabaseHelper databaseHelper;




    @Override
    //** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        imageView = (ImageView) findViewById(R.id.imageSplash);
        databaseHelper = new DatabaseHelper(SplashActivity.this);

        CheckLoginStatus();


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