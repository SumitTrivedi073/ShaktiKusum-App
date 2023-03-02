package com.shaktipumplimited.shaktikusum;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.Adapter_item_list;
import bean.ItemNameBean;
import bean.LoginBean;
import ch.acra.acra.BuildConfig;
import database.DatabaseHelper;
import debugapp.ActivitySurveyList;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static DatabaseHelper dataHelper;
    protected static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    ViewFlipper flvViewFlipperID;
    View.OnClickListener onclick;
    RecyclerView recyclerView;
    Context context;
    String versionName = "0.0", emp_type;

    String country,
            country_text,
            state,
            scheme_state,
            state_text,
            district,
            district_text,
            tehsil, tehsil_text;

    ArrayList<ItemNameBean> itemNameBeans;
    LinearLayout lin1, lin2;
    Adapter_item_list adapter_item_list;

    private LinearLayoutManager layoutManagerSubCategory;

    NavigationView navigationView;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    ProgressDialog progressBar;

    CardView cardSurveySiteID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataHelper = new DatabaseHelper(context);
        versionName = BuildConfig.VERSION_NAME;

        LoginBean loginBean = new LoginBean();

        emp_type = loginBean.getUsertype();

        Log.e("EMP", "&&&" + emp_type);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView username = (TextView) headerView.findViewById(R.id.user_name);
        TextView appversion = (TextView) headerView.findViewById(R.id.app_version);
        TextView projname = (TextView) headerView.findViewById(R.id.proj_name);

        username.setText(CustomUtility.getSharedPreferences(context, "username"));
        projname.setText(CustomUtility.getSharedPreferences(context, "projectname"));
        appversion.setText("Version " + versionName);

        cardSurveySiteID = findViewById(R.id.cardSurveySiteID);
        recyclerView = findViewById(R.id.item_list);

        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);

        flvViewFlipperID = (ViewFlipper) findViewById(R.id.flvViewFlipperID);

        flvViewFlipperID.setFlipInterval(3000); //set 1 seconds for interval time
        flvViewFlipperID.startFlipping();

        if (CustomUtility.isInternetOn()) {
            dataHelper.deleteDashboardData();
            new Dashboard().execute();
        } else {
            Toast.makeText(context, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        cardSurveySiteID.setOnClickListener(v -> {
            Intent mIntent = new Intent(context, ActivitySurveyList.class);
            startActivity(mIntent);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
            alertDialog.setTitle("Confirmation");
            alertDialog.setMessage("Are you sure you wish to Sign Out ?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
            return true;
        }

        if (id == R.id.action_state_city) {
            CustomUtility.getSharedPreferences(context, "usertype");
            if (CustomUtility.getSharedPreferences(context, "usertype").equalsIgnoreCase("02")) {
                if (CustomUtility.isInternetOn()) {
                    syncState();
                } else {
                    Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else if (id == R.id.nav_offlinedata) {
            Intent offlinedata = new Intent(context, OfflineData.class);
            startActivity(offlinedata);
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
            alertDialog.setTitle("Confirmation");
            alertDialog.setMessage("Are you sure you wish to Sign Out ?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } else if (id == R.id.nav_simreplace) {
            Intent intent = new Intent(context, SimCardOptions.class);
            startActivity(intent);
        } else if (id == R.id.nav_devicestatus) {
            Intent intent = new Intent(context, DeviceStatusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_siteaudit) {
            if (CustomUtility.getSharedPreferences(context, "usertype").equalsIgnoreCase("02")) {
                Intent intent = new Intent(context, SiteAuditList.class);
                startActivity(intent);
            } else {
                Toast.makeText(context, "You are not authorized for this.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_rejectsite) {
            Intent intent = new Intent(context, ActivityRejectSite.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        try {
            if (CustomUtility.isInternetOn()) {
                dataHelper.deleteLoginData();
                dataHelper.deleteLoginSelecData();
                dataHelper.deleteDashboardData();
                dataHelper.deleteRegistrationData();
                dataHelper.deleteInstallationListData();
                dataHelper.deleteSurveyListData();
                dataHelper.deleteInstallationData();
                dataHelper.deleteSurveyData();
                CustomUtility.setSharedPreference(context, "userid", "");
                CustomUtility.setSharedPreference(context, "username", "");
                CustomUtility.setSharedPreference(context, "usertype", "");
                CustomUtility.setSharedPreference(context, "projectid", "");
                CustomUtility.setSharedPreference(context, "loginid", "");
                CustomUtility.setSharedPreference(context, "CHECK_OTP_VARIFED", "N");
                OnBackPressed();
            } else {
                Toast.makeText(getApplicationContext(), "No internet Connection ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnBackPressed() {
        System.exit(0);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        itemNameBeans = new ArrayList<ItemNameBean>();
        itemNameBeans = dataHelper.getItemData();
        ItemNameBean itemNameBean = new ItemNameBean("000","Installation Offline");
        itemNameBeans.add(itemNameBean);
        Log.e("SIZE", "&&&&" + itemNameBeans.size());
        if (itemNameBeans != null && itemNameBeans.size() > 0) {
            lin1.setVisibility(View.VISIBLE);
            lin2.setVisibility(View.GONE);
            recyclerView.setAdapter(null);
            Log.e("SIZE", "&&&&" + itemNameBeans.size());
            adapter_item_list = new Adapter_item_list(context, itemNameBeans, onclick);

            layoutManagerSubCategory = new LinearLayoutManager(context);
            layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManagerSubCategory);
            recyclerView.setAdapter(adapter_item_list);
            adapter_item_list.notifyDataSetChanged();
        } else {
            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    @SuppressLint("StaticFieldLeak")
    private class Dashboard extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Please Wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("USERID", CustomUtility.getSharedPreferences(context, "userid")));
            param.add(new BasicNameValuePair("PROJECT_NO", CustomUtility.getSharedPreferences(context, "projectid")));
            param.add(new BasicNameValuePair("PROJECT_LOGIN_NO", CustomUtility.getSharedPreferences(context, "loginid")));
            String login_selec = null, project_no = null, process_no = null, process_nm = null;
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);
                login_selec = CustomHttpClient.executeHttpPost1(WebURL.DASHBOARD_DATA, param);
                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("mapp_dashboard");
                JSONArray ja = new JSONArray(obj1);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    project_no = jo.optString("project_no");
                    process_no = jo.optString("process_no");
                    process_nm = jo.optString("process_nm");
                    if (dataHelper.isRecordExist(dataHelper.TABLE_DASHBOARD, dataHelper.KEY_PROCESS_NO, process_no)) {
                        dataHelper.updateDashboard(project_no, process_no, process_nm);
                    } else {
                        dataHelper.insertDashboardData(project_no, process_no, process_nm);
                    }
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return login_selec;
        }

        @Override
        protected void onPostExecute(String result) {
            // write display tracks logic here
            onResume();
            progressDialog.dismiss();  // dismiss dialog
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionNetworkState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int permissionStorageWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionStorageRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //int permissionManageStorageRead = ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionNetworkState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionInternet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (permissionStorageWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionStorageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void syncState() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Downloading State Data...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;

        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {
                    try {
                        if (CustomUtility.isInternetOn()) {
                            progressBarStatus = 30;
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                            progressBarStatus = getStateData(MainActivity.this);
                            progressBarStatus = 100;
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "No internet Connection ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.dismiss();
                }
            }
        }).start();
    }

    public int getStateData(Context context) {
        int progressBarStatus = 0;
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.STATE_DATA, param);
            JSONArray ja_state = new JSONArray(obj);
            dataHelper.deleteStateSearchHelpData();
            for (int i = 0; i < ja_state.length(); i++) {
                JSONObject jo_state = ja_state.getJSONObject(i);
                country = jo_state.optString("country");
                country_text = jo_state.optString("countrytext");
                state = jo_state.optString("state");
                state_text = jo_state.optString("statetext");
                district = jo_state.optString("district");
                district_text = jo_state.optString("districttext");
                tehsil = jo_state.optString("tehsil");
                tehsil_text = jo_state.optString("tehsiltext");
                dataHelper.insertStateData(country, country_text, state, state_text, district, district_text, tehsil, tehsil_text);
            }
        } catch (Exception e) {
            Log.d("msg", "" + e);
        }
        return progressBarStatus;
    }
}
