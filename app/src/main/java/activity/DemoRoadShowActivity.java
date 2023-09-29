package activity;

import static utility.FileUtils.getPath;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.TestLooperManager;
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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import bean.KusumCSurveyBean;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class DemoRoadShowActivity extends BaseActivity implements ImageSelectionAdapter.ImageSelectionListener, AdapterView.OnItemSelectedListener {

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

    EditText farmerNameExt, contactNumberExt, addressExt, NameSolarPumpManufacture, OldPumpSetDelivery,
            releventInfoExt, moduleManufacturerExt, depth, moduleWattageExt, moduleQtyExt, billNoExt, roadShowPersonQtyExt,beneficiaryNoExt;

    Spinner categorySpinner, sourceofWaterSpinner, borWellPresentSpinner, internetConnectivitySpinner, typesOfIrrigationSpinner, southfacingShadowSpinner,
            typeOfPumpSpinner, pumpSetRatingSpinner;
    TextView save, currentLatLngExt, MaterialReceivingDate;

    RadioButton salesRadio, installerRadio;

    String latitude = "",sendDateMaterial, longitude = "",selectedCategory = "", selectedSourceofWater = "", selectedInternetConnectivity = "", selectedTypesOfIrrigation = "", selectedSouthfacingShadow = "",
           selectedTypeOfPump = "", selectedPumpSetRating = "", selectedborwell = "", Photo1 = "", Photo2 = "", Photo3 = "", Photo4 = "", Photo5 = "", Photo6 = "";
    Calendar calendar;

    public final static SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    public final static SimpleDateFormat sendDateFormat =
            new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_road_show);

        Init();
        listner();
    }

    /*-----------------------------------------------------------------------Initialize---------------------------------------------------------------------*/


    private void Init() {


        calendar = Calendar.getInstance();
        salesRadio = findViewById(R.id.salesRadio);
        installerRadio =findViewById(R.id.installerRadio);
        mToolbar = findViewById(R.id.toolbar);
        farmerNameExt = findViewById(R.id.farmerNameExt);
        contactNumberExt = findViewById(R.id.contactNumberExt);
        addressExt = findViewById(R.id.addressExt);
        NameSolarPumpManufacture = findViewById(R.id.NameSolarPumpManufacture);
        OldPumpSetDelivery = findViewById(R.id.OldPumpSetDelivery);
        releventInfoExt = findViewById(R.id.releventInfoExt);
        moduleManufacturerExt = findViewById(R.id.moduleManufacturerExt);
        beneficiaryNoExt = findViewById(R.id.beneficiaryNoExt);
        moduleWattageExt = findViewById(R.id.moduleWattageExt);
        moduleQtyExt = findViewById(R.id.moduleQtyExt);
        billNoExt = findViewById(R.id.billNoExt);
        roadShowPersonQtyExt = findViewById(R.id.roadShowPersonQtyExt);
        categorySpinner = findViewById(R.id.categorySpinner);
        sourceofWaterSpinner = findViewById(R.id.sourceofWaterSpinner);
        borWellPresentSpinner = findViewById(R.id.borWellPresentSpinner);
        depth = findViewById(R.id.depth);
        internetConnectivitySpinner = findViewById(R.id.internetConnectivitySpinner);
        typesOfIrrigationSpinner = findViewById(R.id.typesOfIrrigationSpinner);
        southfacingShadowSpinner = findViewById(R.id.southfacingShadowSpinner);
        typeOfPumpSpinner = findViewById(R.id.typeOfPumpSpinner);
        pumpSetRatingSpinner = findViewById(R.id.pumpSetRatingSpinner);
        currentLatLngExt = findViewById(R.id.currentLatLngExt);
        MaterialReceivingDate = findViewById(R.id.MaterialReceivingDate);
        photoListView = findViewById(R.id.photoListView);
        save = findViewById(R.id.save);



        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.demo_road_show_form));

        MaterialReceivingDate.setText(CustomUtility.getCurrentDate());
        getGpsLocation();
        SetAdapter();
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        MaterialReceivingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDate();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationCheck();

            }
        });

        categorySpinner.setOnItemSelectedListener(this);
        sourceofWaterSpinner.setOnItemSelectedListener(this);
        borWellPresentSpinner.setOnItemSelectedListener(this);
        internetConnectivitySpinner.setOnItemSelectedListener(this);
        typesOfIrrigationSpinner.setOnItemSelectedListener(this);
        southfacingShadowSpinner.setOnItemSelectedListener(this);
        typeOfPumpSpinner.setOnItemSelectedListener(this);
        pumpSetRatingSpinner.setOnItemSelectedListener(this);
    }

    private void ValidationCheck() {

        if (farmerNameExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_farmar_name), getApplicationContext());
        } else if (contactNumberExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_contact_number), getApplicationContext());
        } else if (!CustomUtility.isValidMobile(contactNumberExt.getText().toString().trim())) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_valid_contact_number), getApplicationContext());
        } else if (addressExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_name_of_village_block_and_district), getApplicationContext());
        } else if (currentLatLngExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_LatLng), getApplicationContext());
        } else if (selectedCategory.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectCategoty), getApplicationContext());
        } else if (selectedSourceofWater.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectSourceOfWater), getApplicationContext());
        }  else if (selectedborwell.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectBorwell), getApplicationContext());
        }  else if (depth.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_depth), getApplicationContext());
        }else if (selectedInternetConnectivity.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectInternetConnectivity), getApplicationContext());
        } else if (selectedTypesOfIrrigation.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectTypesOfIrrigation), getApplicationContext());
        } else if (selectedSouthfacingShadow.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectSouthfacingShadow), getApplicationContext());
        } else if (NameSolarPumpManufacture.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.nameManufacturer), getApplicationContext());
        }  else if (selectedTypeOfPump.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.selectTypeOfPump), getApplicationContext());
        } else if (selectedPumpSetRating.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.select_pumpsetrating), getApplicationContext());
        }  else if ( OldPumpSetDelivery.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_delivery_of_old_pump_set), getApplicationContext());
        }   else if ( releventInfoExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_ReleventInfoExt), getApplicationContext());
        }    else if ( moduleManufacturerExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_module_manufacturer), getApplicationContext());
        }   else if ( moduleWattageExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_module_wattage), getApplicationContext());
        }   else if ( moduleQtyExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_module_qty), getApplicationContext());
        }  else if (billNoExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_bill_No), getApplicationContext());
        }  else if (beneficiaryNoExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_beneficiaryNoExt), getApplicationContext());
        }    else if (roadShowPersonQtyExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_roadShowPersonQtyExt), getApplicationContext());
        }
         if (!imageArrayList.get(0).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.select_installed), getApplicationContext());

        } else if (!imageArrayList.get(1).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.customerSelect), getApplicationContext());

        } else if (!imageArrayList.get(2).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.geoSelect), getApplicationContext());

        } else if (!imageArrayList.get(3).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.PMCSelect), getApplicationContext());

        }  else if (!imageArrayList.get(4).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.presenceSelect), getApplicationContext());

        } else if (!imageArrayList.get(5).isImageSelected()) {
            CustomUtility.ShowToast(getResources().getString(R.string.mobileNoSelect), getApplicationContext());

        } else {

            Photo1 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(0).getImagePath());
            Photo2 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(1).getImagePath());
            Photo3 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(2).getImagePath());
            Photo4 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(3).getImagePath());
            Photo5 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(4).getImagePath());
            Photo6 = CustomUtility.getBase64FromBitmap(getApplicationContext(), imageArrayList.get(5).getImagePath());


            if (CustomUtility.isInternetOn(getApplicationContext())) {
                new submitDemoRoadForm().execute();
            }

        }
    }



    @SuppressLint("StaticFieldLeak")
    private class submitDemoRoadForm extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DemoRoadShowActivity.this);
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

                if (installerRadio.isChecked()) {
                    jsonObj.put( "installer ", installerRadio.getText().toString().trim());
                } else {
                    jsonObj.put("installer", "");
                }
                if (salesRadio.isChecked()) {
                    jsonObj.put("sales_emp", salesRadio.getText().toString().trim());
                } else {
                    jsonObj.put("sales_emp", "");
                }
                jsonObj.put("father_name",  farmerNameExt.getText().toString().trim());
                jsonObj.put("contact", contactNumberExt.getText().toString().trim());
                jsonObj.put("village", addressExt.getText().toString().trim());
                jsonObj.put("lat", latitude);
                jsonObj.put("log", longitude);
                jsonObj.put("catogry", selectedCategory);
                jsonObj.put("source", selectedSourceofWater);
                jsonObj.put("present", selectedborwell);
                jsonObj.put("depth", depth.getText().toString().trim());
                jsonObj.put("type_of_inter", selectedInternetConnectivity);
                jsonObj.put("irrgation", selectedTypesOfIrrigation);
                jsonObj.put("south_facing", selectedSouthfacingShadow);
                jsonObj.put("old_solar", NameSolarPumpManufacture.getText().toString().trim());
                jsonObj.put("type_of_pump", selectedTypeOfPump);
                jsonObj.put("rating", selectedPumpSetRating);
                jsonObj.put("inch",OldPumpSetDelivery.getText().toString().trim() );
                jsonObj.put("remark",  releventInfoExt.getText().toString().trim());
                jsonObj.put("module_manu",moduleManufacturerExt.getText().toString().trim() );
                jsonObj.put("module_watt",moduleWattageExt.getText().toString().trim());
                jsonObj.put("module_qty",moduleQtyExt.getText().toString().trim() );
                jsonObj.put("vbeln",billNoExt.getText().toString().trim() );
                jsonObj.put("beneficiary", beneficiaryNoExt.getText().toString().trim());
                jsonObj.put("date1",sendDateMaterial);
                jsonObj.put("per_aty", roadShowPersonQtyExt.getText().toString().trim() );
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
            Log.e("DemoParam====>", ja_invc_data.toString());
            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("demo_road", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc);
            System.out.println("param1_invc_vihu==>>" + param1_invc);
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.DemoRoadURL, param1_invc);
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

                CustomUtility.showToast(DemoRoadShowActivity.this, message);

            }
        });
    }
    /*------------------------------------------------------------------------Retrieve lat long---------------------------------------------------------------------*/

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            if (!String.valueOf(gps.getLatitude()).isEmpty() && !String.valueOf(gps.getLongitude()).isEmpty()) {

                DecimalFormat decimalFormat = new DecimalFormat("##.#####");
                latitude = decimalFormat.format(gps.getLatitude());
                longitude = decimalFormat.format(gps.getLongitude());
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
        itemNameList.add(getResources().getString(R.string.installed));
        itemNameList.add(getResources().getString(R.string.customer));
        itemNameList.add(getResources().getString(R.string.geo));
        itemNameList.add(getResources().getString(R.string.PMC));
        itemNameList.add(getResources().getString(R.string.presence));
        itemNameList.add(getResources().getString(R.string.mobileNo));


        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo("");
            imageArrayList.add(imageModel);
        }

        /*DatabaseHelper db = new DatabaseHelper(this);

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
        }*/

        customAdapter = new ImageSelectionAdapter(DemoRoadShowActivity.this, imageArrayList);
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
        LayoutInflater inflater = (LayoutInflater) DemoRoadShowActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(DemoRoadShowActivity.this, R.style.MyDialogTheme);

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
                    Intent i_display_image = new Intent(DemoRoadShowActivity.this, PhotoViewerActivity.class);
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

        camraLauncher.launch(new Intent(DemoRoadShowActivity.this, CameraActivity2.class)
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
                    String path = getPath(DemoRoadShowActivity.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(DemoRoadShowActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
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

        if (isUpdate) {
            db.updateKusumCImages(imageArrayList.get(selectedIndex).getName(), path, true, "");
        } else {
            db.insertKusumCImages(imageArrayList.get(selectedIndex).getName(), path, true, "");
        }

        customAdapter.notifyDataSetChanged();


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
        } else if (parent.getId() == R.id.borWellPresentSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select BorWell/ OpenWell Present")) {
                selectedborwell = parent.getSelectedItem().toString();

            }
        }  else if (parent.getId() == R.id.internetConnectivitySpinner) {
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
        }  else if (parent.getId() == R.id.typeOfPumpSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select type of pump")) {
                selectedTypeOfPump = parent.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.pumpSetRatingSpinner) {
            if (!parent.getSelectedItem().toString().equals("Select Pump Rating")) {
                selectedPumpSetRating = parent.getSelectedItem().toString();
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*------------------------------------------------------------------------Date Picker---------------------------------------------------------------------*/

    private void ChooseDate() {

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, R.style.DialogTheme,
                        (view, year, month, dayOfMonth) -> {
                            calendar.set(year, month, dayOfMonth, calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE));
                            MaterialReceivingDate.setText(dateFormat.format(calendar.getTime()));
                            sendDateMaterial= sendDateFormat.format(calendar.getTime());

                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }
}
