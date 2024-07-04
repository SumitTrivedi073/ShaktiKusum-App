package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;

import activity.DocDistOffDetails;
import activity.SelfCheckImageActivity;
import bean.DocDistListBean;
import bean.SelfCheckListBean;

public class DocDistOffAdapter extends RecyclerView.Adapter<DocDistOffAdapter.HomeCategoryViewHolder> implements Filterable {
    private final Context context;
    private List<DocDistListBean.Listing> responseList;
    List<DocDistListBean.Listing> searchList = null;
    TextView noDataFound;

    public DocDistOffAdapter(Context context, List<DocDistListBean.Listing> responseList, TextView noDataFound) {
        this.context = context;
        this.responseList = responseList;
        this.searchList = new ArrayList<DocDistListBean.Listing>();
        this.searchList.addAll(responseList);
        this.noDataFound = noDataFound;
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
                    ArrayList<DocDistListBean.Listing> filteredList = new ArrayList<>();
                    for (DocDistListBean.Listing row : searchList) {

                        if (row.getBeneficiary().toLowerCase().contains(charString.toLowerCase()) || row.getRegisno().toLowerCase().contains(charString.toLowerCase())
                                || row.getbillno().toLowerCase().contains(charString.toLowerCase()) || row.getCustomerName().toLowerCase().contains(charString.toLowerCase())) {
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
                responseList = (ArrayList<DocDistListBean.Listing>) filterResults.values;
                if (responseList.size() > 0) {
                    noDataFound.setVisibility(View.GONE);
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public DocDistOffAdapter.HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dict_doc_list_item, parent, false);
        return new DocDistOffAdapter.HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocDistOffAdapter.HomeCategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (!TextUtils.isEmpty(responseList.get(position).getBeneficiary())) {
            holder.benf_no.setText(responseList.get(position).getBeneficiary());
        }if (!TextUtils.isEmpty(responseList.get(position).getRegisno())) {
            holder.regis_no.setText(responseList.get(position).getRegisno());
        }if (!TextUtils.isEmpty(responseList.get(position).getCustomerName())) {
            holder.cust_nm.setText(responseList.get(position).getCustomerName());
        }if (!TextUtils.isEmpty(responseList.get(position).getbillno())) {
            holder.bill_no.setText(responseList.get(position).getbillno());
        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DocDistOffDetails.class);
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


    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {
        CardView card_view;
        TextView bill_no,benf_no,cust_nm,regis_no;
        public HomeCategoryViewHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            bill_no = itemView.findViewById(R.id.bill_no_txt);
            benf_no = itemView.findViewById(R.id.benf_txt);
            cust_nm = itemView.findViewById(R.id.cust_nm_txt);
            regis_no = itemView.findViewById(R.id.reg_txt);
        }
    }
}
