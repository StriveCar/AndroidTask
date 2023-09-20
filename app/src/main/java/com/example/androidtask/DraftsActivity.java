package com.example.androidtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.ImageText;
import com.example.androidtask.response.LoginData;
import com.example.androidtask.response.Records;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DraftsActivity extends AppCompatActivity {


    private final PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);


    private DraftsAdapter adapter;

    private String userId;

    private LoginData mlogindata = LoginData.getMloginData();

    private List<Records> list;
    private RecyclerView rv;
    private Toolbar toolbar;

//    private Context context = DraftsActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_drafts);
        initData();

        toolbar = findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle("保存记录");
    }

    private void initData() {

        userId = mlogindata.getId();
        photoService.getAdd(userId).enqueue(new Callback<BaseResponse<Data<Records>>>() {
            @Override
            public void onResponse(Call<BaseResponse<Data<Records>>> call, Response<BaseResponse<Data<Records>>> response) {
                if (response.body().getCode() == 200) {
                    list = response.body().getData().getRecords();
                    adapter = new DraftsAdapter(DraftsActivity.this,list);
                    rv = findViewById(R.id.lv_drafts_list);
                    LinearLayoutManager llm = new LinearLayoutManager(DraftsActivity.this);
                    rv.setLayoutManager(llm);
                    rv.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();


                } else if (response.body().getCode() == 500) {
                    System.out.println(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Data<Records>>> call, Throwable t) {
                Toast.makeText(DraftsActivity.this, "新增失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}