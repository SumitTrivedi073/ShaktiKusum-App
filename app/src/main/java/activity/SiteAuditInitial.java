package activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bean.AuditSiteBean;
import bean.ImageModel;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class SiteAuditInitial extends BaseActivity {

    Context context;
    DatabaseHelper db;
    List<ImageModel> imageList = new ArrayList<>();
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";
    String billno = "";
    String billdate = "";
    String name = "";
    String state_id = "";
    String city_id = "";
    String address = "";
    String projectno = "";
    String regisno = "";
    String auddate = "";
    String contactno = "";
    String beneficiary ="";
    AuditSiteBean auditSiteBean;
    EditText foud_rea_txt,stru_rea_txt,drvcb_txt,laer_txt,wrk_txt,siteaudit_date;

    String pernr,project_no, found,
            found_remark,struc,stru_remark,drv_mount,drv_mount_remark,la_earth,la_earth_remark,wrkmn_qlty,wrkmn_qlty_remark;

    int selectedId1;
    int selectedId2;
    int selectedId3;
    int selectedId4;
    int selectedId5;

    Float site_rating;


    RadioGroup radioGroup1,radioGroup2,radioGroup3,radioGroup4,radioGroup5;
    RadioButton fou_ok,fou_notok,stru_ok,stru_notok,drvcb_ok,drvcb_notok,laer_ok,laer_notok,wrk_ok,wrk_notok;
    RadioButton radioButton1,radioButton2,radioButton3,radioButton4,radioButton5;

    android.os.Handler mHandler2 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(SiteAuditInitial.this, mString, Toast.LENGTH_LONG).show();


        }
    };
    private Toolbar mToolbar;
    TextView save;
    ImageView inst_location = null;
    RatingBar siterating;

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
        setContentView(R.layout.activity_site_audit);
        context = this;

        db = new DatabaseHelper(context);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {

                billno     = null;
                billdate   = null;
                name       = null;
                state_id  = null;
                city_id   = null;
                address    = null;
                regisno    = null;
                projectno  = null;
                contactno  = null;
                beneficiary = null;

            } else {

                billno     = extras.getString("bill_no");
                billdate   = extras.getString("bill_date");
                name       = extras.getString("name");
                state_id  = extras.getString("state");
                city_id   = extras.getString("city");
                address    = extras.getString("address");
                regisno    = extras.getString("regisno");
                projectno  = extras.getString("projectno");
                contactno  = extras.getString("contact");
                beneficiary = extras.getString("benficiary");

            }
        } else {

            billno     = (String) savedInstanceState.getSerializable("bill_no");
            billdate   = (String) savedInstanceState.getSerializable("bill_date");
            name       = (String) savedInstanceState.getSerializable("name");
            state_id = (String) savedInstanceState.getSerializable("state");
            city_id   = (String) savedInstanceState.getSerializable("city");
            address    = (String) savedInstanceState.getSerializable("address");
            regisno    = (String) savedInstanceState.getSerializable("regisno");
            projectno  = (String) savedInstanceState.getSerializable("projectno");
            contactno  = (String) savedInstanceState.getSerializable("contact");
            beneficiary = (String) savedInstanceState.getSerializable("benficiary");
        }


        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Audit Form");

        getLayout();

        setData();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();
                // find which radioButton is checked by id
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 301) {

                String year = data.getStringExtra("year");
                String month = data.getStringExtra("month");
                String date = data.getStringExtra("date");
                String finaldate2 = year + "-" + month + "-" + date;
                finaldate2 = CustomUtility.formateDate(finaldate2);
                siteaudit_date.setText(finaldate2);
            }

        }
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
        int id_temp = item.getItemId();

        switch (id_temp) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.act_comp_attach_image:

                Intent intent = new Intent(getApplicationContext(), SiteAuditImageActivity.class);
                intent.putExtra("billno", billno);
                intent.putExtra("custnm", name);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveData1();

    }

    public void saveData1() {

        Toast.makeText(context, "Your Data is Save to Offline Mode...", Toast.LENGTH_SHORT).show();

        //GET DATA
        getData();

        AuditSiteBean auditSiteBean = new AuditSiteBean(pernr,
                project_no,
                billno,
                billdate,
                auddate,
                name,
                contactno,
                state_id,
                city_id,
                address,
                regisno,
                found,
                struc,
                drv_mount,
                la_earth,
                wrkmn_qlty,
                site_rating,
                found_remark,
                stru_remark,
                drv_mount_remark,
                la_earth_remark,
                wrkmn_qlty_remark
        );

        if (db.isRecordExist(DatabaseHelper.TABLE_AUDIT_PUMP_DATA, DatabaseHelper.KEY_BILL_NO, billno)) {
            db.updateAuditData(billno, auditSiteBean);
        } else {
            db.insertAuditData(billno, auditSiteBean);
        }

    }

    public void saveData() {

        if (CustomUtility.isInternetOn(getApplicationContext())) {
            //GET DATA
            getData();

            AuditSiteBean auditSiteBean = new AuditSiteBean(pernr,
                    project_no,
                    billno,
                    billdate,
                    auddate,
                    name,
                    contactno,
                    state_id,
                    city_id,
                    address,
                    regisno,
                    found,
                    struc,
                    drv_mount,
                    la_earth,
                    wrkmn_qlty,
                    site_rating,
                    found_remark,
                    stru_remark,
                    drv_mount_remark,
                    la_earth_remark,
                    wrkmn_qlty_remark
            );

            if (db.isRecordExist(DatabaseHelper.TABLE_AUDIT_PUMP_DATA, DatabaseHelper.KEY_BILL_NO, billno)) {
                db.updateAuditData(billno, auditSiteBean);
            } else {
                db.insertAuditData(billno, auditSiteBean);
            }

            if(selectedId1 != 0) {

                if(selectedId2 != 0) {

                    if(selectedId3 != 0) {

                        if(selectedId4 != 0) {

                            if(selectedId5 != 0) {

                                if(site_rating != 0.0) {

                                    if (CustomUtility.getSharedPreferences(context, "AUDSYNC" + billno).equalsIgnoreCase("1")) {
                                        new SyncAuditData().execute();
                                    } else {
                                        Toast.makeText(context, "Please Select Photos", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(context, "Please Select Site rating out of 5.", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(context, "Please Select Status Workmanship and Work Quality.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Please Select Status LA and Earthing.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Please Select Status Drive Mounting and Cable Dressing.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Please Select Status Structure Assembly.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(context, "Please Select Status Foundation.", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(context, "Please Connect to Internet, Your Data is Save to Offline Mode...", Toast.LENGTH_SHORT).show();

            //GET DATA
            getData();

            AuditSiteBean auditSiteBean = new AuditSiteBean(pernr,
                    project_no,
                    billno,
                    billdate,
                    auddate,
                    name,
                    contactno,
                    state_id,
                    city_id,
                    address,
                    regisno,
                    found,
                    struc,
                    drv_mount,
                    la_earth,
                    wrkmn_qlty,
                    site_rating,
                    found_remark,
                    stru_remark,
                    drv_mount_remark,
                    la_earth_remark,
                    wrkmn_qlty_remark
            );

            if (db.isRecordExist(DatabaseHelper.TABLE_AUDIT_PUMP_DATA, DatabaseHelper.KEY_BILL_NO, billno)) {
                db.updateAuditData(billno, auditSiteBean);
            } else {
                db.insertAuditData(billno, auditSiteBean);
            }

        }
    }

    public void getData() {

        pernr = CustomUtility.getSharedPreferences(context, "userid");
        project_no = CustomUtility.getSharedPreferences(context, "projectid");

        selectedId1 = radioGroup1.getCheckedRadioButtonId();
        selectedId2 = radioGroup2.getCheckedRadioButtonId();
        selectedId3 = radioGroup3.getCheckedRadioButtonId();
        selectedId4 = radioGroup4.getCheckedRadioButtonId();
        selectedId5 = radioGroup5.getCheckedRadioButtonId();

        found_remark = foud_rea_txt.getText().toString();
        stru_remark = stru_rea_txt.getText().toString();
        drv_mount_remark = drvcb_txt.getText().toString();
        la_earth_remark = laer_txt.getText().toString();
        wrkmn_qlty_remark = wrk_txt.getText().toString();

        site_rating = siterating.getRating(); // get rating number from a rating bar

        auddate = siteaudit_date.getText().toString();

        // find the radiobutton by returned id
        radioButton1 = findViewById(selectedId1);
        radioButton2 = findViewById(selectedId2);
        radioButton3 = findViewById(selectedId3);
        radioButton4 = findViewById(selectedId4);
        radioButton5 = findViewById(selectedId5);

        if(radioButton1 != null)
        {
            found = radioButton1.getText().toString();
        }

        if(radioButton2 != null) {
            struc = radioButton2.getText().toString();
        }
        if(radioButton3 != null) {
            drv_mount = radioButton3.getText().toString();
        }
        if(radioButton4 != null) {
            la_earth = radioButton4.getText().toString();
        }
        if(radioButton5 != null) {
            wrkmn_qlty = radioButton5.getText().toString();
        }


    }

    public void getLayout() {

        save = findViewById(R.id.save);

        siterating = findViewById(R.id.ratingBar);

        siteaudit_date = findViewById(R.id.siteaudit_date);

        foud_rea_txt = findViewById(R.id.foud_rea_txt);
        stru_rea_txt = findViewById(R.id.stru_rea_txt);
        drvcb_txt = findViewById(R.id.drvcb_txt);
        laer_txt = findViewById(R.id.laer_txt);
        wrk_txt = findViewById(R.id.wrk_txt);


        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup3 = findViewById(R.id.radioGroup3);
        radioGroup4 = findViewById(R.id.radioGroup4);
        radioGroup5 = findViewById(R.id.radioGroup5);

      /*  fou_ok = (RadioButton) findViewById(R.id.fou_ok);
        fou_notok = (RadioButton) findViewById(R.id.fou_notok);
        stru_ok = (RadioButton) findViewById(R.id.stru_ok);
        stru_notok = (RadioButton) findViewById(R.id.stru_notok);
        drvcb_ok = (RadioButton) findViewById(R.id.drvcb_ok);
        drvcb_notok = (RadioButton) findViewById(R.id.drvcb_notok);
        laer_ok = (RadioButton) findViewById(R.id.laer_ok);
        laer_notok = (RadioButton) findViewById(R.id.laer_notok);
        wrk_ok = (RadioButton) findViewById(R.id.wrk_ok);
        wrk_notok = (RadioButton) findViewById(R.id.wrk_notok);*/


    }

    public void setData() {

        auditSiteBean = new AuditSiteBean(pernr, project_no, billno, billdate, auddate, name, contactno, state_id, city_id, address, regisno, found, struc, drv_mount, la_earth, wrkmn_qlty, site_rating, found_remark, stru_remark, drv_mount_remark, la_earth_remark, wrkmn_qlty_remark);
        auditSiteBean = db.getAuditData(CustomUtility.getSharedPreferences(context, "userid"), billno);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String current_date = simpleDateFormat.format(new Date());
        siteaudit_date.setText(current_date);


        if(auditSiteBean.getFound() != null) {
            if (!auditSiteBean.getFound().equalsIgnoreCase("")) {
                if (auditSiteBean.getFound().equalsIgnoreCase("OK")) {
                    radioGroup1.check(R.id.fou_ok);
                } else {
                    radioGroup1.check(R.id.fou_notok);
                }
            }
        }

        if(auditSiteBean.getStruc_assem() != null) {
            if (!auditSiteBean.getStruc_assem().equalsIgnoreCase("")) {
                if (auditSiteBean.getStruc_assem().equalsIgnoreCase("OK")) {
                    radioGroup2.check(R.id.stru_ok);
                } else {
                    radioGroup2.check(R.id.stru_notok);
                }
            }
        }

        if(auditSiteBean.getDrv_mount() != null) {
            if (!auditSiteBean.getDrv_mount().equalsIgnoreCase("")) {

                if (auditSiteBean.getDrv_mount().equalsIgnoreCase("OK")) {
                    radioGroup3.check(R.id.drvcb_ok);
                } else {
                    radioGroup3.check(R.id.drvcb_notok);
                }
            }
        }

        if(auditSiteBean.getLa_earthing() != null) {
            if (!auditSiteBean.getLa_earthing().equalsIgnoreCase("")) {
                if (auditSiteBean.getLa_earthing().equalsIgnoreCase("OK")) {
                    radioGroup4.check(R.id.laer_ok);
                } else {
                    radioGroup4.check(R.id.laer_notok);
                }
            }
        }

        if(auditSiteBean.getWrk_quality() != null) {
            if (!auditSiteBean.getWrk_quality().equalsIgnoreCase("")) {
                if (auditSiteBean.getWrk_quality().equalsIgnoreCase("OK")) {
                    radioGroup5.check(R.id.wrk_ok);
                } else {
                    radioGroup5.check(R.id.wrk_notok);
                }
            }
        }

        if(!TextUtils.isEmpty(auditSiteBean.getFound_rmk()) && auditSiteBean.getFound_rmk() != null)
        {
            foud_rea_txt.setText(auditSiteBean.getFound_rmk());
        }

        if(!TextUtils.isEmpty(auditSiteBean.getStruc_assem_rmk()) && auditSiteBean.getStruc_assem_rmk() != null)
        {
            stru_rea_txt.setText(auditSiteBean.getStruc_assem_rmk());
        }

        if(!TextUtils.isEmpty(auditSiteBean.getLa_earthing_rmk()) && auditSiteBean.getLa_earthing_rmk() != null)
        {
            laer_txt.setText(auditSiteBean.getLa_earthing_rmk());
        }

        if(!TextUtils.isEmpty(auditSiteBean.getDrv_mount_rmk()) && auditSiteBean.getDrv_mount_rmk() != null)
        {
            drvcb_txt.setText(auditSiteBean.getDrv_mount_rmk());
        }

        if(!TextUtils.isEmpty(auditSiteBean.getWrk_quality_rmk()) && auditSiteBean.getWrk_quality_rmk() != null)
        {
            wrk_txt.setText(auditSiteBean.getWrk_quality_rmk());
        }

        if(auditSiteBean.getSite_art() != null)
        {
            siterating.setRating(auditSiteBean.getSite_art());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        retriveArrayList();
    }

    private void retriveArrayList() {
      /*  imageList = new ArrayList<>();
        String json = CustomUtility.getSharedPreferences(InstallationInitial.this, InstallationImage);
        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<ImageModel>>() {
        }.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        imageList = new Gson().fromJson(json, type);*/
        imageList = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(this);

        List<ImageModel> siteAuditImages = db.getAllAuditSiteImages();
        Log.e("installationImages===>",""+siteAuditImages.size());
        for (int i = 0; i < siteAuditImages.size(); i++) {

            ImageModel imageModel = new ImageModel();
            imageModel.setName(siteAuditImages.get(i).getName());
            imageModel.setImagePath(siteAuditImages.get(i).getImagePath());
            imageModel.setImageSelected(true);
            imageList.add(imageModel);
        }


    }

    private class SyncAuditData extends AsyncTask<String, String, String> {

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

            AuditSiteBean param_invc = new AuditSiteBean(pernr, project_no, billno, billdate, auddate, name, contactno, state_id, city_id, address, regisno, found, struc, drv_mount, la_earth, wrkmn_qlty, site_rating, found_remark, stru_remark, drv_mount_remark, la_earth_remark, wrkmn_qlty_remark);

            param_invc = db.getAuditData(pernr, billno);

            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                String date_s = param_invc.getAud_date();
                String date_s1 = param_invc.getBill_date();


                SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");

                Date date = dt.parse(date_s);
                Date date1 = dt.parse(date_s1);
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");

                jsonObj.put("vbeln", param_invc.getInst_bill_no());
                jsonObj.put("project_no", param_invc.getProject_no());
                jsonObj.put("regisno", param_invc.getRegis_no());
                jsonObj.put("userid", param_invc.getPernr());
                jsonObj.put("bill_dt", dt1.format(date1));
                jsonObj.put("aud_dt", dt1.format(date));
                jsonObj.put("customer_name", param_invc.getName());
                jsonObj.put("state", param_invc.getState_ins_id());
                jsonObj.put("city", param_invc.getDistrict_ins_id());
                jsonObj.put("address", param_invc.getAddress_ins());
                jsonObj.put("contactno", param_invc.getMobile_no());
                jsonObj.put("foundation", param_invc.getFound());
                jsonObj.put("found_remark", param_invc.getFound_rmk());
                jsonObj.put("stru_assem", param_invc.getStruc_assem());
                jsonObj.put("stru_remark", param_invc.getStruc_assem_rmk());
                jsonObj.put("drv_mount", param_invc.getDrv_mount());
                jsonObj.put("drv_mount_remark", param_invc.getDrv_mount_rmk());
                jsonObj.put("la_earth", param_invc.getLa_earthing());
                jsonObj.put("la_earth_remark", param_invc.getLa_earthing_rmk());
                jsonObj.put("wrkmn_qlty", param_invc.getWrk_quality());
                jsonObj.put("wrkmn_qlty_remark", param_invc.getWrk_quality_rmk());
                jsonObj.put("site_rating", param_invc.getSite_art());
                jsonObj.put("beneficiary", beneficiary);

                if (imageList.size() > 0) {

                    if (imageList.get(0).isImageSelected()) {
                        jsonObj.put("PHOTO1", CustomUtility.getBase64FromBitmap(SiteAuditInitial.this, imageList.get(0).getImagePath()));
                    }
                    if (1 < imageList.size() && imageList.get(1).isImageSelected()) {
                        jsonObj.put("PHOTO2", CustomUtility.getBase64FromBitmap(SiteAuditInitial.this, imageList.get(1).getImagePath()));
                    }
                    if (2 < imageList.size() && imageList.get(2).isImageSelected()) {
                        jsonObj.put("PHOTO3", CustomUtility.getBase64FromBitmap(SiteAuditInitial.this, imageList.get(2).getImagePath()));
                    }
                    if (3 < imageList.size() && imageList.get(3).isImageSelected()) {
                        jsonObj.put("PHOTO4", CustomUtility.getBase64FromBitmap(SiteAuditInitial.this, imageList.get(3).getImagePath()));
                    }

                }
                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("site_audit_data", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc);

            System.out.println(param1_invc);

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.AUDIT_SITES, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {

                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_return");


                    JSONArray ja = new JSONArray(obj1);


                    Log.e("OUTPUT2", "&&&&" + ja);

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);

                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");


                        if (invc_done.equalsIgnoreCase("Y")) {

                            Message msg = new Message();
                            msg.obj = "Data Submitted Successfully...";
                            mHandler2.sendMessage(msg);

                            Log.e("DOCNO", "&&&&" + billno);
                            db.deleteAuditData(billno);
                            db.deleteAuditSiteListData1(billno);
                            db.deleteAuditImages(billno);
                           // deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SKAPP/AUD/" + billno));

                            /*CustomUtility.setSharedPreference(context, billno + "PHOTO_1", "");
                            CustomUtility.setSharedPreference(context, billno + "PHOTO_2", "");
                            CustomUtility.setSharedPreference(context, billno + "PHOTO_3", "");
                            CustomUtility.setSharedPreference(context, billno + "PHOTO_4", "");*/


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

}