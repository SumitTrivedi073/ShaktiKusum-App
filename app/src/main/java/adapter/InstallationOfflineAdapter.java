package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import bean.InstallationOfflineBean;
import database.DatabaseHelper;
import com.shaktipumplimited.shaktikusum.InstallationDetailOfflineActivity;
import com.shaktipumplimited.shaktikusum.R;
import utility.CustomUtility;

public class InstallationOfflineAdapter extends RecyclerView.Adapter<InstallationOfflineAdapter.HomeCategoryViewHolder> {
    View.OnClickListener onclick_listener;
    DatabaseHelper db;
    private Context context;
    private ArrayList<InstallationOfflineBean> responseList;
    private List<InstallationOfflineBean> SearchesList = null;
    String pernr, billno;

    public InstallationOfflineAdapter(Context context, ArrayList<InstallationOfflineBean> responseList) {
        this.context = context;
        this.responseList = responseList;
        db = new DatabaseHelper(context);
        this.SearchesList = new ArrayList<InstallationOfflineBean>();
        this.SearchesList.addAll(responseList);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_installation_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {
        try {
            if (!TextUtils.isEmpty(responseList.get(position).getVbeln())) {
                holder.bill_no.setText(responseList.get(position).getVbeln());
                billno = responseList.get(position).getVbeln();
                pernr = CustomUtility.getSharedPreferences(context, "userid");
            }
            if (!TextUtils.isEmpty(responseList.get(position).getBeneficiary())) {
                holder.ben_no.setText(responseList.get(position).getBeneficiary());
            }
            if (!TextUtils.isEmpty(responseList.get(position).getGstInvNo())) {
                holder.gst_bill_no.setText(responseList.get(position).getGstInvNo());
            }
            if (!TextUtils.isEmpty(responseList.get(position).getFkdat())) {
                holder.bill_dt.setText(responseList.get(position).getFkdat());
            }
            if (!TextUtils.isEmpty(responseList.get(position).getName())) {
                holder.cust_nm.setText(responseList.get(position).getName());
            }
            if (!TextUtils.isEmpty(responseList.get(position).getContactNo())) {
                holder.cnt_no.setText(responseList.get(position).getContactNo());
            }
            if (!TextUtils.isEmpty(responseList.get(position).getAddress())) {
                holder.addrs.setText(responseList.get(position).getAddress());
            }

            holder.status.setImageResource(R.drawable.red_icn);

            holder.cardView.setOnClickListener(view -> {
                    Intent in = new Intent(context, InstallationDetailOfflineActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("bill_no", responseList.get(position).getVbeln());
//                    extras.putString("set_matno", responseList.get(position).getSetMatno());
//                    extras.putString("simha2", responseList.get(position).getSimha2());
//                    extras.putString("kunnr", responseList.get(position).getKunnr());
//                    extras.putString("gst_bill_no", responseList.get(position).getGstInvNo());
//                    extras.putString("tehvillage", responseList.get(position).getRegio());
//                    extras.putString("bill_date", responseList.get(position).getBilldate());
//                    extras.putString("disp_date", responseList.get(position).getDispdate());
//                    extras.putString("name", responseList.get(position).getCustomer_name());
//                    extras.putString("state", responseList.get(position).getState());
//                    extras.putString("city", responseList.get(position).getCity());
//                    extras.putString("state_txt", responseList.get(position).getStatetxt());
//                    extras.putString("city_txt", responseList.get(position).getCitytxt());
//                    extras.putString("address", responseList.get(position).getAddress());
//                    extras.putString("mobile", responseList.get(position).getContact_no());
//                    extras.putString("controller", responseList.get(position).getController());
//                    extras.putString("motor", responseList.get(position).getMotor());
//                    extras.putString("pump", responseList.get(position).getPump());
//                    extras.putString("simno", responseList.get(position).getSimno());
//                    extras.putString("regisno", responseList.get(position).getRegisno());
//                    extras.putString("projectno", responseList.get(position).getProjectno());
//                    extras.putString("loginno", responseList.get(position).getLoginno());
//                    extras.putString("moduleqty", responseList.get(position).getModuleqty());
//                    extras.putString("CUS_CONTACT_NO", responseList.get(position).getCUS_CONTACT_NO());
                    in.putExtras(extras);
                    context.startActivity(in);
            });

            InstallationOfflineBean param_invc = new InstallationOfflineBean();
//            param_invc = db.getInstallationData(pernr, billno);
//            if ((!TextUtils.isEmpty(param_invc.getLatitude()) && !TextUtils.isEmpty(param_invc.getLongitude())) &&
//                    (!TextUtils.isEmpty(param_invc.getSolarpanel_wattage())) && (!TextUtils.isEmpty(param_invc.getNo_of_module_value()))) {
//                holder.status.setImageResource(R.drawable.icn_yellow);
//            }
//            if (!TextUtils.isEmpty(param_invc.getLatitude()) && !TextUtils.isEmpty(param_invc.getLongitude()) && CustomUtility.getSharedPreferences(context, "INSTSYNC" + billno).equalsIgnoreCase("1") && !TextUtils.isEmpty(param_invc.getSolarpanel_wattage()) && !TextUtils.isEmpty(param_invc.getNo_of_module_value())) {
//                holder.status.setImageResource(R.drawable.right_mark_icn_green);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        responseList.clear();
        if (charText.length() == 0) {
            responseList.addAll(SearchesList);
        } else {
            for (InstallationOfflineBean cs : SearchesList) {
                if (cs.getVbeln().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getBeneficiary().toLowerCase(Locale.getDefault()).contains(charText)) {
                    responseList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }

    class HomeCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView bill_no, bill_dt, cust_nm, cnt_no, addrs, gst_bill_no, ben_no;
        ImageView status, icon;
        CardView cardView;

        HomeCategoryViewHolder(View itemView) {
            super(itemView);
            bill_no = itemView.findViewById(R.id.bill_no);
            bill_dt = itemView.findViewById(R.id.bill_dt);
            cust_nm = itemView.findViewById(R.id.cust_nm);
            cnt_no = itemView.findViewById(R.id.cnt_no);
            addrs = itemView.findViewById(R.id.addrs);
            gst_bill_no = itemView.findViewById(R.id.gst_bill_no);
            ben_no = itemView.findViewById(R.id.benf_no);
            status = itemView.findViewById(R.id.status);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}