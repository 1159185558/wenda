package com.example.wenda.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.model.HostHolder;
import com.example.wenda.model.Message;
import com.example.wenda.model.User;
import com.example.wenda.service.LoginService;
import com.example.wenda.service.MessageService;
import com.example.wenda.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/10  8:41
 * @Description:
 */
@RestController
@RequestMapping("message")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    LoginService loginService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    JsonUtil jsonUtil;

    @PostMapping("/sendMessage")
    public JSONObject sendMessage(@RequestParam("content") String cotent,
                                  @RequestParam("toName") String toName) {
        JSONObject jsonObject = new JSONObject();
        try {
            User user = loginService.getUserByName(toName);
            if (user == null) {
                return jsonUtil.toJsonObject("01", "用户不存在");
            }
            /*if (hostHolder.getUser() == null) {
                return jsonUtil.toJsonObject("02", "用户未登录");
            }*/
            Message message = new Message();
            message.setContent(cotent);
            message.setCreateDate(new Date());
            //message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            //在设置conversation_id时，首先根据fron_id和to_id来查询Message对象
            //若Message==null，则将conversation_id设为from_id-to_id这种形式
            //若Message!=null，则将查询出的Message对象中的conversation_id赋值给新的消息对象

//            message.setConversationId(hostHolder.getUser().getId() + "-" + user.getId());
            messageService.sendMessage(message);
            jsonObject = jsonUtil.toJsonObject("00", "发送成功");
        } catch (Exception e) {
            jsonObject = jsonUtil.toJsonObject("03", "发送失败");
            e.printStackTrace();
            logger.error("发送消息失败 " + e.getMessage());
        }
        return jsonObject;
    }
}
