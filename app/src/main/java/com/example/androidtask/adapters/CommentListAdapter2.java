package com.example.androidtask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidtask.R;
import com.example.androidtask.response.Comment;

import java.util.List;

public class CommentListAdapter2 extends BaseAdapter {
    private List<Comment> data;
    private Context context;
    private String userId;
    public CommentListAdapter2(Context c, List<Comment> l, String userId){
        data = l;
        context = c;
        this.userId = userId;
    }
    @Override
    public int getCount() {
        if (data == null){
            return 0;
        } else {
            return data.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return data.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.secondcommentlist_item, viewGroup, false);
            holder.tv = view.findViewById(R.id.tv_secondComment);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //绑定数据
        String text = String.format("%s回复：%s",data.get(i).getUserName(),data.get(i).getContent());
        holder.tv.setText(text);
        return view;
    }

    class ViewHolder{
        TextView tv;
    }
}
