package activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONObject;

import bean.DeviceDetailModel;
import utility.CustomUtility;
import webservice.WebURL;

public class CheckRMSActivity extends BaseActivity {

    TextView deviceno, cust_nam, custphno, operatornam, deviceonline, motorstatus;
    EditText controllerExt;
    RelativeLayout searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_rmsactivity);

        Init();

    }

    private void Init() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Check Device Status");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        deviceno = findViewById(R.id.device_no);
        cust_nam = findViewById(R.id.cust_nam);
        custphno = findViewById(R.id.cust_mb);
        operatornam = findViewById(R.id.operator);
        deviceonline = findViewById(R.id.dev_status);
        motorstatus = findViewById(R.id.motr_stats);
        controllerExt = findViewById(R.id.controllerExt);
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (controllerExt.getText().toString().isEmpty()) {
                    CustomUtility.ShowToast("Please Enter Controller ID first!", CheckRMSActivity.this);
                } else {
                    getDeviceDetail();
                }
            }
        });
    }


    public void getDeviceDetail() {
        CustomUtility.showProgressDialogue(CheckRMSActivity.this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                WebURL.DEVICE_DETAILS + "?DeviceNo=" + controllerExt.getText().toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(CheckRMSActivity.this);

                if (!response.toString().isEmpty()) {
                    Log.e("response======>", response.toString());
                    DeviceDetailModel deviceDetailModel = new Gson().fromJson(response.toString(), DeviceDetailModel.class);
                    if (deviceDetailModel != null && deviceDetailModel.getResponse() != null && String.valueOf(deviceDetailModel.getStatus()).equals("true")) {

                        if (deviceDetailModel.getResponse().getDeviceNo() != null && !deviceDetailModel.getResponse().getDeviceNo().isEmpty()) {
                            deviceno.setText(deviceDetailModel.getResponse().getDeviceNo());
                        }
                        if (deviceDetailModel.getResponse().getCustomerName() != null && !deviceDetailModel.getResponse().getCustomerName().isEmpty()) {
                            cust_nam.setText(deviceDetailModel.getResponse().getCustomerName());
                        }
                        if (deviceDetailModel.getResponse().getCustomerPhoneNo() != null && !deviceDetailModel.getResponse().getCustomerPhoneNo().isEmpty()) {
                            custphno.setText(deviceDetailModel.getResponse().getCustomerPhoneNo());
                        }
                        if (deviceDetailModel.getResponse().getOperatorName() != null && !deviceDetailModel.getResponse().getOperatorName().isEmpty()) {
                            operatornam.setText(deviceDetailModel.getResponse().getOperatorName());
                        }

                        if (deviceDetailModel.getResponse().getOperatorName() != null && !deviceDetailModel.getResponse().getOperatorName().isEmpty()) {
                            operatornam.setText(deviceDetailModel.getResponse().getOperatorName());
                        }

                        if (deviceDetailModel.getResponse().getIsLogin()) {
                            deviceonline.setText(getResources().getString(R.string.online));
                            deviceonline.setTextColor(Color.parseColor("#00FF00"));
                        } else {
                            deviceonline.setText(getResources().getString(R.string.offline));
                            deviceonline.setTextColor(Color.parseColor("#FF0000"));
                        }

                        if (deviceDetailModel.getResponse().getPumpStatus()) {
                            motorstatus.setText(getResources().getString(R.string.online));
                            motorstatus.setTextColor(Color.parseColor("#00FF00"));
                        } else {
                            motorstatus.setText(getResources().getString(R.string.offline));
                            motorstatus.setTextColor(Color.parseColor("#FF0000"));
                        }


                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(CheckRMSActivity.this);
                if (error.getMessage() != null && !error.getMessage().isEmpty()) {
                    CustomUtility.ShowToast(error.getMessage(), CheckRMSActivity.this);

                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}