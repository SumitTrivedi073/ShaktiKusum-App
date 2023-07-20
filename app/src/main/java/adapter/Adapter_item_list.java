package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;

import activity.CustomerRegistrationActivity;
import activity.DDSubmissionActivity;
import activity.DeptDocSubActivity;
import activity.GovtOffVisitActivity;
import activity.InstallationList;
import activity.InstallationListOfflineActivity;
import activity.OffSubDocActivity;
import bean.ItemNameBean;
import database.DatabaseHelper;
import utility.CustomUtility;

public class Adapter_item_list extends RecyclerView.Adapter<Adapter_item_list.HomeCategoryViewHolder> {
    View.OnClickListener onclick_listener;
    DatabaseHelper db;
    String item_id = "";

    private final Context context;

    private final ArrayList<ItemNameBean> responseList;

    public Adapter_item_list(Context context, ArrayList<ItemNameBean> responseList, View.OnClickListener onclick_listener) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_item_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            if (!TextUtils.isEmpty(responseList.get(position).getItem_id())) {

                if (responseList.get(position).getItem_id().equalsIgnoreCase("001")) {
                    holder.icon_img.setImageResource(R.mipmap.registration);
                }else if (responseList.get(position).getItem_id().equalsIgnoreCase("004")) {
                    holder.icon_img.setImageResource(R.mipmap.collection);
                } else if (responseList.get(position).getItem_id().equalsIgnoreCase("003")) {
                    holder.icon_img.setImageResource(R.mipmap.pending);
                } else if (responseList.get(position).getItem_id().equalsIgnoreCase("005")) {
                    holder.icon_img.setImageResource(R.mipmap.workorder);
                } else if (responseList.get(position).getItem_id().equalsIgnoreCase("009")) {
                    holder.icon_img.setImageResource(R.mipmap.insta);
                }else if (responseList.get(position).getItem_id().equalsIgnoreCase("000")) {
                    holder.icon_img.setImageResource(R.mipmap.insta);
                } else if (responseList.get(position).getItem_id().equalsIgnoreCase("010")) {
                    holder.icon_img.setImageResource(R.mipmap.officervisit);
                } else if (responseList.get(position).getItem_id().equalsIgnoreCase("011")) {
                    holder.icon_img.setImageResource(R.mipmap.office);
                } else if (responseList.get(position).getItem_id().equalsIgnoreCase("012")) {
                    holder.icon_img.setImageResource(R.mipmap.department);
                } else if (responseList.get(position).getItem_id().equalsIgnoreCase("015")) {
                    holder.icon_img.setImageResource(R.mipmap.codecreation);
                } else if (responseList.get(position).getItem_id().equalsIgnoreCase("017")) {
                    holder.icon_img.setImageResource(R.mipmap.survey);
                }
            }
            if (!TextUtils.isEmpty(responseList.get(position).getItem_name())) {
                holder.item_txt.setText(responseList.get(position).getItem_name());
            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_id = responseList.get(position).getItem_id();
                    if (responseList.get(position).getItem_id().equalsIgnoreCase("001")) {
                        CustomUtility.setSharedPreference(context, "enqdocid", "");
                        CustomUtility.setSharedPreference(context, "process_no", responseList.get(position).getItem_id());
                        Intent register_form = new Intent(context, CustomerRegistrationActivity.class);
                        context.startActivity(register_form);
                    } else if (responseList.get(position).getItem_id().equalsIgnoreCase("004")) {
                        CustomUtility.setSharedPreference(context, "process_no", responseList.get(position).getItem_id());
                        Intent dd_sub = new Intent(context, DDSubmissionActivity.class);
                        context.startActivity(dd_sub);
                    } else if (responseList.get(position).getItem_id().equalsIgnoreCase("009")) {
                        CustomUtility.setSharedPreference(context, "process_no", responseList.get(position).getItem_id());
                        Intent inst_list = new Intent(context, InstallationList.class);
                        context.startActivity(inst_list);
                    }else if (responseList.get(position).getItem_id().equalsIgnoreCase("000")) {
                        CustomUtility.setSharedPreference(context, "process_no", responseList.get(position).getItem_id());
                        Intent inst_list = new Intent(context, InstallationListOfflineActivity.class);
                        context.startActivity(inst_list);
                    } else if (responseList.get(position).getItem_id().equalsIgnoreCase("010")) {
                        CustomUtility.setSharedPreference(context, "process_no", responseList.get(position).getItem_id());
                        Intent intent = new Intent(context, GovtOffVisitActivity.class);
                        context.startActivity(intent);
                    } else if (responseList.get(position).getItem_id().equalsIgnoreCase("011")) {
                        CustomUtility.setSharedPreference(context, "process_no", responseList.get(position).getItem_id());
                        Intent inst_list = new Intent(context, OffSubDocActivity.class);
                        context.startActivity(inst_list);
                    } else if (responseList.get(position).getItem_id().equalsIgnoreCase("012")) {
                        CustomUtility.setSharedPreference(context, "process_no", responseList.get(position).getItem_id());
                        Intent intent = new Intent(context, DeptDocSubActivity.class);
                        context.startActivity(intent);
                    } /*else if (responseList.get(position).getItem_id().equalsIgnoreCase("017")) {
                        CustomUtility.setSharedPreference(context, "process_no", responseList.get(position).getItem_id());
                        Intent intent = new Intent(context, SurveyList.class);
                        context.startActivity(intent);
                    }*/
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
        TextView item_txt;
        ImageView icon_img;
        CardView cardView;

        public HomeCategoryViewHolder(View itemView) {
            super(itemView);
            item_txt = itemView.findViewById(R.id.item_txt);
            icon_img = itemView.findViewById(R.id.icon_img);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}