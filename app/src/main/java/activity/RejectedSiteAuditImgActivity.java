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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import bean.RejectListBean;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import pub.devrel.easypermissions.EasyPermissions;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class RejectedSiteAuditImgActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, ImageSelectionAdapter.ImageSelectionListener {

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    RecyclerView recyclerview;

    AlertDialog alertDialog;
    int selectedIndex;
    ImageSelectionAdapter customAdapter;

    List<String> itemNameList = new ArrayList<>();

    Toolbar mToolbar;

    RejectListBean.RejectDatum rejectDatum;

    TextView saveBtn;
    boolean isUpdate = false;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejinstreport_image);

        Init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheakPermissions();
    }

    private void CheakPermissions() {
        if (!checkPermission()) {
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
                ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ReadMediaImages =
                ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES);
        int ReadAudioImages =
                ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_AUDIO);
        int writeExternalStorage =
                ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ReadExternalStorage =
                ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

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
                            Toast.makeText(RejectedSiteAuditImgActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean writeExternalStorage =
                                grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadExternalStorage =
                                grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (!ACCESSCAMERA && !writeExternalStorage && !ReadExternalStorage) {
                            Toast.makeText(RejectedSiteAuditImgActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }

                    }
                }

                break;
        }
    }


    private void Init() {

        databaseHelper = new DatabaseHelper(getApplicationContext());
        recyclerview = findViewById(R.id.recyclerview);
        saveBtn = findViewById(R.id.saveBtn);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.rejectedImg));
        retrieveValue();
        SetAdapter();
        listner();

    }

    private void retrieveValue() {
        rejectDatum = (RejectListBean.RejectDatum) getIntent().getSerializableExtra(Constant.RejectSite);

    }


    private void listner() {
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validate = true;
                if (imageArrayList.size() > 0) {
                    for (int i = 0; i < imageArrayList.size(); i++) {
                        validate = !imageArrayList.get(i).isImageSelected();
                    }
                    if (validate) {
                        CustomUtility.showToast(RejectedSiteAuditImgActivity.this, getResources().getString(R.string.selectAllImages));
                    } else {
                        new RejectedSiteAuditImageData().execute();
                    }
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();


    }

   private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        if (rejectDatum.getFoundation() != null && !rejectDatum.getFoundation().isEmpty()&& rejectDatum.getFoundation().equals(getResources().getString(R.string.notok))) {
            itemNameList.add(getResources().getString(R.string.foundation));
        }
       if (rejectDatum.getStruAssem() != null && !rejectDatum.getStruAssem().isEmpty()&& rejectDatum.getStruAssem().equals(getResources().getString(R.string.notok))) {
           itemNameList.add(getResources().getString(R.string.structure_assembly));
        }
       if (rejectDatum.getDrvMount() != null && !rejectDatum.getDrvMount().isEmpty()&& rejectDatum.getDrvMount().equals(getResources().getString(R.string.notok))) {
           itemNameList.add(getResources().getString(R.string.drive_mounting_and_cable_dressing));
        }
       if (rejectDatum.getLaEarth() != null && !rejectDatum.getLaEarth().isEmpty()&& rejectDatum.getLaEarth().equals(getResources().getString(R.string.notok))) {
           itemNameList.add(getResources().getString(R.string.la_and_earthing));
        }
       if (rejectDatum.getWrkmnQlty() != null && !rejectDatum.getWrkmnQlty().isEmpty()&& rejectDatum.getWrkmnQlty().equals(getResources().getString(R.string.notok))) {
           itemNameList.add(getResources().getString(R.string.workmanship_and_work_quality));
        }

        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setPoistion(i);
            imageModel.setLatitude("");
            imageModel.setLongitude("");
            imageArrayList.add(imageModel);
        }

       DatabaseHelper db = new DatabaseHelper(this);

       imageList = db.getRejectedSiteAuditImages();

        if (imageArrayList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int j = 0; j < imageList.size(); j++) {
                if (imageList.get(j).isImageSelected()) {
                    ImageModel imageModel = new ImageModel();
                    imageModel.setName(imageList.get(j).getName());
                    imageModel.setImagePath(imageList.get(j).getImagePath());
                    imageModel.setImageSelected(true);
                    imageModel.setPoistion(imageList.get(j).getPoistion());
                    imageModel.setLatitude(imageList.get(j).getLatitude());
                    imageModel.setLongitude(imageList.get(j).getLongitude());
                    imageArrayList.set(j, imageModel);
                }
            }
        }
        customAdapter = new ImageSelectionAdapter(RejectedSiteAuditImgActivity.this, imageArrayList);
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
        LayoutInflater inflater = (LayoutInflater) RejectedSiteAuditImgActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(RejectedSiteAuditImgActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

        TextView title = layout.findViewById(R.id.titleTxt);
        TextView gallery = layout.findViewById(R.id.gallery);
        TextView gamera = layout.findViewById(R.id.camera);
        TextView cancel = layout.findViewById(R.id.cancel);

        if (value.equals("0")) {
            title.setText(getResources().getString(R.string.select_image));
            gallery.setText(getResources().getString(R.string.gallery));
            gamera.setText(getResources().getString(R.string.camera));
        } else {
            title.setText(getResources().getString(R.string.want_to_perform));
            gallery.setText(getResources().getString(R.string.display));
            gamera.setText(getResources().getString(R.string.change));
        }

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (value.equals("0")) {
                    galleryIntent();
                } else {
                    Intent i_display_image = new Intent(RejectedSiteAuditImgActivity.this, PhotoViewerActivity.class);
                    i_display_image.putExtra("image_path", imageArrayList.get(selectedIndex).getImagePath());
                    startActivity(i_display_image);
                }
            }
        });

        gamera.setOnClickListener(new View.OnClickListener() {
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

        camraLauncher.launch(new Intent(RejectedSiteAuditImgActivity.this, CameraActivity2.class)
                .putExtra("cust_name", rejectDatum.getCustomerName()));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();
                            UpdateArrayList(bundle.get("data").toString(),  bundle.get("latitude").toString(), bundle.get("longitude").toString());

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
                    String path = getPath(RejectedSiteAuditImgActivity.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(RejectedSiteAuditImgActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                    } else {
                        UpdateArrayList(path, "", "");

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
        imageModel.setBillNo(imageArrayList.get(selectedIndex).getBillNo());
        imageModel.setLatitude(latitude);
        imageModel.setLongitude(longitude);
        imageModel.setPoistion(imageArrayList.get(selectedIndex).getPoistion());
        imageArrayList.set(selectedIndex, imageModel);
        addupdateDatabase(path,latitude,longitude);

        customAdapter.notifyDataSetChanged();
    }
    private void addupdateDatabase(String path, String latitude, String longitude) {



        if (isUpdate) {
            databaseHelper.updateRejectedSiteAuditImage(imageArrayList.get(selectedIndex).getName(), path,
                    true, rejectDatum.getVbeln() , latitude, longitude);
        } else {
            databaseHelper.insertRejectedSiteAuditImage(imageArrayList.get(selectedIndex).getName(), path,
                    true, rejectDatum.getVbeln() , latitude, longitude);
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    private class RejectedSiteAuditImageData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(RejectedSiteAuditImgActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog = ProgressDialog.show(RejectedSiteAuditImgActivity.this, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;

            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                jsonObj.put("userid", CustomUtility.getSharedPreferences(RejectedSiteAuditImgActivity.this, "userid"));
                jsonObj.put("vbeln", rejectDatum.getVbeln());
                jsonObj.put("beneficiary", rejectDatum.getBeneficiary());
                jsonObj.put("regisno", rejectDatum.getRegisno());
                jsonObj.put("project_no", CustomUtility.getSharedPreferences(RejectedSiteAuditImgActivity.this, "projectid"));
                jsonObj.put("customer_name", rejectDatum.getCustomerName());


                    if(imageArrayList.size()>0){
                        for (int i=0; i<imageArrayList.size(); i++){

                            if(imageArrayList.get(i).isImageSelected()){
                                jsonObj.put("photo"+(i+1), CustomUtility.getBase64FromBitmap(RejectedSiteAuditImgActivity.this, imageArrayList.get(i).getImagePath()));

                            }
                        }
                    }


                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("site_audit_data", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc);

            System.out.println(param1_invc);

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.REJECT_SITE_AUDIT_IMAGES, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

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
                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));
                            databaseHelper.deleteRejectedSiteAuditImages(rejectDatum.getVbeln());
                            progressDialog.dismiss();
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {

                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));

                            progressDialog.dismiss();
                            finish();
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
            onResume();
            progressDialog.dismiss();  // dismiss dialog


        }
    }
    private void showingMessage(String message) {
        runOnUiThread(new Runnable() {
            public void run() {

                CustomUtility.showToast(RejectedSiteAuditImgActivity.this, message);

            }
        });
    }

}

