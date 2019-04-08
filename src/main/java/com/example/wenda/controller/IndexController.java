package com.example.wenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class IndexController {
    @GetMapping(path = {"/","/home"})
    @ResponseBody
    public String index(HttpSession httpSession){

        return httpSession.getAttribute("user")==null?"hello world":"hello "+httpSession.getAttribute("user");
    }
    @GetMapping("/path")
    @ResponseBody
    public String profile(@RequestParam(value = "name",defaultValue = "山枕檀痕涴",required = false)String name,
                          @RequestParam(value = "phone",defaultValue = "123456", required = false)String phone){
        return "hello "+name+" "+phone;
    }
    @GetMapping("/redirect/{code}")
    public String redirect(@PathVariable("code")int code,
                           HttpSession httpSession){
        httpSession.setAttribute("user","admin");
        return "redirect:/";
    }

}
