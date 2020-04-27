package com.example.a12652.just_try.Fragment;

import android.app.Fragment;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a12652.just_try.Activity.LoginActivity;
import com.example.a12652.just_try.Dao.DevDao;
import com.example.a12652.just_try.Data.DevInfo;
import com.example.a12652.just_try.R;

/**
 * Created by 12652 on 2020/1/10.
 */

public class TriModelFragment extends Fragment implements View.OnTouchListener, AdapterView.OnItemSelectedListener {

    public static float mAngleX, mAngleY;
    public static float mscale = 1;
    public static int dev_num, pre_dev_num;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private GLSurfaceView glSurfaceView;
    private Spinner spinner_deport, spinner_dev;
    private TextView dev_type,dev_status,dev_install_time;
    private float mPreviousX, mPreviousY;
    private float baseValue;
    private com.example.a12652.just_try.Fragment.MyGLRenderer MyGLRenderer;
    private DevDao devDao;
    private DevInfo dev;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trimodel_fragment, container, false);
        glSurfaceView = view.findViewById(R.id.glsv_content);
        spinner_deport = view.findViewById(R.id.sp_chart_deport_number);
        spinner_dev = view.findViewById(R.id.sp_chart_dev_number);

        dev_type=view.findViewById(R.id.dev_type);
        dev_status=view.findViewById(R.id.dev_status);
        dev_install_time=view.findViewById(R.id.dev_install_time);

        devDao=new DevDao(LoginActivity.mDb);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, LoginActivity.userInfoData.getWarehouse_code());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_deport.setAdapter(adapter);
        spinner_dev.setOnItemSelectedListener(this);

        glSurfaceView.setOnTouchListener(this);
        glSurfaceView.setEGLContextClientVersion(2);
        MyGLRenderer = new MyGLRenderer();
        glSurfaceView.setRenderer(MyGLRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sp_chart_dev_number) {
            pre_dev_num = dev_num;
            dev_num = Integer.parseInt(spinner_dev.getSelectedItem().toString());
            dev=devDao.queryOne(dev_num);
            dev_status.setText(dev.getDev_status());
            dev_type.setText(dev.getDev_type());
            dev_install_time.setText(dev.getDev_install_time());
            glSurfaceView.requestRender();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    public void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            baseValue = 0;//前一个手势的两只手指之间的距离
            float xx = event.getRawX();
            float yy = event.getRawY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            if (event.getPointerCount() == 2) {//两只手指时
                float xx = event.getX(0) - event.getX(1);
                float yy = event.getY(0) - event.getY(1);
                float value = (float) Math.sqrt(xx * xx + yy * yy);//计算两只手指之间的距离
                if (baseValue == 0) {
                    baseValue = value;
                } else {
                    if (value - baseValue >= 30 || value - baseValue <= -30) {//防止手指在屏幕上抖动就触发
                        float scale = value / baseValue;
                        mscale = scale;
                        glSurfaceView.requestRender();
                    }
                }
            } else if (event.getPointerCount() == 1) {//一只手指时
                float dy = y - mPreviousY;//计算触控笔Y位移
                float dx = x - mPreviousX;//计算触控笔X位移
                mAngleX += dy * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度
                mAngleY += dx * TOUCH_SCALE_FACTOR;//设置沿y轴旋转角度
                glSurfaceView.requestRender();//重绘画面
                baseValue = 0;
            }
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

}