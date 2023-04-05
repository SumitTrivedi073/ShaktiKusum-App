package com.shaktipumplimited.shaktikusum.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumplimited.shaktikusum.R;

import com.shaktipumplimited.shaktikusum.database.DatabaseHelper;

public class SimCardOptions extends AppCompatActivity {
    Context context;
    private Toolbar mToolbar;

    TextView sim_card_rep,
            offline_data;

    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_option);
        context = this;

        db = new DatabaseHelper(context);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sim Card Replacement");

        sim_card_rep = (TextView) findViewById(R.id.sim_card_rep);
        offline_data = (TextView) findViewById(R.id.offline_data);

        sim_card_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent simcaracti = new Intent(context, SIMActivationDetails.class);
                Bundle extras = new Bundle();
                extras.putString("from", "newform");
                simcaracti.putExtras(extras);
                startActivity(simcaracti);
            }
        });


        offline_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent offline_list = new Intent(context, SimOfflineData.class);
                startActivity(offline_list);
            }
        });


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
            case R.id.action_signout:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}