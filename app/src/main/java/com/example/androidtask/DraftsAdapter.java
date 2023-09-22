package com.example.androidtask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.androidtask.R;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.ImageText;
import com.example.androidtask.response.LoginData;
import com.example.androidtask.response.Records;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.mViewHolder> {

    private List<Records> data;
    private Context context;
    private DraftsAdapter draftsAdapter;

    private final PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);

    private String userId;
    private String shareId;

    private RecyclerView rc;

    public interface onDeleteClick {
        void onDelete(int pos);
    }

    onDeleteClick onDeleteClick;

    public void setOnDeleteClick(DraftsAdapter.onDeleteClick onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    public DraftsAdapter(@NonNull Context context, List<Records> data){
        this.data = data;
        this.context = context;

    }
    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =LayoutInflater.from(context).inflate(R.layout.list_item, parent ,false);
        return new mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.drafts_title.setText(data.get(position).getTitle());
        holder.drafts_content.setText(data.get(position).getContent());
        Glide.with(context).load(data.get(position).getImageUrlList().get(0)).into(holder.drafts_pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = data.get(position).getTitle();
                String content = data.get(position).getContent();
                String imageCode = data.get(position).getImageCode();
                String id = data.get(position).getId();
                String userId = data.get(position).getPUserId();

                List<String> image = data.get(position).getImageUrlList();

                Intent intent = new Intent(context, DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putStringArrayListExtra("stringList", (ArrayList<String>)image);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("imageCode",imageCode);
                intent.putExtra("id",id);
                intent.putExtra("userId",userId);
//                intent.setFlags();
                context.startActivity(intent);
            }
        });
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("是否删除当前内容")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击确定按钮的处理逻辑
                                //获取userId和shareId
                                userId = data.get(position).getPUserId();
                                shareId = data.get(position).getId();
                                photoService.deleteAdd(userId,shareId).enqueue(new Callback<BaseResponse<Data<Records>>>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse<Data<Records>>> call, Response<BaseResponse<Data<Records>>> response) {
                                        if (response.body().getCode() == 200) {
                                            Toast.makeText(view.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                            if (position != -1) onDeleteClick.onDelete(position);
                                            System.out.println("删除成功!  " + response.body());
                                        } else if (response.body().getCode() == 500) {
                                            System.out.println(response.body().getMsg());
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<BaseResponse<Data<Records>>> call, Throwable t) {
                                        Toast.makeText(view.getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击取消按钮的处理逻辑
                                dialog.dismiss();
                            }
                        });
                // 创建并显示弹出框
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


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
        TextView drafts_title;
        TextView drafts_content;
        ImageView drafts_pic;
        ImageButton delete_button;



        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            drafts_title = itemView.findViewById(R.id.df_title);
            drafts_content = itemView.findViewById(R.id.df_content);
            drafts_pic = itemView.findViewById(R.id.df_image);
            delete_button = itemView.findViewById(R.id.deleteButton);

        }
    }

}
