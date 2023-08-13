package tungndph27567.fpoly.assignment_androidnetworiking.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;



import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.models.Manga;

public class InforMangaActivity extends AppCompatActivity {
    private TextView tvNameManga;
    private ImageView imgCover;
    private TextView tvAuthor;
    private TextView tvDate;
    private TextView tvDescribe;
    private Manga objManga;
    private Toolbar id_tollBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_manga);


        tvNameManga = (TextView) findViewById(R.id.tv_nameManga);
        imgCover = (ImageView) findViewById(R.id.img_cover);
        tvAuthor = (TextView) findViewById(R.id.tv_author);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvDescribe = (TextView) findViewById(R.id.tv_describe);
        Bundle bundle = getIntent().getExtras();
        objManga = (Manga) bundle.getSerializable("obj_manga");
        imgCover.setImageBitmap(decodeImage(objManga.getImg_cover()));
        tvNameManga.setText("Tên truyện: "+ objManga.getName());
        tvAuthor.setText("Tên tác giả: "+ objManga.getAuthor());
        tvDate.setText("Năm xuất bản : "+ objManga.getDate());
        tvDescribe.setText(objManga.getDescribe());
        id_tollBar =findViewById(R.id.id_tollBar);
        setSupportActionBar(id_tollBar);
        getSupportActionBar().setTitle("Thông tin truyện "+ objManga.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Bitmap decodeImage(String img) {
        byte[] bytes = Base64.decode(img, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
}