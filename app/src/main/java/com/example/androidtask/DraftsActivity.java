package com.example.androidtask;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.LoginData;
import com.example.androidtask.response.Records;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DraftsActivity extends AppCompatActivity {


    private final PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);

    private RecyclerView rcView;
    private DraftsAdapter adapter;

    private String userId;

    private LoginData mlogindata = LoginData.getMloginData();

    private List<Records> list;
    private RecyclerView rv;
    private Toolbar toolbar;
    private ConstraintLayout tvEmptyView;


    //    private Context context = DraftsActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_drafts);

        rv = findViewById(R.id.lv_drafts_list);
        tvEmptyView =findViewById(R.id.tv_empty);
        //设置头部
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle("保存记录");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.teal_200));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadingDialog.getInstance(DraftsActivity.this).show();
        initData();
    }

    private void initData() {

        userId = mlogindata.getId();
        photoService.getAdd(userId).enqueue(new Callback<BaseResponse<Data<Records>>>() {
            @Override
            public void onResponse(Call<BaseResponse<Data<Records>>> call, Response<BaseResponse<Data<Records>>> response) {
                if (response.body().getCode() == 200 && response.body().getData() != null) {
                    rv.setVisibility(View.VISIBLE);
                    tvEmptyView.setVisibility(View.GONE);
                    list = response.body().getData().getRecords();
                    adapter = new DraftsAdapter(DraftsActivity.this, list);
                    adapter.setOnDeleteClick(new DraftsAdapter.onDeleteClick() {
                        @Override
                        public void onDelete(int pos) {
                            list.remove(pos);
                            adapter.notifyDataSetChanged();
                            if (list.isEmpty()){
                                rv.setVisibility(View.GONE);
                                tvEmptyView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    LinearLayoutManager llm = new LinearLayoutManager(DraftsActivity.this);
                    rv.setLayoutManager(llm);
                    rv.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                } else {
                    rv.setVisibility(View.GONE);
                    tvEmptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Data<Records>>> call, Throwable t) {
                Toast.makeText(DraftsActivity.this, "新增失败", Toast.LENGTH_SHORT).show();
            }
        });
        LoadingDialog.getInstance().dismiss();
    }
}