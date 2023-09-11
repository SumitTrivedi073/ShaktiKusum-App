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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shaktipumplimited.retrofit.ApiClient;
import com.shaktipumplimited.retrofit.ApiInterface;
import com.shaktipumplimited.retrofit.BaseRequest;
import com.shaktipumplimited.retrofit.RequestReciever;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import activity.BaseActivity;
import activity.GPSTracker;
import bean.BTResonseData;
import bean.DeviceDetailModel;
import bean.ProfileUpdateModel;
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

public class BlueToothDebugNewActivity extends BaseActivity {
    private static final CellStyle cs = null;
    private static boolean success = false;
    private static Workbook wb = null;
    private static Sheet sheet1 = null;
    private static Row row;
    private static Cell c = null;
    private final String MDeviceId = "null";
    private final String MLoginType = "null";
    private final String allCammand = "AT+CPIN?";
    //private String []  AllCommandArray ={"AT+CPIN?\r\n","AT+GSN\r\n","AT+CIMI\r\n","AT+QINISTAT\r\n","AT+CSQ\r\n","AT+CREG?\r\n","AT+CGREG?\r\n","AT+CGDCONT?\r\n","AT+QICSGP?\r\n"};
    private final String[] AllCommandArray = {"AT+GSN\r\n", "AT+CIMI\r\n", "AT+QINISTAT\r\n", "AT+CSQ\r\n", "AT+CREG?\r\n", "AT+CGREG?\r\n", "AT+CGDCONT?\r\n", "AT+QICSGP?\r\n"};
    private final boolean mBoolflagCheck = false;
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
    String RMS_ORG_D_F = "", SS = "";
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

    String mAppName = "KUSUM", dirName = "";
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
    // private ImageView imgSendTextID;
    private RelativeLayout rlvSendButtonID;
    private ImageView imgBTShareFILEID;
    private ImageView imgBTSyncFILEID;
    private ImageView imgBTUploadFILEID;
    private boolean flag;
    private int mLengthCount;
    private String headerLenghtMonth = "";
    private List<String> mMonthHeaderList;
    private boolean mBoolflag = false,isDataExtract= false;
    private RelativeLayout rlvLoadingViewID;
    private TextView txtHeadingLabelID;
    private String MEmpType = "null", version;
    private String ControllerSerialNumber;
    private static Cell cell = null;

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


        rlvLoadingViewID = findViewById(R.id.rlvLoadingViewID);
        txtHeadingLabelID = findViewById(R.id.txtHeadingLabelID);
        scrlViewID = findViewById(R.id.scrlViewID);
        rlvSendButtonID = findViewById(R.id.rlvSendButtonID);
        imgBTShareFILEID = findViewById(R.id.imgBTShareFILEID);
        imgBTSyncFILEID = findViewById(R.id.imgBTSyncFILEID);
        imgBTUploadFILEID = findViewById(R.id.imgBTUploadFILEID);

        rlvBackViewID = findViewById(R.id.rlvBackViewID);
        rlvBT_S1_ID = findViewById(R.id.rlvBT_S1_ID);
        rlvBT_S2_ID = findViewById(R.id.rlvBT_S2_ID);
        rlvBT_7_ID = findViewById(R.id.rlvBT_7_ID);
        rlvBT_7_ID_save = findViewById(R.id.rlvBT_7_ID_save);
        rlvBT_8_ID_SimUpdated = findViewById(R.id.rlvBT_8_ID_SimUpdated);
        rlvBT_8_ID = findViewById(R.id.rlvBT_8_ID);
        rlvBT_9_ID = findViewById(R.id.rlvBT_9_ID);
        lvlMainTextContainerID = findViewById(R.id.lvlMainTextContainerID);
        edtPutCommandID = findViewById(R.id.edtPutCommandID);
        mIntCheckDeviceType = 0;


        changeButtonVisibilityRLV(true, 0.5f, rlvBT_S1_ID);
        changeButtonVisibilityRLV(true, 0.5f, rlvBT_S2_ID);
        changeButtonVisibilityRLV(false, 0.5f, rlvBT_7_ID_save);
        setClickEventListner();
        getGpsLocation();
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            version = info.versionName;

