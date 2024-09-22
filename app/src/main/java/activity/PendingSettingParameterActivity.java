package activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.PendingSetParameterAdapter;
import bean.ParameterSettingListModel;
import bluetoothpaireDevice.SetParameter.PairedDeviceActivity;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import debugapp.PendingInstallationModel;
import utility.CustomUtility;
import webservice.WebURL;

public class PendingSettingParameterActivity extends AppCompatActivity implements PendingSetParameterAdapter.ItemCLickListner {

    private RecyclerView customersList;
    private Toolbar mToolbar;
    ArrayList<ParameterSettingListModel.InstallationDatum> pendingInstallationModels;
    AlertDialog alertDialog;

    TextView noDataFound;

    SearchView searchUser;

    PendingSetParameterAdapter pendingSetParameterAdapter;

    RelativeLayout searchRelative;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_parameter_pending);
        Init();
        listner();

    }


    private void Init() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        mToolbar = findViewById(R.id.toolbar);
        customersList = findViewById(R.id.customersList);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.pendingSettingParamters));
        if (CustomUtility.isInternetOn(getApplicationContext())) {
            getcustomersList();
        } else {
            setAdapter();
            CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), getApplicationContext());
        }
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        searchRelative.setOnClickListener(v -> {
            searchUser.setFocusableInTouchMode(true);
            searchUser.requestFocus();
            searchUser.onActionViewExpanded();

        });

        ImageView searchIcon = searchUser.findViewById(R.id.search_button);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_search_24));
        searchIcon.setColorFilter(getResources().getColor(R.color.colorPrimary));

        ImageView searchClose = searchUser.findViewById(R.id.search_close_btn);
        searchClose.setColorFilter(getResources().getColor(R.color.colorPrimary));


        EditText searchEditText = searchUser.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._14sdp));

        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (pendingSetParameterAdapter != null) {
                    if (!query.isEmpty()) {
                        pendingSetParameterAdapter.getFilter().filter(query);
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (pendingSetParameterAdapter != null) {
                    if (!newText.isEmpty()) {
                        pendingSetParameterAdapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });

        searchClose.setOnClickListener(v -> searchUser.onActionViewCollapsed());

    }


    private void getcustomersList() {
        CustomUtility.showProgressDialogue(PendingSettingParameterActivity.this);
        pendingInstallationModels = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.PendingSettingList + "?project_no=" + CustomUtility.getSharedPreferences(getApplicationContext(), "projectid") + "&userid=" + CustomUtility.getSharedPreferences(getApplicationContext(), "userid") + "&project_login_no=01", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                CustomUtility.hideProgressDialog(PendingSettingParameterActivity.this);


                if (response.toString() != null && !response.toString().isEmpty()) {
                    ParameterSettingListModel parameterSettingListModel = new Gson().fromJson(response.toString(), ParameterSettingListModel.class);
                    if (parameterSettingListModel.getInstallationData().size()>0) {

                        for (int i=0; i<parameterSettingListModel.getInstallationData().size(); i++){

                            if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_SETTING_PENDING_LIST, DatabaseHelper.KEY_BILL_NO, parameterSettingListModel.getInstallationData().get(i).getVbeln())) {
                                databaseHelper.insertSettingPendingData(parameterSettingListModel.getInstallationData().get(i));

                            }
                        }


                       setAdapter();
                    } else {
                        noDataFound.setVisibility(View.VISIBLE);
                        customersList.setVisibility(View.GONE);
                    }

                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                    customersList.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomUtility.hideProgressDialog(PendingSettingParameterActivity.this);
                noDataFound.setVisibility(View.VISIBLE);
                customersList.setVisibility(View.GONE);
                Log.e("error", String.valueOf(error));

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void setAdapter() {
        pendingInstallationModels = databaseHelper.getPendingSettingList();
        if(pendingInstallationModels.size()>0) {
            pendingSetParameterAdapter = new PendingSetParameterAdapter(getApplicationContext(), pendingInstallationModels, noDataFound);
            customersList.setHasFixedSize(true);
            customersList.setAdapter(pendingSetParameterAdapter);
            pendingSetParameterAdapter.ItemCLick(PendingSettingParameterActivity.this);
            noDataFound.setVisibility(View.GONE);
            customersList.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void itemClickListner(ParameterSettingListModel.InstallationDatum pendingSettingList, int position) {
        WebURL.BT_DEVICE_NAME = "";
        WebURL.BT_DEVICE_MAC_ADDRESS = "";
        Constant.Bluetooth_Activity_Navigation = 1;///Debug

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            if (CustomUtility.pairedDeviceListGloable(getApplicationContext())) {
                if (WebURL.BT_DEVICE_NAME.equalsIgnoreCase("") || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), PairedDeviceActivity.class);
                    intent.putExtra(Constant.pendingSettingData,pendingSettingList);
                    intent.putExtra(Constant.ControllerSerialNumber, "");
                    intent.putExtra(Constant.debugDataExtract, "false");
                    intent.putExtra(Constant.isPeramterSet, "true");
                    startActivity(intent);
                }
            } else {
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            }
        } else {
            startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
        }
    }
}