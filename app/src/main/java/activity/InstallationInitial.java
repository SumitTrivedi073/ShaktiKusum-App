package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.shaktipumplimited.SetParameter.PairedDeviceActivity;
import com.shaktipumplimited.SettingModel.AllPopupUtil;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import bean.BTResonseData;
import bean.ImageModel;
import bean.InstallationBean;
import database.DatabaseHelper;
import de.hdodenhof.circleimageview.CircleImageView;
import debugapp.Bean.SimDetailsInfoResponse;
import debugapp.GlobalValue.Constant;
import debugapp.VerificationCodeModel;
import debugapp.localDB.DatabaseHelperTeacher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import utility.CustomUtility;
import utility.FileUtils;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class InstallationInitial extends BaseActivity {


    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";
    private DatabaseHelperTeacher mDatabaseHelperTeacher;
    List<SimDetailsInfoResponse> mSimDetailsInfoResponse;
    ArrayAdapter<String> dataAdapter_simoprator;
    ArrayAdapter<String> dataAdapter_conntype;
    List<String> list_simoprator = new ArrayList<>();
    List<String> list_conntype = new ArrayList<>();
    ArrayList<String> scannedDeviceNo = new ArrayList<>();
    List<BTResonseData> mBTResonseDataList;

    Context mContext;
    DatabaseHelper db;
    TextView save, txtDebugAppID, txtLatIDD, txtLongIDD, txtIBaseUpdateID, inst_controller_ser;
    InstallationBean installationBean;
    LabeledSwitch labeledSwitch;
    int index_simoprator, index_conntype, id = 0, vkp = 0, currentScannerFor = -1, value;
    Spinner spinner_simoprator, spinner_conntype;

    String pernr = "", project_no = "", project_no1 = "", MUserId = "", login_no = "", customer_name = "", installation_date = "", address_ins = "", make_ins = "", tehsil_ins = "",
            village_ins = "", mobile_no_ins = "", solarpanel_wattage = "", no_of_module = "", total_watt = "", solarpanel_stand_ins_quantity = "", module_total_plate_watt = "",
            smmd_sno = "", solar_motor_model_details = "", splar_pump_model_details = "", spmd_sno = "", simcard_num = "", solar_controller_model = "", scm_sno = "", inst_latitude = "",
            inst_longitude = "", module_ser_no = "", inst_bill_no = "", inst_bill_date = "", inst_delay_reason = "", hp = "", current_date = "", simoprator_text = "", conntype_text = "", billno = "",
            set_matno = "", simha2 = "", kunnr = "", gstbillno = "", billdate = "", dispdate = "", name = "", state = "", city = "", controller = "", motor = "", pump = "", state_txt = "",
            city_txt = "", address = "", make = "", custname = "", fathname = "", simno = "", regisno = "", projectno = "", loginno = "", moduleqty = "", mobileno = "", tehvillage = "",
            borewellstatus1 = "", DeviceStatus = "", CUS_CONTACT_NO = "", BeneficiaryNo = "", no_of_module_value = "", rmsdata_status = "", mMOBNUM_1, mMOBNUM_2, mMOBNUM_3,
             MEmpType = "null", mAppName = "KUSUM", mInstallerMOB = "", mInstallerName = "", RMS_SERVER_DOWN = "", RMS_DEBUG_EXTRN = "", DEVICE_NO, SIGNL_STREN,
            INVOICE_NO_B, NET_REG, SER_CONNECT, CAB_CONNECT, LATITUDE, LANGITUDE, MOBILE, IMEI, DONGAL_ID = "", SIM_SR_NO = "", SIM = "", RMS_STATUS = "", RMS_LAST_ONLINE_DATE = "", FAULT_CODE = "",
            RMS_CURRENT_ONLINE_STATUS = "", version = "", invc_done = "", docno_sap = "", mDriveSerialNo = "", mMotorSerialNo = "", mPumpSerialNo = "", delay = "",mobileOnlineStatus = "",
            controllerOnlineStatus = "",dirPath = "",finalFileName="",filePath ="",type ="",columnCount ="";
    File selectedFile;
    EditText inst_date, bill_date, bill_no, cust_name, borewellstatus, reasontxt, inst_address, inst_make, inst_village,
            inst_state, inst_district, inst_tehsil, inst_mob_no, inst_panel_stand_qty, inst_panel_watt, inst_total_watt, inst_module_total_plate_watt, inst_no_of_module, inst_module_ser_no,
            inst_motor_model, inst_motor_ser, inst_pump_model, inst_pump_ser, inst_controller_model, inst_simcard_num, inst_hp, inst_fathers_name;

    double inst_latitude_double, inst_longitude_double;
    SimpleDateFormat simpleDateFormat;

    ImageView inst_location, img_scn_one, img_scn_two, img_scn_three, img_scn_four, geoIndigation;

    LinearLayout reason, moduleOneLL;

    Boolean your_date_is_outdated = false, isDongleExtract = false;

    private Dialog dialog;

    ProgressDialog progressDialog;



    @SuppressLint("HandlerLeak")
    android.os.Handler mHandler2 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(InstallationInitial.this, mString, Toast.LENGTH_LONG).show();
        }
    };

    List<ImageModel> imageList = new ArrayList<>();
    boolean isBaseUpdate = false, isControllerIDScan = false, isDebug = false, isSubmit = false;


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation_initial);



        mContext = this;
        progressDialog = new ProgressDialog(InstallationInitial.this);

        WebURL.GALLERY_DIRECTORY_NAME_COMMON = "ShaktiKusum";
        dialog = new Dialog(mContext);
        WebURL.mSettingCheckValue = "0";
        db = new DatabaseHelper(mContext);
        mDatabaseHelperTeacher = new DatabaseHelperTeacher(mContext);
        WebURL.SERVER_CONNECTIVITY_OK = 0;
        WebURL.CHECK_FINAL_ALL_OK = 0;
        project_no1 = CustomUtility.getSharedPreferences(mContext, "projectid");
        MUserId = CustomUtility.getSharedPreferences(mContext, "userid");
        MEmpType = "Vend";

        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("versionErrpr====>", e.getMessage());
            throw new RuntimeException(e);

        }

        mBTResonseDataList = new ArrayList<>();
        WebURL.BT_DEBUG_CHECK = 0;
        Constant.BILL_NUMBER_UNIC = "";
        Bundle extras = getIntent().getExtras();
        pernr = CustomUtility.getSharedPreferences(mContext, "userid");
        billno = extras.getString("bill_no");
        set_matno = extras.getString("set_matno");
        simha2 = extras.getString("simha2");
        kunnr = extras.getString("kunnr");
        gstbillno = extras.getString("gst_bill_no");
        billdate = extras.getString("bill_date");
        dispdate = extras.getString("disp_date");
        name = extras.getString("name");
        state = extras.getString("state");
        city = extras.getString("city");
        state_txt = extras.getString("state_txt");
        city_txt = extras.getString("city_txt");
        address = extras.getString("address");
        tehvillage = extras.getString("tehvillage");
        mobileno = extras.getString("mobile");
        controller = extras.getString("controller");
        motor = extras.getString("motor");
        pump = extras.getString("pump");
        simno = extras.getString("simno");
        regisno = extras.getString("regisno");
        projectno = extras.getString("projectno");
        loginno = extras.getString("loginno");
        moduleqty = extras.getString("moduleqty");
        CUS_CONTACT_NO = extras.getString("CUS_CONTACT_NO");
        BeneficiaryNo = extras.getString("BeneficiaryNo");
        try {
            Constant.BILL_NUMBER_UNIC = billno;
            String[] custnmStr = name.split("S/O", 2);
            custname = custnmStr[0];
            String Custfathname = custnmStr[1];
            String[] custfathStr = Custfathname.split("-", 2);
            fathname = custfathStr[0];
            Log.e("fath", "&&&&" + fathname);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mSimDetailsInfoResponse = new ArrayList<>();
        CustomUtility.setSharedPreference(mContext, "SYNCLIST", "0");

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Installation Form");

        getLayout();

        txtIBaseUpdateID.setOnClickListener(v -> ibaseUpdateFormPopup());

        txtDebugAppID.setOnClickListener(v -> {
            WebURL.BT_DEVICE_NAME = "";
            WebURL.BT_DEVICE_MAC_ADDRESS = "";
            Constant.Bluetooth_Activity_Navigation = 1;///Debug

            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter.isEnabled()) {
                if (AllPopupUtil.pairedDeviceListGloable(mContext)) {
                    if (WebURL.BT_DEVICE_NAME.equalsIgnoreCase("") || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("")) {
                        Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                        intent.putExtra(Constant.ControllerSerialNumber, inst_controller_ser.getText().toString().trim());
                        intent.putExtra(Constant.debugDataExtract, "false");
                        startActivity(intent);
                    }
                } else {
                    startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                }
            } else {
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            }
        });

        installationBean = new InstallationBean();
        installationBean = db.getInstallationData(CustomUtility.getSharedPreferences(mContext, "userid"), billno);

        list_simoprator = new ArrayList<>();
        list_conntype = new ArrayList<>();

        getSimTypeValue();
        getConnTypeValue();


        String dt = dispdate;  // dispatch date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 15);
        String output = sdf.format(c.getTime());

        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = formatter.parse(output);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("Timestamp1", "***" + System.currentTimeMillis());
        assert date != null;
        Log.e("Timestamp2", "***" + date.getTime());

        if (System.currentTimeMillis() > date.getTime()) {
            your_date_is_outdated = true;
            delay = "1";
            reason.setVisibility(View.VISIBLE);
        } else {
            your_date_is_outdated = false;
            delay = "2";
            reason.setVisibility(View.GONE);
        }

        borewellstatus1 = CustomUtility.getSharedPreferences(mContext, "borewellstatus" + billno);

        if (!TextUtils.isEmpty(borewellstatus1)) {
            borewellstatus.setText(borewellstatus1);
        }
        dataAdapter_simoprator = new ArrayAdapter<>(this, R.layout.spinner_item_left_optional, list_simoprator);
        dataAdapter_simoprator.setDropDownViewResource(R.layout.spinner_item_center);
        txtLongIDD = findViewById(R.id.txtLongIDD);
        txtLatIDD = findViewById(R.id.txtLatIDD);

        spinner_simoprator.setAdapter(dataAdapter_simoprator);

        spinner_simoprator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index_simoprator = arg0.getSelectedItemPosition();
                if (spinner_simoprator.getSelectedItem().toString().equalsIgnoreCase("Select Sim Operator")) {
                    simoprator_text = "";
                } else {
                    simoprator_text = spinner_simoprator.getSelectedItem().toString();
                }
                Log.e("SPINNER", "$$$$" + index_simoprator);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        dataAdapter_conntype = new ArrayAdapter<>(this, R.layout.spinner_item_left_optional, list_conntype);
        dataAdapter_conntype.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_conntype.setAdapter(dataAdapter_conntype);

        spinner_conntype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index_conntype = arg0.getSelectedItemPosition();
                if (spinner_conntype.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type")) {
                    conntype_text = "";
                } else {
                    conntype_text = spinner_conntype.getSelectedItem().toString();
                }
                Log.e("SPINNER", "$$$$" + index_conntype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (installationBean != null && !installationBean.toString().isEmpty()) {
            setData(installationBean);
        }


        img_scn_one.setOnClickListener(v -> {
            id = 1000;
            startScanner(id);
        });


        img_scn_two.setOnClickListener(v -> {
            id = 2000;
            startScanner(id);
        });

        img_scn_three.setOnClickListener(v -> {
            id = 3000;
            startScanner(id);
        });

        img_scn_four.setOnClickListener(v -> {
            id = 4000;
            startScanner(id);
        });

        borewellstatus.setOnClickListener(v -> showAlertDialog());

        inst_location.setOnClickListener(v -> {
            if (TextUtils.isEmpty(installationBean.getLatitude()) || installationBean.getLatitude().equals("0.0") || installationBean.getLatitude().equals("null")) {
                getGpsLocation();
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Latitude, Longitude already saved, Do you want to change it?");
                alertDialog.setPositiveButton("Yes", (dialog, which) -> getGpsLocation());
                alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                alertDialog.show();
            }
        });

        save.setOnClickListener(v -> {
            DeviceStatus = CustomUtility.getSharedPreferences(mContext, Constant.deviceStatus);

                if (mBTResonseDataList.size() > 0)
                    mBTResonseDataList.clear();
                Log.e("inst_controller_ser===>", inst_controller_ser.getText().toString().trim());
                Log.e("mDatabaseHelperTeacher=====>", String.valueOf(mDatabaseHelperTeacher.getDeviceInfoDATABTFindDebug(inst_controller_ser.getText().toString().trim() + "-0")));
                mBTResonseDataList = mDatabaseHelperTeacher.getDeviceInfoDATABTFindDebug(inst_controller_ser.getText().toString().trim() + "-0");
                if (mBTResonseDataList.size() > 0) {
                    vkp = mBTResonseDataList.size() - 1;
                    Log.e("vkp1111====>",String.valueOf(vkp));


                   DEVICE_NO = mBTResonseDataList.get(vkp).getDEVICENO();
                    SIGNL_STREN = mBTResonseDataList.get(vkp).getSIGNLSTREN();
                    String[] mStrArry = SIGNL_STREN.split("###");
                    if (mStrArry.length > 0) {
                        SIGNL_STREN = mStrArry[0];
                    }
                    if (mStrArry.length > 1) {
                        INVOICE_NO_B = mStrArry[1];
                    }

                    SIM = mBTResonseDataList.get(vkp).getSIM();
                    String[] mStrArrySim = SIM.split("###");
                    if (mStrArrySim.length > 0) {
                        SIM = mStrArrySim[0];
                    }
                    if (mStrArrySim.length > 1) {
                        SIM_SR_NO = mStrArrySim[1];
                    }
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
                    FAULT_CODE = mBTResonseDataList.get(vkp).getmRMS_FAULT_CODE();
                    mobileOnlineStatus = mBTResonseDataList.get(vkp).getMobileOnline();
                    controllerOnlineStatus = mBTResonseDataList.get(vkp).getControllerOnline();
                    dirPath = mBTResonseDataList.get(vkp).getDirPath();
                    RMS_DEBUG_EXTRN = "ONLINE FROM DEBUG";
                    RMS_SERVER_DOWN = "Working Fine";

                    if (mBTResonseDataList.get(vkp).getDongleDataExtract().equals("true")) {
                        isDongleExtract = true;
                    }


                    if (isControllerIDScan) {
                        saveDataValidation();
                    } else {
                        CustomUtility.ShowToast("Please Scan Controller ID first!", getApplicationContext());
                    }
                } else {
                    CustomUtility.ShowToast("Please Debug Data first!", getApplicationContext());
                }

        });


        labeledSwitch.setOnToggledListener((toggleableView, isOn) -> {
            Intent intent = new Intent(mContext, DeviceStatusActivity.class);
            startActivity(intent);
        });

    }

    void startScanner(int scanID) {
        currentScannerFor = scanID;
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QRCode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);// Use a specific camera of the device
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            setDataToViewFromScanner(scanningResult);
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 301) {
                String year = data.getStringExtra("year");
                String month = data.getStringExtra("month");
                String date = data.getStringExtra("date");
                String finaldate2 = year + "-" + month + "-" + date;
                finaldate2 = CustomUtility.formateDate(finaldate2);
                inst_date.setText(finaldate2);
            }
        }
    }

    void setDataToViewFromScanner(IntentResult scanningResult) {
        try {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            Toast.makeText(getApplicationContext(), scanFormat + scanContent, Toast.LENGTH_SHORT);
            boolean alreadySet = false;
            switch (currentScannerFor) {
                case 1000:
                    inst_motor_ser.setText("");
                    inst_motor_ser.setText(scanContent);
                    alreadySet = true;
                    break;
                case 2000:
                    inst_pump_ser.setText("");
                    inst_pump_ser.setText(scanContent);
                    alreadySet = true;
                    break;
                case 3000:
                    if (scanContent.equals(inst_controller_ser.getText().toString())) {
                        inst_controller_ser.setText("");
                        inst_controller_ser.setText(scanContent);
                        alreadySet = true;
                        isControllerIDScan = true;
                    } else {
                        MatchControllerIDPopup();
                    }

                    break;
                case 4000:
                    inst_simcard_num.setText("");
                    inst_simcard_num.setText(scanContent);
                    alreadySet = true;
                    break;
            }
            if (!alreadySet) {
                if (scannedDeviceNo.size() > 0) {

                    if (!scannedDeviceNo.contains(scanContent)) {
                        EditText edit_O = moduleOneLL.getChildAt(currentScannerFor).findViewById(R.id.view_edit_one);
                        edit_O.setText(scanContent);
                        scannedDeviceNo.add(scanContent);
                    } else {
                        CustomUtility.ShowToast("Already done", getApplicationContext());
                    }
                } else {
                    EditText edit_O = moduleOneLL.getChildAt(currentScannerFor).findViewById(R.id.view_edit_one);

                    edit_O.setText(scanContent);
                    scannedDeviceNo.add(scanContent);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MatchControllerIDPopup() {
        LayoutInflater inflater = (LayoutInflater) InstallationInitial.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InstallationInitial.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (!isFinishing()) {
            alertDialog.show();
        }
        CircleImageView user_img = layout.findViewById(R.id.user_img);
        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);

        user_img.setVisibility(View.GONE);

        title_txt.setText(getResources().getString(R.string.cant_proceed));

        OK_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ibaseUpdateFormPopup();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attachment, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id_temp = item.getItemId();

        switch (id_temp) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.act_comp_setting_pera:

                if (mBTResonseDataList.size() > 0)
                    mBTResonseDataList.clear();

                mBTResonseDataList = mDatabaseHelperTeacher.getDeviceInfoDATABTFindDebug(inst_controller_ser.getText().toString().trim() + "-0");

                if (mBTResonseDataList.size() > 0) {
                    simUpdatePopup();
                } else {
                    if (Constant.Bluetooth_Activity_Navigation1 == 1) {
                        simUpdatePopup();
                    } else {
                        Toast.makeText(mContext, "Please debug at least one time.", Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            case R.id.act_comp_add_damage_complain:
                Intent mIntent = new Intent(InstallationInitial.this, AddDamage_MissingActivity.class);// original
                Bundle extras = new Bundle();
                extras.putString("bill_no", billno);///vbeln
                extras.putString("state", state);//state
                extras.putString("city", city);
                extras.putString("name", name);
                extras.putString("mobile", mobileno);
                extras.putString("bill_date", billdate);
                extras.putString("address", address);
                extras.putString("kunnr", kunnr);
                mIntent.putExtras(extras);
                startActivity(mIntent);
                return true;
            case R.id.act_comp_attach_image:

                borewellstatus1 = CustomUtility.getSharedPreferences(mContext, "borewellstatus" + billno);

                if (!TextUtils.isEmpty(borewellstatus1)) {

                    Intent intent = new Intent(InstallationInitial.this, InstReportImageActivity.class);
                    intent.putExtra("inst_id", bill_no.getText().toString().trim());
                    intent.putExtra("cust_name", custname);
                    intent.putExtra("delay_status", delay);
                    startActivity(intent);

                } else {
                    Toast.makeText(mContext, "Please Select Borewell Status", Toast.LENGTH_SHORT).show();
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void ViewInflate(int value, int new_value) {

        String[] arr = no_of_module_value.split(",");
        moduleOneLL.removeAllViews();

        for (int i = 0; i < new_value; i++) {
            View child_grid = LayoutInflater.from(mContext).inflate(R.layout.view_for_normal, null);
            LinearLayout layout_s = child_grid.findViewById(R.id.sublayout_second);
            LinearLayout layout_f = child_grid.findViewById(R.id.sublayout_first);
            LinearLayout layout_f_inner = layout_f.findViewById(R.id.sublayout_first_inner);
            EditText edit = layout_f_inner.findViewById(R.id.view_edit_one);
            final ImageView scan = layout_f_inner.findViewById(R.id.view_img_one);
            scan.setId(i);

            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    startScanner(id);
                }
            });

            try {
                if (arr.length > 0) {
                    if (i < arr.length) {
                        edit.setText(arr[i]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            layout_s.setVisibility(View.GONE);
            moduleOneLL.setVisibility(View.VISIBLE);
            moduleOneLL.addView(child_grid);

        }
    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Borewell Status");
        String[] items = {"Farmer agreed to clean borewell", "Farmer doesn't agree to clean borewell (no warranty for pump set)", "Above Clause not applicable (in case borewell is old/clean)"};

        int checkedItem = -1;

        if (CustomUtility.getSharedPreferences(mContext, "borewellstatus").equalsIgnoreCase("1")) {
            checkedItem = 0;
        } else if (CustomUtility.getSharedPreferences(mContext, "borewellstatus").equalsIgnoreCase("2")) {
            checkedItem = 1;
        } else if (CustomUtility.getSharedPreferences(mContext, "borewellstatus").equalsIgnoreCase("3")) {
            checkedItem = 2;
        }
        alertDialog.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            switch (which) {
                case 0:
                    borewellstatus1 = items[0];
                    borewellstatus.setText(items[0]);
                    CustomUtility.setSharedPreference(mContext, "borewellstatus" + billno, items[0]);
                    CustomUtility.setSharedPreference(mContext, "borewellstatus", "1");
                    Toast.makeText(mContext, CustomUtility.getSharedPreferences(mContext, "borewellstatus" + billno), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
                case 1:
                    borewellstatus1 = items[1];
                    borewellstatus.setText(items[1]);
                    CustomUtility.setSharedPreference(mContext, "borewellstatus" + billno, items[1]);
                    CustomUtility.setSharedPreference(mContext, "borewellstatus", "2");
                    Toast.makeText(mContext, CustomUtility.getSharedPreferences(mContext, "borewellstatus" + billno), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
                case 2:
                    borewellstatus1 = items[2];
                    borewellstatus.setText(items[2]);
                    CustomUtility.setSharedPreference(mContext, "borewellstatus" + billno, items[2]);
                    CustomUtility.setSharedPreference(mContext, "borewellstatus", "3");
                    Toast.makeText(mContext, CustomUtility.getSharedPreferences(mContext, "borewellstatus" + billno), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.getWindow().setLayout(800, 1400);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    //QR Code check
    public String GetDataOne() {
        String finalValue = "";
        if (!inst_module_ser_no.getText().toString().trim().equals("0")) {

            for (int i = 0; i < moduleOneLL.getChildCount(); i++) {
                EditText edit_O = moduleOneLL.getChildAt(i).findViewById(R.id.view_edit_one);
                if (edit_O.getVisibility() == View.VISIBLE) {
                    String s1 = edit_O.getText().toString().trim();
                    finalValue += s1 + ",";
                }
            }
        } else {
            finalValue = "";
        }
        return finalValue;
    }

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {

            String mLAt = "" + gps.getLatitude();
            String mLOng = "" + gps.getLongitude();


            txtLatIDD.setText(mLAt);
            txtLongIDD.setText(mLOng);

            inst_latitude_double = Double.parseDouble(txtLatIDD.getText().toString().trim());
            inst_longitude_double = Double.parseDouble(txtLongIDD.getText().toString().trim());

            if (txtLatIDD.getText().toString().trim().equalsIgnoreCase("")) {
                inst_latitude_double = 0.0;
            }

            System.out.println("vihu_Check Lat= " + inst_latitude_double + " Long=" + inst_longitude_double);


            if (inst_latitude_double == 0.0) {
                CustomUtility.ShowToast("Lat Long not captured, Please try again.", mContext);
            } else {

                inst_latitude = String.valueOf(inst_latitude_double);
                inst_longitude = String.valueOf(inst_longitude_double);
                geoIndigation.setImageDrawable(getResources().getDrawable(R.drawable.right_mark_icn_green));
                CustomUtility.ShowToast("Latitude:-" + inst_latitude_double + "     " + "Longitude:-" + inst_longitude_double, mContext);

            }
        } else {
            gps.showSettingsAlert();
        }
    }


    private void saveDataValidation() {
        getData();
        int no_of_module_value1 = 0;
        String[] arr = no_of_module_value.split(",");
        if (!no_of_module.isEmpty()) {
            no_of_module_value1 = Integer.parseInt(no_of_module);
        }
        if (inst_latitude != null && !inst_latitude.equals("") && inst_longitude != null && !inst_longitude.equals("") && !inst_longitude.equals("0.0") && !inst_latitude.equals("0.0")) {
            if (inst_bill_no != null && !inst_bill_no.equals("")) {

                if (solarpanel_wattage != null && !solarpanel_wattage.equals("")) {
                    if (hp != null && !hp.equals("")) {
                        if (solarpanel_stand_ins_quantity != null && !solarpanel_stand_ins_quantity.equals("")) {
                            if (total_watt != null && !total_watt.equals("")) {
                                if (conntype_text != null && !conntype_text.equals("")) {
                                    if (solarpanel_stand_ins_quantity != null && !solarpanel_stand_ins_quantity.equals("")) {
                                        //   if (solarpanel_stand_ins_quantity != null && !solarpanel_stand_ins_quantity.equals("")) {
                                        if (module_total_plate_watt != null && !module_total_plate_watt.equals("")) {
                                            if (smmd_sno != null && !smmd_sno.equals("")) {
                                                if (spmd_sno != null && !spmd_sno.equals("")) {
                                                    if (scm_sno != null && !scm_sno.equals("")) {
                                                        if (arr.length == no_of_module_value1) {
                                                            for (int i = 0; i < arr.length - 1; i++) {
                                                                if (arr[i].equalsIgnoreCase("")) {
                                                                    Toast.makeText(mContext, "Please Enter all module serial no.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                            if (inst_make != null && !inst_make.equals("")) {
                                                                if (!TextUtils.isEmpty(borewellstatus1)) {

                                                                    if (!DeviceStatus.isEmpty()) {

                                                                        if (DeviceStatus.equals(getResources().getString(R.string.online))) {
                                                                            if (imageList.size() > 5) {


                                                                                saveInstalltion();

                                                                            } else {
                                                                                CustomUtility.showToast(InstallationInitial.this, getResources().getString(R.string.select_all_image));
                                                                            }
                                                                        } else {

                                                                            if (mSimDetailsInfoResponse.size() > 0)
                                                                                mSimDetailsInfoResponse.clear();
                                                                            mSimDetailsInfoResponse = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);
                                                                            if (mSimDetailsInfoResponse.size() >= 1) {
                                                                                if (mSimDetailsInfoResponse.size() >= 2) {
                                                                                    if (mSimDetailsInfoResponse.size() >= 3) {


                                                                                        if (imageList.size() > 5) {
                                                                                            saveInstalltion();

                                                                                        } else {
                                                                                            CustomUtility.showToast(InstallationInitial.this, getResources().getString(R.string.select_all_image));
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
                                                                    } else {
                                                                        Toast.makeText(mContext, "Please get RMS Device Status.", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                } else {
                                                                    Toast.makeText(mContext, "Please Select Borewell Status.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(mContext, "Please Enter Make", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(mContext, "Please Enter all module serial no.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(mContext, "Please Enter Controller Serial No.", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(mContext, "Please Enter Pump Serial No.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(mContext, "Please Enter Motor Serial No.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(mContext, "Please Enter Total Module Plate Watt", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(mContext, "Please Enter Total Plate Watt", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, "Please Select Connection Type", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Please Enter Total Watt.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "Please Enter Solar Panel Stand Quantity.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Please Enter HP.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Please Solar Panel Wattage.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(mContext, "Please Enter Bill No.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "Please Take Latitude and Longitude", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveInstalltion() {


        String[] arr = no_of_module_value.split(",");
        Set<String> set = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {

            if (set.contains(arr[i].toUpperCase())) {
                CustomUtility.ShowToast(arr[i] + getResources().getString(R.string.moduleMultipleTime), this);
                Log.e(arr[i], " is duplicated");
                isSubmit = false;
                break;
            } else {
                set.add(arr[i].toUpperCase());
                isSubmit = true;
            }
        }
        if (isSubmit) {

            SaveInLocalDataBase();

            if (CustomUtility.isInternetOn(getApplicationContext())) {

                if (reason.getVisibility() == View.VISIBLE) {
                    if (!reasontxt.getText().toString().isEmpty()) {
                          SubmitData();

                    } else {
                        CustomUtility.ShowToast("Please Enter Installation Delay Reason.", getApplicationContext());
                    }
                } else {
                     SubmitData();

                }

            } else {
               CustomUtility.ShowToast(getResources().getString(R.string.savedInLocalDatabase), mContext);
                Intent intent = new Intent(mContext, InstallationList.class);
                startActivity(intent);
                finish();
           }

        } else {
            CustomUtility.ShowToast("Installation Not Submitted,Remove duplicate module Number", this);
        }


    }

    private void SubmitData() {
        if(mobileOnlineStatus.equals(getResources().getString(R.string.offline))&& controllerOnlineStatus.equals(getResources().getString(R.string.offline))){
            sendFileToRMSServer();
        }else {
            SubmitDebugData();
        }
    }

    void sendFileToRMSServer() {
        Uri uri = Uri.parse(dirPath);
        String[] fileName = FileUtils.getPath(InstallationInitial.this, uri).split("/");
        finalFileName = fileName[fileName.length - 1];
        filePath = FileUtils.getPath(InstallationInitial.this, uri);
        Log.e("uri=========>", uri.toString());
        Log.e("finalFileName=========>", finalFileName);
        Log.e("filePath=========>", filePath);

        if (finalFileName.contains(".xls")) {
            selectedFile = new File(filePath);
            if (finalFileName.contains("Dongle_") || finalFileName.contains("Dongle")) {
                type = "DongleMonth";
            } else {
                type = "Month";
            }

            if (CustomUtility.isInternetOn(InstallationInitial.this)) {
                uploadFile();

            } else {
                CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), InstallationInitial.this);
            }
        } else {

            CustomUtility.ShowToast("Please Select file from Data Logger Folder this file is not valid", getApplicationContext());
        }


    }

    private void SaveInLocalDataBase() {
        getData();
        if (!Constant.DBUG_MOB_1.equalsIgnoreCase("")) {
            if (mSimDetailsInfoResponse.size() > 0)
                mSimDetailsInfoResponse.clear();

            mSimDetailsInfoResponse = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);
        }

        borewellstatus1 = CustomUtility.getSharedPreferences(this, "borewellstatus" + billno);
        InstallationBean installationBean = new InstallationBean(pernr, project_no, login_no, inst_latitude, inst_longitude, inst_bill_no, installation_date, inst_bill_date,
                inst_delay_reason, rmsdata_status, customer_name, fathname, mobileno, state, state_txt, city, city_txt, tehsil_ins, village_ins, address, make, solarpanel_wattage,
                solarpanel_stand_ins_quantity, total_watt, hp, no_of_module, no_of_module_value, module_total_plate_watt, solar_motor_model_details, smmd_sno, splar_pump_model_details,
                spmd_sno, solar_controller_model, scm_sno, simoprator_text, conntype_text, simcard_num, regisno, BeneficiaryNo
        );

        if (db.isRecordExist(DatabaseHelper.TABLE_INSTALLATION_PUMP_DATA, DatabaseHelper.KEY_BILL_NO, inst_bill_no)) {
            db.updateInstallationData(inst_bill_no, installationBean);

        } else {
            db.insertInstallationData(inst_bill_no, installationBean);
        }

    }


    public void getData() {

        pernr = CustomUtility.getSharedPreferences(mContext, "userid");
        project_no = CustomUtility.getSharedPreferences(mContext, "projectid");
        login_no = CustomUtility.getSharedPreferences(mContext, "loginid");

        inst_latitude = String.valueOf(inst_latitude_double);
        inst_longitude = String.valueOf(inst_longitude_double);
        inst_bill_no = bill_no.getText().toString();
        inst_bill_date = bill_date.getText().toString();
        inst_delay_reason = reasontxt.getText().toString();
        installation_date = inst_date.getText().toString();
        customer_name = cust_name.getText().toString();
        fathname = inst_fathers_name.getText().toString();
        mobile_no_ins = inst_mob_no.getText().toString();
        address_ins = inst_address.getText().toString();
        make_ins = inst_make.getText().toString();
        tehsil_ins = inst_tehsil.getText().toString();
        village_ins = inst_village.getText().toString();

        address_ins = inst_address.getText().toString();
        make_ins = inst_make.getText().toString();
        solarpanel_wattage = inst_panel_watt.getText().toString();
        hp = inst_hp.getText().toString();
        solarpanel_stand_ins_quantity = inst_panel_stand_qty.getText().toString();
        total_watt = inst_total_watt.getText().toString();
        make = inst_make.getText().toString();
        module_total_plate_watt = inst_module_total_plate_watt.getText().toString();

        no_of_module = inst_no_of_module.getText().toString();
        no_of_module_value = GetDataOne();
        solar_motor_model_details = inst_motor_model.getText().toString();
        smmd_sno = inst_motor_ser.getText().toString();
        splar_pump_model_details = inst_pump_model.getText().toString();
        spmd_sno = inst_pump_ser.getText().toString();

        solar_controller_model = inst_controller_model.getText().toString();
        scm_sno = inst_controller_ser.getText().toString();

        simcard_num = inst_simcard_num.getText().toString();


    }

    public void getLayout() {

        txtDebugAppID = findViewById(R.id.txtDebugAppID);
        txtIBaseUpdateID = findViewById(R.id.txtIBaseUpdateID);

        save = findViewById(R.id.save);

        geoIndigation = findViewById(R.id.geoIndigation);
        inst_location = findViewById(R.id.loaction);

        borewellstatus = findViewById(R.id.borewellstatus);
        reasontxt = findViewById(R.id.reason_txt);
        reason = findViewById(R.id.reason);


        inst_date = findViewById(R.id.Installation_date);
        bill_date = findViewById(R.id.bill_date);
        bill_no = findViewById(R.id.Billing_no);
        cust_name = findViewById(R.id.Customer_name);

        inst_address = findViewById(R.id.address);
        inst_make = findViewById(R.id.make);
        inst_state = findViewById(R.id.state);
        inst_district = findViewById(R.id.district);
        inst_village = findViewById(R.id.village);
        inst_tehsil = findViewById(R.id.tehsil);
        inst_hp = findViewById(R.id.inst_hp);
        inst_mob_no = findViewById(R.id.mobile_no);
        inst_panel_stand_qty = findViewById(R.id.sp_stand_install);
        inst_panel_watt = findViewById(R.id.solar_panel_wattage);
        inst_fathers_name = findViewById(R.id.fathers_name);

        inst_total_watt = findViewById(R.id.total_watts);
        inst_no_of_module = findViewById(R.id.module_serial_no);
        inst_module_total_plate_watt = findViewById(R.id.module_total_plate_watt);

        inst_motor_model = findViewById(R.id.sm_Model_Details_one);
        inst_motor_ser = findViewById(R.id.s_no_one);
        img_scn_one = findViewById(R.id.img_scn_one);

        inst_pump_model = findViewById(R.id.sm_Model_Details_two);
        inst_pump_ser = findViewById(R.id.s_no_two);
        img_scn_two = findViewById(R.id.img_scn_two);

        inst_controller_model = findViewById(R.id.sm_Model_Details_three);
        inst_controller_ser = findViewById(R.id.s_no_three);
        img_scn_three = findViewById(R.id.img_scn_three);

        spinner_simoprator = findViewById(R.id.spinner_simoprator);
        spinner_conntype = findViewById(R.id.spinner_conntype);

        img_scn_four = findViewById(R.id.img_scn_four);

        inst_simcard_num = findViewById(R.id.s_no_four);

        moduleOneLL = findViewById(R.id.layout_one);

        inst_module_ser_no = findViewById(R.id.module_serial_no);

        labeledSwitch = findViewById(R.id.switchview);

        spinner_simoprator.setPrompt("Select SIM Oprator");
        spinner_conntype.setPrompt("Select Connection Type");

    }

    public void setData(InstallationBean installationBean) {
        if (!TextUtils.isEmpty(installationBean.getLatitude()) && !installationBean.getLatitude().equals("0.0") && !installationBean.getLatitude().equals("null")) {
            geoIndigation.setImageDrawable(getResources().getDrawable(R.drawable.right_mark_icn_green));

            inst_latitude_double = Double.parseDouble(installationBean.getLatitude());
            inst_longitude_double = Double.parseDouble(installationBean.getLongitude());
            inst_simcard_num.setText(installationBean.getSimcard_num());
        } else {
            inst_simcard_num.setText(simno);
        }


        if (!installationBean.getScm_sno().isEmpty()) {
            controller = installationBean.getScm_sno().trim();
            isControllerIDScan = true;
        }

        rmsdata_status = installationBean.getRms_data_status();


        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date = simpleDateFormat.format(new Date());
        inst_date.setText(current_date);

        bill_date.setText(billdate);
        bill_no.setText(billno);
        inst_bill_no = billno;
        cust_name.setText(custname);
        inst_fathers_name.setText(fathname);
        inst_mob_no.setText(mobileno);
        inst_address.setText(address);
        inst_state.setText(state_txt);
        inst_district.setText(city_txt);
        inst_tehsil.setText(tehvillage);
        inst_village.setText(tehvillage);

        inst_panel_watt.setText(installationBean.getSolarpanel_wattage());
        inst_hp.setText(installationBean.getInst_hp());
        inst_panel_stand_qty.setText(installationBean.getSolarpanel_stand_ins_quantity());
        inst_total_watt.setText(installationBean.getTotal_watt());
        inst_no_of_module.setText(moduleqty);

        inst_make.setText(installationBean.getMake_ins());

        if (!TextUtils.isEmpty(installationBean.getNo_of_module_qty())) {
            no_of_module_value = installationBean.getNo_of_module_value();
            if (this.installationBean.getNo_of_module_qty().length() != 0 && !this.installationBean.getNo_of_module_qty().equals("0")) {
                value = Integer.parseInt(installationBean.getNo_of_module_qty());
                ViewInflate(value, value);
            }
        } else {

            no_of_module_value = GetDataModule();

            module_ser_no = inst_module_ser_no.getText().toString().trim();
            if (module_ser_no.length() != 0 && !module_ser_no.equals("0")) {
                value = Integer.parseInt(module_ser_no);
                ViewInflate(value, value);
            }
        }

        inst_module_total_plate_watt.setText(installationBean.getModule_total_plate_watt());

        inst_motor_ser.setText(motor);

        inst_pump_ser.setText(pump);

       inst_controller_ser.setText(controller);
       //inst_controller_ser.setText(controller);

        if (!TextUtils.isEmpty(installationBean.getSimoprator())) {
            spinner_simoprator.setSelection(db.getPosition(spinner_simoprator, installationBean.getSimoprator()));
        }

        if (!TextUtils.isEmpty(installationBean.getConntype())) {
            spinner_conntype.setSelection(db.getPosition(spinner_conntype, installationBean.getConntype()));
        }
    }

    public String GetDataModule() {
        String finalValue = "";
        if (!inst_module_ser_no.getText().toString().trim().equals("0")) {

            finalValue = getIntent().getStringExtra("NoOfModule");
        } else {
            finalValue = "";
        }
        return finalValue;
    }


    public void getSimTypeValue() {
        list_simoprator.add("Select SIM Operator");
        list_simoprator.add("IDEA");
        list_simoprator.add("AIRTEL");
        list_simoprator.add("BSNL");
        list_simoprator.add("VODAPHONE");
        list_simoprator.add("JIO");
        list_simoprator.add("TATA DOCOMO");
        list_simoprator.add("RELAINCE");
        list_simoprator.add("OTHER");
    }

    public void getConnTypeValue() {
        list_conntype.add("Select Connection Type");
        list_conntype.add("AC");
        list_conntype.add("DC");

    }

    @SuppressLint("StaticFieldLeak")

    private void simUpdatePopup() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.simupdate_popup);
        dialog.setCancelable(true);
        TextView txtTitleID = dialog.findViewById(R.id.txtTitleID);
        EditText edtSimNumberIDID = dialog.findViewById(R.id.edtSimNumberIDID);
        Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mSimNumberData = edtSimNumberIDID.getText().toString().trim();
                if (mSimNumberData.isEmpty()) {
                    Toast.makeText(mContext, "Please enter sim number", Toast.LENGTH_SHORT).show();
                } else {
                    List<SimDetailsInfoResponse> simArraylist = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);
                    if (simArraylist.size() > 0) {

                        for (int i = 0; i < simArraylist.size(); i++) {

                            if (!simArraylist.get(i).getDEVICENOSIMMOB().equals(mSimNumberData)) {
                                addSimIntoDatabse(mSimNumberData, dialog);
                            } else {
                                CustomUtility.ShowToast("Sim number already added, please try to add another number.", InstallationInitial.this);
                            }
                        }

                    } else {
                        addSimIntoDatabse(mSimNumberData, dialog);

                    }

                }
            }
        });
        dialog.show();
    }

    private void addSimIntoDatabse(String mSimNumberData, Dialog dialog) {
        long iiii = mDatabaseHelperTeacher.insertSimInfoData(controller, mSimNumberData, Constant.BILL_NUMBER_UNIC, Constant.BILL_NUMBER_UNIC, MUserId, true);
        Toast.makeText(mContext, "Sim number insterted successfully!", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    private void ibaseUpdateFormPopup() {
        dialog.setContentView(R.layout.ibase_update_form_popup);
        dialog.setCancelable(true);
        TextView txtTitleID = dialog.findViewById(R.id.txtTitleID);
        EditText edtDriveSerialNoID = dialog.findViewById(R.id.edtDriveSerialNoID);
        EditText edtMotorSerialNoID = dialog.findViewById(R.id.edtMotorSerialNoID);
        TextView edtPumpSerialNoID = dialog.findViewById(R.id.edtPumpSerialNoID);

        edtPumpSerialNoID.setText(pump);

        Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDriveSerialNo = edtDriveSerialNoID.getText().toString().trim();
                mMotorSerialNo = edtMotorSerialNoID.getText().toString().trim();
                mPumpSerialNo = edtPumpSerialNoID.getText().toString().trim();

                if (CustomUtility.isInternetOn(getApplicationContext())) {
                    dialog.dismiss();
                    serverIbaseUpdateFunctions();
                } else {
                    Toast.makeText(mContext, "Please check Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }


    private void serverIbaseUpdateFunctions() {

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("motor_serno", mMotorSerialNo));
        param.add(new BasicNameValuePair("con_serno", mDriveSerialNo));
        param.add(new BasicNameValuePair("set_serno", pump));

        showProgressDialogue(getResources().getString(R.string.I_base_update));

        new Thread() {
            public void run() {
                try {
                    String obj = CustomHttpClient.executeHttpPost1(WebURL.UPDATE_IBASE_VK_PAGE, param);
                    Log.d("check_error", obj);
                    Log.e("check_error", obj);

                    JSONObject object = new JSONObject(obj);
                    String mStatus = object.getString("status");
                    final String mMessage = object.getString("message");
                    if (mStatus.equalsIgnoreCase("true")) {
                        isBaseUpdate = true;
                        stopProgressDialogue();

                        Message msg = new Message();
                        msg.obj = mMessage;
                        mHandler2.sendMessage(msg);
                    } else {
                        isBaseUpdate = false;
                        Message msg = new Message();
                        msg.obj = mMessage;
                        mHandler2.sendMessage(msg);
                        stopProgressDialogue();

                    }

                } catch (Exception e) {
                    isBaseUpdate = false;
                    e.printStackTrace();
                    stopProgressDialogue();
                }
            }

        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        retriveArrayList();

        if (CustomUtility.getSharedPreferences(mContext, Constant.deviceStatus) != null &&
                !CustomUtility.getSharedPreferences(mContext, Constant.deviceStatus).isEmpty()) {
            DeviceStatus = CustomUtility.getSharedPreferences(mContext, Constant.deviceStatus);

            if (DeviceStatus.equals(getResources().getString(R.string.online))) {
                labeledSwitch.setOn(true);
            } else {
                labeledSwitch.setOn(false);
            }
        }
    }


    private void retriveArrayList() {
        imageList = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(this);

        List<ImageModel> installationImages = db.getAllInstallationImages();
        if (installationImages.size() > 0) {
            for (int i = 0; i < installationImages.size(); i++) {
                if (installationImages.get(i).getBillNo().trim().equals(bill_no.getText().toString().trim())) {
                    ImageModel imageModel = new ImageModel();
                    imageModel.setName(installationImages.get(i).getName());
                    imageModel.setImagePath(installationImages.get(i).getImagePath());
                    imageModel.setImageSelected(true);
                    imageModel.setBillNo(installationImages.get(i).getBillNo());
                    imageModel.setLatitude(installationImages.get(i).getLatitude());
                    imageModel.setLongitude(installationImages.get(i).getLongitude());
                    imageModel.setPoistion(installationImages.get(i).getPoistion());
                    imageList.add(imageModel);
                }

            }


        }
    }


    /*-------------------------------------------------------------Upload Excel Sheet TO RMS Server-----------------------------------------------------------------------------*/

    public void uploadFile() {

        showProgressDialogue(getResources().getString(R.string.dataExtractFileToServer));

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        if (type.equals("Month")) {
            columnCount = "15";

        } else if (type.equals("Fault")) {
            columnCount = "6";

        } else {
            columnCount = "35";

        }
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("DeviceNO", DEVICE_NO)
                .addFormDataPart("type", type)
                .addFormDataPart("columnCount", columnCount)
                .addFormDataPart("excel", filePath,
                        RequestBody.create(MediaType.parse("application/vnd.ms-excel"),
                                new File(filePath))).build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(CustomUtility.getSharedPreferences(getApplicationContext(), Constant.RmsBaseUrl) + "RMSApp/ExcelUploadNew")
                .method("POST", body)
                .build();

        Thread gfgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    Log.e("Jobject========>", Jobject.toString());

                    if (Jobject.getString("status").equals("true")) {

                        if (isDongleExtract) {
                            uploadIEMIFile();


                            ShowToast("File Upload Successfully now Uploading IMEI File To Server");

                        } else {
                            stopProgressDialogue();
                            ShowToast("File Upload Successfully");

                        }

                    } else {
                        stopProgressDialogue();
                        ShowToast("File Upload Failed, please try again!");
                    }

                } catch (IOException e) {
                    stopProgressDialogue();
                    e.printStackTrace();
                } catch (JSONException e) {
                    stopProgressDialogue();
                    e.printStackTrace();
                }
            }
        });

        gfgThread.start();

    }

    /*-------------------------------------------------------------Upload IMEI & Excel Sheet-----------------------------------------------------------------------------*/


    public void uploadIEMIFile() {
        stopProgressDialogue();
        showProgressDialogue(getResources().getString(R.string.ImeiFileToServer));

        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();


        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("deviceNo", DEVICE_NO.trim())
                .addFormDataPart("simimei", IMEI.trim())
                .addFormDataPart("file", filePath,
                        RequestBody.create(MediaType.parse("application/vnd.ms-excel"),
                                new File(filePath)))
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(CustomUtility.getSharedPreferences(getApplicationContext(), Constant.RmsBaseUrl) + "NewShakti/BTData")
                .method("POST", body)
                .build();

        Thread gfgThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    okhttp3.Response response = client.newCall(request)
                            .execute();
                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);

                    if (Jobject.getString("status").equals("true")) {
                        stopProgressDialogue();
                        ShowToast("File Upload Successfully");
                        SubmitDebugData();
                    } else {
                        ShowToast("File Upload Failed, please try again!");
                        stopProgressDialogue();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    stopProgressDialogue();
                }
            }
        });

        gfgThread.start();


    }


    /*-------------------------------------------------------------Submit Debug Data-----------------------------------------------------------------------------*/

    private void SubmitDebugData() {
        showProgressDialogue(getResources().getString(R.string.submittingDebugData));

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
            jsonObj.put("FAULT_CODE", FAULT_CODE.trim());

            jsonArray.put(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("URL=====>", WebURL.saveDebugData + "?action=" + jsonArray);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.saveDebugData + "?action=" + jsonArray,

                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    if (jsonObject.toString() != null && !jsonObject.toString().isEmpty()) {

                        String mStatus = jsonObject.getString("status");
                        if (mStatus.equals("true")) {
                            mInstallerMOB = CustomUtility.getSharedPreferences(mContext, "InstallerMOB");
                            mInstallerName = CustomUtility.getSharedPreferences(mContext, "InstallerName");

                            CustomUtility.setSharedPreference(mContext, Constant.isDebugDevice, "true");
                            if (mSimDetailsInfoResponse.size() > 0)
                                mSimDetailsInfoResponse.clear();

                            mSimDetailsInfoResponse = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);
                            Constant.BT_DEVICE_NAME = "";
                            Constant.BT_DEVICE_MAC_ADDRESS = "";


                            submitInstalltion();

                        } else {
                            stopProgressDialogue();
                            CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), getApplicationContext());
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
                Toast.makeText(InstallationInitial.this, error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                5,  /// maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    /*-------------------------------------------------------------Submit Installation-----------------------------------------------------------------------------*/

    private void submitInstalltion() {

        JSONArray ja_invc_data = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        InstallationBean param_invc = new InstallationBean();
        param_invc = db.getInstallationData(pernr, billno);

        try {

            if (!Constant.DBUG_MOB_1.equalsIgnoreCase("")) {
                if (mSimDetailsInfoResponse.size() > 0)
                    mSimDetailsInfoResponse.clear();
                mSimDetailsInfoResponse = mDatabaseHelperTeacher.getSimInfoDATABT(Constant.BILL_NUMBER_UNIC);
            }

            try {

                for (int i = 0; i < mSimDetailsInfoResponse.size(); i++) {

                    if (i == 0)
                        mMOBNUM_1 = mSimDetailsInfoResponse.get(i).getDEVICENOSIMMOB();

                    if (i == 1)
                        mMOBNUM_2 = mSimDetailsInfoResponse.get(i).getDEVICENOSIMMOB();

                    if (i == 2)
                        mMOBNUM_3 = mSimDetailsInfoResponse.get(i).getDEVICENOSIMMOB();

                    Constant.DBUG_MOB_1 = mMOBNUM_1;
                    Constant.DBUG_MOB_2 = mMOBNUM_2;
                    Constant.DBUG_MOB_3 = mMOBNUM_3;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String date_s = param_invc.getInst_date();

            SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");

            Date date = dt.parse(date_s);
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");

            jsonObj.put("userid", param_invc.getPernr());
            if (param_invc.getBeneficiaryNo() != null && !param_invc.getBeneficiaryNo().isEmpty()) {
                jsonObj.put("beneficiary", param_invc.getBeneficiaryNo());
            } else {
                jsonObj.put("beneficiary", BeneficiaryNo);
            }
            jsonObj.put("setting_check", "Para Setting Stop");
            jsonObj.put("project_no", param_invc.getProject_no());
            jsonObj.put("project_login_no", param_invc.getLogin_no());
            jsonObj.put("instdate", dt1.format(date));
            jsonObj.put("total_plate_watt", param_invc.getModule_total_plate_watt());
            jsonObj.put("lat", param_invc.getLatitude());
            jsonObj.put("lng", param_invc.getLongitude());
            jsonObj.put("customer_name", param_invc.getCustomer_name());
            jsonObj.put("father_name", param_invc.getFathers_name());
            jsonObj.put("state", param_invc.getState_ins_id());
            jsonObj.put("city", param_invc.getDistrict_ins_id());
            jsonObj.put("tehsil", param_invc.getTehsil_ins());
            jsonObj.put("village", param_invc.getVillage_ins());
            jsonObj.put("contact_no", param_invc.getMobile_no());
            jsonObj.put("address", param_invc.getAddress_ins());
            jsonObj.put("make", param_invc.getMake_ins());
            jsonObj.put("rms_status", param_invc.getRms_data_status());
            jsonObj.put("SOLAR_PANNEL_WATT ", param_invc.getSolarpanel_wattage());
            jsonObj.put("HP", param_invc.getInst_hp());
            jsonObj.put("PANEL_INSTALL_QTY", param_invc.getSolarpanel_stand_ins_quantity());
            jsonObj.put("TOTAL_WATT", param_invc.getTotal_watt());
            jsonObj.put("PANEL_MODULE_QTY", param_invc.getNo_of_module_qty());
            jsonObj.put("inst_no_of_module_value", param_invc.getNo_of_module_value());
            jsonObj.put("MOTOR_SERNR", param_invc.getSmmd_sno());
            jsonObj.put("PUMP_SERNR", param_invc.getSpmd_sno());
            jsonObj.put("CONTROLLER_SERNR", param_invc.getScm_sno());
            jsonObj.put("SIM_OPRETOR", param_invc.getSimoprator());
            jsonObj.put("SIMNO", param_invc.getSimcard_num());
            jsonObj.put("VBELN", param_invc.getInst_bill_no());
            jsonObj.put("CONNECTION_TYPE", param_invc.getConntype());
            jsonObj.put("REGISNO", param_invc.getRegis_no());
            jsonObj.put("BOREWELLSTATUS", CustomUtility.getSharedPreferences(mContext, "borewellstatus" + billno));
            jsonObj.put("DELAY_REASON", param_invc.getDelay_reason());
            jsonObj.put("dbug_mob_1 ", Constant.DBUG_MOB_1);
            jsonObj.put("dbug_mob_2 ", Constant.DBUG_MOB_2);
            jsonObj.put("dbug_mob_3 ", Constant.DBUG_MOB_3);
            jsonObj.put("dbug_ofline", Constant.DBUG_PER_OFLINE);
            jsonObj.put("dbug_ofline", Constant.DBUG_PER_OFLINE);
            jsonObj.put("app_version", version);

            jsonObj.put("LOGIN_NAME", CustomUtility.getSharedPreferences(getApplicationContext(), Constant.PersonName));
            jsonObj.put("LOGIN_CONT", CustomUtility.getSharedPreferences(getApplicationContext(), Constant.PersonNumber));


            if (imageList.size() > 0) {
                for (int i = 0; i < imageList.size(); i++) {
                    if (imageList.get(i).isImageSelected()) {
                        try {
                            jsonObj.put("PHOTO" + imageList.get(i).getPoistion(), CustomUtility.getBase64FromBitmap(InstallationInitial.this, imageList.get(i).getImagePath()));
                            jsonObj.put("LatLng" + imageList.get(i).getPoistion(), imageList.get(i).getLatitude() + "," + imageList.get(i).getLongitude());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            ja_invc_data.put(jsonObj);


            new syncInstallationData(ja_invc_data).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private class syncInstallationData extends AsyncTask<String, String, String> {

        JSONArray jsonArray;

        public syncInstallationData(JSONArray jaInvcData) {
            jsonArray = jaInvcData;
        }

        @Override
        protected void onPreExecute() {
            stopProgressDialogue();
            showProgressDialogue(getResources().getString(R.string.submittingInatallation));

        }

        @Override
        protected String doInBackground(String... params) {
            String obj2 = null;
            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("installation", String.valueOf(jsonArray)));
            Log.e("DATA", "$$$$" + param1_invc);
            System.out.println("param1_invc_vihu==>>" + param1_invc);
            try {
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.INSTALLATION_DATA, param1_invc);

            } catch (Exception e) {
                e.printStackTrace();
                stopProgressDialogue();
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                Log.e("OUTPUT1", "&&&&" + result);

                if (!result.isEmpty()) {
                    JSONObject object = new JSONObject(result);
                    String obj1 = object.getString("data_return");

                    JSONArray ja = new JSONArray(obj1);

//                    Log.e("OUTPUT2", "&&&&" + ja);

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);

                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");

                        if (invc_done.equals("Y")) {
                            CustomUtility.showToast(InstallationInitial.this, getResources().getString(R.string.dataSubmittedSuccessfully));
                            Log.e("DOCNO", "&&&&" + billno);
                            InstallationDoneSuccessfully();
                        } else {
                            stopProgressDialogue();

                            if (invc_done.equals("N")) {

                                CustomUtility.showToast(InstallationInitial.this, "Data Not Submitted, Please try After Sometime.");

                            } else if (invc_done.equals("P")) {

                                CustomUtility.showToast(InstallationInitial.this, " Controller number mismatch. Please update I-base.");

                            } else if (invc_done.equals("I")) {
                                CustomUtility.setSharedPreference(mContext, "INSTSYNC" + billno, "");

                                CustomUtility.showToast(InstallationInitial.this, "Please Select or Capture All Images First");

                            } else if (invc_done.equals("A")) {

                                CustomUtility.showToast(InstallationInitial.this, "Please Install latest version of the app from the play store");

                            }
                        }
                    }


                } else {
                    stopProgressDialogue();
                    CustomUtility.showToast(InstallationInitial.this, "Data Not Submitted, Please try After Sometime.");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void InstallationDoneSuccessfully() {
        db.deleteInstallationData(billno);
        db.deleteInstallationListData1(billno);
        CustomUtility.setSharedPreference(mContext, "INSTSYNC" + billno, "");
        CustomUtility.setSharedPreference(mContext, "borewellstatus" + billno, "");
        CustomUtility.setSharedPreference(mContext, "borewellstatus", "");

        CustomUtility.setSharedPreference(mContext, "SYNCLIST", "1");

        mDatabaseHelperTeacher.deleteSimInfoData(billno);

        Random random = new Random();
        String generatedVerificationCode = String.format("%04d", random.nextInt(10000));

        if (CustomUtility.isValidMobile(inst_mob_no.getText().toString().trim())) {

            sendVerificationCodeAPI(generatedVerificationCode, inst_mob_no.getText().toString().trim(), inst_hp.getText().toString().trim(), BeneficiaryNo, bill_no.getText().toString());
            CustomUtility.removeValueFromSharedPref(mContext, Constant.isDebugDevice);
        } else {
            Intent intent = new Intent(InstallationInitial.this, PendingInstallationActivity.class);
            startActivity(intent);
            finish();
        }

        mDatabaseHelperTeacher.deleteAllDataFromTable(inst_controller_ser.getText().toString().trim() + "-0");
    }


    /*-------------------------------------------------------------Send OTP to customer-----------------------------------------------------------------------------*/

    private void sendVerificationCodeAPI(String generatedVerificationCode, String ContactNo, String Hp, String beneficiaryNo, String billNo) {
        stopProgressDialogue();
        showProgressDialogue(getResources().getString(R.string.sendingOtpToCustomer));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.SendOTP + "&mobiles=" + ContactNo +
                        "&message=" + beneficiaryNo + " के तहत " + Hp + "HP पंप सेट का इंस्टॉलेशन किया गया है यदि आप संतुष्ट हैं तो इंस्टॉलेशन टीम को OTP-" + generatedVerificationCode + " शेयर करे। शक्ति पम्पस&sender=SHAKTl&unicode=1&route=2&country=91&DLT_TE_ID=1707169744934483345",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                stopProgressDialogue();


                if (!res.toString().isEmpty()) {
                    VerificationCodeModel verificationCodeModel = new Gson().fromJson(res.toString(), VerificationCodeModel.class);
                    if (verificationCodeModel.getStatus().equals("Success")) {
                        CustomUtility.removeValueFromSharedPref(getApplicationContext(), Constant.deviceStatus);
                        db.deleteInstallationImages(bill_no.getText().toString().trim());
                        ShowAlertResponse(generatedVerificationCode, ContactNo, Hp, beneficiaryNo, billNo);
                    }

                }

            }
        }, error -> {
            stopProgressDialogue();
            Log.e("error", String.valueOf(error));
            Toast.makeText(InstallationInitial.this, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void ShowAlertResponse(String generatedVerificationCode, String ContactNo, String Hp, String beneficiaryNo, String billNo) {
        LayoutInflater inflater = (LayoutInflater) InstallationInitial.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InstallationInitial.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);

        title_txt.setText(getResources().getString(R.string.otp_send_successfully));

        OK_txt.setOnClickListener(v -> {
            alertDialog.dismiss();
            Intent intent = new Intent(InstallationInitial.this, PendingInsUnlOTPVerification.class);
            intent.putExtra(Constant.PendingFeedbackContact, ContactNo);
            intent.putExtra(Constant.PendingFeedbackVblen, billNo);
            intent.putExtra(Constant.PendingFeedbackHp, Hp);
            intent.putExtra(Constant.PendingFeedbackBeneficiary, beneficiaryNo);
            intent.putExtra(Constant.VerificationCode, generatedVerificationCode);
            intent.putExtra(Constant.isUnloading, "false");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        });

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

    private void ShowToast(String message) {
        Message msg = new Message();
        msg.obj = message;
        mHandler2.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopProgressDialogue();
    }
}