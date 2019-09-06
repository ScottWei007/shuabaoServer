

package com.shuabao.socketServer.tcpSocket.payload;

/**
 * 响应的消息体bytes/stream载体, 避免在IO线程中序列化/反序列化, 不关注消息体的对象结构.

 */
public class ResponsePayload extends PayloadHolder {

    // 用于映射 <id, request, response> 三元组
    private final long id; // request.invokeId
    private byte status;

    public ResponsePayload(long id) {
        this.id = id;
    }

    public long id() {
        return id;
    }

    public byte status() {
        return status;
    }

    public void status(byte status) {
        this.status = status;
    }
}
