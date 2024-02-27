package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;

import adapter.Adapter_Beneficiary_List;
import bean.BeneficiaryRegistrationBean;
import database.DatabaseHelper;

public class beneficiaryRegistrationList extends AppCompatActivity {
    private Toolbar mToolbar;
    TextView noDataFound;
    SearchView searchUser;
    RelativeLayout searchRelative;
    LinearLayout linear1;
    FloatingActionButton newBeneficiaryAddButton;
    Adapter_Beneficiary_List adapterBeneficiaryList;
    Context context;
    RecyclerView beneficiaryListView;
    DatabaseHelper db;
    ArrayList<BeneficiaryRegistrationBean> beneficiaryBean;

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
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        newBeneficiaryAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), beneficiaryRegistrationForm.class);
            startActivity(intent);
        });
    }


    private void getValueFromDatatbase() {
        db = new DatabaseHelper(this);
            beneficiaryBean = new ArrayList<BeneficiaryRegistrationBean>();
            beneficiaryBean = db.getBeneficiaryListData();
            Log.e("SIZE1", "&&&&" + beneficiaryBean.size());

            if (beneficiaryBean != null && beneficiaryBean.size() > 0) {
                noDataFound.setVisibility(View.GONE);
                Log.e("SIZE", "&&&&" + beneficiaryBean.size());
                adapterBeneficiaryList = new Adapter_Beneficiary_List(context, beneficiaryBean);
                beneficiaryListView.setHasFixedSize(true);
                beneficiaryListView.setAdapter(adapterBeneficiaryList);

            } else {
                Log.e("SIZE===>", "&&&&" + beneficiaryBean.size());
                linear1.setVisibility(View.GONE);
                noDataFound.setVisibility(View.VISIBLE);
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}