package com.example.androidtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidtask.adapters.ShareDetailPageAdapter;
import com.example.androidtask.response.sharelist_item;

import java.util.Objects;

public class ShareDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private ShareDetailPageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);
        //加载顶部工具栏
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        //取出数据，准备加载到页面上
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        sharelist_item item = (sharelist_item) bd.getSerializable("item");
        ImageView iv = findViewById(R.id.iv_profile);
        TextView tv_username = findViewById(R.id.tv_username);
        TextView tv_content = findViewById(R.id.tv_content);
        String st = item.getProfileUrl();
        if(st == "" || st == null){
            iv.setImageResource(R.drawable.baseline_person_outline_24);
        } else {
            Glide.with(this).load(item.getProfileUrl()).into(iv);
        }
        tv_username.setText(item.getUsername());
        tv_content.setText(item.getContent());
        adapter = new ShareDetailPageAdapter(this, item.getImageUrlList());
        RecyclerView rv = findViewById(R.id.rv_imglist);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        rv.setAdapter(adapter);
        rv.setLayoutManager(glm);
    }
}