package com.shaktipumplimited.shaktikusum.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;
import com.shaktipumplimited.shaktikusum.debugapp.GlobalValue.Constant;
import com.shaktipumplimited.shaktikusum.debugapp.VerificationCodeModel;
import com.shaktipumplimited.shaktikusum.utility.CustomUtility;
import com.shaktipumplimited.shaktikusum.webservice.WebURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PendingFeedBackOTPVerification extends AppCompatActivity {

    Toolbar mToolbar;
    LinearLayout resendLinear;
    TextView countdownTxt, resend_btn, verifyOTP;
    EditText et_verification_code;

    String verificationCode,contactNumber,vblen,Hp,beneficiary;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_feed_back_otpverification);

        Init();
        listner();
    }


    private void Init() {
        mToolbar = findViewById(R.id.toolbar);
        et_verification_code = findViewById(R.id.et_verification_code);
        resendLinear = findViewById(R.id.resendLinear);
        countdownTxt = findViewById(R.id.countdownTxt);
        resend_btn = findViewById(R.id.resend_btn);
        verifyOTP = findViewById(R.id.verifyOTP);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.otpVerification));

      //  response = getIntent().getParcelableExtra(Constant.PendingFeedback);
        contactNumber =   getIntent().getStringExtra(Constant.PendingFeedbackContact);
        vblen =   getIntent().getStringExtra(Constant.PendingFeedbackVblen);
        Hp =    getIntent().getStringExtra(Constant.PendingFeedbackHp);
        beneficiary =   getIntent().getStringExtra(Constant.PendingFeedbackBeneficiary);
        verificationCode = getIntent().getStringExtra(Constant.VerificationCode);
        countDownTimer();
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                verificationCode = String.format("%04d", random.nextInt(10000));
                sendVerificationCodeAPI(verificationCode);
            }
        });

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_verification_code.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Verification Code", Toast.LENGTH_LONG).show();
                } else if (!verificationCode.equals(et_verification_code.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Please Enter Correct Verification Code", Toast.LENGTH_LONG).show();
                }else {
                         saveOtpToServer(vblen,et_verification_code.getText().toString());
                }
            }
        });
    }

    private void saveOtpToServer(String vbeln, String ver_otp) {
       /* https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_solar_pro/save_feedback.htm?feedback={"vbeln":"1234",
        "ver_otp":"1234"}*/
        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put("vbeln", vbeln);
            mainObject.put("ver_otp", ver_otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomUtility.showProgressDialogue(PendingFeedBackOTPVerification.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.SendOTPToServer + mainObject.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                CustomUtility.hideProgressDialog(PendingFeedBackOTPVerification.this);


                if (!res.toString().isEmpty()) {

                        ShowAlertResponse("1");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(PendingFeedBackOTPVerification.this);
                Log.e("error", String.valueOf(error));
                Toast.makeText(PendingFeedBackOTPVerification.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void sendVerificationCodeAPI( String generatedVerificationCode) {
        CustomUtility.showProgressDialogue(PendingFeedBackOTPVerification.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.SendOTP + "&mobiles=" +contactNumber+ "&message=प्रिय ग्राहक, आप अपने खेत में शक्ति पंप्स (इंडिया) लिमिटेड द्वारा " +
                        "इन्सटाल्ड " + Hp + " HP रेटिंग सोलर पंप सेट के लिए कस्टमर आईडी " + beneficiary + " के सम्बन्ध में यह मेसेज प्राप्त कर रहे हैं। " +
                        "यह मैसेज आपको फीडबैक के उद्देश्य से दिया जा रहा है । शक्ति पंप इंस्टालर को OTP शेयर करके आप निचे दिए पॉइंट की पुष्टि कर रहे हैं " +
                        "कि (1) आप इंस्टालेशन की क्वालिटी से संतुष्ट हैं। (2) आप सोलर पंप सेट के परफॉरमेंस से संतुष्ट हैं । (3) इंस्टालर द्वारा सोलर सिस्टम लगाने" +
                        " के लिए आपसे किसी भी प्रकार राशि नहीं ली गई है. यदि ऊपर दिए तीनो पॉइंट सही हैं तो कृपया अपने सोलर पंप सेट की 05 वर्ष की सर्विस " +
                        "को सक्रिय करने के लिए इंस्टालर के साथ " + generatedVerificationCode +
                        " ( OTP) शेयर करे।:&sender=SHAKTl&route=2&country=91&DLT_TE_ID=1707168242000670452&unicode=1",

                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                CustomUtility.hideProgressDialog(PendingFeedBackOTPVerification.this);

                if ( !res.toString().isEmpty()) {
                    VerificationCodeModel verificationCodeModel = new Gson().fromJson(res.toString(), VerificationCodeModel.class);
                    if (verificationCodeModel.getStatus().equals("Success")) {

                        ShowAlertResponse("0");
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(PendingFeedBackOTPVerification.this);
                Log.e("error", String.valueOf(error));
                Toast.makeText(PendingFeedBackOTPVerification.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void ShowAlertResponse(String value) {
        LayoutInflater inflater = (LayoutInflater) PendingFeedBackOTPVerification.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(PendingFeedBackOTPVerification.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);

        if(value.equals("0")) {
            title_txt.setText(getResources().getString(R.string.otp_send_successfully));
        }else {
            title_txt.setText(getResources().getString(R.string.verificationCodeMatched));
        }

        OK_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value.equals("0")) {
                    alertDialog.dismiss();
                    countDownTimer();
                }else {
                    alertDialog.dismiss();
                    Intent intent = new Intent(PendingFeedBackOTPVerification.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

    }

    public void countDownTimer() {
        new CountDownTimer(600000, 1000) {

            @SuppressLint("DefaultLocale")
            public void onTick(long millisUntilFinished) {
                resendLinear.setVisibility(View.GONE);
                countdownTxt.setVisibility(View.VISIBLE);
                countdownTxt.setText("You can send verification code again within :- " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                verificationCode = "";
                countdownTxt.setVisibility(View.GONE);
                resendLinear.setVisibility(View.VISIBLE);
            }
        }.start();

    }


}