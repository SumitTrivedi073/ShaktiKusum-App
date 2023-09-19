package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import adapter.Adapter_Installation_list;
import bean.InstallationListBean;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class UnloadList extends BaseActivity {
    public String bill_no = "", gst_bill_no = "", bill_date = "", disp_date = "", kunnr = "", name = ""
            , fathname = "", state = "", state_txt = "", district = "", district_txt = "", address = ""
            , contactno = "", controller = "", motor = "", simno = "", pump = "", regisno = "", projectno = ""
            , loginno = "", module_qty = "", sync = "", CONTACT_NO = "",inst_no_of_module_value="",HP, simha2 = ""
            , set_matno = "", village = "", tehsil = "",beneficiary = "",pump_ser,motor_ser,controller_ser,
            version;
    Context context;

    LinearLayout lin1, lin2;
    RecyclerView recyclerView;
    Adapter_Installation_list adapterInstallationList;
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
        listner();
    }
    private void Init() {
        context = this;
        progressDialog = new ProgressDialog(context);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.unloading_material);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.emp_list);
        editsearch = findViewById(R.id.search);
        lin1 = findViewById(R.id.lin1);
        lin2 = findViewById(R.id.lin2);

        if(CustomUtility.isInternetOn(context)){
            new GetInstallationDataList_Unload().execute();
        }else {
            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);
            CustomUtility.showToast(context,getResources().getString(R.string.check_internet_connection));
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
    }


    private void listner() {
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
            Log.e("param====>",param.toString());
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
                        installationBean = new InstallationListBean(bill_no, CustomUtility.getSharedPreferences(context, "userid"),
                                name, fathname, bill_no, kunnr, gst_bill_no, bill_date, disp_date, state, state_txt, district, district_txt, tehsil, village,
                                contactno, controller, motor, pump, regisno, projectno, loginno, module_qty, address, simno, beneficiary, set_matno,
                                simha2, sync, CONTACT_NO,"",HP,pump_ser,motor_ser,controller_ser);

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
                        installationBean.setNoOfModule(inst_no_of_module_value);
                        installationBean.setHP(HP);
                        installationBean.setPump_ser(pump_ser);
                        installationBean.setMotor_ser(motor_ser);
                        installationBean.setController_ser(controller_ser);
                        installationBeans.add(installationBean);
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
            try {
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                if(installationBeans.size()>0) {
                    lin1.setVisibility(View.VISIBLE);
                    lin2.setVisibility(View.GONE);
                    recyclerView.setAdapter(null);
                    adapterInstallationList = new Adapter_Installation_list(context, installationBeans);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(adapterInstallationList);
                    adapterInstallationList.notifyDataSetChanged();
                    WebURL.CHECK_DATA_UNOLAD = 1;
                }else {
                    lin1.setVisibility(View.GONE);
                    lin2.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
