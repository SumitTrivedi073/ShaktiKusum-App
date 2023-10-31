package activity;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.gson.reflect.TypeToken;
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapter.SurveyListAdapter;
import bean.SurveyListModel;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.WebURL;

public class KusumCSurveyListActivty extends AppCompatActivity {

    private RecyclerView pendingFeedbackList;
    private Toolbar mToolbar;
    TextView noDataFound;
    SearchView searchUser;
    RelativeLayout searchRelative;
    SurveyListAdapter surveyListAdapter;

    List<SurveyListModel.Response> surveyList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kusum_csurvey_list);

        Init();
        listner();
    }

  
    private void Init() {
        mToolbar =  findViewById(R.id.toolbar);
        pendingFeedbackList = findViewById(R.id.surveylist);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.sitesurvey_C));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CustomUtility.isInternetOn(getApplicationContext())) {
            if(CustomUtility.getSharedPreferences(KusumCSurveyListActivty.this,Constant.currentDate)!=null
            && !CustomUtility.getSharedPreferences(KusumCSurveyListActivty.this,Constant.currentDate).isEmpty()
                    &&CustomUtility.getSharedPreferences(KusumCSurveyListActivty.this,Constant.currentDate).equals(CustomUtility.getCurrentDate())) {

                getDataFromLocal();
            }else {
                getSurveyList();
            }
        }else {
           getDataFromLocal();
        }
    }

    private void getDataFromLocal() {
        String json = CustomUtility.getSharedPreferences(KusumCSurveyListActivty.this, Constant.surveyList);
        Type type = new TypeToken<ArrayList<SurveyListModel.Response>>() {}.getType();

        surveyList = new Gson().fromJson(json, type);

            setAdapter();

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
                if (surveyListAdapter != null) {
                    if(!query.isEmpty()) {
                        surveyListAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (surveyListAdapter != null) {
                    if(!newText.isEmpty()) {
                        surveyListAdapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });

        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchUser.onActionViewCollapsed();
                if (surveyListAdapter != null) {
                    surveyListAdapter.getFilter().filter("");
                }
            }
        });

    }



    private void getSurveyList() {
        CustomUtility.showProgressDialogue(KusumCSurveyListActivty.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("URL====>",WebURL.GET_SURVEY_API +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01");
        surveyList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.GET_SURVEY_API +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  response) {


                if(response.toString()!=null && !response.toString().isEmpty()) {
                    SurveyListModel surveyListModel = new Gson().fromJson(response.toString(), SurveyListModel.class);
                    if(surveyListModel.getStatus().equals("true")) {
                        surveyList = surveyListModel.getResponse();
                        CustomUtility.setSharedPreference(KusumCSurveyListActivty.this,Constant.currentDate,CustomUtility.getCurrentDate());
                        CustomUtility.saveSurveyArrayList(KusumCSurveyListActivty.this,surveyList, Constant.surveyList);
                         setAdapter();


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
                CustomUtility.hideProgressDialog(KusumCSurveyListActivty.this);
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

    private void setAdapter() {
        if(surveyList!=null&& surveyList.size()>0) {
            surveyListAdapter = new SurveyListAdapter(getApplicationContext(), surveyList, noDataFound);
            pendingFeedbackList.setHasFixedSize(true);
            pendingFeedbackList.setAdapter(surveyListAdapter);
            noDataFound.setVisibility(View.GONE);
            pendingFeedbackList.setVisibility(View.VISIBLE);
            CustomUtility.hideProgressDialog(KusumCSurveyListActivty.this);

        }else {
            noDataFound.setVisibility(View.VISIBLE);
            pendingFeedbackList.setVisibility(View.GONE);
            CustomUtility.hideProgressDialog(KusumCSurveyListActivty.this);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}