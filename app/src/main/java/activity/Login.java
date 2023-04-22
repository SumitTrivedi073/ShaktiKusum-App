package activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.PREVIEW_SDK_INT;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
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

import bean.LoginBean;
import ch.acra.acra.BuildConfig;
import database.DatabaseHelper;
import debugapp.OTPGenerationActivity;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

@SuppressWarnings({"ResultOfMethodCallIgnored", "resource"})
public class Login extends AppCompatActivity {

    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 100;

    private static final int REQUEST_CODE_PERMISSION = 2;
    Spinner spinner_login_type, spinner_project_type;
    ProgressDialog progressBar;
    int index, index1;
    String username, password, login, userid, usertype, spinner_login_type_text, spinner_project_type_text, spinner_proj_id, spinner_login_id;
    List<String> list = null;
    List<String> projectlist = null;
    List<String> loginlist = null;
    Context context;
    DatabaseHelper dataHelper;
    String country, country_text, state, state_text, district, district_text, tehsil, tehsil_text;
    @SuppressLint("HandlerLeak")
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(Login.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private int progressBarStatus = 0;
    private final Handler progressBarHandler = new Handler();
    private ProgressDialog progressDialog;
    private EditText inputName, inputPassword;
    private TextInputLayout inputLayoutName, inputLayoutPassword;

    boolean BluetoothConnect,BluetoothScan,FineLocationAccepted,CoarseLocationAccepted,Bluetooth,ReadPhoneState,Camera,ReadPhoneStorage,WritePhoneStorage;



    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        dataHelper = new DatabaseHelper(context);



        list = new ArrayList<>();
        projectlist = new ArrayList<>();
        loginlist = new ArrayList<>();

        inputLayoutName =  findViewById(R.id.input_layout_name);
        inputLayoutPassword =  findViewById(R.id.input_layout_password);

        inputName =  findViewById(R.id.login_Et);
        inputPassword =  findViewById(R.id.password);

        TextView btnSignUp =  findViewById(R.id.btn_signup);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        spinner_login_type =  findViewById(R.id.spinner_login_type);
        spinner_project_type =  findViewById(R.id.spinner_project_type);

        spinner_project_type.setPrompt("Select Project");
        spinner_login_type.setPrompt("Select Login");

        projectlist.clear();
        projectlist = dataHelper.getList(DatabaseHelper.KEY_PROJ_TXT, null);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(context, R.layout.spinner_item_center, projectlist);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(R.layout.spinner_item_center);

        // attaching data adapter to spinner
        spinner_project_type.setAdapter(dataAdapter1);


        spinner_project_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                spinner_project_type_text = spinner_project_type.getSelectedItem().toString();
                Log.e("spinner_project_type_text","list"+ spinner_project_type_text);

                if (!spinner_project_type_text.equalsIgnoreCase("Select project type") && !TextUtils.isEmpty(spinner_project_type_text)) {
                    loginlist.clear();
                    loginlist = dataHelper.getList(DatabaseHelper.KEY_LOGIN_TXT, spinner_project_type_text);

                    Log.e("spinner_project_type_text1==","list"+ loginlist);
                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, R.layout.spinner_item_center, loginlist);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

                    // attaching data adapter to spinner
                    spinner_login_type.setAdapter(dataAdapter);

                    spinner_proj_id = dataHelper.getProjLoginValue(DatabaseHelper.KEY_PROJ_ID, spinner_project_type_text);

                    CustomUtility.setSharedPreference(context, "projectid", spinner_proj_id);

                    System.out.println("spinner_proj_id==>>"+spinner_proj_id);

                    String projnametxt = spinner_project_type.getSelectedItem().toString();

                    CustomUtility.setSharedPreference(context, "projectname", projnametxt);

                    Log.e("ID", "&&&" + spinner_proj_id);

                    spinner_login_type.setSelection(0);

                    spinner_login_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                            index1 = arg0.getSelectedItemPosition();
                            spinner_login_type_text = spinner_login_type.getSelectedItem().toString();
                            if (!spinner_login_type_text.equalsIgnoreCase("Select login type") && !TextUtils.isEmpty(spinner_login_type_text)) {

                                spinner_login_id = dataHelper.getProjLoginValue(DatabaseHelper.KEY_LOGIN_ID, spinner_login_type_text);

                                CustomUtility.setSharedPreference(context, "loginid", spinner_login_id);

                                Log.e("ID1", "&&&" + spinner_login_id);

                                if(spinner_login_id.equalsIgnoreCase("02"))
                                {
                                    if (CustomUtility.isInternetOn()) {
                                        // Write Your Code What you want to do
                                        syncState();
                                    } else {
                                        Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });

                } else {
                    Toast.makeText(context, "Please Select Project Type.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        btnSignUp.setOnClickListener(view -> {


            if (validateType()) {
                if (validateType1()) {

                        if (CustomUtility.isInternetOn()) {
                            // Write Your Code What you want to do
                            submitForm();
                        } else {
                            Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                        }


                    }
                }

        });
        getUserTypeValue();
        //InappUpdate
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkUpdate();


    }

    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if  (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, Login.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }

        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    serverLogin();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    serverLogin();
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    askNotificationPermission();
                }
            });
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                serverLogin();
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
                showNotificationPopup();
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void showNotificationPopup() {
        LayoutInflater inflater = (LayoutInflater) Login.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.notification_popup, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(Login.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

        LinearLayout okLinear = layout.findViewById(R.id.okLinear);
        LinearLayout cancelLinear = layout.findViewById(R.id.cancelLinear);

        okLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        });

        cancelLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                askNotificationPermission();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public String getJson() {
        String json = null;
        try {
            // Opening cities.json file
            InputStream is = getAssets().open("state_district.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    /**********************************************************************************************
     *                Server Login
     *********************************************************************************************/

    private void serverLogin() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        username = inputName.getText().toString();
        password = inputPassword.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("USERID", username));
        param.add(new BasicNameValuePair("PASSWORD", password));
        param.add(new BasicNameValuePair("project_no", CustomUtility.getSharedPreferences(context, "projectid")));
        param.add(new BasicNameValuePair("project_login_no", CustomUtility.getSharedPreferences(context, "loginid")));
        param.add(new BasicNameValuePair("DEVICE_NAME", CustomUtility.getDeviceName()));
        param.add(new BasicNameValuePair("APP_VERSION", BuildConfig.VERSION_NAME));
        param.add(new BasicNameValuePair("API", String.valueOf(Build.VERSION.SDK_INT)));
        param.add(new BasicNameValuePair("API_VERSION", Build.VERSION.RELEASE));

//******************************************************************************************/
/*                   server connection
/******************************************************************************************/
        progressDialog = ProgressDialog.show(Login.this, "", "Connecting to server..please wait !");

        new Thread(() -> {

            try {

                String obj = CustomHttpClient.executeHttpPost1(WebURL.LOGIN_PAGE, param);
                Log.d("check_error", obj);
                Log.e("check_error", obj);

//******************************************************************************************/
/*                       get JSONwebservice Data
/******************************************************************************************/
                JSONObject object = new JSONObject(obj);
                String obj1 = object.getString("login_msg");


                JSONArray ja = new JSONArray(obj1);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    login = jo.getString("login");
                    userid = jo.getString("user");
                    username = jo.getString("name");
                    usertype = jo.getString("type");

                }
//******************************************************************************************/
/*                       Call DashBoard
/******************************************************************************************/


                if ("Y".equals(login)) {

                    DatabaseHelper dataHelper = new DatabaseHelper(Login.this);

                    LoginBean loginBean = new LoginBean(userid, username, usertype);

                    dataHelper.insertLoginData(context, loginBean);

                        // dismiss the progress dialog
                        progressDialog.dismiss();


                    CustomUtility.setSharedPreference(context, "userid", userid);
                    CustomUtility.setSharedPreference(context, "username", username);
                    CustomUtility.setSharedPreference(context, "usertype", usertype);

                        Intent intent = new Intent(Login.this, OTPGenerationActivity.class);
                        startActivity(intent);
                        finish();

                } else {

                    progressDialog.dismiss();

//*********************create message in thread*******************************/
                    Message msg = new Message();
                    msg.obj = "Invalid username or password";
                    mHandler.sendMessage(msg);

                    // dismiss the progress dialog

                }

            } catch (Exception e) {

                e.printStackTrace();
                progressDialog.dismiss();

            }

        }).start();
    }

    /**********************************************************************************************
     *                Validating form
     *********************************************************************************************/
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void submitForm() {

        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

//********************   Server Login    *******************************************************/

        if (checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                askNotificationPermission();
            } else {
                serverLogin();
            }

        } else {
           requestPermission();

        }
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void getUserTypeValue() {
        list.add("Select Type");
        list.add("Shakti Employee");
        list.add("Shakti Partner");
        list.add("Other");
    }

    private boolean validateType() {

        boolean value;
        if (index == 0) {
            Toast.makeText(this, "Please Select Project Type", Toast.LENGTH_LONG).show();
            value = false;
        } else {
            value = true;
        }

        return value;
    }

    private boolean validateType1() {

        boolean value;
        if (index1 == 0) {
            Toast.makeText(this, "Please Select Login Type", Toast.LENGTH_LONG).show();
            value = false;
        } else {
            value = true;
        }

        return value;
    }


    public void syncState() {

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Downloading State Data...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;


        new Thread(() -> {

            while (progressBarStatus < 100) {
                // performing operation

                try {

                    if (CustomUtility.isInternetOn()) {
                        progressBarStatus = 30;

                        // Updating the progress bar
                        progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                        // Get State search help Data
                        getStateData(Login.this);
                        progressBarStatus = 100;

                        // Updating the progress bar
                        progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));
                    } else {
                        Toast.makeText(getApplicationContext(), "No internet Connection ", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            if (progressBarStatus >= 100) {
                // sleeping for 1 second after operation completed
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // close the progress bar dialog
                progressBar.dismiss();

            }
        }).start();

    }

    public void getStateData(Context context) {

        DatabaseHelper dataHelper = new DatabaseHelper(context);

        final ArrayList<NameValuePair> param = new ArrayList<>();

        try {

            String obj = CustomHttpClient.executeHttpPost1(WebURL.STATE_DATA, param);

            JSONArray ja_state = new JSONArray(obj);

            dataHelper.deleteStateSearchHelpData();

            for (int i = 0; i < ja_state.length(); i++) {

                JSONObject jo_state = ja_state.getJSONObject(i);

                country = jo_state.optString("country");
                country_text = jo_state.optString("countrytext");
                state = jo_state.optString("state");
                state_text = jo_state.optString("statetext");
                district = jo_state.optString("district");
                district_text = jo_state.optString("districttext");
                tehsil = jo_state.optString("tehsil");
                tehsil_text = jo_state.optString("tehsiltext");

                dataHelper.insertStateData(country, country_text, state, state_text, district, district_text, tehsil, tehsil_text);
            }

        } catch (Exception e) {
            Log.d("msg", "" + e);
        }

    }


    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (view.getId() == R.id.login_Et) {
                validateName();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkPermission() {
        int FineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int CoarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int Bluetooth = ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH);
        int PhoneState = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int Camera = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int MangeExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), MANAGE_EXTERNAL_STORAGE);
        int ReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int WriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ReadMediaImages = ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES);
        int ReadMediaAudio = ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_AUDIO);
        int BluetoothConnect =  ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH_CONNECT);
        int BluetoothScan =  ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH_SCAN);


        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return CoarseLocation == PackageManager.PERMISSION_GRANTED && Bluetooth == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED  && ReadMediaImages == PackageManager.PERMISSION_GRANTED
                    && ReadMediaAudio == PackageManager.PERMISSION_GRANTED && BluetoothConnect == PackageManager.PERMISSION_GRANTED
                    && BluetoothScan == PackageManager.PERMISSION_GRANTED;
        }else  if  ( SDK_INT == Build.VERSION_CODES.S || SDK_INT == Build.VERSION_CODES.R)  {
            return  Camera == PackageManager.PERMISSION_GRANTED
                    && CoarseLocation == PackageManager.PERMISSION_GRANTED  && PhoneState == PackageManager.PERMISSION_GRANTED
                    && Bluetooth == PackageManager.PERMISSION_GRANTED   && BluetoothScan == PackageManager.PERMISSION_GRANTED
                    && BluetoothConnect == PackageManager.PERMISSION_GRANTED;
        } else {
            return FineLocation == PackageManager.PERMISSION_GRANTED && CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Bluetooth == PackageManager.PERMISSION_GRANTED && PhoneState == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && WriteExternalStorage == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,  Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_MEDIA_AUDIO,
                            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.BLUETOOTH ,
                            Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        } if  ( SDK_INT == Build.VERSION_CODES.S || SDK_INT == Build.VERSION_CODES.R)  {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,  Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_CONNECT , Manifest.permission.BLUETOOTH_SCAN },
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0) {

                    if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                        boolean CoarseLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean Bluetooth = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean  Camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean  ReadMediaImages = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                        boolean  ReadMediaAudio = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                        boolean BluetoothConnect = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                        boolean BluetoothScan =grantResults[6] == PackageManager.PERMISSION_GRANTED;

                        if (BluetoothConnect && BluetoothScan && CoarseLocationAccepted && Bluetooth  && Camera && ReadMediaImages && ReadMediaAudio) {
                            // perform action when allow permission success
                                askNotificationPermission();
                        }else {
                           requestPermission();
                        }
                    } else  if ( SDK_INT == Build.VERSION_CODES.S || SDK_INT == Build.VERSION_CODES.R) {
                        boolean Camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean AccessCoarseLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean Bluetooth = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean BluetoothConnect = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                        boolean BluetoothScan = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                        boolean  ReadPhoneState = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                        if (Camera && ReadPhoneState && AccessCoarseLocation && Bluetooth &&  BluetoothConnect && BluetoothScan) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                                startActivityForResult(intent, 2296);
                            } catch (Exception e) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivityForResult(intent, 2296);
                            }
                        } else {
                            requestPermission();
                        }
                    }else  if ( SDK_INT <= Build.VERSION_CODES.Q) {
                        boolean  FineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean CoarseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean Bluetooth = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean  ReadPhoneState = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                        boolean  Camera = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadPhoneStorage = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                        boolean WritePhoneStorage = grantResults[6] == PackageManager.PERMISSION_GRANTED;


                        if(FineLocationAccepted && CoarseLocationAccepted && Bluetooth && ReadPhoneState && Camera && ReadPhoneStorage && WritePhoneStorage ){
                            serverLogin();
                        }else {
                            requestPermission();
                        }
                    }
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
