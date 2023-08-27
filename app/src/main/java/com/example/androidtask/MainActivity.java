package com.example.androidtask;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.ArtWordService;
import com.example.androidtask.response.WordResponse;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer_layout;
    private NavigationView nav_view;

    private Bundle bundle;
    private CircleImageView im_profile;
    private TextView tv_username, tv_introduce, tv_art;

    private final ArtWordService artWordService = RetrofitClient.getInstance().getService("https://apis.tianapi.com/", ArtWordService.class);
    private ImageView iv_image;

    private boolean night_sun = false;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        drawer_layout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        View headerView = nav_view.getHeaderView(0);
        im_profile = headerView.findViewById(R.id.icon_image);
        tv_username = headerView.findViewById(R.id.username);
        tv_introduce = headerView.findViewById(R.id.tv_introduce);
        tv_art = headerView.findViewById(R.id.art_word);
        iv_image = findViewById(R.id.image);

        toolbar.setNavigationIcon(R.drawable.navigation);
        toolbar.setNavigationOnClickListener(v -> drawer_layout.openDrawer(GravityCompat.START));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initData();


        nav_view.setNavigationItemSelectedListener(this);
        drawer_layout.setOnClickListener(this);
    }

    public void initData() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "ZiTiGuanJiaNaNa-2.ttf");
        tv_art.setTypeface(typeface);

        artWordService.getArtWord().enqueue(new Callback<WordResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<WordResponse> call, @NonNull Response<WordResponse> response) {
                if (response.body() != null && response.body().getCode() == 200) {
                    tv_art.setText("ʕ ᵔᴥᵔ ʔ  " + response.body().getResult().getContent());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WordResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "获取文字失败", Toast.LENGTH_SHORT).show();
            }
        });

        bundle = getIntent().getExtras();
        if (bundle != null) {
            tv_username.setText(bundle.getString(LoginActivity.USER_NAME));
            tv_introduce.setText(bundle.getString(LoginActivity.USER_INTRODUCE));
            if (bundle.getString(LoginActivity.USER_PROFILE) != null) {
                Glide.with(this).load(bundle.getString(LoginActivity.USER_PROFILE)).diskCacheStrategy(DiskCacheStrategy.NONE).into(im_profile);
            } else {
                Glide.with(this).load(R.drawable.bysl).diskCacheStrategy(DiskCacheStrategy.NONE).into(im_profile);
            }
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        iv_image.setMaxHeight(displayMetrics.heightPixels - 1300);

    }

    public void changeSelectStatus(){

    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.person_data) {
            Toast.makeText(this, "点击个人信息", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.like_work) {

        } else if (item.getItemId() == R.id.night) {
            night_sun = !night_sun;
            if (night_sun) {
                item.setIcon(R.drawable.baseline_wb_sunny_24);
                setTheme(R.style.Base_Theme_NightAndroidTask);
                recreate();
            } else {

            }

        } else if (item.getItemId() == R.id.about_us) {

        } else if (item.getItemId() == R.id.exit_app) {
            finish();
        }
        return false;
    }
}