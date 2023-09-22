package com.example.androidtask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidtask.OnItemClickListener;
import com.example.androidtask.R;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.sharelist_item;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectListAdapter extends RecyclerView.Adapter<CollectListAdapter.ViewHolder> {
    private  List<sharelist_item> data;
    private  Context context;
    private ImageListAdapter adapter;
    private PhotoService ps = RetrofitClient.getInstance().getService(PhotoService.class);
    private String userId;
    private OnItemClickListener ls;
    public CollectListAdapter(Context context,
                              List<sharelist_item> list,
                              OnItemClickListener listener,
                              String userId){
        this.context = context;
        this.data = list;
        this.userId = userId;
        this.ls = listener;
    }
    @NonNull
    @Override
    public CollectListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.collectlist_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectListAdapter.ViewHolder holder, int position) {
        sharelist_item item = data.get(position);
        //头像
        if (item.getProfileUrl() == "" || item.getProfileUrl() == null) {
            Glide.with(context).load(item.getProfileUrl()).into(holder.iv_profile);
        } else {
            holder.iv_profile.setImageResource(R.drawable.baseline_person_outline_24);
        }
        holder.tv_username.setText(item.getRecord().getUsername());
        holder.iv_collect.setTag(position);
        adapter = new ImageListAdapter(context, item.getRecord().getImageUrlList());
        holder.rv_imglist.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.rv_imglist.setLayoutManager(llm);
    }

    @Override
    public int getItemCount() {
        if(data == null){
            return 0;
        }
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile,iv_collect;
        TextView tv_username;
        RecyclerView rv_imglist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_collect = itemView.findViewById(R.id.iv_collect);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_username = itemView.findViewById(R.id.tv_username);
            rv_imglist = itemView.findViewById(R.id.rv_imglist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ls.onItemClick(position);
                    }
                }
            });

            //取消收藏事件
            iv_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int p = getBindingAdapterPosition();
                    String id = data.get(p).getRecord().getCollectId();
                    data.remove(p);
                    notifyItemRemoved(p);
                    ps.cancelCollect(id).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body().getCode() == 200){
                                System.out.println("取消收藏成功");
                                if(response.body().getData() == null){
                                    Toast.makeText(context, "收藏列表为空", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                String msg = String.format("错误代码：%d",response.body().getCode());
                                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
