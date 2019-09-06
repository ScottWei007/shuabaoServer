
package com.shuabao.socketServer.tcpSocket.server;

import com.shuabao.socketServer.exception.IoSignals;
import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;
import com.shuabao.socketServer.util.Signal;
import com.shuabao.socketServer.util.SystemClock;
import com.shuabao.socketServer.tcpSocket.payload.ResponsePayload;
import com.shuabao.socketServer.tcpSocket.protocal.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;


import java.util.List;

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

 */
public class ProtocolDecoder extends ReplayingDecoder<ProtocolDecoder.State> {

    // 协议体最大限制, 默认5M
    private static final int MAX_BODY_SIZE = 1024 * 1024 * 5;

    /**
     * Cumulate {@link ByteBuf}s by add them to a CompositeByteBuf and so do no memory copy whenever possible.
     * Be aware that CompositeByteBuf use a more complex indexing implementation so depending on your use-case
     * and the decoder implementation this may be slower then just use the {@link #MERGE_CUMULATOR}.
     */
    private static final boolean USE_COMPOSITE_BUF = false;

    public ProtocolDecoder() {
        super(State.MAGIC);
        if (USE_COMPOSITE_BUF) {
            setCumulator(COMPOSITE_CUMULATOR);
        }
    }

    // 协议头
    private final ProtocolHeader header = new ProtocolHeader();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case MAGIC:
                checkMagic(in.readShort());         // MAGIC
                checkpoint(State.TYPE);
            case TYPE:
                header.type(in.readByte());         // 消息标志位
                checkpoint(State.STATUS);
            case STATUS:
                header.status(in.readByte());       // 状态位
                checkpoint(State.ID);
            case ID:
                header.id(in.readLong());           // 消息id
                checkpoint(State.BODY_SIZE);
            case BODY_SIZE:
                header.bodySize(in.readInt());      // 消息体长度
                checkpoint(State.BODY);
            case BODY:
                switch (header.type()) {
                    case ProtocolHeader.HEARTBEAT:
                        break;
                    case ProtocolHeader.REQUEST: {
                        int length = checkBodySize(header.bodySize());
                        byte[] bytes = new byte[length];
                        in.readBytes(bytes);

                        RequestPayload request = new RequestPayload(header.id());
                        request.setTimestamp(SystemClock.millisClock().now());
                        request.setBytes(bytes);
                        out.add(request);
                        break;
                    }
                    case ProtocolHeader.RESPONSE: {
                        int length = checkBodySize(header.bodySize());
                        byte[] bytes = new byte[length];
                        in.readBytes(bytes);
                        ResponsePayload response = new ResponsePayload(header.id());
                        response.status(header.status());
                        response.setBytes(bytes);
                        out.add(response);
                        break;
                    }
                    default:
                        throw IoSignals.ILLEGAL_TYPE;
                }
                checkpoint(State.MAGIC);
        }
    }

    private static void checkMagic(short magic) throws Signal {
        if (magic != ProtocolHeader.MAGIC) {
            throw IoSignals.ILLEGAL_MAGIC;
        }
    }

    private static int checkBodySize(int size) throws Signal {
        if (size > MAX_BODY_SIZE) {
            throw IoSignals.BODY_TOO_LARGE;
        }
        return size;
    }

    enum State {
        MAGIC,
        TYPE,
        STATUS,
        ID,
        BODY_SIZE,
        BODY
    }
}
