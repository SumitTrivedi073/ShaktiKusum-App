package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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
import adapter.RejectionInstAdapter;
import bean.RejectInstallationModel;
import utility.CustomUtility;
import webservice.WebURL;

public class RejectionInstallation extends AppCompatActivity {

    private RecyclerView rejectionInstList;
    private Toolbar mToolbar;
    List<RejectInstallationModel.RejectDatum> rejectionInstArrayList;
    TextView noDataFound;
    SearchView searchUser;
    adapter.RejectionInstAdapter rejectionInstAdapter;
    RelativeLayout searchRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejection_installation);

        Init();
        listner();
    }

    private void Init() {
        rejectionInstList = findViewById(R.id.rejectInstalltionList);
        mToolbar = findViewById(R.id.toolbar);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.rejection));

        if (CustomUtility.isInternetOn(getApplicationContext())) {
            getRejctionInstList();
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
                if (rejectionInstAdapter != null) {
                    if (!query.isEmpty()) {
                        rejectionInstAdapter.getFilter().filter(query);
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (rejectionInstAdapter != null) {
                    if (!newText.isEmpty()) {
                        rejectionInstAdapter.getFilter().filter(newText);
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

    private void getRejctionInstList() {
        rejectionInstArrayList = new ArrayList< >();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("URL=========>",WebURL.rejectionInstalltionAPI + "?project_no=" + CustomUtility.getSharedPreferences(getApplicationContext(), "projectid") + "&userid=" + CustomUtility.getSharedPreferences(getApplicationContext(), "userid"));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.rejectionInstalltionAPI + "?project_no=" + CustomUtility.getSharedPreferences(getApplicationContext(), "projectid") + "&userid=0000" + CustomUtility.getSharedPreferences(getApplicationContext(), "userid")  , null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(RejectionInstallation.this);


                if (response.toString() != null && !response.toString().isEmpty()) {
                    RejectInstallationModel rejectModelList = new Gson().fromJson(response.toString(), RejectInstallationModel.class);
                    if (rejectModelList.getRejectData() != null && rejectModelList.getRejectData().size() > 0) {
                        rejectionInstArrayList = rejectModelList.getRejectData();
                         rejectionInstAdapter = new RejectionInstAdapter(getApplicationContext(), rejectModelList.getRejectData(),noDataFound);
                        rejectionInstList.setHasFixedSize(true);
                        rejectionInstList.setAdapter(rejectionInstAdapter);
                        noDataFound.setVisibility(View.GONE);
                        rejectionInstList.setVisibility(View.VISIBLE);

                    } else {
                        noDataFound.setVisibility(View.VISIBLE);
                        rejectionInstList.setVisibility(View.GONE);

                    }

                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                    rejectionInstList.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(RejectionInstallation.this);
                noDataFound.setVisibility(View.VISIBLE);
                rejectionInstList.setVisibility(View.GONE);
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