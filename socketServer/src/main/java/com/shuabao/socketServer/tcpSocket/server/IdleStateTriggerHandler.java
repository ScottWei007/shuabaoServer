package com.shuabao.socketServer.tcpSocket.server;

import com.shuabao.socketServer.manager.ClientChannelManager;
import com.shuabao.socketServer.tcpSocket.payload.Heartbeats;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.processor.IChannel;
import com.shuabao.socketServer.tcpSocket.processor.IFutureListener;
import com.shuabao.socketServer.tcpSocket.processor.NettyChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public class IdleStateTriggerHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleState idleState = ((IdleStateEvent) evt).state();
            if(idleState == IdleState.READER_IDLE) {
                //用戶已經失去連接，刪除活躍用戶
                NettyChannel nettyChannel = NettyChannel.attachChannel(ctx.channel());
                nettyChannel.write(Heartbeats.heartbeatContent(), new IFutureListener<IChannel>() {
                    @Override
                    public void operationSuccess(IChannel channel) throws Exception {
                    }
                    @Override
                    public void operationFailure(IChannel channel, Throwable cause) throws Exception {
                        channel.close();
                    }
                });




                System.out.println("==========trigger reader idle");

                MessageWrapper messageWrapper = new MessageWrapper();
                messageWrapper.putData("msg","推送所有");
                messageWrapper.putData("code","1");
                ClientChannelManager.getInstance().notifyAllOnline(messageWrapper);

                MessageWrapper message = new MessageWrapper();
                message.putData("msg","推送推送一個");
                message.putData("code","1");
                ClientChannelManager.getInstance().notifyOneOnline(7777, message);
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
