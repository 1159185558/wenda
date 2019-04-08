package com.example.wenda.model;


import java.util.Date;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/26  16:02
 * @Description:
 */

public class LoginTicket {
    private Integer id;
    private Integer userId;
    private Date expired;
    private Integer status;
    private String ticket;

    public LoginTicket() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
