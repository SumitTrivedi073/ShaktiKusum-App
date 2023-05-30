package com.shaktipumplimited.SetParameter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

import activity.BaseActivity;
import database.DatabaseHelper;
import debugapp.GlobalValue.UtilMethod;
import com.shaktipumplimited.SettingModel.AllPopupUtil;
import com.shaktipumplimited.SettingModel.DatabaseHelperTeacher;
import com.shaktipumplimited.SettingModel.SettingModelResponse;
import com.shaktipumplimited.SettingModel.SettingParameterModel;
import com.shaktipumplimited.SettingModel.SettingParameterResponse;
import com.shaktipumplimited.retrofit.BaseRequest;
import com.shaktipumplimited.retrofit.RequestReciever;
import com.shaktipumplimited.shaktikusum.R;
import webservice.WebURL;

import static java.lang.Thread.sleep;


public class DeviceSettingActivity extends BaseActivity {
    Timer timer;
int counterValue = 0;
int mWriteAllCounterValue = 0;
int mReadAllCounterValue = 0;
    private List<SettingParameterResponse> mSettingParameterResponse;
    private Toolbar mToolbar;
    Context mContext;
    DatabaseHelper mDatabaseHelper;
    SwitchCompat switchCompat;
    String speed_mode_param_value = "null";
    private ProgressDialog progressDialog;
    String speed_mode = "null";
    //  TextView read_speed_mode,change_speed_mode,speed_mode_type ;
    boolean read_only = true;
    boolean speed_checked;
    String MUserId = "null", DeviceType = "null", DeviceNo = "null", Mobile = "null", CustomerName = "null",
            RMSingalStr = "null", RMStatusOfProduct = "null", RMLatitude = "null", RMDate = " ";

    //  private RecyclerView rclSettingListViewID;

    private LinearLayoutManager lLayout;
    private RecyclerView.Adapter recyclerViewAdapter;
    private BaseRequest baseRequest;

    private List<SettingModelResponse> mSettingModelResponse;
    private List<EditText> mEditTextList;
    private List<TextView> mTextViewSetIDtList;
    private DatabaseHelperTeacher databaseHelperTeacher;

    private BluetoothSocket btSocket;
    private BluetoothAdapter myBluetooth;

    private UUID mMyUDID;
    private InputStream iStream;
    private Activity mActivity;

    RelativeLayout iv_sub_linearlayout12;
    LinearLayout lvlMainParentLayoutID;

    RelativeLayout rlvMainDynamicViewID;

    private int edtValue = 0;
    private float edtValueFloat = 0;
    private String old_data = "1";

    float mTotalTimeFloatData;
    char mCRCFinalValue;
    char mCRCFinalValueWrite;
    int i = 0;
Intent myIntent;
String mMaterialCode;
    int mGlobalPosition = 0;
    int mGlobalPositionSet = 0;

    ImageView imgRefreshiconID;
    ImageView imgBluetoothiconID;
    LinearLayout lvlBackIconViewID;

    CardView cardViewAddDynamicViewID;
    private boolean mBLTCheckValue = false;

