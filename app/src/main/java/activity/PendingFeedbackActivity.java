package activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

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

import adapter.PendingFeedbackAdapter;
import debugapp.PendingFeedback;
import debugapp.VerificationCodeModel;
import utility.CustomUtility;
import webservice.WebURL;

public class PendingFeedbackActivity extends AppCompatActivity implements PendingFeedbackAdapter.SendOTPListner{

    private  RecyclerView pendingFeedbackList;
    private Toolbar mToolbar;
    ArrayList<PendingFeedback> pendingFeedbacks;
    AlertDialog alertDialog;

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

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.pendingFeedback));
        getPendingFeedbackList();
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private void getPendingFeedbackList() {
        CustomUtility.showProgressDialogue(PendingFeedbackActivity.this);
        pendingFeedbacks = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.PendingFeedback +"?project_no="+CustomUtility.getSharedPreferences(getApplicationContext(), "projectid")+"&userid="+CustomUtility.getSharedPreferences(getApplicationContext(), "userid")+"&project_login_no=01", null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject  response) {
                CustomUtility.hideProgressDialog(PendingFeedbackActivity.this);


                if(response.toString()!=null && !response.toString().isEmpty()) {
                    PendingFeedback pendingFeedback = new Gson().fromJson(response.toString(), PendingFeedback.class);
                    if(pendingFeedback.getStatus().equals("true")) {

                        PendingFeedbackAdapter pendingFeedbackAdapter = new PendingFeedbackAdapter(getApplicationContext(),pendingFeedback.getResponse());
                        pendingFeedbackList.setHasFixedSize(true);
                        pendingFeedbackList.setAdapter(pendingFeedbackAdapter);
                        pendingFeedbackAdapter.SendOTP(PendingFeedbackActivity.this);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(PendingFeedbackActivity.this);
                Log.e("error", String.valueOf(error));
                Toast.makeText(PendingFeedbackActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void sendOtpListener(PendingFeedback.Response response, String generatedVerificationCode) {
        sendVerificationCodeAPI(response,generatedVerificationCode);
    }

    private void sendVerificationCodeAPI(PendingFeedback.Response response, String generatedVerificationCode) {
        CustomUtility.showProgressDialogue(PendingFeedbackActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.SendOTP +"&mobiles="+response.getContactNo()+
                        "&message=प्रिय उपभोक्ता, शक्ति पम्प इंस्टालेशन टीम द्वारा सोलर पम्प सफलतापूर्वक इनस्टॉल कर दिया गया है यदि आप इंस्टालेशन से संतुष्ट है " +
                        "तो इंस्टालेशन टीम को वेरिफिकेशन कोड बताये और यदि संतुष्ट नहीं है तो कृपया इंस्टालेशन टीम को मोबाइल एप्प में कारण दर्ज करवाये " +
                        "Verification Code - "+generatedVerificationCode+" . शक्ति पम्प&sender=SHAKTl&unicode=1&route=2&unicode=1&country=91&DLT_TE_ID=1707165768934110550",

                null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject  response) {
                CustomUtility.hideProgressDialog(PendingFeedbackActivity.this);


                if(response.toString()!=null && !response.toString().isEmpty()) {
                    VerificationCodeModel verificationCodeModel = new Gson().fromJson(response.toString(), VerificationCodeModel.class);
                    if(verificationCodeModel.getStatus().equals("Success")) {

                        ShowAlertResponse();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(PendingFeedbackActivity.this);
                Log.e("error", String.valueOf(error));
                Toast.makeText(PendingFeedbackActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void ShowAlertResponse() {
        LayoutInflater inflater = (LayoutInflater) PendingFeedbackActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(PendingFeedbackActivity.this, R.style.MyDialogTheme);

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
                Intent intent = new Intent(PendingFeedbackActivity.this, PendingFeedBackOTPVerification.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }
}