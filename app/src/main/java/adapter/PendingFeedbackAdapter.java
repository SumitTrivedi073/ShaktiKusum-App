package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import activity.PendingFeedBackOTPVerification;
import debugapp.PendingFeedback;

public class PendingFeedbackAdapter extends RecyclerView.Adapter<PendingFeedbackAdapter.ViewHolder> {
    Context mContext;
    private List<PendingFeedback.Response> pendingFeedbackList;


    public PendingFeedbackAdapter(Context context, List<PendingFeedback.Response> listdata) {
        pendingFeedbackList = listdata;
        mContext = context;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.pending_feedback_item, parent, false);
        return new ViewHolder(listItem);
    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PendingFeedback.Response response = pendingFeedbackList.get(position);
        holder.customerName.setText(response.getCustomerName());
        holder.mobileNumber.setText(response.getContactNo());
        holder.beneficiaryNo.setText("Beneficiary No:- "+response.getBeneficiary());
        holder.billNo.setText("Bill No:- "+response.getVbeln());

      holder.sendOTP.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View view) {
              Random random = new Random();

              String generatedPassword = String.format("%04d", random.nextInt(10000));

              Log.d("MyApp", "Generated Password : " + generatedPassword);
             Intent intent = new Intent(mContext, PendingFeedBackOTPVerification.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              mContext.startActivity(intent);
           }
       });

     
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
}