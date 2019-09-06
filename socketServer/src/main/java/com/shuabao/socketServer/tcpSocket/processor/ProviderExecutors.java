

package com.shuabao.socketServer.tcpSocket.processor;


import com.shuabao.socketServer.tcpSocket.excutor.CloseableExecutor;
import com.shuabao.socketServer.tcpSocket.excutor.ExecutorFactory;
import com.shuabao.socketServer.tcpSocket.excutor.ThreadPoolExecutorFactory;

public class ProviderExecutors {

    private static final CloseableExecutor executor;

    static {
        ExecutorFactory factory = new ThreadPoolExecutorFactory();
        executor = factory.newExecutor( "shuabao.netty.processor");
    }

    public static CloseableExecutor executor() {
        return executor;
    }

    public static void execute(Runnable r) {
        executor.execute(r);
    }
}
