package com.shaktipumplimited.shaktikusum.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.shaktipumplimited.shaktikusum.R;
import com.shaktipumplimited.shaktikusum.bean.LoginBean;
import com.shaktipumplimited.shaktikusum.bean.SimCardBean;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.shaktipumplimited.shaktikusum.database.DatabaseHelper;
import com.shaktipumplimited.shaktikusum.utility.CameraUtils;
import com.shaktipumplimited.shaktikusum.utility.CustomUtility;
import com.shaktipumplimited.shaktikusum.webservice.CustomHttpClient;
import com.shaktipumplimited.shaktikusum.webservice.WebURL;

public class SIMActivationDetails extends AppCompatActivity {
    Context context;
    private Toolbar mToolbar;
    public static final int BITMAP_SAMPLE_SIZE = 6;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static String imageStoragePath;
    String enq_docno = null;
    String msgtyp,serlno;
    private IntentIntegrator qrScan;
    double inst_latitude_double,
            inst_longitude_double;
    private Uri fileUri; // file url to store image
    DatabaseHelper db;
    String lat_text,
            long_text,cust_nam,cust_mb,cust_add,date,sim_old_no,sim_new_no,deviceno,simdate;
    private ProgressDialog progressDialog;
    EditText doc_date,cust_name,cust_mobile,cust_address,ext_sim_no,new_sim_no,device_no;
    TextView ext_sim_photo,new_sim_photo,drive_photo,save;
    String sim_new_photo_text,sim_old_photo_text,drive_photo_text,current_date,pernr,pernr_type,form;
    ImageView scan;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(SIMActivationDetails.this, mString, Toast.LENGTH_LONG).show();

        }
    };


    android.os.Handler mHandler1 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            device_no.setText("");
            device_no.setText(serlno);
        }
    };


    boolean sim_old_photo_flag = false,
            sim_new_photo_flag = false,
            drive_photo_flag = false;

    LoginBean loginBean;
    SimpleDateFormat simpleDateFormat;
    SimCardBean simCardBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_card_act);
        context = this;
        progressDialog = new ProgressDialog(context);
        db = new DatabaseHelper(context);
        loginBean = new LoginBean();

        Bundle bundle = getIntent().getExtras();


        form = bundle.getString("from");

        if(form.equalsIgnoreCase("oldform"))
        {
            enq_docno = bundle.getString("enqdocno");

        }
        //Toolbar code
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sim Card Replacement");

        getLayout();
