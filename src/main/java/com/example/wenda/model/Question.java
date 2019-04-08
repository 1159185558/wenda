package com.example.wenda.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/28  16:58
 * @Description:
 */

public class Question {
    private Integer id;
    private String title;
    private String content;
    private Integer userId;
    private Date createdDate;
    private Integer commentCount;
    private Integer status;

    public Question() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

}
