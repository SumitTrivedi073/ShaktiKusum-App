package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
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

import adapter.DeviceShiftingAdapter;
import bean.DeviceShiftingModel;
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.DeviceShiftingList +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  response) {
                if(response.toString()!=null && !response.toString().isEmpty()) {
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
                2,  // maxNumRetries = 0 means no retry
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

    }/*

    private void sendVerificationCodeAPI(DeviceShiftingModel.Response response, String generatedVerificationCode) {
        CustomUtility.showProgressDialogue(DeviceShiftingActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("PendingInstalltionURL====>",WebURL.SendOTP +"&mobiles="+response.getContactNo()+
                "&message="+response.getBeneficiary()+" के तहत "+response.getHp()+"HP पंप सेट का इंस्टॉलेशन किया गया है यदि आप संतुष्ट हैं तो इंस्टॉलेशन टीम को OTP-"+generatedVerificationCode+" शेयर करे। शक्ति पम्पस&sender=SHAKTl&unicode=1&route=2&country=91&DLT_TE_ID=1707169744934483345"
        );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.SendOTP +"&mobiles="+response.getContactNo()+
                        "&message="+response.getBeneficiary()+" के तहत "+response.getHp()+"HP पंप सेट का इंस्टॉलेशन किया गया है यदि आप संतुष्ट हैं तो इंस्टॉलेशन टीम को OTP-"+generatedVerificationCode+" शेयर करे। शक्ति पम्पस&sender=SHAKTl&unicode=1&route=2&country=91&DLT_TE_ID=1707169744934483345",

                null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject  res) {
                CustomUtility.hideProgressDialog(DeviceShiftingActivity.this);


                if(res.toString()!=null && !res.toString().isEmpty()) {
                    VerificationCodeModel verificationCodeModel = new Gson().fromJson(res.toString(), VerificationCodeModel.class);
                    if(verificationCodeModel.getStatus().equals("Success")) {

                        ShowAlertResponse(response,generatedVerificationCode);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(DeviceShiftingActivity.this);
                Log.e("error", String.valueOf(error));
                Toast.makeText(DeviceShiftingActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }


    private void ShowAlertResponse(DeviceShiftingModel.Response response, String generatedVerificationCode) {
        LayoutInflater inflater = (LayoutInflater) DeviceShiftingActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(DeviceShiftingActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);

        title_txt.setText(getResources().getString(R.string.otp_send_successfully));

        OK_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(DeviceShiftingActivity.this, PendingInsUnlOTPVerification.class);
                intent.putExtra(Constant.PendingFeedbackContact,response.getContactNo());
                intent.putExtra(Constant.PendingFeedbackVblen,response.getVbeln());
                intent.putExtra(Constant.PendingFeedbackHp,response.getHp());
                intent.putExtra(Constant.PendingFeedbackBeneficiary,response.getBeneficiary());
                intent.putExtra(Constant.VerificationCode,generatedVerificationCode);
                intent.putExtra(Constant.regisno,response.getRegisno());
                intent.putExtra(Constant.isUnloading ,"false");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                *//* intent.putExtra(Constant.PendingFeedbackVblen,response.getVbeln());
                intent.putExtra(Constant.PendingFeedbackHp,response.getHp());
                intent.putExtra(Constant.PendingFeedbackBeneficiary,response.getBeneficiary());
                intent.putExtra(Constant.VerificationCode,generatedVerificationCode);*//*
            }
        });

    }*/
    
}