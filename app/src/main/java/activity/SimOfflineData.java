package activity;

/**
 * Created by shakti on 10/3/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.Adapter_sim_list;
import bean.LoginBean;
import bean.SimCardBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class SimOfflineData extends BaseActivity {

    Context context;
    private Toolbar mToolbar;
    List<String> enq_docno = new ArrayList<>();
    View.OnClickListener onclick;
    RecyclerView recyclerView;
    DatabaseHelper dataHelper;



    ArrayList<SimCardBean> simCardBean;

    LinearLayout lin1,lin2;
    Adapter_sim_list adapter_sim_list;
    JSONArray ja_invc_data;

    private LinearLayoutManager layoutManagerSubCategory;

    LoginBean loginBean;


    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(SimOfflineData.this, mString, Toast.LENGTH_LONG).show();

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_offline_data);
        context = this;

        dataHelper = new DatabaseHelper(context);
        loginBean = new LoginBean();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sim Card Offline Data");

        recyclerView = (RecyclerView) findViewById(R.id.emp_list);

        dataHelper = new DatabaseHelper(context);

        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offline, menu);
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
            case R.id.action_sync_offline:

                if (CustomUtility.isInternetOn(getApplicationContext())) {

                   new SIMData().execute();
                }
                else {
                    Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    private class SIMData extends AsyncTask<String, String, String> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {

            //progress = new ProgressDialog(context);
            progress = ProgressDialog.show(context, "Send Data to Server...", "Sync Offline Data, please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObj = null;
            String obj2 = null;
            ja_invc_data = new JSONArray();
            String invc_done = null,date_s = null;



            try{

                ArrayList<SimCardBean> simCardBeans = new ArrayList<SimCardBean>();
                simCardBeans = dataHelper.getSimData(loginBean.getUserid());

                Log.e("SIZE456789", "^^^^" + simCardBeans.size());

                try {


                    for (int i = 0; i < simCardBeans.size(); i++) {
                        jsonObj = new JSONObject();

                         date_s = simCardBeans.get(i).getSim_rep_date();
                        enq_docno.add(simCardBeans.get(i).getEnq_docno());


                        SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

                        Date date = dt.parse(date_s);
                        SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd", Locale.US);


                        jsonObj.put("res_pernr", simCardBeans.get(i).getUser_id());
                        jsonObj.put("res_pernr_type", simCardBeans.get(i).getUser_type());
                        jsonObj.put("date", dt1.format(date));
                        jsonObj.put("lat", simCardBeans.get(i).getSim_lat());
                        jsonObj.put("lng", simCardBeans.get(i).getSim_lng());
                        jsonObj.put("device_no", simCardBeans.get(i).getDevice_no());

                        jsonObj.put("cust_name", simCardBeans.get(i).getCust_name());
                        jsonObj.put("cust_mob", simCardBeans.get(i).getCust_mobile());
                        jsonObj.put("cust_add", simCardBeans.get(i).getCust_address());
                       // jsonObj.put("old_sim_no", simCardBeans.get(i).getSim_old_no());
                        jsonObj.put("new_sim_no", simCardBeans.get(i).getSim_new_no());
                        jsonObj.put("old_sim_pht", simCardBeans.get(i).getSim_old_photo());
                        jsonObj.put("new_sim_pht", simCardBeans.get(i).getSim_new_photo());
                        jsonObj.put("drive_pht",simCardBeans.get(i).getDrive_photo());

                        ja_invc_data.put(jsonObj);

                        Log.e("SIZE","&&&&&"+ja_invc_data.length());

                    }

                    final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
                    param1_invc.add(new BasicNameValuePair("sim_change_data", String.valueOf(ja_invc_data)));
                    Log.e("DATA", "$$$$" + param1_invc.size());
                    Log.e("DATA", "$$$$" + param1_invc.toString());

                    System.out.println(param1_invc.toString());

                    try {


                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);

                        obj2 = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param1_invc);

                        Log.e("OUTPUT1","&&&&"+obj2);

                        if (obj2 != "") {

                            JSONArray ja = new JSONArray(obj2);

                            for (int i = 0; i < ja.length(); i++) {

                                JSONObject jo = ja.getJSONObject(i);

                                invc_done = jo.getString("msgtyp");

                                Log.e("invc_done","&&&&&"+invc_done);

                                if (invc_done.equalsIgnoreCase("S")) {
                                    progress.dismiss();
                                    for(int j =0;j< enq_docno.size();j++)
                                    {
                                        progress.dismiss();
                                        Message msg = new Message();
                                        msg.obj = "Data Submitted Successfully...";
                                        mHandler.sendMessage(msg);

                                        dataHelper.deleteSimData(enq_docno.get(j));

                                    }
                                    finish();

                                } else if (invc_done.equalsIgnoreCase("E")) {

                                    progress.dismiss();
                                    Message msg = new Message();
                                    msg.obj = "Data Not Submitted, Please try After Sometime.";
                                    mHandler.sendMessage(msg);
                                    progress.dismiss();
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

    @Override
    protected void onResume() {

        simCardBean = new ArrayList<SimCardBean>();

        simCardBean = dataHelper.getSimData();


        Log.e("SIZE", "&&&&" + simCardBean.size());
        if (simCardBean != null && simCardBean.size() > 0) {


            lin1.setVisibility(View.VISIBLE);
            lin2.setVisibility(View.GONE);

            recyclerView.setAdapter(null);

            Log.e("SIZE", "&&&&" + simCardBean.size());
            adapter_sim_list = new Adapter_sim_list(context, simCardBean, onclick);
            layoutManagerSubCategory = new LinearLayoutManager(context);
            layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManagerSubCategory);
            recyclerView.setAdapter(adapter_sim_list);
            adapter_sim_list.notifyDataSetChanged();


        } else {

            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);

        }
        super.onResume();
    }
}