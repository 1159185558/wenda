package com.example.wenda.service.serviceImpl;

import com.example.wenda.service.LikeServer;
import com.example.wenda.util.JedisAdapter;
import com.example.wenda.util.RedisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/18  10:30
 * @Description:
 */
@Service
public class LikeServerImpl implements LikeServer {
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyGenerator.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyGenerator.getDislikeKey(entityType, entityId);
        Jedis jedis = jedisAdapter.getJedis();
        Transaction transaction = jedis.multi();
        //将用户添加到点赞的集合中去
        transaction.sadd(likeKey, String.valueOf(userId));
        //将用户从踩的集合中删掉
        transaction.srem(dislikeKey, String.valueOf(userId));
        jedisAdapter.exec(transaction);
        if (jedis != null) {
            jedis.close();
        }
        //返回点赞数
        return jedisAdapter.scard(likeKey);
    }

    @Override
    public long dislike(int userId, int entityType, int entityId) {
        String dislikeKey = RedisKeyGenerator.getDislikeKey(entityType, entityId);
        String likeKey = RedisKeyGenerator.getLikeKey(entityType, entityId);
        Jedis jedis = jedisAdapter.getJedis();
        //采用事务来进行踩操作，将用户添加到踩集合与从点赞集合中删除这两个操作合并为一条事务进行
        Transaction transaction = jedis.multi();
        //将用户加入到踩的集合中
        transaction.sadd(dislikeKey, String.valueOf(userId));
        //将用户从点赞的集合中删除
        transaction.srem(likeKey, String.valueOf(userId));
        jedisAdapter.exec(transaction);
        if (jedis != null) {
            jedis.close();
        }
        //返回踩的个数
        return jedisAdapter.scard(dislikeKey);
    }

    @Override
    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyGenerator.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }

    @Override
    public long getDislikeCount(int entityType, int entityId) {
        String dislikeKey = RedisKeyGenerator.getDislikeKey(entityType, entityId);
        return jedisAdapter.scard(dislikeKey);
    }

    @Override
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String dislikeKey = RedisKeyGenerator.getDislikeKey(entityType, entityId);
        String likeKey = RedisKeyGenerator.getLikeKey(entityType, entityId);
        //该用户如果属于踩集合 则返回-1
        if (jedisAdapter.sismember(dislikeKey, String.valueOf(userId))) {
            return -1;
        }
        //该用户如果属于点赞集合，则返回1
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        //该用户既没有点赞也没有踩返回0
        return 0;
    }
}
