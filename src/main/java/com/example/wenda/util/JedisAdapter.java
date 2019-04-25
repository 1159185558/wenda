package com.example.wenda.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/18  9:12
 * @Description:
 */

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool jedisPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("redis://127.0.0.1:6379/7");
    }

    /**
     * 想集合中添加元素，如果元素的集合中存在则忽略，否则添加进去。
     *
     * @param key
     * @param value
     */
    public void sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("集合添加元素时出错：" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 删除集合中存在的元素，若元素不存在则忽略
     *
     * @param key
     * @param value
     */
    public void srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("删除集合元素时出错：" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 查看集合中元素的个数
     *
     * @param key
     */
    public long scard(String key) {
        Jedis jedis = null;
        long memberCount = 0;
        try {
            jedis = jedisPool.getResource();
            memberCount = jedis.scard(key);
        } catch (Exception e) {
            logger.error("删除集合元素时出错：" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return memberCount;
    }

    /**
     * 判断某个元素是否是集合中的元素
     *
     * @param key
     * @param value
     */
    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        boolean isMember = false;
        try {
            jedis = jedisPool.getResource();
            isMember = jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("删除集合元素时出错：" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return isMember;
    }

    /**
     * 向列表中表首添加元素
     *
     * @param key
     * @param value
     */
    public void lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("向列表中添加元素失败：" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从列表尾部弹出元素
     *
     * @param timeout
     * @param key
     * @return
     */
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        List<String> list = new ArrayList<>();
        try {
            jedis = jedisPool.getResource();
            list = jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("从列表中弹出元素失败：" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return list;
    }

    /**
     * 获取一个Jedis对象，在运行事务时运行
     *
     * @return
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 获取一个事务
     *
     * @return
     */
    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error("启动事务时失败：" + e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 执行事务
     *
     * @param transaction
     * @return
     */
    public List<Object> exec(Transaction transaction) {
        try {
            return transaction.exec();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("命令执行时出错：" + e);
            transaction.discard();
        } finally {
            if (transaction != null) {
                try {
                    transaction.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public void zadd(String key,Long scroe,String member){
        Jedis jedis = null;
        try{
            jedis=jedisPool.getResource();
            jedis.zadd(key,scroe,member);
        }catch (Exception e){
            logger.error("向zset中添加元素失败"+e);
        }
    }


    /**
     * 统计zset中元素的个数
     *
     * @param key
     * @return
     */
    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("统计zset元素数目时出错：" + e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 获取指定区间内zset元素按照分数从小到大排列
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error("升序获取zset中元素列表时出错：" + e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 获取指定区间内zset元素按照分数从大到小排列
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("降序获取zset中元素列表时出错：" + e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("获取zset指定元素的分数时出错：" + e);
        }
        return null;
    }
}
