package activity;

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

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

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

public class InstallationListOfflineActivity extends BaseActivity {
    private final String bill_no = "";
    private final String gst_bill_no = "";
    private final String bill_date = "";
    private final String disp_date = "";
    private final String kunnr = "";
    private final String name = "";
    private final String fathname = "";
    private final String state = "";
    private final String state_txt = "";
    private final String district = "";
    private final String district_txt = "";
    private final String address = "";
    private final String contactno = "";
    private final String controller = "";
    private final String motor = "";
    private final String simno = "";
    private final String pump = "";
    private final String regisno = "";
    private final String projectno = "";
    private final String loginno = "";
    private final String module_qty = "";
    private final String sync = "";
    private final String CONTACT_NO = "";
    private final String simha2 = "";
    private final String set_matno = "";
    private final String villagetehsil = "";
    private final String village = "";
    private final String tehsil = "";
    private final String rec_no = "";
    private final String beneficiary = "";
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
    private final List<String> enq_docno = new ArrayList<>();
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
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.emp_list);

        editsearch = findViewById(R.id.search);
        lin1 = findViewById(R.id.lin1);
        lin2 = findViewById(R.id.lin2);

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

//        if (CustomUtility.isInternetOn(getApplicationContext())) {
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
                    if (CustomUtility.isInternetOn(getApplicationContext())) {
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

                    Gson gson = new Gson();
                    installationOfflineBean = gson.fromJson(String.valueOf(jo), InstallationOfflineBean.class);
                    installationOfflineBean.setUserID(CustomUtility.getSharedPreferences(mContext, "userid"));
                    if (db.isRecordExist(DatabaseHelper.TABLE_INSTALLATION_LIST, DatabaseHelper.KEY_ENQ_DOC, bill_no)) {
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
