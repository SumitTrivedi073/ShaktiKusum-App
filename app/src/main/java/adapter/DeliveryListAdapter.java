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

import bean.ComplianDeliverModel;

public class DeliveryListAdapter extends RecyclerView.Adapter<DeliveryListAdapter.ViewHolder> implements Filterable {
    Context mContext;
    private List<ComplianDeliverModel.ComplaintDatum> deliverList;
    private final List<ComplianDeliverModel.ComplaintDatum> arSearch;


    TextView noDataFound;

    public DeliveryListAdapter(Context context, List<ComplianDeliverModel.ComplaintDatum> listdata, TextView noDataFound) {
        deliverList = listdata;
        mContext = context;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(listdata);
        this.noDataFound = noDataFound;

    }


    @Override
    public DeliveryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.delivery_list_item, parent, false);
        return new DeliveryListAdapter.ViewHolder(listItem);
    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(DeliveryListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ComplianDeliverModel.ComplaintDatum response = deliverList.get(position);
        holder.maktx.setText("Regis No");
        holder.knumh.setText( response.getRegisno());

        holder.Matkl.setText( "Project No");
        holder.Matnr.setText( response.getProjectNo());

        holder.date.setText("Beneficiary");
        holder.dateTo.setText(response.getBeneficiary());

        holder.city.setText("Bill No");
        holder.cityTo.setText(response.getBillno());

        if(response.getDamagePump().isEmpty()){
            holder.pump.setVisibility(View.GONE);
            holder.pumptxt.setVisibility(View.GONE);
            holder.pumpStatus.setVisibility(View.GONE);
            holder.pumpsValue.setVisibility(View.GONE);
        }else{
            holder.pump.setText("Pump");
            holder.pumptxt.setText(response.getDamagePump());
            holder.pumpStatus.setText("Pumps Status");
            holder.pumpsValue.setText(response.getPump());
        }

        if(response.getDamageMotor().isEmpty()){
            holder.motor.setVisibility(View.GONE);
            holder.motortxt.setVisibility(View.GONE);
            holder.motorStatus.setVisibility(View.GONE);
            holder.motorValue.setVisibility(View.GONE);
        }else {
            holder.motor.setText("Motor");
            holder.motortxt.setText(response.getDamageMotor());
            holder.motorStatus.setText("Motor Status");
            holder.motorValue.setText(response.getMotor());
        }

        if(response.getDamageCont().isEmpty()){
            holder.controller.setVisibility(View.GONE);
            holder.contollertxt.setVisibility(View.GONE);
            holder.contStatus.setVisibility(View.GONE);
            holder.contValue.setVisibility(View.GONE);
        }else {
            holder.controller.setText("Controller");
            holder.contollertxt.setText(response.getDamageCont());
            holder.contStatus.setText("Controller Status");
            holder.contValue.setText(response.getController());
        }


        holder.remark.setText("Remark");
        holder.mob.setText(response.getRemark());




    }

    @Override
    public int getItemCount() {
        return deliverList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView maktx,knumh,Matkl,Matnr,date,dateTo,
                city,cityTo,remark,mob, pump , pumptxt, pumpStatus, pumpsValue,
        motor, motortxt, motorStatus, motorValue,
        controller, contollertxt,contStatus, contValue;

        public ViewHolder(View itemView) {
            super(itemView);
            maktx = itemView.findViewById(R.id.maktx);
            knumh = itemView.findViewById(R.id.knumh);
            Matkl = itemView.findViewById(R.id.Matkl);
            Matnr = itemView.findViewById(R.id.Matnr);
            dateTo = itemView.findViewById(R.id.date2);
            date = itemView.findViewById(R.id.date1);
            city = itemView.findViewById(R.id.city);
            cityTo = itemView.findViewById(R.id.toCity);
            mob = itemView.findViewById(R.id.mob );
            remark  = itemView.findViewById(R.id.remark);
            pump = itemView.findViewById(R.id.pump);
            pumptxt = itemView.findViewById(R.id.pumptext);
            pumpStatus = itemView.findViewById(R.id.pumpStatus);
            pumpsValue = itemView.findViewById(R.id.pumpValue);
            motor = itemView.findViewById(R.id.motor);
            motortxt = itemView.findViewById(R.id.motortxt);
            motorStatus= itemView.findViewById(R.id.motorStatus);
            motorValue = itemView.findViewById(R.id.motorValue);
            controller = itemView.findViewById(R.id.controller);
            contollertxt = itemView.findViewById(R.id.controllertxt);
            contStatus = itemView.findViewById(R.id.controllerStatus);
            contValue = itemView.findViewById(R.id.controllerValue);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    deliverList = arSearch;
                } else {
                    List<ComplianDeliverModel.ComplaintDatum> filteredList = new ArrayList<>();
                    for (ComplianDeliverModel.ComplaintDatum row : arSearch) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBeneficiary().toLowerCase().contains(charString.toLowerCase())||row.getBillno().toLowerCase().contains(charString.toLowerCase())
                                ||row.getProjectNo().toLowerCase().contains(charString.toLowerCase())||row.getRegisno().toUpperCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    deliverList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = deliverList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                deliverList = (ArrayList<ComplianDeliverModel.ComplaintDatum>) filterResults.values;
                if (deliverList.size()>0){
                    noDataFound.setVisibility(View.GONE);
                }else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }
}