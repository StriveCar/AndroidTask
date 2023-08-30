package com.example.androidtask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.example.androidtask.response.LoginData;
import com.example.androidtask.response.WordResponse;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer_layout;
    private NavigationView nav_view;

    private CircleImageView im_profile;
    private TextView tv_username, tv_introduce, tv_art;

    private final ArtWordService artWordService = RetrofitClient.getInstance().getService("https://apis.tianapi.com/", ArtWordService.class);
    private ImageView iv_image;

    private boolean night_sun = false;
    private Toolbar toolbar;

    private ImageView icon, icon_1, icon_2, icon_3, icon_4;
    private TextView iconTv, iconTv_1, iconTv_2, iconTv_3, iconTv_4;
    private RelativeLayout rl_1, rl_2, rl_3, rl_4;
    private Intent intent;

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
        icon = findViewById(R.id.bottom_bar_image_1);
        iconTv = findViewById(R.id.bottom_bar_text_1);
        icon_1 = findViewById(R.id.bottom_bar_image_1);
        iconTv_1 = findViewById(R.id.bottom_bar_text_1);
        icon_2 = findViewById(R.id.bottom_bar_image_2);
        iconTv_2 = findViewById(R.id.bottom_bar_text_2);
        icon_3 = findViewById(R.id.bottom_bar_image_4);
        iconTv_3 = findViewById(R.id.bottom_bar_text_4);
        icon_4 = findViewById(R.id.bottom_bar_image_5);
        iconTv_4 = findViewById(R.id.bottom_bar_text_5);
        rl_1 = findViewById(R.id.bottom_bar_1_btn);
        rl_2 = findViewById(R.id.bottom_bar_2_btn);
        rl_3 = findViewById(R.id.bottom_bar_4_btn);
        rl_4 = findViewById(R.id.bottom_bar_5_btn);

        toolbar.setNavigationIcon(R.drawable.navigation);
        toolbar.setNavigationOnClickListener(v -> drawer_layout.openDrawer(GravityCompat.START));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initData();

        nav_view.setNavigationItemSelectedListener(this);
        drawer_layout.setOnClickListener(this);
        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
        rl_4.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoginData mloginData = LoginData.getMloginData();
        tv_username.setText(mloginData.getUsername());
        tv_introduce.setText(mloginData.getIntroduce());
        if (mloginData.getAvatar() != null) {
            Glide.with(this).load(mloginData.getAvatar()).diskCacheStrategy(DiskCacheStrategy.NONE).into(im_profile);
        } else {
            Glide.with(this).load(R.drawable.bysl).diskCacheStrategy(DiskCacheStrategy.NONE).into(im_profile);
        }
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        iv_image.setMaxHeight(displayMetrics.heightPixels - 1300);

    }

    public void changeSelectStatus(ImageView iv, TextView tv) {
        if (iv.getId() == icon.getId()) return;

        tv.setTextColor(Color.parseColor("#49709B"));
        if (iv.getId() == R.id.bottom_bar_image_1) {
            iv.setImageResource(R.drawable.find2);
        } else if (iv.getId() == R.id.bottom_bar_image_2) {
            iv.setImageResource(R.drawable.concern2);
        } else if (iv.getId() == R.id.bottom_bar_image_4) {
            iv.setImageResource(R.drawable.collect2);
        } else if (iv.getId() == R.id.bottom_bar_image_5) {
            iv.setImageResource(R.drawable.my2);
        }

        iconTv.setTextColor(Color.parseColor("#666666"));
        if (icon.getId() == R.id.bottom_bar_image_1) {
            icon.setImageResource(R.drawable.find);
        } else if (icon.getId() == R.id.bottom_bar_image_2) {
            icon.setImageResource(R.drawable.concern);
        } else if (icon.getId() == R.id.bottom_bar_image_4) {
            icon.setImageResource(R.drawable.collect);
        } else if (icon.getId() == R.id.bottom_bar_image_5) {
            icon.setImageResource(R.drawable.my);
        }
        icon = iv;
        iconTv = tv;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bottom_bar_1_btn) {
            changeSelectStatus(icon_1, iconTv_1);
        } else if (v.getId() == R.id.bottom_bar_2_btn) {
            changeSelectStatus(icon_2, iconTv_2);
        } else if (v.getId() == R.id.bottom_bar_3_btn) {

        } else if (v.getId() == R.id.bottom_bar_4_btn) {
            changeSelectStatus(icon_3, iconTv_3);
        } else if (v.getId() == R.id.bottom_bar_5_btn) {
            changeSelectStatus(icon_4, iconTv_4);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.person_data) {
            intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
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