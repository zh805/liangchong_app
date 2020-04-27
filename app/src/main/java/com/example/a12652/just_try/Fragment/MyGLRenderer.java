package com.example.a12652.just_try.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.a12652.just_try.Activity.MainActivity;
import com.example.a12652.just_try.R;
import com.example.a12652.just_try.Shape.Cube;
import com.example.a12652.just_try.Shape.DevShape;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 12652 on 2020/2/21.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private final int ROWNUM = 3;
    private final float ROWSPECING = 1.25f;
    private final float LINESPECING = 1.67f;
    private final float HEIGHTSPECING = 0.5f;
    private final float[] GREEN = {67 / 256f, 185 / 256f, 37 / 256f, 1.0f};
    private final float[] RED = {237 / 256f, 28 / 256f, 36 / 256f, 1.0f};
    private final int PLY = 3;
    private final Bitmap mBm_bg_green = BitmapFactory.decodeResource(MainActivity.Main_Context.getResources(), R.drawable.green);
    private final Bitmap mBm_bg_red = BitmapFactory.decodeResource(MainActivity.Main_Context.getResources(), R.drawable.red);
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mRotationMatrixX = new float[16];
    private float[] mRotationMatrixY = new float[16];
    private float[] mScaleMatrix = new float[16];
    private Cube cube;
    private int dev_num = 15;
    private List<DevShape> devList = new ArrayList<>();
    private float[] cubeCenter = {0f, 0f, 0f};
    private List<Integer> resourceList = new ArrayList<>();
    private List<Bitmap> dev_bitmap = new ArrayList<>();
    private List<Bitmap> bitmapList = new ArrayList<>();
    ;
    private int resourceFlag = 0;
    ;

    public MyGLRenderer() {
        dev_bitmap.clear();
        resourceList.add(R.drawable.dev_1);
        resourceList.add(R.drawable.dev_2);
        resourceList.add(R.drawable.dev_3);
        resourceList.add(R.drawable.dev_4);
        resourceList.add(R.drawable.dev_5);
        resourceList.add(R.drawable.dev_6);
        resourceList.add(R.drawable.dev_7);
        resourceList.add(R.drawable.dev_8);
        resourceList.add(R.drawable.dev_9);
        resourceList.add(R.drawable.dev_10);
        resourceList.add(R.drawable.dev_11);
        resourceList.add(R.drawable.dev_12);
        resourceList.add(R.drawable.dev_13);
        resourceList.add(R.drawable.dev_14);
        resourceList.add(R.drawable.dev_15);
        resourceList.add(R.drawable.dev_16);
        resourceList.add(R.drawable.dev_17);
        resourceList.add(R.drawable.dev_18);
        resourceList.add(R.drawable.dev_19);
        resourceList.add(R.drawable.dev_20);
        resourceList.add(R.drawable.dev_21);
        resourceList.add(R.drawable.dev_22);
        resourceList.add(R.drawable.dev_23);
        resourceList.add(R.drawable.dev_24);
        resourceList.add(R.drawable.dev_25);
        resourceList.add(R.drawable.dev_26);
        resourceList.add(R.drawable.dev_27);
        resourceList.add(R.drawable.dev_28);
        resourceList.add(R.drawable.dev_29);
        resourceList.add(R.drawable.dev_30);
        resourceList.add(R.drawable.dev_31);
        resourceList.add(R.drawable.dev_32);
        resourceList.add(R.drawable.dev_33);
        resourceList.add(R.drawable.dev_34);
        resourceList.add(R.drawable.dev_35);
        resourceList.add(R.drawable.dev_36);
        resourceList.add(R.drawable.dev_37);
        resourceList.add(R.drawable.dev_38);
        resourceList.add(R.drawable.dev_39);
        resourceList.add(R.drawable.dev_40);
        resourceList.add(R.drawable.dev_41);
        resourceList.add(R.drawable.dev_42);
        resourceList.add(R.drawable.dev_43);
        resourceList.add(R.drawable.dev_44);
        resourceList.add(R.drawable.dev_45);
        for (int resource : resourceList) {
            dev_bitmap.add(DrawableToBitmap(resource));
        }
        for (Bitmap bitmap : dev_bitmap) {
            bitmapList.add(mergeWithCrop(mBm_bg_green, bitmap));
        }
    }

    public static int loadShader(int type, String shaderCode) {
        //根据type创建顶点着色器或者片元着色器
        int shader = GLES20.glCreateShader(type);
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL10.GL_SMOOTH);
        cube = new Cube(cubeCenter, 10f, 5f, 2f);
        initDevShape();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 28);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 5.0f, 15.0f, 15.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_BLEND);
        //设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        //设置BlendEquation，GLES2.0中有三种
        GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
        float[] scratchX = new float[16];
        float[] scratchY = new float[16];
        float[] scale = new float[16];
        float[] scratchXY = new float[16];
        Matrix.setRotateM(scratchX, 0, TriModelFragment.mAngleX, 1f, 0f, 0f);
        Matrix.setRotateM(scratchY, 0, TriModelFragment.mAngleY, 0f, 1f, 0f);
        Matrix.multiplyMM(scratchXY, 0, scratchX, 0, scratchY, 0);
        Matrix.scaleM(scratchXY, 0, TriModelFragment.mscale, TriModelFragment.mscale, TriModelFragment.mscale);
        Matrix.multiplyMM(scale, 0, mMVPMatrix, 0, scratchXY, 0);
        for (DevShape devShape : devList) {
            if (devShape == devList.get(TriModelFragment.dev_num - 1)) {
                if (TriModelFragment.pre_dev_num!=0){
                    devList.get(TriModelFragment.pre_dev_num - 1).setCylinderBitmap(mergeWithCrop(mBm_bg_green, dev_bitmap.get(TriModelFragment.pre_dev_num - 1)));
                    devList.get(TriModelFragment.pre_dev_num - 1).drawDev(scale,GREEN);
                }
                devShape.setCylinderBitmap(mergeWithCrop(mBm_bg_red, dev_bitmap.get(TriModelFragment.dev_num - 1)));
                devShape.drawDev(scale, RED);
            } else {
                devShape.drawDev(scale, GREEN);
            }
        }
