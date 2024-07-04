package adapter;

import static debugapp.GlobalValue.UtilMethod.context;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import org.apache.logging.log4j.util.Constants;

import java.util.ArrayList;
import java.util.List;

import activity.SelfCheckImageActivity;
import bean.SelfCheckListBean;

public class SelfCheckDetailsAdapter extends RecyclerView.Adapter<SelfCheckDetailsAdapter.HomeCategoryViewHolder> implements Filterable {

    private final Context context;
    private List<SelfCheckListBean.InstallationDatum> responseList;
    List<SelfCheckListBean.InstallationDatum> searchList = null;
    TextView noDataFound;

    public SelfCheckDetailsAdapter(Context context, List<SelfCheckListBean.InstallationDatum> responseList, TextView noDataFound) {
        this.context = context;
        this.responseList = responseList;
        this.searchList = new ArrayList<SelfCheckListBean.InstallationDatum>();
        this.searchList.addAll(responseList);
        this.noDataFound = noDataFound;
    }

    @NonNull
    @Override
    public SelfCheckDetailsAdapter.HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_installation_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelfCheckDetailsAdapter.HomeCategoryViewHolder holder, int position) {

        if (!TextUtils.isEmpty(responseList.get(position).getBeneficiary())) {
            holder.benf_no.setText(responseList.get(position).getBeneficiary());
        } if (!TextUtils.isEmpty(responseList.get(position).getContactNo())) {
            holder.cnt_no.setText(responseList.get(position).getContactNo());
        } if (!TextUtils.isEmpty(responseList.get(position).getVbeln())) {
            holder.bill_no.setText(responseList.get(position).getVbeln());
        } if (!TextUtils.isEmpty(responseList.get(position).getGstInvNo())) {
            holder.gst_bill_no.setText(responseList.get(position).getGstInvNo());
        } if (!TextUtils.isEmpty(responseList.get(position).getDispatchDate())) {
            holder.bill_dt.setText(responseList.get(position).getDispatchDate());
        } if (!TextUtils.isEmpty(responseList.get(position).getName())) {
            holder.cust_nm.setText(responseList.get(position).getName());
        } if (!TextUtils.isEmpty(responseList.get(position).getAddress())) {
            holder.addrs.setText(responseList.get(position).getAddress());
        }
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelfCheckImageActivity.class);
                intent.putExtra("arrlist",responseList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
                    responseList = searchList;
                } else {
                    ArrayList<SelfCheckListBean.InstallationDatum> filteredList = new ArrayList<>();
                    for (SelfCheckListBean.InstallationDatum row : searchList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getBeneficiary().toLowerCase().contains(charString.toLowerCase())
                                || row.getVbeln().toLowerCase().contains(charString.toLowerCase()) || row.getMobile().toLowerCase().contains(charString.toLowerCase())) {
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
                responseList = (ArrayList<SelfCheckListBean.InstallationDatum>) filterResults.values;
                if (responseList.size() > 0) {
                    noDataFound.setVisibility(View.GONE);
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView status;
        CardView card_view;
        TextView bill_no,benf_no,gst_bill_no,bill_dt,cust_nm,cnt_no,addrs;

        public HomeCategoryViewHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            status = itemView.findViewById(R.id.status);
            bill_no = itemView.findViewById(R.id.bill_no);
            benf_no = itemView.findViewById(R.id.benf_no);
            gst_bill_no = itemView.findViewById(R.id.gst_bill_no);
            bill_dt = itemView.findViewById(R.id.bill_dt);
            cust_nm = itemView.findViewById(R.id.cust_nm);
            cnt_no = itemView.findViewById(R.id.cnt_no);
            addrs = itemView.findViewById(R.id.addrs);
            status.setVisibility(View.GONE);
        }
    }
}
