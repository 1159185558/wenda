package com.example.wenda.model;


import java.util.Date;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/10  8:03
 * @Description:
 */

public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    //两个用户之间会话的conversation_id是唯一的，用来表示某两个用户之间的会话
    private String conversationId;
    private Date createDate;
    //默认为0，表示未读
    private int isRead;

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
