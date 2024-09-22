package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import debugapp.PendingInstallationModel;

public class PendingSetParameterAdapter extends RecyclerView.Adapter<PendingSetParameterAdapter.ViewHolder> implements Filterable {
    Context mContext;
    private List<PendingInstallationModel.Response> pendingFeedbackList;
    private final List<PendingInstallationModel.Response> arSearch;
    private ItemCLickListner itemClickListner;

    TextView noDataFound;

    public PendingSetParameterAdapter(Context context, List<PendingInstallationModel.Response> listdata, TextView noDataFound) {
        pendingFeedbackList = listdata;
        mContext = context;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(listdata);
        this.noDataFound = noDataFound;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.pending_feedback_item, parent, false);
        return new ViewHolder(listItem);
    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final PendingInstallationModel.Response response = pendingFeedbackList.get(position);
        holder.customerName.setText(response.getCustomerName());
        holder.mobileNumber.setText(response.getContactNo());
        holder.beneficiaryNo.setText("Beneficiary No:- "+response.getBeneficiary());
        holder.billNo.setText("Bill No:- "+response.getVbeln());

      holder.sendOTP.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View view) {
              Random random = new Random();
              String generatedVerificationCode = String.format("%04d", random.nextInt(10000));
              itemClickListner.itemClickListner(pendingFeedbackList,position, generatedVerificationCode);

           }
       });


    }
    public void ItemCLick(ItemCLickListner response) {
        try {
            itemClickListner = response;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface ItemCLickListner {
        void itemClickListner(List<PendingInstallationModel.Response> pendingFeedbackList, int position , String generatedVerificationCode);

    }


    @Override
    public int getItemCount() {
        return pendingFeedbackList.size();
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
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    pendingFeedbackList = arSearch;
                } else {
                    List<PendingInstallationModel.Response> filteredList = new ArrayList<>();
                    for (PendingInstallationModel.Response row : arSearch) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())||row.getContactNo().toLowerCase().contains(charString.toLowerCase())
                        ||row.getBeneficiary().toLowerCase().contains(charString.toLowerCase())||row.getVbeln().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    pendingFeedbackList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pendingFeedbackList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pendingFeedbackList = (ArrayList<PendingInstallationModel.Response>) filterResults.values;
                if (pendingFeedbackList.size()>0){
                    noDataFound.setVisibility(View.GONE);
                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }
}