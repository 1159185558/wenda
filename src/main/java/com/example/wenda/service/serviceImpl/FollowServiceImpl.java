package com.example.wenda.service.serviceImpl;

import com.example.wenda.service.FollowService;
import com.example.wenda.util.JedisAdapter;
import com.example.wenda.util.RedisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/24  12:09
 * @Description:
 */
@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        String followingKey = RedisKeyGenerator.getFollowingKey(userId, entityType);
        Jedis jedis = jedisAdapter.getJedis();
        Transaction transaction = jedis.multi();
        //以时间作为zset的分数
        long score = new Date().getTime();
        transaction.zadd(followerKey, score, String.valueOf(userId));
        transaction.zadd(followingKey, score, String.valueOf(entityId));
        jedisAdapter.exec(transaction);
        if (jedis != null) {
            jedis.close();
        }
    }

    @Override
    public void unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        String followingKey = RedisKeyGenerator.getFollowingKey(userId, entityType);
        Jedis jedis = jedisAdapter.getJedis();
        Transaction transaction = jedis.multi();
        transaction.zrem(followerKey, String.valueOf(userId));
        transaction.zrem(followingKey, String.valueOf(entityId));
        jedisAdapter.exec(transaction);
        if (jedis != null) {
            jedis.close();
        }
    }

    @Override
    public List<Integer> getFollowwers(int entityType, int entityId) {
        String followerKey = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        return setToList(jedisAdapter.zrevrange(followerKey, 0, -1));
    }

    public List<Integer> setToList(Set<String> set) {
        List<Integer> list = new ArrayList<>();
        for (String s : set) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    @Override
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    @Override
    public List<Integer> getFollowings(int userId, int entityType) {
        String followingKey = RedisKeyGenerator.getFollowingKey(userId, entityType);
        return setToList(jedisAdapter.zrevrange(followingKey, 0, -1));
    }

    @Override
    public long getFollowingCount(int userId, int entityType) {
        String followingKey = RedisKeyGenerator.getFollowingKey(userId, entityType);
        return jedisAdapter.zcard(followingKey);
    }

    /**
     * 通过查询zset中元素的分数是否为空来判断该元素知否在集合中
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public boolean getFollowStatus(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }
}
