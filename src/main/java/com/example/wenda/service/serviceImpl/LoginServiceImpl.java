package com.example.wenda.service.serviceImpl;

import com.example.wenda.dao.LoginTicketDao;
import com.example.wenda.dao.UserDao;
import com.example.wenda.model.LoginTicket;
import com.example.wenda.model.User;
import com.example.wenda.service.LoginService;
import com.example.wenda.util.MD5;
import com.example.wenda.util.RSAUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/22  11:23
 * @Description:
 */

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private RSAUtils rsaUtils;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginTicketDao loginTicketDao;

    @Override
    public String decryptPassword(String password) {
        try {
            if (rsaUtils.getKeyMap() == null) {
                rsaUtils.getKeyPair();
            }
            String password0 = rsaUtils.decryptData(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    @Override
    public String getPasswordByName(String name) {
        return userDao.getPasswordByUserName(name);
    }

    @Override
    public String getUsernameById(int id) {
        return userDao.getUsernameById(id);
    }

    @Override
    public Map<String, Object> registerUser(String name, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(name)) {
            map.put("code", "01");//用户名不能为空
            return map;
        }
        //用来匹配用户名，用户名中只能是数字、字母，汉字，_，-
        String regex = "^[a-zA-Z0-9\\u4E00-\\u9FA5_-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            map.put("code", "02");//用户名不合法
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("code", "03");//密码不能为空
            return map;
        }
        User user = userDao.getUserByName(name);
        if (user != null) {
            map.put("code", "04");//用户已经被注册
            return map;
        }
        user = new User();
        user.setName(name);
        //生成十位随机数作为密码盐
        String salt = UUID.randomUUID().toString().substring(0, 10);
        user.setSalt(salt);
        user.setPassword(MD5.MD5(password + salt));
        String headUrl = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(headUrl);
        userDao.addUserData(user);
        //初测成功后生成ticket，直接登录
        String ticket = addLoginTicket(userDao.getUserIdByName(name));
        map.put("ticket", ticket);
        map.put("code","00");
        return map;
    }

    @Override
    public Map<String, Object> login(String name, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(name)) {
            map.put("code", "01");//用户名不能为空
            return map;
        }
        if (userDao.getUserByName(name) == null) {
            map.put("code", "02");//用户名或密码不正确
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("code", "03");//密码不能为空
            return map;
        }
        String tmpPassword = userDao.getPasswordByUserName(name);
        String salt = userDao.getSaltByName(name);
        if (!MD5.MD5(password + salt).equals(tmpPassword)) {
            map.put("code", "02");//用户名获密码不正确
            return map;
        }
        int userId = userDao.getUserIdByName(name);
        String ticket = addLoginTicket(userDao.getUserIdByName(name));
        map.put("ticket", ticket);
        map.put("code","00");//登陆成功
        return map;
    }

    @Override
    public void logout(String ticket) {
        loginTicketDao.updateStatus(0, ticket);
    }

    @Override
    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    public String addLoginTicket(Integer userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        loginTicket.setExpired(date);
        loginTicket.setStatus(1);
        String ticket = UUID.randomUUID().toString().replaceAll("-", "");
        loginTicket.setTicket(ticket);
        loginTicketDao.addLoginTicket(loginTicket);
        return ticket;
    }
}
