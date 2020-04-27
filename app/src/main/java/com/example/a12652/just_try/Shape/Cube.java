package com.example.a12652.just_try.Shape;

import android.opengl.GLES20;

import com.example.a12652.just_try.Fragment.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by 12652 on 2020/3/9.
 */

public class Cube {

    final int COORDS_PER_VERTEX = 3;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;"+
                    "varying  vec4 vColor;"+
                    "attribute vec4 aColor;"+
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "  vColor=aColor;"+
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private final short cubeIndex[] = {
            6, 7, 4, 6, 4, 5,    //后面
            6, 3, 7, 6, 2, 3,    //右面
            6, 5, 1, 6, 1, 2,    //下面
            0, 3, 2, 0, 2, 1,    //正面
            0, 1, 5, 0, 5, 4,    //左面
            0, 7, 3, 0, 4, 7,    //上面
    };
    private final float cubeColor[] = {
            0.7f,0.7f,0.7f,0.5f,
            0.7f,0.7f,0.7f,0.5f,
            0.7f,0.7f,0.7f,0.5f,
            0.7f,0.7f,0.7f,0.5f,
            0.7f,0.7f,0.7f,0.5f,
            0.7f,0.7f,0.7f,0.5f,
            0.7f,0.7f,0.7f,0.5f,
            0.7f,0.7f,0.7f,0.5f,
    };
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    private FloatBuffer vertexBuffer, colorBuffer;
    private ShortBuffer indexBuffer;
    private int mProgram;
    private float[] cubeCenter = new float[3];
    private float cubeLong, cubeWidth, cubeHeigh;
    private float[] cubePositions;
    //顶点个数
    private final int vertexCount = 8;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMatrixHandler;

    public Cube(float[] cubeCenter, float cubeLong, float cubeWidth, float cubeHeigh) {
        this.cubeCenter = cubeCenter;
        this.cubeLong = cubeLong;
        this.cubeWidth = cubeWidth;
        this.cubeHeigh = cubeHeigh;
        this.cubePositions = setCubePositions();

        ByteBuffer bb = ByteBuffer.allocateDirect(
                this.cubePositions.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(this.cubePositions);
        vertexBuffer.position(0);

        ByteBuffer dd = ByteBuffer.allocateDirect(
                cubeColor.length * 4);
        dd.order(ByteOrder.nativeOrder());
        colorBuffer = dd.asFloatBuffer();
        colorBuffer.put(cubeColor);
        colorBuffer.position(0);

        ByteBuffer cc= ByteBuffer.allocateDirect(cubeIndex.length*2);
        cc.order(ByteOrder.nativeOrder());
        indexBuffer=cc.asShortBuffer();
        indexBuffer.put(cubeIndex);
        indexBuffer.position(0);
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        //创建一个空的OpenGLES程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);
    }

    private float[] setCubePositions() {
        float[] positions = {
                this.cubeCenter[0] - this.cubeLong / 2, this.cubeCenter[1] + this.cubeHeigh / 2, this.cubeCenter[2] + this.cubeWidth / 2,
                this.cubeCenter[0] - this.cubeLong / 2, this.cubeCenter[1] - this.cubeHeigh / 2, this.cubeCenter[2] + this.cubeWidth / 2,
                this.cubeCenter[0] + this.cubeLong / 2, this.cubeCenter[1] - this.cubeHeigh / 2, this.cubeCenter[2] + this.cubeWidth / 2,
                this.cubeCenter[0] + this.cubeLong / 2, this.cubeCenter[1] + this.cubeHeigh / 2, this.cubeCenter[2] + this.cubeWidth / 2,
                this.cubeCenter[0] - this.cubeLong / 2, this.cubeCenter[1] + this.cubeHeigh / 2, this.cubeCenter[2] - this.cubeWidth / 2,
                this.cubeCenter[0] - this.cubeLong / 2, this.cubeCenter[1] - this.cubeHeigh / 2, this.cubeCenter[2] - this.cubeWidth / 2,
                this.cubeCenter[0] + this.cubeLong / 2, this.cubeCenter[1] - this.cubeHeigh / 2, this.cubeCenter[2] - this.cubeWidth / 2,
                this.cubeCenter[0] + this.cubeLong / 2, this.cubeCenter[1] + this.cubeHeigh / 2, this.cubeCenter[2] - this.cubeWidth / 2,
        };
        return positions;
    }

    public void drawSelf(float[] mMVPMatrix) {
        GLES20.glUseProgram(mProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler= GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        //设置绘制三角形的颜色
//        GLES20.glUniform4fv(mColorHandle, 2, color, 0);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle,4,
                GLES20.GL_FLOAT,false,
                0,colorBuffer);
        //索引法绘制正方体
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,cubeIndex.length, GLES20.GL_UNSIGNED_SHORT,indexBuffer);

        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }


}
