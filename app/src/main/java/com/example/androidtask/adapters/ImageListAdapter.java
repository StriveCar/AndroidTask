package com.example.androidtask.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidtask.R;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private List<String> imgURLlist;
    private Context context;
    ImageListAdapter(Context context, List<String> URLlist){
        this.imgURLlist = URLlist;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.imglist_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(imgURLlist.get(position))
                .fitCenter()
                .override(400,400)
                .into(holder.iv_imglist_item);
    }

    @Override
    public int getItemCount() {
        return imgURLlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_imglist_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_imglist_item = itemView.findViewById(R.id.iv_imglist_item);
        }
    }
}
