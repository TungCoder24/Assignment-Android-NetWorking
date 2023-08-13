package tungndph27567.fpoly.assignment_androidnetworiking.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.adapters.CommentAdapter;
import tungndph27567.fpoly.assignment_androidnetworiking.adapters.ImageContentAdapter;
import tungndph27567.fpoly.assignment_androidnetworiking.models.Comment;
import tungndph27567.fpoly.assignment_androidnetworiking.models.ImageContent;
import tungndph27567.fpoly.assignment_androidnetworiking.models.Manga;
import tungndph27567.fpoly.assignment_androidnetworiking.models.User;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.PreferenceManager;

public class ReadMangaActivity extends AppCompatActivity {
    private Manga objManga;
    private Toolbar idTollBar;
    private RecyclerView recyImgContent;
    private ImageContentAdapter adapter;
    List<ImageContent> listLink;
    private EditText edCmt;
    private ImageView imgSend;
    private RecyclerView recyCmt;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    private PreferenceManager preferenceManager;
    private User objUser = new User();
    private List<Comment> listCmt;
    private CommentAdapter adapterCmt;
    private TextView tvSua;
    private TextView tvHuydon;
    private TextView tvCancel, tv_huy;
    private Comment mObjComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);
        preferenceManager = new PreferenceManager(this);
        getData();
        findId();
        listLink = new ArrayList<>();
        getDataManga();
        getComment();
        setSupportActionBar(idTollBar);
        getSupportActionBar().setTitle("Truyện " + objManga.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edCmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!edCmt.getText().toString().isEmpty()){
                    imgSend.setVisibility(View.VISIBLE);
                }else {
                    imgSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imgSend.setOnClickListener(view -> {
            if (mObjComment == null) {
                getDataUser();
            } else {
                updateCmt();
                mObjComment = null;
                edCmt.setText("");
            }
        });
        tv_huy.setOnClickListener(view -> {
            mObjComment =null;
            tv_huy.setVisibility(View.GONE);
            edCmt.setText("");
        });
    }

    private void getDataUser() {
        String urlComment = "http://" + Constants.KEY_IPCONFIG + ":3000/comment/createComment";
        String username = preferenceManager.getString(Constants.KEY_USERNAME);
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/user/" + username;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    objUser.setId(jsonObject.getString("_id"));
                    objUser.setAvt(jsonObject.getString("avatar"));
                    Date currentTime = Calendar.getInstance().getTime();
                    String time = format.format(currentTime.getTime());
                    JSONObject jsonObject1 = new JSONObject();

                    try {
                        jsonObject1.put("id_manga", objManga.getId());
                        jsonObject1.put("content", edCmt.getText().toString());
                        jsonObject1.put("date_time", time);
                        jsonObject1.put("username", username);
                        jsonObject1.put("avatarUser", objUser.getAvt());
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlComment, jsonObject1, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(ReadMangaActivity.this, "Bình luận thành công", Toast.LENGTH_SHORT).show();
                                edCmt.setText("");
                                getComment();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ReadMangaActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                Log.e("Error", error.toString());

                            }
                        });
                        requestQueue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
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
        requestQueue.add(jsonArrayRequest);

    }

    private void updateCmt() {

        String urlUpdateComment = "http://" + Constants.KEY_IPCONFIG + ":3000/comment/updateComment/" + mObjComment.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content", edCmt.getText().toString());
            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlUpdateComment, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean check = response.getBoolean("checkUpdate");
                        if (check) {
                            Toast.makeText(ReadMangaActivity.this, "chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                            getComment();
                        } else {
                            Toast.makeText(ReadMangaActivity.this, "chỉnh sửa thất bại", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ReadMangaActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", error.toString());
                }
            });
            requestQueue1.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getComment() {
        listCmt = new ArrayList<>();
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/comment/" + objManga.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Comment objComment = new Comment();
                        objComment.setId(jsonObject.getString("_id"));
                        objComment.setContent(jsonObject.getString("content"));
                        objComment.setId_mangar(jsonObject.getString("id_manga"));
                        objComment.setUsername(jsonObject.getString("username"));
                        objComment.setImage_user(jsonObject.getString("avatarUser"));
                        objComment.setDate_time(jsonObject.getString("date_time"));
                        listCmt.add(objComment);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                adapterCmt = new CommentAdapter(listCmt, getApplicationContext(), new CommentAdapter.IclickItem() {
                    @Override
                    public void showDiaLogOptionCmt(Comment objComment) {
                        showDiaLog(objComment);
                    }
                });
                recyCmt.setAdapter(adapterCmt);
                adapterCmt.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getDataManga() {
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
                            ImageContent imageContent = new ImageContent();
                            imageContent.setUrl(jsonObject1.getString("linkImg"));
                            listLink.add(imageContent);
                            adapter = new ImageContentAdapter(listLink, getApplicationContext());
                            recyImgContent.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
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

    public void showDiaLog(Comment objComment) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_option_cmt);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bg_dialogoption));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        windowAttributes.gravity = Gravity.BOTTOM;
        dialog.setCancelable(false);
        dialog.show();


        tvSua = (TextView) dialog.findViewById(R.id.tv_sua);
        tvHuydon = (TextView) dialog.findViewById(R.id.tv_huydon);
        tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tvHuydon.setOnClickListener(view -> {
            DeleteCmt(objComment);
            dialog.dismiss();
        });
        tvCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        tvSua.setOnClickListener(view -> {
            mObjComment = objComment;
            edCmt.setText(mObjComment.getContent());
            tv_huy.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });


    }

    private void DeleteCmt(Comment objComment) {
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/comment/deleteComment/" + objComment.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Base_V14_ThemeOverlay_MaterialComponents_Dialog_Alert);
        builder.setMessage("Bạn có muốn xóa bình luận này không?");
        builder.setTitle("Thông báo!");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean checkDelete = response.getBoolean("checkDelete");
                            if (checkDelete) {
                                Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                adapter.notifyDataSetChanged();
                                getComment();
                            } else {
                                Toast.makeText(getApplicationContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void findId() {
        idTollBar = (Toolbar) findViewById(R.id.id_tollBar);
        recyImgContent = (RecyclerView) findViewById(R.id.recy_img_content);
        edCmt = (EditText) findViewById(R.id.ed_cmt);
        imgSend = (ImageView) findViewById(R.id.img_send);
        recyCmt = (RecyclerView) findViewById(R.id.recy_cmt);
        tv_huy = findViewById(R.id.tv_huy);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        objManga = (Manga) bundle.getSerializable("obj_manga");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}