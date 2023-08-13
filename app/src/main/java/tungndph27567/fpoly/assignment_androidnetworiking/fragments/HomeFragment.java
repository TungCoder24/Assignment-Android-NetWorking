package tungndph27567.fpoly.assignment_androidnetworiking.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tungndph27567.fpoly.assignment_androidnetworiking.R;
import tungndph27567.fpoly.assignment_androidnetworiking.activities.AddMangaActivity;
import tungndph27567.fpoly.assignment_androidnetworiking.activities.InforMangaActivity;
import tungndph27567.fpoly.assignment_androidnetworiking.activities.UpdateMangaActivity;
import tungndph27567.fpoly.assignment_androidnetworiking.adapters.MangaAdapter;
import tungndph27567.fpoly.assignment_androidnetworiking.adapters.MangaNewAdapter;
import tungndph27567.fpoly.assignment_androidnetworiking.models.Manga;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.Constants;
import tungndph27567.fpoly.assignment_androidnetworiking.utilitie.PreferenceManager;


public class HomeFragment extends Fragment {

    private Toolbar toolbar;
    private SearchView searchView;
    private FloatingActionButton btn_addManga;
    private RecyclerView recy_manga;
    private static String url = "http://" + Constants.KEY_IPCONFIG + ":3000/manga";
    private MangaAdapter adapter;
    private List<Manga> list;
    private List<String> listImageContent;
    private PreferenceManager preferenceManager;
    private ViewPager2 pager_newManga;
    private List<Manga> listNewManga;
    private MangaNewAdapter adapterNewManga;
    private Timer mTimer;
    private boolean isLoading;
    private boolean isLastPage;
    private int currentPage = 1;


    private boolean check = true;
    // TODO: Rename and change types of parameters

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.id_tollBar);
        btn_addManga = view.findViewById(R.id.btn_addManga);
        recy_manga = view.findViewById(R.id.recy_manga);
        preferenceManager = new PreferenceManager(getContext());
        pager_newManga = view.findViewById(R.id.pager_newManga);
        listNewManga = new ArrayList<>();
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Manga App");
        btn_addManga.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), AddMangaActivity.class));
        });
        String username = preferenceManager.getString(Constants.KEY_USERNAME);
        if (!username.equals("admin123")) {
            btn_addManga.setVisibility(View.GONE);
        }
        pager_newManga.setClipToPadding(false);
        pager_newManga.setClipChildren(false);
        pager_newManga.setOffscreenPageLimit(3);
        pager_newManga.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        pager_newManga.setPageTransformer(compositePageTransformer);

    }

    public void getDataManga() {
        listImageContent = new ArrayList<>();
        list = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Manga objManga = new Manga();
                        objManga.setId(jsonObject.getString("_id"));
                        objManga.setName(jsonObject.getString("name"));
                        objManga.setDescribe(jsonObject.getString("describe"));
                        objManga.setAuthor(jsonObject.getString("author"));
                        objManga.setDate(jsonObject.getString("date"));
                        objManga.setImg_cover(jsonObject.getString("coverImg"));
                        JSONArray jsonArray = jsonObject.getJSONArray("contentImg");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String link = jsonObject1.getString("linkImg");
                            listImageContent.add(link);
                        }
                        objManga.setListImg_content(listImageContent);
                        list.add(objManga);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                adapter = new MangaAdapter(list, getActivity(), new MangaAdapter.ClickBtnInfor() {
                    @Override
                    public void showInforManga(Manga manga) {
                        Intent i = new Intent(getContext(), InforMangaActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("obj_manga", manga);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }

                    @Override
                    public void showDialogRemove(Manga manga) {
                        diaLogRemoveManga(manga);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void updateManga(Manga manga) {
                        Intent i = new Intent(getContext(), UpdateMangaActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("obj_manga", manga);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                });

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                recy_manga.setLayoutManager(gridLayoutManager);
                recy_manga.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Error", error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getDatanewManga() {
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/manga/newManga";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Manga objManga = new Manga();

                        objManga.setId(jsonObject.getString("_id"));
                        objManga.setName(jsonObject.getString("name"));
                        objManga.setDescribe(jsonObject.getString("describe"));
                        objManga.setAuthor(jsonObject.getString("author"));
                        objManga.setDate(jsonObject.getString("date"));
                        objManga.setImg_cover(jsonObject.getString("coverImg"));
                        listNewManga.add(objManga);
                        autoSlide();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                adapterNewManga = new MangaNewAdapter(listNewManga, getActivity());
                pager_newManga.setAdapter(adapterNewManga);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void diaLogRemoveManga(Manga manga) {
        String url = "http://" + Constants.KEY_IPCONFIG + ":3000/manga/deleteManga/" + manga.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), com.google.android.material.R.style.Base_V14_ThemeOverlay_MaterialComponents_Dialog_Alert);
        builder.setMessage("Bạn có muốn xóa truyện: " + manga.getName() + " không?");
        builder.setTitle("Thông báo!");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean checkDelete = response.getBoolean("checkDelete");
                            if (checkDelete) {
                                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                adapter.notifyDataSetChanged();
                                getDataManga();
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


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Tìm kiếm");
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void autoSlide() {
        if (listNewManga == null || listNewManga.isEmpty() || pager_newManga == null) {
            return;
        }
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            int currentItem = pager_newManga.getCurrentItem();
                            int totalItem = listNewManga.size() - 1;
                            if (currentItem < totalItem) {
                                currentItem++;
                                pager_newManga.setCurrentItem(currentItem);
                            } else {
                                pager_newManga.setCurrentItem(0);
                            }
                        }
                    });
                }
            }, 2000, 3500);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataManga();
        getDatanewManga();

    }
}