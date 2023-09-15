package debugapp;

import static utility.FileUtils.getPath;

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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.shaktipumplimited.shaktikusum.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import activity.BaseActivity;
import activity.CameraActivity2;
import activity.GPSTracker;
import activity.PhotoViewerActivity;
import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import database.DatabaseHelper;
import debugapp.Bean.SurveyListResponse;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class Add_Survey_Activity extends BaseActivity implements AdapterView.OnItemSelectedListener, ImageSelectionAdapter.ImageSelectionListener {


    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();

    AlertDialog alertDialog;
    int selectedIndex;
    boolean isUpdate = false;
    ImageSelectionAdapter customAdapter;

    List<String> itemNameList = new ArrayList<>();
    Toolbar toolbar;
    SurveyListResponse.Response surveyListResponse;
    EditText applicantNameExt, contactNumberExt, applicationNumberExt, addressExt, siteWaterLevelExt, releventInfoExt;
    TextView latitudeExt, longitudeExt,submitBtn;
    RadioButton radio_DarkZone_yesID, radio_DarkZone_NoID, electricConnection_yesID, electricConnection_NoID,
            InstallSolarPump_yesID, InstallSolarPump_NoID;
    Spinner sourceofWaterSpinner, sizeOfBorwellSpinner, pumpWaterLevelSpinner, AcDcSpinner, internetConnectivitySpinner, typesOfIrrigationSpinner,
            southfacingShadowSpinner, pumpSetRatingSpinner, typeOfPumpSpinner;
    RecyclerView photoListView;

    String selectedSourceofWater = "",selectedBorewellSize = "",selectedPumpWaterLevel = "",selectedAcDc = "", selectedInternetConnectivity = "", selectedTypesOfIrrigation = "", selectedSouthfacingShadow = "",
            selectedTypeOfPump = "",selectedPumpSetRating = "", latitude = "", longitude = "", Photo1 = "", Photo2 = "", Photo3 = "", Photo4 = "";

    boolean isdarkzone,isElectricConnectionFormer,isUniversalPump;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_survey);

        Init();
        retriveValue();
        SetAdapter();
    }

    private void retriveValue() {
        if (getIntent().getExtras() != null) {
            surveyListResponse = (SurveyListResponse.Response) getIntent().getSerializableExtra(Constant.surveyList);

            applicantNameExt.setText(surveyListResponse.getCustomerName());
            contactNumberExt.setText(surveyListResponse.getMobile());
            applicationNumberExt.setText(surveyListResponse.getBeneficiary());
            addressExt.setText(surveyListResponse.getAddress());
        }
    }

    private void Init() {
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.kusumSurveyform));


        applicantNameExt = findViewById(R.id.applicantNameExt);
        contactNumberExt = findViewById(R.id.contactNumberExt);
        applicationNumberExt = findViewById(R.id.applicationNumberExt);
        addressExt = findViewById(R.id.addressExt);
        siteWaterLevelExt = findViewById(R.id.siteWaterLevelExt);
        releventInfoExt = findViewById(R.id.releventInfoExt);
        latitudeExt = findViewById(R.id.latitudeExt);
        longitudeExt = findViewById(R.id.longitudeExt);
        radio_DarkZone_yesID = findViewById(R.id.radio_DarkZone_yesID);
        radio_DarkZone_NoID = findViewById(R.id.radio_DarkZone_NoID);
        electricConnection_yesID = findViewById(R.id.electricConnection_yesID);
        electricConnection_NoID = findViewById(R.id.electricConnection_NoID);
        InstallSolarPump_yesID = findViewById(R.id.InstallSolarPump_yesID);
        InstallSolarPump_NoID = findViewById(R.id.InstallSolarPump_NoID);
        sourceofWaterSpinner = findViewById(R.id.sourceofWaterSpinner);
        sizeOfBorwellSpinner = findViewById(R.id.sizeOfBorwellSpinner);
        pumpWaterLevelSpinner = findViewById(R.id.pumpWaterLevelSpinner);
        AcDcSpinner = findViewById(R.id.AcDcSpinner);
        internetConnectivitySpinner = findViewById(R.id.internetConnectivitySpinner);
        typesOfIrrigationSpinner = findViewById(R.id.typesOfIrrigationSpinner);
        southfacingShadowSpinner = findViewById(R.id.southfacingShadowSpinner);
        pumpSetRatingSpinner = findViewById(R.id.pumpSetRatingSpinner);
        typeOfPumpSpinner = findViewById(R.id.typeOfPumpSpinner);
        photoListView = findViewById(R.id.photoListView);
        submitBtn = findViewById(R.id.submitBtn);


        listner();
        getGpsLocation();
    }

    private void listner() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        sourceofWaterSpinner.setOnItemSelectedListener(this);
        sizeOfBorwellSpinner.setOnItemSelectedListener(this);
        pumpWaterLevelSpinner.setOnItemSelectedListener(this);
        AcDcSpinner.setOnItemSelectedListener(this);
        internetConnectivitySpinner.setOnItemSelectedListener(this);
        typesOfIrrigationSpinner.setOnItemSelectedListener(this);
        southfacingShadowSpinner.setOnItemSelectedListener(this);
        typeOfPumpSpinner.setOnItemSelectedListener(this);
        pumpSetRatingSpinner.setOnItemSelectedListener(this);

    }

    private void validation() {
        if(applicantNameExt.getText().toString().isEmpty()){
              CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.enter_applicant_name));
        }else if(contactNumberExt.getText().toString().isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.enter_contact_number));
        }else if(applicationNumberExt.getText().toString().isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.enter_application_number));
        }else if(addressExt.getText().toString().isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.enter_name_of_village_block_and_district));
        }else if(latitudeExt.getText().toString().isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.enterLatitude));
        }else if(longitudeExt.getText().toString().isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.enterLongitude));
        }else if(selectedSourceofWater.isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.selectSourceOfWater));
        }else if(selectedBorewellSize.isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.selectBorewellSize));
        }else if(siteWaterLevelExt.getText().toString().isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.enterSiteWaterLevel));
        }else if(selectedPumpWaterLevel.isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.selectPumpWaterLever));
        }else if(selectedAcDc.isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.selectAcDc));
        }else if(selectedInternetConnectivity.isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.selectInternetConnectivity));
        }else if(selectedTypesOfIrrigation.isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.selectTypesOfIrrigation));
        }else if(selectedSouthfacingShadow.isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.selectSouthfacingShadow));
        }else if(selectedPumpSetRating.isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.select_pumpsetrating));
        }else if(selectedTypeOfPump.isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.selectTypeOfPump));
        }else if(releventInfoExt.getText().toString().isEmpty()){
            CustomUtility.showToast(Add_Survey_Activity.this,getResources().getString(R.string.enter_releventInfo));
        }else if (!imageArrayList.get(0).isImageSelected()) {
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

            if (CustomUtility.isInternetOn(getApplicationContext())) {
              new submitSurveyForm().execute();
            } else {
                CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), getApplicationContext());
        }
    }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       if (parent.getId() == R.id.sourceofWaterSpinner) {
            if(!parent.getSelectedItem().toString().equals("Select source of water")) {
                selectedSourceofWater = parent.getSelectedItem().toString();
            }
        }else if (parent.getId() == R.id.sizeOfBorwellSpinner) {
           if(!parent.getSelectedItem().toString().equals("Select borewell Size")) {
               selectedBorewellSize = parent.getSelectedItem().toString();
           }
       }else if (parent.getId() == R.id.pumpWaterLevelSpinner) {
           if(!parent.getSelectedItem().toString().equals("Select Pump water level")) {
               selectedPumpWaterLevel = parent.getSelectedItem().toString();
           }
       } else if (parent.getId() == R.id.AcDcSpinner) {
           if(!parent.getSelectedItem().toString().equals("Select Ac and Dc")) {
               selectedAcDc = parent.getSelectedItem().toString();
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
        }  else if (parent.getId() == R.id.typeOfPumpSpinner) {
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
                longitude = decimalFormat.format(gps.getLongitude());
                latitudeExt.setText(latitude);
                longitudeExt.setText(longitude);
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

        DatabaseHelper db = new DatabaseHelper(this);

        //Create Table
        imageList = db.getAllkusumCImages();

        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {
                    if (imageList.get(i).getBillNo()!=null &&
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

        customAdapter = new ImageSelectionAdapter(Add_Survey_Activity.this, imageArrayList);
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
        LayoutInflater inflater = (LayoutInflater) Add_Survey_Activity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(Add_Survey_Activity.this, R.style.MyDialogTheme);

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
                    Intent i_display_image = new Intent(Add_Survey_Activity.this, PhotoViewerActivity.class);
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

        camraLauncher.launch(new Intent(Add_Survey_Activity.this, CameraActivity2.class)
                .putExtra("cust_name", surveyListResponse.getCustomerName()));

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
                    String path = getPath(Add_Survey_Activity.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(Add_Survey_Activity.this, "File not valid!", Toast.LENGTH_LONG).show();
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

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        if( isUpdate){
            db.updateKusumCImages(imageArrayList.get(selectedIndex).getName(), path,true, applicationNumberExt.getText().toString());
        }else {
            db.insertKusumCImages(imageArrayList.get(selectedIndex).getName(), path,true, applicationNumberExt.getText().toString());
        }

        customAdapter.notifyDataSetChanged();


    }


 private class submitSurveyForm extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Add_Survey_Activity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Sending Data to server..please wait !");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
           String invc_done = null;
            String obj2 = null;

            JSONArray ja_invc_data = new JSONArray();
            JSONObject jsonObj = new JSONObject();

            try {
                jsonObj.put("project_no", CustomUtility.getSharedPreferences(getApplicationContext(), "projectid"));
                jsonObj.put("userid", CustomUtility.getSharedPreferences(getApplicationContext(), "userid"));
                jsonObj.put("project_login_no", "01");
                jsonObj.put("FARMER_CONTACT_NO", contactNumberExt.getText().toString().trim());
                jsonObj.put("APPLICANT_NO", applicationNumberExt.getText().toString().trim());
                jsonObj.put("REGISNO", surveyListResponse.getRegisno());
                jsonObj.put("BENEFICIARY", surveyListResponse.getBeneficiary());
                jsonObj.put("SITE_ADRC", addressExt.getText().toString().trim());
                jsonObj.put("LAT", latitude);
                jsonObj.put("LNG", longitude);
                jsonObj.put("SURVEYOR_SAP", CustomUtility.getSharedPreferences(getApplicationContext(), "userid"));
                jsonObj.put("APPLICANT_NAME", CustomUtility.getSharedPreferences(getApplicationContext(), Constant.PersonName));
                jsonObj.put("CONTACT_NO", CustomUtility.getSharedPreferences(getApplicationContext(), Constant.PersonNumber));
                jsonObj.put("APPLICANT_NO", CustomUtility.getSharedPreferences(getApplicationContext(), "userid"));
                jsonObj.put("WATER_LVL", selectedPumpWaterLevel);
                jsonObj.put("FARMER_SIGNATURE", surveyListResponse.getCustomerName());
                jsonObj.put("WATER_SOURCE", selectedSourceofWater);
                jsonObj.put("INTERNET_TYPE", selectedInternetConnectivity);
                jsonObj.put("TYPE_OF_IRIGATN", selectedTypesOfIrrigation);
                jsonObj.put("PUMP_TYPE", selectedSourceofWater);
                jsonObj.put("BOREWELL_SIZE", selectedBorewellSize);
                jsonObj.put("PUMP_SET_RATING", selectedPumpSetRating);
                jsonObj.put("PUMP_WATER_LVL", siteWaterLevelExt.getText().toString().trim());
                jsonObj.put("PUMP_AC_DC", selectedAcDc);
                jsonObj.put("VILLAGE", surveyListResponse.getCitycTxt());//userID
                jsonObj.put("SHADOW_FREE_LAND", selectedSouthfacingShadow);
                jsonObj.put("REMARK_ANY_OTH", releventInfoExt.getText().toString());//userID
                jsonObj.put("photo1", Photo1);
                jsonObj.put("photo2", Photo2);
                jsonObj.put("photo3", Photo3);
                jsonObj.put("photo4", Photo4);


                if(radio_DarkZone_yesID.isChecked()){
                    jsonObj.put("DARK_ZONE_OR_NOT", "Yes");
                }else {
                    jsonObj.put("DARK_ZONE_OR_NOT", "No");
                }

                if(electricConnection_yesID.isChecked()){
                    jsonObj.put("ELEC_CON", "Yes");
                }else {
                    jsonObj.put("ELEC_CON", "No");
                }
                if(InstallSolarPump_NoID.isChecked()){
                    jsonObj.put("SOLAR_PUMP_CONTROLLER", "Yes");
                }else {
                    jsonObj.put("SOLAR_PUMP_CONTROLLER", "No");
                }

                
                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("survey", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc);

            System.out.println(param1_invc);

         /*   try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.SAVE_SURVEY_DATA, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (!obj2.equalsIgnoreCase("")) {

                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_save");


                    JSONArray ja = new JSONArray(obj1);


                    Log.e("OUTPUT2", "&&&&" + ja);

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);


                        invc_done = jo.getString("return");


                        if (invc_done.equalsIgnoreCase("Y")) {

                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));

                            progressDialog.dismiss();
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {

                            showingMessage(getResources().getString(R.string.dataNotSubmitted));
                            progressDialog.dismiss();
                            finish();
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                showingMessage(getResources().getString(R.string.somethingWentWrong));
            }*/

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();  // dismiss dialog


        }
    }


    private void showingMessage(String message) {
        runOnUiThread(new Runnable() {
            public void run() {

                CustomUtility.showToast(Add_Survey_Activity.this, message);

            }
        });
    }

}