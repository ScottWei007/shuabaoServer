package com.shuabao.socketServer.tcpSocket.processor;

import com.shuabao.socketServer.tcpSocket.serialization.OutputBuf;

import java.net.SocketAddress;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public interface IChannel {
    IFutureListener<IChannel> CLOSE = new IFutureListener<IChannel>() {
        @Override
        public void operationSuccess(IChannel iChannel) throws Exception {
            iChannel.close();
        }

        @Override
        public void operationFailure(IChannel iChannel, Throwable cause) throws Exception {
            iChannel.close();
        }
    };

    String id();

    boolean isActive();

    boolean inIoThread();

    SocketAddress localAddress();

    SocketAddress remoteAddress();

    boolean isWritable();

    boolean isMarkedReconnect();

    boolean isAutoRead();

    void setAutoRead(boolean autoRead);

    IChannel close();

    IChannel close(IFutureListener<IChannel> listener);

    IChannel write(Object msg);

    IChannel write(Object msg, IFutureListener<IChannel> listener);

    OutputBuf allocOutputBuf();
}
