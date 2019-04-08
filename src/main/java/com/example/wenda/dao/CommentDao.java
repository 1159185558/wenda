package com.example.wenda.dao;

import com.example.wenda.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/3  8:57
 * @Description:
 */
@Mapper
@Repository
public interface CommentDao {

    int updateStatus(@Param("id")int id, @Param("newStatus")int newStatus);

    int addComment(Comment comment);

    int getCommentCount(@Param("entityType") int entityType, @Param("entityId") int entityId);

    int getUserCommentCount(int userId);

    Comment getCommentById(int id);

    //Comment getCommentByEntity(@Param("entityType") int entityType,@Param("entityId") String entityId);

    List<Comment> getCommentListByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId);
}
