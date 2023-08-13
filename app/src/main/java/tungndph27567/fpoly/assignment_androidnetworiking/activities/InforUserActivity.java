package tungndph27567.fpoly.assignment_androidnetworiking.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.appbar.AppBarLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.PreferenceManager;

public class InforUserActivity extends AppCompatActivity {
    private AppBarLayout appbar;
    private Toolbar idTollBar;
    private RoundedImageView imgProfile;
    private EditText edEmail;
    private EditText edFullname;
    private ProgressBar progressbar;
    private Button btnSignUp;
    private PreferenceManager preferenceManager;
    private ActivityResultLauncher<Intent> mLauncher;
    private String encodeImg = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_user);
        findId();
        preferenceManager = new PreferenceManager(this);
        setSupportActionBar(idTollBar);
        getSupportActionBar().setTitle("Thông tin cá nhân");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataUser();
        GallaryLauncher();
        btnSignUp.setOnClickListener(view -> {
            ChangeInforUser();
        });
        imgProfile.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mLauncher.launch(i);
        });

    }

    private void ChangeInforUser() {
        String username = preferenceManager.getString(Constants.KEY_USERNAME);
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/user/updateUser/" + username;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", edEmail.getText().toString().trim());
            jsonObject.put("name", edFullname.getText().toString().trim());
            jsonObject.put("avatar", encodeImg);


            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean check = response.getBoolean("checkUpdate");
                        if (check) {
                            Toast.makeText(InforUserActivity.this, "Cập nhập thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(InforUserActivity.this, "Cập nhập thất bại", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Eror", error.toString());
                    Toast.makeText(InforUserActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void GallaryLauncher() {
        mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imgProfile.setImageBitmap(bitmap);
                        encodeImg = decodeImg(bitmap);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void getDataUser() {
        String username = preferenceManager.getString(Constants.KEY_USERNAME);
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/user/" + username;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    edEmail.setText(jsonObject.getString("email"));
                    edFullname.setText(jsonObject.getString("name"));
                    imgProfile.setImageBitmap(encodeImage(jsonObject.getString("avatar")));
                    encodeImg = jsonObject.getString("avatar");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private Bitmap encodeImage(String img) {
        byte[] bytes = Base64.decode(img, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    private String decodeImg(Bitmap bitmap) {
        int witdh = 400;
        int heigh = bitmap.getWidth() + bitmap.getHeight() / witdh;
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, witdh, heigh, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void findId() {
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        idTollBar = (Toolbar) findViewById(R.id.id_tollBar);
        imgProfile = (RoundedImageView) findViewById(R.id.img_profile);
        edEmail = (EditText) findViewById(R.id.ed_email);
        edFullname = (EditText) findViewById(R.id.ed_fullname);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
    }
}