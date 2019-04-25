package com.example.wenda.service;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/18  10:03
 * @Description:
 */

public interface LikeServer {

    //点赞,返回点赞的个数
    long like(int userId, int entityType, int entityId);

    //踩，返回踩的个数
    long dislike(int userId, int entityType, int entityId);

    //获取点赞的数量
    long getLikeCount(int entityType, int entityId);

    //获得踩的数量
    long getDislikeCount(int entityType, int entityId);

    //查看用户点赞状态，是点赞还是踩还是没有任何操作
    int getLikeStatus(int userId, int entityType, int entityId);
}
