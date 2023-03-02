package com.shaktipumplimited.shaktikusum;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import adapter.InstallationOfflineAdapter;
import bean.BTResonseData;
import bean.InstallationOfflineBean;
import bean.SubmitOfflineDataInput;
import database.DatabaseHelper;
import utility.CameraUtils;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class InstallationListOfflineActivity extends AppCompatActivity {
    private String bill_no = "";
    private String gst_bill_no = "";
    private String bill_date = "";
    private String disp_date = "";
    private String kunnr = "";
    private String name = "";
    private String fathname = "";
    private String state = "";
    private String state_txt = "";
    private String district = "";
    private String district_txt = "";
    private String address = "";
    private String contactno = "";
    private String controller = "";
    private String motor = "";
    private String simno = "";
    private String pump = "";
    private String regisno = "";
    private String projectno = "";
    private String loginno = "";
    private String module_qty = "";
    private String sync = "";
    private String CONTACT_NO = "";
    private String simha2 = "";
    private String set_matno = "";
    private String villagetehsil = "";
    private String village = "";
    private String tehsil = "";
    private String rec_no = "";
    private String beneficiary = "";
    private Context mContext;
    private DatabaseHelper db;
//    private String MUserId = "";
//    private String project_no1 = "";
//    private String MEmpType = "null";
//    private String mAppName = "KUSUM";
    private String user_id;
    private LinearLayout lin1, lin2;
    private RecyclerView recyclerView;
    private InstallationOfflineAdapter adapterInstallationList;
    private List<String> enq_docno = new ArrayList<>();
    private EditText editsearch;
    private ArrayList<InstallationOfflineBean> installationOfflineBeanList;
    private InstallationOfflineBean installationOfflineBean;
    private Toolbar mToolbar;
    private LinearLayoutManager layoutManagerSubCategory;
    private ProgressDialog progressDialog;
    List<BTResonseData> mBTResonseDataList;
    String mImageFolderName = "/SKAPP/UNLOAD/";

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation_list);
        mContext = this;

        db = new DatabaseHelper(mContext);
        mBTResonseDataList = new ArrayList<>();
        progressDialog = new ProgressDialog(mContext);
        user_id = CustomUtility.getSharedPreferences(mContext, "userid");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.emp_list);

        editsearch = (EditText) findViewById(R.id.search);
        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);

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
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });
        submitOfflineSubmittedData();

