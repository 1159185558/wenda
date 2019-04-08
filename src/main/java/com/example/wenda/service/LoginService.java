package com.example.wenda.service;

import java.util.Map;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/22  11:22
 * @Description:
 */

public interface LoginService {

    String decryptPassword(String password);

    String getPasswordByName(String name);

    Map<String, Object> registerUser(String name, String password);

    Map<String,Object> login(String name,String password);

    void logout(String ticket);
}
