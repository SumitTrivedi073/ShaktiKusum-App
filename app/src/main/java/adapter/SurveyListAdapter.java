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

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;

import activity.KusumCSurveyFormActivity;
import bean.SurveyListModel;
import debugapp.GlobalValue.Constant;

public class SurveyListAdapter extends RecyclerView.Adapter<SurveyListAdapter.ViewHolder> implements Filterable {
    Context mContext;
    private List<SurveyListModel.Response> SurveyList;
    private final List<SurveyListModel.Response> arSearch;


    TextView noDataFound;

    public SurveyListAdapter(Context context, List<SurveyListModel.Response> listdata, TextView noDataFound) {
        SurveyList = listdata;
        mContext = context;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(listdata);
        this.noDataFound = noDataFound;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.survey_list_item, parent, false);
        return new ViewHolder(listItem);
    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final SurveyListModel.Response response = SurveyList.get(position);
        holder.customerName.setText(response.getCustomerName());
        holder.mobileNumber.setText(response.getMobile());
        holder.beneficiaryNo.setText("Beneficiary No:- "+response.getBeneficiary());

        holder.surveyFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KusumCSurveyFormActivity.class);
                intent.putExtra(Constant.SurveyData,response);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return SurveyList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView customerName,mobileNumber,beneficiaryNo,surveyFormBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);
            beneficiaryNo = itemView.findViewById(R.id.beneficiaryNo);
            surveyFormBtn = itemView.findViewById(R.id.surveyFormBtn);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    SurveyList = arSearch;
                } else {
                    List<SurveyListModel.Response> filteredList = new ArrayList<>();
                    for (SurveyListModel.Response row : arSearch) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())||row.getMobile().toLowerCase().contains(charString.toLowerCase())
                        ||row.getBeneficiary().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    SurveyList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = SurveyList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                SurveyList = (ArrayList<SurveyListModel.Response>) filterResults.values;
                if (SurveyList.size()>0){
                    noDataFound.setVisibility(View.GONE);
                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }
}