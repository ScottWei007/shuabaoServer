package com.shuabao.core.rpc.zookeeper;

import com.shuabao.core.rpc.dubbo.ProductServerTask;
import com.shuabao.core.rpc.handler.SocketServiceHandler;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Scott Wei on 9/13/2018.
 */
public class SocketServiceConsumeListener extends AbstractConsumeListener{
    private static volatile SocketServiceConsumeListener instance;
    private static final Lock lock = new ReentrantLock();

    public static SocketServiceConsumeListener getInstance() {
        if (Objects.isNull(instance)) {
            try {
                lock.lock();
                if (Objects.isNull(instance)) {
                    instance = new SocketServiceConsumeListener();
                }
            }finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private SocketServiceConsumeListener() {
        //监听服务的路径
        ZookeeperManager.getInstance().addListener(ZookeeperManager.ROOT + ZookeeperManager.LINESEPARATOR + ZookeeperManager.SOCKETSERVER, this);
    }

    @Override
    protected ProductServerTask createServer(String path, String url) {
        return new ProductServerTask(path, url, SocketServiceHandler.class);
    }
}
