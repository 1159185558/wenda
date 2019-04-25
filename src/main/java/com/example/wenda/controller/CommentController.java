package com.example.wenda.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.wenda.entity.Comment;
import com.example.wenda.entity.EntityType;
import com.example.wenda.service.CommentService;
import com.example.wenda.service.QuestionService;
import com.example.wenda.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/3  14:18
 * @Description:
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private static final Integer VALID_STATUS = 1;
    private static final Integer INVALID_STATUS = 0;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @Autowired
    JsonUtil jsonUtil;

    /**
     * 该方法用来发表对问题的回答或者是对问题回答的评论，其中entityType来区分两者，
     * entityType=1表示前者，entityType=2表示后者，而entityId表示questionId和
     * commentId
     * 或者
     * 另一个方案是构建两个表一个用来存储问题的回答表，另一个表用来存储对问题回答的评论表
     *
     * @param content
     * @param questionId
     * @return
     */
    @PostMapping("/add")
    public JSONObject addComment(@RequestParam("content") String content,
                                 @RequestParam("questionId") int questionId) {
        JSONObject jsonObject;
        try {
            //此处以后添加验证用户是否登录，未登录则跳转到登录页面，仅仅在登录之后才可以进行发表评论
            //从session中读取userId，或者HostHolder中获取user对象
            Comment comment = new Comment();
            comment.setUserId(1111);//登录后根据session值设置
            comment.setContent(content);
            comment.setCreateDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            comment.setStatus(VALID_STATUS);
            //status表示数据库中该操作受影响的行数，正常操作添加评论后status值为1
            int status = commentService.addComment(comment);
            if (status == 0) {
                logger.info("添加评论出现问题，插入操作受影响的行数为1");
                return jsonUtil.toJsonObject("01", "添加评论时出错");
            }
            int count = commentService.getCommentCount(comment.getEntityType(), comment.getEntityId());
            questionService.updateCommentCount(comment.getEntityId(), count);
            jsonObject = jsonUtil.toJsonObject("00", "添加评论成功");

        } catch (Exception e) {
            jsonObject = jsonUtil.toJsonObject("02", "添加评论使出错");
            logger.error("添加评论时出错 " + e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 此方法用来删除用户对问题回答的评论，仅能删除自己发表的评论
     * 此处前端来判断，根据返回的comment，从中获取userId，来匹配已经登录的用户，
     * 若匹配成功的话多显示一个删除按钮。
     *
     * @param commentId
     * @param user
     * @return JSONObject
     */
    @GetMapping("/delete")
    public JSONObject deleteComment(@RequestParam("commentId") int commentId,
                                    @RequestParam("user") String user,
                                    HttpServletRequest request) {
        JSONObject jsonObject;
        try {
            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("user");
        /*if (!username.equals(user)){
            jsonObject.put("code","02");
            jsonObject.put("msg","无权删除");
            return jsonObject;
        }*/

            commentService.deleteComment(commentId, INVALID_STATUS);
            jsonObject = jsonUtil.toJsonObject("00", "删除评论成功");
        } catch (Exception e) {
            jsonObject = jsonUtil.toJsonObject("01", "删除评论时出错");
            logger.error(e.getLocalizedMessage());
        }
        return jsonObject;
    }
}
