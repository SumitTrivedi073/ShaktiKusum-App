package activity;

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
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import java.util.List;

import adapter.ImageAdapter;
import bean.InstallationBean;
import database.DatabaseHelper;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import utility.CameraUtils;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

import com.shaktipumplimited.shaktikusum.R;


public class UnloadInstReportImageActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final int RC_FILE_PICKER_PERM = 321;
    public static final int BITMAP_SAMPLE_SIZE = 4;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    Context mContext;
    File file;

    private EditText edtRemarkVKID;
    DatabaseHelper dataHelper;
    double inst_latitude_double,
            inst_longitude_double;
    //  String sync_data = "0",lat,lng,type="INST/";
    String type = "UNLOAD/";
    int PERMISSION_ALL = 1;
    // String mImageFolderName = "/SHAKTI/UNLOAD/";
    String mImageFolderName = "/SKAPP/UNLOAD/";

    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusumUnload";
    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,};
    String imageStoragePath, status, photo1_text, photo2_text, photo3_text, photo4_text, photo5_text, photo6_text, photo7_text, photo8_text, photo9_text, photo10_text, photo11_text, photo12_text, photo13_text, photo14_text, photo15_text, photo16_text;
    TextView photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9, photo10, photo11, photo12, photo13, photo14, photo15, photo16;
    boolean photo1_flag = false,
            photo2_flag = false,
            photo3_flag = false,
            photo4_flag = false,
            photo5_flag = false,
            photo6_flag = false,
            photo7_flag = false,
            photo8_flag = false,
            photo9_flag = false,
            photo10_flag = false,
            photo11_flag = false,
            photo12_flag = false,
            photo13_flag = false,
            photo14_flag = false,
            photo15_flag = false,
            photo16_flag = false;
    private String mHomePath, PathHolder, Filename, cust_name;
    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<String> docPaths = new ArrayList<>();
    private RecyclerView recyclerView;

    Button btnSaveUnlodID;

    String mBilNo, mCustmername, Reg_NO, mBeneficiary, mProject_no, mUserid;

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
        setContentView(R.layout.activity_unload_instreport_image);
        mContext = this;
        WebURL.GALLERY_DIRECTORY_NAME_COMMON = "ShaktiKusumUnload";
        dataHelper = new DatabaseHelper(mContext);
        getGpsLocation();
        CustomUtility.setSharedPreference(mContext, "INSTSYNC" + mBilNo, "");
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mBilNo = null;
                mCustmername = null;
                mUserid = null;
            } else {
                mBilNo = extras.getString("vbeln");
                mCustmername = extras.getString("cust_name");
                mUserid = extras.getString("userid");
            }
        } else {
            mBilNo = (String) savedInstanceState.getSerializable("vbeln");
            mCustmername = (String) savedInstanceState.getSerializable("cust_name");
            mUserid = (String) savedInstanceState.getSerializable("userid");
        }

        mBeneficiary = WebURL.BenificiaryNo_Con;
        Reg_NO = WebURL.RegNo_Con;
        mProject_no = WebURL.ProjectNo_Con;
        WebURL.RegNo_Con = "";
        WebURL.ProjectNo_Con = "";
        WebURL.BenificiaryNo_Con = "";

        Log.e("STATUS", "&&&&" + status);
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), GALLERY_DIRECTORY_NAME);
        File dir = new File(root.getAbsolutePath() + mImageFolderName); //it is my root directory
        File billno = new File(root.getAbsolutePath() + mImageFolderName + mBilNo); // it is my sub folder directory .. it can vary..
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
        photo13 = (TextView) findViewById(R.id.photo13);
        photo14 = (TextView) findViewById(R.id.photo14);
        photo15 = (TextView) findViewById(R.id.photo15);
        photo4 = (TextView) findViewById(R.id.photo4);
        edtRemarkVKID = (EditText) findViewById(R.id.edtRemarkVKID);
        btnSaveUnlodID = findViewById(R.id.btnSaveUnlodID);

        btnSaveUnlodID.setOnClickListener(v -> save());

        recyclerView = (RecyclerView) findViewById(R.id.file_list);

        setData();

        photo13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photo13_text == null || photo13_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO13, "PHOTO_13");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO13, photo13_text, "PHOTO_13");
                }
            }
        });

        photo14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photo14_text == null || photo14_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO14, "PHOTO_14");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO14, photo14_text, "PHOTO_14");
                }
            }
        });

        photo15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photo15_text == null || photo15_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO15, "PHOTO_15");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO15, photo15_text, "PHOTO_15");
                }
            }
        });
    }

    public void openCamera(String name) {
        if (CameraUtils.checkPermissions(mContext)) {
            file = new File(ImageManager.getMediaFilePath(type, name, mBilNo));
            imageStoragePath = file.getAbsolutePath();
            Log.e("PATH", "&&&" + imageStoragePath);

            Intent i = new Intent(mContext, CameraActivity.class);
            i.putExtra("lat", String.valueOf(inst_latitude_double));
            i.putExtra("lng", String.valueOf(inst_longitude_double));
            i.putExtra("cust_name", mCustmername);
            i.putExtra("inst_id", mBilNo);
            i.putExtra("type", type);
            i.putExtra("name", name);

            startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    @AfterPermissionGranted(RC_FILE_PICKER_PERM)
    public void pickDocClicked() {
        if (EasyPermissions.hasPermissions(mContext, CustomUtility.PERMISSIONS_FILE_PICKER)) {
            onPickDoc();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_doc_picker),
                    RC_FILE_PICKER_PERM, CustomUtility.PERMISSIONS_FILE_PICKER);
        }
    }

    private void addThemToView(ArrayList<String> imagePaths, ArrayList<String> docPaths) {
        ArrayList<String> filePaths = new ArrayList<>();
        if (imagePaths != null) filePaths.addAll(imagePaths);
        if (docPaths != null) filePaths.addAll(docPaths);
        if (recyclerView != null) {
            StaggeredGridLayoutManager layoutManager =
                    new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
            layoutManager.setGapStrategy(
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerView.setLayoutManager(layoutManager);

            ImageAdapter imageAdapter = new ImageAdapter(mContext, filePaths);

            recyclerView.setAdapter(imageAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public void onPickDoc() {
        File file = new File(Environment.getExternalStorageDirectory(), "/");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        //intent.setType("application/pdf");
        startActivityForResult(intent, 1);
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
        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_13.jpg");
        if (file.exists()) {
            photo13_text = file.getAbsolutePath();
        }
        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_14.jpg");
        if (file1.exists()) {
            photo14_text = file1.getAbsolutePath();
        }
        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_15.jpg");
        if (file2.exists()) {
            photo15_text = file2.getAbsolutePath();
        }
        setIcon(DatabaseHelper.KEY_PHOTO13);
        setIcon(DatabaseHelper.KEY_PHOTO14);
        setIcon(DatabaseHelper.KEY_PHOTO15);
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
        super.onActivityResult(requestCode, resultCode, data);
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
                if (photo13_flag == true) {
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_13.jpg");
                    if (file.exists()) {
                        photo13_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon(DatabaseHelper.KEY_PHOTO13);
                        CustomUtility.setSharedPreference(mContext, mBilNo + "PHOTO_13", photo13_text);
                        Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(mContext, mBilNo + "PHOTO_13"));
                    }
                }
                if (photo14_flag == true) {
                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_14.jpg");
                    if (file1.exists()) {
                        photo14_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon(DatabaseHelper.KEY_PHOTO14);
                        CustomUtility.setSharedPreference(mContext, mBilNo + "PHOTO_14", photo14_text);
                        Log.e("SIZE2", "&&&&" + photo14_text);
                    }
                }
                if (photo15_flag == true) {
                    File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_15.jpg");
                    if (file2.exists()) {
                        photo15_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        CustomUtility.setSharedPreference(mContext, mBilNo + "PHOTO_15", photo15_text);
                        Log.e("SIZE3", "&&&&" + photo15_text);
                        setIcon(DatabaseHelper.KEY_PHOTO15);
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
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
                        options.inSampleSize = 2;
                        options.inJustDecodeBounds = false;
                        try {
                            Log.e("IMAGEURI", "&&&&" + selectedImageUri);
                            if (selectedImageUri != null) {
                                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                                //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                                int count = bitmap.getByteCount();
                                Log.e("Count", "&&&&&" + count);
                                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                                byte[] byteArray = byteArrayBitmapStream.toByteArray();
                                long size = byteArray.length;
                                Log.e("SIZE1234", "&&&&" + size);
                                Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));
                                if (photo13_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo + "/IMG_PHOTO_13.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_13.jpg");
                                    if (file.exists()) {
                                        photo13_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, mBilNo + "PHOTO_13", photo13_text);
                                        Log.e("SIZE1", "&&&&" + photo13_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO13);
                                    }
                                }
                                if (photo14_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo + "/IMG_PHOTO_14.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_14.jpg");
                                    if (file1.exists()) {
                                        photo14_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, mBilNo + "PHOTO_14", photo14_text);
                                        Log.e("SIZE2", "&&&&" + photo14_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO14);
                                    }
                                }
                                if (photo15_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo + "/IMG_PHOTO_15.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_15.jpg");
                                    if (file2.exists()) {
                                        photo15_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(mContext, mBilNo + "PHOTO_15", photo15_text);
                                        Log.e("SIZE3", "&&&&" + photo15_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO15);
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

        addThemToView(photoPaths, docPaths);
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Image already saved, Do you want to change it or display?");
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
        extras.putString("docno", mBilNo);
        extras.putString("key", key);
        extras.putString("data", type);
        CustomUtility.setSharedPreference(mContext, "data", data);
        i_display_image.putExtras(extras);
        startActivity(i_display_image);
    }

    public void showConfirmationGallery(final String keyimage, final String name) {
        final CustomUtility customUtility = new CustomUtility();
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
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

    private void save() {
        String pht1 = null, pht2 = null, pht3 = null, pht4 = null, pht5 = null, pht6 = null, pht7 = null, pht8 = null, pht9 = null, pht10 = null, pht11 = null, pht12 = null;
        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_13.jpg");
        if (file.exists()) {
            pht1 = file.getAbsolutePath();
        }
        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_14.jpg");
        if (file1.exists()) {
            pht2 = file1.getAbsolutePath();
        }
        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo, "/IMG_PHOTO_15.jpg");
        if (file2.exists()) {
            pht3 = file2.getAbsolutePath();
        }
        if (!TextUtils.isEmpty(pht1)) {
            if (!TextUtils.isEmpty(pht2)) {
                if (!TextUtils.isEmpty(pht3)) {
                    if (!edtRemarkVKID.getText().toString().trim().equalsIgnoreCase("")) {
                        new SyncInstallationData().execute();
                    } else {
                        Toast.makeText(this, "Please Enter Remark.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please Select Photo3.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Select  Photo2.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please Select Photo1.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setFlag(String key) {
        Log.e("FLAG", "&&&" + key);
        photo13_flag = false;
        photo14_flag = false;
        photo15_flag = false;

        switch (key) {
            case DatabaseHelper.KEY_PHOTO13:
                photo13_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO14:
                photo14_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO15:
                photo15_flag = true;
                break;
        }
    }

    public void setIcon(String key) {
        switch (key) {
            case DatabaseHelper.KEY_PHOTO13:
                if (photo13_text == null || photo13_text.isEmpty()) {
                    photo13.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo13.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_PHOTO14:
                if (photo14_text == null || photo14_text.isEmpty()) {
                    photo14.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo14.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case DatabaseHelper.KEY_PHOTO15:
                if (photo15_text == null || photo15_text.isEmpty()) {
                    photo15.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo15.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // Save();
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

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(mContext);
        if (gps.canGetLocation()) {
            inst_latitude_double = gps.getLatitude();
            inst_longitude_double = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
    }

    private class SyncInstallationData extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");
        }

        @Override
        protected String doInBackground(String... params) {
            /////////
//            JSONArray testja = new JSONArray();
//            JSONObject testJason = new JSONObject();
//            try {
//                testJason.put("data_return", testja);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return testJason.toString();
            /////////

            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;
            InstallationBean param_invc = new InstallationBean();
            JSONArray ja_invc_data = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            try {
                SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");
                String mPht1 = CustomUtility.getSharedPreferences(mContext, mBilNo + "PHOTO_13");
                String mPht2 = CustomUtility.getSharedPreferences(mContext, mBilNo + "PHOTO_14");
                String mPht3 = CustomUtility.getSharedPreferences(mContext, mBilNo + "PHOTO_15");
                jsonObj.put("userid", mUserid);
                jsonObj.put("vbeln", mBilNo);
                jsonObj.put("project_no", mProject_no);
                jsonObj.put("beneficiary", mBeneficiary);
                jsonObj.put("regisno", Reg_NO);
                jsonObj.put("unload_remark", edtRemarkVKID.getText().toString().trim());
                jsonObj.put("customer_name ", mCustmername);
                jsonObj.put("project_login_no ", CustomUtility.getSharedPreferences(mContext, "loginid"));
                System.out.println("only_text_jsonObj==>>" + jsonObj);
                jsonObj.put("unld_photo1", mPht1);
                jsonObj.put("unld_photo2", mPht2);
                jsonObj.put("unld_photo3", mPht3);
                ja_invc_data.put(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("unloading", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());
            System.out.println("param1_invc_vihu==>>" + param1_invc.toString());
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.INSTALLATION_DATA_UNLOAD, param1_invc);
                Log.e("OUTPUT1", "&&&&" + obj2);
                System.out.println("OUTPUT1==>>" + obj2);
                if (!obj2.equalsIgnoreCase("")) {
                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_return");
                    JSONArray ja = new JSONArray(obj1);
                    Log.e("OUTPUT2", "&&&&" + ja.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");
                        if (invc_done.equalsIgnoreCase("Y")) {
                            Message msg = new Message();
                            msg.obj = "Data Submitted Successfully...";
                            mHandler2.sendMessage(msg);
                            progressDialog.dismiss();
                            runOnUiThread(() -> {
                                resetData();
                            });
                            finish();
                        } else if (invc_done.equalsIgnoreCase("N")) {
                            Message msg = new Message();
                            msg.obj = "Data Not Submitted, Please try After Sometime.";
                            mHandler2.sendMessage(msg);
                            progressDialog.dismiss();
                            finish();
                        }
                    }
                } else {
                    Message msg = new Message();
                    msg.obj = "Something went wrong, please try again";
                    mHandler2.sendMessage(msg);
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
            onResume();
            progressDialog.dismiss();
        }
    }

    private void resetData() {
        deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + mBilNo));
        CustomUtility.setSharedPreference(mContext, mBilNo + "PHOTO_13", "");
        CustomUtility.setSharedPreference(mContext, mBilNo + "PHOTO_14", "");
        CustomUtility.setSharedPreference(mContext, mBilNo + "PHOTO_15", "");
        photo13_text = null;
        photo14_text = null;
        photo15_text = null;
        setIcon(DatabaseHelper.KEY_PHOTO13);
        setIcon(DatabaseHelper.KEY_PHOTO14);
        setIcon(DatabaseHelper.KEY_PHOTO15);
    }

    android.os.Handler mHandler1 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
            // labeledSwitch.setOn(false);

        }
    };
    android.os.Handler mHandler2 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };

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
}
