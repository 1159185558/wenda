package com.example.wenda.dao;

import com.example.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/26  16:26
 * @Description:
 */

@Mapper
@Repository
public interface LoginTicketDao {
    int addLoginTicket(LoginTicket loginTicket);

    LoginTicket getLoginTicketByTicket(String ticket);

    void updateStatus(@Param("newStatus") Integer newStatus,
                      @Param("ticket") String ticket);
}
