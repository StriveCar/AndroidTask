package com.example.androidtask.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private List<sharelist_item> list = new ArrayList<>();
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private String userId;
    public CollectListFragment(String userId){
        this.userId = userId;
    }
    private int current = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(CollectList == null){
            CollectList = inflater.inflate(R.layout.fragment_collect_list,container,false);
        }
        BindingAdapter();

        getData();

        return CollectList;
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        getData();
    }
    private void BindingAdapter() {
        adapter = new CollectListAdapter(getActivity(), list, new OnItemClickListener(){

            @Override
            public void onItemClick(int position) {
                sharelist_item item = list.get(position);
                Intent intent = new Intent(getActivity(), ShareDetail.class);
                Bundle bd = new Bundle();
                bd.putSerializable("item", item);
                intent.putExtras(bd);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        },userId);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        rv_collectList = CollectList.findViewById(R.id.rv_collectlist);
        rv_collectList.setAdapter(adapter);
        rv_collectList.setLayoutManager(llm);
    }

    private void getData() {
        photoService.getCollect(current,20,userId).subscribe(new FlowableSubscriber<BaseResponse<Data<Records>>>() {
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
                                    list.add(item);
                                    if(list.size() == rlist.size()){
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