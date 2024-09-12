package debugapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import activity.BaseActivity;
import debugapp.Bean.SurveyListResponse;
import utility.CustomUtility;
import webservice.WebURL;


public class ActivitySurveyList extends BaseActivity {
    private Context mContext;
    RelativeLayout searchRelative;
    private RecyclerView rclyTranportListView;
    SearchView searchUser;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiTransport";
    private SurweyListAdapter mSurweyListAdapter;
    List<SurveyListResponse.Response> mSurveyListResponse;
    String mUserID = "", mproject_noID = "", mproject_login_noID = "";

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
        searchRelative = findViewById(R.id.searchRelative);
        searchUser = findViewById(R.id.searchUser);
        mSurveyListResponse = new ArrayList<>();

        rclyTranportListView = findViewById(R.id.rclyTranportListView);
        mToolbar = findViewById(R.id.toolbar);
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
                if (mSurweyListAdapter != null) {
                    if(!query.isEmpty()) {
                        mSurweyListAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mSurweyListAdapter != null) {
                    if(!newText.isEmpty()) {
                        mSurweyListAdapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });

        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchUser.onActionViewCollapsed();
                if (mSurweyListAdapter != null) {
                    mSurweyListAdapter.getFilter().filter("");
                }
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
        mSurveyListResponse = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("URL====>", WebURL.GET_SURVEY_API + "?project_no=" + mproject_noID + "&userid=" + mUserID + "&project_login_no=" + mproject_login_noID);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.GET_SURVEY_API + "?project_no=" + mproject_noID + "&userid=" + mUserID + "&project_login_no=" + mproject_login_noID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(ActivitySurveyList.this);

                Log.e("response", response.toString());
                if (response.toString() != null && !response.toString().isEmpty()) {
                    try {
                        SurveyListResponse SurveyListResponse = new Gson().fromJson(response.toString(),SurveyListResponse.class);

                        if (SurveyListResponse.getStatus().equals("true")) {
                            noDataFound.setVisibility(View.GONE);
                            rclyTranportListView.setVisibility(View.VISIBLE);


                            mSurveyListResponse = SurveyListResponse.getResponse();
                            mSurweyListAdapter = new SurweyListAdapter(mContext, mSurveyListResponse);
                            rclyTranportListView.setHasFixedSize(true);
                            rclyTranportListView.setAdapter(mSurweyListAdapter);
                        } else {
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
    
}
