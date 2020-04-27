package com.example.a12652.just_try.Shape;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static com.example.a12652.just_try.Fragment.MyGLRenderer.loadShader;

/**
 * Created by 12652 on 2020/3/11.
 */

public class Circular {

    private static final int COORDS_PER_VERTEX = 3;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private FloatBuffer vertexBuffer;
    private float[] circularCenter;
    private float[] Positions;
    private float radius;
    private int n = 180;
    private int circularProgram;
    private int mMatrixHandler;
    private int mPositionHandle;
    private int mColorHandle;

    public Circular(float[] center, float r) {
        circularCenter = center;
        radius = r;
        Positions = createPositions();

        ByteBuffer bb = ByteBuffer.allocateDirect(
                Positions.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(Positions);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        //创建一个空的OpenGLES程序
        circularProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(circularProgram, vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(circularProgram, fragmentShader);
        //连接到着色器程序
        GLES20.glLinkProgram(circularProgram);
    }

    public void drawSelf(float[] mMVPMatrix,float[] color) {
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(circularProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES20.glGetUniformLocation(circularProgram, "vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(circularProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                COORDS_PER_VERTEX * 4, vertexBuffer);
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(circularProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, Positions.length / 3);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    private float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        //设置圆心坐标
        data.add(circularCenter[0]);
        data.add(circularCenter[1]);
        data.add(circularCenter[2]);
        float angDegSpan = 360f / n;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (circularCenter[0] + radius * Math.sin(i * Math.PI / 180f)));
            data.add(circularCenter[1]);
            data.add((float) (circularCenter[2] + radius * Math.cos(i * Math.PI / 180f)));
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

}
