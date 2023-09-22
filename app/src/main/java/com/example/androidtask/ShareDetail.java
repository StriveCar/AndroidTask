package com.example.androidtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidtask.adapters.CommentListAdapter;
import com.example.androidtask.adapters.ShareDetailPageAdapter;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Comment;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.Records;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private ShareDetailPageAdapter adapter;
    private ImageView iv,iv_thumbsUp,iv_collect;
    private TextView tv_username,tv_title,tv_content;
    private EditText et_comment;
    private sharelist_item item;
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private String userId;
    private List<Comment> firstcommentdata = new ArrayList<>();
    private CommentListAdapter adapter1;
    private int current = 0,size = 20;

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

        getData();

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

        iv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasCollect = item.getRecord().getHasCollect();
                if(!hasCollect){
                    //收藏
                    photoService.collect(item.getRecord().getId(), userId).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body().getCode() == 200){
                                System.out.println("收藏成功");
                                iv_collect.setImageResource(R.drawable.baseline_star_24);
                                item.getRecord().setHasCollect(true);
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
                } else {
                    //取消收藏
                    photoService.getDetail(item.getRecord().getId(), userId).enqueue(new Callback<BaseResponse<Records>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<Records>> call, Response<BaseResponse<Records>> response) {
                            if (response.body().getCode() == 200){
                                String collectId = response.body().getData().getCollectId();
                                photoService.cancelCollect(collectId).enqueue(new Callback<BaseResponse>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                        if (response.body().getCode() == 200){
                                            System.out.println("取消收藏成功");
                                            iv_collect.setImageResource(R.drawable.baseline_star_border_24);
                                            item.getRecord().setHasCollect(false);
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
                            } else {
                                String msg = String.format("错误代码：%d",response.body().getCode());
                                Toast.makeText(ShareDetail.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<Records>> call, Throwable t) {
                            Toast.makeText(ShareDetail.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        //评论

    }

    private void init() {
        //取出数据，准备加载到页面上
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        userId = i.getStringExtra("userId");
        item = (sharelist_item) bd.getSerializable("item");
        getData();
    }

    private void getData() {
        photoService.getFirstComment(current, item.getRecord().getId(), size).enqueue(new Callback<BaseResponse<Data<Comment>>>() {
            @Override
            public void onResponse(Call<BaseResponse<Data<Comment>>> call, Response<BaseResponse<Data<Comment>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Data<Comment>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.getCode() == 200) {
                        Data<Comment> responseData = baseResponse.getData();
                        if (responseData != null) {
                            for(Comment c:responseData.getRecords()){
                                firstcommentdata.add(c);
                            }
                            System.out.println(String.format("comments:",firstcommentdata));
                            adapter1.notifyDataSetChanged();
                        } else {
                            // 数据为空的处理
                            Toast.makeText(ShareDetail.this, "暂时没有评论", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String msg = String.format("错误代码：%d", baseResponse != null ? baseResponse.getCode() : -1);
                        Toast.makeText(ShareDetail.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 处理网络请求失败的情况
                    String error = "网络请求失败: " + response.code();
                    Toast.makeText(ShareDetail.this, error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Data<Comment>>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });

    }

    private void Binding() {
        iv = findViewById(R.id.iv_profile);
        tv_username = findViewById(R.id.tv_username);
        tv_content = findViewById(R.id.tv_content);
        tv_title = findViewById(R.id.tv_title);
        iv_thumbsUp = findViewById(R.id.iv_thumbsUp);
        iv_collect = findViewById(R.id.iv_collect);
        et_comment = findViewById(R.id.et_comment);

        init();
        //处理头像图片
        String st = item.getProfileUrl();
        if (st == "" || st == null){
            iv.setImageResource(R.drawable.baseline_person_outline_24);
        } else {
            Glide.with(this).load(item.getProfileUrl()).into(iv);
        }
        //处理收藏图片
        if (item.getRecord().getHasCollect()){
            iv_collect.setImageResource(R.drawable.baseline_star_24);
        } else {
            iv_collect.setImageResource(R.drawable.baseline_star_border_24);
        }
        //处理点赞图片
        if (item.getRecord().getHasLike()){
            iv_thumbsUp.setImageResource(R.drawable.baseline_thumb_up_24);
        } else {
            iv_thumbsUp.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
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

        //绑定评论列表
        ListView lv_firstcommentlist = findViewById(R.id.lv_firstCommentList);
        adapter1 = new CommentListAdapter(ShareDetail.this, firstcommentdata, userId);
        lv_firstcommentlist.setAdapter(adapter1);

        //绑定评论输入框，并设置边框
        et_comment = findViewById(R.id.et_comment);
        GradientDrawable border = new GradientDrawable();
        border.setStroke(1, Color.GRAY); // 边框颜色和宽度
        border.setCornerRadius(8); // 圆角半径
        et_comment.setBackground(border);
    }
}