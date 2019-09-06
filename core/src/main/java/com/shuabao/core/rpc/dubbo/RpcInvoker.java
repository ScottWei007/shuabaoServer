package com.shuabao.core.rpc.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.shuabao.core.rpc.handler.RpcHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RpcInvoker {

    private static Map<String, DubboConnector> dubboConnectorMap = new ConcurrentHashMap<>();

  /*  public static <T extends RpcHandler> T getDubboRpc(String ip, int port, Class<T> itf, List<MethodConfig> methods) {
        int times = 4;
        while (times -- > 0) {
            T handler = connectDubboRpc(ip, port, itf, methods);
            if(Objects.nonNull(handler)) {
                return handler;
            }
        }
        return null;
    }*/

    public static <T extends RpcHandler> T getDubboRpc(String ip, int port, Class<T> itf, List<MethodConfig> methods) {
        String key = ip + port + itf.getName();
        DubboConnector dubboConnector = dubboConnectorMap.get(key);
        try {
            if(Objects.isNull(dubboConnector) || Objects.isNull(dubboConnector.getHandler())) {
                DubboConnector newDubboConnector = new DubboConnector(key, ip, port, itf, methods);
                dubboConnector = dubboConnectorMap.putIfAbsent(key, newDubboConnector);
                if(Objects.isNull(dubboConnector)) {
                    dubboConnector = newDubboConnector;
                }else {
                    if(Objects.isNull(dubboConnector.getHandler())) {
                        dubboConnectorMap.remove(key);
                        dubboConnector.destroy();
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return (T) dubboConnector.getHandler();
    }

    public static <T extends RpcHandler> void resetDubboRpc(String ip, int port, Class<T> itf) {
        dubboConnectorMap.remove(ip + port + itf.getName());
    }
}
