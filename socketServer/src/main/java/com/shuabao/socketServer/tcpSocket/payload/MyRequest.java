

package com.shuabao.socketServer.tcpSocket.payload;

import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.serialization.OutputBuf;

/**
 * Consumer's request data.
 *
 * 请求信息载体.

 */
public class MyRequest {

    private final RequestPayload payload;   // 请求bytes/stream
    private MessageWrapper message;          // 请求对象

    public MyRequest() {
        this(new RequestPayload());
    }

    public MyRequest(RequestPayload payload) {
        this.payload = payload;
    }

    public RequestPayload payload() {
        return payload;
    }

    public long invokeId() {
        return payload.getInvokeId();
    }

    public long timestamp() {
        return payload.getTimestamp();
    }

    public void bytes( byte[] bytes) {
        payload.setBytes( bytes);
    }

    public void outputBuf(OutputBuf outputBuf) {
        payload.setOutputBuf(outputBuf);
    }

    public MessageWrapper message() {
        return message;
    }

    public void message(MessageWrapper message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "JRequest{" +
                "invokeId=" + invokeId() +
                ", timestamp=" + timestamp() +
                ", message=" + message +
                '}';
    }
}
