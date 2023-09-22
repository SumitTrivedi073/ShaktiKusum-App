package adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.shaktipumplimited.shaktikusum.R;
import java.util.ArrayList;
import java.util.List;
import activity.RejectedSiteAuditImgActivity;
import bean.RejectListBean;
import debugapp.GlobalValue.Constant;


public class Adapter_reject_list extends RecyclerView.Adapter<Adapter_reject_list.HomeCategoryViewHolder> implements Filterable {
    private final Context context;
    List<RejectListBean.RejectDatum> responseList;
    List<RejectListBean.RejectDatum> arSearch;
    TextView noDataFound;

    public Adapter_reject_list(Context context, List<RejectListBean.RejectDatum> responseList,TextView noDataFound) {
        this.context = context;
        this.responseList = responseList;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(responseList);
        this.noDataFound = noDataFound;

    }

    @Override
    public int getItemViewType(int position) {return position;}

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_reject_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {


        RejectListBean.RejectDatum rejectList = responseList.get(position);
        if (!TextUtils.isEmpty(rejectList.getVbeln())) {
            holder.bill_no.setText(rejectList.getVbeln());
        }

        if (!TextUtils.isEmpty(rejectList.getBeneficiary())) {
            holder.ben_no.setText(rejectList.getBeneficiary());
        }

        if (!TextUtils.isEmpty(rejectList.getRegisno())) {
            holder.reg_no.setText(rejectList.getRegisno());
        }

        if (!TextUtils.isEmpty(rejectList.getCustomerName())) {
            holder.cust_nm.setText(rejectList.getCustomerName());
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(context, RejectedSiteAuditImgActivity.class);
                in.putExtra(Constant.RejectSite, rejectList);
                context.startActivity(in);

            }
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    class HomeCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView bill_no, ben_no, reg_no, cust_nm;
        CardView cardView;
        HomeCategoryViewHolder(View itemView) {
            super(itemView);

            bill_no = itemView.findViewById(R.id.bill_no);
            ben_no = itemView.findViewById(R.id.ben_no);
            reg_no = itemView.findViewById(R.id.reg_no);
            cust_nm = itemView.findViewById(R.id.cust_nm);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    responseList = arSearch;
                } else {
                    List<RejectListBean.RejectDatum> filteredList = new ArrayList<>();
                    for (RejectListBean.RejectDatum row : arSearch) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBeneficiary().toLowerCase().contains(charString.toLowerCase())||row.getRegisno().toLowerCase().contains(charString.toLowerCase())) {
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
                responseList = (List<RejectListBean.RejectDatum>) filterResults.values;
                if (responseList.size()>0){
                    noDataFound.setVisibility(View.GONE);
                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }
}