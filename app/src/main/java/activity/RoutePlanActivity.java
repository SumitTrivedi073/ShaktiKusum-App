package activity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.RoutePlanAdapter;
import debugapp.RoutePlanModel;
import utility.CustomUtility;
import webservice.WebURL;

public class RoutePlanActivity extends BaseActivity implements RoutePlanAdapter.SelectRouteListner {

    private RecyclerView routePlanList;
    private Toolbar mToolbar;
    List<RoutePlanModel.InstallationDatum> routePlanArrayList;
    TextView noDataFound, submitBtn;
    SearchView searchUser;
    RoutePlanAdapter routePlanAdapter;
    RelativeLayout searchRelative;
    JSONArray jsonArray = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeplan);

        Init();
        listner();
    }

    private void Init() {
        routePlanList = findViewById(R.id.routePlanList);
        mToolbar = findViewById(R.id.toolbar);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        submitBtn = findViewById(R.id.submitBtn);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.routeplan));

        if (CustomUtility.isInternetOn(getApplicationContext())) {
            getRoutePlanList();
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

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (routePlanArrayList.size() > 0) {
                    if (CustomUtility.isInternetOn(RoutePlanActivity.this)) {
                        saveRoutePlanAPI();
                    } else {
                        CustomUtility.showToast(RoutePlanActivity.this, getResources().getString(R.string.check_internet_connection));
                    }
                }
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
                if (routePlanAdapter != null) {
                    if (!query.isEmpty()) {
                        routePlanAdapter.getFilter().filter(query);
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (routePlanAdapter != null) {
                    if (!newText.isEmpty()) {
                        routePlanAdapter.getFilter().filter(newText);
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

    @Override
    public void selectRouteListener(RoutePlanModel.InstallationDatum routePlanList, int position) {
        RoutePlanModel.InstallationDatum installationDatum = new RoutePlanModel.InstallationDatum();
        if (!routePlanList.isChecked()) {
            installationDatum.setProjectNo(routePlanList.getProjectNo());
            installationDatum.setRegisno(routePlanList.getRegisno());
            installationDatum.setProcessNo(routePlanList.getProcessNo());
            installationDatum.setUserid(routePlanList.getUserid());
            installationDatum.setBeneficiary(routePlanList.getBeneficiary());
            installationDatum.setSiteAdrc(routePlanList.getSiteAdrc());
            installationDatum.setExisPumpHp(routePlanList.getExisPumpHp());
            installationDatum.setPumpAcDc(routePlanList.getPumpAcDc());
            installationDatum.setChecked(true);
            routePlanArrayList.set(position, installationDatum);
        } else {
            installationDatum.setProjectNo(routePlanList.getProjectNo());
            installationDatum.setRegisno(routePlanList.getRegisno());
            installationDatum.setProcessNo(routePlanList.getProcessNo());
            installationDatum.setUserid(routePlanList.getUserid());
            installationDatum.setBeneficiary(routePlanList.getBeneficiary());
            installationDatum.setSiteAdrc(routePlanList.getSiteAdrc());
            installationDatum.setExisPumpHp(routePlanList.getExisPumpHp());
            installationDatum.setPumpAcDc(routePlanList.getPumpAcDc());
            installationDatum.setChecked(false);
            routePlanArrayList.set(position, installationDatum);
        }

    }


    private void getRoutePlanList() {
        CustomUtility.showProgressDialogue(RoutePlanActivity.this);
        routePlanArrayList = new ArrayList<RoutePlanModel.InstallationDatum>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.RoutePlanAPI + "?project_no=" + CustomUtility.getSharedPreferences(getApplicationContext(), "projectid") + "&userid=" + CustomUtility.getSharedPreferences(getApplicationContext(), "userid") + "&project_login_no=01", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(RoutePlanActivity.this);


                if (response.toString() != null && !response.toString().isEmpty()) {
                    RoutePlanModel routePlanModel = new Gson().fromJson(response.toString(), RoutePlanModel.class);
                    if (routePlanModel.getInstallationData() != null && routePlanModel.getInstallationData().size() > 0) {
                        routePlanArrayList = routePlanModel.getInstallationData();
                        routePlanAdapter = new RoutePlanAdapter(getApplicationContext(), routePlanModel.getInstallationData(), noDataFound);
                        routePlanList.setHasFixedSize(true);
                        routePlanList.setAdapter(routePlanAdapter);
                        routePlanAdapter.SelectRoute(RoutePlanActivity.this);
                        noDataFound.setVisibility(View.GONE);
                        routePlanList.setVisibility(View.VISIBLE);
                        submitBtn.setVisibility(View.VISIBLE);
                    } else {
                        noDataFound.setVisibility(View.VISIBLE);
                        routePlanList.setVisibility(View.GONE);
                        submitBtn.setVisibility(View.GONE);
                    }

                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                    routePlanList.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(RoutePlanActivity.this);
                noDataFound.setVisibility(View.VISIBLE);
                routePlanList.setVisibility(View.GONE);
                Log.e("error", String.valueOf(error));

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void saveRoutePlanAPI() {
        if (routePlanArrayList.size() > 0) {
            jsonArray = new JSONArray();
            for (int i = 0; i < routePlanArrayList.size(); i++) {
                if (routePlanArrayList.get(i).isChecked()) {
                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("project_no", routePlanArrayList.get(i).getProjectNo());
                        jsonObj.put("regisno", routePlanArrayList.get(i).getRegisno());
                        jsonObj.put("process_no", routePlanArrayList.get(i).getPumpAcDc());
                        jsonObj.put("userid", routePlanArrayList.get(i).getUserid());
                        jsonObj.put("beneficiary", routePlanArrayList.get(i).getBeneficiary());
                        jsonObj.put("process_no", routePlanArrayList.get(i).getProcessNo());
                        jsonArray.put(jsonObj);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }

        }


        CustomUtility.showProgressDialogue(RoutePlanActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.saveRoutePlanAPI + jsonArray, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                CustomUtility.hideProgressDialog(RoutePlanActivity.this);


                if (!res.toString().isEmpty()) {

                    CustomUtility.showToast(RoutePlanActivity.this, getResources().getString(R.string.dataSubmittedSuccessfully));
                    onBackPressed();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(RoutePlanActivity.this);
                Log.e("error", String.valueOf(error));
                Toast.makeText(RoutePlanActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
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
