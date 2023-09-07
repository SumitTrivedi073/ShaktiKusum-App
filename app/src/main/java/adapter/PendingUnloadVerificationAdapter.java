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

import debugapp.PendingFeedback;
import debugapp.UnloadingFeedbackModel;

public class PendingUnloadVerificationAdapter extends RecyclerView.Adapter<PendingUnloadVerificationAdapter.ViewHolder> implements Filterable {
    Context mContext;
    private List<UnloadingFeedbackModel.InstallationDatum> pendingFeedbackList;
    private final List<UnloadingFeedbackModel.InstallationDatum> arSearch;
    private SendOTPListner sendOTPListener;

    TextView noDataFound;

    public PendingUnloadVerificationAdapter(Context context, List<UnloadingFeedbackModel.InstallationDatum> listdata, TextView noDataFound) {
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
        final UnloadingFeedbackModel.InstallationDatum response = pendingFeedbackList.get(position);
        holder.customerName.setText(response.getName());
        holder.mobileNumber.setText(response.getContactNo());
        holder.beneficiaryNo.setText("Beneficiary No:- "+response.getBeneficiary());
        holder.billNo.setText("Bill No:- "+response.getVbeln());

      holder.sendOTP.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View view) {
              Random random = new Random();
              String generatedVerificationCode = String.format("%04d", random.nextInt(10000));
              sendOTPListener.sendOtpListener(pendingFeedbackList,position, generatedVerificationCode);

           }
       });


    }
    public void SendOTP(SendOTPListner response) {
        try {
            sendOTPListener = response;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface SendOTPListner {
        void sendOtpListener(List<UnloadingFeedbackModel.InstallationDatum> pendingFeedbackList, int position ,String generatedVerificationCode);

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
                    List<UnloadingFeedbackModel.InstallationDatum> filteredList = new ArrayList<>();
                    for (UnloadingFeedbackModel.InstallationDatum row : arSearch) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())||row.getContactNo().toLowerCase().contains(charString.toLowerCase())
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
                pendingFeedbackList = (ArrayList<UnloadingFeedbackModel.InstallationDatum>) filterResults.values;
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