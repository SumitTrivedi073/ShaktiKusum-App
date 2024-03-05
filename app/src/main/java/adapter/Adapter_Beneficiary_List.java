package adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.InstallationInitial;
import activity.beneficiaryRegistrationForm;
import bean.BeneficiaryRegistrationBean;
import bean.InstallationListBean;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import debugapp.PendingInstallationModel;
import utility.CustomUtility;

public class Adapter_Beneficiary_List  extends RecyclerView.Adapter<Adapter_Beneficiary_List.HomeCategoryViewHolder>implements Filterable {
    private final Context mcontext;
    private  List<BeneficiaryRegistrationBean> responseList;
    private List<BeneficiaryRegistrationBean> SearchesList = new ArrayList<>();
    TextView noDataFound;


    public Adapter_Beneficiary_List(Context context, List<BeneficiaryRegistrationBean> responseList,TextView noDataFound) {
        this.SearchesList = new ArrayList<>();
        this.SearchesList.addAll(responseList);
        this.mcontext = context;
        this.responseList = responseList;
        this.noDataFound = noDataFound;


    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override
    public Adapter_Beneficiary_List.HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.activity_adapter_beneficiary_list, parent, false);
        return new Adapter_Beneficiary_List.HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Beneficiary_List.HomeCategoryViewHolder holder, int position) {
        try {
            if (!responseList.get(position).getSerialId().isEmpty()) {
                holder.serial_id.setText(responseList.get(position).getSerialId());
            }if (!responseList.get(position).getBeneficiaryFormApplicantName().isEmpty()) {
                holder.applicant_name.setText(responseList.get(position).getBeneficiaryFormApplicantName());
            }if (!responseList.get(position).getApplicantMobile().isEmpty()) {
                holder.applicant_mobile.setText(responseList.get(position).getApplicantMobile());
            }
            holder.cardView.setOnClickListener(view -> {
                Intent in = new Intent(mcontext, beneficiaryRegistrationForm.class);
                in.putExtra(Constant.beneficiaryData,responseList.get(position));
                mcontext.startActivity(in);
            });

        }
            catch(Exception e){
                e.printStackTrace();
            }

    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    responseList = SearchesList;
                } else {
                    List<BeneficiaryRegistrationBean> filteredList = new ArrayList<>();
                    for (BeneficiaryRegistrationBean row : SearchesList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSerialId().toLowerCase().contains(charString.toLowerCase())||row.getApplicantMobile().toLowerCase().contains(charString.toLowerCase())
                                ||row.getBeneficiaryFormApplicantName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    responseList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = responseList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                responseList = (ArrayList<BeneficiaryRegistrationBean>) filterResults.values;
                if (responseList.size()>0){
                    noDataFound.setVisibility(View.GONE);
                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView serial_id, applicant_name,applicant_mobile;
        CardView cardView;
        ImageView edit;
        public HomeCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            serial_id=itemView.findViewById(R.id.serial_id);
            applicant_name=itemView.findViewById(R.id.applicant_name);
            applicant_mobile=itemView.findViewById(R.id.applicant_mobile);
            cardView=itemView.findViewById(R.id.card_view);
            edit=itemView.findViewById(R.id.edit);
        }
    }
}