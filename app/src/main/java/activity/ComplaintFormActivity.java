package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import bean.ComplaintInstModel;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class ComplaintFormActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton damageRadio,missedRadio,damageMotorRadio,missedMotorRadio,damageConRadio,missedConRadio,damageOtherRadio,missedOtherRadio,
    pumpNone,motorNone,controllerNone;
    private Toolbar mToolbar;
    TextView submitBtn;

    EditText farmerNameExt, contactNumberExt, applicationNumberExt, addressExt, pumpSrNoExt, motorSrNoExt, controllerSrNoExt, additionalInfoExt;
    ComplaintInstModel.Response complaintInstModel;

    private boolean isDamage = false,isMissed = false,
            isMDamage = false,isMMissed = false,
            isCDamage = false,isCMissed = false,
            isODamage = false,isOMissed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_form);

        Init();
        retrieveValue();
        listner();
    }

    private void listner() {

        submitBtn.setOnClickListener(this);
    }

    private void Init() {
        mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.complaint_before_installation);

        farmerNameExt = findViewById(R.id.farmerNameExt);
        contactNumberExt = findViewById(R.id.contactNumberExt);
        applicationNumberExt = findViewById(R.id.applicationNumberExt);
        addressExt = findViewById(R.id.addressExt);
        pumpSrNoExt = findViewById(R.id.pumpSrNoExt);
        motorSrNoExt = findViewById(R.id.motorSrNoExt);
        controllerSrNoExt = findViewById(R.id.controllerSrNoExt);
        damageRadio = findViewById(R.id.damageRadio);
        missedRadio = findViewById(R.id.missedRadio);
        damageMotorRadio = findViewById(R.id.damageMotorRadio);
        missedMotorRadio = findViewById(R.id.missedMotorRadio);
        damageConRadio = findViewById(R.id.damageConRadio);
        missedConRadio = findViewById(R.id.missedConRadio);
        damageOtherRadio = findViewById(R.id.damageOtherRadio);
        missedOtherRadio = findViewById(R.id.missedOtherRadio);
        pumpNone = findViewById(R.id.pumpRadio);
        motorNone = findViewById(R.id.motorRadio);
        controllerNone =findViewById(R.id.controllerRadio);

        additionalInfoExt = findViewById(R.id.additionalInfoExt);
        submitBtn = findViewById(R.id.submitBtn);
    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {
            complaintInstModel = (ComplaintInstModel.Response) getIntent().getSerializableExtra(Constant.InstallationCompData);

            assert complaintInstModel != null;
            farmerNameExt.setText(complaintInstModel.getName());
            contactNumberExt.setText(complaintInstModel.getContactNo());
            applicationNumberExt.setText(complaintInstModel.getBeneficiary());
            addressExt.setText(complaintInstModel.getAddress());
            pumpSrNoExt.setText(complaintInstModel.getPumpSernr());
            motorSrNoExt.setText(complaintInstModel.getMotorSernr());
            controllerSrNoExt.setText(complaintInstModel.getControllerSernr());
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.submitBtn:
                ValidationCheck();

                break;
        }
    }

    private void ValidationCheck() {
        if (additionalInfoExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_additionalInfoExt), getApplicationContext());
        }else {
           new submitComplainForm().execute();

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class submitComplainForm extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ComplaintFormActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Sending Data to server..please wait !");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String docno_sap = null;
            String status= null;
            String obj2 = null;

            JSONArray ja_invc_data = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            try {
                SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");

                if (damageRadio.isChecked()) {
                    jsonObj.put("DAMAGE_PUMP", complaintInstModel.getPumpSernr());
                    jsonObj.put( "PUMP", "Damage");
                } else if (missedRadio.isChecked()) {
                    jsonObj.put("DAMAGE_PUMP", complaintInstModel.getPumpSernr());
                    jsonObj.put("PUMP", "Missed");
                } else {
                    jsonObj.put("PUMP", "");
                    jsonObj.put("DAMAGE_PUMP", "");
                }
                if (damageMotorRadio.isChecked()) {
                    jsonObj.put("DAMAGE_MOTOR", complaintInstModel.getMotorSernr());
                    jsonObj.put( "MOTOR", "Damage");
                } else  if (missedMotorRadio.isChecked()) {
                    jsonObj.put("MOTOR", "Missed");
                    jsonObj.put("DAMAGE_MOTOR", complaintInstModel.getMotorSernr());
                } else {
                    jsonObj.put("MOTOR", "");
                    jsonObj.put("DAMAGE_MOTOR", "");
                }
                if (damageConRadio.isChecked()) {
                    jsonObj.put( "CONTROLLER", "Damage");
                    jsonObj.put("DAMAGE_CONT", complaintInstModel.getControllerSernr());
                } else if (missedConRadio.isChecked()) {
                    jsonObj.put( "CONTROLLER", "Missed");
                    jsonObj.put("DAMAGE_CONT", complaintInstModel.getControllerSernr());
                } else   {
                    jsonObj.put("CONTROLLER", "");
                    jsonObj.put("DAMAGE_CONT","");
                }
                if (damageOtherRadio.isChecked()) {
                    jsonObj.put( "OTHER", "Damage");
                } else if (missedOtherRadio.isChecked()) {
                    jsonObj.put( "OTHER", "Missed");
                } else   {
                    jsonObj.put("OTHER", "");
                }
                jsonObj.put("REGISNO",  complaintInstModel.getRegisno());
                jsonObj.put("PROJECT_NO",  complaintInstModel.getProjectNo());
                jsonObj.put("PROCESS_NO",complaintInstModel.getProcessNo());
                jsonObj.put("PROJECT_LOGIN_NO", "01");
                jsonObj.put("BENEFICIARY", complaintInstModel.getBeneficiary());
                jsonObj.put("REMARK", additionalInfoExt.getText().toString().trim());
                jsonObj.put("BILLNO", complaintInstModel.getVbeln());
                jsonObj.put("USERID", CustomUtility.getSharedPreferences(ComplaintFormActivity.this, "userid"));

                ja_invc_data.put(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("before1", ja_invc_data.toString());
            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("cmp", String.valueOf(ja_invc_data)));

            System.out.println("before13==>>" + param1_invc);
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.SavecomplianbeforeURL, param1_invc);
           //     Log.e("before1OUTPUT1", "&&&&" + obj2);
                System.out.println("before1OUTPUT1==>>" + obj2);
                progressDialog.dismiss();

                if (!obj2.equalsIgnoreCase("")) {
                    JSONObject object = new JSONObject(obj2);

                    docno_sap = object.getString("data_save");

                    JSONArray ja = new JSONArray(docno_sap);

                    //Log.d("ja", "" + ja);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);

                        status = jo.getString("return");
                     //   ename = jo.getString("NAME");

                    }

                    Log.e("beforeStatus====>", status.toString());

                    if(status.equalsIgnoreCase("Y")){
                        showingMessage("Successfully Saved");
                        finish();
                    }else{
                        showingMessage("Not Saved");
                    }

                } else {
                    showingMessage(getResources().getString(R.string.somethingWentWrong));
                    progressDialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showingMessage(String message) {
        runOnUiThread(new Runnable() {
            public void run() {

                CustomUtility.showToast(ComplaintFormActivity.this, message);

            }
        });
    }
}