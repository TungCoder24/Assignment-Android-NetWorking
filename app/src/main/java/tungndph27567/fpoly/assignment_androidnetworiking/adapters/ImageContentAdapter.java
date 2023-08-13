package tungndph27567.fpoly.assignment_androidnetworiking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.models.ImageContent;

public class ImageContentAdapter extends RecyclerView.Adapter<ImageContentAdapter.ImageContentViewHolder> {
    private List<ImageContent> list;
    private Context mContext;

    public ImageContentAdapter(List<ImageContent> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ImageContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_content, parent, false);
        return new ImageContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageContentViewHolder holder, int position) {

        ImageContent imageContent = list.get(position);
        Glide.with(mContext).load(imageContent.getUrl()).into(holder.imgContent);


    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ImageContentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgContent;


        public ImageContentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgContent = (ImageView) itemView.findViewById(R.id.img_content);

        }
    }
}
