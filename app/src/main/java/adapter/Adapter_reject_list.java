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
    private final Context context;
    private final ArrayList<RejectListBean> responseList;
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


        RejectListBean rejectList = responseList.get(position);
            if (!TextUtils.isEmpty(rejectList.getBillno())) {

                holder.bill_no.setText(rejectList.getBillno());

            }

            if (!TextUtils.isEmpty(rejectList.getBenno())) {

                holder.ben_no.setText(rejectList.getBenno());

            }

            if (!TextUtils.isEmpty(rejectList.getRegno())) {

                holder.reg_no.setText(rejectList.getRegno());

            }

            if (!TextUtils.isEmpty(rejectList.getCustnm())) {

                holder.cust_nm.setText(rejectList.getCustnm());

            }


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Intent in = new Intent(context, RejectInstRepImgActivity.class);
                         in.putExtra("RejectImagesList",rejectList);
                         context.startActivity(in);

                }
            });


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