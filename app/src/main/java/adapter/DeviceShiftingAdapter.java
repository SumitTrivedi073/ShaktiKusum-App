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

import bean.DeviceShiftingModel;


public class DeviceShiftingAdapter extends RecyclerView.Adapter<DeviceShiftingAdapter.ViewHolder> implements Filterable {
    Context mContext;
    private List<DeviceShiftingModel.Response> deviceShiftingList;
    private final List<DeviceShiftingModel.Response> arSearch;
    private ShiftingListner shiftingListener;

    TextView noDataFound;

    public DeviceShiftingAdapter(Context context, List<DeviceShiftingModel.Response> listdata, TextView noDataFound) {
        deviceShiftingList = listdata;
        mContext = context;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(listdata);
        this.noDataFound = noDataFound;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.device_shifting_item, parent, false);
        return new ViewHolder(listItem);
    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final DeviceShiftingModel.Response response = deviceShiftingList.get(position);
        holder.customerName.setText(response.getCustomerName());
        holder.mobileNumber.setText(response.getContactNo());
        holder.beneficiaryNo.setText("Beneficiary No:- "+response.getBeneficiary());
        holder.billNo.setText("Bill No:- "+response.getVbeln());

      holder.shiftingBtn.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View view) {
              Random random = new Random();
              String generatedVerificationCode = String.format("%04d", random.nextInt(10000));
              shiftingListener.shiftingListener(deviceShiftingList,position, generatedVerificationCode);

           }
       });


    }
    public void Deviceshifting(ShiftingListner response) {
        try {
            shiftingListener = response;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface ShiftingListner {
        void shiftingListener(List<DeviceShiftingModel.Response> deviceShiftingList, int position , String generatedVerificationCode);

    }


    @Override
    public int getItemCount() {
        return deviceShiftingList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView customerName,mobileNumber,beneficiaryNo,shiftingBtn,billNo;

        public ViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);
            beneficiaryNo = itemView.findViewById(R.id.beneficiaryNo);
            shiftingBtn = itemView.findViewById(R.id.shiftingBtn);
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
                    deviceShiftingList = arSearch;
                } else {
                    List<DeviceShiftingModel.Response> filteredList = new ArrayList<>();
                    for (DeviceShiftingModel.Response row : arSearch) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())||row.getContactNo().toLowerCase().contains(charString.toLowerCase())
                        ||row.getBeneficiary().toLowerCase().contains(charString.toLowerCase())||row.getVbeln().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    deviceShiftingList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = deviceShiftingList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                deviceShiftingList = (ArrayList<DeviceShiftingModel.Response>) filterResults.values;
                if (deviceShiftingList.size()>0){
                    noDataFound.setVisibility(View.GONE);
                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }
}