//        if (CustomUtility.isInternetOn()) {
//            if (db.getcount(db.TABLE_INSTALLATION_OFFLINE_LIST)) {
//                installationOfflineBeanList = new ArrayList<InstallationOfflineBean>();
//                installationOfflineBeanList = db.getInstallationOfflineListData(user_id);
//                Log.e("SIZE", "&&&&" + installationOfflineBeanList.size());
//                if (installationOfflineBeanList != null && installationOfflineBeanList.size() > 0) {
//                    lin1.setVisibility(View.VISIBLE);
//                    lin2.setVisibility(View.GONE);
//                    recyclerView.setAdapter(null);
//                    Log.e("SIZE", "&&&&" + installationOfflineBeanList.size());
//                    adapterInstallationList = new InstallationOfflineAdapter(mContext, installationOfflineBeanList);
//                    layoutManagerSubCategory = new LinearLayoutManager(mContext);
//                    layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
//                    recyclerView.setLayoutManager(layoutManagerSubCategory);
//                    recyclerView.setAdapter(adapterInstallationList);
//                    adapterInstallationList.notifyDataSetChanged();
//                } else {
//                    lin1.setVisibility(View.GONE);
//                    lin2.setVisibility(View.VISIBLE);
//                }
//            } else {
//                recyclerView.setAdapter(null);
//                db.deleteInstallationOfflineListData();
//                new GetInstallationOfflineDataList_Task().execute();
//            }
//        } else {
//            Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
//            installationOfflineBeanList = new ArrayList<InstallationOfflineBean>();
//            installationOfflineBeanList = db.getInstallationOfflineListData(user_id);
//            Log.e("SIZE", "&&&&" + installationOfflineBeanList.size());
//            if (installationOfflineBeanList != null && installationOfflineBeanList.size() > 0) {
//                lin1.setVisibility(View.VISIBLE);
//                lin2.setVisibility(View.GONE);
//                recyclerView.setAdapter(null);
//                Log.e("SIZE", "&&&&" + installationOfflineBeanList.size());
//                adapterInstallationList = new InstallationOfflineAdapter(mContext, installationOfflineBeanList);
//                layoutManagerSubCategory = new LinearLayoutManager(mContext);
//                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
//                recyclerView.setLayoutManager(layoutManagerSubCategory);
//                recyclerView.setAdapter(adapterInstallationList);
//                adapterInstallationList.notifyDataSetChanged();
//            } else {
//                lin1.setVisibility(View.GONE);
//                lin2.setVisibility(View.VISIBLE);
//            }
//        }
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_unsync, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitOfflineSubmittedData() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        DatabaseHelper dataHelper = new DatabaseHelper(mContext);
//        int count = 0;
        ArrayList<SubmitOfflineDataInput> submitOfflineDataInputLst = dataHelper.getInstallationOfflineSubmittedListData();

        progressDialog = ProgressDialog.show(this, "", "Sync Data on Server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        for (int i = 0; i < submitOfflineDataInputLst.size(); i++) {
                            JSONArray ja_invc_data = new JSONArray();
                            JSONObject jsonObj = new JSONObject();
                            try {
                                jsonObj.put("vbeln", submitOfflineDataInputLst.get(i).getBillNo());
                                jsonObj.put("beneficiary", submitOfflineDataInputLst.get(i).getBeneficiary());
                                jsonObj.put("customer_name", submitOfflineDataInputLst.get(i).getCustomerName());
                                jsonObj.put("project_no", submitOfflineDataInputLst.get(i).getProjectNo());
                                jsonObj.put("userid", submitOfflineDataInputLst.get(i).getUserId());
                                jsonObj.put("regisno", submitOfflineDataInputLst.get(i).getRegisno());
                                jsonObj.put("offphoto", submitOfflineDataInputLst.get(i).getOffPhoto());
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
                                    db.deleteOfflineSubmittedData(submitOfflineDataInputLst.get(i).getBillNo());
                                    CameraUtils.deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + CameraUtils.DELIVERED_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID));
                                }
                            }
                        }
                    }
                    callGetAssignedDeliveryListApi();
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    callGetAssignedDeliveryListApi();
                }
            }
        }.start();
    }

    private void callGetAssignedDeliveryListApi() {
        runOnUiThread(() -> {
            try {
                new GetInstallationOfflineDataList_Task().execute();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private class GetInstallationOfflineDataList_Task extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(mContext, "", "Please Wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("USERID", CustomUtility.getSharedPreferences(mContext, "userid")));
            param.add(new BasicNameValuePair("PROJECT_NO", CustomUtility.getSharedPreferences(mContext, "projectid")));
            param.add(new BasicNameValuePair("PROJECT_LOGIN_NO", CustomUtility.getSharedPreferences(mContext, "loginid")));
            String login_selec = null;
            try {
                login_selec = CustomHttpClient.executeHttpPost1(WebURL.INSTALLATION_DATA_OFFLINE, param);
                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("installation_data");
                JSONArray ja = new JSONArray(obj1);
                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jo = ja.getJSONObject(j);
//                    bill_no = jo.getString("vbeln");
//                    gst_bill_no = jo.getString("gst_inv_no");
//                    bill_date = jo.getString("fkdat");
//                    disp_date = jo.getString("dispatch_date");
//                    kunnr = jo.getString("kunnr");
//                    name = jo.getString("name");
//                    fathname = name ;
//                    tehsil = jo.getString("ort02");
//                    village = tehsil;
//                    state = jo.getString("regio");
//                    district = jo.getString("cityc");
//                    state_txt = jo.getString("regio_txt");
//                    district_txt = jo.getString("cityc_txt");
//                    address = jo.getString("address");
//                    contactno = jo.getString("mobile");
//                    controller = jo.getString("controller_sernr");
//                    motor = jo.getString("motor_sernr");
//                    simno = jo.getString("simno");
////                    simmob
//                    beneficiary = jo.getString("beneficiary");
//                    pump = jo.getString("pump_sernr");
//                    regisno = jo.getString("regisno");
//                    projectno = jo.getString("project_no");
//                    loginno = jo.getString("process_no");
//                    module_qty = jo.getString("module_qty");
//                    sync = jo.getString("sync");
//                    simha2 = jo.getString("simha2");
//                    set_matno = jo.getString("set_matno");
//                    CONTACT_NO = jo.getString("contact_no");
                    Gson gson = new Gson();
                    installationOfflineBean = gson.fromJson(String.valueOf(jo), InstallationOfflineBean.class);
                    installationOfflineBean.setUserID(CustomUtility.getSharedPreferences(mContext, "userid"));
                    if (db.isRecordExist(db.TABLE_INSTALLATION_LIST, db.KEY_ENQ_DOC, bill_no)) {
                        db.updateInstallationOfflineListData(bill_no, installationOfflineBean);
                    } else {
                        db.insertInstallationOfflineListData(installationOfflineBean);
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
            installationOfflineBeanList = new ArrayList<InstallationOfflineBean>();
            installationOfflineBeanList = db.getInstallationOfflineListData(user_id);
            if (installationOfflineBeanList != null && installationOfflineBeanList.size() > 0) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
                adapterInstallationList = new InstallationOfflineAdapter(mContext, installationOfflineBeanList);
                layoutManagerSubCategory = new LinearLayoutManager(mContext);
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
}
