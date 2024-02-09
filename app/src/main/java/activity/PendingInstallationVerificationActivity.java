package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
import java.util.Random;

import adapter.PendingFeedbackAdapter;
import debugapp.GlobalValue.Constant;
import debugapp.PendingInstallationModel;
import debugapp.VerificationCodeModel;
import utility.CustomUtility;
import webservice.WebURL;

public class PendingInstallationVerificationActivity extends BaseActivity implements PendingFeedbackAdapter.SendOTPListner{

    private  RecyclerView pendingFeedbackList;
    private Toolbar mToolbar;
    ArrayList<PendingInstallationModel> pendingInstallationModels;
    AlertDialog alertDialog;

    TextView noDataFound;

    SearchView searchUser;

    PendingFeedbackAdapter pendingFeedbackAdapter;

    RelativeLayout searchRelative;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_installation_verification);

        Init();
        listner();

    }


    private void Init() {
        progressDialog = new ProgressDialog(this);
        mToolbar = findViewById(R.id.toolbar);
        pendingFeedbackList = findViewById(R.id.pendingfeedbacklist);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.pendingInstallationVerification));
        if(CustomUtility.isInternetOn(getApplicationContext())) {
            getPendingInstallationList();
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
                if (pendingFeedbackAdapter != null) {
                    if(!query.isEmpty()) {
                        pendingFeedbackAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (pendingFeedbackAdapter != null) {
                    if(!newText.isEmpty()) {
                        pendingFeedbackAdapter.getFilter().filter(newText);
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

    /*-------------------------------------------------------------Pending Installation List-----------------------------------------------------------------------------*/


    private void getPendingInstallationList() {
        CustomUtility.showProgressDialogue(PendingInstallationVerificationActivity.this);
        pendingInstallationModels = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("PendingInstallation=====>",WebURL.PendingFeedback +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.PendingFeedback +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01", null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject  response) {
                CustomUtility.hideProgressDialog(PendingInstallationVerificationActivity.this);


                if(response.toString()!=null && !response.toString().isEmpty()) {
                    PendingInstallationModel pendingInstallationModel = new Gson().fromJson(response.toString(), PendingInstallationModel.class);
                    if(pendingInstallationModel.getStatus().equals("true")) {

                         pendingFeedbackAdapter = new PendingFeedbackAdapter(getApplicationContext(), pendingInstallationModel.getResponse(),noDataFound);
                        pendingFeedbackList.setHasFixedSize(true);
                        pendingFeedbackList.setAdapter(pendingFeedbackAdapter);
                        pendingFeedbackAdapter.SendOTP(PendingInstallationVerificationActivity.this);
                        noDataFound.setVisibility(View.GONE);
                        pendingFeedbackList.setVisibility(View.VISIBLE);
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
                CustomUtility.hideProgressDialog(PendingInstallationVerificationActivity.this);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void sendOtpListener(List<PendingInstallationModel.Response> pendingFeedbackList, int position) {

        Log.e("BillNo=====>", pendingFeedbackList.get(position).getVbeln());

        String dongleType = pendingFeedbackList.get(position).getDongle().charAt(0) + pendingFeedbackList.get(position).getDongle().substring(1, 2);
      /*   String DongleNo = "99-0070-0-07-06-23";
        String dongleType = DongleNo.charAt(0) + DongleNo.substring(1, 2);;
*/
        //dongle type 99 defile 2G dongles and 6B define 4G dongle
        if (dongleType.equals("99")) {
            Intent intent = new Intent(PendingInstallationVerificationActivity.this, DeviceMappingActivity.class);
            intent.putExtra(Constant.deviceMappingData, pendingFeedbackList.get(position));
            intent.putExtra(Constant.deviceMappingData2, "2");
            startActivity(intent);

        } else {

            if (CustomUtility.isValidMobile(pendingFeedbackList.get(position).getContactNo())) {
                Random random = new Random();
                String generatedVerificationCode = String.format("%04d", random.nextInt(10000));

                if(pendingFeedbackList.get(position).getLatlng()!=null && !pendingFeedbackList.get(position).getLatlng().isEmpty()) {
                    sendLatLngToRmsForFota(pendingFeedbackList.get(position),generatedVerificationCode);
                }else {
                      sendVerificationCodeAPI(pendingFeedbackList.get(position), generatedVerificationCode);
                }
            } else {
                CustomUtility.ShowToast(getResources().getString(R.string.mobile_number_not_valid), PendingInstallationVerificationActivity.this);
            }


        }
    }

    /*-------------------------------------------------------------Send Lat Lng to Rms Server-----------------------------------------------------------------------------*/
    private void sendLatLngToRmsForFota(PendingInstallationModel.Response response, String generatedVerificationCode) {

    //    String [] tempArray = response.getLatlng().substring(response.getLatlng(), response.getLatlng().lastIndexOf(")")).split(",");
        String[] latlong =  response.getLatlng().split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        Log.e("latitude======>", String.valueOf(latitude));
        Log.e("longitude======>", String.valueOf(longitude));

        showProgressDialogue(getResources().getString(R.string.device_initialization_processing));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                CustomUtility.getSharedPreferences(this, Constant.RmsBaseUrl) + WebURL.updateLatLngToRms + "?deviceNo=" + "7F-0135-0-13-06-23-0" + "&lat=" + latitude + "&lon=" + longitude,

                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.toString() != null && !jsonObject.toString().isEmpty()) {

                        String mStatus = jsonObject.getString("status");
                        if (mStatus.equals("true")) {
                            stopProgressDialogue();
                            if (CustomUtility.isValidMobile(response.getContactNo())) {

                                sendVerificationCodeAPI(response, generatedVerificationCode);
                            }
                        } else {
                            stopProgressDialogue();
                            CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), getApplicationContext());


                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    stopProgressDialogue();
                    CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), getApplicationContext());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialogue();
                Log.e("error", String.valueOf(error));
                Toast.makeText(PendingInstallationVerificationActivity.this, error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                5,  /// maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    /*-------------------------------------------------------------Send Verification Code-----------------------------------------------------------------------------*/

    private void sendVerificationCodeAPI(PendingInstallationModel.Response response, String generatedVerificationCode) {
        stopProgressDialogue();
        showProgressDialogue(getResources().getString(R.string.sendingOtpToCustomer));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("PendingInstalltionURL====>", WebURL.SendOTP + "&mobiles=" + response.getContactNo() +
                "&message=" + response.getBeneficiary() + " के तहत " + response.getHp() + "HP पंप सेट का इंस्टॉलेशन किया गया है यदि आप संतुष्ट हैं तो इंस्टॉलेशन टीम को OTP-" + generatedVerificationCode + " शेयर करे। शक्ति पम्पस&sender=SHAKTl&unicode=1&route=2&country=91&DLT_TE_ID=1707169744934483345"
        );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.SendOTP + "&mobiles=" + response.getContactNo() +
                        "&message=" + response.getBeneficiary() + " के तहत " + response.getHp() + "HP पंप सेट का इंस्टॉलेशन किया गया है यदि आप संतुष्ट हैं तो इंस्टॉलेशन टीम को OTP-" + generatedVerificationCode + " शेयर करे। शक्ति पम्पस&sender=SHAKTl&unicode=1&route=2&country=91&DLT_TE_ID=1707169744934483345",

                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  res) {
                stopProgressDialogue();
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
                stopProgressDialogue();
                Log.e("error", String.valueOf(error));
                Toast.makeText(PendingInstallationVerificationActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }


    private void ShowAlertResponse(PendingInstallationModel.Response response, String generatedVerificationCode) {
        LayoutInflater inflater = (LayoutInflater) PendingInstallationVerificationActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(PendingInstallationVerificationActivity.this, R.style.MyDialogTheme);

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
                Intent intent = new Intent(PendingInstallationVerificationActivity.this, PendingInsUnlOTPVerification.class);
                intent.putExtra(Constant.PendingFeedbackContact,response.getContactNo());
                intent.putExtra(Constant.PendingFeedbackVblen,response.getVbeln());
                intent.putExtra(Constant.PendingFeedbackHp,response.getHp());
                intent.putExtra(Constant.PendingFeedbackBeneficiary,response.getBeneficiary());
                intent.putExtra(Constant.VerificationCode, generatedVerificationCode);
                intent.putExtra(Constant.regisno, response.getRegisno());
                intent.putExtra(Constant.isUnloading, "false");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }

    /*-------------------------------------------------------------Show Progress Dialogue-----------------------------------------------------------------------------*/

    public void showProgressDialogue(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(message);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        });

    }

    public void stopProgressDialogue() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }
}