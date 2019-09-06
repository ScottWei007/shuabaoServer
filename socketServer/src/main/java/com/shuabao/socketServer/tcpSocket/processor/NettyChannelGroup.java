
package com.shuabao.socketServer.tcpSocket.processor;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NettyChannelGroup implements IChannelGroup {

    private final int uid;
    private final CopyOnWriteArrayList<NettyChannel> channels = new CopyOnWriteArrayList<>();

    // 连接断开时自动被移除
    private final ChannelFutureListener remover = new ChannelFutureListener() {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            remove(NettyChannel.attachChannel(future.channel()));
        }
    };

    public NettyChannelGroup(int uid) {
        this.uid = uid;
    }


    @Override
    public List<NettyChannel> channels() {
        return new ArrayList((channels));
    }

    @Override
    public boolean isEmpty() {
        return channels.isEmpty();
    }

    @Override
    public boolean add(NettyChannel nettyChannel) {
        boolean added = channels.addIfAbsent(nettyChannel);
        if (added) {
            nettyChannel.channel().closeFuture().addListener(remover);
        }
        return added;
    }

    @Override
    public boolean remove(NettyChannel nettyChannel) {
        return channels.remove(nettyChannel);
    }

    @Override
    public int size() {
        return channels.size();
    }
}
