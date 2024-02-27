package activity;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;

import adapter.Adapter_Beneficiary_List;
import adapter.Adapter_Installation_list;
import adapter.ComplaintInstAdapter;
import bean.BeneficiaryRegistrationBean;
import bean.ComplaintInstModel;
import bean.InstallationListBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.WebURL;

public class beneficiaryRegistrationList extends AppCompatActivity {
    private Toolbar mToolbar;
    TextView noDataFound;
    SearchView searchUser;
    RelativeLayout searchRelative;
    LinearLayout linear1;
    FloatingActionButton newBeneficiaryAddButton;
    Adapter_Beneficiary_List adapterBeneficiaryList;
    Intent intent;
    Context context;
    RecyclerView beneficiaryListView;
    DatabaseHelper db;
    ArrayList<BeneficiaryRegistrationBean> beneficiaryBean;
    private LinearLayoutManager layoutManagerSubCategory;
    String serialID="";


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
        getValueFromDatatbase();
    }

    private void Init() {

        context = this;
        linear1=findViewById(R.id.linear1);
        mToolbar = findViewById(R.id.toolbar);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        beneficiaryListView=findViewById(R.id.beneficiaryListView);
        newBeneficiaryAddButton=findViewById(R.id.newBeneficiaryAddButton);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.beneficiary_registration_form);

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
                intent=new Intent(getApplicationContext(), beneficiaryRegistrationForm.class);
                startActivity(intent);
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
            Log.e("SIZE1", "&&&&" + beneficiaryBean.size());

            if (beneficiaryBean != null && beneficiaryBean.size() > 0) {
                noDataFound.setVisibility(View.GONE);
                Log.e("SIZE", "&&&&" + beneficiaryBean.size());
                adapterBeneficiaryList = new Adapter_Beneficiary_List(context, beneficiaryBean);
                layoutManagerSubCategory = new LinearLayoutManager(context);
                layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                beneficiaryListView.setLayoutManager(layoutManagerSubCategory);
                beneficiaryListView.setAdapter(adapterBeneficiaryList);
                adapterBeneficiaryList.notifyDataSetChanged();
                WebURL.CHECK_DATA_UNOLAD = 0;
            } else {
                Log.e("SIZE===>", "&&&&" + beneficiaryBean.size());
                linear1.setVisibility(View.GONE);
                noDataFound.setVisibility(View.VISIBLE);
            }
        }
    }
}