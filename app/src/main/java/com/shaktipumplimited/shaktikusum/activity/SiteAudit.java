package com.shaktipumplimited.shaktikusum.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;

import com.shaktipumplimited.shaktikusum.database.DatabaseHelper;
import com.shaktipumplimited.shaktikusum.utility.CustomUtility;

public class SiteAudit extends AppCompatActivity implements MediaController.MediaPlayerControl {

    Context context ;
    private Toolbar mToolbar;
    DatabaseHelper db;
    TextView save;
    EditText vendor_no;
    Boolean flag_focusoff = false;

    String spinner_state_txt, spinner_district_txt, spinner_stateid, spinner_districtid;

    Spinner spinner_state,
            spinner_district;
    int index, index1;
    List<String> list_state_inst = null;
    List<String> list_district_inst = null;
    List<String> list_tehsil_inst = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_audit);
        context = this;

        db = new DatabaseHelper(context);

        list_state_inst    = new ArrayList<String>();
        list_district_inst = new ArrayList<String>();
        list_tehsil_inst   = new ArrayList<String>();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Site Audit");


        getLayout();

        list_state_inst.clear();
        list_state_inst = db.getStateDistrictList(DatabaseHelper.KEY_STATE_TEXT, null);

        spinner_state.setPrompt("Select State");
        spinner_district.setPrompt("Select District");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(context, R.layout.spinner_item_center, list_state_inst);

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
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item_center, list_district_inst);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

                // attaching data adapter to spinner
                spinner_district.setAdapter(dataAdapter);

                if (!spinner_state_txt.equalsIgnoreCase("Select State") && !TextUtils.isEmpty(spinner_state_txt)) {
                    spinner_stateid = db.getStateDistrictValue(DatabaseHelper.KEY_STATE, spinner_state_txt);

                    CustomUtility.setSharedPreference(context, "statetext", spinner_state_txt);
                    CustomUtility.setSharedPreference(context, "stateid", spinner_stateid);

                    Log.e("ID", "&&&" + spinner_stateid);

                    spinner_district.setSelection(0);

                    spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                            index1 = arg0.getSelectedItemPosition();
                            spinner_district_txt = spinner_district.getSelectedItem().toString();
                            if (!spinner_district_txt.equalsIgnoreCase("Select District") && !TextUtils.isEmpty(spinner_district_txt)) {

                                spinner_districtid = db.getStateDistrictValue(DatabaseHelper.KEY_DISTRICT, spinner_district_txt);

                                CustomUtility.setSharedPreference(context, "districtid", spinner_districtid);
                                CustomUtility.setSharedPreference(context, "districttext", spinner_district_txt);

                                Log.e("ID1", "&&&" + spinner_districtid);
                            } else {
                                Toast.makeText(context, "Please Select District.", Toast.LENGTH_SHORT).show();
                                CustomUtility.setSharedPreference(context, "districtid", "");
                                CustomUtility.setSharedPreference(context, "districttext", "");
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });

                } else {
                    Toast.makeText(context, "Please Select State.", Toast.LENGTH_SHORT).show();
                    CustomUtility.setSharedPreference(context, "stateid", "");
                    CustomUtility.setSharedPreference(context, "statetext", "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!flag_focusoff)
                {
                    saveData();
                }
                    onBackPressed();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id_temp = item.getItemId();

        switch (id_temp) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_signout:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void  saveData() {

        //GET DATA
        getData();

    }

    public void getData(){


    }


    public void getLayout() {

        save = (TextView) findViewById(R.id.btn_submit);
        vendor_no = (EditText) findViewById(R.id.vendor_no);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_district = (Spinner) findViewById(R.id.spinner_district);


    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {}

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }


    @Override
    public int getAudioSessionId() {
        return 0;
    }





}