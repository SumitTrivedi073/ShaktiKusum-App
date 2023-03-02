package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import bean.SurveyListBean;
import database.DatabaseHelper;

import com.shaktipumplimited.shaktikusum.R;
import com.shaktipumplimited.shaktikusum.SurveyActivity;


public class Adapter_Survey_list extends RecyclerView.Adapter<Adapter_Survey_list.HomeCategoryViewHolder> {
    DatabaseHelper db;
    private Context context;
    private ArrayList<SurveyListBean> responseList;
    private List<SurveyListBean> SearchesList = null;


    public Adapter_Survey_list(Context context, ArrayList<SurveyListBean> responseList) {
        this.context = context;
        this.responseList = responseList;

        db = new DatabaseHelper(context);

        this.SearchesList = new ArrayList<SurveyListBean>();
        this.SearchesList.addAll(responseList);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_survey_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {

        try {

            if (!TextUtils.isEmpty(responseList.get(position).getBen_id())) {

                holder.ben_id.setText(responseList.get(position).getBen_id());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getState())) {

                holder.state.setText(responseList.get(position).getState());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getDistrict())) {

                holder.district.setText(responseList.get(position).getDistrict());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getCustnam())) {

                holder.cust_nm.setText(responseList.get(position).getCustnam());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getContctno())) {

                holder.cnt_no.setText(responseList.get(position).getContctno());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getAddress())) {

                holder.addrs.setText(responseList.get(position).getAddress());

            }




            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                        Intent in = new Intent(context, SurveyActivity.class);

                        Bundle extras = new Bundle();
                        extras.putString("ben_id", responseList.get(position).getBen_id());
                        extras.putString("cust_nm", responseList.get(position).getCustnam());
                        extras.putString("cont_no", responseList.get(position).getContctno());
                        extras.putString("state", responseList.get(position).getState());
                        extras.putString("district", responseList.get(position).getDistrict());
                        extras.putString("address", responseList.get(position).getAddress());
                        extras.putString("regino", responseList.get(position).getRegino());


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
            for (SurveyListBean cs : SearchesList) {
                if (cs.getBen_id().toLowerCase(Locale.getDefault()).contains(charText)) {
                    responseList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }

    class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView ben_id,  cust_nm, cnt_no, state, district,addrs;
        ImageView status;

        CardView cardView;

        HomeCategoryViewHolder(View itemView) {
            super(itemView);

            ben_id = itemView.findViewById(R.id.ben_id);

            cust_nm = itemView.findViewById(R.id.cust_nam);
            cnt_no = itemView.findViewById(R.id.cnt_no);
            state = itemView.findViewById(R.id.state);
            district = itemView.findViewById(R.id.distrct);
            addrs = itemView.findViewById(R.id.addrs);


            cardView = itemView.findViewById(R.id.card_view);

        }
    }


}