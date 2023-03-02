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
import android.widget.EditText;
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

import database.DatabaseHelper;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import utility.CameraUtils;
import utility.CustomUtility;
import utility.dialog3;
import webservice.CustomHttpClient;
import webservice.WebURL;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;


public class GovtOffVisitActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final int RC_FILE_PICKER_PERM = 321;
    public static final int BITMAP_SAMPLE_SIZE = 6;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    Context mContext;
    DatabaseHelper dataHelper;
    String sync_data = "0", current_date = "";
    double inst_latitude_double,
            inst_longitude_double;
    int PERMISSION_ALL = 1;
    Uri fileUri;
    dialog3 yourDialog;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    String imageStoragePath, photo1_text;
    String regisno = "", customer_name = "", beneficiary = "", regio_txt = "", city_txt = "", village = "", contact_no = "", aadhar_no = "", finaldate = "";
    TextView photo1, submit;
    SimpleDateFormat simpleDateFormat;
    TextView reg_no, benef_id, cust_nm, address, contact, aadhar;
    EditText remark;
    boolean photo1_flag = false;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(GovtOffVisitActivity.this, mString, Toast.LENGTH_LONG).show();

        }
    };
    private ProgressDialog progressDialog;
    private GovtOffVisitActivity activity;
    private String mHomePath, PathHolder, Filename;

