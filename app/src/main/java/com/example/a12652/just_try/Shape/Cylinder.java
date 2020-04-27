package com.example.a12652.just_try.Shape;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.example.a12652.just_try.Activity.MainActivity;
import com.example.a12652.just_try.Fragment.MyGLRenderer;
import com.example.a12652.just_try.Util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 12652 on 2020/3/10.
 */

public class Cylinder {

    private static final String VERTEX_SHADER = "" +
            "uniform mat4 u_Matrix;\n" +
            "attribute vec4 a_Position;\n" +
            // 纹理坐标：2个分量，S和T坐标
            "attribute vec2 a_TexCoord;\n" +
            "varying vec2 v_TexCoord;\n" +
            "void main()\n" +
            "{\n" +
            "    v_TexCoord = a_TexCoord;\n" +
            "    gl_Position = u_Matrix * a_Position;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "varying vec2 v_TexCoord;\n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);\n" +
            "}";
    private static final int TEX_VERTEX_COMPONENT_COUNT = 2;
    private FloatBuffer mTexVertexBuffer;
    private float[] mCylinderCenter;
    private float mRadio;
    private float mHeight;
    private TextureHelper.TextureBean mTextureBean,TextureStatus;
    private int cylinderProgram;
    private FloatBuffer vertexBuffer;
    private int n = 360;  //切割份数
    private float[] positions;
    private float[] textures;

    private int mATexCoordLocation;
    private int Texture;

    public Cylinder(float[] cylinderCenter, float radio, float height) {
        mCylinderCenter = cylinderCenter;
        mRadio = radio;
        mHeight = height;
        positions = setPositions();

        ByteBuffer buffer = ByteBuffer.allocateDirect(positions.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(positions);
        vertexBuffer.position(0);

        ByteBuffer c = ByteBuffer.allocateDirect(this.textures.length * 4);
        c.order(ByteOrder.nativeOrder());
        mTexVertexBuffer = c.asFloatBuffer();
        mTexVertexBuffer.put(this.textures);
        mTexVertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                VERTEX_SHADER);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                FRAGMENT_SHADER);
        //创建一个空的OpenGLES程序
        cylinderProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(cylinderProgram, vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(cylinderProgram, fragmentShader);
        //连接到着色器程序
        GLES20.glLinkProgram(cylinderProgram);


        GLES20.glEnable(GL10.GL_BLEND);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void setCylinder(Bitmap bitmap){
        mTextureBean = TextureHelper.loadTexture(MainActivity.Main_Context, bitmap);
    }

    private float[] setPositions() {
        ArrayList<Float> positions_list = new ArrayList<>();
        ArrayList<Float> texture_list = new ArrayList<>();
        for (float angdeg = 0; Math.ceil(angdeg) < 360; angdeg += (360f / n)) {
            double angrad = Math.toRadians(angdeg);
            double angradNext = Math.toRadians(angdeg + (360f / n));
            positions_list.add((float) (this.mCylinderCenter[0] - this.mRadio * Math.sin(angrad)));
            positions_list.add(mCylinderCenter[1] - mHeight / 2);
            positions_list.add((float) (this.mCylinderCenter[2] - this.mRadio * Math.cos(angrad)));

            texture_list.add((float) (angrad / (2 * Math.PI)));
            texture_list.add(1f);

            positions_list.add((float) (this.mCylinderCenter[0] - this.mRadio * Math.sin(angradNext)));
            positions_list.add((float) (mCylinderCenter[1] + mHeight / 2));
            positions_list.add((float) (this.mCylinderCenter[2] - this.mRadio * Math.cos(angradNext)));

            texture_list.add((float) (angradNext / (2 * Math.PI)));
            texture_list.add(0f);

            positions_list.add((float) (this.mCylinderCenter[0] - this.mRadio * Math.sin(angrad)));
            positions_list.add((float) (mCylinderCenter[1] + mHeight / 2));
            positions_list.add((float) (this.mCylinderCenter[2] - this.mRadio * Math.cos(angrad)));

            texture_list.add((float) (angrad / (2 * Math.PI)));
            texture_list.add(0f);

            positions_list.add((float) (this.mCylinderCenter[0] - this.mRadio * Math.sin(angrad)));
            positions_list.add((float) (mCylinderCenter[1] - mHeight / 2));
            positions_list.add((float) (this.mCylinderCenter[2] - this.mRadio * Math.cos(angrad)));

            texture_list.add((float) (angrad / (2 * Math.PI)));
            texture_list.add(1f);

            positions_list.add((float) (this.mCylinderCenter[0] - this.mRadio * Math.sin(angradNext)));
            positions_list.add((float) (mCylinderCenter[1] - mHeight / 2));
            positions_list.add((float) (this.mCylinderCenter[2] - this.mRadio * Math.cos(angradNext)));

            texture_list.add((float) (angradNext / (2 * Math.PI)));
            texture_list.add(1f);

            positions_list.add((float) (this.mCylinderCenter[0] - this.mRadio * Math.sin(angradNext)));
            positions_list.add((float) (mCylinderCenter[1] + mHeight / 2));
            positions_list.add((float) (this.mCylinderCenter[2] - this.mRadio * Math.cos(angradNext)));

            texture_list.add((float) (angradNext / (2 * Math.PI)));
            texture_list.add(0f);
        }
        float[] d = new float[positions_list.size()];
        for (int i = 0; i < d.length; i++) {
            d[i] = positions_list.get(i);
        }
        float[] t = new float[texture_list.size()];
        for (int i = 0; i < t.length; i++) {
            t[i] = texture_list.get(i);
        }
        this.textures = t;
        return d;
    }

    public void drawSelf(float[] mMVPMatrix) {

        int mMatrix = GLES20.glGetUniformLocation(cylinderProgram, "u_Matrix");
        int mPositionHandle = GLES20.glGetAttribLocation(cylinderProgram, "a_Position");
        mATexCoordLocation = GLES20.glGetAttribLocation(cylinderProgram, "a_TexCoord");
        Texture = GLES20.glGetUniformLocation(cylinderProgram, "u_TextureUnit");
        GLES20.glUseProgram(cylinderProgram);
        GLES20.glEnableVertexAttribArray(mATexCoordLocation);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glUniform1i(Texture, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureBean.getTextureId());

        GLES20.glUniformMatrix4fv(mMatrix, 1, false, mMVPMatrix, 0);
        GLES20.glVertexAttribPointer(mATexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mTexVertexBuffer);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, this.textures.length / 2);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
