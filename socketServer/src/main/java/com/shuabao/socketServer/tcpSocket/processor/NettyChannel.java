package com.shuabao.socketServer.tcpSocket.processor;

import com.shuabao.socketServer.tcpSocket.serialization.OutputBuf;
import com.shuabao.socketServer.tcpSocket.protocal.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;


import java.io.OutputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * 对Netty {@link Channel} 的包装, 通过静态方法 {@link #attachChannel(Channel)} 获取一个实例,
 * {@link NettyChannel} 实例构造后会attach到对应 {@link Channel} 上, 不需要每次创建.
 *

 */
public class NettyChannel implements IChannel {

    private static final AttributeKey<NettyChannel> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");

    /**
     * Returns the {@link NettyChannel} for given {@link Channel}, this method never return null.
     */
    public static NettyChannel attachChannel(Channel channel) {
        Attribute<NettyChannel> attr = channel.attr(NETTY_CHANNEL_KEY);
        NettyChannel nChannel = attr.get();
        if (nChannel == null) {
            NettyChannel newNChannel = new NettyChannel(channel);
            nChannel = attr.setIfAbsent(newNChannel);
            if (nChannel == null) {
                nChannel = newNChannel;
            }
        }
        return nChannel;
    }

    private final Channel channel;
    private int uid;
    private final AdaptiveOutputBufAllocator.Handle allocHandle = AdaptiveOutputBufAllocator.DEFAULT.newHandle();

    private NettyChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel channel() {
        return channel;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String id() {
        return channel.id().asShortText(); // 注意这里的id并不是全局唯一, 单节点中是唯一的
    }

    @Override
    public boolean isActive() {
        return channel.isActive();
    }

    @Override
    public boolean inIoThread() {
        return channel.eventLoop().inEventLoop();
    }

    @Override
    public SocketAddress localAddress() {
        return channel.localAddress();
    }

    @Override
    public SocketAddress remoteAddress() {
        return channel.remoteAddress();
    }

    @Override
    public boolean isWritable() {
        return channel.isWritable();
    }

    @Override
    public boolean isMarkedReconnect() {
        //有修改
       return false;
    }

    @Override
    public boolean isAutoRead() {
        return channel.config().isAutoRead();
    }

    @Override
    public void setAutoRead(boolean autoRead) {
        channel.config().setAutoRead(autoRead);
    }

    @Override
    public IChannel close() {
        channel.close();
        return this;
    }

    @Override
    public IChannel close(final IFutureListener<IChannel> listener) {
        final IChannel iChannel = this;
        channel.close().addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    listener.operationSuccess(iChannel);
                } else {
                    listener.operationFailure(iChannel, future.cause());
                }
            }
        });
        return iChannel;
    }

    @Override
    public IChannel write(Object msg) {
        channel.writeAndFlush(msg, channel.voidPromise());
        return this;
    }

    @Override
    public IChannel write(Object msg, final IFutureListener<IChannel> listener) {
        final IChannel iChannel = this;
        channel.writeAndFlush(msg)
                .addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            listener.operationSuccess(iChannel);
                        } else {
                            listener.operationFailure(iChannel, future.cause());
                        }
                    }
                });
        return iChannel;
    }

    @Override
    public OutputBuf allocOutputBuf() {
        return new NettyOutputBuf(allocHandle, channel.alloc());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NettyChannel && channel.equals(((NettyChannel) obj).channel));
    }

    @Override
    public int hashCode() {
        return channel.hashCode();
    }

    @Override
    public String toString() {
        return channel.toString();
    }

    static final class NettyOutputBuf implements OutputBuf {

        private final AdaptiveOutputBufAllocator.Handle allocHandle;
        private final ByteBuf byteBuf;
        private ByteBuffer nioByteBuffer;

        public NettyOutputBuf(AdaptiveOutputBufAllocator.Handle allocHandle, ByteBufAllocator alloc) {
            this.allocHandle = allocHandle;
            byteBuf = allocHandle.allocate(alloc);

            byteBuf.ensureWritable(ProtocolHeader.HEADER_SIZE)
                    // reserved 16-byte protocol header location
                    .writerIndex(byteBuf.writerIndex() + ProtocolHeader.HEADER_SIZE);
        }

        @Override
        public OutputStream outputStream() {
            return new ByteBufOutputStream(byteBuf); // should not be called more than once
        }

        @Override
        public ByteBuffer nioByteBuffer(int minWritableBytes) {
            if (minWritableBytes < 0) {
                minWritableBytes = byteBuf.writableBytes();
            }

            if (nioByteBuffer == null) {
                nioByteBuffer = newNioByteBuffer(byteBuf, minWritableBytes);
            }

            if (nioByteBuffer.remaining() >= minWritableBytes) {
                return nioByteBuffer;
            }

            int position = nioByteBuffer.position();
            nioByteBuffer = newNioByteBuffer(byteBuf, position + minWritableBytes);
            nioByteBuffer.position(position);
            return nioByteBuffer;
        }

        @Override
        public int size() {
            if (nioByteBuffer == null) {
                return byteBuf.readableBytes();
            }
            return Math.max(byteBuf.readableBytes(), nioByteBuffer.position());
        }

        @Override
        public boolean hasMemoryAddress() {
            return byteBuf.hasMemoryAddress();
        }

        @Override
        public Object backingObject() {
            int actualWroteBytes = byteBuf.writerIndex();
            if (nioByteBuffer != null) {
                actualWroteBytes += nioByteBuffer.position();
            }

            allocHandle.record(actualWroteBytes);

            return byteBuf.writerIndex(actualWroteBytes);
        }

        private static ByteBuffer newNioByteBuffer(ByteBuf byteBuf, int writableBytes) {
            return byteBuf
                    .ensureWritable(writableBytes)
                    .nioBuffer(byteBuf.writerIndex(), byteBuf.writableBytes());
        }
    }
}
