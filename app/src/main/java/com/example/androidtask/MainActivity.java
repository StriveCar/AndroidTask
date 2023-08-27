package com.example.androidtask;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private ImageButton navigation;
    private DrawerLayout drawer_layout;
    private NavigationView nav_view;

    private FrameLayout maskLayout;
    private Bundle bundle;
    private CircleImageView im_profile;
    private TextView tv_username,tv_introduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tool_bar);
//        navigation = findViewById(R.id.btn_nva);
        drawer_layout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        View headerView = nav_view.getHeaderView(0);
        im_profile = headerView.findViewById(R.id.icon_image);
        tv_username = headerView.findViewById(R.id.username);
        tv_introduce = headerView.findViewById(R.id.tv_introduce);
        //开启分享列表fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view_shareList, ShareListFragment.class, null)
                    .commit();
        }

        bundle = getIntent().getExtras();
        if (bundle!=null){
            tv_username.setText(bundle.getString(LoginActivity.USER_NAME));
            tv_introduce.setText(bundle.getString(LoginActivity.USER_INTRODUCE));
            if (bundle.getString(LoginActivity.USER_PROFILE) != null){
                Glide.with(this).load(bundle.getString(LoginActivity.USER_PROFILE)).diskCacheStrategy(DiskCacheStrategy.NONE).into(im_profile);
            }else {
                Glide.with(this).load(R.drawable.bysl).diskCacheStrategy(DiskCacheStrategy.NONE).into(im_profile);
            }
        }
//        maskLayout = findViewByIdf(R.id.mask_layout);
//        drawer_layout.setScrimColor(Color.TRANSPARENT);
        toolbar.setNavigationIcon(R.drawable.navigation);

        nav_view.setNavigationItemSelectedListener(this);
        drawer_layout.setOnClickListener(this);
//        navigation.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(v -> {
            drawer_layout.openDrawer(GravityCompat.START);
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.person_data) {
            Toast.makeText(this, "点击个人信息", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.like_work) {

        } else if (item.getItemId() == R.id.exit_app) {

        }
        return false;
    }
}