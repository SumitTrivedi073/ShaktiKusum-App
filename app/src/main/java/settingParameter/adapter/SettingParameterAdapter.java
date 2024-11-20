package settingParameter.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.List;

import settingParameter.model.MotorParamListModel;
import utility.CustomUtility;

public class SettingParameterAdapter extends RecyclerView.Adapter<SettingParameterAdapter.ViewHolder> {
    Context mContext;
    private List<MotorParamListModel.Response> cmponentList;
    private final List<MotorParamListModel.Response> arSearch;

    TextView noDataFound;

    private ItemclickListner itemclickListner;

    public SettingParameterAdapter(Context context, List<MotorParamListModel.Response> listdata, TextView noDataFound) {
        cmponentList = listdata;
        mContext = context;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(listdata);
        this.noDataFound = noDataFound;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.comp_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false);
        final MotorParamListModel.Response response = cmponentList.get(position);
        holder.title.setText(response.getParametersName());
        if(response.getModbusaddress().equals("2006")){
            holder.editTextValue.setText(String.valueOf(    (double) response.getpValue() / (double) 10));
        }else {
            holder.editTextValue.setText(String.valueOf(response.getpValue()));
        }

      //  holder.getBtn.setOnClickListener(v -> itemclickListner.getBtnMethod(response, holder.editTextValue.getText().toString(), position));
        holder.setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!response.getSet().toString().isEmpty() && response.getSet().toString().equals("false")) {
                    itemclickListner.setBtnMethod(response, holder.editTextValue.getText().toString(), position);
                }else {
                    CustomUtility.ShowToast(mContext.getResources().getString(R.string.alreadySet),mContext);
                }
            }
        });

       holder.editTextValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    cmponentList.get(position).setpValue(Float.valueOf(s.toString()));
                }
                // Log.e("getpValue===>", String.valueOf(cmponentList.get(position).getpValue()));
                Log.e("Editable===>", s.toString());
                Log.e("editTextValue===>", holder.editTextValue.getText().toString());
            }
        });

        if (!String.valueOf(cmponentList.get(position).getSet()).isEmpty()&&String.valueOf(cmponentList.get(position).getSet()).equals("false")) {
            holder.editTextValue.setTextColor(mContext.getResources().getColor(R.color.link_color));
            holder.setBtn.setBackgroundColor(mContext.getResources().getColor(R.color.link_color));
        }else {
            holder.editTextValue.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.setBtn.setBackgroundColor(mContext.getResources().getColor(R.color.blue_fb));
        }

    }

    @Override
    public int getItemCount() {
        return cmponentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lvlMainItemViewID;
        Button getBtn, setBtn;
        TextView title;
        EditText editTextValue;

        public ViewHolder(View itemView) {
            super(itemView);

            lvlMainItemViewID = itemView.findViewById(R.id.lvlMainItemViewID);
            getBtn = itemView.findViewById(R.id.getBtn);
            setBtn = itemView.findViewById(R.id.setBtn);
            title = itemView.findViewById(R.id.title);
            editTextValue = itemView.findViewById(R.id.editTextValue);


        }
    }

    public void EditItemClick(ItemclickListner response) {
        try {
            itemclickListner = response;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface ItemclickListner {
        void getBtnMethod(MotorParamListModel.Response response, String editvalue, int pos);

        void setBtnMethod(MotorParamListModel.Response response, String editvalue, int pos);

    }
}
