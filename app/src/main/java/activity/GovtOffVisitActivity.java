package activity;
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

import adapter.RoutePlanAdapter;
import adapter.jointInspectionAdapter;
import bean.JointInspectionModel;
import debugapp.RoutePlanModel;
import utility.CustomUtility;
import webservice.WebURL;


public class GovtOffVisitActivity extends BaseActivity {
    private RecyclerView jointInspectionList;
    private Toolbar mToolbar;
    List<JointInspectionModel.InspectionDatum> jointInspectionArrayList;
    TextView noDataFound;
    SearchView searchUser;
    jointInspectionAdapter jointInspectionAdapter;
    RelativeLayout searchRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddsub_img);
        Init();
        listner();
    }

    private void Init() {
        jointInspectionList = findViewById(R.id.jointInspectionList);
        mToolbar = findViewById(R.id.toolbar);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.govtOfficeVisit));

        if (CustomUtility.isInternetOn(getApplicationContext())) {
            getJointInspectionList();
        } else {
            CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), getApplicationContext());
        }
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        searchRelative.setOnClickListener(v -> {
            searchUser.setFocusableInTouchMode(true);
            searchUser.requestFocus();
            searchUser.onActionViewExpanded();

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
                if (jointInspectionAdapter != null) {
                    if (!query.isEmpty()) {
                        jointInspectionAdapter.getFilter().filter(query);
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (jointInspectionAdapter != null) {
                    if (!newText.isEmpty()) {
                        jointInspectionAdapter.getFilter().filter(newText);
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

    private void getJointInspectionList() {
        CustomUtility.showProgressDialogue(GovtOffVisitActivity.this);
        jointInspectionArrayList = new ArrayList<JointInspectionModel.InspectionDatum>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.jointInspectionAPI + "?project_no=" + CustomUtility.getSharedPreferences(getApplicationContext(), "projectid") + "&userid=" + CustomUtility.getSharedPreferences(getApplicationContext(), "userid") + "&project_login_no=01", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(GovtOffVisitActivity.this);


                if (response.toString() != null && !response.toString().isEmpty()) {
                    JointInspectionModel routePlanModel = new Gson().fromJson(response.toString(), JointInspectionModel.class);
                    if (routePlanModel.getInspectionData() != null && routePlanModel.getInspectionData().size() > 0) {
                        jointInspectionArrayList = routePlanModel.getInspectionData();
                        jointInspectionAdapter = new jointInspectionAdapter(getApplicationContext(), routePlanModel.getInspectionData(),noDataFound);
                        jointInspectionList.setHasFixedSize(true);
                        jointInspectionList.setAdapter(jointInspectionAdapter);
                        noDataFound.setVisibility(View.GONE);
                        jointInspectionList.setVisibility(View.VISIBLE);

                    } else {
                        noDataFound.setVisibility(View.VISIBLE);
                        jointInspectionList.setVisibility(View.GONE);

                    }

                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                    jointInspectionList.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(GovtOffVisitActivity.this);
                noDataFound.setVisibility(View.VISIBLE);
                jointInspectionList.setVisibility(View.GONE);
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

}

