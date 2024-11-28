package settingParameter;

import static java.lang.Thread.sleep;

import static database.DatabaseHelper.COLUMN_MaterialCode;
import static database.DatabaseHelper.KEY_BILL_NO;
import static database.DatabaseHelper.TABLE_PARAMETER_SET_DATA;
import static database.DatabaseHelper.TABLE_SETTING_PARAMETER_LIST;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import activity.InstallationList;
import activity.PendingInstallationActivity;
import activity.UnloadInstReportImageActivity;
import bean.ParameterSettingListModel;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import settingParameter.adapter.SettingParameterAdapter;
import settingParameter.model.MotorParamListModel;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class SettingParameterActivity extends AppCompatActivity implements SettingParameterAdapter.ItemclickListner, View.OnClickListener {
    private UUID mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    RecyclerView parametersList;
    TextView noDataFound;
    DatabaseHelper databaseHelper;
    private Activity mActivity;
    List<MotorParamListModel.Response> parameterSettingList,parameterSettingList2;
    int selected_pos = 0, mGlobalPosition = 0, mGlobalPositionSet = 0, mWriteAllCounterValue = 0, counterValue = 0,mCount=0;
    char mCRCFinalValue;
    private BluetoothAdapter myBluetooth;
    private ProgressDialog progressDialog;
    private BluetoothSocket btSocket;
    private InputStream iStream;
    AlertDialog alertDialog;
    boolean ispaired = false,isAllParameterSet = true;
    private float edtValueFloat = 0, mTotalTimeFloatData;;
    SettingParameterAdapter settingParameterAdapter;
    String BluetoothAddress;
    Toolbar toolbar;
    TextView rlvSetAllViewID,submitBtn;
    String materialCode = "",set_matno,billNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_parameter);

        Init();
        listener();

    }

    private void listener() {
        rlvSetAllViewID.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    private void Init() {
        parametersList = findViewById(R.id.parametersList);
        noDataFound = findViewById(R.id.noDataFound);
        rlvSetAllViewID = findViewById(R.id.rlvSetAllViewID);
        submitBtn = findViewById(R.id.submitBtn);
        mActivity = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.setting_param));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        retrieveValue();

    }


    private void retrieveValue() {
        parameterSettingList = new ArrayList<>();
        parameterSettingList2 = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        if (getIntent().getExtras() != null) {
            BluetoothAddress = getIntent().getStringExtra("BtMacAddressHead");
            billNo = getIntent().getStringExtra(Constant.billNo);
            set_matno =  getIntent().getStringExtra(Constant.pendingSettingData);
            materialCode = set_matno.replace("00000000", "");
            Log.e("materialCode===>",materialCode);
          //  materialCode = "9500002085";
            if (CustomUtility.isInternetOn(getApplicationContext())) {
               if(!databaseHelper.isRecordExist(TABLE_SETTING_PARAMETER_LIST,COLUMN_MaterialCode,materialCode)) {
                   new getAllParameters().execute();
                }else {
                    setAdapter(materialCode);
                }
            } else {
                setAdapter(materialCode);
            }
        }
    }

    private void getAllParameters() {
        parameterSettingList = new ArrayList<>();
        parameterSettingList2 = new ArrayList<>();
        showProgressDialogue(getResources().getString(R.string.loading));
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        Log.e("url===>", CustomUtility.getSharedPreferences(getApplicationContext(), Constant.RmsBaseUrl) + WebURL.paraMeterListAPI);
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, CustomUtility.getSharedPreferences(getApplicationContext(), Constant.RmsBaseUrl) + WebURL.paraMeterListAPI , response -> {
         //   Log.e("response1===>", String.valueOf(response.toString()));
            if (!response.isEmpty()) {
                MotorParamListModel motorParamListModel = new Gson().fromJson(response, MotorParamListModel.class);
              //  Log.e("response2===>", String.valueOf(motorParamListModel.getStatus().equals("true")));
                if (motorParamListModel.getStatus().equals("true")) {
                    insertDataInLocal(motorParamListModel.getResponse());
                    hiddeProgressDialogue();
                } else {
                    hiddeProgressDialogue();
                    noDataFound.setVisibility(View.VISIBLE);
                }

            } else {
                hiddeProgressDialogue();
                noDataFound.setVisibility(View.VISIBLE);
            }

        }, error -> {
            hiddeProgressDialogue();
            noDataFound.setVisibility(View.VISIBLE);
        });

        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }


    private class getAllParameters extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            parameterSettingList = new ArrayList<>();
            parameterSettingList2 = new ArrayList<>();
            showProgressDialogue(getResources().getString(R.string.loading));
        }

        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            String login_selec = null;
            try {
                login_selec = CustomHttpClient.executeHttpGet(CustomUtility.getSharedPreferences(getApplicationContext(), Constant.RmsBaseUrl) + WebURL.paraMeterListAPI);

                Log.e("URL====>",CustomUtility.getSharedPreferences(getApplicationContext(), Constant.RmsBaseUrl) + WebURL.paraMeterListAPI);
                JSONObject object = new JSONObject(login_selec);
                MotorParamListModel motorParamListModel = new Gson().fromJson(object.toString(), MotorParamListModel.class);
                Log.e("response2===>", String.valueOf(motorParamListModel.getStatus().equals("true")));

                if (motorParamListModel.getStatus().equals("true")) {
                    insertDataInLocal(motorParamListModel.getResponse());
                    hiddeProgressDialogue();
                }
            } catch (Exception e) {
                e.printStackTrace();
               hiddeProgressDialogue();

            }

            return login_selec;
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String result) {
            // write display tracks logic here

            hiddeProgressDialogue();
        }
    }

    private void insertDataInLocal(List<MotorParamListModel.Response> parameterSettingList) {
        databaseHelper.deleteParametersData();
        for (int i = 0; i < parameterSettingList.size(); i++) {
            DatabaseRecordInsert(parameterSettingList.get(i), String.valueOf( (double) parameterSettingList.get(i).getpValue() *  (double)  parameterSettingList.get(i).getFactor()));

        }
        setAdapter(materialCode);
    }

    private void DatabaseRecordInsert(MotorParamListModel.Response response, String pValue) {
        databaseHelper.insertParameterRecord(response, pValue);
    }

    private void setAdapter(String materialCode) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
        settingParameterAdapter = null;
        parameterSettingList2 = databaseHelper.getParameterRecordDetails(materialCode.trim());
        if (parameterSettingList2 != null && parameterSettingList2.size() > 0) {


                for (int i = 0; i < parameterSettingList2.size(); i++) {
                    MotorParamListModel.Response response = new MotorParamListModel.Response();

                    response.setParametersName(parameterSettingList2.get(i).getParametersName());
                    response.setMaterialCode(parameterSettingList2.get(i).getMaterialCode());
                    response.setModbusaddress(parameterSettingList2.get(i).getModbusaddress());
                    response.setMobBTAddress(parameterSettingList2.get(i).getMobBTAddress());
                    response.setFactor(parameterSettingList2.get(i).getFactor());
                    response.setOffset(parameterSettingList2.get(i).getOffset());
                    response.setPmId(parameterSettingList2.get(i).getPmId());
                    response.setUnit(parameterSettingList2.get(i).getUnit());
                    response.setpValue(parameterSettingList2.get(i).getpValue());

                    if(parameterSettingList2.get(i).getBillNo().equals(billNo)) {
                        response.setBillNo(parameterSettingList2.get(i).getBillNo());
                        response.setSet(parameterSettingList2.get(i).getSet());
                    }else {
                        response.setBillNo(parameterSettingList2.get(i).getBillNo());
                        response.setSet(false);
                    }
                        parameterSettingList.add(response);

                }


            settingParameterAdapter = new SettingParameterAdapter(SettingParameterActivity.this, parameterSettingList, noDataFound);
            parametersList.setHasFixedSize(true);
            parametersList.setAdapter(settingParameterAdapter);
            settingParameterAdapter.EditItemClick(SettingParameterActivity.this);
            noDataFound.setVisibility(View.GONE);
            parametersList.setVisibility(View.VISIBLE);
        } else {
            noDataFound.setVisibility(View.VISIBLE);
            parametersList.setVisibility(View.GONE);
        }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlvSetAllViewID:
                setAllTheComParameter();
                break;
            case R.id.submitBtn:

                if(!parameterSettingList.isEmpty()){
                    for (int i=0; i<parameterSettingList.size(); i++){
                        if(parameterSettingList.get(i).getSet().toString().equals("false")){
                            isAllParameterSet = false;
                            CustomUtility.ShowToast(getResources().getString(R.string.pleaseSetParametersFirst),getApplicationContext());
                            break;
                        }else {
                            isAllParameterSet = true;
                        }
                    }
                    if(isAllParameterSet){
                        try {
                            ShowAlertResponse("3");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                break;


        }
    }
    @Override
    public void getBtnMethod(MotorParamListModel.Response response, String editvalue, int position) {
        selected_pos = position;
        showProgressDialogue(getResources().getString(R.string.fetchingData));
        int pos = position;
        mGlobalPosition = position;

        String spsp = parameterSettingList.get(pos).getOffset() + "";
        if (!spsp.equalsIgnoreCase("") && !spsp.equalsIgnoreCase("0") && !spsp.equalsIgnoreCase("0.0")) {
            edtValueFloat = Float.parseFloat(parameterSettingList.get(pos).getOffset() + "");
        } else {
            edtValueFloat = 1;
        }

        System.out.println("mTotalTime==>>vvv=offset=>>" + edtValueFloat);

        char[] datar = new char[4];
        int a = Float.floatToIntBits((float) edtValueFloat);
        //  int a= (int) edtValueFloat;
        datar[0] = (char) (a & 0x000000FF);
        datar[1] = (char) ((a & 0x0000FF00) >> 8);
        datar[2] = (char) ((a & 0x00FF0000) >> 16);
        datar[3] = (char) ((a & 0xFF000000) >> 24);
        int crc = CRC16_MODBUS(datar, 4);
        char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
        char reciverbyte2 = (char) (crc & 0x00FF);

        mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);

        String v1 = String.format("%02x", (0xff & datar[0]));
        String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
        String v3 = String.format("%02x", (0xff & datar[2]));
        String v4 = String.format("%02x", (0xff & datar[3]));
        String v5 = Integer.toHexString(mCRCFinalValue);

        String mMOBADDRESS = "";
        String mMobADR = parameterSettingList.get(pos).getMobBTAddress();
        if (!mMobADR.equalsIgnoreCase("")) {
            int mLenth = mMobADR.length();
            if (mLenth == 1) {
                mMOBADDRESS = "000" + mMobADR;
            } else if (mLenth == 2) {
                mMOBADDRESS = "00" + mMobADR;
            } else if (mLenth == 3) {
                mMOBADDRESS = "0" + mMobADR;
            } else {
                mMOBADDRESS = mMobADR;
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.addressnotfound), Toast.LENGTH_SHORT).show();
        }
        final String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
        System.out.println("mTotalTime==>>get=modeBusCommand=>>" + modeBusCommand);
        mTotalTimeFloatData = 0;

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        new BluetoothCommunicationForDynamicParameterRead().execute(modeBusCommand, modeBusCommand, "OK");
                    }
                }, 3000);
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterRead extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        private static final long TIMEOUT = 20000; // Timeout duration in milliseconds (e.g., 10 seconds)
        private Handler handler = new Handler(Looper.getMainLooper());
        private Runnable timeoutRunnable;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            timeoutRunnable = () -> {
                if (BluetoothCommunicationForDynamicParameterRead.this.getStatus() == Status.RUNNING) {
                    // Cancel the AsyncTask if it's still running
                    BluetoothCommunicationForDynamicParameterRead.this.cancel(true);
                    try {
                        onTimeout(); // Handle the timeout case
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            handler.postDelayed(timeoutRunnable, TIMEOUT);
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (!btSocket.isConnected()) {
                        connectToBluetoothSocket();

                    } else {
                        connectToBluetoothSocket();
                    }
                } else {
                    connectToBluetoothSocket();
                }
                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(100);

                        iStream = btSocket.getInputStream();
                        System.out.println("iStream==>>iStream  " + iStream + " ");

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    int[] bytesReaded = new int[4];

                    int mTotalTime;
                    int jjj = 0;

                    for (int i = 0; i < 1; i++) {
                        try {
                            for (int j = 0; j < 6; j++) {

                                int mCharOne1 = iStream.read();
                                Log.e("istream====>", "" + (char) mCharOne1);
                            }

                            int mCharOne = iStream.read();
                            int mCharTwo = iStream.read();
                            bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + "" + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 1] = Integer.parseInt("" + (char) mCharOne + "" + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 2] = Integer.parseInt("" + (char) mCharOne + "" + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 3] = Integer.parseInt("" + (char) mCharOne + "" + (char) mCharTwo, 16);

                            Log.e("byteread0====>", String.valueOf(bytesReaded[i])
                                    + "byteread1====>" + String.valueOf(bytesReaded[i + 1])
                                    + "byteread2====>" + String.valueOf(bytesReaded[i + 2])
                                    + "byteread3====>" + String.valueOf(bytesReaded[i + 3]));

                            Log.e("bytesReaded", bytesReaded.toString());

                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;
                            System.out.println("mTotalTime==>>vvv4  " + jjj + " " + mTotalTime);


                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);
                            Log.e("READ=====>", String.valueOf(mTotalTimeFloatData));
                            runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    try {
                                        ShowAlertResponse("0");

                                        parameterSettingList.get(mWriteAllCounterValue).setpValue((float) mTotalTimeFloatData);
                                        parameterSettingList.get(mWriteAllCounterValue).setBillNo(billNo);
                                        settingParameterAdapter.notifyDataSetChanged();
                                        System.out.println("mGlobalPosition==>>" + mGlobalPosition + "\nmTotalTimeFloatData==>>" + mTotalTimeFloatData);
                                        databaseHelper.updateParameterRecord(parameterSettingList.get(mWriteAllCounterValue));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            jjj++;
                            int mCharOne11;
                            //needed to cancel out the extra buffer
                            for (int ii = 0; ii < 4; ii++) {
                                mCharOne11 = iStream.read();
                                Log.e("mCharOne11===>ii>>>>>", String.valueOf(mCharOne11) + "=====" + ii);
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                hiddeProgressDialogue();
                return false;
            }

            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {

            super.onPostExecute(result);
            hiddeProgressDialogue();
        }
    }

    @Override
    public void setBtnMethod(MotorParamListModel.Response response, String editvalue, int position) {

        try {
            selected_pos = position;
            int pos = position;
            mGlobalPosition = pos;
            String mStringCeck = editvalue.trim();
            if (!mStringCeck.equalsIgnoreCase("")) {
                edtValueFloat = Float.parseFloat(parameterSettingList.get(selected_pos).getpValue().toString().trim());
            }

            char[] datar = new char[4];
            int a = Float.floatToIntBits((float) edtValueFloat);
            datar[0] = (char) (a & 0x000000FF);
            datar[1] = (char) ((a & 0x0000FF00) >> 8);
            datar[2] = (char) ((a & 0x00FF0000) >> 16);
            datar[3] = (char) ((a & 0xFF000000) >> 24);
            int crc = CRC16_MODBUS(datar, 4);
            char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
            char reciverbyte2 = (char) (crc & 0x00FF);
            mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);
            String v1 = String.format("%02x", (0xff & datar[0]));
            String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
            String v3 = String.format("%02x", (0xff & datar[2]));
            String v4 = String.format("%02x", (0xff & datar[3]));
            String v5 = Integer.toHexString(mCRCFinalValue);
            String mMOBADDRESS = "";
            String mMobADR = parameterSettingList.get(pos).getMobBTAddress();
            if (!mMobADR.equalsIgnoreCase("")) {
                int mLenth = mMobADR.length();
                if (mLenth == 1) {
                    mMOBADDRESS = "000" + mMobADR;
                } else if (mLenth == 2) {
                    mMOBADDRESS = "00" + mMobADR;
                }
                if (mLenth == 3) {
                    mMOBADDRESS = "0" + mMobADR;
                } else {
                    mMOBADDRESS = mMobADR;
                }
                String modeBusCommand = "0106" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                System.out.println("mTotalTime==>>vvvSet==>> " + modeBusCommand);
                new BluetoothCommunicationForDynamicParameterWrite().execute(modeBusCommand, modeBusCommand, "OK");

            } else {
                Toast.makeText(this, getResources().getString(R.string.addressnotfound), Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterWrite extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        private static final long TIMEOUT = 20000; // Timeout duration in milliseconds (e.g., 10 seconds)
        private Handler handler = new Handler(Looper.getMainLooper());
        private Runnable timeoutRunnable;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialogue(getResources().getString(R.string.sendingData));
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            // Initialize and start the timeout timer
            timeoutRunnable = () -> {
                if (BluetoothCommunicationForDynamicParameterWrite.this.getStatus() == Status.RUNNING) {
                    // Cancel the AsyncTask if it's still running
                    BluetoothCommunicationForDynamicParameterWrite.this.cancel(true);
                    try {
                        onTimeout(); // Handle the timeout case
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            handler.postDelayed(timeoutRunnable, TIMEOUT);

        }

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (!btSocket.isConnected()) {
                        connectToBluetoothSocket();

                    } else {
                        connectToBluetoothSocket();
                    }
                } else {
                    connectToBluetoothSocket();
                }

                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(1000);
                        iStream = btSocket.getInputStream();

                        // Log.e("iStream===>set", String.valueOf(iStream.read()));
                    } catch (InterruptedException e1) {

                        e1.printStackTrace();
                    }


                    int[] bytesReaded = new int[4];


                    int mTotalTime;

                    int jjj = 0;

                    for (int i = 0; i < 1; i++) {
                        try {
                            for (int j = 0; j < 6; j++) {
                                int mCharOne1 = iStream.read();
                                Log.e("istream====>", "" + (char) mCharOne1);
                            }


                            int mCharOne = iStream.read();
                            int mCharTwo = iStream.read();
                            bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 1] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 2] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 3] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);

                            System.out.println("byte0==>  " + jjj + " " + bytesReaded[i]);
                            System.out.println("byte1==>  " + jjj + " " + bytesReaded[i + 1]);
                            System.out.println("byte2==>  " + jjj + " " + bytesReaded[i + 2]);
                            System.out.println("byte3=>  " + jjj + " " + bytesReaded[i + 3]);


                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;

                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);
                            Log.e("Write=====>", String.valueOf(mTotalTimeFloatData));

                            runOnUiThread(() -> {

                                if (mTotalTimeFloatData == edtValueFloat) {
                                    try {
                                        ShowAlertResponse("1");
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    parameterSettingList.get(selected_pos).setSet(true);
                                } else {
                                    try {
                                        ShowAlertResponse("-1");
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    parameterSettingList.get(selected_pos).setSet(false);
                                }

                                parameterSettingList.get(selected_pos).setpValue((float) edtValueFloat);
                                parameterSettingList.get(selected_pos).setBillNo(billNo);
                                settingParameterAdapter.notifyDataSetChanged();
                                databaseHelper.updateParameterRecord(parameterSettingList.get(selected_pos));


                            });

                            jjj++;

                        } catch (IOException e) {
                            hiddeProgressDialogue();
                            e.printStackTrace();
                        }
                    }
                    while (iStream.available() > 0) {
                        iStream.read();
                    }
                }
            } catch (Exception e) {
                return false;
            }

            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            hiddeProgressDialogue();
            handler.removeCallbacks(timeoutRunnable);
        }
    }

    private void setAllTheComParameter() {
        mCount = 0;
        mGlobalPositionSet = 0;
        mWriteAllCounterValue = 0;
        if (parameterSettingList.size() > 0) {

            try {
                String mStringCeck = parameterSettingList.get(mWriteAllCounterValue).getpValue().toString().trim();


                System.out.println("Vikas!@#==>>" + mStringCeck);

                if (!mStringCeck.equalsIgnoreCase("")) {
                    /*if(parameterSettingList.get(mWriteAllCounterValue).getModbusaddress().equals("2006")){
                        if(parameterSettingList.get(mWriteAllCounterValue).getpValue()<10){
                            edtValueFloat = Float.parseFloat(String.valueOf(parameterSettingList.get(mWriteAllCounterValue).getpValue()*10));
                       Log.e("edtValueFloat====>", String.valueOf(edtValueFloat));
                        }else {
                            edtValueFloat = Float.parseFloat(parameterSettingList.get(mWriteAllCounterValue).getpValue().toString().trim());
                        }
                    }else {
                        edtValueFloat = Float.parseFloat(parameterSettingList.get(mWriteAllCounterValue).getpValue().toString().trim());
                    }*/
                    edtValueFloat = Float.parseFloat(parameterSettingList.get(mWriteAllCounterValue).getpValue().toString().trim());
                }

                counterValue = 0;
                char[] datar = new char[4];
                int a = Float.floatToIntBits((float) edtValueFloat);
                datar[0] = (char) (a & 0x000000FF);
                datar[1] = (char) ((a & 0x0000FF00) >> 8);
                datar[2] = (char) ((a & 0x00FF0000) >> 16);
                datar[3] = (char) ((a & 0xFF000000) >> 24);
                int crc = CRC16_MODBUS(datar, 4);
                char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
                char reciverbyte2 = (char) (crc & 0x00FF);
                mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);
                String v1 = String.format("%02x", (0xff & datar[0]));
                String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
                String v3 = String.format("%02x", (0xff & datar[2]));
                String v4 = String.format("%02x", (0xff & datar[3]));
                String v5 = Integer.toHexString(mCRCFinalValue);
                String mMOBADDRESS = "";
                //  String mMobADR = mSettingParameterResponse.get(pp).getModbusaddress();
                String mMobADR = parameterSettingList.get(mWriteAllCounterValue).getMobBTAddress();
                if (!mMobADR.isEmpty()) {
                    int mLenth = mMobADR.length();
                    if (mLenth == 1) {
                        mMOBADDRESS = "000" + mMobADR;
                    } else if (mLenth == 2) {
                        mMOBADDRESS = "00" + mMobADR;
                    }
                    if (mLenth == 3) {
                        mMOBADDRESS = "0" + mMobADR;
                    } else {
                        mMOBADDRESS = mMobADR;
                    }
                    String modeBusCommand = "0106" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                    System.out.println("mTotalTime==>>vvv==>> " + modeBusCommand);

                    new SettingParameterActivity.BluetoothCommunicationForDynamicParameterWriteAll().execute(modeBusCommand, modeBusCommand, "OK");

                } else {
                    Toast.makeText(SettingParameterActivity.this, "MOB address not found!", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


        } else {
            CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), SettingParameterActivity.this);
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterWriteAll extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        private static final long TIMEOUT = 20000; // Timeout duration in milliseconds (e.g., 10 seconds)
        private Handler handler = new Handler(Looper.getMainLooper());
        private Runnable timeoutRunnable;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialogue(getResources().getString(R.string.setalldata) + " for "+materialCode);
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            timeoutRunnable = () -> {
                if (BluetoothCommunicationForDynamicParameterWriteAll.this.getStatus() == Status.RUNNING) {
                    // Cancel the AsyncTask if it's still running
                    BluetoothCommunicationForDynamicParameterWriteAll.this.cancel(true);
                    try {
                        onTimeout(); // Handle the timeout case
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            handler.postDelayed(timeoutRunnable, TIMEOUT);
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (!btSocket.isConnected()) {
                        connectToBluetoothSocket();

                    } else {
                        connectToBluetoothSocket();
                    }
                } else {
                    connectToBluetoothSocket();
                }
//                if (btSocket != null) {
//                    if (btSocket.isConnected()) {
//
//                    }
//                } else {
//                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
//                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(WebURL.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
//                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
//                    myBluetooth.cancelDiscovery();
//                }
//
//                if (!btSocket.isConnected())
//                    btSocket.connect();//start connection


                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(1000);

                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    int[] bytesReaded = new int[4];
                    int mTotalTime;

                    int jjj = 0;

                    for (int i = 0; i < 1; i++) {
                        try {
                            for (int j = 0; j < 6; j++) {
                                int mCharOne1 = iStream.read();
                                Log.e("istream====>", "" + (char) mCharOne1);
                            }

                            int mCharOne = iStream.read();
                            int mCharTwo = iStream.read();
                            bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 1] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 2] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 3] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);

                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;

                            System.out.println("mTotalTime==>>vvv4  " + jjj + " " + Float.intBitsToFloat(mTotalTime));
                            System.out.println("mTotalTime==>>vikasVihu==>  " + mWriteAllCounterValue + " " + Float.intBitsToFloat(mTotalTime));

                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);

                            Log.e("WriteAll====>", String.valueOf(mTotalTimeFloatData));

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (mTotalTimeFloatData == -1.0) {

                                       /* if (BluetoothCommunicationForDynamicParameterWriteAll.this.getStatus() == Status.RUNNING) {
                                            // Cancel the AsyncTask if it's still running
                                            BluetoothCommunicationForDynamicParameterWriteAll.this.cancel(true);
                                            try {
                                                onTimeout(); // Handle the timeout case
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }*/
                                        parameterSettingList.get(mWriteAllCounterValue).setSet(false);

                                    }
                                    Log.e("name===>", String.valueOf(parameterSettingList.get(mWriteAllCounterValue).getParametersName()));
                                    Log.e("edtValueFloat===>", String.valueOf(edtValueFloat));
                                    Log.e("mTotalTimeFloatData===>", String.valueOf(mTotalTimeFloatData));


                                    if (mTotalTimeFloatData == edtValueFloat) {
                                        parameterSettingList.get(mWriteAllCounterValue).setSet(true);
                                            Log.e("isSetData===>", "true");
                                             mCount++;

                                    } else {
                                        parameterSettingList.get(mWriteAllCounterValue).setSet(false);
                                            Log.e("isSetData===>", "false");
                                            String  value = parameterSettingList.get(mWriteAllCounterValue).getParametersName()+ " Not Set"+ mTotalTimeFloatData;
                                            CustomUtility.ShowToast(value, getApplicationContext());

                                    }
                                    parameterSettingList.get(mWriteAllCounterValue).setpValue((float) edtValueFloat);
                                    parameterSettingList.get(mWriteAllCounterValue).setBillNo(billNo);
                                    settingParameterAdapter.notifyDataSetChanged();
                                    databaseHelper.updateParameterRecord(parameterSettingList.get(mWriteAllCounterValue));

                                }
                            });
                            jjj++;

                        } catch (IOException e) {
                            hiddeProgressDialogue();
                            e.printStackTrace();
                        }
                    }

                    //needed to cancel out extra buffer
                    while (iStream.available() > 0) {
                        iStream.read();
                    }


                }
            } catch (Exception e) {

                return false;
            }

            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);


            mWriteAllCounterValue = mWriteAllCounterValue + 1;
            try {
                hiddeProgressDialogue();
                handler.removeCallbacks(timeoutRunnable);


                if (mWriteAllCounterValue < parameterSettingList.size() && ispaired) {
                    String mStringCeck = parameterSettingList.get(mWriteAllCounterValue).getpValue().toString().trim();
                    System.out.println("Vikas!@#==>>" + mStringCeck);
                    System.out.println("Sumit====>" + parameterSettingList.get(mWriteAllCounterValue).getParametersName());

                    databaseHelper.updateParameter(parameterSettingList.get(mGlobalPosition));

                    if (!mStringCeck.equalsIgnoreCase("")) {
                        edtValueFloat = Float.parseFloat(parameterSettingList.get(mWriteAllCounterValue).getpValue().toString().trim());
                    }

                    counterValue = 0;
                    char[] datar = new char[4];

                    int a = Float.floatToIntBits((float) edtValueFloat);
                    datar[0] = (char) (a & 0x000000FF);
                    datar[1] = (char) ((a & 0x0000FF00) >> 8);
                    datar[2] = (char) ((a & 0x00FF0000) >> 16);
                    datar[3] = (char) ((a & 0xFF000000) >> 24);
                    int crc = CRC16_MODBUS(datar, 4);
                    char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
                    char reciverbyte2 = (char) (crc & 0x00FF);
                    mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);
                    String v1 = String.format("%02x", (0xff & datar[0]));
                    String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
                    String v3 = String.format("%02x", (0xff & datar[2]));
                    String v4 = String.format("%02x", (0xff & datar[3]));
                    String v5 = Integer.toHexString(mCRCFinalValue);
                    String mMOBADDRESS = "";
                    String mMobADR = parameterSettingList.get(mWriteAllCounterValue).getMobBTAddress();
                    if (!mMobADR.equalsIgnoreCase("")) {
                        int mLenth = mMobADR.length();
                        if (mLenth == 1) {
                            mMOBADDRESS = "000" + mMobADR;
                        } else if (mLenth == 2) {
                            mMOBADDRESS = "00" + mMobADR;
                        }
                        if (mLenth == 3) {
                            mMOBADDRESS = "0" + mMobADR;
                        } else {
                            mMOBADDRESS = mMobADR;
                        }
                        String modeBusCommand = "0106" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                        System.out.println("mTotalTime==>>vvv==>> " + modeBusCommand);

                        new SettingParameterActivity.BluetoothCommunicationForDynamicParameterWriteAll().execute(modeBusCommand, modeBusCommand, "OK");

                    } else {
                        Toast.makeText(SettingParameterActivity.this, getResources().getString(R.string.addressnotfound), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    System.out.println("222");
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("111");
            }

        }
    }






    private void onTimeout() throws IOException {
        // Additional handling if needed when the task times out
        ShowAlertResponse("-2");
        hiddeProgressDialogue();
    }

    public static int CRC16_MODBUS(char[] buf, int len) {

        int crc = 0xFFFF;
        int pos = 0, i = 0;
        for (pos = 0; pos < len; pos++) {
            crc ^= (int) buf[pos];    // XOR byte into least sig. byte of crc

            for (i = 8; i != 0; i--) {    // Loop over each bit
                if ((crc & 0x0001) != 0) {      // If the LSB is set
                    crc >>= 1;                    // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                } else                            // Else LSB is not set
                    crc >>= 1;                    // Just shift right
            }
        }

        return crc;
    }


    private void ShowAlertResponse(String value) throws IOException {
        LayoutInflater inflater = (LayoutInflater) SettingParameterActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.devicealert,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingParameterActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        ImageView icon = layout.findViewById(R.id.user_img);
        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);

        if (value.equals("-1")) {
            icon.setImageDrawable(getDrawable(R.drawable.cross));
        } else if (value.equals("0")) {
            icon.setImageDrawable(getDrawable(R.drawable.ic_tick));
        } else if (value.equals("1")) {
            icon.setImageDrawable(getDrawable(R.drawable.ic_tick));
        }  else if (value.equals("3")) {
            icon.setImageDrawable(getDrawable(R.drawable.ic_tick));
        } else {
            icon.setImageDrawable(getDrawable(R.drawable.cross));
        }

        if (value.equals("0")) {
            title_txt.setText(getResources().getString(R.string.alertGetMessage));
        } else if (value.equals("1")) {
            title_txt.setText(getResources().getString(R.string.alertSetMessage));
        } else if (value.equals("-1")) {
            title_txt.setText(getResources().getString(R.string.alertNotSetMessage));
        } else if (value.equals("3")) {
            title_txt.setText(getResources().getString(R.string.parameterSetSuccessfully));
        } else if (value.equals("-2")) {
            parameterSettingList.get(mWriteAllCounterValue).setSet(false);
            settingParameterAdapter.notifyItemChanged(mWriteAllCounterValue);
            btSocket.close();
            title_txt.setText(getResources().getString(R.string.operationUnsuccessfull)+" on "+ parameterSettingList.get(mWriteAllCounterValue).getParametersName());
        } else if (value.equals("-3")) {
            parameterSettingList.get(mWriteAllCounterValue).setSet(false);
            settingParameterAdapter.notifyItemChanged(mWriteAllCounterValue);
            btSocket.close();
            title_txt.setText(getResources().getString(R.string.pairedDevice));
        }


            OK_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (value.equals("3")) {
                        if(!databaseHelper.isRecordExist(TABLE_PARAMETER_SET_DATA,KEY_BILL_NO,billNo)) {
                            databaseHelper.insertParameterSet(billNo, "true");
                        }else {
                            databaseHelper.updateParameterSet(billNo,"true");
                        }
                        onBackPressed();
                    }
                }
            });

    }


    private void connectToBluetoothSocket() throws IOException {


        try {
            if (btSocket != null) {
                if (!btSocket.isConnected()) {
                    btSocket.connect();//start connection
                }
            } else {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(BluetoothAddress);//connects to the device's address and checks if it's available
                btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                myBluetooth.cancelDiscovery();

                if (!btSocket.isConnected()) {
                    btSocket.connect();//start connection
                }
            }
            if (!btSocket.isConnected()){
                btSocket.connect();
            }else{
                ispaired = true;
            }
        } catch (Exception e) {
            CustomUtility.hideProgressDialog(this);
            ispaired = false;

            runOnUiThread(() -> {
                try {
                    ShowAlertResponse("-3");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
//            runOnUiThread(() -> CustomUtility.ShowToast(getResources().getString(R.string.pairedDevice), getApplicationContext()));
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_unsync, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_menu_unsync:
                if (CustomUtility.isInternetOn(getApplicationContext())) {
                    new getAllParameters().execute();


                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialogue(String message) {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(SettingParameterActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.show();
        });
    }

    private void hiddeProgressDialogue() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

}