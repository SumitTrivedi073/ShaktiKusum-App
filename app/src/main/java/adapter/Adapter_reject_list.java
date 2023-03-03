package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bean.RejectListBean;
import database.DatabaseHelper;
import com.shaktipumplimited.shaktikusum.R;
import activity.RejectInstRepImgActivity;
import utility.CustomUtility;


public class Adapter_reject_list extends RecyclerView.Adapter<Adapter_reject_list.HomeCategoryViewHolder> {
    View.OnClickListener onclick_listener;
    DatabaseHelper db;
    RejectListBean rejectListBean;
    private Context context;
    private ArrayList<RejectListBean> responseList;
    private List<RejectListBean> SearchesList = null;


    public Adapter_reject_list(Context context, ArrayList<RejectListBean> responseList) {
        this.context = context;
        this.responseList = responseList;

        db = new DatabaseHelper(context);

        this.SearchesList = new ArrayList<RejectListBean>();
        this.SearchesList.addAll(responseList);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_reject_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {

        try {

            if (!TextUtils.isEmpty(responseList.get(position).getBillno())) {

                holder.bill_no.setText(responseList.get(position).getBillno());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getBenno())) {

                holder.ben_no.setText(responseList.get(position).getBenno());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getRegno())) {

                holder.reg_no.setText(responseList.get(position).getRegno());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getCustnm())) {

                holder.cust_nm.setText(responseList.get(position).getCustnm());

            }


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Intent in = new Intent(context, RejectInstRepImgActivity.class);


                        Bundle extras = new Bundle();
                        extras.putString("bill_no", responseList.get(position).getBillno());
                        extras.putString("userid", CustomUtility.getSharedPreferences(context, "userid"));
                        extras.putString("regisno", responseList.get(position).getRegno());
                        extras.putString("beneficiary", responseList.get(position).getBenno());
                        extras.putString("projno", CustomUtility.getSharedPreferences(context, "projectid"));
                        extras.putString("cust_name", responseList.get(position).getCustnm());


                        extras.putString("photo1", responseList.get(position).getPhoto1());
                        extras.putString("photo2", responseList.get(position).getPhoto2());
                        extras.putString("photo3", responseList.get(position).getPhoto3());
                        extras.putString("photo4", responseList.get(position).getPhoto4());
                        extras.putString("photo5", responseList.get(position).getPhoto5());
                        extras.putString("photo6", responseList.get(position).getPhoto6());
                        extras.putString("photo7", responseList.get(position).getPhoto7());
                        extras.putString("photo8", responseList.get(position).getPhoto8());
                        extras.putString("photo9", responseList.get(position).getPhoto9());
                        extras.putString("photo10", responseList.get(position).getPhoto10());
                        extras.putString("photo11", responseList.get(position).getPhoto11());
                        extras.putString("photo12", responseList.get(position).getPhoto12());

                    extras.putString("remark1", responseList.get(position).getRemark1());
                    extras.putString("remark2", responseList.get(position).getRemark2());
                    extras.putString("remark3", responseList.get(position).getRemark3());
                    extras.putString("remark4", responseList.get(position).getRemark4());
                    extras.putString("remark5", responseList.get(position).getRemark5());
                    extras.putString("remark6", responseList.get(position).getRemark6());
                    extras.putString("remark7", responseList.get(position).getRemark7());
                    extras.putString("remark8", responseList.get(position).getRemark8());
                    extras.putString("remark9", responseList.get(position).getRemark9());
                    extras.putString("remark10", responseList.get(position).getRemark10());
                    extras.putString("remark11", responseList.get(position).getRemark11());
                    extras.putString("remark12", responseList.get(position).getRemark12());

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
            for (RejectListBean cs : SearchesList) {
                if (cs.getBillno().toLowerCase(Locale.getDefault()).contains(charText)) {
                    responseList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }

    class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView bill_no, ben_no,reg_no,cust_nm;


        CardView cardView;

        HomeCategoryViewHolder(View itemView) {
            super(itemView);

            bill_no = itemView.findViewById(R.id.bill_no);
            ben_no = itemView.findViewById(R.id.ben_no);
            reg_no = itemView.findViewById(R.id.reg_no);
            cust_nm = itemView.findViewById(R.id.cust_nm);

            cardView = itemView.findViewById(R.id.card_view);

        }
    }


}