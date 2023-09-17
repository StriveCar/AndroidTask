package com.example.androidtask.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.androidtask.R;
import com.example.androidtask.adapters.ShareListAdapter;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.Records;
import com.example.androidtask.response.UserInfo;
import com.example.androidtask.response.sharelist_item;

import org.checkerframework.checker.units.qual.A;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.FlowableSubscriber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareListFragment extends Fragment {
    private View sharelistView;
    private List<Records> recordlist = new ArrayList<>();
    private ArrayList<sharelist_item> data = new ArrayList<>();
    private RecyclerView rv_sharelist;
    private ShareListAdapter adapter;
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(sharelistView == null){
            sharelistView = inflater.inflate(R.layout.fragment_share_list, container,false);
            initData();
        }
        return sharelistView;
    }

    private void initData() {
        photoService.getShare(0, 60, "1692126434274971648")
                .subscribe(new FlowableSubscriber<BaseResponse<Data<Records>>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE); // 请求数据
                    }

                    @Override
                    public void onNext(BaseResponse<Data<Records>> response) {
                        recordlist = response.getData().getRecords();
                        // 获取头像并添加到数据列表
                        for (int i = 0; i < response.getData().getSize(); i++) {
                            sharelist_item item = new sharelist_item();
                            item.record = recordlist.get(i);
                            photoService.getUserByName(item.record.getUsername()).enqueue(new Callback<BaseResponse<UserInfo>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> profileresponse) {
                                    item.profileUrl = profileresponse.body().getData().getAvatar();
                                    data.add(item);
                                    if (data.size() == response.getData().getSize()) {
                                        rv_sharelist = sharelistView.findViewById(R.id.shareList);
                                        System.out.println(data);
                                        adapter = new ShareListAdapter(getActivity(), data);
                                        rv_sharelist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                                        rv_sharelist.setAdapter(adapter);
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse<UserInfo>> call, Throwable t) {
                                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        // 处理错误情况，比如显示错误信息
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        // 数据流处理完成，可以执行一些收尾操作
                    }
                });
    }
}