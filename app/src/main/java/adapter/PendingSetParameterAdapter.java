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

import bean.ParameterSettingListModel;
import debugapp.PendingInstallationModel;

public class PendingSetParameterAdapter extends RecyclerView.Adapter<PendingSetParameterAdapter.ViewHolder> implements Filterable {
    Context mContext;
    private List<ParameterSettingListModel.InstallationDatum> pendingSettingList;
    private final List<ParameterSettingListModel.InstallationDatum> arSearch;
    private ItemCLickListner itemClickListner;

    TextView noDataFound;

    public PendingSetParameterAdapter(Context context, List<ParameterSettingListModel.InstallationDatum> listdata, TextView noDataFound) {
        pendingSettingList = listdata;
        mContext = context;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(listdata);
        this.noDataFound = noDataFound;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.pending_setting_item, parent, false);
        return new ViewHolder(listItem);
    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ParameterSettingListModel.InstallationDatum response = pendingSettingList.get(position);
        holder.customerName.setText(response.getName());
        holder.beneficiaryNo.setText("Beneficiary No:- "+response.getBeneficiary());
        holder.billNo.setText("Bill No:- "+response.getVbeln());

      holder.sendOTP.setOnClickListener(view -> itemClickListner.itemClickListner(response,position));


    }
    public void ItemCLick(ItemCLickListner response) {
        try {
            itemClickListner = response;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface ItemCLickListner {
        void itemClickListner(ParameterSettingListModel.InstallationDatum pendingSettingList, int position);

    }


    @Override
    public int getItemCount() {
        return pendingSettingList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView customerName,beneficiaryNo,sendOTP,billNo;

        public ViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
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
                    pendingSettingList = arSearch;
                } else {
                    List<ParameterSettingListModel.InstallationDatum> filteredList = new ArrayList<>();
                    for (ParameterSettingListModel.InstallationDatum row : arSearch) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                        ||row.getBeneficiary().toLowerCase().contains(charString.toLowerCase())||row.getVbeln().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    pendingSettingList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pendingSettingList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pendingSettingList = (ArrayList<ParameterSettingListModel.InstallationDatum>) filterResults.values;
                if (pendingSettingList.size()>0){
                    noDataFound.setVisibility(View.GONE);
                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }
}