package debugapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.shaktipumplimited.retrofit.BaseRequest;
import com.shaktipumplimited.retrofit.RequestReciever;
import activity.MainActivity;
import com.shaktipumplimited.shaktikusum.R;

import utility.CustomUtility;


public class OTPGenerationActivity extends AppCompatActivity {

    Context mContext;

    private RelativeLayout lvlOTPMianID;
    private TextView txtPODID;
    private TextView txtOTPID;
    private TextView txtSimNumberID;
    private EditText edtINSTNmaeID;
    private EditText edtOTPID;
    private EditText edtINSTNumberID;
    private String edtOTPIDSTR;
    private String edtINSTNmaeIDSTR;
    private String edtINSTNumberIDSTR;
    private String mORG_OTP_VALUE = "";
    private BaseRequest baseRequest;

    @SuppressLint("MissingInflatedId")
    @Override
    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_genration);
        mContext = this;

        baseRequest = new BaseRequest(this);

        lvlOTPMianID = findViewById(R.id.lvlOTPMianID);
        txtOTPID = (TextView) findViewById(R.id.txtOTPID);
        txtPODID = (TextView) findViewById(R.id.txtPODID);
        txtSimNumberID = (TextView) findViewById(R.id.txtSimNumberID);
        edtOTPID = findViewById(R.id.edtOTPID);
        edtINSTNmaeID = findViewById(R.id.edtINSTNmaeID);
        edtINSTNumberID = findViewById(R.id.edtINSTNumberID);

        txtPODID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                edtINSTNmaeIDSTR = edtINSTNmaeID.getText().toString().trim();
                edtINSTNumberIDSTR = edtINSTNumberID.getText().toString().trim();

                if (edtINSTNmaeIDSTR.equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "Please enter your name.", Toast.LENGTH_SHORT).show();
                } else if (edtINSTNumberIDSTR.equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "Please enter your mobile number.", Toast.LENGTH_SHORT).show();
                } else {

                    CustomUtility.setSharedPreference(mContext, "InstallerName", edtINSTNmaeIDSTR);
                    CustomUtility.setSharedPreference(mContext, "InstallerMOB", edtINSTNumberIDSTR);
                    mORG_OTP_VALUE = getRandomNumberString();
                    lvlOTPMianID.setVisibility(View.VISIBLE);
                    txtOTPID.setVisibility(View.VISIBLE);
                    callInsertAndUpdateDebugDataAPI();

                }
            }
        });
        txtOTPID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                edtOTPIDSTR = edtOTPID.getText().toString().trim();
                if (mORG_OTP_VALUE.equalsIgnoreCase(edtOTPIDSTR)) {
                      Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Please enter valid OTP.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(9999);

        // this will convert any number sequence into 6 character.
        return String.format("%04d", number);
    }

    private void callInsertAndUpdateDebugDataAPI() {
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int APINumber, String Json, Object obj) {
                //  JSONArray arr = (JSONArray) obj;
                try {
                    if (!obj.toString().isEmpty()) {
                        Toast.makeText(mContext, "OTP send successfully", Toast.LENGTH_LONG).show();
                        CustomUtility.setSharedPreference(mContext, "InstallerName", edtINSTNmaeIDSTR);
                        CustomUtility.setSharedPreference(mContext, "InstallerMOB", edtINSTNumberIDSTR);
                        mORG_OTP_VALUE = getRandomNumberString();
                        lvlOTPMianID.setVisibility(View.VISIBLE);
                        txtOTPID.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(mContext, "OTP send failed please try again.", Toast.LENGTH_LONG).show();
                    }
                    baseRequest.hideLoader();
                    //  getDeviceSettingListResponse(mSettingModelView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int APINumber, String errorCode, String message) {
                baseRequest.hideLoader();
             //   Toast.makeText(mContext, "OTP send successfully.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkFailure(int APINumber, String message) {
                baseRequest.hideLoader();
                Toast.makeText(mContext, "Please check internet connection!", Toast.LENGTH_LONG).show();
            }
        });

        Map<String, String> wordsByKey = new HashMap<>();
        System.out.println("jsonObject==>>" + wordsByKey);
        //baseRequest.callAPIGET(1, wordsByKey, NewSolarVFD.GET_DEVICE_SIM_NUMBER_API);/////
       //old baseRequest.callAPIGETDirectURL(1, "http://login.yourbulksms.com/api/sendhttp.php?authkey=8716AQbKpjEHR5b4479de&mobiles=" + edtINSTNumberIDSTR + "&message=Enter The Following OTP To Verify Your Account " + mORG_OTP_VALUE + " SHAKTI&sender=SHAKTl&route=4&country=91&DLT_TE_ID=1707161675029844457");/////
         baseRequest.callAPIGETDirectURL(1, "http://control.yourbulksms.com/api/sendhttp.php?authkey=393770756d707334373701&mobiles="+edtINSTNumberIDSTR+"&message=प्रिय उपभोक्ता, शक्ति पम्प इंस्टालेशन टीम द्वारा सोलर पम्प सफलतापूर्वक इनस्टॉल कर दिया गया है यदि आप इंस्टालेशन से संतुष्ट है तो इंस्टालेशन टीम को OTP बताये और यदि संतुष्ट नहीं है तो कृपया इंस्टालेशन टीम को मोबाइल एप्प में कारण दर्ज करवाये OTP NO - "+ mORG_OTP_VALUE +" . शक्ति पम्प&sender=SHAKTl&unicode=1&route=2&unicode=1&country=91&DLT_TE_ID=1707165768934110550");/////
    }
}