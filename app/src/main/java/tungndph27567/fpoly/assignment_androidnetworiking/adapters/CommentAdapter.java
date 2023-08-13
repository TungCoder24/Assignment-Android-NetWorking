package tungndph27567.fpoly.assignment_androidnetworiking.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.models.Comment;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.PreferenceManager;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> list;
    private Context mContext;
    private IclickItem mIclickItem;

    public interface IclickItem{
       void showDiaLogOptionCmt(Comment objComment);
    }

    public CommentAdapter(List<Comment> list, Context mContext, IclickItem mIclickItem) {
        this.list = list;
        this.mContext = mContext;
        this.mIclickItem = mIclickItem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cmt,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment objComment = list.get(position);
        holder.imgAvt.setImageBitmap(decodeImage(objComment.getImage_user()));
        holder.tvContent.setText(objComment.getContent());
        holder.tvDate.setText(objComment.getDate_time());
        holder.tvName.setText(objComment.getUsername());
        PreferenceManager preferenceManager = new PreferenceManager(mContext);
       String username = preferenceManager.getString(Constants.KEY_USERNAME);
        if(objComment.getUsername().equals(username)){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mIclickItem.showDiaLogOptionCmt(objComment);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }
    private Bitmap decodeImage(String img){
        byte[] bytes = Base64.decode(img,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return bitmap;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgAvt;
        private TextView tvName;
        private TextView tvContent;
        private TextView tvDate;



        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt = (CircleImageView) itemView.findViewById(R.id.img_avt);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
