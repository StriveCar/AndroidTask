package com.example.androidtask.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.androidtask.OnItemClickListener;
import com.example.androidtask.R;
import com.example.androidtask.ShareDetail;
import com.example.androidtask.adapters.ShareListAdapter;
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

import io.reactivex.rxjava3.core.FlowableSubscriber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareListFragment extends Fragment {
    private View sharelistView;
    private List<Records> recordlist = new ArrayList<>();
    private List<sharelist_item> data = new ArrayList<>();
    private RecyclerView rv_sharelist;
    private ShareListAdapter adapter;
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private int current = 0,size = 20;
    private String userId;
    private SwipeRefreshLayout srl;
    public ShareListFragment(String userId){
        this.userId = userId;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(sharelistView == null){
            sharelistView = inflater.inflate(R.layout.fragment_share_list, container,false);
        }

        BindingAdapter();

        getData();

        srl = sharelistView.findViewById(R.id.swipeRefreshLayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                refreshData();
            }
        });
        return sharelistView;
    }

    private void refreshData() {
        photoService.getShare(current,size,userId).subscribe(new FlowableSubscriber<BaseResponse<Data<Records>>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(BaseResponse<Data<Records>> dataBaseResponse) {
                recordlist = dataBaseResponse.getData().getRecords();
                Records temp;
                // 获取头像并添加到数据列表
                for (int i = 0; i < recordlist.size(); i++) {
                    sharelist_item item = new sharelist_item();
                    temp = recordlist.get(i);
                    item.setRecord(temp);
                    photoService.getUserByName(temp.getUsername()).enqueue(new Callback<BaseResponse<UserInfo>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> profileresponse) {
                            item.setProfileUrl(profileresponse.body().getData().getAvatar());
                            data.add(item);
                            if (data.size() == recordlist.size()) {
                                Toast.makeText(getActivity(), "数据加载完成",Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                                srl.setRefreshing(false);
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
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onComplete() {
            }
        });
    }

    private void BindingAdapter() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels / 2;
        rv_sharelist = sharelistView.findViewById(R.id.shareList);
        adapter = new ShareListAdapter(getActivity(), data, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                sharelist_item item = data.get(position);
                Intent intent = new Intent(getActivity(), ShareDetail.class);
                Bundle bd = new Bundle();
                bd.putSerializable("item", item);
                intent.putExtras(bd);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        }, width, userId);
        rv_sharelist.setAdapter(adapter);
        rv_sharelist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void getData() {
        photoService.getShare(current, size, userId)
                .subscribe(new FlowableSubscriber<BaseResponse<Data<Records>>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE); // 请求数据
                    }

                    @Override
                    public void onNext(BaseResponse<Data<Records>> response) {
                        recordlist = response.getData().getRecords();
                        Records temp;
                        // 获取头像并添加到数据列表
                        for (int i = 0; i < recordlist.size(); i++) {
                            sharelist_item item = new sharelist_item();
                            temp = recordlist.get(i);
                            item.setRecord(temp);
                            photoService.getUserByName(temp.getUsername()).enqueue(new Callback<BaseResponse<UserInfo>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> profileresponse) {
                                    item.setProfileUrl(profileresponse.body().getData().getAvatar());
                                    data.add(item);
                                    if (data.size() == recordlist.size()) {
                                        Toast.makeText(getActivity(), "数据加载完成",Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetChanged();
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