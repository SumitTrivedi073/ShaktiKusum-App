package activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static debugapp.GlobalValue.Constant.RejectedImage;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import bean.RejectListBean;
import pub.devrel.easypermissions.EasyPermissions;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class RejectInstRepImgActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, ImageSelectionAdapter.ImageSelectionListener {

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    RecyclerView recyclerview;

    AlertDialog alertDialog;
    int selectedIndex;
    ImageSelectionAdapter customAdapter;

    List<String> itemNameList = new ArrayList<>();

    String customerName, enqDocno, benificiaryNo, regNo, projNo, userID;

    Toolbar mToolbar;

    RejectListBean rejectList;

    TextView saveBtn;

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
                            Toast.makeText(RejectInstRepImgActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean writeExternalStorage =
                                grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadExternalStorage =
                                grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (!ACCESSCAMERA && !writeExternalStorage && !ReadExternalStorage) {
                            Toast.makeText(RejectInstRepImgActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }

                    }
                }

                break;
        }
    }


    private void Init() {
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
        rejectList = getIntent().getParcelableExtra("RejectImagesList");
        customerName = rejectList.getCustnm();
        enqDocno = rejectList.getBillno();
        benificiaryNo = rejectList.getBenno();
        regNo = rejectList.getRegno();
        projNo = CustomUtility.getSharedPreferences(RejectInstRepImgActivity.this, "projectid");
        userID = CustomUtility.getSharedPreferences(RejectInstRepImgActivity.this, "userid");
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
                        CustomUtility.showToast(RejectInstRepImgActivity.this, getResources().getString(R.string.selectAllImages));
                    } else {
                        new SyncRejInstallationData().execute();
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
        if (rejectList.getPhoto1() != null && !rejectList.getPhoto1().isEmpty()) {
            itemNameList.add("Panel Module Photo");
        }
        if (rejectList.getPhoto2() != null && !rejectList.getPhoto2().isEmpty()) {
            itemNameList.add("Controller Photo");
        }
        if (rejectList.getPhoto3() != null && !rejectList.getPhoto3().isEmpty()) {
            itemNameList.add("Motor Pump Photo");
        }
        if (rejectList.getPhoto4() != null && !rejectList.getPhoto4().isEmpty()) {
            itemNameList.add("Discharge Photo");
        }
        if (rejectList.getPhoto5() != null && !rejectList.getPhoto5().isEmpty()) {
            itemNameList.add("Document Photo 1");
        }
        if (rejectList.getPhoto6() != null && !rejectList.getPhoto6().isEmpty()) {
            itemNameList.add("Document Photo 2");
        }
        if (rejectList.getPhoto7() != null && !rejectList.getPhoto7().isEmpty()) {
            itemNameList.add("Document Photo 3");
        }
        if (rejectList.getPhoto8() != null && !rejectList.getPhoto8().isEmpty()) {
            itemNameList.add("Document Photo 4");
        }
        if (rejectList.getPhoto9() != null && !rejectList.getPhoto9().isEmpty()) {
            itemNameList.add("Document Photo 5");
        }
        if (rejectList.getPhoto10() != null && !rejectList.getPhoto10().isEmpty()) {
            itemNameList.add("Document Photo 6");
        }
        if (rejectList.getPhoto11() != null && !rejectList.getPhoto11().isEmpty()) {
            itemNameList.add("Document Photo 7");
        }
        if (rejectList.getPhoto12() != null && !rejectList.getPhoto12().isEmpty()) {
            itemNameList.add("Document Photo 8");
        }


        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageArrayList.add(imageModel);
        }

        imageList = new ArrayList<>();
        String json = CustomUtility.getSharedPreferences(RejectInstRepImgActivity.this, RejectedImage);
        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<ImageModel>>() {
        }.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        imageList = new Gson().fromJson(json, type);

        if (imageArrayList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int j = 0; j < imageList.size(); j++) {
                if (imageList.get(j).isImageSelected()) {
                    ImageModel imageModel = new ImageModel();
                    imageModel.setName(imageList.get(j).getName());
                    imageModel.setImagePath(imageList.get(j).getImagePath());
                    imageModel.setImageSelected(true);
                    imageArrayList.set(j, imageModel);
                }
            }
        }
        customAdapter = new ImageSelectionAdapter(RejectInstRepImgActivity.this, imageArrayList);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);

    }

    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;

        if (imageModelList.isImageSelected()) {
            selectImage("1");
        } else {
            selectImage("0");
        }
    }

    private void selectImage(String value) {
        LayoutInflater inflater = (LayoutInflater) RejectInstRepImgActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(RejectInstRepImgActivity.this, R.style.MyDialogTheme);

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
                    Intent i_display_image = new Intent(RejectInstRepImgActivity.this, PhotoViewerActivity.class);
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

        camraLauncher.launch(new Intent(RejectInstRepImgActivity.this, CameraActivity2.class)
                .putExtra("cust_name", customerName));

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
                    String path = getPath(RejectInstRepImgActivity.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(RejectInstRepImgActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
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
        imageArrayList.set(selectedIndex, imageModel);
        CustomUtility.saveArrayList(RejectInstRepImgActivity.this, imageArrayList, RejectedImage);
        customAdapter.notifyDataSetChanged();


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


    private class SyncRejInstallationData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(RejectInstRepImgActivity.this);
            progressDialog = ProgressDialog.show(RejectInstRepImgActivity.this, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;

            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                jsonObj.put("userid", userID);
                jsonObj.put("vbeln", enqDocno);
                jsonObj.put("beneficiary", benificiaryNo);
                jsonObj.put("regisno", regNo);
                jsonObj.put("project_no", projNo);
                jsonObj.put("customer_name", customerName);


                if (0<imageArrayList.size() && imageArrayList.get(0).isImageSelected()) {
                    jsonObj.put("PHOTO1", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(0).getImagePath()));
                }
                if (1<imageArrayList.size() && imageArrayList.get(1).isImageSelected()) {
                    jsonObj.put("PHOTO2", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(1).getImagePath()));
                }
                if (2<imageArrayList.size() && imageArrayList.get(2).isImageSelected()) {
                    jsonObj.put("PHOTO3", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(2).getImagePath()));
                }
                if (3<imageArrayList.size() &&imageArrayList.get(3).isImageSelected()) {
                    jsonObj.put("PHOTO4", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(3).getImagePath()));
                }
                if (4<imageArrayList.size() && imageArrayList.get(4).isImageSelected()) {
                    jsonObj.put("PHOTO5", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(4).getImagePath()));
                }
                if (5<imageArrayList.size() && imageArrayList.get(5).isImageSelected()) {
                    jsonObj.put("PHOTO6", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(5).getImagePath()));
                }
                if (6<imageArrayList.size() && imageArrayList.get(6).isImageSelected()) {
                    jsonObj.put("PHOTO7", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(6).getImagePath()));
                }
                if (7<imageArrayList.size() && imageArrayList.get(7).isImageSelected()) {
                    jsonObj.put("PHOTO8", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(7).getImagePath()));
                }
                if (8<imageArrayList.size() && imageArrayList.get(8).isImageSelected()) {
                    jsonObj.put("PHOTO9", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(8).getImagePath()));
                }
                if (9<imageArrayList.size() && imageArrayList.get(9).isImageSelected()) {
                    jsonObj.put("PHOTO10", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(9).getImagePath()));
                }
                if (10<imageArrayList.size() && imageArrayList.get(10).isImageSelected()) {
                    jsonObj.put("PHOTO11", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(10).getImagePath()));
                }
                if (11<imageArrayList.size() && imageArrayList.get(11).isImageSelected()) {
                    jsonObj.put("PHOTO12", CustomUtility.getBase64FromBitmap(RejectInstRepImgActivity.this, imageArrayList.get(11).getImagePath()));
                }

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("reject_installation", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc);

            System.out.println(param1_invc);

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.REJECT_INSTALLATION, param1_invc);

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
                              CustomUtility.deleteArrayList(RejectInstRepImgActivity.this,RejectedImage);
                            progressDialog.dismiss();
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {

                            /*Message msg = new Message();
                            msg.obj = "Data Not Submitted, Please try After Sometime.";*/


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
}

