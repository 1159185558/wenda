package com.example.wenda.entity;

import java.util.Date;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/25  20:04
 * @Description:
 */

public class Feed {
    private int id;
    private int feedType;
    private Date createDate;
    private String content;
    private int userId;

    public Feed() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
