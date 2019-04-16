package com.example.wenda.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.model.HostHolder;
import com.example.wenda.model.Message;
import com.example.wenda.model.MessageResponse;
import com.example.wenda.model.User;
import com.example.wenda.service.LoginService;
import com.example.wenda.service.MessageService;
import com.example.wenda.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        JSONObject jsonObject;
        try {
            if (StringUtils.isBlank(cotent) || StringUtils.isBlank(toName)) {
                return jsonUtil.toJsonObject("04", "参数不合法");
            }
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
            //int fromId = hostHolder.getUser().getId();
            //int toId = user.getId();
            Message tmpMessage = messageService.getMessage(2, 1);
            if (tmpMessage != null) {
                message.setConversationId(tmpMessage.getConversationId());
            }/* else {
                message.setConversationId(fromId + "-" + toId);
            }*/
            messageService.sendMessage(message);
            jsonObject = jsonUtil.toJsonObject("00", "发送成功");
        } catch (Exception e) {
            jsonObject = jsonUtil.toJsonObject("03", "发送失败");
            e.printStackTrace();
            logger.error("发送消息失败 " + e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 获取已登录用户的消息列表，对于每一个会话显示最新的消息。
     * 其中显示的内容包括某个会话的最新消息、未读消息数目、所有消息数目、
     * 前端检测到toId为本用户时，显示未读消息条数
     *
     * @return
     */
    @GetMapping("/getMessageLists")
    public JSONObject getMessageLists() {
        JSONObject jsonObject;
        Map<String, Object> date = new HashMap<>();
        try {
            /*if (hostHolder.getUser() == null) {
                return jsonUtil.toJsonObject("01", "用户未登录", null);
            }
            int userId = hostHolder.getUser().getId();*/
            //此处1需要改掉
            List<Message> messageList = messageService.getMessageLists(1);
            List<Integer> messageCounts = messageService.getMessageListCounts(1);
            List<MessageResponse> messageResponseList = new ArrayList<>();
            MessageResponse messageResponse = null;
            for (int i = 0; i < messageList.size(); ++i) {
                messageResponse = new MessageResponse(messageList.get(i));
                messageResponse.setMessageCounts(messageCounts.get(i));
                String conversationId = messageList.get(i).getConversationId();
                int notReadMessageCount = messageService.getNotReadMessageCounts(1, conversationId);
                messageResponse.setNotReadMessageCounts(notReadMessageCount);
                messageResponseList.add(messageResponse);
            }
            jsonObject = jsonUtil.toJsonObject("00", "获取信息成功", messageResponseList);
        } catch (Exception e) {
            jsonObject = jsonUtil.toJsonObject("02", "获取消息时失败", null);
            logger.error("获取信息时失败： " + e.getMessage());
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 查看一个会话的所有消息内容，查看之后该会话的所有消息均显示为已读。
     *
     * @param conversationId
     * @return
     */
    @GetMapping("/details")
    public JSONObject messageDetails(@RequestParam("conversationId") String conversationId) {
        JSONObject jsonObject;
        try {
            if (StringUtils.isBlank(conversationId)) {
                return jsonUtil.toJsonObject("01", "参数不合法", null);
            }
            //返回消息详情
            List<Message> messageList = messageService.getMessageListsByConversationId(conversationId);
            //将该会话的消息设置为已读，即status设为1
            messageService.markMessageReaded(conversationId);
            jsonObject = jsonUtil.toJsonObject("00", "查询成功", messageList);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = jsonUtil.toJsonObject("02", "查询详细信息时出错", null);
            logger.error("查询详细信息时出错 " + e.getMessage());
        }
        return jsonObject;
    }
    @GetMapping("/delete")
    public JSONObject deleteMessage(@RequestParam("conversationId") String conversationId){
        JSONObject jsonObject;
        try{
            if (StringUtils.isBlank(conversationId)){
                return jsonUtil.toJsonObject("01","参数不合法");
            }
            messageService.deleteMessage(conversationId);
            jsonObject=jsonUtil.toJsonObject("00","删除成功");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("删除时出错 "+e.getMessage());
            jsonObject=jsonUtil.toJsonObject("02","删除时出错");
        }
        return jsonObject;
    }
}
