package activity;

import android.app.AlertDialog;
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
import adapter.PendingUnloadVerificationAdapter;
import debugapp.GlobalValue.Constant;
import debugapp.PendingFeedback;
import debugapp.UnloadingFeedbackModel;
import debugapp.VerificationCodeModel;
import utility.CustomUtility;
import webservice.WebURL;

public class PendingUnloadVerificationActivity extends BaseActivity implements PendingUnloadVerificationAdapter.SendOTPListner{

    private  RecyclerView pendingFeedbackList;
    private Toolbar mToolbar;
    ArrayList<PendingFeedback> pendingFeedbacks;
    AlertDialog alertDialog;

    TextView noDataFound;

    SearchView searchUser;

    PendingUnloadVerificationAdapter pendingUnloadVerificationAdapter;

    RelativeLayout searchRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_feedbacck);

        Init();
        listner();

    }


    private void Init() {
        mToolbar =  findViewById(R.id.toolbar);
        pendingFeedbackList = findViewById(R.id.pendingfeedbacklist);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.pendingUnloadingVerification));
        if(CustomUtility.isInternetOn(getApplicationContext())) {
            getUnloadingFeedbackList();
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
                if (pendingUnloadVerificationAdapter != null) {
                    if(!query.isEmpty()) {
                        pendingUnloadVerificationAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (pendingUnloadVerificationAdapter != null) {
                    if(!newText.isEmpty()) {
                        pendingUnloadVerificationAdapter.getFilter().filter(newText);
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


/* param.add(new BasicNameValuePair("USERID", CustomUtility.getSharedPreferences(context, "userid")));
            param.add(new BasicNameValuePair("PROJECT_NO", CustomUtility.getSharedPreferences(context, "projectid")));
            param.add(new BasicNameValuePair("PROJECT_LOGIN_NO", CustomUtility.getSharedPreferences(context, "loginid")));*/
    private void getUnloadingFeedbackList() {
        CustomUtility.showProgressDialogue(PendingUnloadVerificationActivity.this);
        pendingFeedbacks = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Log.e("URL========>",WebURL.unloading_list_verification_pend +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.unloading_list_verification_pend +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01", null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject  response) {
                CustomUtility.hideProgressDialog(PendingUnloadVerificationActivity.this);


                if(response.toString()!=null && !response.toString().isEmpty()) {
                    UnloadingFeedbackModel pendingFeedback = new Gson().fromJson(response.toString(), UnloadingFeedbackModel.class);
                    if(pendingFeedback.getInstallationData()!=null && pendingFeedback.getInstallationData().size()>0) {

                         pendingUnloadVerificationAdapter = new PendingUnloadVerificationAdapter(getApplicationContext(),pendingFeedback.getInstallationData(),noDataFound);
                        pendingFeedbackList.setHasFixedSize(true);
                        pendingFeedbackList.setAdapter(pendingUnloadVerificationAdapter);
                        pendingUnloadVerificationAdapter.SendOTP(PendingUnloadVerificationActivity.this);
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
                CustomUtility.hideProgressDialog(PendingUnloadVerificationActivity.this);
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
    public void sendOtpListener(List<UnloadingFeedbackModel.InstallationDatum> datumList,int position, String generatedVerificationCode) {

        if(CustomUtility.isValidMobile(datumList.get(position).getContactNo())) {
            sendVerificationCodeAPI(datumList.get(position),generatedVerificationCode);
        }else {
            CustomUtility.ShowToast(getResources().getString(R.string.mobile_number_not_valid), PendingUnloadVerificationActivity.this);
        }

    }

    private void sendVerificationCodeAPI(UnloadingFeedbackModel.InstallationDatum response, String generatedVerificationCode) {
        CustomUtility.showProgressDialogue(PendingUnloadVerificationActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.SendOTP + "&mobiles=" +response.getContactNo() +
                        "&message=प्रिय ग्राहक, आपको (shakti energy solution private limited pithampur) द्वारा "+response.getHp() +"HP का पूरा सिस्टम आपके कस्टमर -आय डी "+ response.getBeneficiary() +" के तहत भेज दिया गया है। यदि भेजा गया सिस्टम सफलतापूर्वक आपको पूरा प्राप्त हुआ है तो (shakti energy solution private limited pithampur) द्वारा अधिकृत इंस्टॉलेशन टीम को OTP-"+ generatedVerificationCode +" शेयर कर पुष्टि करे। शक्ति पम्पस&sender=SHAKTl&unicode=1&route=2&country=91&DLT_TE_ID=1707169347351235207",

                null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject  res) {
                CustomUtility.hideProgressDialog(PendingUnloadVerificationActivity.this);


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
                CustomUtility.hideProgressDialog(PendingUnloadVerificationActivity.this);
                Log.e("error", String.valueOf(error));
                Toast.makeText(PendingUnloadVerificationActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }


    private void ShowAlertResponse(UnloadingFeedbackModel.InstallationDatum response, String generatedVerificationCode) {
        LayoutInflater inflater = (LayoutInflater) PendingUnloadVerificationActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(PendingUnloadVerificationActivity.this, R.style.MyDialogTheme);

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
                Intent intent = new Intent(PendingUnloadVerificationActivity.this, PendingFeedBackOTPVerification.class);
                intent.putExtra(Constant.PendingFeedbackContact,response.getContactNo());
                intent.putExtra(Constant.PendingFeedbackVblen,response.getVbeln());
                intent.putExtra(Constant.PendingFeedbackHp,response.getHp());
                intent.putExtra(Constant.PendingFeedbackBeneficiary,response.getBeneficiary());
                intent.putExtra(Constant.VerificationCode,generatedVerificationCode);
                intent.putExtra(Constant.regisno,response.getRegisno());
                intent.putExtra(Constant.isUnloading ,"true");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                /* intent.putExtra(Constant.PendingFeedbackVblen,response.getVbeln());
                intent.putExtra(Constant.PendingFeedbackHp,response.getHp());
                intent.putExtra(Constant.PendingFeedbackBeneficiary,response.getBeneficiary());
                intent.putExtra(Constant.VerificationCode,generatedVerificationCode);*/
            }
        });

    }
}