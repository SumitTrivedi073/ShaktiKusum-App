package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.util.List;

import bean.ImageModel;

public class ImageSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ImageModel> imageModelList;
    Context mcontext;
    private ImageSelectionListener docSelectionListener;
    boolean isInstallation;

    public ImageSelectionAdapter(Context contact_, List<ImageModel> listdata, boolean isInstallation) {
        this.imageModelList = listdata;
        this.mcontext = contact_;
        this.isInstallation = isInstallation;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.imagelistitem, parent, false);

        return new ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder
            , @SuppressLint("RecyclerView") final int position) {

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            final ImageModel imageModel = imageModelList.get(position);

            ((ViewHolder) holder).title.setText(imageModel.getName());

            if(isInstallation){
                if(position==0||position==1||position==2||position==3||position==4||position==11){
                    ((ViewHolder) holder).star_icon.setVisibility(View.VISIBLE);
                }
            }

            if(imageModel.isImageSelected()){
                ((ViewHolder) holder).image.setImageResource(R.drawable.right_mark_icn_green);
            }else {
                ((ViewHolder) holder).image.setImageResource(R.drawable.red_icn);
            }

            ((ViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    docSelectionListener.ImageSelectionListener(imageModel,position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return imageModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image,star_icon;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
            star_icon = itemView.findViewById(R.id.star_icon);
        }
    }
    public void ImageSelection(ImageSelectionListener actDocList) {
        try {
            docSelectionListener = actDocList;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface ImageSelectionListener {
        void ImageSelectionListener(ImageModel imageModelList,int position);
    }
}
