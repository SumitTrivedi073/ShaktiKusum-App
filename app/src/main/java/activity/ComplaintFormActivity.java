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

    RadioButton damageRadio, missedRadio, damageMotorRadio, missedMotorRadio, damageConRadio, missedConRadio, damageOtherRadio, missedOtherRadio,
            pumpNone, motorNone, controllerNone;
    private Toolbar mToolbar;
    TextView submitBtn;

    EditText farmerNameExt, contactNumberExt, applicationNumberExt, addressExt, pumpSrNoExt, motorSrNoExt, controllerSrNoExt, additionalInfoExt;
    ComplaintInstModel.Response complaintInstModel;
    String docno_sap, status;

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
        mToolbar = findViewById(R.id.toolbar);
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
        controllerNone = findViewById(R.id.controllerRadio);

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
        switch (v.getId()) {

            case R.id.submitBtn:
                if (CustomUtility.isInternetOn(getApplicationContext())) {
                    ValidationCheck();
                } else {
                    CustomUtility.showToast(ComplaintFormActivity.this, getResources().getString(R.string.check_internet_connection));
                }

                break;
        }
    }

    private void ValidationCheck() {
        if (additionalInfoExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_additionalInfoExt), getApplicationContext());
        } else {
            JSONArray ja_invc_data = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            try {
                SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");

                if (damageRadio.isChecked()) {
                    jsonObj.put("DAMAGE_PUMP", complaintInstModel.getPumpSernr());
                    jsonObj.put("PUMP", getResources().getString(R.string.damage));
                } else if (missedRadio.isChecked()) {
                    jsonObj.put("DAMAGE_PUMP", complaintInstModel.getPumpSernr());
                    jsonObj.put("PUMP", getResources().getString(R.string.missed));
                } else {
                    jsonObj.put("PUMP", "");
                    jsonObj.put("DAMAGE_PUMP", "");
                }
                if (damageMotorRadio.isChecked()) {
                    jsonObj.put("DAMAGE_MOTOR", complaintInstModel.getMotorSernr());
                    jsonObj.put("MOTOR", getResources().getString(R.string.damage));
                } else if (missedMotorRadio.isChecked()) {
                    jsonObj.put("MOTOR", getResources().getString(R.string.missed));
                    jsonObj.put("DAMAGE_MOTOR", complaintInstModel.getMotorSernr());
                } else {
                    jsonObj.put("MOTOR", "");
                    jsonObj.put("DAMAGE_MOTOR", "");
                }
                if (damageConRadio.isChecked()) {
                    jsonObj.put("CONTROLLER", getResources().getString(R.string.damage));
                    jsonObj.put("DAMAGE_CONT", complaintInstModel.getControllerSernr());
                } else if (missedConRadio.isChecked()) {
                    jsonObj.put("CONTROLLER", getResources().getString(R.string.missed));
                    jsonObj.put("DAMAGE_CONT", complaintInstModel.getControllerSernr());
                } else {
                    jsonObj.put("CONTROLLER", "");
                    jsonObj.put("DAMAGE_CONT", "");
                }
                if (damageOtherRadio.isChecked()) {
                    jsonObj.put("OTHER", getResources().getString(R.string.damage));
                } else if (missedOtherRadio.isChecked()) {
                    jsonObj.put("OTHER", getResources().getString(R.string.missed));
                } else {
                    jsonObj.put("OTHER", "");
                }
                jsonObj.put("REGISNO", complaintInstModel.getRegisno());
                jsonObj.put("PROJECT_NO", complaintInstModel.getProjectNo());
                jsonObj.put("PROCESS_NO", complaintInstModel.getProcessNo());
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
            new submitComplainForm(ja_invc_data).execute();

        }

    }

    private class submitComplainForm extends AsyncTask<String, String, String> {

        JSONArray jsonArray;

        public submitComplainForm(JSONArray jaInvcData) {
            jsonArray = jaInvcData;
        }

        @Override
        protected void onPreExecute() {
            CustomUtility.showProgressDialogue(ComplaintFormActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String obj2 = null;
            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("cmp", String.valueOf(jsonArray)));

            System.out.println("before13==>>" + param1_invc);
            try {
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.SavecomplianbeforeURL, param1_invc);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {
            CustomUtility.hideProgressDialog(ComplaintFormActivity.this);
            try {
                if (!result.isEmpty()) {
                    JSONObject object = new JSONObject(result);

                    docno_sap = object.getString("data_save");

                    JSONArray ja = new JSONArray(docno_sap);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);

                        status = jo.getString("return");

                    }

                    Log.e("beforeStatus====>", status.toString());

                    if (status.equalsIgnoreCase("Y")) {
                        CustomUtility.showToast(ComplaintFormActivity.this, getResources().getString(R.string.dataSubmittedSuccessfully));
                        finish();
                    } else {
                        CustomUtility.showToast(ComplaintFormActivity.this, getResources().getString(R.string.dataNotSubmitted));

                    }

                } else {
                    showingMessage(getResources().getString(R.string.somethingWentWrong));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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