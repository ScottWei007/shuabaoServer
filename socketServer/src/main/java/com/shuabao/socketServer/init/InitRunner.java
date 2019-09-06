package com.shuabao.socketServer.init;


import com.shuabao.core.entity.UserSession;
import com.shuabao.core.manager.EnvironmentManager;
import com.shuabao.core.mapper.TestDao;
import com.shuabao.core.util.RedisUtil;
import com.shuabao.socketServer.rpc.RpcSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Scott Wei on 4/8/2018.
 */
//啓動初始化
@Component
public class InitRunner implements CommandLineRunner {
    @Autowired
    TestDao testDao;

    @Override
    public void run(String... strings) throws Exception {
        //設置持有環境資源
        //這裏可以做一些啓動初始化數據，相當於啓動監聽器

        System.out.println("==========="+EnvironmentManager.getEnvironment().getProperty("spring.redis.common.host"));




//        new Server().start(NumberUtils.toInt( EnvironmentManager.getEnvironment().getProperty("socketserver.port"),28888));//會阻塞住，放最後

        System.out.println("==============hahh");
//        System.out.println("==============hahh" + testDao.findAllEntity().size());


        //rpc start
        RpcSocketServer.start();

        UserSession session = new UserSession();
        session.setUid(7777+"");
        session.setAvatar("/img/boy.png");
        session.setToken("token");
        RedisUtil.setUserSession(session);

    }
}
