package tungndph27567.fpoly.assignment_androidnetworiking.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import tungndph27567.fpoly.assignment_androidnetworiking.MainActivity;
import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.PreferenceManager;

public class SignUpActivity extends AppCompatActivity {
    private EditText edUsername;
    private EditText edEmail;
    private EditText edFullname;
    private EditText edPasswd;
    private EditText edConfirmPasswd;
    private ProgressBar progressbar;
    private Button btnSignUp;
    private TextView tvLogin;
    private FrameLayout layout_avt;
    private ImageView img_avt;
    public static String url = "http://" + Constants.KEY_IPCONFIG + ":3000/user/singUp";
    public static String urlcheckSigUp = "http://" + Constants.KEY_IPCONFIG + ":3000/user/checkSignUp";
    private PreferenceManager preferenceManager;
    private ActivityResultLauncher<Intent> launcher;
    private String encodeImage;
    private TextView tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findId();
        launcherGallary();
        preferenceManager = new PreferenceManager(this);
        btnSignUp.setOnClickListener(view -> {
            if (isValidate()) {
                checkSignUp();
            }
        });
        layout_avt.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            launcher.launch(i);
        });
    }

    private void launcherGallary() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri); // doc uri
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        img_avt.setImageBitmap(bitmap);
                        tv_title.setVisibility(View.GONE);
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
        int preHeigh = bitmap.getHeight() + preWidth / bitmap.getWidth();
        Bitmap preBitmap = Bitmap.createScaledBitmap(bitmap, preWidth, preHeigh, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        preBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void checkSignUp() {
        btnSignUp.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", edUsername.getText().toString().trim());
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlcheckSigUp, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean checkUsername = response.getBoolean("checkUsername");
                        if (checkUsername) {
                            onClickSignUp();
                        } else {
                            btnSignUp.setVisibility(View.VISIBLE);
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this, "Username đã tồn tại", Toast.LENGTH_SHORT).show();
                            edUsername.requestFocus();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void onClickSignUp() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", edUsername.getText().toString().trim());
            jsonBody.put("passwd", edPasswd.getText().toString().trim());
            jsonBody.put("email", edEmail.getText().toString().trim());
            jsonBody.put("name", edFullname.getText().toString().trim());
            jsonBody.put("avatar", encodeImage);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    btnSignUp.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USERNAME, edUsername.getText().toString());
                    startActivity(i);
                    finishAffinity();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", error.toString());
                    Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    private void ToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidate() {
        if (edUsername.getText().toString().isEmpty()) {
            ToastMessage("Không được để trống username");
            return false;
        } else if (edFullname.getText().toString().isEmpty()) {
            ToastMessage("Không được để trống fullname");
            return false;
        } else if (edEmail.getText().toString().isEmpty()) {
            ToastMessage("Không được để trống email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString()).matches()) {
            ToastMessage("Email không đúng định dạng");
            return false;
        } else if (!edConfirmPasswd.getText().toString().trim().equals(edPasswd.getText().toString().trim())) {
            ToastMessage("Xác nhận mật khẩu không chính xác");
            return false;
        } else {
            return true;
        }
    }

    private void findId() {
        edUsername = (EditText) findViewById(R.id.ed_username);
        edEmail = (EditText) findViewById(R.id.ed_email);
        edFullname = (EditText) findViewById(R.id.ed_fullname);
        edPasswd = (EditText) findViewById(R.id.ed_passwd);
        edConfirmPasswd = (EditText) findViewById(R.id.ed_confirmPasswd);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
        tvLogin = (TextView) findViewById(R.id.tv_Login);
        layout_avt = findViewById(R.id.layoutImage);
        img_avt = findViewById(R.id.img_profile);
        tv_title = findViewById(R.id.tv_addImg);
    }
}