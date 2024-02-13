package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.PendingFeedbackAdapter;
import debugapp.GlobalValue.Constant;
import debugapp.PendingInstallationModel;
import utility.CustomUtility;
import webservice.WebURL;

public class PendingInstallationVerificationActivity extends BaseActivity implements PendingFeedbackAdapter.SendOTPListner{

    private  RecyclerView pendingFeedbackList;
    private Toolbar mToolbar;
    List<PendingInstallationModel.Response> pendingInstallationModels;
    AlertDialog alertDialog;

    TextView noDataFound;

    SearchView searchUser;

    PendingFeedbackAdapter pendingFeedbackAdapter;

    RelativeLayout searchRelative;

    ProgressDialog progressDialog;
    int SelectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_installation_verification);

        Init();
        listner();

    }


    private void Init() {
        progressDialog = new ProgressDialog(this);
        mToolbar = findViewById(R.id.toolbar);
        pendingFeedbackList = findViewById(R.id.pendingfeedbacklist);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.pendingInstallationVerification));
        if(CustomUtility.isInternetOn(getApplicationContext())) {
            getPendingInstallationList();
        }else {
            CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection),getApplicationContext());
        }
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser.setFocusableInTouchMode(true);
                searchUser.requestFocus();
                searchUser.onActionViewExpanded();

            }
        });

        ImageView searchIcon = searchUser.findViewById(R.id.search_button);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_search_24));
        searchIcon.setColorFilter(getResources().getColor(R.color.colorPrimary));

        ImageView searchClose = searchUser.findViewById(R.id.search_close_btn);
        searchClose.setColorFilter(getResources().getColor(R.color.colorPrimary));


        EditText searchEditText = searchUser.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._14sdp));

        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (pendingFeedbackAdapter != null) {
                    if(!query.isEmpty()) {
                        pendingFeedbackAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (pendingFeedbackAdapter != null) {
                    if(!newText.isEmpty()) {
                        pendingFeedbackAdapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });

        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchUser.onActionViewCollapsed();
            }
        });

    }

    /*-------------------------------------------------------------Pending Installation List-----------------------------------------------------------------------------*/


    private void getPendingInstallationList() {
        CustomUtility.showProgressDialogue(PendingInstallationVerificationActivity.this);
        pendingInstallationModels = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("PendingInstallation=====>",WebURL.PendingFeedback +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.PendingFeedback +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01", null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject  response) {
                CustomUtility.hideProgressDialog(PendingInstallationVerificationActivity.this);


                if(response.toString()!=null && !response.toString().isEmpty()) {
                    PendingInstallationModel pendingInstallationModel = new Gson().fromJson(response.toString(), PendingInstallationModel.class);
                    if(pendingInstallationModel.getStatus().equals("true")) {

                        pendingInstallationModels = pendingInstallationModel.getResponse();
                        pendingFeedbackAdapter = new PendingFeedbackAdapter(getApplicationContext(), pendingInstallationModel.getResponse(), noDataFound);
                        pendingFeedbackList.setHasFixedSize(true);
                        pendingFeedbackList.setAdapter(pendingFeedbackAdapter);
                        pendingFeedbackAdapter.SendOTP(PendingInstallationVerificationActivity.this);
                        noDataFound.setVisibility(View.GONE);
                        pendingFeedbackList.setVisibility(View.VISIBLE);
                    }else {
                        noDataFound.setVisibility(View.VISIBLE);
                        pendingFeedbackList.setVisibility(View.GONE);
                    }

                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                    pendingFeedbackList.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(PendingInstallationVerificationActivity.this);
                noDataFound.setVisibility(View.VISIBLE);
                pendingFeedbackList.setVisibility(View.GONE);
                Log.e("error", String.valueOf(error));

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void sendOtpListener(List<PendingInstallationModel.Response> pendingFeedbackList, int position) {
        SelectedIndex = position;
        String dongleType = pendingFeedbackList.get(position).getDongle().charAt(0) + pendingFeedbackList.get(position).getDongle().substring(1, 2);
        Log.e("dongleType=====>", dongleType);
        String[] latlong =  pendingFeedbackList.get(position).getLatlng().split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        Log.e("latitude======>", String.valueOf(latitude));
        Log.e("longitude======>", String.valueOf(longitude));

        Intent intent = new Intent(PendingInstallationVerificationActivity.this, DeviceMappingActivity.class);
        intent.putExtra(Constant.deviceMappingData, pendingFeedbackList.get(position));
        intent.putExtra(Constant.deviceMappingData2, "2");
        intent.putExtra(Constant.latitude, String.valueOf(latitude));
        intent.putExtra(Constant.longitude, String.valueOf(longitude));
        if (dongleType.equals("99")) {
            intent.putExtra(Constant.is2GDevice, "true");
        } else {
            intent.putExtra(Constant.is2GDevice, "false");
        }
        startActivity(intent);
    }
}