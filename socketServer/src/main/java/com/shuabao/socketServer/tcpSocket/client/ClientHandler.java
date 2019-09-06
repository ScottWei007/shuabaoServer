

package com.shuabao.socketServer.tcpSocket.client;

import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;
import com.shuabao.socketServer.tcpSocket.serialization.Serializer;
import com.shuabao.socketServer.tcpSocket.serialization.SerializerFactory;
import com.shuabao.socketServer.util.Signal;
import com.shuabao.socketServer.util.StackTraceUtil;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.payload.ResponsePayload;
import com.shuabao.socketServer.tcpSocket.serialization.InputBuf;
import io.netty.channel.*;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;


@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();

        if (msg instanceof ResponsePayload) {
            ResponsePayload _responsePayload = (ResponsePayload) msg;
            InputBuf inputBuf = _responsePayload.getInputBuf();
            Serializer serializer = SerializerFactory.getInstance();
            MessageWrapper wrapper = serializer.readObject(inputBuf, MessageWrapper.class);
            _responsePayload.clear();
            System.out.println("客戶端返回id：" + _responsePayload.id() + " 狀態: " + _responsePayload.status() + " 返回信息: " + wrapper);
        }else if (msg instanceof RequestPayload) {
            RequestPayload requestPayload = (RequestPayload) msg;
            InputBuf inputBuf = requestPayload.getInputBuf();
            Serializer serializer = SerializerFactory.getInstance();
            MessageWrapper wrapper = serializer.readObject(inputBuf, MessageWrapper.class);
            requestPayload.clear();
            System.out.println("客戶端返回id：" + requestPayload.getInvokeId() + " 返回信息: " + wrapper);
        } else {
            logger.warn("Unexpected message type received: {}, channel: {}.", msg.getClass(), ch);
            ReferenceCountUtil.release(msg);
        }
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
            logger.error("I/O exception was caught: {}, force to close channel: {}.", StackTraceUtil.stackTrace(cause), ch);

            ch.close();
        } else if (cause instanceof DecoderException) {
            logger.error("Decoder exception was caught: {}, force to close channel: {}.", StackTraceUtil.stackTrace(cause), ch);

            ch.close();
        } else {
            logger.error("Unexpected exception was caught: {}, channel: {}.", StackTraceUtil.stackTrace(cause), ch);
        }
    }
}
