package com.shuabao.core.rpc.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.shuabao.core.rpc.bean.ServerEntity;
import com.shuabao.core.rpc.handler.RpcHandler;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by Scott Wei on 9/12/2018.
 */
public class ProductServerTask<T extends RpcHandler> implements Runnable{
    private ServerEntity serverEntity;
    private Class<T> itf;
    private List<MethodConfig> methodConfigs;
    private T handler;
    private Thread thread;

    public ProductServerTask(String path, String url, Class<T> itf) {
       this(path, url, itf, null);
    }

    public ProductServerTask(String path, String url, Class<T> itf, List<MethodConfig> methodConfigs) {
        this.itf = itf;
        this.methodConfigs = methodConfigs;
        serverEntity = new ServerEntity(path, url);
        checkThread();
    }

    private synchronized final void checkThread() {
        if(Objects.isNull(thread)) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void updataUrl(String url) {
        serverEntity.updateUrl(url);
    }

    public T getHandler () {
        return handler;
    }

    public boolean isConnected() {
        return Objects.nonNull(handler);
    }

    public String getPath() {
        return this.serverEntity.getPath();
    }

    public String getIp(){
        return this.serverEntity.getIp();
    }

    public int getSort() {
        return this.serverEntity.getSort();
    }

    public String getName(){
        return this.serverEntity.getName();
    }

    public String getServerId(){
        return this.serverEntity.getServerId();
    }

    public void reloadHandler() {
        if(Objects.isNull(handler)) {
            RpcInvoker.resetDubboRpc(serverEntity.getIp(), serverEntity.getPort(), itf);
            checkThread();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                T handler = RpcInvoker.getDubboRpc(serverEntity.getIp(), serverEntity.getPort(), itf, methodConfigs);
                if(Objects.nonNull(handler)) {
                    this.handler = handler;
                    thread = null;
                    break;
                }
                TimeUnit.SECONDS.sleep(1);
            }catch (Exception e) {

            }
        }
    }

    @Override
    public String toString() {
        return serverEntity.toString();
    }
}
