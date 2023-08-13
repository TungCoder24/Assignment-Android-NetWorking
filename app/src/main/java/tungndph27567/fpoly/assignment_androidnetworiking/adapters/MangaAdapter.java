package tungndph27567.fpoly.assignment_androidnetworiking.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;



import org.json.JSONObject;

import java.util.List;

import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.activities.ReadMangaActivity;
import tungndph27567.fpoly.assignment_androidnetworiking.models.Manga;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.PreferenceManager;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {
    private List<Manga> list;
    public ClickBtnInfor mClickBtnInfor;
    private Context mContext;
    private PreferenceManager preferenceManager;

    public interface ClickBtnInfor{
        void showInforManga(Manga manga);
        void showDialogRemove(Manga manga);
        void updateManga(Manga manga);
    }

    public MangaAdapter(List<Manga> list,  Context mContext,ClickBtnInfor mClickBtnInfor) {
        this.list = list;
        this.mContext = mContext;
        this.mClickBtnInfor = mClickBtnInfor;
        notifyDataSetChanged();
    }


    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga,parent,false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {

        Manga objManga = list.get(position);
        holder.imgManga.setImageBitmap(decodeImage(objManga.getImg_cover()));
        holder.tvNameManga.setText(objManga.getName());
        holder.btnReadNow.setOnClickListener(view -> {
            Intent i = new Intent(mContext, ReadMangaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("obj_manga",objManga);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        });
        holder.btnInfor.setOnClickListener(view -> {
            mClickBtnInfor.showInforManga(objManga);
        });
        holder.imgRemove.setOnClickListener(view -> {
            mClickBtnInfor.showDialogRemove(objManga);
        });
        holder.imgEdit.setOnClickListener(view -> {
            mClickBtnInfor.updateManga(objManga);
        });
        preferenceManager = new PreferenceManager(mContext);
        String username  = preferenceManager.getString(Constants.KEY_USERNAME);
        if(!username.equals("admin123")){
            holder.imgEdit.setVisibility(View.GONE);
            holder.imgRemove.setVisibility(View.GONE);
        }
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

    public class MangaViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgManga;
        private TextView tvNameManga;
        private TextView tvNumberRead;
        private Button btnReadNow;
        private Button btnInfor;
        private ImageView imgRemove;
        private ImageView imgEdit;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgManga = (ImageView) itemView.findViewById(R.id.img_manga);
            tvNameManga = (TextView) itemView.findViewById(R.id.tv_nameManga);
//            tvNumberRead = (TextView) itemView.findViewById(R.id.tv_numberRead);
            btnReadNow = (Button) itemView.findViewById(R.id.btn_readNow);
            btnInfor = (Button) itemView.findViewById(R.id.btn_infor);
            imgRemove = (ImageView) itemView.findViewById(R.id.img_remove);
            imgEdit = (ImageView) itemView.findViewById(R.id.img_edit);

        }
    }



}
