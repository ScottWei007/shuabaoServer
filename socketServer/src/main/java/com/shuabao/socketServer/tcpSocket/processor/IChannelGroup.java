

package com.shuabao.socketServer.tcpSocket.processor;


import java.util.List;

/**
 * Based on the same uid of the channel group.
 *
 */
public interface IChannelGroup {

    List<NettyChannel> channels();

    boolean isEmpty();

    boolean add(NettyChannel nettyChannel);

    boolean remove(NettyChannel nettyChannel);

    int size();
}
