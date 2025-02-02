package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import utility.dialog4;
import webservice.WebURL;


public class DeviceStatusActivity extends BaseActivity  {

    Context mContext;
    dialog4 yourDialog;

    private ProgressDialog progressDialog;
    private DeviceStatusActivity activity;
    TextView deviceno,cust_nam,custphno,operatornam,deviceonline,motorstatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        setContentView(R.layout.activity_devicestatus);
        mContext = this;

        progressDialog = new ProgressDialog(mContext);


        yourDialog = new dialog4(activity,getIntent().getStringExtra(Constant.ControllerSerialNumber));
        yourDialog.show();

       Init();

    }

    private void Init() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        deviceno = findViewById(R.id.device_no);
        cust_nam = findViewById(R.id.cust_nam);
        custphno = findViewById(R.id.cust_mb);
        operatornam = findViewById(R.id.operator);
        deviceonline = findViewById(R.id.dev_status);
        motorstatus = findViewById(R.id.motr_stats);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void searchWord(String textString) {

        if (!textString.isEmpty()) {
            if(CustomUtility.isInternetOn(getApplicationContext())){
                getDeviceDetail(textString);
            }else {
                CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection),getApplicationContext());
            }

        } else {
            Toast.makeText(mContext, "Please Enter Controller Id.", Toast.LENGTH_SHORT).show();
        }

    }

    public void getDeviceDetail(String textString){
        CustomUtility.showProgressDialogue(DeviceStatusActivity.this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                CustomUtility.getSharedPreferences(getApplicationContext(), Constant.RmsBaseUrl)+WebURL.DEVICE_DETAILS +"?DeviceNo="+textString, null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject  response) {
                CustomUtility.hideProgressDialog(DeviceStatusActivity.this);

                if(!response.toString().isEmpty()) {
                    Log.e("response======>",response.toString());
                    DeviceDetailModel deviceDetailModel = new Gson().fromJson(response.toString(),DeviceDetailModel.class);
                            if(deviceDetailModel!=null && deviceDetailModel.getResponse()!=null && String.valueOf(deviceDetailModel.getStatus()).equals("true")) {

                                if(deviceDetailModel.getResponse().getDeviceNo()!=null && !deviceDetailModel.getResponse().getDeviceNo().isEmpty()) {
                                    deviceno.setText(deviceDetailModel.getResponse().getDeviceNo());
                                }
                                if(deviceDetailModel.getResponse().getCustomerName()!=null && !deviceDetailModel.getResponse().getCustomerName().isEmpty()) {
                                    cust_nam.setText(deviceDetailModel.getResponse().getCustomerName());
                                }
                                if(deviceDetailModel.getResponse().getCustomerPhoneNo()!=null && !deviceDetailModel.getResponse().getCustomerPhoneNo().isEmpty()) {
                                    custphno.setText(deviceDetailModel.getResponse().getCustomerPhoneNo());
                                }
                                if(deviceDetailModel.getResponse().getOperatorName()!=null && !deviceDetailModel.getResponse().getOperatorName().isEmpty()) {
                                    operatornam.setText(deviceDetailModel.getResponse().getOperatorName());
                                }

                                if(deviceDetailModel.getResponse().getIsLogin()) {
                                    CustomUtility.setSharedPreference(getApplicationContext(),"DeviceStatus",getResources().getString(R.string.online));

                                    deviceonline.setText(getResources().getString(R.string.online));
                                    deviceonline.setTextColor(Color.parseColor("#00FF00"));
                                } else {
                                    CustomUtility.setSharedPreference(getApplicationContext(),"DeviceStatus",getResources().getString(R.string.offline));
                                    deviceonline.setText(getResources().getString(R.string.offline));
                                    deviceonline.setTextColor(Color.parseColor("#FF0000"));
                                }

                                if(deviceDetailModel.getResponse().getPumpStatus()) {
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
                CustomUtility.hideProgressDialog(DeviceStatusActivity.this);
                if(error.getMessage()!=null && !error.getMessage().isEmpty()) {
                    CustomUtility.ShowToast(error.getMessage(),DeviceStatusActivity.this);

                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}

