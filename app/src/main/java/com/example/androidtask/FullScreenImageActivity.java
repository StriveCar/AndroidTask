package com.example.androidtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        String url = bundle.getString("url");
        ImageView iv = findViewById(R.id.iv_0);
        Glide.with(this).load(url).into(iv);

        // 获取背景视图并添加点击事件
        View transparentBackground = findViewById(R.id.BackgroundView);
        transparentBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在点击背景时执行返回操作
                finish(); // 关闭当前 Activity，返回上一个 Activity
            }
        });

        // 获取返回按钮并添加点击事件
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在按钮点击时执行返回操作
                finish(); // 关闭当前 Activity，返回上一个 Activity
            }
        });
    }
}