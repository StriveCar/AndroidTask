package com.example.androidtask.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidtask.FullScreenImageActivity;
import com.example.androidtask.R;

import java.util.ArrayList;
import java.util.List;

public class ShareDetailPageAdapter extends RecyclerView.Adapter<ShareDetailPageAdapter.mViewHolder> {

    private Context context;
    private List<String> imgUrlList = new ArrayList<>();
    public ShareDetailPageAdapter(Context context, List<String> list){
        this.context = context;
        imgUrlList = list;
    }
    @NonNull
    @Override
    public ShareDetailPageAdapter.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sharedetailpage_imglist_item,parent,false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareDetailPageAdapter.mViewHolder holder, int position) {
        Glide.with(context).load(imgUrlList.get(position))
                .fitCenter()
                .override(400,400)
                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return imgUrlList.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_SDP_imglist_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = getBindingAdapterPosition();
                    Intent intent = new Intent(context, FullScreenImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", imgUrlList.get(i));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

}

