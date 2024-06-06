package activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import adapter.BarCodeSelectionAdapter;
import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import bean.InstallationBean;
import bean.InstallationListBean;
import bean.unloadingDataBean;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import debugapp.VerificationCodeModel;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class UnloadInstReportImageActivity extends BaseActivity implements ImageSelectionAdapter.ImageSelectionListener, BarCodeSelectionAdapter.BarCodeSelectionListener {

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
     RecyclerView recyclerview,barcodeListView;
    EditText remarkEdt;
    TextView inst_module_ser_no, pumpSerNo, motorSerNo, controllerSerNo;

    ArrayList<unloadingDataBean> unloadingListdata;
    DatabaseHelper db;
    ImageView pump_scanner, motor_scanner, controllerScanner;
    LinearLayout moduleOneLL;
    Button btnSave;
    AlertDialog alertDialog;
    int selectedIndex,barcodeSelectIndex;
    ImageSelectionAdapter customAdapter;
    BarCodeSelectionAdapter barCodeSelectionAdapter;
    boolean isSubmit = false;
    List<String> itemNameList = new ArrayList<>();
    String customerName, beneficiary, regNO, projectNo, userID, billNo, moduleqty, custMobile, regisno,
            no_of_module_value, noOfModules = "", Hp, unloadingMaterialStatus = "";
    int scannerCode;
    Toolbar mToolbar;
    boolean isUpdate = false, isPumpMotorController = false;
    InstallationListBean installationListBean;
    RadioButton materialStatusOk, materialStatusNotOk, materialStatusDamage;

    List<String> barcodenameList = new ArrayList<>();
    GmsBarcodeScannerOptions options;
    GmsBarcodeScanner scanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unload_instreport_image);

        Init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        CheakPermissions();
    }

    private void CheakPermissions() {
        if (!checkPermission()) {
            requestPermission();
        }

    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,  Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_MEDIA_AUDIO,
                            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        }  else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        }
    }


    private boolean checkPermission() {
        int FineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int CoarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int Camera = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int WriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ReadMediaImages = ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES);
        int ReadMediaAudio = ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_AUDIO);





        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED  && ReadMediaImages == PackageManager.PERMISSION_GRANTED
                    && ReadMediaAudio == PackageManager.PERMISSION_GRANTED ;
        }else {
            return FineLocation == PackageManager.PERMISSION_GRANTED && CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && WriteExternalStorage == PackageManager.PERMISSION_GRANTED;
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

                        boolean CoarseLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean  Camera = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean  ReadMediaImages = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean  ReadMediaAudio = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                        if ( !CoarseLocationAccepted &&  !Camera && !ReadMediaImages && !ReadMediaAudio) {
                            // perform action when allow permission success
                            Toast.makeText(UnloadInstReportImageActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();

                        }
                    } else  {
                        boolean  FineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean CoarseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean  Camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadPhoneStorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                        boolean WritePhoneStorage = grantResults[4] == PackageManager.PERMISSION_GRANTED;


                        if(!FineLocationAccepted && !CoarseLocationAccepted && !Camera && !ReadPhoneStorage && !WritePhoneStorage ){
                            Toast.makeText(UnloadInstReportImageActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();

                        }
                    }
                }
                break;
        }
    }

    private void Init() {
        recyclerview = findViewById(R.id.recyclerview);
        barcodeListView = findViewById(R.id.barcodeListView);
        remarkEdt = findViewById(R.id.edtRemarkVKID);
        inst_module_ser_no = findViewById(R.id.module_serial_no);
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
        db = new DatabaseHelper(getApplicationContext());
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.material_unloading_img));

        retriveValue();

        setUnloadData();
        listner();


    }

    public void InstalledGooglePlayServices(Context context) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                //prompt the dialog to update google play
                googleAPI.getErrorDialog(this, result, 1).show();

            }
        } else {
            //google play up to date
        }
    }
    private void retriveValue() {
        Bundle bundle = getIntent().getExtras();

        installationListBean = (InstallationListBean) bundle.getSerializable(Constant.unloadingData);
        Log.e("installationListBean===>", installationListBean.customer_name);
        customerName = installationListBean.customer_name;
        userID = CustomUtility.getSharedPreferences(UnloadInstReportImageActivity.this, "userid");
        billNo = installationListBean.billno;
        custMobile = installationListBean.getCUS_CONTACT_NO();
        regisno = installationListBean.regisno;
        Hp = installationListBean.getHP();
        beneficiary = WebURL.BenificiaryNo_Con;
        regNO = WebURL.RegNo_Con;
        projectNo = WebURL.ProjectNo_Con;

        inst_module_ser_no.setText(installationListBean.moduleqty);

        SetAdapter();
        isGooglePlayServiceInstalled();
    }

    private void  isGooglePlayServiceInstalled(){
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                //prompt the dialog to update google play
                googleAPI.getErrorDialog(this, result, 1).show();

            }

        }
    }
    private void SetAdapter() {



        barcodenameList = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(installationListBean.moduleqty); i++) {
            barcodenameList.add("");
        }
        Log.e("barcodenameList======>", String.valueOf(barcodenameList.size()));

        barCodeSelectionAdapter = new BarCodeSelectionAdapter(UnloadInstReportImageActivity.this, barcodenameList);
        barcodeListView.setHasFixedSize(true);
        barcodeListView.setAdapter(barCodeSelectionAdapter);
        barCodeSelectionAdapter.BarCodeSelection(this);

        /*----------------------------------Image Adapter----------------------------------*/
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
        customAdapter = new ImageSelectionAdapter(UnloadInstReportImageActivity.this, imageArrayList, false);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);

    }
    private void setUnloadData() {
        unloadingListdata = db.getUnloadingData(billNo);

        if(unloadingListdata.size()>0){
        if (db.isRecordExist(db.TABLE_UNLOADING_FORM_DATA,db.KEY_BILL_NO,billNo)) {
            unloadingListdata = db.getUnloadingData(billNo);
            inst_module_ser_no.setText(unloadingListdata.get(0).getPanel_module_qty());
            pumpSerNo.setText(unloadingListdata.get(0).getPump_serial_no());
            motorSerNo.setText(unloadingListdata.get(0).getMotor_serial_no());
            controllerSerNo.setText(unloadingListdata.get(0).getController_serial_no());
            remarkEdt.setText(unloadingListdata.get(0).getRemark());
            if(unloadingListdata.get(0).getMaterial_status().equals(Constant.partialDamage)) {
                materialStatusDamage.setChecked(true);
            } else if (unloadingListdata.get(0).getMaterial_status().equals(Constant.ok)) {
                materialStatusOk.setChecked(true);
            } else if (unloadingListdata.get(0).getMaterial_status().equals(Constant.not_ok)) {
                materialStatusNotOk.setChecked(true);
            }
            no_of_module_value=unloadingListdata.get(0).getPanel_values();
            String strArray[] = unloadingListdata.get(0).getPanel_values().split(",");
            for (int i = 0; i < strArray.length; i++) {
                barcodenameList.set(i,strArray[i]);
            }

            barCodeSelectionAdapter.notifyDataSetChanged();
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
                } else if (pumpSerNo.getText().toString().trim().isEmpty() || !pumpSerNo.getText().toString().trim().equals(installationListBean.getPump().trim())) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.correctPumpSr) + " " + installationListBean.getPump().trim());
                } else if (motorSerNo.getText().toString().trim().isEmpty() || !motorSerNo.getText().toString().trim().equals(installationListBean.getMotor().trim())) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.correctMotorSr) + " " + installationListBean.getMotor().trim());
                } else if (controllerSerNo.getText().toString().trim().isEmpty() || !controllerSerNo.getText().toString().trim().equals(installationListBean.getController().trim())) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.correctControllerSr) + " " + installationListBean.getController().trim());
                } else if (remarkEdt.getText().toString().trim().isEmpty()) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.writeRemark));
                } else {
                    Set<String> set = new HashSet<>();
                    if (!inst_module_ser_no.getText().toString().trim().equals("0")) {
                        for (int i = 0; i < barcodenameList.size(); i++) {

                            if (barcodenameList.get(i).isEmpty()) {
                                CustomUtility.ShowToast(getResources().getString(R.string.enter_allModuleSrno), getApplicationContext());
                                isSubmit = false;
                            } else {
                                if (set.contains(barcodenameList.get(i).toUpperCase())) {
                                    isSubmit = false;
                                    CustomUtility.ShowToast(barcodenameList.get(i) + getResources().getString(R.string.moduleMultipleTime), this);
                                    break;
                                } else {
                                    set.add(barcodenameList.get(i).toUpperCase());

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
                        if (set.size() == barcodenameList.size()) {
                            isSubmit = true;
                        }
                    }
                        if (isSubmit) {
                            if (materialStatusOk.isChecked()) {
                                unloadingMaterialStatus = materialStatusOk.getText().toString();
                            }
                            if (materialStatusNotOk.isChecked()) {
                                unloadingMaterialStatus = materialStatusNotOk.getText().toString();
                            }
                            if (materialStatusDamage.isChecked()) {
                                unloadingMaterialStatus = materialStatusDamage.getText().toString();
                            }

                            if (CustomUtility.isInternetOn(getApplicationContext())) {
                                InstallationBean param_invc = new InstallationBean();
                                JSONArray ja_invc_data = new JSONArray();
                                JSONObject jsonObj = new JSONObject();
                                try {
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
                                    Log.e("ja_invc_data=====>", ja_invc_data.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                    saveDataLocally();
                                    new SyncInstallationData(ja_invc_data).execute();

                                } else {
                                    saveDataLocally();
                                    CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), getApplicationContext());
                                    CustomUtility.ShowToast(getResources().getString(R.string.savedInLocalDatabase), getApplicationContext());
                                    finish();
                                }


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
                ScanCode(1000);
            }
        });
        motor_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPumpMotorController = true;
                ScanCode(2000);
            }
        });

        controllerScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPumpMotorController = true;
                ScanCode(3000);
            }
        });
    }

    private void saveDataLocally() {
        unloadingDataBean unloadingBean=new unloadingDataBean(
                inst_module_ser_no.getText().toString(),
                noOfModules,
                pumpSerNo.getText().toString(),
                motorSerNo.getText().toString(),
                controllerSerNo.getText().toString(),
                unloadingMaterialStatus,
                remarkEdt.getText().toString(),
                billNo
        );

        if(db.isRecordExist(db.TABLE_UNLOADING_FORM_DATA, db.KEY_BILL_NO, unloadingBean.getBill_no())){
            db.updateUnloadingForm(unloadingBean);
        }else{
            db.insertUnloadingFormData(unloadingBean);
        }




        ArrayList<unloadingDataBean> unload= new ArrayList<>();
        unload=db.getUnloadingData(billNo);
        Log.e("unload==>", String.valueOf(unload.size()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;
        if (!checkPermission()) {
            requestPermission();
        }else {
            if (imageModelList.isImageSelected()) {
                isUpdate = true;
                selectImage("1");
            } else {
                isUpdate = false;
                selectImage("0");
            }
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
                .putExtra("cust_name", customerName)
                .putExtra("pump_sernr", installationListBean.getPump_ser())
                .putExtra("BeneficiaryNo", installationListBean.getBeneficiary())
                .putExtra("PumpLoad", installationListBean.getPump_load()));

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
            setScanValue(scanningResult.getContents(),scannerCode);
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

    @Override
    public void BarCodeSelectionListener(int position) {
        barcodeSelectIndex = position;
        ScanCode(4000);
    }
    private void ScanCode(int scannerCode) {


        this.scannerCode = scannerCode;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            options = new GmsBarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                            Barcode.FORMAT_QR_CODE,
                            Barcode.FORMAT_AZTEC)
                    .build();
            scanner = GmsBarcodeScanning.getClient(this, options);
            scanner.startScan()
                    .addOnSuccessListener(
                            barcode -> {
                                // Task completed successfully
                                String rawValue = barcode.getRawValue();

                                setScanValue(rawValue, scannerCode);


                            })
                    .addOnCanceledListener(
                            () -> {
                                // Task canceled
                                Toast.makeText(getApplicationContext(), "Scanning Cancelled Please try again", Toast.LENGTH_SHORT).show();
                            })
                    .addOnFailureListener(
                            e -> {
                                startScanOldVersion();
                                // Task failed with an exception
                              //  Toast.makeText(getApplicationContext(), "Scanning Failed Please try again", Toast.LENGTH_SHORT).show();
                            });
        }else {
           startScanOldVersion();
        }

    }

    private void startScanOldVersion() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QRCode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);// Use a specific camera of the device
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void setScanValue(String rawValue, int scannerCode) {

        if (scannerCode == 1000) {
            pumpSerNo.setText(rawValue);
        } else if (scannerCode == 2000) {
            motorSerNo.setText(rawValue);
        } else if (scannerCode == 3000) {
            controllerSerNo.setText(rawValue);
        } else {
            if(!barcodenameList.contains(rawValue)){
                barcodenameList.set(barcodeSelectIndex,rawValue);
                barCodeSelectionAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(getApplicationContext(),"Already Scanned",Toast.LENGTH_SHORT).show();
            }
        }
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

                            db.deleteUnloadingImages(billNo);
                            db.deleteUnloadingForm(billNo);

                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));
                            WebURL.CHECK_DATA_UNOLAD = 1;

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
                        db.deleteInstallationImages(billNo);
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

