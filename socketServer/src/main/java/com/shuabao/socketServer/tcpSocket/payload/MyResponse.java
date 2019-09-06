
package com.shuabao.socketServer.tcpSocket.payload;


import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.protocal.Status;

/**
 * Provider's response data.
 *
 * 响应信息载体.
 *
 *
 */
public class MyResponse {

    private final ResponsePayload payload;     // 响应bytes/stream
    private MessageWrapper message;               // 响应对象

    public MyResponse(long id) {
        payload = new ResponsePayload(id);
    }

    public MyResponse(ResponsePayload payload) {
        this.payload = payload;
    }

    public ResponsePayload payload() {
        return payload;
    }

    public long id() {
        return payload.id();
    }

    public byte status() {
        return payload.status();
    }

    public void status(byte status) {
        payload.status(status);
    }

    public void status(Status status) {
        payload.status(status.value());
    }

    public MessageWrapper message() {
        return message;
    }

    public void message(MessageWrapper message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "JResponse{" +
                "status=" + Status.parse(status()) +
                ", id=" + id() +
                ", message=" + message +
                '}';
    }
}
