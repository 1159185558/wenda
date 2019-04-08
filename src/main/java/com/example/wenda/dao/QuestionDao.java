package com.example.wenda.dao;

import com.example.wenda.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/28  17:05
 * @Description:
 */

@Mapper
@Repository
public interface QuestionDao {

    void addQuestion(Question question);

    int updateCommentCount(@Param("id") int id,
                           @Param("commentCount") int commentCount);

    int getCommentCount(int id);

    int updateStatus(@Param("id") int id,
                     @Param("status") int status);
}
