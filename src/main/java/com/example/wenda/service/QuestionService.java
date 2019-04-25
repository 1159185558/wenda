package com.example.wenda.service;

import com.example.wenda.entity.Question;

import java.util.Map;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/28  17:18
 * @Description:
 */

public interface QuestionService {

    Map<String,String> publishQuestion(Question question);

    int getCommentCountByQuestionId(int questionId);

    int updateCommentCount(int questionId,int newCommentCount);

    int deleteQuestion(Integer id,int status);

    Question getQuestionById(int id);

    int getUserQuestionCount(int userId);
}
