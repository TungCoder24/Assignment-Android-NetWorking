package tungndph27567.fpoly.assignment_androidnetworiking.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.models.Manga;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;

public class UpdateMangaActivity extends AppCompatActivity {
    private Toolbar idTollBar;
    private TextInputEditText tvNameManga;
    private TextInputEditText tvAuthor;
    private TextInputEditText tvDate;
    private Button btnAddCoverImg;
    private ImageView imgCoverImg;
    private TextInputEditText edImgContent;
    private TextInputEditText tvDescribe;
    private ProgressBar progressbar;
    private Button btnAddManga;
    private Manga objManga;
    String link = "";
    private ActivityResultLauncher<Intent> mLauncher;
    private String encodeImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_manga);
        finId();
        launcherGallary();
        Bundle bundle = getIntent().getExtras();
        objManga = (Manga) bundle.getSerializable("obj_manga");
        setSupportActionBar(idTollBar);
        getSupportActionBar().setTitle("Chỉnh sửa truyện: " + objManga.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvNameManga.setText(objManga.getName());
        tvAuthor.setText(objManga.getAuthor());
        tvDate.setText(objManga.getDate());
        imgCoverImg.setImageBitmap(enCodeImage(objManga.getImg_cover()));
        tvDescribe.setText(objManga.getDescribe());
        getData();
        btnAddManga.setOnClickListener(view -> {
            onClickUpdateManga();
        });
        btnAddCoverImg.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mLauncher.launch(i);
        });
    }
    private void getData(){
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/manga/" + objManga.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        JSONArray jsonArray = jsonObject.getJSONArray("contentImg");
                        for (int j = 0; i < jsonArray.length(); j++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            link += jsonObject1.getString("linkImg") + ",";
                            edImgContent.setText(link);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Error", error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void launcherGallary() {
        mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Uri uri = result.getData().getData();
                    imgCoverImg.setVisibility(View.VISIBLE);
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imgCoverImg.setImageBitmap(bitmap);
                        encodeImg = decodeImg(bitmap);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private String decodeImg(Bitmap bitmap) {
        int preWidth = 400;
        int preHeigh = preWidth + bitmap.getHeight() / preWidth;
        Bitmap preBitmap = bitmap.createScaledBitmap(bitmap, preWidth, preHeigh, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        preBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void onClickUpdateManga() {
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/manga/updateManga/" + objManga.getId();
        btnAddManga.setVisibility(View.INVISIBLE);
        progressbar.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();
        String linkImgContent = edImgContent.getText().toString().trim();
        String[] link = linkImgContent.split(",");
        JSONArray jsonArray = new JSONArray();
        for (String s : link) {
            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("linkImg", s);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            jsonArray.put(jsonObject1);
        }
        try {
            jsonObject.put("name", tvNameManga.getText().toString());
            jsonObject.put("describe", tvDescribe.getText().toString());
            jsonObject.put("author", tvAuthor.getText().toString());
            jsonObject.put("date", tvDate.getText().toString());
            jsonObject.put("coverImg", encodeImg);
            jsonObject.put("contentImg", jsonArray);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean check = response.getBoolean("checkUpdate");
                        if (check) {
                            Toast.makeText(UpdateMangaActivity.this, "cập nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent();
                            setResult(Activity.RESULT_OK,i);
                            finish();
                        } else {
                            btnAddManga.setVisibility(View.VISIBLE);
                            progressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(UpdateMangaActivity.this, "cập nhập thất bại", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    btnAddManga.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UpdateMangaActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("error", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Bitmap enCodeImage(String str) {
        byte[] bytes = Base64.decode(str, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    private void finId() {
        idTollBar = (Toolbar) findViewById(R.id.id_tollBar);
        tvNameManga = (TextInputEditText) findViewById(R.id.tv_nameManga);
        tvAuthor = (TextInputEditText) findViewById(R.id.tv_author);
        tvDate = (TextInputEditText) findViewById(R.id.tv_date);
        btnAddCoverImg = (Button) findViewById(R.id.btn_addCoverImg);
        imgCoverImg = (ImageView) findViewById(R.id.img_coverImg);
        edImgContent = (TextInputEditText) findViewById(R.id.ed_imgContent);
        tvDescribe = (TextInputEditText) findViewById(R.id.tv_describe);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        btnAddManga = (Button) findViewById(R.id.btn_addManga);
    }
}