//intializing scan object
        qrScan = new IntentIntegrator(this);

        pernr = loginBean.getUserid();
        pernr_type = loginBean.getUsertype();

        if(form.equalsIgnoreCase("newform"))
        {

            setData();

            enq_docno = createDocNo();
            getGpsLocation();

            simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            current_date = simpleDateFormat.format(new Date());

            doc_date.setText(current_date);
        }
        else{

            setData1();
        }

        Log.e("DOC_ID","&&&"+enq_docno);

        ext_sim_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sim_old_photo_text == null || sim_old_photo_text.isEmpty()) {

                    showConfirmationGallery(DatabaseHelper.KEY_SIM_OLD_PHOTO,"SIM_OLD_PHOTO");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_SIM_OLD_PHOTO, sim_old_photo_text,"SIM_OLD_PHOTO");

                }

            }
        });

        new_sim_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sim_new_photo_text == null || sim_new_photo_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_SIM_NEW_PHOTO,"SIM_NEW_PHOTO");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_SIM_NEW_PHOTO, sim_new_photo_text,"SIM_NEW_PHOTO");

                }

            }
        });

        drive_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drive_photo_text == null || drive_photo_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_DRIVE_PHOTO,"DRIVE_PHOTO");
                } else {

                    showConfirmationAlert(DatabaseHelper.KEY_DRIVE_PHOTO, drive_photo_text,"DRIVE_PHOTO");

                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CustomUtility.isInternetOn()) {

                    saveData();
                }
                else {
                    progressDialog = ProgressDialog.show(SIMActivationDetails.this, "", "Please Connect to Internet, Data Save in Offline Mode ..please wait !");
                    saveData();


            }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initiating the qr code scan
                qrScan.initiateScan();
            }
        });

        device_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alert();
            }
        });

    }

    @Override
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
    public void onBackPressed() {

        if(form.equalsIgnoreCase("newform")){
            CustomUtility.setSharedPreference(context,"name",cust_name.getText().toString());
            CustomUtility.setSharedPreference(context,"mobile",cust_mobile.getText().toString());
            CustomUtility.setSharedPreference(context,"address",cust_address.getText().toString());
            CustomUtility.setSharedPreference(context,"deviceno",device_no.getText().toString());
            CustomUtility.setSharedPreference(context,"ext_sim_no",ext_sim_no.getText().toString());
            CustomUtility.setSharedPreference(context,"new_sim_no",new_sim_no.getText().toString());
            CustomUtility.setSharedPreference(context,"sim_old_photo",sim_old_photo_text);
            CustomUtility.setSharedPreference(context,"sim_new_photo",sim_new_photo_text);
            CustomUtility.setSharedPreference(context,"drive_photo",drive_photo_text);
        }
        else{

            simCardBean = new SimCardBean();

            simCardBean = db.getSimDataformation(pernr, enq_docno);

            lat_text = simCardBean.getSim_lat();
            long_text = simCardBean.getSim_lat();

        cust_nam = cust_name.getText().toString();
        cust_mb = cust_mobile.getText().toString();
        cust_add = cust_address.getText().toString();
        sim_old_no = ext_sim_no.getText().toString();
        sim_new_no = new_sim_no.getText().toString();


        deviceno = device_no.getText().toString();

        date = doc_date.getText().toString();

            sim_old_photo_text = simCardBean.getSim_old_photo();
            sim_new_photo_text = simCardBean.getSim_new_photo();
            drive_photo_text = simCardBean.getDrive_photo();

            progressDialog = ProgressDialog.show(SIMActivationDetails.this, "", "Please Connect to Internet, Data Save in Offline Mode ..please wait !");
            SimCardBean simCardBean = new SimCardBean(pernr, pernr_type,
                    enq_docno,
                    deviceno,
                    date,
                    cust_nam,
                    cust_mb,
                    cust_add,
                    lat_text,
                    long_text,
                    sim_new_no,
                    sim_old_no,
                    sim_new_photo_text,
                    drive_photo_text,
                    sim_old_photo_text
            );


            if (db.isRecordExist(DatabaseHelper.TABLE_SIM_REPLACMENT_DATA, DatabaseHelper.KEY_ENQ_DOC, enq_docno)) {
                db.updateSimData(enq_docno, simCardBean);
            }
            else{
                db.insertSimCardData(enq_docno, simCardBean);
            }

            progressDialog.dismiss();
            Toast.makeText(context, "Data Save to Offline Mode.", Toast.LENGTH_SHORT).show();
        }


        super.onBackPressed();
    }


    public void getLayout(){

        doc_date = (EditText) findViewById(R.id.doc_date);

        cust_name = (EditText) findViewById(R.id.cust_name);
        cust_mobile = (EditText) findViewById(R.id.cust_mobile_no);
        cust_address = (EditText) findViewById(R.id.cust_address);
        device_no = (EditText) findViewById(R.id.device_no);
        ext_sim_no = (EditText) findViewById(R.id.sim_card_old);
        new_sim_no = (EditText) findViewById(R.id.sim_card_new);

        ext_sim_photo = (TextView) findViewById(R.id.sim_card_old_photo);
        new_sim_photo = (TextView) findViewById(R.id.sim_card_new_photo);
        drive_photo = (TextView) findViewById(R.id.drive_photo);

        save = (TextView) findViewById(R.id.save);
        scan = (ImageView) findViewById(R.id.view_img_one);


    }

    private void setData() {

        cust_name.setText(CustomUtility.getSharedPreferences(context,"name"));
        cust_mobile.setText(CustomUtility.getSharedPreferences(context,"mobile"));
        cust_address.setText(CustomUtility.getSharedPreferences(context,"address"));
        device_no.setText(CustomUtility.getSharedPreferences(context,"deviceno"));
        ext_sim_no.setText(CustomUtility.getSharedPreferences(context,"ext_sim_no"));
        new_sim_no.setText(CustomUtility.getSharedPreferences(context,"new_sim_no"));
        sim_old_photo_text = CustomUtility.getSharedPreferences(context,"sim_old_photo");
        sim_new_photo_text = CustomUtility.getSharedPreferences(context,"sim_new_photo");
        drive_photo_text = CustomUtility.getSharedPreferences(context,"drive_photo");


        setIcon(DatabaseHelper.KEY_SIM_OLD_PHOTO);
        setIcon(DatabaseHelper.KEY_SIM_NEW_PHOTO);
        setIcon(DatabaseHelper.KEY_DRIVE_PHOTO);
    }

    private void setData1() {

        simCardBean = new SimCardBean();

        simCardBean = db.getSimDataformation(pernr, enq_docno);

        doc_date.setText(simCardBean.getSim_rep_date());
        cust_name.setText(simCardBean.getCust_name());
        cust_mobile.setText(simCardBean.getCust_mobile());
        cust_address.setText(simCardBean.getCust_address());
        device_no.setText(simCardBean.getDevice_no());
        ext_sim_no.setText(simCardBean.getSim_old_no());
        new_sim_no.setText(simCardBean.getSim_new_no());


        sim_old_photo_text = simCardBean.getSim_old_photo();
        sim_new_photo_text = simCardBean.getSim_new_photo();
        drive_photo_text = simCardBean.getDrive_photo();



        setIcon(DatabaseHelper.KEY_SIM_OLD_PHOTO);
        setIcon(DatabaseHelper.KEY_SIM_NEW_PHOTO);
        setIcon(DatabaseHelper.KEY_DRIVE_PHOTO);
    }

    public void alert() {

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // Setting Dialog Title
        dialog.setTitle("Get Device No.");

       final EditText invoice_number = (EditText) dialog.findViewById(R.id.invoice_number);

        // On pressing Settings button
        TextView dialogButton = (TextView) dialog.findViewById(R.id.ok_txt);

        ImageView mCancel = (ImageView) dialog.findViewById(R.id.icn_cancel);


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });


        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String invoiceno = invoice_number.getText().toString();

                if(CustomUtility.isInternetOn()) {

                    if (invoiceno != null && !TextUtils.isEmpty(invoiceno) && !invoiceno.equals("")) {

                        dialog.dismiss();
                        progressDialog = ProgressDialog.show(context, "", "Get Serial Number..please wait !");
                        getSerialnumber(invoiceno);

                    } else {
                        Toast.makeText(context, "Please Enter Travel Mode.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Showing Alert Message
        dialog.show();


    }

    public void openCamera(String keyimage) {

        if (CameraUtils.checkPermissions(context)) {

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            File file = CameraUtils.getOutputMediaFile1(MEDIA_TYPE_IMAGE);

            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
                Log.e("PATH","&&&"+imageStoragePath);
            }

            Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

            Log.e("PATH","&&&"+fileUri);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                    device_no.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // if the result is capturing Image
        Bitmap bitmap = null;
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                try {

                    bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                    int count = bitmap.getByteCount();

                    Log.e("Count", "&&&&&" + count);

                    ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                   /* if(count<=100000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    }
                    else if(count<=200000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    }
                    else if(count<=300000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    }
                    else if(count<=400000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    }
                    else if(count<=500000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    }
                    else if(count<=600000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    }
                    else if(count<=700000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    }
                    else if(count<=800000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    }
                    else if(count<=900000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                    }
                    else if(count<=1000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                    }
                    else if(count<=2000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                    }
                    else if(count<=3000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 65, byteArrayBitmapStream);
                    }
                    else if(count<=4000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 65, byteArrayBitmapStream);
                    }
                    else if(count<=5000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayBitmapStream);
                    }
                    else if(count<=6000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayBitmapStream);
                    }
                    else if(count<=7000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                    }
                    else if(count<=8000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                    }
                    else if(count<=9000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                    }
                    else if(count<=10000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    }
                    else if(count<=20000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    }
                    else if(count<=30000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    }
                    else if(count<=40000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    }
                    else if(count<=50000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    }
                    else if(count<=60000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    }
                    else if(count<=70000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    }
                    else if(count<=80000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    }
                    else if(count<=90000000)
                    {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    }
                    else{
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                    }
*/
                    byte[] byteArray = byteArrayBitmapStream.toByteArray();

                    long size = byteArray.length;

                    Log.e("SIZE1234", "&&&&" + size);

                    Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));

                    if (sim_old_photo_flag == true) {
                        sim_old_photo_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon(DatabaseHelper.KEY_SIM_OLD_PHOTO);
                    }

                    if (sim_new_photo_flag == true) {
                        sim_new_photo_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon(DatabaseHelper.KEY_SIM_NEW_PHOTO);
                    }
                    if (drive_photo_flag == true) {
                        drive_photo_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon(DatabaseHelper.KEY_DRIVE_PHOTO);
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                File file = new File(imageStoragePath);
                if (file.exists()) {
                    file.delete();
                }


            }
        }

    }

    public void setIcon(String key) {


        switch (key) {

            case DatabaseHelper.KEY_SIM_OLD_PHOTO:
                if (sim_old_photo_text == null || sim_old_photo_text.isEmpty()) {
                    ext_sim_photo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    ext_sim_photo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case DatabaseHelper.KEY_SIM_NEW_PHOTO:
                if (sim_new_photo_text == null || sim_new_photo_text.isEmpty()) {
                    new_sim_photo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    new_sim_photo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case DatabaseHelper.KEY_DRIVE_PHOTO:
                if (drive_photo_text == null || drive_photo_text.isEmpty()) {
                    drive_photo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    drive_photo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


        }

    }

    public void showConfirmationAlert(final String keyimage, final String data, final String name) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context,R.style.MyDialogTheme);
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
                showConfirmationGallery(keyimage,name);


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showConfirmationGallery(final String keyimage, final String name) {

        final CustomUtility customUtility = new CustomUtility();

        final CharSequence[] items = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = CustomUtility.checkPermission(context);
                if (items[item].equals("Take Photo")) {

                    if (result) {
                        openCamera(name);
                        setFlag(keyimage);
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void displayImage(String key, String data) {
        Intent i_display_image = new Intent(context, ShowDocument1.class);
        Bundle extras = new Bundle();
        extras.putString("docno", enq_docno);
        extras.putString("key", key);
        extras.putString("data", data);

        i_display_image.putExtras(extras);
        startActivity(i_display_image);
    }


    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            inst_latitude_double = gps.getLatitude();
            inst_longitude_double = gps.getLongitude();
            if (inst_latitude_double == 0.0) {
                CustomUtility.ShowToast("Lat Long not captured, Please try again.", context);
            } else {
                CustomUtility.ShowToast("Latitude:-" + inst_latitude_double + "     " + "Longitude:-" + inst_longitude_double, context);
            }
        } else {
            gps.showSettingsAlert();
        }
    }


    public void saveData() {


            lat_text = String.valueOf(inst_latitude_double);
            long_text = String.valueOf(inst_longitude_double);

            cust_nam = cust_name.getText().toString();
            cust_mb = cust_mobile.getText().toString();
            cust_add = cust_address.getText().toString();
            sim_old_no = ext_sim_no.getText().toString();
            sim_new_no = new_sim_no.getText().toString();
            deviceno = device_no.getText().toString();

            date = doc_date.getText().toString();


           /* if (cust_name != null && !cust_name.equals("")) {
                if (cust_add != null && !cust_add.equals("")) {*/
                   /* if (sim_old_no != null && !sim_old_no.equals("")) {*/

                            if (deviceno != null && !deviceno.equals("")) {
                                if (sim_new_no != null && !sim_new_no.equals("")) {
                                if (!date.equals("")) {
                                    if (lat_text != null && !lat_text.equals("") && !lat_text.equals("0.0")) {
                                        if (long_text != null && !long_text.equals("") && !long_text.equals("0.0")) {
                                            if (sim_old_photo_text != null && !sim_old_photo_text.equals("")) {
                                                if (sim_new_photo_text != null && !sim_new_photo_text.equals("")) {
                                                    if (drive_photo_text != null && !drive_photo_text.equals("")) {
                                                        if (CustomUtility.isInternetOn()) {

                                                            SimCardBean simCardBean = new SimCardBean(pernr, pernr_type,
                                                                    enq_docno,
                                                                    deviceno,
                                                                    date,
                                                                    cust_nam,
                                                                    cust_mb,
                                                                    cust_add,
                                                                    lat_text,
                                                                    long_text,
                                                                    sim_new_no,
                                                                    sim_old_no,
                                                                    sim_new_photo_text,
                                                                    drive_photo_text,
                                                                    sim_old_photo_text
                                                            );

                                                            if (db.isRecordExist(DatabaseHelper.TABLE_SIM_REPLACMENT_DATA, DatabaseHelper.KEY_ENQ_DOC, enq_docno)) {
                                                                db.updateSimData(enq_docno, simCardBean);
                                                            }
                                                            else{
                                                                db.insertSimCardData(enq_docno, simCardBean);
                                                            }


                                                           new SyncSimDataToSap().execute();

                                                        }
                                                        else{
                                                            SimCardBean simCardBean = new SimCardBean(pernr, pernr_type,
                                                                    enq_docno,
                                                                    deviceno,
                                                                    date,
                                                                    cust_nam,
                                                                    cust_mb,
                                                                    cust_add,
                                                                    lat_text,
                                                                    long_text,
                                                                    sim_new_no,
                                                                    sim_old_no,
                                                                    sim_new_photo_text,
                                                                    drive_photo_text,
                                                                    sim_old_photo_text
                                                            );


                                                            if (db.isRecordExist(DatabaseHelper.TABLE_SIM_REPLACMENT_DATA, DatabaseHelper.KEY_ENQ_DOC, enq_docno)) {
                                                                db.updateSimData(enq_docno, simCardBean);
                                                            }
                                                            else{
                                                                db.insertSimCardData(enq_docno, simCardBean);
                                                            }
                                                            if(form.equalsIgnoreCase("newform"))
                                                            {
                                                                CustomUtility.setSharedPreference(context,"name","");
                                                                CustomUtility.setSharedPreference(context,"mobile","");
                                                                CustomUtility.setSharedPreference(context,"address","");
                                                                CustomUtility.setSharedPreference(context,"deviceno","");
                                                                CustomUtility.setSharedPreference(context,"ext_sim_no","");
                                                                CustomUtility.setSharedPreference(context,"new_sim_no","");
                                                                CustomUtility.setSharedPreference(context,"sim_old_photo","");
                                                                CustomUtility.setSharedPreference(context,"sim_new_photo","");
                                                                CustomUtility.setSharedPreference(context,"drive_photo","");
                                                            }

                                                            progressDialog.dismiss();
                                                            Toast.makeText(context, "Data Save to Offline Mode.", Toast.LENGTH_SHORT).show();
                                                            finish();

                                                        }

                                                    }
                                                    else{
                                                        Toast.makeText(context, "Please Take Drive Photo.", Toast.LENGTH_SHORT).show();
                                                        if (progressDialog != null)
                                                            progressDialog.dismiss();
                                                    }

                                                }
                                                else{
                                                    Toast.makeText(context, "Please Take New Sim Photo.", Toast.LENGTH_SHORT).show();
                                                    if (progressDialog != null)
                                                        progressDialog.dismiss();
                                                }

                                            }
                                            else{
                                                Toast.makeText(context, "Please Take Old Sim Photo.", Toast.LENGTH_SHORT).show();
                                                if (progressDialog != null)
                                                    progressDialog.dismiss();
                                            }

                                        }
                                        else{
                                            if (progressDialog != null)
                                                progressDialog.dismiss();
                                        }

                                    }
                                    else{
                                        if (progressDialog != null)
                                            progressDialog.dismiss();
                                    }

                                }
                                else{
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                }
                                }
                                else{
                                    Toast.makeText(context, "Please Enter New Sim No.", Toast.LENGTH_SHORT).show();
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                }

                            }
                            else{
                                Toast.makeText(context, "Please Enter Device No.", Toast.LENGTH_SHORT).show();
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                            }


                   /* }
                    else{
                        Toast.makeText(context, "Please Enter Sim Old No.", Toast.LENGTH_SHORT).show();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
               }
            else{
                Toast.makeText(context, "Please Enter Customer Address", Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
            }

            }
            else{
                Toast.makeText(context, "Please Enter Customer Name", Toast.LENGTH_SHORT).show();
                if (progressDialog != null)
                    progressDialog.dismiss();
            }*/


    }



    public String createDocNo() {

        Calendar cal = Calendar.getInstance();
        int second   = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hour =   cal.get(Calendar.HOUR_OF_DAY);
        int day =    cal.get(Calendar.DAY_OF_MONTH);
        int year =   cal.get(Calendar.YEAR);

        String day_str = null;
        String month_str = null;
        String hour_str = null;
        String second_str = null;
        String minute_str = null;


        DateFormat dateFormat = new SimpleDateFormat("MM", Locale.US);
        Date date = new Date();

        String month =  dateFormat.format(date);


        String userid = loginBean.getUserid();

        if (day < 10){day_str =  ""+ 0 + day;}
        else{ day_str =  ""+ day;}


        if (minute < 10){ minute_str =  ""+ 0 + minute; }
        else{ minute_str =  ""+ minute; }


        if (second < 10){ second_str =  ""+ 0 + second;}
        else{ second_str =  ""+ second; }


        if (hour < 10){ hour_str =  ""+ 0 + hour; }
        else{hour_str =  ""+ hour; }


        if (month.length() == 1 ){ month_str =  ""+ 0 + month;}
        else {month_str = month;}

        String rfq_docno = ""+userid+day_str+month_str+year+hour_str+minute_str+second_str;
        return rfq_docno;
    }


    public void setFlag(String key) {
        sim_old_photo_flag = false;
        sim_new_photo_flag = false;
        drive_photo_flag = false;

        switch (key) {

            case DatabaseHelper.KEY_SIM_OLD_PHOTO:
                sim_old_photo_flag = true;
                break;
            case DatabaseHelper.KEY_SIM_NEW_PHOTO:
                sim_new_photo_flag = true;
                break;
            case DatabaseHelper.KEY_DRIVE_PHOTO:
                drive_photo_flag = true;
                break;

        }

    }

    private  void getSerialnumber(final String serialno) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("bill_number", serialno));


        new Thread() {

            public void run() {

                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.SERIAL_NUMBER, param);
                    Log.d("check_error", obj);
                    if (obj != null) {

/******************************************************************************************/
/*                       get JSONwebservice Data
/******************************************************************************************/
                        JSONArray ja = new JSONArray(obj);

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);

                            msgtyp = jo.getString("MSGTYP");
                            serlno = jo.getString("SERIAL_NO");

                            Log.e("MSG","&&&&"+msgtyp);
                            Log.e("SERIAL_NO","&&&&"+serlno);


                        }
/******************************************************************************************/
/*                       Call DashBoard
/******************************************************************************************/


                        if ("Sucess".equals(msgtyp)) {

                            progressDialog.dismiss();

                            /*********************create message in thread*******************************/
                            Message msg = new Message();
                            msg.obj = "";
                            mHandler1.sendMessage(msg);



                        }
                        else
                        {

                            progressDialog.dismiss();

/*********************create message in thread*******************************/
                            Message msg = new Message();
                            msg.obj = "Please Enter Valid Invoice Number";
                            mHandler.sendMessage(msg);

                            // dismiss the progress dialog

                        }
                    }
                    else {

                        progressDialog.dismiss();
                        Message msg = new Message();
                        msg.obj = "Connection error";
                        mHandler.sendMessage(msg);

                    }

                }catch(Exception e){

                    e.printStackTrace();
                    progressDialog.dismiss();

                }

            }

        }.start();
    }


    private class SyncSimDataToSap extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog = ProgressDialog.show(SIMActivationDetails.this, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {

            String invc_done = null;
            String invc_msg = null;
            String obj2 = null;

            DatabaseHelper db = new DatabaseHelper(context);

            SimCardBean param_invc = new SimCardBean();

            param_invc = db.getSimDataformation(pernr, enq_docno);


            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                String date_s = param_invc.getSim_rep_date();


                SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

                Date date = dt.parse(date_s);
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd", Locale.US);

                enq_docno = param_invc.getEnq_docno();


                jsonObj.put("res_pernr", param_invc.getUser_id());
                jsonObj.put("res_pernr_type", param_invc.getUser_type());
                jsonObj.put("date", dt1.format(date));
                jsonObj.put("lat", param_invc.getSim_lat());
                jsonObj.put("lng", param_invc.getSim_lng());
                jsonObj.put("device_no", param_invc.getDevice_no());

                jsonObj.put("cust_name", param_invc.getCust_name());
                jsonObj.put("cust_mob", param_invc.getCust_mobile());
                jsonObj.put("cust_add", param_invc.getCust_address());
                //jsonObj.put("old_sim_no", param_invc.getSim_old_no());
                jsonObj.put("new_sim_no", param_invc.getSim_new_no());
                jsonObj.put("old_sim_pht", param_invc.getSim_old_photo());
                jsonObj.put("new_sim_pht", param_invc.getSim_new_photo());
                jsonObj.put("drive_pht",param_invc.getDrive_photo());

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("sim_change_data", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param1_invc);

                Log.e("OUTPUT1","&&&&"+obj2);

                if (obj2 != "") {

                    JSONArray ja = new JSONArray(obj2);

                    Log.e("OUTPUT2","&&&&"+ja.toString());

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);


                        invc_done = jo.getString("msgtyp");


                        if (invc_done.equalsIgnoreCase("S")) {
                            progressDialog.dismiss();
                            Message msg = new Message();
                            msg.obj = "Data Submitted Successfully...";
                            mHandler.sendMessage(msg);

                            Log.e("DOCNO","&&&&"+enq_docno);

                            db.deleteSimData(enq_docno);

                            if(form.equalsIgnoreCase("newform"))
                            {
                                CustomUtility.setSharedPreference(context,"name","");
                                CustomUtility.setSharedPreference(context,"mobile","");
                                CustomUtility.setSharedPreference(context,"address","");
                                CustomUtility.setSharedPreference(context,"deviceno","");
                                CustomUtility.setSharedPreference(context,"ext_sim_no","");
                                CustomUtility.setSharedPreference(context,"new_sim_no","");
                                CustomUtility.setSharedPreference(context,"sim_old_photo","");
                                CustomUtility.setSharedPreference(context,"sim_new_photo","");
                                CustomUtility.setSharedPreference(context,"drive_photo","");
                            }
                            finish();

                        } else if (invc_done.equalsIgnoreCase("E")) {
                            progressDialog.dismiss();

                            invc_msg = jo.getString("msg");

                            Message msg = new Message();
                            msg.obj = invc_msg;
                            mHandler.sendMessage(msg);

                           // db.deleteSimData(enq_docno);

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

