package activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapter.DeliveryListAdapter;
import bean.ComplianDeliverModel;
import utility.CustomUtility;
import webservice.WebURL;

public class compliandeliverystatus extends AppCompatActivity {

    private RecyclerView complainDeliverlyList;
    private Toolbar mToolbar;
    EditText date_from, date_to;
    TextView submitBtn,noDataFound;
    ImageButton bt_frm, bt_to;
    String sendFromDate ,sendToDate,spinner_type_text,statustxt;
    ProgressDialog progressDialog;
    DeliveryListAdapter adapter;
    SearchView searchUser;
    RelativeLayout searchRelative;
    private Spinner spinner_type;
    int index1;
    List<String> list = null;
    List<ComplianDeliverModel.ComplaintDatum> complianDeliveryListModels;
   // List<TravelListRequest.Response> travelListModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complian_delivery_status);

        list = new ArrayList<>();
        mToolbar =  findViewById(R.id.toolbar);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        date_from = findViewById(R.id.from);
        date_to =  findViewById(R.id.to);
        bt_frm =  findViewById(R.id.bt_frm);
        bt_to=  findViewById(R.id.bt_to);
        complainDeliverlyList = findViewById(R.id.deliveryList);
        spinner_type = findViewById(R.id.spinner_type);
        submitBtn = findViewById(R.id.submitBtn);
        noDataFound = findViewById(R.id.noDataFound);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.complain_delivery_status));

        getUserTypeValue();
        setSpinner();
        dateCode();
        callAPI();
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

                if(date_from.getText().toString().isEmpty() || date_from.getText().toString().equalsIgnoreCase("Travel From")){
                    CustomUtility.ShowToast("From Date",getApplicationContext());
                }else if(date_to.getText().toString().isEmpty() || date_to.getText().toString().equalsIgnoreCase("Travel To")){
                    CustomUtility.ShowToast("To Date",getApplicationContext());
                }else{
                     sendData();
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
                WebURL.ComplainRequestListURL +  CustomUtility.getSharedPreferences(compliandeliverystatus.this, "userid") +"&frm_dt=" +sendFromDate+"&to_dt="+sendToDate + "&status=" + statustxt + "&project_login_no=01&project_no=" + CustomUtility.getSharedPreferences(compliandeliverystatus.this, "projectid"), null, new Response.Listener<JSONObject>() {

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

    private void dateCode() {

        bt_frm.setOnClickListener(view -> {

            Calendar currentDate;
            int mDay, mMonth, mYear;
            currentDate = Calendar.getInstance();
            mDay = currentDate.get(Calendar.DAY_OF_MONTH);
            mMonth = currentDate.get(Calendar.MONTH);
            mYear = currentDate.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(compliandeliverystatus.this, (datePicker, i, i1, i2) -> {
                i1 = i1 + 1;

                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
                    String selectedDate = i2 + "/" + i1 + "/" + i;

                    Date date1 = sdf.parse(selectedDate);
                    assert date1 != null;
                    date_from.setText(sdf.format(date1));

                    sendFromDate = sdf1.format(date1);
                    Log.e("sendFromDate",sendFromDate);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.setTitle("Travel From");
            datePickerDialog.show();
        });


        bt_to.setOnClickListener(view -> {
            Calendar currentDate;
            int mDay, mMonth, mYear;
            currentDate = Calendar.getInstance();

            mDay = currentDate.get(Calendar.DAY_OF_MONTH);
            mMonth = currentDate.get(Calendar.MONTH);
            mYear = currentDate.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(compliandeliverystatus.this, (datePicker, i, i1, i2) -> {
                i1 = i1 + 1;

                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
                    String selectedDate = i2 + "/" + i1 + "/" + i;

                    Date date1 = sdf.parse(selectedDate);

                    assert date1 != null;
                    date_to.setText(sdf.format(date1));

                    sendToDate =sdf1.format(date1);
                    Log.e("sendToDate",sendToDate);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }


            }, mYear, mMonth, mDay);
            datePickerDialog.setTitle("Travel To");
            datePickerDialog.show();
        });

        searchRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser.setFocusableInTouchMode(true);
                searchUser.requestFocus();
                searchUser.onActionViewExpanded();

            }
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
/*
        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) {
                    if(!query.isEmpty()) {
                        adapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    if(!newText.isEmpty()) {
                        adapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });*/

        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchUser.onActionViewCollapsed();
            }
        });

    }
}