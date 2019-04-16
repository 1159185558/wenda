package com.example.wenda.dao;

import com.example.wenda.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    //根据useId来查询有关于该用户的所有对话，其中list中的每个message对象中的content表示最新的消息，message对象按照时间排序
    List<Message> getMessageLists(int userId);

    //获取List<Message> 中按照时间排序每个会话的消息条数
    List<Integer> getMessageCounts(int userId);

    //根据from_id和to_id来查询Message对象,返回id最小的Message
    Message getMinIdMessage(@Param("fromId") int fromId,
                            @Param("toId") int toId);

    //获取所有会话未读消息的条数，按照时间排序
    List<Integer> getNotReadMessageCounts(int toId);

    //根据conversationId更新status值,从1改为0
    int updateStatus(String conversationId);
}
