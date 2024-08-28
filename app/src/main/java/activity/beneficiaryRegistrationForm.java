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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.ImageSelectionAdapter;
import bean.BeneficiaryRegistrationBean;
import bean.ImageModel;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class beneficiaryRegistrationForm extends BaseActivity implements ImageSelectionAdapter.ImageSelectionListener, AdapterView.OnItemSelectedListener {
    private static final int PICK_FROM_FILE = 102;
    private static final int REQUEST_CODE_PERMISSION = 101;

    private Toolbar mToolbar;
    RecyclerView photoListView;
    AlertDialog alertDialog;
    DatabaseHelper db;
    List<String> itemNameList = new ArrayList<>();
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    ImageSelectionAdapter customAdapter;
    int selectedIndex;
    BeneficiaryRegistrationBean beneficiaryBeanList;
    boolean isUpdate = false;
    EditText serialIdExt, familyIdExt, beneficiaryFormApplicantName, applicantFatherNameExt, applicantMobileExt,
            applicantVillageExt, applicantBlockExt, applicantTehsilExt, applicantDistrictExt, pumpCapacityExt,
            applicantAccountNoExt, applicantIFSCExt, applicantAadharExt;
    Spinner controllerTypeSpinner, pumpTypeSpinner, pumpAcDcSpinner;
    TextView save;
    String selectedControllerType = "", selectedPumpType = "", selectedAcDc = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_registration_form);

        Init();
        listner();

    }

    private void setdata() {
        Bundle extras = getIntent().getExtras();
        if (getIntent().getExtras() != null) {
            assert extras != null;
            beneficiaryBeanList = (BeneficiaryRegistrationBean) extras.getSerializable(Constant.beneficiaryData);
            if (beneficiaryBeanList != null) {

                serialIdExt.setText(beneficiaryBeanList.getSerialId());
                familyIdExt.setText(beneficiaryBeanList.getFamilyId());
                applicantAadharExt.setText(beneficiaryBeanList.getAadharNo());
                beneficiaryFormApplicantName.setText(beneficiaryBeanList.getBeneficiaryFormApplicantName());
                applicantFatherNameExt.setText(beneficiaryBeanList.getApplicantFatherName());
                applicantMobileExt.setText(beneficiaryBeanList.getApplicantMobile());
                applicantVillageExt.setText(beneficiaryBeanList.getApplicantVillage());
                applicantBlockExt.setText(beneficiaryBeanList.getApplicantBlock());
                applicantTehsilExt.setText(beneficiaryBeanList.getApplicantTehsil());
                applicantDistrictExt.setText(beneficiaryBeanList.getApplicantDistrict());
                pumpCapacityExt.setText(beneficiaryBeanList.getPumpCapacity());
                applicantAccountNoExt.setText(beneficiaryBeanList.getApplicantAccountNo());
                applicantIFSCExt.setText(beneficiaryBeanList.getApplicantIFSC());
                selectedAcDc = beneficiaryBeanList.getPumpAcDc();
                if (selectedAcDc.equals(getResources().getString(R.string.AC))) {
                    pumpAcDcSpinner.setSelection(1);
                } else {
                    pumpAcDcSpinner.setSelection(2);
                }
                selectedPumpType = beneficiaryBeanList.getPumpType();
                if (selectedPumpType.equals(getResources().getString(R.string.submersible))) {
                    pumpTypeSpinner.setSelection(1);
                } else {
                    pumpTypeSpinner.setSelection(1);
                }
                selectedControllerType = beneficiaryBeanList.getControllerType();
                if (selectedControllerType.equals(getResources().getString(R.string.Normal))) {
                    controllerTypeSpinner.setSelection(1);
                } else {
                    controllerTypeSpinner.setSelection(2);
                }
            }
        }

    }

    private void Init() {
        db = new DatabaseHelper(this);
        serialIdExt = findViewById(R.id.serialIdExt);
        familyIdExt = findViewById(R.id.familyIdExt);
        beneficiaryFormApplicantName = findViewById(R.id.beneficiaryFormApplicantName);
        applicantFatherNameExt = findViewById(R.id.applicantFatherNameExt);
        applicantMobileExt = findViewById(R.id.applicantMobileExt);
        applicantVillageExt = findViewById(R.id.applicantVillageExt);
        applicantBlockExt = findViewById(R.id.applicantBlockExt);
        applicantTehsilExt = findViewById(R.id.applicantTehsilExt);
        applicantDistrictExt = findViewById(R.id.applicantDistrictExt);
        pumpCapacityExt = findViewById(R.id.pumpCapacityExt);
        applicantAccountNoExt = findViewById(R.id.applicantAccountNoExt);
        applicantIFSCExt = findViewById(R.id.applicantIFSCExt);
        applicantAadharExt = findViewById(R.id.applicantAadharExt);

        save = findViewById(R.id.save);

        controllerTypeSpinner = findViewById(R.id.controllerTypeSpinner);
        pumpTypeSpinner = findViewById(R.id.pumpTypeSpinner);
        pumpAcDcSpinner = findViewById(R.id.pumpAcDcSpinner);

        photoListView = findViewById(R.id.photoListView);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.beneficiary_registration_form);
        setdata();
        SetAdapter();
    }


    private void listner() {
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        save.setOnClickListener(v -> ValidationCheck());
        controllerTypeSpinner.setOnItemSelectedListener(this);
        pumpTypeSpinner.setOnItemSelectedListener(this);
        pumpAcDcSpinner.setOnItemSelectedListener(this);
    }

    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.acknowledgement_challan_details));
        itemNameList.add(getResources().getString(R.string.fard_land_proof));
        itemNameList.add(getResources().getString(R.string.beneficiary_id_proof));
        itemNameList.add(getResources().getString(R.string.payment_receipt));
        itemNameList.add(getResources().getString(R.string.attachment5));
        itemNameList.add(getResources().getString(R.string.attachment6));

        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo("");
            imageModel.setPoistion(i + 1);
            imageArrayList.add(imageModel);
        }

        imageList = db.getAllBeneficiaryImages();

         Log.e("imageList=====>", String.valueOf(imageList.toString()));
        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {

                    Log.e("imageList=====>", imageList.get(i).getBillNo() + "======>" + serialIdExt.getText().toString());

                    if (!imageList.get(i).getBillNo().isEmpty() && imageList.get(i).getBillNo().trim().equals(serialIdExt.getText().toString().trim())) {

                        if (imageList.get(i).getName().equals(itemNameList.get(j))) {
                            Log.e("isImageSelected=====>", String.valueOf(imageList.get(i).isImageSelected()));

                            ImageModel imageModel = new ImageModel();
                            imageModel.setName(imageList.get(i).getName());
                            imageModel.setImagePath(imageList.get(i).getImagePath());
                            imageModel.setImageSelected(true);
                            imageModel.setBillNo(imageList.get(i).getBillNo());
                            imageModel.setLatitude(imageList.get(i).getLatitude());
                            imageModel.setLongitude(imageList.get(i).getLongitude());
                            imageModel.setPoistion(imageList.get(i).getPoistion());
                            imageArrayList.set(j, imageModel);
                        }
                    }
                }
            }
        }
     //   Log.e("imageArrayList=====>", String.valueOf(imageArrayList.toString()));
        customAdapter = new ImageSelectionAdapter(beneficiaryRegistrationForm.this, imageArrayList, false);
        photoListView.setHasFixedSize(true);
        photoListView.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);
    }

    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;
        if (serialIdExt.getText().toString().trim().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_serial_id), getApplicationContext());

        }else if (beneficiaryFormApplicantName.getText().toString().trim().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_applicant_name), getApplicationContext());

        }else {
            if (checkPermission()) {
                if (imageModelList.isImageSelected()) {
                    isUpdate = true;
                    selectImage("1");
                } else {
                    isUpdate = false;
                    selectImage("0");
                }
            } else {
                requestPermission();
            }

        }
    }

    private void selectImage(String value) {
        LayoutInflater inflater = (LayoutInflater) beneficiaryRegistrationForm.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(beneficiaryRegistrationForm.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

        TextView title = layout.findViewById(R.id.titleTxt);
        TextView gallery = layout.findViewById(R.id.gallery);
        TextView camera = layout.findViewById(R.id.camera);
        TextView cancel = layout.findViewById(R.id.cancel);

        if (value.equals("0")) {
//            camera.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.select_image));
            gallery.setText(getResources().getString(R.string.gallery));
            camera.setText(getResources().getString(R.string.camera));

        } else {
            title.setText(getResources().getString(R.string.want_to_perform));
            gallery.setText(getResources().getString(R.string.display));
            camera.setText(getResources().getString(R.string.change));
        }

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (value.equals("0")) {
                    galleryIntent();
                } else {
                    Intent i_display_image = new Intent(beneficiaryRegistrationForm.this, PhotoViewerActivity.class);
                    i_display_image.putExtra("image_path", imageArrayList.get(selectedIndex).getImagePath());
                    startActivity(i_display_image);
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
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

        camraLauncher.launch(new Intent(beneficiaryRegistrationForm.this, CameraActivity2.class)
                .putExtra("cust_name", beneficiaryFormApplicantName.getText().toString().trim()));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();
                            Log.e("bundle====>", bundle.get("data").toString());
                            UpdateArrayList(bundle.get("data").toString(), bundle.get("latitude").toString(), bundle.get("longitude").toString());

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
        switch (requestCode) {

            case PICK_FROM_FILE:
                try {
                    Uri mImageCaptureUri = data.getData();
                    String path = getPath(beneficiaryRegistrationForm.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(beneficiaryRegistrationForm.this, getResources().getString(R.string.file_not_valid), Toast.LENGTH_LONG).show();
                    } else {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);
                        File file1 = CustomUtility.saveFile(bitmap, getResources().getString(R.string.BeneficiaryRegistration), "Images");

                        UpdateArrayList(file1.getPath(), "", "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    private void UpdateArrayList(String path, String latitude, String longitude) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageModel.setPoistion(imageArrayList.get(selectedIndex).getPoistion());
        imageModel.setBillNo(serialIdExt.getText().toString());
        imageModel.setLatitude(latitude);
        imageModel.setLongitude(longitude);
        imageArrayList.set(selectedIndex, imageModel);

        addupdateDatabase(imageModel);

        customAdapter.notifyDataSetChanged();

    }

    private void addupdateDatabase(ImageModel imageModel) {
        Log.e("imageModel======>", imageModel.toString());
        if (isUpdate) {
            db.updateRecordBeneficiary(imageModel,true);
        } else {
            db.insertBeneficiaryImage(imageModel,true);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.pumpTypeSpinner) {
            if (!parent.getSelectedItem().toString().equals(getResources().getString(R.string.select_pump_type))) {
                selectedPumpType = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.pumpAcDcSpinner) {
            if (!parent.getSelectedItem().toString().equals(getResources().getString(R.string.selec_pump_ac_dc))) {
                selectedAcDc = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.controllerTypeSpinner) {
            if (!parent.getSelectedItem().toString().equals(getResources().getString(R.string.selec_controller_type))) {
                selectedControllerType = parent.getSelectedItem().toString();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void ValidationCheck() {
        Log.e("length=====>", String.valueOf(applicantMobileExt.getText().toString().length()));
        if (serialIdExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_serial_id), getApplicationContext());
        } else if (familyIdExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_family_id), getApplicationContext());
        } else if (beneficiaryFormApplicantName.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_applicant_name), getApplicationContext());
        } else if (applicantFatherNameExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_father_name), getApplicationContext());
        } else if (applicantMobileExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_mobile_no), getApplicationContext());
        } else if (applicantMobileExt.getText().toString().length() != 10) {
            CustomUtility.ShowToast(getResources().getString(R.string.valid_mobile_no), getApplicationContext());
        } else if (applicantAadharExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_mobile_no), getApplicationContext());
        } else if (applicantAadharExt.getText().toString().length() < 11) {
            CustomUtility.ShowToast(getResources().getString(R.string.valid_aadhar_no), getApplicationContext());
        } else if (applicantVillageExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_village_name), getApplicationContext());
        } else if (applicantBlockExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_block_name), getApplicationContext());
        } else if (applicantTehsilExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_tehsil_name), getApplicationContext());
        } else if (applicantDistrictExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_district_name), getApplicationContext());
        } else if (pumpCapacityExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_pump_capacity), getApplicationContext());
        } else if (selectedAcDc.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_pump_ac_dc), getApplicationContext());
        } else if (selectedPumpType.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_pump_type), getApplicationContext());
        } else if (selectedControllerType.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_conrtoller_type), getApplicationContext());
        } else if (applicantAccountNoExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_account_no), getApplicationContext());
        } else if (applicantIFSCExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_ifsc_code), getApplicationContext());
        } else {

            if (imageArrayList.size() > 0) {
                if (!imageArrayList.get(0).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_acknowledgment_challan_image), Toast.LENGTH_SHORT).show();
                } else if (CustomUtility.getSharedPreferences(getApplicationContext(), "projectid").equals("3008")) {
                    if (!imageArrayList.get(1).isImageSelected()) {
                        Toast.makeText(this, getResources().getString(R.string.fard_land_proof), Toast.LENGTH_SHORT).show();
                    } else if (!imageArrayList.get(2).isImageSelected()) {
                        Toast.makeText(this, getResources().getString(R.string.select_beneficiary_id_proof), Toast.LENGTH_SHORT).show();
                    } else if (!imageArrayList.get(3).isImageSelected()) {
                        Toast.makeText(this, getResources().getString(R.string.select_payment_receipt), Toast.LENGTH_SHORT).show();
                    } else {
                        saveData();
                    }
                } else {
                    if (!imageArrayList.get(2).isImageSelected()) {
                        Toast.makeText(this, getResources().getString(R.string.select_beneficiary_id_proof), Toast.LENGTH_SHORT).show();
                    } else if (!imageArrayList.get(3).isImageSelected()) {
                        Toast.makeText(this, getResources().getString(R.string.select_payment_receipt), Toast.LENGTH_SHORT).show();
                    } else {
                        saveData();
                    }
                }
            }
        }
    }

    private void saveData() {

        if (CustomUtility.isInternetOn(getApplicationContext())) {

            saveInLocalDatabase();

            JSONArray ja_invc_data = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("BENEFICIARY", serialIdExt.getText().toString().trim());
                jsonObj.put("FAMILY_ID", familyIdExt.getText().toString().trim());
                jsonObj.put("APPLICANT_NAME", beneficiaryFormApplicantName.getText().toString().trim());
                jsonObj.put("APPLICANT_FNAME", applicantFatherNameExt.getText().toString().trim());
                jsonObj.put("APPLICANT_MOB", applicantMobileExt.getText().toString().trim());
                jsonObj.put("aadhar_no",applicantAadharExt.getText().toString().trim());
                jsonObj.put("APPLICANT_VILLAGE", applicantVillageExt.getText().toString().trim());
                jsonObj.put("APPLICANT_BLOCK", applicantBlockExt.getText().toString().trim());
                jsonObj.put("APPLICANT_TEHSIL", applicantTehsilExt.getText().toString().trim());
                jsonObj.put("APPLICANT_DISTRICT", applicantDistrictExt.getText().toString().trim());
                jsonObj.put("PUMP_CAP", pumpCapacityExt.getText().toString().trim());
                jsonObj.put("PUMP_ACDC", selectedAcDc);
                jsonObj.put("CONT_TYPE", selectedControllerType);
                jsonObj.put("PUMP_TYPE", selectedPumpType);
                jsonObj.put("APPLICANT_BANKAC", applicantAccountNoExt.getText().toString().trim());
                jsonObj.put("APPLICANT_IFSC", applicantIFSCExt.getText().toString().trim());
                jsonObj.put("PROJECT_NO", CustomUtility.getSharedPreferences(getApplicationContext(), "projectid"));
                jsonObj.put("LIFNR", CustomUtility.getSharedPreferences(getApplicationContext(), "userid"));


                if (imageArrayList.size() > 0) {
                    for (int i = 0; i < imageArrayList.size(); i++) {
                        if (imageArrayList.get(i).isImageSelected()) {
                            jsonObj.put("PHOTO" + imageArrayList.get(i).getPoistion(), CustomUtility.getBase64FromBitmap(beneficiaryRegistrationForm.this, imageArrayList.get(i).getImagePath()));
                        }
                    }
                }
                ja_invc_data.put(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("BeneParam====>", ja_invc_data.toString());
            new submitBeneficiaryForm(ja_invc_data).execute();


        } else {
            saveInLocalDatabase();
            CustomUtility.ShowToast(getResources().getString(R.string.data_save_in_local), getApplicationContext());
            onBackPressed();
        }

    }

    private void saveInLocalDatabase() {
        BeneficiaryRegistrationBean beneficiaryRegistrationBean = new BeneficiaryRegistrationBean(
                serialIdExt.getText().toString(),
                familyIdExt.getText().toString(),
                beneficiaryFormApplicantName.getText().toString(),
                applicantFatherNameExt.getText().toString(),
                applicantMobileExt.getText().toString(),
                applicantVillageExt.getText().toString(),
                applicantBlockExt.getText().toString(),
                applicantTehsilExt.getText().toString(),
                applicantDistrictExt.getText().toString(),
                pumpCapacityExt.getText().toString(),
                applicantAccountNoExt.getText().toString(),
                applicantIFSCExt.getText().toString(),
                selectedControllerType,
                selectedPumpType,
                selectedAcDc,
                applicantAadharExt.getText().toString().trim());

        if (db.isRecordExist(DatabaseHelper.TABLE_BENEFICIARY_REGISTRATION, DatabaseHelper.KEY_SERIAL_ID, serialIdExt.getText().toString())) {
            db.updateBeneficiaryRegistrationData(beneficiaryRegistrationBean);

        } else {
            db.insertBeneficiaryRegistrationData(beneficiaryRegistrationBean);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class submitBeneficiaryForm extends AsyncTask<String, String, String> {

        JSONArray jsonArray;


        public submitBeneficiaryForm(JSONArray jaInvcData) {
            this.jsonArray = jaInvcData;
        }

        @Override
        protected void onPreExecute() {
            showprogressDialogue();
        }

        @Override
        protected String doInBackground(String... params) {
            String obj2 = null;


            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("post", String.valueOf(jsonArray)));
            Log.e("DATA", "$$$$" + param1_invc);
            try {
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.BeneficiaryRegistrationURL, param1_invc);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {
            stopProgressDialogue();
            try {
                if (!result.isEmpty()) {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("True")) {
                        showingMessage(object.getString("message"));
                        db.deleteBeneficiaryRegistration(serialIdExt.getText().toString().trim());
                        db.deleteBeneficiaryImages(serialIdExt.getText().toString().trim());
                        finish();
                    } else {
                        showingMessage(getResources().getString(R.string.dataNotSubmitted));
                    }

                } else {
                    showingMessage(getResources().getString(R.string.somethingWentWrong));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showingMessage(String message) {
        runOnUiThread(new Runnable() {
            public void run() {

                CustomUtility.showToast(beneficiaryRegistrationForm.this, message);

            }
        });
    }

    private void showprogressDialogue() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(beneficiaryRegistrationForm.this);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(getResources().getString(R.string.sending_data_to_server));
                progressDialog.show();
            }
        });
    }

    public void stopProgressDialogue(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
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
                            Toast.makeText(beneficiaryRegistrationForm.this, "Please allow all the permission", Toast.LENGTH_LONG).show();

                        }
                    } else  {
                        boolean  FineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean CoarseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean  Camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadPhoneStorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                        boolean WritePhoneStorage = grantResults[4] == PackageManager.PERMISSION_GRANTED;


                        if(!FineLocationAccepted && !CoarseLocationAccepted && !Camera && !ReadPhoneStorage && !WritePhoneStorage ){
                            Toast.makeText(beneficiaryRegistrationForm.this, "Please allow all the permission", Toast.LENGTH_LONG).show();

                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}