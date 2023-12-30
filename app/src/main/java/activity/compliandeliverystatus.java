package activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;
import java.util.Locale;

import adapter.DeliveryListAdapter;
import bean.ComplianDeliverModel;
import utility.CustomUtility;
import webservice.WebURL;

public class compliandeliverystatus extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView complainDeliverlyList;
    private Toolbar mToolbar;
    TextView fromDateTxt, toDateTxt ,submitBtn,noDataFound;

    String sendFromDate ,sendToDate,spinner_type_text,statustxt;
    ProgressDialog progressDialog;
    DeliveryListAdapter adapter;
    SearchView searchUser;    RelativeLayout fromDateRl, toDateRl ;
    RelativeLayout searchRelative;
    private Spinner spinner_type;
    int index1;    SimpleDateFormat sdf, senddf;  String myFormat = "dd.MM.yyyy"; String sendFormat = "yyyyMMdd";
    List<String> list = null;
    List<ComplianDeliverModel.ComplaintDatum> complianDeliveryListModels;
    TextInputLayout date;
   // List<TravelListRequest.Response> travelListModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complian_delivery_status);

        list = new ArrayList<>();
        mToolbar =  findViewById(R.id.toolbar);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        fromDateTxt = findViewById(R.id.fromDateTxt);
        toDateTxt = findViewById(R.id.toDateTxt);
        complainDeliverlyList = findViewById(R.id.deliveryList);
        spinner_type = findViewById(R.id.spinner_type);
        fromDateRl = findViewById(R.id.fromDateRl);
        toDateRl = findViewById(R.id.toDateRl);
        submitBtn = findViewById(R.id.submitBtn);
        noDataFound = findViewById(R.id.noDataFound);
        sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        senddf = new SimpleDateFormat(sendFormat, Locale.getDefault());
        fromDateTxt.setText( sdf.format(Calendar.getInstance().getTime()));
        toDateTxt.setText( sdf.format(Calendar.getInstance().getTime()));
        sendFromDate =  senddf.format(Calendar.getInstance().getTime());
        sendToDate =  senddf.format(Calendar.getInstance().getTime());



        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.complain_delivery_status));

        getUserTypeValue();
        setlisnser();
        setSpinner();
        callAPI();
    }

    private void setlisnser() {
        fromDateRl.setOnClickListener(this);
        toDateRl.setOnClickListener(this);
    }

    private void setSpinner() {

        spinner_type.setPrompt(getResources().getString(R.string.select_title_type));
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

        // attaching data adapter to spinner
        spinner_type.setAdapter(dataAdapter);



        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index1 = arg0.getSelectedItemPosition();
                spinner_type_text = spinner_type.getSelectedItem().toString();
                if(spinner_type_text.equalsIgnoreCase("Pending")){
                    statustxt = "PEND";
                }else {
                    statustxt = "DONE";
                }
                Log.e("spinner_type_text=====",spinner_type_text);
                Log.e("statustxt=====",statustxt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void getUserTypeValue() {
        list.add(getApplicationContext().getResources().getString(R.string.select_title_type));
        list.add(getApplicationContext().getResources().getString(R.string.pending));
        list.add(getApplicationContext().getResources().getString(R.string.complete));

    }

    private void callAPI() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!spinner_type_text.equalsIgnoreCase("Select Status Type")){
                    sendData();
                }else
                {
                    CustomUtility.showToast(getApplicationContext(),getResources().getString(R.string.select_title_type));
                }

            }
        });
    }

     private void sendData()  {
        progressDialog = new ProgressDialog(compliandeliverystatus.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        complianDeliveryListModels = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("Url=========>", WebURL.ComplainRequestListURL +  CustomUtility.getSharedPreferences(compliandeliverystatus.this, "userid") +"&frm_dt=" +sendFromDate+"&to_dt="+sendToDate + "&status=" + statustxt + "&project_login_no=01&project_no=" + CustomUtility.getSharedPreferences(compliandeliverystatus.this, "projectid") );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebURL.ComplainRequestListURL +  CustomUtility.getSharedPreferences(compliandeliverystatus.this, "userid") +"&frm_dt=" +sendFromDate +"&to_dt="+ sendToDate + "&status=" + statustxt + "&project_login_no=01&project_no=" + CustomUtility.getSharedPreferences(compliandeliverystatus.this, "projectid"), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject  response) {
                Log.e("resp=====>",response.toString());
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(!response.toString().isEmpty()) {
                    ComplianDeliverModel compliandeliveryList = new Gson().fromJson(response.toString(), ComplianDeliverModel.class);
                    if (compliandeliveryList.getResponse() != null && compliandeliveryList.getResponse().size() > 0) {
                        complianDeliveryListModels = compliandeliveryList.getResponse() ;
                        adapter = new DeliveryListAdapter(getApplicationContext(), complianDeliveryListModels, noDataFound);
                        complainDeliverlyList.setHasFixedSize(true);
                        complainDeliverlyList.setAdapter(adapter);

                        noDataFound.setVisibility(View.GONE);
                        complainDeliverlyList.setVisibility(View.VISIBLE);

                        Log.e("travelListModellength", String.valueOf(complianDeliveryListModels.size()));
                    } else {
                        noDataFound.setVisibility(View.VISIBLE);
                        complainDeliverlyList.setVisibility(View.GONE);
                    }

                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                    complainDeliverlyList.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                noDataFound.setVisibility(View.VISIBLE);
                complainDeliverlyList.setVisibility(View.GONE);
                Log.e("error", String.valueOf(error));

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
              2,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fromDateRl:
                selectDate(true);
                break;

            case R.id.toDateRl:
                selectDate(false);
                break;


        }
    }

    public void selectDate(boolean isFromDate) {

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(compliandeliverystatus.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day_of_month);


                if (isFromDate) {
                    fromDateTxt.setText(sdf.format(calendar.getTime()));
                    sendFromDate = senddf.format(calendar.getTime());

                } else {
                    toDateTxt.setText(sdf.format(calendar.getTime()));
                    sendToDate = senddf.format(calendar.getTime());
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }
}