package com.example.wenda.asynchronization;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/19  10:40
 * @Description: 事件的类定义，该类对象表明一个具体的事件，事件的内容主要包括
 * 事件类型、触发事件的实体id、事件标记、事件所有者
 */

public class EventEntity {
    private EventType eventType;
    //事件的触发者
    private Integer actorId;
    //事件的所有者
    private Integer enventOwnerId;
    //以下两个用来标记一个事件
    private Integer entityType;
    private Integer entityId;
    private String extraVar;

    public String getExtraVar() {
        return extraVar;
    }

    public EventEntity setExtraVar(String key) {
        this.extraVar=key;
        return this;
    }

    public EventEntity() {
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventEntity setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public Integer getActorId() {
        return actorId;
    }

    public EventEntity setActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public Integer getEnventOwnerId() {
        return enventOwnerId;
    }

    public EventEntity setEnventOwnerId(Integer enventOwnerId) {
        this.enventOwnerId = enventOwnerId;
        return this;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public EventEntity setEntityType(Integer entityType) {
        this.entityType = entityType;
        return this;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public EventEntity setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }
}
