package com.example.wenda.model;

import org.springframework.stereotype.Component;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/27  15:01
 * @Description:
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public User getUser() {
        return userThreadLocal.get();
    }

    public void setUser(User user) {
        userThreadLocal.set(user);
    }

    public void clear() {
        userThreadLocal.remove();
    }
}
