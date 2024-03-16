package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.shaktipumplimited.shaktikusum.R;

import java.util.List;

import bean.PairDeviceModel;

public class PairedDeviceAdapter extends RecyclerView.Adapter<PairedDeviceAdapter.ViewHolder> {
    Context mContext;

    private final List<PairDeviceModel> PairDeviceList;
    private static deviceSelectionListener deviceListener;


    public PairedDeviceAdapter(Context context, List<PairDeviceModel> listData) {
        PairDeviceList = listData;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.bt_paired_device_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final PairDeviceModel pairDeviceModel = PairDeviceList.get(position);
             holder.setData(pairDeviceModel,position);

    }


    @Override
    public int getItemCount() {
        return PairDeviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deviceName, deviceAddress;
        LinearLayout pairedDeviceItem;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.txtBTNameID);
            deviceAddress = itemView.findViewById(R.id.txtBTMACAddressID);
            pairedDeviceItem = itemView.findViewById(R.id.pairedDeviceItem);
        }

        public void setData(PairDeviceModel pairDeviceModel, int position) {
            deviceName.setText(pairDeviceModel.getDeviceName());
            deviceAddress.setText(pairDeviceModel.getDeviceAddress());

            pairedDeviceItem.setOnClickListener(v -> deviceListener.DeviceSelectionListener(pairDeviceModel,position));
        }
    }

    public interface deviceSelectionListener {
        void DeviceSelectionListener(PairDeviceModel pairDeviceModel, int position);
    }

    public void deviceSelection(deviceSelectionListener pairDevice) {
        try {
            deviceListener = pairDevice;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
