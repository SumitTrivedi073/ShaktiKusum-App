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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import bean.InstallationBean;
import database.DatabaseHelper;
import debugapp.GlobalValue.NewSolarVFD;
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
    TextView inst_module_ser_no;

    LinearLayout moduleOneLL;

    Button btnSave;
    AlertDialog alertDialog;
    int selectedIndex;
    ImageSelectionAdapter customAdapter;
    boolean isSubmit = false;

    List<String> itemNameList = new ArrayList<>();

    String customerName, beneficiary, regNO, projectNo, userID, billNo, moduleqty, no_of_module_value;
    int value, currentScannerFor = -1;

    Toolbar mToolbar;

    boolean isUpdate = false;

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

        btnSave = findViewById(R.id.btnSave);

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
        customerName = bundle.getString("cust_name");
        userID = bundle.getString("userid");
        billNo = bundle.getString("vbeln");
        moduleqty = bundle.getString("moduleqty");

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
              String noOfModules ="";

            if (imageArrayList != null && imageArrayList.size() > 0) {
                if (!imageArrayList.get(0).isImageSelected()) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.selectLR_photo));
                } else if (!imageArrayList.get(1).isImageSelected()) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.selectInvoicePhoto));
                } else if (!imageArrayList.get(2).isImageSelected()) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.selectCustomerPhoto));
                } else if (remarkEdt.getText().toString().isEmpty()) {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.writeRemark));
                } else {


                    if (!inst_module_ser_no.getText().toString().trim().equals("0")) {

                        for (int i = 0; i < moduleOneLL.getChildCount(); i++) {
                            EditText edit_O = moduleOneLL.getChildAt(i).findViewById(R.id.view_edit_one);

                            if (edit_O.getText().toString().isEmpty()) {
                                CustomUtility.ShowToast(getResources().getString(R.string.enter_allModuleSrno), getApplicationContext());
                             isSubmit = false;
                            } else {
                                isSubmit = true;
                                if(noOfModules.isEmpty()) {
                                    noOfModules = edit_O.getText().toString();
                                }else {
                                    noOfModules= noOfModules+","+edit_O.getText().toString();
                                }

                            }

                        }
                    }
                    if (CustomUtility.isInternetOn(getApplicationContext())) {
                        if (isSubmit) {
                            Log.e("noOfModules====>",noOfModules);
                           new SyncInstallationData().execute();
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
                    if (imageList.get(i).getBillNo().trim().equals(getIntent().getExtras().getString("vbeln").trim())) {
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
                    UpdateArrayList(path);

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

            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;
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
                System.out.println("only_text_jsonObj==>>" + jsonObj);
                jsonObj.put("unld_photo1", CustomUtility.getBase64FromBitmap(UnloadInstReportImageActivity.this, imageArrayList.get(0).getImagePath()));
                jsonObj.put("unld_photo2", CustomUtility.getBase64FromBitmap(UnloadInstReportImageActivity.this, imageArrayList.get(1).getImagePath()));
                jsonObj.put("unld_photo3", CustomUtility.getBase64FromBitmap(UnloadInstReportImageActivity.this, imageArrayList.get(2).getImagePath()));
                ja_invc_data.put(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("Param====>", ja_invc_data.toString());
            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("unloading", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc);
            System.out.println("param1_invc_vihu==>>" + param1_invc);
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.INSTALLATION_DATA_UNLOAD, param1_invc);
                Log.e("OUTPUT1", "&&&&" + obj2);
                System.out.println("OUTPUT1==>>" + obj2);
                progressDialog.dismiss();
                if (!obj2.equalsIgnoreCase("")) {
                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_return");
                    JSONArray ja = new JSONArray(obj1);
                    Log.e("OUTPUT2", "&&&&" + ja);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");
                        if (invc_done.equalsIgnoreCase("Y")) {

                            databaseHelper.deleteUnloadingImages(billNo);
                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));
                            NewSolarVFD.CHECK_DATA_UNOLAD = 0;
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {
                            showingMessage(getResources().getString(R.string.dataNotSubmitted));
                            progressDialog.dismiss();


                        }
                    }
                } else {
                    CustomUtility.showToast(UnloadInstReportImageActivity.this, getResources().getString(R.string.somethingWentWrong));
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
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
            boolean alreadySet = false;
            if (!alreadySet) {
                EditText edit_O = moduleOneLL.getChildAt(currentScannerFor).findViewById(R.id.view_edit_one);

                if (scannedDeviceNo.size() > 0) {
                    if (!scannedDeviceNo.contains(scanContent)) {
                        edit_O.setText(scanContent);
                        scannedDeviceNo.add(scanContent);
                    } else {
                        CustomUtility.ShowToast("Already Done", getApplicationContext());

                    }
                } else {
                    edit_O.setText(scanContent);
                    scannedDeviceNo.add(scanContent);
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
}

