package com.example.androidtask.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidtask.R;
import com.example.androidtask.sharelist_item;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyshareListAdapter extends RecyclerView.Adapter<MyshareListAdapter.ViewHolder> {
    private List<sharelist_item> data = new ArrayList<>();
    private Context context;
    private String userId;
    public MyshareListAdapter(List<sharelist_item> data,Context c,String uid ){
        this.data = data;
        context = c;
        userId = uid;
    }

    @NonNull
    @Override
    public MyshareListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mysharelist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyshareListAdapter.ViewHolder holder, int position) {
        System.out.println("Mysharelistadapter 启动");
        holder.tv_title.setText(data.get(position).getRecord().getTitle());
        holder.tv_content.setText(data.get(position).getRecord().getContent());
        ImageListAdapter adapter = new ImageListAdapter(context,data.get(position).getRecord().getImageUrlList());
        holder.rv_imglist.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        holder.rv_imglist.setLayoutManager(llm);
    }

    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        } else {
            return data.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_content;
        RecyclerView rv_imglist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            rv_imglist = itemView.findViewById(R.id.rv_imglist);

            //点击进入详情页
            //点击全屏看图
        }
    }
}
