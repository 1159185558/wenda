package com.example.wenda.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.model.*;
import com.example.wenda.service.CommentService;
import com.example.wenda.service.LoginService;
import com.example.wenda.service.QuestionService;
import com.example.wenda.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/29  16:27
 * @Description:
 */

@RestController
@RequestMapping("/question")
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    private static final Integer VALID_STATUS = 1;
    private static final Integer INVALID_STATUS = 0;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LoginService loginService;

    /**
     * 需要登录验证
     * 发布新问题，只对已经登陆的用户起作用
     *
     * @param title
     * @param content
     * @return
     */
    @PostMapping("/publish")
    public JSONObject publishQuestion(@RequestParam("title") String title,
                                      @RequestParam("content") String content) {
        JSONObject jsonObject;
        try {
            Question question = new Question();
            question.setCreatedDate(new Date());
            question.setContent(content);
            question.setTitle(title);
            question.setStatus(VALID_STATUS);
            if (hostHolder.getUser() == null) {
                question.setUserId(0);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            Map<String, String> map = questionService.publishQuestion(question);
            jsonObject = jsonUtil.toJsonObject("00", "发布问题成功");
        } catch (Exception e) {
            jsonObject = jsonUtil.toJsonObject("01", "发送失败");
            logger.error("发送消息失败 " + e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 需要登录验证
     * 对于已经登录的用户，可以删除他们曾经发表的额问题
     * 对于已经删除的问题，相应的评论也应该删除
     *
     * @param questionId
     * @return
     */
    @GetMapping("/delete")
    public JSONObject deleteQuestion(@RequestParam("questionId") int questionId,
                                     @RequestParam("user") String user,
                                     HttpServletRequest request) {
        JSONObject jsonObject;
        try {
            if (questionId <= 0) {
                return jsonUtil.toJsonObject("03", "参数不合法");
            }
        /*HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");
        if (!username.equals(user)) {
            jsonObject.put("code", "02");
            jsonObject.put("msg", "无权删除");
            return jsonObject;
        }*/

            questionService.deleteQuestion(questionId, INVALID_STATUS);
            //接下来对已经删除问题的评论进行删除
            List<Comment> answers = commentService.getAllCommentsByEntity(EntityType.ENTITY_QUESTION, questionId);
            for (Comment comment : answers) {
                //首先删除对问题的回答
                commentService.deleteComment(comment.getId(), INVALID_STATUS);
                List<Comment> comments = commentService.getAllCommentsByEntity(EntityType.ENTITY_COMMENT, comment.getId());
                for (Comment comment1 : comments) {
                    //其次删除对问题回答的评论
                    commentService.deleteComment(comment1.getId(), INVALID_STATUS);
                }
            }
            jsonObject = jsonUtil.toJsonObject("00", "删除成功");
        } catch (Exception e) {
            jsonObject = jsonUtil.toJsonObject("02", "删除时出错");
            logger.error("删除时出错" + e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 查看某一问题的详细内容，包括对该问题的回答以及对回答的评论
     *
     * @param questionId
     * @return
     */
    @GetMapping("/questionId")
    public JSONObject getQuestion(@RequestParam("questionId") int questionId) {
        JSONObject jsonObject;
        try {//首先判断参数是否合法
            if (questionId <= 0) {
                return jsonUtil.toJsonObject("01", "参数不合法", null);
            }
            Map<String, Object> data = new HashMap<>();
            Question question = questionService.getQuestionById(questionId);
            //首先确定是否存在此问题，存在在执行以下步骤，避免了问题为空，而评论仍然存在的问题
            if (question != null) {
                data.put("question", question);
                List<Comment> list = commentService.getAllCommentsByEntity(EntityType.ENTITY_QUESTION, questionId);
                //用来存储对于该问题的所有回答
                List<QuestionResponse> answers = new LinkedList<>();
                //用来存储对问题回答的评论，存储的是所有的问题评论，其中每个list与questionList中的每个问题相匹配，
                //即为每个问题的评论
                List<List<QuestionResponse>> comments = new LinkedList<>();
                QuestionResponse questionResponse;
                for (Comment comment : list) {
                    int userId = comment.getUserId();
                    String uesrname1 = loginService.getUsernameById(userId);
                    questionResponse = new QuestionResponse(uesrname1, comment);
                    List<Comment> list1 = commentService.getAllCommentsByEntity(EntityType.ENTITY_COMMENT, comment.getId());
                    //用来存储每个对问题回答的评论，评论有多条，因此返回一个list
                    List<QuestionResponse> list2 = new LinkedList<>();
                    for (Comment comment1 : list1) {
                        String username2 = loginService.getUsernameById(comment1.getUserId());
                        QuestionResponse questionResponse1 = new QuestionResponse(username2, comment1);
                        list2.add(questionResponse1);

                    }
                    answers.add(questionResponse);
                    comments.add(list2);
                }
                data.put("answer", answers);
                data.put("comments", comments);
                return jsonUtil.toJsonObject("00", "查找成功", data);
            }
            jsonObject = jsonUtil.toJsonObject("02", "查找无结果", null);
        } catch (Exception e) {
            jsonObject = jsonUtil.toJsonObject("03", "查找时出错", null);
            logger.error("查找时出错" + e.getMessage());
        }
        return jsonObject;
    }
}
