package com.shaktipumplimited.shaktikusum.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.shaktipumplimited.shaktikusum.bean.SimCardBean;
import com.shaktipumplimited.shaktikusum.database.DatabaseHelper;
import com.shaktipumplimited.shaktikusum.R;
import com.shaktipumplimited.shaktikusum.activity.SIMActivationDetails;
import com.shaktipumplimited.shaktikusum.utility.CustomUtility;


public class Adapter_sim_list extends RecyclerView.Adapter<Adapter_sim_list.HomeCategoryViewHolder> {
    View.OnClickListener onclick_listener;
    DatabaseHelper db;
    String enq_docno = "";
    String user_id = "";
    String bill_date = "";
    String item_no = "";
    String amc_cmc = "";

    CustomUtility customUtility = new CustomUtility();
    private Context context;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ArrayList<SimCardBean> responseList;


    public Adapter_sim_list(Context context, ArrayList<SimCardBean> responseList, View.OnClickListener onclick_listener) {
        this.context = context;
        this.responseList = responseList;
        this.onclick_listener = onclick_listener;


        Log.e("RESPONSE", "&&&&&" + responseList.toString());

        db = new DatabaseHelper(context);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_sim_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {

        try {
            if (!TextUtils.isEmpty(responseList.get(position).getCust_name())) {

                holder.cust_name.setText(responseList.get(position).getCust_name());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getCust_mobile())) {

                holder.cust_mob.setText(responseList.get(position).getCust_mobile());

            }


                if (!TextUtils.isEmpty(responseList.get(position).getCust_address())) {

                    holder.cust_add.setText(responseList.get(position).getCust_address());

                }
                if (!TextUtils.isEmpty(responseList.get(position).getSim_rep_date())) {

                    holder.sim_date.setText(responseList.get(position).getSim_rep_date());

                }
                if (!TextUtils.isEmpty(responseList.get(position).getDevice_no())) {

                    holder.serial_no.setText(responseList.get(position).getDevice_no());

                }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    enq_docno = responseList.get(position).getEnq_docno();

                    Intent offline_list = new Intent(context, SIMActivationDetails.class);
                    Bundle extras = new Bundle();
                    extras.putString("from", "oldform");
                    extras.putString("enqdocno", enq_docno);
                    offline_list.putExtras(extras);
                    context.startActivity(offline_list);
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }


    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView sim_date, serial_no, cust_name, cust_mob, cust_add;
        CardView cardView;

        public HomeCategoryViewHolder(View itemView) {
            super(itemView);


            sim_date = itemView.findViewById(R.id.sim_date);
            serial_no = itemView.findViewById(R.id.serial_no);
            cust_name = itemView.findViewById(R.id.cust_name);
            cust_mob = itemView.findViewById(R.id.cust_mob);
            cust_add = itemView.findViewById(R.id.cust_add);

            cardView = itemView.findViewById(R.id.card_view);


        }
    }



}