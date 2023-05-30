package debugapp;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumplimited.SetParameter.PairedDeviceActivity;
import com.shaktipumplimited.retrofit.BaseRequest;
import com.shaktipumplimited.retrofit.RequestReciever;
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activity.BaseActivity;
import debugapp.GlobalValue.AllPopupUtil;
import debugapp.GlobalValue.Constant;
import debugapp.GlobalValue.NewSolarVFD;

public class NavigateOptionActivity extends BaseActivity {

    // Splash screen timer
    Context context;
    Context mContext;

    private TextView txtTransportID;
    private TextView txtPODID;
    private TextView txtSimNumberID;
    private EditText edtPutCommandID;
    private String edtTextDeviceNo;
    private BaseRequest baseRequest;

    @Override
    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator_option);
        context = this;
        mContext = this;

        baseRequest = new BaseRequest(this);
        txtTransportID = (TextView) findViewById(R.id.txtTransportID);
        txtPODID = (TextView) findViewById(R.id.txtPODID);
        txtSimNumberID = (TextView) findViewById(R.id.txtSimNumberID);
        edtPutCommandID = findViewById(R.id.edtPutCommandID);

        txtPODID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                edtTextDeviceNo=edtPutCommandID.getText().toString().trim();

                if(!edtTextDeviceNo.equalsIgnoreCase(""))
                {
                    callInsertAndUpdateDebugDataAPI();
                }
                else
                {
                    Toast.makeText(context, "Please Device Serial Number.", Toast.LENGTH_SHORT).show();
                }


            }
        });



        txtTransportID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter.isEnabled()) {
                    if (AllPopupUtil.pairedDeviceListGloable(mContext)) {

                        if (Constant.BT_DEVICE_NAME.equalsIgnoreCase("") || Constant.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("")) {
                            Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                            mContext.startActivity(intent);
                        }
                        else
                        {
                            // Intent intent = new Intent(mContext, GetBTDATAListActivity.class);
                            //Intent intent = new Intent(mContext, ShaktiTerminalActivity.class);
                          //  Intent intent = new Intent(mContext, GetBTDebugDataActivity.class);
                           /* Intent intent = new Intent(mContext, BlueToothDebugNewActivity.class);
                            intent.putExtra("BtNameHead",Constant.BT_DEVICE_NAME );
                            intent.putExtra("BtMacAddressHead",Constant.BT_DEVICE_MAC_ADDRESS );
                            mContext.startActivity(intent);*/
                            Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                            mContext.startActivity(intent);
                            //((Activity)mContext).finish();
                        }
                        ///////////////write the query
                        //   new BluetoothCommunicationForMotorStop().execute(":TURNON#", ":TURNON#", "START");
                    } else {
                        // AllPopupUtil.btPopupCreateShow(mContext);
                        mContext.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                    }
                } else {
                    //  AllPopupUtil.btPopupCreateShow(mContext);
                    mContext.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                }
            }
        });

    }


    private void callInsertAndUpdateDebugDataAPI() {
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int APINumber, String Json, Object obj) {
                //  JSONArray arr = (JSONArray) obj;
                try {

                    JSONObject jo = new JSONObject(Json);

                    String mStatus = jo.getString("status");

                    final String mMessage = jo.getString("message");
                    String jo11 = jo.getString("response");
                    System.out.println("jo11==>>"+jo11);
                    if (mStatus.equalsIgnoreCase("true")) {

                        JSONObject join = new JSONObject(jo11);
                        String mSimNo = join.getString("SimMobileNo");
                        txtSimNumberID.setText(mSimNo);

                        Toast.makeText(mContext, mMessage, Toast.LENGTH_LONG).show();
                        baseRequest.hideLoader();

                    }
                    else
                    {
                        Toast.makeText(mContext, mMessage, Toast.LENGTH_LONG).show();
                        baseRequest.hideLoader();
                    }


                    //  getDeviceSettingListResponse(mSettingModelView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int APINumber, String errorCode, String message) {
                baseRequest.hideLoader();
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkFailure(int APINumber, String message) {
                baseRequest.hideLoader();
                Toast.makeText(mContext, "Please check internet connection!", Toast.LENGTH_LONG).show();
            }
        });


        Map<String, String> wordsByKey = new HashMap<>();

        wordsByKey.put("deviceNo", edtTextDeviceNo);
        //baseRequest.callAPIPost(1, jsonObject, Constant.GET_ALL_NOTIFICATION_LIST_API);/////
        //  baseRequest.callAPIPost(1, jsonObject, NewSolarVFD.GET_PLANT_LIST_CHECK);/////
        //baseRequest.callAPIPostIMEI(1, jsonObject, NewSolarVFD.MOTOR_PERSMETER_LIST);/////
        System.out.println("jsonObject==>>"+wordsByKey);
        baseRequest.callAPIGET(1, wordsByKey, NewSolarVFD.GET_DEVICE_SIM_NUMBER_API);/////


      /*  JsonObject jsonObject = new JsonObject();
        try {
            ////Put input parameter here
            jsonObject.addProperty("deviceNo", edtTextDeviceNo);
           // jsonObject.addProperty("Content", AllCommomSTRContainerIN);
            //  jsonObject.addProperty("fcmToken", NewSolarVFD.FCM_TOKEN);
            //  jsonObject.addProperty("imei", NewSolarVFD.IMEI_NUMBER);
         //   System.out.println("RMSVIKAS   Content=" + AllCommomSTRContainerIN + ", DeviceNo=" + mBtNameHead);
        } catch (Exception e) {
            baseRequest.hideLoader();
            e.printStackTrace();
        }
        //  baseRequest.callAPIPost(1, jsonObject, Constant.GET_ALL_NOTIFICATION_LIST_API);/////
        baseRequest.callAPIPost(1, jsonObject, NewSolarVFD.GET_DEVICE_SIM_NUMBER_API);/////
        //baseRequest.callAPIPut(1, jsonObject, NewSolarVFD.ORG_RESET_FORGOTPASS);/////*/
    }

}