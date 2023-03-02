package com.shaktipumplimited.shaktikusum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bean.SurveyBean;
import database.DatabaseHelper;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import utility.CameraUtils;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class SurveyActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final int RC_FILE_PICKER_PERM = 321;
    public static final int BITMAP_SAMPLE_SIZE = 6;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    Context mContext;
    DatabaseHelper dataHelper;
    String sync_data = "0", current_date = "",type= "SURV/";
    double inst_latitude_double,
            inst_longitude_double;
    int PERMISSION_ALL = 1;
    Uri fileUri;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    String imageStoragePath, photo1_text,photo2_text,photo3_text;
    String project_no = "";
    String login_no = "";
    String inst_latitude = "";
    String inst_longitude = "";
    String survy_bill_no = "";
    String pernr = "",spinner_water_resource_text = "", customer_name = "", district_txt = "", address_txt = "", state_txt = "", finaldate = "", contact_no = "",borewell_size_txt = "",borwell_depth_txt = "",cbl_length_txt = "",surf_head_txt = "",len_dia_dis_pip_txt = "";
    TextView photo1,photo2,photo3, submit;
    SimpleDateFormat simpleDateFormat;
    TextView reg_no, state, cust_nm, distrct, contact, address1;
    Spinner spinner_wtr_reso;
    EditText borewell_size,borwell_depth,cbl_length,surf_head,len_dia_dis_pip;
    boolean photo1_flag = false;
    boolean photo2_flag = false;
    boolean photo3_flag = false;
    String ben_id = "";
    String cust_nam = "";
    String cont_no = "";
    String state1 = "";
    String district = "";
    String address = "";
    String regino = "";
    SurveyBean surveyBean;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";

    List<String> list = null;
    int index;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(SurveyActivity.this, mString, Toast.LENGTH_LONG).show();

        }
    };
    private ProgressDialog progressDialog;

    private String PathHolder, Filename;

    /*public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }
    }*/

    private boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return false;
            }
            for(File file : files) {
                if(file.isDirectory()) {
                    deleteDirectory(file);
                }
                else {
                    file.delete();
                }
            }
        }
        return path.exists() && path.delete();
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_punjsurvey_img);
        mContext = this;

        getGpsLocation();
        dataHelper = new DatabaseHelper(mContext);

        progressDialog = new ProgressDialog(mContext);



        list = new ArrayList<String>();
        Bundle bundle = getIntent().getExtras();

        ben_id = bundle.getString("ben_id");
        cust_nam = bundle.getString("cust_nm");
        cont_no = bundle.getString("cont_no");
        state1 = bundle.getString("state");
        district = bundle.getString("district");
        address = bundle.getString("address");
        regino = bundle.getString("regino");

        surveyBean = new SurveyBean();
        surveyBean = dataHelper.getSurveyData(CustomUtility.getSharedPreferences(mContext, "userid"), ben_id);


        getUserTypeValue();

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + "/SKAPP/SURV/"); //it is my root directory

        File billno = new File(root.getAbsolutePath() + "/SKAPP/SURV/" + ben_id); // it is my sub folder directory .. it can vary..

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!billno.exists()) {
                billno.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date = simpleDateFormat.format(new Date());


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        photo1 = (TextView) findViewById(R.id.photo1);
        photo2 = (TextView) findViewById(R.id.photo2);
        photo3 = (TextView) findViewById(R.id.photo3);


        submit = (TextView) findViewById(R.id.btn_submit);
        reg_no = (TextView) findViewById(R.id.reg_no);

        cust_nm = (TextView) findViewById(R.id.cust_nm);

        contact = (TextView) findViewById(R.id.con_no);
        state = (TextView) findViewById(R.id.state);
        distrct = (TextView) findViewById(R.id.distrct);
        address1 = (TextView) findViewById(R.id.addrs);


        spinner_wtr_reso = (Spinner) findViewById(R.id.spinner_wtr_reso);

        borewell_size = (EditText) findViewById(R.id.borewell_size);
        borwell_depth = (EditText) findViewById(R.id.borwell_depth);
        cbl_length = (EditText) findViewById(R.id.cbl_length);
        surf_head = (EditText) findViewById(R.id.surf_head);
        len_dia_dis_pip = (EditText) findViewById(R.id.len_dia_dis_pip);

        setData();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(mContext, R.layout.spinner_item_center, list);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(R.layout.spinner_item_center);

        // attaching data adapter to spinner
        spinner_wtr_reso.setAdapter(dataAdapter1);


        spinner_wtr_reso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                spinner_water_resource_text = spinner_wtr_reso.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Save();

            }
        });





        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("photo1","&&&"+photo1_text);
                if (photo1_text == null || photo1_text.isEmpty()) {

                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO_1");
                } else {


                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO1, photo1_text, "PHOTO_1");

                }
            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("photo2","&&&"+photo2_text);
                if (photo2_text == null || photo2_text.isEmpty()) {

                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO_2");
                } else {


                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO2, photo2_text, "PHOTO_2");

                }
            }
        });

        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("photo3","&&&"+photo3_text);
                if (photo3_text == null || photo3_text.isEmpty()) {

                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO3, "PHOTO_3");
                } else {


                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO3, photo3_text, "PHOTO_3");

                }
            }
        });


    }

    public void getData() {

        pernr = CustomUtility.getSharedPreferences(mContext, "userid");

        project_no = CustomUtility.getSharedPreferences(mContext, "projectid");
        login_no = CustomUtility.getSharedPreferences(mContext, "loginid");

        inst_latitude = String.valueOf(inst_latitude_double);
        inst_longitude = String.valueOf(inst_longitude_double);
        survy_bill_no = ben_id;
        borewell_size_txt = borewell_size.getText().toString();
        borwell_depth_txt = borwell_depth.getText().toString();
        cbl_length_txt = cbl_length.getText().toString();
        surf_head_txt = surf_head.getText().toString();
        len_dia_dis_pip_txt = len_dia_dis_pip.getText().toString();

    }

    private boolean validateType() {

        boolean value;
        if (index == 0) {
            Toast.makeText(this, "Please Select Water Resource", Toast.LENGTH_LONG).show();
            value = false;
        } else {
            value = true;
        }

        return value;
    }

    public void getUserTypeValue() {
        list.add("SELECT WATER RESOURCES");
        list.add("BORWELL");
        list.add("LAKE");
        list.add("WELL");
        list.add("RIVER");
        list.add("OTHER");
    }

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {
            inst_latitude_double = gps.getLatitude();
            inst_longitude_double = gps.getLongitude();
            if (inst_latitude_double == 0.0) {
                CustomUtility.ShowToast("Lat Long not captured, Please try again.", mContext);
            } else {

                CustomUtility.ShowToast("Latitude:-" + inst_latitude_double + "     " + "Longitude:-" + inst_longitude_double, mContext);

            }
        } else {
            gps.showSettingsAlert();
        }
    }

    public void openCamera(String name) {

        if (CameraUtils.checkPermissions(mContext)) {

         /*   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            String from = "SURV/";

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE,ben_id, name, from);

            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
                Log.e("PATH", "&&&" + imageStoragePath);
            }

            Uri fileUri1 = CameraUtils.getOutputMediaFileUri(mContext, file);

            Log.e("fileUri", "&&&" + fileUri1);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/


            File file = new File(ImageManager.getMediaFilePath(type,name, ben_id));

            imageStoragePath = file.getAbsolutePath();
            Log.e("PATH", "&&&" + imageStoragePath);

            Intent i = new Intent(mContext, CameraActivity.class);
            i.putExtra("lat", String.valueOf(inst_latitude_double));
            i.putExtra("lng", String.valueOf(inst_longitude_double));
            i.putExtra("cust_name", cust_nm.getText().toString());
            i.putExtra("inst_id", ben_id);
            i.putExtra("type", "SURV/");
            i.putExtra("name", name);

            startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }


    }

    public void openGallery(String name) {

        if (ActivityCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT <= 19) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);

                ((Activity) mContext).startActivityForResult(Intent.createChooser(i, "Select Photo"), GALLERY_IMAGE_REQUEST_CODE);


            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                ((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "Select Photo"), GALLERY_IMAGE_REQUEST_CODE);


            }

        } else {
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, PERMISSION_ALL);
            }
        }

    }

    private void setData() {

        surveyBean = new SurveyBean();
        surveyBean = dataHelper.getSurveyData(CustomUtility.getSharedPreferences(mContext, "userid"), ben_id);

        reg_no.setText(ben_id);

        cust_nm.setText(cust_nam);
        contact.setText(cont_no);
        state.setText(state1);
        distrct.setText(district);
        address1.setText(address);

        borewell_size.setText(surveyBean.getBorewell_size());
        borwell_depth.setText(surveyBean.getBorwell_depth());
        cbl_length.setText(surveyBean.getCbl_length());
        surf_head.setText(surveyBean.getSurf_head());
        len_dia_dis_pip.setText(surveyBean.getLen_dia_dis_pip());

//        if (!TextUtils.isEmpty(surveyBean.getSpinner_water_resource())) {
//            spinner_wtr_reso.setSelection(dataHelper.getPosition(spinner_wtr_reso, surveyBean.getSpinner_water_resource()));
//        }

        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id, "/IMG_PHOTO_1.jpg");
        if (file.exists()) {
            photo1_text = file.getAbsolutePath();
        }

        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id, "/IMG_PHOTO_2.jpg");
        if (file1.exists()) {
            photo2_text = file1.getAbsolutePath();
        }

        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id, "/IMG_PHOTO_3.jpg");
        if (file2.exists()) {
            photo3_text = file2.getAbsolutePath();
        }


        setIcon(DatabaseHelper.KEY_PHOTO1);
        setIcon(DatabaseHelper.KEY_PHOTO2);
        setIcon(DatabaseHelper.KEY_PHOTO3);


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

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

                try {

                    Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                    int count = bitmap.getByteCount();

                    Log.e("Count", "&&&&&" + count);

                    Log.e("IMAGEURI", "&&&&" + imageStoragePath);

                    ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);

                    byte[] byteArray = byteArrayBitmapStream.toByteArray();

                    long size = byteArray.length;

                    Log.e("SIZE1234", "&&&&" + size);

                    Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));


                    if (photo1_flag == true) {
                        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id, "/IMG_PHOTO_1.jpg");
                        if (file.exists()) {
                            photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, ben_id +"PHOTO_11", photo1_text);
                            Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(mContext, ben_id +"PHOTO_11"));
                            setIcon(DatabaseHelper.KEY_PHOTO1);
                        }

                    }

                    if (photo2_flag == true) {
                        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id, "/IMG_PHOTO_2.jpg");
                        if (file1.exists()) {
                            photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, ben_id +"PHOTO_22", photo2_text);
                            Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(mContext, ben_id +"PHOTO_22"));
                            setIcon(DatabaseHelper.KEY_PHOTO2);
                        }

                    }

                    if (photo3_flag == true) {
                        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id, "/IMG_PHOTO_3.jpg");
                        if (file2.exists()) {
                            photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, ben_id +"PHOTO_33", photo3_text);
                            Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(mContext, ben_id +"PHOTO_33"));
                            setIcon(DatabaseHelper.KEY_PHOTO3);
                        }

                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

             /*   File file = newworkorder File(imageStoragePath);
                if (file.exists()) {
                    file.delete();
                }*/


        } else {
            if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {

                if (resultCode == RESULT_OK) {

                    if (data != null) {


                        Uri selectedImageUri = data.getData();
                        String selectedImagePath = getImagePath(selectedImageUri);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        options.inSampleSize = 6;
                        options.inJustDecodeBounds = false;



                        try {
                            Log.e("IMAGEURI", "&&&&" + selectedImageUri);
                            if (selectedImageUri != null) {

                                Bitmap bitmap  = BitmapFactory.decodeFile(selectedImagePath, options);

                                //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));


                                int count = bitmap.getByteCount();

                                Log.e("Count", "&&&&&" + count);
                                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);

                                byte[] byteArray = byteArrayBitmapStream.toByteArray();

                                long size = byteArray.length;


                                Log.e("SIZE1234", "&&&&" + size);

                                Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));

                                if (photo1_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id + "/IMG_PHOTO_1.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id, "/IMG_PHOTO_1.jpg");
                                    if (file.exists()) {
                                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, ben_id +"PHOTO_11", photo1_text);
                                        Log.e("SIZE1", "&&&&" + photo1_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO1);
                                    }

                                }

                                if (photo2_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id + "/IMG_PHOTO_2.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id, "/IMG_PHOTO_2.jpg");
                                    if (file1.exists()) {
                                        photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, ben_id +"PHOTO_22", photo2_text);
                                        Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(mContext, ben_id +"PHOTO_22"));
                                        setIcon(DatabaseHelper.KEY_PHOTO2);
                                    }

                                }

                                if (photo3_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id + "/IMG_PHOTO_3.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/" + ben_id, "/IMG_PHOTO_3.jpg");
                                    if (file2.exists()) {
                                        photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, ben_id +"PHOTO_33", photo3_text);
                                        Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(mContext, ben_id +"PHOTO_33"));
                                        setIcon(DatabaseHelper.KEY_PHOTO3);
                                    }

                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }

        if (requestCode == 1) {
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
        }

        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == 301) {

                String year = data.getStringExtra("year");
                String month = data.getStringExtra("month");
                String date = data.getStringExtra("date");
                finaldate = year + "-" + month + "-" + date;
                finaldate = CustomUtility.formateDate1(finaldate);
            }
        }
    }


    private void copyFile(String sourceFilePath, String destinationFilePath) {

        Log.e("Source", "&&&&" + sourceFilePath);
        Log.e("Destination", "&&&&" + destinationFilePath);

        File sourceLocation = new File(sourceFilePath);
        File targetLocation = new File(destinationFilePath);
        Log.e("&&&&&", "sourceLocation: " + sourceLocation);
        Log.e("&&&&&", "targetLocation: " + targetLocation);
        try {
            int actionChoice = 2;
            if (actionChoice == 1) {
                if (sourceLocation.renameTo(targetLocation)) {
                    Log.e("&&&&&", "Move file successful.");
                } else {
                    Log.e("&&&&&", "Move file failed.");
                }
            } else {
                if (sourceLocation.exists()) {
                    InputStream in = new FileInputStream(sourceLocation);
                    OutputStream out = new FileOutputStream(targetLocation);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    Log.e("&&&&&", "Copy file successful.");
                } else {
                    Log.e("&&&&&", "Copy file failed. Source file missing.");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showConfirmationAlert(final String keyimage, final String data, final String name) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
        // Setting Dialog Title
        alertDialog.setTitle("Confirmation");
        // Setting Dialog Message
        alertDialog.setMessage("Image already saved, Do you want to change it or display?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Display", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Log.e("KEY", "&&&&" + keyimage);
                Log.e("DATA", "&&&&" + data);

                displayImage(keyimage, data);


            }
        });

        alertDialog.setNegativeButton("Change", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                showConfirmationGallery(keyimage, name);


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public String getImagePath(Uri uri) {

        String s = null;

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        } else {
            String[] projection = {String.valueOf(MediaStore.Images.Media.DATA)};

            Cursor cursor1 = ((Activity) mContext).getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
            Cursor cursor2 = ((Activity) mContext).getContentResolver().query(uri, projection, null, null, null);

            Log.e("CUR1", "&&&&" + cursor1);
            Log.e("CUR2", "&&&&" + cursor2);

            if (cursor1 == null && cursor2 == null) {
                return null;
            } else {

                int column_index = 0;
                if (cursor1 != null) {
                    column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor1.moveToFirst();

                    if (cursor1.moveToFirst()) {
                        s = cursor1.getString(column_index);
                    }
                    cursor1.close();
                }
                int column_index1 = 0;
                if (cursor2 != null) {
                    column_index1 = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor2.moveToFirst();

                    if (cursor2.moveToFirst()) {
                        s = cursor2.getString(column_index1);
                    }
                    cursor2.close();
                }

                return s;
            }
        }
    }

    private void displayImage(String key, String data) {
        Intent i_display_image = new Intent(mContext, ShowDocument2.class);
        Bundle extras = new Bundle();
        //saveData();
        extras.putString("docno", ben_id);
        extras.putString("key", key);
        extras.putString("data", "SURV");

        CustomUtility.setSharedPreference(mContext, "data", data);

        i_display_image.putExtras(extras);
        startActivity(i_display_image);
    }

    public void showConfirmationGallery(final String keyimage, final String name) {

        final CustomUtility customUtility = new CustomUtility();

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = customUtility.checkPermission(mContext);
                if (items[item].equals("Take Photo")) {

                    if (result) {
                        openCamera(name);
                        setFlag(keyimage);
                    }

                } else if (items[item].equals("Choose from Gallery")) {
                    if (result) {
                        openGallery(name);
                        setFlag(keyimage);


                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void Save() {

        if (CustomUtility.isInternetOn()) {

            //GET DATA
            getData();

            SurveyBean surveyBean = new SurveyBean(pernr,
                    project_no,
                    login_no,
                    inst_latitude,
                    inst_longitude,
                    survy_bill_no,
                    spinner_water_resource_text,
                    borewell_size_txt,
                    borwell_depth_txt,
                    cbl_length_txt,
                    surf_head_txt,
                    len_dia_dis_pip_txt
            );

            if (dataHelper.isRecordExist(dataHelper.TABLE_SURVEY_PUMP_DATA, dataHelper.KEY_BILL_NO, survy_bill_no)) {
                dataHelper.updateSurveyData(survy_bill_no, surveyBean);
            } else {
                dataHelper.insertSurveyData(survy_bill_no, surveyBean);
            }

            if (validateType()) {
                if (!TextUtils.isEmpty(borewell_size_txt)) {
                    if (!TextUtils.isEmpty(borwell_depth_txt)) {
                        if (!TextUtils.isEmpty(cbl_length_txt)) {
                            if (!TextUtils.isEmpty(surf_head_txt)) {
                                if (!TextUtils.isEmpty(len_dia_dis_pip_txt)) {
                                    if (!TextUtils.isEmpty(photo1_text)) {
                                        new SurveySubData().execute(borewell_size_txt, borwell_depth_txt, cbl_length_txt, surf_head_txt, len_dia_dis_pip_txt,spinner_water_resource_text);
                                    } else {
                                        Toast.makeText(mContext, "Please Select Water Resource Photos", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, "Please Enter Length and Diameter of Discharge Pipe", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Please Enter Surface Static Head", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "Please Enter Cabel Length", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(mContext, "Please Enter Borwell Depth", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Please Enter Borwell Size", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            Toast.makeText(mContext, "Please Connect to Internet, Your Data is Save to Offline Mode...", Toast.LENGTH_SHORT).show();
            //GET DATA
            getData();

            SurveyBean surveyBean = new SurveyBean(pernr,
                    project_no,
                    login_no,
                    inst_latitude,
                    inst_longitude,
                    survy_bill_no,
                    spinner_water_resource_text,
                    borewell_size_txt,
                    borwell_depth_txt,
                    cbl_length_txt,
                    surf_head_txt,
                    len_dia_dis_pip_txt
            );

            if (dataHelper.isRecordExist(dataHelper.TABLE_SURVEY_PUMP_DATA, dataHelper.KEY_BILL_NO, survy_bill_no)) {
                dataHelper.updateSurveyData(survy_bill_no, surveyBean);
            } else {
                dataHelper.insertSurveyData(survy_bill_no, surveyBean);
            }

            onBackPressed();
        }


//        String pht1 = null;


//        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SURV/" + ben_id, "/IMG_PHOTO_1.jpg");
//        if (file.exists()) {
//            pht1 = file.getAbsolutePath();
//        }

//        if (!TextUtils.isEmpty(pht1)) {
//
//            CustomUtility.setSharedPreference(mContext, "SYNC" + ben_id, "1");
//
//        } else {
//
//            Toast.makeText(this, "Please Select Photo of Water Source.", Toast.LENGTH_SHORT).show();
//        }


    }


    public void setFlag(String key) {

        Log.e("FLAG", "&&&" + key);
        photo1_flag = false;
        photo2_flag = false;
        photo3_flag = false;

        if (DatabaseHelper.KEY_PHOTO1.equals(key)) {
            photo1_flag = true;
        }

        if (DatabaseHelper.KEY_PHOTO2.equals(key)) {
            photo2_flag = true;
        }

        if (DatabaseHelper.KEY_PHOTO3.equals(key)) {
            photo3_flag = true;
        }

    }

    public void setIcon(String key) {


        if (DatabaseHelper.KEY_PHOTO1.equals(key)) {
            if (photo1_text == null || photo1_text.isEmpty()) {
                photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
            } else {
                photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
            }
        }

        if (DatabaseHelper.KEY_PHOTO2.equals(key)) {
            if (photo2_text == null || photo2_text.isEmpty()) {
                photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
            } else {
                photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
            }
        }
        if (DatabaseHelper.KEY_PHOTO3.equals(key)) {
            if (photo3_text == null || photo3_text.isEmpty()) {
                photo3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
            } else {
                photo3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Save();
        super.onBackPressed();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    private class SurveySubData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {

            String invc_done = null;
            String obj2 = null;

            String BOREWELL_SIZE = params[0];
            String BOREWELL_DEPTH = params[1];
            String LENGTH_POWER_CABLE = params[2];
            String SURFACE_HEAD = params[3];
            String LENGTH_DISCHARGE_PIPE = params[4];
            String WATER_SOURCE = params[5];


            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                String date_s = current_date;


                SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");

                Date date = dt.parse(date_s);
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");

                jsonObj.put("PROCESS_NO",  CustomUtility.getSharedPreferences(mContext, "process_no"));
                jsonObj.put("REGISNO", regino);
                jsonObj.put("PROJECT_NO", CustomUtility.getSharedPreferences(mContext, "projectid"));
                jsonObj.put("project_login_no", CustomUtility.getSharedPreferences(mContext, "loginid"));
                jsonObj.put("BENEFICIARY", ben_id);
                jsonObj.put("userid", CustomUtility.getSharedPreferences(mContext, "userid"));
                jsonObj.put("bldat", dt1.format(date));
                jsonObj.put("lat", inst_latitude_double);
                jsonObj.put("lng", inst_longitude_double);
                jsonObj.put("BOREWELL_SIZE", BOREWELL_SIZE);
                jsonObj.put("BOREWELL_DEPTH", BOREWELL_DEPTH);
                jsonObj.put("LENGTH_POWER_CABLE", LENGTH_POWER_CABLE);
                jsonObj.put("SURFACE_HEAD", SURFACE_HEAD);
                jsonObj.put("LENGTH_DISCHARGE_PIPE", LENGTH_DISCHARGE_PIPE);
                jsonObj.put("WATER_SOURCE", WATER_SOURCE);

                jsonObj.put("PHOTO1", CustomUtility.getSharedPreferences(mContext, ben_id+"PHOTO_11"));
                jsonObj.put("PHOTO2", CustomUtility.getSharedPreferences(mContext, ben_id+"PHOTO_22"));
                jsonObj.put("PHOTO3", CustomUtility.getSharedPreferences(mContext, ben_id+"PHOTO_33"));

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("survey", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.SURVEY_DATA, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {

                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_return");


                    JSONArray ja = new JSONArray(obj1);


                    Log.e("OUTPUT2", "&&&&" + ja.toString());

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);

                        invc_done = jo.getString("return");


                        if (invc_done.equalsIgnoreCase("Y")) {

                            Message msg = new Message();
                            msg.obj = "Data Submitted Successfully...";
                            mHandler.sendMessage(msg);

                            dataHelper.deleteSurveyData(ben_id);
                            deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SKAPP/SURV/"));

                            CustomUtility.setSharedPreference(mContext, ben_id+"PHOTO_11", "");
                            CustomUtility.setSharedPreference(mContext, ben_id+"PHOTO_22", "");
                            CustomUtility.setSharedPreference(mContext, ben_id+"PHOTO_33", "");

                            progressDialog.dismiss();
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {

                            Message msg = new Message();
                            msg.obj = "Data Not Submitted, Please try After Sometime.";
                            mHandler.sendMessage(msg);

                            /*dataHelper.deleteSurveyData(ben_id);
                            deleteFiles(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/SURV/");

                            CustomUtility.setSharedPreference(mContext, ben_id+"PHOTO_11", "");
                            CustomUtility.setSharedPreference(mContext, ben_id+"PHOTO_22", "");
                            CustomUtility.setSharedPreference(mContext, ben_id+"PHOTO_33", "");*/

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

