package com.example.androidtask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidtask.OnItemClickListener;
import com.example.androidtask.R;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Records;
import com.example.androidtask.sharelist_item;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.mViewHolder> {

    private String userId;
    private List<sharelist_item> data;
    private Context context;
    private OnItemClickListener listener;
    private int width;

    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);

    public ShareListAdapter(@NonNull Context context,
                            List<sharelist_item> data,
                            OnItemClickListener listener,
                            int width,
                            String userId){
        this.data = data;
        this.context = context;
        this.listener = listener;
        this.width = width;
        this.userId = userId;
    }
    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =LayoutInflater.from(context).inflate(R.layout.sharelist_item, parent ,false);
        return new mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        sharelist_item item = data.get(position);
        String title = String.format("%s",item.getRecord().getUsername());
        holder.sharelist_item_username.setText(title);
        String st = data.get(position).getProfileUrl();
        //头像
        if(st == "" || st == null){
            holder.iv_userprofile.setImageResource(R.drawable.baseline_person_outline_24);
        } else {
            Glide.with(context).load(item.getProfileUrl()).into(holder.iv_userprofile);
        }
        //点赞收藏图标的处理
        if(item.getRecord().getHasLike()){
            holder.iv_thumbsUp.setImageResource(R.drawable.baseline_thumb_up_24);
        } else {
            holder.iv_thumbsUp.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
        }

        //配置嵌套的图片列表适配器
        LinearLayoutManager llm = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        GridLayoutManager glm = new GridLayoutManager(context,3);
        holder.sharelist_item_imagelist.setLayoutManager(llm);
        ImageListAdapter rvadapter = new ImageListAdapter(context, item.getRecord().getImageUrlList());
        holder.sharelist_item_imagelist.setAdapter(rvadapter);
        holder.sharelist_item_imagelist.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        if(data == null){
            System.out.println("没有数据，data为空");
            return 0;
        }
        return data.size();
    }
     class mViewHolder extends RecyclerView.ViewHolder{
        TextView sharelist_item_username;
        RecyclerView sharelist_item_imagelist;
        ImageView iv_userprofile,iv_thumbsUp,iv_collect;
        CardView cv;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            sharelist_item_username = itemView.findViewById(R.id.sharelist_item_username);
            sharelist_item_imagelist = itemView.findViewById(R.id.sharelist_item_imagelist);
            iv_userprofile = itemView.findViewById(R.id.iv_userprofile);
            iv_thumbsUp = itemView.findViewById(R.id.iv_thumbsUp);
            iv_collect = itemView.findViewById(R.id.iv_collect);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 触发点击事件回调，并传递列表项的位置
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
            //屏幕适配
            cv = itemView.findViewById(R.id.root_carview);
            ViewGroup.LayoutParams lp = cv.getLayoutParams();
            lp.width = width;
            cv.setLayoutParams(lp);
            //点赞绑定事件
            iv_thumbsUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int p = getBindingAdapterPosition();
                    sharelist_item item = data.get(p);
                    boolean haslike = item.getRecord().getHasLike();
                    if(!haslike){
                        //点赞
                        photoService.thumbsUp(data.get(p).getRecord().getId(), userId).enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                if(response.body().getCode() == 200){
                                    //点赞成功
                                    item.getRecord().setHasLike(true);
                                    iv_thumbsUp.setImageResource(R.drawable.baseline_thumb_up_24);
                                } else {
                                    //点赞失败，网络或者后台出现问题
                                    String msg = String.format("错误代码：%d",response.body().getCode());
                                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        //取消点赞,先获取likeId
                        photoService.getDetail(data.get(p).getRecord().getId(), userId).enqueue(new Callback<BaseResponse<Records>>() {
                            @Override
                            public void onResponse(Call<BaseResponse<Records>> call, Response<BaseResponse<Records>> response) {
                                System.out.println(String.format("shareId:%s",data.get(p).getRecord().getId()));
                                System.out.println(String.format("userId:%s",userId));

                                String st = response.body().getData().getLikeId();
                                photoService.cancelLike(st).enqueue(new Callback<BaseResponse>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                        if (response.body().getCode() == 200) {
                                            item.getRecord().setHasLike(false);
                                            iv_thumbsUp.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
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

                            @Override
                            public void onFailure(Call<BaseResponse<Records>> call, Throwable t) {
                                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            //收藏
        }
     }
}
