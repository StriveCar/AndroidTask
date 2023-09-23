package com.example.androidtask;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidtask.adapters.CommentListAdapter;
import com.example.androidtask.adapters.ShareDetailPageAdapter;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Comment;
import com.example.androidtask.response.CommentParams;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.Records;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private ShareDetailPageAdapter adapter;
    private ImageView iv,iv_thumbsUp,iv_collect,iv_attention;
    private TextView tv_username,tv_title,tv_content;
    private ListView lv_firstcommentlist;
    private EditText et_comment;
    private Button btn_addcomment;
    private sharelist_item item;
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private String userId;
    private String username;
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
        //点赞
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

        iv_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasFocus = item.getRecord().getHasFocus();
                if(!hasFocus){
                    //点赞
                    photoService.attention(item.getRecord().getPUserId(), userId).enqueue(new Callback<BaseResponse<Object>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                            if(response.body().getCode() == 200){
                                item.getRecord().setHasFocus(true);
                                iv_attention.setImageResource(R.drawable.attention2);
                            } else {
                                if (response.body().getMsg().equals("当前应用下无此用户id")){
                                    Toast.makeText(ShareDetail.this,"无法关注该用户",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                            Toast.makeText(ShareDetail.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //取消关注,先获取likeId
                    photoService.attentionCancel(item.getRecord().getPUserId(), userId).enqueue(new Callback<BaseResponse<Object>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                            if(response.body().getCode() == 200){
                                item.getRecord().setHasFocus(false);
                                iv_attention.setImageResource(R.drawable.attention);
                            } else {
                                String msg = String.format("错误代码：%d",response.body().getCode());
                                Toast.makeText(ShareDetail.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                            Toast.makeText(ShareDetail.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        //收藏
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
        btn_addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = et_comment.getText().toString();
                System.out.println(String.format("content:%s\nshareId:%s\nuserId:%s\nusername:%s\n",content,item.getRecord().getId(),userId,username));
                CommentParams param = new CommentParams();
                param.setContent(content);
                param.setShareId(item.getRecord().getId());
                param.setUserId(userId);
                param.setUserName(username);
                photoService.addFirstCommment(param).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body().getCode() == 200){
                            firstcommentdata.clear();
                            getData();
                        } else {
                            String msg = String.format("错误代码：%d",response.body().getCode());
                            Toast.makeText(ShareDetail.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void init() {
        //取出数据，准备加载到页面上
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        userId = i.getStringExtra("userId");
        username = i.getStringExtra("username");
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter1.notifyDataSetChanged();
                                    ResetListViewHeight();
                                }
                            });
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

    private void ResetListViewHeight() {
        //修复列表只显示一行的问题
        int height = 0;
        if (firstcommentdata.size() == 0){
            System.out.println("数据为空，高度设0");
            return;
        }
        for (int i=0; i<firstcommentdata.size();i++) {
            View listItemView = adapter1.getView(i,null,lv_firstcommentlist);
            listItemView.measure(0,0);
            height = height + listItemView.getMeasuredHeight();
            ViewGroup.LayoutParams param =lv_firstcommentlist.getLayoutParams();
            param.height = height;
            lv_firstcommentlist.setLayoutParams(param);
        }
        return;
    }

    private void Binding() {
        iv = findViewById(R.id.iv_profile);
        tv_username = findViewById(R.id.tv_username);
        tv_content = findViewById(R.id.tv_content);
        tv_title = findViewById(R.id.tv_title);
        iv_thumbsUp = findViewById(R.id.iv_thumbsUp);
        iv_collect = findViewById(R.id.iv_collect);
        iv_attention = findViewById(R.id.iv_attention);
        et_comment = findViewById(R.id.et_comment);
        btn_addcomment = findViewById(R.id.btn_addcomment);

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

        if (item.getRecord().getHasFocus()){
            iv_attention.setImageResource(R.drawable.attention2);
        }  else {
            iv_attention.setImageResource(R.drawable.attention);
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
        lv_firstcommentlist = findViewById(R.id.lv_firstCommentList);
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