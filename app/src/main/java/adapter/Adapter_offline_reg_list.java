package adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;

import bean.RegistrationBean;
import database.DatabaseHelper;


public class Adapter_offline_reg_list extends RecyclerView.Adapter<Adapter_offline_reg_list.HomeCategoryViewHolder> {
    View.OnClickListener onclick_listener;
    DatabaseHelper db;

    private final Context context;

    private final ArrayList<RegistrationBean> responseList;


    public Adapter_offline_reg_list(Context context, ArrayList<RegistrationBean> responseList) {
        this.context = context;
        this.responseList = responseList;
        this.onclick_listener = onclick_listener;

        db = new DatabaseHelper(context);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_reg_offline_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {

        try {

            if (!TextUtils.isEmpty(responseList.get(position).getDate())) {

                holder.reg_dt.setText(responseList.get(position).getDate());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getCustomer_name())) {

                holder.cust_nm.setText(responseList.get(position).getCustomer_name());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getFather_name())) {

                holder.fath_nm.setText(responseList.get(position).getFather_name());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getStatetxt())) {

                holder.state.setText(responseList.get(position).getStatetxt());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getCitytxt())) {

                holder.district.setText(responseList.get(position).getCitytxt());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView reg_dt, cust_nm, fath_nm, state, district;

        CardView cardView;

        public HomeCategoryViewHolder(View itemView) {
            super(itemView);

            reg_dt = itemView.findViewById(R.id.reg_dt);
            cust_nm = itemView.findViewById(R.id.cust_nm);
            fath_nm = itemView.findViewById(R.id.fath_nm);
            state = itemView.findViewById(R.id.state);
            district = itemView.findViewById(R.id.district);


            cardView = itemView.findViewById(R.id.card_view);

        }
    }


}