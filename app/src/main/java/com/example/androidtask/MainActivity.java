package com.example.androidtask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.androidtask.adapters.ViewPagerAdapter;
import com.example.androidtask.fragments.CollectListFragment;
import com.example.androidtask.fragments.FollowListFragment;
import com.example.androidtask.fragments.MyShareListFragment;
import com.example.androidtask.fragments.ShareListFragment;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.ArtWordService;
import com.example.androidtask.response.LoginData;
import com.example.androidtask.response.WordResponse;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ImageView imProfile;
    private TextView tvUsername, tvIntroduce, tvArt;
    private final ArtWordService artWordService = RetrofitClient.getInstance().getService("https://apis.tianapi.com/", ArtWordService.class);
    private Toolbar toolbar;
    private ImageView icon, icon_1, icon_2, icon_3, icon_4, ivImage;
    private TextView iconTv, iconTv_1, iconTv_2, iconTv_3, iconTv_4;
    private RelativeLayout rl_1, rl_2, rl_3, rl_4, relativeLayout;
    private Intent intent;
    private int index = 7;
    private int[] imageResources;
    private int height = 0;
    private CountDownTimer clickTimer;
    private MenuItem night;

    private CardView bottom_bar_3;
    private List<Fragment> fragmentlist = new ArrayList<>();
    private ViewPager2 viewpager;
    private LoginData mloginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        Menu menu = navView.getMenu(); // 获取菜单
        night = menu.findItem(R.id.night);
        imProfile = headerView.findViewById(R.id.icon_image);
        tvUsername = headerView.findViewById(R.id.username);
        tvIntroduce = headerView.findViewById(R.id.tv_introduce);
        tvArt = headerView.findViewById(R.id.art_word);
        relativeLayout = headerView.findViewById(R.id.relative_layout);
        ivImage = findViewById(R.id.image);
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
        bottom_bar_3 = findViewById(R.id.bottom_bar_3_card);
        rl_3 = findViewById(R.id.bottom_bar_4_btn);
        rl_4 = findViewById(R.id.bottom_bar_5_btn);

        toolbar.setNavigationIcon(R.drawable.nav);
        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.lucency));
            }
        });

        initData();

        navView.setNavigationItemSelectedListener(this);
        drawerLayout.setOnClickListener(this);
        bottom_bar_3.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
        rl_4.setOnClickListener(this);
        ivImage.setOnClickListener(this);
        tvArt.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mloginData = LoginData.getMloginData();
        tvUsername.setText(mloginData.getUsername());
        tvIntroduce.setText(mloginData.getIntroduce());
        float radiusDp = 25f;
        float radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radiusDp, getResources().getDisplayMetrics());
        RoundedCorners roundedCorners = new RoundedCorners(Math.round(radiusPx));
        RequestOptions options = new RequestOptions().transform(roundedCorners);
        if (mloginData.getAvatar() != null) {
            Glide.with(this).load(mloginData.getAvatar()).apply(options).diskCacheStrategy(DiskCacheStrategy.NONE).into(imProfile);
        } else {
            Glide.with(this).load(R.drawable.bysl).apply(options).diskCacheStrategy(DiskCacheStrategy.NONE).into(imProfile);
        }
        navView.setNavigationItemSelectedListener(this);
        drawerLayout.setOnClickListener(this);
        initViewPager();
    }


    private void initViewPager() {
        //初始化Fragment，使用Viewpager管理Fragment
        fragmentlist.add(new ShareListFragment(mloginData.getId(), mloginData.getUsername()));
        fragmentlist.add(new FollowListFragment(mloginData.getId(), mloginData.getUsername()));
        fragmentlist.add(new CollectListFragment(mloginData.getId(), mloginData.getUsername()));
        fragmentlist.add(new MyShareListFragment(mloginData.getId()));
        viewpager = findViewById(R.id.viewpager);
        ViewPagerAdapter viewpagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragmentlist);
        viewpager.setAdapter(viewpagerAdapter);
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:changeSelectStatus(icon_1, iconTv_1);break;
                    case 1:changeSelectStatus(icon_2, iconTv_2);break;
                    case 2:changeSelectStatus(icon_3, iconTv_3);break;
                    case 3:changeSelectStatus(icon_4, iconTv_4);break;
                }
            }
        });
        //默认主页,打开软件就显示主页的分享列表
        viewpager.setCurrentItem(0);
        viewpager.isSelected();
    }


    public void initData() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "ZiTiGuanJiaNaNa-2.ttf");
        tvArt.setTypeface(typeface);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.teal_200));
        }

        gainArtWord();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130f, getResources().getDisplayMetrics());
        height = displayMetrics.heightPixels;
        ivImage.setMaxHeight((int) (displayMetrics.heightPixels - radiusPx * 4));
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navView.getLayoutParams();
        params.width = (int) (displayMetrics.widthPixels - radiusPx);
        navView.setLayoutParams(params);


        imageResources = new int[]{R.drawable.image01, R.drawable.image02, R.drawable.image03, R.drawable.image04,
                R.drawable.image05, R.drawable.image06, R.drawable.image07, R.drawable.image08,
                R.drawable.image09, R.drawable.image10, R.drawable.image11, R.drawable.image12,
                R.drawable.image13, R.drawable.image14, R.drawable.image15, R.drawable.image16,
                R.drawable.image17, R.drawable.image18, R.drawable.image19, R.drawable.image20,
                R.drawable.image21, R.drawable.image22, R.drawable.image23, R.drawable.image24,
                R.drawable.image25, R.drawable.image26};

        clickTimer = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                ivImage.setEnabled(true);
            }
        };
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
            Bundle bundle = new Bundle();
            bundle.putInt("mode", 1);
            viewpager.setCurrentItem(0);
            changeSelectStatus(icon_1, iconTv_1);
        } else if (v.getId() == R.id.bottom_bar_2_btn) {
            viewpager.setCurrentItem(1);
            changeSelectStatus(icon_2, iconTv_2);
        } else if (v.getId() == R.id.bottom_bar_3_card) {

            intent = new Intent(this, AddActivity.class);
            startActivity(intent);

        } else if (v.getId() == R.id.bottom_bar_4_btn) {
            viewpager.setCurrentItem(2);
            changeSelectStatus(icon_3, iconTv_3);
        } else if (v.getId() == R.id.bottom_bar_5_btn) {
            viewpager.setCurrentItem(3);
            changeSelectStatus(icon_4, iconTv_4);
        } else if (v.getId() == R.id.image) {
            clickTimer.start();
            ivImage.setEnabled(false);
            Random random = new Random();
            int randomIndex = random.nextInt(imageResources.length);
            if (randomIndex == index) {
                randomIndex = (randomIndex + 1) % imageResources.length;
            }
            index = randomIndex;
            Glide.with(this).load(imageResources[randomIndex]).override(Target.SIZE_ORIGINAL, height).into(ivImage);
        } else if (v.getId() == R.id.art_word) {
            gainArtWord();
        } else if (v.getId() == R.id.relative_layout) {
            intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.person_data) {
            intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.like_work) {
            intent = new Intent(MainActivity.this, LikeListActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.night) {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            invalidateOptionsMenu();
            night.setIcon(R.drawable.baseline_wb_sunny_24);
        } else if (item.getItemId() == R.id.about_us) {
            intent = new Intent(this, ModifyUsernameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(UserInfoActivity.MODE, 3);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (item.getItemId() == R.id.exit_app) {
            finish();
        } else if (item.getItemId() == R.id.swap) {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (item.getItemId() == R.id.drafts) {
            Intent intent = new Intent(this, DraftsActivity.class);
            startActivity(intent);
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickTimer.cancel();
    }

    public void gainArtWord() {
        artWordService.getArtWord().enqueue(new Callback<WordResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<WordResponse> call, @NonNull Response<WordResponse> response) {
                if (response.body() != null && response.body().getCode() == 200) {
                    tvArt.setText("ʕ ᵔᴥᵔ ʔ  " + response.body().getResult().getContent());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WordResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "获取文字失败", Toast.LENGTH_SHORT).show();
            }
        });

    }


}