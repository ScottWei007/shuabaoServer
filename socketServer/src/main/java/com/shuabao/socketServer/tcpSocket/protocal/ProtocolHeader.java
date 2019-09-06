package com.shuabao.socketServer.tcpSocket.protocal;

/**
 * 传输层协议头
 *
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
 * = 2 // magic = (short) 0xbabe                                short类型
 * + 1 // 消息标志位, 用来表示消息类型request/response/heartbeat     byte 类型
 * + 1 // 状态位, 设置请求响应状态                                  byte 类型
 * + 8 // 消息 id,                                              long 类型,
 * + 4 // 消息体 body 长度,                                      int 类型
 *
 *
 */
public class ProtocolHeader {

    /** 协议头长度 */
    public static final int HEADER_SIZE = 16;
    /** Magic */
    public static final short MAGIC = (short) 0xbabe;

    /** Message Code: 0 ~ 9 =================================================================================== */
    public static final byte HEARTBEAT                  = 0;     // Heartbeat
    public static final byte AUTH                       = 1;     // auth用戶認證
    public static final byte RECONNECT                  = 2;     // reconnect重鏈
    public static final byte REQUEST                    = 3;     // Request
    public static final byte RESPONSE                   = 4;     // Response
    public static final byte PUBLISH_SERVICE            = 5;     // 发布服务
    public static final byte PUBLISH_CANCEL_SERVICE     = 6;     // 取消发布服务
    public static final byte SUBSCRIBE_SERVICE          = 7;     // 订阅服务
    public static final byte OFFLINE_NOTICE             = 8;     // 通知下线
    public static final byte ACK                        = 9;     // Acknowledge

    private byte type;       // Message Code


    private byte status;            // 响应状态码
    private long id;                // request.invokeId, 用于映射 <id, request, response> 三元组
    private int bodySize;           // 消息体长度


    public byte type() {
        return type;
    }

    public void type(byte type) {
        this.type = type;
    }

    public byte status() {
        return status;
    }

    public void status(byte status) {
        this.status = status;
    }

    public long id() {
        return id;
    }

    public void id(long id) {
        this.id = id;
    }

    public int bodySize() {
        return bodySize;
    }

    public void bodySize(int bodyLength) {
        this.bodySize = bodyLength;
    }

    @Override
    public String toString() {
        return "JProtocolHeader{" +
                "type=" + type +
                ", status=" + status +
                ", id=" + id +
                ", bodySize=" + bodySize +
                '}';
    }
}
