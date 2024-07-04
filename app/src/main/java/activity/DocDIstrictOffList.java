package activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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

import adapter.DocDistOffAdapter;
import adapter.SelfCheckDetailsAdapter;
import bean.DocDistListBean;
import bean.SelfCheckListBean;
import utility.CustomUtility;
import webservice.WebURL;

public class DocDIstrictOffList extends AppCompatActivity {
    SearchView searchUser;
    RecyclerView recyclerview;
    TextView noDataFound;
    DocDistOffAdapter adapter;
    List<DocDistListBean.Listing> fileList;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doc_district_off_list);
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
        getSupportActionBar().setTitle(R.string.Doc_dist_off_list);
        if (CustomUtility.isInternetOn(getApplicationContext())) {
            DocDistAPI();
        } else {
            CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), getApplicationContext());
        }
    }

    private void DocDistAPI() {
        CustomUtility.showProgressDialogue(DocDIstrictOffList.this);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest mStringRequest = new JsonObjectRequest(Request.Method.GET, WebURL.docDistList + "?userid=" +
                CustomUtility.getSharedPreferences(this, "userid")
                 +
                "&Project_no=" + CustomUtility.getSharedPreferences(this, "projectid"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response====>", String.valueOf(response));
                if (response.toString() != null && !response.toString().isEmpty()) {
                    DocDistListBean docDistListBean = new Gson().fromJson(response.toString(), DocDistListBean.class);
                    Log.e("filelistmodel=>", String.valueOf(docDistListBean.getListing().size()));
                    fileList = new ArrayList<>();
                    if (docDistListBean.getListing().size() > 0) {
                        fileList = docDistListBean.getListing();
                        adapter = new DocDistOffAdapter(DocDIstrictOffList.this, fileList, noDataFound);
                        recyclerview.setHasFixedSize(true);
                        recyclerview.setAdapter(adapter);
                        CustomUtility.hideProgressDialog(DocDIstrictOffList.this);
                    } else {
                        CustomUtility.hideProgressDialog(DocDIstrictOffList.this);
                        noDataFound.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error====>", "Error :" + error.toString());
                CustomUtility.hideProgressDialog(DocDIstrictOffList.this);
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