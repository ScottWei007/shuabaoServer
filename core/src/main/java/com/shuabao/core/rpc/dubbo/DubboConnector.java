package com.shuabao.core.rpc.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.shuabao.core.rpc.handler.RpcHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class DubboConnector<T extends RpcHandler> {
    public static final Logger log = LoggerFactory.getLogger(DubboConnector.class);

    private ReferenceConfig<T> referenceConfig;
    private T handler;

    public DubboConnector(String name, String ip, int port, Class<T> itf, List<MethodConfig> methods) throws Exception {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(name);
        // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        referenceConfig = new ReferenceConfig();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setUrl("dubbo://"+ip+":"+port+"/");
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setInterface(itf);
        referenceConfig.setTimeout(10000);
        if(Objects.nonNull(methods)){
            referenceConfig.setMethods(methods);
        }
        try {
            handler = referenceConfig.get();
        }catch (Exception e) {
            log.error("=========DubboConnector error :" + e.getMessage());
            referenceConfig.destroy();
            throw new Exception();
        }
    }

    public T getHandler(){
        return handler;
    }

    public void destroy (){
        this.handler = null;
        if(Objects.nonNull(referenceConfig)){
            referenceConfig.destroy();
        }
    }
}
