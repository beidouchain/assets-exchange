package com.beidou.test;

import redis.clients.jedis.Jedis;

/**
 * Created by fengguoqing on 2018/7/2.
 */
public class RedisTest {
    public static void main(String args[]) {
        Jedis jedis = new Jedis("120.27.23.110",6379);
        System.out.println(jedis.get("t1"));

    }
}
