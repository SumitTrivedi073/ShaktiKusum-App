package activity;

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

import adapter.Adapter_reject_list;
import bean.RejectListBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class ActivityRejectSite extends BaseActivity {
    public String bill_no = "";
    public String  ben_no = "";
    public String  reg_no = "";
    public String  cust_nm = "";
    public String  photo1 = "";
    public String  photo2 = "";
    public String  photo3 = "";
    public String  photo4 = "";
    public String  photo5 = "";
    public String  photo6 = "";
    public String  photo7 = "";
    public String  photo8 = "";
    public String  photo9 = "";
    public String  photo10 = "";
    public String  photo11 = "";
    public String  photo12 = "";

    Context context;
    DatabaseHelper db;

    String version, device_name, user_id;
    LinearLayout lin1, lin2;
    RecyclerView recyclerView;
    Adapter_reject_list adapter_reject_list;
    List<String> enq_docno = new ArrayList<>();
    EditText editsearch;
    ArrayList<RejectListBean> rejectListBeans;
    String remrk1, remrk2, remrk3, remrk4, remrk5, remrk6, remrk7, remrk8, remrk9, remrk10, remrk11, remrk12;
    RejectListBean rejectListBean;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(ActivityRejectSite.this, mString, Toast.LENGTH_LONG).show();
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

        db = new DatabaseHelper(context);

        progressDialog = new ProgressDialog(context);

        user_id = CustomUtility.getSharedPreferences(context, "userid");


       //version = BuildConfig.VERSION_NAME;
        version = WebURL.APP_VERSION_CODE;
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
                    adapter_reject_list.filter(text);
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
        super.onResume();
        if (CustomUtility.isInternetOn(getApplicationContext())) {
            recyclerView.setAdapter(null);
            db.deleteRejectListData();
            new GetRejectDataList_Task().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
            rejectListBeans = new ArrayList<RejectListBean>();
            rejectListBeans = db.getRejectionListData();
            Log.e("SIZE", "&&&&" + rejectListBeans.size());
            if (rejectListBeans != null && rejectListBeans.size() > 0) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                recyclerView.setAdapter(null);

                Log.e("SIZE", "&&&&" + rejectListBeans.size());
                adapter_reject_list = new Adapter_reject_list(context, rejectListBeans);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManagerSubCategory);
                recyclerView.setAdapter(adapter_reject_list);
                adapter_reject_list.notifyDataSetChanged();
            } else {
                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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

    private class GetRejectDataList_Task extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //progress = newworkorder ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Please Wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.clear();
            param.add(new BasicNameValuePair("userid", CustomUtility.getSharedPreferences(context, "userid")));
            param.add(new BasicNameValuePair("project_no", CustomUtility.getSharedPreferences(context, "projectid")));
            String login_selec = null;
            try {
                login_selec = CustomHttpClient.executeHttpPost1(WebURL.REJECT_DATA, param);
                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("reject_data");
                Log.e("DATA","&&&&"+obj1);
                JSONArray ja = new JSONArray(obj1);
                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jo = ja.getJSONObject(j);
                    bill_no = jo.getString("vbeln");
                    ben_no  = jo.getString("beneficiary");
                    reg_no  = jo.getString("regisno");
                    cust_nm  = jo.getString("customer_name");

                    photo1     = jo.getString("photos1");
                    photo2     = jo.getString("photos2");
                    photo3     = jo.getString("photos3");
                    photo4     = jo.getString("photos4");
                    photo5     = jo.getString("photos5");
                    photo6     = jo.getString("photos6");
                    photo7     = jo.getString("photos7");
                    photo8     = jo.getString("photos8");
                    photo9     = jo.getString("photos9");
                    photo10    = jo.getString("photos10");
                    photo11    = jo.getString("photos11");
                    photo12    = jo.getString("photos12");

                    remrk1= jo.getString("remark1");
                    remrk2= jo.getString("remark2");
                    remrk3= jo.getString("remark3");
                    remrk4= jo.getString("remark4");
                    remrk5= jo.getString("remark5");
                    remrk6= jo.getString("remark6");
                    remrk7= jo.getString("remark7");
                    remrk8= jo.getString("remark8");
                    remrk9= jo.getString("remark9");
                    remrk10= jo.getString("remark10");
                    remrk11= jo.getString("remark11");
                    remrk12= jo.getString("remark12");

                    rejectListBean = new RejectListBean(bill_no,
                            ben_no,
                           reg_no,
                           cust_nm,
                            photo1,
                            photo2,
                            photo3,
                            photo4,
                            photo5,
                            photo6,
                            photo7,
                            photo8,
                            photo9,
                            photo10,
                            photo11,
                            photo12,
                            remrk1,
                            remrk2,
                            remrk3,
                            remrk4,
                            remrk5,
                            remrk6,
                            remrk7,
                            remrk8,
                            remrk9,
                            remrk10,
                            remrk11,
                            remrk12
                            );


                    if (db.isRecordExist(DatabaseHelper.TABLE_REJECTION_LIST, DatabaseHelper.KEY_BILL_NO, bill_no)) {
                        db.updateRejectListData(bill_no, rejectListBean);
                    } else {
                        db.insertRejectListData(bill_no, rejectListBean);
                    }
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return login_selec;
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String result) {
            // write display tracks logic here
            //onResume();
            rejectListBeans = new ArrayList<RejectListBean>();
            rejectListBeans = db.getRejectionListData();
            Log.e("SIZE", "&&&&" + rejectListBeans.size());
            if (rejectListBeans != null && rejectListBeans.size() > 0) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
                Log.e("SIZE", "&&&&" + rejectListBeans.size());
                adapter_reject_list = new Adapter_reject_list(context, rejectListBeans);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManagerSubCategory);
                recyclerView.setAdapter(adapter_reject_list);
                adapter_reject_list.notifyDataSetChanged();
            } else {
                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);
            }
            progressDialog.dismiss();  // dismiss dialog
        }
    }
}
