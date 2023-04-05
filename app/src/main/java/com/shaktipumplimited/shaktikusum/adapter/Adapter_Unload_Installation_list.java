package com.shaktipumplimited.shaktikusum.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.shaktipumplimited.shaktikusum.bean.InstallationBean;
import com.shaktipumplimited.shaktikusum.bean.InstallationListBean;
import com.shaktipumplimited.shaktikusum.database.DatabaseHelper;
import com.shaktipumplimited.shaktikusum.activity.InstallationInitial;
import com.shaktipumplimited.shaktikusum.R;

import com.shaktipumplimited.shaktikusum.utility.CustomUtility;
import com.shaktipumplimited.shaktikusum.webservice.WebURL;


public class Adapter_Unload_Installation_list extends RecyclerView.Adapter<Adapter_Unload_Installation_list.HomeCategoryViewHolder> {
    View.OnClickListener onclick_listener;
    DatabaseHelper db;
    InstallationBean installationBean;
    private Context context;
    private ArrayList<InstallationListBean> responseList;
    private List<InstallationListBean> SearchesList = null;
    String pernr,billno;


    public Adapter_Unload_Installation_list(Context context, ArrayList<InstallationListBean> responseList) {
        this.context = context;
        this.responseList = responseList;
        db = new DatabaseHelper(context);
        this.SearchesList = new ArrayList<InstallationListBean>();
        this.SearchesList.addAll(responseList);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_installation_list, parent, false);
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


          /*  installationBean = new InstallationBean();
            installationBean = db.getInstallationData(CustomUtility.getSharedPreferences(context,"userid"),responseList.get(position).getBillno());*/

              /*  if(TextUtils.isEmpty(installationBean.getLatitude()) && TextUtils.isEmpty(installationBean.getLatitude()) && TextUtils.isEmpty(installationBean.getRms_data_status())&& TextUtils.isEmpty(installationBean.getInst_date()))
                {
                    if(installationBean.getLatitude().equalsIgnoreCase("") && installationBean.getLatitude().equalsIgnoreCase("") && installationBean.getRms_data_status().equalsIgnoreCase("")&& installationBean.getInst_date().equalsIgnoreCase("")) {
                        holder.status.setImageDrawable(ActivityCompat.getDrawable(context,
                                R.drawable.red_icn));
                    }
                }
                else if(!TextUtils.isEmpty(installationBean.getLatitude()) && !TextUtils.isEmpty(installationBean.getLatitude()) && !TextUtils.isEmpty(installationBean.getRms_data_status())&& !TextUtils.isEmpty(installationBean.getInst_date())){

                    if(installationBean.getLatitude().equalsIgnoreCase("") && installationBean.getLatitude().equalsIgnoreCase("") && installationBean.getRms_data_status().equalsIgnoreCase("")&& installationBean.getInst_date().equalsIgnoreCase("")) {
                        holder.status.setImageDrawable(ActivityCompat.getDrawable(context,
                                R.drawable.icn_yellow));
                    }
                }
*/

            holder.status.setImageResource(R.drawable.red_icn);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    WebURL.mSettingCheckValue = "0";

                    if (responseList.get(position).getSync().equalsIgnoreCase("X")) {
                        Toast.makeText(context, "Installation Already Completed...", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent in = new Intent(context, InstallationInitial.class);

                        Bundle extras = new Bundle();

                        extras.putString("bill_no", responseList.get(position).getBillno());
                        extras.putString("set_matno", responseList.get(position).getSet_matno());
                        extras.putString("simha2", responseList.get(position).getSimha2());
                        extras.putString("kunnr", responseList.get(position).getKunnr());
                        extras.putString("gst_bill_no", responseList.get(position).getGstbillno());
                        extras.putString("tehvillage", responseList.get(position).getTehsil());
                        extras.putString("bill_date", responseList.get(position).getBilldate());
                        extras.putString("disp_date", responseList.get(position).getDispdate());
                        extras.putString("name", responseList.get(position).getCustomer_name());
                        extras.putString("state", responseList.get(position).getState());
                        extras.putString("city", responseList.get(position).getCity());
                        extras.putString("state_txt", responseList.get(position).getStatetxt());
                        extras.putString("city_txt", responseList.get(position).getCitytxt());
                        extras.putString("address", responseList.get(position).getAddress());
                        extras.putString("mobile", responseList.get(position).getContact_no());
                        extras.putString("controller", responseList.get(position).getController());
                        extras.putString("motor", responseList.get(position).getMotor());
                        extras.putString("pump", responseList.get(position).getPump());
                        extras.putString("simno", responseList.get(position).getSimno());
                        extras.putString("regisno", responseList.get(position).getRegisno());
                        extras.putString("projectno", responseList.get(position).getProjectno());
                        extras.putString("loginno", responseList.get(position).getLoginno());
                        extras.putString("moduleqty", responseList.get(position).getModuleqty());
                        in.putExtras(extras);
                        context.startActivity(in);
                    }

                }
            });

            InstallationBean param_invc = new InstallationBean();

            param_invc = db.getInstallationData(pernr, billno);


            if ((!TextUtils.isEmpty(param_invc.getLatitude()) && !TextUtils.isEmpty(param_invc.getLongitude())) && (!TextUtils.isEmpty(param_invc.getSolarpanel_wattage())) && (!TextUtils.isEmpty(param_invc.getNo_of_module_value()))) {
                holder.status.setImageResource(R.drawable.icn_yellow);
            }

            if (!TextUtils.isEmpty(param_invc.getLatitude()) && !TextUtils.isEmpty(param_invc.getLongitude()) && CustomUtility.getSharedPreferences(context, "INSTSYNC" + billno).equalsIgnoreCase("1") && !TextUtils.isEmpty(param_invc.getSolarpanel_wattage()) && !TextUtils.isEmpty(param_invc.getNo_of_module_value())) {
                    holder.status.setImageResource(R.drawable.right_mark_icn_green);
            }



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
            for (InstallationListBean cs : SearchesList) {
                if (cs.getBillno().toLowerCase(Locale.getDefault()).contains(charText) || cs.getBeneficiary().toLowerCase(Locale.getDefault()).contains(charText)) {
                    responseList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }

    class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView bill_no, bill_dt, cust_nm, cnt_no, addrs, gst_bill_no,ben_no;
        ImageView status,icon;

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
            status = itemView.findViewById(R.id.status);
             icon  = (ImageView) itemView.findViewById(R.id.icon) ;

            cardView = itemView.findViewById(R.id.card_view);

        }
    }


}