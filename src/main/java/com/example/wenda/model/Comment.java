package com.example.wenda.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/2  17:28
 * @Description:
 */

public class Comment {
    private int id;
    //记录用户的id
    private int userId;
    //用来记录问题或者评论id，1表示问题id，2表示评论id
    private int entityId;
    //用来表示该评论是对于问题的回答还是针对问题回答的评论（评论回复）
    //1表示是针对问题的回答，2表示对评论的回复
    private int entityType;
    //评论的内容
    private String content;
    //格式化时间，格式为 yyyy-MM-ddd HH:mm:ss
    @DateTimeFormat(pattern = "yyyy-MM-ddd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    //用来表示该回答或者评论是否被删除，0表示被删除，1表示处于正常状态
    private int status;

    public Comment() {
    }

    public Comment(int id, int userId, int entityId, int entityType,
                   String content, Date createDate, int status) {
        this.id = id;
        this.userId = userId;
        this.entityId = entityId;
        this.entityType = entityType;
        this.content = content;
        this.createDate = createDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
