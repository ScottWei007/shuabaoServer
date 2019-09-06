
package com.shuabao.socketServer.tcpSocket.serialization;

import com.shuabao.socketServer.util.InternalThreadLocal;
import io.protostuff.LinkedBuffer;


/**

 */
public final class LinkedBuffers {

    // 复用 LinkedBuffer 中链表头结点 byte[]
    private static final InternalThreadLocal<LinkedBuffer> bufThreadLocal = new InternalThreadLocal<LinkedBuffer>() {

        @Override
        protected LinkedBuffer initialValue() {
            return LinkedBuffer.allocate(512);
        }
    };

    public static LinkedBuffer getLinkedBuffer() {
        return bufThreadLocal.get();
    }

    public static void resetBuf(LinkedBuffer buf) {
        buf.clear();
    }

    private LinkedBuffers() {}
}