            Log.e("version=====>", version);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("versionErrpr====>", e.getMessage());
            throw new RuntimeException(e);

        }
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
                    SubmitData();
                } else {
                    Toast.makeText(mContext, "Local database is  empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rlvBackViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                   /* mInstallerMOB = CustomUtility.getSharedPreferences(mContext, "InstallerMOB");
                    mInstallerName = CustomUtility.getSharedPreferences(mContext, "InstallerName");

                    if (UtilMethod.isOnline(mContext)) {
                        if (mSimDetailsInfoResponse.size() > 0)
                            mSimDetailsInfoResponse.clear();
                        mSimDetailsInfoResponse = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);
                        SubmitData();
                    } else {
                        saveDataLocaly();
                    }*/


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
                mInstallerMOB = CustomUtility.getSharedPreferences(mContext, "InstallerMOB");
                mInstallerName = CustomUtility.getSharedPreferences(mContext, "InstallerName");

                if (!mInstallerName.equalsIgnoreCase("") && !mInstallerName.equalsIgnoreCase("null") && !mInstallerMOB.equalsIgnoreCase("") && !mInstallerMOB.equalsIgnoreCase("null")) {
                    if (mSimDetailsInfoResponse.size() > 0)
                        mSimDetailsInfoResponse.clear();
                    mSimDetailsInfoResponse = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);
                    if (!SER_CONNECT.isEmpty() && SER_CONNECT.equals("Connected")) {
                        CustomUtility.setSharedPreference(getApplicationContext(), "DeviceStatus", getResources().getString(R.string.online));
                        if (CustomUtility.isInternetOn(getApplicationContext())) {
                            sendDataToServer();
                        } else {
                            saveDataLocaly();
                        }
                    } else {
                        if (mSimDetailsInfoResponse.size() >= 1) {
                            if (mSimDetailsInfoResponse.size() >= 2) {
                                if (mSimDetailsInfoResponse.size() >= 3) {


                                    if (isDataExtract) {
                                        WebURL.BT_DEBUG_CHECK = 1;
                                        Constant.DBUG_PER_OFLINE = "X";//PER_OFLINE
                                        CustomUtility.setSharedPreference(getApplicationContext(), "DeviceStatus", getResources().getString(R.string.offline));
                                        if (CustomUtility.isInternetOn(getApplicationContext())) {
                                            sendDataToServer();
                                        } else {
                                            saveDataLocaly();
                                        }
                                    } else {
                                        Toast.makeText(mContext, "Please data extract first than submit.", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    CustomUtility.ShowToast(getResources().getString(R.string.insertThirdSim), getApplicationContext());
                                }
                            } else {
                                CustomUtility.ShowToast(getResources().getString(R.string.insertSecondSim), getApplicationContext());
                            }
                        } else {
                            CustomUtility.ShowToast(getResources().getString(R.string.sim_insertMsg), getApplicationContext());

                        }
                    }
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

                if (UtilMethod.isOnline(mContext)) {
                    SyncRMSCHECKDATAAPI();
                } else {
                    Toast.makeText(mContext, "Please check internet connections.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rlvBT_9_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dirName = getMediaFilePath("", "Month_" + ControllerSerialNumber + ".xls");

                if (!dirName.isEmpty()) {
                    if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                        Log.e("Failed", "Storage not available or read only");
                    } else {
                        kk = 0;
                        mmCount = 0;
                        mPostionFinal = 0;
                        mBoolflag = false;
                        if (mMonthHeaderList.size() > 0)
                            mMonthHeaderList.clear();
                        new BluetoothCommunicationGetMonthParameter().execute(":MLENGTH#", ":MLENGTH#", "OKAY");


                    }
                }
            }
        });


    }


    private void sendDataToServer() {

        if (RMS_STATUS.equalsIgnoreCase("YES")) {
            if (!DEVICE_NO.isEmpty() && !NET_REG.isEmpty() && !LATITUDE.isEmpty() && !LANGITUDE.isEmpty()) {
                WebURL.CHECK_FINAL_ALL_OK = 1;
                WebURL.BT_DEBUG_CHECK = 1;
                Constant.DBUG_PER_OFLINE = "";//PER_OFLINE


                SubmitData();
            } else {
                WebURL.BT_DEBUG_CHECK = 0;
                Toast.makeText(mContext, "Debug data not properly please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!DEVICE_NO.isEmpty() && !NET_REG.isEmpty() && !LATITUDE.isEmpty() && !LANGITUDE.isEmpty()) {
                WebURL.BT_DEBUG_CHECK = 1;
                Constant.DBUG_PER_OFLINE = "X";//PER_OFLINE

                SubmitData();

            } else {
                WebURL.BT_DEBUG_CHECK = 0;
                Toast.makeText(mContext, "Debug data not properly please try again.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void saveDataLocaly() {
        CustomUtility.setSharedPreference(mContext, Constant.isDebugDevice, "true");
        mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO, SIGNL_STREN + "###" + Constant.BILL_NUMBER_UNIC, SIM + "###" + SIM_SR_NO, NET_REG, SER_CONNECT, CAB_CONNECT, LATITUDE, LANGITUDE, MOBILE, IMEI, DONGAL_ID, MUserId, RMS_STATUS, RMS_CURRENT_ONLINE_STATUS, RMS_LAST_ONLINE_DATE, mInstallerName, mInstallerMOB, RMS_DEBUG_EXTRN, RMS_SERVER_DOWN, RMS_ORG_D_F, true);
        onBackPressed();
        Toast.makeText(mContext, " Data save in local Data base", Toast.LENGTH_SHORT).show();


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
            jsonObject.addProperty("DeviceNo", mBtNameHead);
            jsonObject.addProperty("Content", AllCommomSTRContainerIN);
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
        baseRequest.showLoader();
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
        super.onBackPressed();
        finish();
    }

    private void callCheckSimDataPackAPI(int mSignalStrength, int mNetworkConnect, int mServerConnect) {

        Map<String, String> wordsByKey = new HashMap<>();
        wordsByKey.put("device", ControllerSerialNumber + "-0");// DEVICE_NO = sssM[0];
        baseRequest.callAPIGETDebugApp(1, wordsByKey, NewSolarVFD.SIM_STATUS_VK_PAGE);/////
        baseRequest.showLoader();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int APINumber, String Json, Object obj) {

                try {
                    Log.e("callCheckSimDataPackAPI===========>", Json.trim());
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


                            AllCommomSTRContainer = AllCommomSTRContainer + " :\nSim  Status :" + mSimStatus;
                            lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nSim  Status : " + mSimStatus));

                            if (mSignalStrength == 1 && mNetworkConnect == 1 && mServerConnect == 1) {
                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack : Activate";
                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : Activate"));

                            } else if (mSignalStrength == 1 && mNetworkConnect == 1 && mServerConnect == 0) {
                                if (mSimStatusActive.equalsIgnoreCase("1")) {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack : Activate";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : Activate"));
                                } else {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack : Not Activate";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : Not Activate"));
                                }
                            } else {
                                AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack :" + mSimStatus;
                                lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : " + mSimStatus));

                                if (mSimStatusActive.equalsIgnoreCase("1")) {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack : Activate";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : Activate"));
                                } else {
                                    AllCommomSTRContainer = AllCommomSTRContainer + " :\n Data Pack : Not Activate";
                                    lvlMainTextContainerID.addView(getTextViewTTpp(pp, "\nData Pack : Not Activate"));
                                }
                            }

                        }


                    } else {
                        baseRequest.hideLoader();

                    }

                } catch (Exception e) {
                    baseRequest.hideLoader();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int APINumber, String errorCode, String message) {
                baseRequest.hideLoader();
                // Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNetworkFailure(int APINumber, String message) {
                baseRequest.hideLoader();
                Toast.makeText(mContext, "Please check internet connection!", Toast.LENGTH_LONG).show();
            }
        });

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
        if (!isFinishing()) {
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
                CustomUtility.setSharedPreference(getApplicationContext(), "DeviceStatus", getResources().getString(R.string.offline));
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

    public void GetProfileUpdate_Task(String deviceno, String type, String len, String filePath) {

        if (UtilMethod.isOnline(mContext)) {
            baseRequest.showLoader();
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

            Call<ProfileUpdateModel> call = apiService.getProfileUpdateDatanew(deviceno1, type1, headerLenght1, body);

            call.enqueue(new Callback<ProfileUpdateModel>() {
                @Override
                public void onResponse(@NonNull Call<ProfileUpdateModel> call, @NonNull retrofit2.Response<ProfileUpdateModel> response) {
                    try {
                        ProfileUpdateModel dashResponse = response.body();
                        baseRequest.hideLoader();
                        Log.e("status", "** " + dashResponse);

                        if (dashResponse.getStatus().equalsIgnoreCase("true")) {

                            Toast.makeText(mContext, dashResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, dashResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

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
                        Log.e("Error====>", t.getMessage());
                    } catch (Exception e) {
                        rlvLoadingViewID.setVisibility(View.GONE);
                        e.printStackTrace();
                        Log.e("Error====>", t.getMessage());
                    }
                }
            });
        } else {
            rlvLoadingViewID.setVisibility(View.GONE);
            baseRequest.hideLoader();
            Toast.makeText(mContext, "Please check internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void SubmitData() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = new JSONObject();

        try {
            String MOB_NAME = CustomUtility.currentVersionName();
            String MOB_API_NAME = CustomUtility.currentVersionAPI();
            String MOB_VERSION_NAME = String.valueOf(Build.VERSION.SDK_INT);
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
            jsonObj.put("RMS_APP_VERSION", mAppName + " - " + version);
            jsonObj.put("RMS_PROJECT_CODE", project_no);
            jsonObj.put("DEBUG_UNAME ", mInstallerName);
            jsonObj.put("DEBUG_UMOB", mInstallerMOB);
            jsonObj.put("SIM_SR_NO", SIM_SR_NO);
            jsonObj.put("DEBUG_EXTRN", mInstallerMOB);
            jsonObj.put("INVOICE_NO", INVOICE_NO_B);
            jsonObj.put("DBUG_EXTRN_STATUS", RMS_DEBUG_EXTRN);
            jsonObj.put("RMS_SERVER_STATUS", RMS_SERVER_DOWN);

            jsonArray.put(jsonObj);
            mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO, SIGNL_STREN + "###" + Constant.BILL_NUMBER_UNIC, SIM + "###" + SIM_SR_NO, NET_REG, SER_CONNECT, CAB_CONNECT, LATITUDE, LANGITUDE, MOBILE, IMEI, DONGAL_ID, MUserId, RMS_STATUS, RMS_CURRENT_ONLINE_STATUS, RMS_LAST_ONLINE_DATE, mInstallerName, mInstallerMOB, RMS_DEBUG_EXTRN, RMS_SERVER_DOWN, RMS_ORG_D_F, true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("URL=====>", NewSolarVFD.saveDebugData + "?action=" + jsonArray);
        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
        param1.add(new BasicNameValuePair("action", String.valueOf(jsonArray)));
        showProgressDialogue();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);

            String obj2 = CustomHttpClient.executeHttpPost1(NewSolarVFD.saveDebugData, param1);

            if (!obj2.isEmpty()) {


                JSONObject jsonObject = new JSONObject(obj2);
                Log.e("Response=====>", jsonObject.toString());


                String mStatus = jsonObject.getString("status");
                if (mStatus.equals("true")) {
                    stopProgressDialogue();
                    mInstallerMOB = CustomUtility.getSharedPreferences(mContext, "InstallerMOB");
                    mInstallerName = CustomUtility.getSharedPreferences(mContext, "InstallerName");

                    CustomUtility.setSharedPreference(mContext, Constant.isDebugDevice, "true");

                    Constant.BT_DEVICE_NAME = "";
                    Constant.BT_DEVICE_MAC_ADDRESS = "";
                    CustomUtility.ShowToast(getResources().getString(R.string.dataSubmittedSuccessfully), getApplicationContext());

                    onBackPressed();
                } else {
                    stopProgressDialogue();
                    CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), getApplicationContext());
                }


            }
        } catch (Exception e) {
            saveDataLocaly();
            stopProgressDialogue();
            e.printStackTrace();
        }
    }

    public void showProgressDialogue() {
        progressDialog.setMessage(getResources().getString(R.string.sendingDataServer));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void stopProgressDialogue() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void SyncRMSCHECKDATAAPI() {
        CustomUtility.showProgressDialogue(BlueToothDebugNewActivity.this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                WebURL.DEVICE_DETAILS + "?DeviceNo=" + DEVICE_NO, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(BlueToothDebugNewActivity.this);
                baseRequest.hideLoader();
                Log.e("SyncRMSCHECKDATAAPI===========>", response.toString().trim());

                if (!response.toString().isEmpty()) {

                    DeviceDetailModel deviceDetailModel = new Gson().fromJson(response.toString(), DeviceDetailModel.class);
                    if (deviceDetailModel != null && deviceDetailModel.getResponse() != null && String.valueOf(deviceDetailModel.getStatus()).equals("true")) {

                        lvlMainTextContainerID.addView(getTextViewTTpp(0, "\nCustomerName : " + deviceDetailModel.getResponse().getCustomerName()));
                        lvlMainTextContainerID.addView(getTextViewTTpp(1, "\nCustomerPhoneNo : " + deviceDetailModel.getResponse().getCustomerPhoneNo()));
                        lvlMainTextContainerID.addView(getTextViewTTpp(2, "\nOperatorName : " + deviceDetailModel.getResponse().getOperatorName()));
                        lvlMainTextContainerID.addView(getTextViewTTpp(3, "\nPumpStatus : " + deviceDetailModel.getResponse().getPumpStatus()));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                baseRequest.hideLoader();
                CustomUtility.hideProgressDialog(BlueToothDebugNewActivity.this);
                if (error.getMessage() != null && !error.getMessage().isEmpty()) {
                    CustomUtility.ShowToast(error.getMessage(), BlueToothDebugNewActivity.this);

                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private class BluetoothCommunicationForDebugCheckDevice extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            baseRequest.showLoader();
        }

        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (btSocket.isConnected()) {
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
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
            AllCommomSTRContainer = AllCommomSTRContainer + "\nIMEI";
            new BluetoothCommunicationForGET_IMEI_66_1().execute(":GET IMEI#", ":GET IMEI#", "Start");
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

                            AllTextSTR = "";
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();
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
            baseRequest.hideLoader();

             if (DEVICE_NO != null && !DEVICE_NO.isEmpty() && !DEVICE_NO.equals(ControllerSerialNumber + "-0")) {
                ShowAlertResponse();
            } else {
                if (CustomUtility.isInternetOn(getApplicationContext())) {
                    Log.e("NetworkAvailable=========>", "true");
                    callCheckSimDataPackAPI(mCheckSignelValue, mCheckNetworkValue, mCheckServerConnectivityValue);

                } else {
                    Log.e("NetworkAvailable=========>", "false");
                }
                changeButtonVisibilityRLV(true, 1.0f, rlvBT_7_ID_save);
            }


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

                            AllTextSTR = "";
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();
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
            scrlViewID.fullScroll(View.FOCUS_DOWN);
            baseRequest.hideLoader();
            WebURL.SERVER_CONNECTIVITY_OK = mCheckServerConnectivityValue;

             if (DEVICE_NO != null && !DEVICE_NO.isEmpty() && !DEVICE_NO.equals(ControllerSerialNumber + "-0")) {
                ShowAlertResponse();
            } else {
                if (CustomUtility.isInternetOn(getApplicationContext())) {
                    Log.e("NetworkAvailable3333========>", "true");
                    callCheckSimDataPackAPI(mCheckSignelValue, mCheckNetworkValue, mCheckServerConnectivityValue);
                } else {
                    Log.e("NetworkAvailable3333=========>", "false");
                    Toast.makeText(mContext, "Please check internet connections.", Toast.LENGTH_SHORT).show();
                }
                changeButtonVisibilityRLV(true, 1.0f, rlvBT_7_ID_save);
            }
        }

    }

    ///// data extraction

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


                            try {

                                RMS_ORG_D_F = AllTextSTR;
                                String[] sssM1 = AllTextSTR.split(",");
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
                                    } else {

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
            scrlViewID.fullScroll(View.FOCUS_DOWN);
            baseRequest.hideLoader();

            WebURL.SERVER_CONNECTIVITY_OK = mCheckServerConnectivityValue;

              if (DEVICE_NO != null && !DEVICE_NO.isEmpty() && !DEVICE_NO.equals(ControllerSerialNumber + "-0")) {
                ShowAlertResponse();
            } else {
                if (CustomUtility.isInternetOn(getApplicationContext())) {
                    Log.e("NetworkAvailable5555=========>", "true");
                    callCheckSimDataPackAPI(mCheckSignelValue, mCheckNetworkValue, mCheckServerConnectivityValue);
                } else {
                    Log.e("NetworkAvailable5555=========>", "false");
                    Toast.makeText(mContext, "Please check internet connections.", Toast.LENGTH_SHORT).show();

                }
                changeButtonVisibilityRLV(true, 1.0f, rlvBT_7_ID_save);
            }

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

        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (btSocket.isConnected()) {
                    } else {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(Constant.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
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
                        }

                    } catch (InterruptedException e1) {
                        baseRequest.hideLoader();
                        CustomUtility.ShowToast(getResources().getString(R.string.pleasetryAgain),mContext);
                        e1.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                baseRequest.hideLoader();
                CustomUtility.ShowToast(getResources().getString(R.string.pleasetryAgain),mContext);
                return false;
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //textView.setText("Your new text");
                            new BluetoothCommunicationForDebugCheckDevice().execute(":DEBUG M66#", ":DEBUG M66#", "START");

                        }
                    }, 2 * 200);
                }
            });


            scrlViewID.fullScroll(View.FOCUS_DOWN);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationGetMonthParameter extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        private final String override = null;
        public int RetryCount = 0;
        private String response;
        private int bytesRead;
        private String condition;

        @Override
        protected void onPreExecute() {

            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

            CustomUtility.showProgressDialogue(BlueToothDebugNewActivity.this);
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
                    btSocket.getOutputStream().write(STARTRequest);
                    sleep(5000);
                    iStream = btSocket.getInputStream();


                    while (iStream.available() > 0) {
                        SS += (char) iStream.read();
                    }

                    if (!SS.trim().isEmpty()) {

                        //   String SSS = SS.replace(",", "");
                        String[] mS = SS.split(",");
                        Log.e("sss====>", SS);
                        Log.e("sss====>", Arrays.toString(mS));
                        if (mS.length > 0) {

                            for (int i = 0; i < mS.length; i++) {

                                System.out.println("mSmSmS====>>" + mS[i]);

                                if (!mS[i].trim().isEmpty()) {
                                    if (i == 0) {
                                        //mLengthCount = Integer.parseInt(mS[i]);
                                        mLengthCount = Integer.parseInt(mS[i]);
                                    } else {
                                        mMonthHeaderList.add(mS[i]);
                                    }
                                }
                                headerLenghtMonth = "" + mMonthHeaderList.size();
                            }

                            System.out.println("headerLenghtMonth==>> " + headerLenghtMonth);

                        }
                    }
                }

            } catch (Exception e) {
                Log.e("Exception====>", e.getMessage());

                Message mess = new Message();
                mess.obj = " Some conflict occurred in data extraction. Please remove and reconnect dongle";
                mHandler.sendMessage(mess);
                CustomUtility.hideProgressDialog(BlueToothDebugNewActivity.this);
                return false;
            }

            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            Log.e("DeviceMonthHeaderList====>", String.valueOf(mMonthHeaderList.size()));

            if (mMonthHeaderList.size() > 0) {
                Log.e("DeviceMonthHeaderList1====>", String.valueOf(mMonthHeaderList.size()));
                new BluetoothCommunicationForFirstActivity().execute(":MDATA#", ":MDATA#", "START");
            } else {
                CustomUtility.hideProgressDialog(BlueToothDebugNewActivity.this);
                Message msg = new Message();
                msg.obj = "Please try again!";
                mHandler.sendMessage(msg);
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new BluetoothCommunicationGetDeviceYearlyData().execute(":MLENGTH#", ":MLENGTH#", "OKAY");
                    }
                });*/
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
                        CustomUtility.hideProgressDialog(BlueToothDebugNewActivity.this);
                        e1.printStackTrace();
                    }
                    for (int i = 0; i < 12; i++) {
                        try {
                            bytesRead = iStream.read();
                        } catch (IOException e) {
                            System.out.println("vikas--2==>2");
                            CustomUtility.hideProgressDialog(BlueToothDebugNewActivity.this);
                            e.printStackTrace();
                        }
                    }
                    int[] bytesReaded;
                    while (true) {
                        bytesReaded = new int[mLengthCount];
                        for (int i = 0; i < mLengthCount; i++) {
                            int mCharOne = 0;
                            int mCharTwo = 0;
                            try {
                                mCharOne = iStream.read();
                                mCharTwo = iStream.read();
                            } catch (IOException e) {
                                CustomUtility.hideProgressDialog(BlueToothDebugNewActivity.this);
                                e.printStackTrace();
                            }
                            try {
                                System.out.println("vikas--3==>" + mCharOne + "" + mCharTwo);
                                if ("TX".equalsIgnoreCase((char) mCharOne + "" + (char) mCharTwo)) {
                                    isDataExtract = true;
                                    CustomUtility.hideProgressDialog(BlueToothDebugNewActivity.this);
                                    Message message = new Message();
                                    message.obj = "Data Extraction Completed!";
                                    mHandler.sendMessage(message);
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
                            if (sheet1 == null) {
                                wb = new HSSFWorkbook();
                                wb.createSheet(ControllerSerialNumber + ".xls");
                            }
                            try {
                                FileOutputStream os = new FileOutputStream(dirName);
                                wb.write(os);
                                os.close();
                                Log.w("FileUtils", "Writing file" + dirName);

                            } catch (IOException e) {
                                Log.w("FileUtils", "Error writing " + dirName, e);
                            } catch (Exception e) {
                                Log.w("FileUtils", "Failed to save file", e);

                            }
                            break;
                        }
                        //  if((mDay == 255) && (mMonth == 255) && (mYear == 255) && (mHour == 255) && (mMinut == 255) && (mStatus == 255))
                        if (((mDay == 255) && (mMonth == 255) && (mYear == 255)) || ((mDay == 0) && (mMonth == 0) && (mYear == 0))) {

                            if (sheet1 == null) {
                                wb = new HSSFWorkbook();
                                wb.createSheet(ControllerSerialNumber + ".xls");
                            }
                            try {
                                FileOutputStream os = new FileOutputStream(dirName);
                                wb.write(os);
                                os.close();
                                Log.w("FileUtils", "Writing file" + dirName);

                            } catch (IOException e) {
                                Log.w("FileUtils", "Error writing " + dirName, e);
                            } catch (Exception e) {
                                Log.w("FileUtils", "Failed to save file", e);

                            }


                        } else {
                            if (mPostionFinal == 0) {
                                //New Workbook
                                wb = new HSSFWorkbook();

                                sheet1 = wb.createSheet(ControllerSerialNumber + "_" + Calendar.getInstance().getTimeInMillis() + ".xls");
                                row = sheet1.createRow(0);

                                for (int k = 0; k < mMonthHeaderList.size(); k++) {

                                    String[] mStringSplitStart = mMonthHeaderList.get(k).split("-");

                                    sheet1.setColumnWidth(k, (10 * 200));
                                    cell = row.createCell(k);
                                    //cell.setCellValue(mMonthHeaderList.get(k));
                                    cell.setCellValue(mStringSplitStart[0]);


                                }

                                row = sheet1.createRow(mPostionFinal + 1);

                                cell = row.createCell(0);
                                cell.setCellValue("" + mDay);


                                cell = row.createCell(1);
                                cell.setCellValue("" + mMonth);


                                cell = row.createCell(2);
                                cell.setCellValue("" + mYear);


                                cell = row.createCell(3);
                                cell.setCellValue("" + mHour);


                                cell = row.createCell(4);
                                cell.setCellValue("" + mMinut);


                                cell = row.createCell(5);
                                cell.setCellValue("" + mStatus);


                                try {
                                    for (int j = 6; j < mMonthHeaderList.size(); j++) {


                                        String[] mStringSplitStart = mMonthHeaderList.get(j).split("-");
                                        int mmIntt = 1;
                                        Log.e("mStringSplitStart===>", Arrays.toString(mStringSplitStart));


                                        try {
                                            mmIntt = Integer.parseInt(mStringSplitStart[1]);
                                        } catch (Exception e) {
                                            mmIntt = 10;
                                        }

                                        try {

                                            if (mmIntt == 1) {


                                                sheet1.setColumnWidth(j, (10 * 200));
                                                fFrequency = mTotalTime[j];

                                                cell = row.createCell(j);
                                                cell.setCellValue("" + fFrequency);

                                            } else {


                                                sheet1.setColumnWidth(j, (10 * 200));
                                                fFrequency = mTotalTime[j];

                                                float mmValue = (((float) mTotalTime[j]) / ((float) mmIntt));

                                                cell = row.createCell(j);

                                                cell.setCellValue("" + mmValue);
                                                Log.e("CellValue2222==========>", fFrequency + "=========>" + String.valueOf(mmValue));
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();

                                }

                            } else {

                                row = sheet1.createRow(mPostionFinal + 1);

                                cell = row.createCell(0);
                                cell.setCellValue("" + mDay);


                                cell = row.createCell(1);
                                cell.setCellValue("" + mMonth);


                                cell = row.createCell(2);
                                cell.setCellValue("" + mYear);


                                cell = row.createCell(3);
                                cell.setCellValue("" + mHour);


                                cell = row.createCell(4);
                                cell.setCellValue("" + mMinut);


                                cell = row.createCell(5);
                                cell.setCellValue("" + mStatus);


                                try {
                                    for (int j = 6; j < mMonthHeaderList.size(); j++) {


                                        String[] mStringSplitStart = mMonthHeaderList.get(j).split("-");
                                        int mmIntt = 1;
                                        try {
                                            mmIntt = Integer.parseInt(mStringSplitStart[1]);
                                        } catch (Exception e) {
                                            mmIntt = 10;
                                        }

                                        try {

                                            if (mmIntt == 1) {

                                                if (j <= mMonthHeaderList.size()) {
                                                    sheet1.setColumnWidth(j, (10 * 200));
                                                    fFrequency = mTotalTime[j];

                                                    cell = row.createCell(j);
                                                    cell.setCellValue("" + fFrequency);

                                                }
                                            } else {


                                                sheet1.setColumnWidth(j, (10 * 200));
                                                fFrequency = mTotalTime[j];

                                                float mmValue = (((float) mTotalTime[j]) / ((float) mmIntt));

                                                cell = row.createCell(j);
                                                cell.setCellValue("" + mmValue);


                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
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
                CustomUtility.hideProgressDialog(BlueToothDebugNewActivity.this);
            }
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
        }
    }

    public String getMediaFilePath(String type, String name) {

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ShaktiKusumExtractionFile");

        File directory = new File(root.getAbsolutePath() + type); //it is my root directory

        try {
            if (!directory.exists()) {
                directory.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a media file name
        return directory.getPath() + File.separator + name;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }

        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}

