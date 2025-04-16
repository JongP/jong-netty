package org.example.jong.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisHelper {
    protected static final String REDIS_HOST = "127.0.0.1";

    //TODO check the port num
    protected static final int REDIS_PORT = 6380;
    private final Set<Jedis> connectionList = new HashSet<>();
    private final JedisPool jedisPool;

    private JedisHelper(){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(20);
        config.setBlockWhenExhausted(true);

        this.jedisPool = new JedisPool(config,REDIS_HOST, REDIS_PORT);
    }

    private static class LazyHolder{
        @SuppressWarnings("synthetic-access")
        private static final JedisHelper INSTANCE = new JedisHelper();
    }

    @SuppressWarnings("synthetic-access")
    public static JedisHelper getInstance(){
        return LazyHolder.INSTANCE;
    }

    final public Jedis getConnection(){
        Jedis jedis = this.jedisPool.getResource();
        this.connectionList.add(jedis);

        return jedis;
    }

    final public void returnResource(Jedis jedis){
        if(jedis != null){
            this.jedisPool.returnResource(jedis);
            //this.connectionList.remove(jedis);
        }
    }

    final public void destroyPool(){
        Iterator<Jedis> jedisIterator = this.connectionList.iterator();

        while(jedisIterator.hasNext()){
            Jedis jedis = jedisIterator.next();
            this.jedisPool.returnBrokenResource(jedis);
        }

        this.jedisPool.destroy();
    }
}
