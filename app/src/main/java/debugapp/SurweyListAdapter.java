package debugapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;
import debugapp.Bean.SurveyListResponse;
import debugapp.GlobalValue.Constant;


public class SurweyListAdapter extends RecyclerView.Adapter<SurweyListAdapter.ViewHolder>  implements Filterable {

    private final Context mContext;
    private List<SurveyListResponse.Response> mSurveyListResponse;
    private final List<SurveyListResponse.Response> arSearch;



    public SurweyListAdapter(Context mContext, List<SurveyListResponse.Response> mLrInvoiceResponse) {
        this.mContext = mContext;
        this.mSurveyListResponse = mLrInvoiceResponse;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(mLrInvoiceResponse);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mSurveyListResponse = arSearch;
                } else {
                    List<SurveyListResponse.Response> filteredList = new ArrayList<>();
                    for (SurveyListResponse.Response row : arSearch) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())||row.getMobile().toLowerCase().contains(charString.toLowerCase())
                                ||row.getBeneficiary().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mSurveyListResponse = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mSurveyListResponse;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mSurveyListResponse = (ArrayList<SurveyListResponse.Response>) filterResults.values;
                if (mSurveyListResponse.size()>0){
                    //noDataFound.setVisibility(View.GONE);
                }else {
                   // noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }
}


