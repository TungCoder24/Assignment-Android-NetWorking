package tungndph27567.fpoly.assignment_androidnetworiking.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.PreferenceManager;

public class ChangePassWdActivity extends AppCompatActivity {
    private AppBarLayout appbar;
    private Toolbar idTollBar;
    private EditText edPassWd;
    private EditText edNewPass;
    private EditText edConfirmPass;
    private ProgressBar progressbar;
    private Button btnSignUp;
    private String PassWd;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_wd);
        findId();
        getData();
        preferenceManager = new PreferenceManager(this);
        btnSignUp.setOnClickListener(view -> {

        });
    }

    private void getData() {
        String username = preferenceManager.getString(Constants.KEY_USERNAME);
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/user/updateUser/" + username;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    PassWd = jsonObject.getString("passwd");
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

    private void findId() {
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        idTollBar = (Toolbar) findViewById(R.id.id_tollBar);
        edPassWd = (EditText) findViewById(R.id.ed_PassWd);
        edNewPass = (EditText) findViewById(R.id.ed_NewPass);
        edConfirmPass = (EditText) findViewById(R.id.ed_confirmPass);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
    }
}