package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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

import adapter.DeviceShiftingAdapter;
import bean.DeviceShiftingModel;
import debugapp.GlobalValue.AllPopupUtil;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.WebURL;

public class DeviceShiftingActivity extends BaseActivity implements DeviceShiftingAdapter.ShiftingListner {

    private RecyclerView DeviceShiftingList;
    private Toolbar mToolbar;
    ArrayList<DeviceShiftingModel> DeviceShiftingModels;
    TextView noDataFound;

    SearchView searchUser;

    DeviceShiftingAdapter deviceShiftingAdapter;

    RelativeLayout searchRelative;

    int SelectedIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_shifting);
        Init();
        listner();
    }



    private void Init() {
        mToolbar =  findViewById(R.id.toolbar);
        DeviceShiftingList = findViewById(R.id.DeviceShiftingList);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.deviceShifting));


        CustomUtility.setSharedPreference(this, Constant.RmsBaseUrl, "https://quality.shaktirms.com/");
        if(CustomUtility.isInternetOn(getApplicationContext())) {
            getDeviceShiftingList();
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
                if (deviceShiftingAdapter != null) {
                    if(!query.isEmpty()) {
                        deviceShiftingAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (deviceShiftingAdapter != null) {
                    if(!newText.isEmpty()) {
                        deviceShiftingAdapter.getFilter().filter(newText);
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



    private void getDeviceShiftingList() {
        CustomUtility.showProgressDialogue(DeviceShiftingActivity.this);
        DeviceShiftingModels = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("DeviceShiftingAPI",WebURL.DeviceShiftingList +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.DeviceShiftingList +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  response) {

                if(!response.toString().isEmpty()) {
                    DeviceShiftingModel deviceShiftingModel = new Gson().fromJson(response.toString(), DeviceShiftingModel.class);
                    if(deviceShiftingModel.getStatus().equals("true")) {

                        deviceShiftingAdapter = new DeviceShiftingAdapter(getApplicationContext(), deviceShiftingModel.getResponse(),noDataFound);
                        DeviceShiftingList.setHasFixedSize(true);
                        DeviceShiftingList.setAdapter(deviceShiftingAdapter);
                        deviceShiftingAdapter.Deviceshifting(DeviceShiftingActivity.this);
                        noDataFound.setVisibility(View.GONE);
                        DeviceShiftingList.setVisibility(View.VISIBLE);
                        CustomUtility.hideProgressDialog(DeviceShiftingActivity.this);

                    }else {
                        noDataFound.setVisibility(View.VISIBLE);
                        DeviceShiftingList.setVisibility(View.GONE);
                        CustomUtility.hideProgressDialog(DeviceShiftingActivity.this);

                    }

                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                    DeviceShiftingList.setVisibility(View.GONE);
                    CustomUtility.hideProgressDialog(DeviceShiftingActivity.this);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(DeviceShiftingActivity.this);
                noDataFound.setVisibility(View.VISIBLE);
                DeviceShiftingList.setVisibility(View.GONE);
                Log.e("error", String.valueOf(error));

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                1,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void shiftingListener(List<DeviceShiftingModel.Response> deviceShiftingModel, int position, String generatedVerificationCode) {

        SelectedIndex = position;


        Intent intent = new Intent(DeviceShiftingActivity.this, DeviceMappingActivity.class);
        intent.putExtra(Constant.deviceMappingData, deviceShiftingModel.get(position));
        startActivity(intent);

    }

    
}