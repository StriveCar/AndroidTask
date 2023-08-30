package com.example.androidtask;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.Records;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;

public class ShareListFragment extends Fragment {
    private View sharelistView;
    private static List<Records> data = new ArrayList<>();
    private RecyclerView rv_sharelist;
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private List<String> data1 = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(sharelistView == null){
            sharelistView = inflater.inflate(R.layout.fragment_share_list, container,false);
        }
        initData();
        rv_sharelist = sharelistView.findViewById(R.id.shareList);
        ShareListAdapter adapter = new ShareListAdapter(getActivity(),data1);
        rv_sharelist.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));
        rv_sharelist.setAdapter(adapter);
        return sharelistView;
    }
    private void initData() {

        for(int i=1;i<100;i++){
            data1.add(String.format("这是第 %d item",i));
            System.out.println(i);
        }
//        photoService.getShare(1,10,0).subscribe(new Consumer<BaseResponse<Data<Records>>>() {
//            @Override
//            public void accept(BaseResponse<Data<Records>> dataBaseResponse) throws Throwable {
//                data = dataBaseResponse.getData().getRecords();
//                System.out.println("data 已加载");
//            }
//        });
    }
}