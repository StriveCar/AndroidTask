package com.example.androidtask;

import android.annotation.SuppressLint;
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
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.androidtask.adapters.ViewPagerAdapter;
import com.example.androidtask.fragments.FollowListFragment;
import com.example.androidtask.fragments.MyShareListFragment;
import com.example.androidtask.fragments.ShareListFragment;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.ArtWordService;
import com.example.androidtask.fragments.CollectListFragment;
import com.example.androidtask.response.WordResponse;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

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
    private RelativeLayout bottom_bar_1,bottom_bar_2,bottom_bar_4,bottom_bar_5;
    private CardView bottom_bar_3;
    private List<Fragment> fragmentlist = new ArrayList<>();
    private ViewPager2 viewpager;
    private String userId;
    private String username;

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
        initBottomNavigationfunction();
        initViewPager();
    }

    private void initViewPager() {
        //初始化Fragment，使用Viewpager管理Fragment
        fragmentlist.add(new ShareListFragment(userId,username));
        fragmentlist.add(new FollowListFragment());
        fragmentlist.add(new CollectListFragment(userId,username));
        fragmentlist.add(new MyShareListFragment(userId));
        viewpager = findViewById(R.id.viewpager);
        ViewPagerAdapter viewpagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragmentlist);
        viewpager.setAdapter(viewpagerAdapter);
        //默认主页,打开软件就显示主页的分享列表
        viewpager.setCurrentItem(0);
        viewpager.isSelected();
    }

    private void initBottomNavigationfunction() {
        //创建底部导航栏
        bottom_bar_1 = findViewById(R.id.bottom_bar_1_btn);
        bottom_bar_5 = findViewById(R.id.bottom_bar_5_btn);
        // bottom_bar_3 = findViewById(R.id.bottom_bar_3_btn);
        bottom_bar_2 = findViewById(R.id.bottom_bar_2_btn);
        bottom_bar_4 = findViewById(R.id.bottom_bar_4_btn);
        //bottom_bar_3.setOnClickListener(this);
        bottom_bar_4.setOnClickListener(this);
        bottom_bar_1.setOnClickListener(this);
        bottom_bar_2.setOnClickListener(this);
        bottom_bar_5.setOnClickListener(this);
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
            userId = bundle.get(LoginActivity.USER_ID).toString();
            username = bundle.get(LoginActivity.USER_NAME).toString();
            tv_username.setText(bundle.getString(LoginActivity.USER_NAME));
            tv_introduce.setText(bundle.getString(LoginActivity.USER_INTRODUCE));
            userId = bundle.getString(LoginActivity.USER_ID);
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
        //fragment 切换
        int id = v.getId();
        if(id == R.id.bottom_bar_1_btn){
            viewpager.setCurrentItem(0);
        } else if (id == R.id.bottom_bar_2_btn) {
            viewpager.setCurrentItem(1);
        }
//        else if (id == R.id.bottom_bar_3_btn) {
//            //放发布页面的Fragment切换
//            Toast.makeText(this,"发布", Toast.LENGTH_SHORT).show();
//        }
        else if (id == R.id.bottom_bar_4_btn) {
            viewpager.setCurrentItem(2);
        } else if (id == R.id.bottom_bar_5_btn) {
            viewpager.setCurrentItem(3);
        }
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