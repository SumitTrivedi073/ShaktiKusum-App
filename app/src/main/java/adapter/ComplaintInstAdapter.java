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
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import activity.ComplaintFormActivity;
import bean.ComplaintInstModel;
import debugapp.GlobalValue.Constant;

public class ComplaintInstAdapter extends RecyclerView.Adapter<ComplaintInstAdapter.ViewHolder> implements Filterable {
    Context mContext;
    private List<ComplaintInstModel.Response> ComplaintInstModelList;
    private final List<ComplaintInstModel.Response> arSearch;


    TextView noDataFound;

    public ComplaintInstAdapter(Context context, List<ComplaintInstModel.Response> listdata, TextView noDataFound) {
        ComplaintInstModelList = listdata;
        mContext = context;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(listdata);
        this.noDataFound = noDataFound;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.pending_feedback_item, parent, false);
        return new ViewHolder(listItem);
    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ComplaintInstModel.Response response = ComplaintInstModelList.get(position);
        holder.customerName.setText(response.getName());
        holder.mobileNumber.setText(response.getContactNo());
        holder.beneficiaryNo.setText("Beneficiary No:- "+response.getBeneficiary());
        holder.billNo.setText("Bill No:- "+response.getVbeln());

      holder.sendOTP.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View view) {

              Intent intent = new Intent(mContext, ComplaintFormActivity.class);
              intent.putExtra(Constant.InstallationCompData,response);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              mContext.startActivity(intent);
           }
       });


    }

    @Override
    public int getItemCount() {
        return ComplaintInstModelList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView customerName,mobileNumber,beneficiaryNo,sendOTP,billNo;

        public ViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);
            beneficiaryNo = itemView.findViewById(R.id.beneficiaryNo);
            sendOTP = itemView.findViewById(R.id.sendOTP);
            billNo = itemView.findViewById(R.id.billNo);

            sendOTP.setText(itemView.getContext().getResources().getString(R.string.complaint_form));
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    ComplaintInstModelList = arSearch;
                } else {
                    List<ComplaintInstModel.Response> filteredList = new ArrayList<>();
                    for (ComplaintInstModel.Response row : arSearch) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())||row.getContactNo().toLowerCase().contains(charString.toLowerCase())
                        ||row.getBeneficiary().toLowerCase().contains(charString.toLowerCase())||row.getVbeln().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    ComplaintInstModelList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = ComplaintInstModelList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ComplaintInstModelList = (ArrayList<ComplaintInstModel.Response>) filterResults.values;
                if (ComplaintInstModelList.size()>0){
                    noDataFound.setVisibility(View.GONE);
                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }
}