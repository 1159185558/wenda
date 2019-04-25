package com.example.wenda.asynchronization.Handler;

import com.example.wenda.asynchronization.EventEntity;
import com.example.wenda.asynchronization.EventHandler;
import com.example.wenda.asynchronization.EventType;
import com.example.wenda.entity.EntityType;
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
 * @CreateTime: 2019/4/18  20:02
 * @Description:
 */
@Component
public class FollowHandler implements EventHandler {
    private static final int SYSTEM_MESSAGE_ID = 1;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public List<EventType> getSupportedEventType() {
        return Arrays.asList(EventType.FOLLOW);
    }

    @Override
    public void handleEvent(EventEntity eventEntity) {
        Message message = new Message();
        message.setFromId(SYSTEM_MESSAGE_ID);
        message.setToId(eventEntity.getEnventOwnerId());
        Message tmpMessage = messageService.getMessage(SYSTEM_MESSAGE_ID, eventEntity.getEnventOwnerId());
        if (tmpMessage == null) {
            message.setConversationId(SYSTEM_MESSAGE_ID + "-" + eventEntity.getEnventOwnerId());
        } else {
            message.setConversationId(tmpMessage.getConversationId());
        }
        message.setCreateDate(new Date());
        if (eventEntity.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户" + userService.getUsernameById(eventEntity.getActorId()) +
                    "关注了你的问题，赶快打开看一下吧！http://127.0.0.1:8080/question/" +
                    eventEntity.getExtraVar());
        } else {
            message.setContent("用户" + userService.getUsernameById(eventEntity.getActorId()) +
                    "关注了你，赶快打开看一下吧！http://127.0.0.1:8080/user/" +
                    eventEntity.getExtraVar());
        }
        messageService.sendMessage(message);
    }
}

