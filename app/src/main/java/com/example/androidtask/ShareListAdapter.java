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

public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.ViewHolder> {

    private List<Records> recordslist;
    private Context context;
    private int resourceId;

    public ShareListAdapter(@NonNull Context context, int resourceId, List<Records> data){
        this.recordslist = data;
        this.context = context;
        this.resourceId = resourceId;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourceId, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Records record = recordslist.get(position);
        String title = String.format("用户 "+record.getUsername()+"的分享");
        holder.sharelist_item_username.setText(title);
    }

    @Override
    public int getItemCount() {
        if(recordslist == null){
            System.out.println("没有数据，data为空");
            return 0;
        }
        return recordslist.size();
    }
    static public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView sharelist_item_userimage;
        TextView sharelist_item_username;
        TextView sharelist_item_content;
        ListView sharelist_item_imagelist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sharelist_item_userimage = itemView.findViewById(R.id.sharelist_item_userimage);
            sharelist_item_username = itemView.findViewById(R.id.sharelist_item_username);
            sharelist_item_content = itemView.findViewById(R.id.sharelist_item_content);
            sharelist_item_imagelist = itemView.findViewById(R.id.sharelist_item_imagelist);
        }
    }
}
