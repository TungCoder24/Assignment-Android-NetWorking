package tungndph27567.fpoly.assignment_androidnetworiking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tungndph27567.fpoly.assignment_androidnetworiking.MainActivity;
import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.PreferenceManager;

public class SignInActivity extends AppCompatActivity {
    private EditText edUsername;
    private EditText edPasswd;
    private ProgressBar progressbar;
    private Button btnLogin;
    private TextView tvCreateAcount;
    private PreferenceManager preferenceManager;
    public static String url = "http://"+ Constants.KEY_IPCONFIG+":3000/user/signIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        findId();
        preferenceManager = new PreferenceManager(this);
        tvCreateAcount.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });
        btnLogin.setOnClickListener(view -> {
            if (isValidate()) {
                onClickSignIn();
            }
        });
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void onClickSignIn() {
        btnLogin.setVisibility(View.INVISIBLE);
        progressbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        try {
            jsonObject.put("username", edUsername.getText().toString().trim());
            jsonObject.put("passwd", edPasswd.getText().toString().trim());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean loggedIn = response.getBoolean("loggedIn");
                    if (loggedIn) {
                        btnLogin.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.INVISIBLE);
                        showToast("Đăng nhập thành công");
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USERNAME, edUsername.getText().toString());
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finishAffinity();
                    } else {
                        btnLogin.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.INVISIBLE);
                        showToast("Thông tin đăng nhập không chính xác");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidate() {
        if (edUsername.getText().toString().isEmpty()) {
            showToast("Không được để trống username");
            return false;
        } else if (edPasswd.getText().toString().isEmpty()) {
            showToast("Không được để trống password");
            return false;
        } else {
            return true;
        }
    }

    private void findId() {
        edUsername = (EditText) findViewById(R.id.ed_email);
        edPasswd = (EditText) findViewById(R.id.ed_passwd);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvCreateAcount = (TextView) findViewById(R.id.tv_createAcount);

    }
}