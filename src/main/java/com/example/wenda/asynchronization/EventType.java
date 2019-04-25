package com.example.wenda.asynchronization;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/19  10:35
 * @Description: 事件的类型，包括评论事件、点赞事件、登陆事件、邮件事件等等
 */

public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    FOLLOW(3),
    MAIL(4);
    private int value;

    EventType() {

    }

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }}
