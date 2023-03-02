package com.shaktipumplimited.shaktikusum;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapter.Adapter_Installation_list;
import adapter.Adapter_Unload_Installation_list;
import bean.BTResonseData;
import bean.InstallationListBean;
import ch.acra.acra.BuildConfig;
import database.DatabaseHelper;
import debugapp.GlobalValue.NewSolarVFD;
import debugapp.localDB.DatabaseHelperTeacher;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class InstallationList extends AppCompatActivity {
    public String bill_no = "";
    public String gst_bill_no = "";
    public String bill_date = "";
    public String disp_date = "";
    public String kunnr = "";
    public String name = "";
    public String fathname = "";
    public String state = "";
    public String state_txt = "";
    public String district = "";
    public String district_txt = "";
    public String address = "";
    public String contactno = "";
    public String controller = "";
    public String motor = "";
    public String simno = "";
    public String pump = "";
    public String regisno = "";
    public String projectno = "";
    public String loginno = "";
    public String module_qty = "";
    public String sync = "";
    public String CONTACT_NO = "";
    public String simha2 = "";
    public String set_matno = "";
    public String villagetehsil = "";
    public String village = "";
    public String tehsil = "";
    public String rec_no = "";
    public String beneficiary = "";
    Context context;
    Context mContext;
    DatabaseHelper db;

    int vkp = 0;
    String mInstallerMOB = "";
    String mInstallerName = "";

    String RMS_SERVER_DOWN = "";
    String RMS_DEBUG_EXTRN = "";
    String MUserId = "";
    String project_no1 = "";
    private String MEmpType = "null";
    String mAppName = "KUSUM";

    private DatabaseHelperTeacher mDatabaseHelperTeacher;

    String version, device_name, user_id;
    LinearLayout lin1, lin2;
    RecyclerView recyclerView;
    Adapter_Installation_list adapterInstallationList;
    Adapter_Unload_Installation_list adapterInstallationList1;
    List<String> enq_docno = new ArrayList<>();
    EditText editsearch;
    ArrayList<InstallationListBean> installationBeans;
    InstallationListBean installationBean;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(InstallationList.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private LinearLayoutManager layoutManagerSubCategory;
    private ProgressDialog progressDialog;

    List<BTResonseData> mBTResonseDataList;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation_list);
        context = this;
        mContext = this;

        project_no1 = CustomUtility.getSharedPreferences(context, "projectid");
        MUserId = CustomUtility.getSharedPreferences(context, "userid");
        MEmpType = "Vend";

        db = new DatabaseHelper(context);
        mBTResonseDataList = new ArrayList<>();

        progressDialog = new ProgressDialog(context);

        user_id = CustomUtility.getSharedPreferences(context, "userid");


        version = BuildConfig.VERSION_NAME;
        device_name = CustomUtility.getDeviceName();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.emp_list);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);
        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);
        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                try {
                    adapterInstallationList.filter(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
        if (CustomUtility.isInternetOn()) {
            if (db.getcount(db.TABLE_INSTALLATION_LIST)) {
                installationBeans = new ArrayList<InstallationListBean>();
                installationBeans = db.getInstallationListData(user_id);
                Log.e("SIZE", "&&&&" + installationBeans.size());
                if (installationBeans != null && installationBeans.size() > 0) {
                    lin1.setVisibility(View.VISIBLE);
                    lin2.setVisibility(View.GONE);
                    recyclerView.setAdapter(null);
                    Log.e("SIZE", "&&&&" + installationBeans.size());
                    adapterInstallationList = new Adapter_Installation_list(context, installationBeans);
                    layoutManagerSubCategory = new LinearLayoutManager(context);
                    layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManagerSubCategory);
                    recyclerView.setAdapter(adapterInstallationList);
                    adapterInstallationList.notifyDataSetChanged();
                } else {
                    lin1.setVisibility(View.GONE);
                    lin2.setVisibility(View.VISIBLE);
                }
            } else {
                recyclerView.setAdapter(null);
                db.deleteInstallationListData();
                new GetInstallationDataList_Task().execute();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
            installationBeans = new ArrayList<InstallationListBean>();
            installationBeans = db.getInstallationListData(user_id);
            Log.e("SIZE", "&&&&" + installationBeans.size());
            if (installationBeans != null && installationBeans.size() > 0) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
                Log.e("SIZE", "&&&&" + installationBeans.size());
                adapterInstallationList = new Adapter_Installation_list(context, installationBeans);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManagerSubCategory);
                recyclerView.setAdapter(adapterInstallationList);
                adapterInstallationList.notifyDataSetChanged();
            } else {
                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);
            }
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        if (adapterInstallationList != null) {
            adapterInstallationList.notifyDataSetChanged();
        }
        if (CustomUtility.getSharedPreferences(context, "SYNCLIST").equalsIgnoreCase("1")) {
            if (CustomUtility.isInternetOn()) {
                recyclerView.setAdapter(null);
                db.deleteInstallationListData();
                CustomUtility.setSharedPreference(context, "SYNCLIST", "0");
                new GetInstallationDataList_Task().execute();
            } else {
                Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
            }
        } else {
            installationBeans = new ArrayList<InstallationListBean>();
            installationBeans = db.getInstallationListData(user_id);
            Log.e("SIZE", "&&&&" + installationBeans.size());
            if (installationBeans != null && installationBeans.size() > 0) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
                Log.e("SIZE", "&&&&" + installationBeans.size());
                adapterInstallationList = new Adapter_Installation_list(context, installationBeans);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManagerSubCategory);
                recyclerView.setAdapter(adapterInstallationList);
                adapterInstallationList.notifyDataSetChanged();
            } else {
                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_unsync, menu);
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
            case R.id.action_menu_unsync:
                if (CustomUtility.isInternetOn()) {
                    recyclerView.setAdapter(null);
                    db.deleteInstallationListData();
                    NewSolarVFD.CHECK_DATA_UNOLAD = 0;
                    new GetInstallationDataList_Task().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_menu_unloading:
                if (CustomUtility.isInternetOn()) {
                    recyclerView.setAdapter(null);
                    // db.deleteInstallationListData();
                    NewSolarVFD.CHECK_DATA_UNOLAD = 1;
                    new GetInstallationDataList_Unload().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetInstallationDataList_Task extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "", "Please Wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.clear();
            param.add(new BasicNameValuePair("USERID", CustomUtility.getSharedPreferences(context, "userid")));
            param.add(new BasicNameValuePair("PROJECT_NO", CustomUtility.getSharedPreferences(context, "projectid")));
            param.add(new BasicNameValuePair("PROJECT_LOGIN_NO", CustomUtility.getSharedPreferences(context, "loginid")));
            String login_selec = null;
            try {
                login_selec = CustomHttpClient.executeHttpPost1(WebURL.INSTALLATION, param);
                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("installation_data");
                JSONArray ja = new JSONArray(obj1);
                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jo = ja.getJSONObject(j);
                    bill_no = jo.getString("vbeln");
                    gst_bill_no = jo.getString("gst_inv_no");
                    bill_date = jo.getString("fkdat");
                    disp_date = jo.getString("dispatch_date");
                    kunnr = jo.getString("kunnr");

                    name = jo.getString("name");
                    fathname = jo.getString("name");

                    tehsil = jo.getString("ort02");
                    village = jo.getString("ort02");

                    state = jo.getString("regio");
                    district = jo.getString("cityc");

                    state_txt = jo.getString("regio_txt");
                    district_txt = jo.getString("cityc_txt");

                    address = jo.getString("address");
                    contactno = jo.getString("mobile");

                    controller = jo.getString("controller_sernr");
                    motor = jo.getString("motor_sernr");

                    simno = jo.getString("simno");
                    beneficiary = jo.getString("beneficiary");
                    pump = jo.getString("pump_sernr");

                    regisno = jo.getString("regisno");
                    projectno = jo.getString("project_no");
                    loginno = jo.getString("process_no");
                    module_qty = jo.getString("module_qty");
                    sync = jo.getString("sync");
                    simha2 = jo.getString("simha2");
                    set_matno = jo.getString("set_matno");
                    CONTACT_NO = jo.getString("contact_no");

                    installationBean = new InstallationListBean(bill_no,
                            CustomUtility.getSharedPreferences(context, "userid"),
                            name,
                            fathname,
                            bill_no,
                            kunnr,
                            gst_bill_no,
                            bill_date,
                            disp_date,
                            state,
                            state_txt,
                            district,
                            district_txt,
                            tehsil,
                            village,
                            contactno,
                            controller,
                            motor,
                            pump,
                            regisno,
                            projectno,
                            loginno,
                            module_qty,
                            address,
                            simno,
                            beneficiary,
                            set_matno,
                            simha2,
                            sync,
                            CONTACT_NO);
                    if (db.isRecordExist(db.TABLE_INSTALLATION_LIST, db.KEY_ENQ_DOC, bill_no)) {
                        db.updateInstallationListData(bill_no, installationBean);
                    } else {
                        db.insertInstallationListData(bill_no, installationBean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            return login_selec;
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String result) {
            // write display tracks logic here
            //onResume();
            installationBeans = new ArrayList<InstallationListBean>();
            installationBeans = db.getInstallationListData(user_id);
            Log.e("SIZE", "&&&&" + installationBeans.size());
            if (installationBeans != null && installationBeans.size() > 0) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
                Log.e("SIZE", "&&&&" + installationBeans.size());
                adapterInstallationList = new Adapter_Installation_list(context, installationBeans);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManagerSubCategory);
                recyclerView.setAdapter(adapterInstallationList);
                adapterInstallationList.notifyDataSetChanged();
            } else {
                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);
            }
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    private class GetInstallationDataList_Unload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            installationBeans = new ArrayList<InstallationListBean>();
            //progress = newworkorder ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Please Wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("USERID", CustomUtility.getSharedPreferences(context, "userid")));
            param.add(new BasicNameValuePair("PROJECT_NO", CustomUtility.getSharedPreferences(context, "projectid")));
            param.add(new BasicNameValuePair("PROJECT_LOGIN_NO", CustomUtility.getSharedPreferences(context, "loginid")));
            String login_selec = null;
            try {
                login_selec = CustomHttpClient.executeHttpPost1(WebURL.INSTALLATION_UNLOAD1, param);
                System.out.println("unload List =>" + WebURL.INSTALLATION_UNLOAD1);
                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("installation_data");
                System.out.println("unload List =>" + obj1);
                JSONArray ja = new JSONArray(obj1);
                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jo = ja.getJSONObject(j);
                    bill_no = jo.getString("vbeln");
                    gst_bill_no = jo.getString("gst_inv_no");
                    bill_date = jo.getString("fkdat");
                    disp_date = jo.getString("dispatch_date");
                    kunnr = jo.getString("kunnr");

                    name = jo.getString("name");
                    fathname = jo.getString("name");

                    tehsil = jo.getString("ort02");
                    village = jo.getString("ort02");

                    state = jo.getString("regio");
                    district = jo.getString("cityc");

                    state_txt = jo.getString("regio_txt");
                    district_txt = jo.getString("cityc_txt");

                    address = jo.getString("address");
                    contactno = jo.getString("mobile");

                    controller = jo.getString("controller_sernr");
                    motor = jo.getString("motor_sernr");

                    simno = jo.getString("simno");
                    beneficiary = jo.getString("beneficiary");
                    pump = jo.getString("pump_sernr");

                    regisno = jo.getString("regisno");
                    projectno = jo.getString("project_no");
                    loginno = jo.getString("process_no");
                    module_qty = jo.getString("module_qty");
                    sync = jo.getString("sync");
                    simha2 = jo.getString("simha2");
                    set_matno = jo.getString("set_matno");
                    CONTACT_NO = jo.getString("contact_no");

                    installationBean = new InstallationListBean(bill_no,
                            CustomUtility.getSharedPreferences(context, "userid"),
                            name,
                            fathname,
                            bill_no,
                            kunnr,
                            gst_bill_no,
                            bill_date,
                            disp_date,
                            state,
                            state_txt,
                            district,
                            district_txt,
                            tehsil,
                            village,
                            contactno,
                            controller,
                            motor,
                            pump,
                            regisno, projectno, loginno, module_qty,
                            address,
                            simno,
                            beneficiary,
                            set_matno,
                            simha2,
                            sync,
                            CONTACT_NO);

                    InstallationListBean installationBean = new InstallationListBean();
                    installationBean.setPernr(CustomUtility.getSharedPreferences(context, "userid"));
                    installationBean.setEnqdoc(bill_no);
                    installationBean.setBillno(bill_no);
                    installationBean.setKunnr(kunnr);
                    installationBean.setGstbillno(gst_bill_no);
                    installationBean.setBilldate(bill_date);
                    installationBean.setCustomer_name(name);
                    installationBean.setFather_name(fathname);
                    installationBean.setState(state);
                    installationBean.setStatetxt(state_txt);
                    installationBean.setCity(district);
                    installationBean.setCitytxt(district_txt);
                    installationBean.setTehsil(tehsil);
                    installationBean.setVillage(village);
                    installationBean.setContact_no(contactno);
                    installationBean.setController(controller);
                    installationBean.setPump(pump);
                    installationBean.setSimno(simno);
                    installationBean.setRegisno(regisno);
                    installationBean.setProjectno(projectno);
                    installationBean.setLoginno(loginno);
                    installationBean.setModuleqty(module_qty);
                    installationBean.setBeneficiary(beneficiary);
                    installationBean.setMotor(motor);
                    installationBean.setAddress(address);
                    installationBean.setDispdate(disp_date);
                    installationBean.setSync(sync);
                    installationBean.setSet_matno(set_matno);
                    installationBean.setSimha2(simha2);
                    installationBean.setCUS_CONTACT_NO(CONTACT_NO);

                    installationBeans.add(installationBean);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (installationBeans != null && installationBeans.size() > 0) {
                            lin1.setVisibility(View.VISIBLE);
                            lin2.setVisibility(View.GONE);
                            recyclerView.setAdapter(null);
                            Log.e("SIZE", "&&&&" + installationBeans.size());
                            adapterInstallationList = new Adapter_Installation_list(context, installationBeans);
                            layoutManagerSubCategory = new LinearLayoutManager(context);
                            layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManagerSubCategory);
                            recyclerView.setAdapter(adapterInstallationList);
                            adapterInstallationList.notifyDataSetChanged();
                        } else {
                            lin1.setVisibility(View.GONE);
                            lin2.setVisibility(View.VISIBLE);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            return login_selec;
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String result) {
            try {
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
