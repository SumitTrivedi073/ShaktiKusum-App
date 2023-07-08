package activity;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import bean.InstallationOfflineBean;
import bean.SubmitOfflineDataInput;
import database.DatabaseHelper;
import utility.CameraUtils;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class InstallationDetailOfflineActivity extends BaseActivity {

    private ProgressDialog progressDialog;
    Context mContext;
    DatabaseHelper db;
    String billno;
    TextView tvGstBillNo, Customer_name, fathers_name, Installation_place, mobile_no, tvContactNo, tvAddress,
            tvStateCode, tvStateName, tvCityCode, tvCityName, tvDistrict, tvStreet, tvCustomerCode,
            tvDispatchDate, tvPumpSmo, lrdate, tvControllerSmo, tvController, Billing_no, bill_date,
            tvMotorSmo, tvSimha, tvSimNo, tvSimMobileNo, sanction_amount, tvSync, sp_beneficiary_share,
            tvProjectNo, tvProcessNo, tvRegistrationNo, module_serial_no, module_total_plate_watt,
            sp_stand_install, sm_Model_Details_one, s_no_one, tvLeft, save;

    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusumUnload";
    private boolean photo1;
    private String imageStoragePath, photo1_text;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    String mImageFolderName = "/SKAPP/UNLOAD/";
    String type = "UNLOAD/";
    private android.os.Handler mHandler;
    InstallationOfflineBean installationOfflineBean;
    double inst_latitude_double, inst_longitude_double;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation_detail_offline);
        mContext = this;
        WebURL.GALLERY_DIRECTORY_NAME_COMMON = "ShaktiKusumUnload";
        db = new DatabaseHelper(mContext);
        Bundle extras = getIntent().getExtras();
        billno = extras.getString("bill_no");
        getGpsLocation();
        inItViews();
        setData();

        mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String mString = (String) msg.obj;
                Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
            }
        };

        tvLeft.setOnClickListener(v -> {
            showConfirmationGallery(DatabaseHelper.KEY_DELIVERY_PHOTO1, "PHOTO_1");
        });

        save.setOnClickListener(v -> {
            if (validationCheck()) {
                saveDeliveredData();
                if (CustomUtility.isInternetOn(getApplicationContext())) {
                    submitOfflineSubmittedData();
                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inItViews() {
        tvGstBillNo = findViewById(R.id.tvGstBillNo);
        Customer_name = findViewById(R.id.Customer_name);
        fathers_name = findViewById(R.id.fathers_name);
        Installation_place = findViewById(R.id.Installation_place);
        mobile_no = findViewById(R.id.mobile_no);
        tvContactNo = findViewById(R.id.tvContactNo);
        tvAddress = findViewById(R.id.tvAddress);
        tvStateCode = findViewById(R.id.tvStateCode);
        tvStateName = findViewById(R.id.tvStateName);
        tvCityCode = findViewById(R.id.tvCityCode);
        tvCityName = findViewById(R.id.tvCityName);
        tvDistrict = findViewById(R.id.tvDistrict);
        tvStreet = findViewById(R.id.tvStreet);
        tvCustomerCode = findViewById(R.id.tvCustomerCode);
        tvDispatchDate = findViewById(R.id.tvDispatchDate);
        tvPumpSmo = findViewById(R.id.tvPumpSmo);

        lrdate = findViewById(R.id.lrdate);
        tvControllerSmo = findViewById(R.id.tvControllerSmo);
        tvController = findViewById(R.id.tvController);
        Billing_no = findViewById(R.id.Billing_no);
        bill_date = findViewById(R.id.bill_date);
        tvMotorSmo = findViewById(R.id.tvMotorSmo);
        tvSimha = findViewById(R.id.tvSimha);
        save = findViewById(R.id.save);
        tvSimNo = findViewById(R.id.tvSimNo);
        tvSimMobileNo = findViewById(R.id.tvSimMobileNo);
        sanction_amount = findViewById(R.id.sanction_amount);
        tvSync = findViewById(R.id.tvSync);
        sp_beneficiary_share = findViewById(R.id.sp_beneficiary_share);
        tvProjectNo = findViewById(R.id.tvProjectNo);
        tvProcessNo = findViewById(R.id.tvProcessNo);
        tvRegistrationNo = findViewById(R.id.tvRegistrationNo);
        module_serial_no = findViewById(R.id.module_serial_no);

        module_total_plate_watt = findViewById(R.id.module_total_plate_watt);
        sp_stand_install = findViewById(R.id.sp_stand_install);
        sm_Model_Details_one = findViewById(R.id.sm_Model_Details_one);
        s_no_one = findViewById(R.id.s_no_one);
        tvLeft = findViewById(R.id.tvLeft);
    }

    private void setData() {
        installationOfflineBean = db.getInstallationOfflineData(billno);
        WebURL.CUSTOMERID_ID = installationOfflineBean.getUserId();
        Billing_no.setText(installationOfflineBean.getVbeln()); //bill no
        tvGstBillNo.setText(installationOfflineBean.getGstInvNo());// Gst No
        bill_date.setText(installationOfflineBean.getFkdat());//Billdate
        tvDispatchDate.setText(installationOfflineBean.getDispatchDate());// Dispatch date
        tvCustomerCode.setText(installationOfflineBean.getKunnr());// Customer code
        Customer_name.setText(installationOfflineBean.getName()); // Customer name
        tvStateCode.setText(installationOfflineBean.getRegio());//state code
        mobile_no.setText(installationOfflineBean.getMobile());
        tvCityCode.setText(installationOfflineBean.getCityc());// city code
        tvCityName.setText(installationOfflineBean.getOrt02());// city name
        tvStreet.setText(installationOfflineBean.getStras()); //street name
        tvAddress.setText(installationOfflineBean.getAddress());// address
        tvStateName.setText(installationOfflineBean.getRegioTxt());//State name
        tvDistrict.setText(installationOfflineBean.getCitycTxt());// District
        tvPumpSmo.setText(installationOfflineBean.getPumpSernr());//Pump Smo
        tvControllerSmo.setText(installationOfflineBean.getControllerSernr());// Controller Smo
        tvController.setText(installationOfflineBean.getControllerMatno());// Controller
        tvMotorSmo.setText(installationOfflineBean.getMotorSernr());//Motor Smo
        tvSimha.setText(installationOfflineBean.getSimha2());// Mat. Group
        tvSimNo.setText(installationOfflineBean.getSimno());//Sim no
        tvSimMobileNo.setText(installationOfflineBean.getSimmob());
        sp_beneficiary_share.setText(installationOfflineBean.getBeneficiary());
        tvProjectNo.setText(installationOfflineBean.getProjectNo());
        tvProcessNo.setText(installationOfflineBean.getProcessNo());
        tvRegistrationNo.setText(installationOfflineBean.getRegisno());// Registration No
        module_serial_no.setText(installationOfflineBean.getModuleQty());//Qty
        tvSync.setText(installationOfflineBean.getSync());//
        tvContactNo.setText(installationOfflineBean.getContactNo());//

        if (db.isRecordExist(DatabaseHelper.TABLE_OFFLINE_SUBMITTED_LIST, DatabaseHelper.KEY_OFFLINE_BILL_NO, installationOfflineBean.getVbeln())) {
            SubmitOfflineDataInput submitOfflineDataInput = db.getInstallationOfflineSubmittedData(installationOfflineBean.getVbeln());
            photo1_text = submitOfflineDataInput.getOffPhoto();
            if (photo1_text == null || photo1_text.isEmpty()) {
                tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
            } else {
                tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
            }
        }
    }

    private void showConfirmationGallery(final String keyimage, final String name) {
        final CustomUtility customUtility = new CustomUtility();
        final CharSequence[] items = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            boolean result = CustomUtility.checkPermission(mContext);
            if (items[item].equals("Take Photo")) {
                if (result) {
                    openCamera(name);
                    photo1 = true;
                }
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void openCamera(String name) {
        if (CameraUtils.checkPermissions(mContext)) {
            File file = new File(ImageManager.getMediaFilePath(type, name, WebURL.CUSTOMERID_ID));
            imageStoragePath = file.getAbsolutePath();
            Log.e("PATH", "&&&" + imageStoragePath);
            Intent i = new Intent(mContext, CameraActivity2.class);
            i.putExtra("lat", String.valueOf(inst_latitude_double));
            i.putExtra("lng", String.valueOf(inst_longitude_double));
            i.putExtra("inst_id", installationOfflineBean.getUserId());
            i.putExtra("cust_name", installationOfflineBean.getName());
            i.putExtra("bill_no", installationOfflineBean.getVbeln());
            i.putExtra("type", type);
            i.putExtra("name", name);
            i.putExtra("isOffline", true);
            startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    private boolean validationCheck() {
        boolean returnValue = false;
        if (null == photo1_text || photo1_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Click Image", Toast.LENGTH_SHORT).show();
        } else {
            returnValue = true;
        }
        return returnValue;
    }

    private void saveDeliveredData() {
        SubmitOfflineDataInput deliveryDeliveredInput = new SubmitOfflineDataInput();
        deliveryDeliveredInput.setBillNo(installationOfflineBean.getVbeln());
        deliveryDeliveredInput.setBeneficiary(installationOfflineBean.getBeneficiary());
        deliveryDeliveredInput.setCustomerName(installationOfflineBean.getName());
        deliveryDeliveredInput.setProjectNo(installationOfflineBean.getProjectNo());
        deliveryDeliveredInput.setUserId(installationOfflineBean.getUserId());
        deliveryDeliveredInput.setRegisno(installationOfflineBean.getRegisno());
        deliveryDeliveredInput.setOffPhoto(photo1_text);
        if (db.isRecordExist(DatabaseHelper.TABLE_OFFLINE_SUBMITTED_LIST, DatabaseHelper.KEY_OFFLINE_BILL_NO, installationOfflineBean.getVbeln())) {
            db.insertInstallationOfflineSubmittedData(deliveryDeliveredInput, false, "");
        } else {
            db.insertInstallationOfflineSubmittedData(deliveryDeliveredInput, true, installationOfflineBean.getVbeln());
        }
    }

    private void submitOfflineSubmittedData() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        SubmitOfflineDataInput submitOfflineDataInput = db.getInstallationOfflineSubmittedData(installationOfflineBean.getVbeln());

        progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn(getApplicationContext())) {
                        JSONArray ja_invc_data = new JSONArray();
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("vbeln", submitOfflineDataInput.getBillNo());
                            jsonObj.put("beneficiary", submitOfflineDataInput.getBeneficiary());
                            jsonObj.put("customer_name", submitOfflineDataInput.getCustomerName());
                            jsonObj.put("project_no", submitOfflineDataInput.getProjectNo());
                            jsonObj.put("userid", submitOfflineDataInput.getUserId());
                            jsonObj.put("regisno", submitOfflineDataInput.getRegisno());
                            jsonObj.put("offphoto", submitOfflineDataInput.getOffPhoto());
                            ja_invc_data.put(jsonObj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
                        param1_invc.add(new BasicNameValuePair("final", String.valueOf(ja_invc_data)));
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.INSTALLATION_OFFLINE_DATA_SUBMIT, param1_invc);
                        if (!obj.equalsIgnoreCase("")) {
                            JSONObject jsonObject = new JSONObject(obj.trim());
                            boolean status = jsonObject.getBoolean("status");
                            if (status || !status) {
                                db.deleteOfflineSubmittedData(installationOfflineBean.getVbeln());
                                CameraUtils.deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + CameraUtils.DELIVERED_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID));
                                photo1_text = null;
                                runOnUiThread(() -> tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0));
                            }
                            Message msg = new Message();
                            msg.obj = jsonObject.optString("message");
                            mHandler.sendMessage(msg);
                        } else {
                            Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Message msg = new Message();
                        msg.obj = "No Internet Connection";
                        mHandler.sendMessage(msg);
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            try {
                Bitmap bitmap = CameraUtils.optimizeBitmap(4, imageStoragePath);
                int count = bitmap.getByteCount();
                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                byte[] byteArray = byteArrayBitmapStream.toByteArray();
                long size = byteArray.length;

                if (photo1 == true) {
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1.jpg");
                    if (file.exists()) {
                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        if (photo1_text == null || photo1_text.isEmpty()) {
                            tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                        } else {
                            tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
