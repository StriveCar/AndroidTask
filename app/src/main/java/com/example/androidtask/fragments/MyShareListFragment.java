package com.example.androidtask.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.androidtask.R;
import com.example.androidtask.adapters.MyshareListAdapter;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.Records;
import com.example.androidtask.sharelist_item;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;

public class MyShareListFragment extends Fragment {
    private View MyShareList;
    private MyshareListAdapter adapter;
    private RecyclerView rv_myShareList;
    private List<sharelist_item> data = new ArrayList<>();
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private String userId;
    private int current = 0,size = 20;
    public MyShareListFragment(String userId){
        this.userId = userId;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(MyShareList == null){
            MyShareList = inflater.inflate(R.layout.fragment_my_share_list,container,false);
        }

        rv_myShareList = MyShareList.findViewById(R.id.rv_myShareList);
        adapter = new MyshareListAdapter(data,getActivity(),userId);
        rv_myShareList.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        rv_myShareList.setLayoutManager(llm);

        getData();
        return MyShareList;
    }

    private void getData() {
        photoService.getMyShare(current,size,userId).subscribe(new FlowableSubscriber<BaseResponse<Data<Records>>>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {s.request(Long.MAX_VALUE);}

            @Override
            public void onNext(BaseResponse<Data<Records>> dataBaseResponse) {
                if (dataBaseResponse.getCode() == 200){
                    if (dataBaseResponse.getData() != null){
                        for(Records i:dataBaseResponse.getData().getRecords()){
                            System.out.println(i);
                            sharelist_item item = new sharelist_item();
                            item.setRecord(i);
                            data.add(item);
                        }
                        if (data.size() == dataBaseResponse.getData().getRecords().size()){
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        System.out.println("没有我自己的作品");
                    }
                } else {
                    String msg = String.format("错误代码：%d",dataBaseResponse.getCode());
                    Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}