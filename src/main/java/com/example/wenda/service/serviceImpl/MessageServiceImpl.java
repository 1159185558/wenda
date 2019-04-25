package com.example.wenda.service.serviceImpl;

import com.example.wenda.dao.MessageDao;
import com.example.wenda.entity.Message;
import com.example.wenda.service.MessageService;
import com.example.wenda.util.SensetiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/10  8:31
 * @Description:
 */

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    SensetiveWordFilter sensetiveWordFilter;
    @Autowired
    MessageDao messageDao;

    @Override
    public int sendMessage(Message message) {
        message.setContent(sensetiveWordFilter.filterSensetiveWord(message.getContent()));
        return messageDao.addMessage(message);
    }

    @Override
    public List<Message> getMessageLists(int userId) {
        return messageDao.getMessageLists(userId);
    }

    @Override
    public Message getMessage(int fromId, int toId) {
        List<Integer> list = new ArrayList<>();
        list.add(fromId);
        list.add(toId);
        return messageDao.getMinIdMessage(list);
    }

    @Override
    public List<Integer> getMessageListCounts(int userId) {
        return messageDao.getMessageCounts(userId);
    }

    @Override
    public Integer getNotReadMessageCounts(int toId, String conversationId) {
        return messageDao.getNotReadMessageCounts(toId, conversationId);
    }

    @Override
    public int deleteMessage(String conversationId) {
        return messageDao.updateStatus(conversationId) > 0 ? messageDao.updateStatus(conversationId) : 0;
    }

    @Override
    public int markMessageReaded(String conversationId) {
        return messageDao.updateIsRead(conversationId);
    }

    @Override
    public List<Message> getMessageListsByConversationId(String conversationId) {
        return messageDao.getMessageListsByConversationId(conversationId);
    }
}
