package com.example.wenda.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.model.HostHolder;
import com.example.wenda.model.Question;
import com.example.wenda.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

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
    private QuestionService questionService;
    @Autowired
    private HostHolder hostHolder;
    
    @PostMapping("publish")
    public JSONObject publishQuestion(@RequestParam("title") String title,
                                      @RequestParam("content") String content) {
        JSONObject jsonObject = new JSONObject();
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
        jsonObject.put("code", map.get("code"));
        jsonObject.put("msg", question);
        return jsonObject;
    }

    /**
     * 对于已经登录的用户，可以删除他们曾经发表的额问题
     * @param questionId
     * @return
     */
    @GetMapping("delete")
    public JSONObject deleteQuestion(@RequestParam("questionId") int questionId,
                                     @RequestParam("user") String user,
                                     HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("user");
        if (!username.equals(user)){
            jsonObject.put("code","02");
            jsonObject.put("msg","无权删除");
            return jsonObject;
        }
        try{
            questionService.deleteQuestion(questionId,INVALID_STATUS);
            jsonObject.put("code","00");
            jsonObject.put("msg","删除成功");
        }catch (Exception e){
            jsonObject.put("code","01");
            jsonObject.put("msg","删除时出错");
            logger.error(e.getLocalizedMessage());
        }
        return jsonObject;
    }
}
