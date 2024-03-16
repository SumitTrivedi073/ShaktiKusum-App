package activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static java.lang.Thread.sleep;
import static utility.FileUtils.getPath;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import adapter.ImageSelectionAdapter;
import adapter.PairedDeviceAdapter;
import bean.DeviceDetailModel;
import bean.DeviceInformationModel;
import bean.DeviceMappingModel;
import bean.DeviceShiftingModel;
import bean.ImageModel;
import bean.PairDeviceModel;
import database.DatabaseHelper;
import debugapp.GlobalValue.AllPopupUtil;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class DeviceMappingActivity extends AppCompatActivity implements View.OnClickListener, ImageSelectionAdapter.ImageSelectionListener, PairedDeviceAdapter.deviceSelectionListener {

    public static UUID my_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<String> itemNameList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();

    RecyclerView recyclerview;
    LinearLayout deviceOnlineLinear, deviceOfflineLinear, deviceStatusLinear;
    CardView retrieveDeviceInfoCardOnline, retrieveDeviceInfoCardOffline;
    TextView write_btn, read_btn, updateDeviceBtn, countDownTimerTxt, countDownTimerTxt1, checkDeviceStatusBtn, checkDeviceStatusBtn1,
            update4GDeviceBtn, btnSave, retrieveDeviceInfoOnline, retrieveDeviceInfoOffline, retrieveDeviceInfoTxtOffline,
            retrieveDeviceInfoTxtOnline, btnSave4G, deviceShiftingStatusTxt;
    EditText remarkExt;
    ImageView writeImg, read_img;
    CountDownTimer timer;
    Toolbar mToolbar;

    DatabaseHelper databaseHelper;
    DeviceShiftingModel.Response deviceShiftingData;

    BluetoothSocket bluetoothSocket;
    private InputStream iStream = null;
    String DeviceInfo = "", DEVICE_NO = "", DONGLE_FIRM_VER = "", DEVICE_FIRM_VER = "", DONGLE_APN = "",
            DONGLE_MODE = "", DONGLE_CONNECTIVITY = "", DONGLE_MQTT1_IP = "", DONGLE_MQTT2_IP = "", DONGLE_D_FOTA = "", TCP_IP = "",
            kkkkkk1 = "", bluetoothDeviceAddress = "", billNo = "", beneficiaryNo = "", contactNo = "", hp = "", regisNo = "",
            controllerSerialNo = "7F-0135-0-13-06-23-0", customerName = "", customerMobile = "", latitude = "", longitude = "";

    int selectedIndex, countDownTimer2G = 900000, countDownTimer4G = 10000;
    boolean isImageUpdate = false, isDeviceOnline = false, is2Gdevice, is4Gupdate = false, isDeviceInformationAvailable = false;
    AlertDialog alertDialog;

    ProgressDialog progressDialog;
    ImageSelectionAdapter customAdapter;

    List<DeviceMappingModel> deviceMappingList = new ArrayList<>();
    List<DeviceMappingModel> deviceMappingData = new ArrayList<>();
    ArrayList<PairDeviceModel> pairedDevicesList = new ArrayList<>();

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    PairedDeviceAdapter pairedDeviceAdapter;
    DeviceInformationModel deviceInformationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_mapping);
        inIt();
        listner();
        retrieveValue();
    }


    @Override
    protected void onResume() {
        super.onResume();
        CheakPermissions();

    }

    private void inIt() {
        progressDialog = new ProgressDialog(this);
        databaseHelper = new DatabaseHelper(this);
        mToolbar = findViewById(R.id.toolbar);

        write_btn = findViewById(R.id.write_btn);
        writeImg = findViewById(R.id.writeImg);
        read_btn = findViewById(R.id.read_btn);
        read_img = findViewById(R.id.read_img);
        updateDeviceBtn = findViewById(R.id.updateDeviceBtn);
        countDownTimerTxt = findViewById(R.id.countDownTimerTxt);
        countDownTimerTxt1 = findViewById(R.id.countDownTimerTxt1);
        checkDeviceStatusBtn = findViewById(R.id.checkDeviceStatusBtn);
        checkDeviceStatusBtn1 = findViewById(R.id.checkDeviceStatusBtn1);
        update4GDeviceBtn = findViewById(R.id.update4GDeviceBtn);
        recyclerview = findViewById(R.id.recycler_view);
        deviceOnlineLinear = findViewById(R.id.deviceOnlineLinear);
        deviceOfflineLinear = findViewById(R.id.deviceOfflineLinear);
        deviceStatusLinear = findViewById(R.id.deviceStatusLinear);
        remarkExt = findViewById(R.id.RemarkExt);
        btnSave = findViewById(R.id.btnSave);
        btnSave4G = findViewById(R.id.btnSave4G);
        retrieveDeviceInfoCardOnline = findViewById(R.id.retrieveDeviceInfoCardOnline);
        retrieveDeviceInfoCardOffline = findViewById(R.id.retrieveDeviceInfoCardOffline);
        retrieveDeviceInfoOnline = findViewById(R.id.retrieveDeviceInfoOnline);
        retrieveDeviceInfoOffline = findViewById(R.id.retrieveDeviceInfoOffline);
        retrieveDeviceInfoTxtOffline = findViewById(R.id.deviceInformationTxtOffline);
        retrieveDeviceInfoTxtOnline = findViewById(R.id.deviceInformationTxtOnline);
        deviceShiftingStatusTxt = findViewById(R.id.deviceShiftingStatusTxt);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.pendingInstallationVerification));

    }

    private void listner() {
        write_btn.setOnClickListener(this);
        read_btn.setOnClickListener(this);
        updateDeviceBtn.setOnClickListener(this);
        countDownTimerTxt.setOnClickListener(this);
        countDownTimerTxt1.setOnClickListener(this);
        checkDeviceStatusBtn.setOnClickListener(this);
        checkDeviceStatusBtn1.setOnClickListener(this);
        update4GDeviceBtn.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnSave4G.setOnClickListener(this);
        retrieveDeviceInfoOnline.setOnClickListener(this);
        retrieveDeviceInfoOffline.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {
            deviceShiftingData = (DeviceShiftingModel.Response) getIntent().getSerializableExtra(Constant.deviceMappingData);

            billNo = deviceShiftingData.getVbeln();
            beneficiaryNo = deviceShiftingData.getBeneficiary();
            contactNo = deviceShiftingData.getContactNo();
            hp = deviceShiftingData.getHp();
            regisNo = deviceShiftingData.getRegisno();
            // controllerSerialNo = deviceShiftingData.getControllerSernr() + "-0";
            customerName = deviceShiftingData.getCustomerName();
            customerMobile = deviceShiftingData.getContactNo();


            String dongleType = deviceShiftingData.getDongle().charAt(0) + deviceShiftingData.getDongle().substring(1, 2);
            Log.e("dongleType=====>", dongleType);

            if (dongleType.equals("99") || dongleType.equals("28")) {
                is2Gdevice = true;
                retrieveDeviceInfoCardOnline.setVisibility(View.GONE);
                retrieveDeviceInfoCardOffline.setVisibility(View.GONE);
            } else {
                is2Gdevice = false;
                if (!deviceShiftingData.getLatlng().isEmpty()) {
                    String[] latlong = deviceShiftingData.getLatlng().split(",");
                    latitude = String.valueOf(Double.parseDouble(latlong[0]));
                    longitude = String.valueOf(Double.parseDouble(latlong[1]));
                }

            }


            if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                getDeviceOnlineStatus();
            } else {
                SetAdapter();
                CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.check_internet_connection));
            }

        }

    }

    private void retrieveDeviceInformation() {
        String deviceInfo = "";
        Log.e("billNo=====>", billNo + "=======>controllerSerialNo========>" + controllerSerialNo);
        deviceInformationModel = databaseHelper.getDeviceInformation(billNo, "7F-0135-0-13-06-23-0");
        Log.e("deviceInformationModel======>", deviceInformationModel.toString());
        if (deviceInformationModel.getDongleFirmVersion() != null && !deviceInformationModel.getDongleFirmVersion().isEmpty()) {
            isDeviceInformationAvailable = true;
            if (deviceInformationModel.getDongleFirmVersion().equals("4.05")
                    || deviceInformationModel.getDongleFirmVersion().equals("4.06")
                    || deviceInformationModel.getDongleFirmVersion().equals("4.07")) {

                deviceInfo = "DEVICE_NO:-" + deviceInformationModel.getDeviceNo() +
                        "\nDONGLE_FIRM_VER:-" + deviceInformationModel.getDongleFirmVersion() +
                        "\nDEVICE_FIRM_VER:-" + deviceInformationModel.getDeviceFirmVersion() +
                        "\nDONGLE_APN:-" + deviceInformationModel.getDongleAPN() +
                        "\nDONGLE_MODE:-" + deviceInformationModel.getDongleMode() +
                        "\nDONGLE_MQTT1_IP:-" + deviceInformationModel.getDongleMqttIp1() +
                        "\nDONGLE_MQTT2_IP:-" + deviceInformationModel.getDongleMqttIp2()
                        + "\nTCP_IP:-" + deviceInformationModel.getTcpIP();

                // Log.e("deviceInfo=====>", deviceInfo);
            } else if (deviceInformationModel.getDongleFirmVersion().equals("4.08")) {
                deviceInfo = "DEVICE_NO:-" + deviceInformationModel.getDeviceNo() +
                        "\nDONGLE_FIRM_VER:-" + deviceInformationModel.getDongleFirmVersion() +
                        "\nDEVICE_FIRM_VER:-" + deviceInformationModel.getDeviceFirmVersion() +
                        "\nDONGLE_APN:-" + deviceInformationModel.getDongleAPN() +
                        "\nDONGLE_MODE:-" + deviceInformationModel.getDongleMode() +
                        "\nDONGLE_CONNECTIVITY:-" + deviceInformationModel.getDongleConnectivity() +
                        "\nDONGLE_MQTT1_IP:-" + deviceInformationModel.getDongleMqttIp1() +
                        "\nDONGLE_MQTT2_IP:-" + deviceInformationModel.getDongleMqttIp2()
                        + "\nDONGLE_D_FOTA:-" + deviceInformationModel.getdFota();


            }

            DEVICE_NO = deviceInformationModel.getDeviceNo();
            DONGLE_FIRM_VER = deviceInformationModel.getDongleFirmVersion();
            DEVICE_FIRM_VER = deviceInformationModel.getDeviceFirmVersion();
            DONGLE_APN = deviceInformationModel.getDongleAPN();
            DONGLE_MODE = deviceInformationModel.getDongleMode();
            DONGLE_CONNECTIVITY = deviceInformationModel.getDongleConnectivity();
            DONGLE_MQTT1_IP = deviceInformationModel.getDongleMqttIp1();
            DONGLE_MQTT2_IP = deviceInformationModel.getDongleMqttIp2();
            if (deviceInformationModel.getdFota() != null) {
                DONGLE_D_FOTA = deviceInformationModel.getdFota();
            }
            if (deviceInformationModel.getTcpIP() != null) {
                TCP_IP = deviceInformationModel.getTcpIP();
            }
            Log.e("deviceInfo=====>", deviceInfo);
            if (isDeviceOnline) {
                retrieveDeviceInfoTxtOnline.setText(deviceInfo);
                btnSave4G.setEnabled(true);
                btnSave4G.setAlpha(1f);
            } else {
                retrieveDeviceInfoTxtOffline.setText(deviceInfo);
                remarkExt.setText(deviceInformationModel.getRemarkTxt());
                btnSave.setEnabled(true);
                btnSave.setAlpha(1f);
            }

        }
    }


    private void startCountDownTimer(int millisecond, TextView countDownTimer) {
        countDownTimer.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(millisecond, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                if (millisecond == countDownTimer2G) {
                    countDownTimer.setText("Please Wait \n" + f.format(min) + " Min. " + f.format(sec) + " Sec. \nDevice Installing latest vesion ");
                } else {
                    countDownTimer.setText(f.format(min) + " Min. " + f.format(sec) + " Sec. ");

                }
            }

            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                countDownTimer.setVisibility(View.GONE);
                if (millisecond == countDownTimer2G) {
                    changeButtonVisibility("4");
                } else {
                    checkDeviceStatusBtn1.setEnabled(true);
                    checkDeviceStatusBtn1.setAlpha(1f);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_btn:
                if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                    write_read_fotaAPI("254", "1", "1.0", "0", "1");
                } else {
                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.check_internet_connection));
                }
                break;
            case R.id.read_btn:
                if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                    write_read_fotaAPI("254", "0", "0.0", "0.0", "2");
                } else {
                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.check_internet_connection));
                }
                break;
            case R.id.updateDeviceBtn:
                if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                    write_read_fotaAPI("255", "1", "1", "0.0", "3");
                } else {
                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.check_internet_connection));
                }
                break;
            case R.id.checkDeviceStatusBtn:
            case R.id.checkDeviceStatusBtn1:
                if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                    checkDeviceShiftingStatusAPI(true);
                } else {
                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.check_internet_connection));
                }

                break;

            case R.id.update4GDeviceBtn:
                if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                    if(!latitude.isEmpty()||!longitude.isEmpty()) {
                        sendLatLngToRmsForFota();
                    }else {
                        CustomUtility.showToast(this,getResources().getString(R.string.empty_latlng));
                    }
                } else {
                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.check_internet_connection));
                }

                break;
            case R.id.btnSave:
                    if (!imageArrayList.get(0).isImageSelected()) {
                        CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.select_image));
                    } else if (remarkExt.getText().toString().trim().isEmpty()) {
                        CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.enter_remark));
                    } else if (DEVICE_NO.isEmpty()) {
                        getResources().getString(R.string.retriveDeviceInfo);
                    } else if (DONGLE_FIRM_VER.isEmpty()) {
                        getResources().getString(R.string.retriveDeviceInfo);
                    } else {
                        DeviceInformationModel deviceInformationModel = new DeviceInformationModel();
                        deviceInformationModel.setDeviceNo(DEVICE_NO);
                        deviceInformationModel.setDeviceFirmVersion(DEVICE_FIRM_VER);
                        deviceInformationModel.setDongleFirmVersion(DONGLE_FIRM_VER);
                        deviceInformationModel.setDongleAPN(DONGLE_APN);
                        deviceInformationModel.setDongleMode(DONGLE_MODE);
                        deviceInformationModel.setDongleConnectivity(DONGLE_CONNECTIVITY);
                        deviceInformationModel.setDongleMqttIp1(DONGLE_MQTT1_IP);
                        deviceInformationModel.setDongleMqttIp2(DONGLE_MQTT2_IP);
                        deviceInformationModel.setdFota(DONGLE_D_FOTA);
                        deviceInformationModel.setTcpIP(TCP_IP);
                        deviceInformationModel.setBillNo(billNo);
                        deviceInformationModel.setRemarkTxt(remarkExt.getText().toString().trim());
                        if (!isDeviceInformationAvailable) {
                            databaseHelper.insertDeviceInformation(deviceInformationModel);
                        } else {
                            databaseHelper.updateDeviceInformation(deviceInformationModel);
                        }
                        if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                            submitOfflineDeviceData();
                        } else {
                            CustomUtility.showToast(this, getResources().getString(R.string.data_save_in_local));
                            onBackPressed();
                          //  CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.check_internet_connection));
                        }
                    }

                break;
            case R.id.retrieveDeviceInfoOnline:
            case R.id.retrieveDeviceInfoOffline:

                openBluetoothPairScreen();

                break;

            case R.id.btnSave4G:
                if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                    saveShiftingStatusToSap(getResources().getString(R.string.online_and_shifted));
                } else {
                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.check_internet_connection));
                }

                break;

        }
    }

    private void changeButtonVisibility(String buttonId) {

        switch (buttonId) {

            case "0":
                write_btn.setEnabled(true);
                write_btn.setAlpha(1f);
                read_btn.setEnabled(false);
                read_btn.setAlpha(0.5f);
                updateDeviceBtn.setEnabled(false);
                updateDeviceBtn.setAlpha(0.5f);
                checkDeviceStatusBtn.setEnabled(false);
                checkDeviceStatusBtn.setAlpha(0.5f);

                break;
            case "1":
                write_btn.setEnabled(false);
                write_btn.setAlpha(0.5f);
                read_btn.setEnabled(true);
                read_btn.setAlpha(1f);
                updateDeviceBtn.setEnabled(false);
                updateDeviceBtn.setAlpha(0.5f);
                checkDeviceStatusBtn.setEnabled(false);
                checkDeviceStatusBtn.setAlpha(0.5f);
                writeImg.setImageResource(R.drawable.right_mark_icn_green);
                DeviceMappingModel deviceMappingModel = new DeviceMappingModel();
                deviceMappingModel.setRead("false");
                deviceMappingModel.setWrite("true");
                deviceMappingModel.setUpdate("false");
                deviceMappingModel.setUpdate4G("false");
                deviceMappingModel.setBillNo(billNo);
                insUpdateData(deviceMappingModel, false);

                break;

            case "2":
                write_btn.setEnabled(false);
                write_btn.setAlpha(0.5f);
                read_btn.setEnabled(false);
                read_btn.setAlpha(0.5f);
                updateDeviceBtn.setEnabled(true);
                updateDeviceBtn.setAlpha(1f);
                checkDeviceStatusBtn.setEnabled(false);
                checkDeviceStatusBtn.setAlpha(0.5f);
                read_img.setImageResource(R.drawable.right_mark_icn_green);
                DeviceMappingModel deviceMappingModel1 = new DeviceMappingModel();
                deviceMappingModel1.setRead("true");
                deviceMappingModel1.setWrite("true");
                deviceMappingModel1.setUpdate("false");
                deviceMappingModel1.setUpdate4G("false");
                deviceMappingModel1.setBillNo(billNo);
                insUpdateData(deviceMappingModel1, true);

                break;

            case "3":
                write_btn.setEnabled(false);
                write_btn.setAlpha(0.5f);
                read_btn.setEnabled(false);
                read_btn.setAlpha(0.5f);
                updateDeviceBtn.setEnabled(false);
                updateDeviceBtn.setAlpha(0.5f);
                checkDeviceStatusBtn.setEnabled(true);
                checkDeviceStatusBtn.setAlpha(1f);
                DeviceMappingModel deviceMappingModel2 = new DeviceMappingModel();
                deviceMappingModel2.setRead("true");
                deviceMappingModel2.setWrite("true");
                deviceMappingModel2.setUpdate("true");
                deviceMappingModel2.setUpdate4G("false");
                deviceMappingModel2.setBillNo(billNo);
                insUpdateData(deviceMappingModel2, true);

                startCountDownTimer(countDownTimer2G, countDownTimerTxt);
                break;

            case "4":
                write_btn.setEnabled(false);
                write_btn.setAlpha(0.5f);
                read_btn.setEnabled(false);
                read_btn.setAlpha(0.5f);
                updateDeviceBtn.setEnabled(false);
                updateDeviceBtn.setAlpha(0.5f);
                checkDeviceStatusBtn.setEnabled(true);
                checkDeviceStatusBtn.setAlpha(1f);

                break;
            case "5":
                update4GDeviceBtn.setEnabled(true);
                update4GDeviceBtn.setAlpha(1f);
                checkDeviceStatusBtn1.setEnabled(false);
                checkDeviceStatusBtn1.setAlpha(0.5f);
                retrieveDeviceInfoOnline.setEnabled(false);
                retrieveDeviceInfoOnline.setAlpha(0.5f);
                btnSave4G.setEnabled(false);
                btnSave4G.setAlpha(0.5f);
                break;
            case "6":
                update4GDeviceBtn.setEnabled(false);
                update4GDeviceBtn.setAlpha(0.5f);
                checkDeviceStatusBtn1.setEnabled(true);
                checkDeviceStatusBtn1.setAlpha(1f);
                retrieveDeviceInfoOnline.setEnabled(false);
                retrieveDeviceInfoOnline.setAlpha(0.5f);
                btnSave4G.setEnabled(false);
                btnSave4G.setAlpha(0.5f);
                DeviceMappingModel deviceMappingModel3 = new DeviceMappingModel();
                deviceMappingModel3.setRead("false");
                deviceMappingModel3.setWrite("false");
                deviceMappingModel3.setUpdate("false");
                deviceMappingModel3.setUpdate4G("true");
                deviceMappingModel3.setBillNo(billNo);
                insUpdateData(deviceMappingModel3, is4Gupdate);
                startCountDownTimer(countDownTimer4G, countDownTimerTxt1);

                break;

            case "7":
                update4GDeviceBtn.setEnabled(false);
                update4GDeviceBtn.setAlpha(0.5f);
                checkDeviceStatusBtn1.setEnabled(false);
                checkDeviceStatusBtn1.setAlpha(0.5f);
                retrieveDeviceInfoOnline.setEnabled(true);
                retrieveDeviceInfoOnline.setAlpha(1f);
                deviceOnlineLinear.setVisibility(View.GONE);
                deviceOfflineLinear.setVisibility(View.GONE);
                deviceStatusLinear.setVisibility(View.VISIBLE);
                retrieveDeviceInfoCardOffline.setVisibility(View.GONE);
                retrieveDeviceInfoCardOnline.setVisibility(View.VISIBLE);
                deviceShiftingStatusTxt.setVisibility(View.GONE);
                countDownTimerTxt1.setVisibility(View.GONE);
                retrieveDeviceInformation();
                break;

            case "8":
                deviceOfflineLinear.setVisibility(View.VISIBLE);
                deviceOnlineLinear.setVisibility(View.GONE);
                deviceStatusLinear.setVisibility(View.GONE);
                btnSave.setEnabled(false);
                btnSave.setAlpha(0.5f);
                retrieveDeviceInformation();
                break;
        }
    }

    private void insUpdateData(DeviceMappingModel deviceMappingModel, boolean isImageUpdate) {
        if (isImageUpdate) {
            databaseHelper.updateDeviceMappingData(deviceMappingModel);
        } else {
            databaseHelper.insertDeviceMappingData(deviceMappingModel);
        }
    }


    /*-------------------------------------------------------------Check Permission-----------------------------------------------------------------------------*/


    private void CheakPermissions() {
        if (!checkPermission()) {
            requestPermission();
        }

    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO},
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);

        }
    }

    private boolean checkPermission() {
        int cameraPermission =
                ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ReadMediaImages =
                ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES);
        int ReadAudioImages =
                ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_AUDIO);
        int writeExternalStorage =
                ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ReadExternalStorage =
                ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && ReadMediaImages == PackageManager.PERMISSION_GRANTED
                    && ReadAudioImages == PackageManager.PERMISSION_GRANTED;
        } else {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED
                    && ReadExternalStorage == PackageManager.PERMISSION_GRANTED;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_CODE_PERMISSION:

                if (grantResults.length > 0) {
                    if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadMediaImages = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadAudioImages = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (!ACCESSCAMERA && !ReadMediaImages && !ReadAudioImages) {
                            Toast.makeText(DeviceMappingActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean writeExternalStorage =
                                grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadExternalStorage =
                                grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (!ACCESSCAMERA && !writeExternalStorage && !ReadExternalStorage) {
                            Toast.makeText(DeviceMappingActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }

                    }
                }

                break;
        }
    }

    /*-------------------------------------------------------------Pick Image and set in recycler view-----------------------------------------------------------------------------*/


    private void SetAdapter() {
        isDeviceOnline = false;
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.please_attach_controller_image));

        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo("");
            imageArrayList.add(imageModel);
        }

        imageList = databaseHelper.getAllOfflineControllerImages();


        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {


            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {
                    if (imageList.get(i).getBillNo().trim().equals(billNo)) {
                        if (imageList.get(i).getName().equals(itemNameList.get(j))) {
                            ImageModel imageModel = new ImageModel();
                            imageModel.setName(imageList.get(i).getName());
                            imageModel.setImagePath(imageList.get(i).getImagePath());
                            imageModel.setImageSelected(true);
                            imageModel.setBillNo(imageList.get(i).getBillNo());
                            imageArrayList.set(j, imageModel);
                        }
                    }
                }
            }
        }

        customAdapter = new ImageSelectionAdapter(DeviceMappingActivity.this, imageArrayList, false);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);
        changeButtonVisibility("8");


    }

    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;
        if (imageModelList.isImageSelected()) {
            isImageUpdate = true;
            selectImage("1");
        } else {
            isImageUpdate = false;
            selectImage("0");
        }
    }

    private void selectImage(String value) {
        LayoutInflater inflater = (LayoutInflater) DeviceMappingActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(DeviceMappingActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

        TextView title = layout.findViewById(R.id.titleTxt);
        TextView gallery = layout.findViewById(R.id.gallery);
        TextView gamera = layout.findViewById(R.id.camera);
        TextView cancel = layout.findViewById(R.id.cancel);

        if (value.equals("0")) {
            //gallery.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.select_image));
            gallery.setText(getResources().getString(R.string.gallery));
            gamera.setText(getResources().getString(R.string.camera));
        } else {
            title.setText(getResources().getString(R.string.want_to_perform));
            gallery.setText(getResources().getString(R.string.display));
            gamera.setText(getResources().getString(R.string.change));
        }

        gallery.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (value.equals("0")) {
                galleryIntent();
            } else {
                Intent i_display_image = new Intent(DeviceMappingActivity.this, PhotoViewerActivity.class);
                i_display_image.putExtra("image_path", imageArrayList.get(selectedIndex).getImagePath());
                startActivity(i_display_image);
            }
        });

        gamera.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (value.equals("0")) {
                cameraIntent();
            } else {
                selectImage("0");
            }
        });

        cancel.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_FILE);
    }

    private void cameraIntent() {

        camraLauncher.launch(new Intent(DeviceMappingActivity.this, CameraActivity2.class)
                .putExtra("cust_name", customerName));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null && result.getData().getExtras() != null) {

                        Bundle bundle = result.getData().getExtras();
                        Log.e("bundle====>", bundle.get("data").toString());
                        UpdateArrayList(bundle.get("data").toString());

                    }

                }
            });


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_FROM_FILE) {
            try {
                Uri mImageCaptureUri = data.getData();
                String path = getPath(DeviceMappingActivity.this, mImageCaptureUri); // From Gallery
                if (path == null) {
                    path = mImageCaptureUri.getPath(); // From File Manager
                }
                Log.e("Activity", "PathHolder22= " + path);
                String filename = path.substring(path.lastIndexOf("/") + 1);
                String file;
                if (filename.indexOf(".") > 0) {
                    file = filename.substring(0, filename.lastIndexOf("."));
                } else {
                    file = "";
                }
                if (TextUtils.isEmpty(file)) {
                    Toast.makeText(DeviceMappingActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                } else {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);
                    File file1 = CustomUtility.saveFile(bitmap, customerName.trim(), "Images");

                    UpdateArrayList(file1.getPath());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void UpdateArrayList(String path) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageModel.setBillNo(billNo);
        imageArrayList.set(selectedIndex, imageModel);
        customAdapter.notifyItemChanged(selectedIndex);

        if (isImageUpdate) {
            databaseHelper.updateOfflineControllerImage(imageModel, true);
        } else {
            databaseHelper.insertOfflineControllerImage(imageModel, true);
        }

    }



    /*-------------------------------------------------------------Device Online Status Check API-----------------------------------------------------------------------------*/

    public void getDeviceOnlineStatus() {
        CustomUtility.showProgressDialogue(DeviceMappingActivity.this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("URL===>", CustomUtility.getSharedPreferences(getApplicationContext(), Constant.RmsBaseUrl) + WebURL.DEVICE_DETAILS + "?DeviceNo=" + controllerSerialNo);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                CustomUtility.getSharedPreferences(getApplicationContext(), Constant.RmsBaseUrl) + WebURL.DEVICE_DETAILS + "?DeviceNo=" + controllerSerialNo, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(DeviceMappingActivity.this);

                if (!response.toString().isEmpty()) {
                    Log.e("response======>", response.toString());
                    DeviceDetailModel deviceDetailModel = new Gson().fromJson(response.toString(), DeviceDetailModel.class);
                    if (deviceDetailModel != null && deviceDetailModel.getResponse() != null && String.valueOf(deviceDetailModel.getStatus()).equals("true")) {

                        if (deviceDetailModel.getResponse().getIsLogin()) {
                            isDeviceOnline = true;
                            checkDeviceShiftingStatusAPI(false);

                        } else {
                            SetAdapter();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(DeviceMappingActivity.this);
                if (error.getMessage() != null && !error.getMessage().isEmpty()) {
                    CustomUtility.ShowToast(error.getMessage(), DeviceMappingActivity.this);

                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void setDeviceData() {
        deviceMappingList = new ArrayList<>();
        deviceMappingData = new ArrayList<>();
        deviceMappingData = databaseHelper.getAllDeviceMappingData();

        Log.e("deviceMappingData======>", deviceMappingData.toString());
        for (int i = 0; i < deviceMappingData.size(); i++) {
            if (deviceMappingData.get(i).getBillNo().trim().equals(billNo)) {
                DeviceMappingModel deviceMappingModel = new DeviceMappingModel();
                deviceMappingModel.setId(deviceMappingData.get(i).getId());
                deviceMappingModel.setRead(deviceMappingData.get(i).getRead());
                deviceMappingModel.setWrite(deviceMappingData.get(i).getWrite());
                deviceMappingModel.setUpdate(deviceMappingData.get(i).getUpdate());
                deviceMappingModel.setUpdate4G(deviceMappingData.get(i).getUpdate4G());
                deviceMappingModel.setBillNo(deviceMappingData.get(i).getBillNo());
                deviceMappingList.add(deviceMappingModel);
            }


        }
        if (is2Gdevice) {
            deviceOnlineLinear.setVisibility(View.VISIBLE);
            deviceOfflineLinear.setVisibility(View.GONE);
            deviceStatusLinear.setVisibility(View.GONE);

            if (deviceMappingList != null && deviceMappingList.size() > 0) {
                if (deviceMappingList.get(0).getWrite().equals("true")) {
                    changeButtonVisibility("1");
                }
                if (deviceMappingList.get(0).getRead().equals("true")) {
                    changeButtonVisibility("2");
                }

                if (deviceMappingList.get(0).getUpdate().equals("true")) {
                    changeButtonVisibility("3");
                }
            } else {
                changeButtonVisibility("0");

            }
        } else {
            deviceOnlineLinear.setVisibility(View.GONE);
            deviceOfflineLinear.setVisibility(View.GONE);
            deviceStatusLinear.setVisibility(View.VISIBLE);

            if (deviceMappingList != null && deviceMappingList.size() > 0) {
                if (deviceMappingList.get(0).getUpdate4G().equals("true")) {
                    is4Gupdate = true;
                    changeButtonVisibility("6");
                } else {
                    changeButtonVisibility("5");
                }
            } else {
                changeButtonVisibility("5");
            }
        }


    }


    /*-------------------------------------------------------------Offline Controller Data Send API-----------------------------------------------------------------------------*/

    private void submitOfflineDeviceData() {
        showProgressDialogue(getResources().getString(R.string.sendingDataServer));
        JSONArray ja_invc_data = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("SHIFTING_REMARK", remarkExt.getText().toString().trim());
            jsonObj.put("regisno", regisNo);
            jsonObj.put("project_no", CustomUtility.getSharedPreferences(getApplicationContext(), "projectid"));
            jsonObj.put("vbeln", billNo);


            if (imageArrayList.size() > 0) {
                if (imageArrayList.get(0).isImageSelected()) {
                    jsonObj.put("photo1", CustomUtility.getBase64FromBitmap(DeviceMappingActivity.this, imageArrayList.get(0).getImagePath()));
                }

            }
            ja_invc_data.put(jsonObj);
            Log.e("ja_invc_data=====>", ja_invc_data.toString());
            new SyncOfflineDeviceData(ja_invc_data).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class SyncOfflineDeviceData extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        JSONArray jsonArray;

        public SyncOfflineDeviceData(JSONArray jaInvcData) {
            jsonArray = jaInvcData;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String obj2 = null;
            Log.e("Param====>", jsonArray.toString());
            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("shifting", String.valueOf(jsonArray)));
            Log.e("DATA", "$$$$" + param1_invc);
            System.out.println("param1_invc_vihu==>>" + param1_invc);
            try {
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.SyncOfflineDeviceData, param1_invc);

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            String docno_sap = null;
            String invc_done = null;
            try {
                if (!result.isEmpty()) {
                    stopProgressDialogue();
                    Log.e("result=====>", result.trim());
                    stopProgressDialogue();
                    JSONObject object = new JSONObject(result);
                    String obj1 = object.getString("data_return");
                    JSONArray ja = new JSONArray(obj1);
                    Log.e("OUTPUT2", "&&&&" + ja);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");
                        if (invc_done.equalsIgnoreCase("Y")) {


                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));

                            runOnUiThread(() -> {
                                saveShiftingStatusToSap(getResources().getString(R.string.permanent_offline));

                            });

                        } else if (invc_done.equalsIgnoreCase("N")) {
                            showingMessage(getResources().getString(R.string.dataNotSubmitted));
                            stopProgressDialogue();


                        }
                    }
                } else {
                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.somethingWentWrong));
                    stopProgressDialogue();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

        }
    }


    /*-------------------------------------------------------------Write Data On Controller API-----------------------------------------------------------------------------*/

    public void write_read_fotaAPI(String address, String RW, String data1, String oldData, String value) {
        stopProgressDialogue();
        if (value.equals("1")) {
            showProgressDialogue(getResources().getString(R.string.writing_data_please_wait));
        } else if (value.equals("2")) {
            showProgressDialogue(getResources().getString(R.string.reading_data_please_wait));
        } else if (value.equals("3")) {
            showProgressDialogue(getResources().getString(R.string.updating_controller));
        }
        String deviceType = controllerSerialNo.charAt(0) + controllerSerialNo.substring(1, 2);

        Log.e("deviceMappingAPIS=====>", CustomUtility.getSharedPreferences(this, Constant.RmsBaseUrl) + WebURL.deviceMappingAPIS);
        RequestQueue queue = Volley.newRequestQueue(DeviceMappingActivity.this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CustomUtility.getSharedPreferences(this, Constant.RmsBaseUrl) + WebURL.deviceMappingAPIS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        stopProgressDialogue();
                        // array to JsonArray
                        Log.e("response=====>", response);
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            JSONObject jsonObj = jsonarray.getJSONObject(0);
                            Log.e("Result=====>", jsonObj.getString("Result"));
                            if (jsonObj.getString("Result").equals("2.0")) {
                                changeButtonVisibility(value);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
                stopProgressDialogue();
                CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.somethingWentWrong));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("address1", address);
                params.put("offset1", "0");
                params.put("NewGateway", "true");
                params.put("did1", controllerSerialNo);
                params.put("RW", RW);
                params.put("data1", data1);
                params.put("OldData", oldData);
                params.put("DeviceType", deviceType);
                params.put("UserId", "22");
                params.put("IPAddress", CustomUtility.getDeviceId(getApplicationContext()));

                Log.e("param=====>", params.toString());
                return params;
            }

        };
        queue.add(stringRequest);
// Adding request to request queue
        // AppController.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    /*-------------------------------------------------------------Device Shifting Status API-----------------------------------------------------------------------------*/

    private void checkDeviceShiftingStatusAPI(boolean isButtonClick) {
        stopProgressDialogue();
        showProgressDialogue(getResources().getString(R.string.checkDeviceShiftingStatus));
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Log.e("DeviceShiftingStatus====>", CustomUtility.getSharedPreferences(this, Constant.RmsBaseUrl) + WebURL.deviceShiftingStatusCheck + "?deviceno=" + controllerSerialNo);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                CustomUtility.getSharedPreferences(this, Constant.RmsBaseUrl) + WebURL.deviceShiftingStatusCheck + "?deviceno=" + controllerSerialNo,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                stopProgressDialogue();
                if (!res.toString().isEmpty()) {
                    Log.e("response======>", res.toString());
                    try {
                        if (res.getString("status").equals("true")) {
                            showingMessage(getResources().getString(R.string.device_shifting_successfully));
                            if (is2Gdevice) {
                                runOnUiThread(() -> {
                                    saveShiftingStatusToSap(getResources().getString(R.string.online_and_shifted));

                                });
                            } else {
                                ShowAlertResponse(getResources().getString(R.string.pairControllerRetrieveDeviceInformation), true);
                                changeButtonVisibility("7");

                            }
                        } else {
                            if (!isButtonClick) {
                                setDeviceData();
                            } else {
                                CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.latest_version_insta));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.somethingWentWrong));
                    }

                }

            }
        }, error -> {
            stopProgressDialogue();
            Log.e("error", String.valueOf(error));
           /* Toast.makeText(DeviceMappingActivity.this, error.getMessage(),
                    Toast.LENGTH_LONG).show();*/
        });
        requestQueue.add(jsonObjectRequest);
    }


    /*-------------------------------------------------------------Send OTP to customer-----------------------------------------------------------------------------*/

    private void saveShiftingStatusToSap(String shifting_remark) {
        stopProgressDialogue();
        showProgressDialogue(getResources().getString(R.string.sendingDataServer));
        JSONObject mainObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            if (is2Gdevice) {
                mainObject.put("vbeln", billNo);
                mainObject.put("SHIFTING_REMARK", shifting_remark);
            } else {
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
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonArray.put(mainObject);

        Log.e("DeviceShiftingAPI=====>",WebURL.saveShiftedDeviceToServer + jsonArray);

        CustomUtility.showProgressDialogue(DeviceMappingActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.saveShiftedDeviceToServer + jsonArray, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                stopProgressDialogue();
                Log.e("Response=====>",res.toString());
                if (!res.toString().isEmpty()) {

                    databaseHelper.deleteOfflineControllerImages(billNo);
                    databaseHelper.deleteDeviceMappingRecords(billNo);
                    databaseHelper.deleteDeviceInformationData(controllerSerialNo, billNo);
                    ShowAlertResponse(getResources().getString(R.string.device_shifting_successfully), false);

                }else {
                    CustomUtility.showToast(DeviceMappingActivity.this,getResources().getString(R.string.device_shifting_unsuccessfully));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialogue();
                Log.e("error", String.valueOf(error));
                Toast.makeText(DeviceMappingActivity.this, error.toString(),
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
        LayoutInflater inflater = (LayoutInflater) DeviceMappingActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DeviceMappingActivity.this, R.style.MyDialogTheme);

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
            if (!isScreenOpen) {
                Intent intent = new Intent(DeviceMappingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        });

    }


    /*---------------------------------------------------------------Open Bluetooth and show paired devices List---------------------------------------------------------------------------------*/
    private void openBluetoothPairScreen() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            if (AllPopupUtil.pairedDeviceListGloable(getApplicationContext())) {
                if (WebURL.BT_DEVICE_NAME.isEmpty() || WebURL.BT_DEVICE_MAC_ADDRESS.isEmpty()) {
                    showPairedDeviceListPopup();
                }
            } else {
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            }
        } else {
            startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
        }
    }

    private void showPairedDeviceListPopup() {
        LayoutInflater inflater = (LayoutInflater) DeviceMappingActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.show_paired_device_popup,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(DeviceMappingActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        RecyclerView bluetoothDeviceList = layout.findViewById(R.id.pairedDeviceList);

        pairedDevicesList = new ArrayList<>();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {

                for (BluetoothDevice device : pairedDevices) {
                    pairedDevicesList.add(new PairDeviceModel(device.getName(), device.getAddress(), false));
                }
                bluetoothDeviceList.setVisibility(View.VISIBLE);
                pairedDeviceAdapter = new PairedDeviceAdapter(DeviceMappingActivity.this, pairedDevicesList);
                bluetoothDeviceList.setAdapter(pairedDeviceAdapter);
                pairedDeviceAdapter.deviceSelection(this);

            }
        }
    }

    @Override
    public void DeviceSelectionListener(PairDeviceModel pairDeviceModel, int position) {

        Log.e("pairDeviceModel=========>", pairDeviceModel.getDeviceName());
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        bluetoothDeviceAddress = pairDeviceModel.getDeviceAddress();
        new BlueToothCommunicationForDeviceInfo4G().execute(":DEVICE INFO#", ":DEVICE INFO#", "OKAY");
    }

    /*-----------------------------------------------------------------------Retrieve Device Information Using Bluetooth---------------------------------------------------------------------------*/

    private class BlueToothCommunicationForDeviceInfo4G extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            my_UUID = UUID.fromString(my_UUID.toString());
            CustomUtility.showProgressDialogue(DeviceMappingActivity.this);
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
            Log.e("DONGLE_FIRM_VER=====>", DONGLE_FIRM_VER);
            if (DONGLE_FIRM_VER.equals("4.05") || DONGLE_FIRM_VER.equals("4.06") || DONGLE_FIRM_VER.equals("4.07")) {

                deviceInfo = "DEVICE_NO:-" + DEVICE_NO + "\nDONGLE_FIRM_VER:-" + DONGLE_FIRM_VER +
                        "\nDEVICE_FIRM_VER:-" + DEVICE_FIRM_VER +
                        "\nDONGLE_APN:-" + DONGLE_APN +
                        "\nDONGLE_MODE:-" + DONGLE_MODE +
                        "\nDONGLE_MQTT1_IP:-" + DONGLE_MQTT1_IP +
                        "\nDONGLE_MQTT2_IP:-" + DONGLE_MQTT2_IP
                        + "\nTCP_IP:-" + TCP_IP;

                Log.e("deviceInfo=====>", deviceInfo);
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

            if (isDeviceOnline) {
                retrieveDeviceInfoTxtOnline.setText(deviceInfo);
                btnSave4G.setEnabled(true);
                btnSave4G.setAlpha(1f);
            } else {
                retrieveDeviceInfoTxtOffline.setText(deviceInfo);
                btnSave.setEnabled(true);
                btnSave.setAlpha(1f);
            }

        }

    }


    private void connectToBluetoothSocket() {
        try {
            bluetoothSocket = null;
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothDeviceAddress);//connects to the device's address and checks if it's available

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


    /*-------------------------------------------------------------Send Lat Lng to Rms Server 4G device Fota-----------------------------------------------------------------------------*/
    private void sendLatLngToRmsForFota() {
        stopProgressDialogue();
        showProgressDialogue(getResources().getString(R.string.device_initialization_processing));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                CustomUtility.getSharedPreferences(this, Constant.RmsBaseUrl) + WebURL.updateLatLngToRms + "?deviceNo=" + controllerSerialNo + "&lat=" + latitude + "&lon=" + longitude,

                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.toString() != null && !jsonObject.toString().isEmpty()) {

                        String mStatus = jsonObject.getString("status");
                        if (mStatus.equals("true")) {
                            stopProgressDialogue();
                            changeButtonVisibility("6");
                        } else {
                            stopProgressDialogue();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    stopProgressDialogue();
                    CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), getApplicationContext());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialogue();
                Log.e("error", String.valueOf(error));
                Toast.makeText(DeviceMappingActivity.this, error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                5,  /// maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }


    /*-------------------------------------------------------------Show Progress Dialogue-----------------------------------------------------------------------------*/

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

    private void showingMessage(String message) {
        runOnUiThread(new Runnable() {
            public void run() {

                CustomUtility.showToast(DeviceMappingActivity.this, message);

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
        stopCountDownTImer();
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

    private void stopCountDownTImer() {
        if (timer != null) {
            timer.cancel();
        }
    }

}