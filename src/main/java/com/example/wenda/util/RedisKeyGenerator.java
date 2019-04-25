package com.example.wenda.util;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/18  9:55
 * @Description:
 */

public class RedisKeyGenerator {
    private static String SPLIT = "-";
    private static String KEY_LIKE = "LIKE";
    private static String KEY_DISLIKE = "DISLIKE";
    private static String KEY_EVENT = "EVENT_QUEUE";
    private static String KEY_FOLLOWER = "FOLLOWERS";//粉丝列表键，代表关注该用户或者该问题的列表
    private static String KEY_FOLLOWING = "FOLLOWING";//关注列表键，代表某用户的关注用户列表或者是关注的问题列表

    public static String getFollowingKey(int userId, int entityType) {
        return KEY_FOLLOWING + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getFollowerKey(int entityType, int entityId) {
        return KEY_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getLikeKey(int entityType, int entityId) {
        return KEY_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityType, int entityId) {
        return KEY_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventKey() {
        return KEY_EVENT;
    }
}
