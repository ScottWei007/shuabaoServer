

package com.shuabao.socketServer.tcpSocket.processor;

import java.util.EventListener;


public interface IFutureListener<C> extends EventListener {

    void operationSuccess(C c) throws Exception;

    void operationFailure(C c, Throwable cause) throws Exception;
}
