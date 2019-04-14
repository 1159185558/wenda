package com.example.wenda.service;

import com.example.wenda.model.Message;

import java.util.List;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/10  8:31
 * @Description:
 */

public interface MessageService {
    //发送信息,将信息保存在数据库中
    int sendMessage(Message message);

    // 获得给用户的会话列表
    List<Message> getMessageLists(int userId);

    // 根据from_id和to_id获取Message
    Message getMessage(int fromId, int toId);

    //获取每个会话的总消息数目
    List<Integer> getMessageListCounts(int userId);

    //获取每个会话的未读消息数目
    List<Integer> getNotReadMessageCounts(int toId);
}
