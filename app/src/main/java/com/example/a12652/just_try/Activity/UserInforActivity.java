package com.example.a12652.just_try.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.a12652.just_try.Adapter.UserInforAdapter;
import com.example.a12652.just_try.R;

public class UserInforActivity extends AppCompatActivity {
    private ListView mlistview;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);
        mlistview = (ListView) findViewById(R.id.userinfor_listview);
        back= (Button) findViewById(R.id.user_info_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UserInforAdapter userinfor_adapter = new UserInforAdapter(UserInforActivity.this);
        mlistview.setAdapter(userinfor_adapter);
    }
}
