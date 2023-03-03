package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import database.DatabaseHelper;

import com.shaktipumplimited.DamageMissBean.DamageMissResponse;
import com.shaktipumplimited.shaktikusum.R;

import utility.CameraUtils;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class AddDamage_MissingActivity extends AppCompatActivity {

    String imageStoragePath, enq_docno, photo1_text, photo2_text, photo3_text, photo4_text, photo5_text, photo6_text, photo7_text, photo8_text;
    String photo1_text2, photo1_text3, photo1_text4, photo1_text5;
    public static final int BITMAP_SAMPLE_SIZE = 4;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    RadioGroup rgGroupID1, rgGroupID2, rgGroupID3, rgGroupID4, rgGroupID5;
    DatabaseHelper db;
    DatabaseHelper dataHelper;
    DamageMissResponse mDamageMissResponse;
    int PERMISSION_ALL = 1;
    public static final String GALLERY_DIRECTORY_NAME = "Solar_Photo";
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    boolean photo1_flag = false, photo1_flag2 = false, photo1_flag3 = false, photo1_flag4 = false, photo1_flag5 = false,
            photo2_flag = false, photo2_flag2 = false, photo2_flag3 = false, photo2_flag4 = false, photo2_flag5 = false,
            photo3_flag = false, photo3_flag2 = false, photo3_flag3 = false, photo3_flag4 = false, photo3_flag5 = false,
            photo4_flag = false, photo4_flag2 = false, photo4_flag3 = false, photo4_flag4 = false, photo4_flag5 = false,
            photo5_flag = false, photo5_flag2 = false, photo5_flag3 = false, photo5_flag4 = false, photo5_flag5 = false,
            photo6_flag = false,
            photo7_flag = false;

    private String mHomePath, PathHolder, Filename, cust_name;

    TextView photo1, photo1_2, photo1_3, photo1_4, photo1_5;
    TextView txtSubmitInspectionID, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9, photo10, photo11, photo12;
    private RadioButton radio_damage1, radio_damage2, radio_damage3, radio_damage4, radio_damage5;
    private RadioButton radio_miss1, radio_miss2, radio_miss3, radio_miss4, radio_miss5;

    private Context context;
    private RelativeLayout rlvComplainID1, rlvComplainID2, rlvComplainID3, rlvComplainID4, rlvComplainID5;
    private LinearLayout rlvContainerCompID1, rlvContainerCompID2, rlvContainerCompID3, rlvContainerCompID4, rlvContainerCompID5;

    private Spinner spinner_conntype1, spinner_conntype2, spinner_conntype3, spinner_conntype4, spinner_conntype5;
    private EditText edtQuintityID1, edtQuintityID2, edtQuintityID3, edtQuintityID4, edtQuintityID5;
    private EditText edtRemarkID1, edtRemarkID2, edtRemarkID3, edtRemarkID4, edtRemarkID5;
    private RelativeLayout rlvPhotoID1, rlvPhotoID2, rlvPhotoID3, rlvPhotoID4, rlvPhotoID5;
    private RelativeLayout rlvBackViewID;
    private ImageView imgPhotoID1, imgPhotoID2, imgPhotoID3, imgPhotoID4, imgPhotoID5;

    private String mDropdownValue1, mDropdownValue2, mDropdownValue3, mDropdownValue4, mDropdownValue5;
    private String mRodioValue1, mRodioValue2, mRodioValue3, mRodioValue4, mRodioValue5;
    private String mQuentityValue1, mQuentityValue2, mQuentityValue3, mQuentityValue4, mQuentityValue5;
    private String mRemarkValue1, mRemarkValue2, mRemarkValue3, mRemarkValue4, mRemarkValue5;
    private String mPhotoValue2, mPhotoValue3, mPhotoValue4, mPhotoValue5;
    private String mPhotoValue1, mPhotoValue1_2, mPhotoValue1_3, mPhotoValue1_4, mPhotoValue1_5;

    private ArrayAdapter<String> dataAdapter_simoprator1, dataAdapter_simoprator2, dataAdapter_simoprator3, dataAdapter_simoprator4, dataAdapter_simoprator5;
    private ArrayAdapter<String> dataAdapter_conntype1, dataAdapter_conntype2, dataAdapter_conntype3, dataAdapter_conntype4, dataAdapter_conntype5;

    private List<String> list_conntype = null;
    private int index_conntype1, index_conntype2, index_conntype3, index_conntype4, index_conntype5;
    String conntype_text1 = "", conntype_text2 = "", conntype_text3 = "", conntype_text4 = "", conntype_text5 = "";

    List<String> mDropDownList;
    List<String> mRoadioList;
    List<String> mQuentityList;
    List<String> mRemarkList;
    List<String> mPhotoList;

    String billnoN;
    String billdate, name, state, city, mobileno, address, kunnr, pernr;

    private RelativeLayout rlvSaveButtonID;

    String mImageFolderName = "/SKAPP/DMGMISS/";

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
        setContentView(R.layout.activity_add_damage_missing);

        WebURL.GALLERY_DIRECTORY_NAME_COMMON = "Solar_Photo";

        list_conntype = new ArrayList<String>();
        mDropDownList = new ArrayList<>();
        mRoadioList = new ArrayList<>();
        mQuentityList = new ArrayList<>();
        mRemarkList = new ArrayList<>();
        mPhotoList = new ArrayList<>();
        context = this;
        initView();
    }

    private void initView() {
        db = new DatabaseHelper(context);
        pernr = CustomUtility.getSharedPreferences(context, "userid");
        Bundle extras = getIntent().getExtras();
        billnoN = extras.getString("bill_no");
        state = extras.getString("state");
        name = extras.getString("name");
        city = extras.getString("city");
        billdate = extras.getString("bill_date");
        mobileno = extras.getString("mobile");
        address = extras.getString("address");
        kunnr = extras.getString("kunnr");

        mRodioValue5 = "Damage";
        mRodioValue4 = "Damage";
        mRodioValue3 = "Damage";
        mRodioValue2 = "Damage";
        mRodioValue1 = "Damage";

        dataHelper = new DatabaseHelper(context);
        mDamageMissResponse = new DamageMissResponse();

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        WebURL.CUSTOMERID_ID = CustomUtility.getSharedPreferences(context, "userid");
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), GALLERY_DIRECTORY_NAME);
        File dir = new File(root.getAbsolutePath() + mImageFolderName); //it is my root directory
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

        rlvSaveButtonID = findViewById(R.id.rlvSaveButtonID);
        rlvBackViewID = findViewById(R.id.rlvBackViewID);
        rgGroupID1 = findViewById(R.id.rgGroupID1);
        rgGroupID2 = findViewById(R.id.rgGroupID2);
        rgGroupID3 = findViewById(R.id.rgGroupID3);
        rgGroupID4 = findViewById(R.id.rgGroupID4);
        rgGroupID5 = findViewById(R.id.rgGroupID5);

        radio_damage1 = findViewById(R.id.radio_damage1);
        radio_damage2 = findViewById(R.id.radio_damage2);
        radio_damage3 = findViewById(R.id.radio_damage3);
        radio_damage4 = findViewById(R.id.radio_damage4);
        radio_damage5 = findViewById(R.id.radio_damage5);

        radio_miss1 = findViewById(R.id.radio_miss1);
        radio_miss2 = findViewById(R.id.radio_miss2);
        radio_miss3 = findViewById(R.id.radio_miss3);
        radio_miss4 = findViewById(R.id.radio_miss4);
        radio_miss5 = findViewById(R.id.radio_miss5);

        rlvComplainID1 = findViewById(R.id.rlvComplainID1);
        rlvComplainID2 = findViewById(R.id.rlvComplainID2);
        rlvComplainID3 = findViewById(R.id.rlvComplainID3);
        rlvComplainID4 = findViewById(R.id.rlvComplainID4);
        rlvComplainID5 = findViewById(R.id.rlvComplainID5);

        rlvContainerCompID1 = findViewById(R.id.rlvContainerCompID1);
        rlvContainerCompID2 = findViewById(R.id.rlvContainerCompID2);
        rlvContainerCompID3 = findViewById(R.id.rlvContainerCompID3);
        rlvContainerCompID4 = findViewById(R.id.rlvContainerCompID4);
        rlvContainerCompID5 = findViewById(R.id.rlvContainerCompID5);

        spinner_conntype1 = findViewById(R.id.spinner_conntype1);
        spinner_conntype2 = findViewById(R.id.spinner_conntype2);
        spinner_conntype3 = findViewById(R.id.spinner_conntype3);
        spinner_conntype4 = findViewById(R.id.spinner_conntype4);
        spinner_conntype5 = findViewById(R.id.spinner_conntype5);

        edtQuintityID1 = findViewById(R.id.edtQuintityID1);
        edtQuintityID2 = findViewById(R.id.edtQuintityID2);
        edtQuintityID3 = findViewById(R.id.edtQuintityID3);
        edtQuintityID4 = findViewById(R.id.edtQuintityID4);
        edtQuintityID5 = findViewById(R.id.edtQuintityID5);

        edtRemarkID1 = findViewById(R.id.edtRemarkID1);
        edtRemarkID2 = findViewById(R.id.edtRemarkID2);
        edtRemarkID3 = findViewById(R.id.edtRemarkID3);
        edtRemarkID4 = findViewById(R.id.edtRemarkID4);
        edtRemarkID5 = findViewById(R.id.edtRemarkID5);

        rlvPhotoID1 = findViewById(R.id.rlvPhotoID1);
        rlvPhotoID2 = findViewById(R.id.rlvPhotoID2);
        rlvPhotoID3 = findViewById(R.id.rlvPhotoID3);
        rlvPhotoID4 = findViewById(R.id.rlvPhotoID4);
        rlvPhotoID5 = findViewById(R.id.rlvPhotoID5);

        photo1 = (TextView) findViewById(R.id.photo1);
        photo1_2 = (TextView) findViewById(R.id.photo1_2);
        photo1_3 = (TextView) findViewById(R.id.photo1_3);
        photo1_4 = (TextView) findViewById(R.id.photo1_4);
        photo1_5 = (TextView) findViewById(R.id.photo1_5);

        photo2 = (TextView) findViewById(R.id.photo2);
        photo3 = (TextView) findViewById(R.id.photo3);
        photo4 = (TextView) findViewById(R.id.photo4);
        photo5 = (TextView) findViewById(R.id.photo5);

        getConnTypeValue();

        dataAdapter_conntype1 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype);
        dataAdapter_conntype1.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_conntype1.setAdapter(dataAdapter_conntype1);

        dataAdapter_conntype2 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype);
        dataAdapter_conntype2.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_conntype2.setAdapter(dataAdapter_conntype2);

        dataAdapter_conntype3 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype);
        dataAdapter_conntype3.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_conntype3.setAdapter(dataAdapter_conntype3);

        dataAdapter_conntype4 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype);
        dataAdapter_conntype4.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_conntype4.setAdapter(dataAdapter_conntype4);

        dataAdapter_conntype5 = new ArrayAdapter<String>(this, R.layout.spinner_item_left_optional, list_conntype);
        dataAdapter_conntype5.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_conntype5.setAdapter(dataAdapter_conntype5);

        initClickEvent();
        initClickEventSpinner();

        if (db.isRecordExist(db.TABLE_DAMAGE_MISS_COMPLAIN, db.KEY_BILL_NO, billnoN)) {

            mDamageMissResponse = db.getDamageMissData(billnoN);
            int spinnerPosition1 = dataAdapter_conntype1.getPosition(mDamageMissResponse.getMDropdownValue1());
            spinner_conntype1.setSelection(spinnerPosition1);

            int spinnerPosition2 = dataAdapter_conntype2.getPosition(mDamageMissResponse.getMDropdownValue2());
            spinner_conntype2.setSelection(spinnerPosition2);

            int spinnerPosition3 = dataAdapter_conntype3.getPosition(mDamageMissResponse.getMDropdownValue3());
            spinner_conntype3.setSelection(spinnerPosition3);

            int spinnerPosition4 = dataAdapter_conntype4.getPosition(mDamageMissResponse.getMDropdownValue4());
            spinner_conntype4.setSelection(spinnerPosition4);

            int spinnerPosition5 = dataAdapter_conntype5.getPosition(mDamageMissResponse.getMDropdownValue5());
            spinner_conntype5.setSelection(spinnerPosition5);

            edtRemarkID1.setText(mDamageMissResponse.getMRemarkValue1());
            edtRemarkID2.setText(mDamageMissResponse.getMRemarkValue2());
            edtRemarkID3.setText(mDamageMissResponse.getMRemarkValue3());
            edtRemarkID4.setText(mDamageMissResponse.getMRemarkValue4());
            edtRemarkID5.setText(mDamageMissResponse.getMRemarkValue5());
            edtQuintityID1.setText(mDamageMissResponse.getMQuentityValue1());
            edtQuintityID2.setText(mDamageMissResponse.getMQuentityValue2());
            edtQuintityID3.setText(mDamageMissResponse.getMQuentityValue3());
            edtQuintityID4.setText(mDamageMissResponse.getMQuentityValue4());
            edtQuintityID5.setText(mDamageMissResponse.getMQuentityValue5());
            try {
                String RDBValue1 = mDamageMissResponse.getMRodioValue1();
                String RDBValue2 = mDamageMissResponse.getMRodioValue2();
                String RDBValue3 = mDamageMissResponse.getMRodioValue3();
                String RDBValue4 = mDamageMissResponse.getMRodioValue4();
                String RDBValue5 = mDamageMissResponse.getMRodioValue5();
                System.out.println("RDBValue1==>>" + RDBValue1);
                if (RDBValue1.equalsIgnoreCase("Damage")) {
                    rgGroupID1.check(R.id.radio_damage1);
                } else {
                    rgGroupID1.check(R.id.radio_miss1);
                }

                if (RDBValue2.equalsIgnoreCase("Damage")) {
                    rgGroupID2.check(R.id.radio_damage2);
                } else {
                    rgGroupID2.check(R.id.radio_miss2);
                }

                if (RDBValue3.equalsIgnoreCase("Damage")) {
                    rgGroupID3.check(R.id.radio_damage3);
                } else {
                    rgGroupID3.check(R.id.radio_miss3);
                }
                if (RDBValue4.equalsIgnoreCase("Damage")) {
                    rgGroupID4.check(R.id.radio_damage4);
                } else {
                    rgGroupID4.check(R.id.radio_miss4);
                }
                if (RDBValue5.equalsIgnoreCase("Damage")) {
                    rgGroupID5.check(R.id.radio_damage5);
                } else {
                    rgGroupID5.check(R.id.radio_miss5);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                photo1_text = mDamageMissResponse.getMPhotoValue1();
                if (photo1_text == null || photo1_text.isEmpty()) {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }

                photo1_text2 = mDamageMissResponse.getMPhotoValue1_2();
                if (photo1_text2 == null || photo1_text2.isEmpty()) {
                    photo1_2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1_2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }

                photo1_text3 = mDamageMissResponse.getMPhotoValue1_3();
                if (photo1_text3 == null || photo1_text3.isEmpty()) {
                    photo1_3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1_3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                photo1_text4 = mDamageMissResponse.getMPhotoValue1_4();
                if (photo1_text4 == null || photo1_text4.isEmpty()) {
                    photo1_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                photo1_text5 = mDamageMissResponse.getMPhotoValue1_5();
                if (photo1_text5 == null || photo1_text5.isEmpty()) {
                    photo1_5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1_5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }

                photo2_text = mDamageMissResponse.getMPhotoValue2();
                if (photo2_text == null || photo2_text.isEmpty()) {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                photo3_text = mDamageMissResponse.getMPhotoValue3();
                if (photo3_text == null || photo3_text.isEmpty()) {
                    photo3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                photo4_text = mDamageMissResponse.getMPhotoValue4();
                if (photo4_text == null || photo4_text.isEmpty()) {
                    photo4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                photo5_text = mDamageMissResponse.getMPhotoValue5();
                if (photo5_text == null || photo5_text.isEmpty()) {
                    photo5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private boolean intiValidationCheck() {
        mDropdownValue1 = conntype_text1;
        mDropdownValue2 = conntype_text2;
        mDropdownValue3 = conntype_text3;
        mDropdownValue4 = conntype_text4;
        mDropdownValue5 = conntype_text5;

        mPhotoValue1 = photo1_text;
        mPhotoValue1_2 = photo1_text2;
        mPhotoValue1_3 = photo1_text3;
        mPhotoValue1_4 = photo1_text4;
        mPhotoValue1_5 = photo1_text5;
        mPhotoValue2 = photo2_text;
        mPhotoValue3 = photo3_text;
        mPhotoValue4 = photo4_text;
        mPhotoValue5 = photo5_text;

        mRemarkValue1 = edtRemarkID1.getText().toString().trim();
        mRemarkValue2 = edtRemarkID2.getText().toString().trim();
        mRemarkValue3 = edtRemarkID3.getText().toString().trim();
        mRemarkValue4 = edtRemarkID4.getText().toString().trim();
        mRemarkValue5 = edtRemarkID5.getText().toString().trim();

        mDamageMissResponse.setMBillNo(billnoN);
        mDamageMissResponse.setMDropdownValue1(mDropdownValue1);
        mDamageMissResponse.setMDropdownValue2(mDropdownValue2);
        mDamageMissResponse.setMDropdownValue3(mDropdownValue3);
        mDamageMissResponse.setMDropdownValue4(mDropdownValue4);
        mDamageMissResponse.setMDropdownValue5(mDropdownValue5);
        mDamageMissResponse.setMRemarkValue1(mRemarkValue1);
        mDamageMissResponse.setMRemarkValue2(mRemarkValue2);
        mDamageMissResponse.setMRemarkValue3(mRemarkValue3);
        mDamageMissResponse.setMRemarkValue4(mRemarkValue4);
        mDamageMissResponse.setMRemarkValue5(mRemarkValue5);
        mDamageMissResponse.setMQuentityValue1(mQuentityValue1);
        mDamageMissResponse.setMQuentityValue2(mQuentityValue2);
        mDamageMissResponse.setMQuentityValue3(mQuentityValue3);
        mDamageMissResponse.setMQuentityValue4(mQuentityValue4);
        mDamageMissResponse.setMQuentityValue5(mQuentityValue5);
        mDamageMissResponse.setMRodioValue1(mRodioValue1);
        mDamageMissResponse.setMRodioValue2(mRodioValue2);
        mDamageMissResponse.setMRodioValue3(mRodioValue3);
        mDamageMissResponse.setMRodioValue4(mRodioValue4);
        mDamageMissResponse.setMRodioValue5(mRodioValue5);
        mDamageMissResponse.setMPhotoValue1(mPhotoValue1);
        mDamageMissResponse.setMPhotoValue2(mPhotoValue2);
        mDamageMissResponse.setMPhotoValue3(mPhotoValue3);
        mDamageMissResponse.setMPhotoValue4(mPhotoValue4);
        mDamageMissResponse.setMPhotoValue5(mPhotoValue5);

        mQuentityValue1 = edtQuintityID1.getText().toString().trim();
        mQuentityValue2 = edtQuintityID2.getText().toString().trim();
        mQuentityValue3 = edtQuintityID3.getText().toString().trim();
        mQuentityValue4 = edtQuintityID4.getText().toString().trim();
        mQuentityValue5 = edtQuintityID5.getText().toString().trim();

        if (mDropdownValue2 == null || mDropdownValue2.equalsIgnoreCase("")) {
            mRodioValue2 = "";
            mRemarkValue2 = "";
            mQuentityValue2 = "";
            mPhotoValue3 = "";
            photo2_text = "";
        }
        if (mDropdownValue3 == null || mDropdownValue3.equalsIgnoreCase("")) {
            mRodioValue3 = "";
            mRemarkValue3 = "";
            mQuentityValue3 = "";
            mPhotoValue3 = "";
            photo3_text = "";

        }
        if (mDropdownValue4 == null || mDropdownValue4.equalsIgnoreCase("")) {
            mRodioValue4 = "";
            mRemarkValue4 = "";
            mQuentityValue4 = "";
            mPhotoValue4 = "";
            photo4_text = "";
        }

        if (mDropdownValue5 == null || mDropdownValue5.equalsIgnoreCase("")) {
            mRodioValue5 = "";
            mRemarkValue5 = "";
            mQuentityValue5 = "";
            mPhotoValue5 = "";
            photo5_text = "";
        }

        if (db.isRecordExist(db.TABLE_DAMAGE_MISS_COMPLAIN, db.KEY_BILL_NO, billnoN)) {
            db.updatedDamageMissData(mDamageMissResponse);
        } else {
            db.insertDamageMissData(mDamageMissResponse);
        }

        try {
            if (mDropdownValue1.equalsIgnoreCase("")) {
                Toast.makeText(context, "Please select drop down value", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mRodioValue1.equalsIgnoreCase("")) {
                Toast.makeText(context, "Please select Complain Type", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mQuentityValue1.equalsIgnoreCase("")) {
                Toast.makeText(context, "Please enter Quantity.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mRemarkValue1.equalsIgnoreCase("")) {
                Toast.makeText(context, "Please write remark.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (photo1_text == null || photo1_text.isEmpty()) {
                Toast.makeText(context, "Please select photo 1.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (photo1_text2 == null || photo1_text2.isEmpty()) {
                Toast.makeText(context, "Please select photo 2.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (photo1_text3 == null || photo1_text3.isEmpty()) {
                Toast.makeText(context, "Please select photo 3.", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private void initClickEventSpinner() {
        spinner_conntype1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index_conntype1 = arg0.getSelectedItemPosition();
                if (spinner_conntype1.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type")) {
                    conntype_text1 = "";
                } else {
                    conntype_text1 = spinner_conntype1.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_conntype2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index_conntype2 = arg0.getSelectedItemPosition();
                if (spinner_conntype2.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type")) {
                    conntype_text2 = "";
                } else {
                    conntype_text2 = spinner_conntype2.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_conntype3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index_conntype3 = arg0.getSelectedItemPosition();
                if (spinner_conntype3.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type")) {
                    conntype_text3 = "";
                } else {
                    conntype_text3 = spinner_conntype3.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_conntype4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index_conntype4 = arg0.getSelectedItemPosition();
                if (spinner_conntype4.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type")) {
                    conntype_text4 = "";
                } else {
                    conntype_text4 = spinner_conntype4.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_conntype5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index_conntype5 = arg0.getSelectedItemPosition();
                if (spinner_conntype5.getSelectedItem().toString().equalsIgnoreCase("Select Connection Type")) {
                    conntype_text5 = "";
                } else {
                    conntype_text5 = spinner_conntype5.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void getConnTypeValue() {
        list_conntype.add("Select Connection Type");
        list_conntype.add("SOLAR PUMP SET");
        list_conntype.add("SOLAR MODULES");
        list_conntype.add("HDPE PIPE");
        list_conntype.add("SOLAR STRUCTURE PARTS");
        list_conntype.add("SOLAR STRUCTURE HARDWARE MATERIAL");
        list_conntype.add("AC CABLE");
        list_conntype.add("DC CABLE");
        list_conntype.add("ROPE");
        list_conntype.add("EARTHING");
        list_conntype.add("SOLAR ACCESSORIES");
        list_conntype.add("DC CIRCUIT BREAKER");
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
                if (intiValidationCheck()) {
                    if (CustomUtility.isInternetOn()) {
                        new SyncDAMAGEMISSData().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        rlvComplainID1.setOnClickListener(view -> rlvContainerCompID1.setVisibility(View.VISIBLE));

        rlvComplainID2.setOnClickListener(view -> rlvContainerCompID2.setVisibility(View.VISIBLE));

        rlvComplainID3.setOnClickListener(view -> rlvContainerCompID3.setVisibility(View.VISIBLE));

        rlvComplainID4.setOnClickListener(view -> rlvContainerCompID4.setVisibility(View.VISIBLE));

        rlvComplainID5.setOnClickListener(view -> rlvContainerCompID5.setVisibility(View.VISIBLE));

        photo1.setOnClickListener(view -> { showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO_1"); });

        photo1_2.setOnClickListener(view -> showConfirmationGallery(DatabaseHelper.KEY_PHOTO1_2, "PHOTO_1_2"));

        photo1_3.setOnClickListener(view -> showConfirmationGallery(DatabaseHelper.KEY_PHOTO1_3, "PHOTO_1_3"));

        photo1_4.setOnClickListener(view -> showConfirmationGallery(DatabaseHelper.KEY_PHOTO1_4, "PHOTO_1_4"));

        photo1_5.setOnClickListener(view -> showConfirmationGallery(DatabaseHelper.KEY_PHOTO1_5, "PHOTO_1_5"));

        photo2.setOnClickListener(view -> { showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO_2"); });

        photo3.setOnClickListener(view -> showConfirmationGallery(DatabaseHelper.KEY_PHOTO3, "PHOTO_3"));

        photo4.setOnClickListener(view -> showConfirmationGallery(DatabaseHelper.KEY_PHOTO4, "PHOTO_4"));

        photo5.setOnClickListener(view -> showConfirmationGallery(DatabaseHelper.KEY_PHOTO5, "PHOTO_5"));
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_damage1:
                if (checked)
                    mRodioValue1 = "Damage";

                break;
            case R.id.radio_miss1:
                if (checked)
                    mRodioValue1 = "Miss";
                break;

            case R.id.radio_damage2:
                if (checked)
                    mRodioValue2 = "Damage";
                break;
            case R.id.radio_miss2:
                if (checked)
                    mRodioValue2 = "Miss";
                break;

            case R.id.radio_damage3:
                if (checked)
                    mRodioValue3 = "Damage";
                break;
            case R.id.radio_miss3:
                if (checked)
                    mRodioValue3 = "Miss";
                break;
            case R.id.radio_damage4:
                if (checked)
                    mRodioValue4 = "Damage";
                break;
            case R.id.radio_miss4:
                if (checked)
                    mRodioValue4 = "Miss";
                break;

            case R.id.radio_damage5:
                if (checked)
                    mRodioValue5 = "Damage";
                break;
            case R.id.radio_miss5:
                if (checked)
                    mRodioValue5 = "Miss";
                break;
        }
    }

    public void showConfirmationGallery(final String keyimage, final String name) {
        final CustomUtility customUtility = new CustomUtility();
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
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
    }

    public void openCamera(String name) {
        if (CameraUtils.checkPermissions(context)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE, WebURL.CUSTOMERID_ID, name);
            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
            }
            Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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
        photo1_flag = false;
        photo1_flag2 = false;
        photo1_flag3 = false;
        photo1_flag4 = false;
        photo1_flag5 = false;
        photo2_flag = false;
        photo2_flag2 = false;
        photo2_flag3 = false;
        photo2_flag4 = false;
        photo2_flag5 = false;
        photo3_flag = false;
        photo3_flag2 = false;
        photo3_flag3 = false;
        photo3_flag4 = false;
        photo3_flag5 = false;
        photo4_flag = false;
        photo4_flag2 = false;
        photo4_flag3 = false;
        photo4_flag4 = false;
        photo4_flag5 = false;
        photo5_flag = false;
        photo5_flag2 = false;
        photo5_flag3 = false;
        photo5_flag4 = false;
        photo5_flag5 = false;
        photo6_flag = false;
        photo7_flag = false;

        switch (key) {
            case DatabaseHelper.KEY_PHOTO1:
                photo1_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO1_2:
                photo1_flag2 = true;
                break;
            case DatabaseHelper.KEY_PHOTO1_3:
                photo1_flag3 = true;
                break;
            case DatabaseHelper.KEY_PHOTO1_4:
                photo1_flag4 = true;
                break;
            case DatabaseHelper.KEY_PHOTO1_5:
                photo1_flag5 = true;
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
            case DatabaseHelper.KEY_PHOTO5:
                photo5_flag = true;
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
            case DatabaseHelper.KEY_PHOTO1_2:
                if (photo1_text2 == null || photo1_text2.isEmpty()) {
                    photo1_2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1_2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_PHOTO1_3:
                if (photo1_text3 == null || photo1_text.isEmpty()) {
                    photo1_3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1_3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_PHOTO1_4:
                if (photo1_text4 == null || photo1_text4.isEmpty()) {
                    photo1_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_PHOTO1_5:
                if (photo1_text5 == null || photo1_text5.isEmpty()) {
                    photo1_5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1_5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
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
                    photo4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_PHOTO5:
                if (photo5_text == null || photo5_text.isEmpty()) {
                    photo5.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo5.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
        }
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            try {
                Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
                int count = bitmap.getByteCount();
                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                byte[] byteArray = byteArrayBitmapStream.toByteArray();
                long size = byteArray.length;
                if (photo1_flag == true) {
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1.jpg");
                    if (file.exists()) {
                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue1 = photo1_text;
                        setIcon(DatabaseHelper.KEY_PHOTO1);
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1", photo1_text);
                    }
                }

                if (photo1_flag2 == true) {
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1_2.jpg");
                    if (file.exists()) {
                        photo1_text2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue1_2 = photo1_text2;
                        setIcon(DatabaseHelper.KEY_PHOTO1_2);
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1_2", photo1_text2);
                    }
                }
                if (photo1_flag3 == true) {
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1_3.jpg");
                    if (file.exists()) {
                        photo1_text3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue1_3 = photo1_text3;
                        setIcon(DatabaseHelper.KEY_PHOTO1_3);
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1_3", photo1_text3);
                    }
                }

                if (photo1_flag4 == true) {
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1_4.jpg");
                    if (file.exists()) {
                        photo1_text4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue1_4 = photo1_text4;
                        setIcon(DatabaseHelper.KEY_PHOTO1_4);
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1_4", photo1_text4);
                    }
                }

                if (photo1_flag5 == true) {
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1_5.jpg");
                    if (file.exists()) {
                        photo1_text5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue1_5 = photo1_text5;
                        setIcon(DatabaseHelper.KEY_PHOTO1_5);
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1_5", photo1_text5);
                    }
                }

                if (photo2_flag == true) {
                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_2.jpg");
                    if (file1.exists()) {
                        photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue2 = photo2_text;
                        setIcon(DatabaseHelper.KEY_PHOTO2);
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_2", photo2_text);
                    }
                }
                if (photo3_flag == true) {
                    File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_3.jpg");
                    if (file2.exists()) {
                        photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue3 = photo3_text;
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_3", photo3_text);
                        setIcon(DatabaseHelper.KEY_PHOTO3);
                    }
                }

                if (photo4_flag == true) {
                    File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_4.jpg");
                    if (file3.exists()) {
                        photo4_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue4 = photo4_text;
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_4", photo4_text);
                        setIcon(DatabaseHelper.KEY_PHOTO4);
                    }
                }

                if (photo5_flag == true) {
                    File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_5.jpg");
                    if (file4.exists()) {
                        photo5_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue5 = photo5_text;
                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_5", photo5_text);
                        Log.e("SIZE5", "&&&&" + photo5_text);
                        setIcon(DatabaseHelper.KEY_PHOTO5);
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
                        options.inSampleSize = 6;
                        options.inJustDecodeBounds = false;
                        try {
                            if (selectedImageUri != null) {
                                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                                int count = bitmap.getByteCount();
                                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                                byte[] byteArray = byteArrayBitmapStream.toByteArray();
                                long size = byteArray.length;
                                if (photo1_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_1.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1.jpg");
                                    if (file.exists()) {
                                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1", photo1_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO1);
                                    }
                                }
                                if (photo1_flag2 == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_1_2.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1_2.jpg");
                                    if (file.exists()) {
                                        photo1_text2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1", photo1_text2);
                                        setIcon(DatabaseHelper.KEY_PHOTO1_2);
                                    }
                                }

                                if (photo1_flag3 == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_1_3.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1_3.jpg");
                                    if (file.exists()) {
                                        photo1_text3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1", photo1_text3);
                                        setIcon(DatabaseHelper.KEY_PHOTO1_3);
                                    }
                                }

                                if (photo1_flag4 == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_1_4.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1_4.jpg");
                                    if (file.exists()) {
                                        photo1_text4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1", photo1_text4);
                                        setIcon(DatabaseHelper.KEY_PHOTO1_4);
                                    }
                                }

                                if (photo1_flag5 == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_1_5.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1_5.jpg");
                                    if (file.exists()) {
                                        photo1_text5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_1", photo1_text5);
                                        setIcon(DatabaseHelper.KEY_PHOTO1_5);
                                    }
                                }

                                if (photo2_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_2.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_2.jpg");
                                    if (file1.exists()) {
                                        photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_2", photo2_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO2);
                                    }
                                }
                                if (photo3_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_3.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_3.jpg");
                                    if (file2.exists()) {
                                        photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_3", photo3_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO3);
                                    }
                                }

                                if (photo4_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_4.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_4.jpg");
                                    if (file3.exists()) {
                                        photo4_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_4", photo4_text);
                                        Log.e("SIZE4", "&&&&" + photo4_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO4);
                                    }
                                }

                                if (photo5_flag == true) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID + "/IMG_PHOTO_5.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_5.jpg");
                                    if (file4.exists()) {
                                        photo5_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, WebURL.CUSTOMERID_ID + "PHOTO_5", photo5_text);
                                        Log.e("SIZE5", "&&&&" + photo5_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO5);
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
                Uri uri = data.getData();
                String uriString = null;
                if (uri != null) {
                    uriString = uri.toString();
                }
                File myFile = new File(uriString);
                Filename = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {

                            Filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            PathHolder = getExternalStorageDirectory() + "/Download/" + Filename;
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Image already saved, Do you want to change it or display?");
        alertDialog.setPositiveButton("Display", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
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
            return null;
        } else {
            String[] projection = {String.valueOf(MediaStore.Images.Media.DATA)};
            Cursor cursor1 = ((Activity) context).getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
            Cursor cursor2 = ((Activity) context).getContentResolver().query(uri, projection, null, null, null);
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
            DatabaseHelper db = new DatabaseHelper(context);
            JSONArray ja_invc_data = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("vbeln", billnoN);
                jsonObj.put("regio", state);
                jsonObj.put("cityc", city);
                jsonObj.put("name", name);
                jsonObj.put("mobile", mobileno);
                jsonObj.put("fkdat", billdate);
                jsonObj.put("address", address);
                jsonObj.put("kunnr", kunnr);//customerID
                jsonObj.put("lifnr", pernr);//userID
                jsonObj.put("categorytyp1", mDropdownValue1);
                jsonObj.put("categorytyp2", mDropdownValue2);
                jsonObj.put("categorytyp3", mDropdownValue3);
                jsonObj.put("categorytyp4", mDropdownValue4);
                jsonObj.put("categorytyp5", mDropdownValue5);
                jsonObj.put("category1", mRodioValue1);
                jsonObj.put("category2", mRodioValue2);
                jsonObj.put("category3", mRodioValue3);
                jsonObj.put("category4", mRodioValue4);
                jsonObj.put("category5", mRodioValue5);
                jsonObj.put("qty1", mQuentityValue1);
                jsonObj.put("qty2", mQuentityValue2);
                jsonObj.put("qty3", mQuentityValue3);
                jsonObj.put("qty4", mQuentityValue4);
                jsonObj.put("qty5", mQuentityValue5);
                jsonObj.put("remark1", mRemarkValue1);
                jsonObj.put("remark2", mRemarkValue2);
                jsonObj.put("remark3", mRemarkValue3);
                jsonObj.put("remark4", mRemarkValue4);
                jsonObj.put("remark5", mRemarkValue5);
                jsonObj.put("PHOTO1", photo1_text);
                jsonObj.put("PHOTO2", photo1_text2);
                jsonObj.put("PHOTO3", photo1_text3);
                jsonObj.put("PHOTO4", photo1_text4);
                jsonObj.put("PHOTO5", photo1_text5);
                jsonObj.put("PHOTO6", photo2_text);
                jsonObj.put("PHOTO7", photo3_text);
                jsonObj.put("PHOTO8", photo4_text);
                jsonObj.put("PHOTO9", photo5_text);
                ja_invc_data.put(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }

            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("create_complaint", String.valueOf(ja_invc_data)));
            System.out.println(param1_invc.toString());
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.DAMAGE_MISS_DATA, param1_invc);
                if (obj2 != "") {
                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_return");
                    JSONArray ja = new JSONArray(obj1);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");
                        if (invc_done.equalsIgnoreCase("Y")) {
                            Message msg = new Message();
                            msg.obj = "Data Submitted Successfully...";
                            mHandler2.sendMessage(msg);
                            db.deleteDAMAGEMISSData(billnoN);
                            deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + billnoN));
                            CustomUtility.setSharedPreference(context, billnoN + "PHOTO_1", "");
                            CustomUtility.setSharedPreference(context, billnoN + "PHOTO_2", "");
                            CustomUtility.setSharedPreference(context, billnoN + "PHOTO_3", "");
                            CustomUtility.setSharedPreference(context, billnoN + "PHOTO_4", "");
                            CustomUtility.setSharedPreference(context, billnoN + "PHOTO_5", "");
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
            onResume();
            progressDialog.dismiss();  // dismiss dialog
        }
    }

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

    android.os.Handler mHandler2 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();
        }
    };
}