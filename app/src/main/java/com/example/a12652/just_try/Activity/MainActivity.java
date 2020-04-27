package com.example.a12652.just_try.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a12652.just_try.Adapter.ItemAdapter;
import com.example.a12652.just_try.Fragment.ChartFragment;
import com.example.a12652.just_try.Fragment.MapFragment;
import com.example.a12652.just_try.Fragment.SampleFragment;
import com.example.a12652.just_try.Fragment.TriModelFragment;
import com.example.a12652.just_try.R;
import com.readystatesoftware.viewbadger.BadgeView;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;

/**
 * Created by 12652 on 2020/1/10.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static Context Main_Context;
    private final int IMAGE_CAMERA = 123;
    private ImageView iv_trimodel, iv_chart, iv_map, iv_sample;
    private DrawerLayout mDrawerLayout;
    private TriModelFragment trimodel_fragment;
    private MapFragment map_fragment;
    private ChartFragment chart_frament;
    private SampleFragment sample_fragment;
    private ListView listView;
    private TextView mtitle, muser_mobile;
    private Button mopen_draw, quit_user, quit_app,bt_alarm;
    private String user_mobile;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Main_Context = getApplicationContext();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.main_layout);
        init();
        setListeners();
        getFragmentManager().beginTransaction().add(R.id.fl_main, trimodel_fragment).commitAllowingStateLoss();
        setImageTint(iv_trimodel);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user_mobile = bundle.getString("mobile");
        muser_mobile.setText(user_mobile.substring(0, 3) + "-" + user_mobile.substring(3, 7) + "-" + user_mobile.substring(7, 11));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (position == 0) {
                    intent = new Intent(MainActivity.this, UserInforActivity.class);
                } else if (position == 1) {
                    intent = new Intent(MainActivity.this, WarehouseInfo.class);
                } else if (position == 2) {
                    intent = new Intent(MainActivity.this, AlarmInfoActivity.class);
                } else if (position == 3) {
                    intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                } else {
                }
                startActivity(intent);
            }
        });
    }

    private void init() {
        bt_alarm= (Button) findViewById(R.id.bt_alarm);
        iv_trimodel = (ImageView) findViewById(R.id.iv_TriModel);
        iv_chart = (ImageView) findViewById(R.id.iv_Chart);
        iv_map = (ImageView) findViewById(R.id.iv_Map);
        iv_sample = (ImageView) findViewById(R.id.iv_Sample);
        trimodel_fragment = new TriModelFragment();
        chart_frament = new ChartFragment();
        map_fragment = new MapFragment();
        sample_fragment = new SampleFragment();
        mtitle = (TextView) findViewById(R.id.tv_main_title);
        mopen_draw = (Button) findViewById(R.id.btn_to_PersonalCenter);
        mopen_draw = (Button) findViewById(R.id.btn_to_PersonalCenter);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sample_fragment = new SampleFragment();
        listView = (ListView) findViewById(R.id.lv_menu);
        quit_user = (Button) findViewById(R.id.bt_quit_user);
        quit_app = (Button) findViewById(R.id.bt_quit_app);
        ItemAdapter item_adapter = new ItemAdapter(MainActivity.this);
        listView.setAdapter(item_adapter);
        muser_mobile = (TextView) findViewById(R.id.tv_usermobile);

        
        BadgeView bv = new BadgeView(this, bt_alarm);
        bv.setTextColor(Color.YELLOW);
        bv.setTextSize(8);
        bv.setBadgeMargin(0);
        bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //默认值
        //bv.hide();
        bv.show();

        //setDrawerLeftEdgeSize(this, mDrawerLayout, 1);
    }

    private void setListeners() {
        bt_alarm.setOnClickListener(this);
        iv_trimodel.setOnClickListener(this);
        iv_chart.setOnClickListener(this);
        iv_map.setOnClickListener(this);
        iv_sample.setOnClickListener(this);
        mopen_draw.setOnClickListener(this);
        quit_app.setOnClickListener(this);
        quit_user.setOnClickListener(this);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_main, fragment);
        transaction.commitAllowingStateLoss();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setImageTint(ImageView imageView) {
        iv_trimodel.setImageTintList(ColorStateList.valueOf(Color.BLACK));
        iv_sample.setImageTintList(ColorStateList.valueOf(Color.BLACK));
        iv_chart.setImageTintList(ColorStateList.valueOf(Color.BLACK));
        iv_map.setImageTintList(ColorStateList.valueOf(Color.BLACK));
        imageView.setImageTintList(ColorStateList.valueOf(Color.BLUE));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                ImageView miv_sample_icon = sample_fragment.getView().findViewById(R.id.iv_sample_icon);
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(SampleFragment.mPhotoUri));
                    miv_sample_icon.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "未成功", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_TriModel:
                replaceFragment(trimodel_fragment);
                setImageTint(iv_trimodel);
                mtitle.setText("设备分布");
                break;
            case R.id.iv_Chart:
                replaceFragment(chart_frament);
                setImageTint(iv_chart);
                mtitle.setText("虫情查看");
                break;
            case R.id.iv_Map:
                replaceFragment(map_fragment);
                setImageTint(iv_map);
                mtitle.setText("粮虫图鉴");
                break;
            case R.id.iv_Sample:
                replaceFragment(sample_fragment);
                setImageTint(iv_sample);
                mtitle.setText("数据上传");
                break;
            case R.id.btn_to_PersonalCenter:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.bt_quit_app:
                finish();
                break;
            case R.id.bt_quit_user:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.bt_alarm:

                normalNotification();
                Intent alarmIntent = new Intent(this,AlarmActivity.class);
                startActivity(alarmIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField =
                    drawerLayout.getClass().getDeclaredField("mLeftDragger");//Right
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            // 设置新的边缘大小
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x *
                    displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void normalNotification(){
        Intent intent=new Intent(Settings.ACTION_SETTINGS);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification=new Notification.Builder(this)
                .setSmallIcon(R.drawable.add)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.alarm))
                .setContentTitle("title")
                .setContentText("content")
                .setContentIntent(pendingIntent)
                .setNumber(1)
                .build();
        notification.flags|=Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);
    }

}
