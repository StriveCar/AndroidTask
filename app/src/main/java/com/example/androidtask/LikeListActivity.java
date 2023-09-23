package com.example.androidtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.example.androidtask.adapters.ShareListAdapter;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.LoginData;
import com.example.androidtask.response.Records;
import com.example.androidtask.response.UserInfo;
import com.nostra13.universalimageloader.utils.L;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikeListActivity extends AppCompatActivity {

    private RecyclerView rv_likelist;
    private SwipeRefreshLayout srl;
    private ShareListAdapter adapter;
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private List<sharelist_item> data = new ArrayList<>();
    private LoginData mlogindata = LoginData.getMloginData();
    private Toolbar toolbar;
    private int current = 0,size = 20;
    private boolean refresh = false;
    private ConstraintLayout tvEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_list);

        tvEmptyView = findViewById(R.id.tv_empty);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle("点赞列表");

        Binding();

        getData();

        srl = findViewById(R.id.swipeRefreshLayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                refreshData();
            }
        });

    }

    private void refreshData() {
        refresh = true;
        getData();
    }


    private void getData() {
        photoService.getLike(current, size, mlogindata.getId())
                .subscribe(new FlowableSubscriber<BaseResponse<Data<Records>>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE); // 请求数据
                    }

                    @Override
                    public void onNext(BaseResponse<Data<Records>> dataresponse) {
                        if(dataresponse.getCode() == 200){
                            if(dataresponse.getData() != null){
                                for (Records i:dataresponse.getData().getRecords()) {
                                    sharelist_item item = new sharelist_item();
                                    item.setRecord(i);
                                    photoService.getUserByName(item.getRecord().getUsername()).enqueue(new Callback<BaseResponse<UserInfo>>() {
                                        @Override
                                        public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> response) {
                                            if(response.body().getCode() == 200){
                                                item.setProfileUrl(response.body().getData().getAvatar());
                                                data.add(item);
                                                if(data.size() == dataresponse.getData().getRecords().size()){
                                                    adapter.notifyDataSetChanged();
                                                    if(refresh){
                                                        srl.setRefreshing(false);
                                                    }
                                                    refresh = false;
                                                }
                                            } else {
                                                String msg = String.format("错误代码：%d",response.body().getCode());
                                                Toast.makeText(LikeListActivity.this,msg,Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BaseResponse<UserInfo>> call, Throwable t) {
                                            Toast.makeText(LikeListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        srl.setVisibility(View.GONE);
                                        tvEmptyView.setVisibility(View.VISIBLE);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        } else {
                            String msg = String.format("错误代码：%d",dataresponse.getCode());
                            Toast.makeText(LikeListActivity.this,msg,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        // 处理错误情况，比如显示错误信息
                        System.out.println(String.format("异常：%s",t.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        // 数据流处理完成，可以执行一些收尾操作
                    }
                });
    }

    private void Binding() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels / 2;
        rv_likelist = findViewById(R.id.rv_likeList);
        adapter = new ShareListAdapter(LikeListActivity.this, data, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                sharelist_item item = data.get(position);
                Intent intent = new Intent(LikeListActivity.this, ShareDetail.class);
                Bundle bd = new Bundle();
                bd.putSerializable("item", item);
                intent.putExtras(bd);
                intent.putExtra("userId", mlogindata.getId());
                intent.putExtra("username", mlogindata.getUsername());
                startActivity(intent);
            }
        }, width, mlogindata.getId());
        rv_likelist.setAdapter(adapter);
        rv_likelist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }
}