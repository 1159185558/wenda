package com.example.wenda.service;

import com.example.wenda.model.User;

import java.util.Map;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/22  11:22
 * @Description:
 */

public interface LoginService {

    String decryptPassword(String password);

    String getPasswordByName(String name);

    String getUsernameById(int id);

    Map<String, Object> registerUser(String name, String password);

    Map<String,Object> login(String name,String password);

    void logout(String ticket);

    User getUserByName(String name);
}
