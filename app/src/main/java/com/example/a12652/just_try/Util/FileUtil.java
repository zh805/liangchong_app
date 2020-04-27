package com.example.a12652.just_try.Util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by 12652 on 2020/2/13.
 */

class FileUtil {
    public static FloatBuffer getFloatBuffer(float[] array) {
        //初始化字节缓冲区的大小=数组长度*数组元素大小。float类型的元素大小为Float.SIZE，
        //int类型的元素大小为Integer.SIZE，double类型的元素大小为Double.SIZE。
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * Float.SIZE);
        //以本机字节顺序来修改字节缓冲区的字节顺序
        //OpenGL在底层的实现是C语言，与Java默认的数据存储字节顺序可能不同，即大端小端问题。
        //因此，为了保险起见，在将数据传递给OpenGL之前，需要指明使用本机的存储顺序
        byteBuffer.order(ByteOrder.nativeOrder());
        //根据设置好的参数构造浮点缓冲区
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        //把数组数据写入缓冲区
        floatBuffer.put(array);
        //设置浮点缓冲区的初始位置
        floatBuffer.position(0);
        return floatBuffer;
    }
}
