package debugapp;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.JsonObject;
import com.shaktipumplimited.retrofit.ApiClient;
import com.shaktipumplimited.retrofit.ApiInterface;
import com.shaktipumplimited.retrofit.BaseRequest;
import com.shaktipumplimited.retrofit.RequestReciever;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import activity.GPSTracker;
import bean.BTResonseData;
import bean.ProfileUpdateModel;
import ch.acra.acra.BuildConfig;
import de.hdodenhof.circleimageview.CircleImageView;
import debugapp.Bean.SimDetailsInfoResponse;
import debugapp.GlobalValue.Constant;
import debugapp.GlobalValue.CustomHttpClient;
import debugapp.GlobalValue.NewSolarVFD;
import debugapp.GlobalValue.UtilMethod;
import debugapp.localDB.DatabaseHelperTeacher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import utility.CustomUtility;
import webservice.WebURL;

public class BlueToothDebugNewActivity extends AppCompatActivity {
    private static boolean success = false;
    private static Workbook wb = null;
    private static CellStyle cs = null;
    private static Sheet sheet1 = null;
    private static Row row;
    private static Cell c = null;
    int latLenght;
    int longLenght;
    BluetoothSocket btSocket;
    BluetoothAdapter myBluetooth;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    int vkp = 0;
    RelativeLayout rlvBT_7_ID, rlvBT_7_ID_save, rlvBT_8_ID, rlvBT_9_ID, rlvBT_8_ID_SimUpdated;
    RelativeLayout rlvBT_S1_ID, rlvBT_S2_ID;
    ProgressDialog progressDialog;
    LinearLayout lvlMainTextContainerID;
    EditText edtPutCommandID;
    String AllCommomSTRContainer = "";
    /////////////////day
    /////////////this is for months
    int[] mTotalTime;

    int pp = 1;

    String RMS_ORG_D_F = "";

    String mCheckExtraction = "No";
    String AllTextSTR = "";
    int mIntCheckDeviceType = 0;
    int mIntCheckDeviceTypeFirst = 0;

    int jk = 0;

