package debugapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.List;

import debugapp.Bean.SurweyListResponse;


public class SurweyListAdapter extends RecyclerView.Adapter<SurweyListAdapter.ViewHolder> {

    private final Context mContext;
    private final List<SurweyListResponse> mSurweyListResponse;
    private List<Boolean> mLrInvoiceSelectionCheck;


    public SurweyListAdapter(Context mContext, List<SurweyListResponse> mLrInvoiceResponse) {
        this.mContext = mContext;
        this.mSurweyListResponse = mLrInvoiceResponse;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.surwey_list_item, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txtMaterialCodeValueID.setText(mSurweyListResponse.get(position).getBeneficiary());
        holder.txtDecriptionValueID.setText(mSurweyListResponse.get(position).getCustomerName());
        holder.txtQuentityValueID.setText(mSurweyListResponse.get(position).getMobile());
        holder.txtPriceValueID.setText(mSurweyListResponse.get(position).getAddress());


        holder.rlvMainItemViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Add_Surway_Activity.class);
                intent.putExtra("Beneficiary", mSurweyListResponse.get(position).getBeneficiary());
                intent.putExtra("ProjectNo", mSurweyListResponse.get(position).getProjectNo());
                intent.putExtra("Mobile", mSurweyListResponse.get(position).getMobile());
                intent.putExtra("Lifnr", mSurweyListResponse.get(position).getLifnr());
                intent.putExtra("CitycTxt", mSurweyListResponse.get(position).getAddress());
                intent.putExtra("ApplicantName", mSurweyListResponse.get(position).getCustomerName());

                intent.putExtra("RegistraionNo", mSurweyListResponse.get(position).getRegisno());

                intent.putExtra("regio_txt", mSurweyListResponse.get(position).getAddress()+" "+mSurweyListResponse.get(position).getCitycTxt()+" "+mSurweyListResponse.get(position).getRegioTxt());

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        // return galleryModelsList.size();
        if (mSurweyListResponse != null && mSurweyListResponse.size() > 0)
            return mSurweyListResponse.size();
        else
            return 0;
       //  return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


    ImageView imgCheckBoxImageID;
       // public TextView txtTRLRValueID, txtTRNameValueID, txtMOBNumberValueID, txtLRDateValueID,txtSAPBillValueID, txtTRBLDValueID, txtGSTBillValueID;
        public TextView txtMaterialCodeValueID, txtDecriptionValueID, txtQuentityValueID, txtPriceValueID;


        public RelativeLayout rlvNotifyItemMainViewID, rlvMainItemViewID;

        public ViewHolder(View v) {

            super(v);

            rlvMainItemViewID =  v.findViewById(R.id.rlvMainItemViewID);
            txtMaterialCodeValueID =  v.findViewById(R.id.txtMaterialCodeValueID);
            txtDecriptionValueID =  v.findViewById(R.id.txtDecriptionValueID);
            txtQuentityValueID =  v.findViewById(R.id.txtQuentityValueID);
            txtPriceValueID =  v.findViewById(R.id.txtPriceValueID);

          }
    }




/*

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
*/


}


