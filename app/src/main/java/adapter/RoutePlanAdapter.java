package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import debugapp.PendingFeedback;
import debugapp.RoutePlanModel;

public class RoutePlanAdapter extends RecyclerView.Adapter<RoutePlanAdapter.ViewHolder> implements Filterable {
    Context mContext;
    private List<RoutePlanModel.InstallationDatum> pendingFeedbackList;
    private final List<RoutePlanModel.InstallationDatum> arSearch;
    private SelectRouteListner selectRouteListener;

    TextView noDataFound;

    public RoutePlanAdapter(Context context, List<RoutePlanModel.InstallationDatum> listdata, TextView noDataFound) {
        pendingFeedbackList = listdata;
        mContext = context;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(listdata);
        this.noDataFound = noDataFound;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.routeplan_item, parent, false);
        return new ViewHolder(listItem);
    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final RoutePlanModel.InstallationDatum response = pendingFeedbackList.get(position);
        holder.beneficiaryNo.setText("Beneficiary No:- "+response.getBeneficiary());
        holder.regisno.setText("registration No:- "+response.getRegisno());

        if(response.isChecked()){
            holder.checkbox.setChecked(true);
        }

      holder.checkbox.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View view) {
              selectRouteListener.selectRouteListener(pendingFeedbackList.get(position),position);

           }
       });


    }
    public void SelectRoute(SelectRouteListner response) {
        try {
            selectRouteListener = response;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface SelectRouteListner {
        void selectRouteListener(RoutePlanModel.InstallationDatum installationDatum, int position);

    }


    @Override
    public int getItemCount() {
        return pendingFeedbackList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView beneficiaryNo,regisno;
        CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
            beneficiaryNo = itemView.findViewById(R.id.beneficiaryNo);
            regisno = itemView.findViewById(R.id.regisno);
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
                    List<RoutePlanModel.InstallationDatum> filteredList = new ArrayList<>();
                    for (RoutePlanModel.InstallationDatum row : arSearch) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBeneficiary().toLowerCase().contains(charString.toLowerCase())||row.getRegisno().toLowerCase().contains(charString.toLowerCase())) {
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
                pendingFeedbackList = (ArrayList<RoutePlanModel.InstallationDatum>) filterResults.values;
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