package com.shuabao.core.rpc.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.shuabao.core.rpc.handler.RpcHandler;

public class DubboFactory {

    public static <T extends RpcHandler> void endpointReg(String name ,int port, Class<T> itf, T clz) {
        // 当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(name);
        // 服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(port);
        protocolConfig.setThreads(200);
        // 服务提供者暴露服务配置,注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口,
        //此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        ServiceConfig<T> serviceConfig = new ServiceConfig();
        serviceConfig.setRegister(false);
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setProtocol(protocolConfig);// 多个协议可以用setProtocols()
        serviceConfig.setInterface(itf);
        serviceConfig.setRef(clz);
        serviceConfig.setVersion("1.0.0");
        serviceConfig.export();// 暴露及注册服务
    }
}
