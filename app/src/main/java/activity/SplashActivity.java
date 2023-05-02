package activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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



public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    Context mContext;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        databaseHelper = new DatabaseHelper(SplashActivity.this);

        CheckLoginStatus();


    }

    private void CheckLoginStatus() {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                     if (databaseHelper.getLogin() &&  CustomUtility.getSharedPreferences(mContext, "CHECK_OTP_VERIFED").equals("Y")) {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                         if (CustomUtility.isInternetOn()) {
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


    public void loginSelection(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.LOGIN_SELEC_PAGE, null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject response) {

                if(!response.toString().isEmpty()) {
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
             //   CustomUtility.hideProgressDialog(SplashActivity.this);
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