package com.example.wenda.asynchronization.Handler;

import com.example.wenda.asynchronization.EventEntity;
import com.example.wenda.asynchronization.EventHandler;
import com.example.wenda.asynchronization.EventType;
import com.example.wenda.entity.Message;
import com.example.wenda.service.MessageService;
import com.example.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/18  19:38
 * @Description:
 */
@Component
public class LikeHandler implements EventHandler {
    private static final int SYSTEM_MESSAGE_ID = 1;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Override
    public List<EventType> getSupportedEventType() {
        return Arrays.asList(EventType.LIKE);
    }

    /**
     * 用于点赞成功后发送通知消息
     *
     * @param eventEntity
     */
    @Override
    public void handleEvent(EventEntity eventEntity) {
        Message message = new Message();
        message.setCreateDate(new Date());
        String username = userService.getUsernameById(eventEntity.getActorId());
        message.setContent("用户" + username
                + "点赞了你的回答，赶快打开看一下吧！http://127.0.0.1:8080/question/"
                + eventEntity.getExtraVar());
        message.setFromId(SYSTEM_MESSAGE_ID);
        message.setToId(eventEntity.getEnventOwnerId());
        Message tmpMessage = messageService.getMessage(SYSTEM_MESSAGE_ID, eventEntity.getEnventOwnerId());
        if (tmpMessage != null) {
            message.setConversationId(tmpMessage.getConversationId());
        } else {
            message.setConversationId(SYSTEM_MESSAGE_ID + "-" + eventEntity.getEnventOwnerId());
        }
        messageService.sendMessage(message);
    }
}

