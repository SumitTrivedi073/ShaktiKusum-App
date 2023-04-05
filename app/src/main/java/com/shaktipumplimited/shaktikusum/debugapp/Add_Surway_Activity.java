package com.shaktipumplimited.shaktikusum.debugapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.shaktipumplimited.shaktikusum.database.DatabaseHelper;

import com.shaktipumplimited.shaktikusum.DamageMissBean.DamageMissResponse;
import com.shaktipumplimited.shaktikusum.activity.CameraActivitySurvey;
import com.shaktipumplimited.shaktikusum.activity.GPSTracker;
import com.shaktipumplimited.shaktikusum.activity.ImageManager;
import com.shaktipumplimited.shaktikusum.R;
import com.shaktipumplimited.shaktikusum.activity.ShowDocument;
import com.shaktipumplimited.shaktikusum.utility.CameraUtils;
import com.shaktipumplimited.shaktikusum.utility.CustomUtility;
import com.shaktipumplimited.shaktikusum.webservice.CustomHttpClient;
import com.shaktipumplimited.shaktikusum.webservice.WebURL;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class Add_Surway_Activity extends AppCompatActivity {

    double inst_latitude_double,
            inst_longitude_double;
  private EditText edtApplicantNameID, edtContactNoID, edtApplicationNoID, edtSiteAddressID, edtVillageNameID, edtLatLongID, edtSizeOfBorewellID,edtRelevantInformationID,
            edtWaterLeveatSiteID, edtPumpingwaterlevelrequiredID,edtRatingOfPumpSetID, edtSurvePersonContactID,edtSurvePersonNameID,edtFarmerContactID, edtFarmerNameID;

  private String strApplicantNameID, strContactNoID, strApplicationNoID, strSiteAddressID, strVillageNameID, strLatLongID, strSizeOfBorewellID,strRelevantInformationID,
          strWaterLeveatSiteID, strPumpingwaterlevelrequiredID,strRatingOfPumpSetID, strSurvePersonContactID,strSurvePersonNameID,strFarmerContactID, strFarmerNameID;

    private Spinner spinner_RatingOfPumpSetID,spinner_SizeOfBorewellID,spinner_SourceOfwaterID,spinner_InternetConnectivityAvailableID,spinner_PumpingwaterlevelrequiredID,spinner_ACandDCID,spinner_TypeOfIrrigationSystemInstalled,spinner_TypeOfPumpID,spinner_conntype5;

    private RadioGroup rgGroupDarkZoneID1,rgGroupFormerHavingElectricConnectionID1, rgGroupSouthFacingShadowFreeID1, rgAgreeToInstallUniversalsolarPumpID1;
    private RadioButton radio_DarkZone_yesID,radio_FormerHavingElectricConnection_yesID,radio_SouthFacingShadowFree_yesID,radio_AgreeToInstallUniversalsolarPump_yesID;
    private RadioButton radio_DarkZone_NoID,radio_FormerHavingElectricConnection_NoID,radio_SouthFacingShadowFree_NoID,radio_AgreeToInstallUniversalsolarPump_NoID;

    String imageStoragePath, enq_docno, photo1_text, photo2_text, photo3_text;
    File file;
   // String type="INST/";
    String type="SURVAEY/";
    String mImageFolderName = "/SKAPP/SURVAEY/";
   // String mImageFolderName = "/SKAPP/DMGMISS/";
   // String mImageFolderName = "/SHAKTI/DMGMISS/";

    public static final int BITMAP_SAMPLE_SIZE = 4;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;

    DatabaseHelper db;
    DatabaseHelper dataHelper;
    DamageMissResponse mDamageMissResponse;
    int PERMISSION_ALL = 1;
    public static final String GALLERY_DIRECTORY_NAME = "SurveyPhoto";

    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    boolean photo1_flag = false,photo2_flag = false, photo3_flag = false;

    private String mHomePath, PathHolder, Filename,cust_name;

    TextView photo1,photo2, photo3;
    TextView txtSubmitInspectionID;

  
    private Context context;



private RelativeLayout rlvPhotoID1,rlvPhotoID2,rlvPhotoID3,rlvPhotoID4,rlvPhotoID5;
private RelativeLayout rlvBackViewID;
private ImageView imgPhotoID1,imgPhotoID2,imgPhotoID3,imgPhotoID4,imgPhotoID5;

    private String mDropdownValue1,mDropdownValue2,mDropdownValue3,mDropdownValue4,mDropdownValue5, mDropdownValue6, mDropdownValue7, mDropdownValue8;
    private String mRodioValue1,mRodioValue2,mRodioValue3,mRodioValue4,mRodioValue5;
    
    private String mPhotoValue3;
    private String mPhotoValue2;
    private String mPhotoValue1;

private ArrayAdapter<String> dataAdapter_simoprator1, dataAdapter_simoprator2,dataAdapter_simoprator3,dataAdapter_simoprator4,dataAdapter_simoprator5;
    private   ArrayAdapter<String> dataAdapter_conntype1,dataAdapter_conntype2,dataAdapter_conntype3,dataAdapter_conntype4,dataAdapter_conntype5, dataAdapter_conntype6, dataAdapter_conntype7,dataAdapter_conntype8;

    private List<String> list_conntype1 = null;
    private List<String> list_conntype2 = null;
    private List<String> list_conntype3 = null;
    private List<String> list_conntype4 = null;
    private List<String> list_conntype5 = null;
    private List<String> list_conntype6 = null;
    private List<String> list_conntype7 = null;
    private List<String> list_conntype8 = null;
   private int index_conntype1,index_conntype2,index_conntype3,index_conntype4,index_conntype5,index_conntype6,index_conntype7, index_conntype8;
    String conntype_text1 = "", conntype_text2 = "",conntype_text3 = "",conntype_text4 = "",conntype_text5 = "", conntype_text6 = "",conntype_text7 = "", conntype_text8 = "";

    List<String> mDropDownList ;
    List<String> mRoadioList ;
    List<String> mQuentityList ;
    List<String> mRemarkList ;
    List<String> mPhotoList ;

    String billnoN ;
    String billdate,name,state, city,mobileno,address,kunnr, pernr;

    TextView txtLatID, txtLongID;
    String strLatID, strLongID;

    String mUserID = "";
    String mproject_noID = "";
    String mproject_login_noID = "";

    private RelativeLayout rlvSaveButtonID;

    String mApplicationNo, mProjectNo, mMobileNo,mLifnrNo,mCitycTxt, mApplicantName, mregio_txt, mRegistraionNo;
Intent mIntent ;

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
        setContentView(R.layout.activity_add_surway);

        WebURL.GALLERY_DIRECTORY_NAME_COMMON = "SurveyPhoto";

        list_conntype1 = new ArrayList<String>();
        list_conntype2 = new ArrayList<String>();
        list_conntype3 = new ArrayList<String>();
        list_conntype4 = new ArrayList<String>();
        list_conntype5 = new ArrayList<String>();
        list_conntype6 = new ArrayList<String>();
        list_conntype7 = new ArrayList<String>();
        list_conntype8 = new ArrayList<String>();

        mDropDownList = new ArrayList<>();
        mRoadioList = new ArrayList<>();
        mQuentityList = new ArrayList<>();
        mRemarkList = new ArrayList<>();
        mPhotoList = new ArrayList<>();

        mIntent = getIntent();



        mApplicationNo = mIntent.getStringExtra("Beneficiary");
        mProjectNo = mIntent.getStringExtra("ProjectNo");
        mMobileNo = mIntent.getStringExtra("Mobile");
        mLifnrNo = mIntent.getStringExtra("Lifnr");
        mCitycTxt = mIntent.getStringExtra("CitycTxt");
        mApplicantName = mIntent.getStringExtra("ApplicantName");
        mregio_txt = mIntent.getStringExtra("regio_txt");
        mRegistraionNo = mIntent.getStringExtra("RegistraionNo");

        //intent.putExtra("RegistraionNo", mSurweyListResponse.get(position).getRegisno());
        context = this;

        mproject_login_noID = CustomUtility.getSharedPreferences(context, "loginid");
        mproject_noID = CustomUtility.getSharedPreferences(context, "projectid");
        mUserID = CustomUtility.getSharedPreferences(context, "userid");
        getGpsLocation();
        initView();
        initViewSurvay();
    }

    private void initViewSurvay() {

        txtLatID = findViewById(R.id.txtLatID);
        txtLongID = findViewById(R.id.txtLongID);

        txtLatID.setText(""+inst_latitude_double);
        txtLongID.setText(""+inst_longitude_double);

        strLatID =  txtLatID.getText().toString().trim();
        strLongID =  txtLongID.getText().toString().trim();

        edtApplicantNameID = findViewById(R.id.edtApplicantNameID);
        edtContactNoID = findViewById(R.id.edtContactNoID);
        edtApplicationNoID = findViewById(R.id.edtApplicationNoID);
        edtSiteAddressID = findViewById(R.id.edtSiteAddressID);
        edtVillageNameID = findViewById(R.id.edtVillageNameID);
        edtLatLongID = findViewById(R.id.edtLatLongID);

      //  edtSizeOfBorewellID = findViewById(R.id.edtSizeOfBorewellID);
        edtRelevantInformationID = findViewById(R.id.edtRelevantInformationID);
        edtWaterLeveatSiteID = findViewById(R.id.edtWaterLeveatSiteID);
      //  edtPumpingwaterlevelrequiredID = findViewById(R.id.edtPumpingwaterlevelrequiredID);
      //  edtRatingOfPumpSetID = findViewById(R.id.edtRatingOfPumpSetID);
        edtSurvePersonContactID = findViewById(R.id.edtSurvePersonContactID);
        edtSurvePersonNameID = findViewById(R.id.edtSurvePersonNameID);
        edtFarmerContactID = findViewById(R.id.edtFarmerContactID);
        edtFarmerNameID = findViewById(R.id.edtFarmerNameID);

        edtApplicantNameID.setText(mApplicantName);
        edtContactNoID.setText(mMobileNo);
        edtApplicationNoID.setText(mApplicationNo);
        edtSiteAddressID.setText(mCitycTxt);
        edtVillageNameID.setText(mregio_txt);
        //edtLatLongID.setText(inst_latitude_double+" , "+inst_longitude_double);
        edtLatLongID.setText(strLatID+" , "+strLongID);




    }


    private void initView() {
        db = new DatabaseHelper(context);

        pernr  = CustomUtility.getSharedPreferences(context, "userid");

        mRodioValue5 = "YES";
        mRodioValue4 = "YES";
        mRodioValue3 = "YES";
        mRodioValue2 = "YES";
        mRodioValue1 = "YES";


        dataHelper = new DatabaseHelper(context);
        mDamageMissResponse = new DamageMissResponse();

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        WebURL.CUSTOMERID_ID = CustomUtility.getSharedPreferences(context, "userid");

        System.out.println("WebURL.CUSTOMERID_ID==>>"+WebURL.CUSTOMERID_ID);

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + mImageFolderName); //it is my root directory

       // File billno = new File(root.getAbsolutePath() + mImageFolderName + WebURL.CUSTOMERID_ID); // it is my sub folder directory .. it can vary..
        File billno = new File(root.getAbsolutePath() + mImageFolderName + CustomUtility.getSharedPreferences(context, "userid")); // it is my sub folder directory .. it can vary..

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


        rlvSaveButtonID= findViewById(R.id.rlvSaveButtonID);

        rlvBackViewID= findViewById(R.id.rlvBackViewID);
        rgGroupDarkZoneID1= findViewById(R.id.rgGroupDarkZoneID1);
        rgGroupFormerHavingElectricConnectionID1= findViewById(R.id.rgGroupFormerHavingElectricConnectionID1);
        rgGroupSouthFacingShadowFreeID1= findViewById(R.id.rgGroupSouthFacingShadowFreeID1);
        rgAgreeToInstallUniversalsolarPumpID1= findViewById(R.id.rgAgreeToInstallUniversalsolarPumpID1);

        radio_DarkZone_yesID= findViewById(R.id.radio_DarkZone_yesID);
        radio_FormerHavingElectricConnection_yesID= findViewById(R.id.radio_FormerHavingElectricConnection_yesID);
        radio_SouthFacingShadowFree_yesID= findViewById(R.id.radio_SouthFacingShadowFree_yesID);
        radio_AgreeToInstallUniversalsolarPump_yesID= findViewById(R.id.radio_AgreeToInstallUniversalsolarPump_yesID);


        radio_DarkZone_NoID= findViewById(R.id.radio_DarkZone_NoID);
        radio_FormerHavingElectricConnection_NoID= findViewById(R.id.radio_FormerHavingElectricConnection_NoID);
        radio_SouthFacingShadowFree_NoID= findViewById(R.id.radio_SouthFacingShadowFree_NoID);
        radio_AgreeToInstallUniversalsolarPump_NoID= findViewById(R.id.radio_AgreeToInstallUniversalsolarPump_NoID);

        spinner_RatingOfPumpSetID= findViewById(R.id.spinner_RatingOfPumpSetID);
        spinner_SizeOfBorewellID= findViewById(R.id.spinner_SizeOfBorewellID);
        spinner_SourceOfwaterID= findViewById(R.id.spinner_SourceOfwaterID);
        spinner_InternetConnectivityAvailableID= findViewById(R.id.spinner_InternetConnectivityAvailableID);

        spinner_PumpingwaterlevelrequiredID= findViewById(R.id.spinner_PumpingwaterlevelrequiredID);
        spinner_ACandDCID= findViewById(R.id.spinner_ACandDCID);
        spinner_TypeOfIrrigationSystemInstalled= findViewById(R.id.spinner_TypeOfIrrigationSystemInstalled);
        spinner_TypeOfPumpID= findViewById(R.id.spinner_TypeOfPumpID);


       

        rlvPhotoID1= findViewById(R.id.rlvPhotoID1);
        rlvPhotoID2= findViewById(R.id.rlvPhotoID2);
        rlvPhotoID3= findViewById(R.id.rlvPhotoID3);
        rlvPhotoID4= findViewById(R.id.rlvPhotoID4);
        rlvPhotoID5= findViewById(R.id.rlvPhotoID5);

      /*  imgPhotoID1= findViewById(R.id.imgPhotoID1);
        imgPhotoID2= findViewById(R.id.imgPhotoID2);
        imgPhotoID3= findViewById(R.id.imgPhotoID3);
        imgPhotoID4= findViewById(R.id.imgPhotoID4);
        imgPhotoID5= findViewById(R.id.imgPhotoID5);*/

        photo1 = (TextView) findViewById(R.id.photo1);
        photo2 = (TextView) findViewById(R.id.photo2);
        photo3 = (TextView) findViewById(R.id.photo3);


        getConnTypeValue1();
        getConnTypeValue2();
        getConnTypeValue3();
        getConnTypeValue4();
        getConnTypeValue5();
        getConnTypeValue6();
        getConnTypeValue7();
        getConnTypeValue8();

        dataAdapter_conntype1 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype1);
        dataAdapter_conntype1.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_SourceOfwaterID.setAdapter(dataAdapter_conntype1);



        dataAdapter_conntype2 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype2);
        dataAdapter_conntype2.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_InternetConnectivityAvailableID.setAdapter(dataAdapter_conntype2);

        dataAdapter_conntype3 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype3);
        dataAdapter_conntype3.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_TypeOfIrrigationSystemInstalled.setAdapter(dataAdapter_conntype3);

        dataAdapter_conntype4 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype4);
        dataAdapter_conntype4.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_TypeOfPumpID.setAdapter(dataAdapter_conntype4);


        dataAdapter_conntype5 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype5);
        dataAdapter_conntype5.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_SizeOfBorewellID.setAdapter(dataAdapter_conntype5);

        dataAdapter_conntype6 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype6);
        dataAdapter_conntype6.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_RatingOfPumpSetID.setAdapter(dataAdapter_conntype6);


        dataAdapter_conntype7 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype7);
        dataAdapter_conntype7.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_PumpingwaterlevelrequiredID.setAdapter(dataAdapter_conntype7);

        dataAdapter_conntype8 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype8);
        dataAdapter_conntype8.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_ACandDCID.setAdapter(dataAdapter_conntype8);


      /*  dataAdapter_conntype5 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype5);
        dataAdapter_conntype5.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_conntype5.setAdapter(dataAdapter_conntype5);*/


        initClickEvent();
        initClickEventSpinner();


       // intiValidationCheck();
    }

    private boolean intiValidationCheck() {

       //String mDropdownValue1,mDropdownValue2,mDropdownValue3,mDropdownValue4,mDropdownValue5;

        strApplicantNameID = edtApplicantNameID.getText().toString().trim();
        strContactNoID = edtContactNoID.getText().toString().trim();
        strApplicationNoID = edtApplicationNoID.getText().toString().trim();
        strSiteAddressID = edtSiteAddressID.getText().toString().trim();
        strVillageNameID = edtVillageNameID.getText().toString().trim();
        strVillageNameID = edtVillageNameID.getText().toString().trim();

      //  strSizeOfBorewellID = edtSizeOfBorewellID.getText().toString().trim();
        strWaterLeveatSiteID = edtWaterLeveatSiteID.getText().toString().trim();
      //  strPumpingwaterlevelrequiredID = edtPumpingwaterlevelrequiredID.getText().toString().trim();

      //  strRatingOfPumpSetID = edtRatingOfPumpSetID.getText().toString().trim();

        strRelevantInformationID = edtRelevantInformationID.getText().toString().trim();
        strSurvePersonContactID = edtSurvePersonContactID.getText().toString().trim();
        strSurvePersonNameID = edtSurvePersonNameID.getText().toString().trim();
        strFarmerContactID = edtFarmerContactID.getText().toString().trim();
        strFarmerNameID = edtFarmerNameID.getText().toString().trim();
        mDropdownValue1 = conntype_text1;
        mDropdownValue2 = conntype_text2;
        mDropdownValue3 = conntype_text3;
        mDropdownValue4 = conntype_text4;
        mDropdownValue5 = conntype_text5;
        mDropdownValue6 = conntype_text6;
        mDropdownValue7 = conntype_text7;
        mDropdownValue8 = conntype_text8;
        mPhotoValue1 = photo1_text;
        mPhotoValue2 = photo2_text;
        mPhotoValue3 = photo3_text;
        //mRodioValue1 =

        try {
            if(mDropdownValue1.equalsIgnoreCase("") || mDropdownValue1.equalsIgnoreCase("Select Connection Type"))
            {
                Toast.makeText(context, "Select Connection Type", Toast.LENGTH_SHORT).show();
                return false;
            }
           /* else if(mDropdownValue1.equalsIgnoreCase("Bore well"))
            {
                if(strSizeOfBorewellID.equalsIgnoreCase(""))
                {
                    Toast.makeText(context, "Please enter borewell size.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else
                {

                }
                return false;
            }
            else if(strSizeOfBorewellID.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please enter borewell size.", Toast.LENGTH_SHORT).show();
                return false;
            }*/
            else if(strWaterLeveatSiteID.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please enter Water Level at site( in meters)", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(mDropdownValue7.equalsIgnoreCase("") || mDropdownValue7.equalsIgnoreCase("Select Internet Connectivity Type"))
            {
                Toast.makeText(context, "Please enter Pumping water level in Required ( in meters)", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(mDropdownValue8.equalsIgnoreCase("") || mDropdownValue8.equalsIgnoreCase("Select Internet Connectivity Type"))
            {
                Toast.makeText(context, "Select AC and DC", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(mDropdownValue2.equalsIgnoreCase("") || mDropdownValue2.equalsIgnoreCase("Select Internet Connectivity Type"))
            {
                Toast.makeText(context, "Select Internet Connectivity Type", Toast.LENGTH_SHORT).show();
                return false;
            }
            else   if(mDropdownValue3.equalsIgnoreCase("") || mDropdownValue3.equalsIgnoreCase("Select Type Irrigation system installed"))
            {
                Toast.makeText(context, "Select Type Irrigation system installed", Toast.LENGTH_SHORT).show();
                return false;
            }
           /* else if(strRatingOfPumpSetID.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please enter Rating of Pump Set (HP)", Toast.LENGTH_SHORT).show();
                return false;
            }*/
            else if(mDropdownValue6.equalsIgnoreCase("") || mDropdownValue6.equalsIgnoreCase("Select Rating of pump set(HP)"))
            {
                Toast.makeText(context, "Please Select Rating of pump set(HP)", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(mDropdownValue4.equalsIgnoreCase("") || mDropdownValue4.equalsIgnoreCase("Select Pump Type"))
            {
                Toast.makeText(context, "Please Select Pump Type", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(strRelevantInformationID.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please enter Any other relevant Information", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(strFarmerNameID.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please enter Farmer name", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(strFarmerContactID.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please enter Farmer contact number", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(strSurvePersonNameID.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please enter Survey person name", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(strSurvePersonContactID.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please enter Survey person contact number", Toast.LENGTH_SHORT).show();
                return false;
            }
            else  if(mRodioValue1.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please select Complain Type1", Toast.LENGTH_SHORT).show();
                return false;
            }
            else  if(mRodioValue2.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please select Complain Type2", Toast.LENGTH_SHORT).show();
                return false;
            }
            else  if(mRodioValue3.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please select Complain Type3", Toast.LENGTH_SHORT).show();
                return false;
            }
            else  if(mRodioValue4.equalsIgnoreCase(""))
            {
                Toast.makeText(context, "Please select Complain Type4", Toast.LENGTH_SHORT).show();
                return false;
            }
            else  if (photo1_text == null || photo1_text.isEmpty())
            {
                Toast.makeText(context, "Please select photo 1.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else  if (photo2_text == null || photo2_text.isEmpty())
            {
                Toast.makeText(context, "Please select photo 2.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else  if (photo3_text == null || photo3_text.isEmpty())
            {
                Toast.makeText(context, "Please select photo 3.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        // return true;
    }


    private void initClickEventSpinner() {




        spinner_SourceOfwaterID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index_conntype1 = arg0.getSelectedItemPosition();
                //conntype_text = spinner_conntype.getSelectedItem().toString();

                if(spinner_SourceOfwaterID.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type"))
                {
                    conntype_text1 = "";
                }
                else{
                    conntype_text1 = spinner_SourceOfwaterID.getSelectedItem().toString();
                }

                Log.e("SPINNER", "$$$$" + index_conntype1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_InternetConnectivityAvailableID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index_conntype2 = arg0.getSelectedItemPosition();
                //conntype_text = spinner_conntype.getSelectedItem().toString();

                if(spinner_InternetConnectivityAvailableID.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type"))
                {
                    conntype_text2 = "";
                }
                else{
                    conntype_text2 = spinner_InternetConnectivityAvailableID.getSelectedItem().toString();
                }

                Log.e("SPINNER", "$$$$" + index_conntype2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_TypeOfIrrigationSystemInstalled.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index_conntype3 = arg0.getSelectedItemPosition();
                //conntype_text = spinner_conntype.getSelectedItem().toString();

                if(spinner_TypeOfIrrigationSystemInstalled.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type"))
                {
                    conntype_text3 = "";
                }
                else{
                    conntype_text3 = spinner_TypeOfIrrigationSystemInstalled.getSelectedItem().toString();
                }

                Log.e("SPINNER", "$$$$" + index_conntype3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_TypeOfPumpID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index_conntype4 = arg0.getSelectedItemPosition();
                //conntype_text = spinner_conntype.getSelectedItem().toString();

                if(spinner_TypeOfPumpID.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type"))
                {
                    conntype_text4 = "";
                }
                else{
                    conntype_text4 = spinner_TypeOfPumpID.getSelectedItem().toString();
                }

                Log.e("SPINNER", "$$$$" + index_conntype4);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_SizeOfBorewellID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index_conntype5 = arg0.getSelectedItemPosition();
                //conntype_text = spinner_conntype.getSelectedItem().toString();

                if(spinner_SizeOfBorewellID.getSelectedItem().toString().equalsIgnoreCase("Select Borewell Size"))
                {
                    conntype_text5 = "";
                }
                else{
                    conntype_text5 = spinner_SizeOfBorewellID.getSelectedItem().toString();
                }

                Log.e("SPINNER", "$$$$" + index_conntype5);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinner_RatingOfPumpSetID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index_conntype6 = arg0.getSelectedItemPosition();
                //conntype_text = spinner_conntype.getSelectedItem().toString();

                if(spinner_RatingOfPumpSetID.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type"))
                {
                    conntype_text6 = "";
                }
                else{
                    conntype_text6 = spinner_RatingOfPumpSetID.getSelectedItem().toString();
                }

                Log.e("SPINNER", "$$$$" + index_conntype6);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinner_PumpingwaterlevelrequiredID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index_conntype7 = arg0.getSelectedItemPosition();
                //conntype_text = spinner_conntype.getSelectedItem().toString();

                if(spinner_PumpingwaterlevelrequiredID.getSelectedItem().toString().equalsIgnoreCase("Select Pumping water level required in meter head"))
                {
                    conntype_text7 = "";
                }
                else{
                    conntype_text7 = spinner_PumpingwaterlevelrequiredID.getSelectedItem().toString();
                }

                Log.e("SPINNER", "$$$$" + index_conntype7);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinner_ACandDCID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index_conntype8 = arg0.getSelectedItemPosition();
                //conntype_text = spinner_conntype.getSelectedItem().toString();

                if(spinner_ACandDCID.getSelectedItem().toString().equalsIgnoreCase("Select AC and DC"))
                {
                    conntype_text8 = "";
                }
                else{
                    conntype_text8 = spinner_ACandDCID.getSelectedItem().toString();
                }

                Log.e("SPINNER", "$$$$" + index_conntype8);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    public void getConnTypeValue1() {
        list_conntype1.add("Select Connection Type");
        list_conntype1.add("Bore well");
        list_conntype1.add("Pond");
        list_conntype1.add("River");
        list_conntype1.add("Canal water");
        list_conntype1.add("Open well");

    }
    public void getConnTypeValue2() {
        list_conntype2.add("Select Internet Connectivity Type");
        list_conntype2.add("GPRS");
        list_conntype2.add("EDGE");
        list_conntype2.add("2G");
        list_conntype2.add("3G");
        list_conntype2.add("4G");
        list_conntype2.add("Not Available");

    }
    public void getConnTypeValue3() {
        list_conntype3.add("Select Type Irrigation system installed");
        list_conntype3.add("UGPL");
        list_conntype3.add("Sprinkler");
        list_conntype3.add("Drip");
        list_conntype3.add("Flood");
    }
    public void getConnTypeValue4() {
        list_conntype4.add("Select Pump Type");
        list_conntype4.add("Surface");
        list_conntype4.add("Submersible");
    }

    public void getConnTypeValue5() {
        list_conntype5.add("Select Borewell Size");
        list_conntype5.add("4");
        list_conntype5.add("6");
        list_conntype5.add("8");
        list_conntype5.add("10");
        list_conntype5.add("12");
    }

    public void getConnTypeValue6() {
        list_conntype6.add("Select Rating of pump set(HP)");
        list_conntype6.add("1");
        list_conntype6.add("2");
        list_conntype6.add("3");
        list_conntype6.add("5");
        list_conntype6.add("7.5");
        list_conntype6.add("10");
        list_conntype6.add("12.5");
        list_conntype6.add("15");
        list_conntype6.add("20");
    }


    public void getConnTypeValue7() {
        list_conntype7.add("Select Pumping water level required in meter head");
        list_conntype7.add("20");
        list_conntype7.add("30");
        list_conntype7.add("50");
        list_conntype7.add("70");
        list_conntype7.add("100");
       /* list_conntype7.add("120");
        list_conntype7.add("130");
        list_conntype7.add("150");*/  //this option commented by YAsh patidar and Himanshu Sir

    }

    public void getConnTypeValue8() {
        list_conntype8.add("Select AC and DC");
        list_conntype8.add("AC");
        list_conntype8.add("DC");

    }

    private void initClickEvent() {


        rlvBackViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rlvSaveButtonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(intiValidationCheck()) {
                    if (CustomUtility.isInternetOn()) {

                        new SyncDAMAGEMISSData().execute();

                    } else {
                        Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
                    }
                }
                //intiValidationCheck();
             //   dataHelper.insertDamageMissData(mDamageMissResponse);
            }
        });



        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pickDocClicked();
                showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO_1");
                /*if (photo1_text == null || photo1_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO_1");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO1, photo1_text, "PHOTO_1");
                }*/
            }
        });

   photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pickDocClicked();
                showConfirmationGallery(DatabaseHelper.KEY_PHOTO3, "PHOTO_3");
                /*if (photo1_text == null || photo1_text.isEmpty()) {
                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO_1");
                } else {
                    showConfirmationAlert(DatabaseHelper.KEY_PHOTO1, photo1_text, "PHOTO_1");
                }*/
            }
        });

      photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO_2");

            }
        });



    }

    // rgGroupFormerHavingElectricConnectionID1, rgGroupSouthFacingShadowFreeID1, rgAgreeToInstallUniversalsolarPumpID1

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked

        switch(view.getId()) {
            case R.id.radio_DarkZone_yesID:
                if (checked)
                    // Pirates are the best
                    mRodioValue1 = "YES";
               // mRoadioList.add(mRodioValue1);

                    break;
            case R.id.radio_DarkZone_NoID:
                if (checked)
                    mRodioValue1 = "NO";
                    // Ninjas rule
              //  mRoadioList.add(mRodioValue1);
                    break;

            case R.id.radio_FormerHavingElectricConnection_yesID:
                if (checked)
                    mRodioValue2 = "YES";
             //   mRoadioList.add(mRodioValue2);
                break;
            case R.id.radio_FormerHavingElectricConnection_NoID:
                if (checked)
                    mRodioValue2 = "NO";
            //    mRoadioList.add(mRodioValue2);
                // Ninjas rule
                break;

            case R.id.radio_SouthFacingShadowFree_yesID:
                if (checked)
                    // Pirates are the best
                    mRodioValue3 = "YES";

               // mRoadioList.add(mRodioValue3);
                break;
            case R.id.radio_SouthFacingShadowFree_NoID:
                if (checked)
                    mRodioValue3 = "NO";
              //  mRoadioList.add(mRodioValue3);
                // Ninjas rule
                break;

            case R.id.radio_AgreeToInstallUniversalsolarPump_yesID:
                if (checked)
                    mRodioValue4 = "YES";

              //  mRoadioList.add(mRodioValue4);

                break;
            case R.id.radio_AgreeToInstallUniversalsolarPump_NoID:
                if (checked)
                    mRodioValue4 = "NO";
             //   mRoadioList.add(mRodioValue4);
                // Ninjas rule
                break;

        }
    }

   /* public void showConfirmationGallery(final String keyimage, final String name) {

        final CustomUtility customUtility = new CustomUtility();

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = customUtility.checkPermission(context);
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
    }*/

    public void showConfirmationGallery(final String keyimage, final String name) {

        final CustomUtility customUtility = new CustomUtility();

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

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

    public void openCamera(String name) {

        if (CameraUtils.checkPermissions(context)) {

           /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            String from = "INST/";

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE, enq_docno, name, from);

            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
                Log.e("PATH", "&&&" + imageStoragePath);
            }

            fileUri1 = CameraUtils.getOutputMediaFileUri(mContext, file);

            Log.e("fileUri", "&&&" + fileUri1);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
*/

            file = new File(ImageManager.getMediaFilePath(type,name, WebURL.CUSTOMERID_ID));

            imageStoragePath = file.getAbsolutePath();
            Log.e("PATH", "&&&" + imageStoragePath);

            Intent i = new Intent(context, CameraActivitySurvey.class);
            i.putExtra("lat", String.valueOf(inst_latitude_double));
            i.putExtra("lng", String.valueOf(inst_longitude_double));
            i.putExtra("cust_name", cust_name);
            i.putExtra("inst_id", WebURL.CUSTOMERID_ID);
            i.putExtra("type", type);
            i.putExtra("name", name);

            startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }


    }

    public void openGallery(String name) {

        if (ActivityCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT <= 19) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);

                ((Activity) context).startActivityForResult(Intent.createChooser(i, "Select Photo"), GALLERY_IMAGE_REQUEST_CODE);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Photo"), GALLERY_IMAGE_REQUEST_CODE);

            }

        } else {
            if (!hasPermissions(context, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_ALL);
            }
        }
    }

    public void setFlag(String key) {

        Log.e("FLAG", "&&&" + key);
        photo1_flag = false;
        photo2_flag = false;
        photo3_flag = false;



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

        }
    }

    @SuppressLint("Range")
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

               // Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));

               // if (photo1_flag == true)
                if (photo1_flag)
                {
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1.jpg");
                    if (file.exists()) {
                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue1 = photo1_text;
                        setIcon(DatabaseHelper.KEY_PHOTO1);
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1", photo1_text);
                        Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(context, WebURL.CUSTOMERID_ID + "PHOTO_1"));

                    }
                }

                if (photo2_flag) {
                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_2.jpg");
                    if (file1.exists()) {
                        photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue2 = photo2_text;
                        setIcon(DatabaseHelper.KEY_PHOTO2);
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_2", photo2_text);
                        Log.e("SIZE2", "&&&&" + photo2_text);

                    }
                }

                if (photo3_flag) {
                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_3.jpg");
                    if (file1.exists()) {
                        photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue3 = photo3_text;
                        setIcon(DatabaseHelper.KEY_PHOTO3);
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_2", photo3_text);
                        Log.e("SIZE2", "&&&&" + photo3_text);

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
        else {
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

                                if (photo1_flag) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_1.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1.jpg");
                                    if (file.exists()) {
                                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1", photo1_text);
                                        Log.e("SIZE1", "&&&&" + photo1_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO1);
                                    }

                                }

                                if (photo2_flag) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_2.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_2.jpg");
                                    if (file1.exists()) {
                                        photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_2", photo2_text);
                                        Log.e("SIZE2", "&&&&" + photo2_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO2);
                                    }
                                }

                                if (photo3_flag) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_3.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_3.jpg");
                                    if (file1.exists()) {
                                        photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_3", photo3_text);
                                        Log.e("SIZE2", "&&&&" + photo3_text);
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

            Cursor cursor1 = ((Activity) context).getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
            Cursor cursor2 = ((Activity) context).getContentResolver().query(uri, projection, null, null, null);

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
        Intent i_display_image = new Intent(context, ShowDocument.class);
        Bundle extras = new Bundle();
        //saveData();
        extras.putString("docno", enq_docno);
        extras.putString("key", key);
        extras.putString("data", "INST");

        CustomUtility.setSharedPreference(context, "data", data);

        i_display_image.putExtras(extras);
        startActivity(i_display_image);
    }



    private class SyncDAMAGEMISSData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;

          //  DatabaseHelper db = new DatabaseHelper(context);

          //  InstallationBean param_invc = new InstallationBean();
           // param_invc = db.getInstallationData(pernr, billno);


            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                jsonObj.put("project_no", mproject_noID);//customerID
                jsonObj.put("regisno", mRegistraionNo);//customerID
                jsonObj.put("REGISNO", mRegistraionNo);//customerID
                jsonObj.put("userid", mUserID);//customerID
                jsonObj.put("project_login_no", mproject_login_noID);//customerID
                jsonObj.put("SURVEYOR_SAP", pernr);//customerID

                jsonObj.put("SURVEYOR_SAP", pernr);//customerID
                jsonObj.put("APPLICANT_NAME", strApplicantNameID);//userID
                jsonObj.put("CONTACT_NO", strContactNoID);//userID
                jsonObj.put("APPLICANT_NO", strApplicationNoID);//userID
                jsonObj.put("SITE_ADRC", strSiteAddressID);//userID
                jsonObj.put("VILLAGE", strVillageNameID);//userID
                jsonObj.put("Lat", strLatID);//userID
                jsonObj.put("lng", strLongID);//userID

              //  jsonObj.put("BOREWELL_SIZE", strSizeOfBorewellID);//userID

                jsonObj.put("WATER_LVL", strWaterLeveatSiteID);//userID
               // jsonObj.put("PUMP_WATER_LVL", strPumpingwaterlevelrequiredID);//userID

               // jsonObj.put("PUMP_SET_RATING", strRatingOfPumpSetID);//userID

                jsonObj.put("REMARK_ANY_OTH", strRelevantInformationID);//userID
                jsonObj.put("SURVEYOR_SIGN_NAME", strSurvePersonNameID);//userID
                jsonObj.put("SURVEYOR_CONTACT_NO", strSurvePersonContactID);//userID
                jsonObj.put("FARMER_SIGNATURE", strFarmerNameID);//userID
                jsonObj.put("FARMER_CONTACT_NO", strFarmerContactID);//userID

                jsonObj.put("WATER_SOURCE", mDropdownValue1);
                jsonObj.put("INTERNET_TYPE", mDropdownValue2);
                jsonObj.put("TYPE_OF_IRIGATN", mDropdownValue3);
                jsonObj.put("PUMP_TYPE", mDropdownValue4);
              //  jsonObj.put("categorytyp5", mDropdownValue5);
                jsonObj.put("BOREWELL_SIZE", mDropdownValue5);//userID
                jsonObj.put("PUMP_SET_RATING", mDropdownValue6);//userID
                jsonObj.put("PUMP_WATER_LVL", mDropdownValue7);//userID
                jsonObj.put("PUMP_AC_DC", mDropdownValue8);//userID

                jsonObj.put("DARK_ZONE_OR_NOT", mRodioValue1);
                jsonObj.put("ELEC_CON", mRodioValue2);
                jsonObj.put("SHADOW_FREE_LAND", mRodioValue3);
                jsonObj.put("SOLAR_PUMP_CONTROLLER", mRodioValue4);
             //   jsonObj.put("category5", mRodioValue5);

                jsonObj.put("PHOTO1", photo1_text);
                jsonObj.put("PHOTO2", photo2_text);
                jsonObj.put("PHOTO3", photo3_text);


               // jsonObj.put("PHOTO5", CustomUtility.getSharedPreferences(context, billno + "PHOTO_5"));

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
          //  param1_invc.add(new BasicNameValuePair("installation", String.valueOf(ja_invc_data)));
            param1_invc.add(new BasicNameValuePair("survey", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.SAVE_SURVEY_DATA, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (!obj2.equalsIgnoreCase("")) {

                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_save");


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

                            Log.e("DOCNO", "&&&&" + billnoN);
                          //  db.deleteDAMAGEMISSData(billnoN);
                           // db.deleteInstallationListData1(billnoN);

                            deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID));


                            CustomUtility.setSharedPreference(context, billnoN + "PHOTO_1", "");
                            CustomUtility.setSharedPreference(context, billnoN + "PHOTO_2", "");
                            CustomUtility.setSharedPreference(context, billnoN + "PHOTO_3", "");

                            progressDialog.dismiss();
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {

                            Message msg = new Message();
                            msg.obj = "Data Not Submitted, Please try After Sometime.";
                            mHandler2.sendMessage(msg);
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


    android.os.Handler mHandler2 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();


        }
    };

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            inst_latitude_double = gps.getLatitude();
            inst_longitude_double = gps.getLongitude();
          /*  if (inst_latitude_double == 0.0) {
                CustomUtility.ShowToast("Lat Long not captured, Please try again.", mContext);
            } else {
                //CustomUtility.ShowToast("Latitude:-" + inst_latitude_double + "     " + "Longitude:-" + inst_longitude_double, mContext);
            }*/
        } else {
            gps.showSettingsAlert();
        }
    }

}