package activity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import adapter.ComplaintInstAdapter;
import bean.ComplaintInstModel;
import utility.CustomUtility;
import webservice.WebURL;

public class complaintInstallationActivity extends BaseActivity {

    private RecyclerView complaintInstallationList;
    private Toolbar mToolbar;
    List<ComplaintInstModel.Response> complaintInstList;
    AlertDialog alertDialog;

    TextView noDataFound;

    SearchView searchUser;

    ComplaintInstAdapter complaintInstAdapter;

    RelativeLayout searchRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_installtion);

        Init();
        listner();
    }


    private void Init() {
        mToolbar =  findViewById(R.id.toolbar);
        complaintInstallationList = findViewById(R.id.complaintInstallationList);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.complaint_before_installation);
        if(CustomUtility.isInternetOn(getApplicationContext())) {
            getcomplaintInstallationList();
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
                if (complaintInstAdapter != null) {
                    if(!query.isEmpty()) {
                        complaintInstAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (complaintInstAdapter != null) {
                    if(!newText.isEmpty()) {
                        complaintInstAdapter.getFilter().filter(newText);
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



    private void getcomplaintInstallationList() {
        CustomUtility.showProgressDialogue(complaintInstallationActivity.this);
        complaintInstList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
       Log.e("URL====>",WebURL.ComplaintBeforeInstallationAPI +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.ComplaintBeforeInstallationAPI +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  response) {
                CustomUtility.hideProgressDialog(complaintInstallationActivity.this);


                if(response.toString()!=null && !response.toString().isEmpty()) {
                    ComplaintInstModel complaintInstModel = new Gson().fromJson(response.toString(), ComplaintInstModel.class);
                    if(complaintInstModel.getStatus().equals("true")) {
                        complaintInstList = complaintInstModel.getResponse();
                        complaintInstAdapter = new ComplaintInstAdapter(getApplicationContext(),complaintInstList,noDataFound);
                        complaintInstallationList.setHasFixedSize(true);
                        complaintInstallationList.setAdapter(complaintInstAdapter);
                       // complaintInstAdapter.SendOTP(complaintInstallationActivity.this);
                        noDataFound.setVisibility(View.GONE);
                        complaintInstallationList.setVisibility(View.VISIBLE);
                    }else {
                        noDataFound.setVisibility(View.VISIBLE);
                        complaintInstallationList.setVisibility(View.GONE);
                    }

                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                    complaintInstallationList.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(complaintInstallationActivity.this);
                noDataFound.setVisibility(View.VISIBLE);
                complaintInstallationList.setVisibility(View.GONE);
                Log.e("error", String.valueOf(error));

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                2,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}