package com.shuabao.core.config.redis;

import com.shuabao.core.config.BaseRedisConfigurer;
import com.shuabao.core.manager.EnvironmentManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Scott Wei on 4/8/2018.
 */
public class CommonRedisConfigurer extends BaseRedisConfigurer {

    private static volatile CommonRedisConfigurer instance;
    private static final Lock lock = new ReentrantLock();

    //多綫程單利模式
    public static CommonRedisConfigurer getInstance() {
        if(instance == null) {
            try {
                lock.lock();
                if(instance == null) {
                    instance = new CommonRedisConfigurer();
                }
            }finally {
                lock.unlock();
            }
        }
        return instance;
    }


    private final JedisPool jedisPool;

    private CommonRedisConfigurer() {
        JedisPoolConfig jedisPoolConfig = super.configure(new JedisPoolConfig());
        String host = EnvironmentManager.getEnvironment().getProperty("spring.redis.common.host");
        String port = EnvironmentManager.getEnvironment().getProperty("spring.redis.common.port");
        String password = EnvironmentManager.getEnvironment().getProperty("spring.redis.common.password");
        if(StringUtils.isNotEmpty(password)) {
            jedisPool = new JedisPool(jedisPoolConfig, host, NumberUtils.toInt(port),0 , password);
        }else {
            jedisPool = new JedisPool(jedisPoolConfig, host, NumberUtils.toInt(port),0);
        }
    }

    @Override
    protected JedisPool getJedisPool() {
        return jedisPool;
    }
}
