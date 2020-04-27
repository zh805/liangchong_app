package com.example.a12652.just_try.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a12652.just_try.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText newpw,newpw_again;
    private TextView userIphone;
    private Button bt_getCode,bt_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        initView();
        if (!newpw.getText().toString().equals(newpw_again.getText().toString())){
            Toast.makeText(ChangePasswordActivity.this,"两次密码输入不同",Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        newpw= (EditText) findViewById(R.id.et_new_password);
        newpw_again= (EditText) findViewById(R.id.et_new_password_again);
        bt_getCode= (Button) findViewById(R.id.bt_idCode);
        bt_ok= (Button) findViewById(R.id.bt_changepassword);
        userIphone= (TextView) findViewById(R.id.user_phone);
        userIphone.setText(LoginActivity.userInfoData.getUsermobile());
    }
}
