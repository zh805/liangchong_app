package com.example.a12652.just_try.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a12652.just_try.Dao.UserDao;
import com.example.a12652.just_try.Data.UserInfoData;
import com.example.a12652.just_try.R;
import com.example.a12652.just_try.Util.AssetsDatabaseManager;
import com.example.a12652.just_try.Util.SPHelper;


/**
 * Created by 12652 on 2020/1/8.
 */

public class LoginActivity extends Activity implements View.OnClickListener, View.OnTouchListener, CompoundButton.OnCheckedChangeListener {
    public static SQLiteDatabase mDb;
    public static AssetManager mAm;
    public static UserInfoData userInfoData ;
    //判断是否自动登陆
    boolean isAutoLogin = false;
    private EditText et_user, et_password;
    private Button mbtn_login;
    private boolean pwstatus;
    private CheckBox msavepassword, mauto_login;
    private UserDao userDao;
    private SPHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        //读取数据库文件
        mAm = getAssets();
        AssetsDatabaseManager.initManager(this);
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        mDb = mg.getDatabase("PestManager.db");
        userDao = new UserDao(mDb);

        //判断用户第一次登录
        idFirstlogin();
        //初始化数据
        initView();
    }

    private void initView() {

        et_user = (EditText) findViewById(R.id.et_user);
        msavepassword = findViewById(R.id.save_password);
        mauto_login = findViewById(R.id.auto_login);
        et_password = (EditText) findViewById(R.id.et_password);
        mbtn_login = (Button) findViewById(R.id.btn_login);

        //密码初始状态为不可见
        pwstatus = false;


        msavepassword.setOnCheckedChangeListener(this);
        mauto_login.setOnCheckedChangeListener(this);
        et_user.setOnTouchListener(this);
        et_password.setOnTouchListener(this);
        mbtn_login.setOnClickListener(this);

        //获取SharePreference文件数据
        SPHelper helper = new SPHelper(this, "login");
        //判断用户是否自动登陆
        isAutoLogin = helper.getBoolean("isAutoLogin");
        //获取用户名和密码
        String name = helper.getString("username");
        String pass = helper.getString("password");
        //如果记录有用户名和密码，把用户名和密码放到输入框中
        if (!TextUtils.isEmpty(name)) {
            et_user.setText(name);
            et_password.setText(pass);
            //选中记住密码的选框
            msavepassword.setChecked(true);

        }

        //记得在软件内部设置取消自动登录、否则一旦自动登录，就见不到登录界面

//        if (isAutoLogin) {
//            Bundle bundle = new Bundle();
//            bundle.putString("mobile", name);
//
//            //选中记住密码的选框
//            mauto_login.setChecked(true);
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
//            finish();
//        }
    }

    private void idFirstlogin() {
        SPHelper helper = new SPHelper(this, "set");
        boolean isfirt = helper.getBoolean("isfirst");
        if (isfirt) {
            //这里创建一个ContentVa对象
            helper.putValues(new SPHelper.ContentValue("isfirst", false));
            Toast.makeText(this, "第一次登陆", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //当对记住密码选框操作时
        if (buttonView == msavepassword) {
            //当取消时，自动登陆也要取消
            if (!isChecked) {
                mauto_login.setChecked(false);
            }
        }

        //当对自动登陆选框操作时
        if (buttonView == mauto_login) {
            //当选择时，记住密码也要自动去选择
            if (isChecked) {
                msavepassword.setChecked(true);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == et_user) {
            Drawable drawable = et_user.getCompoundDrawables()[2];
            if (drawable == null)
                return false;
            if (event.getAction() != MotionEvent.ACTION_UP)
                return false;
            if (event.getX() > et_password.getWidth() - et_password.getPaddingRight() - drawable.getIntrinsicWidth()) {
                et_user.setText("");
                et_password.setText("");
            }
            return false;
        } else if (v == et_password) {
            Drawable drawable = et_password.getCompoundDrawables()[2];
            if (drawable == null)
                return false;
            if (event.getAction() != MotionEvent.ACTION_UP)
                return false;
            if (event.getX() > et_password.getWidth() - et_password.getPaddingRight() - drawable.getIntrinsicWidth()) {
                if (pwstatus) {
                    et_password.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.password), null, getResources().getDrawable(R.drawable.login_password_close), null);
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password.setSelection(et_password.getText().length());
                    pwstatus = false;
                } else {
                    et_password.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.password), null, getResources().getDrawable(R.drawable.login_password_open), null);
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_password.setSelection(et_password.getText().length());
                    pwstatus = true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (et_user.getText().toString().equals("") || et_password.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "输入为空", Toast.LENGTH_LONG).show();
        } else if (et_user.getText().length() != 11) {
            Toast.makeText(LoginActivity.this, "账号格式输入有误", Toast.LENGTH_LONG).show();
        } else {
            if (userDao.queryOne(et_user.getText().toString()) == null || !userDao.queryOne(et_user.getText().toString()).getPassword().equals(et_password.getText().toString())) {
                Toast.makeText(LoginActivity.this, "密码错误或账号不存在", Toast.LENGTH_SHORT).show();
            } else if (userDao.queryOne(et_user.getText().toString()).getPassword().equals(et_password.getText().toString())) {
                //登录成功
                Log.i("jintian","ok");
                userInfoData=userDao.queryOne(et_user.getText().toString());

                Log.i("jintian","ok"+userInfoData.getUsermobile());
                helper = new SPHelper(LoginActivity.this, "login");
                //检查记住密码按钮状态
                if (msavepassword.isChecked()) {
                    //保存用户名
                    helper.putValues(new SPHelper.ContentValue("username", et_user.getText().toString()));
                    //保存密码
                    helper.putValues(new SPHelper.ContentValue("password", et_password.getText().toString()));
                } else {
                    helper.clear();
                }

                //检查自动登录按钮状态
                if (mauto_login.isChecked()) {
                    helper.putValues(new SPHelper.ContentValue("isAutoLogin", true));
                } else {
                    helper.putValues(new SPHelper.ContentValue("isAutoLogin", false));
                }

                Bundle bundle = new Bundle();
                bundle.putString("mobile", userInfoData.getUsermobile());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }
    }
}