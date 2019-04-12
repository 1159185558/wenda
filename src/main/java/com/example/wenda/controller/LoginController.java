package com.example.wenda.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.model.User;
import com.example.wenda.service.LoginService;
import com.example.wenda.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    LoginService loginService;
    @Autowired
    JsonUtil jsonUtil;

    @PostMapping("/login")
    public JSONObject login(@RequestParam("name") String name,
                            @RequestParam("password") String password,
                            @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                            @RequestParam(value = "next", required = false) String next,
                            HttpServletResponse httpServletResponse,
                            HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, Object> map = loginService.login(name, password);
            String code = (String) map.get("code");
            switch (code) {
                case "00":
                    jsonObject = jsonUtil.toJsonObject(code, "登陆成功");
                    break;
                case "01":
                    jsonObject = jsonUtil.toJsonObject(code, "用户名不能为空");
                    break;
                case "02":
                    jsonObject = jsonUtil.toJsonObject(code, "用户名或密码不正确");
                    break;
                case "03":
                    jsonObject = jsonUtil.toJsonObject(code, "密码不能为空");
                    break;
                default:
                    jsonUtil.toJsonObject("05", "嘿嘿嘿");
                    break;
            }
            /*HttpSession session = request.getSession();
            session.setAttribute("user", name);*/
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                Cookie cookie1 = new Cookie("user", name);
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                httpServletResponse.addCookie(cookie);
                httpServletResponse.addCookie(cookie1);
                if (StringUtils.isNotBlank(next)) {
                    jsonObject.put("next", next);
                }
            }
        } catch (Exception e) {
            jsonUtil.toJsonObject("04", "登录时出错");
            logger.error("登陆时出错 " + e.getMessage());
        }
        return jsonObject;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user,
                           @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                           @RequestParam(value = "next", required = false) String next,
                           HttpServletResponse httpServletResponse,
                           HttpServletRequest request) {
        String name = user.getName();
        String password = user.getPassword();
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, Object> map = loginService.registerUser(name, password);
            String code = (String) map.get("code");
            switch (code) {
                case "00":
                    jsonObject = jsonUtil.toJsonObject(code, "注册成功");
                    break;
                case "01":
                    jsonObject = jsonUtil.toJsonObject(code, "用户名不能为空");
                    break;
                case "02":
                    jsonObject = jsonUtil.toJsonObject(code, "用户名不合法");
                    break;
                case "03":
                    jsonObject = jsonUtil.toJsonObject(code, "密码不能为空");
                    break;
                case "04":
                    jsonObject = jsonUtil.toJsonObject(code, "用户已经被注册");
                    break;
                default:
                    jsonUtil.toJsonObject("06", "嘿嘿嘿");
                    break;
            }
            /*HttpSession session = request.getSession();
            session.setAttribute("user", name);*/
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                httpServletResponse.addCookie(cookie);
                if (StringUtils.isNotBlank(next)) {
                    jsonObject.put("next", next);
                }
            }
        } catch (Exception e) {
            jsonUtil.toJsonObject("05", "注册时出错");
            logger.error("注册时出错 " + e.getMessage());
        }
        return jsonObject.toJSONString();
    }

    @GetMapping
    public JSONObject logout(@CookieValue("ticket") String ticket) {
        JSONObject jsonObject = new JSONObject();
        try {
            loginService.logout(ticket);
            jsonObject = jsonUtil.toJsonObject("00", "退出成功");
        } catch (Exception e) {
            jsonObject = jsonUtil.toJsonObject("01", "退出时出错");
            logger.error("退出时出错 " + e.getMessage());
        }
        return jsonObject;
    }

}
