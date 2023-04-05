package com.shaktipumplimited.shaktikusum.utility;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.shaktipumplimited.shaktikusum.database.DatabaseHelper;
import com.shaktipumplimited.shaktikusum.R;
import com.shaktipumplimited.shaktikusum.activity.SiteAuditList;

public class dialog5 extends Dialog {

    private SiteAuditList activity;
    private TextView search, cancel;
    private dialog5 thisDialog;
    DatabaseHelper db;
    EditText vendor_no;

    String spinner_state_txt, spinner_district_txt, spinner_stateid, spinner_districtid;

    Spinner spinner_state,
            spinner_district;
    int index, index1;
    List<String> list_state_inst = null;
    List<String> list_district_inst = null;
    List<String> list_tehsil_inst = null;

    public dialog5(SiteAuditList context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.activity = context;
        this.thisDialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailog_search2);

        db = new DatabaseHelper(activity);

        list_state_inst    = new ArrayList<String>();
        list_district_inst = new ArrayList<String>();
        list_tehsil_inst   = new ArrayList<String>();


        thisDialog.setCancelable(false);
        thisDialog.setCanceledOnTouchOutside(false);
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        initalize();

        list_state_inst.clear();
        list_state_inst = db.getStateDistrictList(DatabaseHelper.KEY_STATE_TEXT, null);

        spinner_state.setPrompt("Select State");
        spinner_district.setPrompt("Select District");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(activity, R.layout.spinner_item_center, list_state_inst);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(R.layout.spinner_item_center);

        // attaching data adapter to spinner
        spinner_state.setAdapter(dataAdapter1);


        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                spinner_state_txt = spinner_state.getSelectedItem().toString();

                list_district_inst.clear();
                list_district_inst = db.getStateDistrictList(DatabaseHelper.KEY_DISTRICT_TEXT, spinner_state_txt);
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, R.layout.spinner_item_center, list_district_inst);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

                // attaching data adapter to spinner
                spinner_district.setAdapter(dataAdapter);

                if (!spinner_state_txt.equalsIgnoreCase("Select State") && !TextUtils.isEmpty(spinner_state_txt)) {
                    spinner_stateid = db.getStateDistrictValue(DatabaseHelper.KEY_STATE, spinner_state_txt);

                    CustomUtility.setSharedPreference(activity, "statetext", spinner_state_txt);
                    CustomUtility.setSharedPreference(activity, "stateid", spinner_stateid);

                    Log.e("ID", "&&&" + spinner_stateid);

                    spinner_district.setSelection(0);

                    spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                            index1 = arg0.getSelectedItemPosition();
                            spinner_district_txt = spinner_district.getSelectedItem().toString();
                            if (!spinner_district_txt.equalsIgnoreCase("Select District") && !TextUtils.isEmpty(spinner_district_txt)) {

                                spinner_districtid = db.getStateDistrictValue(DatabaseHelper.KEY_DISTRICT, spinner_district_txt);

                                CustomUtility.setSharedPreference(activity, "districtid", spinner_districtid);
                                CustomUtility.setSharedPreference(activity, "districttext", spinner_district_txt);

                                Log.e("ID1", "&&&" + spinner_districtid);
                            } else {
                                Toast.makeText(activity, "Please Select District.", Toast.LENGTH_SHORT).show();
                                CustomUtility.setSharedPreference(activity, "districtid", "");
                                CustomUtility.setSharedPreference(activity, "districttext", "");
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });

                } else {
                    Toast.makeText(activity, "Please Select State.", Toast.LENGTH_SHORT).show();
                    CustomUtility.setSharedPreference(activity, "stateid", "");
                    CustomUtility.setSharedPreference(activity, "statetext", "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void initalize() {
        // TODO Auto-generated method stub
        vendor_no = (EditText) findViewById(R.id.vendor_no);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_district = (Spinner) findViewById(R.id.spinner_district);
        search = (TextView) findViewById(R.id.btn_submit);
        cancel = (TextView) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                thisDialog.cancel();
                activity.finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String textstate = CustomUtility.getSharedPreferences(activity, "stateid");
                String textdistrict =  CustomUtility.getSharedPreferences(activity, "districtid");
                String textvendor = vendor_no.getText().toString();
                activity.searchWord(textstate,textdistrict,textvendor);
            }
        });
    }


}
