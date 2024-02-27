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
import utility.CustomUtility;

public class Adapter_Beneficiary_List  extends RecyclerView.Adapter<Adapter_Beneficiary_List.HomeCategoryViewHolder> {
    DatabaseHelper db;
    private final Context mcontext;
    private final ArrayList<BeneficiaryRegistrationBean> responseList;
    private List<BeneficiaryRegistrationBean> SearchesList = null;


    public Adapter_Beneficiary_List(Context context, ArrayList<BeneficiaryRegistrationBean> responseList) {
        this.SearchesList = new ArrayList<>();
        this.SearchesList.addAll(responseList);
        this.mcontext = context;
        this.responseList = responseList;
        db = new DatabaseHelper(mcontext);

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
            if (!TextUtils.isEmpty(responseList.get(position).getSerialId())) {
                holder.serial_id.setText(responseList.get(position).getSerialId());
            }if (!TextUtils.isEmpty(responseList.get(position).getBeneficiaryFormApplicantName())) {
                holder.applicant_name.setText(responseList.get(position).getBeneficiaryFormApplicantName());
            }if (!TextUtils.isEmpty(responseList.get(position).getApplicantMobile())) {
                holder.applicant_mobile.setText(responseList.get(position).getApplicantMobile());
            }
            Log.e("responseList.serialId==>",responseList.get(position).getSerialId());
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
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        responseList.clear();
        if (charText.length() == 0) {
            responseList.addAll(SearchesList);
        } else {
            for (BeneficiaryRegistrationBean cs : SearchesList) {
                if (cs.getSerialId().toLowerCase(Locale.getDefault()).contains(charText) || cs.getBeneficiaryFormApplicantName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    responseList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
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