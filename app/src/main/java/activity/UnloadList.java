package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import adapter.Adapter_Unload_Installation_list;
import bean.InstallationListBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class UnloadList extends BaseActivity {
    public String bill_no = "", gst_bill_no = "", bill_date = "", disp_date = "", kunnr = "", name = ""
            , fathname = "", state = "", state_txt = "", district = "", district_txt = "", address = ""
            , contactno = "", controller = "", motor = "", simno = "", pump = "", regisno = "", projectno = ""
            , loginno = "", module_qty = "", sync = "", CONTACT_NO = "",inst_no_of_module_value="",HP="", simha2 = "",pumpLoad =""
            , set_matno = "", village = "", tehsil = "",beneficiary = "",pump_ser,motor_ser,controller_ser,
            version ,user_id, aadhar_no = "";

    Context context;
    DatabaseHelper db;
    LinearLayout lin1, lin2;
    RecyclerView recyclerView;
    Adapter_Unload_Installation_list adapterInstallationList;
    EditText editsearch;
    ArrayList<InstallationListBean> installationBeans;
    InstallationListBean installationBean;
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;


    @SuppressLint("WrongConstant")
    @Override
    protected void  onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation_list);


        Init();
        if (CustomUtility.isInternetOn(getApplicationContext())) {
            recyclerView.setAdapter(null);
            db.deleteUnloadInstallationListData();
            WebURL.CHECK_DATA_UNOLAD = 1;
            new GetInstallationDataList_Unload().execute();
        } else {
            getValueFromDatabase();
            Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
        }


        listner();
    }

    private void getValueFromDatabase() {
        db = new DatabaseHelper(context);
        if (db.getcount(DatabaseHelper.TABLE_INSTALLATION_UNLOAD_LIST)) {
            installationBeans = new ArrayList<InstallationListBean>();
            installationBeans = db.getUnloadInstallationListData(user_id);
            Log.e("SIZE", "&&&&" + installationBeans.size());
            if (installationBeans != null && installationBeans.size() > 0) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
                Log.e("SIZE", "&&&&" + installationBeans.size());
                Log.e("pumpserno====>",installationBeans.get(3).getPump());
                adapterInstallationList = new Adapter_Unload_Installation_list(context, installationBeans);
                recyclerView.setAdapter(adapterInstallationList);
                adapterInstallationList.notifyDataSetChanged();
                WebURL.CHECK_DATA_UNOLAD = 1;
            } else {
                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);
            }
        } else {
            recyclerView.setAdapter(null);
            db.deleteUnloadInstallationListData();
            new GetInstallationDataList_Unload().execute();
        }
    }

    private void Init() {
        context = this;
        db = new DatabaseHelper(context);
        progressDialog = new ProgressDialog(context);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.unloading_list));

        recyclerView = findViewById(R.id.emp_list);
        editsearch = findViewById(R.id.search);
        lin1 = findViewById(R.id.lin1);
        lin2 = findViewById(R.id.lin2);

        user_id = CustomUtility.getSharedPreferences(context, "userid");


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
    }

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_menu_unsync:
                if (CustomUtility.isInternetOn(getApplicationContext())) {
                    recyclerView.setAdapter(null);
                    db.deleteUnloadInstallationListData();
                    WebURL.CHECK_DATA_UNOLAD = 1;
                    new GetInstallationDataList_Unload().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void listner() {
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                if (adapterInstallationList != null ) {
                    adapterInstallationList.filter(text);
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
    }

    private class GetInstallationDataList_Unload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            installationBeans = new ArrayList<InstallationListBean>();
            progressDialog = ProgressDialog.show(context, "", "Please Wait...");

        }

        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("USERID", CustomUtility.getSharedPreferences(context, "userid")));
            param.add(new BasicNameValuePair("PROJECT_NO", CustomUtility.getSharedPreferences(context, "projectid")));
            param.add(new BasicNameValuePair("PROJECT_LOGIN_NO", CustomUtility.getSharedPreferences(context, "loginid")));
            String login_selec = null;
       //     Log.e("param====>",param.toString());
            try {
                login_selec = CustomHttpClient.executeHttpPost1(WebURL.INSTALLATION_UNLOAD1, param);
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
                        pump_ser = jo.getString("pump_sernr");
                        motor_ser = jo.getString("motor_sernr");
                        controller_ser = jo.getString("controller_sernr");
                        HP = jo.getString("hp");
                        pumpLoad = jo.getString("pump_load");
                        aadhar_no = jo.getString("aadhar_no");

                        installationBean = new InstallationListBean(bill_no, CustomUtility.getSharedPreferences(context, "userid"),
                                name, fathname, bill_no, kunnr, gst_bill_no, bill_date, disp_date, state, state_txt, district, district_txt, tehsil, village,
                                contactno, controller, motor, pump, regisno, projectno, loginno, module_qty, address, simno, beneficiary, set_matno,
                                simha2, sync, CONTACT_NO,"",HP,pump_ser,motor_ser,controller_ser,pumpLoad,aadhar_no);

                        if (db.isRecordExist(DatabaseHelper.TABLE_INSTALLATION_UNLOAD_LIST, DatabaseHelper.KEY_ENQ_DOC, bill_no)) {
                            db.updateUnloadInstallationListData(bill_no, installationBean);
                        } else {
                            db.insertUnloadInstallationListData(bill_no, installationBean);
                        }
            } }catch (Exception e){
                    e.printStackTrace();
                }
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
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
            installationBeans = db.getUnloadInstallationListData(user_id);

            if (installationBeans != null && installationBeans.size() > 0) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
                adapterInstallationList = new Adapter_Unload_Installation_list(context, installationBeans);
                recyclerView.setAdapter(adapterInstallationList);
                adapterInstallationList.notifyDataSetChanged();
                WebURL.CHECK_DATA_UNOLAD = 1;
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
