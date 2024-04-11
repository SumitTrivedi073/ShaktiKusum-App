package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
    RelativeLayout searchRelative;
    FloatingActionButton newBeneficiaryAddButton;
    Adapter_Beneficiary_List adapterBeneficiaryList;
    Context context;
    RecyclerView beneficiaryListView;
    DatabaseHelper db;
    ArrayList<BeneficiaryRegistrationBean> beneficiaryBean;

    SearchView searchUser;

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
        mToolbar = findViewById(R.id.toolbar);
        noDataFound = findViewById(R.id.noDataFound);
        beneficiaryListView = findViewById(R.id.beneficiaryListView);
        newBeneficiaryAddButton = findViewById(R.id.newBeneficiaryAddButton);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
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
                Intent intent = new Intent(getApplicationContext(), beneficiaryRegistrationForm.class);
                startActivity(intent);
            }
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

        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapterBeneficiaryList != null) {
                    if(!query.isEmpty()) {
                        adapterBeneficiaryList.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapterBeneficiaryList != null) {
                    if(!newText.isEmpty()) {
                        adapterBeneficiaryList.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });

        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchUser.onActionViewCollapsed();
                if (adapterBeneficiaryList != null) {
                    adapterBeneficiaryList.getFilter().filter("");
                }
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

            beneficiaryBean = new ArrayList<BeneficiaryRegistrationBean>();
            beneficiaryBean = db.getBeneficiaryListData();
            if (beneficiaryBean != null && beneficiaryBean.size() > 0) {
                beneficiaryListView.setVisibility(View.VISIBLE);
                noDataFound.setVisibility(View.GONE);
                Log.e("SIZE", "&&&&" + beneficiaryBean.size());
                adapterBeneficiaryList = new Adapter_Beneficiary_List(context, beneficiaryBean,noDataFound);
                beneficiaryListView.setHasFixedSize(true);
                beneficiaryListView.setAdapter(adapterBeneficiaryList);
            } else {
                beneficiaryListView.setVisibility(View.GONE);
                noDataFound.setVisibility(View.VISIBLE);
            }
        }

}