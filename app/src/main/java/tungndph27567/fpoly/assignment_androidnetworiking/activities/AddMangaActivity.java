package tungndph27567.fpoly.assignment_androidnetworiking.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;

public class AddMangaActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private Toolbar idTollBar;
    private TextInputEditText tvNameManga;
    private TextInputEditText tvAuthor;
    private TextInputEditText tvDate, tvDescirbe, edLinkImgaeContent;
    private Button btnAddCoverImg;
    private ImageView imgCoverImg;
    private Button btn_addManga;
    private ActivityResultLauncher<Intent> launcher;
    private String encodeImage;
    private static String url = "http://" + Constants.KEY_IPCONFIG + ":3000/manga/createManga";
    private List<String> listImg_Content;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manga);
        findId();
        launcherGallary();
        listImg_Content = new ArrayList<>();
        setSupportActionBar(idTollBar);
        getSupportActionBar().setTitle("Thêm truyện");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnAddCoverImg.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(i);
        });

        btn_addManga.setOnClickListener(view -> {
            onClickAddManga();
        });
    }

    private void onClickAddManga() {
        progressBar.setVisibility(View.VISIBLE);
        btn_addManga.setVisibility(View.INVISIBLE);
        String linkImgContent = edLinkImgaeContent.getText().toString();
        String[] linkImageContent = linkImgContent.split(",");
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (String s:linkImageContent) {
            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("linkImg",s);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            jsonArray.put(jsonObject1);
        }
        try {
            jsonObject.put("name", tvNameManga.getText().toString().trim());
            jsonObject.put("describe", tvDescirbe.getText().toString().trim());
            jsonObject.put("author", tvAuthor.getText().toString().trim());
            jsonObject.put("date", tvDate.getText().toString().trim());
            jsonObject.put("coverImg", encodeImage);
            jsonObject.put("contentImg", jsonArray);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    btn_addManga.setVisibility(View.VISIBLE);
                    Toast.makeText(AddMangaActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    btn_addManga.setVisibility(View.VISIBLE);
                    Toast.makeText(AddMangaActivity.this, "Thêm mới thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("Error", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }




    private void launcherGallary() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Uri uri = result.getData().getData();
                    imgCoverImg.setVisibility(View.VISIBLE);
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imgCoverImg.setImageBitmap(bitmap);
                        encodeImage = decodeImage(bitmap);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private String decodeImage(Bitmap bitmap) {
        int preWidth = 400;
        int preHeigh = bitmap.getHeight() + bitmap.getHeight() / preWidth;
        Bitmap preBitmap = Bitmap.createScaledBitmap(bitmap, preWidth, preHeigh, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        preBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);

    }

    private void findId() {
        idTollBar = findViewById(R.id.id_tollBar);
        tvNameManga = (TextInputEditText) findViewById(R.id.tv_nameManga);
        tvAuthor = (TextInputEditText) findViewById(R.id.tv_author);
        tvDate = (TextInputEditText) findViewById(R.id.tv_date);
        btnAddCoverImg = (Button) findViewById(R.id.btn_addCoverImg);
        imgCoverImg = (ImageView) findViewById(R.id.img_coverImg);
        btn_addManga = findViewById(R.id.btn_addManga);
        progressBar = findViewById(R.id.progressbar);
        tvDescirbe = findViewById(R.id.tv_describe);
        edLinkImgaeContent = findViewById(R.id.ed_imgContent);
    }
}