package activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
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
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.Adapter_item_list;
import bean.ItemNameBean;
import bean.LoginBean;
import database.DatabaseHelper;
import debugapp.ActivitySurveyList;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

@SuppressWarnings("resource")
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static DatabaseHelper dataHelper;
    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 100;
    ViewFlipper flvViewFlipperID;
    View.OnClickListener onclick;
    RecyclerView recyclerView;
    Context context;
    String versionName = "0.0", emp_type;

    String country, country_text, state, state_text, district, district_text, tehsil, tehsil_text;

    ArrayList<ItemNameBean> itemNameBeans = new ArrayList<>();
    LinearLayout lin1, lin2;
    Adapter_item_list adapter_item_list;

    NavigationView navigationView;
    private int progressBarStatus = 0;
    private final Handler progressBarHandler = new Handler();
    ProgressDialog progressBar;

    CardView cardSurveySiteID;

    TextView pendingFeedback;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataHelper = new DatabaseHelper(context);

        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = getApplicationContext().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert pkgInfo != null;
        versionName = String.valueOf(pkgInfo.versionName);

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
        TextView username =  headerView.findViewById(R.id.user_name);
        TextView appversion =  headerView.findViewById(R.id.app_version);
        TextView projname =  headerView.findViewById(R.id.proj_name);

        username.setText(CustomUtility.getSharedPreferences(context, "username"));
        projname.setText(CustomUtility.getSharedPreferences(context, "projectname"));
        appversion.setText("Version " + versionName);
        CustomUtility.setSharedPreference(context, "CHECK_OTP_VERIFED", "Y");


        cardSurveySiteID = findViewById(R.id.cardSurveySiteID);
        recyclerView = findViewById(R.id.item_list);
        pendingFeedback = findViewById(R.id.pendingFeedback);
        lin1 =  findViewById(R.id.lin1);
        lin2 =  findViewById(R.id.lin2);

        flvViewFlipperID =  findViewById(R.id.flvViewFlipperID);

        flvViewFlipperID.setFlipInterval(3000); //set 1 seconds for interval time
        flvViewFlipperID.startFlipping();

        if (CustomUtility.isInternetOn(getApplicationContext())) {
            new Dashboard().execute();
        } else {
            getListData();
            Toast.makeText(context, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        cardSurveySiteID.setOnClickListener(v -> {
            Intent mIntent = new Intent(context, ActivitySurveyList.class);
            startActivity(mIntent);
        });

        pendingFeedback.setOnClickListener(view -> {
            Intent mIntent = new Intent(context, PendingFeedbackActivity.class);
            startActivity(mIntent);
        });

        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkUpdate();
    }

    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if  (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, MainActivity.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
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
            alertDialog.setPositiveButton("Yes", (dialog, which) -> logout());
            alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            alertDialog.show();
            return true;
        }

        if (id == R.id.action_state_city) {
            CustomUtility.getSharedPreferences(context, "usertype");
            if (CustomUtility.getSharedPreferences(context, "usertype").equalsIgnoreCase("02")) {
                if (CustomUtility.isInternetOn(getApplicationContext())) {
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
            alertDialog.setPositiveButton("Yes", (dialog, which) -> logout());
            alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
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
            if (CustomUtility.isInternetOn(getApplicationContext())) {
                dataHelper = new DatabaseHelper(context);
                dataHelper.deleteLoginData();
                dataHelper.deleteDashboardData();
                dataHelper.deleteRegistrationData();
                dataHelper.deleteInstallationListData();
                dataHelper.deleteSurveyListData();
                dataHelper.deleteInstallationData();
                dataHelper.deleteSurveyData();
                dataHelper.deleteInstallationImages();
                dataHelper.deleteSiteAuditImages();
                dataHelper.deleteUnloadingImages();
                CustomUtility.clearSharedPrefrences(context);

                Intent intent = new Intent(context, Login.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "No internet Connection ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getListData() {

        if (dataHelper.getItemData() != null && dataHelper.getItemData().size() > 0) {
        itemNameBeans = dataHelper.getItemData();
        ItemNameBean itemNameBean = new ItemNameBean("000","Installation Offline");
        itemNameBeans.add(itemNameBean);


            lin1.setVisibility(View.VISIBLE);
            lin2.setVisibility(View.GONE);

            adapter_item_list = new Adapter_item_list(context, itemNameBeans, onclick);

            LinearLayoutManager layoutManagerSubCategory = new LinearLayoutManager(context);
            layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManagerSubCategory);
            recyclerView.setAdapter(adapter_item_list);
            adapter_item_list.notifyDataSetChanged();

        } else {
            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);
        }
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
            final ArrayList<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("USERID", CustomUtility.getSharedPreferences(context, "userid")));
            param.add(new BasicNameValuePair("PROJECT_NO", CustomUtility.getSharedPreferences(context, "projectid")));
            param.add(new BasicNameValuePair("PROJECT_LOGIN_NO", CustomUtility.getSharedPreferences(context, "loginid")));
            String login_selec = null, project_no, process_no , process_nm ;
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
                    if (dataHelper.isRecordExist(DatabaseHelper.TABLE_DASHBOARD, DatabaseHelper.KEY_PROCESS_NO, process_no)) {
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
            getListData();
            progressDialog.dismiss();  // dismiss dialog
        }
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

        new Thread(() -> {
            while (progressBarStatus < 100) {
                try {
                    if (CustomUtility.isInternetOn(getApplicationContext())) {
                        progressBarStatus = 30;
                        progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));
                        getStateData(MainActivity.this);
                        progressBarStatus = 100;
                        progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));
                    } else {
                        Toast.makeText(getApplicationContext(), "No internet Connection ", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressBar.dismiss();
        }).start();
    }

    public void getStateData(Context context) {

        DatabaseHelper dataHelper = new DatabaseHelper(context);
        final ArrayList<NameValuePair> param = new ArrayList<>();
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

    }
}
