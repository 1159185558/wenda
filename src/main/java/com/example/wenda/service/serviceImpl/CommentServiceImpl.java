package com.example.wenda.service.serviceImpl;

import com.example.wenda.dao.CommentDao;
import com.example.wenda.entity.Comment;
import com.example.wenda.service.CommentService;
import com.example.wenda.util.SensetiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/3  12:09
 * @Description:
 */

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private SensetiveWordFilter sensetiveWordFilter;

    @Override
    public List<Comment> getAllCommentsByEntity(int entityType, int entityId) {
        return commentDao.getCommentListByEntity(entityType, entityId);
    }

    @Override
    public Comment getCommentById(int id) {
        return commentDao.getCommentById(id);
    }

    @Override
    public int addComment(Comment comment) {
        String content = comment.getContent();
        comment.setContent(HtmlUtils.htmlEscape(content));
        comment.setContent(sensetiveWordFilter.filterSensetiveWord(content));
        return commentDao.addComment(comment);
    }

    @Override
    public int getUserCommentCountByUserId(int userId) {
        return commentDao.getUserCommentCount(userId);
    }

    @Override
    public int getCommentCount(int entityType, int entityId) {
        return commentDao.getCommentCount(entityType, entityId);
    }

    @Override
    public void deleteComment(int id,int status) {
        commentDao.updateStatus(id, status);
    }
}
