package com.example.wenda.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.model.User;
import com.example.wenda.service.LoginService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public JSONObject login(@RequestParam("name") String name,
                            @RequestParam("password") String password,
                            @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                            @RequestParam(value = "next", required = false) String next,
                            HttpServletResponse httpServletResponse,
                            HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> map = loginService.login(name, password);
        if (map.containsKey("msg")) {
            jsonObject.put("msg", map.get("msg"));
            return jsonObject;
        }
        HttpSession session = request.getSession();
        session.setAttribute("user",name);
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
        jsonObject.put("msg", "登陆成功");
        return jsonObject;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user,
                           @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                           HttpServletResponse httpServletResponse,
                           HttpServletRequest request) {
        String name = user.getName();
        String password = user.getPassword();
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> map = loginService.registerUser(name, password);
        if (map.get("msg") != null) {
            jsonObject.put("msg", map.get("msg"));
            return jsonObject.toJSONString();
        }
        HttpSession session = request.getSession();
        session.setAttribute("user",name);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath("/");
            if (rememberme) {
                cookie.setMaxAge(3600 * 24 * 5);
            }
            httpServletResponse.addCookie(cookie);
        }
        jsonObject.put("msg", "注册成功");
        return jsonObject.toJSONString();
    }

    @GetMapping
    public String logout(@CookieValue("ticket") String ticket) {
        loginService.logout(ticket);
        return "redirect:/";
    }

}