//        cube.drawSelf(scale);
    }

    private void initDevShape() {
        int row = 0, last_row = 0, ply = PLY;
        float[] devCenter = {1.67f - 5, 0.8f, 1.25f - 2.5f};

        for (; ply > 0; ply--) {
            if (this.dev_num % ROWNUM == 0) {
                row = this.dev_num / ROWNUM - 1;
                last_row = ROWNUM;
            } else {
                row = this.dev_num / ROWNUM;
                last_row = this.dev_num % ROWNUM;
            }
            for (; row > 0; row--) {
                for (int j = 0; j < ROWNUM; j++, resourceFlag++) {
                    DevShape devShape = new DevShape(devCenter);
                    devShape.setCylinderBitmap(bitmapList.get(resourceFlag));
                    devList.add(devShape);
                    devCenter[2] = devCenter[2] + ROWSPECING;
                }
                devCenter[0] = devCenter[0] + LINESPECING;
                devCenter[2] = 1.25f - 2.5f;
            }
            for (; last_row > 0; last_row--, resourceFlag++) {
                DevShape devShape = new DevShape(devCenter);
                devShape.setCylinderBitmap(bitmapList.get(resourceFlag));
                devList.add(devShape);
                devCenter[2] = devCenter[2] + ROWSPECING;
            }
            devCenter[1] = devCenter[1] - HEIGHTSPECING;
            devCenter[2] = 1.25f - 2.5f;
            devCenter[0] = 1.67f - 5;
        }
        resourceFlag = 0;
    }

    public Bitmap DrawableToBitmap(int resourceId) {
        Bitmap bm_num = BitmapFactory.decodeResource(MainActivity.Main_Context.getResources(), resourceId);
        return bm_num;
    }

    public Bitmap mergeWithCrop(Bitmap bg, Bitmap bm) {
        int width = bg.getWidth();
        int height = bg.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(bg, 0, 0, null);
        cv.drawBitmap(bm, 0, 0, null);
        return newBitmap;
    }

}
