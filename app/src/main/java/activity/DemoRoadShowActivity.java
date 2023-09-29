package activity;

import static utility.FileUtils.getPath;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import database.DatabaseHelper;
import utility.CustomUtility;

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
            releventInfoExt, moduleManufacturerExt, moduleWattageExt, moduleQtyExt, billNoExt, roadShowPersonQtyExt;

    Spinner categorySpinner, sourceofWaterSpinner, borWellPresentSpinner, internetConnectivitySpinner, typesOfIrrigationSpinner, southfacingShadowSpinner,
            typeOfPumpSpinner, pumpSetRatingSpinner;
    TextView currentLatLngExt, MaterialReceivingDate;

    String latitude = "", longitude = "",selectedCategory = "", selectedSourceofWater = "", selectedInternetConnectivity = "", selectedTypesOfIrrigation = "", selectedSouthfacingShadow = "",
           selectedTypeOfPump = "", selectedPumpSetRating = "";
    Calendar calendar;

    public final static SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

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
        mToolbar = findViewById(R.id.toolbar);
        farmerNameExt = findViewById(R.id.farmerNameExt);
        contactNumberExt = findViewById(R.id.contactNumberExt);
        addressExt = findViewById(R.id.addressExt);
        NameSolarPumpManufacture = findViewById(R.id.NameSolarPumpManufacture);
        OldPumpSetDelivery = findViewById(R.id.OldPumpSetDelivery);
        releventInfoExt = findViewById(R.id.releventInfoExt);
        moduleManufacturerExt = findViewById(R.id.moduleManufacturerExt);
        moduleWattageExt = findViewById(R.id.moduleWattageExt);
        moduleQtyExt = findViewById(R.id.moduleQtyExt);
        billNoExt = findViewById(R.id.billNoExt);
        roadShowPersonQtyExt = findViewById(R.id.roadShowPersonQtyExt);
        categorySpinner = findViewById(R.id.categorySpinner);
        sourceofWaterSpinner = findViewById(R.id.sourceofWaterSpinner);
        borWellPresentSpinner = findViewById(R.id.borWellPresentSpinner);
        internetConnectivitySpinner = findViewById(R.id.internetConnectivitySpinner);
        typesOfIrrigationSpinner = findViewById(R.id.typesOfIrrigationSpinner);
        southfacingShadowSpinner = findViewById(R.id.southfacingShadowSpinner);
        typeOfPumpSpinner = findViewById(R.id.typeOfPumpSpinner);
        pumpSetRatingSpinner = findViewById(R.id.pumpSetRatingSpinner);
        currentLatLngExt = findViewById(R.id.currentLatLngExt);
        MaterialReceivingDate = findViewById(R.id.MaterialReceivingDate);
        photoListView = findViewById(R.id.photoListView);

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

        categorySpinner.setOnItemSelectedListener(this);
        sourceofWaterSpinner.setOnItemSelectedListener(this);
        internetConnectivitySpinner.setOnItemSelectedListener(this);
        typesOfIrrigationSpinner.setOnItemSelectedListener(this);
        southfacingShadowSpinner.setOnItemSelectedListener(this);
        typeOfPumpSpinner.setOnItemSelectedListener(this);
        pumpSetRatingSpinner.setOnItemSelectedListener(this);
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
        itemNameList.add("Geo-tagged photos of the surveyed site where pumps of another company are installed.");
        itemNameList.add("Completed survey form with signatures of customer and installer.)");
        itemNameList.add("Geo-tagged photos & videos of the entire system already installed at the customer's place ");
        itemNameList.add("Geo-tagged photos of Shakti Pumps with complete PMC system.");
        itemNameList.add("Geo-tagged photos of farmers' presence");
        itemNameList.add("Customer Attendance List with Mobile Number");


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

                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }
}
