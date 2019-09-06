package com.shuabao.socketServer.tcpSocket.excutor;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public class ThreadPoolExecutorFactory implements ExecutorFactory {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolExecutorFactory.class);

    @Override
    public CloseableExecutor newExecutor(String name) {
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() << 1,
                512,
                120L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(32768),
                new DefaultThreadFactory(name,Thread.NORM_PRIORITY));

        return new CloseableExecutor() {

            @Override
            public void execute(Runnable r) {
                executor.execute(r);
            }

            @Override
            public void shutdown() {
                logger.warn("ThreadPoolExecutorFactory#{} shutdown.", executor);
                executor.shutdownNow();
            }
        };
    }
}
