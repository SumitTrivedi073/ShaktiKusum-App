package com.shaktipumplimited.shaktikusum.debugapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.shaktipumplimited.shaktikusum.debugapp.Bean.SurweyListResponse;

import com.shaktipumplimited.shaktikusum.R;
import com.shaktipumplimited.shaktikusum.utility.CustomUtility;
import com.shaktipumplimited.shaktikusum.webservice.CustomHttpClient;
import com.shaktipumplimited.shaktikusum.webservice.WebURL;


public class ActivitySurveyList extends AppCompatActivity {
    private Context mContext;
    private Context context;
    boolean connected = false;
    private RelativeLayout rlvBackViewID;
    private RelativeLayout rlvAddDEviceViewID;
    private RecyclerView rclyTranportListView;
   // private TransportListAdapter mTransportListAdapter;

   // private BaseRequest baseRequest;

    private Intent myIntent;
    private String mPlantID;
  //  private List<LrInvoiceResponse> mLrInvoiceResponse;
    private List<Boolean> mLrInvoiceSelectionCheck;
    public static final int BITMAP_SAMPLE_SIZE = 4;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    private String imageStoragePath, enq_docno;

    public static final String GALLERY_DIRECTORY_NAME = "ShaktiTransport";

    TextView txtSubmitInspectionID, photo1, photo2, txtSubmitePhotoID;
    boolean photo1_flag = false,
            photo2_flag = false;

    String photo1_text="", photo2_text="";

    int PERMISSION_ALL = 1;

    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private String mHomePath, PathHolder, Filename,cust_name;
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private SurweyListAdapter mSurweyListAdapter;

    List<SurweyListResponse> mSurweyListResponse;
   // private BaseRequest baseRequest;
    private ProgressDialog progressDialog;

    String mUserID = "";
    String mproject_noID = "";
    String mproject_login_noID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list);
        mContext = this;
        initView();
    }

    private void initView() {

       // WebURL.mSapBillNumber  ="";
      //  baseRequest = new BaseRequest(this);
        mproject_login_noID = CustomUtility.getSharedPreferences(mContext, "loginid");
        mproject_noID = CustomUtility.getSharedPreferences(mContext, "projectid");
       // mUserID = CustomUtility.getSharedPreferences(mContext, "UserID");
        mUserID = CustomUtility.getSharedPreferences(mContext, "userid");

        System.out.println("mUserID==>>"+mUserID);

        mSurweyListResponse = new ArrayList<>();
        mLrInvoiceSelectionCheck = new ArrayList<>();

        rlvBackViewID = findViewById(R.id.rlvBackViewID);
        rlvAddDEviceViewID = findViewById(R.id.rlvAddDEviceViewID);

        rclyTranportListView = findViewById(R.id.rclyTranportListView);

        rclyTranportListView.setLayoutManager(new LinearLayoutManager(this));

        /*mSurweyListAdapter = new SurweyListAdapter(mContext, mSurweyListResponse, mLrInvoiceSelectionCheck);
        rclyTranportListView.setAdapter(mSurweyListAdapter );*/

        rlvBackViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlvAddDEviceViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    callgetSurweyListAPI();
                }
                else
                {
                    Toast.makeText(mContext, "Please check internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (isOnline()) {
            callgetSurweyListAPI();
        }
        else
        {
            Toast.makeText(mContext, "Please check internet connection.", Toast.LENGTH_SHORT).show();
        }
    }


   public void callgetSurweyListAPI() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        //   username = inputName.getText().toString();
        //   password = inputPassword.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("userid", mUserID));
        param.add(new BasicNameValuePair("project_no", mproject_noID));
        param.add(new BasicNameValuePair("project_login_no", mproject_login_noID));

        //param.add(new BasicNameValuePair("pernr", "58503"));
        // param.add(new BasicNameValuePair("lrdate", mLRMobileValue));
        // param.add(new BasicNameValuePair("mobno", mLRMobileValue));

        //  jsonObject.addProperty("lrno", mLRNumberValue);
        // jsonObject.addProperty("mobno", mLRMobileValue);

        //  param.add(new BasicNameValuePair("pernr", username));
        // param.add(new BasicNameValuePair("pass", password));

/******************************************************************************************//*
*//*                   server connection
/******************************************************************************************/
        progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {
                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.GET_SURVEY_API, param);
                    Log.d("check_error", obj);
                    Log.e("check_error", obj);

                    JSONObject jo = new JSONObject(obj);

                    String mStatus = jo.getString("status");
                    final String mMessage = jo.getString("message");
                    String jo11 = jo.getString("response");
                    //String jo11 = jo.getString(obj);
                    if (mStatus.equalsIgnoreCase("true")) {

                        if(mSurweyListResponse.size()>0)
                            mSurweyListResponse.clear();

                        JSONArray ja = new JSONArray(jo11);
                        // JSONObject jo = ja.getJSONObject(0);

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject join = ja.getJSONObject(i);
                            SurweyListResponse mmSurweyListResponse = new SurweyListResponse();

                            mmSurweyListResponse.setBeneficiary(join.getString("beneficiary"));
                            mmSurweyListResponse.setCustomerName(join.getString("customer_name"));
                            mmSurweyListResponse.setMobile(join.getString("mobile"));
                            mmSurweyListResponse.setAddress(join.getString("address"));
                            mmSurweyListResponse.setState(join.getString("state"));
                            mmSurweyListResponse.setRegioTxt(join.getString("regio_txt"));
                            mmSurweyListResponse.setCitycTxt(join.getString("cityc_txt"));
                            mmSurweyListResponse.setCity(join.getString("city"));
                            mmSurweyListResponse.setProjectNo(join.getString("project_no"));
                            mmSurweyListResponse.setProcessNo(join.getString("process_no"));
                            mmSurweyListResponse.setRegisno(join.getString("regisno"));
                            mmSurweyListResponse.setLifnr(join.getString("lifnr"));

                            mSurweyListResponse.add(mmSurweyListResponse);


                        }


                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                mSurweyListAdapter = new SurweyListAdapter(mContext, mSurweyListResponse);
                                rclyTranportListView.setAdapter(mSurweyListAdapter );
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        });
                        //   Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    // dismiss the progress dialog
                    progressDialog.dismiss();
                }

            }

        }.start();

    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
          //  Toast.makeText(LrtransportList.this, mString, Toast.LENGTH_LONG).show();

        }
    };

    public boolean isOnline() {
        try {


            context = getApplicationContext();

            ConnectivityManager  connectivityManager = (ConnectivityManager) this.context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();

            try {
                Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
                int returnVal = p1.waitFor();
                connected = (returnVal == 0);
                return connected;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;


        } catch (Exception e) {
//            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
}
