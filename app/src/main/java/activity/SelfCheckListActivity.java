package activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

import adapter.SelfCheckDetailsAdapter;
import bean.SelfCheckListBean;
import utility.CustomUtility;
import webservice.WebURL;

public class SelfCheckListActivity extends AppCompatActivity {
    SearchView searchUser;
    RecyclerView recyclerview;
    TextView noDataFound;
    SelfCheckDetailsAdapter adapter;
    List<SelfCheckListBean.InstallationDatum> fileList;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_self_check_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inIt();
        listener();
    }
    private void inIt() {
        searchUser = findViewById(R.id.searchUser);
        recyclerview = findViewById(R.id.recyclerview);
        noDataFound = findViewById(R.id.noDataFound);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.vendor_check_list);
        if (CustomUtility.isInternetOn(getApplicationContext())) {
            SelfCheckAPI();
        } else {
            CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), getApplicationContext());
        }
    }
    private void SelfCheckAPI() {
        CustomUtility.showProgressDialogue(SelfCheckListActivity.this);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest mStringRequest = new JsonObjectRequest(Request.Method.GET, WebURL.selfCheckListAPI + "?project_no="
                + CustomUtility.getSharedPreferences(this, "projectid") + "&userid=" + CustomUtility.getSharedPreferences(this, "userid") +
                "&project_login_no=" + CustomUtility.getSharedPreferences(this, "loginid"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response====>", String.valueOf(response));
                if (response.toString() != null && !response.toString().isEmpty()) {
                    SelfCheckListBean selfCheckListBean = new Gson().fromJson(response.toString(), SelfCheckListBean.class);
                    Log.e("filelistmodel=>", String.valueOf(selfCheckListBean.getInstallationData().size()));
                    fileList = new ArrayList<>();
                    if (selfCheckListBean.getInstallationData().size() > 0) {
                        fileList = selfCheckListBean.getInstallationData();
                        adapter = new SelfCheckDetailsAdapter(SelfCheckListActivity.this, fileList, noDataFound);
                        recyclerview.setHasFixedSize(true);
                        recyclerview.setAdapter(adapter);
                        CustomUtility.hideProgressDialog(SelfCheckListActivity.this);
                    } else {
                        CustomUtility.hideProgressDialog(SelfCheckListActivity.this);
                        noDataFound.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error====>", "Error :" + error.toString());
                CustomUtility.hideProgressDialog(SelfCheckListActivity.this);
            }
        });
        mRequestQueue.add(mStringRequest);
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                5,  /// maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) {
                    adapter.getFilter().filter(query);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }
}