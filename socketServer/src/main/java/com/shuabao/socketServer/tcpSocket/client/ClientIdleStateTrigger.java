
package com.shuabao.socketServer.tcpSocket.client;

import com.shuabao.socketServer.tcpSocket.payload.Heartbeats;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**

 */
@ChannelHandler.Sharable
public class ClientIdleStateTrigger extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
// write heartbeat to server
//                ctx.writeAndFlush(Heartbeats.heartbeatContent());


               /*IChannel iChannel = NettyChannel.attachChannel(ctx.channel());
                MessageWrapper messageWrapper = new MessageWrapper();
                messageWrapper.putParams("msg","業務");
                messageWrapper.putParams("code","1");
                Serializer serializer = SerializerFactory.getInstance();
                OutputBuf outputBuf = serializer.writeObject(iChannel.allocOutputBuf(), messageWrapper);
                RequestPayload requestPayload = new RequestPayload(20000);
                requestPayload.setOutputBuf(outputBuf);
                iChannel.write(requestPayload);*/

                System.out.println("======trigger writer idle");

            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
