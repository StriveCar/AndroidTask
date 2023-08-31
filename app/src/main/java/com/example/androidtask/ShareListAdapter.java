package com.example.androidtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtask.response.Records;

import java.util.List;

public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.mViewHolder> {

    private List<Records> data;
    private Context context;

    public ShareListAdapter(@NonNull Context context, List<Records> data){
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
        Records record = data.get(position);
        holder.sharelist_item_username.setText(record.getUsername());
        holder.sharelist_item_content.setText(record.getContent());
        System.out.println(record.getUsername());
        System.out.println(record.getContent());
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
        //ImageView sharelist_item_userimage;
        TextView sharelist_item_username;
        TextView sharelist_item_content;
        //ListView sharelist_item_imagelist;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            sharelist_item_username = itemView.findViewById(R.id.sharelist_item_username);
            sharelist_item_content = itemView.findViewById(R.id.sharelist_item_content);
        }


     }
}
