package com.example.a12652.just_try.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.a12652.just_try.R;
import com.example.a12652.just_try.Util.SPHelper;

public class AlarmInfoActivity extends AppCompatActivity {
    private Switch mswitch_alarm;
    private LinearLayout mll_alarm;
    boolean alarm_state;
    SPHelper helper1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_info);
        helper1=new SPHelper(AlarmInfoActivity.this,"AlarmSet");
        mswitch_alarm = (Switch) findViewById(R.id.sw_alarm);
        mll_alarm = (LinearLayout) findViewById(R.id.ll_alarm);
        mswitch_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    mll_alarm.setVisibility(View.VISIBLE);
                } else {
                    mll_alarm.setVisibility(View.INVISIBLE);
                }
                helper1.putValues(new SPHelper.ContentValue("alarm_state", isChecked));
            }
        });

        mswitch_alarm.setChecked(helper1.getBoolean("alarm_state"));
    }
}
