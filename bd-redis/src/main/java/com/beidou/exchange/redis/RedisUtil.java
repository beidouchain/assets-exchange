package com.beidou.exchange.redis;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.io.*;
import java.util.*;


public class RedisUtil {

    private static final Logger LOG = Logger.getLogger(RedisUtil.class);
    private static JedisPool jedisPool = null;
    public static final String FILE_PATH = "redis.properties";
    static {
        init();
    }
    public static RedisProperty setProperty () {
        RedisProperty rp = new RedisProperty();
        Properties properties = new Properties();
        try {
            properties.load(RedisUtil.class.getClassLoader().getResourceAsStream(FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException("File Read Failed...", e);
        }
        Iterator it = properties.keySet().iterator();
        while (it.hasNext()) {
            String key = (String)it.next();
            if ("redis.ip".equals(key)) {
                rp.setIp(properties.getProperty("redis.ip"));
            } else if ("redis.port".equals(key)) {
                rp.setPort(Integer.valueOf(properties.getProperty("redis.port")) );
            } else if ("redis.db".equals(key)) {
                rp.setDb(Integer.valueOf(properties.getProperty("redis.db")));
            } else if ("redis.password".equals(key)) {
                rp.setPassword(properties.getProperty("redis.password"));
            }
            /*
            else if ("redis.maxtTotal".equals(key)) {
                rp.setMaxtTotal((Integer) properties.get("redis.maxtTotal"));
            } else if ("redis.maxIdle".equals(key)) {
                rp.setMaxIdle((Integer) properties.get("redis.maxIdle"));
            } else if ("redis.maxWaitMillis".equals(key)) {
                rp.setMaxWaitMillis((Long)properties.get("redis.maxWaitMillis"));
            } else if ("redis.minIdle".equals(key)) {
                rp.setMinIdle((Integer) properties.get("redis.minIdle"));
            } else if ("redis.testOnBorrow".equals(key)) {
                rp.setTestOnBorrow((Boolean) properties.get("redis.testOnBorrow"));
            } else if ("redis.testOnReturn".equals(key)) {
                rp.setTestOnReturn((Boolean) properties.get("redis.testOnReturn"));
            }*/
        }
        return rp;
    }
    public static String getValue(String key){
        Properties properties = new Properties();
        try {
            properties.load(RedisUtil.class.getClassLoader().getResourceAsStream(FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException("File Read Failed...", e);
        }
        return properties.getProperty(key);
    }
	private static void init() {
        RedisProperty rp = setProperty();
        JedisPoolConfig config = new JedisPoolConfig();
        System.out.println("redisip=" + getValue("redis.ip"));

        if (rp.getMaxtTotal() != null) {
            config.setMaxTotal(rp.getMaxtTotal());
        }

        if (rp.getMaxIdle() != null) {
            config.setMaxIdle(rp.getMaxIdle());
        }

        if (rp.getMaxWaitMillis() != null) {
            config.setMaxWaitMillis(rp.getMaxWaitMillis());
        }

        if (rp.getMinIdle() != null) {
            config.setMaxIdle(rp.getMinIdle());
        }

        if (rp.getTestOnBorrow() != null) {
            config.setTestOnBorrow(rp.getTestOnBorrow());
        }

        if (rp.getTestOnReturn() != null) {
            config.setTestOnReturn(rp.getTestOnBorrow());
        }
        String password = rp.getPassword();
        int timeout = 0;
        if (rp.getTimeout() != null) {
            timeout = rp.getTimeout();
        }
        String host = rp.getIp();
        int port = rp.getPort();
        int dbIndex = rp.getDb();
        if (StringUtils.isEmpty(password)) {
            jedisPool = new JedisPool(config, host, port,timeout);
        } else {
            jedisPool = new JedisPool(config, host, port, timeout, password, dbIndex);
        }

	}

    /**
     * 根据key获取记录
     *
     * @param key
     * @return 值
     * */
    public static String getString(String key) {
        String value = null;
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            if (jedis.exists(key)) {
                value = jedis.get(key);
                value = StringUtils.isNotBlank(value) && !"null".equalsIgnoreCase(value)?value:null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return value;
    }

    /**
     * 批量获取指定的多个key的值，如果key不存在，则返回'nil'，此方法不会失败。
     * Get the values of all the specified keys. If one or more keys dont exist or is not of type
     * String, a 'nil' value is returned instead of the value of the specified key, but the operation
     * never fails.
     * @param keys 指定的一个或多个key
     * @return
     */
    public static List<String> mget(final String... keys) {
        List<String> values = null;
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            values = jedis.mget(keys);
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return values;
    }

    /**
     * 添加值
     *
     * @param key
     * @param value
     * @return String 操作状态
     * */
    public static String setString(String key, String value) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            String str = jedis.set(key, value);
            return str;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 添加有过期时间的记录
     *
     * @param key
     * @param seconds 过期时间，以秒为单位
     * @param value
     * @return String 操作状态
     * */
    public static String setStringEx(String key, int seconds, String value) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            String str = jedis.setex(key, seconds, value);
            return str;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 添加有过期时间的记录
     *
     * @param key            Redis数据的KEY
     * @param timeMillis     Java时间戳（毫秒数），如 date.getTime(), System.currentTimeMillis()
     * @param value          key对应的值
     * @return String 操作状态
     * */
    public static String setStringExAtMillisTimestamp(String key, long timeMillis, String value) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            String str = jedis.set(key, value);
            /*
            java时间戳转换成Unix timestamp
            Unix时间戳(Unix timestamp)，定义为从格林威治时间1970年01月01日00时00分00秒起至现在的总秒数。
            注意：Unix timestamp 得到的是秒数，java用getTime()得到的是毫秒！
            */
            long unixTimestamp = timeMillis / 1000;
            if (unixTimestamp > 0) {
                jedis.expireAt(key, unixTimestamp);
            }
            return str;
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 对一个key设置过期时间
     * @param key            要设置过期时间的key
     * @param timeMillis     Java时间戳（毫秒数），如 date.getTime(), System.currentTimeMillis()
     * @return               1=设置成功，0=设置失败
     */
    public static Long expireAtMillisTimestamp(final String key, final long timeMillis) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            /*
            java时间戳转换成Unix timestamp
            Unix时间戳(Unix timestamp)，定义为从格林威治时间1970年01月01日00时00分00秒起至现在的总秒数。
            注意：Unix timestamp 得到的是秒数，java用getTime()得到的是毫秒！
            */
            long unixTimestamp = timeMillis / 1000;
            if(unixTimestamp > 0) {
                return jedis.expireAt(key, unixTimestamp);
            }
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }
    
    /**
     * 如果指定key不存在,设值,并设置有效时间(大于0的时间才会被设置)
     * 此方法可以用作分布式锁
     * @param key     键
     * @param value   值
     * @param seconds 失效时间，单位：秒
     * @return        1=设值成功，0=未设值，-1=设值失败
     */
    public static long setnx(String key,String value,int seconds){
    	boolean isBroken = false;
    	Jedis jedis = null;
    	long result = -1;
    	try{
    		jedis = getJedis();
    		result = jedis.setnx(key, value);
    		if(result == 1 && seconds > 0){
    			jedis.expire(key, seconds);
    		}
    	} catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
    	return result;
    }

    /**
     * 获取Redis锁
     * @param key      锁键
     * @param value    值
     * @param seconds  锁失效时间
     * @return         true=得到锁，false=未得到锁
     */
    public static boolean getRedisLock(String key,String value,int seconds) {
        if(seconds < 0) {
            seconds = 0;
        }
        long lockResult = RedisUtil.setnx(key, value, seconds); //60秒内不允许重复执行
        if (lockResult == 1) {
            return true; //获取锁成功
        }
        return false;
    }
    
    /**
     * 查询符合匹配条件的键
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern){
    	boolean isBroken = false;
    	Jedis jedis = null;
    	try{
    		jedis = getJedis();
            return jedis.keys(pattern + "*");
    	} catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
    	return null;
    }


    /**
     * 删除keys对应的记录,可以是多个key
     *
     * @param keys 要删除的key数组
     * @return     删除的记录数
     *
     * */
    public static long del(String... keys) {
        boolean isBroken = false;
        Jedis jedis = null;
        long result = 0;
        try {
            jedis = getJedis();
            return jedis.del(keys);
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return result;
    }


    /**
     * 从jedis连接池中获取获取jedis对象
     *
     * @return
     */
    public static Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException e) {
            e.printStackTrace();
            LOG.error(e.getLocalizedMessage(), e);
            returnBrokenJedis(jedis);
            throw e;
        }
        return jedis;
    }

    /**
     * 回收资源
     *
     * @param jedis
     */
    public static void returnJedis(Jedis jedis) {
        if(jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * Jedis连接发生错误时，必须调用returnBrokenResource返还给pool，否则下次通过getResource得到的instance的缓冲区可能还存在数据，出现问题
     *
     * @param jedis
     */
    public static void returnBrokenJedis(Jedis jedis) {
        if(jedis != null) {
            jedisPool.returnBrokenResource(jedis);
        }
    }

    /**
     * 验证key 是否存在
     * @param key
     * @return
     */
    public static boolean existKey(String key) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            return jedis.exists(key);
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return false;
    }

    /**
     * 在instance出错时，必须调用returnBrokenResource返还给pool，否则下次通过getResource得到的instance的缓冲区可能还存在数据，出现问题
     * @param jedis     Jedis连接对象
     * @param isBroken  true: instance出错, false:instance正常
     */
    public static void release(Jedis jedis, boolean isBroken) {
        if (jedis != null) {
            if (isBroken) {
                jedisPool.returnBrokenResource(jedis);
            } else {
                jedisPool.returnResource(jedis);
            }
        }
    }
    /**
     * 添加有过期时间的记录
     *
     * @param strKey
     * @param seconds 过期时间，以秒为单位
     * @param obj
     * */
    public static void setObjex(String strKey, int seconds, Object obj) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            String key = strKey;
            jedis = getJedis();
            byte[] value = serialize(obj);
            if (value != null) {
                jedis.setex(key.getBytes(), seconds, value);
            }
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
    }
    /**
     * cache对象
     *   cache的实际key = obj类名 + "_" + key(参数)
     * @param strKey - key
     * @param obj - cache对象
     */
    public static void setObj(String strKey, Object obj) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            String key = obj.getClass().getSimpleName() + "_" + strKey;
            jedis = getJedis();
            byte[] value = serialize(obj);

            if (value != null) {
                jedis.set(key.getBytes(), value);
            }
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
    }

    /**
     * 取得cache对象
     * @param strKey - key
     * @param objClass - 对象Class
     * @return
     */
    public static Object getObj(String strKey, Class objClass) {
        Jedis jedis = null;
        Object returnObj = null;
        boolean isBroken = false;
        try {
            String key = objClass.getSimpleName() + "_" + strKey;
            jedis = getJedis();
            byte[] value = jedis.get(key.getBytes());
            if (value != null) {
                returnObj = unserialize(value);
            }
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return returnObj;
    }

    public static Object getObjex(String strKey, Class objClass) {
        Jedis jedis = null;
        Object returnObj = null;
        boolean isBroken = false;
        try {
            String key = strKey;
            jedis = getJedis();
            byte[] value = jedis.get(key.getBytes());

            if (value != null) {
                returnObj = unserialize(value);
            }
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return returnObj;
    }
    /**
     * 删除cache对象
     * @param strKey
     * @param objClass
     * @return
     */
    public static long delObj(String strKey, Class objClass) {
        boolean isBroken = false;
        Jedis jedis = null;
        long result = 0;
        try {
            String key = objClass.getSimpleName() + "_" + strKey;
            jedis = getJedis();
            return jedis.del(key.getBytes());
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }

        return result;
    }

    /**
     * 删除cache对象
     * @param strKeys
     * @param objClass
     * @return
     */
    public static long delObjs(String[] strKeys, Class objClass) {
        boolean isBroken = false;
        Jedis jedis = null;
        long result = 0;
        try {
            byte[][] byteKeys = new byte[strKeys.length][];
            int cnt = 0;
            for (String strKey : strKeys) {
                String key = objClass.getSimpleName() + "_" + strKey;
                byteKeys[cnt] = key.getBytes();
                cnt++;
            }

            jedis = getJedis();
            return jedis.del(byteKeys);
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }

        return result;
    }

    /**
     * 序列化对象
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;

        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();

            return bytes;
        } catch (Exception ex) {
            LOG.error(ex.getLocalizedMessage(), ex);
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;

        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);

            return ois.readObject();
        } catch (Exception ex) {
            LOG.error(ex.getLocalizedMessage(), ex);
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 把元素插入LIST的左侧（头部添加）
     * @param key       LIST结构名字
     * @param strings   String对象元素
     * @return          返回插入元素后，LIST中元素的总数
     */
    public static Long lpush(final String key, final String... strings) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.lpush(key, strings);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 把元素插入LIST的右侧（尾部添加）
     * @param key       LIST结构名字
     * @param strings   String对象元素
     * @return          返回插入元素后，LIST中元素的总数
     */
    public static Long rpush(final String key, final String... strings) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.rpush(key, strings);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 取出一个范围内的元素列表，取出所有：start=0, end=-1
     * @param key     LIST结构名字
     * @param start   开始位置，从0开始
     * @param end     结束位置，-1表示最后一个元素
     * @return
     */
    public static List<String> lrange(final String key, final long start, final long end) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            List<String> result = jedis.lrange(key, start, end);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 获取LIST中元素个数
     * @param key  LIST结构名字
     * @return
     */
    public static Long llen(final String key) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.llen(key);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 设置HASH结构中的键值对
     * @param key     HASH名字
     * @param field
     * @param value
     * @return
     */
    public static Long hset(final String key, final String field, final String value) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.hset(key, field, value);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 从HASH结构中获取数据
     * @param key    HASH名字
     * @param field  HASH中的键值对key
     * @return
     */
    public static String hget(final String key, final String field) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            String result = jedis.hget(key, field);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    public static Set<String> hkeys(final String key){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            return jedis.hkeys(key);
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    public static List<String> hvals(final String key){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            return jedis.hvals(key);
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 将HASH结构数据存入Redis
     * @param key   HASH结构本身KEY
     * @param hash
     * @return
     */
    public static String hmset(final String key, final Map<String, String> hash) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            String result = jedis.hmset(key, hash);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 从HASH结构中一次获取多个值
     * @param key     HASH名字
     * @param fields
     * @return
     */
    public static List<String> hmget(final String key, final String... fields) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            List<String> result = jedis.hmget(key, fields);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 获取HASH的全部key-value对
     * @param key
     * @return
     */
    public static Map<String,String> hgetall(final String key) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Map<String, String> result = jedis.hgetAll(key);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 从HASH中删除键值对
     * @param key     HASH名字
     * @param fields
     * @return
     */
    public static Long hdel(final String key, final String... fields) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.hdel(key, fields);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 将数据存入ZSET结构中，存在则更新
     * @param key      ZSET名字
     * @param score
     * @param member
     * @return
     */
    public static Long zadd(final String key, final double score, final String member) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.zadd(key, score, member);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 批量存入ZSET结构中，存在则更新
     * @param key             ZSET名字
     * @param scoreMembers
     * @return
     */
    public static Long zadd(final String key, final Map<String, Double> scoreMembers) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.zadd(key, scoreMembers);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 从ZSET中获取数据，获取score在min~max之间的所有member
     * (including elements with score equal to min or max)
     * @param key   ZSET名字
     * @param min   SCORE最小值
     * @param max   SCORE最大值
     * @return
     */
    public static Set<String> zrangeByScore(final String key, final double min, final double max) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Set<String> result = jedis.zrangeByScore(key, min, max);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 从ZSET中获取数据，获取score在min~max之间的所有member
     * (including elements with score equal to min or max)
     * @param key      ZSET名字
     * @param min      SCORE最小值
     * @param max      SCORE最大值
     * @param offset   分页开始位置
     * @param count    本次获取最大条数
     * @return
     */
    public static Set<String> zrangeByScore(final String key, final double min, final double max,
                                            final int offset, final int count) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Set<String> result = jedis.zrangeByScore(key, min, max, offset, count);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 获取ZSET全部元素，演示，获取全部：zrange key 0 -1
     * @param key      ZSET名字
     * @param start    索引开始位置
     * @param end      索引结束位置
     * @return
     */
    public static Set<String> zrange(final String key, final long start, final long end) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Set<String> result = jedis.zrange(key, start, end);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 从ZSET中删除
     * @param key       ZSET名字
     * @param members   member名字
     * @return
     */
    public static Long zrem(final String key, final String... members) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.zrem(key, members);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 获取ZSET中某个元素的score，如果member不存在或key不存在，则返回null
     * @param key      ZSET名字
     * @param member   member名字
     * @return
     */
    public static Double zscore(final String key, final String member) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Double result = jedis.zscore(key, member);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 增加ZSET中member的score，不存在则直接添加
     * @param key        ZSET名字
     * @param score      增量
     * @param member     member名字
     * @return
     */
    public static Double zincrby(final String key, final double score, final String member) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Double result = jedis.zincrby(key, score, member);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * redis自增长序列
     * @param key
     * @param integer 步长
     * @return 失败返回 -1
     */
    public static long incrBy(final String key,final long integer) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            long result = jedis.incrBy(key, integer);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return -1;
    }


    /**
     * 对一个key设置过期时间，到期后key将会被自动删除
     * @param key      要设置过期时间的key
     * @param seconds  过期的时间，单位：秒
     * @return         1=设置成功，0=设置失败
     */
    public static Long expire(final String key, final int seconds) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.expire(key, seconds);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }

    /**
     * 获取一个key还剩余的超时时间
     * @param key       Redis的key
     * @return          -2:key不存在，-1:没有为key设置超时时间，>0:超时时间
     */
    public static Long ttl(final String key) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.ttl(key);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }
    /**
     * flashall
     * @return
     */
    public static void flashAll() {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.flushAll();
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
    }
    /**
     * SET 添加
     * @param key
     * @param members
     * @return
     */
    public static Long sadd(String key,String... members){
    	Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long result = jedis.sadd(key, members);
            return result;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }
    /**
     * 查找某一个member是否在SET中存在
     * @param key
     * @param member
     * @return
     */
    public static Boolean simember(String key,String member){
    	Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Boolean b =jedis.sismember(key, member);
            return b;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }
    /**
     * 获取SET所有元素
     * @param key
     * @return
     */
    public static Set<String> smembers(String key){
    	Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Set<String> set = jedis.smembers(key);
            
            return set;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }
    
    /**
     * 删除SET几个元素
     * @param key
     * @return
     */
    public static Long srem(String key,String... members){
    	Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            Long count = jedis.srem(key, members);
            
            return count;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            isBroken = true;
        } finally {
            release(jedis, isBroken);
        }
        return null;
    }
    
    public static void main(String[] args) {
        Jedis jedis = getJedis();
        jedis.set("czh", 31 + "");

        System.out.println(jedis.get("czh1"));

    }
}
