package com.sdu.tample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sdu.tample.adapter.AdapterProvince;
import com.sdu.tample.model.ModelProvince;
import com.sdu.tample.model.ModelProvinceNew;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import mehdi.sakout.fancybuttons.FancyButton;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProvinceActivity extends AppCompatActivity {
    SwipeRefreshLayout mSwipeRefreshLayout;
    Context mContext;
    ArrayList<ModelProvinceNew> posts;
    FancyButton btn_start_quiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_province);
        btn_start_quiz = (FancyButton) findViewById(R.id.btn_start_quiz);
        btn_start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProvinceActivity.this,ProvinceSearchActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        mContext = this;

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ConnectAPI().getProvinceAll(ProvinceActivity.this);
            }
        });

        new ConnectAPI().getProvinceAll(mContext);
    }

    public void setAdap(String data, String url) {

        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<ModelProvinceNew>>() {}.getType();
        Collection<ModelProvinceNew> enums = gson.fromJson(data, collectionType);
        posts = new ArrayList<ModelProvinceNew>(enums);
        final String[] list = new String[posts.size()];

        for (int i = 0; i < posts.size(); i++) {
            list[i] = posts.get(i).getProvince().getProvinceName();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dummyfrag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        AdapterProvince adapter = new AdapterProvince(this, posts, url);
        recyclerView.setAdapter(adapter);

        try {
            mSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {

        }

        adapter.SetOnItemClickListener(new AdapterProvince.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int ID = posts.get(position).getProvince().getId();
                String province = posts.get(position).getProvince().getProvinceName();
                startActivity(new Intent(mContext, MainActivity.class).putExtra("id", ID).putExtra("province", province));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}