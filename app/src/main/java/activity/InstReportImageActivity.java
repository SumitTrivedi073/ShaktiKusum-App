package activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static utility.FileUtils.getPath;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import database.DatabaseHelper;
import utility.CustomUtility;



public class InstReportImageActivity extends BaseActivity implements ImageSelectionAdapter.ImageSelectionListener {


    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    RecyclerView recyclerview;

    AlertDialog alertDialog;
    int selectedIndex;
    ImageSelectionAdapter customAdapter;

    List<String> itemNameList = new ArrayList<>();

    String customerName, enqDocno, status,latitude = "",longitude = "";

    Toolbar mToolbar;

    boolean isBackPressed = false,isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instreport_image);
        Init();
        CheakPermissions();
    }


    private void CheakPermissions() {
        if (checkPermission()) {
            SetAdapter();
        } else {
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


    private boolean checkPermission() {
        int cameraPermission =
                ContextCompat.checkSelfPermission(InstReportImageActivity.this, CAMERA);
        int ReadMediaImages =
                ContextCompat.checkSelfPermission(InstReportImageActivity.this, READ_MEDIA_IMAGES);
        int ReadAudioImages =
                ContextCompat.checkSelfPermission(InstReportImageActivity.this, READ_MEDIA_AUDIO);
        int writeExternalStorage =
                ContextCompat.checkSelfPermission(InstReportImageActivity.this, WRITE_EXTERNAL_STORAGE);
        int ReadExternalStorage =
                ContextCompat.checkSelfPermission(InstReportImageActivity.this, READ_EXTERNAL_STORAGE);

        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return cameraPermission == PackageManager.PERMISSION_GRANTED&& ReadMediaImages == PackageManager.PERMISSION_GRANTED
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
                            Toast.makeText(InstReportImageActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean writeExternalStorage =
                                grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadExternalStorage =
                                grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (!ACCESSCAMERA && !writeExternalStorage && !ReadExternalStorage) {
                            Toast.makeText(InstReportImageActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }

                    }
                }

                break;
        }
    }

    private void Init() {
        recyclerview = findViewById(R.id.recyclerview);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.installationImg));

        Bundle bundle = getIntent().getExtras();
        customerName = bundle.getString("cust_name");
        enqDocno = bundle.getString("inst_id");
        status = bundle.getString("delay_status");

        SetAdapter();
        listner();
    }


    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Save()) {
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Save()) {
            super.onBackPressed();
        }
    }

    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.photosOfCivilMaterial));
        itemNameList.add(getResources().getString(R.string.pannelModeuleFrontSide));
        itemNameList.add(getResources().getString(R.string.controllerWithFormer));
        itemNameList.add(getResources().getString(R.string.dischargeWithFormer));
        itemNameList.add(getResources().getString(R.string.foundationWithFormer));
        itemNameList.add(getResources().getString(R.string.earthingAndLighting));
        itemNameList.add(getResources().getString(R.string.noDuesForm));
        itemNameList.add(getResources().getString(R.string.noNetworkNoc));
        itemNameList.add(getResources().getString(R.string.delayInstallation));
        itemNameList.add(getResources().getString(R.string.insideCOntroller));
        itemNameList.add(getResources().getString(R.string.outsideController));
        itemNameList.add(getResources().getString(R.string.namePlate));


        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo("");
            imageModel.setLatitude("");
            imageModel.setLongitude("");
            imageArrayList.add(imageModel);
        }

        DatabaseHelper db = new DatabaseHelper(this);

        imageList = db.getAllInstallationImages();


        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int i = 0; i < imageList.size(); i++) {
                  for (int j = 0; j < itemNameList.size(); j++) {
                    if (imageList.get(i).getBillNo().trim().equals(getIntent().getExtras().getString("inst_id").trim())) {
                        if (imageList.get(i).getName().equals(itemNameList.get(j))) {
                            ImageModel imageModel = new ImageModel();
                            imageModel.setName(imageList.get(i).getName());
                            imageModel.setImagePath(imageList.get(i).getImagePath());
                            imageModel.setImageSelected(true);
                            imageModel.setBillNo(imageList.get(i).getBillNo());
                            imageModel.setLatitude(imageList.get(i).getLatitude());
                            imageModel.setLongitude(imageList.get(i).getLongitude());
                            imageArrayList.set(j, imageModel);
                        }
                    }
                }
            }
        }
        customAdapter = new ImageSelectionAdapter(InstReportImageActivity.this, imageArrayList);
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
        LayoutInflater inflater = (LayoutInflater) InstReportImageActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(InstReportImageActivity.this, R.style.MyDialogTheme);

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
            if(selectedIndex==0 || selectedIndex==4) {
                gallery.setVisibility(View.VISIBLE);
            }else {
                gallery.setVisibility(View.GONE);
            }
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
                    Intent i_display_image = new Intent(InstReportImageActivity.this, PhotoViewerActivity.class);
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

        camraLauncher.launch(new Intent(InstReportImageActivity.this, CameraActivity2.class)
                .putExtra("cust_name", customerName));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();
                            Log.e("bundle====>", bundle.get("data").toString()+"latitude=====>"+latitude+"longitude========>"+longitude);
                            UpdateArrayList(bundle.get("data").toString(),"0",bundle.get("latitude").toString(),bundle.get("longitude").toString());

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
                    String path = getPath(InstReportImageActivity.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(InstReportImageActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                    } else {
                        UpdateArrayList(path,"1", latitude, longitude);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    private void UpdateArrayList(String path, String value, String latitude, String longitude) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageModel.setBillNo(enqDocno);
        if(value.equals("0")) {
            imageModel.setLatitude(latitude);
            imageModel.setLongitude(longitude);
            imageArrayList.set(selectedIndex, imageModel);
            addupdateDatabase(path,latitude,longitude);
        }else {
            imageModel.setLatitude("");
            imageModel.setLongitude("");
            imageArrayList.set(selectedIndex, imageModel);
            addupdateDatabase(path,"","");
        }
        customAdapter.notifyDataSetChanged();

    }

    private void addupdateDatabase(String path, String latitude, String longitude) {

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        if (isUpdate) {
            db.updateRecordAlternate(imageArrayList.get(selectedIndex).getName(), path,
                    true, enqDocno, latitude, longitude);
        } else {
            db.insertInstallationImage(imageArrayList.get(selectedIndex).getName(), path,
                    true, enqDocno, latitude, longitude);
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public boolean Save() {
        String DeviceStatus = CustomUtility.getSharedPreferences(InstReportImageActivity.this, "DeviceStatus");

        if (DeviceStatus.equals(getResources().getString(R.string.online))){

            if (CustomUtility.getSharedPreferences(InstReportImageActivity.this, "borewellstatus").equalsIgnoreCase("2")) {
                if (!imageArrayList.get(0).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_photosOfCivilMaterial), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(1).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_pannel_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(2).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_controller_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(3).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_discharge_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(4).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_foundation_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(5).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_ethr_light_photo), Toast.LENGTH_SHORT).show();
                } else {
                    CustomUtility.setSharedPreference(InstReportImageActivity.this, "INSTSYNC" + enqDocno, "1");
                    isBackPressed = true;
                }

            }
            else if (CustomUtility.getSharedPreferences(InstReportImageActivity.this, "borewellstatus").equalsIgnoreCase("1")) {
                if (!imageArrayList.get(0).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_photosOfCivilMaterial), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(1).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_pannel_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(2).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_controller_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(3).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_discharge_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(4).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_foundation_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(5).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_ethr_light_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(6).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_nodues_signature), Toast.LENGTH_SHORT).show();
                }  else {
                    CustomUtility.setSharedPreference(InstReportImageActivity.this, "INSTSYNC" + enqDocno, "1");
                    isBackPressed = true;
                    // finish();
                }

            }
            else {
                if (!imageArrayList.get(0).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_photosOfCivilMaterial), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(1).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_pannel_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(2).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_controller_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(3).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_discharge_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(4).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_foundation_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(5).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_ethr_light_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(6).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_nodues_signature), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(7).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_noNetworkNoc), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(8).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_delay_installtion_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(9).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_inside_controllerID), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(10).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_outside_controllerID), Toast.LENGTH_SHORT).show();
                } else {
                    CustomUtility.setSharedPreference(InstReportImageActivity.this, "INSTSYNC" + enqDocno, "1");
                    isBackPressed = true;
                    //  finish();
                }

            }
        }
        else {
            Log.e("DeviceStatus==>", ""+ DeviceStatus);
            if (CustomUtility.getSharedPreferences(InstReportImageActivity.this, "borewellstatus").equalsIgnoreCase("2")) {
                if (!imageArrayList.get(0).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_photosOfCivilMaterial), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(1).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_pannel_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(2).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_controller_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(3).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_discharge_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(4).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_foundation_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(5).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_ethr_light_photo), Toast.LENGTH_SHORT).show();
                }  else if (!imageArrayList.get(7).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_noNetworkNoc), Toast.LENGTH_SHORT).show();
                }else {
                    CustomUtility.setSharedPreference(InstReportImageActivity.this, "INSTSYNC" + enqDocno, "1");
                    isBackPressed = true;
                }

            } else if (CustomUtility.getSharedPreferences(InstReportImageActivity.this, "borewellstatus").equalsIgnoreCase("1")) {
                if (!imageArrayList.get(0).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_photosOfCivilMaterial), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(1).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_pannel_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(2).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_controller_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(3).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_discharge_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(4).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_foundation_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(5).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_ethr_light_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(6).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_nodues_signature), Toast.LENGTH_SHORT).show();
                }  else if (!imageArrayList.get(7).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_noNetworkNoc), Toast.LENGTH_SHORT).show();
                } else {
                    CustomUtility.setSharedPreference(InstReportImageActivity.this, "INSTSYNC" + enqDocno, "1");
                    isBackPressed = true;
                    // finish();
                }

            } else {
                if (!imageArrayList.get(0).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_photosOfCivilMaterial), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(1).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_pannel_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(2).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_controller_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(3).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_discharge_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(4).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_foundation_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(5).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_ethr_light_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(6).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_nodues_signature), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(7).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_noNetworkNoc), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(8).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_delay_installtion_photo), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(9).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_inside_controllerID), Toast.LENGTH_SHORT).show();
                } else if (!imageArrayList.get(10).isImageSelected()) {
                    Toast.makeText(this, getResources().getString(R.string.select_outside_controllerID), Toast.LENGTH_SHORT).show();
                } else {
                    CustomUtility.setSharedPreference(InstReportImageActivity.this, "INSTSYNC" + enqDocno, "1");
                    isBackPressed = true;
                    //  finish();
                }

            }

        }


        return isBackPressed;

    }


}
