package activity;

import static utility.FileUtils.getPath;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import bean.InstallationBean;
import bean.SurveyListModel;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class KusumCSurveyFormActivity extends AppCompatActivity implements ImageSelectionAdapter.ImageSelectionListener, AdapterView.OnItemSelectedListener {


    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();

    AlertDialog alertDialog;
    int selectedIndex;
    boolean isUpdate = false;
    ImageSelectionAdapter customAdapter;

    List<String> itemNameList = new ArrayList<>();
    Toolbar mToolbar;
    RecyclerView photoListView;

    EditText farmerNameExt, contactNumberExt, applicationNumberExt, addressExt, currentLatLngExt, cropPatternAreaExt, electricConnectionIdentificationNoEXT,
            pumpMakeEXT, voltageV1Ext, voltageV2Ext, voltageV3Ext, lineVoltageV1VoltExt, lineVoltageV2VoltExt, lineVoltageV3VoltExt,
            current1AmpExt, current2AmpExt, current3AmpExt, frequencyHzExt, powerFactor1Ext, powerFactor2Ext, powerFactor3Ext, BorwellDiameterExt, BorwellDepthExt, pumpSetDepthExt, pumpSetDischargeExt,
            pumpSetDeliveryExt, distanceFromProposedSolarPlantExt;
    Spinner categorySpinner, sourceofWaterSpinner, internetConnectivitySpinner, typesOfIrrigationSpinner, southfacingShadowSpinner,
            electicConnectionTypeSpinner, typeOfPumpSpinner,pumpSetRatingSpinner;

    LinearLayout sourceOfWaterLinear;
    String selectedCategory = "", selectedSourceofWater = "", selectedInternetConnectivity = "", selectedTypesOfIrrigation = "", selectedSouthfacingShadow = "",
            selectedElectricConnectionType = "", selectedTypeOfPump = "",selectedPumpSetRating = "", latitude = "", longitude = "", Photo1 = "", Photo2 = "", Photo3 = "", Photo4 = "";

    TextView submitBtn;
    SurveyListModel.Response surveyListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kusum_csurveyform);


        surveyListModel = (SurveyListModel.Response) getIntent().getSerializableExtra(Constant.SurveyData);


        Init();
        listner();

    }


    private void Init() {


        mToolbar = findViewById(R.id.toolbar);

        photoListView = findViewById(R.id.photoListView);

        farmerNameExt = findViewById(R.id.farmerNameExt);
        contactNumberExt = findViewById(R.id.contactNumberExt);
        applicationNumberExt = findViewById(R.id.applicationNumberExt);
        addressExt = findViewById(R.id.addressExt);
        currentLatLngExt = findViewById(R.id.currentLatLngExt);

        cropPatternAreaExt = findViewById(R.id.cropPatternAreaExt);
        electricConnectionIdentificationNoEXT = findViewById(R.id.electricConnectionIdentificationNoEXT);

        pumpMakeEXT = findViewById(R.id.pumpMakeEXT);
        voltageV1Ext = findViewById(R.id.voltageV1Ext);
        voltageV2Ext = findViewById(R.id.voltageV2Ext);
        voltageV3Ext = findViewById(R.id.voltageV3Ext);
        lineVoltageV1VoltExt = findViewById(R.id.lineVoltageV1VoltExt);
        lineVoltageV2VoltExt = findViewById(R.id.lineVoltageV2VoltExt);
        lineVoltageV3VoltExt = findViewById(R.id.lineVoltageV3VoltExt);
        current1AmpExt = findViewById(R.id.current1AmpExt);
        current2AmpExt = findViewById(R.id.current2AmpExt);
        current3AmpExt = findViewById(R.id.current3AmpExt);
        frequencyHzExt = findViewById(R.id.frequencyHzExt);
        powerFactor1Ext = findViewById(R.id.powerFactor1Ext);
        powerFactor2Ext = findViewById(R.id.powerFactor2Ext);
        powerFactor3Ext = findViewById(R.id.powerFactor3Ext);
        BorwellDiameterExt = findViewById(R.id.BorwellDiameterExt);
        BorwellDepthExt = findViewById(R.id.BorwellDepthExt);
        pumpSetDepthExt = findViewById(R.id.pumpSetDepthExt);
        pumpSetDischargeExt = findViewById(R.id.pumpSetDischargeExt);
        pumpSetDeliveryExt = findViewById(R.id.pumpSetDeliveryExt);
        distanceFromProposedSolarPlantExt = findViewById(R.id.distanceFromProposedSolarPlantExt);
        categorySpinner = findViewById(R.id.categorySpinner);
        sourceofWaterSpinner = findViewById(R.id.sourceofWaterSpinner);
        internetConnectivitySpinner = findViewById(R.id.internetConnectivitySpinner);
        typesOfIrrigationSpinner = findViewById(R.id.typesOfIrrigationSpinner);
        southfacingShadowSpinner = findViewById(R.id.southfacingShadowSpinner);
        electicConnectionTypeSpinner = findViewById(R.id.electicConnectionTypeSpinner);
        typeOfPumpSpinner = findViewById(R.id.typeOfPumpSpinner);
        pumpSetRatingSpinner = findViewById(R.id.pumpSetRatingSpinner);
        submitBtn = findViewById(R.id.submitBtn);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.kusumSurveyform));

        setData();
        SetAdapter();
        getGpsLocation();


    }

    private void setData() {
        farmerNameExt.setText(surveyListModel.getCustomerName());
        contactNumberExt.setText(surveyListModel.getMobile());
        applicationNumberExt.setText(surveyListModel.getBeneficiary());
        addressExt.setText(surveyListModel.getAddress());
    }


    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CustomUtility.isInternetOn(getApplicationContext())) {
                    ValidationCheck();
                } else {
                    CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), getApplicationContext());
                }
            }
        });

        categorySpinner.setOnItemSelectedListener(this);
        sourceofWaterSpinner.setOnItemSelectedListener(this);
        internetConnectivitySpinner.setOnItemSelectedListener(this);
        typesOfIrrigationSpinner.setOnItemSelectedListener(this);
        southfacingShadowSpinner.setOnItemSelectedListener(this);
        electicConnectionTypeSpinner.setOnItemSelectedListener(this);
        typeOfPumpSpinner.setOnItemSelectedListener(this);
        pumpSetRatingSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.categorySpinner) {
            if(!parent.getSelectedItem().toString().equals("Select Category")) {
                selectedCategory = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.sourceofWaterSpinner) {
            if(!parent.getSelectedItem().toString().equals("Select source of water")) {
                selectedSourceofWater = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.internetConnectivitySpinner) {
            if(!parent.getSelectedItem().toString().equals("Select internet connectivity type")) {
                selectedInternetConnectivity = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.typesOfIrrigationSpinner) {
            if(!parent.getSelectedItem().toString().equals("Select type of irrigation installed")) {
                selectedTypesOfIrrigation = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.southfacingShadowSpinner) {
            if(!parent.getSelectedItem().toString().equals("Select option for south facing shadow")) {
                selectedSouthfacingShadow = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.electicConnectionTypeSpinner) {
            if(!parent.getSelectedItem().toString().equals("Select electric Connection Type")) {
                selectedElectricConnectionType = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.typeOfPumpSpinner) {
            if(!parent.getSelectedItem().toString().equals("Select type of pump")) {
                selectedTypeOfPump = parent.getSelectedItem().toString();
            }
        }
        else if (parent.getId() == R.id.pumpSetRatingSpinner) {
            if(!parent.getSelectedItem().toString().equals("Select Pump Rating")) {
                selectedPumpSetRating = parent.getSelectedItem().toString();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            if (!String.valueOf(gps.getLatitude()).isEmpty() && !String.valueOf(gps.getLongitude()).isEmpty()) {

                DecimalFormat decimalFormat = new DecimalFormat("##.#####");
                latitude = decimalFormat.format(gps.getLatitude());
                longitude = decimalFormat.format(gps.getLatitude());
                currentLatLngExt.setText(latitude + "," + longitude);
            }
        } else {
            gps.showSettingsAlert();
        }
    }


    /*------------------------------------------------------------------------ImageList---------------------------------------------------------------------*/


    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.watersourcephotograpth));
        itemNameList.add(getResources().getString(R.string.transformer));
        itemNameList.add(getResources().getString(R.string.attechformphoto1));
        itemNameList.add(getResources().getString(R.string.attechformphoto2));


        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo("");
            imageArrayList.add(imageModel);
        }

        customAdapter = new ImageSelectionAdapter(KusumCSurveyFormActivity.this, imageArrayList);
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
        LayoutInflater inflater = (LayoutInflater) KusumCSurveyFormActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(KusumCSurveyFormActivity.this, R.style.MyDialogTheme);

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
            gallery.setVisibility(View.GONE);
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
                    Intent i_display_image = new Intent(KusumCSurveyFormActivity.this, PhotoViewerActivity.class);
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

        camraLauncher.launch(new Intent(KusumCSurveyFormActivity.this, CameraActivity2.class)
                .putExtra("cust_name", surveyListModel.getCustomerName()));

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
                    String path = getPath(KusumCSurveyFormActivity.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(KusumCSurveyFormActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                    } else {
                        UpdateArrayList(path);

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
        imageModel.setBillNo("");
        imageArrayList.set(selectedIndex, imageModel);

        customAdapter.notifyDataSetChanged();


    }


    /*------------------------------------------------------------------------API Methods---------------------------------------------------------------------*/

    private void ValidationCheck() {
        if (farmerNameExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_farmar_name), getApplicationContext());
        } else if (contactNumberExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_contact_number), getApplicationContext());
        } else if (!CustomUtility.isValidMobile(contactNumberExt.getText().toString().trim())) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_valid_contact_number), getApplicationContext());
        } else if (applicationNumberExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_application_number), getApplicationContext());
        } else if (addressExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_name_of_village_block_and_district), getApplicationContext());
        } else if (currentLatLngExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_LatLng), getApplicationContext());
        } else if (selectedCategory.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectCategoty), getApplicationContext());
        } else if (selectedSourceofWater.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectSourceOfWater), getApplicationContext());
        } else if (selectedInternetConnectivity.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectInternetConnectivity), getApplicationContext());
        } else if (cropPatternAreaExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.entercropPatternArea), getApplicationContext());
        } else if (selectedTypesOfIrrigation.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectTypesOfIrrigation), getApplicationContext());
        } else if (selectedSouthfacingShadow.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectSouthfacingShadow), getApplicationContext());
        } else if (selectedElectricConnectionType.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectElectricConnectionType), getApplicationContext());
        } else if (electricConnectionIdentificationNoEXT.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_electricConnectionIdentificationNo), getApplicationContext());
        } else if (selectedTypeOfPump.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectTypeOfPump), getApplicationContext());
        } else if (selectedPumpSetRating.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.select_pumpsetrating), getApplicationContext());
        } else if (pumpMakeEXT.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_pumpsetmake), getApplicationContext());
        } else if (voltageV1Ext.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.voltageV1Volt), getApplicationContext());
        } else if (voltageV2Ext.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.voltageV2Volt), getApplicationContext());
        } else if (voltageV3Ext.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.voltageV3Volt), getApplicationContext());
        } else if (lineVoltageV1VoltExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.linevoltageV1Volt), getApplicationContext());
        } else if (lineVoltageV2VoltExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.linevoltageV2Volt), getApplicationContext());
        } else if (lineVoltageV3VoltExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.linevoltageV3Volt), getApplicationContext());
        } else if (current1AmpExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.current1Amp), getApplicationContext());
        } else if (current2AmpExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.current2Amp), getApplicationContext());
        } else if (current3AmpExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.current3Amp), getApplicationContext());
        } else if (frequencyHzExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.frequencyHz), getApplicationContext());
        } else if (powerFactor1Ext.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.powerFactor1), getApplicationContext());
        } else if (powerFactor2Ext.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.powerFactor2), getApplicationContext());
        } else if (powerFactor3Ext.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.powerFactor3), getApplicationContext());
        } else if (BorwellDiameterExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_diameterOfBorwell), getApplicationContext());
        } else if (BorwellDepthExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_depthOfBorewell), getApplicationContext());
        } else if (pumpSetDepthExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_depthOfPumpSet), getApplicationContext());
        } else if (pumpSetDischargeExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_dischargeOfPumpSet), getApplicationContext());
        } else if (pumpSetDeliveryExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_deliveryOfPumpSet), getApplicationContext());
        } else if (distanceFromProposedSolarPlantExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_distanceFromProposedSolarPlantLocation), getApplicationContext());
        } else if (!imageArrayList.get(0).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.attechWaterSourcePhoto), getApplicationContext());

        } else if (!imageArrayList.get(1).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.attechTransformerPhoto), getApplicationContext());

        } else if (!imageArrayList.get(2).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.attechFormPhoto), getApplicationContext());

        } else if (!imageArrayList.get(3).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.attechFormPhoto), getApplicationContext());

        } else {

            Photo1 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(0).getImagePath());
            Photo2 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(1).getImagePath());
            Photo3 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(2).getImagePath());
            Photo4 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(3).getImagePath());

            new  submitSurveyForm().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class submitSurveyForm extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(KusumCSurveyFormActivity.this);
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
                jsonObj.put("project_no", CustomUtility.getSharedPreferences(getApplicationContext(), "projectid"));
                jsonObj.put("userid", CustomUtility.getSharedPreferences(getApplicationContext(), "userid"));
                jsonObj.put("project_login_no", "01");
                jsonObj.put("FARMER_CONTACT_NO", contactNumberExt.getText().toString().trim());
                jsonObj.put("APPLICANT_NO", applicationNumberExt.getText().toString().trim());
                jsonObj.put("REGISNO", surveyListModel.getRegisno());
                jsonObj.put("BENEFICIARY", surveyListModel.getBeneficiary());
                jsonObj.put("SITE_ADRC", addressExt.getText().toString().trim());
                jsonObj.put("LAT", latitude);
                jsonObj.put("LNG", longitude);
                jsonObj.put("CATEGORY", selectedCategory);
                jsonObj.put("WATER_SOURCE", selectedSourceofWater);
                jsonObj.put("INTERNET_TYPE", selectedInternetConnectivity);
                jsonObj.put("CROP_PATTERN", cropPatternAreaExt.getText().toString().trim());
                jsonObj.put("TYPE_OF_IRIGATN", selectedTypesOfIrrigation);
                jsonObj.put("SHADOW_FREE_LAND", selectedSouthfacingShadow);
                jsonObj.put("ELEC_CON", selectedElectricConnectionType);
                jsonObj.put("ELEC_IDEN_NO", electricConnectionIdentificationNoEXT.getText().toString().trim());
                jsonObj.put("PUMP_TYPE", selectedTypeOfPump);
                jsonObj.put("PUMP_SET_RATING", selectedPumpSetRating);
                jsonObj.put("PUMP_MAKE", pumpMakeEXT.getText().toString().trim());
                jsonObj.put("PHASE_VOL_V1", voltageV1Ext.getText().toString().trim());
                jsonObj.put("PHASE_VOL_V2", voltageV2Ext.getText().toString().trim());
                jsonObj.put("PHASE_VOL_V3", voltageV3Ext.getText().toString().trim());
                jsonObj.put("LINE_VOL_V1", lineVoltageV1VoltExt.getText().toString().trim());
                jsonObj.put("LINE_VOL_V2", lineVoltageV2VoltExt.getText().toString().trim());
                jsonObj.put("LINE_VOL_V3", lineVoltageV3VoltExt.getText().toString().trim());
                jsonObj.put("LINE_CRNT_AMP1", current1AmpExt.getText().toString().trim());
                jsonObj.put("LINE_CRNT_AMP2", current2AmpExt.getText().toString().trim());
                jsonObj.put("LINE_CRNT_AMP3", current3AmpExt.getText().toString().trim());
                jsonObj.put("FREQ_HERTZ", frequencyHzExt.getText().toString().trim());
                jsonObj.put("LINE_POWFACT_1", powerFactor1Ext.getText().toString().trim());
                jsonObj.put("LINE_POWFACT_2", powerFactor2Ext.getText().toString().trim());
                jsonObj.put("LINE_POWFACT_3", powerFactor3Ext.getText().toString().trim());
                jsonObj.put("BOREWELL_SIZE", BorwellDiameterExt.getText().toString().trim());
                jsonObj.put("BOREWELL_DEPTH", BorwellDepthExt.getText().toString().trim());
                jsonObj.put("PUMP_SET_DEPTH", pumpSetDepthExt.getText().toString().trim());
                jsonObj.put("DIS_PUMP_LPM", pumpSetDischargeExt.getText().toString().trim());
                jsonObj.put("DEL_PUMP_LPM", pumpSetDeliveryExt.getText().toString().trim());
                jsonObj.put("DISTANCE", distanceFromProposedSolarPlantExt.getText().toString().trim());
                jsonObj.put("photo1", Photo1);
                jsonObj.put("photo2", Photo2);
                jsonObj.put("photo3", Photo3);
                jsonObj.put("photo4", Photo4);

                ja_invc_data.put(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("Param====>", ja_invc_data.toString());
            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("survey", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc);
            System.out.println("param1_invc_vihu==>>" + param1_invc);
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.KusumCSurvey, param1_invc);
                Log.e("OUTPUT1", "&&&&" + obj2);
                System.out.println("OUTPUT1==>>" + obj2);
                progressDialog.dismiss();
                if (!obj2.equalsIgnoreCase("")) {
                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_save");
                    JSONArray ja = new JSONArray(obj1);
                    Log.e("OUTPUT2", "&&&&" + ja);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");
                        if (invc_done.equalsIgnoreCase("Y")) {

                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));
                           finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {
                            showingMessage(getResources().getString(R.string.dataNotSubmitted));
                            progressDialog.dismiss();


                        }
                    }
                } else {
                    CustomUtility.showToast(KusumCSurveyFormActivity.this, getResources().getString(R.string.somethingWentWrong));
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

                CustomUtility.showToast(KusumCSurveyFormActivity.this, message);

            }
        });
    }
    /*------------------------------------------------------------------------onBackPressed---------------------------------------------------------------------*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
