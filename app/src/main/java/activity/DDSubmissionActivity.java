package activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static debugapp.GlobalValue.Constant.DDSubmissionImage;
import static utility.FileUtils.getPath;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bean.ImageModel;
import bean.InstallationBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import utility.dialog;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class DDSubmissionActivity extends AppCompatActivity {
    public static final int RC_FILE_PICKER_PERM = 321;
    public static final int BITMAP_SAMPLE_SIZE = 6;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int REQUEST_CODE_PERMISSION = 100;

    private static final int PICK_FROM_FILE = 101;

    Context mContext;
    DatabaseHelper dataHelper;
    String sync_data = "0", current_date = "";
    double inst_latitude_double,
            inst_longitude_double;
    int PERMISSION_ALL = 1;
    Uri fileUri;
    dialog yourDialog;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    String imageStoragePath, photo1_text, type = "DDSUB/";
    String regisno = "", customer_name = "", beneficiary = "", regio_txt = "", city_txt = "", village = "", contact_no = "", aadhar_no = "", finaldate = "";
    TextView photo1, submit;
    SimpleDateFormat simpleDateFormat;
    TextView reg_no, benef_id, cust_nm, address, contact, aadhar;
    EditText remark;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";
    boolean photo1_flag = false;
    Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(DDSubmissionActivity.this, mString, Toast.LENGTH_LONG).show();

        }
    };
    private ProgressDialog progressDialog;
    private DDSubmissionActivity activity;

    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    AlertDialog alertDialog;

    int selectedIndex = 0;
    ImageView inst_location, geoIndigation;

    InstallationBean installationBean;

    DatabaseHelper db;

    private String mHomePath, PathHolder, Filename;



    private boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return false;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return path.exists() && path.delete();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        setContentView(R.layout.activity_ddsub_img);
        mContext = this;

        dataHelper = new DatabaseHelper(mContext);


        progressDialog = new ProgressDialog(mContext);


        yourDialog = new dialog(activity);
        yourDialog.show();


        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + "/SKAPP/DDSUB/"); //it is my root directory


        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date = simpleDateFormat.format(new Date());
        db = new DatabaseHelper(mContext);
        installationBean = new InstallationBean();
        installationBean = db.getInstallationData(CustomUtility.getSharedPreferences(mContext, "userid"), "");


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        photo1 = (TextView) findViewById(R.id.photo1);
        remark = (EditText) findViewById(R.id.remark);
        submit = (TextView) findViewById(R.id.btn_submit);
        reg_no = (TextView) findViewById(R.id.reg_no);
        benef_id = (TextView) findViewById(R.id.benef_id);
        cust_nm = (TextView) findViewById(R.id.cust_nm);
        address = (TextView) findViewById(R.id.address);
        contact = (TextView) findViewById(R.id.cnt_no);
        aadhar = (TextView) findViewById(R.id.aadhar_no);
        inst_location = (ImageView) findViewById(R.id.loaction);
        geoIndigation = findViewById(R.id.geoIndigation);

        setData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark_txt = remark.getText().toString();
                if (!TextUtils.isEmpty(remark_txt)) {
                    if(imageArrayList.size()>0 && imageArrayList.get(selectedIndex).isImageSelected()) {
                        new DDSubmissionData().execute(remark_txt);
                    }else {
                        CustomUtility.showToast(DDSubmissionActivity.this,getResources().getString(R.string.select_image));
                    }
                } else {
                    Toast.makeText(mContext, "Please Enter Remark", Toast.LENGTH_SHORT).show();
                }
            }
        });

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {
                    if (imageArrayList.get(selectedIndex).isImageSelected()) {
                        selectImage("1");
                    } else {
                        selectImage("0");
                    }
                } else {
                    requestPermission();
                }

            }
        });
        inst_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(installationBean.getLatitude()) || installationBean.getLatitude().equals("0.0") || installationBean.getLatitude().equals("null")) {
                    getGpsLocation();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
                    alertDialog.setTitle("Confirmation");
                    alertDialog.setMessage("Latitude, Longitude already saved, Do you want to change it?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getGpsLocation();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

    }

    private void setData() {
        imageArrayList = new ArrayList<>();

        ImageModel imageModel = new ImageModel();
        imageModel.setName("dd_collection_image");
        imageModel.setImagePath("");
        imageModel.setImageSelected(false);
        imageArrayList.add(imageModel);

        imageList = new ArrayList<>();
        String json = CustomUtility.getSharedPreferences(DDSubmissionActivity.this, DDSubmissionImage);
        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<ImageModel>>() {
        }.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        imageList = new Gson().fromJson(json, type);

        if (imageArrayList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int j = 0; j < imageList.size(); j++) {
                if (imageList.get(j).isImageSelected()) {
                    ImageModel imageModel1 = new ImageModel();
                    imageModel1.setName(imageList.get(j).getName());
                    imageModel1.setImagePath(imageList.get(j).getImagePath());
                    imageModel1.setImageSelected(true);
                    imageArrayList.set(j, imageModel1);
                }
            }
            setIcon();
        }
    }



    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
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

        if (SDK_INT >= Build.VERSION_CODES.R) {
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
                    if (SDK_INT >= Build.VERSION_CODES.R) {
                        boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadMediaImages = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadAudioImages = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (ACCESSCAMERA && ReadMediaImages && ReadAudioImages) {

                            selectImage("0");
                        } else {
                            Toast.makeText(DDSubmissionActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean writeExternalStorage =
                                grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadExternalStorage =
                                grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (ACCESSCAMERA && writeExternalStorage && ReadExternalStorage) {
                            selectImage("0");
                        } else {
                            Toast.makeText(DDSubmissionActivity.this, "Please allow all the permission", Toast.LENGTH_LONG).show();
                        }

                    }
                }

                break;
        }
    }




    private void selectImage(String value) {
        LayoutInflater inflater = (LayoutInflater) DDSubmissionActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final androidx.appcompat.app.AlertDialog.Builder builder =
                new AlertDialog.Builder(DDSubmissionActivity.this, R.style.MyDialogTheme);

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
                    Intent i_display_image = new Intent(DDSubmissionActivity.this, PhotoViewerActivity.class);
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

        camraLauncher.launch(new Intent(DDSubmissionActivity.this, CameraActivity2.class)
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


    @SuppressLint("Range")
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
                    String path = getPath(DDSubmissionActivity.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(DDSubmissionActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                    } else {
                        UpdateArrayList(path);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String uriString = null;
                    if (uri != null) {
                        uriString = uri.toString();
                    }
                    File myFile = new File(uriString);
                    //PathHolder = myFile.getPath();
                    Filename = null;


                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {

                                Filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                PathHolder = getExternalStorageDirectory() + "/Download/" + Filename;
                                Log.e("&&&&", "DDDDD" + Filename);

                                if (PathHolder != null && !PathHolder.equals("")) {

                                } else {

                                }
                            }
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
                        }
                    } else if (uriString.startsWith("file://")) {
                        Filename = myFile.getName();
                        PathHolder = getExternalStorageDirectory() + "/Download/" + Filename;
                        Log.e("&&&&", "DDDDD" + Filename);
                        if (PathHolder != null && !PathHolder.equals("")) {

                        } else {

                        }
                    }
                }
                break;

            case 301:
                String year = data.getStringExtra("year");
                String month = data.getStringExtra("month");
                String date = data.getStringExtra("date");
                finaldate = year + "-" + month + "-" + date;
                finaldate = CustomUtility.formateDate1(finaldate);
                break;
        }
    }

    private void UpdateArrayList(String path) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageArrayList.set(selectedIndex, imageModel);
        CustomUtility.saveArrayList(DDSubmissionActivity.this, imageArrayList, DDSubmissionImage);
        setIcon();

    }


    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {

            String mLAt = "" + gps.getLatitude();
            String mLOng = "" + gps.getLongitude();

            inst_latitude_double = Double.parseDouble(mLAt);
            inst_longitude_double = Double.parseDouble(mLOng);

            if (inst_latitude_double == 0.0) {
                CustomUtility.ShowToast("Lat Long not captured, Please try again.", mContext);
            } else {


                geoIndigation.setImageResource(R.drawable.right_mark_icn_green);
                CustomUtility.ShowToast("Latitude:-" + inst_latitude_double + "     " + "Longitude:-" + inst_longitude_double, mContext);

            }
        } else {
            gps.showSettingsAlert();
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void Save() {



        if (imageArrayList.get(selectedIndex).isImageSelected()) {

            CustomUtility.setSharedPreference(mContext, "SYNC", "1");

        } else {

            Toast.makeText(this, "Please Select DD Photo.", Toast.LENGTH_SHORT).show();
        }


    }


    public void setIcon() {
          if(imageArrayList.size()>0){
        if (imageArrayList.get(selectedIndex).isImageSelected()) {
            photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
        } else {
            photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
        }
          }

    }

    @Override
    public void onBackPressed() {
        Save();
        super.onBackPressed();
    }


    public void searchWord(String textString) {

        if (!textString.equals("")) {
            new GetBeneficaryDeails_Task().execute(textString);
        } else {
            Toast.makeText(mContext, "Please Enter Beneficary Id.", Toast.LENGTH_SHORT).show();
        }

    }

    private class GetBeneficaryDeails_Task extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            //progress = newworkorder ProgressDialog(context);
            progressDialog = ProgressDialog.show(mContext, "", "Please Wait...");

        }

        @Override
        protected String doInBackground(String... params) {

            String beneficiaryId = params[0];
            Log.e("ID", "&&&" + beneficiaryId);
            Log.e("ID", "&&&" + CustomUtility.getSharedPreferences(mContext, "projectid"));
            Log.e("ID", "&&&" + CustomUtility.getSharedPreferences(mContext, "loginid"));
            Log.e("ID", "&&&" + CustomUtility.getSharedPreferences(mContext, "process_no"));

            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            param.clear();
            param.add(new BasicNameValuePair("beneficiary", beneficiaryId));
            param.add(new BasicNameValuePair("PROJECT_NO", CustomUtility.getSharedPreferences(mContext, "projectid")));
            param.add(new BasicNameValuePair("PROCESS_NO", CustomUtility.getSharedPreferences(mContext, "process_no")));

            String login_selec = null;


            try {

                login_selec = CustomHttpClient.executeHttpPost1(WebURL.BENF_DETAILS, param);

                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("beneficiary_detail");

                Log.e("DATA", "&&&&" + obj1);


                JSONArray ja = new JSONArray(obj1);

                CustomUtility.deleteArrayList(DDSubmissionActivity.this,DDSubmissionImage);
                if (ja.length() > 0) {
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jo = ja.getJSONObject(j);

                        if (jo.length() > 0) {

                            regisno = jo.getString("regisno");
                            customer_name = jo.getString("customer_name");
                            beneficiary = jo.getString("beneficiary");
                            regio_txt = jo.getString("regio_txt");

                            city_txt = jo.getString("city_txt");
                            village = jo.getString("village");
                            contact_no = jo.getString("contact_no");
                            aadhar_no = jo.getString("aadhar_no");

                            yourDialog.dismiss();


                        } else {

                            Handler handler = new Handler(mContext.getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(mContext, "Data not Available...,Please Try Again.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    }

                } else {

                    Handler handler = new Handler(mContext.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(mContext, "Data not Available...,Please Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                progressDialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            return login_selec;

        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here

            reg_no.setText(regisno);
            benef_id.setText(beneficiary);
            cust_nm.setText(customer_name);
            address.setText(village + "," + city_txt + "." + regio_txt);
            contact.setText(contact_no);
            aadhar.setText(aadhar_no);

            progressDialog.dismiss();  // dismiss dialog
        }
    }

    private class DDSubmissionData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {

            String invc_done = null;
            String docno_sap = null;
            String obj2 = null;
            String remark_txt = params[0];

            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                String date_s = current_date;


                SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");

                Date date = dt.parse(date_s);
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");

                jsonObj.put("PROCESS_NO", CustomUtility.getSharedPreferences(mContext, "process_no"));
                jsonObj.put("REGISNO", regisno);
                jsonObj.put("PROJECT_NO", CustomUtility.getSharedPreferences(mContext, "projectid"));
                jsonObj.put("project_login_no", CustomUtility.getSharedPreferences(mContext, "loginid"));
                jsonObj.put("beneficiary", beneficiary);
                jsonObj.put("userid", CustomUtility.getSharedPreferences(mContext, "userid"));
                jsonObj.put("date", dt1.format(date));
                jsonObj.put("lat", inst_latitude_double);
                jsonObj.put("lng", inst_longitude_double);
                jsonObj.put("remark", remark_txt);

                jsonObj.put("PHOTO1",CustomUtility.getBase64FromBitmap(DDSubmissionActivity.this,imageArrayList.get(selectedIndex).getImagePath()));

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("dd_collection", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.DD_SUB_DATA, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {

                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_return");


                    JSONArray ja = new JSONArray(obj1);


                    Log.e("OUTPUT2", "&&&&" + ja.toString());

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);


                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");

                        //{"data_return":[{"mdocno":"","return":"Y"}] }


                        if (invc_done.equalsIgnoreCase("Y")) {

                            Message msg = new Message();
                            msg.obj = "Data Submitted Successfully...";
                            mHandler.sendMessage(msg);

                            deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SKAPP/DDSUB/"));

                            CustomUtility.setSharedPreference(mContext, "PHOTO_1", "");

                            progressDialog.dismiss();
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {

                            Message msg = new Message();
                            msg.obj = "Data Not Submitted, Please try After Sometime.";
                            mHandler.sendMessage(msg);

                           /* deleteFiles(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/DDSUB/");

                            CustomUtility.setSharedPreference(mContext, "PHOTO_1", "");*/

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

