

package com.shuabao.socketServer.tcpSocket.payload;

import com.shuabao.socketServer.tcpSocket.protocal.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Shared heartbeat content.
 *
 */
public class Heartbeats {

    private static final ByteBuf HEARTBEAT_BUF;

    static {
        ByteBuf buf = Unpooled.buffer(ProtocolHeader.HEADER_SIZE);
        buf.writeShort(ProtocolHeader.MAGIC);
        buf.writeByte(ProtocolHeader.HEARTBEAT); // 心跳包
        buf.writeByte(0);
        buf.writeLong(0);
        buf.writeInt(0);//心跳數據,BODY必須爲空,不然影響後面的其他信息
        HEARTBEAT_BUF = Unpooled.unreleasableBuffer(buf).asReadOnly();
    }

    /**
     * Returns the shared heartbeat content.
     */
    public static ByteBuf heartbeatContent() {
        return HEARTBEAT_BUF.duplicate();
    }
}
