package com.example.wenda.dao;

import com.example.wenda.entity.Comment;
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

    //默认查出的结果status=1
    int getCommentCount(@Param("entityType") int entityType, @Param("entityId") int entityId);

    //默认查出的结果status=1,查出该用户对问题的回答数，默认entityType=1
    int getUserCommentCount(int userId);

    Comment getCommentById(int id);

    //Comment getCommentByEntity(@Param("entityType") int entityType,@Param("entityId") String entityId);
    //默认查出的结果status=1
    List<Comment> getCommentListByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId);
}
