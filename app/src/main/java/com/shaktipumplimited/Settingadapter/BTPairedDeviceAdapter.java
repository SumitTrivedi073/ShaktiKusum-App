package com.shaktipumplimited.Settingadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import debugapp.BlueToothDebugNewActivity;
import debugapp.GlobalValue.Constant;
import com.shaktipumplimited.shaktikusum.R;
import webservice.WebURL;


public class BTPairedDeviceAdapter extends RecyclerView.Adapter<BTPairedDeviceAdapter.ViewHolder> {

    private Context mContext;

    private List mDeviceNameList;
    private List mDeviceMACAddressList;
    //private HomeUserNameClickListener mHomeUserNameClickListener;

    public BTPairedDeviceAdapter(Context mContext, List mDeviceNameList, List mDeviceMACAddressList) {

        this.mDeviceNameList = mDeviceNameList;
        this.mDeviceMACAddressList = mDeviceMACAddressList;
        // this.mHomeUserNameClickListener = mHomeUserNameClickListener;
        this.mContext = mContext;

    }

    public BTPairedDeviceAdapter(String SSS, Context mContext) {
        // this.galleryModelsList = galleryModelsList;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.bt_paired_device_item, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // holder.rlvHomeMainView.getLayoutParams().width = Validation.getDeviceHeightWidth(mContext, true)/2;
        //  holder.rlvHomeMainView.getLayoutParams().height = Validation.getDeviceHeightWidth(mContext, false)/3+30;

        //changeButtonVisibility(false,0.5f, holder);
        holder.txtBTNameID.setText("Name: "+mDeviceNameList.get(position).toString());
        holder.txtBTMACAddressID.setText("MAC Address: "+mDeviceMACAddressList.get(position).toString());






        holder.cardMainViewMyNotifyID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Constant.Bluetooth_Activity_Navigation = 2;


                WebURL.BT_DEVICE_NAME = mDeviceNameList.get(position).toString();
                holder.txtDeviceNoID.setText(WebURL.BT_DEVICE_NAME);
                System.out.println("BT_NAME_SAP_MATCH=1=>"+WebURL.BT_DEVICE_NAME);
                WebURL.BT_DEVICE_MAC_ADDRESS = mDeviceMACAddressList.get(position).toString();
                System.out.println("BT_NAME_SAP_MATCH=2=>"+WebURL.BT_DEVICE_NAME);
                String BT_NAME_ORG = holder.txtDeviceNoID.getText().toString().trim();

                Constant.BT_DEVICE_NAME = mDeviceNameList.get(position).toString();
                Constant.BT_DEVICE_MAC_ADDRESS = mDeviceMACAddressList.get(position).toString();

                Intent intent = new Intent(mContext, BlueToothDebugNewActivity.class);
                intent.putExtra("BtNameHead",Constant.BT_DEVICE_NAME );
                intent.putExtra("BtMacAddressHead",Constant.BT_DEVICE_MAC_ADDRESS );
                mContext.startActivity(intent);

                ((Activity)mContext).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        // return galleryModelsList.size();
        if (mDeviceMACAddressList != null && mDeviceMACAddressList.size() > 0)
            return mDeviceMACAddressList.size();
        else
            return 0;
         // return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txtBTNameID, txtBTMACAddressID;
        public TextView txtDeviceNoID;

        public CardView cardMainViewMyNotifyID;


        public ViewHolder(View v) {

            super(v);


            txtDeviceNoID = (TextView) v.findViewById(R.id.txtDeviceNoID);
            txtBTNameID = (TextView) v.findViewById(R.id.txtBTNameID);
            txtBTMACAddressID = (TextView) v.findViewById(R.id.txtBTMACAddressID);
            cardMainViewMyNotifyID = (CardView) v.findViewById(R.id.cardMainViewMyNotifyID);

        }
    }



   /* private void changeButtonVisibility(boolean state, float alphaRate, BTPairedDeviceAdapter.ViewHolder holder) {
        holder.txtSetBTNID.setEnabled(state);
        holder.txtSetBTNID.setAlpha(alphaRate);
    }*/
}


