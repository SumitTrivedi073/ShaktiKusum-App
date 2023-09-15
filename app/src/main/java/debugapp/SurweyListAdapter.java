package debugapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.List;

import debugapp.Bean.SurveyListResponse;
import debugapp.GlobalValue.Constant;


public class SurweyListAdapter extends RecyclerView.Adapter<SurweyListAdapter.ViewHolder> {

    private final Context mContext;
    private final List<SurveyListResponse> mSurveyListResponse;



    public SurweyListAdapter(Context mContext, List<SurveyListResponse> mLrInvoiceResponse) {
        this.mContext = mContext;
        this.mSurveyListResponse = mLrInvoiceResponse;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.surwey_list_item, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.txtMaterialCodeValueID.setText(mSurveyListResponse.get(position).getBeneficiary());
        holder.txtDecriptionValueID.setText(mSurveyListResponse.get(position).getCustomerName());
        holder.txtQuentityValueID.setText(mSurveyListResponse.get(position).getMobile());
        holder.txtPriceValueID.setText(mSurveyListResponse.get(position).getAddress());

        holder.rlvMainItemViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Add_Survey_Activity.class);
                intent.putExtra(Constant.surveyList,mSurveyListResponse.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
         if (mSurveyListResponse != null && mSurveyListResponse.size() > 0)
            return mSurveyListResponse.size();
        else
            return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMaterialCodeValueID, txtDecriptionValueID, txtQuentityValueID, txtPriceValueID;

        public RelativeLayout rlvMainItemViewID;

        public ViewHolder(View v) {
            super(v);
            rlvMainItemViewID =  v.findViewById(R.id.rlvMainItemViewID);
            txtMaterialCodeValueID =  v.findViewById(R.id.txtMaterialCodeValueID);
            txtDecriptionValueID =  v.findViewById(R.id.txtDecriptionValueID);
            txtQuentityValueID =  v.findViewById(R.id.txtQuentityValueID);
            txtPriceValueID =  v.findViewById(R.id.txtPriceValueID);

          }
    }
}


