package com.example.wenda.interceptor;

import com.example.wenda.dao.LoginTicketDao;
import com.example.wenda.dao.UserDao;
import com.example.wenda.entity.HostHolder;
import com.example.wenda.entity.LoginTicket;
import com.example.wenda.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/27  9:56
 * @Description:
 */

@Component
public class PassportedInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginTicketDao loginTicketDao;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String ticket = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDao.getLoginTicketByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 1) {
                if (loginTicket != null && loginTicket.getExpired().before(new Date())) {
                    //ticket时间到期后需要修改相应的状态
                    loginTicketDao.updateStatus(0, ticket);
                }
                return true;
            }
            User user = userDao.getUserById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
