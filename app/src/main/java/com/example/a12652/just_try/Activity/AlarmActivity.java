package com.example.a12652.just_try.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.a12652.just_try.Adapter.AlarmRecycleViewAdapter;
import com.example.a12652.just_try.R;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btBack;
    private RecyclerView recyclerView;
    private AlarmRecycleViewAdapter alarmRecycleViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List content_list=new ArrayList(),time_list=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        initData();
        initView();
    }

    private void initData() {
        content_list.add("本周32号仓累计监测到蛀食性害虫5头、粉食性害虫8头，虫粮等级：基本无虫粮。");
        content_list.add("30号仓10号设备监测点近三日害虫日增长率10%，请及时关注。");
        time_list.add("2019/10/06");
        time_list.add("2019/10/28");
    }

    private void initView() {
        btBack= (Button) findViewById(R.id.back_alarm_activity);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_alarm_activity);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //创建适配器，将数据传递给适配器
        alarmRecycleViewAdapter = new AlarmRecycleViewAdapter(content_list,time_list);
        //设置布局管理器
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器adapter
        recyclerView.setAdapter(alarmRecycleViewAdapter);

        btBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_alarm_activity:
                finish();
                break;
        }
    }
}
