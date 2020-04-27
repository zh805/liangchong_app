package com.example.a12652.just_try.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a12652.just_try.R;

import java.util.List;

/**
 * Created by 12652 on 2020/3/16.
 */

public class AlarmRecycleViewAdapter extends RecyclerView.Adapter<AlarmRecycleViewAdapter.MyHolder> {
    private  List content_list,time_list;

    public AlarmRecycleViewAdapter(List content_list,List time_list) {
        this.content_list=content_list;
        this.time_list=time_list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_recycler, parent, false);
        //将view传递给我们自定义的ViewHolder
        MyHolder holder = new MyHolder(view);
        //返回这个MyHolder实体
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv_alarmContent.setText(content_list.get(position).toString());
        holder.tv_alarmTime.setText(time_list.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return content_list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView tv_alarmContent;
        TextView tv_alarmTime;

        public MyHolder(View itemView) {
            super(itemView);
            tv_alarmContent=itemView.findViewById(R.id.alarm_content);
            tv_alarmTime=itemView.findViewById(R.id.alarm_time);
        }
    }
}
