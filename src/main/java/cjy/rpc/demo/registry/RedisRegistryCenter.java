package cjy.rpc.demo.registry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis作为服务的注册中心
 */
public class RedisRegistryCenter {

    private static Jedis jedis;

    /**
     * 初始化redis
     * @param host 注册中心地址
     * @param port 端口号
     */
    public static void init(String host,int port) {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setTestOnBorrow(false);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port);
        jedis = jedisPool.getResource();
    }

    /**
     * 注册生产者
     * @param key 接口信息
     * @param infos 生产者信息
     * @return
     */
    public static Long registryProvider(String key, String infos) {
        return jedis.sadd(key);
    }

    /**
     * 获取生产者
     * @param key
     * @return
     */
    public static String obtainProvider(String key) {
        return jedis.srandmember(key);
    }

    /**
     * 获取jedis
     * @return
     */
    public static Jedis getJedis() {
        return jedis;
    }

    /**
     * 生成key
     * @param _interface 接口
     * @param alias 别名
     * @param protocol 协议
     * @return
     */
    public static String genericKey(String _interface, String alias, String protocol) {
        String key = _interface + "_" + alias + "_" + protocol;
        return key;
    }

}
