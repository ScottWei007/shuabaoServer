package com.shuabao.socketServer.tcpSocket.serialization;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public interface InputBuf {
    InputStream inputStream();

    ByteBuffer nioByteBuffer();

    int size();

    boolean hasMemoryAddress();

    boolean release();
}
