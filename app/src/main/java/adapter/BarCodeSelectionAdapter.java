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

public class BarCodeSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<String> barcodeModelList;
    Context mcontext;
    private BarCodeSelectionListener docSelectionListener;
    public BarCodeSelectionAdapter(Context context, List<String> listdata) {
        this.barcodeModelList = listdata;
        this.mcontext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.barcodelistitem, parent, false);

        return new ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder
            , @SuppressLint("RecyclerView") final int position) {

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            ((ViewHolder) holder).title.setText(barcodeModelList.get(position));


            ((ViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    docSelectionListener.BarCodeSelectionListener(position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return barcodeModelList.size();
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
            //star_icon = itemView.findViewById(R.id.star_icon);
        }
    }

    public void BarCodeSelection(BarCodeSelectionListener actDocList) {
        try {
            docSelectionListener = actDocList;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface BarCodeSelectionListener {
        void BarCodeSelectionListener(int position);
    }
}
