package activity;

import static utility.FileUtils.getPath;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
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

public class beneficiaryRegistrationForm extends BaseActivity implements ImageSelectionAdapter.ImageSelectionListener, AdapterView.OnItemSelectedListener{
    private static final int PICK_FROM_FILE = 102;

    private Toolbar mToolbar;
    RecyclerView photoListView;
    AlertDialog alertDialog;
    DatabaseHelper db;
    List<String> itemNameList = new ArrayList<>();
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    ImageSelectionAdapter customAdapter;
    int selectedIndex;
    boolean isUpdate = false;
    EditText serialIdExt,familyIdExt,beneficiaryFormApplicantName,applicantFatherNameExt,applicantMobileExt,applicantVillageExt,applicantBlockExt,applicantTehsilExt,applicantDistrictExt,pumpCapacityExt,applicantAccountNoExt,applicantIFSCExt;
    Spinner controllerTypeSpinner,pumpTypeSpinner,pumpAcDcSpinner;
    TextView save;
    String selectedControllerType="",selectedPumpType="",selectedAcDc="",serialId="",familyId="",beneficiaryApplicantName="",
            applicantFatherName="",applicantMobile="",applicantVillage="",applicantBlock="",applicantTehsil="",
            applicantDistrict="",pumpCapacity="",applicantAccountNo="",applicantIFSC="";
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_registration_form);

        Init();
        listner();
        setdata();
    }

    private void setdata() {
        Bundle extras = getIntent().getExtras();

            if( getIntent().getExtras() != null){
                serialId=extras.getString("serial_id");
                serialIdExt.setText(extras.getString("serial_id"));
                familyIdExt.setText(extras.getString("family_id"));
                beneficiaryFormApplicantName.setText(extras.getString("applicant_name"));
                applicantFatherNameExt.setText(extras.getString("applicant_father_name"));
                applicantMobileExt.setText(extras.getString("applicant_mobile_no"));
                applicantVillageExt.setText(extras.getString("applicant_village"));
                applicantBlockExt.setText(extras.getString("applicant_block"));
                applicantTehsilExt.setText(extras.getString("applicant_tehsil"));
                applicantDistrictExt.setText(extras.getString("applicant_district"));
                pumpCapacityExt.setText(extras.getString("pump_capacity"));
                applicantAccountNoExt.setText(extras.getString("applicant_account_no"));
                applicantIFSCExt.setText(extras.getString("applicant_ifsc_code"));
                selectedAcDc=extras.getString("pump_ac_dc");
                if(selectedAcDc=="AC"){
                    pumpAcDcSpinner.setSelection(1);
                }
                else{
                    pumpAcDcSpinner.setSelection(2);
                }
                selectedPumpType=extras.getString("pump_type");
                if(selectedPumpType=="Submersible")
                {
                    pumpTypeSpinner.setSelection(1);
                }
                else{
                    pumpTypeSpinner.setSelection(1);
                }
                selectedControllerType=extras.getString("controller_type");
                if(selectedControllerType=="Normal")
                {
                    controllerTypeSpinner.setSelection(1);
                }
                else{
                    controllerTypeSpinner.setSelection(2);
                }
            }

//        extras.putString("pump_ac_dc", responseList.get(position).getPumpAcDc());
//        extras.putString("pump_type", responseList.get(position).getPumpType());
//        extras.putString("controller_type", responseList.get(position).getControllerType());


    }

    private void Init() {
        db = new DatabaseHelper(this);
        serialIdExt=findViewById(R.id.serialIdExt);
        familyIdExt=findViewById(R.id.familyIdExt);
        beneficiaryFormApplicantName=findViewById(R.id.beneficiaryFormApplicantName);
        applicantFatherNameExt=findViewById(R.id.applicantFatherNameExt);
        applicantMobileExt=findViewById(R.id.applicantMobileExt);
        applicantVillageExt=findViewById(R.id.applicantVillageExt);
        applicantBlockExt=findViewById(R.id.applicantBlockExt);
        applicantTehsilExt=findViewById(R.id.applicantTehsilExt);
        applicantDistrictExt=findViewById(R.id.applicantDistrictExt);
        pumpCapacityExt=findViewById(R.id.pumpCapacityExt);
        applicantAccountNoExt=findViewById(R.id.applicantAccountNoExt);
        applicantIFSCExt=findViewById(R.id.applicantIFSCExt);

        save=findViewById(R.id.save);

        controllerTypeSpinner=findViewById(R.id.controllerTypeSpinner);
        pumpTypeSpinner=findViewById(R.id.pumpTypeSpinner);
        pumpAcDcSpinner=findViewById(R.id.pumpAcDcSpinner);

        photoListView = findViewById(R.id.photoListView);
        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.beneficiary_registration_list);
        SetAdapter();
    }



    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationCheck();
            }
        });
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
            imageModel.setPoistion(i+1);
            imageArrayList.add(imageModel);
        }
