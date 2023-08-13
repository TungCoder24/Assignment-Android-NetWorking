package tungndph27567.fpoly.assignment_androidnetworiking.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.activities.ReadMangaActivity;
import tungndph27567.fpoly.assignment_androidnetworiking.models.Manga;

public class MangaNewAdapter extends RecyclerView.Adapter<MangaNewAdapter.MangaNewViewHolder> {
    private List<Manga> list;
    private Context mContext;

    public MangaNewAdapter(List<Manga> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MangaNewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga_new,parent,false);

        return new MangaNewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaNewViewHolder holder, int position) {
        Manga objManga = list.get(position);
        holder.tvNameManga.setText(objManga.getName());
        holder.imgManga.setImageBitmap(decodeImage(objManga.getImg_cover()));
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(mContext, ReadMangaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("obj_manga",objManga);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
    private Bitmap decodeImage(String img){
        byte[] bytes = Base64.decode(img,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return bitmap;
    }


    public class MangaNewViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgManga;
        private TextView tvNameManga;

        public MangaNewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgManga = (ImageView) itemView.findViewById(R.id.img_manga);
            tvNameManga = (TextView) itemView.findViewById(R.id.tv_nameManga);
        }
    }
}
