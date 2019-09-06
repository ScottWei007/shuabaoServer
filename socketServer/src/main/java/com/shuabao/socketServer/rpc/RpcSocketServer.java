package com.shuabao.socketServer.rpc;

import com.shuabao.core.manager.EnvironmentManager;
import com.shuabao.core.rpc.dubbo.DubboFactory;
import com.shuabao.core.rpc.handler.SocketServiceHandler;
import com.shuabao.core.rpc.zookeeper.ZookeeperManager;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class RpcSocketServer {
    public static final Logger log = LoggerFactory.getLogger(RpcSocketServer.class);

    public static void start() {
        try {
            //向dubbo注册服务
            DubboFactory.endpointReg(EnvironmentManager.getEnvironment().getProperty("rpcserver.name"),
                    NumberUtils.toInt(EnvironmentManager.getEnvironment().getProperty("rpcserver.port")),
                    SocketServiceHandler.class,
                    new SocketServiceHandle());
            //向注册中心zk保存服务器地址
            StringBuilder sb = new StringBuilder().append("dubbo://")
                    .append(EnvironmentManager.getEnvironment().getProperty("rpcserver.ip"))
                    .append(":")
                    .append(EnvironmentManager.getEnvironment().getProperty("rpcserver.port"))
                    .append("?name=")
                    .append(EnvironmentManager.getEnvironment().getProperty("rpcserver.id"))
                    .append("&sort=0");
            ZookeeperManager.getInstance().saveRegDatas(ZookeeperManager.SOCKETSERVER,
                    EnvironmentManager.getEnvironment().getProperty("rpcserver.id"),
                    sb.toString());
        } catch (RemoteException e) {
            log.error("=========RpcSocketServer start error: " + e.getMessage());
        }
    }
}
