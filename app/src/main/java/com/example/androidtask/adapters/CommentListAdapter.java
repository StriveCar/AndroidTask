package com.example.androidtask.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtask.R;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Comment;
import com.example.androidtask.response.Data;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;

public class CommentListAdapter extends BaseAdapter {
    private String userId;
    private Context context;
    private CommentListAdapter2 adapter2;
    private List<Comment> data = new ArrayList<>();
    private List<Comment> data2 = new ArrayList<>();
    private PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private int current = 0,size = 50;

    public CommentListAdapter(Context c, List<Comment> d, String id){
        data = d;
        context = c;
        userId = id;
        adapter2 = new CommentListAdapter2(context, data2, userId);
    }
    @Override
    public int getCount() {
        if(data == null){
            return 0;
        } else {
            return data.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.firstcommentlist_item,viewGroup,false);
            holder.tv = view.findViewById(R.id.tv_firstComment);
            holder.lv_SecondCommentList = view.findViewById(R.id.lv_SecondCommentList);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //绑定数据
        String text = String.format("%s：%s",data.get(i).getUserName(),data.get(i).getContent());
        holder.tv.setText(text);
//        adapter2 = new CommentListAdapter2(context, data2, userId);
        holder.lv_SecondCommentList.setAdapter(adapter2);
        getSecondCommentsData(i);

        return view;
    }

    private void getSecondCommentsData(int i) {
        photoService.getsecondComment(data.get(i).getId(), current, data.get(i).getShareId(),size).subscribe(new FlowableSubscriber<BaseResponse<Data<Comment>>>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {s.request(Long.MAX_VALUE);}

            @Override
            public void onNext(BaseResponse<Data<Comment>> dataBaseResponse) {
                if(dataBaseResponse.getCode() == 200){
                    if(dataBaseResponse.getData()!=null){
                        Comment comment = new Comment();
                        for (int i=0;i<dataBaseResponse.getData().getSize();i++){
                            comment = dataBaseResponse.getData().getRecords().get(i);
                            data2.add(comment);
                            if (data2.size() == dataBaseResponse.getData().getRecords().size()){
                                //数据添加完成
                                adapter2.notifyDataSetChanged();
                            }
                        }
                    } else {
                        System.out.println("没有二级评论");
                    }
                } else {
                    String msg = String.format("错误代码：%d",dataBaseResponse.getCode());
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(String.format("错误：%s",t.getMessage()));
            }

            @Override
            public void onComplete() {
            }
        });
    }

    class ViewHolder{
        TextView tv;
        ListView lv_SecondCommentList;
    }
}
