package com.example.wenda.service;


import com.example.wenda.model.Comment;

import java.util.List;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/3  10:09
 * @Description:
 */

public interface CommentService {

    List<Comment> getAllCommentsByEntity(int entityType, int entityId);

    Comment getCommentById(int id);

    int addComment(Comment comment);

    int getUserCommentCountByUserId(int userId);

    int getCommentCount(int entityType, int entityId);

    void deleteComment(int id,int status);

}
