package activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static utility.FileUtils.getPath;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import bean.InstallationBean;
import bean.InstallationListBean;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import debugapp.VerificationCodeModel;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class UnloadInstReportImageActivity extends BaseActivity implements ImageSelectionAdapter.ImageSelectionListener {

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    ArrayList<String> scannedDeviceNo = new ArrayList<>();
    RecyclerView recyclerview;
    EditText remarkEdt;
    TextView inst_module_ser_no, pumpSerNo, motorSerNo, controllerSerNo;

    ImageView pump_scanner, motor_scanner, controllerScanner;
    LinearLayout moduleOneLL;
    Button btnSave;
    AlertDialog alertDialog;
    int selectedIndex;
    ImageSelectionAdapter customAdapter;
    boolean isSubmit = false;
    List<String> itemNameList = new ArrayList<>();
    String customerName, beneficiary, regNO, projectNo, userID, billNo, moduleqty, custMobile, regisno,
            no_of_module_value, noOfModules = "", Hp,unloadingMaterialStatus="";
    int value, currentScannerFor = -1;
    Toolbar mToolbar;
    boolean isUpdate = false, isPumpMotorController = false;
    InstallationListBean installationListBean;
    RadioButton materialStatusOk, materialStatusNotOk, materialStatusDamage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unload_instreport_image);
        Init();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onResume() {
        super.onResume();
        CheakPermissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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
                            Toast.makeText(UnloadInstReportImageActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean writeExternalStorage =
                                grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadExternalStorage =
                                grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (!ACCESSCAMERA && !writeExternalStorage && !ReadExternalStorage) {
                            Toast.makeText(UnloadInstReportImageActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }

                    }
                }

                break;
        }
    }

    private void Init() {
        recyclerview = findViewById(R.id.recyclerview);
        remarkEdt = findViewById(R.id.edtRemarkVKID);
        inst_module_ser_no = findViewById(R.id.module_serial_no);
        moduleOneLL = findViewById(R.id.layout_one);
        pumpSerNo = findViewById(R.id.pumpSerNo);
        motorSerNo = findViewById(R.id.motorSerNo);
        controllerSerNo = findViewById(R.id.controllerSerNo);
        btnSave = findViewById(R.id.btnSave);
        pump_scanner = findViewById(R.id.pump_scanner);
        motor_scanner = findViewById(R.id.motor_scanner);
        controllerScanner = findViewById(R.id.controllerScanner);
        materialStatusOk = findViewById(R.id.materialStatusOk);
        materialStatusNotOk = findViewById(R.id.materialStatusNotOk);
        materialStatusDamage = findViewById(R.id.materialStatusDamage);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.material_unloading_img));

        retriveValue();
        SetAdapter();
        listner();
    }

    private void retriveValue() {
        Bundle bundle = getIntent().getExtras();

        installationListBean = (InstallationListBean) bundle.getSerializable(Constant.unloadingData);
        Log.e("installationListBean===>", installationListBean.customer_name);
        customerName = installationListBean.customer_name;
        userID = CustomUtility.getSharedPreferences(UnloadInstReportImageActivity.this, "userid");
        billNo = installationListBean.billno;
        moduleqty = installationListBean.moduleqty;
        custMobile = installationListBean.getCUS_CONTACT_NO();
        regisno = installationListBean.regisno;
        Hp = installationListBean.getHP();
        beneficiary = WebURL.BenificiaryNo_Con;
        regNO = WebURL.RegNo_Con;
        projectNo = WebURL.ProjectNo_Con;

        inst_module_ser_no.setText(moduleqty);
        no_of_module_value = GetDataOne();

        if (!TextUtils.isEmpty(moduleqty)) {
            if (moduleqty.length() != 0 && !moduleqty.equals("0")) {
                value = Integer.parseInt(moduleqty);
                ViewInflate(value);
            }
        }


    }

    private void listner() {
        btnSave.setOnClickListener(view -> {

            if (imageArrayList != null && imageArrayList.size() > 0) {
                if (!imageArrayList.get(0).isImageSelected()) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.selectLR_photo));
                } else if (!imageArrayList.get(1).isImageSelected()) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.selectInvoicePhoto));
                } else if (!imageArrayList.get(2).isImageSelected()) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.selectCustomerPhoto));
                }   else {
                    Set<String> set = new HashSet<>();
                    if (!inst_module_ser_no.getText().toString().trim().equals("0")) {

                        for (int i = 0; i < moduleOneLL.getChildCount(); i++) {
                            EditText edit_O = moduleOneLL.getChildAt(i).findViewById(R.id.view_edit_one);

                            if (edit_O.getText().toString().isEmpty()) {
                                CustomUtility.ShowToast(getResources().getString(R.string.enter_allModuleSrno), getApplicationContext());
                                isSubmit = false;
                            } else {
                                if (set.contains(edit_O.getText().toString().toUpperCase())) {
                                    isSubmit = false;
                                    CustomUtility.ShowToast(edit_O.getText().toString() + getResources().getString(R.string.moduleMultipleTime), this);
                                    Log.e(edit_O.getText().toString(), " is duplicated");
                                    break;
                                } else {
                                    set.add(edit_O.getText().toString().toUpperCase());

                                }
                            }
                        }
                        noOfModules = "";
                        for (String ele : set) {
                            if (noOfModules.isEmpty()) {
                                noOfModules = ele;
                            } else {
                                noOfModules = noOfModules + "," + ele;
                            }

                        }
                        if (set.size() == moduleOneLL.getChildCount()) {
                            isSubmit = true;
                        }


                    }

                    if (CustomUtility.isInternetOn(getApplicationContext())) {
                        if (isSubmit) {
                            if (pumpSerNo.getText().toString().trim().isEmpty() || !pumpSerNo.getText().toString().trim().equals(installationListBean.getPump_ser().trim())) {
                                CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.correctPumpSr) +" "+ installationListBean.getPump_ser().trim());
                            } else if (motorSerNo.getText().toString().trim().isEmpty() || !motorSerNo.getText().toString().trim().equals(installationListBean.getMotor_ser().trim())) {
                                CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.correctMotorSr)+" "+ installationListBean.getMotor_ser().trim());
                            } else if (controllerSerNo.getText().toString().trim().isEmpty() || !controllerSerNo.getText().toString().trim().equals(installationListBean.getController_ser().trim())) {
                                CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.correctControllerSr)+" "+ installationListBean.getController_ser().trim());
                            }else if (remarkEdt.getText().toString().trim().isEmpty()) {
                                CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.writeRemark));
                            }else {
                                if(materialStatusOk.isChecked()){
                                    unloadingMaterialStatus = materialStatusOk.getText().toString();
                                }
                                if(materialStatusNotOk.isChecked()){
                                    unloadingMaterialStatus = materialStatusNotOk.getText().toString();
                                }
                                if(materialStatusDamage.isChecked()){
                                    unloadingMaterialStatus = materialStatusDamage.getText().toString();
                                }
                                InstallationBean param_invc = new InstallationBean();
                                JSONArray ja_invc_data = new JSONArray();
                                JSONObject jsonObj = new JSONObject();
                                try {
                                    SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");
                                    jsonObj.put("userid", userID);
                                    jsonObj.put("vbeln", billNo);
                                    jsonObj.put("project_no", projectNo);
                                    jsonObj.put("beneficiary", beneficiary);
                                    jsonObj.put("regisno", regNO);
                                    jsonObj.put("unload_remark", remarkEdt.getText().toString().trim());
                                    jsonObj.put("customer_name ", customerName);
                                    jsonObj.put("project_login_no ", CustomUtility.getSharedPreferences(UnloadInstReportImageActivity.this, "loginid"));
                                    jsonObj.put("inst_no_of_module_value ", noOfModules);
                                    jsonObj.put("UNLOAD_MAT_STATS ", unloadingMaterialStatus);

                                    if (imageArrayList.size() > 0) {
                                        if (imageArrayList.get(0).isImageSelected()) {
                                            jsonObj.put("unld_photo1", CustomUtility.getBase64FromBitmap(UnloadInstReportImageActivity.this, imageArrayList.get(0).getImagePath()));
                                        }
                                        if (1 < imageArrayList.size() && imageArrayList.get(1).isImageSelected()) {
                                            jsonObj.put("unld_photo2", CustomUtility.getBase64FromBitmap(UnloadInstReportImageActivity.this, imageArrayList.get(1).getImagePath()));
                                        }
                                        if (2 < imageArrayList.size() && imageArrayList.get(2).isImageSelected()) {
                                            jsonObj.put("unld_photo3", CustomUtility.getBase64FromBitmap(UnloadInstReportImageActivity.this, imageArrayList.get(2).getImagePath()));
                                        }
                                    }
                                    ja_invc_data.put(jsonObj);
                                    Log.e("ja_invc_data=====>",ja_invc_data.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                               new SyncInstallationData(ja_invc_data).execute();
                            }
                        }
                    } else {
                        CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), getApplicationContext());
                    }


                }
            } else {
                CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.select_image));

            }
        });
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        pump_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPumpMotorController = true;
                startScanner(1000);
            }
        });
        motor_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPumpMotorController = true;
                startScanner(2000);
            }
        });

        controllerScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPumpMotorController = true;
                startScanner(3000);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.selectLR_photo));
        itemNameList.add(getResources().getString(R.string.selectInvoicePhoto));
        itemNameList.add(getResources().getString(R.string.selectCustomerPhoto));

        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo("");
            imageArrayList.add(imageModel);
        }

        imageList = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(this);

        imageList = db.getAllUnloadingImages();


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
        customAdapter = new ImageSelectionAdapter(UnloadInstReportImageActivity.this, imageArrayList);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);

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
        LayoutInflater inflater = (LayoutInflater) UnloadInstReportImageActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(UnloadInstReportImageActivity.this, R.style.MyDialogTheme);

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
            title.setText(getResources().getString(R.string.select_image));
            gallery.setText(getResources().getString(R.string.gallery));
            gamera.setText(getResources().getString(R.string.camera));
        } else {
            title.setText(getResources().getString(R.string.want_to_perform));
            gallery.setText(getResources().getString(R.string.display));
            gamera.setText(getResources().getString(R.string.change));
        }

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (value.equals("0")) {
                    galleryIntent();
                } else {
                    Intent i_display_image = new Intent(UnloadInstReportImageActivity.this, PhotoViewerActivity.class);
                    i_display_image.putExtra("image_path", imageArrayList.get(selectedIndex).getImagePath());
                    startActivity(i_display_image);
                }
            }
        });

        gamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (value.equals("0")) {
                    cameraIntent();
                } else {
                    selectImage("0");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_FILE);
    }

    private void cameraIntent() {

        camraLauncher.launch(new Intent(UnloadInstReportImageActivity.this, CameraActivity2.class)
                .putExtra("cust_name", customerName));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();
                            Log.e("bundle====>", bundle.get("data").toString());
                            UpdateArrayList(bundle.get("data").toString());

                        }

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
                String path = getPath(UnloadInstReportImageActivity.this, mImageCaptureUri); // From Gallery
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
                    Toast.makeText(UnloadInstReportImageActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                } else {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , mImageCaptureUri);
                   File file1 = CustomUtility.saveFile(bitmap,customerName.trim(),"Images");

                    UpdateArrayList(file1.getPath());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            setDataToViewFromScanner(scanningResult);
        }

    }

    private void UpdateArrayList(String path) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageModel.setBillNo(billNo);
        imageArrayList.set(selectedIndex, imageModel);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        if (isUpdate) {
            db.updateUnloadingAlternate(imageArrayList.get(selectedIndex).getName(), path,
                    true, billNo);
        } else {
            db.insertUnloadingImage(imageArrayList.get(selectedIndex).getName(), path,
                    true, billNo);
        }


        customAdapter.notifyDataSetChanged();


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @SuppressLint("StaticFieldLeak")
    private class SyncInstallationData extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
         JSONArray jsonArray;
        public SyncInstallationData(JSONArray jaInvcData) {
            jsonArray = jaInvcData;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UnloadInstReportImageActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Sending Data to server..please wait !");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String obj2 = null;
            Log.e("Param====>", jsonArray.toString());
            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("unloading", String.valueOf(jsonArray)));
            Log.e("DATA", "$$$$" + param1_invc);
            System.out.println("param1_invc_vihu==>>" + param1_invc);
          try {
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.INSTALLATION_DATA_UNLOAD, param1_invc);

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
                    JSONObject object = new JSONObject(result);
                    String obj1 = object.getString("data_return");
                    JSONArray ja = new JSONArray(obj1);
                    Log.e("OUTPUT2", "&&&&" + ja);
                    if(progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");
                        if (invc_done.equalsIgnoreCase("Y")) {

                            databaseHelper.deleteUnloadingImages(billNo);
                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));
                            WebURL.CHECK_DATA_UNOLAD = 0;

                            Random random = new Random();
                            String generatedVerificationCode = String.format("%04d", random.nextInt(10000));

                            runOnUiThread(() -> {
                                if (CustomUtility.isValidMobile(custMobile)) {

                                    sendVerificationCodeAPI(generatedVerificationCode, custMobile, Hp, beneficiary);

                                } else {
                                    Intent intent = new Intent(UnloadInstReportImageActivity.this, PendingInstallationActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        } else if (invc_done.equalsIgnoreCase("N")) {
                            showingMessage(getResources().getString(R.string.dataNotSubmitted));
                            if(progressDialog!=null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                        }
                    }
                } else {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.somethingWentWrong));
                    if(progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

        }
    }

    private void showingMessage(String message) {
        runOnUiThread(new Runnable() {
            public void run() {

                CustomUtility.showToast(UnloadInstReportImageActivity.this, message);

            }
        });
    }

    private void ViewInflate(int new_value) {

        String[] arr = no_of_module_value.split(",");
        moduleOneLL.removeAllViews();

        for (int i = 0; i < new_value; i++) {
            View child_grid = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_for_normal, null);
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
                    edit.setText(arr[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            layout_s.setVisibility(View.GONE);
            moduleOneLL.setVisibility(View.VISIBLE);
            moduleOneLL.addView(child_grid);


        }
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


    void setDataToViewFromScanner(IntentResult scanningResult) {
        try {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            Toast.makeText(getApplicationContext(), scanFormat + scanContent, Toast.LENGTH_SHORT);
            if (!isPumpMotorController) {
                boolean alreadySet = false;
                if (!alreadySet) {
                    if (scannedDeviceNo.size() > 0) {

                        if (!scannedDeviceNo.contains(scanContent)) {
                            EditText edit_O = moduleOneLL.getChildAt(currentScannerFor).findViewById(R.id.view_edit_one);

                            edit_O.setText(scanContent);
                            scannedDeviceNo.add(scanContent);
                        } else {
                            CustomUtility.ShowToast("Already done",getApplicationContext());
                        }
                    } else {
                        EditText edit_O = moduleOneLL.getChildAt(currentScannerFor).findViewById(R.id.view_edit_one);

                        edit_O.setText(scanContent);
                        scannedDeviceNo.add(scanContent);
                    }

                }
            } else {
                switch (currentScannerFor) {
                    case 1000:
                        pumpSerNo.setText(scanContent);
                        break;
                    case 2000:
                        motorSerNo.setText(scanContent);
                        break;
                    case 3000:
                        controllerSerNo.setText(scanContent);


                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public String GetDataOne() {
        String finalValue = "";
        if (!inst_module_ser_no.getText().toString().trim().equals("0")) {

            for (int i = 0; i < Integer.parseInt(inst_module_ser_no.getText().toString()); i++) {
                if (finalValue.isEmpty()) {
                    finalValue = ",";
                } else {
                    finalValue = finalValue + ",";
                }


            }
        } else {
            finalValue = "";
        }
        return finalValue;
    }

    private void sendVerificationCodeAPI(String generatedVerificationCode, String ContactNo, String Hp, String beneficiaryNo) {
        CustomUtility.showProgressDialogue(UnloadInstReportImageActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.SendOTP + "&mobiles=" + ContactNo +
                        "&message=" +beneficiaryNo +" के तहत " +Hp +" HP पंप सेट का मटेरियल प्राप्त हुआ है तो इंस्टॉलेशन टीम को OTP-" +generatedVerificationCode +" शेयर करे। शक्ति पम्पस&sender=SHAKTl&unicode=1&route=2&country=91&DLT_TE_ID=1707169744864682632",

                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                CustomUtility.hideProgressDialog(UnloadInstReportImageActivity.this);


                if (!res.toString().isEmpty()) {
                    VerificationCodeModel verificationCodeModel = new Gson().fromJson(res.toString(), VerificationCodeModel.class);
                    if (verificationCodeModel.getStatus().equals("Success")) {
                        databaseHelper.deleteInstallationImages(billNo);
                        ShowAlertResponse(generatedVerificationCode, ContactNo, Hp, beneficiaryNo, billNo);
                    }

                }

            }
        }, error -> {
            CustomUtility.hideProgressDialog(UnloadInstReportImageActivity.this);
            Log.e("error", String.valueOf(error));
            Toast.makeText(UnloadInstReportImageActivity.this, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void ShowAlertResponse(String generatedVerificationCode, String ContactNo, String Hp, String beneficiaryNo, String billNo) {
        LayoutInflater inflater = (LayoutInflater) UnloadInstReportImageActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UnloadInstReportImageActivity.this, R.style.MyDialogTheme);

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
            Intent intent = new Intent(UnloadInstReportImageActivity.this, PendingInsUnlOTPVerification.class);
            intent.putExtra(Constant.PendingFeedbackContact, ContactNo);
            intent.putExtra(Constant.PendingFeedbackHp, Hp);
            intent.putExtra(Constant.PendingFeedbackBeneficiary, beneficiaryNo);
            intent.putExtra(Constant.VerificationCode, generatedVerificationCode);
            intent.putExtra(Constant.regisno, regisno);
            intent.putExtra(Constant.isUnloading, "true");
            intent.putExtra(Constant.PendingFeedbackVblen, billNo);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        });

    }

}