    RelativeLayout rlvBackViewID;
    int kkkkkk;
    String kkkkkk1;
    int clientid = 0;
    String ssssss;
    String mvFault;
    String mvInvTemp;
    float fvFrequency = 0;
    float fvRMSVoltage = 0;
    float fvOutputCurrent = 0;
    float fvLPM = 0;
    float fvPVVoltage = 0;
    float fvPVCurrent = 0;
    float fvInvTemp = 0;
    String mvRPM;
    String mInstallerMOB = "";
    String mInstallerName = "";
    String RMS_SERVER_DOWN = "";
    String RMS_DEBUG_EXTRN = "";
    int checkFirstTimeOlineStstus = 0;
    String DEVICE_NO, SIGNL_STREN, INVOICE_NO_B, NET_REG, SER_CONNECT, CAB_CONNECT, LATITUDE, LANGITUDE, MOBILE, IMEI, DONGAL_ID = "", SIM_SR_NO = "", KUNNR, SIM = "", RMS_STATUS = "", RMS_LAST_ONLINE_DATE = "", RMS_CURRENT_ONLINE_STATUS = "";
    //private String []  AllCommandArray ={"AT+CPIN?","AT+GSN","AT+CIMI","AT+QINISTAT","AT+CSQ","AT+CREG?","AT+CGREG?","AT+CGDCONT?","AT+QICSGP?"};
    List<BTResonseData> mBTResonseDataList;
    String mSimStatus;
    String mSimStatusActive;
    int mSimSValue = 0;
    int mCheckSignelValue = 0;
    int mCheckNetworkValue = 0;
    int mCheckServerConnectivityValue = 0;
    int mCheckCableOKValue = 0;
    String inst_latitude_double,
            inst_longitude_double;
    int mPostionFinal = 0;
    int kk = 0;
    int mvDay = 0;
    int mvMonth = 0;
    int mvYear = 0;
    int mvHournew = 0;
    int mvMinutenew = 0;
    String mvFaultnew = "";
    float fvTotalEnergy = 0;
    float fvTotalFlow = 0;
    float fvTotalTime = 0;
    int mmCount = 0;
    String mvHour;
    String mvMinute;
    String mvNo_of_Start;
    String filePath;
    String versionName;
    String mAppName = "KUSUM";
    String project_no = "";
    File file;
    TextView txtLatID, txtLongID;
    List<SimDetailsInfoResponse> mSimDetailsInfoResponse;
    TelephonyManager telephonyManager;
    private BaseRequest baseRequest;
    private DatabaseHelperTeacher mDatabaseHelperTeacher;
    private InputStream iStream = null;
    private UUID mMyUDID;
    private Dialog dialog;
    private ScrollView scrlViewID;
    private Context mContext;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();

        }
    };
    private String mModelType, mBtNameHead, mDeviceType, mBtMacAddressHead;
    private String MUserId = "null";
    private final String MDeviceId = "null";
    private final String MLoginType = "null";
    // private ImageView imgSendTextID;
    private RelativeLayout rlvSendButtonID;
    private ImageView imgBTShareFILEID;
    private ImageView imgBTSyncFILEID;
    private ImageView imgBTUploadFILEID;
    private final String allCammand = "AT+CPIN?";
    //private String []  AllCommandArray ={"AT+CPIN?\r\n","AT+GSN\r\n","AT+CIMI\r\n","AT+QINISTAT\r\n","AT+CSQ\r\n","AT+CREG?\r\n","AT+CGREG?\r\n","AT+CGDCONT?\r\n","AT+QICSGP?\r\n"};
    private final String[] AllCommandArray = {"AT+GSN\r\n", "AT+CIMI\r\n", "AT+QINISTAT\r\n", "AT+CSQ\r\n", "AT+CREG?\r\n", "AT+CGREG?\r\n", "AT+CGDCONT?\r\n", "AT+QICSGP?\r\n"};
    private boolean flag;
    private int mLengthCount;
    private String headerLenghtDAy = "";
    private  String headerLenghtMonth = "";
    private final String headerLenghtDayDongle = "";
    private final String headerLenghtMonthDongle = "";
    private final String headerLenghtFalt = "";
    private final boolean mBoolflag1 = false;
    private int mCheckCLICKDayORMonth = 0;
    private List<String> mMonthHeaderList;
    private boolean mBoolflag = false;
    private final boolean mBoolflagCheck = false;
    private RelativeLayout rlvLoadingViewID;
    private TextView txtHeadingLabelID;
    private String MEmpType = "null";
    private String ControllerSerialNumber;
    private int mCheckButtonclick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_new_layout);
        mContext = this;
        getDateTime();
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        initView();
    }

    private void initView() {
        mDatabaseHelperTeacher = new DatabaseHelperTeacher(mContext);
        RMS_DEBUG_EXTRN = "ONLINE FROM SERVER";
        mCheckSignelValue = 0;
        mCheckNetworkValue = 0;
        mCheckServerConnectivityValue = 0;
        mCheckCableOKValue = 0;

        INVOICE_NO_B = Constant.BILL_NUMBER_UNIC;

        Constant.Bluetooth_Activity_Navigation1 = 1;

        versionName = BuildConfig.VERSION_NAME;
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        project_no = CustomUtility.getSharedPreferences(mContext, "projectid");
        baseRequest = new BaseRequest(mContext);
        dialog = new Dialog(mContext);
        mBtNameHead = getIntent().getStringExtra("BtNameHead");
        mBtMacAddressHead = getIntent().getStringExtra("BtMacAddressHead");
        progressDialog = new ProgressDialog(mContext);
        mBTResonseDataList = new ArrayList<>();
        mMonthHeaderList = new ArrayList<>();
        mSimDetailsInfoResponse = new ArrayList<>();
        if (getIntent().getExtras() != null) {
            ControllerSerialNumber = getIntent().getStringExtra(Constant.ControllerSerialNumber);
        }

        try {
            MUserId = CustomUtility.getSharedPreferences(mContext, "userid");
            MEmpType = "Vend";// UtilMethod.setSharedPreference(mContext,"userType",checkUSERId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        txtLongID = findViewById(R.id.txtLongID);
        txtLatID = findViewById(R.id.txtLatID);


        rlvLoadingViewID = (RelativeLayout) findViewById(R.id.rlvLoadingViewID);
        txtHeadingLabelID = (TextView) findViewById(R.id.txtHeadingLabelID);
        scrlViewID = (ScrollView) findViewById(R.id.scrlViewID);
        rlvSendButtonID = (RelativeLayout) findViewById(R.id.rlvSendButtonID);
        imgBTShareFILEID = (ImageView) findViewById(R.id.imgBTShareFILEID);
        imgBTSyncFILEID = (ImageView) findViewById(R.id.imgBTSyncFILEID);
        imgBTUploadFILEID = (ImageView) findViewById(R.id.imgBTUploadFILEID);

        rlvBackViewID = (RelativeLayout) findViewById(R.id.rlvBackViewID);
        rlvBT_S1_ID = (RelativeLayout) findViewById(R.id.rlvBT_S1_ID);
        rlvBT_S2_ID = (RelativeLayout) findViewById(R.id.rlvBT_S2_ID);
        rlvBT_7_ID = (RelativeLayout) findViewById(R.id.rlvBT_7_ID);
        rlvBT_7_ID_save = (RelativeLayout) findViewById(R.id.rlvBT_7_ID_save);
        rlvBT_8_ID_SimUpdated = (RelativeLayout) findViewById(R.id.rlvBT_8_ID_SimUpdated);
        rlvBT_8_ID = (RelativeLayout) findViewById(R.id.rlvBT_8_ID);
        rlvBT_9_ID = (RelativeLayout) findViewById(R.id.rlvBT_9_ID);

        lvlMainTextContainerID = (LinearLayout) findViewById(R.id.lvlMainTextContainerID);
        edtPutCommandID = (EditText) findViewById(R.id.edtPutCommandID);
        mIntCheckDeviceType = 0;


        if (WebURL.APP_VERSION_CODE.equalsIgnoreCase("0")) {
            WebURL.APP_VERSION_CODE = "5.3";
        }

        changeButtonVisibilityRLV(true, 0.5f, rlvBT_S1_ID);
        changeButtonVisibilityRLV(true, 0.5f, rlvBT_S2_ID);
        changeButtonVisibilityRLV(false, 0.5f, rlvBT_7_ID_save);
        setClickEventListner();
       getGpsLocation();

    }

    private void setClickEventListner() {

        rlvBT_S1_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntCheckDeviceType = 0;
                mIntCheckDeviceTypeFirst = 1;
                changeButtonVisibilityRLV(false, 1.0f, rlvBT_S1_ID);
                changeButtonVisibilityRLV(true, 0.5f, rlvBT_S2_ID);

            }
        });

        rlvBT_S2_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntCheckDeviceType = 1;
                mIntCheckDeviceTypeFirst = 2;
                //finish();
               /* changeButtonVisibilityRLV(true, 0.5f, rlvBT_S2_ID);
                changeButtonVisibilityRLV(true, 1.0f, rlvBT_S1_ID);*/

                changeButtonVisibilityRLV(true, 0.5f, rlvBT_S1_ID);
                changeButtonVisibilityRLV(false, 1.0f, rlvBT_S2_ID);

            }
        });

        imgBTUploadFILEID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                SaveImage(AllCommomSTRContainer);
                baseRequest.showLoader();
                callInsertAndUpdateDebugDataAPI(AllCommomSTRContainer);
            }
        });

        imgBTShareFILEID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                SaveImage(AllCommomSTRContainer);
                shareDATA(AllCommomSTRContainer);
            }
        });

        imgBTSyncFILEID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBTResonseDataList.size() > 0)
                    mBTResonseDataList.clear();
                mBTResonseDataList = mDatabaseHelperTeacher.getDeviceInfoDATABT();


                if (mBTResonseDataList.size() > 0) {

                    DEVICE_NO = mBTResonseDataList.get(vkp).getDEVICENO();

                    SIGNL_STREN = mBTResonseDataList.get(vkp).getSIGNLSTREN();
                    String[] mStrArrySignal = SIGNL_STREN.split("###");
                    SIGNL_STREN = mStrArrySignal[0];
                    INVOICE_NO_B = mStrArrySignal[1];

                    SIM = mBTResonseDataList.get(vkp).getSIM();
                    String[] mStrArrySim = SIM.split("###");
                    SIM = mStrArrySim[0];
                    SIM_SR_NO = mStrArrySim[1];

                    NET_REG = mBTResonseDataList.get(vkp).getNETREG();
                    SER_CONNECT = mBTResonseDataList.get(vkp).getSERCONNECT();
                    CAB_CONNECT = mBTResonseDataList.get(vkp).getCABCONNECT();
                    LATITUDE = mBTResonseDataList.get(vkp).getLATITUDE();
                    LANGITUDE = mBTResonseDataList.get(vkp).getLANGITUDE();
                    MOBILE = mBTResonseDataList.get(vkp).getMOBILE();
                    IMEI = mBTResonseDataList.get(vkp).getIMEI();
                    DONGAL_ID = mBTResonseDataList.get(vkp).getDONGALID();
                    RMS_STATUS = mBTResonseDataList.get(vkp).getRMS_STATUS();
                    RMS_CURRENT_ONLINE_STATUS = mBTResonseDataList.get(vkp).getRMS_CURRENT_ONLINE_STATUS();
                    RMS_LAST_ONLINE_DATE = mBTResonseDataList.get(vkp).getRMS_LAST_ONLINE_DATE();

                    mInstallerMOB = CustomUtility.getSharedPreferences(mContext, "InstallerMOB");
                    mInstallerName = CustomUtility.getSharedPreferences(mContext, "InstallerName");
                    RMS_DEBUG_EXTRN = "ONLINE FROM DEBUG";
                    RMS_SERVER_DOWN = "Working Fine";
                    progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");


                    new SyncDebugDataFromLocal().execute();
                } else {
                    Toast.makeText(mContext, "Local database is  empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rlvBackViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    mInstallerMOB = CustomUtility.getSharedPreferences(mContext, "InstallerMOB");
                    mInstallerName = CustomUtility.getSharedPreferences(mContext, "InstallerName");

                    if (UtilMethod.isOnline(mContext)) {

                        if (mSimDetailsInfoResponse.size() > 0)
                            mSimDetailsInfoResponse.clear();

                        mSimDetailsInfoResponse = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);

                        WebURL.CHECK_FINAL_ALL_OK = 1;
                        WebURL.BT_DEBUG_CHECK = 1;
                        Constant.DBUG_PER_OFLINE = "";//PER_OFLINE

                        new SyncInstallationData1().execute();

                    } else {
                        long iiii = mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO, SIGNL_STREN + "###" + Constant.BILL_NUMBER_UNIC, SIM + "###" + SIM_SR_NO, NET_REG, SER_CONNECT, CAB_CONNECT, LATITUDE, LANGITUDE, MOBILE, IMEI, DONGAL_ID, MUserId, RMS_STATUS, RMS_CURRENT_ONLINE_STATUS, RMS_LAST_ONLINE_DATE, mInstallerName, mInstallerMOB, RMS_DEBUG_EXTRN, RMS_SERVER_DOWN, RMS_ORG_D_F, true);

                        System.out.println("iiii=inserted=>>" + iiii);
                        Toast.makeText(mContext, "Data save in loacl Data base", Toast.LENGTH_SHORT).show();

                        //  finish();

                    }


                    WebURL.BT_DEVICE_NAME = "";
                    WebURL.BT_DEVICE_MAC_ADDRESS = "";

                    Constant.BT_DEVICE_NAME = "";
                    Constant.BT_DEVICE_MAC_ADDRESS = "";
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        rlvBT_7_ID_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  showPopupSAVEData();

                mInstallerMOB = CustomUtility.getSharedPreferences(mContext, "InstallerMOB");
                mInstallerName = CustomUtility.getSharedPreferences(mContext, "InstallerName");

                // sendDataToServer();

                if (!mInstallerName.equalsIgnoreCase("") && !mInstallerName.equalsIgnoreCase("null") && !mInstallerMOB.equalsIgnoreCase("") && !mInstallerMOB.equalsIgnoreCase("null")) {
                    sendDataToServer();
                } else {
                    InstallerInfoUpdatePopup();
                }
            }
        });

        rlvBT_7_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lvlMainTextContainerID.addView(getTextViewTT(pp, ":DEBUG M66#"));

                AllCommomSTRContainer = AllCommomSTRContainer + "\n :DEBUG M66#";

                if (mIntCheckDeviceType == 0) {
                    new BluetoothCommunicationForDebugM66().execute(":DEBUG M66#", ":DEBUG M66#", "START");
                } else if (mIntCheckDeviceType == 2) {
                    new BluetoothCommunicationForDebugM66CommonCode().execute(":DEBUG M66#", ":DEBUG M66#", "START");
                } else {
                    new BluetoothCommunicationForDebugM66ShimhaTwo().execute(":DEBUG M66#", ":DEBUG M66#", "START");

                }
            }
        });

        rlvBT_8_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckButtonclick = 1;


                if (UtilMethod.isOnline(mContext)) {
                    // checkRMSAPIStatus();

                    new SyncRMSCHECKDATAAPI().execute();
                } else {
                    Toast.makeText(mContext, "Please check internet connections.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rlvBT_9_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kk = 0;
                mmCount = 0;
                mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//////////////this is fixed for blue tooth deviceee data
                mBoolflag = false;
                mPostionFinal = 0;
                mCheckCLICKDayORMonth = 0;


                RMS_DEBUG_EXTRN = "ONLINE FROM DEBUG";
                checkFirstTimeOlineStstus = 1;
                if (mMonthHeaderList.size() > 0)
                    mMonthHeaderList.clear();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new BluetoothCommunicationGetMonthParameter().execute(":MLENGTH#", ":MDATA#", "START");

                            }
                        }, 2 * 100);
                    }
                });


            }
        });

        rlvSendButtonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pp++;
                if (!edtPutCommandID.getText().toString().isEmpty()) {
                    //  getTextViewTT(pp, edtPutCommandID.getText().toString());
                    lvlMainTextContainerID.addView(getTextViewTT(pp, edtPutCommandID.getText().toString()));
                    //AllTextSTR = AllTextSTR +"\n"+edtPutCommandID.getText().toString();

                    AllCommomSTRContainer = AllCommomSTRContainer + "\n" + edtPutCommandID.getText().toString();
                    new BluetoothCommunicationForDebugStartType().execute(edtPutCommandID.getText().toString() + "\r\n", edtPutCommandID.getText().toString(), "Start");
                } else {
                    Toast.makeText(mContext, "Please write the cammand!", Toast.LENGTH_SHORT).show();
                }
                if (!edtPutCommandID.getText().toString().isEmpty()) {
                    File file = new File(mContext.getFilesDir(), "text");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    try {
                        File gpxfile = new File(file, "Vikas_testing_text");
                        FileWriter writer = new FileWriter(gpxfile);
                        writer.append(edtPutCommandID.getText().toString());
                        writer.flush();
                        writer.close();
                        // Toast.makeText(mContext, "Saved your text", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void sendDataToServer() {


        if (UtilMethod.isOnline(mContext)) {

            try {
                if (mSimDetailsInfoResponse.size() > 0)
                    mSimDetailsInfoResponse.clear();


                mSimDetailsInfoResponse = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);


                if (SER_CONNECT.equalsIgnoreCase("Connected")) {
                    if (RMS_STATUS.equalsIgnoreCase("YES")) {
                        if (!DEVICE_NO.equalsIgnoreCase("") && !NET_REG.equalsIgnoreCase("") && !LATITUDE.equalsIgnoreCase("") && !LANGITUDE.equalsIgnoreCase("")) {
                            WebURL.CHECK_FINAL_ALL_OK = 1;
                            WebURL.BT_DEBUG_CHECK = 1;
                            Constant.DBUG_PER_OFLINE = "";//PER_OFLINE

                            new SyncInstallationData1().execute();
                        } else {
                            WebURL.BT_DEBUG_CHECK = 0;
                            Toast.makeText(mContext, "Debug data not properly please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!DEVICE_NO.equalsIgnoreCase("") && !NET_REG.equalsIgnoreCase("") && !LATITUDE.equalsIgnoreCase("") && !LANGITUDE.equalsIgnoreCase("")) {
                            WebURL.BT_DEBUG_CHECK = 1;
                            Constant.DBUG_PER_OFLINE = "X";//PER_OFLINE
                            if (checkFirstTimeOlineStstus != 0) {
                                new SyncInstallationData1().execute();
                            } else {
                                Toast.makeText(mContext, "Please debug first and try again.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            WebURL.BT_DEBUG_CHECK = 0;
                            Toast.makeText(mContext, "Debug data not properly please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!DEVICE_NO.equalsIgnoreCase("") && !SER_CONNECT.equalsIgnoreCase("") && !NET_REG.equalsIgnoreCase("") && !LATITUDE.equalsIgnoreCase("") && !LANGITUDE.equalsIgnoreCase("")) {
                        WebURL.BT_DEBUG_CHECK = 1;
                        Constant.DBUG_PER_OFLINE = "X";//PER_OFLINE

                        if (checkFirstTimeOlineStstus != 0) {
                            new SyncInstallationData1().execute();
                        } else {
                            Toast.makeText(mContext, "Please debug first and try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        WebURL.BT_DEBUG_CHECK = 0;
                        Toast.makeText(mContext, "Debug data not properly please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            long iiii = mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO, SIGNL_STREN + "###" + Constant.BILL_NUMBER_UNIC, SIM + "###" + SIM_SR_NO, NET_REG, SER_CONNECT, CAB_CONNECT, LATITUDE, LANGITUDE, MOBILE, IMEI, DONGAL_ID, MUserId, RMS_STATUS, RMS_CURRENT_ONLINE_STATUS, RMS_LAST_ONLINE_DATE, mInstallerName, mInstallerMOB, RMS_DEBUG_EXTRN, RMS_SERVER_DOWN, RMS_ORG_D_F, true);

            System.out.println("iiii=inserted=>>" + iiii);
            Toast.makeText(mContext, "Data save in loacl Data base", Toast.LENGTH_SHORT).show();

            finish();

        }
    }

    private void SaveImage(String filename) {

        File file = new File(mContext.getExternalFilesDir(null), "DEBUG_" + mBtNameHead + ".txt");
        // File file = new File(mContext.getExternalFilesDir(null), "Month_" + mBtNameHead + ".xlsx");
        FileOutputStream os = null;
        System.out.println("vikas--4==>4");
        //baseRequest.hideLoader();
        try {
            os = new FileOutputStream(file);
            byte[] b = filename.getBytes();//converting string into byte array
            os.write(b);
            os.flush();
            //   wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            //success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                os = new FileOutputStream(file);
                //   wb.write(os);

                byte[] b = filename.getBytes();//converting string into byte array
                os.write(b);
                os.flush();
                //wb.write(os);
                if (null != os)
                    os.close();
            } catch (Exception ex) {
                System.out.println("vikas--5==>5");
                // baseRequest.hideLoader();
                ex.printStackTrace();
            }
        }

        try {

            File myDir = new File(getCacheDir(), "folder");
            myDir.mkdir();

            String rootPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/MyFolder/";
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdirs();
            }
            File f = new File(rootPath + "mttext.txt");
            if (f.exists()) {
                f.delete();
            }
            f.mkdirs();
            f.createNewFile();

            FileOutputStream out = new FileOutputStream(f);
            out.write(Integer.parseInt(filename));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Shakti_MOBILE/Debug_Data");
        myDir.mkdirs();

        String fname = "RMS_DEBUG.txt";
        File file1 = new File(myDir, fname);
        if (file1.exists()) file1.delete();
        try {
            file1.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(file1);
            //  finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(out);
            myOutWriter.append(filename);
            myOutWriter.close();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private TextView getTextViewTT(int id, String title) {
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 20);
        tv.setLayoutParams(params);
        tv.setId(id);
        // tv.setText(title.toUpperCase());
        tv.setText(title);
        tv.setTextColor(getResources().getColor(R.color.white));

        tv.setTextSize((int) getResources().getDimension(R.dimen._6ssp));
        tv.setBackgroundColor(getResources().getColor(R.color.black));
        tv.setGravity(Gravity.START);


        return tv;
    }

    private TextView getTextViewTTppSingle(int id, String title) {
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(5, 0, 5, 20);
        tv.setLayoutParams(params);
        tv.setId(id);
        // tv.setText(title.toUpperCase());
        tv.setText(title);
        tv.setTextColor(getResources().getColor(R.color.green));

        tv.setTextSize((int) getResources().getDimension(R.dimen._9ssp));
        // tv.setBackgroundColor(getResources().getColor(R.color.black));
        tv.setGravity(Gravity.START);

        // tv.setWidth(200);
        // tv.setOnClickListener(this);
        return tv;
    }
    //float sizeInPixels = getResources().getDimension(R.dimen.my_value);

    private TextView getTextViewTTpp(int id, String title) {
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(5, -5, 5, 10);
        tv.setLayoutParams(params);
        tv.setId(id);
        // tv.setText(title.toUpperCase());
        tv.setText(title);
        tv.setTextColor(getResources().getColor(R.color.green));

        tv.setTextSize((int) getResources().getDimension(R.dimen._9ssp));
        tv.setBackgroundColor(getResources().getColor(R.color.black));
        tv.setGravity(Gravity.START);

        // tv.setWidth(200);
        // tv.setOnClickListener(this);
        return tv;
    }

    private void shareDATA(String filename) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        //  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(filename));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, filename);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    private void callInsertAndUpdateDebugDataAPI(String AllCommomSTRContainerIN) {
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int APINumber, String Json, Object obj) {
                //  JSONArray arr = (JSONArray) obj;
                try {

                    JSONObject jo = new JSONObject(Json);

                    String mStatus = jo.getString("status");

                    final String mMessage = jo.getString("message");
                    String jo11 = jo.getString("response");
                    System.out.println("jo11==>>" + jo11);
                    if (mStatus.equalsIgnoreCase("true")) {

                        Toast.makeText(mContext, mMessage, Toast.LENGTH_LONG).show();
                        baseRequest.hideLoader();

                    } else {
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

        JsonObject jsonObject = new JsonObject();
        try {
            ////Put input parameter here
            jsonObject.addProperty("DeviceNo", mBtNameHead);
            jsonObject.addProperty("Content", AllCommomSTRContainerIN);
            //  jsonObject.addProperty("fcmToken", NewSolarVFD.FCM_TOKEN);
            //  jsonObject.addProperty("imei", NewSolarVFD.IMEI_NUMBER);
            System.out.println("RMSVIKAS   Content=" + AllCommomSTRContainerIN + ", DeviceNo=" + mBtNameHead);
        } catch (Exception e) {
            baseRequest.hideLoader();
            e.printStackTrace();
        }
        //  baseRequest.callAPIPost(1, jsonObject, Constant.GET_ALL_NOTIFICATION_LIST_API);/////
        baseRequest.callAPIPostDebugApp(1, jsonObject, NewSolarVFD.INSERT_DEBUG_DATA_API);/////
        //baseRequest.callAPIPut(1, jsonObject, NewSolarVFD.ORG_RESET_FORGOTPASS);/////
    }

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {
            String lat111 = "" + gps.getLatitude();
            String long111 = "" + gps.getLongitude();

            System.out.println("lat111==>>" + lat111 + "   , " + long111);

            txtLatID.setText(lat111);
            txtLongID.setText(long111);

            lat111 = txtLatID.getText().toString().trim();
            long111 = txtLongID.getText().toString().trim();

            System.out.println("lat111==>>" + lat111 + "   , " + long111);

            inst_latitude_double = "" + lat111;
            inst_longitude_double = "" + long111;

            System.out.println("lat111==>>1" + inst_latitude_double + "   , " + inst_longitude_double);

            latLenght = inst_latitude_double.length();
            longLenght = inst_longitude_double.length();

            if (inst_latitude_double.equalsIgnoreCase("0.0")) {
                Toast.makeText(mContext, "Lat Long not captured, Please try again", Toast.LENGTH_SHORT).show();

            } else {
                baseRequest.showLoader();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //textView.setText("Your new text");
                        new BluetoothCommunicationSET_LAT().execute(":LAT :0" + latLenght + "," + inst_latitude_double + "#", ":LAT :" + latLenght + "," + inst_latitude_double + "#", "Start");

                    }
                }, 10000);//800

            }
        } else {
            gps.showSettingsAlert();
        }
    }

    private void changeButtonVisibilityRLV(boolean state, float alphaRate, RelativeLayout txtDataExtractionID) {
        txtDataExtractionID.setEnabled(state);
        txtDataExtractionID.setAlpha(alphaRate);
        //  hideBTN();
    }


    @Override
    public void onBackPressed() {

        mInstallerMOB = CustomUtility.getSharedPreferences(mContext, "InstallerMOB");
        mInstallerName = CustomUtility.getSharedPreferences(mContext, "InstallerName");

        if (UtilMethod.isOnline(mContext)) {

            if (mSimDetailsInfoResponse.size() > 0)
                mSimDetailsInfoResponse.clear();

            mSimDetailsInfoResponse = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);

            WebURL.CHECK_FINAL_ALL_OK = 1;
            WebURL.BT_DEBUG_CHECK = 1;
            Constant.DBUG_PER_OFLINE = "";//PER_OFLINE

            new SyncInstallationData1().execute();
        } else {
            mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO, SIGNL_STREN + "###" + Constant.BILL_NUMBER_UNIC, SIM + "###" + SIM_SR_NO, NET_REG, SER_CONNECT, CAB_CONNECT, LATITUDE, LANGITUDE, MOBILE, IMEI, DONGAL_ID, MUserId, RMS_STATUS, RMS_CURRENT_ONLINE_STATUS, RMS_LAST_ONLINE_DATE, mInstallerName, mInstallerMOB, RMS_DEBUG_EXTRN, RMS_SERVER_DOWN, RMS_ORG_D_F, true);


            Toast.makeText(mContext, "Data save in loacl Data base", Toast.LENGTH_SHORT).show();

            //  finish();

        }

        if (flag) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void callCheckSimDataPackAPI(int mSignalStrength, int mNetworkConnect, int mServerConnect) {
        baseRequest.showLoader();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int APINumber, String Json, Object obj) {
                //  JSONArray arr = (JSONArray) obj;
                try {

                    JSONObject jo = new JSONObject(Json);
                    String mStatus = jo.getString("status");
                    final String mMessage = jo.getString("message");
                    String jo11 = jo.getString("response");

                    if (mStatus.equalsIgnoreCase("true")) {

                        JSONArray ja = new JSONArray(jo11);

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject join = ja.getJSONObject(i);

                            mSimStatus = join.getString("status_txt").trim();
                            mSimStatusActive = join.getString("status").trim();

                           /* if(mSimStatus.equalsIgnoreCase("Activate"))
                            {
                                mSimSValue = 1;
                            }
                            else if(mSimStatus.equalsIgnoreCase("Deactivate"))
                            {
                                mSimSValue = 2;
                            }else
                            {
                                mSimSValue = 3;
                            }*/
                            AllCommomSTRContainer = AllCommomSTRContainer + " :\nSim  Status :" + mSimStatus;
                            //AllCommomSTRContainer = AllCommomSTRContainer + " :\n " + AllTextSTR +"\n";
                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSim  Status : " + mSimStatus));

                            if (mSignalStrength == 1 && mNetworkConnect == 1 && mServerConnect == 1) {
                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack : Activate";
                                //AllCommomSTRContainer = AllCommomSTRContainer + " :\n " + AllTextSTR +"\n";
                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : Activate"));

                            } else if (mSignalStrength == 1 && mNetworkConnect == 1 && mServerConnect == 0) {
                                if (mSimStatusActive.equalsIgnoreCase("1")) {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack : Activate";
                                    //AllCommomSTRContainer = AllCommomSTRContainer + " :\n " + AllTextSTR +"\n";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : Activate"));
                                } else {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack : Not Activate";
                                    //AllCommomSTRContainer = AllCommomSTRContainer + " :\n " + AllTextSTR +"\n";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : Not Activate"));
                                }
                            } else {
                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack :" + mSimStatus;
                                //AllCommomSTRContainer = AllCommomSTRContainer + " :\n " + AllTextSTR +"\n";
                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : " + mSimStatus));

                                if (mSimStatusActive.equalsIgnoreCase("1")) {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack : Activate";
                                    //AllCommomSTRContainer = AllCommomSTRContainer + " :\n " + AllTextSTR +"\n";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : Activate"));
                                } else {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack : Not Activate";
                                    //AllCommomSTRContainer = AllCommomSTRContainer + " :\n " + AllTextSTR +"\n";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : Not Activate"));
                                }
                            }
                            // mLoginResponseList.add(mmLoginResponse);
                        }
                        //baseRequest.hideLoader();
                      /*  Message msg1 = new Message();
                        msg1.obj = mMessage;
                        mHandler.sendMessage(msg1);*/

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (UtilMethod.isOnline(mContext)) {
                                    // checkRMSAPIStatus();

                                    new SyncRMSCHECKDATAAPI().execute();
                                } else {
                                    Toast.makeText(mContext, "Please check internet connections.", Toast.LENGTH_SHORT).show();

                                }
                                // addDataMonth(mPostionFinal + 1, mvDay + "", mvMonth + "", mvYear + "", mvHour, mvMinute, mvNo_of_Start, fvFrequency, fvRMSVoltage, fvOutputCurrent, mvRPM, fvLPM, fvPVVoltage, fvPVCurrent, mvFault, fvInvTemp);
                            }
                        });

                    } else {
                        // baseRequest.hideLoader();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();

                                if (UtilMethod.isOnline(mContext)) {
                                    // checkRMSAPIStatus();
                                    new SyncRMSCHECKDATAAPI().execute();

                                } else {
                                    Toast.makeText(mContext, "Please check internet connections.", Toast.LENGTH_SHORT).show();
                                }
                                // addDataMonth(mPostionFinal + 1, mvDay + "", mvMonth + "", mvYear + "", mvHour, mvMinute, mvNo_of_Start, fvFrequency, fvRMSVoltage, fvOutputCurrent, mvRPM, fvLPM, fvPVVoltage, fvPVCurrent, mvFault, fvInvTemp);
                            }
                        });

                       /* Message msg1 = new Message();
                        msg1.obj = "Invalid username or password";
                        mHandler.sendMessage(msg1);*/
                    }

                    //  getDeviceSettingListResponse(mSettingModelView);
                } catch (Exception e) {
                    baseRequest.hideLoader();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int APINumber, String errorCode, String message) {
                baseRequest.hideLoader();
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (UtilMethod.isOnline(mContext)) {
                            // checkRMSAPIStatus();

                            new SyncRMSCHECKDATAAPI().execute();
                        } else {
                            Toast.makeText(mContext, "Please check internet connections.", Toast.LENGTH_SHORT).show();

                        }
                        // addDataMonth(mPostionFinal + 1, mvDay + "", mvMonth + "", mvYear + "", mvHour, mvMinute, mvNo_of_Start, fvFrequency, fvRMSVoltage, fvOutputCurrent, mvRPM, fvLPM, fvPVVoltage, fvPVCurrent, mvFault, fvInvTemp);
                    }
                });
            }

            @Override
            public void onNetworkFailure(int APINumber, String message) {
                baseRequest.hideLoader();
                Toast.makeText(mContext, "Please check internet connection!", Toast.LENGTH_LONG).show();
            }
        });


        Map<String, String> wordsByKey = new HashMap<>();

        //  wordsByKey.put("userid", inputName.getText().toString().trim());
        wordsByKey.put("device", DEVICE_NO);// DEVICE_NO = sssM[0];


        if (DEVICE_NO!=null&& !DEVICE_NO.isEmpty() &&!DEVICE_NO.equals(ControllerSerialNumber+"-0")) {
            ShowAlertResponse();
        }else {
            CustomUtility.ShowToast("Not able to read Device Serial Number", getApplicationContext());
        }
        baseRequest.callAPIGETDebugApp(1, wordsByKey, NewSolarVFD.SIM_STATUS_VK_PAGE);/////

    }

    private void ShowAlertResponse() {
        LayoutInflater inflater = (LayoutInflater) BlueToothDebugNewActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(BlueToothDebugNewActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if(!isFinishing()) {
            alertDialog.show();
        }
        CircleImageView user_img = layout.findViewById(R.id.user_img);
        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);

        user_img.setVisibility(View.GONE);

        title_txt.setText(getResources().getString(R.string.cant_Debug));

        OK_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

    }

    private String getDateTime() {
        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println("Date==>>" + dateFormat.format(date));

        String[] sspdDate = dateFormat.format(date).split(" ");
        String sDate = sspdDate[0];
        System.out.println("DateOnly==>>" + sDate);
        return sDate;
    }

    public void GetProfileUpdate_Task(String deviceno, String type, String len) {

        if (UtilMethod.isOnline(mContext)) {
            //  baseRequest.showLoader();
            // rlvLoadingViewID.setVisibility(View.GONE);
            ApiInterface apiService = ApiClient.getClientFileUpload().create(ApiInterface.class);
            RequestBody fbody;
            MultipartBody.Part body = null;
            Log.e("fileActualPath", "& " + filePath);
            if (!UtilMethod.isStringNullOrBlank(filePath)) {
                file = new File(filePath);
                // fbody = RequestBody.create(MediaType.parse("xls/*"), file);
                fbody = RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), file);
                body = MultipartBody.Part.createFormData("fname", file.getName(), fbody);
            }
            RequestBody deviceno1 = RequestBody.create(MediaType.parse("multipart/form-data"), deviceno);
            RequestBody type1 = RequestBody.create(MediaType.parse("multipart/form-data"), type);
            RequestBody headerLenght1 = RequestBody.create(MediaType.parse("multipart/form-data"), len);
            // RequestBody lenCount = RequestBody.create(MediaType.parse("multipart/form-data"), lenCount);
            //Call<ProfileUpdateModel> call = apiService.getProfileUpdateData(deviceno1, type1, body);
            Call<ProfileUpdateModel> call = apiService.getProfileUpdateDatanew(deviceno1, type1, headerLenght1, body);

            call.enqueue(new Callback<ProfileUpdateModel>() {
                @Override
                public void onResponse(Call<ProfileUpdateModel> call, retrofit2.Response<ProfileUpdateModel> response) {
                    try {
                        ProfileUpdateModel dashResponse = response.body();

                        //    System.out.println("dashResponse==>>"+dashResponse.getStatus());
                        //  System.out.println("dashResponse==>>"+dashResponse.getMessage());
                        Log.e("status", "** " + dashResponse);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (dashResponse.getStatus().equalsIgnoreCase("true")) {
                                    Toast.makeText(mContext, dashResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, dashResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                baseRequest.hideLoader();
                            }
                        });

                    } catch (Exception e) {
                        baseRequest.hideLoader();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProfileUpdateModel> call, @NonNull Throwable t) {
                    try {
                        rlvLoadingViewID.setVisibility(View.GONE);
                        Toast.makeText(mContext, "File upload faild.", Toast.LENGTH_SHORT).show();
                        baseRequest.hideLoader();
                    } catch (Exception e) {
                        rlvLoadingViewID.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            });
        } else {
            rlvLoadingViewID.setVisibility(View.GONE);
            baseRequest.hideLoader();
            Toast.makeText(mContext, "Please check internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void InstallerInfoUpdatePopup() {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.user_pr_infor);
        // dialog.setTitle("Title...");
        dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        TextView txtTitleID = (TextView) dialog.findViewById(R.id.txtTitleID);
        EditText edtMobileInstallerID = (EditText) dialog.findViewById(R.id.edtMobileInstallerID);
        EditText edtNameInstallerID = (EditText) dialog.findViewById(R.id.edtNameInstallerID);
        // text.setText("Android custom dialog example!");
      /*  ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_launcher);*/

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mMobileInstallerID = edtMobileInstallerID.getText().toString().trim();
                String mNameInstallerID = edtNameInstallerID.getText().toString().trim();

                if (mNameInstallerID.equalsIgnoreCase("")) {

                    Toast.makeText(mContext, "Please enter your name", Toast.LENGTH_SHORT).show();
                } else if (mMobileInstallerID.equalsIgnoreCase("")) {

                    Toast.makeText(mContext, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                } else {

                    mInstallerMOB = mMobileInstallerID;
                    mInstallerName = mNameInstallerID;

                    CustomUtility.setSharedPreference(mContext, "InstallerName", mInstallerName);
                    CustomUtility.setSharedPreference(mContext, "InstallerMOB", mInstallerMOB);

                    //  sendDataToServer();
                    //  long iiii = mDatabaseHelperTeacher.insertSimInfoData(controller,mSimNumberData,Constant.BILL_NUMBER_UNIC,Constant.BILL_NUMBER_UNIC,MUserId, true);
                    Toast.makeText(mContext, "User Information insterted successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDEVICE_INFO extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            baseRequest.showLoader();
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (btSocket.isConnected()) {
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                        if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Boolean TODO = null;
                            return TODO;
                        }
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                        myBluetooth.cancelDiscovery();
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
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
                        while (true) {
                            try {
                                kkkkkk1 = (char) iStream.read() + "";
                                AllTextSTR = AllTextSTR + kkkkkk1;
                                // AllTextSTR = AllTextSTR.replaceAll("[\r]", "");
                                // AllTextSTR = AllTextSTR.replaceAll("[\n]", "");
                                if (iStream.available() == 0) {
                                    break;
                                }
                            } catch (IOException e) {
                                baseRequest.hideLoader();
                                e.printStackTrace();
                                break;
                            }
                            //ssssss = ssssss + () kkkkkk1;
                        }

                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///addHeadersMonths();
                            try {
                                String[] sssM = AllTextSTR.split(",");

                                for (int i = 0; i < sssM.length; i++) {

                                    pp++;

                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n" + sssM[i];
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n" + sssM[i]));

                                }

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            baseRequest.hideLoader();
                            AllTextSTR = "";
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();
                // btSocket = null;
                //   Toast.makeText(mActivity, "BT Connection lost..", Toast.LENGTH_SHORT).show();
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
            pp++;
            baseRequest.hideLoader();

            scrlViewID.fullScroll(View.FOCUS_DOWN);

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDebugCheckDevice extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

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
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                        if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Boolean TODO = null;
                            return TODO;
                        }
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                        myBluetooth.cancelDiscovery();
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                }

                if (!btSocket.isConnected())
                    btSocket.connect();//start connection
                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);
                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(800);
                        iStream = btSocket.getInputStream();
                        while (true) {
                            try {
                                kkkkkk1 = (char) iStream.read() + "";
                                AllTextSTR = AllTextSTR + kkkkkk1;
                                // AllTextSTR = AllTextSTR.replaceAll("[\r]", "");
                                // AllTextSTR = AllTextSTR.replaceAll("[\n]", "");
                                if (iStream.available() == 0) {
                                    break;
                                }
                            } catch (IOException e) {
                                baseRequest.hideLoader();
                                e.printStackTrace();
                                break;
                            }
                            //ssssss = ssssss + () kkkkkk1;
                        }

                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///addHeadersMonths();
                            try {

                                String[] sssM = AllTextSTR.split(",");

                                if (sssM.length == 12) {
                                    mIntCheckDeviceType = 2;
                                } else {
                                    for (int i = 0; i < sssM.length; i++) {

                                        pp++;

                                        if (i == 0) {

                                        } else if (i == 1) {

                                        } else if (i == 2) {
                                            String[] ssSubIn1 = sssM[2].split("-");

                                            if (ssSubIn1[0].equalsIgnoreCase("SIM")) {
                                                mIntCheckDeviceType = 1;
                                            } else {
                                                mIntCheckDeviceType = 0;
                                            }
                                        }

                                    }
                                }


                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            AllTextSTR = "";
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();
                return false;
            }

            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///
                    // bpbp

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lvlMainTextContainerID.addView(getTextViewTT(pp, ":DEBUG M66#"));

                            AllCommomSTRContainer = AllCommomSTRContainer + "\n :DEBUG M66#";


                            if (mIntCheckDeviceType == 0) {
                                new BluetoothCommunicationForDebugM66().execute(":DEBUG M66#", ":DEBUG M66#", "START");
                            } else if (mIntCheckDeviceType == 2) {
                                new BluetoothCommunicationForDebugM66CommonCode().execute(":DEBUG M66#", ":DEBUG M66#", "START");
                            } else {
                                new BluetoothCommunicationForDebugM66ShimhaTwo().execute(":DEBUG M66#", ":DEBUG M66#", "START");

                            }

                        }
                    }, 2 * 200);
                }
            });


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDebugM66 extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

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
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                        if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Boolean TODO = null;
                            return TODO;
                        }
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                        myBluetooth.cancelDiscovery();
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
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
                        while (true) {
                            try {
                                kkkkkk1 = (char) iStream.read() + "";
                                AllTextSTR = AllTextSTR + kkkkkk1;
                                if (iStream.available() == 0) {
                                    break;
                                }
                            } catch (IOException e) {
                                baseRequest.hideLoader();
                                e.printStackTrace();
                                break;
                            }
                        }


                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///addHeadersMonths();
                            try {
                                RMS_ORG_D_F = AllTextSTR;

                                String[] sssM = AllTextSTR.split(",");

                                for (int i = 0; i < sssM.length; i++) {

                                    pp++;

                                    if (sssM.length == 8) {
                                        if (i == 0) {
                                            DEVICE_NO = sssM[0];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Device No :" + sssM[0];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDevice No : " + sssM[0]));
                                        } else if (i == 1) {
                                            String[] ssSubIn1 = sssM[1].split("-");

                                            mCheckSignelValue = 1;
                                            SIGNL_STREN = ssSubIn1[1];

                                        } else if (i == 2) {
                                            String[] ssSubIn1 = sssM[2].split("-");

                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                mCheckNetworkValue = 0;
                                                NET_REG = "Not registered";
                                                SIGNL_STREN = "0";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + SIGNL_STREN;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + SIGNL_STREN));
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:  Not registered";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration:  Not registered"));
                                            } else {
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + SIGNL_STREN;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + SIGNL_STREN));
                                                mCheckNetworkValue = 1;
                                                NET_REG = "Registered";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:   Registered";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration:  Registered"));
                                            }


                                        } else if (i == 3) {
                                            String[] ssSubIn1 = sssM[3].split("-");

                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                mCheckServerConnectivityValue = 0;
                                                SER_CONNECT = "Not connected";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Not connected";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity:  Not connected"));
                                            } else {
                                                mCheckServerConnectivityValue = 1;
                                                SER_CONNECT = "Connected";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Connected";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity: Connected."));
                                            }
                                        } else if (i == 4) {
                                            String[] ssSubIn1 = sssM[4].split("-");

                                            if (mCheckServerConnectivityValue == 1) {
                                                if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                    mCheckCableOKValue = 0;
                                                    CAB_CONNECT = "Not working";
                                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not working";
                                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not working"));
                                                } else {
                                                    mCheckCableOKValue = 1;
                                                    CAB_CONNECT = "Ok";
                                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity: Ok";
                                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity: Ok"));
                                                }
                                            } else {
                                                mCheckCableOKValue = 0;
                                                CAB_CONNECT = "Not applicable";

                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not applicable";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not applicable"));
                                            }


                                        } else if (i == 5) {
                                            String[] ssSubIn2 = sssM[5].split("-");


                                            if (!ssSubIn2[1].equalsIgnoreCase("")) {
                                                LATITUDE = ssSubIn2[1];

                                                if (LATITUDE.equalsIgnoreCase("1.00000000") || LATITUDE.equalsIgnoreCase("1") || LATITUDE.equalsIgnoreCase("0") || LATITUDE.equalsIgnoreCase("0.00000000")) {
                                                    LATITUDE = inst_latitude_double;

                                                }


                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: " + LATITUDE;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: " + LATITUDE));
                                            } else {

                                                LATITUDE = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: Not Available";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: Not Available"));
                                            }

                                        } else if (i == 6) {
                                            String[] ssSubIn3 = sssM[6].split("-");

                                            if (!ssSubIn3[1].equalsIgnoreCase("")) {

                                                LANGITUDE = ssSubIn3[1];
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: " + ssSubIn3[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: " + ssSubIn3[1]));
                                            } else {

                                                LANGITUDE = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: Not Available ";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: Not Available"));
                                            }
                                        } else if (i == 7) {
                                            String[] ssSubIn1 = sssM[7].split("-");

                                            if (!ssSubIn1[1].equalsIgnoreCase("")) {
                                                MOBILE = ssSubIn1[1];
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Mobile Number:" + ssSubIn1[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nMobile Number:" + ssSubIn1[1]));
                                            } else {
                                                MOBILE = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Mobile Number: Not Available";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nMobile Number: Not Available"));
                                            }
                                        }
                                    } else {
                                        if (i == 0) {
                                            DEVICE_NO = sssM[0];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Device No :" + sssM[0];
                                            //AllCommomSTRContainer = AllCommomSTRContainer + " :\n " + AllTextSTR +"\n";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDevice No : " + sssM[0]));
                                        } else if (i == 1) {
                                            String[] ssSubIn1 = sssM[1].split("-");

                                            mCheckSignelValue = 1;
                                            SIGNL_STREN = ssSubIn1[1];


                                        } else if (i == 2) {
                                            String[] ssSubIn1 = sssM[2].split("-");

                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                mCheckNetworkValue = 0;
                                                NET_REG = "Not registered";
                                                //  SIGNL_STREN ="0";
                                                SIGNL_STREN = "0";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + SIGNL_STREN;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + SIGNL_STREN));
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:  Not registered";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration:  Not registered"));
                                            } else {
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + SIGNL_STREN;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + SIGNL_STREN));
                                                mCheckNetworkValue = 1;
                                                NET_REG = "Registered";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:   Registered";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration:  Registered"));
                                            }
                                        } else if (i == 3) {
                                            String[] ssSubIn1 = sssM[3].split("-");

                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                mCheckServerConnectivityValue = 0;
                                                SER_CONNECT = "Not connected";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Not connected";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity:  Not connected"));
                                            } else {
                                                mCheckServerConnectivityValue = 1;
                                                SER_CONNECT = "Connected";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Connected";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity: Connected."));
                                            }
                                        } else if (i == 4) {
                                            String[] ssSubIn1 = sssM[4].split("-");

                                            if (mCheckServerConnectivityValue == 1) {
                                                if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                    mCheckCableOKValue = 0;
                                                    CAB_CONNECT = "Not working";
                                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not working";
                                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not working"));
                                                } else {
                                                    mCheckCableOKValue = 1;
                                                    CAB_CONNECT = "Ok";
                                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity: Ok";
                                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity: Ok"));
                                                }
                                            } else {
                                                mCheckCableOKValue = 0;
                                                CAB_CONNECT = "Not applicable";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not applicable";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not applicable"));
                                            }


                                        } else if (i == 5) {
                                            String[] ssSubIn2 = sssM[5].split("-");

                                            if (!ssSubIn2[1].equalsIgnoreCase("")) {
                                                LATITUDE = ssSubIn2[1];


                                                if (LATITUDE.equalsIgnoreCase("1.00000000") || LATITUDE.equalsIgnoreCase("1") || LATITUDE.equalsIgnoreCase("0") || LATITUDE.equalsIgnoreCase("0.00000000")) {
                                                    LATITUDE = inst_latitude_double;

                                                }


                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: " + LATITUDE;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: " + LATITUDE));
                                            } else {

                                                LATITUDE = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: Not Available";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: Not Available"));
                                            }


                                        } else if (i == 6) {
                                            String[] ssSubIn3 = sssM[6].split("-");

                                            if (!ssSubIn3[1].equalsIgnoreCase("")) {

                                                LANGITUDE = ssSubIn3[1];
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: " + ssSubIn3[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: " + ssSubIn3[1]));
                                            } else {

                                                LANGITUDE = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: Not Available ";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: Not Available"));
                                            }
                                            MOBILE = "Not Available";
                                        }

                                    }

                                }

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }


                            AllTextSTR = "";
                         }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();

                return false;
            }

            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            pp++;
            // baseRequest.hideLoader();
            AllCommomSTRContainer = AllCommomSTRContainer + "\nIMEI";
            // AllTextSTR = AllTextSTR +"\n"+edtPutCommandID.getText().toString();
            new BluetoothCommunicationForGET_IMEI_66_1().execute(":GET IMEI#", ":GET IMEI#", "Start");
            // AllCommomSTRContainer = AllCommomSTRContainer + " \n IMEI";

            scrlViewID.fullScroll(View.FOCUS_DOWN);

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForGET_IMEI_66_1 extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            //baseRequest.showLoader();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (btSocket.isConnected()) {
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                        myBluetooth.cancelDiscovery();
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available

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
                        while (true) {
                            try {
                                kkkkkk1 = (char) iStream.read() + "";
                                AllTextSTR = AllTextSTR + kkkkkk1;
                                // AllTextSTR = AllTextSTR.replaceAll("[\r]", "");
                                // AllTextSTR = AllTextSTR.replaceAll("[\n]", "");
                                if (iStream.available() == 0) {
                                    break;
                                }
                            } catch (IOException e) {
                                baseRequest.hideLoader();
                                e.printStackTrace();
                                break;
                            }
                            //ssssss = ssssss + () kkkkkk1;
                        }

                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///addHeadersMonths();
                            try {

                                String[] stst = AllTextSTR.split(":");
                                // AllCommomSTRContainer = AllCommomSTRContainer + " :\n "+AllTextSTR;

                                IMEI = stst[1];
                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n " + stst[1];
                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n" + AllTextSTR));

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            //  AllCommomSTRContainer = AllCommomSTRContainer + " : " + AllTextSTR +"\n";
                            //AllCommomSTRContainer = AllCommomSTRContainer + "\n" + AllTextSTR;
                            //  lvlMainTextContainerID.addView(getTextViewTTpp(pp, "" + AllTextSTR));
                            //  baseRequest.hideLoader();
                            AllTextSTR = "";
                            // addDataMonth(mPostionFinal + 1, mvDay + "", mvMonth + "", mvYear + "", mvHour, mvMinute, mvNo_of_Start, fvFrequency, fvRMSVoltage, fvOutputCurrent, mvRPM, fvLPM, fvPVVoltage, fvPVCurrent, mvFault, fvInvTemp);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();
                // btSocket = null;
                //   Toast.makeText(mActivity, "BT Connection lost..", Toast.LENGTH_SHORT).show();
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
            pp++;
            //  callInsertAllDebugDataAPI();
            // baseRequest.hideLoader();
            flag = true;

            scrlViewID.fullScroll(View.FOCUS_DOWN);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///addHeadersMonths();
                    try {

                        callCheckSimDataPackAPI(mCheckSignelValue, mCheckNetworkValue, mCheckServerConnectivityValue);

                        changeButtonVisibilityRLV(true, 1.0f, rlvBT_7_ID_save);

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }


                }
            });

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDebugM66CommonCode extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

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
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                        if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Boolean TODO = null;
                            return TODO;
                        }
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                        myBluetooth.cancelDiscovery();
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Boolean TODO = null;

                        return TODO;
                    }
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
                        while (true) {
                            try {
                                kkkkkk1 = (char) iStream.read() + "";
                                AllTextSTR = AllTextSTR + kkkkkk1;
                                // AllTextSTR = AllTextSTR.replaceAll("[\r]", "");
                                // AllTextSTR = AllTextSTR.replaceAll("[\n]", "");
                                if (iStream.available() == 0) {
                                    break;
                                }
                            } catch (IOException e) {
                                baseRequest.hideLoader();
                                e.printStackTrace();
                                break;
                            }
                            //ssssss = ssssss + () kkkkkk1;
                        }

                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///addHeadersMonths();


                            try {

                                RMS_ORG_D_F = AllTextSTR;


                                String[] sssM = AllTextSTR.split(",");
                                System.out.println("Shimha2==>>" + sssM.length);
                                System.out.println("Shimha2==>>" + AllTextSTR);


                                for (int i = 0; i < sssM.length; i++) {

                                    pp++;


                                    if (i == 0) {

                                        DEVICE_NO = sssM[0];
                                       AllCommomSTRContainer = AllCommomSTRContainer + " :\n Device No :" + sssM[0];
                                        lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDevice No : " + sssM[0]));
                                    } else if (i == 1) {
                                        String[] ssSubIn1 = sssM[1].split("-");
                                        mCheckSignelValue = 1;
                                        SIGNL_STREN = ssSubIn1[1];


                                    } else if (i == 2) {
                                        String[] ssSubIn1 = sssM[2].split("-");

                                        if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                            SIM = "Not inserted";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n SIM :  Not inserted";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSIM :  Not inserted"));
                                        } else {
                                            SIM = "Inserted";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n SIM :  Inserted";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSIM :  Inserted"));
                                        }
                                    } else if (i == 3) {
                                        String[] ssSubIn1 = sssM[3].split("-");

                                        if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                            mCheckNetworkValue = 0;
                                            NET_REG = "Not registered";
                                            SIGNL_STREN = "0";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + SIGNL_STREN;
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + SIGNL_STREN));
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:  Not registered";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration:  Not registered"));
                                        } else {
                                            mCheckNetworkValue = 1;
                                            NET_REG = "Registered";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + SIGNL_STREN;
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + SIGNL_STREN));
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:  Registered";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration: Registered"));
                                        }

                                    } else if (i == 4) {
                                        String[] ssSubIn1 = sssM[4].split("-");

                                        if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                            mCheckServerConnectivityValue = 0;
                                            SER_CONNECT = "Not connected";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Not connected";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity:  Not connected"));
                                        } else {
                                            mCheckServerConnectivityValue = 1;
                                            SER_CONNECT = "Connected";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Connected";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity: Connected."));
                                        }
                                    } else if (i == 5) {
                                        String[] ssSubIn1 = sssM[5].split("-");

                                        if (mCheckServerConnectivityValue == 1) {
                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                mCheckCableOKValue = 0;
                                                CAB_CONNECT = "Not working";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not working";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not working"));
                                            } else {
                                                mCheckCableOKValue = 1;
                                                CAB_CONNECT = "Ok";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity: Ok";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity: Ok"));
                                            }
                                        } else {
                                            mCheckCableOKValue = 0;
                                            CAB_CONNECT = "Not applicable";

                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not applicable";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not applicable"));
                                        }


                                    } else if (i == 6) {

                                        String[] ssSubIn2 = sssM[6].split("-");


                                        if (!ssSubIn2[1].equalsIgnoreCase("")) {
                                            System.out.println("LATITUDE==>>" + ssSubIn2[1]);
                                            LATITUDE = ssSubIn2[1];

                                            if (LATITUDE.equalsIgnoreCase("1.00000000") || LATITUDE.equalsIgnoreCase("1") || LATITUDE.equalsIgnoreCase("0") || LATITUDE.equalsIgnoreCase("0.00000000")) {
                                                LATITUDE = inst_latitude_double;

                                            }


                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: " + LATITUDE;
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: " + LATITUDE));
                                        } else {
                                            LATITUDE = "Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: Not Available";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: Not Available"));
                                        }


                                    } else if (i == 7) {
                                        String[] ssSubIn3 = sssM[7].split("-");

                                        if (!ssSubIn3[1].equalsIgnoreCase("")) {
                                            LANGITUDE = ssSubIn3[1];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: " + ssSubIn3[1];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: " + ssSubIn3[1]));
                                        } else {
                                            LANGITUDE = "Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: Not Available ";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: Not Available"));
                                        }
                                    } else if (i == 8) {
                                        String[] ssSubIn1 = sssM[8].split("-");

                                        if (!ssSubIn1[1].equalsIgnoreCase("")) {
                                            MOBILE = ssSubIn1[1];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Mobile Number:" + ssSubIn1[1];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nMobile Number:" + ssSubIn1[1]));
                                        } else {
                                            MOBILE = "Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Mobile Number: Not Available";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nMobile Number: Not Available"));
                                        }
                                    } else if (i == 9) {
                                        String[] ssSubIn1 = sssM[9].split("-");

                                        if (!ssSubIn1[1].equalsIgnoreCase("")) {
                                            IMEI = ssSubIn1[1];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n IMEI:" + ssSubIn1[1];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nIMEI:" + ssSubIn1[1]));
                                        } else {
                                            IMEI = "Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n IMEI: Not Available";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nIMEI: Not Available"));
                                        }
                                    } else if (i == 10) {
                                        String[] ssSubIn1 = sssM[10].split("-");

                                        if (!ssSubIn1[1].equalsIgnoreCase("")) {
                                            DONGAL_ID = ssSubIn1[1] + "-" + ssSubIn1[2] + "-" + ssSubIn1[3] + "-" + ssSubIn1[4] + "-" + ssSubIn1[5] + "-" + ssSubIn1[6] + "-" + ssSubIn1[7];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Dongle Id:" + ssSubIn1[1];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDongle Id:" + ssSubIn1[1]));
                                        } else {
                                            DONGAL_ID = "Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Dongle Id: Not Available";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDongle Id: Not Available"));
                                        }
                                    } else if (i == 11) {
                                        String[] ssSubIn1 = sssM[11].split("-");

                                        if (!ssSubIn1[1].equalsIgnoreCase("")) {
                                            SIM_SR_NO = ssSubIn1[1];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Sim Serial Number:" + ssSubIn1[1];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSim Serial Number:" + ssSubIn1[1]));
                                        } else {
                                            SIM_SR_NO = "Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Sim Serial Number: Not Available";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSim Serial Number: Not Available"));
                                        }
                                    }


                                }

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            //  AllCommomSTRContainer = AllCommomSTRContainer + " : " + AllTextSTR +"\n";
                            //AllCommomSTRContainer = AllCommomSTRContainer + "\n" + AllTextSTR;
                            //  lvlMainTextContainerID.addView(getTextViewTTpp(pp, "" + AllTextSTR));
                            //  baseRequest.hideLoader();
                            AllTextSTR = "";
                            // addDataMonth(mPostionFinal + 1, mvDay + "", mvMonth + "", mvYear + "", mvHour, mvMinute, mvNo_of_Start, fvFrequency, fvRMSVoltage, fvOutputCurrent, mvRPM, fvLPM, fvPVVoltage, fvPVCurrent, mvFault, fvInvTemp);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();
                // btSocket = null;
                //   Toast.makeText(mActivity, "BT Connection lost..", Toast.LENGTH_SHORT).show();
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
            pp++;
            // callInsertAllDebugDataAPI();
            //   baseRequest.hideLoader();

            scrlViewID.fullScroll(View.FOCUS_DOWN);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///addHeadersMonths();
                    try {
                        WebURL.SERVER_CONNECTIVITY_OK = mCheckServerConnectivityValue;

                        callCheckSimDataPackAPI(mCheckSignelValue, mCheckNetworkValue, mCheckServerConnectivityValue);

                        changeButtonVisibilityRLV(true, 1.0f, rlvBT_7_ID_save);

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }


                }
            });

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDebugM66ShimhaTwo extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

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
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                        if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Boolean TODO = null;

                            return TODO;
                        }
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                        myBluetooth.cancelDiscovery();
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Boolean TODO = null;

                        return TODO;
                    }
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
                        while (true) {
                            try {
                                kkkkkk1 = (char) iStream.read() + "";
                                AllTextSTR = AllTextSTR + kkkkkk1;
                                // AllTextSTR = AllTextSTR.replaceAll("[\r]", "");
                                // AllTextSTR = AllTextSTR.replaceAll("[\n]", "");
                                if (iStream.available() == 0) {
                                    break;
                                }
                            } catch (IOException e) {
                                baseRequest.hideLoader();
                                e.printStackTrace();
                                break;
                            }
                            //ssssss = ssssss + () kkkkkk1;
                        }

                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///addHeadersMonths();
                           /* try {

                                String [] sssM = AllTextSTR.split(",");

                                for (int i = 0; i < sssM.length; i++) {

                                    pp++;

                                    if(i == 0)
                                    {
                                       // DEVICE_NO=sssM[0];
                                        DEVICE_NO = sssM[0];
                                        AllCommomSTRContainer = AllCommomSTRContainer + " :\n Device No :" + sssM[0] ;
                                        //AllCommomSTRContainer = AllCommomSTRContainer + " :\n " + AllTextSTR +"\n";
                                        lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDevice No : " + sssM[0]));
                                    }
                                    else  if(i == 1)
                                    {
                                        String [] ssSubIn1 = sssM[1].split("-");

                                        mCheckSignelValue = 1;
                                        SIGNL_STREN = ssSubIn1[1];
                                        AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + ssSubIn1[1];
                                        lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + ssSubIn1[1]));

                                     */
                           /*   if(Integer.parseInt(ssSubIn1[1]) > 7 && Integer.parseInt(ssSubIn1[1]) < 32)
                                        {
                                            mCheckSignelValue = 1;
                                            SIGNL_STREN = ssSubIn1[1];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + ssSubIn1[1];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + ssSubIn1[1]));
                                        }
                                        else
                                        {
                                            mCheckSignelValue = 0;
                                            SIGNL_STREN = "0";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength : 0";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : 0"));
                                        }*/
                           /*
                                    }
                                    else  if(i == 2)
                                    {
                                        String [] ssSubIn1 = sssM[2].split("-");

                                        if(Integer.parseInt(ssSubIn1[1]) == 0)
                                        {
                                            SIM = "Not inserted";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n SIM :  Not inserted";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSIM :  Not inserted"));
                                        }
                                        else
                                        {
                                            SIM = "Inserted";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n SIM :  Inserted";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSIM :  Inserted"));
                                        }
                                    } else  if(i == 3)
                                    {
                                        String [] ssSubIn1 = sssM[3].split("-");

                                        if(Integer.parseInt(ssSubIn1[1]) == 0)
                                        {
                                            mCheckNetworkValue =0;
                                            NET_REG = "Not registered";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:  Not registered";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration:  Not registered"));
                                        }
                                        else
                                        {
                                            mCheckNetworkValue = 1;
                                            NET_REG = "Registered";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:  Registered";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration: Registered"));
                                        }
                                    }
                                    else  if(i == 4)
                                    {
                                        String [] ssSubIn1 = sssM[4].split("-");

                                        if(Integer.parseInt(ssSubIn1[1]) == 0)
                                        {
                                            mCheckServerConnectivityValue =0;
                                            SER_CONNECT = "Not connected";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Not connected";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity:  Not connected"));
                                        }
                                        else
                                        {
                                            mCheckServerConnectivityValue =1;
                                            SER_CONNECT = "Connected";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Connected";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity: Connected."));
                                        }
                                    }
                                    else  if(i == 5)
                                    {
                                        String [] ssSubIn1 = sssM[5].split("-");

                                        if(Integer.parseInt(ssSubIn1[1]) == 0)
                                        {
                                            mCheckCableOKValue = 0;
                                            CAB_CONNECT = "Not working";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not working";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not working"));
                                        }
                                        else
                                        {
                                            mCheckCableOKValue = 1;
                                            CAB_CONNECT = "Ok";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity: Ok";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity: Ok"));
                                        }
                                    }
                                    else  if(i == 6)
                                    {
                                        String [] ssSubIn2 = sssM[6].split("-");

                                        if(!ssSubIn2[1].equalsIgnoreCase("")) {
                                            System.out.println("LATITUDE==>>"+ssSubIn2[1]);
                                            LATITUDE = ssSubIn2[1];

                                            if(LATITUDE.equalsIgnoreCase("1.00000000"))
                                            {
                                                --i;
                                                --pp;
                                            }
                                            else
                                            {
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: " + ssSubIn2[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: " + ssSubIn2[1]));
                                            }
                                          //  AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: " + ssSubIn2[1];
                                         //   lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: " + ssSubIn2[1]));
                                        }
                                        else {
                                            LATITUDE = "Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: Not Available";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: Not Available"));
                                        }
                                    }
                                    else  if(i == 7)
                                    {
                                        String [] ssSubIn3 = sssM[7].split("-");

                                        if(!ssSubIn3[1].equalsIgnoreCase("")) {
                                            LANGITUDE = ssSubIn3[1];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: " + ssSubIn3[1];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: " + ssSubIn3[1]));
                                        }
                                        else {
                                            LANGITUDE = "Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: Not Available ";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: Not Available"));
                                        }
                                    }
                                    else  if(i == 8)
                                    {
                                        String [] ssSubIn1 = sssM[8].split("-");

                                        if(!ssSubIn1[1].equalsIgnoreCase(""))
                                        {
                                            MOBILE = ssSubIn1[1];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Mobile Number:"+ssSubIn1[1];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nMobile Number:"+ssSubIn1[1]));
                                        }
                                        else
                                        {
                                            MOBILE = "Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Mobile Number: Not Available";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nMobile Number: Not Available"));
                                        }
                                    }
                                    else  if(i == 9)
                                    {
                                        String [] ssSubIn1 = sssM[9].split("-");

                                        if(!ssSubIn1[1].equalsIgnoreCase(""))
                                        {
                                            IMEI = ssSubIn1[1];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n IMEI:"+ssSubIn1[1];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nIMEI:"+ssSubIn1[1]));
                                        }
                                        else
                                        {
                                            IMEI = "Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n IMEI: Not Available";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nIMEI: Not Available"));
                                        }
                                    }
                                    else  if(i == 10)
                                    {
                                        String [] ssSubIn1 = sssM[10].split("-");

                                        if(!ssSubIn1[1].equalsIgnoreCase(""))
                                        {
                                            DONGAL_ID=ssSubIn1[1];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Dongle Id:"+ssSubIn1[1];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDongle Id:"+ssSubIn1[1]));
                                        }
                                        else
                                        {
                                            DONGAL_ID ="Not Available";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Dongle Id: Not Available";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDongle Id: Not Available"));
                                        }
                                    }

                                }

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }*/


                            try {

                                RMS_ORG_D_F = AllTextSTR;

                                String[] sssM1 = AllTextSTR.split(",");
                                //String AllTextSTR1;

                                if (sssM1.length <= 9) {
                                    AllTextSTR = AllTextSTR.replace("IMEI", ",IMEI");
                                }


                                String[] sssM = AllTextSTR.split(",");
                                System.out.println("Shimha2==>>" + sssM.length);
                                System.out.println("Shimha2==>>" + AllTextSTR);


                                for (int i = 0; i < sssM.length; i++) {

                                    pp++;

                                    if (sssM.length > 10) {
                                        if (i == 0) {
                                            DEVICE_NO = sssM[0];
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n Device No :" + sssM[0];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDevice No : " + sssM[0]));
                                        } else if (i == 1) {
                                            String[] ssSubIn1 = sssM[1].split("-");
                                            mCheckSignelValue = 1;
                                            SIGNL_STREN = ssSubIn1[1];

                                        } else if (i == 2) {
                                            String[] ssSubIn1 = sssM[2].split("-");

                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                SIM = "Not inserted";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n SIM :  Not inserted";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSIM :  Not inserted"));
                                            } else {
                                                SIM = "Inserted";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n SIM :  Inserted";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSIM :  Inserted"));
                                            }
                                        } else if (i == 3) {
                                            String[] ssSubIn1 = sssM[3].split("-");

                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                mCheckNetworkValue = 0;
                                                NET_REG = "Not registered";
                                                SIGNL_STREN = "0";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + SIGNL_STREN;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + SIGNL_STREN));
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:  Not registered";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration:  Not registered"));
                                            } else {
                                                mCheckNetworkValue = 1;
                                                NET_REG = "Registered";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + SIGNL_STREN;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + SIGNL_STREN));
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:  Registered";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration: Registered"));
                                            }

                                        } else if (i == 4) {
                                            String[] ssSubIn1 = sssM[4].split("-");

                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                mCheckServerConnectivityValue = 0;
                                                SER_CONNECT = "Not connected";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Not connected";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity:  Not connected"));
                                            } else {
                                                mCheckServerConnectivityValue = 1;
                                                SER_CONNECT = "Connected";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Connected";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity: Connected."));
                                            }
                                        } else if (i == 5) {
                                            String[] ssSubIn1 = sssM[5].split("-");

                                            if (mCheckServerConnectivityValue == 1) {
                                                if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                    mCheckCableOKValue = 0;
                                                    CAB_CONNECT = "Not working";
                                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not working";
                                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not working"));
                                                } else {
                                                    mCheckCableOKValue = 1;
                                                    CAB_CONNECT = "Ok";
                                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity: Ok";
                                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity: Ok"));
                                                }
                                            } else {
                                                mCheckCableOKValue = 0;
                                                CAB_CONNECT = "Not applicable";

                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not applicable";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not applicable"));
                                            }


                                        } else if (i == 6) {

                                            String[] ssSubIn2 = sssM[6].split("-");


                                            if (!ssSubIn2[1].equalsIgnoreCase("")) {
                                                System.out.println("LATITUDE==>>" + ssSubIn2[1]);
                                                LATITUDE = ssSubIn2[1];

                                                if (LATITUDE.equalsIgnoreCase("1.00000000") || LATITUDE.equalsIgnoreCase("1") || LATITUDE.equalsIgnoreCase("0") || LATITUDE.equalsIgnoreCase("0.00000000")) {
                                                    LATITUDE = inst_latitude_double;

                                                }


                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: " + LATITUDE;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: " + LATITUDE));
                                            } else {
                                                LATITUDE = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: Not Available";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: Not Available"));
                                            }


                                        } else if (i == 7) {
                                            String[] ssSubIn3 = sssM[7].split("-");

                                            if (!ssSubIn3[1].equalsIgnoreCase("")) {
                                                LANGITUDE = ssSubIn3[1];
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: " + ssSubIn3[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: " + ssSubIn3[1]));
                                            } else {
                                                LANGITUDE = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: Not Available ";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: Not Available"));
                                            }
                                        } else if (i == 8) {
                                            String[] ssSubIn1 = sssM[8].split("-");

                                            if (!ssSubIn1[1].equalsIgnoreCase("")) {
                                                MOBILE = ssSubIn1[1];
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Mobile Number:" + ssSubIn1[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nMobile Number:" + ssSubIn1[1]));
                                            } else {
                                                MOBILE = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Mobile Number: Not Available";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nMobile Number: Not Available"));
                                            }
                                        } else if (i == 9) {
                                            String[] ssSubIn1 = sssM[9].split("-");

                                            if (!ssSubIn1[1].equalsIgnoreCase("")) {
                                                IMEI = ssSubIn1[1];
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n IMEI:" + ssSubIn1[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nIMEI:" + ssSubIn1[1]));
                                            } else {
                                                IMEI = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n IMEI: Not Available";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nIMEI: Not Available"));
                                            }
                                        } else if (i == 10) {
                                            String[] ssSubIn1 = sssM[10].split("-");

                                            if (!ssSubIn1[1].equalsIgnoreCase("")) {
                                                DONGAL_ID = ssSubIn1[1];
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Dongle Id:" + ssSubIn1[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDongle Id:" + ssSubIn1[1]));
                                            } else {
                                                DONGAL_ID = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Dongle Id: Not Available";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDongle Id: Not Available"));
                                            }
                                        }
                                    } else /////// wrong from code
                                    {

                                        if (i == 0) {

                                            DEVICE_NO = sssM[0];
                                             AllCommomSTRContainer = AllCommomSTRContainer + " :\n Device No :" + sssM[0];
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDevice No : " + sssM[0]));
                                        } else if (i == 1) {
                                            String[] ssSubIn1 = sssM[1].split("-");

                                            mCheckSignelValue = 1;
                                            SIGNL_STREN = ssSubIn1[1];

                                        } else if (i == 2) {
                                            String[] ssSubIn1 = sssM[2].split("-");

                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                SIM = "Not inserted";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n SIM :  Not inserted";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSIM :  Not inserted"));
                                            } else {
                                                SIM = "Inserted";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n SIM :  Inserted";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSIM :  Inserted"));
                                            }
                                        } else if (i == 3) {
                                            String[] ssSubIn1 = sssM[3].split("-");


                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                mCheckNetworkValue = 0;
                                                NET_REG = "Not registered";
                                                SIGNL_STREN = "0";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + SIGNL_STREN;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + SIGNL_STREN));
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:  Not registered";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration:  Not registered"));
                                            } else {
                                                mCheckNetworkValue = 1;
                                                NET_REG = "Registered";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Signal Strength :" + SIGNL_STREN;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSignal Strength : " + SIGNL_STREN));
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Network Registration:  Registered";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nNetwork Registration: Registered"));
                                            }
                                        } else if (i == 4) {
                                            String[] ssSubIn1 = sssM[4].split("-");

                                            if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                mCheckServerConnectivityValue = 0;
                                                SER_CONNECT = "Not connected";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Not connected";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity:  Not connected"));
                                            } else {
                                                mCheckServerConnectivityValue = 1;
                                                SER_CONNECT = "Connected";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Server connectivity:  Connected";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nServer connectivity: Connected."));
                                            }
                                        } else if (i == 5) {
                                            String[] ssSubIn1 = sssM[5].split("-");


                                            if (mCheckServerConnectivityValue == 1) {
                                                if (Integer.parseInt(ssSubIn1[1]) == 0) {
                                                    mCheckCableOKValue = 0;
                                                    CAB_CONNECT = "Not working";
                                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not working";
                                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not working"));
                                                } else {
                                                    mCheckCableOKValue = 1;
                                                    CAB_CONNECT = "Ok";
                                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity: Ok";
                                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity: Ok"));
                                                }
                                            } else {
                                                mCheckCableOKValue = 0;
                                                CAB_CONNECT = "Not applicable";

                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n FRC cable connectivity:  Not applicable";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nFRC cable connectivity:  Not applicable"));
                                            }

                                        } else if (i == 6) {

                                            String[] ssSubIn2 = sssM[6].split("-");


                                            if (!ssSubIn2[1].equalsIgnoreCase("")) {
                                                System.out.println("LATITUDE==>>" + ssSubIn2[1]);
                                                LATITUDE = ssSubIn2[1];


                                                if (LATITUDE.equalsIgnoreCase("1.00000000") || LATITUDE.equalsIgnoreCase("1") || LATITUDE.equalsIgnoreCase("0") || LATITUDE.equalsIgnoreCase("0.00000000")) {
                                                    LATITUDE = inst_latitude_double;

                                                }


                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: " + LATITUDE;
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: " + LATITUDE));

                                            } else {
                                                LATITUDE = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Latitude: Not Available";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLatitude: Not Available"));
                                            }


                                        } else if (i == 7) {
                                            String[] ssSubIn3 = sssM[7].split("-");

                                            if (!ssSubIn3[1].equalsIgnoreCase("")) {
                                                LANGITUDE = ssSubIn3[1];
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: " + ssSubIn3[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: " + ssSubIn3[1]));
                                            } else {
                                                LANGITUDE = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Longitude: Not Available ";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nLongitude: Not Available"));
                                            }
                                        } else if (i == 8) {
                                            MOBILE = "Not Available";
                                            String[] ssSubIn1 = sssM[8].split("-");

                                            if (!ssSubIn1[1].equalsIgnoreCase("")) {
                                                IMEI = ssSubIn1[1];
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n IMEI:" + ssSubIn1[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nIMEI:" + ssSubIn1[1]));
                                            } else {
                                                IMEI = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n IMEI: Not Available";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nIMEI: Not Available"));
                                            }
                                        } else if (i == 9) {
                                            String[] ssSubIn1 = sssM[9].split("-");

                                            if (!ssSubIn1[1].equalsIgnoreCase("")) {
                                                DONGAL_ID = ssSubIn1[1];
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Dongle Id:" + ssSubIn1[1];
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDongle Id:" + ssSubIn1[1]));
                                            } else {
                                                DONGAL_ID = "Not Available";
                                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Dongle Id: Not Available";
                                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nDongle Id: Not Available"));
                                            }
                                        }

                                    }


                                }

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            //  AllCommomSTRContainer = AllCommomSTRContainer + " : " + AllTextSTR +"\n";
                            //AllCommomSTRContainer = AllCommomSTRContainer + "\n" + AllTextSTR;
                            //  lvlMainTextContainerID.addView(getTextViewTTpp(pp, "" + AllTextSTR));
                            //  baseRequest.hideLoader();
                            AllTextSTR = "";
                            // addDataMonth(mPostionFinal + 1, mvDay + "", mvMonth + "", mvYear + "", mvHour, mvMinute, mvNo_of_Start, fvFrequency, fvRMSVoltage, fvOutputCurrent, mvRPM, fvLPM, fvPVVoltage, fvPVCurrent, mvFault, fvInvTemp);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();
                // btSocket = null;
                //   Toast.makeText(mActivity, "BT Connection lost..", Toast.LENGTH_SHORT).show();
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
            pp++;
            // callInsertAllDebugDataAPI();
            //   baseRequest.hideLoader();

            scrlViewID.fullScroll(View.FOCUS_DOWN);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///addHeadersMonths();
                    try {
                        WebURL.SERVER_CONNECTIVITY_OK = mCheckServerConnectivityValue;

                        callCheckSimDataPackAPI(mCheckSignelValue, mCheckNetworkValue, mCheckServerConnectivityValue);

                        changeButtonVisibilityRLV(true, 1.0f, rlvBT_7_ID_save);

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }


                }
            });

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDebugStartType extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

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
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                        if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Boolean TODO = null;
                            return TODO;
                        }
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                        myBluetooth.cancelDiscovery();
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Boolean TODO = null;
                        return TODO;
                    }
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Boolean TODO = null;
                        return TODO;
                    }
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
                        while (true) {
                            try {
                                kkkkkk1 = (char) iStream.read() + "";
                                AllTextSTR = AllTextSTR + kkkkkk1;
                                AllTextSTR = AllTextSTR.replaceAll("[\\r]", "");
                                AllTextSTR = AllTextSTR.replaceAll("[\\n]", "");
                                if (iStream.available() == 0) {
                                    break;
                                }
                            } catch (IOException e) {
                                baseRequest.hideLoader();
                                e.printStackTrace();
                                break;
                            }
                            //ssssss = ssssss + () kkkkkk1;
                        }

                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///  addHeadersMonths();
                            // AllCommomSTRContainer = AllCommomSTRContainer + "\n" + AllTextSTR;
                            AllCommomSTRContainer = AllCommomSTRContainer + " : " + AllTextSTR + "\n";
                            lvlMainTextContainerID.addView(getTextViewTTppSingle(pp, "" + AllTextSTR));
                            AllTextSTR = "";
                            // addDataMonth(mPostionFinal + 1, mvDay + "", mvMonth + "", mvYear + "", mvHour, mvMinute, mvNo_of_Start, fvFrequency, fvRMSVoltage, fvOutputCurrent, mvRPM, fvLPM, fvPVVoltage, fvPVCurrent, mvFault, fvInvTemp);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();
                // btSocket = null;
                //   Toast.makeText(mActivity, "BT Connection lost..", Toast.LENGTH_SHORT).show();
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
            scrlViewID.fullScroll(View.FOCUS_DOWN);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationSET_LAT extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

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
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                        myBluetooth.cancelDiscovery();
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                }

                if (!btSocket.isConnected())
                    btSocket.connect();//start connection
                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);
                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(500);
                        iStream = btSocket.getInputStream();
                        while (true) {
                            try {
                                iStream.read();

                                if (iStream.available() == 0) {
                                    break;
                                }
                            } catch (IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext, "3", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                baseRequest.hideLoader();
                                e.printStackTrace();
                                break;
                            }
                            //ssssss = ssssss + () kkkkkk1;
                        }

                    } catch (InterruptedException e1) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                            }
                        });
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();

                return false;
            }
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            // Do this instead


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //textView.setText("Your new text");
                            new BluetoothCommunicationSET_Long().execute(":LONG :0" + longLenght + "," + inst_longitude_double + "#", ":LONG :" + longLenght + "," + inst_longitude_double + "#", "Start");

                        }
                    }, 2 * 400);
                }
            });
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationSET_Long extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            //   baseRequest.showLoader();
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (btSocket.isConnected()) {
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                        if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Boolean TODO = null;
                            return TODO;
                        }
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                        myBluetooth.cancelDiscovery();
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    //   BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(mBtMacAddressHead);//connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Boolean TODO = null;
                        return TODO;
                    }
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                }

                if (!btSocket.isConnected())
                    if (ActivityCompat.checkSelfPermission(BlueToothDebugNewActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Boolean TODO = null;
                        return TODO;
                    }
                btSocket.connect();//start connection
                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);
                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(500);
                        iStream = btSocket.getInputStream();
                        while (true) {
                            try {
                                iStream.read();

                                if (iStream.available() == 0) {
                                    break;
                                }
                            } catch (IOException e) {
                                baseRequest.hideLoader();
                                e.printStackTrace();
                                break;
                            }
                            //ssssss = ssssss + () kkkkkk1;
                        }

                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                  /*  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///  addHeadersMonths();
                            // AllCommomSTRContainer = AllCommomSTRContainer + "\n" + AllTextSTR;
                            AllCommomSTRContainer = AllCommomSTRContainer + " : " + AllTextSTR + "\n";
                            lvlMainTextContainerID.addView(getTextViewTTppSingle(pp, "" + AllTextSTR));
                            AllTextSTR = "";
                            // addDataMonth(mPostionFinal + 1, mvDay + "", mvMonth + "", mvYear + "", mvHour, mvMinute, mvNo_of_Start, fvFrequency, fvRMSVoltage, fvOutputCurrent, mvRPM, fvLPM, fvPVVoltage, fvPVCurrent, mvFault, fvInvTemp);
                        }
                    });*/
                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();
                // btSocket = null;
                //   Toast.makeText(mActivity, "BT Connection lost..", Toast.LENGTH_SHORT).show();
                // myBluetooth.disable();
                return false;
            }
            // baseRequest.hideLoader();
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            //   baseRequest.hideLoader();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///  addHeadersMonths();
                    // AllCommomSTRContainer = AllCommomSTRContainer + "\n" + AllTextSTR;
                    // Do this instead

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //textView.setText("Your new text");
                            new BluetoothCommunicationForDebugCheckDevice().execute(":DEBUG M66#", ":DEBUG M66#", "START");

                        }
                    }, 2 * 200);


                    //  new BluetoothCommunicationSET_Long().execute(":LONG :"+longLenght+","+inst_latitude_double+"#", ":LONG :"+longLenght+","+inst_latitude_double+"#", "Start");

                    //   new BluetoothCommunicationSET_Long().execute(":LONG :06,"+inst_longitude_double+"#", ":LONG :06,"+inst_longitude_double+"#", "Start");
                    // addDataMonth(mPostionFinal + 1, mvDay + "", mvMonth + "", mvYear + "", mvHour, mvMinute, mvNo_of_Start, fvFrequency, fvRMSVoltage, fvOutputCurrent, mvRPM, fvLPM, fvPVVoltage, fvPVCurrent, mvFault, fvInvTemp);
                }
            });


            scrlViewID.fullScroll(View.FOCUS_DOWN);
        }
    }

    ///// data extraction

    private class SyncDebugDataFromLocal extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;

            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {
                String MOB_NAME = UtilMethod.getSharedPreferences(mContext, "MOBName");
                String MOB_API_NAME = UtilMethod.getSharedPreferences(mContext, "MOBversionAPI");
                String MOB_VERSION_NAME = UtilMethod.getSharedPreferences(mContext, "MOBversionRelease");

                jsonObj.put("MOB_NAME", MOB_NAME);
                jsonObj.put("MOB_API_NAME", MOB_API_NAME);
                jsonObj.put("MOB_VERSION_NAME", MOB_VERSION_NAME);
                jsonObj.put("INVOICE_NO", INVOICE_NO_B);
                jsonObj.put("DEVICE_NO", DEVICE_NO);
                jsonObj.put("SIGNL_STREN", SIGNL_STREN);
                jsonObj.put("SIM", SIM);
                jsonObj.put("NET_REG", NET_REG);
                jsonObj.put("SER_CONNECT", SER_CONNECT);
                jsonObj.put("CAB_CONNECT", CAB_CONNECT);
                jsonObj.put("LATITUDE", LATITUDE);
                jsonObj.put("LANGITUDE", LANGITUDE);
                jsonObj.put("MOBILE", MOBILE);
                jsonObj.put("IMEI", IMEI);
                jsonObj.put("DONGAL_ID", DONGAL_ID);
                jsonObj.put("KUNNR", MUserId);
                jsonObj.put("EmpType", MEmpType);
                jsonObj.put("RMS_STATUS", RMS_STATUS);
                jsonObj.put("RMS_CURRENT_ONLINE_STATUS", RMS_CURRENT_ONLINE_STATUS);
                jsonObj.put("RMS_LAST_ONLINE_DATE", RMS_LAST_ONLINE_DATE);
                jsonObj.put("RMS_APP_VERSION", mAppName + " - " + WebURL.APP_VERSION_CODE);
                jsonObj.put("RMS_PROJECT_CODE", project_no);
                jsonObj.put("DEBUG_UNAME ", mInstallerName);
                jsonObj.put("DEBUG_UMOB", mInstallerMOB);

                jsonObj.put("DEBUG_UMOB", mInstallerMOB);
                jsonObj.put("SIM_SR_NO", SIM_SR_NO);


                jsonObj.put("DBUG_EXTRN_STATUS", RMS_DEBUG_EXTRN);
                jsonObj.put("RMS_SERVER_STATUS", RMS_SERVER_DOWN);


                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "No internet connection!!", Toast.LENGTH_SHORT).show();
                // mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO,SIGNL_STREN,SIM,NET_REG,SER_CONNECT,CAB_CONNECT,LATITUDE,LANGITUDE,MOBILE,IMEI,DONGAL_ID,MUserId,true);

                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("action", String.valueOf(ja_invc_data)));///array name lr_save
            Log.e("DATA", "$$$$" + param1_invc);

            System.out.println(param1_invc);

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                //obj2 = CustomHttpClient.executeHttpPost1(WebURL.SAVE_INSTALLATION_DATA, param1_invc);
                obj2 = CustomHttpClient.executeHttpPost1(NewSolarVFD.SAVE_VK_PAGE, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {
                    JSONObject object = new JSONObject(obj2);
                    String mStatus = object.getString("status");
                    final String mMessage = object.getString("message");
                    String jo11 = object.getString("response");
                    System.out.println("jo11==>>" + jo11);
                    if (mStatus.equalsIgnoreCase("true")) {
                        if ((vkp + 1) == mBTResonseDataList.size()) {
                            Message msg = new Message();
                            msg.obj = "Data Submitted Successfully...";
                            mHandler.sendMessage(msg);
                            dialog.dismiss();
                            progressDialog.dismiss();

                            Constant.BT_DEVICE_NAME = "";
                            Constant.BT_DEVICE_MAC_ADDRESS = "";
                        }
                        //   finish();
                        //finish();
                    } else {


                        //  mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO,SIGNL_STREN,SIM,NET_REG,SER_CONNECT,CAB_CONNECT,LATITUDE,LANGITUDE,MOBILE,IMEI,DONGAL_ID,MUserId,true);

                        Message msg = new Message();
                        msg.obj = "Data Not Submitted, Please try After Sometime.";
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                        progressDialog.dismiss();
                        //  finish();
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
                progressDialog.dismiss();
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                vkp++;

                System.out.println("vkp==Vikas==>>" + vkp);

                if (vkp < mBTResonseDataList.size()) {
                    DEVICE_NO = mBTResonseDataList.get(vkp).getDEVICENO();

                    SIGNL_STREN = mBTResonseDataList.get(vkp).getSIGNLSTREN();
                    String[] mStrArry = SIGNL_STREN.split("###");
                    SIGNL_STREN = mStrArry[0];
                    INVOICE_NO_B = mStrArry[1];
                    SIM = mBTResonseDataList.get(vkp).getSIM();

                    String[] mStrArrySim = SIM.split("###");
                    SIM = mStrArrySim[0];
                    SIM_SR_NO = mStrArrySim[1];

                    NET_REG = mBTResonseDataList.get(vkp).getNETREG();
                    SER_CONNECT = mBTResonseDataList.get(vkp).getSERCONNECT();
                    CAB_CONNECT = mBTResonseDataList.get(vkp).getCABCONNECT();
                    LATITUDE = mBTResonseDataList.get(vkp).getLATITUDE();
                    LANGITUDE = mBTResonseDataList.get(vkp).getLANGITUDE();
                    MOBILE = mBTResonseDataList.get(vkp).getMOBILE();
                    IMEI = mBTResonseDataList.get(vkp).getIMEI();
                    DONGAL_ID = mBTResonseDataList.get(vkp).getDONGALID();
                    RMS_STATUS = mBTResonseDataList.get(vkp).getRMS_STATUS();
                    RMS_CURRENT_ONLINE_STATUS = mBTResonseDataList.get(vkp).getRMS_CURRENT_ONLINE_STATUS();
                    RMS_LAST_ONLINE_DATE = mBTResonseDataList.get(vkp).getRMS_LAST_ONLINE_DATE();
                    mInstallerMOB = CustomUtility.getSharedPreferences(mContext, "InstallerMOB");
                    mInstallerName = CustomUtility.getSharedPreferences(mContext, "InstallerName");
                    RMS_DEBUG_EXTRN = "ONLINE FROM SERVER";
                    RMS_SERVER_DOWN = "Working Fine";

                    System.out.println("VikasVIHU==>>" + mBTResonseDataList.get(vkp).getDEVICENO());

                    new SyncDebugDataFromLocal().execute();
                } else {

                    mDatabaseHelperTeacher.deleteAllDataFromTable();
                    dialog.dismiss();
                    progressDialog.dismiss();  // dismiss dialog
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private class SyncInstallationData1 extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");
        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;
            JSONArray ja_invc_data = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            try {
                String MOB_NAME = UtilMethod.getSharedPreferences(mContext, "MOBName");
                String MOB_API_NAME = UtilMethod.getSharedPreferences(mContext, "MOBversionAPI");
                String MOB_VERSION_NAME = UtilMethod.getSharedPreferences(mContext, "MOBversionRelease");

                jsonObj.put("MOB_NAME", MOB_NAME);

                jsonObj.put("MOB_API_NAME", MOB_API_NAME);
                jsonObj.put("MOB_VERSION_NAME", MOB_VERSION_NAME);

                jsonObj.put("DEVICE_NO", DEVICE_NO);
                jsonObj.put("SIGNL_STREN", SIGNL_STREN);
                jsonObj.put("SIM", SIM);
                jsonObj.put("NET_REG", NET_REG);
                jsonObj.put("SER_CONNECT", SER_CONNECT);
                jsonObj.put("CAB_CONNECT", CAB_CONNECT);
                jsonObj.put("LATITUDE", LATITUDE);
                jsonObj.put("LANGITUDE", LANGITUDE);
                jsonObj.put("MOBILE", MOBILE);
                jsonObj.put("IMEI", IMEI);
                jsonObj.put("DONGAL_ID", DONGAL_ID);
                jsonObj.put("KUNNR", MUserId);
                jsonObj.put("EmpType", MEmpType);
                jsonObj.put("RMS_STATUS", RMS_STATUS);
                jsonObj.put("RMS_CURRENT_ONLINE_STATUS", RMS_CURRENT_ONLINE_STATUS);
                jsonObj.put("RMS_LAST_ONLINE_DATE", RMS_LAST_ONLINE_DATE);
                jsonObj.put("RMS_APP_VERSION", mAppName + " - " + WebURL.APP_VERSION_CODE);
                jsonObj.put("RMS_PROJECT_CODE", project_no);

                jsonObj.put("DEBUG_UNAME ", mInstallerName);
                jsonObj.put("DEBUG_UMOB", mInstallerMOB);

                jsonObj.put("SIM_SR_NO", SIM_SR_NO);

                jsonObj.put("DEBUG_EXTRN", mInstallerMOB);

                jsonObj.put("INVOICE_NO", INVOICE_NO_B);

                jsonObj.put("DBUG_EXTRN_STATUS", RMS_DEBUG_EXTRN);
                jsonObj.put("RMS_SERVER_STATUS", RMS_SERVER_DOWN);

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "No internet connection!!\n\nData save in local storage", Toast.LENGTH_SHORT).show();
                mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO, SIGNL_STREN + "###" + Constant.BILL_NUMBER_UNIC, SIM + "###" + SIM_SR_NO, NET_REG, SER_CONNECT, CAB_CONNECT, LATITUDE, LANGITUDE, MOBILE, IMEI, DONGAL_ID, MUserId, RMS_STATUS, RMS_CURRENT_ONLINE_STATUS, RMS_LAST_ONLINE_DATE, mInstallerName, mInstallerMOB, RMS_DEBUG_EXTRN, RMS_SERVER_DOWN, RMS_ORG_D_F, true);

                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("action", String.valueOf(ja_invc_data)));///array name lr_save
            Log.e("DATA", "$$$$" + param1_invc);

            Log.e("DebugDiagnosis===>", param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                //obj2 = CustomHttpClient.executeHttpPost1(WebURL.SAVE_INSTALLATION_DATA, param1_invc);
                obj2 = CustomHttpClient.executeHttpPost1(NewSolarVFD.SAVE_VK_PAGE, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {
                    JSONObject object = new JSONObject(obj2);
                    String mStatus = object.getString("status");
                    final String mMessage = object.getString("message");
                    String jo11 = object.getString("response");
                    System.out.println("jo11==>>" + jo11);
                    if (mStatus.equalsIgnoreCase("true")) {
                        Message msg = new Message();
                        msg.obj = "Data Submitted Successfully...";
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                        progressDialog.dismiss();
                        Constant.BT_DEVICE_NAME = "";
                        Constant.BT_DEVICE_MAC_ADDRESS = "";
                        finish();

                    } else {
                        mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO, SIGNL_STREN + "###" + Constant.BILL_NUMBER_UNIC, SIM + "###" + SIM_SR_NO, NET_REG, SER_CONNECT, CAB_CONNECT, LATITUDE, LANGITUDE, MOBILE, IMEI, DONGAL_ID, MUserId, RMS_STATUS, RMS_CURRENT_ONLINE_STATUS, RMS_LAST_ONLINE_DATE, mInstallerName, mInstallerMOB, RMS_DEBUG_EXTRN, RMS_SERVER_DOWN, RMS_ORG_D_F, true);

                        Message msg = new Message();
                        msg.obj = "Data Not Submitted, Please try After Sometime.";
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                        progressDialog.dismiss();

                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
                progressDialog.dismiss();
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here

            dialog.dismiss();
            progressDialog.dismiss();  // dismiss dialog


        }
    }

    private class SyncRMSCHECKDATAAPI extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        String obj;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            try {
              //  String url = NewSolarVFD.BASE_URL_VK_CHECK_STATUS + "a=" + DEVICE_NO;
                String url  = WebURL.DEVICE_DETAILS+"?DeviceNo="+DEVICE_NO;
                System.out.println("home_obj====>" + url);
                String obj = CustomHttpClient.executeHttpGet(url);
                if (!obj.equalsIgnoreCase("")) {

                    JSONObject jresponse = new JSONObject(obj.substring(1, obj.length() - 1));
                    //  JSONObject jresponse = new JSONObject(obj);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                RMS_SERVER_DOWN = "Working Fine";

                                String[] mDate = jresponse.getString("lastUpdateTime").split(" ");

                                RMS_LAST_ONLINE_DATE = mDate[0];
                                System.out.println("======= min :: " + jresponse.getString("lastUpdateTime"));
                                System.out.println("======= min :: " + RMS_LAST_ONLINE_DATE);

                                if (mDate[0].equalsIgnoreCase("null") || mDate[0].equalsIgnoreCase("")) {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n RMS Last Online Date: Not Available";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n RMS Last Online Date: Not Available"));

                                    RMS_DEBUG_EXTRN = "ONLINE FROM DEBUG";


                                } else {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n RMS Last Online Date: " + mDate[0];
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n RMS Last Online Date: " + mDate[0]));
                                }
                                Date d = new Date();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String currentDateTimeString = sdf.format(d);

                                System.out.println("currentDateTimeString= " + currentDateTimeString);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


                                Date date1 = simpleDateFormat.parse(currentDateTimeString);
                                Date date2 = simpleDateFormat.parse(jresponse.getString("lastUpdateTime"));


                                long difference = date2.getTime() - date1.getTime();
                                int days = (int) (difference / (1000 * 60 * 60 * 24));
                                int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                                int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                                hours = (hours < 0 ? -hours : hours);

                                Log.i("======= Hours", " :: " + hours);
                                String mSTRN = String.valueOf(min);
                                String mSTRN1 = mSTRN.replace("-", "");

                                min = Integer.parseInt(mSTRN1);

                                System.out.println("======= min :: " + min);


                                if (days == 0) {
                                    if (hours > 0) {
                                        changeButtonVisibilityRLV(true, 1.0f, rlvBT_9_ID);
                                        RMS_CURRENT_ONLINE_STATUS = "Offline";
                                        AllCommomSTRContainer = AllCommomSTRContainer + " :\n RMS Current Status: Offline";
                                        lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n RMS Current Status: Offline"));
                                        if (mCheckServerConnectivityValue == 1) {
                                            if (mCheckCableOKValue == 1) {
                                                CAB_CONNECT = "Ok";
                                            } else {
                                                CAB_CONNECT = "Not working";
                                            }
                                        } else   ///// change by me
                                        {
                                            CAB_CONNECT = "Not applicable";
                                        }
                                    } else {
                                        if (min < 15) {
                                            changeButtonVisibilityRLV(false, 0.5f, rlvBT_9_ID);
                                            RMS_CURRENT_ONLINE_STATUS = "Online";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n RMS Current Status: Online";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n RMS Current Status: Online"));
                                        } else {
                                            changeButtonVisibilityRLV(true, 1.0f, rlvBT_9_ID);
                                            RMS_CURRENT_ONLINE_STATUS = "Offline";
                                            AllCommomSTRContainer = AllCommomSTRContainer + " :\n RMS Current Status: Offline";
                                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n RMS Current Status: Offline"));
                                            if (mCheckServerConnectivityValue == 1) {
                                                if (mCheckCableOKValue == 1) {
                                                    CAB_CONNECT = "Ok";
                                                } else {
                                                    CAB_CONNECT = "Not working";
                                                }

                                            } else   ///// change by me
                                            {
                                                CAB_CONNECT = "Not applicable";
                                            }
                                        }
                                    }
                                } else {
                                    changeButtonVisibilityRLV(true, 1.0f, rlvBT_9_ID);
                                    RMS_CURRENT_ONLINE_STATUS = "Offline";
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n RMS Current Status: Offline";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n RMS Current Status: Offline"));
                                    if (mCheckServerConnectivityValue == 1) {
                                        if (mCheckCableOKValue == 1) {
                                            CAB_CONNECT = "Ok";
                                        } else {
                                            CAB_CONNECT = "Not working";
                                        }

                                    } else   ///// change by me
                                    {
                                        CAB_CONNECT = "Not applicable";
                                    }
                                }

                                if (jresponse.getBoolean("dataRecived")) {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n RMS Data Status: YES";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n RMS Data Status: YES"));
                                    RMS_STATUS = "YES";
                                    if (checkFirstTimeOlineStstus == 1) {
                                        RMS_DEBUG_EXTRN = "ONLINE FROM DEBUG";
                                    } else {
                                        RMS_DEBUG_EXTRN = "ONLINE FROM SERVER";
                                        checkFirstTimeOlineStstus = 1;
                                    }
                                } else {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n RMS Data Status: NO";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n RMS Data Status: NO"));
                                    RMS_STATUS = "NO";
                                }


                                if (mCheckButtonclick == 0) {
                                    if (!MOBILE.equalsIgnoreCase("Not Available")) {
                                        long iiii = mDatabaseHelperTeacher.insertSimInfoData(DEVICE_NO, MOBILE, Constant.BILL_NUMBER_UNIC, Constant.BILL_NUMBER_UNIC, MUserId, true);
                                    }

                                }


                                if (mSimDetailsInfoResponse.size() > 0)
                                    mSimDetailsInfoResponse.clear();

                                mSimDetailsInfoResponse = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);

                                if (SER_CONNECT.equalsIgnoreCase("Connected")) {
                                    if (RMS_STATUS.equalsIgnoreCase("YES")) {
                                        mCheckExtraction = "Yes";
                                    } else {
                                        mCheckExtraction = "No";
                                    }
                                } else {
                                    if (mSimDetailsInfoResponse.size() == 3 || mSimDetailsInfoResponse.size() > 3) {
                                        mCheckExtraction = "Yes";
                                    } else {
                                        mCheckExtraction = "No";
                                    }

                                }

                                // sendDataToServer();

                            } catch (Exception e) {
                                // sendDataToServer();
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(mContext, "RMS CHECK", Toast.LENGTH_SHORT).show();
                            // Stuff that updates the UI
                            try {
                                baseRequest.hideLoader();
                                RMS_STATUS = "NO";
                                RMS_CURRENT_ONLINE_STATUS = "NA";
                                RMS_LAST_ONLINE_DATE = "NA";
                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n RMS Data Status: NO";
                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\n RMS Data Status: NO"));
                                checkFirstTimeOlineStstus = 1;
                                changeButtonVisibilityRLV(true, 1.0f, rlvBT_9_ID);

                                RMS_DEBUG_EXTRN = "ONLINE FROM DEBUG";
                                RMS_SERVER_DOWN = "Not Responding";

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    // CustomUtility.isErrorDialog(context, getString(R.string.error), getString(R.string.err_connection));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return obj;
        }

        @Override
        protected void onPostExecute(String result) {

            baseRequest.hideLoader();


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationGetMonthParameter extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;
        private String response;
        private int bytesRead;
        private String condition;
        private final String override = null;

        @Override
        protected void onPreExecute() {

            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

            if (!Constant.isLoding) {
                baseRequest.showLoader();
            }

            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {

            try {
                if (btSocket != null) {
                    if (btSocket.isConnected()) {

                    } else {
                        btSocket = null;
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                        myBluetooth.cancelDiscovery();
                    }
                } else {
                    btSocket = null;
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                }

              /*  if (btSocket == null) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                }*/
                if (!btSocket.isConnected())
                    btSocket.connect();//start connection


                if (btSocket.isConnected()) {

                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(1000);
                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {
                        //  baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    // final InputStream iStream = btSocket.getInputStream();

                    String SS = "";

                    System.out.println("iStream.available()==>>" + iStream.available());

                    while (iStream.available() > 0) {
                        SS += (char) iStream.read();
                    }
//                   String SS =convertStreamToString();

                    if (SS.trim().equalsIgnoreCase("")) {

                    } else {
                        String SSS = SS.replace(",", "VIKASGOTHI");
                        // String [] mS = SS.split(",");
                        String[] mS = SSS.split("VIKASGOTHI");

                        if (mS.length > 0) {

                            for (int i = 0; i < mS.length; i++) {

                                System.out.println("mSmSmS====>>" + mS[i]);

                                if (!mS[i].trim().equalsIgnoreCase("")) {
                                    if (i == 0) {
                                        //mLengthCount = Integer.parseInt(mS[i]);
                                        mLengthCount = Integer.valueOf(mS[i]);
                                    } else {
                                        mMonthHeaderList.add(mS[i]);
                                    }
                                }
                                headerLenghtMonth = "" + mMonthHeaderList.size();
                            }

                            System.out.println("headerLenghtMonth==>> " + headerLenghtMonth);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //new BluetoothCommunicationGetDayParameter().execute(":DLENGTH#", ":DLENGTH#", "OKAY");
                                }
                            });
                        }
                    }

                    while (iStream.available() > 0) {
                        int djdjd = iStream.read();
                    }

                }
            } catch (Exception e) {

                // baseRequest.hideLoader();
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
            // baseRequest.hideLoader();
            if (mMonthHeaderList.size() > 0) {
                new BluetoothCommunicationForFirstActivity().execute(":MDATA#", ":MDATA#", "START");
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new BluetoothCommunicationGetMonthParameter().execute(":MLENGTH#", ":MLENGTH#", "OKAY");
                    }
                });
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForFirstActivity extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;
        private int bytesRead;

        @Override
        protected void onPreExecute() {
            kk = 0;
            mBoolflag = false;
            //  baseRequest.showLoader();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                // btSocket.close();
                if (!btSocket.isConnected())
                    btSocket.connect();//start connection
                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);
                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(400);
                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {
                        System.out.println("vikas--1==>1");
                        //baseRequest.hideLoader();
                        e1.printStackTrace();
                    }
                    for (int i = 0; i < 12; i++) {
                        try {
                            bytesRead = iStream.read();
                        } catch (IOException e) {
                            System.out.println("vikas--2==>2");
                            //baseRequest.hideLoader();
                            e.printStackTrace();
                        }
                    }
                    int[] bytesReaded;
                    //   while (iStream.available() > 0)
                    while (true) {
                        bytesReaded = new int[mLengthCount];
                        for (int i = 0; i < mLengthCount; i++) {
                            // Character mCharOne = (char) iStream.read();
                            //  Character mCharTwo = (char) iStream.read();
                            int mCharOne = 0;
                            int mCharTwo = 0;
                            try {
                                mCharOne = iStream.read();
                                mCharTwo = iStream.read();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                System.out.println("vikas--3==>" + mCharOne + "" + mCharTwo);
                                if ("TX".equalsIgnoreCase((char) mCharOne + "" + (char) mCharTwo)) {

                                    baseRequest.hideLoader();
                                    mBoolflag = true;
                                    break;
                                } else {
                                    if (mCharOne == 0 || mCharTwo == 0) {
                                        bytesReaded[i] = Integer.parseInt("" + mCharOne + mCharTwo, 16);// iStream.read();
                                    } else {
                                        bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);// iStream.read();
                                    }
                                }
                            } catch (NumberFormatException e) {
                                baseRequest.hideLoader();
                                System.out.println("vikas--3==>N");
                                e.printStackTrace();
                            }
                        }

                        int mDay = 0;
                        int mMonth = 0;
                        int mYear = 0;
                        int mHour = 0;
                        int mMinut = 0;
                        int mStatus = 0;
                        int mRPM = 0;
                        int mFault = 0;

                        float fFrequency = 0;
                        float fRMSVoltage = 0;
                        float fOutputCurrent = 0;
                        float fLPM = 0;
                        float fPVVoltage = 0;
                        float fPVCurrent = 0;
                        float fInvTemp = 0;

                        if (!mBoolflag) {
                            kk++;

                            System.out.println("kk++ ==>> " + kk);
                            mDay = bytesReaded[0];
                            mMonth = bytesReaded[1];
                            mYear = bytesReaded[2];
                            mHour = bytesReaded[3];
                            mMinut = bytesReaded[4];
                            mStatus = bytesReaded[5];

                            mTotalTime = new int[10];

                            mTotalTime[0] = mDay;
                            mTotalTime[1] = mMonth;
                            mTotalTime[2] = mYear;
                            mTotalTime[3] = mHour;
                            mTotalTime[4] = mMinut;
                            mTotalTime[5] = mStatus;

                            int i = 6;
                            // System.out.println("bytesReadednm==>> ");
                            for (int k = 6; k < mLengthCount; k += 2) {
                                mTotalTime = Arrays.copyOf(mTotalTime, i + 1);
                                mTotalTime[i] = bytesReaded[k] << 8;
                                mTotalTime[i] |= bytesReaded[k + 1];

                                i++;
                            }
                        } else {

                            File file = new File(UtilMethod.commonDocumentDirPath("ShaktiKusumDebugExtractionFile"), "Month_" + mBtNameHead + ".xls");

                            // File file = new File(mContext.getExternalFilesDir(null), "Month_" + mBtNameHead + ".xls");
                            // File file = new File(mContext.getExternalFilesDir(null), "Month_" + mBtNameHead + ".xlsx");
                            FileOutputStream os = null;
                            System.out.println("vikas--4==>4");
                            //baseRequest.hideLoader();
                            try {
                                os = new FileOutputStream(file);
                                wb.write(os);
                                Log.w("FileUtils", "Writing file" + file);
                                success = true;
                            } catch (IOException e) {
                                Log.w("FileUtils", "Error writing " + file, e);
                            } catch (Exception e) {
                                Log.w("FileUtils", "Failed to save file", e);
                            } finally {
                                try {
                                    os = new FileOutputStream(file);
                                    wb.write(os);
                                    if (null != os)
                                        os.close();
                                } catch (Exception ex) {
                                    System.out.println("vikas--5==>5");
                                    // baseRequest.hideLoader();
                                    ex.printStackTrace();
                                }
                            }
                            // myBluetooth.disable();

                            // Toast.makeText(mContext, "Process completed..1", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        //  if((mDay == 255) && (mMonth == 255) && (mYear == 255) && (mHour == 255) && (mMinut == 255) && (mStatus == 255))
                        if (((mDay == 255) && (mMonth == 255) && (mYear == 255)) || ((mDay == 0) && (mMonth == 0) && (mYear == 0))) {
                            // File file = new File(mContext.getExternalFilesDir(null), "Month_" + mBtNameHead + ".xlsx");

                            File file = new File(UtilMethod.commonDocumentDirPath("ShaktiKusumDebugExtractionFile"), "Month_" + mBtNameHead + ".xls");

                            //  File file = new File(mContext.getExternalFilesDir(null), "Month_" + mBtNameHead + ".xls");
                            FileOutputStream os = null;
                            //  File file = new File(mContext.getExternalFilesDir(null), "Month" + mBtNameHead + ".xls");
                            //   FileOutputStream os = null;
                            //    baseRequest.hideLoader();
                            try {
                                os = new FileOutputStream(file);
                                wb.write(os);
                                Log.w("FileUtils", "Writing file" + file);
                                success = true;
                            } catch (IOException e) {
                                Log.w("FileUtils", "Error writing " + file, e);
                            } catch (Exception e) {
                                Log.w("FileUtils", "Failed to save file", e);
                            } finally {
                                try {
                                    os = new FileOutputStream(file);
                                    wb.write(os);
                                    if (null != os)
                                        os.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            mBoolflag = true;
                        } else {
                            // saveExcelFile(mContext, "VikasTest.xls", mPostionFinal,mDay,mMonth,mYear,mHour,mMinut,mStatus,mFrequency,mRMSVoltage,mOutputCurrent,mRPM,mLPM,mPVVoltage,mPVCurrent,mFault,mInvTemp);
                            if (mPostionFinal == 0) {
                                //New Workbook
                                wb = new HSSFWorkbook();

                                sheet1 = wb.createSheet("myOrder");
                                row = sheet1.createRow(0);
                                //Cell style for header row
                               /* cs = wb.createCellStyle();
                                cs.setFillForegroundColor(HSSFColor.LIME.index);
                                cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);*/
                                //New Sheet


                                for (int k = 0; k < mMonthHeaderList.size(); k++) {

                                    String[] mStringSplitStart = mMonthHeaderList.get(k).split("-");

                                    sheet1.setColumnWidth(k, (10 * 200));
                                    c = row.createCell(k);
                                    // c.setCellValue(mMonthHeaderList.get(k));
                                    c.setCellValue(mStringSplitStart[0]);
                                    c.setCellStyle(cs);
                                }
                                /*
                                c = row.createCell(3);
                                c.setCellValue("Hour");
                                c.setCellStyle(cs);
                        */
                                row = sheet1.createRow(mPostionFinal + 1);

                                c = row.createCell(0);
                                c.setCellValue("" + mDay);
                                c.setCellStyle(cs);

                                c = row.createCell(1);
                                c.setCellValue("" + mMonth);
                                c.setCellStyle(cs);

                                c = row.createCell(2);
                                c.setCellValue("" + mYear);
                                c.setCellStyle(cs);

                                c = row.createCell(3);
                                c.setCellValue("" + mHour);
                                c.setCellStyle(cs);

                                c = row.createCell(4);
                                c.setCellValue("" + mMinut);
                                c.setCellStyle(cs);

                                c = row.createCell(5);
                                c.setCellValue("" + mStatus);
                                c.setCellStyle(cs);

                                try {
                                    //  for (int j = 3; j < mLengthCount; j++)
                                    for (int j = 6; j < mMonthHeaderList.size(); j++) {
                                        //     fTotalEnergy = Float.intBitsToFloat(mDayDataList.get(i)[j]);


                                        String[] mStringSplitStart = mMonthHeaderList.get(j).split("-");
                                        int mmIntt = 1;
                                        mmIntt = Integer.parseInt(mStringSplitStart[1]);

                                        try {

                                            if (mmIntt == 1) {


                                                sheet1.setColumnWidth(j, (10 * 200));
                                                fFrequency = mTotalTime[j];

                                                c = row.createCell(j);
                                                c.setCellValue("" + fFrequency);
                                                c.setCellStyle(cs);

                                                // tr.addView(getTextView(counter, ((mTotalTime[i] / mmIntt)) + "", Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.white)));
                                            } else {


                                                sheet1.setColumnWidth(j, (10 * 200));
                                                fFrequency = mTotalTime[j];

                                                float mmValue = (((float) mTotalTime[j]) / ((float) mmIntt));

                                                c = row.createCell(j);
                                                // c.setCellValue("" + fFrequency);
                                                c.setCellValue("" + mmValue);
                                                c.setCellStyle(cs);

                                                //  tr.addView(getTextView(counter, ( (((float)mTotalTime[i]) / ((float)mmIntt))) + "", Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.white)));
                                            }

                                        } catch (Exception e) {
                                            //   baseRequest.hideLoader();
                                            e.printStackTrace();
                                        }


                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    baseRequest.hideLoader();
                                }

                            } else {
                                // cs.setFillPattern(HSSFCellStyle.NO_FILL);
                                row = sheet1.createRow(mPostionFinal + 1);

                                c = row.createCell(0);
                                c.setCellValue("" + mDay);
                                c.setCellStyle(cs);

                                c = row.createCell(1);
                                c.setCellValue("" + mMonth);
                                c.setCellStyle(cs);

                                c = row.createCell(2);
                                c.setCellValue("" + mYear);
                                c.setCellStyle(cs);

                                c = row.createCell(3);
                                c.setCellValue("" + mHour);
                                c.setCellStyle(cs);

                                c = row.createCell(4);
                                c.setCellValue("" + mMinut);
                                c.setCellStyle(cs);

                                c = row.createCell(5);
                                c.setCellValue("" + mStatus);
                                c.setCellStyle(cs);


                                try {
                                    //  for (int j = 3; j < mLengthCount; j++)
                                    for (int j = 6; j < mMonthHeaderList.size(); j++) {
                                        //     fTotalEnergy = Float.intBitsToFloat(mDayDataList.get(i)[j]);


                                        String[] mStringSplitStart = mMonthHeaderList.get(j).split("-");
                                        int mmIntt = 1;
                                        mmIntt = Integer.parseInt(mStringSplitStart[1]);

                                        try {

                                            if (mmIntt == 1) {


                                                sheet1.setColumnWidth(j, (10 * 200));
                                                fFrequency = mTotalTime[j];

                                                c = row.createCell(j);
                                                c.setCellValue("" + fFrequency);
                                                c.setCellStyle(cs);

                                                // tr.addView(getTextView(counter, ((mTotalTime[i] / mmIntt)) + "", Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.white)));
                                            } else {


                                                sheet1.setColumnWidth(j, (10 * 200));
                                                fFrequency = mTotalTime[j];

                                                float mmValue = (((float) mTotalTime[j]) / ((float) mmIntt));

                                                c = row.createCell(j);
                                                // c.setCellValue("" + fFrequency);
                                                c.setCellValue("" + mmValue);
                                                c.setCellStyle(cs);

                                                //  tr.addView(getTextView(counter, ( (((float)mTotalTime[i]) / ((float)mmIntt))) + "", Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.white)));
                                            }

                                        } catch (Exception e) {
                                               baseRequest.hideLoader();
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    baseRequest.hideLoader();
                                }


                            }

                            mvDay = mDay;
                            mvMonth = mMonth;
                            mvYear = mYear;
                            mvHour = "" + mHour;
                            mvMinute = "" + mMinut;
                            mvNo_of_Start = "" + mStatus;
                            fvFrequency = fFrequency;
                            fvRMSVoltage = fRMSVoltage;
                            fvOutputCurrent = fOutputCurrent;
                            mvRPM = "" + mRPM;
                            fvLPM = fLPM;
                            fvPVVoltage = fPVVoltage;
                            fvPVCurrent = fPVCurrent;
                            mvFault = "" + mFault;
                            fvInvTemp = fInvTemp;

                            mPostionFinal++;
                        }
                    }
                    while (iStream.available() > 0) {
                        int djdjd = iStream.read();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("vikas--8==>8");
                 baseRequest.hideLoader();
            }
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            // baseRequest.hideLoader();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    baseRequest.hideLoader();
                }
            });

            super.onPostExecute(result);
            //btSocket = null;
        }
    }
}

