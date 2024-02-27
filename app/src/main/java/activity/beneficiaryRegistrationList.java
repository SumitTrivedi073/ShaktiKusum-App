package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.Locale;

import adapter.Adapter_Beneficiary_List;
import bean.BeneficiaryRegistrationBean;
import database.DatabaseHelper;

public class beneficiaryRegistrationList extends AppCompatActivity {
    private Toolbar mToolbar;
    TextView noDataFound;
    EditText editsearch;
    RelativeLayout searchRelative;
    LinearLayout linear2;
    FloatingActionButton newBeneficiaryAddButton;
    Adapter_Beneficiary_List adapterBeneficiaryList;
    Intent intent;
    Context context;
    RecyclerView beneficiaryListView;
    DatabaseHelper db;
    ArrayList<BeneficiaryRegistrationBean> beneficiaryBean;
    private LinearLayoutManager layoutManagerSubCategory;
    String serialID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_registration_list);
        Init();

        listner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onresume1==>", "hello");
        getValueFromDatatbase();
        Log.e("onresume2==>", "hello");
    }

    private void Init() {

        context = this;
        mToolbar = findViewById(R.id.toolbar);
        noDataFound = findViewById(R.id.noDataFound);
        linear2 = findViewById(R.id.linear2);
        editsearch = findViewById(R.id.search);
        searchRelative = findViewById(R.id.searchRelative);
        beneficiaryListView = findViewById(R.id.beneficiaryListView);
        newBeneficiaryAddButton = findViewById(R.id.newBeneficiaryAddButton);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.beneficiary_registration_list);

    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        newBeneficiaryAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), beneficiaryRegistrationForm.class);
                startActivity(intent);
            }
        });
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                try {
                    adapterBeneficiaryList.filter(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getValueFromDatatbase() {
        db = new DatabaseHelper(this);
        if (db.getcount(DatabaseHelper.TABLE_BENEFICIARY_REGISTRATION)) {
            beneficiaryBean = new ArrayList<BeneficiaryRegistrationBean>();
            beneficiaryBean = db.getBeneficiaryListData();
            Log.e("SIZE1234", "&&&&" + beneficiaryBean.size());
            if (beneficiaryBean != null && beneficiaryBean.size() > 0) {
                linear2.setVisibility(View.VISIBLE);
                noDataFound.setVisibility(View.GONE);
                Log.e("SIZE", "&&&&" + beneficiaryBean.size());
                adapterBeneficiaryList = new Adapter_Beneficiary_List(context, beneficiaryBean);
                beneficiaryListView.setHasFixedSize(true);
                beneficiaryListView.setAdapter(adapterBeneficiaryList);
            } else {
                Log.e("SIZE===>", "&&&&");
                linear2.setVisibility(View.GONE);
                noDataFound.setVisibility(View.VISIBLE);
            }
        }
    }
}