
package com.shuabao.socketServer.tcpSocket.excutor;


public interface CloseableExecutor {

    void execute(Runnable r);

    void shutdown();
}
