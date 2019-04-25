package com.example.wenda.service;

import java.util.List;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/23  17:08
 * @Description:
 */

public interface FollowService {
    //关注一个问题或者用户
    void follow(int userId, int entityType, int entityId);

    //取消关注
    void unfollow(int userId, int entityType, int entityId);

    //获取关注该问题或者用户的粉丝
    List<Integer> getFollowwers(int entityType, int entityId);

    //获取粉丝数目
    long getFollowerCount(int entityType, int entityId);

    //获取用户的关注列表
    List<Integer> getFollowings(int userId, int entityType);

    //获取关注的数量
    long getFollowingCount(int userId, int entityType);

    //查看用户的关注状态
    boolean getFollowStatus(int userId,int entityType,int entityId);
}
