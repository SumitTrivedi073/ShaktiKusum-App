package activity;

import static utility.FileUtils.getPath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;

public class KusumCSurveyFormActivity extends AppCompatActivity implements ImageSelectionAdapter.ImageSelectionListener {


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

    EditText farmerNameExt, contactNumberExt, applicationNumberExt, currentLatLngExt, sourceofWaterExt, cropPatternAreaExt, electricConnectionIdentificationNoEXT,
            pumpSetRatingExt, voltageV1Ext, voltageV2Ext, voltageV3Ext, lineVoltageV1VoltExt, lineVoltageV2VoltExt, lineVoltageV3VoltExt,
            current1AmpExt, current2AmpExt, current3AmpExt, frequencyHzExt, BorwellDiameterExt, BorwellDepthExt, pumpSetDepthExt, pumpSetDischargeExt,
            pumpSetDeliveryExt, distanceFromProposedSolarPlantExt;
    Spinner categorySpinner, sourceofWaterSpinner, internetConnectivitySpinner, typesOfIrrigationSpinner, southfacingShadowSpinner,
            electicConnectionTypeSpinner, typeOfPumpSpinner;

    TextView submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kusum_csurveyform);

        Init();
        listner();

    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void Init() {


        mToolbar =  findViewById(R.id.toolbar);

        photoListView = findViewById(R.id.photoListView);

        farmerNameExt = findViewById(R.id.farmerNameExt);
        contactNumberExt = findViewById(R.id.contactNumberExt);
        applicationNumberExt = findViewById(R.id.applicationNumberExt);
        currentLatLngExt = findViewById(R.id.currentLatLngExt);
        sourceofWaterExt = findViewById(R.id.sourceofWaterExt);
        cropPatternAreaExt = findViewById(R.id.cropPatternAreaExt);
        electricConnectionIdentificationNoEXT = findViewById(R.id.electricConnectionIdentificationNoEXT);
        pumpSetRatingExt = findViewById(R.id.pumpSetRatingExt);
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


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.kusumSurveyform));

        getGpsLocation();
        SetAdapter();
    }
    
    
    
    

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            if(!String.valueOf(gps.getLatitude()).isEmpty() &&  !String.valueOf(gps.getLongitude()).isEmpty()){
                currentLatLngExt.setText(String.valueOf(gps.getLatitude())+","+String.valueOf(gps.getLongitude()));
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



    /*------------------------------------------------------------------------onBackPressed---------------------------------------------------------------------*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
