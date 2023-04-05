package com.shaktipumplimited.shaktikusum.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.shaktipumplimited.shaktikusum.bean.SiteAuditListBean;
import com.shaktipumplimited.shaktikusum.database.DatabaseHelper;
import com.shaktipumplimited.shaktikusum.R;
import com.shaktipumplimited.shaktikusum.activity.SiteAuditInitial;

import com.shaktipumplimited.shaktikusum.utility.CustomUtility;


public class Adapter_auditsite_list extends RecyclerView.Adapter<Adapter_auditsite_list.HomeCategoryViewHolder> {
    DatabaseHelper db;
    private Context context;
    private ArrayList<SiteAuditListBean> responseList;
    private List<SiteAuditListBean> SearchesList = null;
    String pernr,billno;


    public Adapter_auditsite_list(Context context, ArrayList<SiteAuditListBean> responseList) {
        this.context = context;
        this.responseList = responseList;
        db = new DatabaseHelper(context);
        this.SearchesList = new ArrayList<SiteAuditListBean>();
        this.SearchesList.addAll(responseList);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_auditsite_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {

        try {

            if (!TextUtils.isEmpty(responseList.get(position).getBillno())) {

                holder.bill_no.setText(responseList.get(position).getBillno());

                billno = responseList.get(position).getBillno();
                pernr  = CustomUtility.getSharedPreferences(context, "userid");

            }

            if (!TextUtils.isEmpty(responseList.get(position).getGstbillno())) {

                holder.gst_bill_no.setText(responseList.get(position).getGstbillno());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getBilldate())) {

                holder.bill_dt.setText(responseList.get(position).getBilldate());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getCustomer_name())) {

                holder.cust_nm.setText(responseList.get(position).getCustomer_name());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getContact_no())) {

                holder.cnt_no.setText(responseList.get(position).getContact_no());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getAddress())) {

                holder.addrs.setText(responseList.get(position).getAddress());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getBeneficiary())) {

                holder.ben_no.setText(responseList.get(position).getBeneficiary());

            }


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("registr","&&&"+responseList.get(position).getRegisno());

                        Intent in = new Intent(context, SiteAuditInitial.class);
                        Bundle extras = new Bundle();
                        extras.putString("bill_no", responseList.get(position).getBillno());
                        extras.putString("bill_date", responseList.get(position).getBilldate());
                        extras.putString("name", responseList.get(position).getCustomer_name());
                        extras.putString("regisno", responseList.get(position).getRegisno());
                        extras.putString("state", responseList.get(position).getState());
                        extras.putString("city", responseList.get(position).getCity());
                        extras.putString("address", responseList.get(position).getAddress());
                        extras.putString("contact", responseList.get(position).getContact_no());
                        in.putExtras(extras);
                        context.startActivity(in);

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

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        responseList.clear();
        if (charText.length() == 0) {
            responseList.addAll(SearchesList);
        } else {
            for (SiteAuditListBean cs : SearchesList) {
                if (cs.getBillno().toLowerCase(Locale.getDefault()).contains(charText) || cs.getBeneficiary().toLowerCase(Locale.getDefault()).contains(charText)) {
                    responseList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }

    class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView bill_no, bill_dt, cust_nm, cnt_no, addrs, gst_bill_no,ben_no;
        ImageView status;

        CardView cardView;

        HomeCategoryViewHolder(View itemView) {
            super(itemView);

            bill_no = itemView.findViewById(R.id.bill_no);
            bill_dt = itemView.findViewById(R.id.bill_dt);
            cust_nm = itemView.findViewById(R.id.cust_nm);
            cnt_no = itemView.findViewById(R.id.cnt_no);
            addrs = itemView.findViewById(R.id.addrs);
            gst_bill_no = itemView.findViewById(R.id.gst_bill_no);
            ben_no = itemView.findViewById(R.id.benf_no);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }


}