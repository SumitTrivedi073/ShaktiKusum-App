package adapter;

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

import activity.DeptDocSubImageActivity;
import bean.JointInspectionModel;
import debugapp.GlobalValue.Constant;

public class DeptDocSubAdapter extends RecyclerView.Adapter<DeptDocSubAdapter.ViewHolder> implements Filterable {

    private final Context context;
    List<JointInspectionModel.InspectionDatum> responseList;
    List<JointInspectionModel.InspectionDatum> arSearch;
    TextView noDataFound;
    public DeptDocSubAdapter(Context context, List<JointInspectionModel.InspectionDatum> responseList, TextView noDataFound) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.joint_inspection_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

                 holder.custNameTxt.setText(responseList.get(position).getName());
                holder.billNoTxt.setText(responseList.get(position).getVbeln());
                holder.beneficiaryId.setText(responseList.get(position).getBeneficiary());
                holder.dispatchDate.setText(responseList.get(position).getDispatchDate());
                holder.contactNo.setText(responseList.get(position).getContactNo());
                holder.addressTxt.setText(responseList.get(position).getAddress());

                holder.item_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DeptDocSubImageActivity.class);
                        intent.putExtra(Constant.JointInspection,responseList.get(position));
                        context.startActivity(intent);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView custNameTxt, billNoTxt, beneficiaryId, dispatchDate, contactNo, addressTxt;
        CardView item_card;

        ViewHolder(View itemView) {
            super(itemView);
            custNameTxt = itemView.findViewById(R.id.custNameTxt);
            billNoTxt = itemView.findViewById(R.id.billNoTxt);
            beneficiaryId = itemView.findViewById(R.id.beneficiaryId);
            dispatchDate = itemView.findViewById(R.id.dispatchDate);
            contactNo = itemView.findViewById(R.id.contactNo);
            addressTxt = itemView.findViewById(R.id.addressTxt);
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
                    List<JointInspectionModel.InspectionDatum> filteredList = new ArrayList<>();
                    for (JointInspectionModel.InspectionDatum row : arSearch) {

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
                responseList = (List<JointInspectionModel.InspectionDatum>) filterResults.values;
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