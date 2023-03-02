package com.shaktipumplimited.shaktikusum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.WebURL;


public class OfflineData extends AppCompatActivity {
    Context context;

    DatabaseHelper db;

    String doc_no, version, device_name;
    private Toolbar mToolbar;
    private TextView regis_data, inst_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_data);
        context = this;


        //version = BuildConfig.VERSION_NAME;
        version = WebURL.APP_VERSION_CODE;
        device_name = CustomUtility.getDeviceName();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regis_data = (TextView) findViewById(R.id.regis_data);
        inst_data = (TextView) findViewById(R.id.inst_data);


        regis_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, OfflineDataReg.class);

                startActivity(intent);
            }
        });


        inst_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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


}
