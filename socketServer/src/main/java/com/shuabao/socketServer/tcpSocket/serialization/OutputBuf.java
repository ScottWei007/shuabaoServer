package com.shuabao.socketServer.tcpSocket.serialization;

import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public interface OutputBuf {
    OutputStream outputStream();

    ByteBuffer nioByteBuffer(int minWritableBytes);

    int size();

    boolean hasMemoryAddress();

    Object backingObject();
}