    private RelativeLayout rlvGetAllViewID, rlvSetAllViewID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);

        mDatabaseHelper = new DatabaseHelper(this);
        baseRequest = new BaseRequest(this);
        mSettingParameterResponse = new ArrayList<>();
        databaseHelperTeacher = new DatabaseHelperTeacher(this);
        //   rclSettingListViewID = (RecyclerView) findViewById(R.id.rclSettingListViewID);
        lLayout = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mEditTextList = new ArrayList<>();
        mTextViewSetIDtList = new ArrayList<>();
        mSettingModelResponse = new ArrayList<>();

        myIntent = getIntent(); // gets the previously created intent
        mMaterialCode = myIntent.getStringExtra("MCode"); // will return "FirstKeyValue"
        //   rclSettingListViewID.setNestedScrollingEnabled(false);
        //  rclSettingListViewID.setLayoutManager(lLayout);
        // mToolbar = (Toolbar) findViewById(R.id.toolbar);
        rlvGetAllViewID = findViewById(R.id.rlvGetAllViewID);
        rlvSetAllViewID = findViewById(R.id.rlvSetAllViewID);
        lvlBackIconViewID = (LinearLayout) findViewById(R.id.lvlBackIconViewID);
       // imgRefreshiconID = (ImageView) findViewById(R.id.imgRefreshiconID);
        imgBluetoothiconID = (ImageView) findViewById(R.id.imgBluetoothiconID);

        mContext = this;
        mActivity = this;
        //  setSupportActionBar(mToolbar);

      /*  Bundle bundle = getIntent().getExtras();
        MUserId = bundle.getString("MUserId");
        DeviceType = bundle.getString("DeviceType");
        DeviceNo = bundle.getString("DeviceNo");*/
        initView();
        // Toast.makeText(mContext, "CustomUtility.getDeviceId(mContext)==>>"+CustomUtility.getDeviceId(mContext), Toast.LENGTH_SHORT).show();
        // callSettingParameterValueAPI();
        if(UtilMethod.isOnline(mContext))
        {
            callGetPlantListCheckAPI();
        }
        else
        {
            if(mSettingParameterResponse.size() > 0)
                mSettingParameterResponse.clear();

          mSettingParameterResponse =  mDatabaseHelper.getSettingPerameterList(mMaterialCode);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addDynamicViewProNew(mSettingParameterResponse);
            }
        }

        //addDynamicViewPro(mSettingModelResponse);
    }

    private void initView() {
        lvlMainParentLayoutID = (LinearLayout) findViewById(R.id.lvlMainParentLayoutID);






        imgBluetoothiconID.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                //finish();
                if(mBLTCheckValue)
                {
                    mBLTCheckValue = false;
                    imgBluetoothiconID.setImageResource(R.drawable.ic_bluetooth_gray);
                }
                else
                {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        if (AllPopupUtil.pairedDeviceListGloable(mContext)) {

                            if (WebURL.BT_DEVICE_NAME.equalsIgnoreCase("")  || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("")) {
                                Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                                mContext.startActivity(intent);
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

                    mBLTCheckValue = true;
                    imgBluetoothiconID.setImageResource(R.drawable.ic_bluetooth_blue);
                }
            }
        });

        lvlBackIconViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgBluetoothiconID.setVisibility(View.VISIBLE);
     //   imgRefreshiconID.setVisibility(View.VISIBLE);


        rlvGetAllViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    mGlobalPosition = 0;
                mReadAllCounterValue = 0;
                WebURL.mSettingCheckValue = "1";

                    String sspp = mSettingParameterResponse.get(mReadAllCounterValue).getOffset() + "";

                    if (!sspp.equalsIgnoreCase("") && !sspp.equalsIgnoreCase("0") && !sspp.equalsIgnoreCase("0.0")) {
                        edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mReadAllCounterValue).getOffset() + "");
                    } else {
                        edtValueFloat = 1;
                    }

                    char[] datar = new char[4];
                    int a = Float.floatToIntBits((float) edtValueFloat);
                    // int a= (int) edtValueFloat;
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
                    String mMobADR = mSettingParameterResponse.get(0).getMobBTAddress();
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
                    } else {
                        Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                    }
                    String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write

                    //  String modeBusCommand = "0103"+mSettingModelResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                    System.out.println("mTotalTime==>>vvv=>>offset==>>" + edtValueFloat);
                    System.out.println("mTotalTime==>>vvv=>>Read_Input==>>" + modeBusCommand);
                    mTotalTimeFloatData = 0;
                    new BluetoothCommunicationForDynamicParameterReadAll().execute(modeBusCommand, modeBusCommand, "OK");



            }
        });

        rlvSetAllViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* for (int j = 0; j < mSettingParameterResponse.size(); j++) {


                    mGlobalPositionSet = 0;

                    try {
                   */
                /*     int iii = v.getId();
                        int pp = iii - 3;
                        mGlobalPosition = pp;*/
                /*
                        //Toast.makeText(mContext, "jai hooo...==>>  "+pp, Toast.LENGTH_SHORT).show();
                        String mStringCeck = mEditTextList.get(j).getText().toString().trim();

                        System.out.println("Vikas!@#==>>"+mStringCeck);

                        if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                            edtValueFloat = Float.parseFloat(mEditTextList.get(j).getText().toString().trim());
                        } else {
                            edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(j).getOffset() + "");
                        }
                        // edtValueFloat = Float.parseFloat(mEditTextList.get(pp).getText().toString().trim());
                        //  edtValue = Integer.parseInt(mEditTextList.get(pp).getText().toString().trim());
                        //   if (edtValueFloat >= mSettingParameterResponse.get(pp).getPMin() && edtValueFloat <= mSettingParameterResponse.get(pp).getPMax())
                        {
                            */
                /*  setDeviceMode(mSettingModelResponse.get(position).getAddress());*/
                /*
                            //changeButtonVisibility(true, 1.0f, holder);

                            counterValue = 0;
                            char[] datar = new char[4];
                            // int a=Float.floatToIntBits((float) edtValue);
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
                            String mMobADR = mSettingParameterResponse.get(j).getMobBTAddress();
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
                                //  String modeBusCommand1 = "0103"+mSettingModelResponse.get(position).getMobBTAddress()+""+"crc";

                                new BluetoothCommunicationForDynamicParameterWriteAll().execute(modeBusCommand, modeBusCommand, "OK");
                            } else {
                                Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                            }
                            //String modeBusCommand = "0106"+Integer.toHexString(mSettingModelResponse.get(position).getMobBTAddress())+Integer.toHexString(datar[0])+Integer.toHexString(datar[1])+Integer.toHexString(datar[2])+Integer.toHexString(datar[3])+Integer.toHexString(reciverbyte1)+Integer.toHexString(reciverbyte2);//write
                            // String modeBusCommand = "0106"+mSettingModelResponse.get(i).getMobBTAddress()+Integer.toHexString(datar[0])+Integer.toHexString(datar[1])+Integer.toHexString(datar[2])+Integer.toHexString(datar[3])+Integer.toHexString(mCRCFinalValue);//write
                            //   String modeBusCommand = "0106"+mSettingModelResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                        }
                      *//*  else {
                            Toast.makeText(mContext, "Please enter value between min=" + mSettingModelResponse.get(pp).getPMin() + " Max=" + mSettingModelResponse.get(pp).getPMax(), Toast.LENGTH_SHORT).show();
                        }*//*
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }*/


                //   for (int j = 0; j < mSettingParameterResponse.size(); j++) {

                WebURL.mSettingCheckValue = "1";
                mGlobalPositionSet = 0;
                System.out.println("mWriteAllCounterValue==>>"+mWriteAllCounterValue);
                if(mSettingParameterResponse.size() >= mWriteAllCounterValue)
                {
                try {


                    String mStringCeck = mEditTextList.get(mWriteAllCounterValue).getText().toString().trim();

                    System.out.println("Vikas!@#==>>" + mStringCeck);

                    if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                        edtValueFloat = Float.parseFloat(mEditTextList.get(mWriteAllCounterValue).getText().toString().trim());
                    } else {
                        edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mWriteAllCounterValue).getOffset() + "");
                    }
                    // edtValueFloat = Float.parseFloat(mEditTextList.get(pp).getText().toString().trim());
                    //  edtValue = Integer.parseInt(mEditTextList.get(pp).getText().toString().trim());
                    //   if (edtValueFloat >= mSettingParameterResponse.get(pp).getPMin() && edtValueFloat <= mSettingParameterResponse.get(pp).getPMax())
                    {
                        /*  setDeviceMode(mSettingModelResponse.get(position).getAddress());*/
                        //changeButtonVisibility(true, 1.0f, holder);

                        counterValue = 0;
                        char[] datar = new char[4];
                        // int a=Float.floatToIntBits((float) edtValue);
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
                        String mMobADR = mSettingParameterResponse.get(mWriteAllCounterValue).getMobBTAddress();
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
                            //  String modeBusCommand1 = "0103"+mSettingModelResponse.get(position).getMobBTAddress()+""+"crc";

                            new BluetoothCommunicationForDynamicParameterWriteAll().execute(modeBusCommand, modeBusCommand, "OK");

                        } else {
                            Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                        }
                        //String modeBusCommand = "0106"+Integer.toHexString(mSettingModelResponse.get(position).getMobBTAddress())+Integer.toHexString(datar[0])+Integer.toHexString(datar[1])+Integer.toHexString(datar[2])+Integer.toHexString(datar[3])+Integer.toHexString(reciverbyte1)+Integer.toHexString(reciverbyte2);//write
                        // String modeBusCommand = "0106"+mSettingModelResponse.get(i).getMobBTAddress()+Integer.toHexString(datar[0])+Integer.toHexString(datar[1])+Integer.toHexString(datar[2])+Integer.toHexString(datar[3])+Integer.toHexString(mCRCFinalValue);//write
                        //   String modeBusCommand = "0106"+mSettingModelResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                    }
                      /*  else {
                            Toast.makeText(mContext, "Please enter value between min=" + mSettingModelResponse.get(pp).getPMin() + " Max=" + mSettingModelResponse.get(pp).getPMax(), Toast.LENGTH_SHORT).show();
                        }*/
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
               // }


            }
        });

      /*  imgRefreshiconID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  for (int jj = 0; jj < mSettingModelResponse.size(); jj++)
                {
                    mGlobalPosition = 0;

                    String sspp = mSettingParameterResponse.get(mGlobalPosition).getOffset() + "";

                    if (!sspp.equalsIgnoreCase("") && !sspp.equalsIgnoreCase("0") && !sspp.equalsIgnoreCase("0.0")) {
                        edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mGlobalPosition).getOffset() + "");
                    } else {
                        edtValueFloat = 1;
                    }

                    char[] datar = new char[4];
                    int a = Float.floatToIntBits((float) edtValueFloat);
                    // int a= (int) edtValueFloat;
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
                    String mMobADR = mSettingModelResponse.get(0).getMobBTAddress();
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
                    } else {
                        Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                    }
                    String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write

                    //  String modeBusCommand = "0103"+mSettingModelResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                    System.out.println("mTotalTime==>>vvv=>>offset==>>" + edtValueFloat);
                    System.out.println("mTotalTime==>>vvv=>>Read_Input==>>" + modeBusCommand);
                    mTotalTimeFloatData = 0;
                    new BluetoothCommunicationForDynamicParameterReadRefresh().execute(modeBusCommand, modeBusCommand, "OK");

                }
            }
        });*/

    }

    @Override
    public void onDestroy() {
        try {
            progressDialog.dismiss();

            if (progressDialog != null)
                progressDialog = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

/*    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };*/
    ////////////////vikas service calling new

    private void callSettingParameterValueAPI() {

        mSettingModelResponse = databaseHelperTeacher.getDevicePARAMeterListData(DeviceType);

        //  addDynamicViewProORG(mSettingModelResponse);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void addDynamicViewProNew(final List<SettingParameterResponse> mSettingParameterResponse) {
        try {
            if (mEditTextList.size() > 0) {
                mEditTextList.clear();
            }
            if (mTextViewSetIDtList.size() > 0) {
                mTextViewSetIDtList.clear();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///  addHeadersMonths();
                    lvlMainParentLayoutID.removeAllViews();
                    // addDataMonth(mPostionFinal + 1, mvDay + "", mvMonth + "", mvYear + "", mvHour, mvMinute, mvNo_of_Start, fvFrequency, fvRMSVoltage, fvOutputCurrent, mvRPM, fvLPM, fvPVVoltage, fvPVCurrent, mvFault, fvInvTemp);
                }
            });


        } catch (Exception exp) {
            exp.printStackTrace();
        }

        for (i = 0; i < mSettingParameterResponse.size(); i++)
        //for (int i = 0; i < 10; i++)
        {

            cardViewAddDynamicViewID = new CardView(this);
            CardView.LayoutParams cardViewAddDynamicViewIDoutparams12 = new CardView.LayoutParams
                    ((int) CardView.LayoutParams.MATCH_PARENT, (int) CardView.LayoutParams.WRAP_CONTENT);
            //cardViewAddDynamicViewIDoutparams12.setMarginEnd((int) getResources().getDimension(R.dimen._1sdp));
            cardViewAddDynamicViewIDoutparams12.setMarginEnd((int) getResources().getDimension(R.dimen._1sdp));
            cardViewAddDynamicViewIDoutparams12.setMarginStart((int) getResources().getDimension(R.dimen._1sdp));
            cardViewAddDynamicViewIDoutparams12.setMargins((int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp));
            cardViewAddDynamicViewID.setCardBackgroundColor(getResources().getColor(R.color.white));
            cardViewAddDynamicViewID.setRadius(15);
            // cardViewAddDynamicViewID.setId(mIDIndex + 1);
            cardViewAddDynamicViewID.setElevation(5);
            cardViewAddDynamicViewID.setLayoutParams(cardViewAddDynamicViewIDoutparams12);
            //lvlMainParentLayoutID.add(cardViewAddDynamicViewID);

            rlvMainDynamicViewID = new RelativeLayout(this);
            RelativeLayout.LayoutParams rlvMainDynamicViewIDParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen._50sdp));

            //  rlvMainDynamicViewIDParam.setMargins(10, 5, 10, 5);
            rlvMainDynamicViewIDParam.setMargins((int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp));
            rlvMainDynamicViewIDParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            rlvMainDynamicViewID.setLayoutParams(rlvMainDynamicViewIDParam);

            // cardViewAddDynamicViewID.addView(rlvMainDynamicViewID);

            iv_sub_linearlayout12 = new RelativeLayout(this);
            RelativeLayout.LayoutParams iv_outparams12 = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.MATCH_PARENT);
            iv_outparams12.setMarginEnd((int) getResources().getDimension(R.dimen._1sdp));
            iv_outparams12.setMarginStart((int) getResources().getDimension(R.dimen._1sdp));
            iv_outparams12.setMargins((int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp));
            iv_sub_linearlayout12.setBackgroundColor(getResources().getColor(R.color.white));
            //  iv_sub_linearlayout12.setOrientation(LinearLayout.HORIZONTAL);
            iv_sub_linearlayout12.setLayoutParams(iv_outparams12);

            TextView txtPeraNameID = new TextView(this);
            RelativeLayout.LayoutParams txtFromTextHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            //txtFromTextHeadParam.setMarginStart(20);
            // txtFromTextHeadParam.setMargins(0, 5, 0, 5);
            txtFromTextHeadParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
            txtPeraNameID.setGravity(Gravity.CENTER_HORIZONTAL);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            // txtPeraNameID.setText(getResources().getString(R.string.Get_text));
            txtPeraNameID.setText(mSettingParameterResponse.get(i).getParametersName());
            /// txtPeraNameID.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.textsize));
            txtPeraNameID.setTextSize((int) getResources().getDimension(R.dimen._4ssp));
            txtPeraNameID.setId(i + 1);
            txtPeraNameID.setTypeface(null, Typeface.BOLD);
            txtPeraNameID.setTextColor(getResources().getColor(R.color.black));
            txtPeraNameID.setLayoutParams(txtFromTextHeadParam);
            iv_sub_linearlayout12.addView(txtPeraNameID);

            RelativeLayout rlvMainViewLayoutIN = new RelativeLayout(this);
            RelativeLayout.LayoutParams rlvMainParamIN = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen._45sdp));
            rlvMainParamIN.setMargins((int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp));
            rlvMainParamIN.addRule(RelativeLayout.BELOW, txtPeraNameID.getId());
            rlvMainViewLayoutIN.setLayoutParams(rlvMainParamIN);
            iv_sub_linearlayout12.addView(rlvMainViewLayoutIN);

            TextView txtGetID = new TextView(this);
            RelativeLayout.LayoutParams txtGetIDParam = new RelativeLayout.LayoutParams
                    ((int) (int) getResources().getDimension(R.dimen._60sdp),  (int) getResources().getDimension(R.dimen._45sdp));
            //txtGetIDParam.setMarginStart(20);
            // txtGetIDParam.setMargins(0, 0, 0, 0);
            txtGetIDParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtGetID.setGravity(Gravity.CENTER);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtGetID.setText(getResources().getString(R.string.Get_text));
            txtGetID.setTextSize((int) getResources().getDimension(R.dimen._4ssp));
            txtGetID.setId(i + 2);
            txtGetID.setTypeface(null, Typeface.BOLD);

            txtGetID.setTextColor(getResources().getColor(R.color.white));
            txtGetID.setBackground(getResources().getDrawable(R.drawable.rounded_shape));
            // txtGetID.setBackground(getResources().getDrawable(R.drawable.blue_btn_round));
            txtGetID.setLayoutParams(txtGetIDParam);
            rlvMainViewLayoutIN.addView(txtGetID);

            txtGetID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mBLTCheckValue)
                    {
                        int iii = v.getId();
                        int pp = iii - 2;
                        mGlobalPosition = pp;

                        String spsp = mSettingParameterResponse.get(pp).getOffset() + "";
                        if (!spsp.equalsIgnoreCase("") && !spsp.equalsIgnoreCase("0") && !spsp.equalsIgnoreCase("0.0")) {
                            edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(pp).getOffset() + "");
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
                    //    String mMobADR = mSettingParameterResponse.get(pp).getModbusaddress();
                        String mMobADR = mSettingParameterResponse.get(pp).getMobBTAddress();
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
                            Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                        }
                        final String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                        //  String modeBusCommand = "0103"+mSettingParameterResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                        System.out.println("mTotalTime==>>vvv=modeBusCommand=>>" + modeBusCommand);
                        mTotalTimeFloatData = 0;

                        baseRequest.showLoader();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        new BluetoothCommunicationForDynamicParameterRead().execute(modeBusCommand, modeBusCommand, "OK");
                                        //Log.i("tag","A Kiss after 5 seconds");
                                    }
                                }, 3000);
                    }
                    else
                    {
                        mBLTCheckValue = true;
                        imgBluetoothiconID.setImageResource(R.drawable.ic_bluetooth_blue);

                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBluetoothAdapter.isEnabled()) {
                            if (AllPopupUtil.pairedDeviceListGloable(mContext)) {

                                if (WebURL.BT_DEVICE_NAME.equalsIgnoreCase("") || WebURL.BT_DEVICE_NAME.equalsIgnoreCase(null) || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("") || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase(null)) {
                                    Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                                    mContext.startActivity(intent);
                                }
                                else
                                {
                                    int iii = v.getId();
                                    int pp = iii - 2;
                                    mGlobalPosition = pp;

                                    String spsp = mSettingParameterResponse.get(pp).getOffset() + "";
                                    if (!spsp.equalsIgnoreCase("") && !spsp.equalsIgnoreCase("0") && !spsp.equalsIgnoreCase("0.0")) {
                                        edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(pp).getOffset() + "");
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
                                  //  String mMobADR = mSettingParameterResponse.get(pp).getModbusaddress();
                                    String mMobADR = mSettingParameterResponse.get(pp).getMobBTAddress();
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
                                        Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                                    }
                                    final String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                                    //  String modeBusCommand = "0103"+mSettingParameterResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                                    System.out.println("mTotalTime==>>vvv=modeBusCommand=>>" + modeBusCommand);
                                    mTotalTimeFloatData = 0;

                                    baseRequest.showLoader();
                                    new Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    new BluetoothCommunicationForDynamicParameterRead().execute(modeBusCommand, modeBusCommand, "OK");
                                                    //Log.i("tag","A Kiss after 5 seconds");
                                                }
                                            }, 100);
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
                    //  new BluetoothCommunicationForDynamicParameterRead().execute(modeBusCommand, modeBusCommand, "OK");
