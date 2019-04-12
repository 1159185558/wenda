package com.example.wenda.service;

import com.example.wenda.model.Message;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/10  8:31
 * @Description:
 */

public interface MessageService {
    //发送信息，将信息保存在数据库中
    int sendMessage(Message message);
}
