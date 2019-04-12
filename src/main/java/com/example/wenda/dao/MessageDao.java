package com.example.wenda.dao;

import com.example.wenda.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/10  8:18
 * @Description:
 */

@Mapper
@Repository
public interface MessageDao {
    //添加消息
    int addMessage(Message message);
    //
    Message getMessageLists();
}
