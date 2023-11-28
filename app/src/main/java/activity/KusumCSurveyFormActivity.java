package activity;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static activity.Config.TAG;
import static utility.FileUtils.getPath;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import bean.InstallationBean;
import bean.KusumCSurveyBean;
import bean.SurveyListModel;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class KusumCSurveyFormActivity extends AppCompatActivity implements ImageSelectionAdapter.ImageSelectionListener, AdapterView.OnItemSelectedListener {


    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    DatabaseHelper db;
    AlertDialog alertDialog;
    int selectedIndex;
    boolean isUpdate = false;
    ImageSelectionAdapter customAdapter;

    List<String> itemNameList = new ArrayList<>();
    Toolbar mToolbar;
    RecyclerView photoListView;
    Context mContext;
    EditText farmerNameExt, contactNumberExt, applicationNumberExt, addressExt, currentLatLngExt, cropPatternAreaExt, electricConnectionIdentificationNoEXT,
            pumpMakeEXT, voltageV1Ext, voltageV2Ext, voltageV3Ext, lineVoltageV1VoltExt, lineVoltageV2VoltExt, lineVoltageV3VoltExt,
            current1AmpExt, current2AmpExt, current3AmpExt, frequencyHzExt, powerFactor1Ext, powerFactor2Ext, powerFactor3Ext, BorwellDiameterExt, BorwellDepthExt, pumpSetDepthExt, pumpSetDischargeExt,
            pumpSetDeliveryExt, distanceFromProposedSolarPlantExt, electricConnectionRatingExt, exisCableDetailsExt, deliveryPipeLineExt, totalDynamicHeadExt, transformerRatingExt,
            serviceLineExt, threePhaseSupplyExt, ElectricityBillMonthlyExt, StructureToWaterSourceExt, feederToFarmerSiteExt, additionalInfoExt,powerInVolt,expumpSetDischargeExt,extotalDynamicHeadExt;
    Spinner categorySpinner, sourceofWaterSpinner, internetConnectivitySpinner, typesOfIrrigationSpinner, southfacingShadowSpinner,
            electicConnectionTypeSpinner, typeOfPumpSpinner, pumpSetRatingSpinner, neutralAvailabilitySpinner;

    ProgressDialog progressDialog;
    LinearLayout sourceOfWaterLinear;
    String selectedCategory = "", selectedSourceofWater = "", selectedInternetConnectivity = "", selectedTypesOfIrrigation = "", selectedSouthfacingShadow = "",
            selectedElectricConnectionType = "", selectedTypeOfPump = "", selectedPumpSetRating = "", selectedNeutralAvailability = "", latitude = "", longitude = "",
            Photo1 = "", Photo2 = "", Photo3 = "", Photo4 = "", Photo5 = "", Photo6 = "", docno_sap = null, invc_done, obj2 = null;
    ;

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
        mContext = this;
        db = new DatabaseHelper(mContext);
        cropPatternAreaExt = findViewById(R.id.cropPatternAreaExt);
        powerInVolt = findViewById(R.id.powerInVolt);
        expumpSetDischargeExt = findViewById(R.id.expumpSetDischargeExt);
        extotalDynamicHeadExt= findViewById(R.id.extotalDynamicHeadExt);
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
        electricConnectionRatingExt = findViewById(R.id.electricConnectionRatingExt);
        exisCableDetailsExt = findViewById(R.id.exisCableDetailsExt);
        deliveryPipeLineExt = findViewById(R.id.deliveryPipeLineExt);
        totalDynamicHeadExt = findViewById(R.id.totalDynamicHeadExt);
        transformerRatingExt = findViewById(R.id.transformerRatingExt);
        serviceLineExt = findViewById(R.id.serviceLineExt);
        threePhaseSupplyExt = findViewById(R.id.threePhaseSupplyExt);
        ElectricityBillMonthlyExt = findViewById(R.id.ElectricityBillMonthlyExt);
        StructureToWaterSourceExt = findViewById(R.id.StructureToWaterSourceExt);
        feederToFarmerSiteExt = findViewById(R.id.feederToFarmerSiteExt);
        additionalInfoExt = findViewById(R.id.additionalInfoExt);
        neutralAvailabilitySpinner = findViewById(R.id.neutralAvailabilitySpinner);
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

        KusumCSurveyBean kusumCSurveyBean = new KusumCSurveyBean();

        kusumCSurveyBean = db.getKusumCSurvey(applicationNumberExt.getText().toString());

        if (kusumCSurveyBean.getAPPLICANT_NO() != null) {

          //  cropPatternAreaExt = findViewById(R.id.cropPatternAreaExt);
            electricConnectionIdentificationNoEXT = findViewById(R.id.electricConnectionIdentificationNoEXT);

            pumpMakeEXT.setText(kusumCSurveyBean.getPUMP_MAKE());
            voltageV1Ext.setText(kusumCSurveyBean.getPHASE_VOL_V1());
            voltageV2Ext.setText(kusumCSurveyBean.getPHASE_VOL_V2());
            voltageV3Ext.setText(kusumCSurveyBean.getPHASE_VOL_V3());
            lineVoltageV1VoltExt.setText(kusumCSurveyBean.getLINE_VOL_V1());
            lineVoltageV2VoltExt.setText(kusumCSurveyBean.getLINE_VOL_V2());
            lineVoltageV3VoltExt.setText(kusumCSurveyBean.getLINE_VOL_V3());
            current1AmpExt.setText(kusumCSurveyBean.getLINE_CRNT_AMP1());
            current2AmpExt.setText(kusumCSurveyBean.getLINE_CRNT_AMP2());
            current3AmpExt.setText(kusumCSurveyBean.getLINE_CRNT_AMP3());
            frequencyHzExt.setText(kusumCSurveyBean.getFREQ_HERTZ());
            powerFactor1Ext.setText(kusumCSurveyBean.getLINE_POWFACT_1());
            powerFactor2Ext.setText(kusumCSurveyBean.getLINE_POWFACT_2());
            powerFactor3Ext.setText(kusumCSurveyBean.getLINE_POWFACT_3());
            BorwellDiameterExt.setText(kusumCSurveyBean.getBOREWELL_SIZE());
            BorwellDepthExt.setText(kusumCSurveyBean.getBOREWELL_DEPTH());
            pumpSetDepthExt.setText(kusumCSurveyBean.getPUMP_SET_DEPTH());
            pumpSetDischargeExt.setText(kusumCSurveyBean.getDIS_PUMP_LPM());
            pumpSetDeliveryExt.setText(kusumCSurveyBean.getDEL_PUMP_LPM());
            distanceFromProposedSolarPlantExt.setText(kusumCSurveyBean.getDISTANCE());
            electricConnectionIdentificationNoEXT.setText(kusumCSurveyBean.getELEC_IDEN_NO());
            frequencyHzExt.setText(kusumCSurveyBean.getFREQ_HERTZ());
            cropPatternAreaExt.setText(kusumCSurveyBean.getCROP_PATTERN());
            powerInVolt.setText(kusumCSurveyBean.getpowerInVolt());
            expumpSetDischargeExt.setText(kusumCSurveyBean.getExDischarge());
            extotalDynamicHeadExt.setText(kusumCSurveyBean.getExDynamichead());
            electricConnectionRatingExt.setText(kusumCSurveyBean.getELEC_CONN_RAT());
            exisCableDetailsExt.setText(kusumCSurveyBean.getCABLE_DET_MAKE());
            deliveryPipeLineExt.setText(kusumCSurveyBean.getPIPE_LEN_SIZE());
            totalDynamicHeadExt.setText(kusumCSurveyBean.getDYNAMIC_HEAD());

            transformerRatingExt.setText(kusumCSurveyBean.getTRANSF_RATING());
            serviceLineExt.setText(kusumCSurveyBean.getSERVICE_LINE());
            threePhaseSupplyExt.setText(kusumCSurveyBean.getTHREE_PH_SUPPLY());
            ElectricityBillMonthlyExt.setText(kusumCSurveyBean.getELECTRIC_BILL());
            StructureToWaterSourceExt.setText(kusumCSurveyBean.getWATER_SOURC_LEN());
            feederToFarmerSiteExt.setText(kusumCSurveyBean.getDIST_FARMAR());
            additionalInfoExt.setText(kusumCSurveyBean.getIFNO_REMARK());

            List<String> categoryList = Arrays.asList(getResources().getStringArray(R.array.categorySpinner));

            for (int i = 0; i < categoryList.size(); i++) {
                if (kusumCSurveyBean.getCATEGORY().equalsIgnoreCase(categoryList.get(i))) {
                    categorySpinner.setSelection(i, true);
                    selectedCategory = kusumCSurveyBean.getCATEGORY();
                }
            }

            List<String> sourceofwaterList = Arrays.asList(getResources().getStringArray(R.array.sourceOfWater));

            for (int i = 0; i < sourceofwaterList.size(); i++) {
                if (kusumCSurveyBean.getWATER_SOURCE().equalsIgnoreCase(sourceofwaterList.get(i))) {
                    sourceofWaterSpinner.setSelection(i, true);
                    selectedSourceofWater = kusumCSurveyBean.getWATER_SOURCE();
                }
            }

            List<String> internetConnectivitySpinnerList = Arrays.asList(getResources().getStringArray(R.array.internetConnectivity));

            for (int i = 0; i < internetConnectivitySpinnerList.size(); i++) {
                if (kusumCSurveyBean.getINTERNET_TYPE().equalsIgnoreCase(internetConnectivitySpinnerList.get(i))) {
                    internetConnectivitySpinner.setSelection(i, true);
                    selectedInternetConnectivity = kusumCSurveyBean.getINTERNET_TYPE();
                }
            }

            List<String> typesOfIrrigationSpinnerList = Arrays.asList(getResources().getStringArray(R.array.irrigationInstalled));

            for (int i = 0; i < typesOfIrrigationSpinnerList.size(); i++) {
                if (kusumCSurveyBean.getTYPE_OF_IRIGATN().equalsIgnoreCase(typesOfIrrigationSpinnerList.get(i))) {
                    typesOfIrrigationSpinner.setSelection(i, true);
                    selectedTypesOfIrrigation = kusumCSurveyBean.getTYPE_OF_IRIGATN();
                }
            }

            List<String> southfacingShadowSpinnerList = Arrays.asList(getResources().getStringArray(R.array.yesNoSpinner));

            for (int i = 0; i < southfacingShadowSpinnerList.size(); i++) {
                if (kusumCSurveyBean.getSHADOW_FREE_LAND().equalsIgnoreCase(southfacingShadowSpinnerList.get(i))) {
                    southfacingShadowSpinner.setSelection(i, true);
                    selectedSouthfacingShadow = kusumCSurveyBean.getSHADOW_FREE_LAND();
                }
            }

            List<String> electicConnectionTypeSpinnerList = Arrays.asList(getResources().getStringArray(R.array.electricConnectionType));

            for (int i = 0; i < electicConnectionTypeSpinnerList.size(); i++) {
                if (kusumCSurveyBean.getELEC_CON().equalsIgnoreCase(electicConnectionTypeSpinnerList.get(i))) {
                    electicConnectionTypeSpinner.setSelection(i, true);
                    selectedElectricConnectionType = kusumCSurveyBean.getELEC_CON();
                }
            }

            List<String> typeOfPumpSpinnerList = Arrays.asList(getResources().getStringArray(R.array.typeOfPump));

            for (int i = 0; i < typeOfPumpSpinnerList.size(); i++) {
                if (kusumCSurveyBean.getPUMP_TYPE().equalsIgnoreCase(typeOfPumpSpinnerList.get(i))) {
                    typeOfPumpSpinner.setSelection(i, true);
                    selectedTypeOfPump = kusumCSurveyBean.getPUMP_TYPE();
                }
            }

            List<String> pumpSetRatingSpinnerList = Arrays.asList(getResources().getStringArray(R.array.pumpRating));

            for (int i = 0; i < pumpSetRatingSpinnerList.size(); i++) {
                if (kusumCSurveyBean.getPUMP_SET_RATING().equalsIgnoreCase(pumpSetRatingSpinnerList.get(i))) {
                    pumpSetRatingSpinner.setSelection(i, true);
                    selectedPumpSetRating = kusumCSurveyBean.getPUMP_SET_RATING();
                }
            }
            List<String> neutralAvailabilitySpinnerList = Arrays.asList(getResources().getStringArray(R.array.neutralAvailability));

            for (int i = 0; i < neutralAvailabilitySpinnerList.size(); i++) {
                if (kusumCSurveyBean.getNEUTRL_GRID_AVBL().equalsIgnoreCase(neutralAvailabilitySpinnerList.get(i))) {
                    neutralAvailabilitySpinner.setSelection(i, true);
                    selectedNeutralAvailability = kusumCSurveyBean.getNEUTRL_GRID_AVBL();
                }
            }

        }


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
                ValidationCheck();

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
        neutralAvailabilitySpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.categorySpinner) {
            if (!parent.getSelectedItem().toString().equals("Select Category")) {
                selectedCategory = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.sourceofWaterSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select source of water")) {
                selectedSourceofWater = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.internetConnectivitySpinner) {
            if (!parent.getSelectedItem().toString().equals("Select internet connectivity type")) {
                selectedInternetConnectivity = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.typesOfIrrigationSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select type of irrigation installed")) {
                selectedTypesOfIrrigation = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.southfacingShadowSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select option for south facing shadow")) {
                selectedSouthfacingShadow = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.electicConnectionTypeSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select electric Connection Type")) {
                selectedElectricConnectionType = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.typeOfPumpSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select type of pump")) {
                selectedTypeOfPump = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.pumpSetRatingSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select Pump Rating")) {
                selectedPumpSetRating = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.neutralAvailabilitySpinner) {
            if (!parent.getSelectedItem().toString().equals("Select option for Neutral Availability")) {
                selectedNeutralAvailability = parent.getSelectedItem().toString();
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

                latitude = String.valueOf(gps.getLatitude());
                longitude = String.valueOf(gps.getLongitude());
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
        itemNameList.add(getResources().getString(R.string.attechformphoto3));
        itemNameList.add(getResources().getString(R.string.attechformphoto4));

        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo("");
            imageArrayList.add(imageModel);
        }

        DatabaseHelper db = new DatabaseHelper(this);

        //Create Table
        imageList = db.getAllkusumCImages();

        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {
                    if (imageList.get(i).getBillNo() != null &&
                            imageList.get(i).getBillNo().trim().equals(applicationNumberExt.getText().toString())) {
                        if (imageList.get(i).getName().equals(itemNameList.get(j))) {
                            ImageModel imageModel = new ImageModel();
                            imageModel.setName(imageList.get(i).getName());
                            imageModel.setImagePath(imageList.get(i).getImagePath());
                            imageModel.setBillNo(imageList.get(i).getBillNo());
                            imageModel.setImageSelected(true);
                            imageArrayList.set(j, imageModel);
                        }
                    }
                }
            }
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
            // gallery.setVisibility(View.GONE);
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

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);
                        File file1 = CustomUtility.saveFile(bitmap, surveyListModel.getCustomerName().trim(), "Images");

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
        imageModel.setBillNo("");
        imageArrayList.set(selectedIndex, imageModel);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        if (isUpdate) {
            db.updateKusumCImages(imageArrayList.get(selectedIndex).getName(), path, true, applicationNumberExt.getText().toString());
        } else {
            db.insertKusumCImages(imageArrayList.get(selectedIndex).getName(), path, true, applicationNumberExt.getText().toString());
        }

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
        } else if (electricConnectionRatingExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_electric_connection_rating_hp), getApplicationContext());
        } else if (selectedPumpSetRating.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.select_pumpsetrating), getApplicationContext());
        } else if (pumpMakeEXT.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_pumpsetmake), getApplicationContext());
        } else if (exisCableDetailsExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_existing_cable_details), getApplicationContext());
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
        } else if (deliveryPipeLineExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_delivery_pipe_line), getApplicationContext());
        } else if (totalDynamicHeadExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_total_dynamic_head_meter), getApplicationContext());
        } else if (transformerRatingExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_transformer_rating), getApplicationContext());
        } else if (serviceLineExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_service_line), getApplicationContext());
        } else if (threePhaseSupplyExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_three_phase_supply_time), getApplicationContext());
        } else if (ElectricityBillMonthlyExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_electricity_bill), getApplicationContext());
        } else if (selectedNeutralAvailability.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.select_option_for_neutral_Availability), getApplicationContext());
        } else if (StructureToWaterSourceExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_structure_to_water_source), getApplicationContext());
        } else if (feederToFarmerSiteExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_feeder_to_farmer_site), getApplicationContext());
        } else if(powerInVolt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.enterPowerIn), getApplicationContext());
        } else if(expumpSetDischargeExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.expumpSetDischarge), getApplicationContext());
        }/* else if(extotalDynamicHeadExt.getText().toString().isEmpty()){
            CustomUtility.ShowToast(getResources().getString(R.string.exDynamic), getApplicationContext());
        }*/else if (!imageArrayList.get(0).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.attechWaterSourcePhoto), getApplicationContext());

        } else if (!imageArrayList.get(1).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.attechTransformerPhoto), getApplicationContext());

        } else if (!imageArrayList.get(2).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.attechFormPhoto), getApplicationContext());

        } else if (!imageArrayList.get(3).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.attechFormPhoto), getApplicationContext());

        } else if (!imageArrayList.get(4).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.attechFormPhoto), getApplicationContext());

        } else if (!imageArrayList.get(5).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.attechFormPhoto), getApplicationContext());

        } else {

            Photo1 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(0).getImagePath());
            Photo2 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(1).getImagePath());
            Photo3 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(2).getImagePath());
            Photo4 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(3).getImagePath());
            Photo5 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(4).getImagePath());
            Photo6 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(5).getImagePath());


            savedInDataBase();


        }
    }

    private void savedInDataBase() {
        KusumCSurveyBean kusumCSurveyBean = new KusumCSurveyBean(CustomUtility.getSharedPreferences(getApplicationContext(), "projectid"), CustomUtility.getSharedPreferences(getApplicationContext(), "userid"),
                "01", contactNumberExt.getText().toString().trim(), applicationNumberExt.getText().toString().trim(), surveyListModel.getRegisno(), surveyListModel.getBeneficiary(), addressExt.getText().toString().trim(), latitude, longitude, selectedCategory, selectedSourceofWater,
                selectedInternetConnectivity, cropPatternAreaExt.getText().toString().trim(), selectedTypesOfIrrigation,
                selectedSouthfacingShadow, selectedElectricConnectionType, electricConnectionIdentificationNoEXT.getText().toString().trim(),
                selectedTypeOfPump, selectedPumpSetRating, pumpMakeEXT.getText().toString().trim(), voltageV1Ext.getText().toString().trim(),
                voltageV2Ext.getText().toString().trim(), voltageV3Ext.getText().toString().trim(), lineVoltageV1VoltExt.getText().toString().trim(),
                lineVoltageV2VoltExt.getText().toString().trim(), lineVoltageV3VoltExt.getText().toString().trim(), current1AmpExt.getText().toString().trim(),
                current2AmpExt.getText().toString().trim(), current3AmpExt.getText().toString().trim(), frequencyHzExt.getText().toString().trim(), powerFactor1Ext.getText().toString().trim(),
                powerFactor2Ext.getText().toString().trim(), powerFactor3Ext.getText().toString().trim(), BorwellDiameterExt.getText().toString().trim(), BorwellDepthExt.getText().toString().trim(),
                pumpSetDepthExt.getText().toString().trim(), pumpSetDischargeExt.getText().toString().trim(), pumpSetDeliveryExt.getText().toString().trim(), distanceFromProposedSolarPlantExt.getText().toString().trim(),
                electricConnectionRatingExt.getText().toString().trim(), exisCableDetailsExt.getText().toString().trim(), deliveryPipeLineExt.getText().toString().trim(),
                totalDynamicHeadExt.getText().toString().trim(), transformerRatingExt.getText().toString().trim(),
                serviceLineExt.getText().toString().trim(), threePhaseSupplyExt.getText().toString().trim(), ElectricityBillMonthlyExt.getText().toString().trim(),
                selectedNeutralAvailability, StructureToWaterSourceExt.getText().toString().trim(), feederToFarmerSiteExt.getText().toString().trim(),
                additionalInfoExt.getText().toString().trim(),powerInVolt.getText().toString().trim(),expumpSetDischargeExt.getText().toString().trim(),extotalDynamicHeadExt.getText().toString().trim(),imageArrayList.get(0).getImagePath(), imageArrayList.get(1).getImagePath(),
                imageArrayList.get(2).getImagePath(), imageArrayList.get(3).getImagePath(), imageArrayList.get(4).getImagePath(), imageArrayList.get(5).getImagePath());


        if (db.isRecordExist(DatabaseHelper.TABLE_KUSUMCSURVEYFORM, DatabaseHelper.KEY_APPLICANT_NO, applicationNumberExt.getText().toString().trim())) {
            db.updateKusumCSurveyform(applicationNumberExt.getText().toString().trim(), kusumCSurveyBean);
        } else {
            db.insertKusumCSurveyform(applicationNumberExt.getText().toString().trim(), kusumCSurveyBean);
        }


        if (CustomUtility.isInternetOn(getApplicationContext())) {

           new  submitSurveyForm().execute();

        } else {
            CustomUtility.ShowToast(getResources().getString(R.string.dataSavedOffline), getApplicationContext());
            onBackPressed();
        }
    }

    private class submitSurveyForm extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {


            CustomUtility.showProgressDialogue(KusumCSurveyFormActivity.this);

        }

        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
        @Override
        protected String doInBackground(String... params) {
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
                jsonObj.put("SITE_ADRC", addressExt.getText().toString().trim().replaceAll("/"," ").replaceAll("-"," "));
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
                jsonObj.put("ELEC_CONN_RAT", electricConnectionRatingExt.getText().toString().trim());
                jsonObj.put("PUMP_SET_RATING", selectedPumpSetRating);
                jsonObj.put("PUMP_MAKE", pumpMakeEXT.getText().toString().trim());
                jsonObj.put("CABLE_DET_MAKE", exisCableDetailsExt.getText().toString().trim());
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

                jsonObj.put("EXP_VOLT", powerInVolt.getText().toString().trim());
                jsonObj.put("EXP_DISCHARGE", expumpSetDischargeExt.getText().toString().trim());
                jsonObj.put("EXP_HEAD", extotalDynamicHeadExt.getText().toString().trim());


                jsonObj.put("PIPE_LEN_SIZE", deliveryPipeLineExt.getText().toString().trim());
                jsonObj.put("DYNAMIC_HEAD", totalDynamicHeadExt.getText().toString().trim());

                jsonObj.put("TRANSF_RATING", transformerRatingExt.getText().toString().trim());
                jsonObj.put("SERVICE_LINE", serviceLineExt.getText().toString().trim());
                jsonObj.put("THREE_PH_SUPPLY", threePhaseSupplyExt.getText().toString().trim());
                jsonObj.put("ELECTRIC_BILL", ElectricityBillMonthlyExt.getText().toString().trim());
                jsonObj.put("NEUTRL_GRID_AVBL", selectedNeutralAvailability);
                jsonObj.put("WATER_SOURC_LEN", StructureToWaterSourceExt.getText().toString().trim());
                jsonObj.put("DIST_FARMAR", feederToFarmerSiteExt.getText().toString().trim());
                jsonObj.put("IFNO_REMARK", additionalInfoExt.getText().toString().trim());


                jsonObj.put("photo1", Photo1);
                jsonObj.put("photo2", Photo2);
                jsonObj.put("photo3", Photo3);
                jsonObj.put("photo4", Photo4);
                jsonObj.put("photo5", Photo5);
                jsonObj.put("photo6", Photo6);

                ja_invc_data.put(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("survey", String.valueOf(ja_invc_data)));

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);


                obj2 = CustomHttpClient.executeHttpPost1(WebURL.KusumCSurvey, param1_invc);

                Log.e("Response",obj2.toString());

                if (!obj2.isEmpty()) {

                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_save");
                    Log.e("Response1234",obj1.toString());
                    JSONArray ja = new JSONArray(obj1);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        invc_done = jo.getString("return");
                        if (invc_done.equalsIgnoreCase("Y")) {


                        if (db.isRecordExist(DatabaseHelper.TABLE_KUSUMCSURVEYFORM, DatabaseHelper.KEY_APPLICANT_NO, applicationNumberExt.getText().toString().trim())) {
                            db.deleteKusumCSurveyFromSpecificItem(applicationNumberExt.getText().toString().trim());
                        }
                        CustomUtility.deleteArrayList(getApplicationContext(), Constant.surveyList);
                        CustomUtility.removeValueFromSharedPref(getApplicationContext(), Constant.currentDate);

                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));
                            CustomUtility.hideProgressDialog(KusumCSurveyFormActivity.this);
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {
                            showingMessage(getResources().getString(R.string.dataNotSubmitted));
                            CustomUtility.hideProgressDialog(KusumCSurveyFormActivity.this);
                        }
                    }
                } else {
                    showingMessage(getResources().getString(R.string.somethingWentWrong));
                    CustomUtility.hideProgressDialog(KusumCSurveyFormActivity.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
                CustomUtility.hideProgressDialog(KusumCSurveyFormActivity.this);
            }
            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

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
