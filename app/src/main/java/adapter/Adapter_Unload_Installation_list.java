package adapter;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.InstallationInitial;
import activity.UnloadInstReportImageActivity;
import bean.InstallationBean;
import bean.InstallationListBean;
import bean.unloadingDataBean;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.WebURL;


public class Adapter_Unload_Installation_list extends RecyclerView.Adapter<Adapter_Unload_Installation_list.HomeCategoryViewHolder> {
    View.OnClickListener onclick_listener;
    DatabaseHelper db;
    private final Context context;
    private final ArrayList<InstallationListBean> responseList;
    private List<InstallationListBean> SearchesList = null;
    List<unloadingDataBean> unloadingDataBeans;
    String pernr,billno;


    public Adapter_Unload_Installation_list(Context context, ArrayList<InstallationListBean> responseList) {
        this.context = context;
        this.responseList = responseList;
        db = new DatabaseHelper(context);
        this.SearchesList = new ArrayList<>();
        this.SearchesList.addAll(responseList);
        this.unloadingDataBeans = new ArrayList<>();

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
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, @SuppressLint("RecyclerView") final int position) {

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

            holder.cardView.setOnClickListener(view -> {

                WebURL.mSettingCheckValue = "0";

                String custname = "";
                String fathname = "";
                String project_no = "", regisno = "", beneficiary = "";
                try {
                    regisno = responseList.get(position).getRegisno();
                    beneficiary = responseList.get(position).getBeneficiary();
                    project_no = responseList.get(position).getProjectno();
                    WebURL.ProjectNo_Con = project_no;
                    WebURL.BenificiaryNo_Con = beneficiary;
                    WebURL.RegNo_Con = regisno;

                    System.out.println("unload List =>" + WebURL.RegNo_Con + "/n WebURL.BenificiaryNo_Con=" + WebURL.BenificiaryNo_Con + "/n WebURL.ProjectNo_Con=" + WebURL.ProjectNo_Con);
                    String[] custnmStr = responseList.get(position).getCustomer_name().split("S/O", 2);
                    if (custnmStr.length == 2) {
                        custname = custnmStr[0];
                        String Custfathname = custnmStr[1];
                        String[] custfathStr = Custfathname.split("-", 2);
                        fathname = custfathStr[0];
                        Log.e("fath", "&&&&" + fathname);
                    } else {
                        custname = custnmStr[0];
                        String Custfathname = "";
                        fathname = "";
                        Log.e("fath", "&&&&" + fathname);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, UnloadInstReportImageActivity.class);
                intent.putExtra(Constant.unloadingData,responseList.get(position));
                context.startActivity(intent);
            });


            unloadingDataBeans = db.getUnloadingData(billno);

            if(unloadingDataBeans.size()>0){
                if ((!TextUtils.isEmpty(unloadingDataBeans.get(0).getPump_serial_no()) && !TextUtils.isEmpty(unloadingDataBeans.get(0).getMotor_serial_no())) && (!TextUtils.isEmpty(unloadingDataBeans.get(0).getController_serial_no())) && (!TextUtils.isEmpty(unloadingDataBeans.get(0).getPanel_module_qty()))) {
                    holder.status.setImageResource(R.drawable.icn_yellow);
                }

                if (!TextUtils.isEmpty(unloadingDataBeans.get(0).getPump_serial_no()) && !TextUtils.isEmpty(unloadingDataBeans.get(0).getMotor_serial_no())  && (!TextUtils.isEmpty(unloadingDataBeans.get(0).getController_serial_no()))  && (!TextUtils.isEmpty(unloadingDataBeans.get(0).getPanel_module_qty())) && !TextUtils.isEmpty(unloadingDataBeans.get(0).getMaterial_status())) {
                    holder.status.setImageResource(R.drawable.right_mark_icn_green);
                }
            }else {
                holder.status.setImageResource(R.drawable.red_icn);
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
             icon  = itemView.findViewById(R.id.icon);

            cardView = itemView.findViewById(R.id.card_view);

        }
    }


}