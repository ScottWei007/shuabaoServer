
package com.shuabao.socketServer.tcpSocket.server;

import com.shuabao.core.base.ReturnCode;
import com.shuabao.socketServer.exception.IoSignals;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;
import com.shuabao.socketServer.tcpSocket.payload.ResponsePayload;
import com.shuabao.socketServer.tcpSocket.processor.NettyChannel;
import com.shuabao.socketServer.tcpSocket.protocal.ProtocolHeader;
import com.shuabao.socketServer.tcpSocket.protocal.Status;
import com.shuabao.socketServer.tcpSocket.serialization.InputBuf;
import com.shuabao.socketServer.tcpSocket.serialization.OutputBuf;
import com.shuabao.socketServer.tcpSocket.serialization.Serializer;
import com.shuabao.socketServer.tcpSocket.serialization.SerializerFactory;
import com.shuabao.socketServer.util.Signal;
import com.shuabao.socketServer.util.SystemClock;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.io.InputStream;
import java.nio.ByteBuffer;
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
 * + 1 // 消息标志位, 表示消息类型request/response/heartbeat等
 * + 1 // 状态位, 设置请求响应状态
 * + 8 // 消息 id, long 类型, 未来jupiter可能将id限制在48位, 留出高地址的16位作为扩展字段
 * + 4 // 消息体 body 长度, int 类型
 * </pre>
 *
 */
public class LowCopyProtocolDecoder extends ReplayingDecoder<LowCopyProtocolDecoder.State> {

    // 协议体最大限制, 默认5M
    private static final int MAX_BODY_SIZE = 1024 * 1024 * 5;

    /**
     * Cumulate {@link ByteBuf}s by add them to a CompositeByteBuf and so do no memory copy whenever possible.
     * Be aware that CompositeByteBuf use a more complex indexing implementation so depending on your use-case
     * and the decoder implementation this may be slower then just use the {@link #MERGE_CUMULATOR}.
     */
    private static final boolean USE_COMPOSITE_BUF = false;

    public LowCopyProtocolDecoder() {
        super(State.MAGIC);
        if (USE_COMPOSITE_BUF) {
            super.setCumulator(super.COMPOSITE_CUMULATOR);
        }
    }

    // 协议头
    private final ProtocolHeader header = new ProtocolHeader();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        switch (super.state()) {
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
                    case ProtocolHeader.HEARTBEAT://心跳數據,BODY必須爲空,不然影響後面的數據
                        break;
                    case ProtocolHeader.AUTH: {//認證
                        RequestPayload request = doDecode(in);
                        out.add(request);
                        break;
                    }
                    case ProtocolHeader.RECONNECT: {//重鏈
                        RequestPayload request = doDecode(in);
                        out.add(request);
                        break;
                    }
                    case ProtocolHeader.REQUEST: {//請求
                        RequestPayload request = doDecode(in);
                        NettyChannel nettyChannel = NettyChannel.attachChannel(ctx.channel());
                       /* if(nettyChannel.getUid() == 0) {//登錄認證,必須在in.readRetainedSlice讀出數據之後,不然影響後面的數據
                            unAuthResponse(nettyChannel);
                            request.clear();//釋放數據
                            break;
                        }*/
                        out.add(request);
                        break;
                    }
                    case ProtocolHeader.RESPONSE: {//響應
                        int length = checkBodySize(header.bodySize());
                        ByteBuf bodyByteBuf = in.readRetainedSlice(length);
                        ResponsePayload response = new ResponsePayload(header.id());
                        response.status(header.status());
                        response.setInputBuf(new NettyInputBuf(bodyByteBuf));
                        out.add(response);
                        break;
                    }
                    default:
                        throw IoSignals.ILLEGAL_TYPE;
                }
                checkpoint(State.MAGIC);
        }
    }

    private final RequestPayload doDecode(ByteBuf in) throws Exception{
        int length = checkBodySize(header.bodySize());
        ByteBuf bodyByteBuf = in.readRetainedSlice(length);
        RequestPayload request = new RequestPayload(header.id());
        request.setTimestamp(SystemClock.millisClock().now());
        request.setInputBuf(new NettyInputBuf(bodyByteBuf));
        return request;
    }

    private final void unAuthResponse(NettyChannel nettyChannel) {
        MessageWrapper message = new MessageWrapper();
        message.setReturnInfo(ReturnCode.USER_NOT_AUTH, message.getLang());//默認簡體中文
        Serializer serializer = SerializerFactory.getInstance();
        OutputBuf outputBuf = serializer.writeObject(nettyChannel.allocOutputBuf(), message);
        ResponsePayload responsePayload = new ResponsePayload(0);
        responsePayload.setOutputBuf(outputBuf);
        responsePayload.status(Status.UNAUTH.value());//未認證
        nettyChannel.write(responsePayload);
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

    static final class NettyInputBuf implements InputBuf {

        private final ByteBuf byteBuf;

        NettyInputBuf(ByteBuf byteBuf) {
            this.byteBuf = byteBuf;
        }

        @Override
        public InputStream inputStream() {
            return new ByteBufInputStream(byteBuf); // should not be called more than once
        }

        @Override
        public ByteBuffer nioByteBuffer() {
            return byteBuf.nioBuffer(); // should not be called more than once
        }

        @Override
        public int size() {
            return byteBuf.readableBytes();
        }

        @Override
        public boolean hasMemoryAddress() {
            return byteBuf.hasMemoryAddress();
        }

        @Override
        public boolean release() {
            return byteBuf.release();
        }
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
