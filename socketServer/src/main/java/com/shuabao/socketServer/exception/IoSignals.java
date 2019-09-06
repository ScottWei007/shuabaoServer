

package com.shuabao.socketServer.exception;


import com.shuabao.socketServer.util.Signal;

public class IoSignals {

    /** 错误的MAGIC */
    public static final Signal ILLEGAL_MAGIC    = Signal.valueOf(IoSignals.class, "ILLEGAL_MAGIC");
    /** 错误的消息标志位 */
    public static final Signal ILLEGAL_TYPE     = Signal.valueOf(IoSignals.class, "ILLEGAL_TYPE");
    /** Read idle 链路检测 */
    public static final Signal READER_IDLE      = Signal.valueOf(IoSignals.class, "READER_IDLE");
    /** Protocol body 太大 */
    public static final Signal BODY_TOO_LARGE   = Signal.valueOf(IoSignals.class, "BODY_TOO_LARGE");
}
