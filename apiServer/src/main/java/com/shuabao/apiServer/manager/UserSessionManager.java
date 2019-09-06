package com.shuabao.apiServer.manager;


import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Scott Wei on 4/17/2018.
 */
public class UserSessionManager {
    //多綫程單利模式
    private static volatile UserSessionManager instance;
    private static final Lock lock = new ReentrantLock();
    public static UserSessionManager getInstance() {
        if(Objects.isNull(instance)) {
            try {
                lock.lock();
                if(Objects.isNull(instance)) {
                    instance = new UserSessionManager();
                }
            }finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private UserSessionManager(){
        tokenCache = new ConcurrentHashMap(500);
    }

    //session緩存容器
    private final Map<Integer, String> tokenCache;

    public String getUserToken (int uid ) {
        return tokenCache.get(uid);
    }

    public void addUserToken (int uid, String token) {
       /* String pre = tokenCache.putIfAbsent(entity.getUid(), entity.getToken());//不會替換原來的
        if(Objects.isNull(pre)) {//為null説明成功放入值，不爲null説明已經有值，則不放入
            pre = entity.getToken();
        }*/
        tokenCache.put(uid, token);//會替換原來的
    }

    public void removeUserToken (int uid) {
        tokenCache.remove(uid);//必須收到轉成integer類型
    }
}
