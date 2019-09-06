package com.shuabao.socketServer.tcpSocket.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public final class MyChannelInitializer extends ChannelInitializer<SocketChannel>{
    private final LowCopyProtocolEncoder lowCopyProtocolEncoder;
    private final BussinessHandler bussinessHandler;

    public MyChannelInitializer(LowCopyProtocolEncoder lowCopyProtocolEncoder, BussinessHandler bussinessHandler) {
        this.lowCopyProtocolEncoder = lowCopyProtocolEncoder;
        this.bussinessHandler = bussinessHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(60,0,0));
        pipeline.addLast(new IdleStateTriggerHandler());
        pipeline.addLast(new LowCopyProtocolDecoder());
//        pipeline.addLast(new ProtocolDecoder());
        pipeline.addLast(lowCopyProtocolEncoder);//使用同一個
        pipeline.addLast(bussinessHandler);//使用同一個
    }

}
