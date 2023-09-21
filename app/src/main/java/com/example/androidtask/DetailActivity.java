package com.example.androidtask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.ImageText;
import com.example.androidtask.response.Records;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ed_title = findViewById(R.id.ed_title);
        ed_content = findViewById(R.id.ed_content);
        im_image = findViewById(R.id.im_picture);
        change_button = findViewById(R.id.change_button);

        change_button.setOnClickListener(this);

        //接收数据
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String image = getIntent().getStringExtra("image");
        String imageCode = getIntent().getStringExtra("imageCode");
        String id = getIntent().getStringExtra("id");
        String userId = getIntent().getStringExtra("userId");

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


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.change_button){
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

                            Intent intent = new Intent(DetailActivity.this,DraftsActivity.class);
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