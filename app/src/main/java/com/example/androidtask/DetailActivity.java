package com.example.androidtask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.androidtask.response.BaseResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity{

    private Toolbar toolbar;

    private EditText ed_title;
    private EditText ed_content;
    private ImageView im_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ed_title = findViewById(R.id.ed_title);
        ed_content = findViewById(R.id.ed_content);
        im_image = findViewById(R.id.im_picture);


        //接收数据
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String image = getIntent().getStringExtra("image");

        ed_title.setText(title);
        ed_content.setText(content);
        Glide.with(this).load(image).into(im_image);

        //实现头部
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle("详情");
    }


}