package tungndph27567.fpoly.assignment_androidnetworiking.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.activities.InforUserActivity;
import tungndph27567.fpoly.assignment_androidnetworiking.activities.SignInActivity;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.PreferenceManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcountFragment extends Fragment {
    private Button btnLogout;
    private PreferenceManager preferenceManager;
    private AppBarLayout appbar;
    private Toolbar idTollBar;
    private CircleImageView imgAvt;
    private TextView tvName;
    private CardView cvInfor;
    private CardView cvChangPasswd;


    public AcountFragment() {
        // Required empty public constructor
    }

    public static AcountFragment newInstance() {
        AcountFragment fragment = new AcountFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acount, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        btnLogout = view.findViewById(R.id.btn_logout);
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        idTollBar = view.findViewById(R.id.id_tollBar);
        imgAvt = (CircleImageView) view.findViewById(R.id.img_avt);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        cvInfor = (CardView) view.findViewById(R.id.cv_infor);
        cvChangPasswd = (CardView) view.findViewById(R.id.cv_changPasswd);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(idTollBar);
        ActionBar actionBar =  ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Account");
        cvInfor.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), InforUserActivity.class));
        });
        preferenceManager = new PreferenceManager(getContext());
        btnLogout.setOnClickListener(view1 -> {
            preferenceManager.clear();
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finishAffinity();
        });

    }
    private void getDataUser(){
        String username = preferenceManager.getString(Constants.KEY_USERNAME);
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/user/" + username;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    imgAvt.setImageBitmap(encodeImage(jsonObject.getString("avatar")));
                    tvName.setText(jsonObject.getString("name"));
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
    private Bitmap encodeImage(String img){
        byte[] bytes = Base64.decode(img,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return bitmap;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataUser();
    }
}