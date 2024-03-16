package activity;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.WebURL;

public class DeviceInformationActivity extends AppCompatActivity implements View.OnClickListener {

    public static UUID my_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    TextView deviceInformation,submitBtn;
    BluetoothSocket bluetoothSocket;
    private InputStream iStream = null;
    String DeviceInfo = "", DEVICE_NO = "", DONGLE_FIRM_VER = "", DEVICE_FIRM_VER = "", DONGLE_APN = "",
            DONGLE_MODE = "", DONGLE_CONNECTIVITY = "", DONGLE_MQTT1_IP = "", DONGLE_MQTT2_IP = "", DONGLE_D_FOTA = "",TCP_IP = "",
            kkkkkk1 = "", ControllerId = "",billNo="",deviceOnlineDetails ="",docno_sap="",invc_done="";
    ProgressDialog progressDialog;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);
        Init();
        retrieveData();
    }


    private void Init() {
        progressDialog = new ProgressDialog(this);
        mToolbar =  findViewById(R.id.toolbar);
        deviceInformation = findViewById(R.id.deviceInformation);
        submitBtn = findViewById(R.id.submitBtn);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.deviceInformation));


        submitBtn.setOnClickListener(this);
    }
    private void retrieveData() {
        if (getIntent().getExtras() != null) {
            ControllerId = getIntent().getStringExtra(Constant.ControllerSerialNumber);
            billNo = getIntent().getStringExtra(Constant.billNo);
            deviceOnlineDetails = getIntent().getStringExtra(Constant.deviceOnlineDetail);
            Log.e("billNo======>",billNo);
            Log.e("deviceOnlineDetails======>",deviceOnlineDetails);
            new BlueToothCommunicationForDeviceInfo4G().execute(":DEVICE INFO#", ":DEVICE INFO#", "OKAY");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitBtn:
                if(CustomUtility.isInternetOn(getApplicationContext())){
                    if(DEVICE_NO.isEmpty()){
                        getResources().getString(R.string.retriveDeviceInfo);
                    }else if(DONGLE_FIRM_VER.isEmpty()){
                        getResources().getString(R.string.retriveDeviceInfo);
                    }else {
                        saveShiftingStatusToSap(deviceOnlineDetails);
                    }
                }else {
                    CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection),this);
                }
                break ;
        }
    }


    private class BlueToothCommunicationForDeviceInfo4G extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            my_UUID = UUID.fromString(my_UUID.toString());
            CustomUtility.showProgressDialogue(DeviceInformationActivity.this);
        }

        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (bluetoothSocket != null) {
                    if (!bluetoothSocket.isConnected()) {
                        connectToBluetoothSocket();

                    }
                } else {
                    connectToBluetoothSocket();
                }


                if (bluetoothSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);
                    try {
                        bluetoothSocket.getOutputStream().write(STARTRequest);
                        sleep(1000);
                        iStream = bluetoothSocket.getInputStream();
                        DeviceInfo = "";
                        while (true) {
                            try {
                                kkkkkk1 = (char) iStream.read() + "";
                                DeviceInfo = DeviceInfo + kkkkkk1;

                                //  Log.e("DeviceInfo==:-",DeviceInfo);
                                if (iStream.available() == 0) {
                                    break;
                                }
                            } catch (IOException e) {
                                CustomUtility.hideProgressDialog(getApplicationContext());
                                e.printStackTrace();
                                break;
                            }
                        }

                    } catch (InterruptedException e1) {
                        CustomUtility.hideProgressDialog(getApplicationContext());
                        e1.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///addHeadersMonths();


                            try {

                                String[] sssM = DeviceInfo.split(",");
                                System.out.println("Shimha2==>>" + sssM.length);
                                System.out.println("Shimha2==>>" + DeviceInfo);
                                // Log.e("DeviceInfo==:-",DeviceInfo);

                                if (!sssM[0].isEmpty()) {
                                    String deviceNo = sssM[0];

                                    DEVICE_NO = deviceNo.replaceAll("DEVICE NO:", "");
                                } else {
                                    String deviceNo = "Not Available";
                                    DEVICE_NO = deviceNo;
                                }

                                if (!sssM[1].isEmpty()) {
                                    String dongleFirmVer = sssM[1];

                                    DONGLE_FIRM_VER = dongleFirmVer.replaceAll("DONGLE_FIRM VER:", "");
                                } else {
                                    String dongleFirmVer = "Not Available";
                                    DONGLE_FIRM_VER = dongleFirmVer;
                                }


                                if (DONGLE_FIRM_VER.equals("4.05") || DONGLE_FIRM_VER.equals("4.06") || DONGLE_FIRM_VER.equals("4.07")) {

                                    if (!sssM[2].isEmpty()) {
                                        String deviceFirmVer = sssM[2];

                                        DEVICE_FIRM_VER = deviceFirmVer.replaceAll("DEVICE_FIRM_VER:", "");
                                    } else {
                                        String deviceFirmVer = "Not Available";
                                        DEVICE_FIRM_VER = deviceFirmVer;
                                    }

                                    if (!sssM[3].isEmpty()) {
                                        String dongleApn = sssM[3];

                                        DONGLE_APN = dongleApn.replaceAll("APN:", "");
                                    } else {
                                        String dongleApn = "Not Available";
                                        DONGLE_APN = dongleApn;
                                    }


                                    if (!sssM[4].isEmpty()) {
                                        String dongleMode = sssM[4];

                                        DONGLE_MODE = dongleMode.replaceAll("MODE:", "");
                                    } else {
                                        String dongleMode = "Not Available";
                                        DONGLE_MODE = dongleMode;
                                    }


                                    if (!sssM[5].isEmpty()) {
                                        String dongleMqttIP = sssM[5];

                                        DONGLE_MQTT1_IP = dongleMqttIP.replaceAll("MQTT1_IP:", "");
                                    } else {
                                        String dongleMqttIP = "Not Available";
                                        DONGLE_MQTT1_IP = dongleMqttIP;
                                    }


                                    if (!sssM[6].isEmpty()) {
                                        String dongleMqtt2IP = sssM[6];

                                        DONGLE_MQTT2_IP = dongleMqtt2IP.replaceAll("MQTT2_IP:", "");
                                    } else {
                                        String dongleMqtt2IP = "Not Available";
                                        DONGLE_MQTT2_IP = dongleMqtt2IP;
                                    }

                                    if (!sssM[7].isEmpty()) {
                                        String tcpIp = sssM[7];

                                        TCP_IP = tcpIp.replaceAll("TCP IP:", "");
                                    } else {
                                        String tcpIp = "Not Available";
                                        TCP_IP = tcpIp;
                                    }


                                } else if (DONGLE_FIRM_VER.equals("4.08")) {
                                    if (!sssM[2].isEmpty()) {
                                        String deviceFirmVer = sssM[2];

                                        DEVICE_FIRM_VER = deviceFirmVer.replaceAll("DEVICE_FIRM_VER:", "");
                                    } else {
                                        String deviceFirmVer = "Not Available";
                                        DEVICE_FIRM_VER = deviceFirmVer;
                                    }

                                    if (!sssM[3].isEmpty()) {
                                        String dongleApn = sssM[3];

                                        DONGLE_APN = dongleApn.replaceAll("APN:", "");
                                    } else {
                                        String dongleApn = "Not Available";
                                        DONGLE_APN = dongleApn;
                                    }


                                    if (!sssM[4].isEmpty()) {
                                        String dongleMode = sssM[4];

                                        DONGLE_MODE = dongleMode.replaceAll("MODE:", "");
                                    } else {
                                        String dongleMode = "Not Available";
                                        DONGLE_MODE = dongleMode;
                                    }

                                    if (!sssM[5].isEmpty()) {
                                        String dongleConnectivity = sssM[5];

                                        DONGLE_CONNECTIVITY = dongleConnectivity.replaceAll("CONNECTIVITY:", "");
                                    } else {
                                        String dongleConnectivity = "Not Available";
                                        DONGLE_CONNECTIVITY = dongleConnectivity;
                                    }


                                    if (!sssM[6].isEmpty()) {
                                        String dongleMqttIP = sssM[6];

                                        DONGLE_MQTT1_IP = dongleMqttIP.replaceAll("MQTT1_IP:", "");
                                    } else {
                                        String dongleMqttIP = "Not Available";
                                        DONGLE_MQTT1_IP = dongleMqttIP;
                                    }


                                    if (!sssM[7].isEmpty()) {
                                        String dongleMqtt2IP = sssM[7];

                                        DONGLE_MQTT2_IP = dongleMqtt2IP.replaceAll("MQTT2_IP:", "");
                                    } else {
                                        String dongleMqtt2IP = "Not Available";
                                        DONGLE_MQTT2_IP = dongleMqtt2IP;
                                    }

                                    if (!sssM[8].isEmpty()) {
                                        String D_Fota = sssM[8];

                                        DONGLE_D_FOTA = D_Fota.replaceAll("D_FOTA:", "");
                                    } else {
                                        String D_Fota = "Not Available";
                                        DONGLE_D_FOTA = D_Fota;
                                    }

                                }


                            } catch (Exception exception) {
                                exception.printStackTrace();
                                CustomUtility.hideProgressDialog(getApplicationContext());
                            }

                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                CustomUtility.hideProgressDialog(getApplicationContext());
                return false;
            }

            CustomUtility.hideProgressDialog(getApplicationContext());
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            CustomUtility.hideProgressDialog(getApplicationContext());
            String deviceInfo = "";
            Log.e("DONGLE_FIRM_VER=====>",DONGLE_FIRM_VER);
            if (DONGLE_FIRM_VER.equals("4.05") || DONGLE_FIRM_VER.equals("4.06") || DONGLE_FIRM_VER.equals("4.07")) {

                deviceInfo = "DEVICE_NO:-" + DEVICE_NO + "\nDONGLE_FIRM_VER:-" + DONGLE_FIRM_VER +
                        "\nDEVICE_FIRM_VER:-" + DEVICE_FIRM_VER +
                        "\nDONGLE_APN:-" + DONGLE_APN +
                        "\nDONGLE_MODE:-" + DONGLE_MODE +
                        "\nDONGLE_MQTT1_IP:-" + DONGLE_MQTT1_IP +
                        "\nDONGLE_MQTT2_IP:-" + DONGLE_MQTT2_IP
                        + "\nTCP_IP:-" + TCP_IP;

                Log.e("deviceInfo=====>",deviceInfo);
            } else if (DONGLE_FIRM_VER.equals("4.08")) {
            deviceInfo = "DEVICE_NO:-" + DEVICE_NO + "\nDONGLE_FIRM_VER:-" + DONGLE_FIRM_VER +
                    "\nDEVICE_FIRM_VER:-" + DEVICE_FIRM_VER +
                    "\nDONGLE_APN:-" + DONGLE_APN +
                    "\nDONGLE_MODE:-" + DONGLE_MODE +
                    "\nDONGLE_CONNECTIVITY:-" + DONGLE_CONNECTIVITY +
                    "\nDONGLE_MQTT1_IP:-" + DONGLE_MQTT1_IP +
                    "\nDONGLE_MQTT2_IP:-" + DONGLE_MQTT2_IP
                    + "\nDONGLE_D_FOTA:-" + DONGLE_D_FOTA;

               // Log.e("deviceInfo=====>",deviceInfo);
        }

            deviceInformation.setText(deviceInfo);


        }

    }


    private void connectToBluetoothSocket() {
        try {
            bluetoothSocket = null;
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(ControllerId);//connects to the device's address and checks if it's available

            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(my_UUID);//create a RFCOMM (SPP) connection
            bluetoothAdapter.cancelDiscovery();
            if (!bluetoothSocket.isConnected())
                bluetoothSocket.connect();


        } catch (Exception e) {
            CustomUtility.hideProgressDialog(getApplicationContext());

            runOnUiThread(() -> CustomUtility.ShowToast(getResources().getString(R.string.pairedDevice), getApplicationContext()));
            e.printStackTrace();
        }

    }


    private void saveShiftingStatusToSap(String shifting_remark) {
        stopProgressDialogue();
        showProgressDialogue(getResources().getString(R.string.sendingDataServer));
        JSONObject mainObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            mainObject.put("vbeln", billNo);
            mainObject.put("SHIFTING_REMARK", shifting_remark);
            mainObject.put("DEVICE_NO", DEVICE_NO);
            mainObject.put("DONGLE_FIRM_VER", DONGLE_FIRM_VER);
            mainObject.put("DEVICE_FIRM_VER", DEVICE_FIRM_VER);
            mainObject.put("DONGLE_APN", DONGLE_APN);
            mainObject.put("DONGLE_MODE", DONGLE_MODE);
            mainObject.put("DONGLE_CONNECTIVITY", DONGLE_CONNECTIVITY);
            mainObject.put("DONGLE_MQTT1_IP", DONGLE_MQTT1_IP);
            mainObject.put("DONGLE_MQTT2_IP", DONGLE_MQTT2_IP);
            mainObject.put("DONGLE_D_FOTA", DONGLE_D_FOTA);
            mainObject.put("TCP_IP", TCP_IP);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonArray.put(mainObject);

        Log.e("DeviceShiftingAPI=====>", WebURL.saveShiftedDeviceToServer + jsonArray);

        CustomUtility.showProgressDialogue(DeviceInformationActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.saveShiftedDeviceToServer + jsonArray, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                stopProgressDialogue();
                Log.e("Response=====>",res.toString());
                try {
                if (!res.toString().isEmpty()) {



                    JSONObject object = new JSONObject(res.toString());
                    String obj1 = object.getString("data_return");

                    JSONArray ja = new JSONArray(obj1);

//                    Log.e("OUTPUT2", "&&&&" + ja);

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);

                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");

                        if (invc_done.equals("Y")) {
                            ShowAlertResponse(getResources().getString(R.string.device_shifting_successfully), false);
                        }else if(invc_done.equals("N")){
                            CustomUtility.showToast(DeviceInformationActivity.this,getResources().getString(R.string.device_shifting_unsuccessfully));
                        }
                    }



                }else {
                    CustomUtility.showToast(DeviceInformationActivity.this,getResources().getString(R.string.device_shifting_unsuccessfully));
                }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialogue();
                Log.e("error", String.valueOf(error));
                Toast.makeText(DeviceInformationActivity.this, error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void ShowAlertResponse(String message, boolean isScreenOpen) {
        LayoutInflater inflater = (LayoutInflater) DeviceInformationActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DeviceInformationActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);

        title_txt.setText(message);

        OK_txt.setOnClickListener(v -> {
            alertDialog.dismiss();
                Intent intent = new Intent(DeviceInformationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


        });

    }


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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectBtSocket();
    }

    private void disconnectBtSocket() {
        if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
            try {
                bluetoothSocket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}