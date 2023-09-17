package com.example.androidtask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.androidtask.R;
import com.example.androidtask.response.Records;
import com.example.androidtask.response.sharelist_item;

import java.util.List;

public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.mViewHolder> {

    private List<sharelist_item> data;
    private Context context;

    public ShareListAdapter(@NonNull Context context, List<sharelist_item> data){
        this.data = data;
        this.context = context;
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
        String title = String.format("%s",item.record.getUsername());
        holder.sharelist_item_username.setText(title);
        String st = data.get(position).profileUrl;
        if(st == "" || st == null){
            holder.iv_userprofile.setImageResource(R.drawable.baseline_person_outline_24);
        } else {
            Glide.with(context).load(item.profileUrl).into(holder.iv_userprofile);
        }
        //配置嵌套的图片列表适配器
        LinearLayoutManager llm = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        GridLayoutManager glm = new GridLayoutManager(context,3);
        holder.sharelist_item_imagelist.setLayoutManager(llm);
        ImageListAdapter rvadapter = new ImageListAdapter(context, item.record.getImageUrlList());
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
        ImageView iv_userprofile;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            sharelist_item_username = itemView.findViewById(R.id.sharelist_item_username);
            sharelist_item_imagelist = itemView.findViewById(R.id.sharelist_item_imagelist);
            iv_userprofile = itemView.findViewById(R.id.iv_userprofile);
        }
     }
}
