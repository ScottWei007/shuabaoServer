
package com.shuabao.socketServer.tcpSocket.server;

import com.shuabao.socketServer.tcpSocket.payload.PayloadHolder;
import com.shuabao.socketServer.tcpSocket.protocal.Status;
import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;
import com.shuabao.socketServer.tcpSocket.payload.ResponsePayload;
import com.shuabao.socketServer.tcpSocket.protocal.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * <pre>
 * **************************************************************************************************
 *                                          Protocol
 *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *       2   │   1   │    1   │     8     │      4      │
 *  ├ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┤
 *           │       │        │           │             │
 *  │  MAGIC   Type    Status   Invoke Id    Body Size                    Body Content              │
 *           │       │        │           │             │
 *  └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 *
 * 消息头16个字节定长
 * = 2 // magic = (short) 0xbabe
 * + 1 // 消息标志位, 用来表示消息类型request/response/heartbeat
 * + 1 // 状态位, 设置请求响应状态
 * + 8 // 消息 id, long 类型, 未来jupiter可能将id限制在48位, 留出高地址的16位作为扩展字段
 * + 4 // 消息体 body 长度, int 类型
 * </pre>
 *
 *
 */
@ChannelHandler.Sharable
public class ProtocolEncoder extends MessageToByteEncoder<PayloadHolder> {

    @Override
    protected void encode(ChannelHandlerContext ctx, PayloadHolder msg, ByteBuf out) throws Exception {
        if (msg instanceof RequestPayload) {
            doEncodeRequest((RequestPayload) msg, out);
        } else if (msg instanceof ResponsePayload) {
            doEncodeResponse((ResponsePayload) msg, out);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, PayloadHolder msg, boolean preferDirect) throws Exception {
        if (preferDirect) {
            return ctx.alloc().ioBuffer(ProtocolHeader.HEADER_SIZE + msg.size());
        } else {
            return ctx.alloc().heapBuffer(ProtocolHeader.HEADER_SIZE + msg.size());
        }
    }

    private void doEncodeRequest(RequestPayload request, ByteBuf out) {
        long invokeId = request.getInvokeId();
        byte[] bytes = request.getBytes();
        int length = bytes.length;

        out.writeShort(ProtocolHeader.MAGIC)
                .writeByte(ProtocolHeader.REQUEST)
                .writeByte(Status.REQUEST.value())
                .writeLong(invokeId)
                .writeInt(length)
                .writeBytes(bytes);
    }

    private void doEncodeResponse(ResponsePayload response, ByteBuf out) {
        byte status = response.status();
        long invokeId = response.id();
        byte[] bytes = response.getBytes();
        int length = bytes.length;

        out.writeShort(ProtocolHeader.MAGIC)
                .writeByte(ProtocolHeader.RESPONSE)
                .writeByte(status)
                .writeLong(invokeId)
                .writeInt(length)
                .writeBytes(bytes);
    }
}
