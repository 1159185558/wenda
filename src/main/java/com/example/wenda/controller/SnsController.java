package com.example.wenda.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.asynchronization.EventEntity;
import com.example.wenda.asynchronization.EventProducer;
import com.example.wenda.asynchronization.EventType;
import com.example.wenda.entity.EntityType;
import com.example.wenda.entity.HostHolder;
import com.example.wenda.entity.Question;
import com.example.wenda.entity.User;
import com.example.wenda.service.*;
import com.example.wenda.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/18  11:26
 * @Description:
 */

@RestController
@RequestMapping("/sns")
public class SnsController {
    private static final Logger logger = LoggerFactory.getLogger(SnsController.class);
    @Autowired
    LikeServer likeServer;
    @Autowired
    FollowService followService;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;

    @GetMapping("/like")
    public JSONObject like(@RequestParam("commentId") int commentId,
                           HttpServletRequest request) {
        JSONObject jsonObject;
        Map<String, Long> data = new HashMap<>();
        try {
            HttpSession httpSession = request.getSession();
            String username = (String) httpSession.getAttribute("user");
            if (username == null) {
                return jsonUtil.toJsonObject("01", "用户未登录", null);
            }
            if (commentService.getCommentById(commentId) == null) {
                return jsonUtil.toJsonObject("03", "参数错误");
            }
            int userId = userService.getUserByName(username).getId();
            EventEntity eventEntity = new EventEntity().setEventType(EventType.LIKE).setEntityId(commentId)
                    .setEntityType(EntityType.ENTITY_QUESTION)
                    .setActorId(userId).setEnventOwnerId(commentService.getCommentById(commentId).getUserId())
                    .setExtraVar(String.valueOf(commentService.getCommentById(commentId).getEntityId()));
            eventProducer.fireEvent(eventEntity);
            long likeCount = likeServer.like(userId, EntityType.ENTITY_QUESTION, commentId);
            long dislikeCount = likeServer.getDislikeCount(EntityType.ENTITY_QUESTION, commentId);
            data.put("likeCount", likeCount);
            data.put("dislikeCount", dislikeCount);
            jsonObject = jsonUtil.toJsonObject("00", "点赞成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = jsonUtil.toJsonObject("02", "点赞时出错", null);
            logger.error("点赞时出错： " + e.getMessage());
        }
        return jsonObject;
    }

    @GetMapping("/dislike")
    public JSONObject dislike(@RequestParam("commentId") int commentId,
                              HttpServletRequest request) {
        JSONObject jsonObject;
        try {
            Map<String, Long> data = new HashMap<>();
            if (request.getSession().getAttribute("user") == null) {
                return jsonUtil.toJsonObject("01", "用户未登录", null);
            }
            if (commentService.getCommentById(commentId) == null) {
                return jsonUtil.toJsonObject("03", "参数错误");
            }
            String username = (String) request.getSession().getAttribute("user");
            int userId = userService.getUserByName(username).getId();
            long dislikeCount = likeServer.dislike(userId, EntityType.ENTITY_QUESTION, commentId);
            long likeCount = likeServer.getLikeCount(EntityType.ENTITY_QUESTION, commentId);
            data.put("likeCount", likeCount);
            data.put("dislikeCount", dislikeCount);
            jsonObject = jsonUtil.toJsonObject("00", "踩成功", data);
        } catch (Exception e) {
            e.getMessage();
            logger.error("踩时出错：" + e.getMessage());
            jsonObject = jsonUtil.toJsonObject("02", "踩时出错", null);
        }
        return jsonObject;
    }

    /**
     * 关注某一类实体，entityType=1，表示关注问题，entityType=3表示关注用户
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @PostMapping("/follow")
    public JSONObject followEntity(@RequestParam("entityType") int entityType,
                                   @RequestParam("entityId") int entityId,
                                   HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            /*if (hostHolder.getUser() == null) {
                return jsonUtil.toJsonObject("01", "用户未登录");
            }*/
            HttpSession session = request.getSession();
            User user = userService.getUserByName((String) session.getAttribute("user"));
            if (user == null) {
                return jsonUtil.toJsonObject("11", "用户未登录");
            }
            if (entityType == 1 && questionService.getQuestionById(entityId) == null
                    || entityType == 3 && userService.getUserById(entityId) == null) {
                return jsonUtil.toJsonObject("03", "该实体不存在，传参错误");
            }
            int userId = user.getId();
            followService.follow(userId, entityType, entityId);
            long followers = followService.getFollowerCount(entityType, entityId);
            EventEntity eventEntity = new EventEntity();
            eventEntity.setActorId(userId).setEntityType(entityType)
                    .setEntityId(entityId).setEventType(EventType.FOLLOW)
                    .setExtraVar(String.valueOf(userId));
            if (entityType==EntityType.ENTITY_USER){
                eventEntity.setEnventOwnerId(entityId);
            }else {
                eventEntity.setEnventOwnerId(questionService.getQuestionById(entityId).getUserId());
            }
            eventProducer.fireEvent(eventEntity);
            jsonObject = jsonUtil.toJsonObject("00", "关注成功", followers);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("关注实体时出错：" + e);
            jsonObject = jsonUtil.toJsonObject("02", "关注实体时出错");
        }
        return jsonObject;
    }

    @PostMapping("/unfollow")
    public JSONObject unfollowEntity(@RequestParam("entityType") int entityType,
                                     @RequestParam("entityId") int entityId,
                                     HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            /*User user = hostHolder.getUser();
            if (user == null) {
                return jsonUtil.toJsonObject("01", "用户未登录");
            }*/
            HttpSession httpSession = request.getSession();
            User user = userService.getUserByName((String) httpSession.getAttribute("user"));
            if (user == null) {
                return jsonUtil.toJsonObject("11", "用户未登录");
            }
            if (entityType == 1 && questionService.getQuestionById(entityId) == null
                    || entityType == 3 && userService.getUserById(entityId) == null) {
                return jsonUtil.toJsonObject("03", "参数错误");
            }
            int userId = user.getId();
            followService.unfollow(userId, entityType, entityId);
            long followers = followService.getFollowerCount(entityType, entityId);

            jsonObject = jsonUtil.toJsonObject("00", "取关成功", followers);
        } catch (Exception e) {
            logger.error("取关实体时出错：" + e);
            jsonObject = jsonUtil.toJsonObject("02", "取关实体时出错：");
        }
        return jsonObject;
    }

    /**
     * 获取关注该实体的用户列表
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @PostMapping("/getFollowers")
    public JSONObject getEntityfollowers(@RequestParam("entityType") int entityType,
                                         @RequestParam("entityId") int entityId,
                                         HttpServletRequest request) {
        JSONObject jsonObject;
        try {
            /*if (hostHolder.getUser() == null) {
                return jsonUtil.toJsonObject("01", "用户未登录");
            }*/

            if (entityType == 1 && questionService.getQuestionById(entityId) == null
                    || entityType == 3 && userService.getUserById(entityId) == null) {
                return jsonUtil.toJsonObject("03", "参数错误");
            }
            List<Integer> followerIds = followService.getFollowwers(entityType, entityId);
            List<User> followers = new ArrayList<>();
            for (Integer integer : followerIds) {
                followers.add(userService.getUserById(integer));
            }
            jsonObject = jsonUtil.toJsonObject("00", "success", followers);
        } catch (Exception e) {
            logger.error("获取实体关注列表失败");
            jsonObject = jsonUtil.toJsonObject("02", "failed：" + e);
        }
        return jsonObject;
    }

    @GetMapping("/getFollowing")
    public JSONObject getFollowing(@RequestParam("entityType") int entityType,
                                   HttpServletRequest request) {
        JSONObject jsonObject;
        try {
            /*User user = hostHolder.getUser();
            if (user == null) {
                return jsonUtil.toJsonObject("01", "用户未登录");
            }*/
            HttpSession httpSession = request.getSession();
            User user = userService.getUserByName((String) httpSession.getAttribute("user"));
            if (user == null) {
                return jsonUtil.toJsonObject("11", "用户未登录");
            }
            /*if (entityType != 1 && entityType != 3) {
                return jsonUtil.toJsonObject("03", "参数错误");
            }*/
            int userId = user.getId();
            List<Integer> followings = followService.getFollowings(userId, entityType);
            List<Map<String, Object>> result = new ArrayList<>();
            if (entityType == EntityType.ENTITY_QUESTION) {
                //返回简要的问题信息
                for (Integer integer : followings) {
                    Question q = questionService.getQuestionById(integer);
                    Map<String, Object> questionResult = new HashMap<>();
                    questionResult.put("title", q.getTitle());
                    questionResult.put("date", q.getCreatedDate());
                    questionResult.put("answerCount", q.getCommentCount());
                    questionResult.put("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, integer));
                    result.add(questionResult);
                }
            } else {
                for (Integer integer : followings) {
                    User user1 = userService.getUserById(integer);
                    Map<String, Object> userResult = new HashMap<>();
                    userResult.put("username", user1.getName());
                    userResult.put("signature", user1.getSignature());
                    userResult.put("answerCount", commentService.getUserCommentCountByUserId(integer));
                    userResult.put("questionCount", questionService.getUserQuestionCount(integer));
                    userResult.put("followCount", followService.getFollowerCount(EntityType.ENTITY_USER, integer));
                    result.add(userResult);
                }
            }
            jsonObject = jsonUtil.toJsonObject("00", "success", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取实体关注列表失败: " + e);
            jsonObject = jsonUtil.toJsonObject("01", "failed");
        }
        return jsonObject;
    }
}
