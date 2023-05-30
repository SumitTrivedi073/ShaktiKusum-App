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

import adapter.Adapter_Survey_list;
import bean.SurveyListBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class SurveyList extends BaseActivity {
    public String ben_id = "";
    public String custnam = "";
    public String contctno = "";
    public String state_txt = "";

    public String district_txt = "";
    public String address_txt = "";
    public String regisno_txt = "";



    Context context;
    DatabaseHelper db;

    String version, device_name, user_id;
    LinearLayout lin1, lin2;
    RecyclerView recyclerView;
    Adapter_Survey_list adapter_survey_list;
    List<String> enq_docno = new ArrayList<>();
    EditText editsearch;
    ArrayList<SurveyListBean> surveyListBeans;
    SurveyListBean surveyListBean;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(SurveyList.this, mString, Toast.LENGTH_LONG).show();
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


        version = WebURL.APP_VERSION_CODE;
        //version = BuildConfig.VERSION_NAME;
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
                    adapter_survey_list.filter(text);
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
            db.deleteSurveyListData();

            new GetSurveyDataList_Task().execute();

        } else {
            Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();

            surveyListBeans = new ArrayList<SurveyListBean>();

            surveyListBeans = db.getSurveyListData(user_id);


            Log.e("SIZE", "&&&&" + surveyListBeans.size());
            if (surveyListBeans != null && surveyListBeans.size() > 0) {


                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);

                recyclerView.setAdapter(null);

                Log.e("SIZE", "&&&&" + surveyListBeans.size());
                adapter_survey_list = new Adapter_Survey_list(context, surveyListBeans);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManagerSubCategory);
                recyclerView.setAdapter(adapter_survey_list);
                adapter_survey_list.notifyDataSetChanged();


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


    private class GetSurveyDataList_Task extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            //progress = newworkorder ProgressDialog(context);
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

                login_selec = CustomHttpClient.executeHttpPost1(WebURL.SURVEY, param);

                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("survey_data");


                JSONArray ja = new JSONArray(obj1);


                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jo = ja.getJSONObject(j);

                    ben_id = jo.getString("beneficiary");

                    custnam = jo.getString("customer_name");
                    contctno = jo.getString("mobile");



                    state_txt = jo.getString("regio_txt");
                    district_txt = jo.getString("cityc_txt");
                    address_txt = jo.getString("address");
                    regisno_txt = jo.getString("regisno");


                    surveyListBean = new SurveyListBean(ben_id,
                            CustomUtility.getSharedPreferences(context, "userid"),
                            regisno_txt,
                            custnam,
                            contctno,
                            state_txt,
                            district_txt,
                            address_txt);


                    if (db.isRecordExist(db.TABLE_SURVEY_LIST, db.KEY_ENQ_DOC, ben_id)) {
                        db.updateSurveyListData(ben_id, surveyListBean);
                    } else {
                        db.insertSurveyListData(ben_id, surveyListBean);
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
            surveyListBeans = new ArrayList<SurveyListBean>();

            surveyListBeans = db.getSurveyListData(user_id);


            Log.e("SIZE", "&&&&" + surveyListBeans.size());
            if (surveyListBeans != null && surveyListBeans.size() > 0) {


                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);

                recyclerView.setAdapter(null);

                Log.e("SIZE", "&&&&" + surveyListBeans.size());
                adapter_survey_list = new Adapter_Survey_list(context, surveyListBeans);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManagerSubCategory);
                recyclerView.setAdapter(adapter_survey_list);
                adapter_survey_list.notifyDataSetChanged();


            } else {

                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);

            }
            progressDialog.dismiss();  // dismiss dialog
        }
    }

}
