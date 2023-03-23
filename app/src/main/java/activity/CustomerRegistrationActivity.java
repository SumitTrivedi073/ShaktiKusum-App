package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bean.ImageModel;
import bean.RegistrationBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

import static android.os.Environment.getExternalStoragePublicDirectory;

import static debugapp.GlobalValue.Constant.RegistrationImage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shaktipumplimited.shaktikusum.R;


public class CustomerRegistrationActivity extends AppCompatActivity {


    Context context;
    String versionName = "0.0", spinner_state_txt, spinner_district_txt, spinner_stateid, spinner_districtid, acc_type_text;
    String pernr, login_no, project_no, latitude, longitude, customer_name, fat_name, vill, teh, state, city, statetxt, citytxt, contact_no, acc_type, bankname, branchname, ifsccode, amt, aadhar, bank_acc_no, current_date, date, pdf, sync;
    EditText cust_name, father_name, tehsil, village, contact, aadhar_no, bank_name, bank_acc, branch_name, amount, ifsc_code, doc_date;
    Spinner spinner_state, spinner_district, spinner_acc_typ;
    String photo1_text, photo2_text, photo3_text, photo4_text, photo5_text, photo6_text, photo7_text, photo8_text, photo9_text, photo10_text, photo11_text, photo12_text;
    TextView btn_submit;
    String enq_docno = null;
    int index, index1;
    DatabaseHelper dataHelper;
    double inst_latitude_double,
            inst_longitude_double;
    List<String> list = null;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";
    // for listing all states
    ArrayList<String> listState = new ArrayList<String>();
    // for listing all cities
    ArrayList<String> listCity = new ArrayList<String>();
    SimpleDateFormat simpleDateFormat;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(CustomerRegistrationActivity.this, mString, Toast.LENGTH_LONG).show();

        }
    };
    private ProgressDialog progressDialog;
    
    List<ImageModel>imageList;

   /* public static void deleteFiles(String path) {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_registration);
        context = this;

        dataHelper = new DatabaseHelper(context);

        list = new ArrayList<String>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //versionName = BuildConfig.VERSION_NAME;
        versionName = WebURL.APP_VERSION_CODE;
        getGpsLocation();
        getlayout();
        getAccTypeValue();


        enq_docno = createDocNo();

        CustomUtility.setSharedPreference(context, "enqdocid", enq_docno);


        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date = simpleDateFormat.format(new Date());

        doc_date.setText(current_date);

        listState.clear();
        listState = dataHelper.getStateDistrictList(DatabaseHelper.KEY_STATE_TEXT, null);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(context, R.layout.spinner_item_center, listState);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(R.layout.spinner_item_center);

        // attaching data adapter to spinner
        spinner_state.setAdapter(dataAdapter1);


        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                spinner_state_txt = spinner_state.getSelectedItem().toString();

                listCity.clear();
                listCity = dataHelper.getStateDistrictList(DatabaseHelper.KEY_DISTRICT_TEXT, spinner_state_txt);
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item_center, listCity);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

                // attaching data adapter to spinner
                spinner_district.setAdapter(dataAdapter);

                if (!spinner_state_txt.equalsIgnoreCase("Select State") && !TextUtils.isEmpty(spinner_state_txt)) {
                    spinner_stateid = dataHelper.getStateDistrictValue(DatabaseHelper.KEY_STATE, spinner_state_txt);

                    CustomUtility.setSharedPreference(context, "statetext", spinner_state_txt);
                    CustomUtility.setSharedPreference(context, "stateid", spinner_stateid);

                    Log.e("ID", "&&&" + spinner_stateid);

                    spinner_district.setSelection(0);

                    spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                            index1 = arg0.getSelectedItemPosition();
                            spinner_district_txt = spinner_district.getSelectedItem().toString();
                            if (!spinner_district_txt.equalsIgnoreCase("Select District") && !TextUtils.isEmpty(spinner_district_txt)) {

                                spinner_districtid = dataHelper.getStateDistrictValue(DatabaseHelper.KEY_DISTRICT, spinner_district_txt);

                                CustomUtility.setSharedPreference(context, "districtid", spinner_districtid);
                                CustomUtility.setSharedPreference(context, "districttext", spinner_district_txt);

                                Log.e("ID1", "&&&" + spinner_districtid);
                            } else {
                                Toast.makeText(context, "Please Select District.", Toast.LENGTH_SHORT).show();
                                CustomUtility.setSharedPreference(context, "districtid", "");
                                CustomUtility.setSharedPreference(context, "districttext", "");
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });

                } else {
                    Toast.makeText(context, "Please Select State.", Toast.LENGTH_SHORT).show();
                    CustomUtility.setSharedPreference(context, "stateid", "");
                    CustomUtility.setSharedPreference(context, "statetext", "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_acc_typ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                acc_type_text = spinner_acc_typ.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinner_acc_typ.setPrompt("Select Account Type");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_center, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

        // attaching data adapter to spinner
        spinner_acc_typ.setAdapter(dataAdapter);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CustomUtility.isInternetOn()) {

                    saveData();
                } else {
                    progressDialog = ProgressDialog.show(CustomerRegistrationActivity.this, "", "Please Connect to Internet, Data Save in Offline Mode ..please wait !");
                    saveData();


                }
            }
        });


    }

    public void getlayout() {
        cust_name = findViewById(R.id.cust_name);
        father_name = findViewById(R.id.father_name);
        tehsil = findViewById(R.id.tehsil);
        village = findViewById(R.id.village);
        contact = findViewById(R.id.contact);
        aadhar_no = findViewById(R.id.aadhar_no);
        bank_name = findViewById(R.id.bank_name);
        bank_acc = findViewById(R.id.bank_acc);
        spinner_acc_typ = findViewById(R.id.spinner_acc_typ);
        branch_name = findViewById(R.id.branch_name);
        ifsc_code = findViewById(R.id.ifsc_code);
        amount = findViewById(R.id.amount);
        doc_date = findViewById(R.id.doc_date);

        spinner_state = findViewById(R.id.spinner_state);
        spinner_district = findViewById(R.id.spinner_district);

        spinner_state.setPrompt("Select State");
        spinner_district.setPrompt("Select District");

        btn_submit = findViewById(R.id.btn_submit);

    }

    public void getAccTypeValue() {
        list.add("Select Account Type");
        list.add("Current");
        list.add("Saving");
    }

    public String createDocNo() {

        Calendar cal = Calendar.getInstance();
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);

        String day_str = null;
        String month_str = null;
        String hour_str = null;
        String second_str = null;
        String minute_str = null;


        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();

        String month = dateFormat.format(date);


        String userid = CustomUtility.getSharedPreferences(context, "userid");

        if (day < 10) {
            day_str = "" + 0 + day;
        } else {
            day_str = "" + day;
        }


        if (minute < 10) {
            minute_str = "" + 0 + minute;
        } else {
            minute_str = "" + minute;
        }


        if (second < 10) {
            second_str = "" + 0 + second;
        } else {
            second_str = "" + second;
        }


        if (hour < 10) {
            hour_str = "" + 0 + hour;
        } else {
            hour_str = "" + hour;
        }


        if (month.length() == 1) {
            month_str = "" + 0 + month;
        } else {
            month_str = month;
        }

        String rfq_docno = "" + userid + day_str + month_str + year + hour_str + minute_str + second_str;
        return rfq_docno;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SKAPP/" + CustomUtility.getSharedPreferences(context, "enqdocid")));
        CustomUtility.setSharedPreference(context, "enqdocid", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attachment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.act_comp_attach_image:
           /*     if(SDK_INT >= 30) {
                    if (!Environment.isExternalStorageManager()) {
                        Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Settings", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            Uri uri = Uri.parse("package:" + shakti.shaktipumps.shaktikusum.BuildConfig.APPLICATION_ID);
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                            startActivity(intent);
                                        } catch (Exception ex) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                            startActivity(intent);
                                        }
                                    }
                                })
                                .show();
                    }
                }
                else {*/
                    Intent intent = new Intent(getApplicationContext(), RegReportImageActivity.class);
                    intent.putExtra("reg_id", CustomUtility.getSharedPreferences(context, "enqdocid"));
                    intent.putExtra("cust_nm", cust_name.getText().toString());
                    startActivity(intent);

                    return true;
               // }


        }
        return super.onOptionsItemSelected(item);
    }

    public void saveData() {

        latitude = String.valueOf(inst_latitude_double);
        longitude = String.valueOf(inst_longitude_double);

        pernr = CustomUtility.getSharedPreferences(context, "userid");
        project_no = CustomUtility.getSharedPreferences(context, "projectid");
        login_no = CustomUtility.getSharedPreferences(context, "loginid");
        customer_name = cust_name.getText().toString();
        fat_name = father_name.getText().toString();
        state = CustomUtility.getSharedPreferences(context, "stateid");
        city = CustomUtility.getSharedPreferences(context, "districtid");
        statetxt = spinner_state.getSelectedItem().toString();
        citytxt = spinner_district.getSelectedItem().toString();
        teh = tehsil.getText().toString();
        vill = village.getText().toString();
        contact_no = contact.getText().toString();
        aadhar = aadhar_no.getText().toString();
        bankname = bank_name.getText().toString();
        bank_acc_no = bank_acc.getText().toString();
        acc_type = spinner_acc_typ.getSelectedItem().toString();
        branchname = branch_name.getText().toString();
        ifsccode = ifsc_code.getText().toString();
        amt = amount.getText().toString();
        pdf = "";

        date = doc_date.getText().toString();

        enq_docno = CustomUtility.getSharedPreferences(context, "enqdocid");

        if (customer_name != null && !customer_name.equals("")) {
            if (fat_name != null && !fat_name.equals("")) {
                if (state != null && !state.equals("") && !state.equalsIgnoreCase("Select State")) {
                    if (city != null && !city.equals("") && !city.equalsIgnoreCase("Select City")) {
                        if (teh != null && !teh.equals("")) {
                            if (!date.equals("")) {
                                if (vill != null && !vill.equals("")) {
                                    if (contact_no != null && !contact_no.equals("")) {
                                        if (aadhar != null && !aadhar.equals("")) {
                                            if (bankname != null && !bankname.equals("")) {
                                                if (bank_acc_no != null && !bank_acc_no.equals("")) {
                                                    if (acc_type != null && !acc_type.equals("") && !acc_type.equalsIgnoreCase("Select Account Type.")) {
                                                        if (branchname != null && !branchname.equals("")) {
                                                            if (ifsccode != null && !ifsccode.equals("")) {
                                                                if (amt != null && !amt.equals("")) {
                                                                    if (CustomUtility.isInternetOn()) {


                                                                        RegistrationBean registrationBean = new RegistrationBean(enq_docno, pernr, project_no,
                                                                                login_no, date, latitude, longitude, customer_name, fat_name, state, statetxt, city,
                                                                                citytxt, teh, vill, contact_no, aadhar, bankname, bank_acc_no, acc_type, branchname, ifsccode,
                                                                                amt, pdf, "1"
                                                                        );

                                                                        if (dataHelper.isRecordExist(dataHelper.TABLE_REGISTRATION, dataHelper.KEY_ENQ_DOC, enq_docno)) {
                                                                            dataHelper.updateRegistrationData(enq_docno, registrationBean);
                                                                        } else {
                                                                            dataHelper.insertRegistrationData(enq_docno, registrationBean);
                                                                        }

                                                                        if (CustomUtility.getSharedPreferences(context, "SYNC" + enq_docno).equalsIgnoreCase("1")) {
                                                                          
                                                                             if(imageList!=null && imageList.size()>0) {
                                                                                 new RegistrationData().execute();
                                                                             }else {
                                                                                 CustomUtility.showToast(CustomerRegistrationActivity.this, getResources().getString(R.string.selectRegistrationImage));
                                                                             }
                                                                        } else {
                                                                            if (progressDialog != null)
                                                                                progressDialog.dismiss();
                                                                            Toast.makeText(context, "Please Select Photos", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    } else {
                                                                        if (CustomUtility.getSharedPreferences(context, "SYNC" + enq_docno).equalsIgnoreCase("1")) {
                                                                            RegistrationBean registrationBean = new RegistrationBean(enq_docno, pernr, project_no,
                                                                                    login_no, date, latitude, longitude, customer_name, fat_name, state, statetxt, city,
                                                                                    citytxt, teh, vill, contact_no, aadhar, bankname, bank_acc_no, acc_type, branchname, ifsccode,
                                                                                    amt, pdf, "1"
                                                                            );


                                                                            if (dataHelper.isRecordExist(dataHelper.TABLE_REGISTRATION, dataHelper.KEY_ENQ_DOC, enq_docno)) {
                                                                                dataHelper.updateRegistrationData(enq_docno, registrationBean);
                                                                            } else {
                                                                                dataHelper.insertRegistrationData(enq_docno, registrationBean);
                                                                            }

                                                                            if (progressDialog != null)
                                                                                progressDialog.dismiss();
                                                                            Toast.makeText(context, "Data Save to Offline Mode.", Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        } else {
                                                                            if (progressDialog != null)
                                                                                progressDialog.dismiss();
                                                                            Toast.makeText(context, "Please Select Photos", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                } else {
                                                                    Toast.makeText(context, "Please Enter Amount.", Toast.LENGTH_SHORT).show();
                                                                    if (progressDialog != null)
                                                                        progressDialog.dismiss();
                                                                }
                                                            } else {
                                                                Toast.makeText(context, "Please Enter IFSC Code.", Toast.LENGTH_SHORT).show();
                                                                if (progressDialog != null)
                                                                    progressDialog.dismiss();
                                                            }
                                                        } else {
                                                            Toast.makeText(context, "Please Enter Branch Name.", Toast.LENGTH_SHORT).show();
                                                            if (progressDialog != null)
                                                                progressDialog.dismiss();
                                                        }
                                                    } else {
                                                        Toast.makeText(context, "Please Select Account Type", Toast.LENGTH_SHORT).show();
                                                        if (progressDialog != null)
                                                            progressDialog.dismiss();
                                                    }

                                                } else {
                                                    Toast.makeText(context, "Please Enter Bank Account No.", Toast.LENGTH_SHORT).show();
                                                    if (progressDialog != null)
                                                        progressDialog.dismiss();
                                                }

                                            } else {
                                                Toast.makeText(context, "Please Enter Bank Name.", Toast.LENGTH_SHORT).show();
                                                if (progressDialog != null)
                                                    progressDialog.dismiss();
                                            }

                                        } else {
                                            Toast.makeText(context, "Please Enter Aadhar No.", Toast.LENGTH_SHORT).show();
                                            if (progressDialog != null)
                                                progressDialog.dismiss();
                                        }

                                    } else {
                                        Toast.makeText(context, "Please Enter Contact No.", Toast.LENGTH_SHORT).show();
                                        if (progressDialog != null)
                                            progressDialog.dismiss();
                                    }

                                } else {
                                    Toast.makeText(context, "Please Enter Village.", Toast.LENGTH_SHORT).show();
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                }

                            } else {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(context, "Please Enter Tehsil", Toast.LENGTH_SHORT).show();
                            if (progressDialog != null)
                                progressDialog.dismiss();
                        }

                    } else {
                        Toast.makeText(context, "Please Select District.", Toast.LENGTH_SHORT).show();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                } else {
                    Toast.makeText(context, "Please Select State.", Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }

            } else {
                Toast.makeText(context, "Please Enter Father Name.", Toast.LENGTH_SHORT).show();
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        } else {
            Toast.makeText(context, "Please Enter Customer Name", Toast.LENGTH_SHORT).show();
            if (progressDialog != null)
                progressDialog.dismiss();
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        enq_docno = CustomUtility.getSharedPreferences(context, "enqdocid");
        retriveRegistrationImages();

    }

    private void retriveRegistrationImages() {
        imageList = new ArrayList<>();
        String json = CustomUtility.getSharedPreferences(CustomerRegistrationActivity.this, RegistrationImage);
        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<ImageModel>>() {
        }.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        imageList = new Gson().fromJson(json, type);

    }

    private class RegistrationData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog = ProgressDialog.show(CustomerRegistrationActivity.this, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;

            DatabaseHelper db = new DatabaseHelper(context);

            RegistrationBean param_invc = new RegistrationBean();

            enq_docno = CustomUtility.getSharedPreferences(context, "enqdocid");

            param_invc = db.getRegistrationData(pernr, enq_docno);


            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                String date_s = param_invc.getDate();


                SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");

                Date date = dt.parse(date_s);
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");

                enq_docno = CustomUtility.getSharedPreferences(context, "enqdocid");


                jsonObj.put("userid", param_invc.getPernr());
                jsonObj.put("mdocno", CustomUtility.getSharedPreferences(context, "enqdocid"));
                jsonObj.put("project_no", param_invc.getProject_no());
                jsonObj.put("project_login_no", param_invc.getLogin_no());
                jsonObj.put("regis_date", dt1.format(date));
                jsonObj.put("lat", param_invc.getLat());
                jsonObj.put("lng", param_invc.getLng());
                jsonObj.put("customer_name", param_invc.getCustomer_name());
                jsonObj.put("father_name", param_invc.getFather_name());
                jsonObj.put("state", param_invc.getState());
                jsonObj.put("city", param_invc.getCity());

                Log.e("STATE", "&&&&" + param_invc.getState());
                Log.e("CITY", "&&&&" + param_invc.getCity());
                jsonObj.put("tehsil", param_invc.getTehsil());
                jsonObj.put("village", param_invc.getVillage());
                jsonObj.put("contact_no", param_invc.getContact_no());
                jsonObj.put("aadhar_no", param_invc.getAadhar_no());
                jsonObj.put("bank_name", param_invc.getBank_name());
                jsonObj.put("BANK_ACC_NO", param_invc.getBank_acc_no());
                jsonObj.put("ACCOUNT_TYPE", param_invc.getAccount_type());
                jsonObj.put("BRANCH_NAME", param_invc.getBranch_name());
                jsonObj.put("IFSC_CODE", param_invc.getIfsc_code());
                jsonObj.put("AMOUNT", param_invc.getAmount());
                jsonObj.put("PDF", param_invc.getPdf());

                if(imageList!=null && imageList.size()>0) {
                    if (imageList.get(1).isImageSelected()) {
                        jsonObj.put("PHOTO1", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(1).getImagePath()));
                    } else if (imageList.get(2).isImageSelected()) {
                        jsonObj.put("PHOTO2", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(2).getImagePath()));
                    } else if (imageList.get(3).isImageSelected()) {
                        jsonObj.put("PHOTO3", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(3).getImagePath()));
                    } else if (imageList.get(4).isImageSelected()) {
                        jsonObj.put("PHOTO4", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(4).getImagePath()));
                    } else if (imageList.get(5).isImageSelected()) {
                        jsonObj.put("PHOTO5", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(5).getImagePath()));
                    } else if (imageList.get(6).isImageSelected()) {
                        jsonObj.put("PHOTO6", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(6).getImagePath()));
                    } else if (imageList.get(7).isImageSelected()) {
                        jsonObj.put("PHOTO7", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(7).getImagePath()));
                    } else if (imageList.get(8).isImageSelected()) {
                        jsonObj.put("PHOTO8", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(8).getImagePath()));
                    } else if (imageList.get(9).isImageSelected()) {
                        jsonObj.put("PHOTO9", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(9).getImagePath()));
                    } else if (imageList.get(10).isImageSelected()) {
                        jsonObj.put("PHOTO10", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(10).getImagePath()));
                    } else if (imageList.get(11).isImageSelected()) {
                        jsonObj.put("PHOTO11", CustomUtility.getBase64FromBitmap(CustomerRegistrationActivity.this, imageList.get(11).getImagePath()));
                    }

                }

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("registration", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.REGISTRATION_DATA, param1_invc);

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

                            Log.e("DOCNO", "&&&&" + enq_docno);

                            db.deleteRegistrationData(enq_docno);
                            deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SKAPP/REG/" + enq_docno));
                            CustomUtility.setSharedPreference(context, "enqdocid", "");

                            progressDialog.dismiss();
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {

                            Message msg = new Message();
                            msg.obj = "Data Not Submitted, Please try After Sometime.";
                            mHandler.sendMessage(msg);
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
