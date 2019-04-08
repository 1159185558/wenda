package com.example.wenda.service.serviceImpl;

import com.example.wenda.dao.QuestionDao;
import com.example.wenda.model.Question;
import com.example.wenda.service.QuestionService;
import com.example.wenda.util.SensetiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/29  16:17
 * @Description:
 */
@Service
public class QuestionServiImpl implements QuestionService {
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private SensetiveWordFilter sensetiveWordFilter;

    @Override
    public Map<String, String> publishQuestion(Question question) {
        Map<String, String> map = new HashMap<>();
        try {
            //对标题和内容进行html标签过滤
            question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
            question.setContent(HtmlUtils.htmlEscape(question.getContent()));
            question.setTitle(sensetiveWordFilter.filterSensetiveWord(question.getTitle()));
            question.setContent(sensetiveWordFilter.filterSensetiveWord(question.getContent()));
            questionDao.addQuestion(question);
            map.put("code", "00");
        } catch (Exception e) {
            map.put("code", "01");
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public int getCommentCountByQuestionId(int questionId) {
        return questionDao.getCommentCount(questionId);
    }

    @Override
    public int updateCommentCount(int questionId, int newCommentCount) {
        return questionDao.updateCommentCount(questionId, newCommentCount);
    }

    @Override
    public int deleteQuestion(Integer id,int status) {
        if (id<0||id>1){
            return 1;
        }
        questionDao.updateStatus(id,status);
        return 0;
    }
}
