package adapter;

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
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.InstallationInitial;
import activity.UnloadInstReportImageActivity;
import bean.InstallationBean;
import bean.InstallationListBean;
import database.DatabaseHelper;
import debugapp.GlobalValue.Constant;
import debugapp.GlobalValue.NewSolarVFD;
import utility.CustomUtility;
import webservice.WebURL;

public class Adapter_Installation_list extends RecyclerView.Adapter<Adapter_Installation_list.HomeCategoryViewHolder> {
    DatabaseHelper db;
    private final Context context;
    private final ArrayList<InstallationListBean> responseList;
    private List<InstallationListBean> SearchesList = null;
    String pernr, billno;


    public Adapter_Installation_list(Context context, ArrayList<InstallationListBean> responseList) {
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
                pernr = CustomUtility.getSharedPreferences(context, "userid");
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
            if (!TextUtils.isEmpty(responseList.get(position).getCUS_CONTACT_NO())) {
                holder.cnt_no.setText(responseList.get(position).getCUS_CONTACT_NO());
            }
            if (!TextUtils.isEmpty(responseList.get(position).getAddress())) {
                holder.addrs.setText(responseList.get(position).getAddress());
            }
            if (!TextUtils.isEmpty(responseList.get(position).getBeneficiary())) {
                holder.ben_no.setText(responseList.get(position).getBeneficiary());
            }


            holder.status.setImageResource(R.drawable.red_icn);

            holder.cardView.setOnClickListener(view -> {
                    if (NewSolarVFD.CHECK_DATA_UNOLAD == 0) {
                        WebURL.mSettingCheckValue = "0";
                        if (responseList.get(position).getSync().equalsIgnoreCase("X")) {
                            Toast.makeText(context, "Installation Already Completed...", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent in = new Intent(context, InstallationInitial.class);
                            Bundle extras = new Bundle();
                            //    extras.putString("bill_no", responseList.get(position).get);
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
                            extras.putString("mobile", responseList.get(position).getCUS_CONTACT_NO());
                            extras.putString("controller", responseList.get(position).getController());
                            extras.putString("motor", responseList.get(position).getMotor());
                            extras.putString("pump", responseList.get(position).getPump());
                            extras.putString("simno", responseList.get(position).getSimno());
                            extras.putString("regisno", responseList.get(position).getRegisno());
                            extras.putString("projectno", responseList.get(position).getProjectno());
                            extras.putString("loginno", responseList.get(position).getLoginno());
                            extras.putString("moduleqty", responseList.get(position).getModuleqty());
                            extras.putString("CUS_CONTACT_NO", responseList.get(position).getCUS_CONTACT_NO());
                            extras.putString("BeneficiaryNo", responseList.get(position).getBeneficiary());
                            extras.putString("NoOfModule", responseList.get(position).getNoOfModule());
                            extras.putString("HP", responseList.get(position).getHP());
                            in.putExtras(extras);
                            context.startActivity(in);
                        }
                    } else {
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
                        /* intent.putExtra("vbeln", responseList.get(position).getBillno());
                        intent.putExtra("cust_name", custname);
                        intent.putExtra("userid", pernr);
                        intent.putExtra("moduleqty", responseList.get(position).getModuleqty());
                        intent.putExtra("mobile", responseList.get(position).getCUS_CONTACT_NO());
                        intent.putExtra("HP", responseList.get(position).getHP());
                        intent.putExtra("HP", responseList.get(position).getHP());
                        intent.putExtra("HP", responseList.get(position).getHP());
                        intent.putExtra("HP", responseList.get(position).getHP());
                        intent.putExtra(Constant.regisno, responseList.get(position).getRegisno());*/
                        context.startActivity(intent);
                    }
            });

            InstallationBean param_invc = new InstallationBean();
            param_invc = db.getInstallationData(pernr, billno);
            Log.e("param_invc===>",CustomUtility.getSharedPreferences(context, "INSTSYNC" + billno));
            if ((!TextUtils.isEmpty(param_invc.getLatitude()) && !TextUtils.isEmpty(param_invc.getLongitude())) && (!TextUtils.isEmpty(param_invc.getSolarpanel_wattage())) && (!TextUtils.isEmpty(param_invc.getNo_of_module_value()))) {
                holder.status.setImageResource(R.drawable.icn_yellow);
            }
            if ( !TextUtils.isEmpty(param_invc.getLatitude()) && !TextUtils.isEmpty(param_invc.getLongitude()) && CustomUtility.getSharedPreferences(context, "INSTSYNC" + billno).equalsIgnoreCase("1") && !TextUtils.isEmpty(param_invc.getSolarpanel_wattage()) && !TextUtils.isEmpty(param_invc.getNo_of_module_value())) {
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
        TextView bill_no, bill_dt, cust_nm, cnt_no, addrs, gst_bill_no, ben_no;
        ImageView status, icon;
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
            icon = itemView.findViewById(R.id.icon);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}