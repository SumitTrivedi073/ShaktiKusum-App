package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapter.Adapter_auditsite_list;
import bean.SiteAuditListBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import utility.dialog5;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class SiteAuditList extends BaseActivity {
    public String bill_no = "";
    public String gst_bill_no = "";
    public String bill_date = "";
    public String disp_date = "";
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
    public String villagetehsil = "";
    public String village = "";
    public String tehsil = "";
    public String rec_no = "";
    public String beneficiary = "",vendor="";
    Context context;
    DatabaseHelper db;
    dialog5 yourDialog;
    String  device_name, user_id;
    LinearLayout lin1, lin2;
    RecyclerView recyclerView;
    Adapter_auditsite_list adapter_auditsite_list;
    List<String> enq_docno = new ArrayList<>();
    EditText editsearch;
    private SiteAuditList activity;
    ArrayList<SiteAuditListBean> auditListBeans;
    SiteAuditListBean installationBean;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(SiteAuditList.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private LinearLayoutManager layoutManagerSubCategory;
    private ProgressDialog progressDialog;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation_list);
        context = this;
        this.activity = this;
        db = new DatabaseHelper(context);

        progressDialog = new ProgressDialog(context);

        user_id = CustomUtility.getSharedPreferences(context, "userid");

        yourDialog = new dialog5(activity);
        yourDialog.show();


        device_name = CustomUtility.getDeviceName();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.emp_list);

        // Locate the EditText in listview_main.xml
        editsearch = findViewById(R.id.search);


        lin1 = findViewById(R.id.lin1);
        lin2 = findViewById(R.id.lin2);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                try {
                    adapter_auditsite_list.filter(text);
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


    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {

        if(adapter_auditsite_list != null) {
            adapter_auditsite_list.notifyDataSetChanged();
        }


            auditListBeans = new ArrayList<SiteAuditListBean>();

            auditListBeans = db.getAuditSiteListData(user_id);


            Log.e("SIZE", "&&&&" + auditListBeans.size());
            if (auditListBeans != null && auditListBeans.size() > 0) {


                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);

                recyclerView.setAdapter(null);

                Log.e("SIZE", "&&&&" + auditListBeans.size());
                adapter_auditsite_list = new Adapter_auditsite_list(context, auditListBeans);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManagerSubCategory);
                recyclerView.setAdapter(adapter_auditsite_list);
                adapter_auditsite_list.notifyDataSetChanged();

        }
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return true;
    }

    public void searchWord(String textState,String textDistrict,String textvendorno) {

        if (CustomUtility.isInternetOn(getApplicationContext())) {
            if (!textState.equals("")) {
                if (!textDistrict.equals("")) {
                    if (!textvendorno.equals("")) {
                        recyclerView.setAdapter(null);
                        db.deleteAuditSiteListData();
                        new GetSiteAuditList_Task().execute(textState, textDistrict, textvendorno);
                    } else {
                        Toast.makeText(context, "Please Enter Vendor No.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Please Select District.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please Select State.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(context, "Please Connect to Internet.", Toast.LENGTH_SHORT).show();
        }

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


    private class GetSiteAuditList_Task extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            //progress = newworkorder ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Please Wait...");

        }

        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<>();

            String stateId = params[0];
            String districtId = params[1];
            String userid = params[2];

            param.clear();

            param.add(new BasicNameValuePair("stateid", stateId));
            param.add(new BasicNameValuePair("districtid", districtId));
            param.add(new BasicNameValuePair("userid", userid));
            param.add(new BasicNameValuePair("project",CustomUtility.getSharedPreferences(context,"projectid")));

            String login_selec = null;


            try {

                login_selec = CustomHttpClient.executeHttpPost1(WebURL.INST_CMP, param);

                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("installation_cmp_data");

                JSONArray ja = new JSONArray(obj1);

                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jo = ja.getJSONObject(j);

                    bill_no = jo.getString("vbeln");
                    gst_bill_no = jo.getString("gst_inv_no");
                    bill_date = jo.getString("fkdat");
                    disp_date = jo.getString("dispatch_date");

                    name = jo.getString("name");
                    fathname = jo.getString("name");

                    state = jo.getString("regio");
                    district = jo.getString("cityc");

                    state_txt = jo.getString("regio_txt");
                    district_txt = jo.getString("cityc_txt");

                    address = jo.getString("address");
                    contactno = jo.getString("mobile");

                    beneficiary = jo.getString("beneficiary");

                    regisno = jo.getString("regisno");
                    projectno = jo.getString("project_no");
                    vendor = jo.getString("vendor");
                    installationBean = new SiteAuditListBean(bill_no,
                            CustomUtility.getSharedPreferences(context, "userid"),
                            name,
                            fathname,
                            bill_no,
                            gst_bill_no,
                            bill_date,
                            disp_date,
                            state,
                            state_txt,
                            district,
                            district_txt,
                            contactno,
                            regisno,
                            projectno,
                            address,
                            beneficiary,
                            vendor);


                    if (db.isRecordExist(DatabaseHelper.TABLE_AUDITSITE_LIST, DatabaseHelper.KEY_ENQ_DOC, bill_no)) {
                        db.updateAuditSiteListData(bill_no, installationBean);
                    } else {
                        db.insertAuditSiteListData(bill_no, installationBean);
                    }


                }

                yourDialog.dismiss();


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

            auditListBeans = new ArrayList<SiteAuditListBean>();
            auditListBeans = db.getAuditSiteListData(user_id);

            Log.e("SIZE", "1234" + result);
            if (auditListBeans != null && auditListBeans.size() > 0) {

                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);

                recyclerView.setAdapter(null);

                Log.e("SIZE", "" + auditListBeans.size());
                adapter_auditsite_list = new Adapter_auditsite_list(context, auditListBeans);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManagerSubCategory);
                recyclerView.setAdapter(adapter_auditsite_list);
                adapter_auditsite_list.notifyDataSetChanged();


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
