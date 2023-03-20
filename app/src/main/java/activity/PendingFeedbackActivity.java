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
import debugapp.GlobalValue.Constant;
import debugapp.PendingFeedback;
import debugapp.VerificationCodeModel;
import utility.CustomUtility;
import webservice.WebURL;

public class PendingFeedbackActivity extends AppCompatActivity implements PendingFeedbackAdapter.SendOTPListner{

    private  RecyclerView pendingFeedbackList;
    private Toolbar mToolbar;
    ArrayList<PendingFeedback> pendingFeedbacks;
    AlertDialog alertDialog;

    TextView noDataFound;

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
                CustomUtility.hideProgressDialog(PendingFeedbackActivity.this);
                noDataFound.setVisibility(View.VISIBLE);
                pendingFeedbackList.setVisibility(View.GONE);
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
                WebURL.SendOTP +"&mobiles=918770957105"+
                        "&message=आप अपने खेत में शक्ति पम्प्स (इंडिया) लिमिटेड द्वारा स्थापित "+response.getHp()+" एचपी रेटिंग सोलर पंप सेट के लिए लाभार्थी आईडी "+response.getBeneficiary()+" के संदर्भ में यह संदेश प्राप्त कर रहे हैं।" +
                        " यह संदेश केवल आपकी प्रतिक्रिया के उद्देश्य से है शक्ति पंप्स इंस्टालर को सत्यपान कोड साझा करके आप निम्नलिखित की पुष्टि कर रहे हैं 1) आप स्थापना की गुणवत्ता से संतुष्ट हैं" +
                        " 2) आप सोलर पंप सेट के प्रदर्शन से संतुष्ट हैं 3) इंस्टॉलर ने किसी भी प्रकार की सामग्री या स्थापना कार्य के लिए कोई राशि नहीं ली हैं यदि उपरोक्त सभी तीन कथन सही हैं, " +
                        "तो कृपया अपने सोलर पम्प सेट की 5 वर्ष की सेवा को सक्रिय करने के लिए इंस्टॉलर के साथ सत्यपान कोड "+generatedVerificationCode+" साझा करें।:&sender=SHAKTl&route=2&country=91&DLT_TE_ID=1707167928540679513&unicode=1",

                null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject  res) {
                CustomUtility.hideProgressDialog(PendingFeedbackActivity.this);


                if(response.toString()!=null && !res.toString().isEmpty()) {
                    VerificationCodeModel verificationCodeModel = new Gson().fromJson(res.toString(), VerificationCodeModel.class);
                    if(verificationCodeModel.getStatus().equals("Success")) {

                        ShowAlertResponse(response,generatedVerificationCode);
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


    private void ShowAlertResponse(PendingFeedback.Response response, String generatedVerificationCode) {
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
                intent.putExtra(Constant.PendingFeedback,response);
                intent.putExtra(Constant.VerificationCode,generatedVerificationCode);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }
}