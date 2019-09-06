

package com.shuabao.socketServer.tcpSocket.processor;

/**
 * 服务限流, 限流规则在服务端执行, 这可能会有一点点性能开销.
 */
public interface FlowController<T> {

    ControlResult flowControl(T t);
}
