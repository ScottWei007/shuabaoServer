package com.shuabao.socketServer.tcpSocket.server;

import com.shuabao.core.entity.UserSession;
import com.shuabao.core.util.RedisUtil;
import com.shuabao.socketServer.manager.ClientChannelManager;
import com.shuabao.socketServer.tcpSocket.processor.ProviderProcessor;
import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;
import com.shuabao.socketServer.tcpSocket.processor.IChannel;
import com.shuabao.socketServer.tcpSocket.processor.NettyChannel;
import com.shuabao.socketServer.util.Signal;
import io.netty.channel.*;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.Objects;

import static com.shuabao.socketServer.util.StackTraceUtil.stackTrace;


/**
 *業務處理器
 */
@ChannelHandler.Sharable
public class BussinessHandler extends ChannelInboundHandlerAdapter {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(BussinessHandler.class);
    private final ProviderProcessor processor;

    public BussinessHandler(ProviderProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (msg instanceof RequestPayload) {
            IChannel iChannel = NettyChannel.attachChannel(ch);
            try {
                processor.handleRequest(iChannel, (RequestPayload) msg);
            } catch (Exception e) {
                logger.error("An exception was caught while processing request: {}, {}.", iChannel.remoteAddress(), e.getMessage());
                processor.handleException(iChannel, (RequestPayload) msg);
            }
        } else {
            logger.warn("Unexpected message type received: {}, channel: {}.", msg.getClass(), ch);
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Connects with {} , the online num {}.", ctx.channel(),ClientChannelManager.getInstance().totalOnline());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannel nettyChannel = NettyChannel.attachChannel(ctx.channel());
        int uid = nettyChannel.getUid();
        if(uid != 0 ) {
            //設置為未認證，標記離綫
            nettyChannel.setUid(0);
            //刪除在綫人數
            ClientChannelManager.getInstance().remove(uid);
            //設置緩存離綫狀態
            UserSession session = RedisUtil.getUserSession(String.valueOf(uid), null);
            if(Objects.nonNull(session)) {
                session.setOnlive("2");
                session.setHost("");
                session.setPort("");
                RedisUtil.setUserSession(session);
            }
        }
        logger.warn("Disconnects with {} , the online num {}.", ctx.channel(),ClientChannelManager.getInstance().totalOnline());
        super.channelInactive(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        ChannelConfig config = ch.config();
        // 高水位线: ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK
        // 低水位线: ChannelOption.WRITE_BUFFER_LOW_WATER_MARK
        if (!ch.isWritable()) {
            // 当前channel的缓冲区(OutboundBuffer)大小超过了WRITE_BUFFER_HIGH_WATER_MARK
            if (logger.isWarnEnabled()) {
                logger.warn("{} is not writable, high water mask: {}, the number of flushed entries that are not written yet: {}.",
                        ch, config.getWriteBufferHighWaterMark(), ch.unsafe().outboundBuffer().size());
            }
            config.setAutoRead(false);
        } else {
            // 曾经高于高水位线的OutboundBuffer现在已经低于WRITE_BUFFER_LOW_WATER_MARK了
            if (logger.isWarnEnabled()) {
                logger.warn("{} is writable(rehabilitate), low water mask: {}, the number of flushed entries that are not written yet: {}.",
                        ch, config.getWriteBufferLowWaterMark(), ch.unsafe().outboundBuffer().size());
            }
            config.setAutoRead(true);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel ch = ctx.channel();
        if (cause instanceof Signal) {
            logger.error("I/O signal was caught: {}, force to close channel: {}.", ((Signal) cause).name(), ch);
            ch.close();
        } else if (cause instanceof IOException) {
            logger.error("An I/O exception was caught: {}, force to close channel: {}.", stackTrace(cause), ch);
            ch.close();
        } else if (cause instanceof DecoderException) {
            logger.error("Decoder exception was caught: {}, force to close channel: {}.", stackTrace(cause), ch);
            ch.close();
        } else {
            logger.error("Unexpected exception was caught: {}, channel: {}.", stackTrace(cause), ch);
        }
    }

    public ProviderProcessor processor() {
        return processor;
    }

}
