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

import activity.InstallationInitial;
import activity.beneficiaryRegistrationForm;
import bean.BeneficiaryRegistrationBean;
import bean.InstallationListBean;
import database.DatabaseHelper;
import utility.CustomUtility;

public class Adapter_Beneficiary_List  extends RecyclerView.Adapter<Adapter_Beneficiary_List.HomeCategoryViewHolder> {
    DatabaseHelper db;
    private final Context mcontext;
    private final ArrayList<BeneficiaryRegistrationBean> responseList;
    private List<BeneficiaryRegistrationBean> SearchesList = null;


    public Adapter_Beneficiary_List(Context context, ArrayList<BeneficiaryRegistrationBean> responseList) {
        this.mcontext = context;
        this.responseList = responseList;
        db = new DatabaseHelper(mcontext);
        this.SearchesList = new ArrayList<BeneficiaryRegistrationBean>();
        this.SearchesList.addAll(responseList);
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
                Bundle extras = new Bundle();
                extras.putString("serial_id", responseList.get(position).getSerialId());
                extras.putString("family_id", responseList.get(position).getFamilyId());
                extras.putString("applicant_name", responseList.get(position).getBeneficiaryFormApplicantName());
                extras.putString("applicant_father_name", responseList.get(position).getApplicantFatherName());
                extras.putString("applicant_mobile_no", responseList.get(position).getApplicantMobile());
                extras.putString("applicant_village", responseList.get(position).getApplicantVillage());
                extras.putString("applicant_block", responseList.get(position).getApplicantBlock());
                extras.putString("applicant_tehsil", responseList.get(position).getApplicantTehsil());
                extras.putString("applicant_district", responseList.get(position).getApplicantDistrict());
                extras.putString("pump_capacity", responseList.get(position).getPumpCapacity());
                extras.putString("pump_ac_dc", responseList.get(position).getPumpAcDc());
                extras.putString("pump_type", responseList.get(position).getPumpType());
                extras.putString("controller_type", responseList.get(position).getControllerType());
                extras.putString("applicant_account_no", responseList.get(position).getApplicantAccountNo());
                extras.putString("applicant_ifsc_code", responseList.get(position).getApplicantIFSC());

                in.putExtras(extras);
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

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView serial_id, applicant_name,applicant_mobile;
        CardView cardView;
        public HomeCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            serial_id=itemView.findViewById(R.id.serial_id);
            applicant_name=itemView.findViewById(R.id.applicant_name);
            applicant_mobile=itemView.findViewById(R.id.applicant_mobile);
            cardView=itemView.findViewById(R.id.card_view);
        }
    }
}