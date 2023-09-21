package com.example.androidtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidtask.adapters.ShareDetailPageAdapter;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Records;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private ShareDetailPageAdapter adapter;
    private ImageView iv,iv_thumbsUp,iv_collect,iv_comment;
    private TextView tv_username,tv_title,tv_content;
    private sharelist_item item;
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private String userId;

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

        Binding();

        BindingEvent();

    }

    private void BindingEvent() {
        iv_thumbsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean haslike = item.getRecord().getHasLike();
                if (!haslike) {
                    photoService.thumbsUp(item.getRecord().getId(), userId).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if(response.body().getCode() == 200){
                                //点赞成功
                                item.getRecord().setHasLike(true);
                                iv_thumbsUp.setImageResource(R.drawable.baseline_thumb_up_24);
                            } else {
                                //点赞失败，网络或者后台出现问题
                                String msg = String.format("错误代码：%d",response.body().getCode());
                                Toast.makeText(ShareDetail.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Toast.makeText(ShareDetail.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //取消点赞
                    photoService.getDetail(item.getRecord().getId(), userId).enqueue(new Callback<BaseResponse<Records>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<Records>> call, Response<BaseResponse<Records>> response) {
                            System.out.println(String.format("shareId:%s",item.getRecord().getId()));
                            System.out.println(String.format("userId:%s",userId));

                            String st = response.body().getData().getLikeId();
                            photoService.cancelLike(st).enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                    if (response.body().getCode() == 200) {
                                        item.getRecord().setHasLike(false);
                                        iv_thumbsUp.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                                    } else {
                                        String msg = String.format("错误代码：%d",response.body().getCode());
                                        Toast.makeText(ShareDetail.this,msg,Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {
                                    Toast.makeText(ShareDetail.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<Records>> call, Throwable t) {
                            Toast.makeText(ShareDetail.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void getData() {
        //取出数据，准备加载到页面上
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        userId = i.getStringExtra("userId");
        item = (sharelist_item) bd.getSerializable("item");
    }

    private void Binding() {
        iv = findViewById(R.id.iv_profile);
        tv_username = findViewById(R.id.tv_username);
        tv_content = findViewById(R.id.tv_content);
        tv_title = findViewById(R.id.tv_title);
        iv_thumbsUp = findViewById(R.id.iv_thumbsUp);
        iv_collect = findViewById(R.id.iv_collect);

        getData();
        //处理头像
        String st = item.getProfileUrl();
        if(st == "" || st == null){
            iv.setImageResource(R.drawable.baseline_person_outline_24);
        } else {
            Glide.with(this).load(item.getProfileUrl()).into(iv);
        }
        //绑定数据
        tv_username.setText(item.getRecord().getUsername());
        tv_content.setText(item.getRecord().getContent());
        tv_title.setText(item.getRecord().getTitle());

        //把图片列表加载到页面上
        adapter = new ShareDetailPageAdapter(this, item.getRecord().getImageUrlList());
        RecyclerView rv = findViewById(R.id.rv_imglist);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        rv.setAdapter(adapter);
        rv.setLayoutManager(glm);
    }
}