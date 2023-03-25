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
import activity.InstallationDetailOfflineActivity;
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
                    in.putExtras(extras);
                    context.startActivity(in);
            });



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