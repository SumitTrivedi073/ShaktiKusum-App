package debugapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import activity.BaseActivity;
import debugapp.Bean.SurweyListResponse;
import utility.CustomUtility;
import webservice.WebURL;


public class ActivitySurveyList extends BaseActivity {
    private Context mContext;
    private RecyclerView rclyTranportListView;

    public static final int MEDIA_TYPE_IMAGE = 1;

    public static final String GALLERY_DIRECTORY_NAME = "ShaktiTransport";
    private SurweyListAdapter mSurweyListAdapter;
    List<SurweyListResponse> mSurweyListResponse;
    String mUserID = "",mproject_noID = "", mproject_login_noID = "";

    Toolbar mToolbar;
    TextView noDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list);
        mContext = this;
        initView();
    }

    private void initView() {

        mproject_login_noID = CustomUtility.getSharedPreferences(mContext, "loginid");
        mproject_noID = CustomUtility.getSharedPreferences(mContext, "projectid");
        mUserID = CustomUtility.getSharedPreferences(mContext, "userid");

        mSurweyListResponse = new ArrayList<>();

        rclyTranportListView = findViewById(R.id.rclyTranportListView);
        mToolbar =  findViewById(R.id.toolbar);
        noDataFound = findViewById(R.id.noDataFound);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.sitesurvey_B);

        listner();

    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CustomUtility.isInternetOn(getApplicationContext())) {
            callgetSurweyListAPI();
        } else {
            Toast.makeText(mContext, getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void callgetSurweyListAPI() {
        CustomUtility.showProgressDialogue(ActivitySurveyList.this);
        mSurweyListResponse = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("URL====>",WebURL.GET_SURVEY_API + "?project_no=" + mproject_noID + "&userid=" + mUserID + "&project_login_no=" + mproject_login_noID);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.GET_SURVEY_API + "?project_no=" + mproject_noID + "&userid=" + mUserID + "&project_login_no=" + mproject_login_noID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(ActivitySurveyList.this);

                Log.e("response", response.toString());
                if (response.toString() != null && !response.toString().isEmpty()) {
                    try {


                        JSONObject jo = new JSONObject(response.toString());

                        String mStatus = jo.getString("status");
                        final String mMessage = jo.getString("message");
                        if (mStatus.equals("true")) {
                            noDataFound.setVisibility(View.GONE);
                            rclyTranportListView.setVisibility(View.VISIBLE);
                            String jo11 = jo.getString("response");
                            JSONArray ja = new JSONArray(jo11);
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
                            mSurweyListAdapter = new SurweyListAdapter(mContext, mSurweyListResponse);
                            rclyTranportListView.setHasFixedSize(true);
                            rclyTranportListView.setAdapter(mSurweyListAdapter);
                        }else {
                            noDataFound.setVisibility(View.VISIBLE);
                            rclyTranportListView.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(ActivitySurveyList.this);
                Log.e("error", String.valueOf(error));
                noDataFound.setVisibility(View.VISIBLE);
                rclyTranportListView.setVisibility(View.GONE);

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }


/*   public void callgetSurweyListAPI() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("userid", mUserID));
        param.add(new BasicNameValuePair("project_no", mproject_noID));
        param.add(new BasicNameValuePair("project_login_no", mproject_login_noID));

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

    }*/


}
