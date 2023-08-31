package activity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import debugapp.GlobalValue.Constant;
import debugapp.VerificationCodeModel;
import utility.CustomUtility;
import webservice.WebURL;

public class UnloadingOTPVerification extends BaseActivity {

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

        contactNumber =   getIntent().getStringExtra(Constant.PendingFeedbackContact);
        vblen =   getIntent().getStringExtra(Constant.PendingFeedbackVblen);
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

        CustomUtility.showProgressDialogue(UnloadingOTPVerification.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.SendOTPToServer + mainObject, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                CustomUtility.hideProgressDialog(UnloadingOTPVerification.this);


                if (!res.toString().isEmpty()) {
                 ShowAlertResponse("1");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(UnloadingOTPVerification.this);
                Log.e("error", String.valueOf(error));
                Toast.makeText(UnloadingOTPVerification.this, error.getMessage(),
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
        CustomUtility.showProgressDialogue(UnloadingOTPVerification.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.SendOTP + "&mobiles=" +contactNumber+
                        "&message=प्रिय ग्राहक, आपको (shakti energy solution private limited pithampur) द्वारा 5 HP का पूरा सिस्टम आपके कस्टमर -आय डी "+ beneficiary +" के तहत भेज दिया गया है। यदि भेजा गया सिस्टम सफलतापूर्वक आपको पूरा प्राप्त हुआ है तो (shakti energy solution private limited pithampur) द्वारा अधिकृत इंस्टॉलेशन टीम को OTP-"+ generatedVerificationCode+" शेयर कर पुष्टि करे। शक्ति पम्पस&sender=SHAKTl&unicode=1&route=2&country=91&DLT_TE_ID=1707169347351235207",

           /*http://control.yourbulksms.com/api/sendhttp.php?authkey=393770756d707334373701&mobiles=7869901656&message=प्रिय ग्राहक, आपको (shakti energy solution private limited pithampur) द्वारा 120.5 HP का पूरा सिस्टम आपके कस्टमर -आय डी SWPS/2022/00009895 के तहत भेज दिया गया है। यदि भेजा गया सिस्टम सफलतापूर्वक आपको पूरा प्राप्त हुआ है तो (shakti energy solution private limited pithampur) द्वारा अधिकृत इंस्टॉलेशन टीम को OTP-1234 शेयर कर पुष्टि करे। शक्ति पम्पस&sender=SHAKTl&unicode=1&route=2&country=91&DLT_TE_ID=1707169347351235207*/


                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                CustomUtility.hideProgressDialog(UnloadingOTPVerification.this);


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
                CustomUtility.hideProgressDialog(UnloadingOTPVerification.this);
                Log.e("error", String.valueOf(error));
                Toast.makeText(UnloadingOTPVerification.this, error.getMessage(),
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
        LayoutInflater inflater = (LayoutInflater) UnloadingOTPVerification.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(UnloadingOTPVerification.this, R.style.MyDialogTheme);

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
                    Intent intent = new Intent(UnloadingOTPVerification.this,MainActivity.class);
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