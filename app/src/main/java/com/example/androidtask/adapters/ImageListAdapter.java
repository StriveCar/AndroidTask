package com.example.androidtask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.tv_imageurl.setText(imgURLlist.get(position));
        System.out.println(imgURLlist.get(position));
    }

    @Override
    public int getItemCount() {
        return imgURLlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_imageurl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_imageurl = itemView.findViewById(R.id.tv_imgurl);
        }
    }
}
