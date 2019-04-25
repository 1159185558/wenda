package com.example.wenda.asynchronization;

import java.util.List;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/18  19:37
 * @Description: 对于事件的处理类，该类仅仅是一个接口，对于不同的事件的处理过程由实现类来具体实现
 */

public interface EventHandler {
    //查看该处理过程支持的事件类型
    List<EventType> getSupportedEventType();

    //事件的具体处理过程，不同的实现类处理过程不同
    void handleEvent(EventEntity eventEntity);
}
