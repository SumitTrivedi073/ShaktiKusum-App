package activity;

import android.annotation.SuppressLint;
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
import adapter.Adapter_reject_list;
import bean.RejectListBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.WebURL;


public class ActivityRejectSite extends BaseActivity {


    private RecyclerView rejectSiteList;
    private Toolbar mToolbar;
    List<RejectListBean.RejectDatum> rejectData;
    TextView noDataFound;
    SearchView searchUser;
    Adapter_reject_list adapterRejectList;
    RelativeLayout searchRelative;
    DatabaseHelper databaseHelper;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_site);
        Init();
        listner();


    }
    @Override
    protected void onResume() {
        super.onResume();
        if (CustomUtility.isInternetOn(getApplicationContext())) {
            databaseHelper.deleteRejectAuditSiteListData();
            getRejectAuditSiteList();
        }else {
            CustomUtility.showToast(ActivityRejectSite.this,getResources().getString(R.string.check_internet_connection));
            rejectData = new ArrayList<RejectListBean.RejectDatum>();
            rejectData = databaseHelper.getRejectedAuditSiteListData();
            if(rejectData.size()>0){
                setAdapter();
            }else {
                noDataFound.setVisibility(View.VISIBLE);
                rejectSiteList.setVisibility(View.GONE);

            }
        }
    }

    private void Init() {
        rejectSiteList = findViewById(R.id.rejectSiteList);
        mToolbar = findViewById(R.id.toolbar);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.menu_rejectsites));

        databaseHelper = new DatabaseHelper(this);

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
                if (adapterRejectList != null) {
                    if (!query.isEmpty()) {
                        adapterRejectList.getFilter().filter(query);
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapterRejectList != null) {
                    if (!newText.isEmpty()) {
                        adapterRejectList.getFilter().filter(newText);
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

    private void getRejectAuditSiteList() {
       CustomUtility.showProgressDialogue(ActivityRejectSite.this);
           rejectData = new ArrayList<RejectListBean.RejectDatum>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.REJECT_DATA + "?project_no=" + CustomUtility.getSharedPreferences(getApplicationContext(), "projectid") + "&userid=" + CustomUtility.getSharedPreferences(getApplicationContext(), "userid") + "&project_login_no=01", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(ActivityRejectSite.this);


                if (response.toString() != null && !response.toString().isEmpty()) {
                    RejectListBean rejectListBean = new Gson().fromJson(response.toString(), RejectListBean.class);
                    if (rejectListBean.getRejectData() != null && rejectListBean.getRejectData() .size() > 0) {
                        rejectData = rejectListBean.getRejectData();
                          setAdapter();

                         for(int i=0; i<rejectData.size(); i++){
                             if (databaseHelper.isRecordExist(DatabaseHelper.TABLE_REJECTION_LIST, DatabaseHelper.KEY_BILL_NO, rejectData.get(i).getVbeln())) {
                                 databaseHelper.updateRejectedAuditSiteListData(rejectData.get(i).getVbeln(), rejectData.get(i));
                             } else {
                                 databaseHelper.insertRejectedAuditSiteListData(rejectData.get(i).getVbeln(), rejectData.get(i));
                             }
                         }

                    } else {
                        noDataFound.setVisibility(View.VISIBLE);
                        rejectSiteList.setVisibility(View.GONE);

                    }

                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                    rejectSiteList.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(ActivityRejectSite.this);
                noDataFound.setVisibility(View.VISIBLE);
                rejectSiteList.setVisibility(View.GONE);
                Log.e("error", String.valueOf(error));

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void setAdapter() {
        adapterRejectList = new Adapter_reject_list(ActivityRejectSite.this, rejectData,noDataFound);
        rejectSiteList.setHasFixedSize(true);
        rejectSiteList.setAdapter(adapterRejectList);
        noDataFound.setVisibility(View.GONE);
        rejectSiteList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



   /* @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        if (CustomUtility.isInternetOn(getApplicationContext())) {
            recyclerView.setAdapter(null);
            db.deleteRejectListData();
            new GetRejectDataList_Task().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
            rejectListBeans = new ArrayList<RejectListBean>();
            rejectListBeans = db.getRejectionListData();
            Log.e("SIZE", "&&&&" + rejectListBeans.size());
            if (rejectListBeans != null && rejectListBeans.size() > 0) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                recyclerView.setAdapter(null);

                Log.e("SIZE", "&&&&" + rejectListBeans.size());
                adapter_reject_list = new Adapter_reject_list(context, rejectListBeans);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManagerSubCategory);
                recyclerView.setAdapter(adapter_reject_list);
                adapter_reject_list.notifyDataSetChanged();
            } else {
                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);
            }
        }
    }
*/


}
