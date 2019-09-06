package com.shuabao.core.config;

import com.shuabao.core.util.SerializeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;

/**
 * Created by Scott Wei on 4/8/2018.
 */
public abstract class BaseRedisConfigurer implements Configurer<JedisPoolConfig> {

    @Override
    public JedisPoolConfig configure(JedisPoolConfig poolConfig) {
        poolConfig.setMaxTotal(-1);
        poolConfig.setMaxIdle(5000);
        poolConfig.setMaxWaitMillis(10000);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(false);
        return poolConfig;
    }
    
    protected abstract JedisPool getJedisPool();

    /**
     * 获取连接对象
     *@param RedisPool
     *@return
     */
    public Jedis getJedis(JedisPool RedisPool) {
        int timeoutCount = 0;
        while (true) {// 如果是网络超时则多试几次
            try {
                Jedis jedis = RedisPool.getResource();
                return jedis;
            } catch (Exception e) {
                // 底层原因是SocketTimeoutException，不过redis已经捕捉且抛出JedisConnectionException，不继承于前者
                if (e instanceof JedisConnectionException || e instanceof SocketTimeoutException) {
                    timeoutCount++;
                    if (timeoutCount > 6) {
                        break;
                    }
                }else {
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 返还到连接池
     *
     * @param pool
     * @param redis
     */
    public void returnResource(JedisPool pool, Jedis redis) {
        try{
            if (pool != null && redis != null) {
                redis.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 是否存在
     *@param key
     *@return
     */
    public boolean exists(String key) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            return jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return false;
    }

    /**
     * 设置过期时间
     *@param key
     *@param exptime
     */
    public void expire(String key, int exptime) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.expire(key, exptime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
    }

    public void set(String key, Object value) {
        set(key,value,0);
    }

    /**
     * set
     *@param key
     *@param value
     *@param expire
     */
    public void set(String key, Object value, int expire) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.set(key.getBytes(), SerializeUtil.serialize(value));
            if (expire > 0)
                jedis.expire(key, expire);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
    }


    public Object get(String key) {
        return get(key.getBytes());
    }

    public Object get(byte[] key) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            byte[] data = jedis.get(key);
            return SerializeUtil.unserialize(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return null;
    }

    /**
     * set 添加元素
     *@param key
     *@param members
     *@param expire
     */
    public void sadd(String key,String members,int expire){
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.sadd(key, members);
            if (expire > 0) jedis.expire(key, expire);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
    }

    /**
     * set 添加元素
     *@param key
     *@param members
     *@param
     */
    public void sadd(String key,String... members){
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.sadd(key, members);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
    }
    /**
     *  set 添加元素
     *@param key
     *@param members
     */
    public void sadd(String key,String members){
        sadd(key, members, 0);
    }
    /**
     * sorted set添加元素
     *@param key
     *@param member
     *@param score
     *@param expire
     */
    public void zadd(String key,String member,double score,int expire){
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.zadd(key, score, member);
            if (expire > 0) jedis.expire(key, expire);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
    }
    /**
     * sorted set添加元素
     *@param key
     *@param member
     *@param score
     */
    public void zadd(String key,String member,double score){
        zadd(key, member, score, 0);
    }

    public Double zincrby(String key, String member, double score) {
        return zincrby(key,  member, score, 0);
    }

    /**
     *
     * @Title: zincrby
     * @Description: TODO(累加score的值)
     * @return void 返回类型
     * @param key
     * @param score
     * @param member
     * @param expire
     */
    public Double zincrby(String key, String member, double score, int expire) {
        JedisPool pool = null;
        Jedis jedis = null;
        Double result =null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            result =jedis.zincrby(key.getBytes(), score, member.getBytes());
            if (expire > 0) {
                jedis.expire(key.getBytes(), expire);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    /**
     * 判断 member 元素是否集合 key 的成员 如果 member 元素是集合的成员，返回 1 如果 member 元素不是集合的成员，或
     * key 不存在，返回 0
     */
    public boolean sismember(String key, String member) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            return jedis.sismember(key, member);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return false;
    }

    /**
     * 返回集合中的所有的成员。 不存在的集合 key 被视为空集合。
     *@param key
     *@return
     */
    public Set<String> smembers(String key) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            return jedis.smembers(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return null;
    }


    public void delete(String key) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);

            jedis.del(key.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
    }

    /**
     * 返回有序集 key中，指定区间内的成员
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key,long start,long end) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return null;
    }


    /***
     * hash操作 hget
     *
     * @param key
     * @return
     */
    public String hget(String key, String field) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            return jedis.hget(key, field);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
    }


    /***
     * hash操作 hgetAll
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            return jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
    }

    /***
     * hash操作 hmset
     *
     * @param key
     * @return
     */
    public void hmset(String key, Map<String, String> map) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.hmset(key, map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
    }

    /***
     * hash操作 删除
     *
     * @param key
     * @return
     */
    public void hdel(String key, String... fields) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.hdel(key, fields);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
    }

    public void lPush(String key, String value, int expire) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            jedis.lpush(key, value);
            if (expire > 0)
                jedis.expire(key, expire);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
    }

    public String rPop(String key) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = getJedis(pool);
            return jedis.rpop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 返还到连接池
            returnResource(pool, jedis);
        }
    }


  
}
