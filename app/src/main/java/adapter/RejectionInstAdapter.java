package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import activity.JointInspectionImageActivity;
import activity.RejectInstallationImageActivity;
import bean.RejectInstallationModel;
import debugapp.GlobalValue.Constant;

public class RejectionInstAdapter extends RecyclerView.Adapter<RejectionInstAdapter.ViewHolder> implements Filterable {

private final Context context;
        List<RejectInstallationModel.RejectDatum> responseList;
        List<RejectInstallationModel.RejectDatum> arSearch;
        TextView noDataFound;
public RejectionInstAdapter(Context context, List<RejectInstallationModel.RejectDatum> responseList,TextView noDataFound) {
        this.context = context;
        this.responseList = responseList;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(responseList);
        this.noDataFound = noDataFound;
        }

@Override
public int getItemViewType(int position) {
        return position;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rejection_inst_item, parent, false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.custNameTxt.setText(responseList.get(position).getCustomerName());
        holder.billNoTxt.setText(responseList.get(position).getVbeln());
        holder.beneficiaryId.setText(responseList.get(position).getBeneficiary());
        holder.Regis.setText(responseList.get(position).getRegisno());
        holder.projectNo.setText(responseList.get(position).getProjectNo());
         

        holder.item_card.setOnClickListener(v -> {
                Intent intent = new Intent(context, RejectInstallationImageActivity.class);
                intent.putExtra(Constant.RejectInstallation,responseList.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
                });

        }

@Override
public int getItemCount() {
        return responseList.size();
        }

static class ViewHolder extends RecyclerView.ViewHolder {
    TextView custNameTxt, billNoTxt, beneficiaryId, Regis, projectNo;
    CardView item_card;

    ViewHolder(View itemView) {
        super(itemView);
        custNameTxt = itemView.findViewById(R.id.custNameTxt);
        billNoTxt = itemView.findViewById(R.id.billNoTxt);
        beneficiaryId = itemView.findViewById(R.id.beneficiaryId);
        Regis = itemView.findViewById(R.id.regis);
        projectNo = itemView.findViewById(R.id.projectNo);
        item_card = itemView.findViewById(R.id.item_card);
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
                    List<RejectInstallationModel.RejectDatum> filteredList = new ArrayList<>();
                    for (RejectInstallationModel.RejectDatum row : arSearch) {

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
                responseList = (List<RejectInstallationModel.RejectDatum>) filterResults.values;
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