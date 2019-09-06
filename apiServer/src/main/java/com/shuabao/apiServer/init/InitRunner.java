package com.shuabao.apiServer.init;

import com.shuabao.core.config.redis.CommonRedisConfigurer;
import com.shuabao.core.entity.HomePageTypeEntity;
import com.shuabao.core.manager.EnvironmentManager;
import com.shuabao.core.rpc.zookeeper.SocketServiceConsumeListener;
import com.shuabao.core.util.RedisUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Scott Wei on 4/8/2018.
 */
//啓動初始化
@Component
public class InitRunner implements CommandLineRunner{


    @Override
    public void run(String... strings) throws Exception {

        //這裏可以做一些啓動初始化數據，相當於啓動監聽器
//        System.out.println("==========="+EnvironmentManager.getEnvironment().getProperty("spring.redis.common.host"));
//        System.out.println("===========test"+EnvironmentManager.getEnvironment().getProperty("test.id"));
//        System.out.println("============="+ CommonRedisConfigurer.getInstance().get("TEST_REDIS"));
        //初始化
        /*String[] arr = {"watch","follow","share"};
        for (int i = 10000; i < 10025; i++) {
            long time = c; UserSession session = new UserSession();
            session.setUid(i+"");
            if((i & 1) == 0) {//偶数
                session.setAvatar("/img/boy.png");
            }else {
                session.setAvatar("/img/girl.png");
            }
            session.setBonds(ThreadLocalRandom.current().nextInt() + "");
            RedisUtil.setHomepageType(i,time, HomePageTypeEntity.HomePageType.NEW, null);
            RedisUtil.setHomepageType(i,Long.valueOf(session.getBonds()),HomePageTypeEntity.HomePageType.HOT, null);
            if((i & 1) == 0) {//偶数
                RedisUtil.setHomepageType(i,time, HomePageTypeEntity.HomePageType.FOLLOW, 10086);
            }
            RedisUtil.setUserSession(session);

            RedisUtil.removeUserSession(i+"", arr);
            RedisUtil.getUserSession(i+"", null);
        }*/

      /*  RedisUtil.setHomepageType(11000055,System.nanoTime(),HomePageTypeEntity.HomePageType.NEW,null);
        RedisUtil.setHomepageType(11000055,System.nanoTime(), HomePageTypeEntity.HomePageType.FOLLOW, 10086);
        RedisUtil.removeUserSession(11000055+"", arr);
        RedisUtil.setHomepageType(11000066,System.nanoTime(), HomePageTypeEntity.HomePageType.NEW, null);
        RedisUtil.setHomepageType(11000066,11000066, HomePageTypeEntity.HomePageType.HOT, null);
        RedisUtil.setHomepageType(11000077,System.nanoTime(), HomePageTypeEntity.HomePageType.NEW, null);
        RedisUtil.setHomepageType(11000077,11000077,HomePageTypeEntity.HomePageType.HOT, null);*/


      //监听socketserver的服务, 以便远程调用它提供的服务
//        SocketServiceConsumeListener.getInstance();



        //测试发消息
        System.out.println(System.currentTimeMillis());

    }
}