serialId=serialIdExt.getText().toString();
        imageList = db.getAllBeneficiaryImages();
        Log.e("imageList==>", String.valueOf(imageList.size()));
        Log.e("imagearrayList==>", String.valueOf(imageArrayList.size()));

        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {
                    Log.e("1imageList.get(i)==>",imageList.get(i).getBillNo());
                    Log.e("1serial_id==>",serialId);
                    if (imageList.get(i).getBillNo().trim().equals(serialId)) {
                        Log.e("2imageList.get(i)==>",imageList.get(i).getBillNo());//intent add
                        if (imageList.get(i).getName().equals(itemNameList.get(j))) {
                            Log.e("3imageList.get(i)==>",imageList.get(i).getBillNo());
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
        customAdapter = new ImageSelectionAdapter(beneficiaryRegistrationForm.this, imageArrayList);
        photoListView.setHasFixedSize(true);
        photoListView.setAdapter(customAdapter);
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
            camera.setVisibility(View.GONE);
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
                .putExtra("cust_name", ""));

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
                        Toast.makeText(beneficiaryRegistrationForm.this, "File not valid!", Toast.LENGTH_LONG).show();
                    } else {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , mImageCaptureUri);
                        File file1 = CustomUtility.saveFile(bitmap,"BeneficiaryRegistration","Images");

                        UpdateArrayList(file1.getPath());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    private void UpdateArrayList(String path) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageModel.setPoistion(imageArrayList.get(selectedIndex).getPoistion());
        imageModel.setBillNo(serialIdExt.getText().toString());
        imageArrayList.set(selectedIndex, imageModel);

        imageArrayList.set(selectedIndex, imageModel);
        addupdateDatabase(path,"","",imageArrayList.get(selectedIndex).getPoistion());

        customAdapter.notifyDataSetChanged();

    }
    private void addupdateDatabase(String path, String latitude, String longitude,int position) {

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        if (isUpdate) {
            db.updateRecordBeneficiary(imageArrayList.get(selectedIndex).getName(), path,
                    true, serialIdExt.getText().toString(),position);
        } else {
            db.insertBeneficiaryImage(imageArrayList.get(selectedIndex).getName(), path,
                    true, serialIdExt.getText().toString(),position);
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.pumpTypeSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select Pump Type")) {
                selectedPumpType = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.pumpAcDcSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select AC/DC Pump")) {
                selectedAcDc = parent.getSelectedItem().toString();
            }
        }  else if (parent.getId() == R.id.controllerTypeSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select Controller Type")) {
                selectedControllerType = parent.getSelectedItem().toString();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void ValidationCheck() {
        if (serialIdExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_serial_id), getApplicationContext());
        } else if(familyIdExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_family_id), getApplicationContext());
        }else if(beneficiaryFormApplicantName.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_applicant_name), getApplicationContext());
        }else if(applicantFatherNameExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_father_name), getApplicationContext());
        }else if(applicantMobileExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_mobile_no), getApplicationContext());
        }else if(applicantVillageExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_village_name), getApplicationContext());
        }else if(applicantBlockExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_block_name), getApplicationContext());
        }else if(applicantTehsilExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_tehsil_name), getApplicationContext());
        }else if(applicantDistrictExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_district_name), getApplicationContext());
        }else if(pumpCapacityExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_pump_capacity), getApplicationContext());
        }else if(selectedAcDc.isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_pump_ac_dc), getApplicationContext());
        }else if(selectedPumpType.isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_pump_type), getApplicationContext());
        }else if(selectedControllerType.isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_conrtoller_type), getApplicationContext());
        }else if(applicantAccountNoExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_account_no), getApplicationContext());
        }else if(applicantIFSCExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enter_ifsc_code), getApplicationContext());
        } else {


                if (imageArrayList.size() > 0) {
                    if (!imageArrayList.get(0).isImageSelected()) {
                        Toast.makeText(this, getResources().getString(R.string.select_acknowledgment_challan_image), Toast.LENGTH_SHORT).show();
                    } else if (!imageArrayList.get(1).isImageSelected()) {
                        Toast.makeText(this, getResources().getString(R.string.select_land_proof), Toast.LENGTH_SHORT).show();
                    }
//                    else if (!imageArrayList.get(2).isImageSelected()) {
//                        Toast.makeText(this, getResources().getString(R.string.select_beneficiary_id_proof), Toast.LENGTH_SHORT).show();
//                    } else if (!imageArrayList.get(3).isImageSelected()) {
//                        Toast.makeText(this, getResources().getString(R.string.select_payment_receipt), Toast.LENGTH_SHORT).show();
//                    } else if (!imageArrayList.get(4).isImageSelected()) {
//                        Toast.makeText(this, getResources().getString(R.string.select_attachment_5), Toast.LENGTH_SHORT).show();
//                    } else if (!imageArrayList.get(5).isImageSelected()) {
//                        Toast.makeText(this, getResources().getString(R.string.select_attachment_6), Toast.LENGTH_SHORT).show();
//                    }
                    else{
                   saveDataLocally();
                    }

            }
        }
    }

    private void saveDataLocally() {
        BeneficiaryRegistrationBean beneficiaryRegistrationBean=new BeneficiaryRegistrationBean(
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
                selectedAcDc);

        db.insertBeneficiaryRegistrationData(beneficiaryRegistrationBean);

        if(CustomUtility.isInternetOn(getApplicationContext())){
            new submitDemoRoadForm().execute();
        }else {
            CustomUtility.ShowToast("Data saved In local database", getApplicationContext());
            onBackPressed();
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class submitDemoRoadForm extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(beneficiaryRegistrationForm.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Sending Data to server..please wait !");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String docno_sap = null;
            String invc_done;
            String obj2 = null;

            JSONArray ja_invc_data = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            try {
                SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");

                jsonObj.put("BENEFICIARY",  serialIdExt.getText().toString().trim());
                jsonObj.put("FAMILY_ID",  familyIdExt.getText().toString().trim());
                jsonObj.put("APPLICANT_NAME", beneficiaryFormApplicantName.getText().toString().trim());
                jsonObj.put("APPLICANT_FNAME", applicantFatherNameExt.getText().toString().trim());
                jsonObj.put("APPLICANT_MOB", applicantMobileExt.getText().toString().trim());
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
                jsonObj.put("PROJECT_NO", "01");



                if(imageArrayList.size()>0){
                    for (int i=0; i<imageArrayList.size(); i++){
                        if(imageArrayList.get(i).isImageSelected()) {
                            jsonObj.put("PHOTO" + imageArrayList.get(i).getPoistion(), CustomUtility.getBase64FromBitmap(beneficiaryRegistrationForm.this,imageArrayList.get(i).getImagePath()));
                        }
                    }
                }
                ja_invc_data.put(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("BeneParam====>", ja_invc_data.toString());
            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("post", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc);
            System.out.println("param1_invc_vihu==>>" + param1_invc);
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.BeneficiaryRegistrationURL, param1_invc);
                Log.e("OUTPUT1", "&&&&" + obj2);
                System.out.println("OUTPUT1==>>" + obj2);
                progressDialog.dismiss();
                if (!obj2.equalsIgnoreCase("")) {
                    JSONObject object = new JSONObject(obj2);

                    docno_sap = object.getString("status");
                    invc_done = object.getString("message");
                    if (docno_sap.equalsIgnoreCase("True")) {
                        showingMessage( invc_done);
                        finish();
                    }
                    else {
                        showingMessage(getResources().getString(R.string.dataNotSubmitted));
                        progressDialog.dismiss();
                    }

                } else {
                    showingMessage(getResources().getString(R.string.somethingWentWrong));
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

                CustomUtility.showToast(beneficiaryRegistrationForm.this, message);

            }
        });
    }
}