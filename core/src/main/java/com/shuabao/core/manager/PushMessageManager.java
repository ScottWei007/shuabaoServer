package com.shuabao.core.manager;

import com.shuabao.core.entity.UserSession;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.core.rpc.dubbo.ProductServerTask;
import com.shuabao.core.rpc.handler.SocketServiceHandler;
import com.shuabao.core.rpc.zookeeper.SocketServiceConsumeListener;
import com.shuabao.core.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Objects;

public class PushMessageManager {
    private static final Logger log = LoggerFactory.getLogger(PushMessageManager.class);

    public static boolean pushMessage(int uid, MessageWrapper messageWrapper) {
        boolean result = false;
        ProductServerTask productServerTask = getRpcServer(uid);
        if(Objects.isNull(productServerTask)) {
            log.error("=============PushMessageManager getRpcServer error: ProductServerTask is null");
        }else {
            try {
                result = ((SocketServiceHandler)productServerTask.getHandler()).pushMessage(uid, messageWrapper);
            }catch (RemoteException e) {
                log.error("=============PushMessageManager pushMessage error: " + e.getMessage());
                productServerTask.reloadHandler();
            }
        }
        if(!result) {//缓存离线消息
            RedisUtil.lPushMessage(uid, messageWrapper);
        }
        return result;
    }

    public static ProductServerTask getRpcServer(int uid) {
        UserSession session = RedisUtil.getUserSession(String.valueOf(uid), null);
        if(Objects.isNull(session)) {
            return null;
        }
        if(StringUtils.isAnyEmpty(session.getHost(), session.getPort())) {
            return null;
        }
        return SocketServiceConsumeListener.getInstance().findServer(session.getHost(), session.getPort());
    }
}
