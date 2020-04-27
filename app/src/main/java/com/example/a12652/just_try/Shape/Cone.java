package com.example.a12652.just_try.Shape;

import android.opengl.GLES20;

import com.example.a12652.just_try.Fragment.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by 12652 on 2020/3/12.
 */

public class Cone {
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
    private int coneProgram;
    private int n = 360;  //切割份数
    private float[] center;
    private float[] position;
    private float mRadius, mHeight;
    private FloatBuffer vertexBuffer;

    public Cone(float[] coneCenter, float radius, float height) {
        center = coneCenter;
        mHeight = height;
        mRadius = radius;
        position = setPosition();

        ByteBuffer buffer = ByteBuffer.allocateDirect(position.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(position);
        vertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        //创建一个空的OpenGLES程序
        coneProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(coneProgram, vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(coneProgram, fragmentShader);
        //连接到着色器程序
        GLES20.glLinkProgram(coneProgram);
    }

    public void drawSelf(float[] mMVPMatrix,float[] colors) {
        GLES20.glUseProgram(coneProgram);
        int mMatrix = GLES20.glGetUniformLocation(coneProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mMatrix, 1, false, mMVPMatrix, 0);
        int mPositionHandle = GLES20.glGetAttribLocation(coneProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        int mColorHandle = GLES20.glGetUniformLocation(coneProgram, "vColor");
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glUniform4fv(mColorHandle, 1, colors, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, position.length / 3);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    private float[] setPosition() {
        ArrayList<Float> pos = new ArrayList<>();
        pos.add(center[0]);
        pos.add(center[1] - mHeight);
        pos.add(center[2]);
        float angDegSpan = 360f / n;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            pos.add((float) (center[0] + this.mRadius * Math.sin(i * Math.PI / 180f)));
            pos.add(center[1]);
            pos.add((float) (center[2] + this.mRadius * Math.cos(i * Math.PI / 180f)));
        }
        float[] d = new float[pos.size()];
        for (int i = 0; i < d.length; i++) {
            d[i] = pos.get(i);
        }
        return d;
    }
}