//vikas
                }
            });

            TextView txtSetID = new TextView(this);
            RelativeLayout.LayoutParams txtSetIDParam = new RelativeLayout.LayoutParams
                    ( (int) getResources().getDimension(R.dimen._60sdp), (int) getResources().getDimension(R.dimen._45sdp));
            //txtSetIDParam.setMarginStart(20);
            // txtSetIDParam.setMargins(0, 0, 0, 0);
            txtSetIDParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            txtSetID.setGravity(Gravity.CENTER);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtSetID.setText(getResources().getString(R.string.Set_text));
            txtSetID.setTextSize((int) getResources().getDimension(R.dimen._4ssp));
            txtSetID.setId(i + 3);
            // txtGetID.setTag(i+3);
            txtSetID.setTypeface(null, Typeface.BOLD);
            txtSetID.setTextColor(getResources().getColor(R.color.white));
            //  txtSetID.setBackground(getResources().getDrawable(R.drawable.blue_btn_round));
            txtSetID.setBackground(getResources().getDrawable(R.drawable.rounded_shape));
            txtSetID.setLayoutParams(txtSetIDParam);
            mTextViewSetIDtList.add(txtSetID);
            //if(mSettingParameterResponse.get(i).getEditValue() != null && !mSettingParameterResponse.get(i).getEditValue().equalsIgnoreCase(""))
            String sssss = String.valueOf(mSettingParameterResponse.get(i).getPValue());

            if (!sssss.equalsIgnoreCase("0.0") && String.valueOf(mSettingParameterResponse.get(i).getPValue()) != null && !String.valueOf(mSettingParameterResponse.get(i).getPValue()).equalsIgnoreCase("")) {
                changeButtonVisibility(true, 1.0f, mTextViewSetIDtList.get(i));
            } else {
                changeButtonVisibility(false, 0.5f, mTextViewSetIDtList.get(i));
            }
            rlvMainViewLayoutIN.addView(txtSetID);
            //yo leno hai
            txtSetID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mBLTCheckValue)
                    {
                        try {
                            int iii = v.getId();
                            int pp = iii - 3;
                            mGlobalPosition = pp;
                            //Toast.makeText(mContext, "jai hooo...==>>  "+pp, Toast.LENGTH_SHORT).show();
                            String mStringCeck = mEditTextList.get(pp).getText().toString().trim();
                            if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                                edtValueFloat = Float.parseFloat(mEditTextList.get(pp).getText().toString().trim());
                            } else {
                                edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(pp).getOffset() + "");
                            }
                            // edtValueFloat = Float.parseFloat(mEditTextList.get(pp).getText().toString().trim());
                            //  edtValue = Integer.parseInt(mEditTextList.get(pp).getText().toString().trim());
                            //   if (edtValueFloat >= mSettingParameterResponse.get(pp).getPMin() && edtValueFloat <= mSettingParameterResponse.get(pp).getPMax())
                            {
                                /*  setDeviceMode(mSettingModelResponse.get(position).getAddress());*/
                                //changeButtonVisibility(true, 1.0f, holder);
                                char[] datar = new char[4];
                                // int a=Float.floatToIntBits((float) edtValue);
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
                                String mMobADR = mSettingParameterResponse.get(pp).getMobBTAddress();
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
                                    //  String modeBusCommand1 = "0103"+mSettingModelResponse.get(position).getMobBTAddress()+""+"crc";
                                    new BluetoothCommunicationForDynamicParameterWrite().execute(modeBusCommand, modeBusCommand, "OK");
                                } else {
                                    Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                                }
                                //String modeBusCommand = "0106"+Integer.toHexString(mSettingModelResponse.get(position).getMobBTAddress())+Integer.toHexString(datar[0])+Integer.toHexString(datar[1])+Integer.toHexString(datar[2])+Integer.toHexString(datar[3])+Integer.toHexString(reciverbyte1)+Integer.toHexString(reciverbyte2);//write
                                // String modeBusCommand = "0106"+mSettingModelResponse.get(i).getMobBTAddress()+Integer.toHexString(datar[0])+Integer.toHexString(datar[1])+Integer.toHexString(datar[2])+Integer.toHexString(datar[3])+Integer.toHexString(mCRCFinalValue);//write
                                //   String modeBusCommand = "0106"+mSettingModelResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                            }
                      /*  else {
                            Toast.makeText(mContext, "Please enter value between min=" + mSettingModelResponse.get(pp).getPMin() + " Max=" + mSettingModelResponse.get(pp).getPMax(), Toast.LENGTH_SHORT).show();
                        }*/
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBluetoothAdapter.isEnabled()) {
                            if (AllPopupUtil.pairedDeviceListGloable(mContext)) {

                                if (WebURL.BT_DEVICE_NAME.equalsIgnoreCase("") || WebURL.BT_DEVICE_NAME.equalsIgnoreCase(null) || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("") || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase(null)) {
                                    Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                                    mContext.startActivity(intent);
                                }
                                else
                                {
                                     try {
                                    int iii = v.getId();
                                    int pp = iii - 3;
                                    mGlobalPosition = pp;
                                    //Toast.makeText(mContext, "jai hooo...==>>  "+pp, Toast.LENGTH_SHORT).show();
                                    String mStringCeck = mEditTextList.get(pp).getText().toString().trim();
                                    if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                                        edtValueFloat = Float.parseFloat(mEditTextList.get(pp).getText().toString().trim());
                                    } else {
                                        edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(pp).getOffset() + "");
                                    }
                                    // edtValueFloat = Float.parseFloat(mEditTextList.get(pp).getText().toString().trim());
                                    //  edtValue = Integer.parseInt(mEditTextList.get(pp).getText().toString().trim());
                                    //   if (edtValueFloat >= mSettingParameterResponse.get(pp).getPMin() && edtValueFloat <= mSettingParameterResponse.get(pp).getPMax())
                                    {
                                        /*  setDeviceMode(mSettingModelResponse.get(position).getAddress());*/
                                        //changeButtonVisibility(true, 1.0f, holder);
                                        char[] datar = new char[4];
                                        // int a=Float.floatToIntBits((float) edtValue);
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
                                        String mMobADR = mSettingParameterResponse.get(pp).getMobBTAddress();
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
                                            //  String modeBusCommand1 = "0103"+mSettingModelResponse.get(position).getMobBTAddress()+""+"crc";
                                            new BluetoothCommunicationForDynamicParameterWrite().execute(modeBusCommand, modeBusCommand, "OK");
                                        } else {
                                            Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                                        }
                                        //String modeBusCommand = "0106"+Integer.toHexString(mSettingModelResponse.get(position).getMobBTAddress())+Integer.toHexString(datar[0])+Integer.toHexString(datar[1])+Integer.toHexString(datar[2])+Integer.toHexString(datar[3])+Integer.toHexString(reciverbyte1)+Integer.toHexString(reciverbyte2);//write
                                        // String modeBusCommand = "0106"+mSettingModelResponse.get(i).getMobBTAddress()+Integer.toHexString(datar[0])+Integer.toHexString(datar[1])+Integer.toHexString(datar[2])+Integer.toHexString(datar[3])+Integer.toHexString(mCRCFinalValue);//write
                                        //   String modeBusCommand = "0106"+mSettingModelResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                                    }
                      /*  else {
                            Toast.makeText(mContext, "Please enter value between min=" + mSettingModelResponse.get(pp).getPMin() + " Max=" + mSettingModelResponse.get(pp).getPMax(), Toast.LENGTH_SHORT).show();
                        }*/
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
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

                        mBLTCheckValue = true;
                        imgBluetoothiconID.setImageResource(R.drawable.ic_bluetooth_blue);
                    }
                }
            });
            RelativeLayout rlvEDITLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams rlvEDITParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) (int) getResources().getDimension(R.dimen._45sdp));
            //rlvEDITParam.setMargins(130, 1, 130, 1);
            //rlvEDITParam.addRule(RelativeLayout.LEFT_OF, txtGetID.getId());
            // rlvEDITParam.addRule(RelativeLayout.RIGHT_OF, txtSetID.getId());
            rlvEDITParam.setMarginStart((int) getResources().getDimension(R.dimen._61sdp));
            rlvEDITParam.setMarginEnd((int) getResources().getDimension(R.dimen._61sdp));
            rlvEDITParam.addRule(RelativeLayout.CENTER_IN_PARENT);
            rlvEDITLayout.setLayoutParams(rlvEDITParam);
            
            rlvEDITLayout.setBackground(getResources().getDrawable(R.drawable.gray_round_corner));
            rlvMainViewLayoutIN.addView(rlvEDITLayout);
            //iv_sub_linearlayout12.addView(rlvEDITLayout);

            EditText edtValueID = new EditText(this);
            RelativeLayout.LayoutParams edtValueIDParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.MATCH_PARENT);
            //edtValueIDParam.setMarginStart(20);
            edtValueIDParam.addRule(RelativeLayout.ALIGN_PARENT_START);
          //  edtValueIDParam.setMargins((int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._1sdp));
            edtValueID.setGravity(Gravity.CENTER_VERTICAL);
            String ssssss = String.valueOf(mSettingParameterResponse.get(i).getPValue()*mSettingParameterResponse.get(i).getFactor());
            edtValueID.setText(ssssss);

         /*   if (!ssssss.equalsIgnoreCase("0.0") && String.valueOf(mSettingParameterResponse.get(i).getPValue()) != null && !String.valueOf(mSettingParameterResponse.get(i).getPValue()).equalsIgnoreCase("")) {
                edtValueID.setText(mSettingParameterResponse.get(i).getPValue() + "");
            } else {
                edtValueID.setText("");
            }*/
            // edtValueID.setText(mSettingModelResponse.get(i).getEditValue()+"");
            // edtValueID.setHint(getResources().getString(R.string.Get_text));

            edtValueID.setTextColor(getResources().getColor(R.color.black));
            edtValueID.setBackgroundColor(getResources().getColor(R.color.zxing_transparent));
            edtValueID.setTextSize((int) getResources().getDimension(R.dimen._4ssp));
            edtValueID.setId(i + 4);
            edtValueID.setMaxLines(1);
            edtValueID.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtValueID.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            edtValueID.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            edtValueID.setTypeface(null, Typeface.NORMAL);
            edtValueID.setTextColor(getResources().getColor(R.color.white));
            edtValueID.setLayoutParams(edtValueIDParam);

            rlvEDITLayout.addView(edtValueID);

            mEditTextList.add(edtValueID);

           /* TextView txtUNITID = new TextView(this);
            RelativeLayout.LayoutParams txtUNITIDParam = new RelativeLayout.LayoutParams
                    ((int) (int) getResources().getDimension(R.dimen._50sdp), (int) (int) RelativeLayout.LayoutParams.MATCH_PARENT);
            //txtUNITIDParam.setMarginStart(20);
            // txtUNITIDParam.setMargins(0, 0, 0, 0);
            txtUNITIDParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            txtUNITID.setGravity(Gravity.CENTER);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            // txtUNITID.setText(getResources().getString(R.string.Set_text));
            txtUNITID.setText(mSettingParameterResponse.get(i).getUnit());
            txtUNITID.setTextSize((float) (int) getResources().getDimension(R.dimen._6ssp));
            // txtGetID.setId(i + 5);
            txtUNITID.setTypeface(null, Typeface.NORMAL);
            //  tv_Code.setId(i + 2);
            txtUNITID.setTextColor(getResources().getColor(R.color.colorOrange));
            // txtUNITID.setBackground(getResources().getDrawable(R.drawable.blue_btn_round));
            txtUNITID.setLayoutParams(txtUNITIDParam);
            rlvEDITLayout.addView(txtUNITID);*/

            //mTagIDIndex++;
            rlvMainDynamicViewID.addView(iv_sub_linearlayout12);
            cardViewAddDynamicViewID.addView(rlvMainDynamicViewID);
            lvlMainParentLayoutID.addView(cardViewAddDynamicViewID);

        }

    }


    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterReadAll extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;
        int bytesRead = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            baseRequest.showLoader();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                baseRequest.showLoader();
                if (btSocket != null) {
                    if (!btSocket.isConnected()) {
                        btSocket.connect();//start connection
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(WebURL.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();

                    if (!btSocket.isConnected()) {
                        btSocket.connect();//start connection
                    }
                }

                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(1000);
                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {
                        //baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    int[] bytesReaded = new int[4];

                    for (int i = 0; i < 6; i++) {
                        int mCharOne11 = iStream.read();
                    }

                    //int[] mTotalTime;
                    int mTotalTime;

                    int jjj = 0;
                    // mTotalTime = new int[1];
                    for (int i = 0; i < 1; i++) {
                        try {
                            //bytesRead = iStream.read();
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
                            // int mCharThree = iStream.read();
                            //   int mCharFour = iStream.read();
                            //bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;
                            System.out.println("mTotalTime==>>vvv4  " + jjj + " " + mTotalTime);
                            //   mBTParameterList.add(Float.intBitsToFloat(mTotalTime[jjj])+"");
                            //arraylist.get(jjj).setValue(Float.intBitsToFloat(mTotalTime[jjj])+"");
                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //changeButtonVisibility(true, 1.0f, mEditTextList.get(mGlobalPosition));
                                    mSettingParameterResponse.get(mReadAllCounterValue).setPValue((int) mTotalTimeFloatData);
                                    // addDynamicViewPro(mSettingModelResponse);
                                    mEditTextList.get(mReadAllCounterValue).setText("" + mTotalTimeFloatData);
                                    System.out.println("mGlobalPosition==>>" + mReadAllCounterValue + "\nmTotalTimeFloatData==>>" + mTotalTimeFloatData);
                                    changeButtonVisibility(true, 1.0f, mTextViewSetIDtList.get(mReadAllCounterValue));


                                }
                            });

                            jjj++;

                            for (int ii = 0; ii < 4; ii++) {
                                int mCharOne11 = iStream.read();
                            }

                            while (iStream.available() > 0) {
                                int mCharOne11 = iStream.read();
                            }

                        } catch (IOException e) {
                            // baseRequest.hideLoader();
                            e.printStackTrace();
                        }
                    }

                    while (iStream.available() > 0) {
                        int mCharOne11 = iStream.read();
                    }
                }
            } catch (Exception e) {
                //baseRequest.hideLoader();
                e.printStackTrace();
                return false;
            }

            //  baseRequest.hideLoader();
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            // baseRequest.hideLoader();
            super.onPostExecute(result);

            mReadAllCounterValue = mReadAllCounterValue + 1;

            if(mReadAllCounterValue < mSettingParameterResponse.size()) {

                String sspp = mSettingParameterResponse.get(mReadAllCounterValue).getOffset() + "";

                if (!sspp.equalsIgnoreCase("") && !sspp.equalsIgnoreCase("0") && !sspp.equalsIgnoreCase("0.0")) {
                    edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mReadAllCounterValue).getOffset() + "");
                } else {
                    edtValueFloat = 1;
                }

                char[] datar = new char[4];
                int a = Float.floatToIntBits((float) edtValueFloat);
                // int a= (int) edtValueFloat;
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
                String mMobADR = mSettingParameterResponse.get(0).getMobBTAddress();
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
                } else {
                    Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                }
                String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write

                //  String modeBusCommand = "0103"+mSettingModelResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                System.out.println("mTotalTime==>>vvv=>>offset==>>" + edtValueFloat);
                System.out.println("mTotalTime==>>vvv=>>Read_Input==>>" + modeBusCommand);
                mTotalTimeFloatData = 0;
                new BluetoothCommunicationForDynamicParameterReadAll().execute(modeBusCommand, modeBusCommand, "OK");

            }
            else
            {
                baseRequest.hideLoader();
            }

        }
    }

    /////////////////////////bt read write
    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterWrite extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;
        int bytesRead = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            baseRequest.showLoader();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (btSocket.isConnected()) {

                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(WebURL.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                }

                if (!btSocket.isConnected())
                    btSocket.connect();//start connection


                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(1000);
                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }


                    int[] bytesReaded = new int[4];

                    for (int i = 0; i < 6; i++) {
                        int mCharOne11 = iStream.read();
                    }

                    //int[] mTotalTime;
                    int mTotalTime;

                    int jjj = 0;
                    // mTotalTime = new int[1];
                    for (int i = 0; i < 1; i++) {
                        try {
                            //bytesRead = iStream.read();
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
                            // int mCharThree = iStream.read();
                            //   int mCharFour = iStream.read();
//                            bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;/* mTotalTime[jjj] = bytesReaded[i];
                            mTotalTime[jjj] |= bytesReaded[i + 1] << 8;
                            mTotalTime[jjj] |= bytesReaded[i + 2] << 16;
                            mTotalTime[jjj] |= bytesReaded[i + 3] << 24;*/

                            System.out.println("mTotalTime==>>vvv4  " + jjj + " " + Float.intBitsToFloat(mTotalTime));
                            //   mBTParameterList.add(Float.intBitsToFloat(mTotalTime[jjj])+"");
                            //arraylist.get(jjj).setValue(Float.intBitsToFloat(mTotalTime[jjj])+"");
                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);
                            // mTotalTimeFloatData = (float) mTotalTime;

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //changeButtonVisibility(true, 1.0f, mEditTextList.get(mGlobalPosition));
                                    mSettingParameterResponse.get(mGlobalPosition).setPValue((int) mTotalTimeFloatData);
                                    // addDynamicViewPro(mSettingModelResponse);
                                    mEditTextList.get(mGlobalPosition).setText("" + mTotalTimeFloatData);
                                    //changeButtonVisibility(true,1.0f, mTextViewSetIDtList.get(mGlobalPosition));
                                }
                            });

                            jjj++;

                            for (int ii = 0; ii < 4; ii++) {
                                int mCharOne11 = iStream.read();
                            }

                            while (iStream.available() > 0) {
                                int mCharOne11 = iStream.read();
                            }

                        } catch (IOException e) {
                            baseRequest.hideLoader();
                            e.printStackTrace();
                        }
                    }

                    // baseRequest.hideLoader();
                }
            } catch (Exception e) {
                baseRequest.hideLoader();
                //btSocket = null;
                //mmCheckStart = 1;
                //   Toast.makeText(mContext, "BT Connection lost..", Toast.LENGTH_SHORT).show();
                // myBluetooth.disable();
                return false;
            }

            baseRequest.hideLoader();
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            baseRequest.hideLoader();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterWriteAll extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;
        int bytesRead = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            baseRequest.showLoader();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (btSocket.isConnected()) {

                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(WebURL.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                }

                if (!btSocket.isConnected())
                    btSocket.connect();//start connection


                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(1000);
                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    int[] bytesReaded = new int[4];

                    for (int i = 0; i < 6; i++) {
                        int mCharOne11 = iStream.read();
                    }

                    //int[] mTotalTime;
                    int mTotalTime;

                    int jjj = 0;
                    // mTotalTime = new int[1];
                    for (int i = 0; i < 1; i++) {
                        try {
                            //bytesRead = iStream.read();
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
                            // int mCharThree = iStream.read();
                            //   int mCharFour = iStream.read();
//                            bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;/* mTotalTime[jjj] = bytesReaded[i];
                            mTotalTime[jjj] |= bytesReaded[i + 1] << 8;
                            mTotalTime[jjj] |= bytesReaded[i + 2] << 16;
                            mTotalTime[jjj] |= bytesReaded[i + 3] << 24;*/

                            System.out.println("mTotalTime==>>vvv4  " + jjj + " " + Float.intBitsToFloat(mTotalTime));
                            System.out.println("mTotalTime==>>vikasVihu==>  " + mWriteAllCounterValue + " " + Float.intBitsToFloat(mTotalTime));
                            //   mBTParameterList.add(Float.intBitsToFloat(mTotalTime[jjj])+"");
                            //arraylist.get(jjj).setValue(Float.intBitsToFloat(mTotalTime[jjj])+"");
                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);
                            // mTotalTimeFloatData = (float) mTotalTime;

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //changeButtonVisibility(true, 1.0f, mEditTextList.get(mGlobalPosition));
                                    mSettingParameterResponse.get(mWriteAllCounterValue).setPValue((int) mTotalTimeFloatData);
                                    // addDynamicViewPro(mSettingModelResponse);
                                    mEditTextList.get(mWriteAllCounterValue).setText("" + mTotalTimeFloatData);
                                    //changeButtonVisibility(true,1.0f, mTextViewSetIDtList.get(mGlobalPosition));
                                }
                            });
                            jjj++;

                            for (int ii = 0; ii < 4; ii++) {
                                int mCharOne11 = iStream.read();
                            }

                            while (iStream.available() > 0) {
                                int mCharOne11 = iStream.read();
                            }

                        } catch (IOException e) {
                            baseRequest.hideLoader();
                            e.printStackTrace();
                        }
                    }

                    // baseRequest.hideLoader();
                }
            } catch (Exception e) {
                baseRequest.hideLoader();
                //btSocket = null;
                //mmCheckStart = 1;
                //   Toast.makeText(mContext, "BT Connection lost..", Toast.LENGTH_SHORT).show();
                // myBluetooth.disable();
                return false;
            }

          //  baseRequest.hideLoader();
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            mWriteAllCounterValue= mWriteAllCounterValue+1;
           try {
                if(mWriteAllCounterValue < mSettingParameterResponse.size())
                {
                   /* int iii = v.getId();
                        int pp = iii - 3;
                        mGlobalPosition = pp;*/
                //Toast.makeText(mContext, "jai hooo...==>>  "+pp, Toast.LENGTH_SHORT).show();
                String mStringCeck = mEditTextList.get(mWriteAllCounterValue).getText().toString().trim();
                System.out.println("Vikas!@#==>>" + mStringCeck);
                if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                    edtValueFloat = Float.parseFloat(mEditTextList.get(mWriteAllCounterValue).getText().toString().trim());
                } else {
                    edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mWriteAllCounterValue).getOffset() + "");
                }
                // edtValueFloat = Float.parseFloat(mEditTextList.get(pp).getText().toString().trim());
                //  edtValue = Integer.parseInt(mEditTextList.get(pp).getText().toString().trim());
                //   if (edtValueFloat >= mSettingParameterResponse.get(pp).getPMin() && edtValueFloat <= mSettingParameterResponse.get(pp).getPMax())
                {
                    /*  setDeviceMode(mSettingModelResponse.get(position).getAddress());*/
                    //changeButtonVisibility(true, 1.0f, holder);
                    counterValue = 0;
                    char[] datar = new char[4];
                    // int a=Float.floatToIntBits((float) edtValue);
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
                    String mMobADR = mSettingParameterResponse.get(mWriteAllCounterValue).getMobBTAddress();
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
                        //  String modeBusCommand1 = "0103"+mSettingModelResponse.get(position).getMobBTAddress()+""+"crc";
                        new BluetoothCommunicationForDynamicParameterWriteAll().execute(modeBusCommand, modeBusCommand, "OK");

                    } else {
                        Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                    }
                    //String modeBusCommand = "0106"+Integer.toHexString(mSettingModelResponse.get(position).getMobBTAddress())+Integer.toHexString(datar[0])+Integer.toHexString(datar[1])+Integer.toHexString(datar[2])+Integer.toHexString(datar[3])+Integer.toHexString(reciverbyte1)+Integer.toHexString(reciverbyte2);//write
                    // String modeBusCommand = "0106"+mSettingModelResponse.get(i).getMobBTAddress()+Integer.toHexString(datar[0])+Integer.toHexString(datar[1])+Integer.toHexString(datar[2])+Integer.toHexString(datar[3])+Integer.toHexString(mCRCFinalValue);//write
                    //   String modeBusCommand = "0106"+mSettingModelResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                }
                      /*  else {
                            Toast.makeText(mContext, "Please enter value between min=" + mSettingModelResponse.get(pp).getPMin() + " Max=" + mSettingModelResponse.get(pp).getPMax(), Toast.LENGTH_SHORT).show();
                        }*/
                }
                else
                {
                    baseRequest.hideLoader();
                    System.out.println("222");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("111");
            }
            /////////end of ifff
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterRead extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;
        int bytesRead = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            baseRequest.showLoader();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (!btSocket.isConnected()) {
                        btSocket.connect();//start connection
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(WebURL.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();

                    if (!btSocket.isConnected()) {
                        btSocket.connect();//start connection
                    }
                }
                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(100);
                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                   int[] bytesReaded = new int[4];

                    for (int i = 0; i < 6; i++) {
                        int mCharOne11 = iStream.read();
                    }
                    //int[] mTotalTime;
                    int mTotalTime;
                    int jjj = 0;
                    // mTotalTime = new int[1];
                    for (int i = 0; i < 1; i++) {
                        try {
                            //bytesRead = iStream.read();
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
                            // int mCharThree = iStream.read();
                            //   int mCharFour = iStream.read();
//                            bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;/* mTotalTime[jjj] = bytesReaded[i];
                            mTotalTime[jjj] |= bytesReaded[i + 1] << 8;
                            mTotalTime[jjj] |= bytesReaded[i + 2] << 16;
                            mTotalTime[jjj] |= bytesReaded[i + 3] << 24;*/
                            System.out.println("mTotalTime==>>vvv4  " + jjj + " " + mTotalTime);
                            //   mBTParameterList.add(Float.intBitsToFloat(mTotalTime[jjj])+"");
                            //arraylist.get(jjj).setValue(Float.intBitsToFloat(mTotalTime[jjj])+"");
                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);
                            mActivity.runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    //changeButtonVisibility(true, 1.0f, mEditTextList.get(mGlobalPosition));
                                    try {
                                        mSettingParameterResponse.get(mGlobalPosition).setPValue((int) mTotalTimeFloatData);
                                        // addDynamicViewPro(mSettingModelResponse);
                                        //  Float fgfg = mTotalTimeFloatData/mSettingModelResponse.get(mGlobalPosition).getDivisible();
                                        Float fgfg;
                                        if ((mSettingParameterResponse.get(mGlobalPosition).getOffset() != 0) || (mSettingParameterResponse.get(mGlobalPosition).getOffset() != 0.0)) {
                                            fgfg = mTotalTimeFloatData ;
                                          //  fgfg = mTotalTimeFloatData * mSettingParameterResponse.get(mGlobalPosition).getOffset();
                                        } else {
                                            fgfg = mTotalTimeFloatData;
                                        }
                                        mEditTextList.get(mGlobalPosition).setText("" + fgfg);
                                        System.out.println("mGlobalPosition==>>" + mGlobalPosition + "\nmTotalTimeFloatData==>>" + mTotalTimeFloatData);
                                        changeButtonVisibility(true, 1.0f, mTextViewSetIDtList.get(mGlobalPosition));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            jjj++;

                            for (int ii = 0; ii < 4; ii++) {
                                int mCharOne11 = iStream.read();
                            }
                        } catch (IOException e) {
                            baseRequest.hideLoader();
                            e.printStackTrace();
                        }
                    }

                    while (iStream.available() > 0) {
                        int mCharOne11 = iStream.read();
                    }

                }
            } catch (Exception e) {
                baseRequest.hideLoader();
                e.printStackTrace();
                return false;
            }

            baseRequest.hideLoader();
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            baseRequest.hideLoader();
            super.onPostExecute(result);

        }
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

    private void changeButtonVisibility(boolean state, float alphaRate, TextView text) {
        text.setEnabled(state);
        text.setAlpha(alphaRate);
    }

