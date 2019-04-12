package com.example.wenda.model;

import java.util.List;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/9  9:57
 * @Description:
 */

public class QuestionResponse {
    private String username;
    private Comment comment;

    public QuestionResponse(String username, Comment comment) {
        this.username = username;
        this.comment = comment;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
