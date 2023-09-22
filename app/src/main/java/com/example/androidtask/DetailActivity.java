package com.example.androidtask;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.ImageText;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private EditText ed_title;
    private EditText ed_content;
    private ImageView im_image;
    private Button change_button;

    private final PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private String title;
    private String content;
    private ArrayList<String> image;
    private String imageCode;
    private String id;
    private String userId;
    private LinearLayout imageContainer, imageContainer1;
    LinearLayout horizontalLayout = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ed_title = findViewById(R.id.ed_title);
        ed_content = findViewById(R.id.ed_content);
        im_image = findViewById(R.id.im_picture);
        change_button = findViewById(R.id.change_button);
        imageContainer = findViewById(R.id.image_container);
        imageContainer1 = findViewById(R.id.image_container1);

        change_button.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.teal_200));
        }

        //接收数据
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        image = getIntent().getStringArrayListExtra("stringList");
        imageCode = getIntent().getStringExtra("imageCode");
        id = getIntent().getStringExtra("id");
        userId = getIntent().getStringExtra("userId");


        uploadInfo();

        //实现头部
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle("详情");
    }


    private void uploadInfo() {
        ed_title.setText(title);
        ed_content.setText(content);
        Log.d("kkx",image.toString());
        for (int i = 0; i < image.size(); i++) {
            String element = image.get(i);
            ImageView imageView2 = new ImageView(this);
            imageView2.setLayoutParams(new LinearLayout.LayoutParams(
                    320,
                    320
            ));
            imageView2.setPadding(10, 4, 4, 10);
            imageView2.setBackgroundColor(getResources().getColor(R.color.white));
            Glide.with(this).load(element).into(imageView2);
            if (image.size() % 3 != 0 && (image.size() - i) <= image.size() % 3) {
                imageContainer.addView(imageView2,i%3);
            } else {
                if (i % 3 == 0) {
                    horizontalLayout = new LinearLayout(this);
                    horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLayout.addView(imageView2);
                } else if (i % 3 == 2) {
                    horizontalLayout.addView(imageView2);
                    imageContainer1.addView(horizontalLayout, 0);
                } else {
                    horizontalLayout.addView(imageView2);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.change_button) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("是否提交当前内容")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //点击确定得处理逻辑
                            ImageText imageText = new ImageText();
                            String title = getIntent().getStringExtra("title");
                            String content = getIntent().getStringExtra("content");
                            String imageCode = getIntent().getStringExtra("imageCode");
                            String id = getIntent().getStringExtra("id");
                            String userId = getIntent().getStringExtra("userId");

                            imageText.setImageCode(imageCode);
                            imageText.setPUserId(userId);
                            imageText.setContent(content);
                            imageText.setTitle(title);
                            imageText.setId(id);

                            photoService.changeStatus(imageText).enqueue(new Callback<BaseResponse<Object>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                                    if (response.body().getCode() == 200) {
                                        Toast.makeText(DetailActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                        System.out.println("提交成功!  " + response.body());
                                    } else if (response.body().getCode() == 500) {
                                        System.out.println(response.body().getMsg());
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                                    Toast.makeText(DetailActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Intent intent = new Intent(DetailActivity.this, DraftsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击取消按钮的处理逻辑

                            dialog.dismiss();
                        }
                    });
            // 创建并显示弹出框
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}