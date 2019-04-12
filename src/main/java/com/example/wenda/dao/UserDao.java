package com.example.wenda.dao;

import com.example.wenda.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/25  9:59
 * @Description:
 */

@Mapper
@Repository
public interface UserDao {
    String getPasswordByUserName(String name);

    User getUserByName(String name);

    int addUserData(User user);

    String getSaltByName(String name);

    Integer getUserIdByName(String name);

    User getUserById(Integer id);

    String getUsernameById(Integer id);
}
