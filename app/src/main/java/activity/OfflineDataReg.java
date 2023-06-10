package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.Adapter_offline_reg_list;
import bean.RegistrationBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

import static android.os.Environment.getExternalStoragePublicDirectory;

import com.shaktipumplimited.shaktikusum.R;


public class OfflineDataReg extends BaseActivity {
    Context context;

    DatabaseHelper db;

    String doc_no, version, device_name;
    LinearLayout lin1, lin2;
    RecyclerView recyclerView;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";
    Adapter_offline_reg_list adapterEmployeeList;
    List<String> enq_docno = new ArrayList<>();
    String photo1_text, photo2_text, photo3_text, photo4_text, photo5_text, photo6_text, photo7_text, photo8_text, photo9_text, photo10_text, photo11_text, photo12_text;
    ArrayList<RegistrationBean> registrationBeans;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(OfflineDataReg.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private LinearLayoutManager layoutManagerSubCategory;

   /* public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                e.printStackTrace();
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

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_reg);
        context = this;

        db = new DatabaseHelper(context);


       // version = BuildConfig.VERSION_NAME;
        version = WebURL.APP_VERSION_CODE;

        device_name = CustomUtility.getDeviceName();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.emp_list);

        db = new DatabaseHelper(context);

        lin1 = findViewById(R.id.lin1);
        lin2 = findViewById(R.id.lin2);

        registrationBeans = new ArrayList<RegistrationBean>();

        registrationBeans = db.getRegistrationData();


        Log.e("SIZE", "&&&&" + registrationBeans.size());
        if (registrationBeans != null && registrationBeans.size() > 0) {


            lin1.setVisibility(View.VISIBLE);
            lin2.setVisibility(View.GONE);

            recyclerView.setAdapter(null);

            Log.e("SIZE", "&&&&" + registrationBeans.size());
            adapterEmployeeList = new Adapter_offline_reg_list(context, registrationBeans);
            layoutManagerSubCategory = new LinearLayoutManager(context);
            layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManagerSubCategory);
            recyclerView.setAdapter(adapterEmployeeList);
            adapterEmployeeList.notifyDataSetChanged();


        } else {

            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);

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
                if (CustomUtility.isInternetOn(getApplicationContext())) {

                    syncOfflineData();

                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection...., Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void syncOfflineData() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                if (CustomUtility.isInternetOn(getApplicationContext())) {

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            new REGDATA().execute();

                        }
                    });


                } else {


                    Message msg = new Message();
                    msg.obj = "No Internet Connection";
                    mHandler.sendMessage(msg);

                    //Toast.makeText(MainActivity.this, "No internet Connection. ", Toast.LENGTH_SHORT).show();
                }

            }
        }).start();


    }

    private class REGDATA extends AsyncTask<String, String, String> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {

            //progress = new ProgressDialog(context);
            progress = ProgressDialog.show(context, "Send Data to Server...", "Sync Offline Data, please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObj = null;
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;
            JSONArray ja_invc_data = new JSONArray();

            db = new DatabaseHelper(context);

            try {

                ArrayList<RegistrationBean> documentBeanArrayList = new ArrayList<RegistrationBean>();
                documentBeanArrayList = db.getRegistrationData();

                Log.e("SIZE456789", "^^^^" + documentBeanArrayList.size());

                try {

                    for (int i = 0; i < documentBeanArrayList.size(); i++) {
                        jsonObj = new JSONObject();

                        try {

                            enq_docno.add(documentBeanArrayList.get(i).getEnqdoc());

                            Log.e("IDDD", "&&&&" + enq_docno.get(i));


                            String date_s = documentBeanArrayList.get(i).getDate();

                            SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");

                            Date date = dt.parse(date_s);
                            SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");

                            Log.e("DATE", "%%%%" + dt1.format(date));

                            jsonObj.put("userid", documentBeanArrayList.get(i).getPernr());
                            jsonObj.put("mdocno", documentBeanArrayList.get(i).getEnqdoc());
                            jsonObj.put("project_no", documentBeanArrayList.get(i).getProject_no());
                            jsonObj.put("project_login_no", documentBeanArrayList.get(i).getLogin_no());
                            jsonObj.put("regis_date", dt1.format(date));
                            jsonObj.put("lat", documentBeanArrayList.get(i).getLat());
                            jsonObj.put("lng", documentBeanArrayList.get(i).getLng());
                            jsonObj.put("customer_name", documentBeanArrayList.get(i).getCustomer_name());
                            jsonObj.put("father_name", documentBeanArrayList.get(i).getFather_name());
                            jsonObj.put("state", documentBeanArrayList.get(i).getState());
                            jsonObj.put("city", documentBeanArrayList.get(i).getCity());

                            Log.e("STATE", "&&&&" + documentBeanArrayList.get(i).getState());
                            Log.e("CITY", "&&&&" + documentBeanArrayList.get(i).getCity());

                            jsonObj.put("tehsil", documentBeanArrayList.get(i).getTehsil());
                            jsonObj.put("village", documentBeanArrayList.get(i).getVillage());
                            jsonObj.put("contact_no", documentBeanArrayList.get(i).getContact_no());
                            jsonObj.put("aadhar_no", documentBeanArrayList.get(i).getAadhar_no());
                            jsonObj.put("bank_name", documentBeanArrayList.get(i).getBank_name());
                            jsonObj.put("BANK_ACC_NO", documentBeanArrayList.get(i).getBank_acc_no());
                            jsonObj.put("ACCOUNT_TYPE", documentBeanArrayList.get(i).getAccount_type());
                            jsonObj.put("BRANCH_NAME", documentBeanArrayList.get(i).getBranch_name());
                            jsonObj.put("IFSC_CODE", documentBeanArrayList.get(i).getIfsc_code());
                            jsonObj.put("AMOUNT", documentBeanArrayList.get(i).getAmount());
                            jsonObj.put("PDF", documentBeanArrayList.get(i).getPdf());
                            jsonObj.put("PHOTO1", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_1"));
                            jsonObj.put("PHOTO2", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_2"));
                            jsonObj.put("PHOTO3", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_3"));
                            jsonObj.put("PHOTO4", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_4"));
                            jsonObj.put("PHOTO5", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_5"));
                            jsonObj.put("PHOTO6", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_6"));
                            jsonObj.put("PHOTO7", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_7"));
                            jsonObj.put("PHOTO8", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_8"));
                            jsonObj.put("PHOTO9", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_9"));
                            jsonObj.put("PHOTO10", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_10"));
                            jsonObj.put("PHOTO11", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_11"));
                            jsonObj.put("PHOTO12", CustomUtility.getSharedPreferences(context, enq_docno.get(i) + "PHOTO_12"));

                            ja_invc_data.put(jsonObj);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
                    param1_invc.add(new BasicNameValuePair("registration", String.valueOf(ja_invc_data)));

                    Log.e("DATA", "$$$$" + param1_invc.size());
                    Log.e("DATA", "$$$$" + param1_invc);


                    System.out.println(param1_invc);

                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);

                        obj2 = CustomHttpClient.executeHttpPost1(WebURL.REGISTRATION_DATA, param1_invc);

                        Log.e("OBJECT", "&&&&" + obj2);

                        if (obj2 != "") {

                            JSONObject object = new JSONObject(obj2);
                            String obj1 = object.getString("data_return");


                            JSONArray ja = new JSONArray(obj1);

                            for (int i = 0; i < ja.length(); i++) {


                                JSONObject jo = ja.getJSONObject(i);


                                docno_sap = jo.getString("mdocno");
                                invc_done = jo.getString("return");


                                if (invc_done.equalsIgnoreCase("Y")) {
                                    Message msg = new Message();
                                    msg.obj = "Data Submitted Successfully...";
                                    mHandler.sendMessage(msg);

                                    Log.e("DOCNO", "&&&&" + enq_docno);

                                    for (int j = 0; j < enq_docno.size(); j++) {
                                        db.deleteRegistrationData(enq_docno.get(j));
                                        deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SKAPP/REG/" + enq_docno.get(j)));
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_1", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_2", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_3", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_4", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_5", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_6", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_7", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_8", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_9", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_10", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_11", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_12", "");
                                    }
                                    progress.dismiss();
                                    finish();

                                } else if (invc_done.equalsIgnoreCase("N")) {

                                    Message msg = new Message();
                                    msg.obj = "Data Not Submitted, Please try After Sometime.";
                                    mHandler.sendMessage(msg);

                                    for (int j = 0; j < enq_docno.size(); j++) {
                                        db.deleteRegistrationData(enq_docno.get(j));
                                      /*  deleteFiles(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP//REG/" + enq_docno.get(j));
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_1", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_2", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_3", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_4", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_5", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_6", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_7", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_8", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_9", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_10", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_11", "");
                                        CustomUtility.setSharedPreference(context, enq_docno.get(j) + "PHOTO_12", "");*/
                                    }
                                    progress.dismiss();
                                    finish();

                                }


                            }
                        }

                    } catch (Exception e) {
                        progress.dismiss();
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    progress.dismiss();
                    e.printStackTrace();
                }
            } catch (Exception e) {
                progress.dismiss();
                e.printStackTrace();
            }

            return obj2;

        }

        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
            onResume();
            progress.dismiss();  // dismiss dialog
        }
    }
}
