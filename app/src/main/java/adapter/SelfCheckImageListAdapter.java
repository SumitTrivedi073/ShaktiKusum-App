package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shaktipumplimited.shaktikusum.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

import activity.PhotoViewerActivity;
import bean.SelfCheckImageBean;
import bean.SelfCheckListBean;
import debugapp.GlobalValue.Constant;
import debugapp.PendingInstallationModel;
import utility.CustomUtility;


public class SelfCheckImageListAdapter extends RecyclerView.Adapter<SelfCheckImageListAdapter.HomeCategoryViewHolder> {
    List<SelfCheckImageBean> imageList;
    private final Context mcontext;
    private  SelfCheckListBean.InstallationDatum responseList;
    private SelfCheckImageListAdapter.SendImageListener sendIMGListener;

    public SelfCheckImageListAdapter(Context mcontext, List<SelfCheckImageBean> imageList, SelfCheckListBean.InstallationDatum responseList) {
        this.imageList = imageList;
        this.mcontext = mcontext;
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public SelfCheckImageListAdapter.HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.adapter_self_image_item, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelfCheckImageListAdapter.HomeCategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

            Glide.with(mcontext)
                    .load(imageList.get(position).getImage1())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(mcontext.getResources().getDrawable(R.drawable.logo))

                    .into(holder.icon_img);
//            holder.icon_img.setImageBitmap(imageList.get(position).getImage1());


            holder.icon_img.setOnClickListener(v -> sendIMGListener.sendImageListener(imageList,position));

    }

    public void SendIMG(SelfCheckImageListAdapter.SendImageListener response) {
        try {
            sendIMGListener = response;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface SendImageListener {
         void sendImageListener(List<SelfCheckImageBean> imageList, int position);

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView icon_img;
        CardView card_view;
        public HomeCategoryViewHolder(View itemView) {
            super(itemView);
            icon_img = itemView.findViewById(R.id.icon_img);
            card_view = itemView.findViewById(R.id.card_view);

        }
    }

}
