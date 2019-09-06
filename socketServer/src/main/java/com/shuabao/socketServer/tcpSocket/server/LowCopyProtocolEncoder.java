package com.shuabao.socketServer.tcpSocket.server;

import com.shuabao.socketServer.tcpSocket.payload.PayloadHolder;
import com.shuabao.socketServer.tcpSocket.protocal.Status;
import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;
import com.shuabao.socketServer.tcpSocket.payload.ResponsePayload;
import com.shuabao.socketServer.tcpSocket.protocal.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;

import java.util.Objects;

/**
 * Created by Scott Wei on 8/4/2018.
 */
@ChannelHandler.Sharable
public class LowCopyProtocolEncoder extends ChannelOutboundHandlerAdapter{
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf byteBuf = null;
        try{
            if(msg instanceof PayloadHolder) {
                PayloadHolder payloadHolder = (PayloadHolder) msg;
                byteBuf = encode(payloadHolder);
                ctx.write(byteBuf, promise);
                byteBuf = null;
            }else {
                ctx.write(msg, promise);
            }
        }catch (Exception e){
            throw new EncoderException(e);
        }finally {
            if(Objects.nonNull(byteBuf)) {
                byteBuf.release();
            }
        }
    }

    private ByteBuf encode(PayloadHolder payloadHolder) throws Exception{
        if(payloadHolder instanceof RequestPayload) {
            return doEncodeRequest((RequestPayload) payloadHolder);
        }else if(payloadHolder instanceof ResponsePayload) {
            return doEncodeResponse((ResponsePayload) payloadHolder);
        }else {
            throw new IllegalArgumentException();
        }
    }

    private ByteBuf doEncodeRequest(RequestPayload request) {
        long invokeId = request.getInvokeId();
        ByteBuf byteBuf = (ByteBuf) request.getOutputBuf().backingObject();
        int length = byteBuf.readableBytes();
        byteBuf.markWriterIndex();
        byteBuf.writerIndex(byteBuf.writerIndex() - length);
        byteBuf.writeShort(ProtocolHeader.MAGIC)
                .writeByte(ProtocolHeader.REQUEST)
                .writeByte(Status.REQUEST.value())//請求狀態
                .writeLong(invokeId)
                .writeInt(length - ProtocolHeader.HEADER_SIZE);

        byteBuf.resetWriterIndex();
        return byteBuf;
    }

    private ByteBuf doEncodeResponse(ResponsePayload response) {
        byte state = response.status();
        long invokeId = response.id();
        ByteBuf byteBuf = (ByteBuf) response.getOutputBuf().backingObject();
        int length = byteBuf.readableBytes();
        byteBuf.markWriterIndex();
        byteBuf.writerIndex(byteBuf.writerIndex() - length);
        byteBuf.writeShort(ProtocolHeader.MAGIC)
                .writeByte(ProtocolHeader.RESPONSE)
                .writeByte(state)
                .writeLong(invokeId)
                .writeInt(length - ProtocolHeader.HEADER_SIZE);

        byteBuf.resetWriterIndex();
        return byteBuf;
    }

}
