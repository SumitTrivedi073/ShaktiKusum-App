package com.shaktipumplimited.shaktikusum.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import com.shaktipumplimited.shaktikusum.database.DatabaseHelper;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import com.shaktipumplimited.shaktikusum.utility.CameraUtils;
import com.shaktipumplimited.shaktikusum.utility.CustomUtility;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStoragePublicDirectory;

import com.shaktipumplimited.shaktikusum.R;


public class SiteAuditImageActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final int BITMAP_SAMPLE_SIZE = 4 ;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    Context mContext;
    DatabaseHelper dataHelper;
    double AUD_latitude_double,
            AUD_longitude_double;
    int PERMISSION_ALL = 1;
    String type="AUD/";
    Uri fileUri1;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    String imageStoragePath, enq_docno,cust_nm, status, photo1_text, photo2_text, photo3_text, photo4_text;
    TextView photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9, photo10, photo11, photo12;
    boolean photo1_flag = false,
            photo2_flag = false,
            photo3_flag = false,
            photo4_flag = false;


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
        setContentView(R.layout.activity_auditsitereport_image);
        mContext = this;

        dataHelper = new DatabaseHelper(mContext);

        getGpsLocation();

        CustomUtility.setSharedPreference(mContext, "AUDSYNC" + enq_docno, "");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {

                enq_docno= null;
                cust_nm= null;

            } else {

                enq_docno= extras.getString("billno");
                cust_nm= extras.getString("custnm");

            }
        } else {

            enq_docno= (String) savedInstanceState.getSerializable("billno");
            cust_nm= (String) savedInstanceState.getSerializable("custnm");

        }

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + "/SKAPP/AUD/"); //it is my root directory

        File billno = new File(root.getAbsolutePath() + "/SKAPP/AUD/" + enq_docno); // it is my sub folder directory .. it can vary..

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


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        photo1 = (TextView) findViewById(R.id.photo1);
        photo2 = (TextView) findViewById(R.id.photo2);
        photo3 = (TextView) findViewById(R.id.photo3);
        photo4 = (TextView) findViewById(R.id.photo4);


        setData();

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


                if (photo3_text == null || photo3_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO3, "PHOTO_3");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO3, photo3_text, "PHOTO_3");

                }

            }
        });

        photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (photo4_text == null || photo4_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO4, "PHOTO_4");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO4, photo4_text, "PHOTO_4");

                }

            }
        });



    }

    public void openCamera(String name) {

        if (CameraUtils.checkPermissions(mContext)) {

            File file = new File(ImageManager.getMediaFilePath(type,name, enq_docno));

            imageStoragePath = file.getAbsolutePath();
            Log.e("PATH", "&&&" + imageStoragePath);

            Intent i = new Intent(mContext, CameraActivity.class);
            i.putExtra("lat", String.valueOf(AUD_latitude_double));
            i.putExtra("lng", String.valueOf(AUD_longitude_double));
            i.putExtra("cust_name", cust_nm);
            i.putExtra("inst_id", enq_docno);
            i.putExtra("type", "AUD/");
            i.putExtra("name", name);

            startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }


    }

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {
            AUD_latitude_double = gps.getLatitude();
            AUD_longitude_double = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
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


        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_1.jpg");
        if (file.exists()) {
            photo1_text = file.getAbsolutePath();
        }

        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_2.jpg");
        if (file1.exists()) {
            photo2_text = file1.getAbsolutePath();
        }

        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_3.jpg");
        if (file2.exists()) {
            photo3_text = file2.getAbsolutePath();
        }

        File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_4.jpg");
        if (file3.exists()) {
            photo4_text = file3.getAbsolutePath();
        }


        setIcon(DatabaseHelper.KEY_PHOTO1);
        setIcon(DatabaseHelper.KEY_PHOTO2);
        setIcon(DatabaseHelper.KEY_PHOTO3);
        setIcon(DatabaseHelper.KEY_PHOTO4);


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

                try {

                    Log.e("Count", "&&&&&" + imageStoragePath);

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
                        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_1.jpg");
                        if (file.exists()) {
                            photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            setIcon(DatabaseHelper.KEY_PHOTO1);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_1", photo1_text);
                            Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(mContext, enq_docno + "PHOTO_1"));

                        }

                    }

                    if (photo2_flag == true) {
                        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_2.jpg");
                        if (file1.exists()) {
                            photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            setIcon(DatabaseHelper.KEY_PHOTO2);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_2", photo2_text);
                            Log.e("SIZE2", "&&&&" + photo2_text);

                        }
                    }
                    if (photo3_flag == true) {
                        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_3.jpg");
                        if (file2.exists()) {
                            photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_3", photo3_text);
                            Log.e("SIZE3", "&&&&" + photo3_text);
                            setIcon(DatabaseHelper.KEY_PHOTO3);
                        }
                    }

                    if (photo4_flag == true) {
                        File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_4.jpg");
                        if (file3.exists()) {
                            photo4_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_4", photo4_text);
                            Log.e("SIZE4", "&&&&" + photo4_text);
                            setIcon(DatabaseHelper.KEY_PHOTO4);
                        }
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

        }
        else {
            if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {

                if (resultCode == RESULT_OK) {

                    if (data != null) {


                        Uri selectedImageUri = data.getData();
                        String selectedImagePath = getImagePath(selectedImageUri);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        options.inSampleSize = 2;
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
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ "/SKAPP/AUD/" + enq_docno + "/IMG_PHOTO_1.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_1.jpg");
                                    if (file.exists()) {
                                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_1", photo1_text);
                                        Log.e("SIZE1", "&&&&" + photo1_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO1);
                                    }

                                }

                                if (photo2_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno + "/IMG_PHOTO_2.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_2.jpg");
                                    if (file1.exists()) {
                                        photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_2", photo2_text);
                                        Log.e("SIZE2", "&&&&" + photo2_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO2);
                                    }
                                }
                                if (photo3_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno + "/IMG_PHOTO_3.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_3.jpg");
                                    if (file2.exists()) {
                                        photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_3", photo3_text);
                                        Log.e("SIZE3", "&&&&" + photo3_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO3);
                                    }
                                }

                                if (photo4_flag == true) {
                                    String destFile =getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ "/SKAPP/AUD/" + enq_docno + "/IMG_PHOTO_4.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_4.jpg");
                                    if (file3.exists()) {
                                        photo4_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, enq_docno + "PHOTO_4", photo4_text);
                                        Log.e("SIZE4", "&&&&" + photo4_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO4);
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
        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == 301) {

                String year = data.getStringExtra("year");
                String month = data.getStringExtra("month");
                String date = data.getStringExtra("date");
                String finaldate = year + "-" + month + "-" + date;
                finaldate = CustomUtility.formateDate(finaldate);
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
        extras.putString("docno", enq_docno);
        extras.putString("key", key);
        extras.putString("data", "AUD");

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


        String pht1 = null, pht2 = null, pht3 = null, pht4 = null;


        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_1.jpg");
        if (file.exists()) {
            pht1 = file.getAbsolutePath();
        }

        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_2.jpg");
        if (file1.exists()) {
            pht2 = file1.getAbsolutePath();
        }

        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_3.jpg");
        if (file2.exists()) {
            pht3 = file2.getAbsolutePath();
        }

        File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/AUD/" + enq_docno, "/IMG_PHOTO_4.jpg");
        if (file3.exists()) {
            pht4 = file3.getAbsolutePath();
        }


        if (!TextUtils.isEmpty(pht1)) {
            if (!TextUtils.isEmpty(pht2)) {
                if (!TextUtils.isEmpty(pht3)) {
                    if (!TextUtils.isEmpty(pht4)) {

                        CustomUtility.setSharedPreference(mContext, "AUDSYNC" + enq_docno, "1");
                    } else {

                        Toast.makeText(this, "Please Select MISC. Photo.", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(this, "Please Select LA & Earthing Photo.", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(this, "Please Select Structure Assembly Photo.", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(this, "Please Select Foundation Photo.", Toast.LENGTH_SHORT).show();
        }

    }


    public void setFlag(String key) {

        Log.e("FLAG", "&&&" + key);
        photo1_flag = false;
        photo2_flag = false;
        photo3_flag = false;
        photo4_flag = false;

        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                photo1_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO2:
                photo2_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO3:
                photo3_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO4:
                photo4_flag = true;
                break;

        }

    }

    public void setIcon(String key) {


        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                if (photo1_text == null || photo1_text.isEmpty()) {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case DatabaseHelper.KEY_PHOTO2:
                if (photo2_text == null || photo2_text.isEmpty()) {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case DatabaseHelper.KEY_PHOTO3:
                if (photo3_text == null || photo3_text.isEmpty()) {
                    photo3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case DatabaseHelper.KEY_PHOTO4:
                if (photo4_text == null || photo4_text.isEmpty()) {
                    photo4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Save();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

}
