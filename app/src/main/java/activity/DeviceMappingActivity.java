package activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static utility.FileUtils.getPath;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import adapter.ImageSelectionAdapter;
import bean.DeviceDetailModel;
import bean.DeviceMappingModel;
import bean.ImageModel;
import bean.InstallationBean;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import debugapp.PendingInstallationModel;
import debugapp.VerificationCodeModel;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class DeviceMappingActivity extends AppCompatActivity implements View.OnClickListener, ImageSelectionAdapter.ImageSelectionListener {

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<String> itemNameList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();

    RecyclerView recyclerview;
    LinearLayout deviceOnlineLinear, deviceOfflineLinear;
    TextView write_btn, read_btn, updateDeviceBtn, countDownTimerTxt, checkDeviceStatusBtn, btnSave;
    EditText remarkExt;
    ImageView writeImg, read_img;
    CountDownTimer timer;
    Toolbar mToolbar;

    DatabaseHelper databaseHelper;
    PendingInstallationModel.Response pendingInstallationData;
    InstallationBean installationBean;

    String billNo = "", beneficiaryNo = "", contactNo = "", hp = "", regisNo = "", controllerSerialNo = "7F-0135-0-13-06-23-0", customerName = "", customerMobile = "";

    int selectedIndex;
    boolean isUpdate = false;
    AlertDialog alertDialog;

    ProgressDialog progressDialog;
    ImageSelectionAdapter customAdapter;

    List<DeviceMappingModel> deviceMappingList = new ArrayList<>();
    List<DeviceMappingModel> deviceMappingData = new ArrayList<>();

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
        checkDeviceStatusBtn = findViewById(R.id.checkDeviceStatusBtn);
        recyclerview = findViewById(R.id.recycler_view);
        deviceOnlineLinear = findViewById(R.id.deviceOnlineLinear);
        deviceOfflineLinear = findViewById(R.id.deviceOfflineLinear);
        remarkExt = findViewById(R.id.RemarkExt);
        btnSave = findViewById(R.id.btnSave);

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
        checkDeviceStatusBtn.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {
            if (Objects.equals(getIntent().getStringExtra(Constant.deviceMappingData2), "1")) {
                installationBean = (InstallationBean) getIntent().getSerializableExtra(Constant.deviceMappingData);
                billNo = installationBean.getInst_bill_no();
                beneficiaryNo = installationBean.getBeneficiaryNo();
                contactNo = installationBean.getMobile_no();
                hp = installationBean.getInst_hp();
                regisNo = installationBean.getRegis_no();
                // controllerSerialNo = installationBean.getScm_sno() + "-0";
                customerName = installationBean.getCustomer_name();
                customerMobile = installationBean.getMobile_no();
            }

            if (Objects.equals(getIntent().getStringExtra(Constant.deviceMappingData2), "2")) {
                pendingInstallationData = (PendingInstallationModel.Response) getIntent().getSerializableExtra(Constant.deviceMappingData);

                billNo = pendingInstallationData.getVbeln();
                beneficiaryNo = pendingInstallationData.getBeneficiary();
                contactNo = pendingInstallationData.getContactNo();
                hp = pendingInstallationData.getHp();
                regisNo = pendingInstallationData.getRegisno();
                //  controllerSerialNo = pendingInstallationData.getControllerSernr() + "-0";
                customerName = pendingInstallationData.getCustomerName();
                customerMobile = pendingInstallationData.getContactNo();

                Log.e("controllerSerialNo====>", controllerSerialNo);
            }
            if(CustomUtility.isInternetOn(DeviceMappingActivity.this)){
                getDeviceOnlineStatus();
            }else {
                CustomUtility.showToast(DeviceMappingActivity.this,getResources().getString(R.string.check_internet_connection));
            }
        }
    }


    private void startCountDownTimer() {
        timer = new CountDownTimer(900000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                countDownTimerTxt.setText("Please Wait \n" + f.format(min) + " Min. " + f.format(sec) + " Sec. \nDevice Installing latest vesion ");
            }

            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                countDownTimerTxt.setVisibility(View.GONE);
                changeButtonVisibility("4");

            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_btn:
                if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                    write_read_fotaAPI("254", "1", "0", "0", "1");
                } else {
                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.check_internet_connection));
                }
                break;
            case R.id.read_btn:
                if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                    write_read_fotaAPI("254", "0", "1.0", "1.0", "2");
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
                if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                    checkDeviceShiftingStatusAPI();
                } else {
                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.check_internet_connection));
                }

                break;
            case R.id.btnSave:
                if (CustomUtility.isInternetOn(DeviceMappingActivity.this)) {
                    if (!imageArrayList.get(0).isImageSelected()) {
                        CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.select_image));
                    } else if (remarkExt.getText().toString().trim().isEmpty()) {
                        CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.enter_remark));

                    } else {
                        submitOfflineDeviceData();
                    }
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
                DeviceMappingModel deviceMappingModel2 = new DeviceMappingModel();
                deviceMappingModel2.setRead("true");
                deviceMappingModel2.setWrite("true");
                deviceMappingModel2.setUpdate("true");
                deviceMappingModel2.setBillNo(billNo);
                insUpdateData(deviceMappingModel2, true);

                startCountDownTimer();
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
        }
    }

    private void insUpdateData(DeviceMappingModel deviceMappingModel, boolean isUpdate) {
        if (isUpdate) {
            databaseHelper.updateDeviceMappingData(deviceMappingModel);
        } else {
            databaseHelper.insertDeviceMappingData(deviceMappingModel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCountDownTImer();
    }

    private void stopCountDownTImer() {
        if (timer != null) {
            timer.cancel();
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

        customAdapter = new ImageSelectionAdapter(DeviceMappingActivity.this, imageArrayList);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);
        deviceOfflineLinear.setVisibility(View.VISIBLE);
        deviceOnlineLinear.setVisibility(View.GONE);
    }

    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;
        if (imageModelList.isImageSelected()) {
            isUpdate = true;
            selectImage("1");
        } else {
            isUpdate = false;
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
            gallery.setVisibility(View.GONE);
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

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , mImageCaptureUri);
                    File file1 = CustomUtility.saveFile(bitmap,customerName.trim(),"Images");

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

        if (isUpdate) {
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
                            setDeviceData();
                            deviceOnlineLinear.setVisibility(View.VISIBLE);
                            deviceOfflineLinear.setVisibility(View.GONE);
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
                deviceMappingModel.setBillNo(deviceMappingData.get(i).getBillNo());
                deviceMappingList.add(deviceMappingModel);
            }


        }

        Log.e("deviceMappingList=======>", String.valueOf(deviceMappingList.size()));
        Log.e("deviceMappingList======>", deviceMappingList.toString());

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
                                if (CustomUtility.isValidMobile(customerMobile)) {

                                    Random random = new Random();
                                    String generatedVerificationCode = String.format("%04d", random.nextInt(10000));

                                    sendVerificationCodeAPI(generatedVerificationCode, customerMobile, hp, beneficiaryNo,billNo);

                                } else {
                                    stopProgressDialogue();
                                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.mobile_number_not_valid));
                                }
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
        }else if (value.equals("3")) {
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

    private void checkDeviceShiftingStatusAPI() {
        stopProgressDialogue();
        showProgressDialogue(getResources().getString(R.string.checkDeviceShiftingStatus));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                CustomUtility.getSharedPreferences(this, Constant.RmsBaseUrl) + WebURL.deviceShiftingStatusCheck + "?deviceno=" + controllerSerialNo,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                stopProgressDialogue();
                if (!res.toString().isEmpty()) {
                    try {
                        if(res.getString("status").equals("true")){
                            showingMessage(getResources().getString(R.string.device_shifting_successfully));

                            runOnUiThread(() -> {
                                if (CustomUtility.isValidMobile(customerMobile)) {

                                    Random random = new Random();
                                    String generatedVerificationCode = String.format("%04d", random.nextInt(10000));

                                    sendVerificationCodeAPI(generatedVerificationCode, customerMobile, hp, beneficiaryNo,billNo);

                                } else {
                                    CustomUtility.showToast(DeviceMappingActivity.this, getResources().getString(R.string.mobile_number_not_valid));
                                }
                            });
                        }else {
                            showingMessage(getResources().getString(R.string.device_shifting_successfully));

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        }, error -> {
            stopProgressDialogue();
            Log.e("error", String.valueOf(error));
            Toast.makeText(DeviceMappingActivity.this, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        });
        requestQueue.add(jsonObjectRequest);
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
                    databaseHelper.deleteOfflineControllerImages(billNo);
                    databaseHelper.deleteDeviceMappingRecords(billNo);
                    VerificationCodeModel verificationCodeModel = new Gson().fromJson(res.toString(), VerificationCodeModel.class);
                    if (verificationCodeModel.getStatus().equals("Success")) {
                        ShowAlertResponse(generatedVerificationCode, ContactNo, Hp, beneficiaryNo, billNo);
                    }

                }

            }
        }, error -> {
            stopProgressDialogue();
            Log.e("error", String.valueOf(error));
            Toast.makeText(DeviceMappingActivity.this, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void ShowAlertResponse(String generatedVerificationCode, String ContactNo, String Hp, String beneficiaryNo, String billNo) {
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

        title_txt.setText(getResources().getString(R.string.otp_send_successfully));

        OK_txt.setOnClickListener(v -> {
            alertDialog.dismiss();
            Intent intent = new Intent(DeviceMappingActivity.this, PendingInsUnlOTPVerification.class);
            intent.putExtra(Constant.PendingFeedbackContact, ContactNo);
            intent.putExtra(Constant.PendingFeedbackVblen, billNo);
            intent.putExtra(Constant.PendingFeedbackHp, Hp);
            intent.putExtra(Constant.PendingFeedbackBeneficiary, beneficiaryNo);
            intent.putExtra(Constant.VerificationCode, generatedVerificationCode);
            intent.putExtra(Constant.isUnloading, "false");
            intent.putExtra(Constant.regisno, regisNo);

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
    private void showingMessage(String message) {
        runOnUiThread(new Runnable() {
            public void run() {

                CustomUtility.showToast(DeviceMappingActivity.this, message);

            }
        });
    }

}