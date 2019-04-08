package com.example.wenda.model;

import java.util.List;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/8  9:57
 * @Description: 用来返回问题列表，包括问题本身和回答以及针对回答的评论
 */

public class QuestionListResponse {
    public Question question;
    public List<List<Comment>> answers;
}
