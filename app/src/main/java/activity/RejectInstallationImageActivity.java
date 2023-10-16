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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import bean.ImageModel;
import bean.InstallationBean;
import bean.RejectInstallationModel;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class RejectInstallationImageActivity extends BaseActivity implements ImageSelectionAdapter.ImageSelectionListener {

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    RecyclerView recyclerview;
    AlertDialog alertDialog;
    int selectedIndex;
    ImageSelectionAdapter customAdapter;
    List<String> itemNameList = new ArrayList<>();
    List<String> positionList = new ArrayList<>();
    String enqDocno, latitude = "", longitude = "";
    Toolbar mToolbar;
    EditText remarkTxt;
    TextView submitBtn;
    boolean isUpdate = false;
    String versionName = "0.0";
    RejectInstallationModel.RejectDatum rejectDatum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_installation_image);

        Init();
        checkPermissions();

    }

    private void Init() {
        recyclerview = findViewById(R.id.recyclerview);
        remarkTxt = findViewById(R.id.remarkTxt);
        submitBtn = findViewById(R.id.submitBtn);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.rejectionImgae));

        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = getApplicationContext().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert pkgInfo != null;
        versionName = String.valueOf(pkgInfo.versionName);
        retrieveValue();
        SetAdapter();
        listner();
    }

    private void checkPermissions() {
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
                ContextCompat.checkSelfPermission(RejectInstallationImageActivity.this, CAMERA);
        int ReadMediaImages =
                ContextCompat.checkSelfPermission(RejectInstallationImageActivity.this, READ_MEDIA_IMAGES);
        int ReadAudioImages =
                ContextCompat.checkSelfPermission(RejectInstallationImageActivity.this, READ_MEDIA_AUDIO);
        int writeExternalStorage =
                ContextCompat.checkSelfPermission(RejectInstallationImageActivity.this, WRITE_EXTERNAL_STORAGE);
        int ReadExternalStorage =
                ContextCompat.checkSelfPermission(RejectInstallationImageActivity.this, READ_EXTERNAL_STORAGE);

        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && ReadMediaImages == PackageManager.PERMISSION_GRANTED
                    && ReadAudioImages == PackageManager.PERMISSION_GRANTED;
        } else {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED
                    && ReadExternalStorage == PackageManager.PERMISSION_GRANTED;

        }

    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {
            rejectDatum = (RejectInstallationModel.RejectDatum) getIntent().getSerializableExtra(Constant.RejectInstallation);

        }
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageArrayList.size() > 0) {

                    for (int i = 0; i < imageArrayList.size(); i++) {
                        if (!imageArrayList.get(i).isImageSelected()) {
                            CustomUtility.showToast(RejectInstallationImageActivity.this, getResources().getString(R.string.select_image));
                        } else {
                            new SubmitRejectImage().execute();
                        }
                    }

                } else {
                    CustomUtility.showToast(RejectInstallationImageActivity.this, getResources().getString(R.string.select_image));
                }

            }
        });
    }

    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();

        if (!rejectDatum.getPhotos1().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.photosOfCivilMaterial));
            positionList.add(rejectDatum.getPhotos1());
        }
        if (!rejectDatum.getPhotos2().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.pannelModeuleFrontSide));
            positionList.add(rejectDatum.getPhotos2());
        }
        if (!rejectDatum.getPhotos3().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.controllerWithFormer));
            positionList.add(rejectDatum.getPhotos3());
        }
        if (!rejectDatum.getPhotos4().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.dischargeWithFormer));
            positionList.add(rejectDatum.getPhotos4());
        }
        if (!rejectDatum.getPhotos5().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.foundationWithFormer));
            positionList.add(rejectDatum.getPhotos5());
        }
        if (!rejectDatum.getPhotos6().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.earthingAndLighting));
            positionList.add(rejectDatum.getPhotos6());
        }
        if (!rejectDatum.getPhotos7().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.noDuesForm));
            positionList.add(rejectDatum.getPhotos7());
        }
        if (!rejectDatum.getPhotos8().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.noNetworkNoc));
            positionList.add(rejectDatum.getPhotos8());
        }
        if (!rejectDatum.getPhotos9().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.delayInstallation));
            positionList.add(rejectDatum.getPhotos9());
        }
        if (!rejectDatum.getPhotos10().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.insideCOntroller));
            positionList.add(rejectDatum.getPhotos10());
        }
        if (!rejectDatum.getPhotos11().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.outsideController));
            positionList.add(rejectDatum.getPhotos11());
        }
        if (!rejectDatum.getPhotos12().isEmpty()) {
            itemNameList.add(getResources().getString(R.string.namePlate));
            positionList.add(rejectDatum.getPhotos12());
        }

        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo("");
            imageModel.setLatitude("");
            imageModel.setLongitude("");
            imageModel.setPoistion(i + 1);
            imageArrayList.add(imageModel);
        }

        DatabaseHelper db = new DatabaseHelper(this);

        imageList = db.getRejectedInstallationImages();


        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {
                    if (imageList.get(i).getBillNo().trim().equals(rejectDatum.getVbeln())) {
                        if (imageList.get(i).getName().equals(itemNameList.get(j))) {
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

        customAdapter = new ImageSelectionAdapter(RejectInstallationImageActivity.this, imageArrayList);
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
        LayoutInflater inflater = (LayoutInflater) RejectInstallationImageActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(RejectInstallationImageActivity.this, R.style.MyDialogTheme);

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
                    Intent i_display_image = new Intent(RejectInstallationImageActivity.this, PhotoViewerActivity.class);
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

        camraLauncher.launch(new Intent(RejectInstallationImageActivity.this, CameraActivity2.class)
                .putExtra("cust_name", rejectDatum.getCustomerName()));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();
                            UpdateArrayList(bundle.get("data").toString(), "0", bundle.get("latitude").toString(), bundle.get("longitude").toString());

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
                    String path = getPath(RejectInstallationImageActivity.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(RejectInstallationImageActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                    } else {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , mImageCaptureUri);
                        File file1 = CustomUtility.saveFile(bitmap,rejectDatum.getCustomerName().trim(),"Images");


                        UpdateArrayList(file1.getPath(), "1", latitude, longitude);

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

        imageModel.setPoistion(imageArrayList.get(selectedIndex).getPoistion());
        if (value.equals("0")) {
            imageModel.setLatitude(latitude);
            imageModel.setLongitude(longitude);
            imageArrayList.set(selectedIndex, imageModel);
            addupdateDatabase(path, latitude, longitude);
        } else {
            imageModel.setLatitude("");
            imageModel.setLongitude("");
            imageArrayList.set(selectedIndex, imageModel);
            addupdateDatabase(path, "", "");
        }
        customAdapter.notifyDataSetChanged();
    }

    private void addupdateDatabase(String path, String latitude, String longitude) {

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        if (isUpdate) {
            db.updateRejectedInstallationImage(imageArrayList.get(selectedIndex).getName(), path,
                    true, rejectDatum.getVbeln() , latitude, longitude,selectedIndex);
        } else {
            db.insertRejectedInstallationImage(imageArrayList.get(selectedIndex).getName(), path,
                    true, rejectDatum.getVbeln(), latitude, longitude,selectedIndex);
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private class SubmitRejectImage extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RejectInstallationImageActivity.this);
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
                jsonObj.put("project_no", rejectDatum.getProjectNo());
                jsonObj.put("project_login_no", "01");
                jsonObj.put("app_version", versionName);
                jsonObj.put("userid", CustomUtility.getSharedPreferences(RejectInstallationImageActivity.this, "userid"));
                jsonObj.put("beneficiary", rejectDatum.getBeneficiary());
                jsonObj.put("vbeln", rejectDatum.getVbeln());
                jsonObj.put("customer_name", rejectDatum.getCustomerName());
                jsonObj.put("LatLng1", imageArrayList.get(0).getLatitude() + "," + imageArrayList.get(0).getLongitude());

                if (imageArrayList.size() > 0) {
                    for (int i = 0; i < imageArrayList.size(); i++) {
                        if (imageArrayList.get(i).isImageSelected()) {
                            jsonObj.put("PHOTO" + imageArrayList.get(i).getPoistion(), CustomUtility.getBase64FromBitmap(RejectInstallationImageActivity.this, imageArrayList.get(i).getImagePath()));
                        }
                    }
                }

                ja_invc_data.put(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("Param====>", ja_invc_data.toString());
            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("Reject", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc);
            System.out.println("param1_invc_vihu==>>" + param1_invc);
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.saveRejectImageAPI, param1_invc);
                Log.e("OUTPUT1", "&&&&" + obj2);
                System.out.println("OUTPUT1==>>" + obj2);
                progressDialog.dismiss();
                if (!obj2.isEmpty()) {
                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_return");
                    JSONArray ja = new JSONArray(obj1);
                    Log.e("OUTPUT2", "&&&&" + ja);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");
                        if (invc_done.equalsIgnoreCase("Y")) {
                            databaseHelper.deleteRejectedInstallationImages(rejectDatum.getVbeln());
                            progressDialog.dismiss();
                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));
                            finish();
                        } else if (invc_done.equalsIgnoreCase("N")) {
                            showingMessage(getResources().getString(R.string.dataNotSubmitted));
                            progressDialog.dismiss();
                        }
                    }
                } else {
                    CustomUtility.showToast(RejectInstallationImageActivity.this, getResources().getString(R.string.somethingWentWrong));
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

                CustomUtility.showToast(RejectInstallationImageActivity.this, message);

            }
        });
    }

}