/*    public void setTimer(final String modeBusCommand) {

        timer = new Timer();
        //Set the schedule function and rate
        timer.scheduleAtFixedRate(
                new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(
                                new Runnable() {

                                    @Override
                                    public void run() {
                                        baseRequest.showLoader();
                                        new BluetoothCommunicationForDynamicParameterReadAll().execute(modeBusCommand, modeBusCommand, "OK");
                                    }
                                });
                    }
                }
                ,
                0,

                25000);     //1000 = 10  second
    }*/


    private void callGetPlantListCheckAPI() {
        baseRequest.showLoader();
        if (AllPopupUtil.isOnline(mContext)) {

            baseRequest.setBaseRequestListner(new RequestReciever() {
                @Override
                public void onSuccess(int APINumber, String Json, Object obj) {
                    //  JSONArray arr = (JSONArray) obj;
                    try {
                      /*  Gson gson = new Gson();
                        //////////////add model class here
                        SettingParameterModel mSettingParameterModel = gson.fromJson(Json, SettingParameterModel.class);
                        getPlantListStatusCheck(mSettingParameterModel);*/

                        JSONObject jo = new JSONObject(Json);

                        String mStatus = jo.getString("status");
                        final String mMessage = jo.getString("message");
                        String jo11 = jo.getString("response");
                        System.out.println("jo11==>>"+jo11);
                        if (mStatus.equalsIgnoreCase("true")) {

                            if(mSettingParameterResponse.size()>0)
                                mSettingParameterResponse.clear();

                            JSONArray ja = new JSONArray(jo11);
                            // JSONObject jo = ja.getJSONObject(0);

                            System.out.println("ja==>>"+ja.get(0));


                            for (int i = 0; i < ja.length(); i++) {

                                JSONObject join = ja.getJSONObject(i);
                                SettingParameterResponse mmComplainAllResponse = new SettingParameterResponse();

                                mmComplainAllResponse.setPmId(join.getString("pmId"));
                                mmComplainAllResponse.setParametersName(join.getString("parametersName"));
                                mmComplainAllResponse.setModbusaddress(join.getString("modbusaddress"));
                                mmComplainAllResponse.setMobBTAddress(join.getString("mobBTAddress"));
                                mmComplainAllResponse.setFactor(Integer.parseInt(join.getString("factor")));
                                mmComplainAllResponse.setPValue(Integer.parseInt(join.getString("pValue")));
                                //  mmComplainAllResponse.setM(join.getString("materialCode"));
                                mmComplainAllResponse.setUnit(join.getString("unit"));
                                mmComplainAllResponse.setOffset(Integer.parseInt(join.getString("offset")));



                                mSettingParameterResponse.add(mmComplainAllResponse);

                                mDatabaseHelper.insertSettingPerameterList(mContext,mmComplainAllResponse, mMaterialCode);

                            }

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                                 //   addDynamicViewProNew(mSettingParameterResponse);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        addDynamicViewProNew(mSettingParameterResponse);
                                    }

                                    baseRequest.hideLoader();
                                }


                            });

                        }
                        else
                        {
                            Toast.makeText(mContext, mMessage, Toast.LENGTH_LONG).show();
                            baseRequest.hideLoader();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int APINumber, String errorCode, String message) {
                    baseRequest.hideLoader();
                    // new HomeFragment.Worker().execute();
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNetworkFailure(int APINumber, String message) {
                    baseRequest.hideLoader();
                    Toast.makeText(mContext, "Please check internet connection!", Toast.LENGTH_LONG).show();
                }
            });

           /* JsonObject jsonObject = new JsonObject();
            try {
                ////Put input parameter here
                // jsonObject.addProperty("id", mUserID);
                //  jsonObject.addProperty("plantid", "1");
            //    jsonObject.addProperty("id", "9500000854");///Material code
                    jsonObject.addProperty("id", mMaterialCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //baseRequest.callAPIPost(1, jsonObject, Constant.GET_ALL_NOTIFICATION_LIST_API);/////
            //  baseRequest.callAPIPost(1, jsonObject, NewSolarVFD.GET_PLANT_LIST_CHECK);/////
              //baseRequest.callAPIPostIMEI(1, jsonObject, NewSolarVFD.MOTOR_PERSMETER_LIST);/////
            System.out.println("jsonObject==>>"+jsonObject);
            baseRequest.callAPIPost(1, jsonObject, WebURL.MOTOR_PERSMETER_LIST);/////*/

            Map<String, String> wordsByKey = new HashMap<>();

            wordsByKey.put("id", mMaterialCode);



            //baseRequest.callAPIPost(1, jsonObject, Constant.GET_ALL_NOTIFICATION_LIST_API);/////
            //  baseRequest.callAPIPost(1, jsonObject, NewSolarVFD.GET_PLANT_LIST_CHECK);/////
            //baseRequest.callAPIPostIMEI(1, jsonObject, NewSolarVFD.MOTOR_PERSMETER_LIST);/////
            System.out.println("jsonObject==>>"+wordsByKey);
           // baseRequest.callAPIGET(1, wordsByKey, WebURL.MOTOR_PERSMETER_LIST);/////
            baseRequest.callAPIGETIMEI(1, wordsByKey, WebURL.MOTOR_PERSMETER_LIST);/////

        } else {
            baseRequest.hideLoader();
        }
    }





    private void getPlantListStatusCheck(SettingParameterModel mSettingParameterModel) {

        if (mSettingParameterModel.getStatus()) {

          /*  if(mGetPlantResponse != null && mGetPlantResponse.size() > 0)
                mGetPlantResponse.clear();*/

            mSettingParameterResponse = mSettingParameterModel.getResponse();//setDataAdapter();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addDynamicViewProNew(mSettingParameterResponse);
            }

            baseRequest.hideLoader();

        } else {
            Toast.makeText(mContext, mSettingParameterModel.getMessage(), Toast.LENGTH_LONG).show();

            baseRequest.hideLoader();
        }
    }


}
