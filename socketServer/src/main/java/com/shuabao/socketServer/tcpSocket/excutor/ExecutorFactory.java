package com.shuabao.socketServer.tcpSocket.excutor;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public interface ExecutorFactory {
    CloseableExecutor newExecutor(String name);
}