/*    public static void deleteFiles(String path) {

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
        this.activity = this;
        setContentView(R.layout.activity_ddsub_img);
        mContext = this;


        getGpsLocation();
        dataHelper = new DatabaseHelper(mContext);

        progressDialog = new ProgressDialog(mContext);


        yourDialog = new dialog3(activity);
        yourDialog.show();


       /* File root = getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + "/SKAPP/DDSUB/"); //it is my root directory


        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }*/

        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date = simpleDateFormat.format(new Date());


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

        //setData();
        photo1.setVisibility(View.GONE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark_txt = remark.getText().toString();
                if (!TextUtils.isEmpty(remark_txt)) {
                    new GovtSubData().execute(remark_txt);
                } else {
                    Toast.makeText(mContext, "Please Enter Remark", Toast.LENGTH_SHORT).show();
                }
            }
        });

      /*  photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (photo1_text == null || photo1_text.isEmpty()) {

                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO_1");
                } else {


                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO1, photo1_text, "PHOTO_1");

                }
            }
        });*/


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

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            String from = "DDSUB/";

            File file = CameraUtils.getOutputMediaFile1(MEDIA_TYPE_IMAGE, name, from);

            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
                Log.e("PATH", "&&&" + imageStoragePath);
            }

            Uri fileUri1 = CameraUtils.getOutputMediaFileUri(mContext, file);

            Log.e("fileUri", "&&&" + fileUri1);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

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


        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/DDSUB/", "/IMG_PHOTO_1.jpg");
        if (file.exists()) {
            photo1_text = file.getAbsolutePath();
        }


        setIcon(DatabaseHelper.KEY_PHOTO1);


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
            if (resultCode == RESULT_OK) {

                try {

                    Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                    int count = bitmap.getByteCount();

                    Log.e("Count", "&&&&&" + count);

                    Log.e("IMAGEURI", "&&&&" + imageStoragePath);

                    ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
                   /* if (count <= 100000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                    } else if (count <= 200000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                    } else if (count <= 300000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                    } else if (count <= 400000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                    } else if (count <= 500000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                    } else if (count <= 600000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                    } else if (count <= 700000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                    } else if (count <= 800000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                    } else if (count <= 900000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                    } else if (count <= 1000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                    } else if (count <= 2000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                    } else if (count <= 3000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                    } else if (count <= 4000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayBitmapStream);
                    } else if (count <= 5000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayBitmapStream);
                    } else if (count <= 6000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    } else if (count <= 7000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    } else if (count <= 8000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                    } else if (count <= 9000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                    } else if (count <= 10000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                    } else if (count <= 20000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                    } else if (count <= 30000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                    } else if (count <= 40000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    } else if (count <= 50000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    } else if (count <= 60000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                    } else if (count <= 70000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                    } else if (count <= 80000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                    } else if (count <= 90000000) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                    } else {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 35, byteArrayBitmapStream);
                    }*/
                    byte[] byteArray = byteArrayBitmapStream.toByteArray();

                    long size = byteArray.length;

                    Log.e("SIZE1234", "&&&&" + size);

                    Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));


                    if (photo1_flag == true) {
                        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/DDSUB/", "/IMG_PHOTO_1.jpg");
                        if (file.exists()) {
                            photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, "PHOTO_1", photo1_text);
                            Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(mContext, "PHOTO_1"));
                            setIcon(DatabaseHelper.KEY_PHOTO1);
                        }

                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

             /*   File file = newworkorder File(imageStoragePath);
                if (file.exists()) {
                    file.delete();
                }*/

            }
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
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayBitmapStream);
                               /* if (count <= 100000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                } else if (count <= 200000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                } else if (count <= 300000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                } else if (count <= 400000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                } else if (count <= 500000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                } else if (count <= 600000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                } else if (count <= 700000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                } else if (count <= 800000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                } else if (count <= 900000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                } else if (count <= 1000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                                } else if (count <= 2000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                                } else if (count <= 3000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                                } else if (count <= 4000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayBitmapStream);
                                } else if (count <= 5000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayBitmapStream);
                                } else if (count <= 6000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                                } else if (count <= 7000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                                } else if (count <= 8000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                                } else if (count <= 9000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                                } else if (count <= 10000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                                } else if (count <= 20000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                                } else if (count <= 30000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                                } else if (count <= 40000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                                } else if (count <= 50000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                                } else if (count <= 60000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                } else if (count <= 70000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                } else if (count <= 80000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                } else if (count <= 90000000) {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                } else {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 35, byteArrayBitmapStream);
                                }*/
                                byte[] byteArray = byteArrayBitmapStream.toByteArray();

                                long size = byteArray.length;


                                Log.e("SIZE1234", "&&&&" + size);

                                Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));

                                if (photo1_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/DDSUB/" + "/IMG_PHOTO_1.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/DDSUB/", "/IMG_PHOTO_1.jpg");
                                    if (file.exists()) {
                                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, "PHOTO_1", photo1_text);
                                        Log.e("SIZE1", "&&&&" + photo1_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO1);
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
        Intent i_display_image = new Intent(mContext, ShowDocument.class);
        Bundle extras = new Bundle();
        //saveData();
        extras.putString("key", key);
        extras.putString("data", "DDSUB");

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


        String pht1 = null;


        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/DDSUB/", "/IMG_PHOTO_1.jpg");
        if (file.exists()) {
            pht1 = file.getAbsolutePath();
        }

        if (!TextUtils.isEmpty(pht1)) {

            CustomUtility.setSharedPreference(mContext, "SYNC", "1");

        } else {

            Toast.makeText(this, "Please Select DD Photo.", Toast.LENGTH_SHORT).show();
        }


    }


    public void setFlag(String key) {

        Log.e("FLAG", "&&&" + key);
        photo1_flag = false;

        if (DatabaseHelper.KEY_PHOTO1.equals(key)) {
            photo1_flag = true;
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
    }

    @Override
    public void onBackPressed() {
        //Save();
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

    private class GovtSubData extends AsyncTask<String, String, String> {

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

                jsonObj.put("PROCESS_NO",  CustomUtility.getSharedPreferences(mContext, "process_no"));
                jsonObj.put("REGISNO", regisno);
                jsonObj.put("PROJECT_NO", CustomUtility.getSharedPreferences(mContext, "projectid"));
                jsonObj.put("project_login_no", CustomUtility.getSharedPreferences(mContext, "loginid"));
                jsonObj.put("beneficiary", beneficiary);
                jsonObj.put("userid", CustomUtility.getSharedPreferences(mContext, "userid"));
                jsonObj.put("date", dt1.format(date));
                jsonObj.put("lat", inst_latitude_double);
                jsonObj.put("lng", inst_longitude_double);
                jsonObj.put("remark", remark_txt);

                //jsonObj.put("PHOTO1", CustomUtility.getSharedPreferences(mContext, "PHOTO_1"));

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("gov_officer", String.valueOf(ja_invc_data)));
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

                           // deleteFiles(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/DDSUB/");

                           // CustomUtility.setSharedPreference(mContext, "PHOTO_1", "");

                            progressDialog.dismiss();
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {

                            Message msg = new Message();
                            msg.obj = "Data Not Submitted, Please try After Sometime.";
                            mHandler.sendMessage(msg);

                           // deleteFiles(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/DDSUB/");

                           // CustomUtility.setSharedPreference(mContext, "PHOTO_1", "");

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

