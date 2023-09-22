package com.example.androidtask.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androidtask.OnItemClickListener;
import com.example.androidtask.R;
import com.example.androidtask.ShareDetail;
import com.example.androidtask.adapters.CollectListAdapter;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.Records;
import com.example.androidtask.response.UserInfo;
import com.example.androidtask.sharelist_item;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectListFragment extends Fragment {
    private View CollectList;
    private RecyclerView rv_collectList;
    private CollectListAdapter adapter;
    private List<sharelist_item> data = new ArrayList<>();
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private String userId;
    private String username;
    public CollectListFragment(String userId, String username){
        this.userId = userId;
        this.username = username;
    }
    private SwipeRefreshLayout srl;
    private int current = 1,size = 20;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(CollectList == null){
            CollectList = inflater.inflate(R.layout.fragment_collect_list,container,false);
        }
        BindingAdapter();

        getData();

        srl = CollectList.findViewById(R.id.swipeRefreshLayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                refreshData();
            }
        });

        return CollectList;
    }

    private void refreshData() {
        photoService.getCollect(current,size,userId).subscribe(new FlowableSubscriber<BaseResponse<Data<Records>>>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {s.request(Long.MAX_VALUE);}

            @Override
            public void onNext(BaseResponse<Data<Records>> dataBaseResponse) {
                if(dataBaseResponse.getCode() == 200){
                    if(dataBaseResponse.getData() != null){
                        Records temp;

                        String url;
                        for(int i=0; i<dataBaseResponse.getData().getRecords().size();i++){
                            sharelist_item item = new sharelist_item();
                            temp = dataBaseResponse.getData().getRecords().get(i);
                            item.setRecord(temp);
                            photoService.getUserByName(temp.getUsername()).enqueue(new Callback<BaseResponse<UserInfo>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> response) {
                                    if(response.body().getCode() == 200){
                                        if(response.body().getData() != null){
                                            item.setProfileUrl(response.body().getData().getAvatar());
                                            data.add(item);
                                            if(data.size() == dataBaseResponse.getData().getRecords().size()){
                                                //Toast.makeText(getActivity(), "数据加载完成",Toast.LENGTH_SHORT).show();
                                                adapter.notifyDataSetChanged();
                                                srl.setRefreshing(false);
                                            }
                                        } else {
                                            System.out.println("数据为空");
                                        }
                                    } else {
                                        String msg = String.format("错误代码：%d",response.body().getCode());
                                        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse<UserInfo>> call, Throwable t) {

                                }
                            });
                        }
                    } else {
                        System.out.println("收藏列表为空");
                    }
                } else {
                    String msg = String.format("错误代码：%d",dataBaseResponse.getCode());
                    //Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();\
                    System.out.println(msg);
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        data.clear();
        getData();
    }
    private void BindingAdapter() {
        adapter = new CollectListAdapter(getActivity(), data, new OnItemClickListener(){

            @Override
            public void onItemClick(int position) {
                sharelist_item item = data.get(position);
                Intent intent = new Intent(getActivity(), ShareDetail.class);
                Bundle bd = new Bundle();
                bd.putSerializable("item", item);
                intent.putExtras(bd);
                intent.putExtra("userId", userId);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        },userId);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        rv_collectList = CollectList.findViewById(R.id.rv_collectlist);
        rv_collectList.setAdapter(adapter);
        rv_collectList.setLayoutManager(llm);
    }

    private void getData() {
        photoService.getCollect(current,size,userId).subscribe(new FlowableSubscriber<BaseResponse<Data<Records>>>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(BaseResponse<Data<Records>> dataBaseResponse) {
                if (dataBaseResponse.getData() == null) {
                    //收藏列表为空
                    Toast.makeText(getContext(), "收藏列表为空", Toast.LENGTH_LONG).show();
                } else {
                    List<Records> rlist = dataBaseResponse.getData().getRecords();
                    for(int i=0;i<rlist.size();i++){
                        sharelist_item item = new sharelist_item();
                        item.setRecord(rlist.get(i));
                        photoService.getUserByName(item.getRecord().getUsername()).enqueue(new Callback<BaseResponse<UserInfo>>() {
                            @Override
                            public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> response) {
                                if(response.body().getCode() == 200){
                                    item.setProfileUrl(response.body().getData().getAvatar());
                                    data.add(item);
                                    if(data.size() == rlist.size()){
                                        Toast.makeText(getActivity(), "数据加载完成",Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    String msg = String.format("错误代码：%d",response.body().getCode());
                                    Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse<UserInfo>> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}